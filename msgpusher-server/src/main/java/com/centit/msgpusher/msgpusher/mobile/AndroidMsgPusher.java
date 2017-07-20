package com.centit.msgpusher.msgpusher.mobile;

import com.alibaba.fastjson.JSON;
import com.centit.msgpusher.common.MsgPushConfig;
import com.centit.msgpusher.common.OSMsgPushInfo;
import com.centit.msgpusher.common.OptMsgPushInfo;
import com.centit.msgpusher.msgpusher.PushResult;
import com.centit.msgpusher.po.MessageDelivery;
import com.centit.support.network.UrlOptUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToAllRequest;
import com.baidu.yun.push.model.PushMsgToAllResponse;
import com.centit.msgpusher.msgpusher.MsgPusher;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by codefan on 17-4-6.
 */
@Service("androidMsgPusher")
public class AndroidMsgPusher extends BaiduMsgPusher implements MsgPusher
{
	@Value("${ANDROID_API_KEY}")
	public String  ANDROID_API_KEY;

	@Value("${ANDROID_SECRET_KEY}")
	public String  ANDROID_SECRET_KEY;

	@Resource
	private MsgPushConfig msgPushConfig;


	private String makePkgContent(MessageDelivery msg){
		OSMsgPushInfo osConfig = msgPushConfig.getOSConfig(msg.getOsId());
		Map<String, String> optKeyValue = new HashMap<>();
		StringBuilder pkgContent = new StringBuilder("#Intent;component=");
		OptMsgPushInfo optConfig = null;
		if (osConfig != null){
			if (msg.getOptTag() != null && !"".equals(msg.getOptTag())){
				optKeyValue = (Map) JSON.parse(msg.getOptTag());
			}
			if (msg.getOptId() != null && !"".equals(msg.getOptId())) {
				optConfig = osConfig.getOptMsgPushConfig(msg.getOptId());
				if (optConfig != null && "".equals(optConfig)) {
					pkgContent.append(osConfig.getAndroidPkg())
							.append("/")
							.append(optConfig.getAndroidView())
							.append(";");
					for (Map.Entry<String, String> ent : optKeyValue.entrySet()) {
						try {
							pkgContent.append("S.")
									.append(ent.getKey())
									.append("=")
									.append(URLEncoder.encode(ent.getValue(), "utf-8"))
									.append(";");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		pkgContent.append("end");
		return pkgContent.toString();
	}

	/** 广播发送信息
	 * @param msg     发送的消息
	 * @return MSGID 表示成功， null 和空 其他的为错误信息
	 */
	@Override
	public PushResult pushMsgToAll(MessageDelivery msg) throws PushClientException, PushServerException{
			PushResult pushResult = new PushResult();
			Map<String,String> androidMap = new HashMap<>();
			String msgId = null;
			String errorReason = null;
			String state = null;
			// 1. get apiKey and secretKey from developer console
			String apiKey = ANDROID_API_KEY;
			String secretKey = ANDROID_SECRET_KEY;
			PushKeyPair pair = new PushKeyPair(apiKey, secretKey);

			// 2. build a BaidupushClient object to access released interfaces
			BaiduPushClient pushClient = new BaiduPushClient(pair,BaiduPushConstants.CHANNEL_REST_URL);

			// 3. register a YunLogHandler to get detail interacting information
			// in this request.
			pushClient.setChannelLogHandler(new YunLogHandler() {
				@Override
				public void onHandle(YunLogEvent event) {
					System.out.println(event.getMessage());
				}
			});
			try {
				//创建pkg_content
				String pkg = new StringBuffer()
						.append("#Intent;component=com.centit.reporter/")
						.toString();
				JSONObject notification = new JSONObject();
				notification.put("title", msg.getMsgSubject());
				notification.put("description", msg.getMsgContent());
				notification.put("open_type", 2);
				notification.put("pkg_content", makePkgContent(msg));
				notification.put("url", msg.getRelUrl());
				PushMsgToAllRequest request = new PushMsgToAllRequest()
						.addMsgExpires(msg.getMsgExpireSeconds())
						.addMessageType(1)// 设置消息类型,0表示透传消息,1表示通知,默认为0.
						.addMessage(notification.toString())
	//							.addSendTime(System.currentTimeMillis() / 1000 + 70)// 设置定时推送时间，必需超过当前时间一分钟，单位秒.实例70秒后推送
						.addDeviceType(3);// 设置设备类型，deviceType => 1 for web, 2 for
												// pc,
												// 3 for android, 4 for ios, 5 for wp.
				// 5. http request
				PushMsgToAllResponse response = pushClient.pushMsgToAll(request);
				// Http请求返回值解析
				msgId = response.getMsgId();
				state = "1";
			} catch (PushClientException e) {
				if (BaiduPushConstants.ERROROPTTYPE) {
					throw e;
				} else {
					e.printStackTrace();
				}
			} catch (PushServerException e) {
				if (BaiduPushConstants.ERROROPTTYPE) {
					throw e;
				} else {
					errorReason = e.getErrorMsg();
					state = "2";
				}
			}
			pushResult.setPushState(state);
			pushResult.setMsgId(msgId);
			androidMap.put("android_error",errorReason);
			pushResult.setMap(androidMap);
			return pushResult;
    }
}

package com.centit.msgpusher.msgpusher.mobile;

import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;
import com.centit.msgpusher.msgpusher.PushResult;
import com.centit.msgpusher.po.MessageDelivery;
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

import java.util.HashMap;
import java.util.Map;


/**
 * Created by codefan on 17-4-6.
 * 对百度推送进行封装
 */
@Service("iosMsgPusher")
public class IOSMsgPusher extends BaiduMsgPusher implements MsgPusher
{


	@Value("${IOS_API_KEY}")
	public String  IOS_API_KEY;

	@Value("${IOS_SECRET_KEY}")
	public String  IOS_SECRET_KEY;

	/** 广播发送信息
	 * @param msg     发送的消息
	 * @return MSGID 表示成功， null 和空 其他的为错误信息
	 */
	@Override
	public PushResult pushMsgToAll(MessageDelivery msg) throws PushClientException, PushServerException{
			PushResult pushResult = new PushResult();
			Map<String,String> iosMap = new HashMap<>();
			String msgId = null;
			String errorReason = null;
			String state = null;
			// 1. get apiKey and secretKey from developer console
			String apiKey = IOS_API_KEY;
			String secretKey = IOS_SECRET_KEY;
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
				// 4. specify request arguments
				JSONObject notification = new JSONObject();
				JSONObject jsonAPS = new JSONObject();
				jsonAPS.put("alert", msg.getMsgSubject());
				jsonAPS.put("sound", "ttt"); // 设置通知铃声样式，例如"ttt"，用户自定义。
				notification.put("aps", jsonAPS);
				notification.put("description",msg.getMsgContent());
				notification.put("open_type", 1);
				notification.put("url", msg.getRelUrl());
				PushMsgToAllRequest request = new PushMsgToAllRequest()
						.addMsgExpires(msg.getMsgExpireSeconds())
						.addMessageType(1)// 设置消息类型,0表示透传消息,1表示通知,默认为0.
						.addMessage(notification.toString())
						.addSendTime(System.currentTimeMillis() / 1000 + 70 )// 设置定时推送时间，必需超过当前时间一分钟，单位秒.实例70秒后推送
						.addDepolyStatus(1)// 1开发者2生产状态
						.addDeviceType(4);// 设置设备类型，deviceType => 1 for web, 2
												// for
												// pc,
												// 3 for android, 4 for ios, 5 for
												// wp.
				// 5. http request
				PushMsgToAllResponse response = pushClient
						.pushMsgToAll(request);
				// Http请求返回值解析
				msgId = response.getMsgId();
				state = "1";
	//				Thread.sleep(1 * 5 * 1000);

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
			pushResult.setMsgId2(msgId);
			iosMap.put("ios_error",errorReason);
			pushResult.setMap(iosMap);
			return pushResult;
    }
}

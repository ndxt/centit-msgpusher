package com.centit.msgpusher.plugins;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.msgpusher.adapter.MsgPusher;
import com.centit.msgpusher.po.IPushMessage;
import com.centit.msgpusher.po.IPushMsgPoint;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by codefan on 17-4-6.
 * 对百度推送进行封装
 */
public class BaiduMsgPusher implements MsgPusher {
    @Setter
    private String androidApiKey;
    @Setter
    private String androidSecretKey;
    @Setter
    private String iosApiKey;
    @Setter
    private String iosSecretKey;
    @Setter
    private IBaiduPushSupport baiduPushSupport;

    private String makePkgContent(NoticeMessage msg){
        Map<String, String> optKeyValue = new HashMap<>();
        StringBuilder pkgContent = new StringBuilder("#Intent;component=");
        if (msg.getOptTag() != null && !"".equals(msg.getOptTag())){
            optKeyValue = (Map) JSON.parse(msg.getOptTag());
        }
        if (msg.getOptId() != null && !"".equals(msg.getOptId())) {
            pkgContent.append(baiduPushSupport.getAndroidPkg())
                    .append("/")
                    .append(baiduPushSupport.getAndroidView())
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
        pkgContent.append("end");
        return pkgContent.toString();
    }

    /** 点对点发送信息
     * @param msg     发送的消息
     * @param receiver   接收人内部用户编码
     * @return MSGID 表示成功， null 和空 其他的为错误信息
     * @throws PushServerException PushServerException
     * @throws PushClientException PushClientException
     */
    @Override
    public PushResult pushMessage(IPushMessage msg, IPushMsgPoint receiver)throws PushClientException, PushServerException{
        PushResult pushResult = new PushResult();
        Map<String,String> map = new HashMap<>();
        String msgId = null;
        String errorDesc = null;
        String state = null;
        String deviceType = receiver.getDeviceType();
        String channelId = receiver.getChannelId();
        PushKeyPair pair = null;
        // 1. get apiKey and secretKey from developer console
        JSONObject notification = new JSONObject();
        if(deviceType.equals("3")){                                //Android
            String apiKey = androidApiKey;
            String secretKey = androidSecretKey;
            pair = new PushKeyPair(apiKey, secretKey);
            //创建 Android的通知
            notification.put("title", msg.getMsgSubject());
            notification.put("description", msg.getMsgContent());
            notification.put("open_type", 2);
            notification.put("pkg_content", makePkgContent(msg));
            notification.put("url", msg.getRelUrl());
        } else if(deviceType.equals("4")){                        //IOS
            String apiKey = iosApiKey;
            String secretKey = iosSecretKey;
            pair = new PushKeyPair(apiKey, secretKey);
            //创建IOS的通知
            JSONObject jsonAPS = new JSONObject();
            jsonAPS.put("alert", msg.getMsgSubject());
            jsonAPS.put("sound", "ttt"); // 设置通知铃声样式，例如"ttt"，用户自定义。
            notification.put("aps", jsonAPS);
            notification.put("description",msg.getMsgContent());
            notification.put("open_type", 1);
//            notification.put("pkg_content", "");
            notification.put("url", msg.getRelUrl());
        }
        // 2. build a BaidupushClient object to access released interfaces
        BaiduPushClient pushClient = new BaiduPushClient(pair,
                BaiduPushConstants.CHANNEL_REST_URL);

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
            PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest()
                    .addChannelId(channelId)
                    .addMsgExpires(msg.getMsgExpireSeconds()) // message有效时间
                    .addMessageType(1)// 1：通知,0:透传消息. 默认为0 注：IOS只有通知.
                    .addMessage(notification.toString())
                    .addDeployStatus(1)// IOS,DeployStatus,1: Developer 2: Production.
                    .addDeviceType(new Integer(deviceType));// deviceType => 3:android, 4:ios
            // 5. http request
            PushMsgToSingleDeviceResponse response = pushClient
                    .pushMsgToSingleDevice(request);
            // Http请求结果解析打印
            msgId = response.getMsgId();
            state = "1";
//            pushResult.setMsgId(msgId);
        } catch (PushClientException e) {
            /*
             * ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,'true' 表示抛出, 'false' 表示捕获。
             */
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                e.printStackTrace();
            }
        } catch (PushServerException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                errorDesc = e.getErrorMsg();
                state = "2";
            }
        }
        pushResult.setPushState(state);
        pushResult.setMsgId(msgId);
        map.put("app",errorDesc);
        pushResult.setMap(map);
        return pushResult;
    }

}

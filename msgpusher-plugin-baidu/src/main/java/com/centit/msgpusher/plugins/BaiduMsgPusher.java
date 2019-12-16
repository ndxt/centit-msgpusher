package com.centit.msgpusher.plugins;

import com.alibaba.fastjson.JSONObject;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToAllRequest;
import com.baidu.yun.push.model.PushMsgToAllResponse;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;
import com.centit.framework.common.ResponseData;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.common.DoubleAspect;
import com.centit.support.network.UrlOptUtils;
import javafx.util.Pair;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by codefan on 17-4-6.
 * 对百度推送进行封装
 */
public class BaiduMsgPusher implements MessageSender {
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

    private static final Logger logger = LoggerFactory.getLogger(BaiduMsgPusher.class);

    private String makePkgContent(NoticeMessage msg){
        Map<String, String> optKeyValue = new HashMap<>();
        StringBuilder pkgContent = new StringBuilder("#Intent;component=");
        if (msg.getOptTag() != null && !"".equals(msg.getOptTag())){
            optKeyValue = UrlOptUtils.splitUrlParamter(msg.getOptTag());
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

    private JSONObject makeMessagePackage(String deviceType, NoticeMessage message){
        JSONObject notification = new JSONObject();
        if(deviceType.equals("3")){                                //Android
             //创建 Android的通知
            notification.put("title", message.getMsgSubject());
            notification.put("description", message.getMsgContent());
            notification.put("open_type", 2);
            notification.put("pkg_content", makePkgContent(message));
            //notification.put("url", message.getRelUrl());
        } else if(deviceType.equals("4")){                        //IOS

            //创建IOS的通知
            JSONObject jsonAPS = new JSONObject();
            jsonAPS.put("alert", message.getMsgSubject());
            jsonAPS.put("sound", "ttt"); // 设置通知铃声样式，例如"ttt"，用户自定义。
            notification.put("aps", jsonAPS);
            notification.put("description",message.getMsgContent());
            notification.put("open_type", 1);
//            notification.put("pkg_content", "");
            //notification.put("url", message.getRelUrl());
        }
        return notification;
    }
    /**
     * 发送内部系统消息
     *
     * @param sender   发送人内部用户编码
     * @param receiver 接收人内部用户编码
     * @param message  消息主体
     * @return "OK" 表示成功，其他的为错误信息
     */
    @Override
    public ResponseData sendMessage(String sender, String receiver, NoticeMessage message) {
        Pair<String, String> mobile = baiduPushSupport.getReceiverDeviceAndChannel(receiver);
        String deviceType = mobile.getKey();
        String channelId = mobile.getValue();
        PushKeyPair pair = deviceType.equals("3")? new PushKeyPair(androidApiKey, androidSecretKey)
            : new PushKeyPair(iosApiKey, iosSecretKey);
        // 1. get apiKey and secretKey from developer console
        JSONObject notification = makeMessagePackage(deviceType, message);
        // 2. build a BaidupushClient object to access released interfaces
        BaiduPushClient pushClient = new BaiduPushClient(pair,
                BaiduPushConstants.CHANNEL_REST_URL);

        // 3. register a YunLogHandler to get detail interacting information
        // in this request.
        pushClient.setChannelLogHandler((event) -> System.out.println(event.getMessage()));
        try {
            // 4. specify request arguments
            PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest()
                    .addChannelId(channelId)
                    //.addMsgExpires(message.getMsgExpireSeconds()) // message有效时间
                    .addMessageType(1)// 1：通知,0:透传消息. 默认为0 注：IOS只有通知.
                    .addMessage(notification.toString())
                    .addDeployStatus(1)// IOS,DeployStatus,1: Developer 2: Production.
                    .addDeviceType(new Integer(deviceType));// deviceType => 3:android, 4:ios
            // 5. http request
            PushMsgToSingleDeviceResponse response = pushClient
                    .pushMsgToSingleDevice(request);
            // Http请求结果解析打印
            return ResponseData.makeResponseData(response);
        } catch (PushClientException e) {
            /*
             * ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,'true' 表示抛出, 'false' 表示捕获。
             if (BaiduPushConstants.ERROROPTTYPE) {
            }*/
            logger.error(e.getMessage(), e);
            return ResponseData.makeErrorMessage(2, e.getMessage());
        } catch (PushServerException e) {
            logger.error(e.getMessage(), e);
            return ResponseData.makeErrorMessage(2, e.getErrorMsg());
        }
    }

    private ResponseData broadcastAndroid(NoticeMessage message) {
        // 1. get apiKey and secretKey from developer console
        PushKeyPair pair = new PushKeyPair(androidApiKey, androidSecretKey);

        // 2. build a BaidupushClient object to access released interfaces
        BaiduPushClient pushClient = new BaiduPushClient(pair,BaiduPushConstants.CHANNEL_REST_URL);

        // 3. register a YunLogHandler to get detail interacting information
        // in this request.
        pushClient.setChannelLogHandler((event) -> System.out.println(event.getMessage()));
        try {
            //创建pkg_content
            String pkg = new StringBuffer()
                .append("#Intent;component=com.centit.reporter/")
                .toString();
            JSONObject notification = makeMessagePackage("3", message);
            PushMsgToAllRequest request = new PushMsgToAllRequest()
                //.addMsgExpires(msg.getMsgExpireSeconds())
                .addMessageType(1)// 设置消息类型,0表示透传消息,1表示通知,默认为0.
                .addMessage(notification.toString())
                //                            .addSendTime(System.currentTimeMillis() / 1000 + 70)// 设置定时推送时间，必需超过当前时间一分钟，单位秒.实例70秒后推送
                .addDeviceType(3);// 设置设备类型，deviceType => 1 for web, 2 for
            // pc,
            // 3 for android, 4 for ios, 5 for wp.
            // 5. http request
            PushMsgToAllResponse response = pushClient.pushMsgToAll(request);
            // Http请求返回值解析
            return ResponseData.makeResponseData(response);
        } catch (PushClientException e) {
            /*
             * ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,'true' 表示抛出, 'false' 表示捕获。
             if (BaiduPushConstants.ERROROPTTYPE) {
            }*/
            logger.error(e.getMessage(), e);
            return ResponseData.makeErrorMessage(2, e.getMessage());
        } catch (PushServerException e) {
            logger.error(e.getMessage(), e);
            return ResponseData.makeErrorMessage(2, e.getErrorMsg());
        }
    }

    private ResponseData broadcastIOS(NoticeMessage message) {
        // 1. get apiKey and secretKey from developer console
        PushKeyPair pair = new PushKeyPair(iosApiKey, iosSecretKey);

        // 2. build a BaidupushClient object to access released interfaces
        BaiduPushClient pushClient = new BaiduPushClient(pair,BaiduPushConstants.CHANNEL_REST_URL);

        // 3. register a YunLogHandler to get detail interacting information
        // in this request.
        pushClient.setChannelLogHandler((event) -> System.out.println(event.getMessage()));
        try {
            // 4. specify request arguments
            JSONObject notification = makeMessagePackage("4", message);
            
            PushMsgToAllRequest request = new PushMsgToAllRequest()
                //.addMsgExpires(msg.getMsgExpireSeconds())
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
            return ResponseData.makeResponseData(response);
        } catch (PushClientException e) {
            /*
             * ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,'true' 表示抛出, 'false' 表示捕获。
             if (BaiduPushConstants.ERROROPTTYPE) {
            }*/
            logger.error(e.getMessage(), e);
            return ResponseData.makeErrorMessage(2, e.getMessage());
        } catch (PushServerException e) {
            logger.error(e.getMessage(), e);
            return ResponseData.makeErrorMessage(2, e.getErrorMsg());
        }
    }

    /**
     * 广播信息
     *
     * @param sender     发送人内部用户编码
     * @param message    消息主体
     * @param userInline DoubleAspec.ON 在线用户  OFF 离线用户 BOTH 所有用户
     * @return 默认没有实现
     */
    @Override
    public ResponseData broadcastMessage(String sender, NoticeMessage message, DoubleAspect userInline) {
        ResponseData androidRes = broadcastAndroid(message);
        ResponseData iosRes = broadcastIOS(message);
        return ResponseData.makeErrorMessageWithData(
            CollectionsOpt.createHashMap("android", androidRes.getData(),
                "iOS", iosRes.getData()),
            androidRes.getCode() + iosRes.getCode(),
            androidRes.getMessage() + iosRes.getMessage()
        );
    }
}

package com.centit.msgpusher.plugins;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.centit.framework.common.ResponseData;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.common.DoubleAspect;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JiguangMsgPusher implements MessageSender {

    @Setter
    private String appKey;
    @Setter
    private String masterScretKey;

    @Setter
    private IJiguangPushSupport jiguangPushSupport = new IJiguangPushSupport(){};

    private static final Logger logger = LoggerFactory.getLogger(JiguangMsgPusher.class);

    @Override
    public ResponseData sendMessage(String sender, String receiver, NoticeMessage noticeMessage) {
        PushResult pushResult = jPushMessage(sender,
            new String[]{jiguangPushSupport.getReceiverAlias(receiver)},
            noticeMessage);
        return ResponseData.makeErrorMessageWithData(pushResult,
            pushResult.error==null? 0 :pushResult.error.getCode(),
            pushResult.error==null? "ok!" : pushResult.error.getMessage());
    }

    /**
     * 批量发送内部系统消息
     *
     * @param sender    发送人内部用户编码
     * @param receivers 接收人内部用户编码
     * @param message   消息主体
     * @return "OK" 表示成功，其他的为错误信息
     */
    @Override
    public ResponseData sendMessage(String sender, Collection<String> receivers, NoticeMessage message) {
        PushResult pushResult = jPushMessage(sender,
            jiguangPushSupport.getReceiversAlias(CollectionsOpt.listToArray(receivers)),
            message);
        return ResponseData.makeErrorMessageWithData(pushResult,
            pushResult.error==null? 0 :pushResult.error.getCode(),
            pushResult.error==null? "ok!" : pushResult.error.getMessage());
    }

    private PushPayload.Builder buildPayload(String sender,  NoticeMessage noticeMessage){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("pushTime", DatetimeOpt.currentDatetime());// new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        paramMap.put("sender", sender);
        //paramMap.put("receiver", receiver);
        paramMap.put("msgType", noticeMessage.getMsgType());
        paramMap.put("optId", noticeMessage.getOptId());
        paramMap.put("optMethod", noticeMessage.getOptMethod());
        paramMap.put("optTag", noticeMessage.getOptTag());
        //iOS的title和notification都在alert中
        IosAlert iosAlert = IosAlert.newBuilder().setTitleAndBody(
            noticeMessage.getMsgSubject(), null, noticeMessage.getMsgContent()).build();

        //推送的关键,构造一个payload
        PushPayload.Builder payloadBuilder = PushPayload.newBuilder()
            .setPlatform(Platform.all())  //所有平台的用户
//                .setAudience(Audience.registrationId(parm.get("id")))//registrationId指定用户
            //.setAudience(isAll ? Audience.all() : Audience.alias(alias))  //根据别名推送
            .setNotification(Notification.newBuilder()
                .addPlatformNotification(IosNotification.newBuilder() //发送ios
                    .setAlert(iosAlert) //消息体
                    .setBadge(+1)
                    .setSound("happy") //ios提示音
                    .addExtras(paramMap) //附加参数
                    .build())
                .addPlatformNotification(AndroidNotification.newBuilder() //发送android
                    .setTitle(noticeMessage.getMsgSubject())
                    .setAlert(noticeMessage.getMsgContent()) //消息体
                    .addExtras(paramMap) //附加参数
                    .build())
                .build())
            .setOptions(Options.newBuilder().setApnsProduction(false).build())//指定开发环境 true为生产模式 false 为测试模式 (android不区分模式,ios区分模式)
            .setMessage(Message.newBuilder().setMsgContent(noticeMessage.getMsgContent()).addExtras(paramMap).build());//自定义信息
        return payloadBuilder;
    }

    public PushResult jPushMessage(String sender, String[] alias, NoticeMessage noticeMessage) {
        JPushClient jpushClient = new JPushClient(masterScretKey, appKey);  //创建JPushClient(极光推送的实例)
        PushPayload.Builder payloadBuilder = buildPayload(sender, noticeMessage);
        PushResult pushResult =  new PushResult();
        try {
            int userCount = alias.length;
            if(userCount > 999){
                int batch = (userCount -1 ) / 999 + 1;
                for(int i=0; i<batch; i++){
                    payloadBuilder.setAudience(Audience.alias(
                        ArrayUtils.subarray(alias, i*999 , (i+1)*999)));
                    pushResult = jpushClient.sendPush(payloadBuilder.build());
                }
            }else{
                payloadBuilder.setAudience(Audience.alias(alias));
                pushResult = jpushClient.sendPush(payloadBuilder.build());
            }
        } catch (APIConnectionException | APIRequestException e) {
            logger.error(e.getMessage(), e);
        }
        return  pushResult;
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
        JPushClient jpushClient = new JPushClient(masterScretKey, appKey);  //创建JPushClient(极光推送的实例)
        PushPayload.Builder payloadBuilder = buildPayload(sender, message);
        PushResult pushResult =  new PushResult();
        try {
            payloadBuilder.setAudience(Audience.all());
            pushResult = jpushClient.sendPush(payloadBuilder.build());
        } catch (APIConnectionException | APIRequestException e) {
            logger.error(e.getMessage(), e);
        }
        return ResponseData.makeErrorMessageWithData(pushResult,
            pushResult.error==null? 0 :pushResult.error.getCode(),
            pushResult.error==null? "ok!" : pushResult.error.getMessage());
    }
}

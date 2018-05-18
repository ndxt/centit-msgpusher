package com.centit.msgpusher.client;

import com.alibaba.fastjson.JSON;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.msgpusher.client.po.MessageDelivery;
import com.centit.msgpusher.client.po.PushResult;

/**
 * Created by codefan on 17-4-11.
 */
public class MsgPusherNotificationImpl
        implements NotificationCenter {

    private String currentOsId;
    private String defaultNoticeType;
    private MsgPusherClient msgPusherClient;

    public MsgPusherNotificationImpl(){
        defaultNoticeType = "A";
    }
    /**
     * 注册新的发送消息通知
     *
     * @param sendType 发送类型
     * @param sender 发送人
     * @return 响应
     */
    @Override
    public NotificationCenter registerMessageSender(String sendType, MessageSender sender) {
        return this;
    }

    /**
     * 设置默认的发送通知内部
     *
     * @param sendType 发送类型
     * @return 发送人
     */
    @Override
    public MessageSender appointDefaultSendType(String sendType) {
        return this;
    }

    /**
     * @param sender     发送人内部用户编码
     * @param receiver   接收人内部用户编码
     * @param message 消息主题
     * @return "OK" 表示成功，其他的为错误信息
     */
    @Override
    public String sendMessage(String sender, String receiver, NoticeMessage message) {
        return sendMessageAppointedType(defaultNoticeType, sender, receiver, message) ;
    }


    /**
     * @param sender     发送人内部用户编码
     * @param receiver   接收人内部用户编码
     * @param message 消息主题
     * @param noticeType 指定发送类别
     * @return "OK" 表示成功，其他的为错误信息
     */
    @Override
    public String sendMessageAppointedType(String noticeType, String sender, String receiver, NoticeMessage message) {

        MessageDelivery msgdlvry = new MessageDelivery();
        msgdlvry.copyFromNoticeMessage(message);

        msgdlvry.setOsId(currentOsId);
        msgdlvry.setMsgSender(sender);
        msgdlvry.setMsgReceiver(receiver);
        msgdlvry.setNoticeTypes(noticeType);

        try {
            PushResult jsonStr =jsonStr = msgPusherClient.pushMessage(msgdlvry);
            return JSON.toJSONString(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setMsgPusherClient(MsgPusherClient msgPusherClient) {
        this.msgPusherClient = msgPusherClient;
    }

    public void setCurrentOsId(String currentOsId) {
        this.currentOsId = currentOsId;
    }

    public void setDefaultNoticeType(String defaultNoticeType) {
        this.defaultNoticeType = defaultNoticeType;
    }

}

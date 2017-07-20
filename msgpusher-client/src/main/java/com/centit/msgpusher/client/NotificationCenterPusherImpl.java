package com.centit.msgpusher.client;

import com.alibaba.fastjson.JSON;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.msgpusher.client.po.MessageDelivery;
import com.centit.msgpusher.client.po.PushResult;

/**
 * Created by codefan on 17-4-11.
 */
public class NotificationCenterPusherImpl
        implements NotificationCenter, MessageSender {

    private String currentOsId;
    private String defaultNoticeType;
    private MsgPusherClient msgPusherClient;

    public NotificationCenterPusherImpl(){
        defaultNoticeType = "A";
    }
    /**
     * 注册新的发送消息通知
     *
     * @param sendType
     * @param sender
     * @return
     */
    @Override
    public NotificationCenter registerMessageSender(String sendType, MessageSender sender) {
        return this;
    }

    /**
     * 设置默认的发送通知内部
     *
     * @param sendType
     * @return
     */
    @Override
    public MessageSender setDefaultSendType(String sendType) {
        return this;
    }

    /**
     * @param sender     发送人内部用户编码
     * @param receiver   接收人内部用户编码
     * @param msgSubject 消息主题
     * @param msgContent 消息内容
     * @return "OK" 表示成功，其他的为错误信息
     */
    @Override
    public String sendMessage(String sender, String receiver, String msgSubject, String msgContent) {
        return  sendMessage(sender, receiver, msgSubject,  msgContent,  null,
                null,  null, defaultNoticeType) ;
    }

    /**
     * @param sender     发送人内部用户编码
     * @param receiver   接收人内部用户编码
     * @param msgSubject 消息主题
     * @param msgContent 消息内容
     * @param noticeType 指定发送类别
     * @return "OK" 表示成功，其他的为错误信息
     */
    @Override
    public String sendMessage(String sender, String receiver, String msgSubject, String msgContent, String noticeType) {
        return  sendMessage(sender, receiver, msgSubject,  msgContent,  null,
                null,  null, noticeType) ;
    }

    /**
     * @param sender     发送人内部用户编码
     * @param receiver   接收人内部用户编码
     * @param msgSubject 消息主题
     * @param msgContent 消息内容
     * @param optId      关联的业务编号
     * @param optMethod  管理的操作
     * @param optTag     业务主键 ，复合主键用URL方式对的格式 a=v1&b=v2
     * @return "OK" 表示成功，其他的为错误信息
     */
    @Override
    public String sendMessage(String sender, String receiver, String msgSubject, String msgContent, String optId, String optMethod, String optTag) {
        return sendMessage(sender, receiver, msgSubject,  msgContent,  optId,
                 optMethod,  optTag, defaultNoticeType) ;
    }

    /**
     * @param sender     发送人内部用户编码
     * @param receiver   接收人内部用户编码
     * @param msgSubject 消息主题
     * @param msgContent 消息内容
     * @param optId      关联的业务编号
     * @param optMethod  管理的操作
     * @param optTag     业务主键 ，复合主键用URL方式对的格式 a=v1&b=v2
     * @param noticeType 指定发送类别
     * @return "OK" 表示成功，其他的为错误信息
     */
    @Override
    public String sendMessage(String sender, String receiver, String msgSubject, String msgContent, String optId,
                              String optMethod, String optTag, String noticeType) {

        MessageDelivery msgdlvry = new MessageDelivery();
        msgdlvry.setMsgSubject(msgSubject);
        msgdlvry.setMsgContent(msgContent);
        msgdlvry.setNoticeTypes(noticeType);
        msgdlvry.setOptId(optId);
        msgdlvry.setOptMethod(optMethod);
        msgdlvry.setOptTag(optTag);
        msgdlvry.setOsId(currentOsId);
        msgdlvry.setMsgSender(sender);
        msgdlvry.setMsgReceiver(receiver);

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

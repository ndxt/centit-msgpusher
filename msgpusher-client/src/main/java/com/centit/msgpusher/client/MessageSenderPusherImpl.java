package com.centit.msgpusher.client;

import com.centit.framework.common.ResponseData;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.msgpusher.client.po.MessageDelivery;

/**
 * Created by codefan on 17-4-11.
 */
public class MessageSenderPusherImpl implements MessageSender {

    private String currentOsId;
    private MsgPusherClient msgPusherClient;

    /**
     * 发送内部系统消息
     *
     * @param sender     发送人内部用户编码
     * @param receiver   接收人内部用户编码
     * @param message 消息主题
     * @return "OK" 表示成功，其他的为错误信息
     */
    @Override
    public ResponseData sendMessage(String sender, String receiver, NoticeMessage message) {
        MessageDelivery msgdlvry = new MessageDelivery();
        msgdlvry.copyFromNoticeMessage(message);

        msgdlvry.setNoticeTypes("A");
        msgdlvry.setOsId(currentOsId);
        msgdlvry.setMsgSender(sender);
        msgdlvry.setMsgReceiver(receiver);

        try {
            return msgPusherClient.pushMessage(msgdlvry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setCurrentOsId(String currentOsId) {
        this.currentOsId = currentOsId;
    }

    public void setMsgPusherClient(MsgPusherClient msgPusherClient) {
        this.msgPusherClient = msgPusherClient;
    }
}

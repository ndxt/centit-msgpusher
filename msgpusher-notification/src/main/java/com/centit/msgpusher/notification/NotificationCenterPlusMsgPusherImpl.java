package com.centit.msgpusher.notification;

import com.centit.framework.components.impl.NotificationCenterImpl;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.msgpusher.po.SimplePushMessage;
import com.centit.msgpusher.po.SimplePushMsgPoint;
import com.centit.msgpusher.websocket.SocketMsgPusher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通知中心实现，所有的消息通过此类进行发送，消息中心会通过接收用户设置的消息接收方式自行决定使用哪种消息发送方式
 */
public class NotificationCenterPlusMsgPusherImpl extends NotificationCenterImpl {

    private static final Logger logger = LoggerFactory.getLogger(NotificationCenterPlusMsgPusherImpl.class);

    protected SocketMsgPusher socketMsgPusher;
    private boolean useWebSocketPusher;

    public NotificationCenterPlusMsgPusherImpl() {
        super();
        socketMsgPusher = null;
        useWebSocketPusher = false;

    }

    public void setUseWebSocketPusher(boolean useWebSocketPusher) {
        this.useWebSocketPusher = useWebSocketPusher;
    }

    public void setSocketMsgPusher(SocketMsgPusher socketMsgPusher) {
        this.socketMsgPusher = socketMsgPusher;
    }

    private void pushMsgBySocket(String sender, String receiver, NoticeMessage message){
        try {
            SimplePushMessage pushMessage = new SimplePushMessage(sender,message.getMsgSubject(), message.getMsgContent());
            pushMessage.setMsgType( message.getMsgType());
            pushMessage.setMsgReceiver(receiver);
            pushMessage.setOptId(message.getOptId());
            pushMessage.setOptMethod(message.getOptMethod());
            pushMessage.setOptTag(message.getOptTag());

            socketMsgPusher.pushMessage(pushMessage,
                new SimplePushMsgPoint(receiver));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
    }
    /**
     * 根据用户设定的方式发送消息
     * @param sender     发送人内部用户编码
     * @param receiver   接收人内部用户编码
     * @param message 消息主题
     * @return 结果
     */
    @Override
    public String sendMessage(String sender, String receiver, NoticeMessage message) {
        String returnText = super.sendMessage(sender,  receiver,  message);

        if(useWebSocketPusher && socketMsgPusher!=null){
            pushMsgBySocket(sender,  receiver,  message);
        }

        return returnText;
    }



    /**
     * 发送指定类别的消息
     * @param sender     发送人内部用户编码
     * @param receiver   接收人内部用户编码
     * @param message 消息主题
     * @param noticeType   指定发送类别
     * @return 结果
     */
    @Override
    public String sendMessageAppointedType(String noticeType, String sender, String receiver, NoticeMessage message) {
        String returnText = super.sendMessageAppointedType(noticeType, sender,  receiver,  message);

        if(useWebSocketPusher && socketMsgPusher!=null){
            pushMsgBySocket(sender,  receiver,  message);
        }

        return returnText;
    }

}

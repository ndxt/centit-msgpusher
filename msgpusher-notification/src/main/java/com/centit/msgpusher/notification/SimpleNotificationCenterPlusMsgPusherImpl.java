package com.centit.msgpusher.notification;

import com.centit.framework.common.ResponseData;
import com.centit.framework.components.impl.SimpleNotificationCenterImpl;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.support.common.DoubleAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通知中心实现，所有的消息通过此类进行发送，消息中心会通过接收用户设置的消息接收方式自行决定使用哪种消息发送方式
 */
public class SimpleNotificationCenterPlusMsgPusherImpl extends SimpleNotificationCenterImpl {

    private static final Logger logger = LoggerFactory.getLogger(SimpleNotificationCenterPlusMsgPusherImpl.class);

    protected MessageSender msgPusher;
    private boolean useMsgPusher;

    public SimpleNotificationCenterPlusMsgPusherImpl() {
        super();
        msgPusher = null;
        useMsgPusher = false;

    }

    public void setMsgPusher(MessageSender msgPusher) {
        this.msgPusher = msgPusher;
        this.useMsgPusher = this.msgPusher!=null;
    }

    /**
     * 根据用户设定的方式发送消息
     * @param sender     发送人内部用户编码
     * @param receiver   接收人内部用户编码
     * @param message 消息主题
     * @return 结果
     */
    @Override
    public ResponseData sendMessage(String sender, String receiver, NoticeMessage message) {
        ResponseData returnText = super.sendMessage(sender,  receiver,  message);
        if(useMsgPusher){
            msgPusher.sendMessage(sender, receiver, message);
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
    public ResponseData sendMessageAppointedType(String noticeType, String sender, String receiver, NoticeMessage message) {
        ResponseData returnText = super.sendMessageAppointedType(noticeType, sender,  receiver,  message);
        if(useMsgPusher){
            msgPusher.sendMessage(sender, receiver, message);
        }
        return returnText;
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
        if(!useMsgPusher) return ResponseData.errorResponse;
        return msgPusher.broadcastMessage(sender, message, userInline);
    }
}

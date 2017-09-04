package com.centit.msgpusher.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.appclient.AppSession;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.msgpusher.client.po.MessageDelivery;
import com.centit.msgpusher.client.po.PushResult;
import com.centit.support.network.HttpExecutor;
import org.apache.http.impl.client.CloseableHttpClient;

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
     * @param msgSubject 消息主题
     * @param msgContent 消息内容
     * @return "OK" 表示成功，其他的为错误信息
     */
    @Override
    public String sendMessage(String sender, String receiver, String msgSubject, String msgContent) {
        return  sendMessage( sender,  receiver,  msgSubject,
                 msgContent, null,null,null /*optId,  optMethod,  optTag*/);
    }

    /**
     * 发送内部系统消息
     *
     * @param sender     发送人内部用户编码
     * @param receiver   接收人内部用户编码
     * @param msgSubject 消息主题
     * @param msgContent 消息内容
     * @param optId      关联的业务编号
     * @param optMethod  管理的操作
     * @param optTag     业务主键 复合主键用URL方式对的格式 a等于v1 且 b等于v2
     * @return "OK" 表示成功，其他的为错误信息
     */
    @Override
    public String sendMessage(String sender, String receiver, String msgSubject,
                              String msgContent, String optId, String optMethod, String optTag) {
        MessageDelivery msgdlvry = new MessageDelivery();
        msgdlvry.setMsgSubject(msgSubject);
        msgdlvry.setMsgContent(msgContent);
        msgdlvry.setNoticeTypes("A");
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

    public void setCurrentOsId(String currentOsId) {
        this.currentOsId = currentOsId;
    }

    public void setMsgPusherClient(MsgPusherClient msgPusherClient) {
        this.msgPusherClient = msgPusherClient;
    }
}

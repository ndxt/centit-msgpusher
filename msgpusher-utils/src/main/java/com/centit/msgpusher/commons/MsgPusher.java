package com.centit.msgpusher.commons;

import com.centit.msgpusher.po.IPushMessage;
import com.centit.msgpusher.po.IPushMsgPoint;

/**
 * Created by codefan on 17-4-6.
 */
public interface MsgPusher {

    /** 点对点发送信息
     * @param msg     发送的消息
     * @param receiver   接收人内部用户编码
     * @return MSGID 表示成功， null 和空 其他的为错误信息
     * @throws Exception Exception
     */
    PushResult pushMessage(IPushMessage msg, IPushMsgPoint receiver) throws Exception;




    /** 广播发送信息
     * @param msg     发送的消息
     * @return MSGID 表示成功， null 和空 其他的为错误信息
     * @throws Exception Exception
     */
    PushResult pushMsgToAll(IPushMessage msg) throws Exception;


}

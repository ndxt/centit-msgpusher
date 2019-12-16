package com.centit.msgpusher.service;

import com.centit.framework.common.ResponseData;
import com.centit.msgpusher.po.MessageDelivery;
import com.centit.msgpusher.po.UserMsgPoint;

/**
 * Created by codefan on 17-4-10.
 */
public interface MsgPusherCenter  {

    /**
     * 点对点发送信息
     *
     * @param msg  发送人内部用户编码
     * @param userMsgPoint userMsgPoint
     * @return MSGID 表示成功， null 和空 其他的为错误信息
     * @throws Exception Exception
     */

    ResponseData pushMessage(MessageDelivery msg, UserMsgPoint userMsgPoint) throws Exception;




    /**
     * 广播发送信息
     *
     * @param msg  发送人内部用户编码
     * @return MSGID 表示成功， null 和空 其他的为错误信息
     * @throws Exception Exception
     */
    ResponseData pushMsgToAll(MessageDelivery msg) throws Exception;
}

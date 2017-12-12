package com.centit.msgpusher.msgpusher.websocket;

import com.centit.msgpusher.msgpusher.MsgPusher;

import javax.websocket.Session;

/**
 * Created by zhang_gd on 2017/4/18.
 */

public interface SocketMsgPusher extends MsgPusher {

    /**
     * 登录
     * @param osId 系统ID
     * @param userCode 用户编号
     * @param session session
     */
    void signInUser(String osId, String userCode, Session session);

    /**
     * 登出服务
     * @param session session
     */
    void signOutUser(Session session);

    /**
     * 接受消息，并对消息进行处理
     * @param session session
     * @param jsonMessage jsonMessage
     */
    void recvMessage(Session session, String jsonMessage);

}

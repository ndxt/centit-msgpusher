package com.centit.msgpusher.msgpusher;

import javax.websocket.Session;

/**
 * Created by zhang_gd on 2017/4/18.
 */

public interface SocketMsgPusher extends MsgPusher {

    /**
     * 登录
     * @param userCode
     * @param session
     */
    void signInUser(String osId, String userCode, Session session);

    /**
     * 登出服务
     * @param session
     */
    void signOutUser(Session session);

    /**
     * 接受消息，并对消息进行处理
     * @param session
     * @param jsonMessage
     */
    void recvMessage(Session session, String jsonMessage);

}

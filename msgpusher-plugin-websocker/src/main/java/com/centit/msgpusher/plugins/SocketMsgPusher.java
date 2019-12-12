package com.centit.msgpusher.plugins;

import com.centit.framework.model.adapter.MessageSender;

import javax.websocket.Session;

/**
 * Created by zhang_gd on 2017/4/18.
 */

public interface SocketMsgPusher extends MessageSender {

    /**
     * 登录
     * @param userCode 用户编号
     * @param session session
     */
    void signInUser(String userCode, Session session);

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

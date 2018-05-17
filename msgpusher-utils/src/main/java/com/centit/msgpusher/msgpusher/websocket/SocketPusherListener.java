package com.centit.msgpusher.msgpusher.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by codefan on 17-5-19.
 */
@ServerEndpoint(value="/pusher/{osId}/{userCode}" ,configurator = SpringConfigurator.class)
@Service
public class SocketPusherListener {

    @Resource
    protected SocketMsgPusher socketMsgPusher;

    /**
     * 连接建立成功调用的方法
     * @param session session
     * @param osId 系统ID
     * @param userCode 用户编号
     */
    @OnOpen
    public void onOpen(Session session,
                       @PathParam("osId") String osId,
                       @PathParam("userCode") String userCode) {
        socketMsgPusher.signInUser(osId, userCode,session);
    }

    /**
     * 连接关闭调用的方法
     * @param session session
     */
    @OnClose
    public void onClose(Session session) {
        socketMsgPusher.signOutUser(session);
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 消息
     * @param session session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        socketMsgPusher.recvMessage(session, message);
    }

    /**
     * 发生错误时调用
     *
     * @param error 错误异常
     */
    @OnError
    public void onError(Throwable error) {
        //System.out.println("llws发生错误");
        error.printStackTrace();
    }
}

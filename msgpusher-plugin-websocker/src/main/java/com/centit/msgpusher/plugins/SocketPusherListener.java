package com.centit.msgpusher.plugins;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by codefan on 17-5-19.
 */
//@Service 这个在 spring boot中是必须的，在web 容器（tomcat）的war包中不需要，
// 所以去掉然后在spring boot的配置类中添加这个bean的创建方法
@ServerEndpoint(value="/pusher/{userCode}" /*,configurator = SpringConfigurator.class*/)
public class SocketPusherListener {

    @Resource
    protected SocketMsgPusher socketMsgPusher;

    /**
     * 连接建立成功调用的方法
     * @param session session
     * @param userCode 用户编号
     */
    @OnOpen
    public void onOpen(Session session,
                       @PathParam("userCode") String userCode) {
        socketMsgPusher.signInUser(userCode,session);
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

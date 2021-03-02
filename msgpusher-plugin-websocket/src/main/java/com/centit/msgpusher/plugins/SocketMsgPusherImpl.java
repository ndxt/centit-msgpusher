package com.centit.msgpusher.plugins;

import com.alibaba.fastjson.JSON;
import com.centit.framework.common.ResponseData;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.support.common.DoubleAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhang_gd on 2017/4/18.
 */

public class SocketMsgPusherImpl implements ISocketMsgEvent, MessageSender {

    private static final Logger logger = LoggerFactory.getLogger(SocketMsgPusherImpl.class);

    private static ConcurrentHashMap<String, Session> userCodeToSession
            = new ConcurrentHashMap<>();//根据用户找session
    private static ConcurrentHashMap<Session, String> sessionToUserCode
            = new ConcurrentHashMap<>();//根据session找用户


    private static boolean webSockectPushMessage(Session session , String message) {
        if(session==null) {
            return false;
        }
        boolean pushOk = true;
        synchronized (session) {
            //session.getAsyncRemote().sendText(message);
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                pushOk=false;
                logger.info(e.getLocalizedMessage());
            }
        }
        return pushOk;
    }

    /**
     * 发送内部系统消息
     *
     * @param sender   发送人内部用户编码
     * @param receiver 接收人内部用户编码
     * @param message  消息主体
     * @return "OK" 表示成功，其他的为错误信息
     */
    @Override
    public ResponseData sendMessage(String sender, String receiver, NoticeMessage message) {
        logger.debug("给"+ receiver + "发送消息"+ message.getMsgContent());
        Session socketIOClient = getSessionByUserCode(receiver);
        boolean result = webSockectPushMessage(socketIOClient, JSON.toJSONString(message));
        return result ? ResponseData.successResponse : ResponseData.errorResponse;
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
        int succeed = 0;
        if(DoubleAspect.ON.matchAspect(userInline)){

            for(Map.Entry<String, Session> ent : userCodeToSession.entrySet()) {
                if(webSockectPushMessage(ent.getValue(), JSON.toJSONString(message))){
                    succeed ++;
                }
            }
        }
        return ResponseData.makeErrorMessageWithData(succeed,0, "成功推送消息给" + succeed + "人");
    }



    private static Session getSessionByUserCode(String userCode){
        if(userCode == null) {
            return null;
        }
        return userCodeToSession.get(userCode);
    }

    private static String getUserCodeBySession(Session session){
        if(session == null) {
            return null;
        }
        return sessionToUserCode.get(session);
    }

    @Override
    public void signInUser(String userCode, Session session) {
        //Session oldSession = getSessionByUserCode(userCode);
        userCodeToSession.put(userCode, session);
        sessionToUserCode.put(session, userCode);
    }

    /**
     * 登出服务
     *
     * @param session session
     */
    @Override
    public void signOutUser(Session session) {
        String userCode = getUserCodeBySession(session);
        userCodeToSession.remove(userCode);
        sessionToUserCode.remove(session);
    }

    @Override
    public void recvMessage(Session session, String jsonMessage){
        String userCode = getUserCodeBySession(session);
        logger.info(userCode +" push "+ jsonMessage);
    }

}

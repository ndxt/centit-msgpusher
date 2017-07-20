package com.centit.msgpusher.msgpusher.websocket;

import com.alibaba.fastjson.JSON;
import com.centit.msgpusher.msgpusher.PushResult;
import com.centit.msgpusher.msgpusher.SocketMsgPusher;
import com.centit.msgpusher.po.MessageDelivery;
import com.centit.msgpusher.po.UserMsgPoint;
import com.centit.msgpusher.service.MessageDeliveryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhang_gd on 2017/4/18.
 */
@Service("socketMsgPusher")
public class SocketMsgPusherImpl implements SocketMsgPusher {

    @Resource
    private MessageDeliveryManager messageDeliveryManager;

    private static final Logger logger = LoggerFactory.getLogger(SocketMsgPusherImpl.class);

    private static ConcurrentHashMap<String, Session> userCodeToSession
            = new ConcurrentHashMap<>();//根据用户找session
    private static ConcurrentHashMap<Session, String> sessionToUserCode
            = new ConcurrentHashMap<>();//根据session找用户


    private static boolean webSockectPushMessage(Session session , String message) {
        if(session==null)
            return false;
        session.getAsyncRemote().sendText(message);
        return true;
    }

    @Override
    public PushResult pushMessage(MessageDelivery msg, UserMsgPoint receiver) throws Exception {
        PushResult pushResult = new PushResult();
        Map<String,String> pcMap =new HashMap<>();
        String osId = msg.getOsId();
        String userCode = msg.getMsgReceiver();
        String user = osId + "_" + userCode;
        String contentText = msg.getMsgContent();
        //String viewUrl = msg.getRelUrl();
        logger.debug("给"+ userCode + "发送消息"+contentText);

        Session socketIOClient = userCodeToSession.get(user);
        if( webSockectPushMessage(socketIOClient, JSON.toJSONString(msg)) ) {
            pcMap.put("pc","OK");
            pushResult.setMap(pcMap);
            pushResult.setPushState("1");
        }else{
            pcMap.put("pc","用户不存在");
            pushResult.setMap(pcMap);
            pushResult.setPushState("2");
        }
        //socketIOClient.sendEvent("msg", json);
        return pushResult;
    }


    @Override
    public PushResult pushMsgToAll(MessageDelivery msg) throws Exception {
        PushResult pushResult = new PushResult();
        Map<String,String> pcMap =new HashMap<>();
        //int succeed = 0;
        for(Map.Entry<String, Session> ent : userCodeToSession.entrySet()) {
            webSockectPushMessage(ent.getValue(), JSON.toJSONString(msg));
        }
        int socNo = userCodeToSession.size();
        pcMap.put("pc","OK，连接数：" + socNo);
        pushResult.setPushState("0");
        pushResult.setMap(pcMap);
        return pushResult;
    }



    private static Session getSessionByUserCode(String userCode){
        if(userCode == null)
            return null;
        return userCodeToSession.get(userCode);
    }

    private static String getUserCodeBySession(Session session){
        if(session == null)
            return null;
        return sessionToUserCode.get(session);
    }

    @Override
    public void signInUser(String osId, String userCode, Session session) {
        Session oldSession = getSessionByUserCode(userCode);
        userCodeToSession.put(osId + "_" + userCode, session);
        sessionToUserCode.put(session,osId + "_" + userCode);
    }

    /**
     * 登出服务
     *
     * @param session
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

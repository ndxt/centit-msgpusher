package com.centit.msgpusher.service.impl;

import com.centit.framework.common.ResponseData;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.msgpusher.dao.MessageDeliveryDao;
import com.centit.msgpusher.dao.UserNotifySettingDao;
import com.centit.msgpusher.po.MessageDelivery;
import com.centit.msgpusher.po.UserMsgPoint;
import com.centit.msgpusher.service.MessageDeliveryManager;
import com.centit.msgpusher.service.MsgPusherCenter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by codefan on 17-4-10.
 */
@Service("msgPusherCenter")
public class MsgPusherCenterImpl implements MsgPusherCenter {

    @Resource(name="appMsgPusher")
    private MessageSender appPusher;

    @Resource(name="emailMsgPusher")
    private MessageSender emailPusher;

    @Resource(name="wxMsgPusher")
    private MessageSender wxPusher;

    @Resource(name="smsMsgPusher")
    private MessageSender smsPusher;

    @Resource(name="socketMsgPusher")
    private MessageSender socketPusher;

    @Resource(name = "userNotifySettingDao")
    private UserNotifySettingDao userNotifySettingDao ;

    @Resource(name = "messageDeliveryDao")
    private MessageDeliveryDao messageDeliveryDao ;


    @Resource
    private MessageDeliveryManager messageDeliveryManager;

    private Map<String/*pushType*/,MessageSender> pusherMap;

    @PostConstruct
    public void init(){
        //通知方式 可以多种方式  A：app推送， S：短信  C：微信  N：内部通知系统 U: unknown 未指定
        pusherMap = new HashMap<>();
        pusherMap.put(MessageDelivery.NOTICE_TYPE_APP,appPusher);
        pusherMap.put(MessageDelivery.NOTICE_TYPE_EMAIL,emailPusher);
        pusherMap.put(MessageDelivery.NOTICE_TYPE_WX,wxPusher);
        pusherMap.put(MessageDelivery.NOTICE_TYPE_SMS,smsPusher);
    }




    /**
     * 点对点发送信息
     *
     * @param msg     发送人内部用户编码
     * @return MSGID 表示成功， null 和空 其他的为错误信息
     */

    @Override
    public ResponseData pushMessage(MessageDelivery msg, UserMsgPoint userMsgPoint)throws Exception{
        /*ResponseData pushResult = ResponseData.makeSuccessResponse();
        Map<String, ResponseData> resultMap = new HashMap<>();
        Map<String, String> pushMap = new HashMap<>();
        String noticeTypes = msg.getNoticeTypes();
        if (!StringUtils.isBlank(noticeTypes)) {
            String[] noticeTypeArray =  noticeTypes.split(",");
            //多种发送方式循环发送
            for (int i = 0; i < noticeTypeArray.length; i++) {
                if (pusherMap.get(noticeTypeArray[i]) != null){
                    ResponseData pushResultTemp =
                        pusherMap.get(noticeTypeArray[i])
                            .sendMessage(msg, userMsgPoint);
                    resultMap.put(noticeTypeArray[i], pushResultTemp); //存储不同推送方式的返回信息
                }
            }
            if (resultMap.size() == 0) {
                return null;
            }
            String appPushState = null;
            String eMailPushState = null;
            for (Map.Entry<String, PushResult> entry : resultMap.entrySet()) {
                switch (entry.getKey()) {
                    case "A"://app推送结果
                        PushResult appPushResult = entry.getValue();
                        pushResult.setMsgId(appPushResult.getMsgId());
                        if (StringUtils.isBlank(appPushResult.getMsgId())) {
                            pushMap.put("app", appPushResult.getMap().get("app"));
                        }
                        //添加pc推送结果
                        pushMap.put("pc", appPushResult.getMap().get("pc"));
                        appPushState = appPushResult.getPushState();
                        pushResult.setPushState(appPushState);
                        break;
                    case "E"://eMail推送结果
                        PushResult eMailPushResult = entry.getValue();
                        pushMap.put("eMail", eMailPushResult.getMap().get("eMail"));
                        eMailPushState = eMailPushResult.getPushState();
                        pushResult.setPushState(eMailPushState);
                        break;
                }
            }
            if (!StringUtils.equals(appPushState,eMailPushState)){
                pushResult.setPushState("3");//部分推送成功
            }
            pushResult.setMap(pushMap);
        }*/
        return ResponseData.successResponse;
    }




    /**
     * 广播发送信息
     *
     * @param msg     发送人内部用户编码
     * @return MSGID 表示成功， null 和空 其他的为错误信息
     */
    @Override
    public ResponseData pushMsgToAll(MessageDelivery msg) throws Exception{
        /*PushResult pushResult = new PushResult();
        String noticeTypes = msg.getNoticeTypes();
        Map<String,PushResult> map = new HashMap<>();
        Map<String, String> pushMap = new HashMap<>();
        String[] noticeTypeArray = noticeTypes.split(",");
        //多种发送方式循环发送
        for(int i = 0;i<noticeTypeArray.length;i++){
            if (pusherMap.get(noticeTypeArray[i]) != null){
                PushResult pushResultTemp = pusherMap.get(noticeTypeArray[i]).pushMsgToAll(msg);
                map.put(noticeTypeArray[i],pushResultTemp);//存储不同推送方式的返回信息
            }
        }
        if (map.size() == 0) {
            return null;
        }
        String appPushState = null;
        String eMailPushState = null;
        for (Map.Entry<String,PushResult> entry : map.entrySet()) {
            switch (entry.getKey()){
                //解析APP和PC发送返回信息
                case "A":
                    PushResult appPushResult =entry.getValue();
                    //添加APP推送结果
                    pushResult.setMsgId(appPushResult.getMsgId());
                    pushResult.setMsgId2(appPushResult.getMsgId2());
                    if(StringUtils.isBlank(appPushResult.getMsgId())){
                        pushMap.put("android_error",appPushResult.getMap().get("android_error"));
                    }
                    if(StringUtils.isBlank(appPushResult.getMsgId2())){
                        pushMap.put("ios_error",appPushResult.getMap().get("ios_error"));
                    }
                    pushMap.put("pc",appPushResult.getMap().get("pc"));
                    appPushState = pushResult.getPushState();
                    pushResult.setPushState(appPushState);
                    break;
                //解析e_mail发送返回信息
                case "E":
                    PushResult eMailPushResult =entry.getValue();
                    pushMap.put("eMail",eMailPushResult.getMap().get("eMail"));
                    eMailPushState = eMailPushResult.getPushState();
                    pushResult.setPushState(eMailPushState);
                    break;
                default:
            }
        }
        if (!StringUtils.equals(appPushState,eMailPushState)){
            pushResult.setPushState("3");//部分推送成功
        }
        pushResult.setMap(pushMap);*/
        return ResponseData.successResponse;
    }

}

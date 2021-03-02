package com.centit.msgpusher.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.ResponseData;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.msgpusher.po.MessageDelivery;
import com.centit.msgpusher.po.UserMsgPoint;
import com.centit.msgpusher.service.MsgPusherCenter;
import com.centit.support.common.DoubleAspect;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by codefan on 17-4-10.
 */
//@Service("msgPusherCenter")
public class MsgPusherCenterImpl implements MsgPusherCenter {

    /*@Resource(name="appMsgPusher")
    private MessageSender appPusher;

    @Resource(name="emailMsgPusher")
    private MessageSender emailPusher;

    @Resource(name="wxMsgPusher")
    private MessageSender wxPusher;

    @Resource(name="smsMsgPusher")
    private MessageSender smsPusher;

    @Resource(name="socketMsgPusher")
    private MessageSender socketPusher;
*/
    private String defaultPushType;

 /*   @Resource(name = "messageDeliveryDao")
    private MessageDeliveryDao messageDeliveryDao ;

    @Resource
    private MessageDeliveryManager messageDeliveryManager;*/

    private Map<String/*noticeTypes*/,MessageSender> pusherMap;

/*    @PostConstruct
    public void init(){
        //通知方式 可以多种方式  A：app推送， S：短信  C：微信  N：内部通知系统 U: unknown 未指定
        pusherMap = new HashMap<>();
        pusherMap.put(MessageDelivery.NOTICE_TYPE_APP,appPusher);
        pusherMap.put(MessageDelivery.NOTICE_TYPE_EMAIL,emailPusher);
        pusherMap.put(MessageDelivery.NOTICE_TYPE_WX,wxPusher);
        pusherMap.put(MessageDelivery.NOTICE_TYPE_SMS,smsPusher);
    }*/

    public MsgPusherCenterImpl(){
        pusherMap = new HashMap<>();
    }

    /**
     * 注册消息推送
     * @param sendType  推送方式
     * @param sender    消息推送体
     */
    @Override
    public void registerMessageSender(String sendType, MessageSender sender){
        pusherMap.put(sendType, sender);
    }

    /**
     * 设置默认推送方式
     * @param defaultPushType   默认推送方式
     */

    public void setDefaultPushType(String defaultPushType) {
        this.defaultPushType = defaultPushType;
    }

    public String getDefaultPushType() {
        return this.defaultPushType;
    }


    /**
     * 点对点发送信息
     * @param msg     发送人内部用户编码
     * @return MSGID 表示成功， null 和空 其他的为错误信息
     */

    @Override
    public ResponseData pushMessage(MessageDelivery msg, UserMsgPoint userMsgPoint)throws Exception{
        if(pusherMap.size()==0){//这个判断是为了防止后面去掉默认值后出错
            return  ResponseData.makeErrorMessage("消息发送失败，必须调用registerMessageSender()指定消息发送类型！例如：微信，短信");
        }
        Map<String, ResponseData> resultMap = new HashMap<>();
        String noticeTypes = msg.getNoticeTypes();
        if (!StringUtils.isBlank(noticeTypes)) {
            String[] noticeTypeArray = noticeTypes.split(",");
            //多种发送方式循环发送
            for (int i = 0; i < noticeTypeArray.length; i++) {
                if (pusherMap.get(noticeTypeArray[i]) != null) {//推送缓存中必须要存在消息推送方式
                    ResponseData pushResultTemp = pusherMap.get(noticeTypeArray[i]).sendMessage(msg.getMsgSender(), msg.getMsgReceiver(), msg.toNoticeMessage());
                    resultMap.put(noticeTypeArray[i], pushResultTemp); //存储不同推送方式的返回信息
                }
            }
            if (resultMap.size() == 0) {
                return null;
            }
        }
        if(resultMap.size()==1){
            return resultMap.values().iterator().next();
        }
        JSONObject jsonObject =new JSONObject();
        AtomicBoolean flag= new AtomicBoolean(true);
        resultMap.forEach((key,value)->{
            if (value.getCode()!=0){
                jsonObject.put(key,"消息发送失败！错误信息："+value.getMessage());
                flag.set(false);
            }else {
                jsonObject.put(key,"消息发送成功！返回信息："+value.getMessage());
            }
        });
        return flag.get()?ResponseData.makeResponseData(jsonObject):ResponseData.makeErrorMessageWithData(jsonObject,3,"部分失败,请见返回详情!");//3 代表部分成功
    }

    /**
     * 广播发送信息
     *
     * @param msg     发送人内部用户编码
     * @return MSGID 表示成功， null 和空 其他的为错误信息
     */
    @Override
    public ResponseData pushMsgToAll(MessageDelivery msg) throws Exception{
        if(pusherMap.size()==0){
            return  ResponseData.makeErrorMessage("消息发送失败，必须调用registerMessageSender()指定消息发送类型！举例： A：app推送， S：短信  C：微信  E：邮件");
        }
        String noticeTypes = msg.getNoticeTypes();
        Map<String,ResponseData> map = new HashMap<>();
        String[] noticeTypeArray = noticeTypes.split(",");
        //多种发送方式循环发送
        for(int i = 0;i<noticeTypeArray.length;i++){
            if (pusherMap.get(noticeTypeArray[i]) != null){
                ResponseData pushResultTemp = pusherMap.get(noticeTypeArray[i]).broadcastMessage(
                    msg.getMsgSender(), msg.toNoticeMessage() , DoubleAspect.YES);
                map.put(noticeTypeArray[i],pushResultTemp);//存储不同推送方式的返回信息
            }
        }
        if (map.size() == 0) {
            return null;
        }
        int eerors = 0;
        StringBuilder errormsg = new StringBuilder();
        for (Map.Entry<String, ResponseData> entry : map.entrySet()) {
            ResponseData resp = entry.getValue();
            if(resp.getCode() != 0){
                eerors ++;
                errormsg.append("发送方式：").append(entry.getKey())
                    .append(":广播消息出错，错误提示：").append(resp.getMessage())
                    .append("。\r\n");
            }else {
                errormsg.append("发送方式：").append(entry.getKey())
                    .append(":广播消息成功，返回结果：").append(resp.getMessage())
                    .append("。\r\n");
            }
        }
        return ResponseData.makeErrorMessage(eerors==0?0:(eerors==map.size()?2:3),
            errormsg.toString());
    }

}

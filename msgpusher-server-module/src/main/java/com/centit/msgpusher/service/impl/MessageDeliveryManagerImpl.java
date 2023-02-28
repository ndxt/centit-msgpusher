package com.centit.msgpusher.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.msgpusher.dao.MessageDeliveryDao;
import com.centit.msgpusher.dao.UserMsgPointDao;
import com.centit.msgpusher.po.MessageDelivery;
import com.centit.msgpusher.po.UserMsgPoint;
import com.centit.msgpusher.service.MessageDeliveryManager;
import com.centit.msgpusher.service.MsgPusherCenter;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.*;


/**
 * IPushMessage  Service.
 * create by scaffold 2017-04-07
 * @author codefan@sina.com
 * 消息推送null
*/
@Service("messageDeliveryManager")
public class MessageDeliveryManagerImpl
        extends BaseEntityManagerImpl<MessageDelivery, String, MessageDeliveryDao>
    implements MessageDeliveryManager{

    public static final Logger logger = LoggerFactory.getLogger(MessageDeliveryManagerImpl.class);


    @Value("${plugins.msg.validtyPeriod:1}")
    private int validtyPeriod;

    @Resource(name="msgPusherCenter")
    private MsgPusherCenter msgPusherCenter;

    @Resource(name = "userMsgPointDao")
    private UserMsgPointDao userMsgPointDao ;

    private MessageDeliveryDao messageDeliveryDao ;

    @Resource(name = "messageDeliveryDao")
    @NotNull
    public void setMessageDeliveryDao(MessageDeliveryDao baseDao)
    {
        this.messageDeliveryDao = baseDao;
        setBaseDao(this.messageDeliveryDao);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public JSONArray listMessageDeliverysAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc){
        return DictionaryMapUtils.mapJsonArray( messageDeliveryDao.listObjectsPartFieldByPropertiesAsJson(
            filterMap, CollectionsOpt.arrayToList(fields), pageDesc),MessageDelivery.class);
    }

    /**
     * 点对点推送消息的方法
     * @param msg 消息
     * @return 返回消息ID
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public ResponseData pushMessage(MessageDelivery msg){
        if(msg.getMsgExpireSeconds() == null || msg.getMsgExpireSeconds() == 0){
            msg.setMsgExpireSeconds(3600 * 5);//消息默认过期时间
        }
        if (StringUtils.isBlank(msg.getMsgReceiver())){
            return new ResponseMapData(ResponseData.ERROR_USER_CONFIG,"请填写接收人");
        }
        //如果接收人不存在则返回接收人不存在
        UserMsgPoint userMsgPoint = userMsgPointDao.getObjectById(msg.getMsgReceiver());
        if (userMsgPoint == null){
            return makeErrorPushResult("接收人不存在",msg);
        }

        JSONObject json = new JSONObject();
        Date planPushTime = msg.getPlanPushTime();
        msg.setPushType("0");//点对点推送
        //如果指定为定时发送且发送时间大于当前时间则直接保存不发送
        // 疑问：这个后面是否需要加一个定时任务定时扫描f_message_delivery消息推送表，将前面添加的定时发送消息给推送出去？
        if (planPushTime != null && planPushTime.after(DatetimeOpt.currentUtilDate())){
            msg.setPushState("0");
            messageDeliveryDao.saveNewObject(msg);
            return ResponseData.successResponse;
        }
        //如果未指定推送方式则查询设定的推送方式
        /*if (StringUtils.isBlank(msg.getNoticeTypes()) || "U".equals(msg.getNoticeTypes())){
            String notifySetting = userNotifySettingDao.getNotifyByUserCode(msg.getMsgReceiver(),msg.getOsId());
            msg.setNoticeTypes(notifySetting);
            //如果设定的推送方式也为空则返回为指定推送方式异常
            if(StringUtils.isBlank(notifySetting) || "U".equals(notifySetting)){
                return makeErrorPushResult("未指定推送方式",msg);
            }
        }*/

        try {
            ResponseData pushResult = msgPusherCenter.pushMessage(msg, userMsgPoint);
            if (3==pushResult.getCode()){
                msg.setPushState("3");//部分成功
            }else {
                msg.setPushState(pushResult.getCode() == 0?"1":"2");
            }
            msg.setPushTime(DatetimeOpt.currentUtilDate());//当前时间
            msg.setPushResult(json.toString());
            //默认消息有效时间为发送时间加一天
            if (msg.getValidPeriod() == null){
                msg.setValidPeriod(changeDate(DatetimeOpt.currentUtilDate(),1));
            }
            if (StringUtils.isBlank(msg.getMsgId())){
                messageDeliveryDao.saveNewObject(msg);
            }else{
                messageDeliveryDao.updateObject(msg);
            }
            return pushResult;
        } catch (Exception e) {
            logger.error("推送服务异常");
            return makeErrorPushResult("推送服务异常",msg);
        }
    }

    /**
     * 消息广播
     *
     * @param msg 消息
     * @return 返回消息ID
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public ResponseData pushMsgToAll(MessageDelivery msg){
        String notifyType = msg.getNoticeTypes();
        //群发未指定推送方式则直接返回未指定推送方式异常
        if (StringUtils.isBlank(notifyType) || "U".equals(notifyType)) {
            return makeErrorPushResult("未指定推送方式",msg);
        }
        if(msg.getMsgExpireSeconds() == null || msg.getMsgExpireSeconds() == 0){
            msg.setMsgExpireSeconds(3600 * 5);//消息默认过期时间
        }
        JSONObject json = new JSONObject();
        Date planPushTime = msg.getPlanPushTime();
        msg.setPushType("1");//群发
        //如果指定为定时发送且发送时间大于当前时间则直接保存不发送
        if(planPushTime != null && planPushTime.after(DatetimeOpt.currentUtilDate())){
            msg.setPushState("0");
            messageDeliveryDao.saveNewObject(msg);
            return ResponseData.successResponse;
        }

        try {
            ResponseData pushResult = msgPusherCenter.pushMsgToAll(msg);
            if (3==pushResult.getCode()){
                msg.setPushState("3");//部分成功
            }else {
                msg.setPushState(pushResult.getCode() == 0?"1":"2");
            }
            msg.setPushTime(DatetimeOpt.currentUtilDate());//当前时间
            msg.setPushResult(json.toString());
            //默认消息有效时间为发送时间加一天
            if (msg.getValidPeriod() == null){
                msg.setValidPeriod(changeDate(DatetimeOpt.currentUtilDate(),1));
            }
            if (StringUtils.isBlank(msg.getMsgId())){
                messageDeliveryDao.saveNewObject(msg);
            }else{
                messageDeliveryDao.updateObject(msg);
            }
            return pushResult;
        } catch (Exception e) {
            logger.error("推送服务异常");
            return makeErrorPushResult("推送服务异常",msg);
        }
    }

    /**
     * 时间向后推n天，n未负值表示向前推
     * @param time 时间
     * @param n int
     * @return
     */
    private Date changeDate(Date time,int n){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(time);
        calendar.add(Calendar.DATE, n);//把日期往后增加一天.整数往后推,负数往前移动
        return calendar.getTime();
    }

    /**
     * 保存推送失败原因
     * @param errorReason 错误原因
     * @param msg 消息
     * @return
     */
    private ResponseData makeErrorPushResult(String errorReason, MessageDelivery msg){
        JSONObject json = new JSONObject();
        json.put("error",errorReason);
        msg.setPushResult(json.toString());
        msg.setPushState("2");
        messageDeliveryDao.saveNewObject(msg);
        return ResponseData.makeErrorMessage(2, errorReason);
    }

    /**
     * 这个维护型作业可以直接调用 delete 语句
     *
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRecords() {
        Date validDate = new Date(System.currentTimeMillis()-validtyPeriod*86400000L);
        String hql = "delete from IPushMessage m where m.pushTime<=?";
        DatabaseOptUtils.doExecuteSql(messageDeliveryDao,
                hql,new Object[]{validDate});
        /*List<IPushMessage> msgList = messageDeliveryDao.listObjects(hql,new Object[]{validDate});
        JSONObject obj = new JSONObject();
        int deleteSum = msgList.length();
        if(msgList.isEmpty()){
            return deleteSum;
        }
        messageDeliveryDao.deleteObjectsAsTabulation(msgList);
        return deleteSum;*/
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<MessageDelivery> viewRecords(String osId, String optId, Date pushTimeStart, Date pushTimeEnd) {
        String hql = "where osId=? and optId=? and pushTime>? and pushTime<=?";
        List<MessageDelivery> msgList = messageDeliveryDao.listObjectsByFilter(hql,new Object[]{osId, optId, pushTimeStart, pushTimeEnd});
        return msgList;
    }


    /**
     * 查询出所有定时推送的消息记录
     * @param queryParamsMap 查询参数
     * @param pageDesc 分页
     * @return JSONArray
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public JSONArray listAllPlanPush(Map<String, Object> queryParamsMap, PageDesc pageDesc) {
        JSONArray dataList = messageDeliveryDao.listPlanPushMsg(baseDao,queryParamsMap,pageDesc);
        return dataList;
    }



    /**
     * 取消定时发送
     * @param msgId 消息ID
     * @return String
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String changePushState(String msgId) {
        MessageDelivery msg = messageDeliveryDao.getObjectById(msgId);
        msg.setPushState("4");
        messageDeliveryDao.mergeObject(msg);
        return msgId;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseData pushAgain(String userCode, String osId) {
        List<MessageDelivery> msgList = messageDeliveryDao.listPushAgain(userCode,osId);
        for (MessageDelivery msg:msgList){
            try {
                pushMessage(msg);
            } catch (Exception e) {
               logger.error(e.getMessage(), e);
               //return ResponseData.makeErrorMessage(e.getMessage());
            }
        }
        return ResponseData.successResponse;
    }

    /**
     * 定时推送任务
     *
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void timerPusher() {
        List<MessageDelivery> list;
        do {
            list = messageDeliveryDao.listMsgNoPush(new PageDesc(1, 100));
            if (list != null && list.size() > 0) {
                for (MessageDelivery messageDelivery : list) {
                    try {
                        if (messageDelivery.getPushType().equals("0")){
                            pushMessage(messageDelivery);
                        }else if (messageDelivery.getPushType().equals("1")){
                            pushMsgToAll(messageDelivery);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } while (list.size() == 100);
    }
}


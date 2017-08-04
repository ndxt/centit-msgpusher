package com.centit.msgpusher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import com.centit.msgpusher.dao.MessageDeliveryDao;
import com.centit.msgpusher.dao.UserMsgPointDao;
import com.centit.msgpusher.dao.UserNotifySettingDao;
import com.centit.msgpusher.msgpusher.PushResult;
import com.centit.msgpusher.po.MessageDelivery;
import com.centit.msgpusher.po.UserMsgPoint;
import com.centit.msgpusher.po.UserMsgPointId;
import com.centit.msgpusher.service.MessageDeliveryManager;
import com.centit.msgpusher.service.MsgPusherCenter;
import com.centit.support.algorithm.DatetimeOpt;
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
 * MessageDelivery  Service.
 * create by scaffold 2017-04-07 
 * @author codefan@sina.com
 * 消息推送null   
*/
@Service
public class MessageDeliveryManagerImpl 
		extends BaseEntityManagerImpl<MessageDelivery,java.lang.String,MessageDeliveryDao>
	implements MessageDeliveryManager{

	public static final Logger logger = LoggerFactory.getLogger(MessageDeliveryManagerImpl.class);


	@Value("${msgpusher.msg.validtyPeriod}")
	private int validtyPeriod;

	@Resource(name="msgPusherCenter")
	private MsgPusherCenter msgPusherCenter;

	@Resource(name = "userMsgPointDao")
	private UserMsgPointDao userMsgPointDao ;

	@Resource(name = "userNotifySettingDao")
	private UserNotifySettingDao userNotifySettingDao ;


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
			
		return DictionaryMapUtils.objectsToJSONArray(
				baseDao.listObjects(filterMap, pageDesc), fields);
	}

	/**
	 * 点对点推送消息的方法
	 * @param msg
	 * @return 返回消息ID
	 */
	@Override
	@Transactional(propagation= Propagation.REQUIRED)
	public PushResult pushMessage(MessageDelivery msg){
		PushResult pushResult = new PushResult();
		if(msg.getMsgExpireSeconds() == null || msg.getMsgExpireSeconds() == 0){
			msg.setMsgExpireSeconds(3600 * 5);//消息默认过期时间
		}
		JSONObject json = new JSONObject();
		Date planPushTime = msg.getPlanPushTime();
		msg.setPushType("0");//点对点推送
		//如果指定为定时发送且发送时间大于当前时间则直接保存不发送
		if (planPushTime != null && planPushTime.after(DatetimeOpt.currentUtilDate())){
			msg.setPushState("0");
			messageDeliveryDao.saveNewObject(msg);
			return pushResult;
		}
		//如果未指定推送方式则查询设定的推送方式
		if (StringUtils.isBlank(msg.getNoticeTypes()) || "U".equals(msg.getNoticeTypes())){
			String notifySetting = userNotifySettingDao.getNotifyByUserCode(msg.getMsgReceiver(),msg.getOsId());
			msg.setNoticeTypes(notifySetting);
			//如果设定的推送方式也为空则返回为指定推送方式异常
			if(StringUtils.isBlank(notifySetting) || "U".equals(notifySetting)){
				return saveErrorPushResult("未指定推送方式",msg);
			}
		}
		//如果接收人不存在则返回接收人不存在
		UserMsgPoint userMsgPoint = userMsgPointDao.getObjectById(new UserMsgPointId(msg.getOsId(), msg.getMsgReceiver()));
		if (userMsgPoint == null){
			return saveErrorPushResult("接收人不存在",msg);
		}
		try {
			pushResult = msgPusherCenter.pushMessage(msg, userMsgPoint);
		} catch (Exception e) {
			logger.error("推送服务异常");
			return saveErrorPushResult("推送服务异常",msg);
		}
		//推送结果为空则返回推送服务异常
		if (pushResult == null){
			return saveErrorPushResult("推送方式不支持",msg);
		}
		msg.setPushState(pushResult.getPushState());
		if (!StringUtils.isBlank(pushResult.getMsgId()))
			json.put(MessageDelivery.NOTICE_TYPE_APP, pushResult.getMsgId());
		for (Map.Entry<String, String> entry : pushResult.getMap().entrySet()) {
			json.put(entry.getKey(),entry.getValue());
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
            messageDeliveryDao.saveObject(msg);
        }
		return pushResult;
	}

	/**
	 * 消息广播
	 *
	 * @param msg
	 * @return 返回消息ID
	 */
	@Override
	@Transactional(propagation= Propagation.REQUIRED)
	public PushResult pushMsgToAll(MessageDelivery msg){
		String notifyType = msg.getNoticeTypes();
		PushResult pushResult = new PushResult();
		if(msg.getMsgExpireSeconds() == null || msg.getMsgExpireSeconds() == 0){
			msg.setMsgExpireSeconds(3600 * 5);//消息默认过期时间
		}
		JSONObject json = new JSONObject();
		Date planPushTime = msg.getPlanPushTime();
		msg.setPushType("1");//群发
		//如果指定为定时发送且发送时间大于当前时间则直接保存不发送
		if(planPushTime != null || planPushTime.after(DatetimeOpt.currentUtilDate())){
			msg.setPushState("0");
			messageDeliveryDao.saveNewObject(msg);
			return pushResult;
		}
		//群发未指定推送方式则直接返回未指定推送方式异常
		if (StringUtils.isBlank(notifyType) || "U".equals(notifyType)) {
			return saveErrorPushResult("未指定推送方式",msg);
		}
		try {
			pushResult = msgPusherCenter.pushMsgToAll(msg);
		} catch (Exception e) {
			logger.error("推送服务异常");
			return saveErrorPushResult("推送服务异常",msg);
		}
		if (pushResult == null){
			return saveErrorPushResult("推送方式不支持",msg);
		}
		msg.setPushState(pushResult.getPushState());

		if (!StringUtils.isBlank(pushResult.getMsgId()))
			json.put("android", pushResult.getMsgId());
		if (!StringUtils.isBlank(pushResult.getMsgId2()))
			json.put("ios", pushResult.getMsgId2());
		for (Map.Entry<String, String> entry : pushResult.getMap().entrySet()) {
			json.put(entry.getKey(),entry.getValue());
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
            messageDeliveryDao.saveObject(msg);
        }
		return pushResult;
	}

	/**
	 * 时间向后推n天，n未负值表示向前推
	 * @param time
	 * @param n
	 * @return
	 */
	private Date changeDate(Date time,int n){
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(time);
		calendar.add(calendar.DATE, n);//把日期往后增加一天.整数往后推,负数往前移动
		return calendar.getTime();
	}

	/**
	 * 保存推送失败原因
	 * @param errorReason
	 * @param msg
	 * @return
	 */
	private PushResult saveErrorPushResult(String errorReason,MessageDelivery msg){
		JSONObject json = new JSONObject();
		PushResult pushResult = new PushResult();
		Map<String,String> resultMap = new HashMap<>();
		pushResult.setPushState("2");
		resultMap.put("error",errorReason);
		pushResult.setMap(resultMap);
		json.put("error",errorReason);
		msg.setPushResult(json.toString());
		msg.setPushState("2");
		messageDeliveryDao.saveNewObject(msg);
		return pushResult;
	}



	/**
	 * 这个维护型作业可以直接调用 delete 语句
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteRecords() {
		Date validDate = new Date(new Date().getTime()-validtyPeriod*86400000L);
		String hql = "delete from MessageDelivery m where m.pushTime<=?";
		DatabaseOptUtils.doExecuteHql(messageDeliveryDao,
				hql,new Object[]{validDate});
		/*List<MessageDelivery> msgList = messageDeliveryDao.listObjects(hql,new Object[]{validDate});
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
		String hql = "from MessageDelivery m where m.osId=? and m.optId=? and m.pushTime>? and m.pushTime<=?";
		List<MessageDelivery> msgList = messageDeliveryDao.listObjects(hql,new Object[]{osId, optId, pushTimeStart, pushTimeEnd});
		return msgList;
	}



	/**
	 * 查询出所有定时推送的消息记录
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public JSONArray listAllPlanPush(Map<String, Object> queryParamsMap, PageDesc pageDesc) {
		JSONArray dataList = messageDeliveryDao.listPlanPushMsg(baseDao,queryParamsMap,pageDesc);
		return dataList;
	}



	/**
	 * 取消定时发送
	 * @param msgId
	 * @return
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
	public PushResult pushAgain(String userCode,String osId) {
		PushResult result = new PushResult();
		List<MessageDelivery> msgList = messageDeliveryDao.listPushAgain(userCode,osId);
		for (MessageDelivery msg:msgList){
			try {
				result = pushMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 定时推送任务
	 * @return
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


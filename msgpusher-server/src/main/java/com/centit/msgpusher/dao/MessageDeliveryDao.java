package com.centit.msgpusher.dao;

import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.msgpusher.po.MessageDelivery;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;





/**
 * MessageDeliveryDao  Repository.
 * create by scaffold 2017-04-07 
 * @author codefan@sina.com
 * 消息推送null   
*/

@Repository
public class MessageDeliveryDao extends BaseDaoImpl<MessageDelivery,java.lang.String>
	{

	public static final Log log = LogFactory.getLog(MessageDeliveryDao.class);
	
	@Override
	public Map<String, String> getFilterField() {
		if( filterField == null){
			filterField = new HashMap<String, String>();

			filterField.put("notifyId" , CodeBook.EQUAL_HQL_ID);

			filterField.put("notifySender" , CodeBook.EQUAL_HQL_ID);

			filterField.put("notifyReceiver" , CodeBook.EQUAL_HQL_ID);

			filterField.put("msgSubject" , CodeBook.EQUAL_HQL_ID);

			filterField.put("msgContent" , CodeBook.EQUAL_HQL_ID);

			filterField.put("relUrl" , CodeBook.EQUAL_HQL_ID);

			filterField.put("noticeTypes" , CodeBook.EQUAL_HQL_ID);

			filterField.put("notifyState" , CodeBook.EQUAL_HQL_ID);

			filterField.put("errorMsg" , CodeBook.EQUAL_HQL_ID);

			filterField.put("planNotifyTime" , CodeBook.EQUAL_HQL_ID);

			filterField.put("notifyTime" , CodeBook.EQUAL_HQL_ID);

			filterField.put("osId" , CodeBook.EQUAL_HQL_ID);

			filterField.put("optId" , CodeBook.EQUAL_HQL_ID);

			filterField.put("optMethod" , CodeBook.EQUAL_HQL_ID);

			filterField.put("optTag" , CodeBook.EQUAL_HQL_ID);

		}
		return filterField;
	}

		/**
		 * 定时推送时根据推送类型查询当前需要推送的消息
		 * @return
		 */
	public List<MessageDelivery> listMsgNoPush(PageDesc pageDesc){
		Date currentDate = DatetimeOpt.currentUtilDate();
		return this.listObjects("From MessageDelivery f " +
				"where f.pushState='0' " +
						"and f.planPushTime < ? and (f.validPeriod is null or f.validPeriod > ?) ",
				new Object[]{currentDate,currentDate},pageDesc);
	}


		/**
		 * 查询出所有定时推送的消息记录
		 * @return
		 */
	public JSONArray listPlanPushMsg(BaseDaoImpl baseDao, Map<String, Object> queryParamsMap, PageDesc pageDesc){
		String queryStatement =
				"select h.msgSender, h.msgReceiver, h.pushType, h.pushState,h.planPushTime,h.pushTime,h.msgId"
						+" from MessageDelivery h WHERE 1=1 "
						+ " [ :osId | and h.osId = :osId ]"
						+ " [ :optId | and h.optId = :optId ]"
						+ " [ :pushState | and h.pushState = :pushState ]"
						+ " [ :begin | and h.createTime > :begin ]"
						+ " [ :end | and h.createTime < :end ]";
		QueryAndNamedParams qap = QueryUtils.translateQuery(queryStatement,queryParamsMap);
		JSONArray dataList = DictionaryMapUtils.objectsToJSONArray(
					DatabaseOptUtils.findObjectsByHql(baseDao,
							qap.getQuery(), qap.getParams(),pageDesc));
		return dataList;
	}

		/**
		 * 查询登录用户推送失败的消息
		 * @return
		 */
		public List<MessageDelivery> listPushAgain(String userCode,String osId){
			Date currentDate = DatetimeOpt.currentUtilDate();
			String hql = "From MessageDelivery f " +
					"WHERE f.osId =? AND f.msgReceiver=? AND f.pushState='2' " +
						"AND  f.planPushTime < ? and (f.validPeriod is null or f.validPeriod > ?) ";
			List<MessageDelivery> msgList = this.listObjects(hql,
					new Object[]{osId, userCode, currentDate,currentDate});
			return msgList;
		}



}

package com.centit.msgpusher.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.msgpusher.po.UserMsgPoint;
import com.centit.msgpusher.po.UserMsgPointId;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



/**
 * UserMsgPointDao  Repository.
 * create by scaffold 2017-04-07 
 * @author codefan@sina.com
 * 用户消息接收端口信息用户设置 自己接收 通知的方式。   
*/

@Repository
public class UserMsgPointDao extends BaseDaoImpl<UserMsgPoint,UserMsgPointId>
	{

	public static final Log log = LogFactory.getLog(UserMsgPointDao.class);
	
	@Override
	public Map<String, String> getFilterField() {
		if( filterField == null){
			filterField = new HashMap<String, String>();

			filterField.put("userCode" , CodeBook.EQUAL_HQL_ID);

			filterField.put("osId" , CodeBook.EQUAL_HQL_ID);


			filterField.put("androidToken" , CodeBook.EQUAL_HQL_ID);

			filterField.put("iosToken" , CodeBook.EQUAL_HQL_ID);

			filterField.put("pcHost" , CodeBook.EQUAL_HQL_ID);

			filterField.put("mobilePhone" , CodeBook.EQUAL_HQL_ID);

			filterField.put("wxToken" , CodeBook.EQUAL_HQL_ID);

		}
		return filterField;
	}

	public List<String> listAllEmailAddress(){
		String sql = " SELECT f.EMAIL_ADDRESS FROM f_user_msg_point f WHERE f.EMAIL_ADDRESS IS NOT NULL ";
		List<Object[]> l = (List<Object[]>) DatabaseOptUtils.findObjectsBySql(this, sql);
		if(l==null)
			return null;
		List<String> list = new ArrayList<>();
		for (int i = 0;i<l.size();i++){
			String address = l.get(i).toString();
			list.add(address);
		}
		return list;
	}


}

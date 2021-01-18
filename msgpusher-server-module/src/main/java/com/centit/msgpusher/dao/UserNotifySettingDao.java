package com.centit.msgpusher.dao;

import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.msgpusher.po.UserNotifySetting;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;



/**
 * UserNotifySettingDao  Repository.
 * create by scaffold 2017-04-07
 * @author codefan@sina.com
 * 用户通知接受参数设置用户设置 自己接收 通知的方式。
*/

@Repository
public class UserNotifySettingDao extends BaseDaoImpl<UserNotifySetting,String>
    {

    public static final Log log = LogFactory.getLog(UserNotifySettingDao.class);

    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("userSettingId" , CodeBook.EQUAL_HQL_ID);
        filterField.put("userCode" , CodeBook.EQUAL_HQL_ID);
        filterField.put("osId" , CodeBook.EQUAL_HQL_ID);
        filterField.put("optId" , CodeBook.EQUAL_HQL_ID);
        filterField.put("notifyTypes" , CodeBook.EQUAL_HQL_ID);
        return filterField;
    }


    //根据userCode和osid查询用户设置的消息接收方式
    public String getNotifyByUserCode(String userCode,String osId){
        Map<String, Object> map = new HashMap<>();
        map.put("userCode",userCode);
        map.put("osId",osId);
        UserNotifySetting notifyType = this.getObjectByProperties(map);
        return notifyType.getNotifyTypes();
    }
}

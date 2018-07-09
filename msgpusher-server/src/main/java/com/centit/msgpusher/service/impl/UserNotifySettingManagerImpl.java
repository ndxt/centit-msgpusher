package com.centit.msgpusher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.core.dao.QueryParameterPrepare;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.msgpusher.dao.UserNotifySettingDao;
import com.centit.msgpusher.po.UserNotifySetting;
import com.centit.msgpusher.service.UserNotifySettingManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * UserNotifySetting  Service.
 * create by scaffold 2017-04-07
 * @author codefan@sina.com
 * 用户通知接受参数设置用户设置 自己接收 通知的方式。
*/
@Service
public class UserNotifySettingManagerImpl
        extends BaseEntityManagerImpl<UserNotifySetting,String,UserNotifySettingDao>
    implements UserNotifySettingManager{

    //public static final Logger logger = LoggerFactory.getLogger(UserNotifySettingManager.class);


    private UserNotifySettingDao userNotifySettingDao ;

    @Resource(name = "userNotifySettingDao")
    @NotNull
    public void setUserNotifySettingDao(UserNotifySettingDao baseDao)
    {
        this.userNotifySettingDao = baseDao;
        setBaseDao(this.userNotifySettingDao);
    }

/*
     @PostConstruct
    public void init() {

    }

 */
    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public JSONArray listUserNotifySettingsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc){

        return DictionaryMapUtils.objectsToJSONArray(
            baseDao.pageQuery(QueryParameterPrepare.makeMybatisOrderByParam(
                QueryParameterPrepare.prepPageParams(filterMap,pageDesc,
                    baseDao.pageCount(filterMap)),UserNotifySetting.class)), fields);
    }

}


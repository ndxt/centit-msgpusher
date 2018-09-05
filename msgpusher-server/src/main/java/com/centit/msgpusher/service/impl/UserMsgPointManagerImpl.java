package com.centit.msgpusher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.DictionaryMapUtils;
import com.centit.support.database.utils.PageDesc;
import com.centit.framework.core.dao.QueryParameterPrepare;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.msgpusher.dao.UserMsgPointDao;
import com.centit.msgpusher.po.UserMsgPoint;
import com.centit.msgpusher.po.UserMsgPointId;
import com.centit.msgpusher.service.UserMsgPointManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * IPushMsgPoint  Service.
 * create by scaffold 2017-04-07
 * @author codefan@sina.com
 * 用户消息接收端口信息用户设置 自己接收 通知的方式。
*/
@Service
public class UserMsgPointManagerImpl
        extends BaseEntityManagerImpl<UserMsgPoint,UserMsgPointId,UserMsgPointDao>
    implements UserMsgPointManager{

    //public static final Logger logger = LoggerFactory.getLogger(UserMsgPointManager.class);

    private UserMsgPointDao userMsgPointDao ;

    @Resource(name = "userMsgPointDao")
    @NotNull
    public void setUserMsgPointDao(UserMsgPointDao baseDao)
    {
        this.userMsgPointDao = baseDao;
        setBaseDao(this.userMsgPointDao);
    }

/*
     @PostConstruct
    public void init() {

    }

 */
    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public JSONArray listUserMsgPointsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc){

        return DictionaryMapUtils.objectsToJSONArray(
            baseDao.pageQuery(QueryParameterPrepare.makeMybatisOrderByParam(
                QueryParameterPrepare.prepPageParams(filterMap,pageDesc,
                    baseDao.pageCount(filterMap)),UserMsgPoint.class)), fields);
    }

    @Override
    public UserMsgPoint getUserMsgPoint(String osId,String receiver){
        UserMsgPoint userMsgPoint = userMsgPointDao.getObjectById(new UserMsgPointId(osId,receiver));
        return userMsgPoint;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void registerUserPoint(UserMsgPoint userMsgPoint) {
        UserMsgPoint userMsgPointOld = userMsgPointDao.getObjectById(new UserMsgPointId(userMsgPoint.getOsId(),userMsgPoint.getUserCode()));
        String deviceType = userMsgPoint.getDeviceType();
        String wXToken = userMsgPoint.getWxToken();
        String phoneNo = userMsgPoint.getMobilePhone();
        String emailAddress = userMsgPoint.getEmailAddress();
        if (userMsgPointOld == null){
            userMsgPointDao.saveNewObject(userMsgPoint);
            return;
        }else{
            if ("0".equals(deviceType)){
                userMsgPoint.setDeviceType(userMsgPointOld.getDeviceType());
                userMsgPoint.setChannelId(userMsgPointOld.getChannelId());
            }
            if(wXToken == null || "".equals(wXToken)) {
                userMsgPoint.setWxToken(userMsgPointOld.getWxToken());
            }
            if (phoneNo == null || "".equals(phoneNo)){
                userMsgPoint.setMobilePhone(userMsgPointOld.getMobilePhone());
            }
            if (emailAddress == null || "".equals(emailAddress)){
                userMsgPoint.setEmailAddress(userMsgPointOld.getEmailAddress());
            }
        }
        userMsgPointDao.mergeObject(userMsgPoint);
    }

}


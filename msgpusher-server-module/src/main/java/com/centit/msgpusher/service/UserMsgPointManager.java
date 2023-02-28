package com.centit.msgpusher.service;

import com.alibaba.fastjson2.JSONArray;
import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.msgpusher.po.UserMsgPoint;
import com.centit.support.database.utils.PageDesc;

import java.util.Map;

/**
 * IPushMsgPoint  Service.
 * create by scaffold 2017-04-07
 * @author codefan@sina.com
 * 用户消息接收端口信息用户设置 自己接收 通知的方式。
*/

public interface UserMsgPointManager extends BaseEntityManager<UserMsgPoint, String>
{

    JSONArray listUserMsgPointsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);

    UserMsgPoint getUserMsgPoint(String osId,String receiver);

    void registerUserPoint(UserMsgPoint userMsgPoint);
}

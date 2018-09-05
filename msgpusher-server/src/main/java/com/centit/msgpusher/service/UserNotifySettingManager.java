package com.centit.msgpusher.service;

import com.alibaba.fastjson.JSONArray;
import com.centit.support.database.utils.PageDesc;
import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.msgpusher.po.UserNotifySetting;

import java.util.Map;

/**
 * UserNotifySetting  Service.
 * create by scaffold 2017-04-07
 * @author codefan@sina.com
 * 用户通知接受参数设置用户设置 自己接收 通知的方式。
*/

public interface UserNotifySettingManager extends BaseEntityManager<UserNotifySetting,String>
{

    JSONArray listUserNotifySettingsAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);

}

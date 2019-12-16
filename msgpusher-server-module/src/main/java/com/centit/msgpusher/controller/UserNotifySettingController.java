package com.centit.msgpusher.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.msgpusher.po.UserNotifySetting;
import com.centit.msgpusher.service.UserNotifySettingManager;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;
/**
 * UserNotifySetting  Controller.
 * create by scaffold 2017-04-07
 * @author codefan@sina.com
 * 用户通知接受参数设置用户设置 自己接收 通知的方式。
*/


@Controller
@RequestMapping("/notifysetting")
public class UserNotifySettingController  extends BaseController {
    private static final Log log = LogFactory.getLog(UserNotifySettingController.class);

    @Resource
    private UserNotifySettingManager userNotifySettingMag;
    /*public void setUserNotifySettingMag(UserNotifySettingManager basemgr)
    {
        userNotifySettingMag = basemgr;
        //this.setBaseEntityManager(userNotifySettingMag);
    }*/

    /**
     * 查询所有   用户通知接受参数设置  列表
     *
     * @param field  json中只保存需要的属性名
     * @param pageDesc 分页
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = BaseController.collectRequestParameters(request);
        JSONArray listObjects = userNotifySettingMag.listUserNotifySettingsAsJson(field,searchColumn, pageDesc);
        if (null == pageDesc) {
            JsonResultUtils.writeSingleDataJson(listObjects, response);
            return;
        }

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(BaseController.OBJLIST, listObjects);
        resData.addResponseData(BaseController.PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 查询单个  用户通知接受参数设置

     * @param userSettingId  USER_SETTING_ID
     * @param response  HttpServletResponse
     */
    @RequestMapping(value = "/{userSettingId}", method = {RequestMethod.GET})
    public void getUserNotifySetting(@PathVariable String userSettingId, HttpServletResponse response) {

        UserNotifySetting userNotifySetting =
                userNotifySettingMag.getObjectById( userSettingId);

        JsonResultUtils.writeSingleDataJson(userNotifySetting, response);
    }

    /**
     * 新增 用户通知接受参数设置
     *
     * @param userNotifySetting UserNotifySetting
     * @param response  HttpServletResponse
     */
    @RequestMapping(method = {RequestMethod.POST})
    public void createUserNotifySetting(@RequestBody @Valid UserNotifySetting userNotifySetting, HttpServletResponse response) {
        userNotifySettingMag.saveNewObject(userNotifySetting);
        JsonResultUtils.writeSingleDataJson(
                userNotifySetting.getUserSettingId(),response);
    }

    /**
     * 删除单个  用户通知接受参数设置

     * @param userSettingId  USER_SETTING_ID
     * @param response  HttpServletResponse
     */
    @RequestMapping(value = "/{userSettingId}", method = {RequestMethod.DELETE})
    public void deleteUserNotifySetting(@PathVariable String userSettingId, HttpServletResponse response) {

        userNotifySettingMag.deleteObjectById( userSettingId);
        JsonResultUtils.writeBlankJson(response);
    }

    /**
     * 新增或保存 用户通知接受参数设置

     * @param userSettingId  USER_SETTING_ID
     * @param userNotifySetting  {@link UserNotifySetting}
     * @param response    {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{userSettingId}", method = {RequestMethod.PUT})
    public void updateUserNotifySetting(@PathVariable String userSettingId,
        @RequestBody @Valid UserNotifySetting userNotifySetting, HttpServletResponse response) {
        UserNotifySetting dbUserNotifySetting  =
                userNotifySettingMag.getObjectById( userSettingId);

        if (null != userNotifySetting) {
            userNotifySettingMag.mergeObject(userNotifySetting);
        } else {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        JsonResultUtils.writeBlankJson(response);
    }
}

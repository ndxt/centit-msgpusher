package com.centit.msgpusher.controller;

import com.centit.msgpusher.po.UserNotifySetting;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.centit.msgpusher.service.UserNotifySettingManager;
	

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
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
     * @param field    json中只保存需要的属性名
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {data:[]}
     */
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);        
        
        JSONArray listObjects = userNotifySettingMag.listUserNotifySettingsAsJson(field,searchColumn, pageDesc);

        if (null == pageDesc) {
            JsonResultUtils.writeSingleDataJson(listObjects, response);
            return;
        }
        
        ResponseData resData = new ResponseData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }
    
    /**
     * 查询单个  用户通知接受参数设置 
	
	 * @param userSettingId  USER_SETTING_ID
     *
     * @param response    {@link HttpServletResponse}
     * @return {data:{}}
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
     * @param userNotifySetting  {@link UserNotifySetting}
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST})
    public void createUserNotifySetting(@RequestBody @Valid UserNotifySetting userNotifySetting, HttpServletResponse response) {
    	Serializable pk = userNotifySettingMag.saveNewObject(userNotifySetting);
        JsonResultUtils.writeSingleDataJson(pk,response);
    }

    /**
     * 删除单个  用户通知接受参数设置 
	
	 * @param userSettingId  USER_SETTING_ID
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
        	dbUserNotifySetting .copy(userNotifySetting);
        	userNotifySettingMag.mergeObject(dbUserNotifySetting);
        } else {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        JsonResultUtils.writeBlankJson(response);
    }
}

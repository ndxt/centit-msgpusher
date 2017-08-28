package com.centit.msgpusher.controller;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.msgpusher.po.UserMsgPoint;
import com.centit.msgpusher.po.UserMsgPointId;
import com.centit.msgpusher.service.UserMsgPointManager;
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
import java.io.Serializable;
import java.util.Map;
/**
 * UserMsgPoint  Controller.
 * create by scaffold 2017-04-07 
 * @author codefan@sina.com
 * 用户消息接收端口信息用户设置 自己接收 通知的方式。   
*/


@Controller
@RequestMapping("/userpoint")
public class UserMsgPointController  extends BaseController {
	private static final Log log = LogFactory.getLog(UserMsgPointController.class);
	
	@Resource
	private UserMsgPointManager userMsgPointMag;	
	/*public void setUserMsgPointMag(UserMsgPointManager basemgr)
	{
		userMsgPointMag = basemgr;
		//this.setBaseEntityManager(userMsgPointMag);
	}*/

    /**
     * 查询所有   用户消息接收端口信息  列表
     *
     * @param field    json中只保存需要的属性名
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {data:[]}
     */
    @RequestMapping(method = RequestMethod.GET)
    public void list(String[] field, PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);        
        
        JSONArray listObjects = userMsgPointMag.listUserMsgPointsAsJson(field,searchColumn, pageDesc);

        if (null == pageDesc) {
            JsonResultUtils.writeSingleDataJson(listObjects, response);
            return;
        }

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }
    
    /**
     * 查询单个  用户消息接收端口信息 
	
	 * @param userCode  User_Code
	 * @param osId  OS_ID
     *
     * @param response    {@link HttpServletResponse}
     * @return {data:{}}
     */
    @RequestMapping(value = "/{userCode}/{osId}", method = {RequestMethod.GET})
    public void getUserMsgPoint(@PathVariable String userCode,@PathVariable String osId, HttpServletResponse response) {
    	
    	UserMsgPoint userMsgPoint =
    			userMsgPointMag.getObjectById(new UserMsgPointId(  userCode, osId) );
    	
        JsonResultUtils.writeSingleDataJson(userMsgPoint, response);
    }
    
    /**
     * 新增 用户消息接收端口信息
     *
     * @param userMsgPoint  {@link UserMsgPoint}
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST})
    public void createUserMsgPoint(@RequestBody @Valid UserMsgPoint userMsgPoint, HttpServletResponse response) {
    	userMsgPointMag.saveNewObject(userMsgPoint);
        JsonResultUtils.writeSingleDataJson(userMsgPoint.getCid(),response);
    }

    /**
     * 删除单个  用户消息接收端口信息 
	
	 * @param userCode  User_Code
	 * @param osId  OS_ID
     */
    @RequestMapping(value = "/{userCode}/{osId}", method = {RequestMethod.DELETE})
    public void deleteUserMsgPoint(@PathVariable String userCode,@PathVariable String osId, HttpServletResponse response) {
    	
    	userMsgPointMag.deleteObjectById(new UserMsgPointId(  userCode, osId) );
    	
        JsonResultUtils.writeBlankJson(response);
    } 
    
    /**
     * 新增或保存 用户消息接收端口信息 
    
	 * @param userCode  User_Code
	 * @param osId  OS_ID
	 * @param userMsgPoint  {@link UserMsgPoint}
     * @param response    {@link HttpServletResponse}
     */
    @RequestMapping(value = "/{userCode}/{osId}", method = {RequestMethod.PUT})
    public void updateUserMsgPoint(@PathVariable String userCode,@PathVariable String osId, 
    	@RequestBody @Valid UserMsgPoint userMsgPoint, HttpServletResponse response) {
    	
    	
    	UserMsgPoint dbUserMsgPoint =     			
    			userMsgPointMag.getObjectById(new UserMsgPointId(  userCode, osId) );
    	
        

        if (null != userMsgPoint) {
        	dbUserMsgPoint .copy(userMsgPoint);
        	userMsgPointMag.mergeObject(dbUserMsgPoint);
        } else {
            JsonResultUtils.writeErrorMessageJson("当前对象不存在", response);
            return;
        }

        JsonResultUtils.writeBlankJson(response);
    }
}

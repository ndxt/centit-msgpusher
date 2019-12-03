package com.centit.msgpusher.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.msgpusher.commons.PushResult;
import com.centit.msgpusher.po.MessageDelivery;
import com.centit.msgpusher.po.UserMsgPoint;
import com.centit.msgpusher.service.MessageDeliveryManager;
import com.centit.msgpusher.service.UserMsgPointManager;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * IPushMessage  Controller.
 * create by scaffold 2017-04-07
 * @author codefan@sina.com
 * 消息推送null
*/


@Controller
@RequestMapping("/msgdlvry")
public class MessageDeliveryController  extends BaseController {
    private static final Log log = LogFactory.getLog(MessageDeliveryController.class);

    @Resource
    private MessageDeliveryManager messageDeliveryManager;

    @Resource
    private UserMsgPointManager userMsgPointManager;


    private MessageDelivery
        fetchMessageDelivery(HttpServletRequest request) throws IOException {

        String contentType = request.getContentType();
        if(StringUtils.indexOf(contentType,"form")>0){
            Map<String, String[]>  params = request.getParameterMap();
            Map<String, String> objMap = new HashMap<>();
            for(Map.Entry<String, String[]> ent : params.entrySet()){
                if(ent.getValue()!=null && ent.getValue().length>0)
                    objMap.put(ent.getKey(),ent.getValue()[0]);
            }
            MessageDelivery msg = JSON.parseObject( JSON.toJSONString(objMap),MessageDelivery.class);
            return msg;
        }else if(StringUtils.indexOf(contentType,"json")>0){
            MessageDelivery msg = JSON.parseObject(request.getInputStream(),MessageDelivery.class);
            return msg;
        }
        return null;
    }

    private UserMsgPoint fetchUserMsgPoint(HttpServletRequest request) throws IOException {
        String contentType = request.getContentType();
        if(StringUtils.indexOf(contentType,"form")>0){
            Map<String, String[]>  params = request.getParameterMap();
            Map<String, String> objMap = new HashMap<>();
            for(Map.Entry<String, String[]> ent : params.entrySet()){
                if(ent.getValue()!=null && ent.getValue().length>0)
                    objMap.put(ent.getKey(),ent.getValue()[0]);
            }
            UserMsgPoint userMsgPoint = JSON.parseObject( JSON.toJSONString(objMap),UserMsgPoint.class);
            return userMsgPoint;
        }else if(StringUtils.indexOf(contentType,"json")>0){
            UserMsgPoint userMsgPoint = JSON.parseObject(request.getInputStream(),UserMsgPoint.class);
            return userMsgPoint;
        }
        return null;
    }


    /**
     * 需要有一下函数
     *
     * 1. 注册用户信息（用户登录后 在这个服务上注册 用户的设备信息， 如果是移动端 注册用户的channel id）
     * 2. 推送消息
     * 3. 广播消息
     * 4. 查看消息状态
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException IOException
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void registerUserMsgPoint(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserMsgPoint userMsgPoint = fetchUserMsgPoint(request);
        if(userMsgPoint==null){
            JsonResultUtils.writeErrorMessageJson(400,"表单参数错误",response);
            return;
        }
//        userMsgPointManager.saveNewObject(userMsgPoint);
        userMsgPointManager.registerUserPoint(userMsgPoint);
        //重新推送消息有效期内推送失败的消息
        PushResult result = messageDeliveryManager.pushAgain(userMsgPoint.getUserCode(),userMsgPoint.getOsId());

        JsonResultUtils.writeSingleDataJson(userMsgPoint,response);

        //TODO 添加 消息推送 PUSH_State='2' and valid_period >= 当前时间

    }



    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public void pushMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MessageDelivery msg = fetchMessageDelivery(request);
        PushResult result = null;
        if(msg==null){
            JsonResultUtils.writeErrorMessageJson(400,"表单参数错误",response);
            return;
        }
        try {
            result = messageDeliveryManager.pushMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonResultUtils.writeSingleDataJson(result,response);
    }


    @RequestMapping(value = "/pushall", method = RequestMethod.POST)
    public void pushMessageToAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MessageDelivery msg = fetchMessageDelivery(request);
        PushResult result = null;
        if(msg==null){
            JsonResultUtils.writeErrorMessageJson(400,"表单参数错误",response);
            return;
        }
        try {
            result = messageDeliveryManager.pushMsgToAll(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonResultUtils.writeSingleDataJson(result,response);
    }

    @RequestMapping(value="/delete", method = RequestMethod.POST)
    public void deleteRecords( HttpServletResponse response) throws IOException {
//        int result = messageDeliveryManager.deleteRecords();
        messageDeliveryManager.deleteRecords();
        JsonResultUtils.writeSingleDataJson("删除了记录。", response);
    }

    @RequestMapping(value="/view", method=RequestMethod.GET)
    public void viewRecords(PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> queryParamsMap = collectRequestParameters(request);
        JSONArray listObjects = messageDeliveryManager.listMessageDeliverysAsJson(
                null,queryParamsMap, pageDesc);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);

    }

    /**
     * 查询出所有定时推送的消息记录
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param pageDesc 分页
     * @throws IOException IOException
     */
    @RequestMapping(value="/listAllPlanPush", method = RequestMethod.GET)
    public void listAllPlanPush(HttpServletRequest request, HttpServletResponse response,PageDesc pageDesc) throws IOException {
        Map<String, Object> map = new HashMap<>();
        String osId = request.getParameter("osId");
        String optId = request.getParameter("optId");
        String pushState = request.getParameter("pushState");
        Date begin = DatetimeOpt.convertStringToDate(request.getParameter("begin"),"yyyy-MM-dd HH:mm:ss");
        Date end = DatetimeOpt.convertStringToDate(request.getParameter("end"),"yyyy-MM-dd HH:mm:ss");
        map.put("osId",osId);
        map.put("optId",optId);
        map.put("pushState",(StringUtils.isBlank(pushState)?"0":pushState));
        map.put("begin",begin);
        map.put("end",end);
        JSONArray listObjects = messageDeliveryManager.listAllPlanPush(map, pageDesc);
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    /**
     * 取消定时发送
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param msgId 消息ID
     * @throws IOException IOException
     */
    @RequestMapping(value="/cancelPlanPush", method = RequestMethod.POST)
    public void cancelPlanPush(HttpServletRequest request, HttpServletResponse response,@RequestParam("msgId" ) String msgId) throws IOException {
        messageDeliveryManager.changePushState(msgId);
    }

}

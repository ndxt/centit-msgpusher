package com.centit.msgpusher.service;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.msgpusher.commons.PushResult;
import com.centit.msgpusher.po.MessageDelivery;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * IPushMessage  Service.
 * create by scaffold 2017-04-07
 * @author codefan@sina.com
 * 消息推送null
*/

public interface MessageDeliveryManager extends BaseEntityManager<MessageDelivery,String>
{

    JSONArray listMessageDeliverysAsJson(
            String[] fields,
            Map<String, Object> filterMap, PageDesc pageDesc);

    /**
     *
     * 点对点推送消息的方法
     * @param msg 发送人内部用户编码
     * @return 返回消息ID
     * @throws Exception Exception
     */
    PushResult pushMessage(MessageDelivery msg)throws Exception;

    /**
     * 消息广播
     * @param msg 发送人内部用户编码
     * @return 返回消息ID
     * @throws Exception Exception
     */
    PushResult pushMsgToAll(MessageDelivery msg) throws Exception;


    void deleteRecords();

    List<MessageDelivery> viewRecords(String osId, String optId, Date pushTimeStart, Date pushTimeEnd);

    /**
     * 查询出所有定时推送的消息记录
     * @param queryParamsMap 查询参数
     * @param pageDesc 分页
     * @return JSONArray
     */
    JSONArray listAllPlanPush(Map<String, Object> queryParamsMap, PageDesc pageDesc);


    /**
     * 取消定时发送
     * @param msgId 消息ID
     * @return String
     */
    String changePushState(String msgId);

    PushResult pushAgain(String userCode,String osId);

    /**
     * 定时推送任务
     *
     */
    void timerPusher();
}

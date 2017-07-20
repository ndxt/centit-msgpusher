package com.centit.msgpusher.client;

import com.centit.msgpusher.client.po.MessageDelivery;
import com.centit.msgpusher.client.po.PushResult;
import com.centit.msgpusher.client.po.UserMsgPoint;
import com.centit.msgpusher.client.po.UserNotifySetting;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Created by codefan on 17-4-11.
 */
public interface MsgPusherClient {

    /**
     * 注册用户信息，
     *
     * 在用户登录后 调用这个方法在消息推送中心注册用户信息，
     * 1, pc端登录后在 登录成功式 调用这个接口
     * 2, 移动端登录成够后，链接百度推送获取推送标识channel id，这个会传送到服务端，服务端通过这个接口保存到推送服务器
     *
     * 这个其实并不是必须的，也可以在客户端直接调用消息推送中心的接口，这个将是我们推荐的做法，这样业务系统更本不要管
     * 消息服务的运行机制，它只管直接调用消息推送接口就可以了。
     *
     */
    String registerUser(CloseableHttpClient httpClient, UserMsgPoint msgPoint) throws Exception;

    String registerUser(UserMsgPoint msgPoint) throws Exception;

    /**
     * 注册用户信息
     * @param userCode 用户代码
     * @param osId 业务系统ID
     */
    String registerUser(String userCode, String osId) throws Exception;

    /**
     * 移动用户注册
     * @param userCode 用户代码
     * @param osId 业务系统ID
     * @param deviceId 设备标识码
     * @param deviceType 移动设备类型 3 ：android， 4：apple，0：没有
     * @param channelId 百度推送通道
     */
    String registerUser(String userCode, String osId, String deviceId, String deviceType, String channelId) throws Exception;

    /**
     * 邮箱用户注册
     * @param userCode 用户代码
     * @param osId 业务系统ID
     * @param emailAddress 邮箱地址
     */
    String registerUserEmail(String userCode, String osId, String emailAddress) throws Exception;

    /**
     * 短信用户注册
     * @param userCode 用户代码
     * @param osId 业务系统ID
     * @param mobilePhone 移动电话
     */
    String registerUserPhone(String userCode, String osId, String mobilePhone) throws Exception;

    /**
     * 微信用户注册
     * @param userCode 用户代码
     * @param osId 业务系统ID
     * @param wxToken 微信令牌 公众号下的令牌
     */
    String registerUserWeChat(String userCode, String osId, String wxToken) throws Exception;

    /**
     * 设置接收通知方式
     */
    String userNotifySetting(CloseableHttpClient httpClient, UserNotifySetting notifySetting) throws Exception;
    String userNotifySetting(UserNotifySetting notifySetting) throws Exception;

    /**
     * 用户设置接收通知方式
     * @param userCode 用户代码
     * @param osId 业务系统ID
     * @param notifyTypes 接收通知方式 可以多种方式  A：app推送， E: email推送，S：短信  C：微信  N：内部通知系统 U:未指定
     *                      格式：Json数组字符串：[{'notifyTypes':'A'},{'notifyTypes':'E'}]
     */
    String userNotifySetting(String userCode, String osId, String notifyTypes) throws Exception;

    /**
     * 点对点的消息推送接口，一般发送方均为系统 admin或者 system；如果系统中事件是由某个人发起的发送方也可以是某个具体的人
     */
    PushResult pushMessage(MessageDelivery msgdlvry) throws Exception;

    PushResult pushMessage(CloseableHttpClient httpClient, MessageDelivery msgdlvry) throws Exception;

    /**
     * 消息点对点推送
     * @param userCode 收件人
     * @param title 消息主题
     * @param message 消息内容
     * @param optId 业务项目模块 模块，或者表
     * @param osId 业务系统ID
     * @param msgSender 发送人
     */
    PushResult pushAppMessage(String userCode, String title , String message, String osId, String optId, String msgSender) throws Exception;

    /**
     * 指定类型的消息点对点推送，目前支持的推送方式有 app系统推送 email邮件推送 sms 短线推送 wx推送 msg 内置消息箱
     * @param userCode 收件人
     * @param title 消息主题
     * @param message 消息内容
     * @param noticeTypes 通知方式 可以多种方式  A：app推送， E: email推送，S：短信  C：微信  N：内部通知系统 U:未指定
     *                      格式：Json数组：[{'notifyTypes':'A'},{'notifyTypes':'E'}]
     * @param optId 业务项目模块 模块，或者表
     * @param osId 业务系统ID
     * @param msgSender 发送人
     */
    PushResult pushMessage(String userCode, String title, String message, String noticeTypes, String osId, String optId, String msgSender) throws Exception;

    /**
     * 消息广播
     */
    PushResult pushMsgToAll(CloseableHttpClient httpClient, MessageDelivery msgdlvry) throws Exception;

    PushResult pushMsgToAll(MessageDelivery msgdlvry) throws Exception;

    /**
     * 消息广播
     * @param title 消息主题
     * @param message 消息内容
     * @param osId 业务系统ID
     * @param optId 业务项目模块 模块，或者表
     * @param msgSender 发送人
     */
    PushResult pushAppMsgToAll(String title , String message, String osId, String optId, String msgSender) throws Exception;

    /**
     * 指定类型的消息广播
     * @param title 消息主题
     * @param message 消息内容
     * @param osId 业务系统ID
     * @param optId 业务项目模块 模块，或者表
     * @param noticeTypes 通知方式 可以多种方式  A：app推送， E: email推送，S：短信  C：微信  N：内部通知系统 U:未指定
     *                      格式：Json数组：[{'notifyTypes':'A'},{'notifyTypes':'E'}]
     * @param msgSender 发送人
     */
    PushResult pushMsgToAll(String title , String message, String noticeTypes, String osId, String optId, String msgSender) throws Exception;

}

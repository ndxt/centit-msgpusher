package com.centit.msgpusher.client;

import com.centit.framework.model.adapter.MessageSender;
import com.centit.msgpusher.client.po.UserMsgPoint;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Created by codefan on 17-4-11.
 */
public interface MsgPusherClient extends MessageSender {

    /**
     * 注册用户信息
     *
     * 在用户登录后 调用这个方法在消息推送中心注册用户信息
     * 1, pc端登录后在 登录成功式 调用这个接口
     * 2, 移动端登录成够后，链接百度推送获取推送标识channel id，这个会传送到服务端，服务端通过这个接口保存到推送服务器
     *
     * 这个其实并不是必须的，也可以在客户端直接调用消息推送中心的接口，这个将是我们推荐的做法，这样业务系统更本不要管
     * 消息服务的运行机制，它只管直接调用消息推送接口就可以了。
     * @param httpClient CloseableHttpClient
     * @param msgPoint 用户消息接收端口
     * @return 注册的用户
     * @throws Exception Exception
     */
    String registerUser(CloseableHttpClient httpClient, UserMsgPoint msgPoint) throws Exception;

    String registerUser(UserMsgPoint msgPoint) throws Exception;

    /**
     *
     * 注册用户信息
     * @param userCode 用户代码
     * @param osId 业务系统ID
     * @return String
     * @throws Exception Exception
     */
    String registerUser(String userCode, String osId) throws Exception;

    /**
     * 移动用户注册
     * @param userCode 用户代码
     * @param osId 业务系统ID
     * @param deviceId 设备标识码
     * @param deviceType 移动设备类型 3 ：android， 4：apple，0：没有
     * @param channelId 百度推送通道
     * @return String
     * @throws Exception Exception
     */
    String registerUser(String userCode, String osId, String deviceId, String deviceType, String channelId) throws Exception;

    /**
     * 邮箱用户注册
     * @param userCode 用户代码
     * @param osId 业务系统ID
     * @param emailAddress 邮箱地址
     * @return String
     * @throws Exception Exception
     */
    String registerUserEmail(String userCode, String osId, String emailAddress) throws Exception;

    /**
     * 短信用户注册
     * @param userCode 用户代码
     * @param osId 业务系统ID
     * @param mobilePhone 移动电话
     * @return String
     * @throws Exception Exception
     */
    String registerUserPhone(String userCode, String osId, String mobilePhone) throws Exception;

    /**
     * 微信用户注册
     * @param userCode 用户代码
     * @param osId 业务系统ID
     * @param wxToken 微信令牌 公众号下的令牌
     * @return String
     * @throws Exception Exception
     */
    String registerUserWeChat(String userCode, String osId, String wxToken) throws Exception;


}

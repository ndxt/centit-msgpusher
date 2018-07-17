package com.centit.msgpusher.po;

/**
 * create by scaffold 2017-04-10
 * @author codefan@sina.com

  用户消息接收端口（设备）描述。
*/

public interface IPushMsgPoint {

    String getOsId();

    /**
     * 用户代码
     */
    String getUserCode();

    /**
     * 用户名
     */
    String getUserName();

    /**
     * 移动设备类型
     */
    String getDeviceType();

    /**
     * 设备标识码
     */
    String getChannelId();

    /**
     * 邮箱
     */
    String getEmailAddress();

    /**
     * 手机号
     */
    String getMobilePhone();

    /**
     * 微信
     */
    String getWxToken();
}

package com.centit.msgpusher.msgpusher.po;

/**
 * create by scaffold 2017-04-10 
 * @author codefan@sina.com
 
  用户消息接收端口（设备）描述。
*/

public interface IPushMsgPoint {

    String getOsId();

    String getUserCode();

    String getUserName();

    String getDeviceType();

    String getEmailAddress();

    String getChannelId();
}

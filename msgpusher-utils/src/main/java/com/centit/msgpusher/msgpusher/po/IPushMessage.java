package com.centit.msgpusher.msgpusher.po;

/**
 * create by scaffold 2017-04-10 
 * @author codefan@sina.com
 
  消息推送null   
*/

public interface IPushMessage {

    String getMsgSender();

    String getMsgReceiver();

    String getMsgType();

    String getMsgSubject();

    String getMsgContent();

    String getRelUrl();

    Integer getMsgExpireSeconds();

    String getOsId();

    String getOptId();

    String getOptMethod();

    String getOptTag();
}
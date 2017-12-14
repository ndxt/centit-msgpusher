package com.centit.msgpusher.msgpusher.po;

import java.util.Date;

/**
 * create by scaffold 2017-04-10 
 * @author codefan@sina.com
 
  消息推送null   
*/

public class SimplePushMessage implements IPushMessage {
    public SimplePushMessage(){
        this.msgType = "msg";
        this.msgSender = "system";
    }
    public SimplePushMessage(String msgContent){
        this.msgType = "msg";
        this.msgSender = "system";
        this.msgContent = msgContent;
    }

    public SimplePushMessage(String msgSender, String msgContent){
        this.msgType = "msg";
        this.msgSender = msgSender;
        this.msgContent = msgContent;
    }

    public SimplePushMessage(String msgSender,String msgSubject, String msgContent){
        this.msgType = "msg";
        this.msgSender = msgSender;
        this.msgSubject = msgSubject;
        this.msgContent = msgContent;
    }

    public SimplePushMessage(String msgSender,
                             String msgSubject, String msgContent,String relUrl){
        this.msgSender = msgSender;
        this.msgSubject = msgSubject;
        this.msgContent = msgContent;
        this.relUrl = relUrl;
    }

    private String  msgSender;
    /**
     * 收件人 null
     */
    private String  msgReceiver;
    /**
     * 消息类型
     */
    private String  msgType;
    /**
     * 主题 null
     */
    private String  msgSubject;
    /**
     * 内容 null
     */
    private String  msgContent;
    /**
     * 关联URL null
     */

    private String  relUrl;

    /**
     * 业务项目模块 模块，或者表
     */
    private String  optId;
    /**
     * 业务操作方法 方法，或者字段
     */
    private String  optMethod;
    /**
     * 业务操作业务标记 一般用于关联到业务主体
     * key1等于value1 且 key2等于value2
     */
    private String  optTag;

    @Override
    public String getMsgSender() {
        return msgSender;
    }

    public void setMsgSender(String msgSender) {
        this.msgSender = msgSender;
    }

    @Override
    public String getMsgReceiver() {
        return msgReceiver;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public String getMsgType() {
        return this.msgType;
    }

    public void setMsgReceiver(String msgReceiver) {
        this.msgReceiver = msgReceiver;
    }

    @Override
    public String getMsgSubject() {
        return msgSubject;
    }

    public void setMsgSubject(String msgSubject) {
        this.msgSubject = msgSubject;
    }

    @Override
    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    @Override
    public String getRelUrl() {
        return relUrl;
    }

    @Override
    public Integer getMsgExpireSeconds() {
        return 18000;
    }

    @Override
    public String getOsId() {
        return "default";
    }

    public void setRelUrl(String relUrl) {
        this.relUrl = relUrl;
    }

    @Override
    public String getOptId() {
        return optId;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }

    @Override
    public String getOptMethod() {
        return optMethod;
    }

    public void setOptMethod(String optMethod) {
        this.optMethod = optMethod;
    }

    @Override
    public String getOptTag() {
        return optTag;
    }

    public void setOptTag(String optTag) {
        this.optTag = optTag;
    }
}
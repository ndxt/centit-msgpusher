package com.centit.msgpusher.client.po;


import java.util.Date;

/**
 * Created by codefan on 17-4-11.
 */
public class MessageDelivery implements java.io.Serializable {
        private static final long serialVersionUID =  1L;

        public static final String NOTICE_TYPE_APP = "A";
        public static final String NOTICE_TYPE_EMAIL = "E";
        public static final String NOTICE_TYPE_SMS = "S";
        public static final String NOTICE_TYPE_WX = "C";

        public final static int  DEFALUT_MSG_EXPIRES=3600*5;
        private Long msgId;

        /**
         * 发送人 null
         */
        private String  msgSender;
        /**
         * 推送类别 点对点、群发
         */

        private String  pushType;
        /**
         * 收件人 null
         */

        private String  msgReceiver;
        /**
         * 消息类别 客户端用来解析
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
         * 通知方式 可以多种方式  A：app推送， S：短信  C：微信  N：内部通知系统 U:未指定
         */
        private String noticeTypes;
        /**
         * 设定发送事件 null
         */
        private Date planPushTime;

        /**
         * 消息有效期 单位为 秒
         */
        private  Integer msgExpireSeconds;
        /**
         * 业务系统ID null
         */
        private String  osId;
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
         */
        private String  optTag;

        // Constructors
        /** default constructor */
	public MessageDelivery() {
        this.noticeTypes = "A";
        this.msgType= "message";
        this.msgExpireSeconds= DEFALUT_MSG_EXPIRES;
    }
        /** minimal constructor */
	public MessageDelivery(
                Long msgId
                ,String  msgReceiver,String  msgContent,String  optId) {
        this.noticeTypes ="A";
        this.msgExpireSeconds= DEFALUT_MSG_EXPIRES;
        this.msgId = msgId;
        this.msgType= "message";
        this.msgReceiver= msgReceiver;
        this.msgContent= msgContent;
        this.optId= optId;
    }

/** full constructor */
	public MessageDelivery(
                Long msgId
                ,String  msgSender,String  pushType,String  msgReceiver,String  msgType,String  msgSubject,
                String  msgContent,String  relUrl,String  noticeTypes,
                Date  planPushTime,int  msgExpires,String  osId,String  optId,String  optMethod,String  optTag) {

            this.msgId = msgId;
            this.msgSender= msgSender;
            this.pushType= pushType;
            this.msgReceiver= msgReceiver;
            this.msgType= msgType;
            this.msgSubject= msgSubject;
            this.msgContent= msgContent;
            this.relUrl= relUrl;
            this.noticeTypes= noticeTypes;
            this.planPushTime= planPushTime;
            this.msgExpireSeconds= msgExpires;
            this.osId= osId;
            this.optId= optId;
            this.optMethod= optMethod;
            this.optTag= optTag;
        }



    public Long getMsgId() {
        return this.msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }
    // Property accessors

    public String getMsgSender() {
        return this.msgSender;
    }

    public void setMsgSender(String msgSender) {
        this.msgSender = msgSender;
    }

    public String getPushType() {
        return this.pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getMsgReceiver() {
        return this.msgReceiver;
    }

    public void setMsgReceiver(String msgReceiver) {
        this.msgReceiver = msgReceiver;
    }

    public String getMsgType() {
        return this.msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgSubject() {
        return this.msgSubject;
    }

    public void setMsgSubject(String msgSubject) {
        this.msgSubject = msgSubject;
    }

    public String getMsgContent() {
        return this.msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getRelUrl() {
        return this.relUrl;
    }

    public void setRelUrl(String relUrl) {
        this.relUrl = relUrl;
    }

    public String getNoticeTypes() {
        return this.noticeTypes;
    }

    public void setNoticeTypes(String noticeTypes) {
        this.noticeTypes = noticeTypes;
    }


    public Date getPlanPushTime() {
        return this.planPushTime;
    }

    public void setPlanPushTime(Date planPushTime) {
        this.planPushTime = planPushTime;
    }


    public Integer getMsgExpireSeconds() {
        return this.msgExpireSeconds;
    }

    public void setMsgExpireSeconds(Integer msgExpires) {
        this.msgExpireSeconds = msgExpires;
    }

    public String getOsId() {
        return this.osId;
    }

    public void setOsId(String osId) {
        this.osId = osId;
    }

    public String getOptId() {
        return this.optId;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }

    public String getOptMethod() {
        return this.optMethod;
    }

    public void setOptMethod(String optMethod) {
        this.optMethod = optMethod;
    }

    public String getOptTag() {
        return this.optTag;
    }

    public void setOptTag(String optTag) {
        this.optTag = optTag;
    }



    public MessageDelivery copy(MessageDelivery other){

        this.setMsgId(other.getMsgId());

        this.msgSender= other.getMsgSender();
        this.pushType= other.getPushType();
        this.msgReceiver= other.getMsgReceiver();
        this.msgType= other.getMsgType();
        this.msgSubject= other.getMsgSubject();
        this.msgContent= other.getMsgContent();
        this.relUrl= other.getRelUrl();
        this.noticeTypes= other.getNoticeTypes();
        this.planPushTime= other.getPlanPushTime();
        this.msgExpireSeconds= other.getMsgExpireSeconds();
        this.osId= other.getOsId();
        this.optId= other.getOptId();
        this.optMethod= other.getOptMethod();
        this.optTag= other.getOptTag();

        return this;
    }

    public MessageDelivery copyNotNullProperty(MessageDelivery other){

        if( other.getMsgId() != null)
            this.setMsgId(other.getMsgId());

        if( other.getMsgSender() != null)
            this.msgSender= other.getMsgSender();
        if( other.getPushType() != null)
            this.pushType= other.getPushType();
        if( other.getMsgReceiver() != null)
            this.msgReceiver= other.getMsgReceiver();
        if( other.getMsgType() != null)
            this.msgType= other.getMsgType();
        if( other.getMsgSubject() != null)
            this.msgSubject= other.getMsgSubject();
        if( other.getMsgContent() != null)
            this.msgContent= other.getMsgContent();
        if( other.getRelUrl() != null)
            this.relUrl= other.getRelUrl();
        if( other.getNoticeTypes() != null)
            this.noticeTypes= other.getNoticeTypes();
        if( other.getPlanPushTime() != null)
            this.planPushTime= other.getPlanPushTime();
        //if( other.getMsgExpireSeconds() != null)
        this.msgExpireSeconds= other.getMsgExpireSeconds();
        if( other.getOsId() != null)
            this.osId= other.getOsId();
        if( other.getOptId() != null)
            this.optId= other.getOptId();
        if( other.getOptMethod() != null)
            this.optMethod= other.getOptMethod();
        if( other.getOptTag() != null)
            this.optTag= other.getOptTag();

        return this;
    }

    public MessageDelivery clearProperties(){

        this.msgSender= null;
        this.pushType= null;
        this.msgReceiver= null;
        this.msgType= null;
        this.msgSubject= null;
        this.msgContent= null;
        this.relUrl= null;
        this.noticeTypes= null;
        this.planPushTime= null;
        this.osId= null;
        this.optId= null;
        this.optMethod= null;
        this.optTag= null;
        return this;
    }
}

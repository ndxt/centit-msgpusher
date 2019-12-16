package com.centit.msgpusher.client.po;


import com.centit.framework.model.basedata.NoticeMessage;
import lombok.Data;

import java.util.Date;

/**
 * Created by codefan on 17-4-11.
 */
@Data
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


    public MessageDelivery copyFromNoticeMessage(NoticeMessage other){

        this.msgType= other.getMsgType();
        this.msgSubject= other.getMsgSubject();
        this.msgContent= other.getMsgContent();

        this.optId= other.getOptId();
        this.optMethod= other.getOptMethod();
        this.optTag= other.getOptTag();

        return this;
    }
}

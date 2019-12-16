package com.centit.msgpusher.po;

import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Date;


/**
 * create by scaffold 2017-04-10
 * @author codefan@sina.com

  消息推送null
*/
@Data
@Entity
@Table(name = "F_MESSAGE_DELIVERY")
public class MessageDelivery implements java.io.Serializable {
    private static final long serialVersionUID =  1L;

    public static final String NOTICE_TYPE_APP = "A";
    public static final String NOTICE_TYPE_EMAIL = "E";
    public static final String NOTICE_TYPE_SMS = "S";
    public static final String NOTICE_TYPE_WX = "C";
    public static final String NOTICE_TYPE_PC = "P";

    /**
     * 通知ID null
     */
    @Id
    @Column(name = "MSG_ID")
    @ValueGenerator(strategy = GeneratorType.UUID)
    private String  msgId;

    /**
     * 发送人 null
     */
    @Column(name = "MSG_SENDER")
    @Length(max = 100, message = "字段长度不能大于{max}")
    private String  msgSender;
    /**
     * 推送类别 点对点、群发
     */
    @Column(name = "PUSH_TYPE")
    @Length(max = 1, message = "字段长度不能大于{max}")
    private String  pushType;
    /**
     * 收件人 null
     */
    @Column(name = "MSG_RECEIVER")
    /*@NotBlank(message = "字段不能为空")*/
    @Length(max = 100, message = "字段长度不能大于{max}")
    private String  msgReceiver;
    /**
     * 消息类别 客户端用来解析
     */
    @Column(name = "MSG_TYPE")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String  msgType;
    /**
     * 主题 null
     */
    @Column(name = "MSG_SUBJECT")
    @Length(max = 100, message = "字段长度不能大于{max}")
    private String  msgSubject;
    /**
     * 内容 null
     */
    @Column(name = "MSG_CONTENT")
    @NotBlank(message = "字段不能为空")
    @Length(max = 1000, message = "字段长度不能大于{max}")
    private String  msgContent;
    /**
     * 关联URL null
     */
    @Column(name = "REL_URL")
    @Length(max = 500, message = "字段长度不能大于{max}")
    private String  relUrl;
    /**
     * 通知方式 可以多种方式  A：app推送， S：短信  C：微信  N：内部通知系统 U: unknown 未指定
     */
    @Column(name = "NOTICE_TYPES")
    @Length(max = 100, message = "字段长度不能大于{max}")
    private String  noticeTypes;
    /**
     * 发送状态 0 待发送 1 成功， 2失败 3 部分成功 4 定时发送消息被后台取消
     */
    @Column(name = "PUSH_STATE")
    @Length(max = 1, message = "字段长度不能大于{max}")
    private String  pushState;
    /**
     * 发送结果 成功为 第三方 msgid 失败为 失败原因
     */
    @Column(name = "PUSH_RESULT")
    @Length(max = 500, message = "字段长度不能大于{max}")
    private String  pushResult;
    /**
     * 设定发送事件 null
     */
    @Column(name = "PLAN_PUSH_TIME")
    private Date  planPushTime;
    /**
     * 实际发送时间 null
     */
    @Column(name = "PUSH_TIME")
    private Date  pushTime;
    /**
     * 消息有效期 null
     */
    @Column(name = "VALID_PERIOD")
    private Date  validPeriod;

    /**
     * 消息有效期 单位为 秒
     */
    @Transient
    private Integer msgExpireSeconds;
    /**
     * 业务系统ID null
     */
    @Column(name = "OS_ID")
    @Length(max = 20, message = "字段长度不能大于{max}")
    private String  osId;
    /**
     * 业务项目模块 模块，或者表
     */
    @Column(name = "OPT_ID")
    @NotBlank(message = "字段不能为空")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String  optId;
    /**
     * 业务操作方法 方法，或者字段
     */
    @Column(name = "OPT_METHOD")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String  optMethod;
    /**
     * 业务操作业务标记 一般用于关联到业务主体
     * key1等于value1 且 key2等于value2
     */
    @Column(name = "OPT_TAG")
    @Length(max = 1000, message = "字段长度不能大于{max}")
    private String  optTag;

    // Constructors
    public MessageDelivery() {
        this.msgType= "message";
    }

    public MessageDelivery(
        String msgId
        ,String  msgReceiver,String  msgContent,String optId){
        this.msgType= "message";
        this.msgId = msgId;
        this.msgReceiver= msgReceiver;
        this.msgContent= msgContent;
        this.optId= optId;
    }

}

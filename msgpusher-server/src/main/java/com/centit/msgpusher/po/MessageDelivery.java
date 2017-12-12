package com.centit.msgpusher.po;

import java.util.Date;
import javax.persistence.*;


import javax.validation.constraints.NotNull;

import com.centit.msgpusher.msgpusher.po.IPushMessage;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;


/**
 * create by scaffold 2017-04-10 
 * @author codefan@sina.com
 
  消息推送null   
*/
@Entity
@Table(name = "F_MESSAGE_DELIVERY")
public class MessageDelivery implements IPushMessage, java.io.Serializable {
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
	@GeneratedValue(generator = "assignedGenerator")
	@GenericGenerator(name = "assignedGenerator", strategy = "uuid")
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

	public MessageDelivery(
		 String msgId
		,String  msgSender,String  pushType,String  msgReceiver,String  msgType,String  msgSubject,String  msgContent,String  relUrl,String  noticeTypes,String  pushState,String  pushResult,Date  planPushTime,Date  pushTime,Date  validPeriod,String  osId,String  optId,String  optMethod,String  optTag) {

		this.msgId = msgId;
		this.msgSender= msgSender;
		this.pushType= pushType;
		this.msgReceiver= msgReceiver;
		this.msgType= msgType;
		this.msgSubject= msgSubject;
		this.msgContent= msgContent;
		this.relUrl= relUrl;
		this.noticeTypes= noticeTypes;
		this.pushState= pushState;
		this.pushResult= pushResult;
		this.planPushTime= planPushTime;
		this.pushTime= pushTime;
		this.validPeriod= validPeriod;
		this.osId= osId;
		this.optId= optId;
		this.optMethod= optMethod;
		this.optTag= optTag;		
	}


	public Integer getMsgExpireSeconds() {
		return this.msgExpireSeconds;
	}

	public void setMsgExpireSeconds(Integer msgExpires) {
		this.msgExpireSeconds = msgExpires;
	}

  
	public String getMsgId() {
		return this.msgId;
	}

	public void setMsgId(String msgId) {
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
  
	public String getPushState() {
		return this.pushState;
	}
	
	public void setPushState(String pushState) {
		this.pushState = pushState;
	}
  
	public String getPushResult() {
		return this.pushResult;
	}
	
	public void setPushResult(String pushResult) {
		this.pushResult = pushResult;
	}
  
	public Date getPlanPushTime() {
		return this.planPushTime;
	}
	
	public void setPlanPushTime(Date planPushTime) {
		this.planPushTime = planPushTime;
	}
  
	public Date getPushTime() {
		return this.pushTime;
	}
	
	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}
  
	public Date getValidPeriod() {
		return this.validPeriod;
	}
	
	public void setValidPeriod(Date validPeriod) {
		this.validPeriod = validPeriod;
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
		this.pushState= other.getPushState();  
		this.pushResult= other.getPushResult();  
		this.planPushTime= other.getPlanPushTime();  
		this.pushTime= other.getPushTime();  
		this.validPeriod= other.getValidPeriod();  
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
		if( other.getPushState() != null)
			this.pushState= other.getPushState();  
		if( other.getPushResult() != null)
			this.pushResult= other.getPushResult();  
		if( other.getPlanPushTime() != null)
			this.planPushTime= other.getPlanPushTime();  
		if( other.getPushTime() != null)
			this.pushTime= other.getPushTime();  
		if( other.getValidPeriod() != null)
			this.validPeriod= other.getValidPeriod();  
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
		this.pushState= null;  
		this.pushResult= null;  
		this.planPushTime= null;  
		this.pushTime= null;  
		this.validPeriod= null;  
		this.osId= null;  
		this.optId= null;  
		this.optMethod= null;  
		this.optTag= null;

		return this;
	}
}
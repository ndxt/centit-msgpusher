package com.centit.msgpusher.po;

import javax.persistence.Column;

import javax.persistence.EmbeddedId;


import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;


/**
 * create by scaffold 2017-04-10 
 * @author codefan@sina.com
 
  用户消息接收端口（设备）信息用户设置 自己接收 通知的方式。   
*/
@Entity
@Table(name = "F_USER_MSG_POINT")
public class UserMsgPoint implements java.io.Serializable {
	private static final long serialVersionUID =  1L;

	@EmbeddedId
	private UserMsgPointId cid;

    @Column(name = "USER_NAME")
    @Length(max = 50, message = "字段长度不能大于{max}")
    private String userName;

	/**
	 * 移动设备类型 3 ：android ； 4：apple ， 0： 没有 
	 */
	@Column(name = "DEVICE_TYPE")
	@Length(max = 1, message = "字段长度不能大于{max}")
	private String  deviceType;
	/**
	 * 设备标识码 null 
	 */
	@Column(name = "DEVICE_ID")
	@Length(max = 100, message = "字段长度不能大于{max}")
	private String  deviceId;
	/**
	 * 设备系统版本 null 
	 */
	@Column(name = "OS_VERSION")
	@Length(max = 100, message = "字段长度不能大于{max}")
	private String  osVersion;
	/**
	 * 百度推送通道 null 
	 */
	@Column(name = "CHANNEL_ID")
	@Length(max = 100, message = "字段长度不能大于{max}")
	private String  channelId;
	/**
	 * 微信令牌 公众号下的令牌 
	 */
	@Column(name = "WX_TOKEN")
	@Length(max = 100, message = "字段长度不能大于{max}")
	private String  wxToken;
	/**
	 * 移动电话 用于短信 
	 */
	@Column(name = "MOBILE_PHONE")
	@Length(max = 13, message = "字段长度不能大于{max}")
	private String  mobilePhone;

	/**
	 * Email地址 用于短信
	 */
	@Column(name = "EMAIL_ADDRESS")
	@Length(max = 100, message = "字段长度不能大于{max}")
	private String  emailAddress;
	// Constructors
	/** default constructor */
	public UserMsgPoint() {
	}
	/** minimal constructor */
	public UserMsgPoint(UserMsgPointId id) {
		this.cid = id;
	}

/** full constructor */
	public UserMsgPoint(UserMsgPointId id , String userName, String deviceType, String deviceId, String osVersion, String channelId, String wxToken, String mobilePhone, String emailAddress) {
		this.cid = id;
        this.userName = userName;
        this.deviceType= deviceType;
		this.deviceId= deviceId;
		this.osVersion= osVersion;
		this.channelId= channelId;
		this.wxToken= wxToken;
		this.mobilePhone= mobilePhone;
        this.emailAddress = emailAddress;
    }

	public UserMsgPointId getCid() {
		return this.cid;
	}
	
	public void setCid(UserMsgPointId id) {
		this.cid = id;
	}
  
	public String getUserCode() {
		if(this.cid==null)
			this.cid = new UserMsgPointId();
		return this.cid.getUserCode();
	}
	
	public void setUserCode(String userCode) {
		if(this.cid==null)
			this.cid = new UserMsgPointId();
		this.cid.setUserCode(userCode);
	}
  
	public String getOsId() {
		if(this.cid==null)
			this.cid = new UserMsgPointId();
		return this.cid.getOsId();
	}
	
	public void setOsId(String osId) {
		if(this.cid==null)
			this.cid = new UserMsgPointId();
		this.cid.setOsId(osId);
	}


	// Property accessors

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

	public String getDeviceType() {
		return this.deviceType;
	}
	
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
  
	public String getDeviceId() {
		return this.deviceId;
	}
	
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
  
	public String getOsVersion() {
		return this.osVersion;
	}
	
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
  
	public String getChannelId() {
		return this.channelId;
	}
	
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
  
	public String getWxToken() {
		return this.wxToken;
	}
	
	public void setWxToken(String wxToken) {
		this.wxToken = wxToken;
	}
  
	public String getMobilePhone() {
		return this.mobilePhone;
	}
	
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}



	public UserMsgPoint copy(UserMsgPoint other){
  
		this.setUserCode(other.getUserCode());  
		this.setOsId(other.getOsId());
        this.userName = other.getUserName();
		this.deviceType= other.getDeviceType();  
		this.deviceId= other.getDeviceId();  
		this.osVersion= other.getOsVersion();  
		this.channelId= other.getChannelId();  
		this.wxToken= other.getWxToken();  
		this.mobilePhone= other.getMobilePhone();
        this.emailAddress = other.getEmailAddress();
		return this;
	}
	
	public UserMsgPoint copyNotNullProperty(UserMsgPoint other){
  
        if( other.getUserCode() != null)
            this.setUserCode(other.getUserCode());
        if( other.getOsId() != null)
            this.setOsId(other.getOsId());
        if( other.getUserName() != null)
            this.userName = other.getUserName();

		if( other.getDeviceType() != null)
			this.deviceType= other.getDeviceType();  
		if( other.getDeviceId() != null)
			this.deviceId= other.getDeviceId();  
		if( other.getOsVersion() != null)
			this.osVersion= other.getOsVersion();  
		if( other.getChannelId() != null)
			this.channelId= other.getChannelId();  
		if( other.getWxToken() != null)
			this.wxToken= other.getWxToken();  
		if( other.getMobilePhone() != null)
			this.mobilePhone= other.getMobilePhone();
        if( other.getEmailAddress() != null)
            this.emailAddress = other.getEmailAddress();

        return this;
	}

	public UserMsgPoint clearProperties(){
        this.userName = null;
		this.deviceType= null;  
		this.deviceId= null;  
		this.osVersion= null;  
		this.channelId= null;  
		this.wxToken= null;  
		this.mobilePhone= null;
        this.emailAddress = null;

		return this;
	}
}

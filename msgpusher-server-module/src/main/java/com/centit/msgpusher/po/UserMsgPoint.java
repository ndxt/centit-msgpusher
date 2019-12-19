package com.centit.msgpusher.po;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * create by scaffold 2017-04-10
 * @author codefan@sina.com

  用户消息接收端口（设备）信息用户设置 自己接收 通知的方式。
*/
@Data
@Entity
@Table(name = "F_USER_MSG_POINT")
public class UserMsgPoint implements java.io.Serializable {
    private static final long serialVersionUID =  1L;

    @Id
    @Column(name = "USER_CODE")
    @NotBlank(message = "字段不能为空")
    private String userCode;

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
    public UserMsgPoint() {
    }

    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }


    public UserMsgPoint copy(UserMsgPoint other){
        this.setUserCode(other.getUserCode());
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
}

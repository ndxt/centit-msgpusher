package com.centit.msgpusher.client.po;

/**
 * create by scaffold 2017-04-10
 * @author codefan@sina.com

  用户消息接收端口（设备）信息用户设置 自己接收 通知的方式。
*/
public class UserMsgPoint implements java.io.Serializable {
    private static final long serialVersionUID =  1L;

    /**
     * 用户代码 null
     */
    private String userCode;


    /**
     * 业务系统ID null
     */
    private String osId;



    private String userName;

    /**
     * 移动设备类型 3 ：android ； 4：apple ， 0： 没有
     */

    private String  deviceType;
    /**
     * 设备标识码 null
     */
    private String  deviceId;
    /**
     * 设备系统版本 null
     */

    private String  osVersion;
    /**
     * 百度推送通道 null
     */

    private String  channelId;
    /**
     * 微信令牌 公众号下的令牌
     */

    private String  wxToken;
    /**
     * 移动电话 用于短信
     */

    private String  mobilePhone;

    /**
     * Email地址 用于短信
     */

    private String  emailAddress;
    // Constructors
    /** default constructor */
    public UserMsgPoint() {
    }


    // minimal constructor
    public UserMsgPoint(String osId,String userCode) {

        this.userCode = userCode;
        this.osId = osId;
    }


    public UserMsgPoint(String osId,String userCode, String userName, String deviceType,
                        String deviceId, String osVersion, String channelId,
                        String wxToken, String mobilePhone, String emailAddress) {

        this.userCode = userCode;
        this.osId = osId;
        this.userName = userName;
        this.deviceType= deviceType;
        this.deviceId= deviceId;
        this.osVersion= osVersion;
        this.channelId= channelId;
        this.wxToken= wxToken;
        this.mobilePhone= mobilePhone;
        this.emailAddress = emailAddress;
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



    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getOsId() {
        return this.osId;
    }

    public void setOsId(String osId) {
        this.osId = osId;
    }


    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof UserMsgPoint))
            return false;

        UserMsgPoint castOther = (UserMsgPoint) other;
        boolean ret = true;

        ret = ret && ( this.getUserCode() == castOther.getUserCode() ||
                (this.getUserCode() != null && castOther.getUserCode() != null
                        && this.getUserCode().equals(castOther.getUserCode())));

        ret = ret && ( this.getOsId() == castOther.getOsId() ||
                (this.getOsId() != null && castOther.getOsId() != null
                        && this.getOsId().equals(castOther.getOsId())));

        return ret;
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result +
                (this.getUserCode() == null ? 0 :this.getUserCode().hashCode());

        result = 37 * result +
                (this.getOsId() == null ? 0 :this.getOsId().hashCode());

        return result;
    }
}

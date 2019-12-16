package com.centit.msgpusher.client.po;

import lombok.Data;

/**
 * create by scaffold 2017-04-10
 * @author codefan@sina.com

  用户消息接收端口（设备）信息用户设置 自己接收 通知的方式。
*/
@Data
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

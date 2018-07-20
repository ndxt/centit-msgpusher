package com.centit.msgpusher.po;

import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * create by scaffold 2017-04-10
 * @author codefan@sina.com

  用户通知接受参数设置用户设置 自己接收 通知的方式。
*/
@Entity
@Table(name = "F_USER_NOTIFY_SETTING")
public class UserNotifySetting implements java.io.Serializable {
    private static final long serialVersionUID =  1L;
    /**
     * 用户设定编号 null
     */
    @Id
    @Column(name = "USER_SETTING_ID")
    @ValueGenerator(strategy = GeneratorType.UUID)
    private String userSettingId;

    /**
     * 用户代码 null
     */
    @Column(name = "USER_CODE")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String  userCode;
    /**
     * 业务系统ID "Default" 表示所有系统的默认值
     */
    @Column(name = "OS_ID")
    @Length(max = 20, message = "字段长度不能大于{max}")
    private String  osId;
    /**
     * 业务项目模块 "Default" 表示所有系统的默认值模块
     */
    @Column(name = "OPT_ID")
    @NotBlank(message = "字段不能为空")
    @Length(max = 64, message = "字段长度不能大于{max}")
    private String  optId;
    /**
     * 接收通知方式 接收通知的方式，可以有多种方式
     */
    @Column(name = "NOTIFY_TYPES")
    @Length(max = 100, message = "字段长度不能大于{max}")
    private String  notifyTypes;

    // Constructors
    public UserNotifySetting() {
    }

    public UserNotifySetting(
        String userSettingId
        ,String  optId) {


        this.userSettingId = userSettingId;

        this.optId= optId;
    }

    public UserNotifySetting(
     String userSettingId
    ,String  userCode,String  osId,String  optId,String  notifyTypes) {

        this.userSettingId = userSettingId;
        this.userCode= userCode;
        this.osId= osId;
        this.optId= optId;
        this.notifyTypes= notifyTypes;
    }



    public String getUserSettingId() {
        return this.userSettingId;
    }

    public void setUserSettingId(String userSettingId) {
        this.userSettingId = userSettingId;
    }
    // Property accessors

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

    public String getOptId() {
        return this.optId;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }

    public String getNotifyTypes() {
        return this.notifyTypes;
    }

    public void setNotifyTypes(String notifyTypes) {
        this.notifyTypes = notifyTypes;
    }



    public UserNotifySetting copy(UserNotifySetting other){

        this.setUserSettingId(other.getUserSettingId());

        this.userCode= other.getUserCode();
        this.osId= other.getOsId();
        this.optId= other.getOptId();
        this.notifyTypes= other.getNotifyTypes();

        return this;
    }

    public UserNotifySetting copyNotNullProperty(UserNotifySetting other){

        if( other.getUserSettingId() != null) {
            this.setUserSettingId(other.getUserSettingId());
        }
        if( other.getUserCode() != null) {
            this.userCode = other.getUserCode();
        }
        if( other.getOsId() != null) {
            this.osId = other.getOsId();
        }
        if( other.getOptId() != null) {
            this.optId = other.getOptId();
        }
        if( other.getNotifyTypes() != null) {
            this.notifyTypes = other.getNotifyTypes();
        }

        return this;
    }

    public UserNotifySetting clearProperties(){

        this.userCode= null;
        this.osId= null;
        this.optId= null;
        this.notifyTypes= null;

        return this;
    }
}

package com.centit.msgpusher.po;

import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
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

  用户通知接受参数设置用户设置 自己接收 通知的方式。
*/
@Data
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

}

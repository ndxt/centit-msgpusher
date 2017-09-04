package com.centit.msgpusher.po;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.hibernate.validator.constraints.NotBlank;

/**
 * UserMsgPointId  entity.
 * create by scaffold 2017-04-10 
 * @author codefan@sina.com
 * 用户消息接收端口（设备）信息用户设置 自己接收 通知的方式。   
*/
//用户消息接收端口（设备）信息 的主键
@Embeddable
public class UserMsgPointId implements java.io.Serializable {
	private static final long serialVersionUID =  1L;

	/**
	 * 用户代码 null 
	 */
	@Column(name = "USER_CODE")
	@NotBlank(message = "字段不能为空")
	private String userCode;

	/**
	 * 业务系统ID null 
	 */
	@Column(name = "OS_ID")
	@NotBlank(message = "字段不能为空")
	private String osId;

	// Constructors
	public UserMsgPointId() {
	}
	public UserMsgPointId(String osId,String userCode) {
		this.userCode = userCode;
		this.osId = osId;	
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
		if (!(other instanceof UserMsgPointId))
			return false;
		
		UserMsgPointId castOther = (UserMsgPointId) other;
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

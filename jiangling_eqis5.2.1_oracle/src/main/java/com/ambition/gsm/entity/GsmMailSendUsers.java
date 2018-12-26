package com.ambition.gsm.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.norteksoft.product.orm.IdEntity;
/**
 * 类名: SupplierMailSendUsers 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  张顺治
 */
@Entity
@Table(name="GSM_MAIL_SEND_USERS")
public class GsmMailSendUsers extends IdEntity{ 
	private static final long serialVersionUID = 1L;
	private String userName;//用户名
	private String userLoginName;//用户登录名
	@ManyToOne
	@JoinColumn(name="FK_GSM_MAIL_SEND_USERS_ID")
	private GsmMailSettings gsmMailSettings;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserLoginName() {
		return userLoginName;
	}
	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}
	public GsmMailSettings getGsmMailSettings() {
		return gsmMailSettings;
	}
	public void setGsmMailSettings(GsmMailSettings gsmMailSettings) {
		this.gsmMailSettings = gsmMailSettings;
	}
}

 /**   
 * @Title: SupplierMailSettings.java 
 * @Package com.ambition.supplier.entity 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 刘承斌   
 * @date 2015-3-17 下午4:09:16 
 * @version V1.0   
 */ 
package com.ambition.gsm.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.norteksoft.product.orm.IdEntity;

 
@Entity
@Table(name="GSM_EQUIPMENT_MAIL_SETTINGS")
public class GsmMailSettings extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String businessCode;
	private Boolean enabled=true;//是否启用 true：启用，false：不启用
	private Integer days;
	private Integer days1;
	
	@JsonIgnore
	@OneToMany(mappedBy="gsmMailSettings",cascade=javax.persistence.CascadeType.ALL)
    @Cascade(value=CascadeType.DELETE_ORPHAN)
	private List<GsmMailSendUsers> gsmMailSendUserss;
	
	public Integer getDays1() {
		return days1;
	}
	public void setDays1(Integer days1) {
		this.days1 = days1;
	}
	public String getBusinessCode() {
		return businessCode;
	}
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public List<GsmMailSendUsers> getGsmMailSendUserss() {
		return gsmMailSendUserss;
	}
	public void setGsmMailSendUserss(ArrayList<GsmMailSendUsers> gsmMailSendUsers) {
		this.gsmMailSendUserss=gsmMailSendUsers;	
	}
}

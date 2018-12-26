package com.ambition.gsm.entity;

//import java.util.List;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
//import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:仪器标准件维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月8日 发布
 */
@Entity
@Table(name = "GSM_EQUIPMENT_STANDARD_ITEM")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class EquipmentStandardItem extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String standardName;//标准件名称
	private String standardNo;//标准件编号
	private Date validityDate;//有效期至
	private String certificateNo;//证书编号	
	@ManyToOne
	@JoinColumn(name = "FK_EQUIPMENT_STANDARD_NO")
	@JsonIgnore
	private EquipmentStandard equipmentStandard;//
	public String getStandardName() {
		return standardName;
	}
	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}
	public String getStandardNo() {
		return standardNo;
	}
	public void setStandardNo(String standardNo) {
		this.standardNo = standardNo;
	}
	public Date getValidityDate() {
		return validityDate;
	}
	public void setValidityDate(Date validityDate) {
		this.validityDate = validityDate;
	}
	public String getCertificateNo() {
		return certificateNo;
	}
	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}
	public EquipmentStandard getEquipmentStandard() {
		return equipmentStandard;
	}
	public void setEquipmentStandard(EquipmentStandard equipmentStandard) {
		this.equipmentStandard = equipmentStandard;
	}

}

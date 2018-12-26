package com.ambition.gsm.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 类名:NewEquipmentSublist
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：新设备申请登记附属子表</p>
 * @author  肖美华
 * @version 1.00 2016-11-16 发布
 */
@Entity
@Table(name="GSM_EQUIPMENT_SUBLIST")
public class NewEquipmentSublist extends IdEntity{
	private static final long serialVersionUID = 1L;
	
	private String deviceName;//设备名称
	private String modelSpecification;//型号规格
	private String manufacturer;//厂商
	private String factoryNumber;//出厂编号
	private String preserver;//保管人
	private String preserverLogin;//保管人登录名
	private String nstrumentNumber;//仪器管理编号
	private String remark;//备注
	
	@ManyToOne
    @JsonIgnore
    @JoinColumn(name="GSM_SUBLIST_ID")
    private NewEquipmentRegister newEquipmentRegister;

	
	
	public String getPreserverLogin() {
		return preserverLogin;
	}
	public void setPreserverLogin(String preserverLogin) {
		this.preserverLogin = preserverLogin;
	}
	public NewEquipmentRegister getNewEquipmentRegister() {
		return newEquipmentRegister;
	}
	public void setNewEquipmentRegister(NewEquipmentRegister newEquipmentRegister) {
		this.newEquipmentRegister = newEquipmentRegister;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getModelSpecification() {
		return modelSpecification;
	}
	public void setModelSpecification(String modelSpecification) {
		this.modelSpecification = modelSpecification;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getFactoryNumber() {
		return factoryNumber;
	}
	public void setFactoryNumber(String factoryNumber) {
		this.factoryNumber = factoryNumber;
	}
	public String getPreserver() {
		return preserver;
	}
	public void setPreserver(String preserver) {
		this.preserver = preserver;
	}
	public String getNstrumentNumber() {
		return nstrumentNumber;
	}
	public void setNstrumentNumber(String nstrumentNumber) {
		this.nstrumentNumber = nstrumentNumber;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}



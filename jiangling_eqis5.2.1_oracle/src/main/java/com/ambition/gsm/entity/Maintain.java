package com.ambition.gsm.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="GSM_MAINTAIN")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Maintain extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String companyMain;//公司主体
	private String businessDivision;//事业部
	private String devName;//使用部门
	private String responsible;//责任人
	private String responsibleLogin;//责任人
	private String fixedAssets;//固定资产编号
	private String managerAssets;//管理编号
	private String equipmentName;//设备名称
	private String equipmentModel;//设备型号
	private String manufacturer;//制造商
	private String factoryNumber;//机身号
	private Date maintainDate;//维修日期
	private String accendant;//维修人
	private String checkMethod;//校检方式
	private String checkState;//校检状态
	private String remark;//备注
	private String maintainItem;//维修项目
	private String maintainResult;//维修结果
	public String getCompanyMain() {
		return companyMain;
	}
	public void setCompanyMain(String companyMain) {
		this.companyMain = companyMain;
	}
	public String getBusinessDivision() {
		return businessDivision;
	}
	public void setBusinessDivision(String businessDivision) {
		this.businessDivision = businessDivision;
	}
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getResponsible() {
		return responsible;
	}
	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
	public String getFixedAssets() {
		return fixedAssets;
	}
	public void setFixedAssets(String fixedAssets) {
		this.fixedAssets = fixedAssets;
	}
	public String getManagerAssets() {
		return managerAssets;
	}
	public void setManagerAssets(String managerAssets) {
		this.managerAssets = managerAssets;
	}
	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	public String getEquipmentModel() {
		return equipmentModel;
	}
	public void setEquipmentModel(String equipmentModel) {
		this.equipmentModel = equipmentModel;
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
	public Date getMaintainDate() {
		return maintainDate;
	}
	public void setMaintainDate(Date maintainDate) {
		this.maintainDate = maintainDate;
	}
	public String getAccendant() {
		return accendant;
	}
	public void setAccendant(String accendant) {
		this.accendant = accendant;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getResponsibleLogin() {
		return responsibleLogin;
	}
	public void setResponsibleLogin(String responsibleLogin) {
		this.responsibleLogin = responsibleLogin;
	}
	public String getCheckMethod() {
		return checkMethod;
	}
	public void setCheckMethod(String checkMethod) {
		this.checkMethod = checkMethod;
	}
	public String getCheckState() {
		return checkState;
	}
	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}
	public String getMaintainItem() {
		return maintainItem;
	}
	public void setMaintainItem(String maintainItem) {
		this.maintainItem = maintainItem;
	}
	public String getMaintainResult() {
		return maintainResult;
	}
	public void setMaintainResult(String maintainResult) {
		this.maintainResult = maintainResult;
	}
	
}

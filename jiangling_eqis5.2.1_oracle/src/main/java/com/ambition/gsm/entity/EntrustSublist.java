package com.ambition.gsm.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="GSM_ENTRUST_SUBLIST")
public class EntrustSublist extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String managerAssets;//管理编号
	private String equipmentName;//设备名称
	private String equipmentModel;//设备型号
	private String address;//安装地址
	private String dutyMan;//责任人
	private String measuringRange;//测量范围
	private String manufacturer;//生产商
	private String factoryNumber;//机身号
	private Date calibrationDate;//校准日期
	private String checkMethod;//校检方式
	private String parameterStandard;//使用参数标准
	private String measurementState;//量具状态 
	private String confirmor;//确认人
	
	@ManyToOne
	@JoinColumn(name="GSM_SUBLIST_ID")
    @JsonIgnore()
	private Entrust entrust;
	
	public Entrust getEntrust() {
		return entrust;
	}
	public void setEntrust(Entrust entrust) {
		this.entrust = entrust;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDutyMan() {
		return dutyMan;
	}
	public void setDutyMan(String dutyMan) {
		this.dutyMan = dutyMan;
	}
	public String getMeasuringRange() {
		return measuringRange;
	}
	public void setMeasuringRange(String measuringRange) {
		this.measuringRange = measuringRange;
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
	public Date getCalibrationDate() {
		return calibrationDate;
	}
	public void setCalibrationDate(Date calibrationDate) {
		this.calibrationDate = calibrationDate;
	}
	public String getCheckMethod() {
		return checkMethod;
	}
	public void setCheckMethod(String checkMethod) {
		this.checkMethod = checkMethod;
	}
	public String getParameterStandard() {
		return parameterStandard;
	}
	public void setParameterStandard(String parameterStandard) {
		this.parameterStandard = parameterStandard;
	}
	public String getMeasurementState() {
		return measurementState;
	}
	public void setMeasurementState(String measurementState) {
		this.measurementState = measurementState;
	}
	public String getConfirmor() {
		return confirmor;
	}
	public void setConfirmor(String confirmor) {
		this.confirmor = confirmor;
	}
	
	
}

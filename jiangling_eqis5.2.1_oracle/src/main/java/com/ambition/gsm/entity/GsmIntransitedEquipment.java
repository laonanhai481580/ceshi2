package com.ambition.gsm.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.norteksoft.product.orm.IdEntity;

/**
 * 在途量检具(ENTITY)
 * @author 张顺治
 *
 */
public class GsmIntransitedEquipment extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String measurementNo;//量检具编号
	private String measurementName;//量检具名称
	private String measurementSpecification;//型号/规格
	private String manufacturer;//生产商
	private String type;//类别
	private String secondaryType;//二级分类
	@JsonIgnore
	private Date purchaseDate;//采购日期
	@JsonIgnore
	private Date agreedDeliveryDate;//约定送货日期
	private String outFactoryNO;//出厂编号
	private String brand;//品牌
	private String founder;//创建人
	private String confirmReceiveDate;//确认收货日期
	private String confirmPeople;//确认人
	private String  note;//备注
	
	public String getMeasurementNo() {
		return measurementNo;
	}
	public void setMeasurementNo(String measurementNo) {
		this.measurementNo = measurementNo;
	}
	public String getMeasurementName() {
		return measurementName;
	}
	public void setMeasurementName(String measurementName) {
		this.measurementName = measurementName;
	}
	public String getMeasurementSpecification() {
		return measurementSpecification;
	}
	public void setMeasurementSpecification(String measurementSpecification) {
		this.measurementSpecification = measurementSpecification;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSecondaryType() {
		return secondaryType;
	}
	public void setSecondaryType(String secondaryType) {
		this.secondaryType = secondaryType;
	}
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public Date getAgreedDeliveryDate() {
		return agreedDeliveryDate;
	}
	public void setAgreedDeliveryDate(Date agreedDeliveryDate) {
		this.agreedDeliveryDate = agreedDeliveryDate;
	}
	public String getOutFactoryNO() {
		return outFactoryNO;
	}
	public void setOutFactoryNO(String outFactoryNO) {
		this.outFactoryNO = outFactoryNO;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getFounder() {
		return founder;
	}
	public void setFounder(String founder) {
		this.founder = founder;
	}
	public String getConfirmReceiveDate() {
		return confirmReceiveDate;
	}
	public void setConfirmReceiveDate(String confirmReceiveDate) {
		this.confirmReceiveDate = confirmReceiveDate;
	}
	public String getConfirmPeople() {
		return confirmPeople;
	}
	public void setConfirmPeople(String confirmPeople) {
		this.confirmPeople = confirmPeople;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}

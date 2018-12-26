package com.ambition.gsm.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;


/**
 * 在途量检具基类(ENTITY)
 * @author 张顺治
 *
 */
@Entity
@Table(name = "INTRANSITED_EQUIPMENT_BASE")
public class IntransitedEquipmentBase extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String measurementNo;//量检具编号
	private String measurementName;//量检具名称
	private String measurementSpecification;//型号/规格
	private String manufacturer;//生产商
	private String measurementType;//类别
	private String secondaryClassification;//二级分类
	
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
	public String getMeasurementType() {
		return measurementType;
	}
	public void setMeasurementType(String measurementType) {
		this.measurementType = measurementType;
	}
	public String getSecondaryClassification() {
		return secondaryClassification;
	}
	public void setSecondaryClassification(String secondaryClassification) {
		this.secondaryClassification = secondaryClassification;
	}
	
}

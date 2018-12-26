package com.ambition.gsm.entity;

import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 类名:检验标准文件
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2019-11-25 发布
 */
@Entity
@Table(name="GSM_STANDARD_ATTACH")
public class GsmStandardAttach extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String measurementName;//器具名称
	private String measurementSpecification;//器具型号/规格
	private String manufacturer;//生产厂家 
	private String fileName;//上传时的文件名称
	private Blob blobValue;//检验标准文件
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Blob getBlobValue() {
		return blobValue;
	}
	public void setBlobValue(Blob blobValue) {
		this.blobValue = blobValue;
	}

	
}

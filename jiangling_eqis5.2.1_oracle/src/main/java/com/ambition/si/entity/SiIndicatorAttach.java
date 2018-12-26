package com.ambition.si.entity;

import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 类名:检验标准附件
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2013-12-11 发布
 */
@Entity
@Table(name="SI_INDICATOR_ATTACH")
public class SiIndicatorAttach extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String model;//机种		
	private String modelName;//机种名称
	private String fileName;//上传时的文件名称
	private Blob blobValue;//检验标准文件

	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
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

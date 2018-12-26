package com.ambition.util.useFile.entity;

import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 文件 
 */
@Entity
@Table(name = "MFG_USE_FILE")
public class UseFile extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String fileName;//文件名称
	private String type;//文件类型
	private Long fileSize;//文件大小
	private Blob blobValue;//文件
	private Boolean isUse = false;//是否正在使用
	private String realFileName;//真实文件名称，存储在硬盘上的名字
	private String path;//文件路径
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	
	public Blob getBlobValue() {
		return blobValue;
	}
	public void setBlobValue(Blob blob) {
		this.blobValue = blob;
	}
	public Boolean getIsUse() {
		return isUse;
	}
	public void setIsUse(Boolean isUse) {
		this.isUse = isUse;
	}
	public String getRealFileName() {
		return realFileName;
	}
	public void setRealFileName(String realFileName) {
		this.realFileName = realFileName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}

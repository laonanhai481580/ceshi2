package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
/**
 * 类名:供应商调查表台账
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月9日 发布
 */
@Entity
@Table(name = "SUPPLIER_SURVEY")
public class SupplierSurvey extends IdEntity {

		/**
		  *SupplierSurvey.java2016年10月9日
		 */
	private static final long serialVersionUID = 1L;
	private String supplierName;//供应商名称
	private String supplyMaterialCode;//供应物料号
	private String supplierAddress;//供应商地址
	private String conclusion;//结论
	private Date surveyDate;//调查日期
	private String fileName;//调查表
	private String remark;//备注
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplyMaterialCode() {
		return supplyMaterialCode;
	}
	public void setSupplyMaterialCode(String supplyMaterialCode) {
		this.supplyMaterialCode = supplyMaterialCode;
	}
	public String getSupplierAddress() {
		return supplierAddress;
	}
	public void setSupplierAddress(String supplierAddress) {
		this.supplierAddress = supplierAddress;
	}
	public String getConclusion() {
		return conclusion;
	}
	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}
	public Date getSurveyDate() {
		return surveyDate;
	}
	public void setSurveyDate(Date surveyDate) {
		this.surveyDate = surveyDate;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}

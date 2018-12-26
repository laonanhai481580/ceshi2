package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;
/**
 * 
 * 类名:每年两次被评为D级的供应商数据
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2017年7月5日 发布
 */
@Entity
@Table(name = "SUPPLIER_CANCLE_DATAS")
public class SupplierCancleDatas extends  WorkflowIdEntity{

		/**
		  *SupplierCancleDatas.java2017年7月5日
		 */
	private static final long serialVersionUID = 1L;
	private Long supplierId;
	private String supplierCode;
	private String supplierName;
	private String cycle;//评价周期
	private String materialType;//物料类别
	private Integer evaluateYear;//评价年份
	private Boolean haveCancled=false;
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public Integer getEvaluateYear() {
		return evaluateYear;
	}
	public void setEvaluateYear(Integer evaluateYear) {
		this.evaluateYear = evaluateYear;
	}
	public Boolean getHaveCancled() {
		return haveCancled;
	}
	public void setHaveCancled(Boolean haveCancled) {
		this.haveCancled = haveCancled;
	}
	
}

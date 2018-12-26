package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="SUPPLIER_ADMIT_BASICS")
public class SupplierAdmitBasics extends IdEntity{

		/**
		  *SupplierAdmitFile.java2018年3月28日
		 */
	private static final long serialVersionUID = 1L;
	private String materialSort;//材料类别
	private String auditDepartment;//审核部门
	private String materialName;//资料名称
	private String materialFile;//文件
	private String remark;//备注
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "FK_MATERIAL_TYPE_NO")
	private SupplierAdmitClass supplierAdmitClass;
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAuditDepartment() {
		return auditDepartment;
	}
	public void setAuditDepartment(String auditDepartment) {
		this.auditDepartment = auditDepartment;
	}
	public String getMaterialFile() {
		return materialFile;
	}
	public void setMaterialFile(String materialFile) {
		this.materialFile = materialFile;
	}
	public String getMaterialSort() {
		return materialSort;
	}
	public void setMaterialSort(String materialSort) {
		this.materialSort = materialSort;
	}
	public SupplierAdmitClass getSupplierAdmitClass() {
		return supplierAdmitClass;
	}
	public void setSupplierAdmitClass(SupplierAdmitClass supplierAdmitClass) {
		this.supplierAdmitClass = supplierAdmitClass;
	}
	
}

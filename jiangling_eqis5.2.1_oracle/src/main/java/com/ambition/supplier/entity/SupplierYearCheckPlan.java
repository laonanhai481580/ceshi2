package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "SUPPLIER_YEAR_CHECK_PLAN")
public class SupplierYearCheckPlan extends IdEntity{

		/**
		  *SupplierYearCheckPlan.java2016年10月31日
		 */
	private static final long serialVersionUID = 1L;
	private Date planDate;//计划日期
	private Date designDate;//实际日期
	@ManyToOne
    @JsonIgnore
    @JoinColumn(name="FK_SUPPLIER_CHECK_ID")
	private SupplierYearCheck supplierYearCheck;
	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	public Date getDesignDate() {
		return designDate;
	}
	public void setDesignDate(Date designDate) {
		this.designDate = designDate;
	}
	public SupplierYearCheck getSupplierYearCheck() {
		return supplierYearCheck;
	}
	public void setSupplierYearCheck(SupplierYearCheck supplierYearCheck) {
		this.supplierYearCheck = supplierYearCheck;
	}
	
}

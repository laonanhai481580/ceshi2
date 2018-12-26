package com.ambition.supplier.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "SUPPLIER_ADMIT_CLASS")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class SupplierAdmitClass extends IdEntity{

		/**
		  *SupplierAdmitFile.java2018年3月28日
		 */
	private static final long serialVersionUID = 1L;
	//private String businessUnit;//事业部
	private String supplierAdmitClass;//材料类别
	@OneToMany(mappedBy="supplierAdmitClass")
	@OrderBy("materialSort")
	@JsonIgnore
	List<SupplierAdmitBasics> supplierAdmitBasics;
	
	public String getSupplierAdmitClass() {
		return supplierAdmitClass;
	}
	public void setSupplierAdmitClass(String supplierAdmitClass) {
		this.supplierAdmitClass = supplierAdmitClass;
	}
	public List<SupplierAdmitBasics> getSupplierAdmitBasics() {
		return supplierAdmitBasics;
	}
	public void setSupplierAdmitBasics(List<SupplierAdmitBasics> supplierAdmitBasics) {
		this.supplierAdmitBasics = supplierAdmitBasics;
	}
	
}

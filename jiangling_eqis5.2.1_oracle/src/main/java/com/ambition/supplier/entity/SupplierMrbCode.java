package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
@Entity
@Table(name = "SUPPLIER_MRB_CODE")
public class SupplierMrbCode extends IdEntity{

		/**
		  *SupplierMrbCode.java2017年1月13日
		 */
	private static final long serialVersionUID = 1L;

	private String mrbCode;

	public String getMrbCode() {
		return mrbCode;
	}

	public void setMrbCode(String mrbCode) {
		this.mrbCode = mrbCode;
	}
	
}

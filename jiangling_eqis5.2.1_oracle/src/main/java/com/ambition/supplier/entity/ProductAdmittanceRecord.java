package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ambition.product.base.IdEntity;
import com.norteksoft.wf.engine.client.ExtendField;

/**
 * 供应商准入的记录
 * @author 赵骏
 *
 */
@Entity
@Table(name = "SUPPLIER_PRODUCT_ADMITTANCE_RECORD")
public class ProductAdmittanceRecord extends IdEntity {
	private static final long serialVersionUID = -5675690918786197495L;
	@ManyToOne
	@JoinColumn(name = "FK_SUPPLIER_ID")
	private Supplier supplier;//供应商
	private String materialType;//物料类别
	private String code;//物料编号
	private String name;//物料名称
	private String importance;//重要度
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date admissionDate;//准入日期
	
	private String admittanceMan;//批准人员
	
	@Embedded
	private ExtendField extendField;
	
	public Supplier getSupplier() {
		return supplier;
	}


	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}


	public String getMaterialType() {
		return materialType;
	}


	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getImportance() {
		return importance;
	}


	public void setImportance(String importance) {
		this.importance = importance;
	}

	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	public String getAdmittanceMan() {
		return admittanceMan;
	}

	public void setAdmittanceMan(String admittanceMan) {
		this.admittanceMan = admittanceMan;
	}

	public ExtendField getExtendField() {
		return extendField;
	}

	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}
	
	public String toString(){
		return "供应商质量管理：供应商产品名称"+this.name+",物料类别"+this.materialType;
	}
}

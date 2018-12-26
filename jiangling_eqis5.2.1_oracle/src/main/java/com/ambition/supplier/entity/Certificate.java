package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.norteksoft.wf.engine.client.ExtendField;

/**
 * 体系证书
 * @author ambition-zhaojun
 *
 */
@Entity
@Table(name = "SUPPLIER_CERTIFICATE")
public class Certificate extends IdEntity {
	private static final long serialVersionUID = 1621768121887327213L;
	@ManyToOne
	@JoinColumn(name = "FK_SUPPLIER_ID")
	private Supplier supplier;//供应商
	private String name;//体系名称
	private String organization;//颁发机构
	private Date certificationDate;//认证日期
	private Date invalidationDate;//无效日期
	private String certificationFiles;//证书
	
	@Embedded
	private ExtendField extendField;
	
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public Date getCertificationDate() {
		return certificationDate;
	}
	public void setCertificationDate(Date certificationDate) {
		this.certificationDate = certificationDate;
	}
	public Date getInvalidationDate() {
		return invalidationDate;
	}
	public void setInvalidationDate(Date invalidationDate) {
		this.invalidationDate = invalidationDate;
	}
	
	public String getCertificationFiles() {
		return certificationFiles;
	}
	public void setCertificationFiles(String certificationFiles) {
		this.certificationFiles = certificationFiles;
	}
	public ExtendField getExtendField() {
		return extendField;
	}
	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}
	public String toString(){
		return "供应商质量管理：体系证书    体系名称"+this.name;
	}
}

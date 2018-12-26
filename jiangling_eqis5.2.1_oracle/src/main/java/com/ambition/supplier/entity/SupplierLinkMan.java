package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 供应商联系人
 * @author 赵骏
 *
 */
@Entity
@Table(name = "SUPPLIER_SUPPLIER_LINK_MAN")
public class SupplierLinkMan extends IdEntity {
	private static final long serialVersionUID = 4518308827240189057L;
	@ManyToOne
	@JoinColumn(name = "FK_SUPPLIER_ID")
	private Supplier supplier;
	private String linkMan;//联系人
	private String linkPhone;//联系电话
	private String mobilephone;//手机
	private String fax;//传真
	private String postCode;//邮编
	private String email;//邮箱地址
	private String linkManDepartment;//联系人部门
	private String linkManDuty;//联系人职务
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getLinkPhone() {
		return linkPhone;
	}
	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLinkManDepartment() {
		return linkManDepartment;
	}
	public void setLinkManDepartment(String linkManDepartment) {
		this.linkManDepartment = linkManDepartment;
	}
	public String getLinkManDuty() {
		return linkManDuty;
	}
	public void setLinkManDuty(String linkManDuty) {
		this.linkManDuty = linkManDuty;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
}

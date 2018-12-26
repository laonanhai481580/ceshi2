package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 类名:供应商调查表联系人信息
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2013-4-28 发布
 */
@Entity
@Table(name = "SUPPLIER_INVESTIGATE_CONTACT")
public class SupplierInvestigateContact extends IdEntity {
	private static final long serialVersionUID = 4518308827240189057L;
	@ManyToOne
	@JoinColumn(name = "FK_SUPPLIER_INVESTIGATE_ID")
	private SupplierInvestigate supplierInvestigate;
	private String department;//部门
	private String job;//职务
	private String contact;//联系人
	private String contactPhone;//联系电话
	private String contactFax;//联系传真
	private String email;//邮箱地址
	public SupplierInvestigate getSupplierInvestigate() {
		return supplierInvestigate;
	}
	public void setSupplierInvestigate(SupplierInvestigate supplierInvestigate) {
		this.supplierInvestigate = supplierInvestigate;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getContactFax() {
		return contactFax;
	}
	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
}

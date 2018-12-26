package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 类名:供应商调查表试验设备和检测手段
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2013-4-28 发布
 */
@Entity
@Table(name = "SUPPLIER_INVESTIGATE_TESTING_DEVICE")
public class SupplierInvestigateTestingDevice extends IdEntity {
	private static final long serialVersionUID = 4518308827240189057L;
	@ManyToOne
	@JoinColumn(name = "FK_SUPPLIER_INVESTIGATE_ID")
	private SupplierInvestigate supplierInvestigate;
	private String cots;//加工检测设备
	private String model;//型号
	private String vendor;//生产厂家
	public SupplierInvestigate getSupplierInvestigate() {
		return supplierInvestigate;
	}
	public void setSupplierInvestigate(SupplierInvestigate supplierInvestigate) {
		this.supplierInvestigate = supplierInvestigate;
	}
	public String getCots() {
		return cots;
	}
	public void setCots(String cots) {
		this.cots = cots;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
}

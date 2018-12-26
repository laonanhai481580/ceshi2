package com.ambition.cost.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 内部成品损失表
 * @author ambition-zjh
 *
 */
@Entity
@Table(name = "COST_INNER_PRODUCT_LOSS")
public class InnerProductLoss extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String code ;//成品机种长代码
	private String name;//品名
	private String customerCode;//客户编码
	private String customerName;//客户名称
	private String lossType;//类别
	private Integer dengji;//档次
	private Date maintenanceDate;//维护日期
	private String maintenancePerson;//维护人
	private String projectLevel;//项目档次
	private String remark;//备注
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
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getLossType() {
		return lossType;
	}
	public void setLossType(String lossType) {
		this.lossType = lossType;
	}

	public Integer getDengji() {
		return dengji;
	}
	public void setDengji(Integer dengji) {
		this.dengji = dengji;
	}
	public Date getMaintenanceDate() {
		return maintenanceDate;
	}
	public void setMaintenanceDate(Date maintenanceDate) {
		this.maintenanceDate = maintenanceDate;
	}
	public String getMaintenancePerson() {
		return maintenancePerson;
	}
	
	
	public String getProjectLevel() {
		return projectLevel;
	}
	public void setProjectLevel(String projectLevel) {
		this.projectLevel = projectLevel;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setMaintenancePerson(String maintenancePerson) {
		this.maintenancePerson = maintenancePerson;
	}
}

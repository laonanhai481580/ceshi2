package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**    
 * SupplierDelivery.java
 * @authorBy YUKE
 *
 */
@Entity
@Table(name = "SUPPLIER_DELIVERY")
public class SupplierDelivery extends IdEntity {
	private static final long serialVersionUID = 1L;
	private Date startDate;//开始日期
	private Date endDate;//截止日期
	private Integer month;
	private String supplierCode;
	private String supplierName;
	private Integer dueAmount;//到期订单批数
	private Integer delayAmount;//延期交货批数
	private Integer onTimeAount;//准时交货批数
	private Double onTimeRate;//准时交货率
	@Column(length=10)
	private String level;//等级
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public Integer getDueAmount() {
		return dueAmount;
	}
	public void setDueAmount(Integer dueAmount) {
		this.dueAmount = dueAmount;
	}
	public Integer getDelayAmount() {
		return delayAmount;
	}
	public void setDelayAmount(Integer delayAmount) {
		this.delayAmount = delayAmount;
	}
	public Integer getOnTimeAount() {
		return onTimeAount;
	}
	public void setOnTimeAount(Integer onTimeAount) {
		this.onTimeAount = onTimeAount;
	}
	public Double getOnTimeRate() {
		return onTimeRate;
	}
	public void setOnTimeRate(Double onTimeRate) {
		this.onTimeRate = onTimeRate;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
}

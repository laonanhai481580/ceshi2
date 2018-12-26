package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ambition.product.base.IdEntity;

/**
 * 供应的产品准入记录
 * @author 赵骏
 *
 */
@Entity
@Table(name = "SUPPLIER_SUPPLY_PRODUCT_ADMITTANCE_RECORD")
public class SupplyProductAdmittanceRecord extends IdEntity {
	private static final long serialVersionUID = 1621768121887327214L;
	private String developmentState;//开发状态
	private String state;//状态
	private Long targetId;//对象的ID:InspectionReport,AppraisalReport
	private Long supplyProductId;//供应产品的ID
	private String operator;//操作人员
	@Temporal(TemporalType.TIMESTAMP)
	private Date executeTime;//执行时间
	
	public String getDevelopmentState() {
		return developmentState;
	}
	public void setDevelopmentState(String developmentState) {
		this.developmentState = developmentState;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	public Long getSupplyProductId() {
		return supplyProductId;
	}
	public void setSupplyProductId(Long supplyProductId) {
		this.supplyProductId = supplyProductId;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Date getExecuteTime() {
		return executeTime;
	}
	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}
}

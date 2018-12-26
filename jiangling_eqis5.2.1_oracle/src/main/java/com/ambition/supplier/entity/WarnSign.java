package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**    
 * WarnSign.java
 * @authorBy YUKE
 *
 */
@Entity
@Table(name = "SUPPLIER_WARN_SIGN")
public class WarnSign extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String supplierCode;//供应商编号
	private String supplierName;//供应商名称
	private Double goal1;
	private Double goal2;
	private Double estimateGoal;//评价得分
	private String goalRange;//得分范围
	private String estimateDegree;//评价等级
	private String waringSign;//红黄牌标识
	private String remark;//备注
	
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public Double getGoal1() {
		return goal1;
	}
	public void setGoal1(Double goal1) {
		this.goal1 = goal1;
	}
	public Double getGoal2() {
		return goal2;
	}
	public void setGoal2(Double goal2) {
		this.goal2 = goal2;
	}
	public Double getEstimateGoal() {
		return estimateGoal;
	}
	public void setEstimateGoal(Double estimateGoal) {
		this.estimateGoal = estimateGoal;
	}
	public String getGoalRange() {
		return goalRange;
	}
	public void setGoalRange(String goalRange) {
		this.goalRange = goalRange;
	}
	public String getEstimateDegree() {
		return estimateDegree;
	}
	public void setEstimateDegree(String estimateDegree) {
		this.estimateDegree = estimateDegree;
	}
	public String getWaringSign() {
		return waringSign;
	}
	public void setWaringSign(String waringSign) {
		this.waringSign = waringSign;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String toString(){
		return "供应商质量管理：红黄牌，供应商名称"+this.supplierName+"，评价得分"+this.estimateGoal;
	}
}

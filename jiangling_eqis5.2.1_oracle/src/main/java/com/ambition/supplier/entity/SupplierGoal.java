package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.ambition.product.base.IdEntity;

/**    
 * SupplierGoal.java
 * @authorBy YUKE
 *
 */
@Entity
@Table(name = "SUPPLIER_SUPPLIER_GOAL")
public class SupplierGoal extends IdEntity {
	private static final long serialVersionUID = 1L;
	@Index(columnNames={"business_unit_code","supplier_code","evaluate_year"},name="supplier_evaluate_year_index")
	private Integer evaluateYear;//评价年份
	private Double totalPoints;//总分
	private String evaluateGrade;//评价等级
	private String redYellowCard;//红黄牌
	private String supplierCode;//供应商编码
	private String supplierName;//供应商名称
	private String importance;//重要度
	public Integer getEvaluateYear() {
		return evaluateYear;
	}
	public void setEvaluateYear(Integer evaluateYear) {
		this.evaluateYear = evaluateYear;
	}
	public Double getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(Double totalPoints) {
		this.totalPoints = totalPoints;
	}
	public String getEvaluateGrade() {
		return evaluateGrade;
	}
	public void setEvaluateGrade(String evaluateGrade) {
		this.evaluateGrade = evaluateGrade;
	}
	public String getRedYellowCard() {
		return redYellowCard;
	}
	public void setRedYellowCard(String redYellowCard) {
		this.redYellowCard = redYellowCard;
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
	public String getImportance() {
		return importance;
	}
	public void setImportance(String importance) {
		this.importance = importance;
	}
	public String toString(){
		return "供应商质量管理：供应商得分，供应商名称"+this.getSupplierName()+"，评价总分"+this.totalPoints;
	}
}

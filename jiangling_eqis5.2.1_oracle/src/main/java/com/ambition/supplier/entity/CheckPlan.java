package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 稽查计划
 * @author 赵骏
 *
 */
@Entity
@Table(name = "SUPPLIER_CHECK_PLAN")
public class CheckPlan extends IdEntity {
	private static final long serialVersionUID = 1621768121887327213L;
	private String planCode;//计划编号
	private Long supplierId;//供应商编号
	private String supplierCode;//供应商编号
	private String supplierName;//供应商名称
	private String supplyProducts;//主要的产品
	private String supplierImportance;//供应商重要度
	private String checkBomCode;//重点检查的零部件/物料编号
	private String checkBomName;//重点检查的零部件/物料名称
	private String materialType;//重点检查的零部件/物料类型
	private Date planDate;//计划检查的日期
	private String auditGroupLeader;//审核组长
	private String recommendReason;//推荐理由
	private String isOntime;//是否按时完成计划
	
	@ManyToOne
	@JoinColumn(name="FK_CHECK_REPORT_ID")
	private CheckReport checkReport;//检查的报告
	
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierImportance() {
		return supplierImportance;
	}
	public void setSupplierImportance(String supplierImportance) {
		this.supplierImportance = supplierImportance;
	}
	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	public String getAuditGroupLeader() {
		return auditGroupLeader;
	}
	public void setAuditGroupLeader(String auditGroupLeader) {
		this.auditGroupLeader = auditGroupLeader;
	}
	public String getRecommendReason() {
		return recommendReason;
	}
	public void setRecommendReason(String recommendReason) {
		this.recommendReason = recommendReason;
	}
	public CheckReport getCheckReport() {
		return checkReport;
	}
	public void setCheckReport(CheckReport checkReport) {
		this.checkReport = checkReport;
	}
	public String getCheckBomCode() {
		return checkBomCode;
	}
	public void setCheckBomCode(String checkBomCode) {
		this.checkBomCode = checkBomCode;
	}
	public String getCheckBomName() {
		return checkBomName;
	}
	public void setCheckBomName(String checkBomName) {
		this.checkBomName = checkBomName;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplyProducts() {
		return supplyProducts;
	}
	public void setSupplyProducts(String supplyProducts) {
		this.supplyProducts = supplyProducts;
	}
	public String getIsOntime() {
		return isOntime;
	}
	public void setIsOntime(String isOntime) {
		this.isOntime = isOntime;
	}
	public String toString(){
		return "供应商质量管理：监察计划，计划编号"+this.planCode+",供应商"+this.supplierName;
	}
}

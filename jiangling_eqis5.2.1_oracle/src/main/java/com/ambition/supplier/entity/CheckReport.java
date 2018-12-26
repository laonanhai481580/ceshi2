package com.ambition.supplier.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;
import com.ambition.supplier.utils.DateUtil;

/**
 * 稽查报告
 * @author 赵骏
 *
 */
@Entity
@Table(name = "SUPPLIER_CHECK_REPORT")
public class CheckReport extends IdEntity {
	private static final long serialVersionUID = 1621768121887327213L;
	private String code;//编号
	private String planCode;//稽查计划号
	private String auditReason;//审核理由
	private String modelSpecification;//机型号规格
	private Long supplierId;//供应商Id
	private String supplierCode;//供应商编号
	private String supplierName;//供应商名称
	@Temporal(TemporalType.DATE)
	private Date checkDate;//实际监察的日期
	private String checkBomCode;//零件编号
	private String checkBomName;//零件名称
	private Double totalFee;//总分
	private Double realFee;//实际得分
	private String remark;//备注
	private String groupMembers;//小组成员
	@OneToMany(mappedBy="checkReport",cascade=CascadeType.ALL)
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OrderBy("orderNum asc")
	private List<ReportGrade> reportGrades;//评分项目
	
	@OneToMany(mappedBy="checkReport",cascade=CascadeType.ALL)
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OrderBy("orderNum asc")
	private List<ReportInput> reportInputs;//评分项目
	
	@ManyToOne
	@JoinColumn(name="FK_CHECK_PLAN_ID")
	private CheckPlan checkPlan;//稽查计划
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAuditReason() {
		return auditReason;
	}
	public void setAuditReason(String auditReason) {
		this.auditReason = auditReason;
	}
	public String getModelSpecification() {
		return modelSpecification;
	}
	public void setModelSpecification(String modelSpecification) {
		this.modelSpecification = modelSpecification;
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
	public Date getCheckDate() {
		return checkDate;
	}
	public String getCheckDateStr(){
		if(checkDate != null){
			return DateUtil.formateDateStr(checkDate);
		}else{
			return "";
		}
	}
	
	public List<ReportGrade> getReportGrades() {
		return reportGrades;
	}
	public void setReportGrades(List<ReportGrade> reportGrades) {
		this.reportGrades = reportGrades;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public CheckPlan getCheckPlan() {
		return checkPlan;
	}
	
	public void setCheckPlan(CheckPlan checkPlan) {
		this.checkPlan = checkPlan;
	}
	
	public String getGroupMembers() {
		return groupMembers;
	}
	public void setGroupMembers(String groupMembers) {
		this.groupMembers = groupMembers;
	}
	
	public Double getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}
	public Double getRealFee() {
		return realFee;
	}
	public void setRealFee(Double realFee) {
		this.realFee = realFee;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
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
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	public List<ReportInput> getReportInputs() {
		return reportInputs;
	}
	public void setReportInputs(List<ReportInput> reportInputs) {
		this.reportInputs = reportInputs;
	}
	public String toString(){
		return "供应商质量管理：监察报告，报告编号"+this.code+",供应商"+this.supplierName;
	}
}

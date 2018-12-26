package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;

/**
 * 类名:供应商变更
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月12日 发布
 */
@Entity
@Table(name = "SUPPLIER_CHANGE")
public class SupplierChange extends  WorkflowIdEntity {

		/**
		  *SupplierChange.java2016年10月12日
		 */
	private static final long serialVersionUID = 1L;
	private String formNo;//
	private String applicant;//申请公司
	private Date applyingDate;//申请日期
	private String reason;//申请事由
	private String businessUnit;//事业部
	private String materialName;//材料名
	private String materialCode;//物料编码
	private String applyingProject;//适用机型
	private String projectChange;//变更项目
	private String changeLevel;//变更等级
	private String contentBefore;//变更前内容
	private String beforeFile;
	private String contentAfter;//变更后内容
	private String afterFile;
	private String assessmentReport;//评估报告
	private String reportFile;
	private String procurementOpinion;//采购部意见
	private String procurementProcesser;//采购部处理人
	private String procurementProcesserLog;
	private String qualityOpinion;//品质部意见
	private String qualityFile;
	private String qualityProcesser;//品质部经办人
	private String qualityProcesserLog;
	private String qualityChecker;//品质部审核人
	private String qualityCheckerLog;
	private String developOpinion;//研发部意见
	private String developFile;
	private String developProcesser;//研发部经办人
	private String developProcessLog;
	private String developChecker;//研发部审核人
	private String developCheckLog;
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	
	public Date getApplyingDate() {
		return applyingDate;
	}
	public void setApplyingDate(Date applyingDate) {
		this.applyingDate = applyingDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getApplyingProject() {
		return applyingProject;
	}
	public void setApplyingProject(String applyingProject) {
		this.applyingProject = applyingProject;
	}
	public String getProjectChange() {
		return projectChange;
	}
	public void setProjectChange(String projectChange) {
		this.projectChange = projectChange;
	}
	public String getChangeLevel() {
		return changeLevel;
	}
	public void setChangeLevel(String changeLevel) {
		this.changeLevel = changeLevel;
	}
	public String getContentBefore() {
		return contentBefore;
	}
	public void setContentBefore(String contentBefore) {
		this.contentBefore = contentBefore;
	}
	public String getBeforeFile() {
		return beforeFile;
	}
	public void setBeforeFile(String beforeFile) {
		this.beforeFile = beforeFile;
	}
	public String getContentAfter() {
		return contentAfter;
	}
	public void setContentAfter(String contentAfter) {
		this.contentAfter = contentAfter;
	}
	public String getAfterFile() {
		return afterFile;
	}
	public void setAfterFile(String afterFile) {
		this.afterFile = afterFile;
	}
	public String getProcurementOpinion() {
		return procurementOpinion;
	}
	public void setProcurementOpinion(String procurementOpinion) {
		this.procurementOpinion = procurementOpinion;
	}
	public String getProcurementProcesser() {
		return procurementProcesser;
	}
	public void setProcurementProcesser(String procurementProcesser) {
		this.procurementProcesser = procurementProcesser;
	}
	public String getProcurementProcesserLog() {
		return procurementProcesserLog;
	}
	public void setProcurementProcesserLog(String procurementProcesserLog) {
		this.procurementProcesserLog = procurementProcesserLog;
	}
	public String getQualityOpinion() {
		return qualityOpinion;
	}
	public void setQualityOpinion(String qualityOpinion) {
		this.qualityOpinion = qualityOpinion;
	}
	public String getQualityFile() {
		return qualityFile;
	}
	public void setQualityFile(String qualityFile) {
		this.qualityFile = qualityFile;
	}
	public String getQualityProcesser() {
		return qualityProcesser;
	}
	public void setQualityProcesser(String qualityProcesser) {
		this.qualityProcesser = qualityProcesser;
	}
	public String getQualityProcesserLog() {
		return qualityProcesserLog;
	}
	public void setQualityProcesserLog(String qualityProcesserLog) {
		this.qualityProcesserLog = qualityProcesserLog;
	}
	public String getQualityChecker() {
		return qualityChecker;
	}
	public void setQualityChecker(String qualityChecker) {
		this.qualityChecker = qualityChecker;
	}
	public String getQualityCheckerLog() {
		return qualityCheckerLog;
	}
	public void setQualityCheckerLog(String qualityCheckerLog) {
		this.qualityCheckerLog = qualityCheckerLog;
	}
	public String getDevelopOpinion() {
		return developOpinion;
	}
	public void setDevelopOpinion(String developOpinion) {
		this.developOpinion = developOpinion;
	}
	public String getDevelopProcesser() {
		return developProcesser;
	}
	public void setDevelopProcesser(String developProcesser) {
		this.developProcesser = developProcesser;
	}
	public String getDevelopProcessLog() {
		return developProcessLog;
	}
	public void setDevelopProcessLog(String developProcessLog) {
		this.developProcessLog = developProcessLog;
	}
	public String getDevelopFile() {
		return developFile;
	}
	public void setDevelopFile(String developFile) {
		this.developFile = developFile;
	}
	public String getDevelopChecker() {
		return developChecker;
	}
	public void setDevelopChecker(String developChecker) {
		this.developChecker = developChecker;
	}
	public String getDevelopCheckLog() {
		return developCheckLog;
	}
	public void setDevelopCheckLog(String developCheckLog) {
		this.developCheckLog = developCheckLog;
	}
	public String getAssessmentReport() {
		return assessmentReport;
	}
	public void setAssessmentReport(String assessmentReport) {
		this.assessmentReport = assessmentReport;
	}
	public String getReportFile() {
		return reportFile;
	}
	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	
}

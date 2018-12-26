package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;

/**
 * 类名:供应商不符合项改善报告
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年11月1日 发布
 */
@Entity
@Table(name = "SUPPLIER_AUDIT_IMPROVE")
public class SupplierAuditImprove extends  WorkflowIdEntity{

	private String formNo;
	private String supplierName;
	private String supplierCode;
	private String supplierDeputy;//供应商代表
	private Date evaluateDate;//评鉴日期
	private String evaluateMan;//评鉴人员
	private String evaluateManLog;
	private String unqualifiedItem;//不合格项
	private String causeAnalysis;//原因分析
	private String tempSituation;//临时对策
	private String longSituation;//长期对策
	private String situationFile;
	private String closeCheck;//问题关闭确认
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierDeputy() {
		return supplierDeputy;
	}
	public void setSupplierDeputy(String supplierDeputy) {
		this.supplierDeputy = supplierDeputy;
	}
	public Date getEvaluateDate() {
		return evaluateDate;
	}
	public void setEvaluateDate(Date evaluateDate) {
		this.evaluateDate = evaluateDate;
	}
	public String getEvaluateMan() {
		return evaluateMan;
	}
	public void setEvaluateMan(String evaluateMan) {
		this.evaluateMan = evaluateMan;
	}
	public String getEvaluateManLog() {
		return evaluateManLog;
	}
	public void setEvaluateManLog(String evaluateManLog) {
		this.evaluateManLog = evaluateManLog;
	}
	public String getUnqualifiedItem() {
		return unqualifiedItem;
	}
	public void setUnqualifiedItem(String unqualifiedItem) {
		this.unqualifiedItem = unqualifiedItem;
	}
	public String getCauseAnalysis() {
		return causeAnalysis;
	}
	public void setCauseAnalysis(String causeAnalysis) {
		this.causeAnalysis = causeAnalysis;
	}
	public String getTempSituation() {
		return tempSituation;
	}
	public void setTempSituation(String tempSituation) {
		this.tempSituation = tempSituation;
	}
	public String getLongSituation() {
		return longSituation;
	}
	public void setLongSituation(String longSituation) {
		this.longSituation = longSituation;
	}
	public String getSituationFile() {
		return situationFile;
	}
	public void setSituationFile(String situationFile) {
		this.situationFile = situationFile;
	}
	public String getCloseCheck() {
		return closeCheck;
	}
	public void setCloseCheck(String closeCheck) {
		this.closeCheck = closeCheck;
	}
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	
	
}

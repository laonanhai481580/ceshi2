package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;

/**
 * 类名:供应商材料承认
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月17日 发布
 */
@Entity
@Table(name = "SUPPLIER_MATERIAL_ADMIT")
public class SupplierMaterialAdmit extends  WorkflowIdEntity{

		/**
		  *SupplierMaterialAdmit.java2016年10月19日
		 */
	private static final long serialVersionUID = 1L;
	private String formNo;
	private String productVersion;//规格型号版本号
	private String productName;//产品名称
	private String materialName;//物料名称
	private String materialCode;//物料代码
	private Date applyDate;//申请日期
	private String supplierName;//供应商
	private String supplierEmail;//供应商邮箱
	private String item;//项目
	private String admitProject;//承认项目
	private String purchaseProcesser;//采购部经办人
	private String purchaseProcessLog;//
	private String supplier;//指定供应商
	private String supplierLoginName;//供应商登录名
	private String checkDeptMans;//会签部门人员
	private String checkDeptMansLog;//会签人登陆名
	private String supplierFile;//ME文档附件
	private String pmChecker;//pm核准
	private String pmCheckerLog;//
	private String developChecker;//研发核准
	private String developCheckerLog;//研发核准人登录名
	private String qualityChecker;//品保核准
	private String qualityCheckerLog;//品保核准人登陆名
	private String qsChecker;//QS核准
	private String qsCheckerLog;//QS核准登录名
	private String sqeChecker;//SQE核准
	private String sqeCheckerLog;//SQE核准登录名
	private String admitStatus;//承认状态
	private String countersignPM;//会签意见PM
	private String countersignRD;//会签意见研发
	private String countersignQA;//会签意见品保
	private String countersignQS;//会签意见QS
	private String countersignSQE;//会签意见SQE
	private String engineeringDrawing;//工程画图
	private String checkoutReport;//出货检验报告
	private String packing;//包装方式
	private String codeExplain;//代码说明
	private String environmentalMaterial;//环保资料
	private String bomFile;//文件
	private String surveyFile;//调查文件
	private String  docControl;//文控
	private String copyMan;//抄送人
	private String copyManLogin;//抄送人登录名
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getProductVersion() {
		return productVersion;
	}
	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getAdmitProject() {
		return admitProject;
	}
	public void setAdmitProject(String admitProject) {
		this.admitProject = admitProject;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getCheckDeptMans() {
		return checkDeptMans;
	}
	public void setCheckDeptMans(String checkDeptMans) {
		this.checkDeptMans = checkDeptMans;
	}
	public String getCheckDeptMansLog() {
		return checkDeptMansLog;
	}
	public void setCheckDeptMansLog(String checkDeptMansLog) {
		this.checkDeptMansLog = checkDeptMansLog;
	}
	public String getSupplierFile() {
		return supplierFile;
	}
	public void setSupplierFile(String supplierFile) {
		this.supplierFile = supplierFile;
	}
	public String getPmChecker() {
		return pmChecker;
	}
	public void setPmChecker(String pmChecker) {
		this.pmChecker = pmChecker;
	}
	public String getPmCheckerLog() {
		return pmCheckerLog;
	}
	public void setPmCheckerLog(String pmCheckerLog) {
		this.pmCheckerLog = pmCheckerLog;
	}
	public String getDevelopChecker() {
		return developChecker;
	}
	public void setDevelopChecker(String developChecker) {
		this.developChecker = developChecker;
	}
	public String getDevelopCheckerLog() {
		return developCheckerLog;
	}
	public void setDevelopCheckerLog(String developCheckerLog) {
		this.developCheckerLog = developCheckerLog;
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
	public String getQsChecker() {
		return qsChecker;
	}
	public void setQsChecker(String qsChecker) {
		this.qsChecker = qsChecker;
	}
	public String getQsCheckerLog() {
		return qsCheckerLog;
	}
	public void setQsCheckerLog(String qsCheckerLog) {
		this.qsCheckerLog = qsCheckerLog;
	}
	public String getSupplierLoginName() {
		return supplierLoginName;
	}
	public void setSupplierLoginName(String supplierLoginName) {
		this.supplierLoginName = supplierLoginName;
	}
	public String getPurchaseProcesser() {
		return purchaseProcesser;
	}
	public void setPurchaseProcesser(String purchaseProcesser) {
		this.purchaseProcesser = purchaseProcesser;
	}
	public String getPurchaseProcessLog() {
		return purchaseProcessLog;
	}
	public void setPurchaseProcessLog(String purchaseProcessLog) {
		this.purchaseProcessLog = purchaseProcessLog;
	}
	public String getAdmitStatus() {
		return admitStatus;
	}
	public void setAdmitStatus(String admitStatus) {
		this.admitStatus = admitStatus;
	}
	public String getCountersignPM() {
		return countersignPM;
	}
	public void setCountersignPM(String countersignPM) {
		this.countersignPM = countersignPM;
	}
	public String getCountersignRD() {
		return countersignRD;
	}
	public void setCountersignRD(String countersignRD) {
		this.countersignRD = countersignRD;
	}
	public String getCountersignQA() {
		return countersignQA;
	}
	public void setCountersignQA(String countersignQA) {
		this.countersignQA = countersignQA;
	}
	public String getCountersignQS() {
		return countersignQS;
	}
	public void setCountersignQS(String countersignQS) {
		this.countersignQS = countersignQS;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getSupplierEmail() {
		return supplierEmail;
	}
	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}
	public String getEngineeringDrawing() {
		return engineeringDrawing;
	}
	public void setEngineeringDrawing(String engineeringDrawing) {
		this.engineeringDrawing = engineeringDrawing;
	}
	public String getCheckoutReport() {
		return checkoutReport;
	}
	public void setCheckoutReport(String checkoutReport) {
		this.checkoutReport = checkoutReport;
	}
	public String getPacking() {
		return packing;
	}
	public void setPacking(String packing) {
		this.packing = packing;
	}
	public String getCodeExplain() {
		return codeExplain;
	}
	public void setCodeExplain(String codeExplain) {
		this.codeExplain = codeExplain;
	}
	public String getEnvironmentalMaterial() {
		return environmentalMaterial;
	}
	public void setEnvironmentalMaterial(String environmentalMaterial) {
		this.environmentalMaterial = environmentalMaterial;
	}
	public String getBomFile() {
		return bomFile;
	}
	public void setBomFile(String bomFile) {
		this.bomFile = bomFile;
	}
	public String getSurveyFile() {
		return surveyFile;
	}
	public void setSurveyFile(String surveyFile) {
		this.surveyFile = surveyFile;
	}
	public String getCountersignSQE() {
		return countersignSQE;
	}
	public void setCountersignSQE(String countersignSQE) {
		this.countersignSQE = countersignSQE;
	}
	public String getSqeChecker() {
		return sqeChecker;
	}
	public void setSqeChecker(String sqeChecker) {
		this.sqeChecker = sqeChecker;
	}
	public String getSqeCheckerLog() {
		return sqeCheckerLog;
	}
	public void setSqeCheckerLog(String sqeCheckerLog) {
		this.sqeCheckerLog = sqeCheckerLog;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getDocControl() {
		return docControl;
	}
	public void setDocControl(String docControl) {
		this.docControl = docControl;
	}
	public String getCopyMan() {
		return copyMan;
	}
	public void setCopyMan(String copyMan) {
		this.copyMan = copyMan;
	}
	public String getCopyManLogin() {
		return copyManLogin;
	}
	public void setCopyManLogin(String copyManLogin) {
		this.copyManLogin = copyManLogin;
	}
	
}

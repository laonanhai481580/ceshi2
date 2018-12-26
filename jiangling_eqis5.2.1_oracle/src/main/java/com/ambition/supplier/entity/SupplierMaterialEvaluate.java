package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;

/**
 * 类名:样件评估表
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月14日 发布
 */
@Entity
@Table(name = "SUPPLIER_MATERIAL_EVALUATE")
public class SupplierMaterialEvaluate extends  WorkflowIdEntity{

		/**
		  *SupplierMaterialEvaluate.java2016年10月17日
		 */
	private static final long serialVersionUID = 1L;
	private String formNo;
	private Long transactionId;//事物ID 样件评估接口
	private String transNumber;//单号；样件评估接口
	private String degreeLevel;//紧急程度
	private String productType;//产品型号
	private String materialType;//物料类别
	private String materialName;//物料名称
	private String materialCode;//物料编号
	private String currentTransactor;//流程当前环节办理人
	private Date workFlowStageTime;//流程流转时间
	private String supplier;
	private String supplierCode;//供应商编号
	private String supplierEmail;
	private String qualifiedSupplier;//是否合格供应商
	private Date sendInspectionDate;//送检日期
	private Integer sendInspectionTimes;//送检次数
	private Double materialAmount;//样品数量
	private String sendReason;//送样原因
	private String otherReason;//其他原因---------
	private String materialEvaluate;//厂商样品评估
	private String testReportNum;//测试报告编号
	private String otherEvaluate;//其他评估------
	private String fileName;
	private String materialValidity;//样品正确性
	private String materialUseInfo;//样品使用紧急情况
	private String dept;//部门
	private String reportChecker;//审核人
	private String reportCheckerLog;//审核人登录名
	private String supplierEvaluateProject;//评估项目(厂商)
	private String supplierEvaluateStandar;//评估标准(厂商)
	private String supplierEvaluateResult;//评估结果(厂商)
//	private String supplierEvaluateDept;//评估部门(厂商)
	private String sampleEvaluateProject;//实物样件
	private String sampleEvaluateStandar;//
	private String sampleEvaluateResult;
	private String sampleEvaluateDept;
	private String rdProcesser;//RD评估人
	private String rdProcesserLog;
	private String hsfEvaluateProject;//HSF属性
	private String hsfEvaluateStandar;//
	private String hsfEvaluateResult;
	private String hsfEvaluateDept;
	private String hsfProcesser;//QS办理人
	private String hsfProcesserLog;
	private String msdsEvaluateProject;//化学品msds
	private String msdsEvaluateStandar;
	private String msdsEvaluateResult;
	private String msdsEvaluateDept;
	private String msdsProcesser;//EHS办理人
	private String msdsProcesserLog;
	private String inspectionResult;//入料检验结果
	private String inspectionReportFile;//检验报告附件
	private String outInspectionResult;//供应商出货检验报告结果
	private String inspectionReporter;//填表人
	private String inspectionReporterLog;
	private String inspectionChecker;//审核人
	private String inspectionCheckerLog;
	private String rdIfTest;//是否试做
	private String rdReportFile;//rd报告附件
	private String qsReportFile;//qs报告附件
	private String rdOpinion;//rd建议与评述
	private String qsOpinion;//qs建议与评述
	private String rdReporter;//Rd填表人
	private String rdReporterLog;
	private String labIfTest;//是否实验
	private String labTestContent;//实验内容
	private String labTestResult;//实验结果
	private String labTestNum;//实验编号
	private String labReportFile;//lab实验附件
	private String labReporter;//lab填表人
	private String labReporterLog;
	private String evaluateResult;//综合评估结果
	private String evaluateOkInfo;
	private String evaluateNgInfo;
	private String evaluateReporter;//综合评估填表人
	private String evaluateReporterLog;
	private String evaluateChecker;//审核
	private String evaluateCheckerLog;
	private String ifToManager;//是否事业部长审核
	private String managerName;//事业部长
	private String managerLoginName;//事业部长登录名
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getDegreeLevel() {
		return degreeLevel;
	}
	public void setDegreeLevel(String degreeLevel) {
		this.degreeLevel = degreeLevel;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
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
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getQualifiedSupplier() {
		return qualifiedSupplier;
	}
	public void setQualifiedSupplier(String qualifiedSupplier) {
		this.qualifiedSupplier = qualifiedSupplier;
	}
	public Date getSendInspectionDate() {
		return sendInspectionDate;
	}
	public void setSendInspectionDate(Date sendInspectionDate) {
		this.sendInspectionDate = sendInspectionDate;
	}
	
	public Integer getSendInspectionTimes() {
		return sendInspectionTimes;
	}
	public void setSendInspectionTimes(Integer sendInspectionTimes) {
		this.sendInspectionTimes = sendInspectionTimes;
	}

	public Double getMaterialAmount() {
		return materialAmount;
	}
	public void setMaterialAmount(Double materialAmount) {
		this.materialAmount = materialAmount;
	}
	public String getSendReason() {
		return sendReason;
	}
	public void setSendReason(String sendReason) {
		this.sendReason = sendReason;
	}
	public String getOtherReason() {
		return otherReason;
	}
	public void setOtherReason(String otherReason) {
		this.otherReason = otherReason;
	}
	public String getMaterialEvaluate() {
		return materialEvaluate;
	}
	public void setMaterialEvaluate(String materialEvaluate) {
		this.materialEvaluate = materialEvaluate;
	}
	public String getTestReportNum() {
		return testReportNum;
	}
	public void setTestReportNum(String testReportNum) {
		this.testReportNum = testReportNum;
	}
	public String getOtherEvaluate() {
		return otherEvaluate;
	}
	public void setOtherEvaluate(String otherEvaluate) {
		this.otherEvaluate = otherEvaluate;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getMaterialValidity() {
		return materialValidity;
	}
	public void setMaterialValidity(String materialValidity) {
		this.materialValidity = materialValidity;
	}
	public String getMaterialUseInfo() {
		return materialUseInfo;
	}
	public void setMaterialUseInfo(String materialUseInfo) {
		this.materialUseInfo = materialUseInfo;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getReportChecker() {
		return reportChecker;
	}
	public void setReportChecker(String reportChecker) {
		this.reportChecker = reportChecker;
	}
	public String getReportCheckerLog() {
		return reportCheckerLog;
	}
	public void setReportCheckerLog(String reportCheckerLog) {
		this.reportCheckerLog = reportCheckerLog;
	}
	public String getSupplierEvaluateProject() {
		return supplierEvaluateProject;
	}
	public void setSupplierEvaluateProject(String supplierEvaluateProject) {
		this.supplierEvaluateProject = supplierEvaluateProject;
	}
	public String getSupplierEvaluateStandar() {
		return supplierEvaluateStandar;
	}
	public void setSupplierEvaluateStandar(String supplierEvaluateStandar) {
		this.supplierEvaluateStandar = supplierEvaluateStandar;
	}
	public String getSupplierEvaluateResult() {
		return supplierEvaluateResult;
	}
	public void setSupplierEvaluateResult(String supplierEvaluateResult) {
		this.supplierEvaluateResult = supplierEvaluateResult;
	}
	public String getSampleEvaluateProject() {
		return sampleEvaluateProject;
	}
	public void setSampleEvaluateProject(String sampleEvaluateProject) {
		this.sampleEvaluateProject = sampleEvaluateProject;
	}
	public String getSampleEvaluateStandar() {
		return sampleEvaluateStandar;
	}
	public void setSampleEvaluateStandar(String sampleEvaluateStandar) {
		this.sampleEvaluateStandar = sampleEvaluateStandar;
	}
	public String getSampleEvaluateResult() {
		return sampleEvaluateResult;
	}
	public void setSampleEvaluateResult(String sampleEvaluateResult) {
		this.sampleEvaluateResult = sampleEvaluateResult;
	}
	public String getSampleEvaluateDept() {
		return sampleEvaluateDept;
	}
	public void setSampleEvaluateDept(String sampleEvaluateDept) {
		this.sampleEvaluateDept = sampleEvaluateDept;
	}
	public String getHsfEvaluateProject() {
		return hsfEvaluateProject;
	}
	public void setHsfEvaluateProject(String hsfEvaluateProject) {
		this.hsfEvaluateProject = hsfEvaluateProject;
	}
	public String getHsfEvaluateStandar() {
		return hsfEvaluateStandar;
	}
	public void setHsfEvaluateStandar(String hsfEvaluateStandar) {
		this.hsfEvaluateStandar = hsfEvaluateStandar;
	}
	public String getHsfEvaluateResult() {
		return hsfEvaluateResult;
	}
	public void setHsfEvaluateResult(String hsfEvaluateResult) {
		this.hsfEvaluateResult = hsfEvaluateResult;
	}
	public String getHsfEvaluateDept() {
		return hsfEvaluateDept;
	}
	public void setHsfEvaluateDept(String hsfEvaluateDept) {
		this.hsfEvaluateDept = hsfEvaluateDept;
	}
	public String getMsdsEvaluateProject() {
		return msdsEvaluateProject;
	}
	public void setMsdsEvaluateProject(String msdsEvaluateProject) {
		this.msdsEvaluateProject = msdsEvaluateProject;
	}
	public String getMsdsEvaluateStandar() {
		return msdsEvaluateStandar;
	}
	public void setMsdsEvaluateStandar(String msdsEvaluateStandar) {
		this.msdsEvaluateStandar = msdsEvaluateStandar;
	}
	public String getMsdsEvaluateResult() {
		return msdsEvaluateResult;
	}
	public void setMsdsEvaluateResult(String msdsEvaluateResult) {
		this.msdsEvaluateResult = msdsEvaluateResult;
	}
	public String getMsdsEvaluateDept() {
		return msdsEvaluateDept;
	}
	public void setMsdsEvaluateDept(String msdsEvaluateDept) {
		this.msdsEvaluateDept = msdsEvaluateDept;
	}
	public String getInspectionResult() {
		return inspectionResult;
	}
	public void setInspectionResult(String inspectionResult) {
		this.inspectionResult = inspectionResult;
	}
	public String getInspectionReportFile() {
		return inspectionReportFile;
	}
	public void setInspectionReportFile(String inspectionReportFile) {
		this.inspectionReportFile = inspectionReportFile;
	}
	public String getOutInspectionResult() {
		return outInspectionResult;
	}
	public void setOutInspectionResult(String outInspectionResult) {
		this.outInspectionResult = outInspectionResult;
	}
	public String getInspectionReporter() {
		return inspectionReporter;
	}
	public void setInspectionReporter(String inspectionReporter) {
		this.inspectionReporter = inspectionReporter;
	}
	public String getInspectionChecker() {
		return inspectionChecker;
	}
	public void setInspectionChecker(String inspectionChecker) {
		this.inspectionChecker = inspectionChecker;
	}
	public String getInspectionCheckerLog() {
		return inspectionCheckerLog;
	}
	public void setInspectionCheckerLog(String inspectionCheckerLog) {
		this.inspectionCheckerLog = inspectionCheckerLog;
	}
	public String getRdIfTest() {
		return rdIfTest;
	}
	public void setRdIfTest(String rdIfTest) {
		this.rdIfTest = rdIfTest;
	}
	public String getRdReportFile() {
		return rdReportFile;
	}
	public void setRdReportFile(String rdReportFile) {
		this.rdReportFile = rdReportFile;
	}
	public String getRdOpinion() {
		return rdOpinion;
	}
	public void setRdOpinion(String rdOpinion) {
		this.rdOpinion = rdOpinion;
	}
	public String getRdReporter() {
		return rdReporter;
	}
	public void setRdReporter(String rdReporter) {
		this.rdReporter = rdReporter;
	}
	public String getLabIfTest() {
		return labIfTest;
	}
	public void setLabIfTest(String labIfTest) {
		this.labIfTest = labIfTest;
	}
	public String getLabTestContent() {
		return labTestContent;
	}
	public void setLabTestContent(String labTestContent) {
		this.labTestContent = labTestContent;
	}
	public String getLabTestNum() {
		return labTestNum;
	}
	public void setLabTestNum(String labTestNum) {
		this.labTestNum = labTestNum;
	}
	public String getLabReportFile() {
		return labReportFile;
	}
	public void setLabReportFile(String labReportFile) {
		this.labReportFile = labReportFile;
	}
	public String getLabReporter() {
		return labReporter;
	}
	public void setLabReporter(String labReporter) {
		this.labReporter = labReporter;
	}
	public String getEvaluateResult() {
		return evaluateResult;
	}
	public void setEvaluateResult(String evaluateResult) {
		this.evaluateResult = evaluateResult;
	}
	public String getEvaluateOkInfo() {
		return evaluateOkInfo;
	}
	public void setEvaluateOkInfo(String evaluateOkInfo) {
		this.evaluateOkInfo = evaluateOkInfo;
	}
	public String getEvaluateNgInfo() {
		return evaluateNgInfo;
	}
	public void setEvaluateNgInfo(String evaluateNgInfo) {
		this.evaluateNgInfo = evaluateNgInfo;
	}
	
	public String getEvaluateReporter() {
		return evaluateReporter;
	}
	public void setEvaluateReporter(String evaluateReporter) {
		this.evaluateReporter = evaluateReporter;
	}
	public String getEvaluateChecker() {
		return evaluateChecker;
	}
	public void setEvaluateChecker(String evaluateChecker) {
		this.evaluateChecker = evaluateChecker;
	}
	public String getEvaluateCheckerLog() {
		return evaluateCheckerLog;
	}
	public void setEvaluateCheckerLog(String evaluateCheckerLog) {
		this.evaluateCheckerLog = evaluateCheckerLog;
	}
	public String getIfToManager() {
		return ifToManager;
	}
	public void setIfToManager(String ifToManager) {
		this.ifToManager = ifToManager;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public String getManagerLoginName() {
		return managerLoginName;
	}
	public void setManagerLoginName(String managerLoginName) {
		this.managerLoginName = managerLoginName;
	}
	public String getLabTestResult() {
		return labTestResult;
	}
	public void setLabTestResult(String labTestResult) {
		this.labTestResult = labTestResult;
	}
	public String getInspectionReporterLog() {
		return inspectionReporterLog;
	}
	public void setInspectionReporterLog(String inspectionReporterLog) {
		this.inspectionReporterLog = inspectionReporterLog;
	}
	public String getRdReporterLog() {
		return rdReporterLog;
	}
	public void setRdReporterLog(String rdReporterLog) {
		this.rdReporterLog = rdReporterLog;
	}
	public String getLabReporterLog() {
		return labReporterLog;
	}
	public void setLabReporterLog(String labReporterLog) {
		this.labReporterLog = labReporterLog;
	}
	public String getEvaluateReporterLog() {
		return evaluateReporterLog;
	}
	public void setEvaluateReporterLog(String evaluateReporterLog) {
		this.evaluateReporterLog = evaluateReporterLog;
	}
	public String getRdProcesser() {
		return rdProcesser;
	}
	public void setRdProcesser(String rdProcesser) {
		this.rdProcesser = rdProcesser;
	}
	public String getRdProcesserLog() {
		return rdProcesserLog;
	}
	public void setRdProcesserLog(String rdProcesserLog) {
		this.rdProcesserLog = rdProcesserLog;
	}
	public String getHsfProcesser() {
		return hsfProcesser;
	}
	public void setHsfProcesser(String hsfProcesser) {
		this.hsfProcesser = hsfProcesser;
	}
	
	public String getHsfProcesserLog() {
		return hsfProcesserLog;
	}
	public void setHsfProcesserLog(String hsfProcesserLog) {
		this.hsfProcesserLog = hsfProcesserLog;
	}
	public String getMsdsProcesser() {
		return msdsProcesser;
	}
	public void setMsdsProcesser(String msdsProcesser) {
		this.msdsProcesser = msdsProcesser;
	}
	public String getMsdsProcesserLog() {
		return msdsProcesserLog;
	}
	public void setMsdsProcesserLog(String msdsProcesserLog) {
		this.msdsProcesserLog = msdsProcesserLog;
	}
	public String getQsReportFile() {
		return qsReportFile;
	}
	public void setQsReportFile(String qsReportFile) {
		this.qsReportFile = qsReportFile;
	}
	public String getQsOpinion() {
		return qsOpinion;
	}
	public void setQsOpinion(String qsOpinion) {
		this.qsOpinion = qsOpinion;
	}
	public String getCurrentTransactor() {
		return currentTransactor;
	}
	public void setCurrentTransactor(String currentTransactor) {
		this.currentTransactor = currentTransactor;
	}
	public Date getWorkFlowStageTime() {
		return workFlowStageTime;
	}
	public void setWorkFlowStageTime(Date workFlowStageTime) {
		this.workFlowStageTime = workFlowStageTime;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierEmail() {
		return supplierEmail;
	}
	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	
	
}

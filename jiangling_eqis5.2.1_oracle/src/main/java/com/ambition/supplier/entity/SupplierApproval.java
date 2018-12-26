package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;

/**
 * 
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：材料承认 2.0</p>
 * @author  Janam
 * @version 1.00 2017年9月11日 发布
 */
@Entity
@Table(name="SUPPLIER_SUPPLIER_APPROVAL")
public class SupplierApproval  extends WorkflowIdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "SUPPLIER_SUPPLIER_APPROVAL";//实体编码
	public static final String ENTITY_LIST_NAME = "材料承认管理";//实体名称
	/*材料承认发起 */
	private String formNo;//表单编号
	private String productVersion;//规格型号版本号
	private String productName;//产品名称
	private String materialName;//物料名称
	private String materialCode;//物料代码
	private Date applyDate;//申请日期
	private String supplierName;//供应商名称
	private String supplierCode;//供应商编码
	private String supplierEmail;//供应商邮箱
	private String item;//项目
	private String admitProject;//承认项目
	private String applicat;//申请人
	private String applicatLog;//申请人登录名
	private String consignorDept;//当前部门
	/*
	 * 采购部
	 */
	private String purchaseProcesser;//采购部
	private String purchaseProcessLog;//采购部登录名
	private String supplier;//指定供应商
	private String supplierLoginName;//供应商登录名
	private String opinionPurchase;//意见
	/*
	 * 供应商上传
	 */
	private String vendorNo;//供应商料号
	private String address;//生产地址
	private String specification;//规格书
	private String drawing;//图纸
	private String cpkReport;//CPK报告
	private String faiReport;//FAI报告
	private String raReport;//可靠性报告
	private String qcPlan;//QC工程图
	private String evaluateReport;//性能评估报告
	private String fmea;//FMEA
	private String entityPhoto;//实物照片
	private String productDeclare;//产品成分宣告
	private String surveyFile;//调查文件
	private String bomFile;//BOM文件
	private String codeExplain;//代码说明
	private String packing;//包装方式
	private String checkoutReport;//出货检验报告
	private String others;//其他
	private String materialBook;//材料书
	private String testReport;//测试报告
	private String msds;//MSDS
	/*
	 * 按钮
	 */
	private String vendorNoBt;//供应商料号
	private String addressBt;//生产地址
	private String specificationBt;//规格书
	private String drawingBt;//图纸
	private String cpkReportBt;//CPK报告
	private String faiReportBt;//FAI报告
	private String raReportBt;//可靠性报告
	private String qcPlanBt;//QC工程图
	private String evaluateReportBt;//性能评估报告
	private String fmeaBt;//FMEA
	private String entityPhotoBt;//实物照片
	private String productDeclareBt;//产品成分宣告
	private String surveyFileBt;//调查文件
	private String bomFileBt;//BOM文件
	private String codeExplainBt;//代码说明
	private String packingBt;//包装方式
	private String checkoutReportBt;//出货检验报告
	private String othersBt;//其他
	private String materialBookBt;//材料书
	private String testReportBt;//测试报告
	private String msdsBt;//MSDS
	/*
	 * 时间
	 */
	private Date specificationTime;//规格书
	private Date drawingTime;//图纸
	private Date cpkReportTime;//CPK报告
	private Date evaluateReportTime;//性能评估报告
	/*
	 * 会签
	 */
	private String checkDeptMans;//会签部门人员
	private String checkDeptMansLog;//会签人登陆名
	private String checkDeptMansLog2;//会签人登陆名
	private String rdChecker;//研发核准
	private String rdCheckerLog;//研发核准人登录名
	private String pmChecker;//pm核准
	private String pmCheckerLog;//pm核准登录名
	private String sqeChecker;//SQE核准
	private String sqeCheckerLog;//SQE核准登录名
	private String qsChecker;//QS核准
	private String qsCheckerLog;//QS核准登录名
	private String npiChecker;//NPI核准
	private String npiCheckerLog;//NPI核准登录名
	private String dqeChecker;//DQE核准
	private String dqeCheckerLog;//DQE核准登录名
	private String projectChecker;//工程核准
	private String projectCheckerLog;//工程核准登录名
	/*
	 * 承认状态
	 */
	private String rdStatus;//承认状态研发
	private String pmStatus;//承认状态PM
	private String sqeStatus;//承认状态SQE
	private String qsStatus;//承认状态QS
	private String npiStatus;//承认状态NPI
	private String dqeStatus;//承认状态DQE
	private String projectStatus;//承认状态工程
	private String finalStatus;//最终状态
	/*
	 * 意见
	 */
	private String countersignRD;//会签意见研发
	private String countersignPM;//会签意见PM
	private String countersignSQE;//会签意见SQE
	private String countersignQS;//会签意见QS
	private String countersignNpi;//会签意见NPI
	private String countersignDqe;//会签意见DEQ
	private String countersignProject;//会签意见工程
	private String docControl;//文控
	private String docControlLoging;//文控登录名
	private String opiniondoc;//文控意见
	private String copyMan;//抄送人
	private String copyManLogin;//抄送人登录名
	private String fileRD;//研发附件
	private String filePM;//PM附件
	private String fileSQE;//SQE附件
	private String fileQS;//QS附件
	private String fileNpi;//Npi附件
	private String fileDqe;//Dqe附件
	/*
	 * 文件名称
	 */
	private String productFileName;
	private String surveyFileNmae;
	private Date docControlDate;//文控提交时间
	private String endType;//结束状态
	private Long inspectionId;
	private String textResult;//试验结果（返回iqc）
	private String inspectionNo;//进货表单编号
	private String gpMaterialNo;//产品宣告编号
	private String gpMaterialId;//产品宣告Id
	private String gpMaterialState;//产品宣告状态
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
	public String getSupplierEmail() {
		return supplierEmail;
	}
	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getAdmitProject() {
		return admitProject;
	}
	public void setAdmitProject(String admitProject) {
		this.admitProject = admitProject;
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
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getSupplierLoginName() {
		return supplierLoginName;
	}
	public void setSupplierLoginName(String supplierLoginName) {
		this.supplierLoginName = supplierLoginName;
	}
	
	public String getApplicat() {
		return applicat;
	}
	public void setApplicat(String applicat) {
		this.applicat = applicat;
	}
	public String getApplicatLog() {
		return applicatLog;
	}
	public void setApplicatLog(String applicatLog) {
		this.applicatLog = applicatLog;
	}
	public String getOpinionPurchase() {
		return opinionPurchase;
	}
	public void setOpinionPurchase(String opinionPurchase) {
		this.opinionPurchase = opinionPurchase;
	}
	public String getVendorNo() {
		return vendorNo;
	}
	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getDrawing() {
		return drawing;
	}
	public void setDrawing(String drawing) {
		this.drawing = drawing;
	}
	public String getCpkReport() {
		return cpkReport;
	}
	public void setCpkReport(String cpkReport) {
		this.cpkReport = cpkReport;
	}
	public String getFaiReport() {
		return faiReport;
	}
	public void setFaiReport(String faiReport) {
		this.faiReport = faiReport;
	}
	public String getRaReport() {
		return raReport;
	}
	public void setRaReport(String raReport) {
		this.raReport = raReport;
	}
	public String getQcPlan() {
		return qcPlan;
	}
	public void setQcPlan(String qcPlan) {
		this.qcPlan = qcPlan;
	}
	public String getEvaluateReport() {
		return evaluateReport;
	}
	public void setEvaluateReport(String evaluateReport) {
		this.evaluateReport = evaluateReport;
	}
	public String getFmea() {
		return fmea;
	}
	public void setFmea(String fmea) {
		this.fmea = fmea;
	}
	public String getEntityPhoto() {
		return entityPhoto;
	}
	public void setEntityPhoto(String entityPhoto) {
		this.entityPhoto = entityPhoto;
	}
	public String getProductDeclare() {
		return productDeclare;
	}
	public void setProductDeclare(String productDeclare) {
		this.productDeclare = productDeclare;
	}
	public String getSurveyFile() {
		return surveyFile;
	}
	public void setSurveyFile(String surveyFile) {
		this.surveyFile = surveyFile;
	}
	public String getBomFile() {
		return bomFile;
	}
	public void setBomFile(String bomFile) {
		this.bomFile = bomFile;
	}
	public String getCodeExplain() {
		return codeExplain;
	}
	public void setCodeExplain(String codeExplain) {
		this.codeExplain = codeExplain;
	}
	public String getPacking() {
		return packing;
	}
	public void setPacking(String packing) {
		this.packing = packing;
	}
	public String getCheckoutReport() {
		return checkoutReport;
	}
	public void setCheckoutReport(String checkoutReport) {
		this.checkoutReport = checkoutReport;
	}
	public String getOthers() {
		return others;
	}
	public void setOthers(String others) {
		this.others = others;
	}
	public String getMaterialBook() {
		return materialBook;
	}
	public void setMaterialBook(String materialBook) {
		this.materialBook = materialBook;
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
	public String getRdChecker() {
		return rdChecker;
	}
	public void setRdChecker(String rdChecker) {
		this.rdChecker = rdChecker;
	}
	public String getRdCheckerLog() {
		return rdCheckerLog;
	}
	public void setRdCheckerLog(String rdCheckerLog) {
		this.rdCheckerLog = rdCheckerLog;
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
	public String getNpiChecker() {
		return npiChecker;
	}
	public void setNpiChecker(String npiChecker) {
		this.npiChecker = npiChecker;
	}
	public String getNpiCheckerLog() {
		return npiCheckerLog;
	}
	public void setNpiCheckerLog(String npiCheckerLog) {
		this.npiCheckerLog = npiCheckerLog;
	}
	public String getDqeChecker() {
		return dqeChecker;
	}
	public void setDqeChecker(String dqeChecker) {
		this.dqeChecker = dqeChecker;
	}
	public String getDqeCheckerLog() {
		return dqeCheckerLog;
	}
	public void setDqeCheckerLog(String dqeCheckerLog) {
		this.dqeCheckerLog = dqeCheckerLog;
	}
	public String getProjectChecker() {
		return projectChecker;
	}
	public void setProjectChecker(String projectChecker) {
		this.projectChecker = projectChecker;
	}
	public String getProjectCheckerLog() {
		return projectCheckerLog;
	}
	public void setProjectCheckerLog(String projectCheckerLog) {
		this.projectCheckerLog = projectCheckerLog;
	}
	public String getRdStatus() {
		return rdStatus;
	}
	public void setRdStatus(String rdStatus) {
		this.rdStatus = rdStatus;
	}
	public String getPmStatus() {
		return pmStatus;
	}
	public void setPmStatus(String pmStatus) {
		this.pmStatus = pmStatus;
	}
	public String getSqeStatus() {
		return sqeStatus;
	}
	public void setSqeStatus(String sqeStatus) {
		this.sqeStatus = sqeStatus;
	}
	public String getQsStatus() {
		return qsStatus;
	}
	public void setQsStatus(String qsStatus) {
		this.qsStatus = qsStatus;
	}
	public String getNpiStatus() {
		return npiStatus;
	}
	public void setNpiStatus(String npiStatus) {
		this.npiStatus = npiStatus;
	}
	public String getDqeStatus() {
		return dqeStatus;
	}
	public void setDqeStatus(String dqeStatus) {
		this.dqeStatus = dqeStatus;
	}
	public String getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	public String getCountersignRD() {
		return countersignRD;
	}
	public void setCountersignRD(String countersignRD) {
		this.countersignRD = countersignRD;
	}
	public String getCountersignPM() {
		return countersignPM;
	}
	public void setCountersignPM(String countersignPM) {
		this.countersignPM = countersignPM;
	}
	public String getCountersignSQE() {
		return countersignSQE;
	}
	public void setCountersignSQE(String countersignSQE) {
		this.countersignSQE = countersignSQE;
	}
	public String getCountersignQS() {
		return countersignQS;
	}
	public void setCountersignQS(String countersignQS) {
		this.countersignQS = countersignQS;
	}
	public String getCountersignNpi() {
		return countersignNpi;
	}
	public void setCountersignNpi(String countersignNpi) {
		this.countersignNpi = countersignNpi;
	}
	public String getCountersignDqe() {
		return countersignDqe;
	}
	public void setCountersignDqe(String countersignDqe) {
		this.countersignDqe = countersignDqe;
	}
	public String getCountersignProject() {
		return countersignProject;
	}
	public void setCountersignProject(String countersignProject) {
		this.countersignProject = countersignProject;
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
	public Date getSpecificationTime() {
		return specificationTime;
	}
	public void setSpecificationTime(Date specificationTime) {
		this.specificationTime = specificationTime;
	}
	public Date getDrawingTime() {
		return drawingTime;
	}
	public void setDrawingTime(Date drawingTime) {
		this.drawingTime = drawingTime;
	}
	public Date getCpkReportTime() {
		return cpkReportTime;
	}
	public void setCpkReportTime(Date cpkReportTime) {
		this.cpkReportTime = cpkReportTime;
	}
	public Date getEvaluateReportTime() {
		return evaluateReportTime;
	}
	public void setEvaluateReportTime(Date evaluateReportTime) {
		this.evaluateReportTime = evaluateReportTime;
	}
	public String getConsignorDept() {
		return consignorDept;
	}
	public void setConsignorDept(String consignorDept) {
		this.consignorDept = consignorDept;
	}
	public static String getEntityListCode() {
		return ENTITY_LIST_CODE;
	}
	public static String getEntityListName() {
		return ENTITY_LIST_NAME;
	}
	public String getDocControlLoging() {
		return docControlLoging;
	}
	public void setDocControlLoging(String docControlLoging) {
		this.docControlLoging = docControlLoging;
	}
	public String getOpiniondoc() {
		return opiniondoc;
	}
	public void setOpiniondoc(String opiniondoc) {
		this.opiniondoc = opiniondoc;
	}
	public String getFileRD() {
		return fileRD;
	}
	public void setFileRD(String fileRD) {
		this.fileRD = fileRD;
	}
	public String getTestReport() {
		return testReport;
	}
	public void setTestReport(String testReport) {
		this.testReport = testReport;
	}
	public String getMsds() {
		return msds;
	}
	public void setMsds(String msds) {
		this.msds = msds;
	}
	public String getProductFileName() {
		return productFileName;
	}
	public void setProductFileName(String productFileName) {
		this.productFileName = productFileName;
	}
	public String getSurveyFileNmae() {
		return surveyFileNmae;
	}
	public void setSurveyFileNmae(String surveyFileNmae) {
		this.surveyFileNmae = surveyFileNmae;
	}
	public String getCheckDeptMansLog2() {
		return checkDeptMansLog2;
	}
	public void setCheckDeptMansLog2(String checkDeptMansLog2) {
		this.checkDeptMansLog2 = checkDeptMansLog2;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getFinalStatus() {
		return finalStatus;
	}
	public void setFinalStatus(String finalStatus) {
		this.finalStatus = finalStatus;
	}
	public Date getDocControlDate() {
		return docControlDate;
	}
	public void setDocControlDate(Date docControlDate) {
		this.docControlDate = docControlDate;
	}
	public String getFilePM() {
		return filePM;
	}
	public void setFilePM(String filePM) {
		this.filePM = filePM;
	}
	public String getFileSQE() {
		return fileSQE;
	}
	public void setFileSQE(String fileSQE) {
		this.fileSQE = fileSQE;
	}
	public String getFileQS() {
		return fileQS;
	}
	public void setFileQS(String fileQS) {
		this.fileQS = fileQS;
	}
	public String getFileNpi() {
		return fileNpi;
	}
	public void setFileNpi(String fileNpi) {
		this.fileNpi = fileNpi;
	}
	public String getFileDqe() {
		return fileDqe;
	}
	public void setFileDqe(String fileDqe) {
		this.fileDqe = fileDqe;
	}
	public String getEndType() {
		return endType;
	}
	public void setEndType(String endType) {
		this.endType = endType;
	}
	public Long getInspectionId() {
		return inspectionId;
	}
	public void setInspectionId(Long inspectionId) {
		this.inspectionId = inspectionId;
	}
	public String getTextResult() {
		return textResult;
	}
	public void setTextResult(String textResult) {
		this.textResult = textResult;
	}
	public String getInspectionNo() {
		return inspectionNo;
	}
	public void setInspectionNo(String inspectionNo) {
		this.inspectionNo = inspectionNo;
	}
	public String getVendorNoBt() {
		return vendorNoBt;
	}
	public void setVendorNoBt(String vendorNoBt) {
		this.vendorNoBt = vendorNoBt;
	}
	public String getAddressBt() {
		return addressBt;
	}
	public void setAddressBt(String addressBt) {
		this.addressBt = addressBt;
	}
	public String getSpecificationBt() {
		return specificationBt;
	}
	public void setSpecificationBt(String specificationBt) {
		this.specificationBt = specificationBt;
	}
	public String getDrawingBt() {
		return drawingBt;
	}
	public void setDrawingBt(String drawingBt) {
		this.drawingBt = drawingBt;
	}
	public String getCpkReportBt() {
		return cpkReportBt;
	}
	public void setCpkReportBt(String cpkReportBt) {
		this.cpkReportBt = cpkReportBt;
	}
	public String getFaiReportBt() {
		return faiReportBt;
	}
	public void setFaiReportBt(String faiReportBt) {
		this.faiReportBt = faiReportBt;
	}
	public String getRaReportBt() {
		return raReportBt;
	}
	public void setRaReportBt(String raReportBt) {
		this.raReportBt = raReportBt;
	}
	public String getQcPlanBt() {
		return qcPlanBt;
	}
	public void setQcPlanBt(String qcPlanBt) {
		this.qcPlanBt = qcPlanBt;
	}
	public String getEvaluateReportBt() {
		return evaluateReportBt;
	}
	public void setEvaluateReportBt(String evaluateReportBt) {
		this.evaluateReportBt = evaluateReportBt;
	}
	public String getFmeaBt() {
		return fmeaBt;
	}
	public void setFmeaBt(String fmeaBt) {
		this.fmeaBt = fmeaBt;
	}
	public String getEntityPhotoBt() {
		return entityPhotoBt;
	}
	public void setEntityPhotoBt(String entityPhotoBt) {
		this.entityPhotoBt = entityPhotoBt;
	}
	public String getProductDeclareBt() {
		return productDeclareBt;
	}
	public void setProductDeclareBt(String productDeclareBt) {
		this.productDeclareBt = productDeclareBt;
	}
	public String getSurveyFileBt() {
		return surveyFileBt;
	}
	public void setSurveyFileBt(String surveyFileBt) {
		this.surveyFileBt = surveyFileBt;
	}
	public String getBomFileBt() {
		return bomFileBt;
	}
	public void setBomFileBt(String bomFileBt) {
		this.bomFileBt = bomFileBt;
	}
	public String getCodeExplainBt() {
		return codeExplainBt;
	}
	public void setCodeExplainBt(String codeExplainBt) {
		this.codeExplainBt = codeExplainBt;
	}
	public String getPackingBt() {
		return packingBt;
	}
	public void setPackingBt(String packingBt) {
		this.packingBt = packingBt;
	}
	public String getCheckoutReportBt() {
		return checkoutReportBt;
	}
	public void setCheckoutReportBt(String checkoutReportBt) {
		this.checkoutReportBt = checkoutReportBt;
	}
	public String getOthersBt() {
		return othersBt;
	}
	public void setOthersBt(String othersBt) {
		this.othersBt = othersBt;
	}
	public String getMaterialBookBt() {
		return materialBookBt;
	}
	public void setMaterialBookBt(String materialBookBt) {
		this.materialBookBt = materialBookBt;
	}
	public String getTestReportBt() {
		return testReportBt;
	}
	public void setTestReportBt(String testReportBt) {
		this.testReportBt = testReportBt;
	}
	public String getMsdsBt() {
		return msdsBt;
	}
	public void setMsdsBt(String msdsBt) {
		this.msdsBt = msdsBt;
	}
	public String getGpMaterialNo() {
		return gpMaterialNo;
	}
	public void setGpMaterialNo(String gpMaterialNo) {
		this.gpMaterialNo = gpMaterialNo;
	}
	public String getGpMaterialId() {
		return gpMaterialId;
	}
	public void setGpMaterialId(String gpMaterialId) {
		this.gpMaterialId = gpMaterialId;
	}
	public String getGpMaterialState() {
		return gpMaterialState;
	}
	public void setGpMaterialState(String gpMaterialState) {
		this.gpMaterialState = gpMaterialState;
	}
	
}

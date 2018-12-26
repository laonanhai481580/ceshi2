package com.ambition.epm.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name="EPM_EXCEPTION_SINGLE")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class ExceptionSingle extends WorkflowIdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "EPM_EXCEPTION_SINGLE";//实体编码
	public static final String ENTITY_LIST_NAME = "异常处理单";//实体名称
	private String formNo;//表单编号
	private String reportNo;//报告编号
	private String itemNo;//项目编号
	private String customerNo;//客户
	private String productNo;//机种
	private String sampleType;//样品类别
	private String supplier;//供应商
	private String testSend;//送测人
	private String testSendLogin;//送测人登录名
	private String testDepartment;//送测部门
	private String productStage;//产品阶段
	private String model;//规格型号
	private String lotNo;//批号
	private Integer quantity;//样品数量
	private Integer defectNumber;//不良数量
	private Integer invalidNumber;//无效数量
	private String defectRate;//不良率
	private String severity;//严重度
	private String defection;//不良率
	private String source;//不良来源厂区
	private String testEngineer;//测试员
	private String testEngineerLogin;//测试员登录名
	private Date submitDate;//提交日期
	private String exceptionDescr;//异常描述
	private String exceptionFile;//异常附件
	private String temporaryDisposal;//临时对策
	private String temporaryFile;//临时附件
	private String reasonAnalysis;//原因分析
	private String rootCausesFile;//原因附件
	private Date completionTime;//要求完成时间
	private Date finishTime;//实际完成时间
	private String improvement;//改善对策
	private String improveFile;//改善对策附件
	private Date completionDate;//改善开始时间
	private Date finishTimeDate;//改善完成时间
	private String resultsConfirm;//效果确认
	private String resultsFile;//确认附件
	private String resultsText;//确认意见
	private String lead;//领导
	private String leadLogin;//领导登录名
	private String hsfNo;//HSF单号
	private String ortNo;//ORT单号
	private Long entrustId;
	private String managerAssets;//管理编号
	private String exceptionState = "N";//隐藏状态
	
	private String laboratory;//实验室人员(问题改善人)
	private String laboratoryLogin;//实验室人员登录名
	private String temporary;//临时办理人
	private String temporaryLogin;//临时办理人登录名
	private String rootCauses;//原因分析人(原因分析人)
	private String rootCausesLogin;//原因分析人登录名
	private String improvePerson;//改善人(改善对策人)
	private String improvePersonLogin;//改善人登录名
	private String resultsPeople;//确认人(确认人)
	private String resultsPeopleLogin;//确认人登录名
	private String labPeople;//实验确认人(结案人)
	private String labPeopleLogin;//实验确认人登录名
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getTestDepartment() {
		return testDepartment;
	}
	public void setTestDepartment(String testDepartment) {
		this.testDepartment = testDepartment;
	}
	public String getExceptionDescr() {
		return exceptionDescr;
	}
	public void setExceptionDescr(String exceptionDescr) {
		this.exceptionDescr = exceptionDescr;
	}
	public String getExceptionFile() {
		return exceptionFile;
	}
	public void setExceptionFile(String exceptionFile) {
		this.exceptionFile = exceptionFile;
	}

	public String getTemporaryDisposal() {
		return temporaryDisposal;
	}
	public void setTemporaryDisposal(String temporaryDisposal) {
		this.temporaryDisposal = temporaryDisposal;
	}
	
	public String getSampleType() {
		return sampleType;
	}
	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getTestSend() {
		return testSend;
	}
	public void setTestSend(String testSend) {
		this.testSend = testSend;
	}
	public String getTestSendLogin() {
		return testSendLogin;
	}
	public void setTestSendLogin(String testSendLogin) {
		this.testSendLogin = testSendLogin;
	}
	public String getProductStage() {
		return productStage;
	}
	public void setProductStage(String productStage) {
		this.productStage = productStage;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getDefection() {
		return defection;
	}
	public void setDefection(String defection) {
		this.defection = defection;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTestEngineer() {
		return testEngineer;
	}
	public void setTestEngineer(String testEngineer) {
		this.testEngineer = testEngineer;
	}
	public String getTestEngineerLogin() {
		return testEngineerLogin;
	}
	public void setTestEngineerLogin(String testEngineerLogin) {
		this.testEngineerLogin = testEngineerLogin;
	}
	public Date getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}
	public String getLaboratory() {
		return laboratory;
	}
	public void setLaboratory(String laboratory) {
		this.laboratory = laboratory;
	}
	public String getLaboratoryLogin() {
		return laboratoryLogin;
	}
	public void setLaboratoryLogin(String laboratoryLogin) {
		this.laboratoryLogin = laboratoryLogin;
	}
	public String getTemporaryFile() {
		return temporaryFile;
	}
	public void setTemporaryFile(String temporaryFile) {
		this.temporaryFile = temporaryFile;
	}
	public String getTemporary() {
		return temporary;
	}
	public void setTemporary(String temporary) {
		this.temporary = temporary;
	}
	public String getTemporaryLogin() {
		return temporaryLogin;
	}
	public void setTemporaryLogin(String temporaryLogin) {
		this.temporaryLogin = temporaryLogin;
	}
	public String getReasonAnalysis() {
		return reasonAnalysis;
	}
	public void setReasonAnalysis(String reasonAnalysis) {
		this.reasonAnalysis = reasonAnalysis;
	}
	public String getRootCausesFile() {
		return rootCausesFile;
	}
	public void setRootCausesFile(String rootCausesFile) {
		this.rootCausesFile = rootCausesFile;
	}
	public String getRootCauses() {
		return rootCauses;
	}
	public void setRootCauses(String rootCauses) {
		this.rootCauses = rootCauses;
	}
	public String getRootCausesLogin() {
		return rootCausesLogin;
	}
	public void setRootCausesLogin(String rootCausesLogin) {
		this.rootCausesLogin = rootCausesLogin;
	}
	public Date getCompletionTime() {
		return completionTime;
	}
	public void setCompletionTime(Date completionTime) {
		this.completionTime = completionTime;
	}
	public Date getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	public String getResultsFile() {
		return resultsFile;
	}
	public void setResultsFile(String resultsFile) {
		this.resultsFile = resultsFile;
	}
	public String getResultsPeople() {
		return resultsPeople;
	}
	public void setResultsPeople(String resultsPeople) {
		this.resultsPeople = resultsPeople;
	}
	public String getResultsPeopleLogin() {
		return resultsPeopleLogin;
	}
	public void setResultsPeopleLogin(String resultsPeopleLogin) {
		this.resultsPeopleLogin = resultsPeopleLogin;
	}
	public String getLead() {
		return lead;
	}
	public void setLead(String lead) {
		this.lead = lead;
	}
	public String getLeadLogin() {
		return leadLogin;
	}
	public void setLeadLogin(String leadLogin) {
		this.leadLogin = leadLogin;
	}
	public String getResultsConfirm() {
		return resultsConfirm;
	}
	public String getImprovement() {
		return improvement;
	}
	public void setImprovement(String improvement) {
		this.improvement = improvement;
	}
	public String getImproveFile() {
		return improveFile;
	}
	public void setImproveFile(String improveFile) {
		this.improveFile = improveFile;
	}
	public String getImprovePerson() {
		return improvePerson;
	}
	public void setImprovePerson(String improvePerson) {
		this.improvePerson = improvePerson;
	}
	public String getImprovePersonLogin() {
		return improvePersonLogin;
	}
	public void setImprovePersonLogin(String improvePersonLogin) {
		this.improvePersonLogin = improvePersonLogin;
	}
	public Date getCompletionDate() {
		return completionDate;
	}
	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}
	public Date getFinishTimeDate() {
		return finishTimeDate;
	}
	public void setFinishTimeDate(Date finishTimeDate) {
		this.finishTimeDate = finishTimeDate;
	}
	public void setResultsConfirm(String resultsConfirm) {
		this.resultsConfirm = resultsConfirm;
	}
	public static String getEntityListCode() {
		return ENTITY_LIST_CODE;
	}
	public static String getEntityListName() {
		return ENTITY_LIST_NAME;
	}
	public String getHsfNo() {
		return hsfNo;
	}
	public void setHsfNo(String hsfNo) {
		this.hsfNo = hsfNo;
	}
	public String getOrtNo() {
		return ortNo;
	}
	public void setOrtNo(String ortNo) {
		this.ortNo = ortNo;
	}
	public String getLabPeople() {
		return labPeople;
	}
	public void setLabPeople(String labPeople) {
		this.labPeople = labPeople;
	}
	public String getLabPeopleLogin() {
		return labPeopleLogin;
	}
	public void setLabPeopleLogin(String labPeopleLogin) {
		this.labPeopleLogin = labPeopleLogin;
	}
	public Long getEntrustId() {
		return entrustId;
	}
	public void setEntrustId(Long entrustId) {
		this.entrustId = entrustId;
	}
	public String getManagerAssets() {
		return managerAssets;
	}
	public void setManagerAssets(String managerAssets) {
		this.managerAssets = managerAssets;
	}
	public String getExceptionState() {
		return exceptionState;
	}
	public void setExceptionState(String exceptionState) {
		this.exceptionState = exceptionState;
	}
	public String getDefectRate() {
		return defectRate;
	}
	public void setDefectRate(String defectRate) {
		this.defectRate = defectRate;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getDefectNumber() {
		return defectNumber;
	}
	public void setDefectNumber(Integer defectNumber) {
		this.defectNumber = defectNumber;
	}
	public Integer getInvalidNumber() {
		return invalidNumber;
	}
	public void setInvalidNumber(Integer invalidNumber) {
		this.invalidNumber = invalidNumber;
	}
	public String getResultsText() {
		return resultsText;
	}
	public void setResultsText(String resultsText) {
		this.resultsText = resultsText;
	}
	
}

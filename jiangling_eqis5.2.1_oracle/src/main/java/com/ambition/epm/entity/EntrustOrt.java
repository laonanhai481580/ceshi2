package com.ambition.epm.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="EPM_ENTRUST_ORT")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class EntrustOrt extends WorkflowIdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "EPM_ENTRUST_ORT";//实体编码
	public static final String ENTITY_LIST_NAME = "ORT试验委托";//实体名称
	
	private String formNo;//表单编号
	private String consignor;//申请人
	private String consignorLogin;//申请人登录名
	private String consignorDept;//申请部门
	private Date   consignableDate;//申请日期
	private Date   deadline;//希望完成日期
	private String items;//项目
	private String category;//产品阶段
	private String categoryText;//产品阶段
	private String categoryRemark;//产品其他
	private String productionLine;//生产线别
	private String productionPeriod;//生产时段
	private String customer;//客户
	private String customerNo;//客户编号
	private String modelName;//机种名称（客户）新加  
	private String productNo;//机种（欧菲）
	private String sampleType;//样品类别
	private String lotNo;//批号
	private Integer quantity;//样品数量
	private String purpose;//目的
	private String sampleDiscription;//样品描述
	private String aimFile;//目的附件
	private String describeFile;//样品描述附件
	private String confirmDept;//确认部门
	private String confirmDeptLoing;//确认部门登录名
	private String schedule;//实验室排程
	private String scheduleLoing;//实验室排程登录名
	private String tester;//实验室测试员
	private String testerLoing;//实验室测试员登录名
	private String reportUpda;//上传报告人
	private String reportUpdaLoing;//上传报告登录名
	private String reportAudit;//报告审核
	private String reportAuditLoing;//报告审核登录名
	private String testReport;//测试报告
	private String sampleHandling;//样品处理
	private String epmState;//隐藏状态
	private String contact;//联系方式
	private String managerAssets;//管理编号
	private String endType;//结束状态
	private Long   inspectionId;
	private String specical;//特殊测试月份
	private String textResult;//试验结果（返回iqc）
	private String inspectionNo;//进货表单编号
	private String abnormalOdd;//异常申请单号
	private String factory;//厂区
	private String requisitionNo;//领料单号
	private String rejectedMaterialNo;//退料单号
	private String turnoverNo;//周转单号
	private String degree;//严重程度
	private String customerNeed;//客户需要
	@OneToMany(mappedBy = "entrustOrt",cascade={CascadeType.ALL})
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	private List<EntrustOrtSublist> entrustOrtSublists;
	
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getConsignor() {
		return consignor;
	}
	public void setConsignor(String consignor) {
		this.consignor = consignor;
	}
	public String getConsignorDept() {
		return consignorDept;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public void setConsignorDept(String consignorDept) {
		this.consignorDept = consignorDept;
	}
	public Date getConsignableDate() {
		return consignableDate;
	}
	public void setConsignableDate(Date consignableDate) {
		this.consignableDate = consignableDate;
	}
	public Date getDeadline() {
		return deadline;
	}
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	
	public String getConsignorLogin() {
		return consignorLogin;
	}
	public void setConsignorLogin(String consignorLogin) {
		this.consignorLogin = consignorLogin;
	}
	public String getSampleType() {
		return sampleType;
	}
	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getSampleDiscription() {
		return sampleDiscription;
	}
	public void setSampleDiscription(String sampleDiscription) {
		this.sampleDiscription = sampleDiscription;
	}
	public String getAimFile() {
		return aimFile;
	}
	public void setAimFile(String aimFile) {
		this.aimFile = aimFile;
	}
	public String getDescribeFile() {
		return describeFile;
	}
	public void setDescribeFile(String describeFile) {
		this.describeFile = describeFile;
	}
	public List<EntrustOrtSublist> getEntrustOrtSublists() {
		return entrustOrtSublists;
	}
	public void setEntrustOrtSublists(List<EntrustOrtSublist> entrustOrtSublists) {
		this.entrustOrtSublists = entrustOrtSublists;
	}
	public static String getEntityListCode() {
		return ENTITY_LIST_CODE;
	}
	public static String getEntityListName() {
		return ENTITY_LIST_NAME;
	}
	public String getConfirmDept() {
		return confirmDept;
	}
	public void setConfirmDept(String confirmDept) {
		this.confirmDept = confirmDept;
	}
	public String getConfirmDeptLoing() {
		return confirmDeptLoing;
	}
	public void setConfirmDeptLoing(String confirmDeptLoing) {
		this.confirmDeptLoing = confirmDeptLoing;
	}
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	public String getScheduleLoing() {
		return scheduleLoing;
	}
	public void setScheduleLoing(String scheduleLoing) {
		this.scheduleLoing = scheduleLoing;
	}
	public String getTester() {
		return tester;
	}
	public void setTester(String tester) {
		this.tester = tester;
	}
	public String getTesterLoing() {
		return testerLoing;
	}
	public void setTesterLoing(String testerLoing) {
		this.testerLoing = testerLoing;
	}
	public String getReportAudit() {
		return reportAudit;
	}
	public void setReportAudit(String reportAudit) {
		this.reportAudit = reportAudit;
	}
	public String getReportAuditLoing() {
		return reportAuditLoing;
	}
	public void setReportAuditLoing(String reportAuditLoing) {
		this.reportAuditLoing = reportAuditLoing;
	}
	public String getTestReport() {
		return testReport;
	}
	public void setTestReport(String testReport) {
		this.testReport = testReport;
	}
	public String getSampleHandling() {
		return sampleHandling;
	}
	public void setSampleHandling(String sampleHandling) {
		this.sampleHandling = sampleHandling;
	}
	public String getEpmState() {
		return epmState;
	}
	public void setEpmState(String epmState) {
		this.epmState = epmState;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getManagerAssets() {
		return managerAssets;
	}
	public void setManagerAssets(String managerAssets) {
		this.managerAssets = managerAssets;
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
	public String getAbnormalOdd() {
		return abnormalOdd;
	}
	public void setAbnormalOdd(String abnormalOdd) {
		this.abnormalOdd = abnormalOdd;
	}
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getProductionLine() {
		return productionLine;
	}
	public void setProductionLine(String productionLine) {
		this.productionLine = productionLine;
	}
	public String getProductionPeriod() {
		return productionPeriod;
	}
	public void setProductionPeriod(String productionPeriod) {
		this.productionPeriod = productionPeriod;
	}
	public String getRequisitionNo() {
		return requisitionNo;
	}
	public void setRequisitionNo(String requisitionNo) {
		this.requisitionNo = requisitionNo;
	}
	public String getRejectedMaterialNo() {
		return rejectedMaterialNo;
	}
	public void setRejectedMaterialNo(String rejectedMaterialNo) {
		this.rejectedMaterialNo = rejectedMaterialNo;
	}
	public String getTurnoverNo() {
		return turnoverNo;
	}
	public void setTurnoverNo(String turnoverNo) {
		this.turnoverNo = turnoverNo;
	}
	public String getReportUpda() {
		return reportUpda;
	}
	public void setReportUpda(String reportUpda) {
		this.reportUpda = reportUpda;
	}
	public String getReportUpdaLoing() {
		return reportUpdaLoing;
	}
	public void setReportUpdaLoing(String reportUpdaLoing) {
		this.reportUpdaLoing = reportUpdaLoing;
	}
	public String getCategoryText() {
		return categoryText;
	}
	public void setCategoryText(String categoryText) {
		this.categoryText = categoryText;
	}
	public String getCategoryRemark() {
		return categoryRemark;
	}
	public void setCategoryRemark(String categoryRemark) {
		this.categoryRemark = categoryRemark;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getSpecical() {
		return specical;
	}
	public void setSpecical(String specical) {
		this.specical = specical;
	}
	public String getCustomerNeed() {
		return customerNeed;
	}
	public void setCustomerNeed(String customerNeed) {
		this.customerNeed = customerNeed;
	}
	
}

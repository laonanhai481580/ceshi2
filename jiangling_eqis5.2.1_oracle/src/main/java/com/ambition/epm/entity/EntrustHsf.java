package com.ambition.epm.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name="EPM_ENTRUST_HSF")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class EntrustHsf extends WorkflowIdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "EPM_ENTRUST_HSF";//实体编码
	public static final String ENTITY_LIST_NAME = "HSF实验委托";//实体名称
	
	private String formNo;//表单编号
	private String consignor;//申请人
	private String consignorLogin;//申请人登录名
	private String consignorDept;//申请部门
	private Date consignableDate;//申请日期
	private String category;//产品阶段
	private String categoryText;//产品阶段
	private String categoryRemark;//产品其他
	private String quantity;//样品数量
	private String purpose;//实验目的
	private String aimFile;//附件
	private String confirmDept;//确认部门
	private String confirmDeptLoing;//确认部门登录名
	private String schedule;//实验室排程
	private String scheduleLoing;//实验室排程登录名
	private String tester;//实验室测试员
	private String testerLoing;//实验室测试员登录名
	private String reportAudit;//报告审核
	private String reportUpda;//上传报告人
	private String reportUpdaLoing;//上传报告登录名
	private String reportAuditLoing;//报告审核登录名
	private String sampleHandling;//样品处理
	private String epmState;//隐藏状态
	private String contact;//联系方式
	private String managerAssets;//管理编号
	private String endType;//结束状态
	private Long inspectionId;
	private String textResult;//试验结果（返回iqc）
	private String inspectionNo;//进货表单编号
	private String abnormalOdd;//异常申请单号
	private String requisitionNo;//领料单号
	private String rejectedMaterialNo;//退料单号
	private String turnoverNo;//周转单号
	private String supplierName;//供应商
	private String supplierCode;//供应商
	private String supplierNameIn;//供应商
	private String supplierCodeIn;//供应商
	private String supplierNameOut;//供应商
	private String supplierCodeOut;//供应商
	private String clientName;//客户
	private String customerNeed;//客户需要
	private String sampleType;//样品类别
	private String degree;//严重程度
	
	@OneToMany(mappedBy = "entrustHsf",cascade={javax.persistence.CascadeType.ALL})
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	private List<EntrustHsfSublist> entrustHsfSublists;
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
	public String getConsignorLogin() {
		return consignorLogin;
	}
	public void setConsignorLogin(String consignorLogin) {
		this.consignorLogin = consignorLogin;
	}
	public String getConsignorDept() {
		return consignorDept;
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
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public List<EntrustHsfSublist> getEntrustHsfSublists() {
		return entrustHsfSublists;
	}
	public void setEntrustHsfSublists(List<EntrustHsfSublist> entrustHsfSublists) {
		this.entrustHsfSublists = entrustHsfSublists;
	}
	public String getAimFile() {
		return aimFile;
	}
	public void setAimFile(String aimFile) {
		this.aimFile = aimFile;
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getSampleType() {
		return sampleType;
	}
	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
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
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierNameIn() {
		return supplierNameIn;
	}
	public void setSupplierNameIn(String supplierNameIn) {
		this.supplierNameIn = supplierNameIn;
	}
	public String getSupplierCodeIn() {
		return supplierCodeIn;
	}
	public void setSupplierCodeIn(String supplierCodeIn) {
		this.supplierCodeIn = supplierCodeIn;
	}
	public String getSupplierNameOut() {
		return supplierNameOut;
	}
	public void setSupplierNameOut(String supplierNameOut) {
		this.supplierNameOut = supplierNameOut;
	}
	public String getSupplierCodeOut() {
		return supplierCodeOut;
	}
	public void setSupplierCodeOut(String supplierCodeOut) {
		this.supplierCodeOut = supplierCodeOut;
	}
	public String getCustomerNeed() {
		return customerNeed;
	}
	public void setCustomerNeed(String customerNeed) {
		this.customerNeed = customerNeed;
	}
	
}

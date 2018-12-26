package com.ambition.epm.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name="EPM_ENTRUST_HSF_SUBLIST")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class EntrustHsfSublist extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String sampleName;//样品名称
	private String sampleMaterialCode;//样品物料编码
	private String model;//规格型号
	private String lotNo;//批号
	private Integer amount;//数量
	private Integer defectNumber;//不良数量
	private Integer invalidNumber;//无效数量
	private Integer defectRate;//不良率
	private String supplier;//供应商2
	private String supplierCode;//供应商2
	private String supplierIn;//供应商2
	private String supplierCodeIn;//供应商2
	private String supplierOut;//供应商2
	private String supplierCodeOut;//供应商2
	private String client;//客户1
	private String sampleStage;//样品阶段
	private String testItem;//测试项目
	private Date startTime;//预计开始时间
	private Date endTime;//预计结束时间
	private Date factStartTime;//实际开始时间
	private Date factEndTime;//实际结束时间
	private String testPoint;//测试点数
	private String ngPoint;//NG点数
	private String testBefore;//测试前结果
	private String testAfter;//测试后结果
	private String testReport;//测试报告
	private String exceptionNo;//异常处理单号
	private String exceptionNoHide;//异常处理单号
	private String hsfId;//hsfId
	private String sendItemList;
	private String remark;//备注
	private String riskGrade;//风险等级
	private String sampleSort;//样品类别
	private String testAfterIn;//对内测试后结果
	private String remarkIn;//对内备注
	private String testAfterOut;//对外测试后结果
	private String remarkOut;//对外对内备注
	@ManyToOne
	@JoinColumn(name="EPM_SUBLIST_ID")
    @JsonIgnore()
	private EntrustHsf entrustHsf;
	
	public EntrustHsf getEntrustHsf() {
		return entrustHsf;
	}
	public void setEntrustHsf(EntrustHsf entrustHsf) {
		this.entrustHsf = entrustHsf;
	}
	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
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
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getTestItem() {
		return testItem;
	}
	public void setTestItem(String testItem) {
		this.testItem = testItem;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getFactStartTime() {
		return factStartTime;
	}
	public void setFactStartTime(Date factStartTime) {
		this.factStartTime = factStartTime;
	}
	public Date getFactEndTime() {
		return factEndTime;
	}
	public void setFactEndTime(Date factEndTime) {
		this.factEndTime = factEndTime;
	}
	public String getTestBefore() {
		return testBefore;
	}
	public void setTestBefore(String testBefore) {
		this.testBefore = testBefore;
	}
	public String getTestAfter() {
		return testAfter;
	}
	public void setTestAfter(String testAfter) {
		this.testAfter = testAfter;
	}
	public String getTestReport() {
		return testReport;
	}
	public void setTestReport(String testReport) {
		this.testReport = testReport;
	}
	public String getExceptionNo() {
		return exceptionNo;
	}
	public void setExceptionNo(String exceptionNo) {
		this.exceptionNo = exceptionNo;
	}
	public String getHsfId() {
		return hsfId;
	}
	public void setHsfId(String hsfId) {
		this.hsfId = hsfId;
	}
	public String getSendItemList() {
		return sendItemList;
	}
	public void setSendItemList(String sendItemList) {
		this.sendItemList = sendItemList;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getExceptionNoHide() {
		return exceptionNoHide;
	}
	public void setExceptionNoHide(String exceptionNoHide) {
		this.exceptionNoHide = exceptionNoHide;
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
	public Integer getDefectRate() {
		return defectRate;
	}
	public void setDefectRate(Integer defectRate) {
		this.defectRate = defectRate;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getRiskGrade() {
		return riskGrade;
	}
	public void setRiskGrade(String riskGrade) {
		this.riskGrade = riskGrade;
	}
	public String getSampleSort() {
		return sampleSort;
	}
	public void setSampleSort(String sampleSort) {
		this.sampleSort = sampleSort;
	}
	public String getTestAfterIn() {
		return testAfterIn;
	}
	public void setTestAfterIn(String testAfterIn) {
		this.testAfterIn = testAfterIn;
	}
	public String getTestAfterOut() {
		return testAfterOut;
	}
	public void setTestAfterOut(String testAfterOut) {
		this.testAfterOut = testAfterOut;
	}
	public String getRemarkIn() {
		return remarkIn;
	}
	public void setRemarkIn(String remarkIn) {
		this.remarkIn = remarkIn;
	}
	public String getRemarkOut() {
		return remarkOut;
	}
	public void setRemarkOut(String remarkOut) {
		this.remarkOut = remarkOut;
	}
	public String getSampleMaterialCode() {
		return sampleMaterialCode;
	}
	public void setSampleMaterialCode(String sampleMaterialCode) {
		this.sampleMaterialCode = sampleMaterialCode;
	}
	public String getSampleStage() {
		return sampleStage;
	}
	public void setSampleStage(String sampleStage) {
		this.sampleStage = sampleStage;
	}
	public String getTestPoint() {
		return testPoint;
	}
	public void setTestPoint(String testPoint) {
		this.testPoint = testPoint;
	}
	public String getNgPoint() {
		return ngPoint;
	}
	public void setNgPoint(String ngPoint) {
		this.ngPoint = ngPoint;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierIn() {
		return supplierIn;
	}
	public void setSupplierIn(String supplierIn) {
		this.supplierIn = supplierIn;
	}
	public String getSupplierCodeIn() {
		return supplierCodeIn;
	}
	public void setSupplierCodeIn(String supplierCodeIn) {
		this.supplierCodeIn = supplierCodeIn;
	}
	public String getSupplierOut() {
		return supplierOut;
	}
	public void setSupplierOut(String supplierOut) {
		this.supplierOut = supplierOut;
	}
	public String getSupplierCodeOut() {
		return supplierCodeOut;
	}
	public void setSupplierCodeOut(String supplierCodeOut) {
		this.supplierCodeOut = supplierCodeOut;
	}
	
}

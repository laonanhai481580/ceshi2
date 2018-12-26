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
@Table(name="EPM_ENTRUST_ORT_SUBLIST")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class EntrustOrtSublist extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String properties;//试验项目
	private String testCondition;//测试条件
	private Integer testNumber;//测试数量
	private Integer defectNumber;//不良数量
	private Integer invalidNumber;//无效数量
	private String defectRate;//不良率
	private String criterionG;//判定标准
	private String criterionY;//判定标准
	private String criterionR;//判定标准
	
	private String quickResponse;//二维码
	private String  testStation  ; //测试工站
	private String  equipmentNumber;//测试设备
	private String  testItem;//测试项目
	private Integer lower;//规格下限
	private Integer upper;//规格上限
	private Integer testValue;//测试值
	private String upload;//是否上传

	
	
	private Date startTime;//预计开始时间
	private Date endTime;//预计结束时间
	private Date factStartTime;//实际开始时间
	private Date factEndTime;//实际结束时间
	private String testBefore;//测试前结果
	private String testAfter;//测试后结果
	private String testReport;//测试报告
	private String testResult;//测试结果
	private String exceptionNo;//异常处理单号
	private String exceptionNoHide;//异常处理单号
	private String ortId;//ortId
	private String remark;//备注
	private String testAfterIn;//对内测试后结果
	private Integer defectNumberIn;//对内不良数量
	private String remarkIn;//对内备注
	private String defectRateIn;//不良率
	private String testAfterOut;//对外测试后结果
	private Integer defectNumberOut;//对外不良数量
	private String remarkOut;//对外对内备注
	private String defectRateOut;//不良率
	@ManyToOne
	@JoinColumn(name="EPM_SUBLIST_ID")
    @JsonIgnore()
	private EntrustOrt entrustOrt;
	
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}
	public String getTestCondition() {
		return testCondition;
	}
	public void setTestCondition(String testCondition) {
		this.testCondition = testCondition;
	}
	public String getCriterionG() {
		return criterionG;
	}
	public void setCriterionG(String criterionG) {
		this.criterionG = criterionG;
	}
	public String getCriterionY() {
		return criterionY;
	}
	public void setCriterionY(String criterionY) {
		this.criterionY = criterionY;
	}
	public String getCriterionR() {
		return criterionR;
	}
	public void setCriterionR(String criterionR) {
		this.criterionR = criterionR;
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
	public String getTestResult() {
		return testResult;
	}
	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}
	public EntrustOrt getEntrustOrt() {
		return entrustOrt;
	}
	public void setEntrustOrt(EntrustOrt entrustOrt) {
		this.entrustOrt = entrustOrt;
	}
	public String getExceptionNo() {
		return exceptionNo;
	}
	public void setExceptionNo(String exceptionNo) {
		this.exceptionNo = exceptionNo;
	}
	public String getOrtId() {
		return ortId;
	}
	public void setOrtId(String ortId) {
		this.ortId = ortId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDefectRate() {
		return defectRate;
	}
	public void setDefectRate(String defectRate) {
		this.defectRate = defectRate;
	}
	public String getExceptionNoHide() {
		return exceptionNoHide;
	}
	public void setExceptionNoHide(String exceptionNoHide) {
		this.exceptionNoHide = exceptionNoHide;
	}
	public Integer getTestNumber() {
		return testNumber;
	}
	public void setTestNumber(Integer testNumber) {
		this.testNumber = testNumber;
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
	public String getTestAfterIn() {
		return testAfterIn;
	}
	public void setTestAfterIn(String testAfterIn) {
		this.testAfterIn = testAfterIn;
	}
	public Integer getDefectNumberIn() {
		return defectNumberIn;
	}
	public void setDefectNumberIn(Integer defectNumberIn) {
		this.defectNumberIn = defectNumberIn;
	}
	public String getTestAfterOut() {
		return testAfterOut;
	}
	public void setTestAfterOut(String testAfterOut) {
		this.testAfterOut = testAfterOut;
	}
	public Integer getDefectNumberOut() {
		return defectNumberOut;
	}
	public void setDefectNumberOut(Integer defectNumberOut) {
		this.defectNumberOut = defectNumberOut;
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
	public String getDefectRateIn() {
		return defectRateIn;
	}
	public void setDefectRateIn(String defectRateIn) {
		this.defectRateIn = defectRateIn;
	}
	public String getDefectRateOut() {
		return defectRateOut;
	}
	public void setDefectRateOut(String defectRateOut) {
		this.defectRateOut = defectRateOut;
	}
	public String getTestStation() {
		return testStation;
	}
	public void setTestStation(String testStation) {
		this.testStation = testStation;
	}
	public String getEquipmentNumber() {
		return equipmentNumber;
	}
	public void setEquipmentNumber(String equipmentNumber) {
		this.equipmentNumber = equipmentNumber;
	}
	public String getTestItem() {
		return testItem;
	}
	public void setTestItem(String testItem) {
		this.testItem = testItem;
	}
	public Integer getLower() {
		return lower;
	}
	public void setLower(Integer lower) {
		this.lower = lower;
	}
	public Integer getUpper() {
		return upper;
	}
	public void setUpper(Integer upper) {
		this.upper = upper;
	}
	public Integer getTestValue() {
		return testValue;
	}
	public void setTestValue(Integer testValue) {
		this.testValue = testValue;
	}
	public String getQuickResponse() {
		return quickResponse;
	}
	public void setQuickResponse(String quickResponse) {
		this.quickResponse = quickResponse;
	}
	public String getUpload() {
		return upload;
	}
	public void setUpload(String upload) {
		this.upload = upload;
	}

	
}

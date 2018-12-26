package com.ambition.gsm.entity;

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

/**
 * 类名:內校报告
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016-11-26 发布
 */
@Entity
@Table(name = "GSM_INNER_CHECK_REPORT")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class GsmInnerCheckReport  extends WorkflowIdEntity{

	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "GSM_INNER_CHECK_REPORT";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "內校报告";//实体_列表_名称
	
	private String formNo;//报告编号
	private String measurementName;//仪器名称
	private String measurementSpecification;//规格型号
	private String manufacturer;//制造厂商 
	private String managementNo;//管理编号
	private String departMent;//使用部门
	private String installPlace;//安装地点
	private String frequency;//频率
	private String measurementAppearance;//仪器外观
	private String remark1;//情况说明
	private String indicationError;//示值误差
	private String checkResult;//校验结果
	private String checkBasis;//校验依据
	private String temperature;//温度
	private String humidity;//湿度
	private String checkPlace;//地点
	private String checkMan;//校验人
	private String checkManLogin;//校验人
	private Date checkDate;//校验日期
	private Date nextCheckDate;//下次校验日期
	private String remark2;//备注
	private String planId;//计划ID
	private String hasNext;//是否生成过下次校验计划
	private String gsmState;//隐藏状态
	
	private String auditMan;//审核人
	private String auditManLogin;//审核人登录名
	private Date auditDate;//审核日期
	private String auditText;//审核意见
	private String dutyMan;//责任人
	private String dutyLoginMan;//责任人登录名
	private String copyMan;//抄送人
	private String copyLoginMan;//抄送登录名
	@OneToMany(mappedBy="gsmInnerCheckReport",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	private List<CheckReportDetail> checkReportDetails;//检验项目	
	
	@OneToMany(mappedBy="gsmInnerCheckReport",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	private List<CheckReportItem> checkReportItems;//校验标准件

	public String getFormNo() {
		return formNo;
	}

	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}

	public String getMeasurementName() {
		return measurementName;
	}

	public void setMeasurementName(String measurementName) {
		this.measurementName = measurementName;
	}

	public String getMeasurementSpecification() {
		return measurementSpecification;
	}

	public void setMeasurementSpecification(String measurementSpecification) {
		this.measurementSpecification = measurementSpecification;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getManagementNo() {
		return managementNo;
	}

	public void setManagementNo(String managementNo) {
		this.managementNo = managementNo;
	}

	public String getDepartMent() {
		return departMent;
	}

	public void setDepartMent(String departMent) {
		this.departMent = departMent;
	}

	public String getInstallPlace() {
		return installPlace;
	}

	public void setInstallPlace(String installPlace) {
		this.installPlace = installPlace;
	}

	public String getMeasurementAppearance() {
		return measurementAppearance;
	}

	public void setMeasurementAppearance(String measurementAppearance) {
		this.measurementAppearance = measurementAppearance;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getIndicationError() {
		return indicationError;
	}

	public void setIndicationError(String indicationError) {
		this.indicationError = indicationError;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String getCheckBasis() {
		return checkBasis;
	}

	public void setCheckBasis(String checkBasis) {
		this.checkBasis = checkBasis;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getCheckPlace() {
		return checkPlace;
	}

	public void setCheckPlace(String checkPlace) {
		this.checkPlace = checkPlace;
	}

	public String getCheckMan() {
		return checkMan;
	}

	public void setCheckMan(String checkMan) {
		this.checkMan = checkMan;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Date getNextCheckDate() {
		return nextCheckDate;
	}

	public void setNextCheckDate(Date nextCheckDate) {
		this.nextCheckDate = nextCheckDate;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public List<CheckReportDetail> getCheckReportDetails() {
		return checkReportDetails;
	}

	public void setCheckReportDetails(List<CheckReportDetail> checkReportDetails) {
		this.checkReportDetails = checkReportDetails;
	}

	public List<CheckReportItem> getCheckReportItems() {
		return checkReportItems;
	}

	public void setCheckReportItems(List<CheckReportItem> checkReportItems) {
		this.checkReportItems = checkReportItems;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getHasNext() {
		return hasNext;
	}

	public void setHasNext(String hasNext) {
		this.hasNext = hasNext;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getAuditMan() {
		return auditMan;
	}

	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}

	public String getAuditManLogin() {
		return auditManLogin;
	}

	public void setAuditManLogin(String auditManLogin) {
		this.auditManLogin = auditManLogin;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditText() {
		return auditText;
	}

	public void setAuditText(String auditText) {
		this.auditText = auditText;
	}

	public String getCheckManLogin() {
		return checkManLogin;
	}

	public void setCheckManLogin(String checkManLogin) {
		this.checkManLogin = checkManLogin;
	}

	public String getGsmState() {
		return gsmState;
	}

	public void setGsmState(String gsmState) {
		this.gsmState = gsmState;
	}

	public String getDutyMan() {
		return dutyMan;
	}

	public void setDutyMan(String dutyMan) {
		this.dutyMan = dutyMan;
	}

	public String getDutyLoginMan() {
		return dutyLoginMan;
	}

	public void setDutyLoginMan(String dutyLoginMan) {
		this.dutyLoginMan = dutyLoginMan;
	}

	public String getCopyMan() {
		return copyMan;
	}

	public void setCopyMan(String copyMan) {
		this.copyMan = copyMan;
	}

	public String getCopyLoginMan() {
		return copyLoginMan;
	}

	public void setCopyLoginMan(String copyLoginMan) {
		this.copyLoginMan = copyLoginMan;
	}
	
}

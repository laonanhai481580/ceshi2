package com.ambition.iqc.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 类名:实验委托管理
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  wuxuming
 * @version 1.00 2016-10-24 发布
 */
@Entity
@Table(name="IQC_EXPERIMENTAL_DELEGATION")
public class ExperimentalDelegation  extends WorkflowIdEntity{

	private static final long serialVersionUID = 1L;
	/**
	 * 填单
	 * */
	private String experimentalNo;//实验委托编码
	private String iqcReportNo;//检验报告编码
	private String sampleCode;//样品编码
	private String sampleName;//样品名称
	private String specificationModel;//规格型号
	private String supplierCode;//供应商编码
	private String supplierName;//供应商名称
	private String meachineNo;//机种编号
	private String coustomerCode;//客户编码
	private String coustomerName;//客户名称
	private String batchNo;//批号
	private String emergencyDegree;//紧急度
	private Double sampleAmount;//样品数量
	private Date  consignableDate;//申请日期
	private String consignor;//申请人
	private String consignDev;//申请部门
	@Column(length=3000)
	private String sampleDiscription;//样品描述
	private String chooseMan;//选择实验人
	private String chooseLoginMan;//实验人员登录名
	private String hsfOrt;//HSF/ORT 
	private String hsf;//HSF
	private String hsfDetailItem;//HSF下的几项
	private String ort;//ORT
	private String other;//其他  OTHER
	private String testProject;//测试项目
	private String testHsf;//HSF
	private String testOrt;//ORT
	private String testOther;//其他other
	@Column(length=3000)
	private String purpose;//实验目的
	
	private String file1;
	private String hisFile1;
	private String file2;
	private String hisFile2;
	private String file3;
	private String hisFile3;
	private String file4;
	private String hisFile4;
	private String file5;
	private String hisFile5;
	private String file6;
	private String hisFile6;
	private String file7;
	private String hisFile7;
	private String file8;
	private String hisFile8;
	
	private String experimentalMan;//实验员
	private String experimentalManLogin;//实验员登录名
	private String experimentalResult;//实验结果
	private String reportFormNo;//检验报告编号
	/**
	 * 主管审核
	 * */
	@Column(length=3000)
	private String auditText;//主管审核
	private Date auditTime;//审核时间
	private String auditMan;//审核人
	private String auditManLogin;//审核人登录名
	@OneToMany(mappedBy = "experimentalDelegation",cascade={CascadeType.ALL})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	private List<OrtExperimentalItem> ortItems;
	public String getExperimentalNo() {
		return experimentalNo;
	}
	public void setExperimentalNo(String experimentalNo) {
		this.experimentalNo = experimentalNo;
	}
	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	public String getSpecificationModel() {
		return specificationModel;
	}
	public void setSpecificationModel(String specificationModel) {
		this.specificationModel = specificationModel;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getMeachineNo() {
		return meachineNo;
	}
	public void setMeachineNo(String meachineNo) {
		this.meachineNo = meachineNo;
	}
	public String getCoustomerCode() {
		return coustomerCode;
	}
	public void setCoustomerCode(String coustomerCode) {
		this.coustomerCode = coustomerCode;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getEmergencyDegree() {
		return emergencyDegree;
	}
	public void setEmergencyDegree(String emergencyDegree) {
		this.emergencyDegree = emergencyDegree;
	}
	public Double getSampleAmount() {
		return sampleAmount;
	}
	public void setSampleAmount(Double sampleAmount) {
		this.sampleAmount = sampleAmount;
	}
	public Date getConsignableDate() {
		return consignableDate;
	}
	public void setConsignableDate(Date consignableDate) {
		this.consignableDate = consignableDate;
	}
	public String getConsignor() {
		return consignor;
	}
	public void setConsignor(String consignor) {
		this.consignor = consignor;
	}
	public String getConsignDev() {
		return consignDev;
	}
	public void setConsignDev(String consignDev) {
		this.consignDev = consignDev;
	}
	public String getSampleDiscription() {
		return sampleDiscription;
	}
	public void setSampleDiscription(String sampleDiscription) {
		this.sampleDiscription = sampleDiscription;
	}
	public String getHsf() {
		return hsf;
	}
	public void setHsf(String hsf) {
		this.hsf = hsf;
	}
	public String getHsfDetailItem() {
		return hsfDetailItem;
	}
	public void setHsfDetailItem(String hsfDetailItem) {
		this.hsfDetailItem = hsfDetailItem;
	}
	public String getOrt() {
		return ort;
	}
	public void setOrt(String ort) {
		this.ort = ort;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getExperimentalMan() {
		return experimentalMan;
	}
	public void setExperimentalMan(String experimentalMan) {
		this.experimentalMan = experimentalMan;
	}
	public String getExperimentalResult() {
		return experimentalResult;
	}
	public void setExperimentalResult(String experimentalResult) {
		this.experimentalResult = experimentalResult;
	}
	public String getReportFormNo() {
		return reportFormNo;
	}
	public void setReportFormNo(String reportFormNo) {
		this.reportFormNo = reportFormNo;
	}
	public String getAuditText() {
		return auditText;
	}
	public void setAuditText(String auditText) {
		this.auditText = auditText;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public String getAuditMan() {
		return auditMan;
	}
	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}
	
	public List<OrtExperimentalItem> getOrtItems() {
		return ortItems;
	}
	public void setOrtItems(List<OrtExperimentalItem> ortItems) {
		this.ortItems = ortItems;
	}
	public String getCoustomerName() {
		return coustomerName;
	}
	public void setCoustomerName(String coustomerName) {
		this.coustomerName = coustomerName;
	}
	public String getHsfOrt() {
		return hsfOrt;
	}
	public void setHsfOrt(String hsfOrt) {
		this.hsfOrt = hsfOrt;
	}
	public String getFile1() {
		return file1;
	}
	public void setFile1(String file1) {
		this.file1 = file1;
	}
	public String getHisFile1() {
		return hisFile1;
	}
	public void setHisFile1(String hisFile1) {
		this.hisFile1 = hisFile1;
	}
	public String getFile2() {
		return file2;
	}
	public void setFile2(String file2) {
		this.file2 = file2;
	}
	public String getHisFile2() {
		return hisFile2;
	}
	public void setHisFile2(String hisFile2) {
		this.hisFile2 = hisFile2;
	}
	public String getFile3() {
		return file3;
	}
	public void setFile3(String file3) {
		this.file3 = file3;
	}
	public String getHisFile3() {
		return hisFile3;
	}
	public void setHisFile3(String hisFile3) {
		this.hisFile3 = hisFile3;
	}
	public String getFile4() {
		return file4;
	}
	public void setFile4(String file4) {
		this.file4 = file4;
	}
	public String getHisFile4() {
		return hisFile4;
	}
	public void setHisFile4(String hisFile4) {
		this.hisFile4 = hisFile4;
	}
	public String getFile5() {
		return file5;
	}
	public void setFile5(String file5) {
		this.file5 = file5;
	}
	public String getHisFile5() {
		return hisFile5;
	}
	public void setHisFile5(String hisFile5) {
		this.hisFile5 = hisFile5;
	}
	public String getFile6() {
		return file6;
	}
	public void setFile6(String file6) {
		this.file6 = file6;
	}
	public String getHisFile6() {
		return hisFile6;
	}
	public void setHisFile6(String hisFile6) {
		this.hisFile6 = hisFile6;
	}
	public String getFile7() {
		return file7;
	}
	public void setFile7(String file7) {
		this.file7 = file7;
	}
	public String getHisFile7() {
		return hisFile7;
	}
	public void setHisFile7(String hisFile7) {
		this.hisFile7 = hisFile7;
	}
	public String getFile8() {
		return file8;
	}
	public void setFile8(String file8) {
		this.file8 = file8;
	}
	public String getHisFile8() {
		return hisFile8;
	}
	public void setHisFile8(String hisFile8) {
		this.hisFile8 = hisFile8;
	}
	public String getSampleCode() {
		return sampleCode;
	}
	public void setSampleCode(String sampleCode) {
		this.sampleCode = sampleCode;
	}
	public String getIqcReportNo() {
		return iqcReportNo;
	}
	public void setIqcReportNo(String iqcReportNo) {
		this.iqcReportNo = iqcReportNo;
	}
	public String getChooseMan() {
		return chooseMan;
	}
	public void setChooseMan(String chooseMan) {
		this.chooseMan = chooseMan;
	}
	public String getChooseLoginMan() {
		return chooseLoginMan;
	}
	public void setChooseLoginMan(String chooseLoginMan) {
		this.chooseLoginMan = chooseLoginMan;
	}
	public String getTestProject() {
		return testProject;
	}
	public void setTestProject(String testProject) {
		this.testProject = testProject;
	}
	public String getTestHsf() {
		return testHsf;
	}
	public void setTestHsf(String testHsf) {
		this.testHsf = testHsf;
	}
	public String getTestOrt() {
		return testOrt;
	}
	public void setTestOrt(String testOrt) {
		this.testOrt = testOrt;
	}
	public String getTestOther() {
		return testOther;
	}
	public void setTestOther(String testOther) {
		this.testOther = testOther;
	}
	public String getExperimentalManLogin() {
		return experimentalManLogin;
	}
	public void setExperimentalManLogin(String experimentalManLogin) {
		this.experimentalManLogin = experimentalManLogin;
	}
	public String getAuditManLogin() {
		return auditManLogin;
	}
	public void setAuditManLogin(String auditManLogin) {
		this.auditManLogin = auditManLogin;
	}
	

}

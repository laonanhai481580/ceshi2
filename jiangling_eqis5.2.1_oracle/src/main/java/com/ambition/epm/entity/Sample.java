package com.ambition.epm.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 类名:实验室样品管理
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  xujiannan
 * @version 1.00 2017年3月3日 发布
 */
@Entity
@Table(name="EPM_SAMPLE")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Sample extends WorkflowIdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "EPM_SAMPLE";//实体编码
	public static final String ENTITY_LIST_NAME = "样品管理";//实体名称
	private String formNo;//表单编号
	private String reportNo;//报告编号
	private String sampleCode;//样品喷码
	@Temporal(TemporalType.TIMESTAMP)
	private Date sendDate;//送样日期
	private String customerNo;//客户
	private String productNo;//机种
	private String inspectionDapt;//送检部门
	private String inspectionPerson;//送检人
	private String inspectionPersonLogin;//送检人登录名
	private String sampleName;//样品名称
	private Integer quantity;//样品数量
	@Temporal(TemporalType.TIMESTAMP)
	private Date receivedDate;//收样日期
	@Temporal(TemporalType.TIMESTAMP)
	private Date receivedTime;//收样时间
	private String specimenAdmin;//样品管理人
	private String specimenAdminLogin;//样品管理人登录名
	private Date grantDate;//发放时间
	private String grantNumber;//发放数量
	private String testEngineer;//测试员
	private String testEngineerLogin;//测试员登录名
	@Temporal(TemporalType.TIMESTAMP)
	private Date returnDate;//归还日期
	private String returnQuantity;//归还数量
	private String sampleHandling;//样品处理
	@Temporal(TemporalType.TIMESTAMP)
	private Date samplesReceived;//样品接收时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date scrapDate;//报废时间
	private String scrapQuantity;//报废数量
	private String scrapPerson;//报废处理人
	private String remark;//备注
	private String remark2;//样品确认留样
	private String sampleSum;//样品总数
	private String sampletype;//归还状态
	private String defectNumber;//不良数量
	private String invalidNumber;//无效数量
	private String defectRate;//不良率
	private String requisitionNo;//领料单号
	private String rejectedMaterialNo;//退料单号
	private String turnoverNo;//周转单号
	
	@OneToMany(mappedBy = "sample",cascade={CascadeType.ALL})
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	private List<SampleSublist> sampleSublists;//样品借还
	
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
	public String getSampleCode() {
		return sampleCode;
	}
	public void setSampleCode(String sampleCode) {
		this.sampleCode = sampleCode;
	}
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	public Date getReceivedTime() {
		return receivedTime;
	}
	public void setReceivedTime(Date receivedTime) {
		this.receivedTime = receivedTime;
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
	public String getInspectionDapt() {
		return inspectionDapt;
	}
	public void setInspectionDapt(String inspectionDapt) {
		this.inspectionDapt = inspectionDapt;
	}
	public String getInspectionPerson() {
		return inspectionPerson;
	}
	public void setInspectionPerson(String inspectionPerson) {
		this.inspectionPerson = inspectionPerson;
	}
	
	public Date getSamplesReceived() {
		return samplesReceived;
	}
	public void setSamplesReceived(Date samplesReceived) {
		this.samplesReceived = samplesReceived;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public String getReturnQuantity() {
		return returnQuantity;
	}
	public void setReturnQuantity(String returnQuantity) {
		this.returnQuantity = returnQuantity;
	}
	public Date getScrapDate() {
		return scrapDate;
	}
	public void setScrapDate(Date scrapDate) {
		this.scrapDate = scrapDate;
	}
	public String getScrapQuantity() {
		return scrapQuantity;
	}
	public void setScrapQuantity(String scrapQuantity) {
		this.scrapQuantity = scrapQuantity;
	}
	public String getScrapPerson() {
		return scrapPerson;
	}
	public void setScrapPerson(String scrapPerson) {
		this.scrapPerson = scrapPerson;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public String getSpecimenAdmin() {
		return specimenAdmin;
	}
	public void setSpecimenAdmin(String specimenAdmin) {
		this.specimenAdmin = specimenAdmin;
	}
	public String getSpecimenAdminLogin() {
		return specimenAdminLogin;
	}
	public void setSpecimenAdminLogin(String specimenAdminLogin) {
		this.specimenAdminLogin = specimenAdminLogin;
	}
	public Date getGrantDate() {
		return grantDate;
	}
	public void setGrantDate(Date grantDate) {
		this.grantDate = grantDate;
	}
	public String getGrantNumber() {
		return grantNumber;
	}
	public void setGrantNumber(String grantNumber) {
		this.grantNumber = grantNumber;
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
	public String getInspectionPersonLogin() {
		return inspectionPersonLogin;
	}
	public void setInspectionPersonLogin(String inspectionPersonLogin) {
		this.inspectionPersonLogin = inspectionPersonLogin;
	}
	public String getSampleHandling() {
		return sampleHandling;
	}
	public void setSampleHandling(String sampleHandling) {
		this.sampleHandling = sampleHandling;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public String getSampleSum() {
		return sampleSum;
	}
	public void setSampleSum(String sampleSum) {
		this.sampleSum = sampleSum;
	}
	public List<SampleSublist> getSampleSublists() {
		return sampleSublists;
	}
	public void setSampleSublists(List<SampleSublist> sampleSublists) {
		this.sampleSublists = sampleSublists;
	}
	public String getSampletype() {
		return sampletype;
	}
	public void setSampletype(String sampletype) {
		this.sampletype = sampletype;
	}
	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	public String getDefectNumber() {
		return defectNumber;
	}
	public void setDefectNumber(String defectNumber) {
		this.defectNumber = defectNumber;
	}
	public String getInvalidNumber() {
		return invalidNumber;
	}
	public void setInvalidNumber(String invalidNumber) {
		this.invalidNumber = invalidNumber;
	}
	public String getDefectRate() {
		return defectRate;
	}
	public void setDefectRate(String defectRate) {
		this.defectRate = defectRate;
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
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
}

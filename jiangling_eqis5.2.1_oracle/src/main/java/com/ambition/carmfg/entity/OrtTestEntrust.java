package com.ambition.carmfg.entity;

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
 * 
 * 类名:实验委托单
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月6日 发布
 */
@Entity
@Table(name = "MFG_ORT_TEST_ENTRUST")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class OrtTestEntrust extends WorkflowIdEntity{

	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "MFG_ORT_TEST_ENTRUST";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "ORT实验委托单";//实体_列表_名称
	private String formNo;//表单编号
	private String simpleName;//样品名称
	private String specification;//规格型号
	private String supplier;//供应商
	private String productNo;//机种编号
	private String customerNo;//客户编号
	private String customer;//客户
	private String lotNo;//批号
	private String emergencyDegree;//紧急程度
	private Integer simpleAmount;//样品数量
	private Date consignableDate;//申请日期
	private String consignor;//申请人
	private String consignorLogin;//申请人登录名
	private String department;//申请部门
	private String enterpriseGroup;//事业群
	private String simpleDiscription;//样品描述
	private String testProject;//测试项目
	private String testHsf;//HSF
	private String testOrt;//ORT
	private String testOther;//其他other
	private String purpose;//测试目的
	private String operator;//实验员
	private String operatorLogin;//实验员登录名
	private String testResult;//测试结果
	private String reportNo;//测试报告编号
	private String testDirector;//实验室主管
	private Date shenheDate;//审核日期
	private String testDirectorLogin;//实验室主管登录名
	private String directorComment;//主管意见
	private String attachment;//附件
	private String planId;//对应计划的ID
	private String entrustState;//委托状态
	@OneToMany(mappedBy = "ortTestEntrust",cascade={CascadeType.ALL})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	List<OrtTestItem> ortTestItems;
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getSimpleName() {
		return simpleName;
	}
	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public String getEmergencyDegree() {
		return emergencyDegree;
	}
	public void setEmergencyDegree(String emergencyDegree) {
		this.emergencyDegree = emergencyDegree;
	}
	public Integer getSimpleAmount() {
		return simpleAmount;
	}
	public void setSimpleAmount(Integer simpleAmount) {
		this.simpleAmount = simpleAmount;
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
	public String getConsignorLogin() {
		return consignorLogin;
	}
	public void setConsignorLogin(String consignorLogin) {
		this.consignorLogin = consignorLogin;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getEnterpriseGroup() {
		return enterpriseGroup;
	}
	public void setEnterpriseGroup(String enterpriseGroup) {
		this.enterpriseGroup = enterpriseGroup;
	}
	public String getSimpleDiscription() {
		return simpleDiscription;
	}
	public void setSimpleDiscription(String simpleDiscription) {
		this.simpleDiscription = simpleDiscription;
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
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOperatorLogin() {
		return operatorLogin;
	}
	public void setOperatorLogin(String operatorLogin) {
		this.operatorLogin = operatorLogin;
	}
	public String getTestResult() {
		return testResult;
	}
	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getTestDirector() {
		return testDirector;
	}
	public void setTestDirector(String testDirector) {
		this.testDirector = testDirector;
	}
	public String getTestDirectorLogin() {
		return testDirectorLogin;
	}
	public void setTestDirectorLogin(String testDirectorLogin) {
		this.testDirectorLogin = testDirectorLogin;
	}
	public String getDirectorComment() {
		return directorComment;
	}
	public void setDirectorComment(String directorComment) {
		this.directorComment = directorComment;
	}
	public List<OrtTestItem> getOrtTestItems() {
		return ortTestItems;
	}
	public void setOrtTestItems(List<OrtTestItem> ortTestItems) {
		this.ortTestItems = ortTestItems;
	}
	public Date getShenheDate() {
		return shenheDate;
	}
	public void setShenheDate(Date shenheDate) {
		this.shenheDate = shenheDate;
	}
	public String getTestProject() {
		return testProject;
	}
	public void setTestProject(String testProject) {
		this.testProject = testProject;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getEntrustState() {
		return entrustState;
	}
	public void setEntrustState(String entrustState) {
		this.entrustState = entrustState;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}


	
	
}

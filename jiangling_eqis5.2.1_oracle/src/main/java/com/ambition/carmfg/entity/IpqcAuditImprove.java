package com.ambition.carmfg.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:IPQC稽核问题点改善
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月9日 发布
 */
@Entity
@Table(name = "MFG_IPQC_AUDIT_IMPROVE")//MFG_IPQC_AUDIT_IMPROVE
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class IpqcAuditImprove  extends WorkflowIdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "MFG_IPQC_AUDIT_IMPROVE";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "IPQC稽核问题点改善";//实体_列表_名称
	private String formNo;//表单编号
	private String businessUnit;//事业部
	private Date auditDate;//稽核日期
	private String classGroup;//班次
	private String station;//站别
    private String ofilmModel;// 欧菲机种
	private String operator;//稽核人员
	private String operatorLogin;//稽核人员登录名
	private String problemDescribe;//问题描述
	private String problemDegree;//问题严重度
	private String auditType;//稽核类别
	private String missingItems;//缺失项目
	private String department;//责任单位
	private Date  planDate;//计划完成日
	private String auditMan;//作业员
	
	private String reasonAnalysis;//原因分析
	private String improveMeasures;//改善对策
	private String departmentMan;//责任单位填写人
	private String departmentManLogin;//责任单位填写人
	private String approveMan;//核准人
	private String approveManLogin;//核准人
	private String  comfirmComment;//确认意见
	private Date departmentDate;//责任单位填写时间
	private Date jieanDate;//结案时间
	private String jiean;//是否结案
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public String getClassGroup() {
		return classGroup;
	}
	public void setClassGroup(String classGroup) {
		this.classGroup = classGroup;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getProblemDescribe() {
		return problemDescribe;
	}
	public void setProblemDescribe(String problemDescribe) {
		this.problemDescribe = problemDescribe;
	}
	public String getProblemDegree() {
		return problemDegree;
	}
	public void setProblemDegree(String problemDegree) {
		this.problemDegree = problemDegree;
	}
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public String getMissingItems() {
		return missingItems;
	}
	public void setMissingItems(String missingItems) {
		this.missingItems = missingItems;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getImproveMeasures() {
		return improveMeasures;
	}
	public void setImproveMeasures(String improveMeasures) {
		this.improveMeasures = improveMeasures;
	}

	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}

	public String getJiean() {
		return jiean;
	}
	public void setJiean(String jiean) {
		this.jiean = jiean;
	}
	public String getAuditMan() {
		return auditMan;
	}
	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}
	public String getReasonAnalysis() {
		return reasonAnalysis;
	}
	public void setReasonAnalysis(String reasonAnalysis) {
		this.reasonAnalysis = reasonAnalysis;
	}
	public String getDepartmentMan() {
		return departmentMan;
	}
	public void setDepartmentMan(String departmentMan) {
		this.departmentMan = departmentMan;
	}
	public String getComfirmComment() {
		return comfirmComment;
	}
	public void setComfirmComment(String comfirmComment) {
		this.comfirmComment = comfirmComment;
	}
	public Date getJieanDate() {
		return jieanDate;
	}
	public void setJieanDate(Date jieanDate) {
		this.jieanDate = jieanDate;
	}
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public Date getDepartmentDate() {
		return departmentDate;
	}
	public void setDepartmentDate(Date departmentDate) {
		this.departmentDate = departmentDate;
	}
	public String getOperatorLogin() {
		return operatorLogin;
	}
	public void setOperatorLogin(String operatorLogin) {
		this.operatorLogin = operatorLogin;
	}
	public String getDepartmentManLogin() {
		return departmentManLogin;
	}
	public void setDepartmentManLogin(String departmentManLogin) {
		this.departmentManLogin = departmentManLogin;
	}
	public String getApproveMan() {
		return approveMan;
	}
	public void setApproveMan(String approveMan) {
		this.approveMan = approveMan;
	}
	public String getApproveManLogin() {
		return approveManLogin;
	}
	public void setApproveManLogin(String approveManLogin) {
		this.approveManLogin = approveManLogin;
	}
	public String getOfilmModel() {
		return ofilmModel;
	}
	public void setOfilmModel(String ofilmModel) {
		this.ofilmModel = ofilmModel;
	}
	
	

}

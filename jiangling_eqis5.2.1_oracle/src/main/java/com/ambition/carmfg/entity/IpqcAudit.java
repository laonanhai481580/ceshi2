package com.ambition.carmfg.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:IPQC稽核
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年8月31日 发布
 */
@Entity
@Table(name = "MFG_IPQC_AUDIT")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class IpqcAudit extends IdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "MFG_IPQC_AUDIT";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "IPQC稽核";//实体_列表_名称
	//private String businessUnit;//事业部
	private String factory; //工厂
	private String processSection; // 制程区段
	private Date   auditDate;//稽核日期
	private String classGroup;//班次
    private String ofilmModel;// 欧菲机种
	private String station;//站别
	private String operator;//作业员
	private String problemDescribe;//问题描述
	private String problemDegree;//问题严重度
	private String auditType;//稽核类别
	private String missingItems;//缺失项目
	private String department;//责任单位
	private String improveMeasures;//改善对策
	private String dutyMan;//担当者
	private String dutyManager;//责任主管
	private String dutyManagerLogin;//责任主管
	private Date  planDate;//计划完成日
	private Date actualDate;//时间完成日
	private String jiean;//结案
	private String auditMan;//稽核人员  auditMan
	private String improveNo;//稽核报告编号
	private String improveId;//稽核报告编号
	private Integer auditCount;//稽核件数
	private Integer badCount=1;//问题件数
	private String isMiss="否";//是否缺失件数
	private Integer problemScore;//问题分数
	private Integer auditScore;//稽核分数
	
	@OneToMany(mappedBy="ipqcAudit",cascade={CascadeType.ALL})
	@JsonIgnore
	List<IpqcWarnUser> ipqcWarnUsers;//IPQC稽核邮件发送人
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
	public String getDutyMan() {
		return dutyMan;
	}
	public void setDutyMan(String dutyMan) {
		this.dutyMan = dutyMan;
	}
	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	public Date getActualDate() {
		return actualDate;
	}
	public void setActualDate(Date actualDate) {
		this.actualDate = actualDate;
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
	public List<IpqcWarnUser> getIpqcWarnUsers() {
		return ipqcWarnUsers;
	}
	public void setIpqcWarnUsers(List<IpqcWarnUser> ipqcWarnUsers) {
		this.ipqcWarnUsers = ipqcWarnUsers;
	}
	public String getProcessSection() {
		return processSection;
	}
	public void setProcessSection(String processSection) {
		this.processSection = processSection;
	}
	public String getDutyManager() {
		return dutyManager;
	}
	public void setDutyManager(String dutyManager) {
		this.dutyManager = dutyManager;
	}
	public String getDutyManagerLogin() {
		return dutyManagerLogin;
	}
	
	
	public String getOfilmModel() {
		return ofilmModel;
	}
	public void setOfilmModel(String ofilmModel) {
		this.ofilmModel = ofilmModel;
	}
	public void setDutyManagerLogin(String dutyManagerLogin) {
		this.dutyManagerLogin = dutyManagerLogin;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getImproveNo() {
		return improveNo;
	}
	public void setImproveNo(String improveNo) {
		this.improveNo = improveNo;
	}
	public String getImproveId() {
		return improveId;
	}
	public void setImproveId(String improveId) {
		this.improveId = improveId;
	}
	public Integer getAuditCount() {
		return auditCount;
	}
	public void setAuditCount(Integer auditCount) {
		this.auditCount = auditCount;
	}
	public Integer getBadCount() {
		return badCount;
	}
	public void setBadCount(Integer badCount) {
		this.badCount = badCount;
	}
	public String getIsMiss() {
		return isMiss;
	}
	public void setIsMiss(String isMiss) {
		this.isMiss = isMiss;
	}
	public Integer getProblemScore() {
		return problemScore;
	}
	public void setProblemScore(Integer problemScore) {
		this.problemScore = problemScore;
	}
	public Integer getAuditScore() {
		return auditScore;
	}
	public void setAuditScore(Integer auditScore) {
		this.auditScore = auditScore;
	}

	
	
	

}

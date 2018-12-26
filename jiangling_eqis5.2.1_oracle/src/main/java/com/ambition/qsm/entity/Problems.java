package com.ambition.qsm.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:体系-内审问题
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  XJN
 * @version 1.00 2017年5月11日 发布
 */
@Entity
@Table(name = "QSM_INNERAUDIT_PROBLEMS")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Problems extends WorkflowIdEntity{
	
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "QSM_INNERAUDIT_PROBLEMS";//实体编码
	public static final String ENTITY_LIST_NAME = "内审问题";//实体名称
	private String formNo;//表单编号
	private String auditType;//体系类别
	private Date auditDate;//审核开始日期
	private Date auditEndDate;//审核结束日期
	private String trialDepartment;//受审部门
	private String violatePact;//违反条约
	private String degree;//问题重要等级
	@Column(length=1000)
	private String problemDescrible;//问题点描述
	private String department;//责任部门	
	private String dutyMan;//责任人
	private String dutyManLogin;//责任人登录名
	private String dutyManLead;//责任人主管
	private String dutyManLeadLogin;//责任人主管登录名
	private String consignor;//提交人
	private String consignorLogin;//提交人
	private Date consignableDate;//提交时间
	@Column(length=1000)
	private String reasonAnalysis;//原因分析
	@Column(length=1000)
	private String improveMeasures;//改善对策
	private String improveFile;//改善附件
	private Date improveDate;//改善时间
	private String auditor;//审核人
	private String auditorLogin;//审核人登录名
	private String auditingOpinion;//审核意见
	private Date auditorDate;//审核日期
	private String authorizer;//核准人
	private String authorizerLogin;//核准人登录名
	private Date authorizerDate;//核准时间
	private Date compliteDate;//计划完成时间
	private Date actuallyDate;//实际完成时间
	private String closeState;//关闭状态
	@Column(length=1000)
	private String improveEffect;//改善效果
	private String remark;//备注
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public Date getAuditEndDate() {
		return auditEndDate;
	}
	public void setAuditEndDate(Date auditEndDate) {
		this.auditEndDate = auditEndDate;
	}
	public String getProblemDescrible() {
		return problemDescrible;
	}
	public void setProblemDescrible(String problemDescrible) {
		this.problemDescrible = problemDescrible;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDutyMan() {
		return dutyMan;
	}
	public void setDutyMan(String dutyMan) {
		this.dutyMan = dutyMan;
	}
	public String getDutyManLogin() {
		return dutyManLogin;
	}
	public void setDutyManLogin(String dutyManLogin) {
		this.dutyManLogin = dutyManLogin;
	}
	public String getDutyManLead() {
		return dutyManLead;
	}
	public void setDutyManLead(String dutyManLead) {
		this.dutyManLead = dutyManLead;
	}
	public String getDutyManLeadLogin() {
		return dutyManLeadLogin;
	}
	public void setDutyManLeadLogin(String dutyManLeadLogin) {
		this.dutyManLeadLogin = dutyManLeadLogin;
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
	public Date getConsignableDate() {
		return consignableDate;
	}
	public void setConsignableDate(Date consignableDate) {
		this.consignableDate = consignableDate;
	}
	public String getReasonAnalysis() {
		return reasonAnalysis;
	}
	public void setReasonAnalysis(String reasonAnalysis) {
		this.reasonAnalysis = reasonAnalysis;
	}
	public String getImproveMeasures() {
		return improveMeasures;
	}
	public void setImproveMeasures(String improveMeasures) {
		this.improveMeasures = improveMeasures;
	}
	public String getImproveFile() {
		return improveFile;
	}
	public void setImproveFile(String improveFile) {
		this.improveFile = improveFile;
	}
	public Date getImproveDate() {
		return improveDate;
	}
	public void setImproveDate(Date improveDate) {
		this.improveDate = improveDate;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public String getAuditorLogin() {
		return auditorLogin;
	}
	public void setAuditorLogin(String auditorLogin) {
		this.auditorLogin = auditorLogin;
	}
	public String getAuditingOpinion() {
		return auditingOpinion;
	}
	public void setAuditingOpinion(String auditingOpinion) {
		this.auditingOpinion = auditingOpinion;
	}
	public Date getAuditorDate() {
		return auditorDate;
	}
	public void setAuditorDate(Date auditorDate) {
		this.auditorDate = auditorDate;
	}
	public String getAuthorizer() {
		return authorizer;
	}
	public void setAuthorizer(String authorizer) {
		this.authorizer = authorizer;
	}
	public String getAuthorizerLogin() {
		return authorizerLogin;
	}
	public void setAuthorizerLogin(String authorizerLogin) {
		this.authorizerLogin = authorizerLogin;
	}
	public Date getAuthorizerDate() {
		return authorizerDate;
	}
	public void setAuthorizerDate(Date authorizerDate) {
		this.authorizerDate = authorizerDate;
	}
	public Date getCompliteDate() {
		return compliteDate;
	}
	public void setCompliteDate(Date compliteDate) {
		this.compliteDate = compliteDate;
	}
	public Date getActuallyDate() {
		return actuallyDate;
	}
	public void setActuallyDate(Date actuallyDate) {
		this.actuallyDate = actuallyDate;
	}
	public String getCloseState() {
		return closeState;
	}
	public void setCloseState(String closeState) {
		this.closeState = closeState;
	}
	public String getImproveEffect() {
		return improveEffect;
	}
	public void setImproveEffect(String improveEffect) {
		this.improveEffect = improveEffect;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTrialDepartment() {
		return trialDepartment;
	}
	public void setTrialDepartment(String trialDepartment) {
		this.trialDepartment = trialDepartment;
	}
	public String getViolatePact() {
		return violatePact;
	}
	public void setViolatePact(String violatePact) {
		this.violatePact = violatePact;
	}
	
	
}

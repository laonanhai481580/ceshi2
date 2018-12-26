package com.ambition.qsm.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:管理评审表
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月27日 发布
 */
@Entity
@Table(name = "QSM_MANAGE_REVIEW")//
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class ManageReview extends WorkflowIdEntity{

	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "QSM_MANAGE_REVIEW";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "管理评审表";//实体_列表_名称
	private String formNo;//表单编号
	private String companyName;//公司名称
	private String systemName;//体系名称
	private Date riqi;//日期
	
	//审核计划
	private String remark1;//评审计划意见
	private String attachment1;//计划附件
	private String submitter1;//计划提交人
	private String submitter1Login;//计划提交人登录名
	private String auditMan1;//计划审核人
	private String auditMan1Login;//计划审核人登录名
	private String approvedMan1;//计划核准人
	private String approvedMan1Login;//计划核准人登录名
	
	//管理报告
	private String remark2;//管理报告意见
	private String attachment2;//报告附件
	private String submitter2;//报告提交人
	private String submitter2Login;//报告提交人登录名
	private String auditMan2;//报告审核人
	private String auditMan2Login;//报告审核人登录名
	private String approvedMan2;//报告核准人
	private String approvedMan2Login;//报告核准人登录名
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public Date getRiqi() {
		return riqi;
	}
	public void setRiqi(Date riqi) {
		this.riqi = riqi;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public String getAttachment1() {
		return attachment1;
	}
	public void setAttachment1(String attachment1) {
		this.attachment1 = attachment1;
	}
	public String getSubmitter1() {
		return submitter1;
	}
	public void setSubmitter1(String submitter1) {
		this.submitter1 = submitter1;
	}
	public String getSubmitter1Login() {
		return submitter1Login;
	}
	public void setSubmitter1Login(String submitter1Login) {
		this.submitter1Login = submitter1Login;
	}
	public String getAuditMan1() {
		return auditMan1;
	}
	public void setAuditMan1(String auditMan1) {
		this.auditMan1 = auditMan1;
	}
	public String getAuditMan1Login() {
		return auditMan1Login;
	}
	public void setAuditMan1Login(String auditMan1Login) {
		this.auditMan1Login = auditMan1Login;
	}
	public String getApprovedMan1() {
		return approvedMan1;
	}
	public void setApprovedMan1(String approvedMan1) {
		this.approvedMan1 = approvedMan1;
	}
	public String getApprovedMan1Login() {
		return approvedMan1Login;
	}
	public void setApprovedMan1Login(String approvedMan1Login) {
		this.approvedMan1Login = approvedMan1Login;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public String getAttachment2() {
		return attachment2;
	}
	public void setAttachment2(String attachment2) {
		this.attachment2 = attachment2;
	}
	public String getSubmitter2() {
		return submitter2;
	}
	public void setSubmitter2(String submitter2) {
		this.submitter2 = submitter2;
	}
	public String getSubmitter2Login() {
		return submitter2Login;
	}
	public void setSubmitter2Login(String submitter2Login) {
		this.submitter2Login = submitter2Login;
	}
	public String getApprovedMan2() {
		return approvedMan2;
	}
	public void setApprovedMan2(String approvedMan2) {
		this.approvedMan2 = approvedMan2;
	}
	public String getApprovedMan2Login() {
		return approvedMan2Login;
	}
	public void setApprovedMan2Login(String approvedMan2Login) {
		this.approvedMan2Login = approvedMan2Login;
	}
	public String getAuditMan2() {
		return auditMan2;
	}
	public void setAuditMan2(String auditMan2) {
		this.auditMan2 = auditMan2;
	}
	public String getAuditMan2Login() {
		return auditMan2Login;
	}
	public void setAuditMan2Login(String auditMan2Login) {
		this.auditMan2Login = auditMan2Login;
	}
	
	
	
}

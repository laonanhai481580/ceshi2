package com.ambition.qsm.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:内审计划与实施
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年10月26日 发布
 */
@Entity
@Table(name = "QSM_AUDIT_PLAN")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class AuditPlan extends WorkflowIdEntity{

	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "QSM_AUDIT_PLAN";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "内审计划与实施";//实体_列表_名称
	private String formNo;//表单编号
	private String companyName;//公司名
	private String auditType;//审核类型
	private String systemType;//体系类型
	private Date auditDate;//审核日期
	private String yearAuditNo;//年度计划表单编号
	//内审计划
	private String remark1;//意见
	private String attachment1;//附件
	private String submitter1;//提交人
	private String submitter1Login;//提交人登录名
	private String auditMan1;//审核人
	private String auditMan1Login;//审核人登录名
	private String approvedMan1;//报告核准人
	private String approvedMan1Login;//报告核准人登录名
	
	//检查报告
	private String remark2;//意见
	private String attachment2;//附件
	private String submitter2;//提交人
	private String submitter2Login;//提交人登录名
	private String auditMan2;//审核人
	private String auditMan2Login;//审核人登录名
	private String approvedMan2;//报告核准人
	private String approvedMan2Login;//报告核准人登录名
	
	//总结报告
	private String remark3;//意见
	private String attachment3;//附件
	private String department;//颁发部门
	private String approvedMan3;//报告核准人
	private String approvedMan3Login;//报告核准人登录名
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
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public String getSystemType() {
		return systemType;
	}
	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public String getYearAuditNo() {
		return yearAuditNo;
	}
	public void setYearAuditNo(String yearAuditNo) {
		this.yearAuditNo = yearAuditNo;
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
	public String getRemark3() {
		return remark3;
	}
	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}
	public String getAttachment3() {
		return attachment3;
	}
	public void setAttachment3(String attachment3) {
		this.attachment3 = attachment3;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getApprovedMan3() {
		return approvedMan3;
	}
	public void setApprovedMan3(String approvedMan3) {
		this.approvedMan3 = approvedMan3;
	}
	public String getApprovedMan3Login() {
		return approvedMan3Login;
	}
	public void setApprovedMan3Login(String approvedMan3Login) {
		this.approvedMan3Login = approvedMan3Login;
	}
	public String getSubmitter1Login() {
		return submitter1Login;
	}
	public void setSubmitter1Login(String submitter1Login) {
		this.submitter1Login = submitter1Login;
	}
	public String getSubmitter2Login() {
		return submitter2Login;
	}
	public void setSubmitter2Login(String submitter2Login) {
		this.submitter2Login = submitter2Login;
	}
	
	
	
	
}

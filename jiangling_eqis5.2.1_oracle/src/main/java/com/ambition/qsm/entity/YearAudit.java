package com.ambition.qsm.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:年度审核计划
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年10月25日 发布
 */
@Entity
@Table(name = "QSM_YEAR_AUDIT")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class YearAudit extends WorkflowIdEntity{

	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "QSM_YEAR_AUDIT";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "年度审核计划";//实体_列表_名称
	private String formNo;//表单编号
	private String companyName;//公司名
	private String auditType;//审核类型
	private String systemType;//体系类型
	private Date auditDate;//审核日期
	private String remark;//意见
	private String attachment;//附件
	private String setMan;//制定人
	private String auditMan1;//审核人
	private String auditMan1Login;//审核人登录名
	private String auditMan2;//核准人
	private String auditMan2Login;//核准人登录名
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getSetMan() {
		return setMan;
	}
	public void setSetMan(String setMan) {
		this.setMan = setMan;
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

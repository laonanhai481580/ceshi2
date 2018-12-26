package com.ambition.qsm.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:公司证书
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月26日 发布
 */
@Entity
@Table(name = "QSM_SYSTEM_CERTIFICATE")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class SystemCertificate extends IdEntity{

		/**
		  *SystemCertificate.java2016年9月26日
		 */
	private static final long serialVersionUID = 1L;
	private String companyName;//公司名称
	private String systemName;//体系名称
	private String certificateNo;//证书编号
	private Date approveDate;//认证日期
	private Date effectiveDate;//有效日期
	private String auditInstitutions;//审核机构
	private String auditMan;//审核老师
	private Date auditDate;//审核日期
	private Date auditEndDate;//审核结束日期
	private String attachment;//报告上传
	private String department;//主导部门
	private String garden;//园区
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
	public String getCertificateNo() {
		return certificateNo;
	}
	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}
	public Date getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getAuditInstitutions() {
		return auditInstitutions;
	}
	public void setAuditInstitutions(String auditInstitutions) {
		this.auditInstitutions = auditInstitutions;
	}
	public String getAuditMan() {
		return auditMan;
	}
	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Date getAuditEndDate() {
		return auditEndDate;
	}
	public void setAuditEndDate(Date auditEndDate) {
		this.auditEndDate = auditEndDate;
	}
	public String getGarden() {
		return garden;
	}
	public void setGarden(String garden) {
		this.garden = garden;
	}
	
	
}

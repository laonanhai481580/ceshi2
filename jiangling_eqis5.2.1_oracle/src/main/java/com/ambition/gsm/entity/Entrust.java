package com.ambition.gsm.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="GSM_ENTRUST")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Entrust extends WorkflowIdEntity{
	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY_LIST_CODE = "GSM_ENTRUST";//实体编码
	public static final String ENTITY_LIST_NAME = "外校委托";//实体名称
	private String formNo;//表单编号
	private String companyby;//公司主体
	private String businessDivision;//事业部
	private String applyReason;//申请原因
	private String useResponsible;//使用责任人
	private String useDepartment;//使用部门
	private Date confirmorDate;//确认日期
	private String auditOfHead;//部门主管审核
	private String deptHead;//部门主管
	private Date auditDate;//审核日期
	private String useDeptLead;//使用部门领导
	private String useDeptCalibr;//使用部门校准
	private Date useDeptCalibrDate;//校准日期
	private String deptHeadLogin;//部门主管登入名
	private String useDeptLeadLogin;//使用部门领导登入名
	
	@OneToMany(mappedBy = "entrust",cascade={CascadeType.ALL})
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	private List<EntrustSublist> entrustSublists;
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getApplyReason() {
		return applyReason;
	}
	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}
	public String getUseResponsible() {
		return useResponsible;
	}
	public void setUseResponsible(String useResponsible) {
		this.useResponsible = useResponsible;
	}
	public String getUseDepartment() {
		return useDepartment;
	}
	public void setUseDepartment(String useDepartment) {
		this.useDepartment = useDepartment;
	}
	public Date getConfirmorDate() {
		return confirmorDate;
	}
	public void setConfirmorDate(Date confirmorDate) {
		this.confirmorDate = confirmorDate;
	}
	public String getAuditOfHead() {
		return auditOfHead;
	}
	public void setAuditOfHead(String auditOfHead) {
		this.auditOfHead = auditOfHead;
	}
	public String getDeptHead() {
		return deptHead;
	}
	public void setDeptHead(String deptHead) {
		this.deptHead = deptHead;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public String getUseDeptLead() {
		return useDeptLead;
	}
	public void setUseDeptLead(String useDeptLead) {
		this.useDeptLead = useDeptLead;
	}
	public String getUseDeptCalibr() {
		return useDeptCalibr;
	}
	public void setUseDeptCalibr(String useDeptCalibr) {
		this.useDeptCalibr = useDeptCalibr;
	}
	public Date getUseDeptCalibrDate() {
		return useDeptCalibrDate;
	}
	public void setUseDeptCalibrDate(Date useDeptCalibrDate) {
		this.useDeptCalibrDate = useDeptCalibrDate;
	}
	public String getDeptHeadLogin() {
		return deptHeadLogin;
	}
	public void setDeptHeadLogin(String deptHeadLogin) {
		this.deptHeadLogin = deptHeadLogin;
	}
	public String getUseDeptLeadLogin() {
		return useDeptLeadLogin;
	}
	public void setUseDeptLeadLogin(String useDeptLeadLogin) {
		this.useDeptLeadLogin = useDeptLeadLogin;
	}
	public static String getEntityListCode() {
		return ENTITY_LIST_CODE;
	}
	public static String getEntityListName() {
		return ENTITY_LIST_NAME;
	}
	public List<EntrustSublist> getEntrustSublists() {
		return entrustSublists;
	}
	public void setEntrustSublists(List<EntrustSublist> entrustSublists) {
		this.entrustSublists = entrustSublists;
	}
	public String getCompanyby() {
		return companyby;
	}
	public void setCompanyby(String companyby) {
		this.companyby = companyby;
	}
	public String getBusinessDivision() {
		return businessDivision;
	}
	public void setBusinessDivision(String businessDivision) {
		this.businessDivision = businessDivision;
	}
	
}

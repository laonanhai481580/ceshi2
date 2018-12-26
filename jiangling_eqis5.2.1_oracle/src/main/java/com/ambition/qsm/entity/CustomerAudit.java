package com.ambition.qsm.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:客户审核履历
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月26日 发布
 */
@Entity
@Table(name = "QSM_CUSTOMER_AUDIT")//QSM_CUSTOMER_AUDIT
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class CustomerAudit extends IdEntity{

		/**
		  *CustomerAudit.java2016年9月26日
		 */
	private static final long serialVersionUID = 1L;
	private String enterpriseGroup;//事业群
	private String customer;//客户
	private String auditType;//审核类型
	private String systemType;//体系类型
	private Date auditDate;//审核日期
	private Date auditEndDate;//审核结束日期
	private String visitor;//来访人员
	private String marketPlayer;//市场担当
	private String department;//主导部门
	private String auditResult;//审核结果
	private String checkType;//查检表类型
	private String customerCheck;//客户check list
	private String closeState;//结案状况
	private String issuesList;//问题点list
	private String remark;//备注
	public String getEnterpriseGroup() {
		return enterpriseGroup;
	}
	public void setEnterpriseGroup(String enterpriseGroup) {
		this.enterpriseGroup = enterpriseGroup;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
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
	public String getVisitor() {
		return visitor;
	}
	public void setVisitor(String visitor) {
		this.visitor = visitor;
	}
	public String getMarketPlayer() {
		return marketPlayer;
	}
	public void setMarketPlayer(String marketPlayer) {
		this.marketPlayer = marketPlayer;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getAuditResult() {
		return auditResult;
	}
	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getCustomerCheck() {
		return customerCheck;
	}
	public void setCustomerCheck(String customerCheck) {
		this.customerCheck = customerCheck;
	}
	public String getCloseState() {
		return closeState;
	}
	public void setCloseState(String closeState) {
		this.closeState = closeState;
	}
	public String getIssuesList() {
		return issuesList;
	}
	public void setIssuesList(String issuesList) {
		this.issuesList = issuesList;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getAuditEndDate() {
		return auditEndDate;
	}
	public void setAuditEndDate(Date auditEndDate) {
		this.auditEndDate = auditEndDate;
	}
	
}

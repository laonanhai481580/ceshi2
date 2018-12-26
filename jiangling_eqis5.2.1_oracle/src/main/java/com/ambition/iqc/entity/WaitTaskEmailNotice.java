package com.ambition.iqc.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

@Entity
@Table(name="QIS_WAIT_TASK_EMAIL_NOTICE")
public class WaitTaskEmailNotice extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String iqcReportNo;
	private String bomCode;
	private String bomName;
	private String bomCategory;//物料类别
	private String supplierCode;
	private String supplierName;
	private Date enterDate;
	private Date inspectionDate;
	private String isTaskEmail;//是否发送检验发邮件
	private String emailTaskUserId;//
	private String emailTaskName;//检验通知邮件 收件人
	private String emailTaskTcpType;
	private Date emailTaskTime;
	private String isDureEmail;//是否发送了超期邮件
	private String emailDureUserId;//
	private String emailDureName;//
	private String acceptNo;//接受行号
	private String emailDureTcpType;//
	private String dureEmailName;//超期邮件收件人
	private String receivedureTcpType;//内网、外网
	private Date emailDureTime;
	
	
	public String getAcceptNo() {
		return acceptNo;
	}
	public void setAcceptNo(String acceptNo) {
		this.acceptNo = acceptNo;
	}
	public String getBomCode() {
		return bomCode;
	}
	public void setBomCode(String bomCode) {
		this.bomCode = bomCode;
	}
	public String getBomName() {
		return bomName;
	}
	public void setBomName(String bomName) {
		this.bomName = bomName;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public Date getEnterDate() {
		return enterDate;
	}
	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}
	public Date getInspectionDate() {
		return inspectionDate;
	}
	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
	public String getIsTaskEmail() {
		return isTaskEmail;
	}
	public void setIsTaskEmail(String isTaskEmail) {
		this.isTaskEmail = isTaskEmail;
	}
	public String getEmailTaskUserId() {
		return emailTaskUserId;
	}
	public void setEmailTaskUserId(String emailTaskUserId) {
		this.emailTaskUserId = emailTaskUserId;
	}
	public String getEmailTaskName() {
		return emailTaskName;
	}
	public void setEmailTaskName(String emailTaskName) {
		this.emailTaskName = emailTaskName;
	}
	public String getEmailTaskTcpType() {
		return emailTaskTcpType;
	}
	public void setEmailTaskTcpType(String emailTaskTcpType) {
		this.emailTaskTcpType = emailTaskTcpType;
	}
	public String getIsDureEmail() {
		return isDureEmail;
	}
	public void setIsDureEmail(String isDureEmail) {
		this.isDureEmail = isDureEmail;
	}
	public String getEmailDureUserId() {
		return emailDureUserId;
	}
	public void setEmailDureUserId(String emailDureUserId) {
		this.emailDureUserId = emailDureUserId;
	}
	public String getDureEmailName() {
		return dureEmailName;
	}
	public void setDureEmailName(String dureEmailName) {
		this.dureEmailName = dureEmailName;
	}
	public String getReceivedureTcpType() {
		return receivedureTcpType;
	}
	public void setReceivedureTcpType(String receivedureTcpType) {
		this.receivedureTcpType = receivedureTcpType;
	}
	public String getBomCategory() {
		return bomCategory;
	}
	public void setBomCategory(String bomCategory) {
		this.bomCategory = bomCategory;
	}
	public String getIqcReportNo() {
		return iqcReportNo;
	}
	public void setIqcReportNo(String iqcReportNo) {
		this.iqcReportNo = iqcReportNo;
	}
	public String getEmailDureName() {
		return emailDureName;
	}
	public void setEmailDureName(String emailDureName) {
		this.emailDureName = emailDureName;
	}
	public String getEmailDureTcpType() {
		return emailDureTcpType;
	}
	public void setEmailDureTcpType(String emailDureTcpType) {
		this.emailDureTcpType = emailDureTcpType;
	}
	public Date getEmailTaskTime() {
		return emailTaskTime;
	}
	public void setEmailTaskTime(Date emailTaskTime) {
		this.emailTaskTime = emailTaskTime;
	}
	public Date getEmailDureTime() {
		return emailDureTime;
	}
	public void setEmailDureTime(Date emailDureTime) {
		this.emailDureTime = emailDureTime;
	}


}

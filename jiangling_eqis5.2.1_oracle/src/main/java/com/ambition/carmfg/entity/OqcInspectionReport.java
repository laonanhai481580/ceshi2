package com.ambition.carmfg.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:体系维护
 * <p>
 * amb
 * </p>
 * <p>
 * 厦门安必兴信息科技有限公司
 * </p>
 * <p>
 * 功能说明：
 * </p>
 * 
 * @author LPF
 * @version 1.00 2016年9月26日 发布
 */
@Entity
@Table(name ="MFG_OQC_INSPECTION_REPORT")//MFG_OQC_INSPECTION_REPORT
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler","fieldHandler" })
public class OqcInspectionReport extends IdEntity {
	/**
	 * SystemCertificate.java2016年9月26日
	 */
	private static final long serialVersionUID = 1L;
	private String enterpriseGroup;// 事业群
	private String batchNumber;// 检验批号
	private String attachment;// 附件
	private String uploadMan;// 上传人
    private Date   uploadDate;// 上传时间
	private String classGroup;//班别
	private Date   reportDate;// 时间
	private String remark;// 备注
	private String personResponsible;//责任人
	private String processSection; // 制程区段
	private String auditMan;//审核人
	private String ofilmModel;// 欧菲机种
	private String customer;// 客户名称
	private String factorySupply;//供应厂区
	private String updateStatus;// 更新状态
	private String isHarmful = STATE_SUBMIT;// 状态
	public static final String STATE_QUALIFIED = "合格";
	public static final String STATE_SUBMIT = "待提交";
	public static final String STATE_PENDING = "待审核";
	public static final String STATE_OVERDUE = "过期";

	public String getEnterpriseGroup() {
		return enterpriseGroup;
	}

	public void setEnterpriseGroup(String enterpriseGroup) {
		this.enterpriseGroup = enterpriseGroup;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getUploadMan() {
		return uploadMan;
	}

	public String getAuditMan() {
		return auditMan;
	}

	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}

	public void setUploadMan(String uploadMan) {
		this.uploadMan = uploadMan;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getRemark() {
		return remark;
	}

	

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getProcessSection() {
		return processSection;
	}

	public void setProcessSection(String processSection) {
		this.processSection = processSection;
	}

	public String getOfilmModel() {
		return ofilmModel;
	}

	public void setOfilmModel(String ofilmModel) {
		this.ofilmModel = ofilmModel;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getIsHarmful() {
		return isHarmful;
	}

	public void setIsHarmful(String isHarmful) {
		this.isHarmful = isHarmful;
	}

	public String getUpdateStatus() {
		return updateStatus;
	}

	public void setUpdateStatus(String updateStatus) {
		this.updateStatus = updateStatus;
	}

	public String getFactorySupply() {
		return factorySupply;
	}

	public void setFactorySupply(String factorySupply) {
		this.factorySupply = factorySupply;
	}

	public String getClassGroup() {
		return classGroup;
	}

	public void setClassGroup(String classGroup) {
		this.classGroup = classGroup;
	}

	public String getPersonResponsible() {
		return personResponsible;
	}

	public void setPersonResponsible(String personResponsible) {
		this.personResponsible = personResponsible;
	}

}

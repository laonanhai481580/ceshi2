package com.ambition.epm.entity;

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
@Table(name ="EPM_REL")//MFG_OQC_INSPECTION_REPORT
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler","fieldHandler" })
public class Rel extends IdEntity {
	/**
	 * SystemCertificate.java2016年9月26日
	 */
	private static final long serialVersionUID = 1L;
	private String auditMan;//审核人
	private String processSection; // 制程区段
	private String customer;// 客户名称
	private Date   reportDate;// 时间
	private String sampleItemCode;//样品项目代码
	private String sampleCategory;//样品类别
	private String prototypePhase;//样品阶段
	private String volumeNumOrConfiguration;//卷号或配置
	private String testPurpose;//测试目的
	private String attachment;// 附件
	private String remark;// 备注
	private String updateStatus;// 更新状态
	private String isHarmful = STATE_SUBMIT;// 状态
	public static final String STATE_QUALIFIED = "合格";
	public static final String STATE_SUBMIT = "待提交";
	public static final String STATE_PENDING = "待审核";
	public static final String STATE_OVERDUE = "过期";
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getAuditMan() {
		return auditMan;
	}

	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
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

	public String getProcessSection() {
		return processSection;
	}

	public void setProcessSection(String processSection) {
		this.processSection = processSection;
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
	public String getSampleItemCode() {
		return sampleItemCode;
	}
	public void setSampleItemCode(String sampleItemCode) {
		this.sampleItemCode = sampleItemCode;
	}
	public String getSampleCategory() {
		return sampleCategory;
	}

	public void setSampleCategory(String sampleCategory) {
		this.sampleCategory = sampleCategory;
	}

	public String getPrototypePhase() {
		return prototypePhase;
	}

	public void setPrototypePhase(String prototypePhase) {
		this.prototypePhase = prototypePhase;
	}

	public String getVolumeNumOrConfiguration() {
		return volumeNumOrConfiguration;
	}

	public void setVolumeNumOrConfiguration(String volumeNumOrConfiguration) {
		this.volumeNumOrConfiguration = volumeNumOrConfiguration;
	}

	public String getTestPurpose() {
		return testPurpose;
	}

	public void setTestPurpose(String testPurpose) {
		this.testPurpose = testPurpose;
	}

}

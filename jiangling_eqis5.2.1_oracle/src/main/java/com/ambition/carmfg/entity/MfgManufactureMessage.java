package com.ambition.carmfg.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "MFG_MANUFACTURE_MESSAGE")
public class MfgManufactureMessage extends IdEntity {

	private static final long serialVersionUID = 1L;
	
	private String qcCode;//二维码编码
	private String marchineNo;//机台
	private String operator;//作业员
	@Temporal(TemporalType.TIMESTAMP)
	private Date manufactureTime;//作业时间
	private String workOrderNo;//工单
	private String section;//工站

	@ManyToOne
	@JoinColumn(name = "FK_MFG_REPORT_ID")
	@JsonIgnore()
	private MfgCheckInspectionReport mfgCheckInspectionReport;

	public String getQcCode() {
		return qcCode;
	}

	public void setQcCode(String qcCode) {
		this.qcCode = qcCode;
	}

	public String getMarchineNo() {
		return marchineNo;
	}

	public void setMarchineNo(String marchineNo) {
		this.marchineNo = marchineNo;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getManufactureTime() {
		return manufactureTime;
	}

	public void setManufactureTime(Date manufactureTime) {
		this.manufactureTime = manufactureTime;
	}

	public String getWorkOrderNo() {
		return workOrderNo;
	}

	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public MfgCheckInspectionReport getMfgCheckInspectionReport() {
		return mfgCheckInspectionReport;
	}

	public void setMfgCheckInspectionReport(
			MfgCheckInspectionReport mfgCheckInspectionReport) {
		this.mfgCheckInspectionReport = mfgCheckInspectionReport;
	}
	

}

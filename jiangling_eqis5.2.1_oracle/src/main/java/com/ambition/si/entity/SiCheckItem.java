package com.ambition.si.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ambition.product.base.IdEntity;

/**
 * 供应商检验检查项目记录明细
 * @author lpf
 *
 */
@Entity
@Table(name="SI_CHECK_ITEM")
public class SiCheckItem extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String defectionTypeNo;//不良类型编码
	private String defectionTypeName;//不良类型名称
	private String defectionCodeNo;//不良项目编码
	private String defectionCodeName;//不良项目名称
	@Temporal(TemporalType.TIMESTAMP)
	private Date inspectionDate; // 检验日期
	private Integer value;//不良数量
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	private String standard;//标准
	private String actualValue;//实际测量值
	private String remark;//备注
	private String snCode;//s/n号
	@ManyToOne
	@JoinColumn(name="FK_SI_REPORT_ID")
	private SiCheckInspectionReport siCheckInspectionReport;//检验报告
	public String getDefectionTypeNo() {
		return defectionTypeNo;
	}
	public void setDefectionTypeNo(String defectionTypeNo) {
		this.defectionTypeNo = defectionTypeNo;
	}
	public String getDefectionTypeName() {
		return defectionTypeName;
	}
	public void setDefectionTypeName(String defectionTypeName) {
		this.defectionTypeName = defectionTypeName;
	}
	public String getDefectionCodeNo() {
		return defectionCodeNo;
	}
	public void setDefectionCodeNo(String defectionCodeNo) {
		this.defectionCodeNo = defectionCodeNo;
	}
	public String getDefectionCodeName() {
		return defectionCodeName;
	}
	public void setDefectionCodeName(String defectionCodeName) {
		this.defectionCodeName = defectionCodeName;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getActualValue() {
		return actualValue;
	}
	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public SiCheckInspectionReport getSiCheckInspectionReport() {
		return siCheckInspectionReport;
	}
	public void setSiCheckInspectionReport(
			SiCheckInspectionReport siCheckInspectionReport) {
		this.siCheckInspectionReport = siCheckInspectionReport;
	}
	public Date getInspectionDate() {
		return inspectionDate;
	}
	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
	public String getSnCode() {
		return snCode;
	}
	public void setSnCode(String snCode) {
		this.snCode = snCode;
	}



}
package com.ambition.carmfg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 类名:产品生产信息
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  吴旭明
 * @version 1.00 2016-9-8 发布
 */
@Entity
@Table(name = "MFG_SUPPLIER_MESSAGE")
public class MfgSupplierMessage  extends IdEntity {

	private static final long serialVersionUID = 1L;
	private String supplierCode;//供应商编码
	private String supplierName;//供应商名称
	private String supplierBatchNo;//供应商批次号
	private String supplierBomCode;//物料编码
	private String supplierBomName;//物料名称
	private String supplierBomBatchNo;//物料批次号
	private String supplierBatchNum;//供应商物料库存数
	@Column(length=3000)
	private String supplierRemark;//备注
	
	@ManyToOne
	@JoinColumn(name = "FK_MFG_REPORT_ID")
	@JsonIgnore()
	private MfgCheckInspectionReport mfgCheckInspectionReport;

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

	public String getSupplierBatchNo() {
		return supplierBatchNo;
	}

	public void setSupplierBatchNo(String supplierBatchNo) {
		this.supplierBatchNo = supplierBatchNo;
	}

	public String getSupplierBomCode() {
		return supplierBomCode;
	}

	public void setSupplierBomCode(String supplierBomCode) {
		this.supplierBomCode = supplierBomCode;
	}

	public String getSupplierBomName() {
		return supplierBomName;
	}

	public void setSupplierBomName(String supplierBomName) {
		this.supplierBomName = supplierBomName;
	}

	public String getSupplierBatchNum() {
		return supplierBatchNum;
	}

	public void setSupplierBatchNum(String supplierBatchNum) {
		this.supplierBatchNum = supplierBatchNum;
	}

	public String getSupplierRemark() {
		return supplierRemark;
	}

	public void setSupplierRemark(String supplierRemark) {
		this.supplierRemark = supplierRemark;
	}

	public MfgCheckInspectionReport getMfgCheckInspectionReport() {
		return mfgCheckInspectionReport;
	}

	public void setMfgCheckInspectionReport(
			MfgCheckInspectionReport mfgCheckInspectionReport) {
		this.mfgCheckInspectionReport = mfgCheckInspectionReport;
	}

	public String getSupplierBomBatchNo() {
		return supplierBomBatchNo;
	}

	public void setSupplierBomBatchNo(String supplierBomBatchNo) {
		this.supplierBomBatchNo = supplierBomBatchNo;
	}
	
}

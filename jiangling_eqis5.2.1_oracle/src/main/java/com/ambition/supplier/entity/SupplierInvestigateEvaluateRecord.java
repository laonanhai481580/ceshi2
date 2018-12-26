package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 类名:供应商调查表评价记录
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2013-4-28 发布
 */
@Entity
@Table(name = "SUPPLIER_INVESTIGATE_EVALUATE_RECORD")
public class SupplierInvestigateEvaluateRecord extends IdEntity {
	private static final long serialVersionUID = 4518308827240189057L;
	@ManyToOne
	@JoinColumn(name = "FK_SUPPLIER_INVESTIGATE_ID")
	private SupplierInvestigate supplierInvestigate;
	private String itemName;//评价项目
	private String selfAssessment;//供应商自评
	private String auditContent;//评审内容
	public SupplierInvestigate getSupplierInvestigate() {
		return supplierInvestigate;
	}
	public void setSupplierInvestigate(SupplierInvestigate supplierInvestigate) {
		this.supplierInvestigate = supplierInvestigate;
	}
	
	public String getSelfAssessment() {
		return selfAssessment;
	}
	public void setSelfAssessment(String selfAssessment) {
		this.selfAssessment = selfAssessment;
	}
	public String getAuditContent() {
		return auditContent;
	}
	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}

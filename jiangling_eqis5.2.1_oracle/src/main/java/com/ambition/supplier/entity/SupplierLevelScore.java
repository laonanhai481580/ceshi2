package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 类名:供应商等级与得分关系
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年9月27日 发布
 */
@Entity
@Table(name = "SUPPLIER_LEVEL_SCORE")
public class SupplierLevelScore extends IdEntity{

		/**
		  *SupplierLevelScore.java2016年9月27日
		 */
	private static final long serialVersionUID = 1L;

	private String auditLevel;//评定等级
	private String scoreArea;//得分范围
	private Double scoreStart;//得分最小
	private Double scoreEnd;//得分最大
	public String getAuditLevel() {
		return auditLevel;
	}
	public void setAuditLevel(String auditLevel) {
		this.auditLevel = auditLevel;
	}
	public String getScoreArea() {
		return scoreArea;
	}
	public void setScoreArea(String scoreArea) {
		this.scoreArea = scoreArea;
	}
	public Double getScoreStart() {
		return scoreStart;
	}
	public void setScoreStart(Double scoreStart) {
		this.scoreStart = scoreStart;
	}
	public Double getScoreEnd() {
		return scoreEnd;
	}
	public void setScoreEnd(Double scoreEnd) {
		this.scoreEnd = scoreEnd;
	}
	
	
	
}

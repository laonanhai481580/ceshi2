package com.ambition.iqc.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 类名:检验周期.java
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  wuxuming
 * @version 1.00 2016-10-31 发布
 */
@Entity
@Table(name="IQC_INSPECTION_INTERVAL")
public class InspectionInterval extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String bomCode;//物料号
	private String inspectionCategory;//物料类别
	private String ctyle;//周期
	public String getBomCode() {
		return bomCode;
	}
	public void setBomCode(String bomCode) {
		this.bomCode = bomCode;
	}
	public String getInspectionCategory() {
		return inspectionCategory;
	}
	public void setInspectionCategory(String inspectionCategory) {
		this.inspectionCategory = inspectionCategory;
	}
	public String getCtyle() {
		return ctyle;
	}
	public void setCtyle(String ctyle) {
		this.ctyle = ctyle;
	}
	
}

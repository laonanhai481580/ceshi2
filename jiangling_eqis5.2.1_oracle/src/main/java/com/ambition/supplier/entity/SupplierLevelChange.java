package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 类名:供应商等级变更关系维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年9月27日 发布
 */
@Entity
@Table(name = "SUPPLIER_LEVEL_CHANGE")
public class SupplierLevelChange extends IdEntity{

		/**
		  *SupplierLevelChange.java2016年9月27日
		 */
	private static final long serialVersionUID = 1L;

	private Integer quarters;//值
	private String auditLevel;//等级
	private String isStartUp="0";//是否启动   0:启动
	public Integer getQuarters() {
		return quarters;
	}
	public void setQuarters(Integer quarters) {
		this.quarters = quarters;
	}
	public String getAuditLevel() {
		return auditLevel;
	}
	public void setAuditLevel(String auditLevel) {
		this.auditLevel = auditLevel;
	}
	public String getIsStartUp() {
		return isStartUp;
	}
	public void setIsStartUp(String isStartUp) {
		this.isStartUp = isStartUp;
	}
	
	
}

package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.Table;


import com.ambition.product.base.IdEntity;

/**
 * 类名:SupplierMappingBusiness.java
 * 中文类名:供应商与进货事业部映射
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * <p>
 * @author 作者 :wuxuming
 * @version 1.0 创建时间：2016-7-5 下午5:12:22
 * </p>
 */
@Entity
@Table(name = "SUPPLIER_MAPPING_BUSINESSUNIT")
public class SupplierMappingBusinessUnit extends IdEntity  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String parentBusinessCode;//事业部编码
	private String parentBusinessName;//事业部名称
	
	private String childBusinessCode;//父级包含的事业部编码
	private String childBusinessName;//父级包含的事业部编码
	public String getParentBusinessCode() {
		return parentBusinessCode;
	}
	public void setParentBusinessCode(String parentBusinessCode) {
		this.parentBusinessCode = parentBusinessCode;
	}
	public String getParentBusinessName() {
		return parentBusinessName;
	}
	public void setParentBusinessName(String parentBusinessName) {
		this.parentBusinessName = parentBusinessName;
	}
	public String getChildBusinessCode() {
		return childBusinessCode;
	}
	public void setChildBusinessCode(String childBusinessCode) {
		this.childBusinessCode = childBusinessCode;
	}
	public String getChildBusinessName() {
		return childBusinessName;
	}
	public void setChildBusinessName(String childBusinessName) {
		this.childBusinessName = childBusinessName;
	}
	
	
	
	
}

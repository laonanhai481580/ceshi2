package com.ambition.supplier.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 类名:物料类别目标维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年9月27日 发布
 */

@Entity
@Table(name = "SUPPLIER_MATERIAL_TYPE_GOAL")
public class SupplierMaterialTypeGoal extends IdEntity{

		/**
		  *SupplierMaterialTypeGoal.java2016年9月27日
		 */
	private static final long serialVersionUID = 1L;
	private String materialType;//物料类别
	private Double comingUpperLimit;//进料批合格率上限目标值
	private Double comingLowerLimit;//进料批合格率下限目标值
	private Double lineUpperLimit;//上线不良率上限目标值
	private Double lineLowerLimit;//上线不良率下限目标值
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public Double getComingUpperLimit() {
		return comingUpperLimit;
	}
	public void setComingUpperLimit(Double comingUpperLimit) {
		this.comingUpperLimit = comingUpperLimit;
	}
	public Double getComingLowerLimit() {
		return comingLowerLimit;
	}
	public void setComingLowerLimit(Double comingLowerLimit) {
		this.comingLowerLimit = comingLowerLimit;
	}
	public Double getLineUpperLimit() {
		return lineUpperLimit;
	}
	public void setLineUpperLimit(Double lineUpperLimit) {
		this.lineUpperLimit = lineUpperLimit;
	}
	public Double getLineLowerLimit() {
		return lineLowerLimit;
	}
	public void setLineLowerLimit(Double lineLowerLimit) {
		this.lineLowerLimit = lineLowerLimit;
	}
	
}

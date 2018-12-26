package com.ambition.gsm.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.norteksoft.product.orm.IdEntity;

/**
 * 类名: InspectionWarnUsers 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：检定计划预警人员表</p>
 * @author  刘承斌
 * @version 1.00  2014-10-20 下午11:27:38  发布
 */
@Entity
@Table(name="GSM_WARN_USER")
public class InspectionWarnUser extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String name;//用户名
	private String userId;//用户ID
	
	@ManyToOne
	@JoinColumn(name="FK_GSM_INSPECTION_PLAN_ID")
	private InspectionPlan inspectionPlan;//检定计划
	@ManyToOne
	@JoinColumn(name="FK_GSM_EQUIPMENT_ID")
	private GsmEquipment gsmEquipment;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public InspectionPlan getInspectionPlan() {
		return inspectionPlan;
	}
	public void setInspectionPlan(InspectionPlan inspectionPlan) {
		this.inspectionPlan = inspectionPlan;
	}
	public GsmEquipment getGsmEquipment() {
		return gsmEquipment;
	}
	public void setGsmEquipment(GsmEquipment gsmEquipment) {
		this.gsmEquipment = gsmEquipment;
	}
}

package com.ambition.gsm.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:仪器标准件维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2017年3月30日 发布
 */
@Entity
@Table(name = "GSM_EQUIPMENT_STANDARD")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class EquipmentStandard extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String managerAssets;//管理编号
	private String equipmentName;//设备名称
	@OneToMany(mappedBy="equipmentStandard")
	@OrderBy("standardNo")
	List<EquipmentStandardItem> equipmentStandardItem;//标准件
	public String getManagerAssets() {
		return managerAssets;
	}
	public void setManagerAssets(String managerAssets) {
		this.managerAssets = managerAssets;
	}
	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	public List<EquipmentStandardItem> getEquipmentStandardItem() {
		return equipmentStandardItem;
	}
	public void setEquipmentStandardItem(
			List<EquipmentStandardItem> equipmentStandardItem) {
		this.equipmentStandardItem = equipmentStandardItem;
	}



}

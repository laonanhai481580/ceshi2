package com.ambition.gsm.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name="GSM_BORROW_RECORD_SUBLIST")
public class BorrowRecordSublist extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String managerAssets;// 仪器管理编号/统一编号
	private String equipmentName;// 仪器名称
	private String equipmentModel;//规格型号
	private String brand;//品牌
	private String remark;//备注

	@ManyToOne
	@JoinColumn(name="GSM_SUBLIST_ID")
    @JsonIgnore()
	private BorrowRecord borrowRecord;
	
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public BorrowRecord getBorrowRecord() {
		return borrowRecord;
	}
	public void setBorrowRecord(BorrowRecord borrowRecord) {
		this.borrowRecord = borrowRecord;
	}
	public String getEquipmentModel() {
		return equipmentModel;
	}
	public void setEquipmentModel(String equipmentModel) {
		this.equipmentModel = equipmentModel;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
}

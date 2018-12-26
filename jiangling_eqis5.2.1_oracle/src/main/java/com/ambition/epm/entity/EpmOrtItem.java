package com.ambition.epm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * ORT测试项目
 * @author lpf
 *
 */
@Entity
@Table(name = "EPM_ORT_ITEM")
public class EpmOrtItem extends IdEntity {
	private static final long serialVersionUID = 1L;
	@Column(length=2500)
	private String itemName;//测试项目名称
	@Column(length=1000)
	private String  condition;//测试条件
	@Column(length=500)
	private Integer count;//测试数量
	private String standardGreen;//判定标准Green
	private String standardYellow;//判定标准Yellow
	private String standardRed;//判定标准Red
	
	private String  quickResponse;//二维码
	private String  testStation  ; //测试工站
	private String  equipmentNumber;//测试设备
	private String  testItem;//测试项目
	private Integer lower;//规格下限
	private Integer upper;//规格上限
	private Integer testValue;//测试值
	private String  upload;//是否上传

    private String remark;//备注
       
	@ManyToOne
	@JoinColumn(name = "FK_ORT_INDICATOR_NO")
	@JsonIgnore
	private EpmOrtIndicator ortIndicator;//ORT测试标准

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getStandardGreen() {
		return standardGreen;
	}

	public void setStandardGreen(String standardGreen) {
		this.standardGreen = standardGreen;
	}

	public String getStandardYellow() {
		return standardYellow;
	}

	public void setStandardYellow(String standardYellow) {
		this.standardYellow = standardYellow;
	}

	public String getStandardRed() {
		return standardRed;
	}

	public void setStandardRed(String standardRed) {
		this.standardRed = standardRed;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public EpmOrtIndicator getOrtIndicator() {
		return ortIndicator;
	}

	public void setOrtIndicator(EpmOrtIndicator ortIndicator) {
		this.ortIndicator = ortIndicator;
	}

	public String getTestStation() {
		return testStation;
	}

	public void setTestStation(String testStation) {
		this.testStation = testStation;
	}

	public String getEquipmentNumber() {
		return equipmentNumber;
	}

	public void setEquipmentNumber(String equipmentNumber) {
		this.equipmentNumber = equipmentNumber;
	}

	public String getTestItem() {
		return testItem;
	}

	public void setTestItem(String testItem) {
		this.testItem = testItem;
	}

	public Integer getLower() {
		return lower;
	}

	public void setLower(Integer lower) {
		this.lower = lower;
	}

	public Integer getUpper() {
		return upper;
	}

	public void setUpper(Integer upper) {
		this.upper = upper;
	}

	public Integer getTestValue() {
		return testValue;
	}

	public void setTestValue(Integer testValue) {
		this.testValue = testValue;
	}

	public String getQuickResponse() {
		return quickResponse;
	}

	public void setQuickResponse(String quickResponse) {
		this.quickResponse = quickResponse;
	}

	public String getUpload() {
		return upload;
	}

	public void setUpload(String upload) {
		this.upload = upload;
	}





}

package com.ambition.aftersales.entity;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "AFS_LAR_DEFECTIVE_ITEMS")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "fieldHandler" })
public class LarDefectiveItem extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String defectionClass;// (功能，尺寸，外观)
	private String defectionItemNo;// 不良项目编码
	private String defectionItemName;// 不良项目
	private Integer defectionItemValue;// 不良项目值

	/*
	 * @ManyToOne
	 * 
	 * @JoinColumn(name = "FK_VLRR_DATA_ID")
	 * 
	 * @JsonIgnore()
	 */

	@ManyToOne
	@JoinColumn(name = "AFS_LAR_DATA_ID")
	@JsonIgnore()
	private LarData larData;

	public String getDefectionClass() {
		return this.defectionClass;
	}

	public void setDefectionClass(String defectionClass) {
		this.defectionClass = defectionClass;
	}

	public String getDefectionItemNo() {
		return this.defectionItemNo;
	}

	public void setDefectionItemNo(String defectionItemNo) {
		this.defectionItemNo = defectionItemNo;
	}

	public String getDefectionItemName() {
		return this.defectionItemName;
	}

	public void setDefectionItemName(String defectionItemName) {
		this.defectionItemName = defectionItemName;
	}

	public Integer getDefectionItemValue() {
		return this.defectionItemValue;
	}

	public void setDefectionItemValue(Integer defectionItemValue) {
		this.defectionItemValue = defectionItemValue;
	}

	public LarData getLarData() {
		return larData;
	}

	public void setLarData(LarData larData) {
		this.larData = larData;
	}
}
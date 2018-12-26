package com.ambition.carmfg.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "MFG_OQC_DEFECTIVE_ITEMS")
public class OqcDefectiveItem extends IdEntity {

	private static final long serialVersionUID = 1L;

	private String defectionCodeNo;//不良编码
	private String defectionCodeName;//不良编码名称
	private Integer defectionCodeValue;//不良编码值
	private String defectionTypeNo;//不良类型编码
	private String defectionTypeName;//不良类型名称
	@ManyToOne
	@JoinColumn(name = "MFG_OQC_INSPECTION_ID")
	@JsonIgnore()
	private OqcInspection oqcInspection;
	public String getDefectionCodeNo() {
		return defectionCodeNo;
	}
	public void setDefectionCodeNo(String defectionCodeNo) {
		this.defectionCodeNo = defectionCodeNo;
	}
	public String getDefectionCodeName() {
		return defectionCodeName;
	}
	public void setDefectionCodeName(String defectionCodeName) {
		this.defectionCodeName = defectionCodeName;
	}
	public String getDefectionTypeNo() {
		return defectionTypeNo;
	}
	public void setDefectionTypeNo(String defectionTypeNo) {
		this.defectionTypeNo = defectionTypeNo;
	}
	public String getDefectionTypeName() {
		return defectionTypeName;
	}
	public void setDefectionTypeName(String defectionTypeName) {
		this.defectionTypeName = defectionTypeName;
	}
	public OqcInspection getOqcInspection() {
		return oqcInspection;
	}
	public void setOqcInspection(OqcInspection oqcInspection) {
		this.oqcInspection = oqcInspection;
	}
	public Integer getDefectionCodeValue() {
		return defectionCodeValue;
	}
	public void setDefectionCodeValue(Integer defectionCodeValue) {
		this.defectionCodeValue = defectionCodeValue;
	}

	
	
	
	
}

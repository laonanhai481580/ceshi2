package com.ambition.carmfg.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.norteksoft.wf.engine.client.ExtendField;

/**
 * 不良代码属性
 */
@Entity
@Table(name = "MFG_DEFECTION_CODE_ATTRIBUTE")
public class DefectionCodeAttribute extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String defectionCodeAttributeNo;//不良属性编号
	private Boolean isRegulate;//是否可调
	private String defectionCodeAttributeLevel;//不良级别
	private Double defectionCodeAttributeScore;//不良分数
	private Double defectionCodeAttributeCost;//不良成本
	private String liabilityDepartment;//责任部门
	private String liabilityGroup;//责任班组
	private Boolean isContinute;//责任是否沿用
	private String remark;//备注
	private String part;//不良BOM
	private String positionCodeNo;//部位代码
	private String positionCodeName;//部位代码名称
	private String directionCodeNo;//方位代码
	private String directionCodeName;//方位代码名称
	private String defectionCodeNo;//不良代码
	private String defectionCodeName;//不良代码名称
	private String defectionTypeNo;//不良类别编码
	private String defectionTypeName;//不良类名称
	@Embedded
	private ExtendField extendField;
	//Get、Set方法
	public String getDefectionCodeAttributeNo() {
		return defectionCodeAttributeNo;
	}
	public String getPositionCodeName() {
		return positionCodeName;
	}
	public void setPositionCodeName(String positionCodeName) {
		this.positionCodeName = positionCodeName;
	}
	public String getDirectionCodeName() {
		return directionCodeName;
	}
	public void setDirectionCodeName(String directionCodeName) {
		this.directionCodeName = directionCodeName;
	}
	public String getDefectionCodeName() {
		return defectionCodeName;
	}
	public void setDefectionCodeName(String defectionCodeName) {
		this.defectionCodeName = defectionCodeName;
	}
	public void setDefectionCodeAttributeNo(String defectionCodeAttributeNo) {
		this.defectionCodeAttributeNo = defectionCodeAttributeNo;
	}
	public Boolean getIsRegulate() {
		return isRegulate;
	}
	public void setIsRegulate(Boolean isRegulate) {
		this.isRegulate = isRegulate;
	}
	public String getDefectionCodeAttributeLevel() {
		return defectionCodeAttributeLevel;
	}
	public void setDefectionCodeAttributeLevel(String defectionCodeAttributeLevel) {
		this.defectionCodeAttributeLevel = defectionCodeAttributeLevel;
	}
	
	public Double getDefectionCodeAttributeScore() {
		return defectionCodeAttributeScore;
	}
	public void setDefectionCodeAttributeScore(Double defectionCodeAttributeScore) {
		this.defectionCodeAttributeScore = defectionCodeAttributeScore;
	}
	public Double getDefectionCodeAttributeCost() {
		return defectionCodeAttributeCost;
	}
	public void setDefectionCodeAttributeCost(Double defectionCodeAttributeCost) {
		this.defectionCodeAttributeCost = defectionCodeAttributeCost;
	}
	public String getLiabilityDepartment() {
		return liabilityDepartment;
	}
	public void setLiabilityDepartment(String liabilityDepartment) {
		this.liabilityDepartment = liabilityDepartment;
	}
	public String getLiabilityGroup() {
		return liabilityGroup;
	}
	public void setLiabilityGroup(String liabilityGroup) {
		this.liabilityGroup = liabilityGroup;
	}
	public Boolean getIsContinute() {
		return isContinute;
	}
	public void setIsContinute(Boolean isContinute) {
		this.isContinute = isContinute;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}	
	public String getPart() {
		return part;
	}
	public void setPart(String part) {
		this.part = part;
	}
	public String getPositionCodeNo() {
		return positionCodeNo;
	}
	public void setPositionCodeNo(String positionCodeNo) {
		this.positionCodeNo = positionCodeNo;
	}
	public String getDirectionCodeNo() {
		return directionCodeNo;
	}
	public void setDirectionCodeNo(String directionCodeNo) {
		this.directionCodeNo = directionCodeNo;
	}
	public String getDefectionCodeNo() {
		return defectionCodeNo;
	}
	public void setDefectionCodeNo(String defectionCodeNo) {
		this.defectionCodeNo = defectionCodeNo;
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
	public ExtendField getExtendField() {
		return extendField;
	}
	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}	
	public String toString() {
		return "制造质量管理：不良代码属性维护    ID"+this.getId()+",不良属性编号"+this.defectionCodeAttributeNo;
	}
}

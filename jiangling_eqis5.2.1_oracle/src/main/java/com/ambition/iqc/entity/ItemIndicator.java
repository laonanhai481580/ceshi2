package com.ambition.iqc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**    
 * ItemIndicator.java
 * @authorBy YUKE
 *
 */
@Entity
@Table(name = "IQC_ITEM_INDICATOR")
public class ItemIndicator extends IdEntity {
	private static final long serialVersionUID = 1L;
	/**数值类型*/
	public static final String VALUETYPE_DEFAULT="number";
	/**角度值类型*/
	public static final String VALUETYPE_DEGREE="degree";
	private Double totalPoints; //总分
	private String indicatorMethod;//检验方法
	private String classification;//缺陷等级
	private String countType = InspectingItem.COUNTTYPE_COUNT;//统计类型
	private String indicatorUnit;//单位
	private String inspectionLevel;//检验水平
	private String inspectionAmount;//检验数量
	private String aqlStandard;//aql标准
	private String specifications;//规格
	private String massParameter;//质量参数
	private String valueType=VALUETYPE_DEFAULT;//计量型时数字类型,默认为数字
	private Double levela;//上限
	private Double levelb;//下限
	private String isInEquipment="否";//是否集成设备
	@Column(length=3000)
    private String remark;//备注
    private Integer orderNum;//排序
    private String featureId;//质量特性
	private String featureName;//质量特性名称
    @ManyToOne
	@JoinColumn(name = "FK_INSPECTING_ITEM_ID")
    private InspectingItem inspectingItem;//检验项目

    @ManyToOne
   	@JoinColumn(name = "FK_INSPECTING_INDICATOR_ID")
    private InspectingIndicator inspectingIndicator;//项目标准
    
	public Double getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Double totalPoints) {
		this.totalPoints = totalPoints;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public Double getLevela() {
		return levela;
	}

	public void setLevela(Double levela) {
		this.levela = levela;
	}

	public Double getLevelb() {
		return levelb;
	}

	public void setLevelb(Double levelb) {
		this.levelb = levelb;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public InspectingItem getInspectingItem() {
		return inspectingItem;
	}

	public void setInspectingItem(InspectingItem inspectingItem) {
		this.inspectingItem = inspectingItem;
	}

	public InspectingIndicator getInspectingIndicator() {
		return inspectingIndicator;
	}

	public void setInspectingIndicator(InspectingIndicator inspectingIndicator) {
		this.inspectingIndicator = inspectingIndicator;
	}

	public String getAqlStandard() {
		return aqlStandard;
	}

	public void setAqlStandard(String aqlStandard) {
		this.aqlStandard = aqlStandard;
	}

	public String getInspectionLevel() {
		return inspectionLevel;
	}

	public void setInspectionLevel(String inspectionLevel) {
		this.inspectionLevel = inspectionLevel;
	}


	public String getInspectionAmount() {
		return inspectionAmount;
	}

	public void setInspectionAmount(String inspectionAmount) {
		this.inspectionAmount = inspectionAmount;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}


	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getCountType() {
		return countType;
	}

	public void setCountType(String countType) {
		this.countType = countType;
	}

	public String getIndicatorMethod() {
		return indicatorMethod;
	}

	public void setIndicatorMethod(String indicatorMethod) {
		this.indicatorMethod = indicatorMethod;
	}

	public String getIndicatorUnit() {
		return indicatorUnit;
	}

	public void setIndicatorUnit(String indicatorUnit) {
		this.indicatorUnit = indicatorUnit;
	}

	public String getMassParameter() {
		return massParameter;
	}

	public void setMassParameter(String massParameter) {
		this.massParameter = massParameter;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getIsInEquipment() {
		return isInEquipment;
	}

	public void setIsInEquipment(String isInEquipment) {
		this.isInEquipment = isInEquipment;
	}

	public String toString(){
		return "操作信息：检验项目指标    检验规格"+this.specifications;
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	
}

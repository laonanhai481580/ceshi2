package com.ambition.si.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 供应商检验检验标准明细
 * @author lpf
 *
 */
@Entity
@Table(name = "SI_ITEM_INDICATOR")
public class SiItemIndicator extends IdEntity {
	private static final long serialVersionUID = 1L;
	private Double totalPoints; //总分
	@Column(length=1000)
	private String method;//检验方法
	private String unit;//单位
	private String inspectionLevel;//检验类别
	private Integer inspectionAmount;//检验数量
	private Integer inAmountFir;//首检检验数量
	private Integer inAmountPatrol;//巡检检验数量
	private Integer inAmountEnd;//末检检验数量
	private String isJnUnit="否";//是否IPQC测试项
	private String isInEquipment="否";//是否集成设备
	private String aqlStandard;//aql标准
	@Column(length=1000)
	private String specifications;//规格
	private String massParameter;//质量参数
	private Double levela;//上限
	private Double levelb;//下限
    private String remark;//备注
    private String featureId;//质量特性
	private String featureName;//质量特性名称
    private Integer orderNum=0;//排序
    
    @ManyToOne
	@JoinColumn(name = "FK_INSPECTING_ITEM_ID")
    private SiInspectingItem siInspectingItem;//检验项目

    @ManyToOne
   	@JoinColumn(name = "FK_INSPECTING_INDICATOR_ID")
    private SiInspectingIndicator siInspectingIndicator;//项目标准

	public Double getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Double totalPoints) {
		this.totalPoints = totalPoints;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getInspectionLevel() {
		return inspectionLevel;
	}

	public void setInspectionLevel(String inspectionLevel) {
		this.inspectionLevel = inspectionLevel;
	}

	public Integer getInspectionAmount() {
		return inspectionAmount;
	}

	public void setInspectionAmount(Integer inspectionAmount) {
		this.inspectionAmount = inspectionAmount;
	}

	public Integer getInAmountFir() {
		return inAmountFir;
	}

	public void setInAmountFir(Integer inAmountFir) {
		this.inAmountFir = inAmountFir;
	}

	public Integer getInAmountPatrol() {
		return inAmountPatrol;
	}

	public void setInAmountPatrol(Integer inAmountPatrol) {
		this.inAmountPatrol = inAmountPatrol;
	}

	public Integer getInAmountEnd() {
		return inAmountEnd;
	}

	public void setInAmountEnd(Integer inAmountEnd) {
		this.inAmountEnd = inAmountEnd;
	}

	public String getIsJnUnit() {
		return isJnUnit;
	}

	public void setIsJnUnit(String isJnUnit) {
		this.isJnUnit = isJnUnit;
	}

	public String getIsInEquipment() {
		return isInEquipment;
	}

	public void setIsInEquipment(String isInEquipment) {
		this.isInEquipment = isInEquipment;
	}

	public String getAqlStandard() {
		return aqlStandard;
	}

	public void setAqlStandard(String aqlStandard) {
		this.aqlStandard = aqlStandard;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getMassParameter() {
		return massParameter;
	}

	public void setMassParameter(String massParameter) {
		this.massParameter = massParameter;
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

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public SiInspectingItem getSiInspectingItem() {
		return siInspectingItem;
	}

	public void setSiInspectingItem(SiInspectingItem siInspectingItem) {
		this.siInspectingItem = siInspectingItem;
	}

	public SiInspectingIndicator getSiInspectingIndicator() {
		return siInspectingIndicator;
	}

	public void setSiInspectingIndicator(SiInspectingIndicator siInspectingIndicator) {
		this.siInspectingIndicator = siInspectingIndicator;
	}
 
}

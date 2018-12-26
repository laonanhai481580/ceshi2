package com.ambition.carmfg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.ambition.product.base.IdEntity;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.norteksoft.product.util.ContextUtils;

/**
 * 制造检验标准明细
 * @author 赵骏
 *
 */
@Entity
@Table(name = "MFG_ITEM_INDICATOR")
public class MfgItemIndicator extends IdEntity {
	private static final long serialVersionUID = 1L;
	private Double totalPoints; //总分
	@Column(length=1000)
	private String method;//检验方法
	private String countType = MfgInspectingItem.COUNTTYPE_COUNT;//统计类型
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
    private MfgInspectingItem mfgInspectingItem;//检验项目

    @ManyToOne
   	@JoinColumn(name = "FK_INSPECTING_INDICATOR_ID")
    private MfgInspectingIndicator mfgInspectingIndicator;//项目标准
    
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
	
	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	@Transient
	public String getFeatureName(){
		if(StringUtils.isEmpty(featureId)){
			return null;
		}
		QualityFeatureManager manager = (QualityFeatureManager)ContextUtils.getBean("qualityFeatureManager");
		QualityFeature feature = manager.getQualityFeatureById(Long.valueOf(featureId));
		if(feature==null){
			return null;
		}else{
			return feature.getName();
		}
	}
	public MfgInspectingItem getMfgInspectingItem() {
		return mfgInspectingItem;
	}

	public void setMfgInspectingItem(MfgInspectingItem mfgInspectingItem) {
		this.mfgInspectingItem = mfgInspectingItem;
	}

	public MfgInspectingIndicator getMfgInspectingIndicator() {
		return mfgInspectingIndicator;
	}

	public void setMfgInspectingIndicator(
			MfgInspectingIndicator mfgInspectingIndicator) {
		this.mfgInspectingIndicator = mfgInspectingIndicator;
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

	public Integer getInspectionAmount() {
		return inspectionAmount;
	}

	public void setInspectionAmount(Integer inspectionAmount) {
		this.inspectionAmount = inspectionAmount;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getCountType() {
		return countType;
	}

	public void setCountType(String countType) {
		this.countType = countType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getMassParameter() {
		return massParameter;
	}
	public void setMassParameter(String massParameter) {
		this.massParameter = massParameter;
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

	

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String toString(){
		return "操作信息：制造检验项目指标    检验规格"+this.specifications;
	}
}

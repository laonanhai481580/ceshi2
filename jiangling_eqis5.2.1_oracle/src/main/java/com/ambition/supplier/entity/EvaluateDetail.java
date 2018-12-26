package com.ambition.supplier.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.norteksoft.wf.engine.client.ExtendField;

/**
 * 评价的明细指标
 * @author ambition-zhaojun
 *
 */
@Entity
@Table(name = "SUPPLIER_EVALUATE_DETAIL")
public class EvaluateDetail  extends IdEntity {
	private static final long serialVersionUID = 1L;
	private Long modelIndicatorId;//对应的模型指标编号
	private Long estimateModelId;//对应的模型编号
	private Long modelIndicatorParentId;//对应的模型指标父编号
	private Long evaluatingIndicatorId;//对应的考评指标id
	private String firstColHtml;
	private String secondColHtml;
	private String thirdColHtml;
	private String level1Name;//每一级名称
	private Integer level1Rowspan = 1;//第一级行合并
	private Integer level1Colspan = 1;//第一级列合并
	private String level2Name;//
	private Integer level2Rowspan = 1;//第二级行合并
	private Integer level2Colspan = 1;//第二级列合并
	private String parentName;//父级指标名称
	private String unit;//指标单位
	private String name;//指标名称
	private Double totalPoints; //总分
	private String levela;//A档分字符串
	private Double levelaMin;//A档分最小分
	private Double levelaMax;//A档分最高分
	private String levelb;//B档分字符串
	private Double levelbMin;//B档分
	private Double levelbMax;//B档分
	private String levelc;//C档分字符串
	private Double levelcMax;//C档分
	private Double levelcMin;//C档分
	private String leveld;//D档分字符串
	private Double leveldMin;//d档分最少分
	private Double leveldMax;//d档分最多分
	private String levele;//E档分字符串
	private Double leveleMin;//e档分最小分
	private Double leveleMax;//e档分最多分
	private Double score;//实绩
	private Double realTotalPoints;//实际得分
    private String remark;//备注
    private Integer orderByNum;

    @Embedded
    private ExtendField extendField;
    
    @ManyToOne
   	@JoinColumn(name = "FK_EVALUATE_ID")
    private Evaluate evaluate;//评价
    
	public Double getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Double totalPoints) {
		this.totalPoints = totalPoints;
	}

	public Double getLevelaMin() {
		return levelaMin;
	}

	public void setLevelaMin(Double levelaMin) {
		this.levelaMin = levelaMin;
	}

	public Double getLevelaMax() {
		return levelaMax;
	}

	public void setLevelaMax(Double levelaMax) {
		this.levelaMax = levelaMax;
	}

	public Double getLevelbMin() {
		return levelbMin;
	}

	public void setLevelbMin(Double levelbMin) {
		this.levelbMin = levelbMin;
	}

	public Double getLevelbMax() {
		return levelbMax;
	}

	public void setLevelbMax(Double levelbMax) {
		this.levelbMax = levelbMax;
	}

	public Double getLevelcMax() {
		return levelcMax;
	}

	public void setLevelcMax(Double levelcMax) {
		this.levelcMax = levelcMax;
	}

	public Double getLevelcMin() {
		return levelcMin;
	}

	public void setLevelcMin(Double levelcMin) {
		this.levelcMin = levelcMin;
	}

	public Double getLeveldMin() {
		return leveldMin;
	}

	public void setLeveldMin(Double leveldMin) {
		this.leveldMin = leveldMin;
	}

	public Double getLeveldMax() {
		return leveldMax;
	}

	public void setLeveldMax(Double leveldMax) {
		this.leveldMax = leveldMax;
	}

	public Double getLeveleMin() {
		return leveleMin;
	}

	public void setLeveleMin(Double leveleMin) {
		this.leveleMin = leveleMin;
	}

	public Double getLeveleMax() {
		return leveleMax;
	}

	public void setLeveleMax(Double leveleMax) {
		this.leveleMax = leveleMax;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Evaluate getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(Evaluate evaluate) {
		this.evaluate = evaluate;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getRealTotalPoints() {
		return realTotalPoints;
	}

	public void setRealTotalPoints(Double realTotalPoints) {
		this.realTotalPoints = realTotalPoints;
	}

	public Long getModelIndicatorId() {
		return modelIndicatorId;
	}

	public void setModelIndicatorId(Long modelIndicatorId) {
		this.modelIndicatorId = modelIndicatorId;
	}
	
	public String getLevela() {
		return levela;
	}

	public void setLevela(String levela) {
		this.levela = levela;
	}

	public String getLevelb() {
		return levelb;
	}

	public void setLevelb(String levelb) {
		this.levelb = levelb;
	}

	public String getLevelc() {
		return levelc;
	}

	public void setLevelc(String levelc) {
		this.levelc = levelc;
	}

	public String getLeveld() {
		return leveld;
	}

	public void setLeveld(String leveld) {
		this.leveld = leveld;
	}

	public String getLevele() {
		return levele;
	}

	public void setLevele(String levele) {
		this.levele = levele;
	}

	public String getLevel1Name() {
		return level1Name;
	}

	public void setLevel1Name(String level1Name) {
		this.level1Name = level1Name;
	}

	public Integer getLevel1Rowspan() {
		return level1Rowspan;
	}

	public void setLevel1Rowspan(Integer level1Rowspan) {
		this.level1Rowspan = level1Rowspan;
	}

	public Integer getLevel1Colspan() {
		return level1Colspan;
	}

	public void setLevel1Colspan(Integer level1Colspan) {
		this.level1Colspan = level1Colspan;
	}

	public String getLevel2Name() {
		return level2Name;
	}

	public void setLevel2Name(String level2Name) {
		this.level2Name = level2Name;
	}

	public Integer getLevel2Rowspan() {
		return level2Rowspan;
	}

	public void setLevel2Rowspan(Integer level2Rowspan) {
		this.level2Rowspan = level2Rowspan;
	}

	public Integer getLevel2Colspan() {
		return level2Colspan;
	}

	public void setLevel2Colspan(Integer level2Colspan) {
		this.level2Colspan = level2Colspan;
	}

	public String getFirstColHtml() {
		return firstColHtml;
	}

	public void setFirstColHtml(String firstColHtml) {
		this.firstColHtml = firstColHtml;
	}

	public String getSecondColHtml() {
		return secondColHtml;
	}

	public void setSecondColHtml(String secondColHtml) {
		this.secondColHtml = secondColHtml;
	}

	public String getThirdColHtml() {
		return thirdColHtml;
	}

	public void setThirdColHtml(String thirdColHtml) {
		this.thirdColHtml = thirdColHtml;
	}

	public ExtendField getExtendField() {
		return extendField;
	}

	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}

	public Long getEstimateModelId() {
		return estimateModelId;
	}

	public void setEstimateModelId(Long estimateModelId) {
		this.estimateModelId = estimateModelId;
	}

	public Long getModelIndicatorParentId() {
		return modelIndicatorParentId;
	}

	public void setModelIndicatorParentId(Long modelIndicatorParentId) {
		this.modelIndicatorParentId = modelIndicatorParentId;
	}

	public Integer getOrderByNum() {
		return orderByNum;
	}

	public void setOrderByNum(Integer orderByNum) {
		this.orderByNum = orderByNum;
	}

	public Long getEvaluatingIndicatorId() {
		return evaluatingIndicatorId;
	}

	public void setEvaluatingIndicatorId(Long evaluatingIndicatorId) {
		this.evaluatingIndicatorId = evaluatingIndicatorId;
	}

	public String toString(){
		return "供应商质量管理：指标明细    名称"+this.name+"，总分"+this.totalPoints+"，实际得分"+this.realTotalPoints;
	}
}

package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 评价模型的指标
 * @author ambition-zhaojun
 *
 */
@Entity
@Table(name = "SUPPLIER_MODEL_INDICATOR")
public class ModelIndicator  extends IdEntity {
	private static final long serialVersionUID = 1L;
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
    private String remark;//备注
    
    @ManyToOne
	@JoinColumn(name = "FK_ESTIMATE_MODEL_ID")
    private EstimateModel estimateModel;//模型分类

    @ManyToOne
   	@JoinColumn(name = "FK_EVALUATING_INDICATOR_ID")
    private EvaluatingIndicator evaluatingIndicator;//评价指标
    
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

	public EstimateModel getEstimateModel() {
		return estimateModel;
	}

	public void setEstimateModel(EstimateModel estimateModel) {
		this.estimateModel = estimateModel;
	}

	public EvaluatingIndicator getEvaluatingIndicator() {
		return evaluatingIndicator;
	}

	public void setEvaluatingIndicator(EvaluatingIndicator evaluatingIndicator) {
		this.evaluatingIndicator = evaluatingIndicator;
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

	public String toString(){
		return "供应商质量管理：评价模型的指标    总分"+this.totalPoints+"，A档"+levelaMin==null||levelaMax==null?"":(levelaMin + "-" + levelaMax);
	}
}

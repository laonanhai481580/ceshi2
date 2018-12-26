package com.ambition.spc.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**    
 * FeatureRules.java
 * @authorBy YUKE
 *
 */
@Entity
@Table(name = "SPC_FEATURE_RULES")
public class FeatureRules extends IdEntity {

	/**
	 * 质量特性-判断准则
	 */
	private static final long serialVersionUID = 1L;
	private String name;//准则名称
	private String no;//准则编号
	private String expression;//准则表达式,准则表达式：控制图类别.控制图类型.点数.上升/下降/不变……
	private String type;//控制图类别,0:主控制图，1：副控制图
	private String model;//控制图类型;0：趋势型，1：运行型，3：交替性，4：其他
	private Long targetId;
	@ManyToOne
	@JoinColumn(name="FK_QUALITY_FEATURE_ID")
	private QualityFeature qualityFeature;//质量特性
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public QualityFeature getQualityFeature() {
		return qualityFeature;
	}
	public void setQualityFeature(QualityFeature qualityFeature) {
		this.qualityFeature = qualityFeature;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	
}

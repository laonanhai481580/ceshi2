package com.ambition.spc.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**    
 * FeatureLayer.java
 * @authorBy YUKE
 *
 */
@Entity
@Table(name = "SPC_FEATURE_LAYER")
public class FeatureLayer extends IdEntity {
	/**
	 * 质量特性-层别信息
	 */
	private static final long serialVersionUID = 1L;
	private String detailName;//名称
	private String detailCode;//简码
	private String sampleMethod;//取值方式
	private String isInputValue;//是否录入取值
	private Long targetId;
	@ManyToOne
	@JoinColumn(name="FK_FEATURE_LAYER_ID")
	private QualityFeature qualityFeature;//质量特性
	
	public String getDetailName() {
		return detailName;
	}
	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}
	public String getDetailCode() {
		return detailCode;
	}
	public void setDetailCode(String detailCode) {
		this.detailCode = detailCode;
	}
	public QualityFeature getQualityFeature() {
		return qualityFeature;
	}
	public void setQualityFeature(QualityFeature qualityFeature) {
		this.qualityFeature = qualityFeature;
	}
	public String getSampleMethod() {
		return sampleMethod;
	}
	public void setSampleMethod(String sampleMethod) {
		this.sampleMethod = sampleMethod;
	}
	public String getIsInputValue() {
		return isInputValue;
	}
	public void setIsInputValue(String isInputValue) {
		this.isInputValue = isInputValue;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	
}

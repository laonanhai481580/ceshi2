package com.ambition.spc.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**    
 * FeaturePerson.java
 * @authorBy YUKE
 *
 */
@Entity
@Table(name = "SPC_FEATURE_PERSON")
public class FeaturePerson extends IdEntity {

	/**
	 * 质量特性-异常通知人员
	 */
	private static final long serialVersionUID = 1L;
	private String name;//名称
	private String code;//代号
	private String email;//邮箱
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
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getTargetId() {
		return targetId;
	}
	
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	
	public QualityFeature getQualityFeature() {
		return qualityFeature;
	}
	
	public void setQualityFeature(QualityFeature qualityFeature) {
		this.qualityFeature = qualityFeature;
	}
	
}

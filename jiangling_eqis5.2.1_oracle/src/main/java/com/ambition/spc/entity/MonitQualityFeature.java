package com.ambition.spc.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import com.ambition.product.base.IdEntity;

/**    
 * MonitQualityFeature.java
 * @authorBy wanglf
 *
 */
@Entity
@Table(name = "SPC_MONIT_QUALITY_FEATURE")
public class MonitQualityFeature extends IdEntity {

	/**
	 * 监控点下面的质量参数
	 */
	private static final long serialVersionUID = 1L;
	//基本信息
	private Long qualityFeatureId;
	@ManyToOne
	@JoinColumn(name="FK_MONIT_POINT_ID")
	private MonitPoint monitPoint;//监控方案
	public Long getQualityFeatureId() {
		return qualityFeatureId;
	}
	public void setQualityFeatureId(Long qualityFeatureId) {
		this.qualityFeatureId = qualityFeatureId;
	}
	public MonitPoint getMonitPoint() {
		return monitPoint;
	}
	public void setMonitPoint(MonitPoint monitPoint) {
		this.monitPoint = monitPoint;
	}
	
	
}

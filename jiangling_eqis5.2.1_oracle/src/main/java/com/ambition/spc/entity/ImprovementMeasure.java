package com.ambition.spc.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**    
 * ImprovementMeasure.java
 * @authorBy YUKE
 * 改善措施
 */
@Entity
@Table(name = "SPC_IMPROVEMENT_MEASURE")
public class ImprovementMeasure extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String measureNo;//措施代码
	private String measureName;//措施名称
	private String measure;//改善措施
	private Date improveTime;//开始改善时间
	private Date measureTime;//采取措施时间
	private String personLiable;//责任人
	
	public String getMeasureNo() {
		return measureNo;
	}

	public void setMeasureNo(String measureNo) {
		this.measureNo = measureNo;
	}

	public String getMeasureName() {
		return measureName;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}

	public String getMeasure() {
		return measure;
	}
	
	public void setMeasure(String measure) {
		this.measure = measure;
	}
	
	public Date getImproveTime() {
		return improveTime;
	}
	
	public void setImproveTime(Date improveTime) {
		this.improveTime = improveTime;
	}
	
	public Date getMeasureTime() {
		return measureTime;
	}
	
	public void setMeasureTime(Date measureTime) {
		this.measureTime = measureTime;
	}
	
	public String getPersonLiable() {
		return personLiable;
	}
	
	public void setPersonLiable(String personLiable) {
		this.personLiable = personLiable;
	}
}

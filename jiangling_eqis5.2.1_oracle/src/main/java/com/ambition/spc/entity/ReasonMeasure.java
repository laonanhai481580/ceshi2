package com.ambition.spc.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**    
 * ReasonMeasure.java
 * @authorBy YUKE
 *	原因措施库
 */
@Entity
@Table(name = "SPC_REASON_MEASURE")
public class ReasonMeasure extends IdEntity {
	
	private static final long serialVersionUID = 1L;
	private String name;//名称
	private String num;//子组号
	private Date occurDate;//发生时间
	
	private String reason;//原因内容
	private Date analyzeStartTime;//分析原因开始时间
	private Date analyzeEndTime;//分析原因结束时间
	private String reasonPersonLiable;//责任人
	
	private String measure;//改善措施
	private Date improveTime;//开始改善时间
	private Date measureTime;//采取措施时间
	private String measurePersonLiable;//责任人
	
	@ManyToOne
	@JoinColumn(name = "FK_QUALITY_FEATURE_ID")
	private QualityFeature qualityFeature;//质量特性
	
	@ManyToOne
	@JoinColumn(name = "FK_ABNORMAL_INFO_ID")
	private AbnormalInfo abnormalInfo;//异常消息

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public Date getOccurDate() {
		return occurDate;
	}

	public void setOccurDate(Date occurDate) {
		this.occurDate = occurDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getAnalyzeStartTime() {
		return analyzeStartTime;
	}

	public void setAnalyzeStartTime(Date analyzeStartTime) {
		this.analyzeStartTime = analyzeStartTime;
	}

	public Date getAnalyzeEndTime() {
		return analyzeEndTime;
	}

	public void setAnalyzeEndTime(Date analyzeEndTime) {
		this.analyzeEndTime = analyzeEndTime;
	}

	public String getReasonPersonLiable() {
		return reasonPersonLiable;
	}

	public void setReasonPersonLiable(String reasonPersonLiable) {
		this.reasonPersonLiable = reasonPersonLiable;
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

	public String getMeasurePersonLiable() {
		return measurePersonLiable;
	}

	public void setMeasurePersonLiable(String measurePersonLiable) {
		this.measurePersonLiable = measurePersonLiable;
	}

	public QualityFeature getQualityFeature() {
		return qualityFeature;
	}

	public void setQualityFeature(QualityFeature qualityFeature) {
		this.qualityFeature = qualityFeature;
	}

	public AbnormalInfo getAbnormalInfo() {
		return abnormalInfo;
	}

	public void setAbnormalInfo(AbnormalInfo abnormalInfo) {
		this.abnormalInfo = abnormalInfo;
	}
}

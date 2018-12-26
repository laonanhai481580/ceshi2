package com.ambition.spc.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**    
 * AbnormalReason.java
 * @authorBy YUKE
 * 异常原因
 */
@Entity
@Table(name = "SPC_ABNORMAL_REASON")
public class AbnormalReason extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String reasonNo;//原因代码
	private String reasonName;//原因名称
	private String reasonType;//原因类型
	private String reason;//原因内容
	private Date analyzeStartTime;//分析原因开始时间
	private Date analyzeEndTime;//分析原因结束时间
	private String personLiable;//责任人
	private String measures;//改善措施
	@ManyToOne
	@JoinColumn(name="FK_ABNORMAL_REASON_TYPE_ID")
	private AbnormalReasonType abnormalReasonType;//原因类别
	
	public String getReasonNo() {
		return reasonNo;
	}

	public void setReasonNo(String reasonNo) {
		this.reasonNo = reasonNo;
	}

	public String getReasonName() {
		return reasonName;
	}

	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}

	public String getReasonType() {
		return reasonType;
	}

	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
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
	
	public String getPersonLiable() {
		return personLiable;
	}
	
	public void setPersonLiable(String personLiable) {
		this.personLiable = personLiable;
	}

	public String getMeasures() {
		return measures;
	}

	public void setMeasures(String measures) {
		this.measures = measures;
	}

	public AbnormalReasonType getAbnormalReasonType() {
		return abnormalReasonType;
	}

	public void setAbnormalReasonType(AbnormalReasonType abnormalReasonType) {
		this.abnormalReasonType = abnormalReasonType;
	}
}

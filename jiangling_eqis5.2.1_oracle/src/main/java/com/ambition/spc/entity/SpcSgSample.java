package com.ambition.spc.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**    
 * SpcSgSample.java
 * @authorBy YUKE
 *
 */
@Entity
@Table(name = "SPC_SG_SAMPLE")
public class SpcSgSample extends IdEntity {

	/**
	 * 子组样本表
	 */
	private static final long serialVersionUID = 1L;
	private String sampleNo;//样本编号
	private String sampleOrderNum;//样本顺序		
	private Double samValue;//取值
	@ManyToOne
	@JoinColumn(name="FK_SUB_GROUP_ID")
	private SpcSubGroup spcSubGroup;//子组表
	
	public String getSampleNo() {
		return sampleNo;
	}
	
	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}

	public String getSampleOrderNum() {
		return sampleOrderNum;
	}

	public void setSampleOrderNum(String sampleOrderNum) {
		this.sampleOrderNum = sampleOrderNum;
	}

	public Double getSamValue() {
		return samValue;
	}

	public void setSamValue(Double samValue) {
		this.samValue = samValue;
	}

	public SpcSubGroup getSpcSubGroup() {
		return spcSubGroup;
	}

	public void setSpcSubGroup(SpcSubGroup spcSubGroup) {
		this.spcSubGroup = spcSubGroup;
	}
}

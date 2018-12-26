package com.ambition.iqc.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 类名:ORT项目测试.java
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  wuxuming
 * @version 1.00 2016-10-24 发布
 */
@Table(name="IQC_ORT_EXPERIMENTAL_ITEM")
@Entity
public class OrtExperimentalItem  extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String ortItemName;//ORT名称
	private String ortResult;//ORT检验结果
	@ManyToOne
	@JoinColumn(name = "FK_EXP_DELEGATION")
	@JsonIgnore()
	private ExperimentalDelegation experimentalDelegation;
	public String getOrtItemName() {
		return ortItemName;
	}
	public void setOrtItemName(String ortItemName) {
		this.ortItemName = ortItemName;
	}
	public String getOrtResult() {
		return ortResult;
	}
	public void setOrtResult(String ortResult) {
		this.ortResult = ortResult;
	}
	public ExperimentalDelegation getExperimentalDelegation() {
		return experimentalDelegation;
	}
	public void setExperimentalDelegation(
			ExperimentalDelegation experimentalDelegation) {
		this.experimentalDelegation = experimentalDelegation;
	}
	
	
}

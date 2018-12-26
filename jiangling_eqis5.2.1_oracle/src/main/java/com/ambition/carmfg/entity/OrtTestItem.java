package com.ambition.carmfg.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "MFG_ORT_TEST_ITEM")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class OrtTestItem extends IdEntity {

	private static final long serialVersionUID = 1L;
	private String testItem;//测试项目
	private String testCondition;//测试条件
	private String judgeStandard;//判定标准
	private Integer value;//测试数量
	private String testResult;//测试结果
	@ManyToOne
	@JoinColumn(name = "MFG_TEST_ENTRUST_ID")
	@JsonIgnore()
	private OrtTestEntrust ortTestEntrust;
	public String getTestItem() {
		return testItem;
	}
	public void setTestItem(String testItem) {
		this.testItem = testItem;
	}
	public String getTestCondition() {
		return testCondition;
	}
	public void setTestCondition(String testCondition) {
		this.testCondition = testCondition;
	}
	public String getJudgeStandard() {
		return judgeStandard;
	}
	public void setJudgeStandard(String judgeStandard) {
		this.judgeStandard = judgeStandard;
	}
	public String getTestResult() {
		return testResult;
	}
	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}
	public OrtTestEntrust getOrtTestEntrust() {
		return ortTestEntrust;
	}
	public void setOrtTestEntrust(OrtTestEntrust ortTestEntrust) {
		this.ortTestEntrust = ortTestEntrust;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}

	
	
	
	
}

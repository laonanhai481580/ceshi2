package com.ambition.carmfg.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:ORT检验项目维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月6日 发布
 */
@Entity
@Table(name = "MFG_ORT_INSPECTION_ITEM")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class OrtInspectionItem extends IdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "MFG_ORT_INSPECTION_ITEM";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "ORT检验项目";//实体_列表_名称
	//private String enterpriseGroup;//事业群
	private String testItem;//测试项目
	private String testCondition;//测试条件
	private Integer value;//测试数量
	private String judgeStandard;//判定标准
	@ManyToOne
	@JoinColumn(name = "FK_ORT_CUSTOMER_NO")
	@JsonIgnore
	private OrtCustomer ortCustomer;//客户	
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
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public OrtCustomer getOrtCustomer() {
		return ortCustomer;
	}
	public void setOrtCustomer(OrtCustomer ortCustomer) {
		this.ortCustomer = ortCustomer;
	}

	
	

}

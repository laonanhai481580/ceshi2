package com.ambition.cost.entity;

import com.norteksoft.acs.entity.IdEntity;
/**
 * 质量成本视图
 * @author ambuser
 *
 */
public class QisCostView extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String formNo;//来源单号
	private String occurringMonthStr;//月份字符串,手动维护记录时有用到
	private Integer occurringMonth;//发生月份
	private String levelTwoCode;//二级代码
	private String levelTwoName;//二级成本名称
	private String levelThreeCode;//三级代码
	private String levelThreeName;//三级成本名称
	private String code;//末级代码
	private String name;//末级名称
	private String dutyDepart;//部门名称
	private Double value;//发生费用
	private String sourceType;
	private String feeState;//费用状态
    private String itemGroup;//物料组
	private String customerName;//客户
	private String project;//项目
	private String companyName;//公司
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getOccurringMonthStr() {
		return occurringMonthStr;
	}
	public void setOccurringMonthStr(String occurringMonthStr) {
		this.occurringMonthStr = occurringMonthStr;
	}
	public Integer getOccurringMonth() {
		return occurringMonth;
	}
	public void setOccurringMonth(Integer occurringMonth) {
		this.occurringMonth = occurringMonth;
	}
	public String getLevelTwoCode() {
		return levelTwoCode;
	}
	public void setLevelTwoCode(String levelTwoCode) {
		this.levelTwoCode = levelTwoCode;
	}
	public String getLevelTwoName() {
		return levelTwoName;
	}
	public void setLevelTwoName(String levelTwoName) {
		this.levelTwoName = levelTwoName;
	}
	public String getLevelThreeCode() {
		return levelThreeCode;
	}
	public void setLevelThreeCode(String levelThreeCode) {
		this.levelThreeCode = levelThreeCode;
	}
	public String getLevelThreeName() {
		return levelThreeName;
	}
	public void setLevelThreeName(String levelThreeName) {
		this.levelThreeName = levelThreeName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getFeeState() {
		return feeState;
	}
	public void setFeeState(String feeState) {
		this.feeState = feeState;
	}
	public String getItemGroup() {
		return itemGroup;
	}
	public void setItemGroup(String itemGroup) {
		this.itemGroup = itemGroup;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getDutyDepart() {
		return dutyDepart;
	}
	public void setDutyDepart(String dutyDepart) {
		this.dutyDepart = dutyDepart;
	}
	
	
}

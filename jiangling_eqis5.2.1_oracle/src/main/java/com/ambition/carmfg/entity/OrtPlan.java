package com.ambition.carmfg.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:ORT检验计划
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年8月31日 发布
 */
@Entity
@Table(name = "MFG_ORT_PLAN")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class OrtPlan extends IdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "MFG_ORT_PLAN";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "ORT计划";//实体_列表_名称
	private String enterpriseGroup;//事业群
	private String model;//机种	
	private String customer;//客户
	private String customerNo;//客户编号
	private String fileCode;//测试标准文件编号
	private Integer count;//数量
	private Date planTestDate;//预计测试时间
	private Date actualDate;//实际完成时间
	private String testResult;//测试结果
	private String testEntrustCode="";//实验委托单号
	private Long testEntrustId;//实验委托ID
	private String producer;//制作人
	private String remark;//备注
	private Integer isTest;//是否发起过委托
	public String getEnterpriseGroup() {
		return enterpriseGroup;
	}
	public void setEnterpriseGroup(String enterpriseGroup) {
		this.enterpriseGroup = enterpriseGroup;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getFileCode() {
		return fileCode;
	}
	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Date getPlanTestDate() {
		return planTestDate;
	}
	public void setPlanTestDate(Date planTestDate) {
		this.planTestDate = planTestDate;
	}
	public Date getActualDate() {
		return actualDate;
	}
	public void setActualDate(Date actualDate) {
		this.actualDate = actualDate;
	}
	public String getTestResult() {
		return testResult;
	}
	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}
	public String getTestEntrustCode() {
		return testEntrustCode;
	}
	public void setTestEntrustCode(String testEntrustCode) {
		this.testEntrustCode = testEntrustCode;
	}
	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getIsTest() {
		return isTest;
	}
	public void setIsTest(Integer isTest) {
		this.isTest = isTest;
	}
	public Long getTestEntrustId() {
		return testEntrustId;
	}
	public void setTestEntrustId(Long testEntrustId) {
		this.testEntrustId = testEntrustId;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	
	

}

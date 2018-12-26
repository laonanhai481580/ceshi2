package com.ambition.carmfg.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:OQC出货报告
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月1日 发布
 */
@Entity
@Table(name = "MFG_OQC_DELIVER")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class OqcDeliver extends IdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "MFG_OQC_DELIVER";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "OQC出货报告";//实体_列表_名称
	//private String businessUnit;//事业部
	private String inspectionBatchNo;//检验批号
	private String processSection; // 制程区段
	private String factory; //工厂
	private String model;//机种	
	private String customer;//客户
	private Date deliverDate;//出货日期
	private Integer deliverCount;//出货数量
	private String judgeResult;//判定
	private String attachment;//附件（报告）
	private String remark;//备注
	private String dutyMan;//责任人
	public String getInspectionBatchNo() {
		return inspectionBatchNo;
	}
	public void setInspectionBatchNo(String inspectionBatchNo) {
		this.inspectionBatchNo = inspectionBatchNo;
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
	public Date getDeliverDate() {
		return deliverDate;
	}
	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}
	public String getJudgeResult() {
		return judgeResult;
	}
	public void setJudgeResult(String judgeResult) {
		this.judgeResult = judgeResult;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDutyMan() {
		return dutyMan;
	}
	public void setDutyMan(String dutyMan) {
		this.dutyMan = dutyMan;
	}
	public Integer getDeliverCount() {
		return deliverCount;
	}
	public void setDeliverCount(Integer deliverCount) {
		this.deliverCount = deliverCount;
	}
	public String getProcessSection() {
		return processSection;
	}
	public void setProcessSection(String processSection) {
		this.processSection = processSection;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}	

}

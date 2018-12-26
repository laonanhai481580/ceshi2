package com.ambition.carmfg.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:尺寸检验数据
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2017年7月7日 发布
 */
@Entity
@Table(name = "MFG_OQC_SIZE_INSPECTION")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class SizeInspection extends IdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "MFG_OQC_SIZE_INSPECTION";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "尺寸检验数据";//实体_列表_名称
	private Date inspectionDate;//日期
	private String classGroup;//班别
	private String inspectionBatchNo;//检验批号
	private String model;//机种	
	private String customerType;//客户类别	
	private String attachment;//附件
	public Date getInspectionDate() {
		return inspectionDate;
	}
	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
	public String getClassGroup() {
		return classGroup;
	}
	public void setClassGroup(String classGroup) {
		this.classGroup = classGroup;
	}
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
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	
	
}

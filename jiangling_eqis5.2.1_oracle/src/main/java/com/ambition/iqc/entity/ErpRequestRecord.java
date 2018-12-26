package com.ambition.iqc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 类名:请检单接口操作记录,方便后续跟踪管理
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2013-8-9 发布
 */
@Entity
@Table(name="IQC_ERP_REQUEST_RECORD")
public class ErpRequestRecord extends IdEntity {
	private static final long serialVersionUID = 8147120884398800570L;
	private String collectionName="新增请检单";//接口名称
	@Column(length=2000)
	private String details;//详细
	public String getCollectionName() {
		return collectionName;
	}
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
}
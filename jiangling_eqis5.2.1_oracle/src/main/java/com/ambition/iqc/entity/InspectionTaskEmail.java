package com.ambition.iqc.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 类名:检验任务通知.java
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  吴旭明
 * @version 1.00 2016-10-21 发布
 */
@Entity
@Table(name="IQC_INSPECTION_TASK_EMAIL")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class InspectionTaskEmail extends IdEntity{
	private static final long serialVersionUID = 1L;
	
	private String materielCategory;//物料类别
	private String userIds;//IDS
	private String reveiveLoginMan;//收件人登录名
	private String receiveMan;//收件人
	public String getMaterielCategory() {
		return materielCategory;
	}
	public void setMaterielCategory(String materielCategory) {
		this.materielCategory = materielCategory;
	}
	public String getReveiveLoginMan() {
		return reveiveLoginMan;
	}
	public void setReveiveLoginMan(String reveiveLoginMan) {
		this.reveiveLoginMan = reveiveLoginMan;
	}
	public String getReceiveMan() {
		return receiveMan;
	}
	public void setReceiveMan(String receiveMan) {
		this.receiveMan = receiveMan;
	}
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	
	
}

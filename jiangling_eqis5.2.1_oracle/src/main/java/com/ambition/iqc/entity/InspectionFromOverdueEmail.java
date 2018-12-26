package com.ambition.iqc.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 类名:检验单超期台帐设置
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  wuxuming
 * @version 1.00 2016-10-21 发布
 */
@Entity
@Table(name = "IQC_INSPECTION_OVERDUE_EMAIL")
public class InspectionFromOverdueEmail extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String materielCategory;//物料类别
	private String materielCode;//物料编码
	private Float overdue;//超期时长
	private String userIds;//使用人员ID
	private String reveiveLoginMan;//收件人登录名
	private String receiveMan;//收件人
	public String getMaterielCategory() {
		return materielCategory;
	}
	public void setMaterielCategory(String materielCategory) {
		this.materielCategory = materielCategory;
	}
	public Float getOverdue() {
		return overdue;
	}
	public void setOverdue(Float overdue) {
		this.overdue = overdue;
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
	public String getMaterielCode() {
		return materielCode;
	}
	public void setMaterielCode(String materielCode) {
		this.materielCode = materielCode;
	}
	
	
	
}

package com.ambition.carmfg.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:IPQC稽核件数维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2018年2月1日 发布
 */
@Entity
@Table(name = "MFG_IPQC_AUDIT_AMOUNT")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class IpqcAuditAmount extends IdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "MFG_IPQC_AUDIT_AMOUNT";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "IPQC稽核件数维护";//实体_列表_名称
	private String factory; //工厂
	private String classGroup;//班次
	private String station;//工序
	private Integer auditAmount;//稽核件数
	private Integer auditScore;//稽核分数
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getClassGroup() {
		return classGroup;
	}
	public void setClassGroup(String classGroup) {
		this.classGroup = classGroup;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public Integer getAuditAmount() {
		return auditAmount;
	}
	public void setAuditAmount(Integer auditAmount) {
		this.auditAmount = auditAmount;
	}
	public Integer getAuditScore() {
		return auditScore;
	}
	public void setAuditScore(Integer auditScore) {
		this.auditScore = auditScore;
	}
	
	
}

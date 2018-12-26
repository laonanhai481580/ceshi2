package com.ambition.carmfg.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:IPQC稽核预警（问题严重度+重复性）
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月20日 发布
 */
@Entity
@Table(name = "MFG_IPQC_AUDIT_WARMING")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class IpqcAuditWarming extends IdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "MFG_IPQC_AUDIT_WARMING";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "IPQC稽核问题严重度+重复性维护";//实体_列表_名称
	//private String businessUnit;//事业部
	private String station;//站别
	private String problemDegree;//问题严重度
	private String missingItems;//缺失项目
	private Integer repeatCount;//重复性
	private String warmingCycle;//统计周期
	private String warmingMan;//预警人
	private String warmingManLogin;//预警人登录名
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public String getProblemDegree() {
		return problemDegree;
	}
	public void setProblemDegree(String problemDegree) {
		this.problemDegree = problemDegree;
	}
	public String getMissingItems() {
		return missingItems;
	}
	public void setMissingItems(String missingItems) {
		this.missingItems = missingItems;
	}
	public Integer getRepeatCount() {
		return repeatCount;
	}
	public void setRepeatCount(Integer repeatCount) {
		this.repeatCount = repeatCount;
	}
	public String getWarmingCycle() {
		return warmingCycle;
	}
	public void setWarmingCycle(String warmingCycle) {
		this.warmingCycle = warmingCycle;
	}
	public String getWarmingMan() {
		return warmingMan;
	}
	public void setWarmingMan(String warmingMan) {
		this.warmingMan = warmingMan;
	}
	public String getWarmingManLogin() {
		return warmingManLogin;
	}
	public void setWarmingManLogin(String warmingManLogin) {
		this.warmingManLogin = warmingManLogin;
	}		
}

package com.ambition.spc.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:PPK规则表(com.ambition.spc.entity)
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2016年7月5日 发布
 */
@Entity
@Table(name = "SPC_PPK_RULE")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})//台账修改保存需要这句话
public class PpkRule extends IdEntity {
	private static final long serialVersionUID = 1L;
	/** FPA目标值(Service) */
	public static final String ENTITY_LIST_CODE = "SPC_PPK_RULE";//实体_列表_编码
	
	private String name;//规则名称(名称不能重复)
	private Double upLimit;//上限(上限和下限不能同时为空)
	private Double belowLimit;//下限(上限和下限不能同时为空)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getUpLimit() {
		return upLimit;
	}
	public void setUpLimit(Double upLimit) {
		this.upLimit = upLimit;
	}
	public Double getBelowLimit() {
		return belowLimit;
	}
	public void setBelowLimit(Double belowLimit) {
		this.belowLimit = belowLimit;
	}
	/**
	 * @return the entityListCode
	 */
	public static String getEntityListCode() {
		return ENTITY_LIST_CODE;
	}
}

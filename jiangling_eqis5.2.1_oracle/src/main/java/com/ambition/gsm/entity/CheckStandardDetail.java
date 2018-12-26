package com.ambition.gsm.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 类名:检验项目明细
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016-11-24 发布
 */
@Entity
@Table(name = "GSM_CHECK_STANDARD_DETAIL")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class CheckStandardDetail  extends IdEntity {

	private static final long serialVersionUID = 1L;
	private String itemName;//项目名称
	private String standardValue;//标准值
	private String allowableError;//允许误差		
    @ManyToOne
	@JoinColumn(name = "FK_CHECK_STANDARD_ID")
    private CheckStandard checkStandard;//检验标准
    @ManyToOne
	@JoinColumn(name = "FK_CHECK_ITEM_ID")
    private GsmCheckItem checkItem;//设备信息
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getStandardValue() {
		return standardValue;
	}
	public void setStandardValue(String standardValue) {
		this.standardValue = standardValue;
	}
	public String getAllowableError() {
		return allowableError;
	}
	public void setAllowableError(String allowableError) {
		this.allowableError = allowableError;
	}
	public CheckStandard getCheckStandard() {
		return checkStandard;
	}
	public void setCheckStandard(CheckStandard checkStandard) {
		this.checkStandard = checkStandard;
	}
	public GsmCheckItem getCheckItem() {
		return checkItem;
	}
	public void setCheckItem(GsmCheckItem checkItem) {
		this.checkItem = checkItem;
	}

	
    
    
}
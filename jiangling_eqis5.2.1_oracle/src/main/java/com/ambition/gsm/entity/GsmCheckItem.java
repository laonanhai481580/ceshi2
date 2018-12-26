package com.ambition.gsm.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 类名:检验项目维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016-11-24 发布
 */
@Entity
@Table(name = "GSM_CHECK_ITEM")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class GsmCheckItem  extends IdEntity {

	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "GSM_CHECK_ITEM";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "检验项目";//实体_列表_名称
	private String itemName;//项目名称
	private String standardValue;//标准值
	private String allowableError;//允许误差	
    @JsonIgnore
    @OneToMany(mappedBy="checkItem",cascade=javax.persistence.CascadeType.REMOVE)
    private List<CheckStandardDetail> checkStandardDetail;
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
	public List<CheckStandardDetail> getCheckStandardDetail() {
		return checkStandardDetail;
	}
	public void setCheckStandardDetail(List<CheckStandardDetail> checkStandardDetail) {
		this.checkStandardDetail = checkStandardDetail;
	}
	
	
    
    
}

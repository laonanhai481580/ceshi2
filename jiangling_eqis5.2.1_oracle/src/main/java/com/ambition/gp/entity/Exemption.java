package com.ambition.gp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:豁免清单
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  xjn
 * @version 1.00 2017年11月10日 发布
 */

@Entity
@Table(name="GP_EXEMPTION")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Exemption extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String exemptionName;//豁免名字
	private String exemptionCode;//豁免编码
	private String element;//元素
	private String text;//
	private String remark;//备注
	private String sortData;//排序
	
	public String getExemptionName() {
		return exemptionName;
	}
	public void setExemptionName(String exemptionName) {
		this.exemptionName = exemptionName;
	}
	public String getExemptionCode() {
		return exemptionCode;
	}
	public void setExemptionCode(String exemptionCode) {
		this.exemptionCode = exemptionCode;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public String getSortData() {
		return sortData;
	}
	public void setSortData(String sortData) {
		this.sortData = sortData;
	}
	
}

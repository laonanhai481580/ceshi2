package com.ambition.epm.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * ORT测试标准维护
 * @author lpf
 *
 */
@Entity
@Table(name="EPM_ORT_INDICATOR")//EPM_ORT_INDICATOR
public class EpmOrtIndicator extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String customerNo;//客户编号
	private String model;//机种		
	private String modelName;//机种名称
	private String samplType;//样品类型
    
    @OneToMany(mappedBy="ortIndicator",cascade=javax.persistence.CascadeType.REMOVE)
    private List<EpmOrtItem> epmOrtItems;//ORT测试项目

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getSamplType() {
		return samplType;
	}

	public void setSamplType(String samplType) {
		this.samplType = samplType;
	}

	public List<EpmOrtItem> getEpmOrtItems() {
		return epmOrtItems;
	}

	public void setEpmOrtItems(List<EpmOrtItem> epmOrtItems) {
		this.epmOrtItems = epmOrtItems;
	}

}

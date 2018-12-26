package com.ambition.aftersales.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:客户场地
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月22日 发布
 */
@Entity
@Table(name = "AFS_CUSTOMER_PLACE")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class CustomerPlace extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String customerPlace;//客户场地
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "FK_CUSTOMER_LIST_NO")
	private CustomerList customerList;//客户清单	
	public CustomerList getCustomerList() {
		return customerList;
	}
	public void setCustomerList(CustomerList customerList) {
		this.customerList = customerList;
	}
	public String getCustomerPlace() {
		return customerPlace;
	}
	public void setCustomerPlace(String customerPlace) {
		this.customerPlace = customerPlace;
	}
	
	
}

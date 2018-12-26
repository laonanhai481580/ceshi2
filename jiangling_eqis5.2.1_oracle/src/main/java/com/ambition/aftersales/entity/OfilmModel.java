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
 * 类名:欧菲机种
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月22日 发布
 */
@Entity
@Table(name = "AFS_OFILM_MODEL")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class OfilmModel extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String ofilmModel;//欧菲机种
	private String customerModel;//客户端机种
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "FK_CUSTOMER_LIST_NO")
	private CustomerList customerList;//客户清单	
	public String getOfilmModel() {
		return ofilmModel;
	}
	public void setOfilmModel(String ofilmModel) {
		this.ofilmModel = ofilmModel;
	}
	public String getCustomerModel() {
		return customerModel;
	}
	public void setCustomerModel(String customerModel) {
		this.customerModel = customerModel;
	}
	public CustomerList getCustomerList() {
		return customerList;
	}
	public void setCustomerList(CustomerList customerList) {
		this.customerList = customerList;
	}
	
	
}

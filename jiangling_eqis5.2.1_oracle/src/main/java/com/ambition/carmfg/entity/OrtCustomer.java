package com.ambition.carmfg.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:客户信息维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2017年3月22日 发布
 */
@Entity
@Table(name = "MFG_ORT_CUSTOMER")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class OrtCustomer extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String processSection; // 制程区段
	private String customerNo;//客户编码
	private String customerName;//客户名称
	@OneToMany(mappedBy="ortCustomer")
	@OrderBy("testItem")
	List<OrtInspectionItem> items;//ORT检验项目
	//Get、Set方法
	public String getProcessSection() {
		return processSection;
	}
	public void setProcessSection(String processSection) {
		this.processSection = processSection;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public List<OrtInspectionItem> getItems() {
		return items;
	}
	public void setItems(List<OrtInspectionItem> items) {
		this.items = items;
	}	
	
}

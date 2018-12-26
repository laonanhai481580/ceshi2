package com.ambition.aftersales.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:客户清单
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月22日 发布
 */
@Entity
@Table(name = "AFS_CUSTOMER_LIST")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class CustomerList extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String customerNo;//客户代码
	private String customerName;//客户名称
	private String linkMan;//联系人
	private String linkPhone;//联系电话
	@OneToMany(mappedBy="customerList",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @JsonIgnore
    private List<OfilmModel> ofilmModels;//欧菲机种
	@OneToMany(mappedBy="customerList",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @JsonIgnore
    private List<CustomerPlace> customerPlaces;//欧菲机种
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
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getLinkPhone() {
		return linkPhone;
	}
	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}
	public List<OfilmModel> getOfilmModels() {
		return ofilmModels;
	}
	public void setOfilmModels(List<OfilmModel> ofilmModels) {
		this.ofilmModels = ofilmModels;
	}
	
	
}

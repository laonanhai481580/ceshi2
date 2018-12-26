package com.ambition.aftersales.entity;

import java.util.Date;
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
 * 类名:OBA明细表
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Entity
@Table(name = "AFS_OBA_DATA")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class ObaData extends IdEntity{

	private static final long serialVersionUID = 1L;
	//private String businessUnit;//事业部
	private Date obaDate;//日期
	private String place;//	场地
	private String customerModel;//客户机型
	private String ofilmModel;//欧菲机型
	private String customerName;//客户名称
	private String productionEnterpriseGroup;//生产事业群
	private String productStructure;//产品结构
	private Date produceDate;//生产日期
	private Integer inputCount;//投入数
	private Integer unqualifiedCount;//不良数
	private String unqualifiedRate;//不良率
	
	@OneToMany(mappedBy = "obaData",cascade={CascadeType.ALL})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	List<ObaDefectiveItem> obaDefectiveItems;//不良明细


	public Date getObaDate() {
		return obaDate;
	}

	public void setObaDate(Date obaDate) {
		this.obaDate = obaDate;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getCustomerModel() {
		return customerModel;
	}

	public void setCustomerModel(String customerModel) {
		this.customerModel = customerModel;
	}

	public String getOfilmModel() {
		return ofilmModel;
	}

	public void setOfilmModel(String ofilmModel) {
		this.ofilmModel = ofilmModel;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getProductionEnterpriseGroup() {
		return productionEnterpriseGroup;
	}

	public void setProductionEnterpriseGroup(String productionEnterpriseGroup) {
		this.productionEnterpriseGroup = productionEnterpriseGroup;
	}

	public String getProductStructure() {
		return productStructure;
	}

	public void setProductStructure(String productStructure) {
		this.productStructure = productStructure;
	}


	public Date getProduceDate() {
		return produceDate;
	}

	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
	}

	public Integer getInputCount() {
		return inputCount;
	}

	public void setInputCount(Integer inputCount) {
		this.inputCount = inputCount;
	}

	public Integer getUnqualifiedCount() {
		return unqualifiedCount;
	}

	public void setUnqualifiedCount(Integer unqualifiedCount) {
		this.unqualifiedCount = unqualifiedCount;
	}

	public String getUnqualifiedRate() {
		return unqualifiedRate;
	}

	public void setUnqualifiedRate(String unqualifiedRate) {
		this.unqualifiedRate = unqualifiedRate;
	}

	public List<ObaDefectiveItem> getObaDefectiveItems() {
		return obaDefectiveItems;
	}

	public void setObaDefectiveItems(List<ObaDefectiveItem> obaDefectiveItems) {
		this.obaDefectiveItems = obaDefectiveItems;
	}

	
	
}

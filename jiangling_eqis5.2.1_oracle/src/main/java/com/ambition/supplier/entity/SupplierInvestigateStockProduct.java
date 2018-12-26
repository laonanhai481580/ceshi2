package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.ambition.product.base.IdEntity;
import com.ambition.supplier.utils.DateUtil;

/**
 * 类名:供应商调查表现有产品
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2013-4-28 发布
 */
@Entity
@Table(name = "SUPPLIER_INVESTIGATE_STOCK_PRODUCT")
public class SupplierInvestigateStockProduct extends IdEntity {
	private static final long serialVersionUID = 4518308827240189057L;
	@ManyToOne
	@JoinColumn(name = "FK_SUPPLIER_INVESTIGATE_ID")
	private SupplierInvestigate supplierInvestigate;
	private String productCode;//产品编号
	private String productName;//产品名称
	private String technics;//工艺
	private String auxiliaryProduct;//配套产品
	private Integer auxiliaryProductNum;//配套产品数量
	@Temporal(TemporalType.DATE)
	private Date auxiliaryDate;//正式配套起始日期
	private String productCapacity;//生产能力
	public SupplierInvestigate getSupplierInvestigate() {
		return supplierInvestigate;
	}
	public void setSupplierInvestigate(SupplierInvestigate supplierInvestigate) {
		this.supplierInvestigate = supplierInvestigate;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getTechnics() {
		return technics;
	}
	public void setTechnics(String technics) {
		this.technics = technics;
	}
	public String getAuxiliaryProduct() {
		return auxiliaryProduct;
	}
	public void setAuxiliaryProduct(String auxiliaryProduct) {
		this.auxiliaryProduct = auxiliaryProduct;
	}
	public Integer getAuxiliaryProductNum() {
		return auxiliaryProductNum;
	}
	public void setAuxiliaryProductNum(Integer auxiliaryProductNum) {
		this.auxiliaryProductNum = auxiliaryProductNum;
	}
	public Date getAuxiliaryDate() {
		return auxiliaryDate;
	}
	@Transient
	public String getAuxiliaryDateStr() {
		return DateUtil.formateDateStr(auxiliaryDate);
	}
	public void setAuxiliaryDate(Date auxiliaryDate) {
		this.auxiliaryDate = auxiliaryDate;
	}
	public String getProductCapacity() {
		return productCapacity;
	}
	public void setProductCapacity(String productCapacity) {
		this.productCapacity = productCapacity;
	}
}

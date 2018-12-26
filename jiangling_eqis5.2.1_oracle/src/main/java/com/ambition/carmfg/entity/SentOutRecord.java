package com.ambition.carmfg.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
/**
 * 类名:发料记录
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2016年11月14日 发布
 */
@Entity
@Table(name = "MFG_SENT_RECORD")
public class SentOutRecord extends IdEntity{

		/**
		  *SentOutRecord.java2016年11月14日
		 */
	private static final long serialVersionUID = 1L;
	private Long transactionId;//交易记录 id 唯一
	private Long organizationId;//组织 id
	private Date transactionDate;//交易日期
	private Long itemId;//物料 id
    private String itemNumber;//物料编号
    private String itemDescription;//物料描述
    private String uom;//物料单位
    private Double primaryQuantity;//事务处理数量
    private String subinventoryCode;//交易子库
    private Long transactionTypeId;//交易类型id
    private String trxTypeName;//交易类型名称
    private String cateogryCode;//物料分类代码
    private String categoryDescription;//物料分类描述
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	
	public Double getPrimaryQuantity() {
		return primaryQuantity;
	}
	public void setPrimaryQuantity(Double primaryQuantity) {
		this.primaryQuantity = primaryQuantity;
	}
	public String getSubinventoryCode() {
		return subinventoryCode;
	}
	public void setSubinventoryCode(String subinventoryCode) {
		this.subinventoryCode = subinventoryCode;
	}
	public Long getTransactionTypeId() {
		return transactionTypeId;
	}
	public void setTransactionTypeId(Long transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}
	public String getTrxTypeName() {
		return trxTypeName;
	}
	public void setTrxTypeName(String trxTypeName) {
		this.trxTypeName = trxTypeName;
	}
	public String getCateogryCode() {
		return cateogryCode;
	}
	public void setCateogryCode(String cateogryCode) {
		this.cateogryCode = cateogryCode;
	}
	public String getCategoryDescription() {
		return categoryDescription;
	}
	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}
    
    
}

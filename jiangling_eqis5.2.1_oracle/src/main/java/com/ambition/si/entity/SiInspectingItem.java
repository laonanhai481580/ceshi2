package com.ambition.si.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.norteksoft.wf.engine.client.ExtendField;

/**
 * 供应商检验检验项目
 * @author lpf
 *
 */
@Entity
@Table(name = "SI_INSPECTING_ITEM")
public class SiInspectingItem extends IdEntity {
	public static String COUNTTYPE_COUNT = "计数";
	public static String COUNTTYPE_METERING = "计量";
	private static final long serialVersionUID = 1L;
	@Column(length=2500)
	private String itemName;//检验项目名称
	@Column(length=1000)
	private String method;//检验方法
	@Column(length=500)
	private String specifications;//规格
	private String countType = COUNTTYPE_COUNT;//统计类型
	private String unit;//单位
	private String attachmentFiles;//附件
    private String remark;//备注
    private Integer itemLevel = 1;//标识

    private Integer orderNum=0;//排序
    
    @Embedded
   	private ExtendField extendField;
    
    @ManyToOne
	@JoinColumn(name = "FK_PARENT_ID")
    private SiInspectingItem itemParent;//上级项目
    
    @OneToMany(mappedBy="itemParent",cascade=javax.persistence.CascadeType.ALL)
    @Cascade(value=CascadeType.DELETE_ORPHAN)
    private List<SiInspectingItem> itemChildren;//子级项目
    
    @JsonIgnore
    @OneToMany(mappedBy="siInspectingItem",cascade=javax.persistence.CascadeType.REMOVE)
    private List<SiItemIndicator> siItemIndicators;
    


	
	
	
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getCountType() {
		return countType;
	}

	public void setCountType(String countType) {
		this.countType = countType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getAttachmentFiles() {
		return attachmentFiles;
	}

	public void setAttachmentFiles(String attachmentFiles) {
		this.attachmentFiles = attachmentFiles;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getItemLevel() {
		return itemLevel;
	}

	public void setItemLevel(Integer itemLevel) {
		this.itemLevel = itemLevel;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public ExtendField getExtendField() {
		return extendField;
	}

	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}

	public SiInspectingItem getItemParent() {
		return itemParent;
	}

	public void setItemParent(SiInspectingItem itemParent) {
		this.itemParent = itemParent;
	}

	public List<SiInspectingItem> getItemChildren() {
		return itemChildren;
	}

	public void setItemChildren(List<SiInspectingItem> itemChildren) {
		this.itemChildren = itemChildren;
	}

	public List<SiItemIndicator> getSiItemIndicators() {
		return siItemIndicators;
	}

	public void setSiItemIndicators(List<SiItemIndicator> siItemIndicators) {
		this.siItemIndicators = siItemIndicators;
	}

	@JsonIgnore
	@Transient
	public SiInspectingItem getTopParent(){
		if(itemParent == null){
			return this;
		}else{
			return itemParent.getTopParent();
		}
	}
	
	public String toString(){
		return "供应商检验检标准维护：检验项目维护    名称"+this.itemName +"  方法"+this.method;
	}
}

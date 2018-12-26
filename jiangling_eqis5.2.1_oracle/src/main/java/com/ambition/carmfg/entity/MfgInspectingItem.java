package com.ambition.carmfg.entity;

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
 * 制造过程检验项目
 * @author 赵骏
 *
 */
@Entity
@Table(name = "MFG_INSPECTING_ITEM")
public class MfgInspectingItem extends IdEntity {
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
    private MfgInspectingItem itemParent;//上级项目
    
    @OneToMany(mappedBy="itemParent",cascade=javax.persistence.CascadeType.ALL)
    @Cascade(value=CascadeType.DELETE_ORPHAN)
    private List<MfgInspectingItem> itemChildren;//子级项目
    
    @JsonIgnore
    @OneToMany(mappedBy="mfgInspectingItem",cascade=javax.persistence.CascadeType.REMOVE)
    private List<MfgItemIndicator> mfgItemIndicators;
    
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
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

	public String getSpecifications() {
		return specifications;
	}
	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}


	
	public List<MfgInspectingItem> getItemChildren() {
		return itemChildren;
	}
	public void setItemChildren(List<MfgInspectingItem> itemChildren) {
		this.itemChildren = itemChildren;
	}
	public List<MfgItemIndicator> getMfgItemIndicators() {
		return mfgItemIndicators;
	}
	public void setMfgItemIndicators(List<MfgItemIndicator> mfgItemIndicators) {
		this.mfgItemIndicators = mfgItemIndicators;
	}
	@Transient
	public Double getTotalPoints(){
		double val = 0.0;
		for(MfgItemIndicator itemIndicator : mfgItemIndicators){
			if(!itemIndicator.getMfgInspectingItem().getItemChildren().isEmpty()
				||itemIndicator.getTotalPoints() == null
				||itemIndicator.getTotalPoints() <= 0){
				continue;
			}
			val += itemIndicator.getTotalPoints();
		}
		return val;
	}
	public String getCountType() {
		return countType;
	}
	public void setCountType(String countType) {
		this.countType = countType;
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
	
	@JsonIgnore
	@Transient
	public MfgInspectingItem getTopParent(){
		if(itemParent == null){
			return this;
		}else{
			return itemParent.getTopParent();
		}
	}
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public MfgInspectingItem getItemParent() {
		return itemParent;
	}
	public void setItemParent(MfgInspectingItem itemParent) {
		this.itemParent = itemParent;
	}
	public String getAttachmentFiles() {
		return attachmentFiles;
	}
	public void setAttachmentFiles(String attachmentFiles) {
		this.attachmentFiles = attachmentFiles;
	}
	public String toString(){
		return "制造检验标准维护：检验项目维护    名称"+this.itemName +"  方法"+this.method;
	}
}

package com.ambition.iqc.entity;

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
 * InspectionItem.java
 * @authorBy YUKE
 *
 */
@Entity
@Table(name = "IQC_INSPECTING_ITEM")
public class InspectingItem extends IdEntity {
	public static String COUNTTYPE_COUNT = "计数";
	public static String COUNTTYPE_METERING = "计量";
	private static final long serialVersionUID = 1L;
	@Column(length=1000)
	private String itemName;//检验项目名称
	private String itemMethod;//检验方法
	private String standards;//规格
	private String classification;//缺陷等级
	private String countType = COUNTTYPE_COUNT;//统计类型
	private String unit;//单位
    private String remark;//备注
    private Integer itemLevel = 1;//标识
    private Long parentInspectingItemId;//顶级项目编号
    private Integer orderNum=0;//排序
    
    @Embedded
   	private ExtendField extendField;
    
    @ManyToOne
	@JoinColumn(name = "FK_PARENT_ID")
    private InspectingItem itemParent;//上级项目
    
    @OneToMany(mappedBy="itemParent",cascade=javax.persistence.CascadeType.ALL)
    @Cascade(value=CascadeType.DELETE_ORPHAN)
    private List<InspectingItem> itemChildren;//子级项目
    
    @JsonIgnore
    @OneToMany(mappedBy="inspectingItem",cascade=javax.persistence.CascadeType.REMOVE)
    private List<ItemIndicator> itemIndicators;
	public String getStandards() {
		return standards;
	}
	public void setStandards(String standards) {
		this.standards = standards;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Long getParentInspectingItemId() {
		return parentInspectingItemId;
	}
	public void setParentInspectingItemId(Long parentInspectingItemId) {
		this.parentInspectingItemId = parentInspectingItemId;
	}
	
	
	
	public InspectingItem getItemParent() {
		return itemParent;
	}
	public void setItemParent(InspectingItem itemParent) {
		this.itemParent = itemParent;
	}
	public List<InspectingItem> getItemChildren() {
		return itemChildren;
	}
	public void setItemChildren(List<InspectingItem> itemChildren) {
		this.itemChildren = itemChildren;
	}
	public List<ItemIndicator> getItemIndicators() {
		return itemIndicators;
	}
	public void setItemIndicators(List<ItemIndicator> itemIndicators) {
		this.itemIndicators = itemIndicators;
	}
	@Transient
	public Double getTotalPoints(){
		double val = 0.0;
		for(ItemIndicator itemIndicator : itemIndicators){
			if(!itemIndicator.getInspectingItem().getItemChildren().isEmpty()
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
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemMethod() {
		return itemMethod;
	}
	public void setItemMethod(String itemMethod) {
		this.itemMethod = itemMethod;
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
	@JsonIgnore
	@Transient
	public InspectingItem getTopParent(){
		if(itemParent == null){
			return this;
		}else{
			return itemParent.getTopParent();
		}
	}
	
	public String toString(){
		return "物料检验标准维护：检验项目维护    名称"+this.itemName +"  方法"+this.itemMethod;
	}
}

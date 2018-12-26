package com.ambition.spc.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;

/**    
 * LayerType.java
 * @authorBy wanglf
 *
 */
@Entity
@Table(name = "SPC_LAYER_TYPE")
public class LayerType extends IdEntity {

	/**
	 * 层别类别
	 */
	private static final long serialVersionUID = 1L;
	private String typeCode;//类别编号
	private String typeName;//类别名称
	private String isInputValue;//是否入入取值
	private String isAutoCopy;//是否自动复制
	private String sampleMethod;//取值方式,自由录入--0,自由录入并加入列表--1,从列表中选择--2
	private Boolean hasChild = false;//是否有子节点,程序维护
	@Column(name="myLevel")
	private Integer level = 1;//标识
    private Integer orderNum = 0;//排序
    private String parentIds;//父级的所有ID,以逗号开始,逗号结束
    @ManyToOne
	@JoinColumn(name = "FK_PARENT_ID")
    private LayerType parent;//上级物料
    @OneToMany(mappedBy="parent")
    @Cascade(value=org.hibernate.annotations.CascadeType.DELETE)
    @OrderBy("typeCode")
    private List<LayerType> children;//子级物料
    @OneToMany(mappedBy="layerType",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<LayerDetail> layerDetails;//质量特性

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Boolean getHasChild() {
		return hasChild;
	}

	public void setHasChild(Boolean hasChild) {
		this.hasChild = hasChild;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public LayerType getParent() {
		return parent;
	}

	public void setParent(LayerType parent) {
		this.parent = parent;
	}

	public List<LayerType> getChildren() {
		return children;
	}

	public void setChildren(List<LayerType> children) {
		this.children = children;
	}


	public List<LayerDetail> getLayerDetails() {
		return layerDetails;
	}

	public void setLayerDetails(List<LayerDetail> layerDetails) {
		this.layerDetails = layerDetails;
	}

	public String toString() {
		return "SPC：层别类别    ID"+this.getId()+",类别编号"+this.typeCode;
	}

	public String getIsInputValue() {
		return isInputValue;
	}

	public void setIsInputValue(String isInputValue) {
		this.isInputValue = isInputValue;
	}

	public String getIsAutoCopy() {
		return isAutoCopy;
	}

	public void setIsAutoCopy(String isAutoCopy) {
		this.isAutoCopy = isAutoCopy;
	}

	public String getSampleMethod() {
		return sampleMethod;
	}

	public void setSampleMethod(String sampleMethod) {
		this.sampleMethod = sampleMethod;
	}

}

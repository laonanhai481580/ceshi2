package com.ambition.spc.entity;

import java.util.Date;
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
 * ProcessPoint.java
 * @authorBy YUKE
 *
 */
@Entity
@Table(name = "SPC_PROCESS_POINT")
public class ProcessPoint extends IdEntity {

	/**
	 * 产品过程节点定义
	 */
	private static final long serialVersionUID = 1L;
	private String code;//产品编号
	private String name;//产品名称
	private String editer;//编辑人
	private Date editDate;//编辑日期
	private String remark;//备注
	private Boolean hasChild = false;//是否有子节点,程序维护
	@Column(name="myLevel")
	private Integer level = 1;//标识
    private Integer orderNum = 0;//排序
    private String parentIds;//父级的所有ID,以逗号开始,逗号结束
    
    @ManyToOne
	@JoinColumn(name = "FK_PARENT_ID")
    private ProcessPoint parent;//上级物料
    
    @OneToMany(mappedBy="parent")
    @Cascade(value=org.hibernate.annotations.CascadeType.DELETE)
    @OrderBy("code")
    private List<ProcessPoint> children;//子级物料
    
    @OneToMany(mappedBy="processPoint",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OrderBy("orderNum")
	private List<QualityFeature> qualityFeatures;//质量特性
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEditer() {
		return editer;
	}

	public void setEditer(String editer) {
		this.editer = editer;
	}

	public Date getEditDate() {
		return editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public ProcessPoint getParent() {
		return parent;
	}

	public void setParent(ProcessPoint parent) {
		this.parent = parent;
	}

	public List<ProcessPoint> getChildren() {
		return children;
	}

	public void setChildren(List<ProcessPoint> children) {
		this.children = children;
	}

	public List<QualityFeature> getQualityFeatures() {
		return qualityFeatures;
	}

	public void setQualityFeatures(List<QualityFeature> qualityFeatures) {
		this.qualityFeatures = qualityFeatures;
	}

	public String toString() {
		return "SPC：过程节点定义    ID"+this.getId()+",产品编号"+this.code;
	}
	
}

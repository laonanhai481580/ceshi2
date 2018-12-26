package com.ambition.cost.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 质量成本构成表
 * @author ambition-zhaojun
 *
 */
@Entity
@Table(name = "COST_COMPOSING")
public class Composing extends IdEntity {
	private static final long serialVersionUID = -9181689261077768159L;
	private String code;//编码
	private String name;//名称
	private String unit;//单位
	private Integer dengji;//级别
	private String checkDepartment;//核算部门
	private String cooperateDepartment;//配合部门
	private String remark;//科目备注
	private Integer orderNum=0;//排序号
	
	@ManyToOne
	@JoinColumn(name="FK_COMPOSING_ID")
	private Composing parent;//父级
	
	@OneToMany(targetEntity=Composing.class,cascade=CascadeType.ALL,mappedBy="parent")
	@OrderBy(value="orderNum")
	private List<Composing> children;//子结点

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

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



	public Integer getDengji() {
		return dengji;
	}

	public void setDengji(Integer dengji) {
		this.dengji = dengji;
	}

	public String getCheckDepartment() {
		return checkDepartment;
	}

	public void setCheckDepartment(String checkDepartment) {
		this.checkDepartment = checkDepartment;
	}

	public String getCooperateDepartment() {
		return cooperateDepartment;
	}

	public void setCooperateDepartment(String cooperateDepartment) {
		this.cooperateDepartment = cooperateDepartment;
	}

	public Composing getParent() {
		return parent;
	}

	public void setParent(Composing parent) {
		this.parent = parent;
	}

	public List<Composing> getChildren() {
		return children;
	}

	public void setChildren(List<Composing> children) {
		this.children = children;
	}
	
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String toString() {
		return "质量成本管理：质量成本  ID"+this.getId()+",编码"+this.code+",科目"+this.name;
	}
}

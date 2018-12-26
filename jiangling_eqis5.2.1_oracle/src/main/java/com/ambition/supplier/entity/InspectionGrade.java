package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 评分项目
 * @author 赵骏
 *
 */
@Entity
@Table(name = "SUPPLIER_INSPECTION_GRADE")
public class InspectionGrade  extends IdEntity {
	private static final long serialVersionUID = 2339315409920892496L;
	private String name; //项目名称
    private Double weight;//分数
    private Integer orderNum = 0;//排序
	private Boolean isSelect = false;//是否选中
	
    @ManyToOne
   	@JoinColumn(name = "FK_INSPECTION_GRADE_TYPE_ID")
    private InspectionGradeType inspectionGradeType;//供应商考察评分类型
    
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	
	public Boolean getIsSelect() {
		return isSelect;
	}
	public void setIsSelect(Boolean isSelect) {
		this.isSelect = isSelect;
	}
	public InspectionGradeType getInspectionGradeType() {
		return inspectionGradeType;
	}
	public void setInspectionGradeType(InspectionGradeType inspectionGradeType) {
		this.inspectionGradeType = inspectionGradeType;
	}
	public String toString(){
		return "供应商质量管理：考察评分项目    名称"+this.name+"，分数"+this.weight;
	}
}

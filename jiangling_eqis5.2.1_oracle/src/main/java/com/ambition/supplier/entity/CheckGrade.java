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
@Table(name = "SUPPLIER_CHECK_GRADE")
public class CheckGrade  extends IdEntity {
	private static final long serialVersionUID = 2339315409920892496L;
	private String name; //项目名称
    private Double weight;//权重
    private String type = CheckGradeType.TYPE_CHECK; //类型
    private Integer orderNum = 0;//排序
    
    @ManyToOne
   	@JoinColumn(name = "FK_CHECK_GRADE_TYPE_ID")
    private CheckGradeType checkGradeType;//对应的评分项目类型
    
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
	public CheckGradeType getCheckGradeType() {
		return checkGradeType;
	}
	public void setCheckGradeType(CheckGradeType checkGradeType) {
		this.checkGradeType = checkGradeType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String toString(){
		return "供应商质量管理：评分项目，项目名称"+this.name+"，类型"+this.type;
	}
}

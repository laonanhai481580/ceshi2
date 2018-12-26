package com.ambition.supplier.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ambition.product.base.IdEntity;

/**
 * 评分项目类型
 * @author 赵骏
 *
 */
@Entity
@Table(name = "SUPPLIER_CHECK_GRADE_TYPE")
public class CheckGradeType extends IdEntity {
	public static String TYPE_CHECK = "check"; //稽查评分
	public static String TYPE_SURVEY = "survey";
	
	private static final long serialVersionUID = 1L;
	private String name; //类型名称
	private String type = CheckGradeType.TYPE_CHECK;
	private Double totalFee = 0.0;//总分
	private Double weight;//权重
    @ManyToOne
   	@JoinColumn(name = "FK_PARENT_ID")
    private CheckGradeType parent;//父类
    
    @OneToMany(mappedBy="parent")
    @Cascade(value=CascadeType.DELETE)
    @OrderBy("orderNum asc")
    private List<CheckGradeType> children;//子级
    
    private Integer level = 1;//等级,默认为1
    private Integer orderNum = 0;//排序
    
    @OneToMany(mappedBy="checkGradeType")
    @Cascade(value=CascadeType.DELETE)
    @OrderBy("orderNum asc")
    private List<CheckGrade> checkGrades;//关联的稽查评分项目集合

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CheckGradeType getParent() {
		return parent;
	}

	public void setParent(CheckGradeType parent) {
		this.parent = parent;
	}

	public List<CheckGradeType> getChildren() {
		return children;
	}

	public void setChildren(List<CheckGradeType> children) {
		this.children = children;
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

	public List<CheckGrade> getCheckGrades() {
		return checkGrades;
	}

	public void setCheckGrades(List<CheckGrade> checkGrades) {
		this.checkGrades = checkGrades;
	}

	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}
	
	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String toString(){
		return "供应商质量管理：评分项目类型，类型名称"+this.name+"，类型"+this.type;
	}
}

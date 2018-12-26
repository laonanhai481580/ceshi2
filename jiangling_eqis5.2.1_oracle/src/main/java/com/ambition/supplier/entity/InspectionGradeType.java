package com.ambition.supplier.entity;

import java.util.ArrayList;
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
 * 考察评价评分类型
 * @author 赵骏
 *
 */
@Entity
@Table(name = "SUPPLIER_INSPECTION_GRADE_TYPE")
public class InspectionGradeType extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String name; //类型名称
	private Double weight;//权重
	private Double totalFee = 0.0;//总分
	private Double realFee = 0.0;//实际得分
	private String remark;//备注
	private String reviewer;//评审人员
	private String reviewerLoginName;//评审人员登录名
	private String reviewers;//所有的评审人员
    @ManyToOne
   	@JoinColumn(name = "FK_PARENT_ID")
    private InspectionGradeType parent;//父类
    
    @OneToMany(mappedBy="parent")
    @Cascade(value=CascadeType.DELETE)
    @OrderBy("orderNum asc")
    private List<InspectionGradeType> children;//子级
    private Integer level = 1;//等级,默认为1
    
    private Integer orderNum = 0;//排序
   
    @OneToMany(mappedBy="inspectionGradeType")
    @Cascade(value=CascadeType.DELETE)
    @OrderBy("orderNum asc")
    private List<InspectionGrade> inspectionGrades;//关联的考察评分项目

    @ManyToOne
   	@JoinColumn(name = "FK_INSPECTION_REPORT_ID")
    private InspectionReport inspectionReport;//考察评分项
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InspectionGradeType getParent() {
		return parent;
	}

	public void setParent(InspectionGradeType parent) {
		this.parent = parent;
	}

	public List<InspectionGradeType> getChildren() {
		return children;
	}

	public List<InspectionGradeType> getAllChildren(){
		List<InspectionGradeType> inspectionGradeTypes = new ArrayList<InspectionGradeType>();
		inspectionGradeTypes.addAll(children);
		for(InspectionGradeType child : children){
			inspectionGradeTypes.addAll(child.getAllChildren());
		}
		return inspectionGradeTypes;
	}
	
	public void setChildren(List<InspectionGradeType> children) {
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

	public List<InspectionGrade> getInspectionGrades() {
		return inspectionGrades;
	}

	public void setInspectionGrades(List<InspectionGrade> inspectionGrades) {
		this.inspectionGrades = inspectionGrades;
	}

	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public Double getRealFee() {
		return realFee;
	}

	public void setRealFee(Double realFee) {
		this.realFee = realFee;
	}

	public InspectionReport getInspectionReport() {
		return inspectionReport;
	}

	public void setInspectionReport(InspectionReport inspectionReport) {
		this.inspectionReport = inspectionReport;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getReviewer() {
		return reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public String getReviewerLoginName() {
		return reviewerLoginName;
	}

	public void setReviewerLoginName(String reviewerLoginName) {
		this.reviewerLoginName = reviewerLoginName;
	}
	
	public String getReviewers() {
		return reviewers;
	}

	public void setReviewers(String reviewers) {
		this.reviewers = reviewers;
	}

	public Double getAllRealFee(){
		if(children.isEmpty()){
			return realFee;
		}else{
			double val = 0.0;
			for(InspectionGradeType child : children){
				Double fee = child.getAllRealFee();
				if(fee != null){
					val += fee;
				}
			}
			return val;
		}
	}
	public String toString(){
		return "供应商质量管理：考察评价评分类型    名称"+this.name+"，实际得分"+this.realFee;
	}
}

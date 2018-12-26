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
@Table(name = "SUPPLIER_REPORT_GRADE")
public class ReportGrade  extends IdEntity {
	private static final long serialVersionUID = 2339315409920892496L;
	private String topTdHtml;//前两格单元格的值
	private String parentName;//上级名称
	private Long parentId;//上级名称
	private String name; //项目名称
    private Double weight;//权重
    private String type = CheckGradeType.TYPE_CHECK; //类型
    private Integer orderNum = 0;//排序
	private Double realFee = 0.0;//实际得分
	private String problem;//问题
	private String remark;//备注
    
    @ManyToOne
   	@JoinColumn(name = "FK_CHECK_REPORT_ID")
    private CheckReport checkReport;//对应的评分表
    
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public CheckReport getCheckReport() {
		return checkReport;
	}
	public void setCheckReport(CheckReport checkReport) {
		this.checkReport = checkReport;
	}
	public Double getRealFee() {
		return realFee;
	}
	public void setRealFee(Double realFee) {
		this.realFee = realFee;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getProblem() {
		return problem;
	}
	public void setProblem(String problem) {
		this.problem = problem;
	}
	public String getTopTdHtml() {
		return topTdHtml;
	}
	public void setTopTdHtml(String topTdHtml) {
		this.topTdHtml = topTdHtml;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String toString(){
		return "供应商质量管理：评分项目    项目名称"+this.name+"，实际得分"+this.realFee;
	}
}

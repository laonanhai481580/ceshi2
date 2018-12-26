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
 * 监察报告
 * @author 王龙峰
 *
 */
@Entity
@Table(name = "SUPPLIER_REPORT_INPUT")
public class ReportInput extends IdEntity {
	public static String TYPE_CHECK = "check"; //稽查评分
	public static String TYPE_SURVEY = "survey";
	
	private static final long serialVersionUID = 1L;
	private String type = ReportInput.TYPE_CHECK;
	private Double totalFee = 0.0;//总分
	private Double realFee = 0.0;//实际得分
	private String problem;//问题
	private String name;//问题
    @ManyToOne
   	@JoinColumn(name = "FK_PARENT_ID")
    private ReportInput parent;//父类
    
    @OneToMany(mappedBy="parent")
    @Cascade(value=CascadeType.DELETE)
    @OrderBy("orderNum asc")
    private List<ReportInput> children;//子级
    private Integer level = 1;//等级,默认为1
    
    private Integer orderNum = 0;//排序
   
    @ManyToOne
   	@JoinColumn(name = "FK_CHECK_REPORT_ID")
    private CheckReport checkReport;//对应的评分表

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public ReportInput getParent() {
		return parent;
	}

	public void setParent(ReportInput parent) {
		this.parent = parent;
	}

	public List<ReportInput> getChildren() {
		return children;
	}

	public void setChildren(List<ReportInput> children) {
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

	public CheckReport getCheckReport() {
		return checkReport;
	}

	public void setCheckReport(CheckReport checkReport) {
		this.checkReport = checkReport;
	}

	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public Double getRealFee() {
		return realFee;
	}

	public void setRealFee(Double realFee) {
		this.realFee = realFee;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		return "供应商质量管理：监察报告    问题"+this.name+"，实际得分"+this.realFee;
	}
}

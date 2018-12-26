package com.ambition.supplier.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ambition.product.base.IdEntity;


/**
 * 评价指标
 * @author 赵骏
 *
 */
@Entity
@Table(name = "SUPPLIER_EVALUATING_INDICATOR")
public class EvaluatingIndicator  extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String name;//指标名称
	private String unit;//指标单位
	private String dataSourceCode;//自动评分数据来源
    private String remark;//备注
    @Column(name="myLevel")
    private Integer level = 1;//标识
    private Boolean readonly = false;//只读 
    private Boolean selectText;//是否下拉
    private Integer orderByNum;//排序
    
    @ManyToOne
	@JoinColumn(name = "FK_PARENT_ID")
    private EvaluatingIndicator parent;//上级指标
    
    @OneToMany(mappedBy="parent",cascade=javax.persistence.CascadeType.ALL)
    @Cascade(value=CascadeType.DELETE_ORPHAN)
    private List<EvaluatingIndicator> children;//子级指标
    
    @OneToMany(mappedBy="evaluatingIndicator",cascade=javax.persistence.CascadeType.REMOVE)
    private List<ModelIndicator> modelIndicators;//已经设置的模型指标
    
    @OneToMany(mappedBy="evaluatingIndicator",cascade=javax.persistence.CascadeType.REMOVE)
    private List<EvaluatingGradeRule> gradeRules;//设置的自动评分规则
    
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public EvaluatingIndicator getParent() {
		return parent;
	}
	public void setParent(EvaluatingIndicator parent) {
		this.parent = parent;
	}
	public List<EvaluatingIndicator> getChildren() {
		return children;
	}
	public void setChildren(List<EvaluatingIndicator> children) {
		this.children = children;
	}
	public List<ModelIndicator> getModelIndicators() {
		return modelIndicators;
	}
	public void setModelIndicators(List<ModelIndicator> modelIndicators) {
		this.modelIndicators = modelIndicators;
	}
	
	public List<ModelIndicator> getAllModelIndicators(){
		List<ModelIndicator> allModelIndicators = new ArrayList<ModelIndicator>();
		if(children.isEmpty()){
			allModelIndicators.addAll(modelIndicators);
		}else{
			for(EvaluatingIndicator evaluatingIndicator : children){
				allModelIndicators.addAll(evaluatingIndicator.getAllModelIndicators());
			}
		}
		return allModelIndicators;
	}
	
	public String getDataSourceCode() {
		return dataSourceCode;
	}
	public void setDataSourceCode(String dataSourceCode) {
		this.dataSourceCode = dataSourceCode;
	}
	
	public List<EvaluatingGradeRule> getGradeRules() {
		return gradeRules;
	}
	public void setGradeRules(List<EvaluatingGradeRule> gradeRules) {
		this.gradeRules = gradeRules;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Boolean getReadonly() {
		return readonly;
	}
	public void setReadonly(Boolean readonly) {
		this.readonly = readonly;
	}
	public String toString(){
		return "供应商质量管理：评价指标    名称"+this.name+"，备注"+this.remark;
	}
	public Integer getOrderByNum() {
		return orderByNum;
	}
	public void setOrderByNum(Integer orderByNum) {
		this.orderByNum = orderByNum;
	}
	public Boolean getSelectText() {
		return selectText;
	}
	public void setSelectText(Boolean selectText) {
		this.selectText = selectText;
	}
	
}

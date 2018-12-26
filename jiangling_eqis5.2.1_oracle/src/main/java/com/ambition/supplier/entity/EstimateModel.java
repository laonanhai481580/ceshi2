package com.ambition.supplier.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ambition.product.base.IdEntity;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
//import com.norteksoft.product.util.WebContextUtils;

/**
 * 评价模型
 * @author ambition-zhaojun
 *
 */
@Entity
@Table(name = "SUPPLIER_ESTIMATE_MODEL")
public class EstimateModel  extends IdEntity {
	public static String STATE_ISUSE = "isUse";
	public static String STATE_DISABLED = "disabled";
	private static final long serialVersionUID = 1L;
	private String name; //模型名称
    private String remark;//备注
    @Column(name = "myLevel")
    private Integer level = 1;//标识
    private Double weight;//权重
    private String cycle;//周期
    private Integer startMonth = 1;//开始月份
    private String state = "isUse";//状态
    private Long parentEstimateModelId;//顶级模型分类编号
    @ManyToOne
	@JoinColumn(name = "FK_PARENT_ID")
    private EstimateModel parent;//上级模型分类
    
    @OneToMany(mappedBy="parent",cascade=javax.persistence.CascadeType.ALL)
    @Cascade(value=CascadeType.DELETE_ORPHAN)
    private List<EstimateModel> children;//子级模型
    
    @OneToMany(mappedBy="estimateModel",cascade=javax.persistence.CascadeType.REMOVE)
    private List<ModelIndicator> modelIndicators;
    
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
	public EstimateModel getParent() {
		return parent;
	}
	public void setParent(EstimateModel parent) {
		this.parent = parent;
	}
	public List<EstimateModel> getChildren() {
		return children;
	}
	public void setChildren(List<EstimateModel> children) {
		this.children = children;
	}
	public Long getParentEstimateModelId() {
		return parentEstimateModelId;
	}
	public void setParentEstimateModelId(Long parentEstimateModelId) {
		this.parentEstimateModelId = parentEstimateModelId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<ModelIndicator> getModelIndicators() {
		return modelIndicators;
	}
	
	public List<ModelIndicator> getAllModelIndicators(){
		List<ModelIndicator> allModelIndicators = new ArrayList<ModelIndicator>();
		if(STATE_ISUSE.equals(this.state)){
			if(this.children.isEmpty()){
				allModelIndicators.addAll(this.modelIndicators);
			}else{
				for(EstimateModel child : children){
					allModelIndicators.addAll(child.getAllModelIndicators());
				}
			}
		}
		return allModelIndicators;
	}
	public void setModelIndicators(List<ModelIndicator> modelIndicators) {
		this.modelIndicators = modelIndicators;
	}
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public Integer getStartMonth() {
		return startMonth;
	}
	public void setStartMonth(Integer startMonth) {
		this.startMonth = startMonth;
	}
	
	@Transient
	public Double getTotalPoints(){
		double val = 0.0;
		for(ModelIndicator modelIndicator : modelIndicators){
			if(EstimateModel.STATE_DISABLED.equals(modelIndicator.getEstimateModel().getState())
				||!modelIndicator.getEstimateModel().getChildren().isEmpty()
				||!modelIndicator.getEvaluatingIndicator().getChildren().isEmpty()
				||modelIndicator.getTotalPoints() == null
				||modelIndicator.getTotalPoints() <= 0){
				continue;
			}
			val += modelIndicator.getTotalPoints();
		}
		return val;
	}
	
	/**
	 * 获取周期的编辑字符串
	 * @return
	 */
	public static String getCycleEditOptions(){
		List<Option> options = getCycleOptionsForSelect();
		StringBuffer sb = new StringBuffer("");
		for(Option option : options){
			if(sb.length() > 0){
				sb.append(";");
			}
			sb.append(option.getName() + ":" + option.getValue());
		}
		return sb.toString();
	}
	/**
	 * 获取周期的编辑字符串
	 * @return
	 */
	public static Map<String,Integer> getCycleMap(){
		List<Option> options = getCycleOptionsForSelect();
		Map<String,Integer> map = new HashMap<String, Integer>();
		for(Option option : options){
			if(StringUtils.isNumeric(option.getValue())){
				map.put(option.getName(),Integer.valueOf(option.getValue()));
			}
		}
		return map;
	}
	
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	/**
	 * 获取周期的Selectlist
	 * @return
	 */
	public static List<Option> getCycleOptionsForSelect(){
//		WorkflowEngine workflowEngine = (WorkflowEngine) ContextUtils.getBean("workflowEngine");
		return ApiFactory.getSettingService().getOptionsByGroupCode("supplier_estimate_model_cycle");
	}
	public String toString(){
		return "供应商质量管理：评价模型    名称"+this.name+"，周期"+this.cycle;
	}
	
}

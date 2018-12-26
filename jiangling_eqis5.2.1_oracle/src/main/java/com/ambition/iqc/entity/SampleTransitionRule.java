package com.ambition.iqc.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.norteksoft.wf.engine.client.ExtendField;

/**
 * 抽样方案转移规则
 * @author 赵骏
 *
 */
@Entity
@Table(name="IQC_SAMPLE_TRANSITION_RULE")
public class SampleTransitionRule extends IdEntity{
	private static final long serialVersionUID = -731322666650992256L;
	//规则
	public static String RULE_ORDINARY = SampleScheme.ORDINARY_TYPE;
	public static String RULE_TIGHTEN = SampleScheme.TIGHTEN_TYPE;
	public static String RULE_RELAX = SampleScheme.RELAX_TYPE;
	public static String RULE_PAUSE = "暂停";
	//状态
	public static String STATE_USE = "使用中";
	public static String STATE_DISABLE = "已禁用";
	//统计方法
	public static String METHOD_SUCCESSION = "连续";
	public static String METHOD_ACCUMULATIVE = "累计";
	private String sourceRule;//源规则
	private String targetRule;//目标规则
	private String flowWay;//流动的方向,up或down
	private String statisticalMethod = METHOD_SUCCESSION;//统计方法
	private String state = STATE_USE;//状态,使用中,已禁用
	private Integer totalRange;//范围
	private String comparisonOperators;//比较字符
	private Integer amount;//数量
	private String conclusion;//结论 OK,NG
	
	@Embedded
	private ExtendField extendField;

	public String getSourceRule() {
		return sourceRule;
	}

	public void setSourceRule(String sourceRule) {
		this.sourceRule = sourceRule;
	}

	public String getTargetRule() {
		return targetRule;
	}

	public void setTargetRule(String targetRule) {
		this.targetRule = targetRule;
	}

	public ExtendField getExtendField() {
		return extendField;
	}

	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}

	public String getStatisticalMethod() {
		return statisticalMethod;
	}

	public void setStatisticalMethod(String statisticalMethod) {
		this.statisticalMethod = statisticalMethod;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getComparisonOperators() {
		return comparisonOperators;
	}

	public void setComparisonOperators(String comparisonOperators) {
		this.comparisonOperators = comparisonOperators;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public Integer getTotalRange() {
		return totalRange;
	}

	public void setTotalRange(Integer totalRange) {
		this.totalRange = totalRange;
	}

	public String getFlowWay() {
		return flowWay;
	}

	public void setFlowWay(String flowWay) {
		this.flowWay = flowWay;
	}

	public String toString(){
		return "物料抽样标准维护：抽样方案转移规则    范围"+this.totalRange +"  状态"+this.state;
	}
}

package com.ambition.supplier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;


/**
 * 类名:评价自动评分规则
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2013-4-20 发布
 */
@Entity
@Table(name = "SUPPLIER_EVALUATING_GRADE_RULE")
public class EvaluatingGradeRule extends IdEntity {
	private static final long serialVersionUID = 6536012810073736196L;
	@Column(name="tostart")
	private Double start;//起始点>=
	private Double toend;//结束点<
	private Double fee;//得分
	@Column(name="myLevel")
	private String level;//级别
    private String remark;//备注

    @ManyToOne
   	@JoinColumn(name = "FK_EVALUATING_INDICATOR_ID")
    private EvaluatingIndicator evaluatingIndicator;//评价指标

	public Double getStart() {
		return start;
	}



	public void setStart(Double start) {
		this.start = start;
	}




	public Double getToend() {
		return toend;
	}



	public void setToend(Double toend) {
		this.toend = toend;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public EvaluatingIndicator getEvaluatingIndicator() {
		return evaluatingIndicator;
	}



	public void setEvaluatingIndicator(EvaluatingIndicator evaluatingIndicator) {
		this.evaluatingIndicator = evaluatingIndicator;
	}

	public Double getFee() {
		return fee;
	}



	public void setFee(Double fee) {
		this.fee = fee;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String toString(){
		return "供应商质量管理：自动评分规则  >="+this.start+"&&<"+this.toend + ",得分为" + this.fee + ",备注:" + this.remark;
	}
}

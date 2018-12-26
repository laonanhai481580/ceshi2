package com.ambition.spc.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**    
 * BsRules.java
 * @authorBy wanglf
 *
 */
@Entity
@Table(name = "SPC_BS_RULES")
public class BsRules extends IdEntity {

	/**
	 * SPC判断规则
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;//规则名称
	private String no;//规则编号
	private String expression;//规则表达式,规则表达式：控制图类别.控制图类型.点数.上升/下降/不变……
	private String type;//控制图类别,0:主控制图，1：副控制图
	private String model;//控制图类型;0：趋势型，1：运行型，2：交替型，3：其他
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}

}

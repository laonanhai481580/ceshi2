package com.ambition.cost.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 类名:质量成本同步时的转换规则
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2014-12-8 发布
 */
@Entity
@Table(name = "COST_COST_CONVERT")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class CostConvert extends com.norteksoft.product.orm.IdEntity {
	/**类型:不需要转换*/
	public static final Integer CONVERT_TYPE_NO = 0;
	/**类型:需要转换*/
	public static final Integer CONVERT_TYPE_YES = 1;
	private static final long serialVersionUID = -9181689261077768159L;
	private String deptName;//部门名称
	private String dutyName;//职务名称
	private String levelTwoName;//二级成本名称
	private String levelTwoCode;//二级成本代码
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDutyName() {
		return dutyName;
	}
	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}
	public String getLevelTwoName() {
		return levelTwoName;
	}
	public void setLevelTwoName(String levelTwoName) {
		this.levelTwoName = levelTwoName;
	}
	public String getLevelTwoCode() {
		return levelTwoCode;
	}
	public void setLevelTwoCode(String levelTwoCode) {
		this.levelTwoCode = levelTwoCode;
	}
}

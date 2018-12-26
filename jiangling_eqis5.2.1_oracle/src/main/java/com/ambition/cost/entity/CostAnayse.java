package com.ambition.cost.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.norteksoft.product.orm.IdEntity;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  林少伟
 * @version 1.00 2014-6-6 发布
 */
@Entity
@Table(name="COST_ANAYSE")
public class CostAnayse extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String occurringMonthStr;//月份字符串,手动维护记录时有用到
    private Integer occurringMonth;//发生月份
    private String levelTwoCode;//二级代码
    private String levelTwoName;//二级成本名称
    private String levelThreeCode;//三级代码
    private String levelThreeName;//三级成本名称
    private String code;//末级代码
    private String name;//末级名称
//    private String deptName;//部门名称
//    private String dutyName;//职务名称
//    private Double budgetValue;//预算金额
    private Double value;//发生费用
    public String getOccurringMonthStr() {
        return occurringMonthStr;
    }
    public void setOccurringMonthStr(String occurringMonthStr) {
        this.occurringMonthStr = occurringMonthStr;
    }
    public Integer getOccurringMonth() {
        return occurringMonth;
    }
    public void setOccurringMonth(Integer occurringMonth) {
        this.occurringMonth = occurringMonth;
    }
    public String getLevelTwoCode() {
        return levelTwoCode;
    }
    public void setLevelTwoCode(String levelTwoCode) {
        this.levelTwoCode = levelTwoCode;
    }
    public String getLevelTwoName() {
        return levelTwoName;
    }
    public void setLevelTwoName(String levelTwoName) {
        this.levelTwoName = levelTwoName;
    }
    public String getLevelThreeCode() {
        return levelThreeCode;
    }
    public void setLevelThreeCode(String levelThreeCode) {
        this.levelThreeCode = levelThreeCode;
    }
    public String getLevelThreeName() {
        return levelThreeName;
    }
    public void setLevelThreeName(String levelThreeName) {
        this.levelThreeName = levelThreeName;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getValue() {
        return value;
    }
    public void setValue(Double value) {
        this.value = value;
    }
    
}

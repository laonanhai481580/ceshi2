package com.ambition.aftersales.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * 类名:Lar目标值维�?
 * <p>amb</p>
 * <p>厦门安必兴信息科�?有限公司</p>
 * <p>功能说明�?</p>
 * @author  LPF
 * @version 1.00 2016�?9�?21�? 发布
 */
@Entity
@Table(name = "AFS_LAR_TARGET")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class LarTarget extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String workingProcedure;//新增字段  工序
	private Integer yearFen;//年份
	//private String businessUnit;//事业部
	private String customerCode;//新增 字段 客户代码
	private String customer;//客户名称
	private String ofilmModel;//新增 字段 欧非机种
	private Integer years;//新增 字段  年度目标值
	private	Float target1;//一月目标值
	private	Float target2;//二月目标值
	private	Float target3;//三月目标值
	private	Float target4;//四月目标值
	private	Float target5;//五月目标值
	private	Float target6;//六月目标值
	private	Float target7;//七月目标值
	private	Float target8;//八月目标值
	private	Float target9;//九月目标值
	private	Float target10;//十月目标值
	private	Float target11;//十一月目标值
	private	Float target12;//十二月目标值
	
	
	private String targetValue;//目标值
	private String warmingMan;//通知人员
	private String warmingManLogin;//通知人员登录名
	public Integer getYearFen() {
		return yearFen;
	}
	public void setYearFen(Integer yearFen) {
		this.yearFen = yearFen;
	}

	public Float getTarget1() {
		return target1;
	}
	public void setTarget1(Float target1) {
		this.target1 = target1;
	}
	public Float getTarget2() {
		return target2;
	}
	public void setTarget2(Float target2) {
		this.target2 = target2;
	}
	public Float getTarget3() {
		return target3;
	}
	public void setTarget3(Float target3) {
		this.target3 = target3;
	}
	public Float getTarget4() {
		return target4;
	}
	public void setTarget4(Float target4) {
		this.target4 = target4;
	}
	public Float getTarget5() {
		return target5;
	}
	public void setTarget5(Float target5) {
		this.target5 = target5;
	}
	public Float getTarget6() {
		return target6;
	}
	public void setTarget6(Float target6) {
		this.target6 = target6;
	}
	public Float getTarget7() {
		return target7;
	}
	public void setTarget7(Float target7) {
		this.target7 = target7;
	}
	public Float getTarget8() {
		return target8;
	}
	public void setTarget8(Float target8) {
		this.target8 = target8;
	}
	public Float getTarget9() {
		return target9;
	}
	public void setTarget9(Float target9) {
		this.target9 = target9;
	}
	public Float getTarget10() {
		return target10;
	}
	public void setTarget10(Float target10) {
		this.target10 = target10;
	}
	public Float getTarget11() {
		return target11;
	}
	public void setTarget11(Float target11) {
		this.target11 = target11;
	}
	public Float getTarget12() {
		return target12;
	}
	public void setTarget12(Float target12) {
		this.target12 = target12;
	}
	public String getWorkingProcedure() {
		return workingProcedure;
	}
	public void setWorkingProcedure(String workingProcedure) {
		this.workingProcedure = workingProcedure;
	}

	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOfilmModel() {
		return ofilmModel;
	}
	public void setOfilmModel(String ofilmModel) {
		this.ofilmModel = ofilmModel;
	}
	public Integer getYears() {
		return years;
	}
	public void setYears(Integer years) {
		this.years = years;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getTargetValue() {
		return targetValue;
	}
	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}
	public String getWarmingMan() {
		return warmingMan;
	}
	public void setWarmingMan(String warmingMan) {
		this.warmingMan = warmingMan;
	}
	public String getWarmingManLogin() {
		return warmingManLogin;
	}
	public void setWarmingManLogin(String warmingManLogin) {
		this.warmingManLogin = warmingManLogin;
	}

}

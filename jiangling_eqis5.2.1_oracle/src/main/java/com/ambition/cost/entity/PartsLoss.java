package com.ambition.cost.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 内部零件损失表
 * @author ambition-cyy
 *
 */
@Entity
@Table(name = "COST_PARTS_LOSS")
public class PartsLoss extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String mainCode;//主料号
    private String mainName;//主料号名称
    private String partImageNo;//零件图号
    private String code;//料号
	private String name;//料号名称
	private String customerCode;//客户代码
	private String customerName;//客户名称
	private String lossType;//类别
	private Integer dengji;//档次
	private String projectLevel;//项目档次
	private  String remark;//备注

	public String getMainCode() {
		return mainCode;
	}

	public void setMainCode(String mainCode) {
		this.mainCode = mainCode;
	}

	public String getMainName() {
		return mainName;
	}

	public void setMainName(String mainName) {
		this.mainName = mainName;
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

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getLossType() {
		return lossType;
	}

	public void setLossType(String lossType) {
		this.lossType = lossType;
	}


	public Integer getDengji() {
		return dengji;
	}

	public void setDengji(Integer dengji) {
		this.dengji = dengji;
	}

	public String getProjectLevel() {
		return projectLevel;
	}

	public void setProjectLevel(String projectLevel) {
		this.projectLevel = projectLevel;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPartImageNo() {
		return partImageNo;
	}

	public void setPartImageNo(String partImageNo) {
		this.partImageNo = partImageNo;
	}

	public String toString() {
		return "质量成本管理：内部零件损失  ID"+this.getId()+",料号"+this.code+",品名"+this.name;
	}
}

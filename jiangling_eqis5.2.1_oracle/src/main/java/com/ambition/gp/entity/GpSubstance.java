package com.ambition.gp.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:管控物质
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  xjn
 * @version 1.00 2017年11月11日 发布
 */
@Entity
@Table(name = "GP_SUBSTANCE")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class GpSubstance extends IdEntity{
	
	private static final long serialVersionUID = 1L;
	private String substanceName;//名字
	private String substanceCode;//编码
	private String substanceweight;//重量
	private Double percentage;//百分比
	private String ratio;//总比率
	private String masterId;
	@ManyToOne
	@JoinColumn(name="FK_GP_ID")
	private GpAverageMaterial gpAverageMaterial;
	
	public String getSubstanceName() {
		return substanceName;
	}
	public void setSubstanceName(String substanceName) {
		this.substanceName = substanceName;
	}
	public String getSubstanceCode() {
		return substanceCode;
	}
	public void setSubstanceCode(String substanceCode) {
		this.substanceCode = substanceCode;
	}
	public String getSubstanceweight() {
		return substanceweight;
	}
	public void setSubstanceweight(String substanceweight) {
		this.substanceweight = substanceweight;
	}

	public Double getPercentage() {
		return percentage;
	}
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public GpAverageMaterial getGpAverageMaterial() {
		return gpAverageMaterial;
	}
	public void setGpAverageMaterial(GpAverageMaterial gpAverageMaterial) {
		this.gpAverageMaterial = gpAverageMaterial;
	}
	public String getMasterId() {
		return masterId;
	}
	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}
	
	
}

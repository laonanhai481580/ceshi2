package com.ambition.aftersales.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 类名:FAR解析单不良现象
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月24日 发布
 */
@Entity
@Table(name = "AFS_FAR_ANALYSIS_ITEMS")
public class FarAnalysisItem extends IdEntity {

	private static final long serialVersionUID = 1L;
	private String defectionItem;//不良项目
	private Integer defectionItemValue;//不良项目数量
	@ManyToOne
	@JoinColumn(name = "FK_FAR_ANALYSIS_ID")
	@JsonIgnore()
	private FarAnalysis farAnalysis;
	public String getDefectionItem() {
		return defectionItem;
	}
	public void setDefectionItem(String defectionItem) {
		this.defectionItem = defectionItem;
	}
	public Integer getDefectionItemValue() {
		return defectionItemValue;
	}
	public void setDefectionItemValue(Integer defectionItemValue) {
		this.defectionItemValue = defectionItemValue;
	}
	public FarAnalysis getFarAnalysis() {
		return farAnalysis;
	}
	public void setFarAnalysis(FarAnalysis farAnalysis) {
		this.farAnalysis = farAnalysis;
	}
	
	

	
	
	
	
}

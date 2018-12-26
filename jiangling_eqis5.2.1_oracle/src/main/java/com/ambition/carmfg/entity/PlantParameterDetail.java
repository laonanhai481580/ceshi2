package com.ambition.carmfg.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 类名:设备参数明细
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016-9-3 发布
 */
@Entity
@Table(name = "MFG_PLANT_PARAMETER_DETAIL")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class PlantParameterDetail  extends IdEntity {

	private static final long serialVersionUID = 1L;
	private String plantName;//设备名称
	private String parameterName;//参数名称
	private String parameterStandard;//参数规格
	private Double levela;//上限
	private Double levelb;//下限
	private String unit;//单位
	private String remark;//备注
    @ManyToOne
	@JoinColumn(name = "FK_PLANT_PARAMETER_ID")
    private PlantParameter plantParameter;//设备参数
    @ManyToOne
	@JoinColumn(name = "FK_PLANT_ITEM_ID")
    private PlantItem plantItem;//设备信息
	public String getPlantName() {
		return plantName;
	}
	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getParameterStandard() {
		return parameterStandard;
	}
	public void setParameterStandard(String parameterStandard) {
		this.parameterStandard = parameterStandard;
	}
	public Double getLevela() {
		return levela;
	}
	public void setLevela(Double levela) {
		this.levela = levela;
	}
	public Double getLevelb() {
		return levelb;
	}
	public void setLevelb(Double levelb) {
		this.levelb = levelb;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public PlantParameter getPlantParameter() {
		return plantParameter;
	}
	public void setPlantParameter(PlantParameter plantParameter) {
		this.plantParameter = plantParameter;
	}
	public PlantItem getPlantItem() {
		return plantItem;
	}
	public void setPlantItem(PlantItem plantItem) {
		this.plantItem = plantItem;
	}
}
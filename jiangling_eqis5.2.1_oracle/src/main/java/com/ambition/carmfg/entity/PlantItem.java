package com.ambition.carmfg.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 类名:设备信息维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016-9-3 发布
 */
@Entity
@Table(name = "MFG_PLANT_ITEM")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class PlantItem  extends IdEntity {

	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "MFG_PLANT_ITEM";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "设备信息";//实体_列表_名称
	private String plantName;//设备名称
	private String parameterName;//参数名称
	private String parameterStandard;//参数规格
	private String unit;//单位
	private String remark;//备注
    @JsonIgnore
    @OneToMany(mappedBy="plantItem",cascade=javax.persistence.CascadeType.REMOVE)
    private List<PlantParameterDetail> plantParameterDetail;
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
	public List<PlantParameterDetail> getPlantParameterDetail() {
		return plantParameterDetail;
	}
	public void setPlantParameterDetail(
			List<PlantParameterDetail> plantParameterDetail) {
		this.plantParameterDetail = plantParameterDetail;
	}
	
}

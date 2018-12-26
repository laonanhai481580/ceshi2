package com.ambition.carmfg.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 类名:设备参数---基础维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016-9-3 发布
 */
@Entity
@Table(name = "MFG_PLANT_PARAMETER")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class PlantParameter  extends IdEntity{

	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "MFG_PLANT_PARAMETER";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "设备参数";//实体_列表_名称
	private String model;//机种		
	private String modelName;//机种名称
	private String workingProcedure;//检验工序	
	private Long plantAttachId;//设备文件的ID
	@OneToMany(mappedBy="plantParameter",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	private List<PlantParameterDetail> checkItems;//设备参数明细
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getWorkingProcedure() {
		return workingProcedure;
	}
	public void setWorkingProcedure(String workingProcedure) {
		this.workingProcedure = workingProcedure;
	}
	public Long getPlantAttachId() {
		return plantAttachId;
	}
	public void setPlantAttachId(Long plantAttachId) {
		this.plantAttachId = plantAttachId;
	}

	public List<PlantParameterDetail> getCheckItems() {
		return checkItems;
	}

	public void setCheckItems(List<PlantParameterDetail> checkItems) {
		this.checkItems = checkItems;
	}
	
	
	
}

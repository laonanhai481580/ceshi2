package com.ambition.carmfg.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:抽检数量维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月27日 发布
 */
@Entity
@Table(name = "MFG_SAMPLING_NUMBER")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class SamplingNumber extends IdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "MFG_SAMPLING";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "抽检数量维护";//实体_列表_名称
	private String storeTissue;//库存组织
	private String inspectionType;//检验类别
	private String model;//机种
	private String workingProcedure;//工序
	private Integer samplingNumber;//抽检数量
	public String getStoreTissue() {
		return storeTissue;
	}
	public void setStoreTissue(String storeTissue) {
		this.storeTissue = storeTissue;
	}
	public String getInspectionType() {
		return inspectionType;
	}
	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getWorkingProcedure() {
		return workingProcedure;
	}
	public void setWorkingProcedure(String workingProcedure) {
		this.workingProcedure = workingProcedure;
	}
	public Integer getSamplingNumber() {
		return samplingNumber;
	}
	public void setSamplingNumber(Integer samplingNumber) {
		this.samplingNumber = samplingNumber;
	}
	

}

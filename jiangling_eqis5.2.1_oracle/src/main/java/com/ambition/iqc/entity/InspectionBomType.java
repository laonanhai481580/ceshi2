package com.ambition.iqc.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 报检物料类别
 * @author ambition-zhaojun
 *
 */
@Entity
@Table(name = "IQC_INSPECTION_BOM_TYPE")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class InspectionBomType  extends IdEntity {
	private static final long serialVersionUID = 1L;
    private String materialType;//物料类别
    private String materialTypeCode;//物料类别代码
    private String remark;//备注
    private String inspectionType="检验";//检验类型（检验/免检）
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public String getMaterialTypeCode() {
		return materialTypeCode;
	}
	public void setMaterialTypeCode(String materialTypeCode) {
		this.materialTypeCode = materialTypeCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getInspectionType() {
		return inspectionType;
	}
	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}
	

	
}

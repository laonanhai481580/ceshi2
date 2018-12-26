package com.ambition.carmfg.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 报检物料实体
 * @author ambition-zhaojun
 *
 */
@Entity
@Table(name = "MFG_PRODUCT_BOM_INSPECTION")
public class ProductBomInspection  extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String materielName; //物料名称
    private String materielCode; //物料编码
    private String materielModel;//规格型号
    private String units;//单位
    private String materialType;//物料类别
    private String materialTypeCode;//物料类别代码
    private String remark;//备注
	public String getMaterielName() {
		return materielName;
	}
	public void setMaterielName(String materielName) {
		this.materielName = materielName;
	}
	public String getMaterielCode() {
		return materielCode;
	}
	public void setMaterielCode(String materielCode) {
		this.materielCode = materielCode;
	}
	public String getMaterielModel() {
		return materielModel;
	}
	public void setMaterielModel(String materielModel) {
		this.materielModel = materielModel;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
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

	
}

package com.ambition.supplier.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.norteksoft.wf.engine.client.ExtendField;

/**
 * 供应的产品
 * @author ambition-zhaojun
 *
 */
@Entity
@Table(name = "SUPPLIER_SUPPLY_PRODUCT")
public class SupplyProduct extends IdEntity {
	/*正在申请*/
	public static String APPLYSTATE_DEFAULT = "未申请";
	public static String APPLYSTATE_INSPECT = "正在考察";
	public static String APPLYSTATE_INSPECTPASS = "考察通过";
	public static String APPLYSTATE_INSPECTFAIL = "考察未通过";
	public static String APPLYSTATE_APPRAISAL_SAMPLE = "正在样件鉴定";
	public static String APPLYSTATE_APPRAISAL_SAMPLEFAIL = "样件鉴定不通过";
	public static String APPLYSTATE_APPRAISAL_SAMPLEPASS = "样件鉴定通过";
	public static String APPLYSTATE_APPRAISAL_SUBLOTS = "正在小批鉴定";
	public static String APPLYSTATE_APPRAISAL_SUBLOTSFAIL = "小批鉴定不通过";
	public static String APPLYSTATE_APPRAISAL_SUBLOTSPASS = "小批鉴定通过";
	public static String APPLYSTATE_ADMITTANCE = "已准入";
	private static final long serialVersionUID = 1621768121887327213L;
	
	@ManyToOne
	@JoinColumn(name = "FK_SUPPLIER_ID")
	private Supplier supplier;//供应商
	private String materialType;//物料类别
	private String code;//物料编号
	private String name;//物料名称
	private String model;//规格型号
	private String purchaseMan;//采购员
	private String importance;//重要度
	private String remark;//说明

	@Embedded
	private ExtendField extendField;
	
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
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
	
	public String getImportance() {
		return importance;
	}
	public void setImportance(String importance) {
		this.importance = importance;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public ExtendField getExtendField() {
		return extendField;
	}
	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getPurchaseMan() {
		return purchaseMan;
	}
	public void setPurchaseMan(String purchaseMan) {
		this.purchaseMan = purchaseMan;
	}
	public String toString(){
		return "供应商质量管理：供应商产品名称"+this.name+",物料类别"+this.materialType;
	}
}

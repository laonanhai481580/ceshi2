package com.ambition.gp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:供应商调查表
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2018年5月25日 发布
 */
@Entity
@Table(name="GP_SURVEY")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class GpSurvey extends IdEntity{
		/**
		  *GpSurvey.java2018年5月10日
		 */
	private static final long serialVersionUID = 1L;
	private String sortData;//排序
	private String substanceName;//名字
	private String substanceCode;//编码
	private String materialName;//物料名称
	private String materialCode;//物料代码
	private String supplyMaterial;//供应物料
	private String materialType;//材料类别
	private String guaranteeFile;//有害物质不使用保证书
	private String surveyFile;//冲突矿产调查表
	private String signatureFile;//HSF技术标准版本升级签收单
	private String remark;//备注
	public String getSortData() {
		return sortData;
	}
	public void setSortData(String sortData) {
		this.sortData = sortData;
	}
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
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public String getGuaranteeFile() {
		return guaranteeFile;
	}
	public void setGuaranteeFile(String guaranteeFile) {
		this.guaranteeFile = guaranteeFile;
	}
	public String getSurveyFile() {
		return surveyFile;
	}
	public void setSurveyFile(String surveyFile) {
		this.surveyFile = surveyFile;
	}
	public String getSignatureFile() {
		return signatureFile;
	}
	public void setSignatureFile(String signatureFile) {
		this.signatureFile = signatureFile;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSupplyMaterial() {
		return supplyMaterial;
	}
	public void setSupplyMaterial(String supplyMaterial) {
		this.supplyMaterial = supplyMaterial;
	}
	
}

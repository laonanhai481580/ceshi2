package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 类名:准入流程配置
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2013-5-9 发布
 */
@Entity
@Table(name = "SUPPLIER_ADMITTANCE_FLOW_CONFIGURE")
public class AdmittanceFlowConfigure extends IdEntity {
	private static final long serialVersionUID = 1621768121887327213L;
	private Boolean isNewSupplier = false;//是否新供应商
	private String materialType;//物料类别
	private Boolean needInvestigate=true;//是否需要供应商调查表
	private Boolean needInspectionReport=true;//是否需要现场考查
	private Boolean needSampleAppraisal=true;//是否需要样件鉴定
	private Integer minSampleAppraisal;//最少多少次样件鉴定
	private Integer maxSampleAppraisal;//最多多少次样件鉴定
	private Boolean needSublotsAppraisal=true;//是否需要小批鉴定
	private Integer minSublotsAppraisal;//最少多少次小批鉴定
	private Integer maxSublotsAppraisal;//最多多少次小批鉴定
	
	public Boolean getIsNewSupplier() {
		return isNewSupplier;
	}


	public void setIsNewSupplier(Boolean isNewSupplier) {
		this.isNewSupplier = isNewSupplier;
	}


	public String getMaterialType() {
		return materialType;
	}


	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}


	public Boolean getNeedInvestigate() {
		return needInvestigate;
	}


	public void setNeedInvestigate(Boolean needInvestigate) {
		this.needInvestigate = needInvestigate;
	}


	public Boolean getNeedInspectionReport() {
		return needInspectionReport;
	}


	public void setNeedInspectionReport(Boolean needInspectionReport) {
		this.needInspectionReport = needInspectionReport;
	}


	public Boolean getNeedSampleAppraisal() {
		return needSampleAppraisal;
	}


	public void setNeedSampleAppraisal(Boolean needSampleAppraisal) {
		this.needSampleAppraisal = needSampleAppraisal;
	}

	public Boolean getNeedSublotsAppraisal() {
		return needSublotsAppraisal;
	}

	public void setNeedSublotsAppraisal(Boolean needSublotsAppraisal) {
		this.needSublotsAppraisal = needSublotsAppraisal;
	}

	public Integer getMinSampleAppraisal() {
		return minSampleAppraisal;
	}

	public void setMinSampleAppraisal(Integer minSampleAppraisal) {
		this.minSampleAppraisal = minSampleAppraisal;
	}


	public Integer getMaxSampleAppraisal() {
		return maxSampleAppraisal;
	}

	public void setMaxSampleAppraisal(Integer maxSampleAppraisal) {
		this.maxSampleAppraisal = maxSampleAppraisal;
	}


	public Integer getMinSublotsAppraisal() {
		return minSublotsAppraisal;
	}


	public void setMinSublotsAppraisal(Integer minSublotsAppraisal) {
		this.minSublotsAppraisal = minSublotsAppraisal;
	}


	public Integer getMaxSublotsAppraisal() {
		return maxSublotsAppraisal;
	}


	public void setMaxSublotsAppraisal(Integer maxSublotsAppraisal) {
		this.maxSublotsAppraisal = maxSublotsAppraisal;
	}


	public String toString(){
		return "供应商质量管理：准入流程配置,是否新供应商:"+this.isNewSupplier+",物料类别:"+this.materialType;
	}
}

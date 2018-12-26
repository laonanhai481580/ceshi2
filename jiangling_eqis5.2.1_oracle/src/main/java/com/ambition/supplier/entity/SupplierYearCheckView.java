package com.ambition.supplier.entity;

import java.util.Date;

import com.ambition.product.base.IdEntity;


/**
 * 类名:供应商年度稽核
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月27日 发布
 */
public class SupplierYearCheckView extends IdEntity{
		/**
		  *SupplierYearCheckView.java2016年10月27日
		 */
	private static final long serialVersionUID = 1L;
	private Long supplierId;
	private String name;
	private String code;
	private String supplyFactory;
	private String supplyMaterial;//供应物料
	private Date firstCheckPlanDate;//首次计划稽核日期
	private Date firstCheckDesignDate;//首次实际稽核日期
	private Date secondCheckPlanDate;//二次计划日期
	private Date secondCheckDesignDate;//二次实际日期
	private String firstCheckResult;//首次结果
	private String secondCheckResult;//二次结果
	private Date firstCheckDate;
	private Date secondCheckDate;
	private String finalCheckResult;//最终稽核结果
	private String checker;//确认人
	private String checkFile;//稽核附件
	private String secondCheckFile;//二次稽核附件
	private String improveFile;//问题改善附件
	private String problemState;//问题状态
	private Date problemCloseDate;//问题关闭日期
	private String remark;//备注
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSupplyFactory() {
		return supplyFactory;
	}
	public void setSupplyFactory(String supplyFactory) {
		this.supplyFactory = supplyFactory;
	}
	public String getSupplyMaterial() {
		return supplyMaterial;
	}
	public void setSupplyMaterial(String supplyMaterial) {
		this.supplyMaterial = supplyMaterial;
	}
	
	
	public Date getFirstCheckPlanDate() {
		return firstCheckPlanDate;
	}
	public void setFirstCheckPlanDate(Date firstCheckPlanDate) {
		this.firstCheckPlanDate = firstCheckPlanDate;
	}
	public Date getFirstCheckDesignDate() {
		return firstCheckDesignDate;
	}
	public void setFirstCheckDesignDate(Date firstCheckDesignDate) {
		this.firstCheckDesignDate = firstCheckDesignDate;
	}
	public String getFinalCheckResult() {
		return finalCheckResult;
	}
	public void setFinalCheckResult(String finalCheckResult) {
		this.finalCheckResult = finalCheckResult;
	}
	public String getChecker() {
		return checker;
	}
	public void setChecker(String checker) {
		this.checker = checker;
	}
	public String getCheckFile() {
		return checkFile;
	}
	public void setCheckFile(String checkFile) {
		this.checkFile = checkFile;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public Date getSecondCheckPlanDate() {
		return secondCheckPlanDate;
	}
	public void setSecondCheckPlanDate(Date secondCheckPlanDate) {
		this.secondCheckPlanDate = secondCheckPlanDate;
	}
	public Date getSecondCheckDesignDate() {
		return secondCheckDesignDate;
	}
	public void setSecondCheckDesignDate(Date secondCheckDesignDate) {
		this.secondCheckDesignDate = secondCheckDesignDate;
	}
	public String getFirstCheckResult() {
		return firstCheckResult;
	}
	public void setFirstCheckResult(String firstCheckResult) {
		this.firstCheckResult = firstCheckResult;
	}
	public String getSecondCheckResult() {
		return secondCheckResult;
	}
	public void setSecondCheckResult(String secondCheckResult) {
		this.secondCheckResult = secondCheckResult;
	}
	public String getSecondCheckFile() {
		return secondCheckFile;
	}
	public void setSecondCheckFile(String secondCheckFile) {
		this.secondCheckFile = secondCheckFile;
	}
	public String getImproveFile() {
		return improveFile;
	}
	public void setImproveFile(String improveFile) {
		this.improveFile = improveFile;
	}
	public String getProblemState() {
		return problemState;
	}
	public void setProblemState(String problemState) {
		this.problemState = problemState;
	}
	public Date getProblemCloseDate() {
		return problemCloseDate;
	}
	public void setProblemCloseDate(Date problemCloseDate) {
		this.problemCloseDate = problemCloseDate;
	}
	public Date getFirstCheckDate() {
		return firstCheckDate;
	}
	public void setFirstCheckDate(Date firstCheckDate) {
		this.firstCheckDate = firstCheckDate;
	}
	public Date getSecondCheckDate() {
		return secondCheckDate;
	}
	public void setSecondCheckDate(Date secondCheckDate) {
		this.secondCheckDate = secondCheckDate;
	}
	
}

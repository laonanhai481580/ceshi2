package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;
/**
 * 类名:合格供应商取消流程
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2016年10月25日 发布
 */
/**
 * 类名:合格供应商取消流程
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2016年10月25日 发布
 */
@Entity
@Table(name = "SUPPLIER_CANCLE")
public class SupplierCancle extends  WorkflowIdEntity {

	private static final long serialVersionUID = 1L;
	private String formNo;
	private String formState="正常";//状态 正常，作废  
	private Long supplierId;//申请取消的供应商id
	private String supplierName;
	private String supplierCode;
	private String supplyMaterial;//供应物料
	private String materialType;//材料类型
	private Date supplierEnterDate;//合格供应商准入时间
	private Date cancleDate;//申请取消时间
	private String applyDept;//申请部门
	private String applyMan;//申请人
	private String problemDesc;//问题点描述及影响范围
    private String purchaseProcesser;//采购经办人
    private String purchaseProcesserLog;//
    private String purchaseChecker;//采购审核人
    private String purchaseCheckerLog;//
    private Double payMoney;//应付账款
    private String financeProcesser;//财务经办人
    private String financeProcesserLog;
    private String financeChecker;//财务审核人
    private String financeCheckerLog;
    private Double pmcStockAmount;//库存数量
    private Double pmcLineAmount;//
    private String pmcProcesser;//物控经办人
    private String pmcProcesserLog;
    private String pmcChecker;//物控审核人
    private String pmcCheckerLog;//
    private String stockInsepctionState;//iqc库存品检验状态
    private String sqmProcesser;//经办人
    private String sqmProcesserLog;
    private String sqmChecker;//审核
    private String sqmCheckerLog;
    private String sqeProcessResult;//sqe处理结果
    private String sqeProcesser;//sqe处理人
    private String sqeProcesserLog;
    private String sqeChecker;//sqe审核人
    private String sqeCheckerLog;
    private String managerIdeal;//最终意见
    private String managerName;//总经理
    private String managerLog;
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplyMaterial() {
		return supplyMaterial;
	}
	public void setSupplyMaterial(String supplyMaterial) {
		this.supplyMaterial = supplyMaterial;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public Date getSupplierEnterDate() {
		return supplierEnterDate;
	}
	public void setSupplierEnterDate(Date supplierEnterDate) {
		this.supplierEnterDate = supplierEnterDate;
	}
	public Date getCancleDate() {
		return cancleDate;
	}
	public void setCancleDate(Date cancleDate) {
		this.cancleDate = cancleDate;
	}
	public String getApplyDept() {
		return applyDept;
	}
	public void setApplyDept(String applyDept) {
		this.applyDept = applyDept;
	}
	public String getApplyMan() {
		return applyMan;
	}
	public void setApplyMan(String applyMan) {
		this.applyMan = applyMan;
	}
	public String getProblemDesc() {
		return problemDesc;
	}
	public void setProblemDesc(String problemDesc) {
		this.problemDesc = problemDesc;
	}
	public String getPurchaseProcesser() {
		return purchaseProcesser;
	}
	public void setPurchaseProcesser(String purchaseProcesser) {
		this.purchaseProcesser = purchaseProcesser;
	}
	public String getPurchaseProcesserLog() {
		return purchaseProcesserLog;
	}
	public void setPurchaseProcesserLog(String purchaseProcesserLog) {
		this.purchaseProcesserLog = purchaseProcesserLog;
	}
	public String getPurchaseChecker() {
		return purchaseChecker;
	}
	public void setPurchaseChecker(String purchaseChecker) {
		this.purchaseChecker = purchaseChecker;
	}
	public String getPurchaseCheckerLog() {
		return purchaseCheckerLog;
	}
	public void setPurchaseCheckerLog(String purchaseCheckerLog) {
		this.purchaseCheckerLog = purchaseCheckerLog;
	}
	public Double getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}
	public String getFinanceProcesser() {
		return financeProcesser;
	}
	public void setFinanceProcesser(String financeProcesser) {
		this.financeProcesser = financeProcesser;
	}
	public String getFinanceProcesserLog() {
		return financeProcesserLog;
	}
	public void setFinanceProcesserLog(String financeProcesserLog) {
		this.financeProcesserLog = financeProcesserLog;
	}
	public String getFinanceChecker() {
		return financeChecker;
	}
	public void setFinanceChecker(String financeChecker) {
		this.financeChecker = financeChecker;
	}
	public String getFinanceCheckerLog() {
		return financeCheckerLog;
	}
	public void setFinanceCheckerLog(String financeCheckerLog) {
		this.financeCheckerLog = financeCheckerLog;
	}
	public Double getPmcStockAmount() {
		return pmcStockAmount;
	}
	public void setPmcStockAmount(Double pmcStockAmount) {
		this.pmcStockAmount = pmcStockAmount;
	}
	public Double getPmcLineAmount() {
		return pmcLineAmount;
	}
	public void setPmcLineAmount(Double pmcLineAmount) {
		this.pmcLineAmount = pmcLineAmount;
	}
	public String getPmcProcesser() {
		return pmcProcesser;
	}
	public void setPmcProcesser(String pmcProcesser) {
		this.pmcProcesser = pmcProcesser;
	}
	public String getPmcProcesserLog() {
		return pmcProcesserLog;
	}
	public void setPmcProcesserLog(String pmcProcesserLog) {
		this.pmcProcesserLog = pmcProcesserLog;
	}
	public String getPmcChecker() {
		return pmcChecker;
	}
	public void setPmcChecker(String pmcChecker) {
		this.pmcChecker = pmcChecker;
	}
	public String getPmcCheckerLog() {
		return pmcCheckerLog;
	}
	public void setPmcCheckerLog(String pmcCheckerLog) {
		this.pmcCheckerLog = pmcCheckerLog;
	}
	public String getStockInsepctionState() {
		return stockInsepctionState;
	}
	public void setStockInsepctionState(String stockInsepctionState) {
		this.stockInsepctionState = stockInsepctionState;
	}
	public String getSqmProcesser() {
		return sqmProcesser;
	}
	public void setSqmProcesser(String sqmProcesser) {
		this.sqmProcesser = sqmProcesser;
	}
	public String getSqmProcesserLog() {
		return sqmProcesserLog;
	}
	public void setSqmProcesserLog(String sqmProcesserLog) {
		this.sqmProcesserLog = sqmProcesserLog;
	}
	public String getSqmChecker() {
		return sqmChecker;
	}
	public void setSqmChecker(String sqmChecker) {
		this.sqmChecker = sqmChecker;
	}
	public String getSqmCheckerLog() {
		return sqmCheckerLog;
	}
	public void setSqmCheckerLog(String sqmCheckerLog) {
		this.sqmCheckerLog = sqmCheckerLog;
	}
	public String getSqeProcessResult() {
		return sqeProcessResult;
	}
	public void setSqeProcessResult(String sqeProcessResult) {
		this.sqeProcessResult = sqeProcessResult;
	}
	public String getSqeProcesser() {
		return sqeProcesser;
	}
	public void setSqeProcesser(String sqeProcesser) {
		this.sqeProcesser = sqeProcesser;
	}
	public String getSqeProcesserLog() {
		return sqeProcesserLog;
	}
	public void setSqeProcesserLog(String sqeProcesserLog) {
		this.sqeProcesserLog = sqeProcesserLog;
	}
	public String getSqeChecker() {
		return sqeChecker;
	}
	public void setSqeChecker(String sqeChecker) {
		this.sqeChecker = sqeChecker;
	}
	public String getSqeCheckerLog() {
		return sqeCheckerLog;
	}
	public void setSqeCheckerLog(String sqeCheckerLog) {
		this.sqeCheckerLog = sqeCheckerLog;
	}
	public String getManagerIdeal() {
		return managerIdeal;
	}
	public void setManagerIdeal(String managerIdeal) {
		this.managerIdeal = managerIdeal;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public String getManagerLog() {
		return managerLog;
	}
	public void setManagerLog(String managerLog) {
		this.managerLog = managerLog;
	}
	public String getFormState() {
		return formState;
	}
	public void setFormState(String formState) {
		this.formState = formState;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
    
}

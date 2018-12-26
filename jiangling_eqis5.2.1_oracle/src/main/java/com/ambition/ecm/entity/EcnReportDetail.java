package com.ambition.ecm.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * 类名:Ecn工程变更明细
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  wuxuming
 * @version 1.00 2016-11-9 发布
 */
@Entity
@Table(name="ECM_ECN_REPORT_DETAIL")
public class EcnReportDetail extends IdEntity {
	
	private static final long serialVersionUID = 1L;
	private String beforeCode;
	private String beforeName;
	private String afterCode;
	private String afterName;
	private String describe;//描述
	private String handle;//处理方式
	private String produceHandle;//工程处理方式
	//PMC
	private Integer bomAmount;//库存数
	private Integer productAmount;//成品库存数
	private Integer rawAmount;//原材料
	private Integer preAmount;//半成品
	private Integer finishAmount;//成品
	private String pmcRemark;//PMC备注
	//调达
	private Integer waitAmount;//待交量
	private String waitResult;
	private Integer supplierAmount;//供应商在制量
	private String supplierResult;
	private Integer supStockAmount;//供应商库存量
	private String supStockResult;
	private Integer supOtherAmount;//其他
	private String supOtherResult;
	private String isSupCanle;//是否取消
	private String supRemark;//备注
	//采购
	private Integer purWaitAmount;//待交量
	private String purWaitResult;
	private Integer purSupAmount;//供应商在制量
	private String purSupResult;
	private Integer purStockAmount;//供应商库存量
	private String purStockResult;
	private Integer purOtherAmount;//其他
	private String purOtherResult;
	private String isPurCanle;//是否取消
	private String purRemark;//备注
	//生产
	private String mfgResult;//在制品处理
	private String mfgRemark;//备注
	private String lastMfgResult;//在制品 处置结果
	private String lastMfgRemark;//最终备注
	@ManyToOne
	@JoinColumn(name="FK_ECN_REPORT")
	@JsonIgnore()
	private EcnReport ecnReport;
	
	public String getBeforeCode() {
		return beforeCode;
	}
	public void setBeforeCode(String beforeCode) {
		this.beforeCode = beforeCode;
	}
	public String getBeforeName() {
		return beforeName;
	}
	public void setBeforeName(String beforeName) {
		this.beforeName = beforeName;
	}
	public String getAfterCode() {
		return afterCode;
	}
	public void setAfterCode(String afterCode) {
		this.afterCode = afterCode;
	}
	public String getAfterName() {
		return afterName;
	}
	public void setAfterName(String afterName) {
		this.afterName = afterName;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getHandle() {
		return handle;
	}
	public void setHandle(String handle) {
		this.handle = handle;
	}
	public Integer getBomAmount() {
		return bomAmount;
	}
	public void setBomAmount(Integer bomAmount) {
		this.bomAmount = bomAmount;
	}
	public Integer getProductAmount() {
		return productAmount;
	}
	public void setProductAmount(Integer productAmount) {
		this.productAmount = productAmount;
	}
	public Integer getRawAmount() {
		return rawAmount;
	}
	public void setRawAmount(Integer rawAmount) {
		this.rawAmount = rawAmount;
	}
	public Integer getPreAmount() {
		return preAmount;
	}
	public void setPreAmount(Integer preAmount) {
		this.preAmount = preAmount;
	}
	public Integer getFinishAmount() {
		return finishAmount;
	}
	public void setFinishAmount(Integer finishAmount) {
		this.finishAmount = finishAmount;
	}
	public String getPmcRemark() {
		return pmcRemark;
	}
	public void setPmcRemark(String pmcRemark) {
		this.pmcRemark = pmcRemark;
	}
	public Integer getWaitAmount() {
		return waitAmount;
	}
	public void setWaitAmount(Integer waitAmount) {
		this.waitAmount = waitAmount;
	}
	public String getWaitResult() {
		return waitResult;
	}
	public void setWaitResult(String waitResult) {
		this.waitResult = waitResult;
	}
	public Integer getSupplierAmount() {
		return supplierAmount;
	}
	public void setSupplierAmount(Integer supplierAmount) {
		this.supplierAmount = supplierAmount;
	}
	public String getSupplierResult() {
		return supplierResult;
	}
	public void setSupplierResult(String supplierResult) {
		this.supplierResult = supplierResult;
	}
	public Integer getSupStockAmount() {
		return supStockAmount;
	}
	public void setSupStockAmount(Integer supStockAmount) {
		this.supStockAmount = supStockAmount;
	}
	public String getSupStockResult() {
		return supStockResult;
	}
	public void setSupStockResult(String supStockResult) {
		this.supStockResult = supStockResult;
	}
	public Integer getSupOtherAmount() {
		return supOtherAmount;
	}
	public void setSupOtherAmount(Integer supOtherAmount) {
		this.supOtherAmount = supOtherAmount;
	}
	public String getSupOtherResult() {
		return supOtherResult;
	}
	public void setSupOtherResult(String supOtherResult) {
		this.supOtherResult = supOtherResult;
	}
	public String getIsSupCanle() {
		return isSupCanle;
	}
	public void setIsSupCanle(String isSupCanle) {
		this.isSupCanle = isSupCanle;
	}
	public String getSupRemark() {
		return supRemark;
	}
	public void setSupRemark(String supRemark) {
		this.supRemark = supRemark;
	}
	public Integer getPurWaitAmount() {
		return purWaitAmount;
	}
	public void setPurWaitAmount(Integer purWaitAmount) {
		this.purWaitAmount = purWaitAmount;
	}
	public String getPurWaitResult() {
		return purWaitResult;
	}
	public void setPurWaitResult(String purWaitResult) {
		this.purWaitResult = purWaitResult;
	}
	public Integer getPurSupAmount() {
		return purSupAmount;
	}
	public void setPurSupAmount(Integer purSupAmount) {
		this.purSupAmount = purSupAmount;
	}
	public String getPurSupResult() {
		return purSupResult;
	}
	public void setPurSupResult(String purSupResult) {
		this.purSupResult = purSupResult;
	}
	public Integer getPurStockAmount() {
		return purStockAmount;
	}
	public void setPurStockAmount(Integer purStockAmount) {
		this.purStockAmount = purStockAmount;
	}
	public String getPurStockResult() {
		return purStockResult;
	}
	public void setPurStockResult(String purStockResult) {
		this.purStockResult = purStockResult;
	}
	public Integer getPurOtherAmount() {
		return purOtherAmount;
	}
	public void setPurOtherAmount(Integer purOtherAmount) {
		this.purOtherAmount = purOtherAmount;
	}
	public String getPurOtherResult() {
		return purOtherResult;
	}
	public void setPurOtherResult(String purOtherResult) {
		this.purOtherResult = purOtherResult;
	}
	public String getIsPurCanle() {
		return isPurCanle;
	}
	public void setIsPurCanle(String isPurCanle) {
		this.isPurCanle = isPurCanle;
	}
	public String getPurRemark() {
		return purRemark;
	}
	public void setPurRemark(String purRemark) {
		this.purRemark = purRemark;
	}
	public String getMfgResult() {
		return mfgResult;
	}
	public void setMfgResult(String mfgResult) {
		this.mfgResult = mfgResult;
	}
	public String getLastMfgResult() {
		return lastMfgResult;
	}
	public void setLastMfgResult(String lastMfgResult) {
		this.lastMfgResult = lastMfgResult;
	}
	public String getLastMfgRemark() {
		return lastMfgRemark;
	}
	public void setLastMfgRemark(String lastMfgRemark) {
		this.lastMfgRemark = lastMfgRemark;
	}
	
	public EcnReport getEcnReport() {
		return ecnReport;
	}
	public void setEcnReport(EcnReport ecnReport) {
		this.ecnReport = ecnReport;
	}
	public String getProduceHandle() {
		return produceHandle;
	}
	public void setProduceHandle(String produceHandle) {
		this.produceHandle = produceHandle;
	}
	public String getMfgRemark() {
		return mfgRemark;
	}
	public void setMfgRemark(String mfgRemark) {
		this.mfgRemark = mfgRemark;
	}
	
	
	
}

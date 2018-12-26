package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
/**
 * 类名:上线异常数据
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年11月5日 发布
 */
@Entity
@Table(name = "SUPPLIER_ABNORMAL")
public class SupplierAbnormal  extends IdEntity{

	private Date enterDate;//录入时间
	private String factory;//厂区
	private String materialType;//材料类别
	private String itemNumber;//物料编号
	private String supplierName;//供应商
	private String modelType;//型号
	private String abnormalDesc;//异常描述
	private Integer checkAmount;//抽检数
	private Integer unqualifiedAmount;//不良数
	private String unqualifiedRate;//不良率
	private String productionChecker;//生产确认人
	private String processResult;//处理结果
	private String reportFile;//附件
	
	public Date getEnterDate() {
		return enterDate;
	}
	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getModelType() {
		return modelType;
	}
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
	public String getAbnormalDesc() {
		return abnormalDesc;
	}
	public void setAbnormalDesc(String abnormalDesc) {
		this.abnormalDesc = abnormalDesc;
	}
	public Integer getCheckAmount() {
		return checkAmount;
	}
	public void setCheckAmount(Integer checkAmount) {
		this.checkAmount = checkAmount;
	}
	public Integer getUnqualifiedAmount() {
		return unqualifiedAmount;
	}
	public void setUnqualifiedAmount(Integer unqualifiedAmount) {
		this.unqualifiedAmount = unqualifiedAmount;
	}

	public String getUnqualifiedRate() {
		return unqualifiedRate;
	}
	public void setUnqualifiedRate(String unqualifiedRate) {
		this.unqualifiedRate = unqualifiedRate;
	}
	public String getProductionChecker() {
		return productionChecker;
	}
	public void setProductionChecker(String productionChecker) {
		this.productionChecker = productionChecker;
	}
	public String getProcessResult() {
		return processResult;
	}
	public void setProcessResult(String processResult) {
		this.processResult = processResult;
	}
	public String getReportFile() {
		return reportFile;
	}
	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	
	
}

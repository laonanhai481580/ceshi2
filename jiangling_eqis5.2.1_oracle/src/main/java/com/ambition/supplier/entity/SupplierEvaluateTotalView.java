package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;


/**
 * 类名:供应商评价汇总表
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年12月9日 发布
 */
@Entity
@Table(name = "SUPPLIER_EVALUATE_TOTAL_VIEW")
public class SupplierEvaluateTotalView extends IdEntity {

		/**
		  *SupplierEvaluateTotalView.java2016年12月9日
		 */
	private static final long serialVersionUID = 1L;
	private String allName;
	private String supplierName;
	private String supplierCode;//供应商编号
	private Long supplierId;//供应商的id
	private String materialType;
	private Integer evaluateYear;//评价年份
	private Integer evaluateMonth;//评价月份
	private Double quality;//质量
	private Double develop;//研发
	private Double purche;//采购
	private Double console;//物控调达
	private Double total;//总分
	private String grade;//级别
	public String getAllName() {
		return allName;
	}
	public void setAllName(String allName) {
		this.allName = allName;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public Integer getEvaluateYear() {
		return evaluateYear;
	}
	public void setEvaluateYear(Integer evaluateYear) {
		this.evaluateYear = evaluateYear;
	}
	public Integer getEvaluateMonth() {
		return evaluateMonth;
	}
	public void setEvaluateMonth(Integer evaluateMonth) {
		this.evaluateMonth = evaluateMonth;
	}
	public Double getQuality() {
		return quality;
	}
	public void setQuality(Double quality) {
		this.quality = quality;
	}
	public Double getDevelop() {
		return develop;
	}
	public void setDevelop(Double develop) {
		this.develop = develop;
	}
	public Double getPurche() {
		return purche;
	}
	public void setPurche(Double purche) {
		this.purche = purche;
	}
	public Double getConsole() {
		return console;
	}
	public void setConsole(Double console) {
		this.console = console;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	
}

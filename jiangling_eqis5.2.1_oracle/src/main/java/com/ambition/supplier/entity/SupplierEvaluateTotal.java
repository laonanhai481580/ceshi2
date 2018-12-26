package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;


/**
 * 类名:供应商评价总分记录表
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年12月12日 发布
 */
@Entity
@Table(name = "SUPPLIER_EVALUATE_TOTAL")
public class SupplierEvaluateTotal extends IdEntity{
		/**
		  *SupplierEvaluateTotal.java2016年12月12日
		 */
	private static final long serialVersionUID = 1L;
	private String supplierName;
	private String cycle;//评价周期
	private Long supplierId;//供应商的编号
	private String materialType;
	private Integer evaluateYear;//评价年份
	private Integer evaluateMonth;//评价月份
	private Date evaluateDate;//评价日期
	private String grade;//级别
	private Double total;//总分
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
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public Date getEvaluateDate() {
		return evaluateDate;
	}
	public void setEvaluateDate(Date evaluateDate) {
		this.evaluateDate = evaluateDate;
	}
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	
}

/**   
 * @Title: PerformanceEvaluationRank.java 
 * @Package com.ambition.supplier.entity 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 刘承斌   
 * @date 2015-3-23 下午5:30:40 
 * @version V1.0   
 */ 
package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 类名: PerformanceEvaluationRank 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：供应商评价排名</p>
 * @author  刘承斌
 * @version 1.00  2015-3-23 下午5:30:40  发布
 */
@Entity
@Table(name="SUPPLIER_PERFORMANCE_EVALUATE")
public class PerformanceEvaluate {
	@Id
	@GenericGenerator(name = "imatrixEntityIdGenerator", strategy = "native")
	@GeneratedValue(generator = "imatrixEntityIdGenerator")
	private Long id;
	private Long companyId;
	private String supplierName;//供应商名称
	private Date evaluateDate;//评价周期
	private Integer ppm;//PPM
	private Integer complaintsNo;//投诉次数
	private Integer eightdTiming;//8D按时
	private Integer quantityAccuracyRate;//数量准确率
	private Integer arrivalAccuracyRate;//时间准确率
	private Integer preminuFreight;//超额运费
	private Integer priceCompetitiveness;//价格逐年竞争力
	private Integer realTotalPoint;//总分
	private String sqeDepartmentName;//事业部
	private String parentModelId;//评价模型
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public Date getEvaluateDate() {
		return evaluateDate;
	}
	public void setEvaluateDate(Date evaluateDate) {
		this.evaluateDate = evaluateDate;
	}
	public Integer getPpm() {
		return ppm;
	}
	public void setPpm(Integer ppm) {
		this.ppm = ppm;
	}
	public Integer getComplaintsNo() {
		return complaintsNo;
	}
	public void setComplaintsNo(Integer complaintsNo) {
		this.complaintsNo = complaintsNo;
	}
	public Integer getEightdTiming() {
		return eightdTiming;
	}
	public void setEightdTiming(Integer eightdTiming) {
		this.eightdTiming = eightdTiming;
	}
	public Integer getQuantityAccuracyRate() {
		return quantityAccuracyRate;
	}
	public void setQuantityAccuracyRate(Integer quantityAccuracyRate) {
		this.quantityAccuracyRate = quantityAccuracyRate;
	}
	public Integer getArrivalAccuracyRate() {
		return arrivalAccuracyRate;
	}
	public void setArrivalAccuracyRate(Integer arrivalAccuracyRate) {
		this.arrivalAccuracyRate = arrivalAccuracyRate;
	}
	public Integer getPreminuFreight() {
		return preminuFreight;
	}
	public void setPreminuFreight(Integer preminuFreight) {
		this.preminuFreight = preminuFreight;
	}
	public Integer getPriceCompetitiveness() {
		return priceCompetitiveness;
	}
	public void setPriceCompetitiveness(Integer priceCompetitiveness) {
		this.priceCompetitiveness = priceCompetitiveness;
	}
	public Integer getRealTotalPoint() {
		return realTotalPoint;
	}
	public void setRealTotalPoint(Integer realTotalPoint) {
		this.realTotalPoint = realTotalPoint;
	}
	public String getSqeDepartmentName() {
		return sqeDepartmentName;
	}
	public void setSqeDepartmentName(String sqeDepartmentName) {
		this.sqeDepartmentName = sqeDepartmentName;
	}
	public String getParentModelId() {
		return parentModelId;
	}
	public void setParentModelId(String parentModelId) {
		this.parentModelId = parentModelId;
	}

}

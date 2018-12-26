package com.ambition.supplier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 类名:供应商QCDS数据
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2013-6-18 发布
 */
@Entity
@Table(name = "SUPPLIER_QCDS")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class SupplierQcds extends IdEntity {
	private static final long serialVersionUID = 4518308827240189057L;
	private Integer evaluateYear;//评价年份
	private Integer evaluateMonth;//评价月份
	private String code;//供应商编号 
	private String name;//供应商名称
	@Column(length=1600)
	private String supplyProducts;//主供物料
	private Double total;//得分
	private Double realTotal;//实际得分
	private Double percentageTotal;//百分制实际得分
	private Integer evaluateTimes;//评分次数
	private String grade;//等级
	private Long evaluate1Id;//评分项1ID
	private String evaluate1Name;//评分项2名称
	private Double evaluate1Weight;//评分项1占比
	private Double evaluate1Total;//评分项1得分
	private Double evaluate1RealTotal;//评分项1实际得分
	private Long evaluate2Id;//评分项1ID
	private String evaluate2Name;//评分项2名称
	private Double evaluate2Total;//评分项1得分
	private Double evaluate2Weight;//评分项2占比
	private Double evaluate2RealTotal;//评分项1实际得分
	private Long evaluate3Id;//评分项1ID
	private String evaluate3Name;//评分项2名称
	private Double evaluate3Total;//评分项1得分
	private Double evaluate3Weight;//评分项3占比
	private Double evaluate3RealTotal;//评分项1实际得分
	private Long evaluate4Id;//评分项1ID
	private String evaluate4Name;//评分项2名称
	private Double evaluate4Total;//评分项1得分
	private Double evaluate4Weight;//评分项4占比
	private Double evaluate4RealTotal;//评分项1实际得分
	private Long evaluate5Id;//评分项1ID
	private String evaluate5Name;//评分项2名称
	private Double evaluate5Total;//评分项1得分
	private Double evaluate5Weight;//评分项5占比
	private Double evaluate5RealTotal;//评分项1实际得分
	private Long evaluate6Id;//评分项1ID
	private String evaluate6Name;//评分项2名称
	private Double evaluate6Weight;//评分项6占比
	private Double evaluate6Total;//评分项1得分
	private Double evaluate6RealTotal;//评分项1实际得分
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
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Double getRealTotal() {
		return realTotal;
	}
	public void setRealTotal(Double realTotal) {
		this.realTotal = realTotal;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public Long getEvaluate1Id() {
		return evaluate1Id;
	}
	public void setEvaluate1Id(Long evaluate1Id) {
		this.evaluate1Id = evaluate1Id;
	}
	public String getEvaluate1Name() {
		return evaluate1Name;
	}
	public void setEvaluate1Name(String evaluate1Name) {
		this.evaluate1Name = evaluate1Name;
	}
	public Double getEvaluate1Total() {
		return evaluate1Total;
	}
	public void setEvaluate1Total(Double evaluate1Total) {
		this.evaluate1Total = evaluate1Total;
	}
	public Double getEvaluate1RealTotal() {
		return evaluate1RealTotal;
	}
	public void setEvaluate1RealTotal(Double evaluate1RealTotal) {
		this.evaluate1RealTotal = evaluate1RealTotal;
	}
	public Long getEvaluate2Id() {
		return evaluate2Id;
	}
	public void setEvaluate2Id(Long evaluate2Id) {
		this.evaluate2Id = evaluate2Id;
	}
	public String getEvaluate2Name() {
		return evaluate2Name;
	}
	public void setEvaluate2Name(String evaluate2Name) {
		this.evaluate2Name = evaluate2Name;
	}
	public Double getEvaluate2Total() {
		return evaluate2Total;
	}
	public void setEvaluate2Total(Double evaluate2Total) {
		this.evaluate2Total = evaluate2Total;
	}
	public Double getEvaluate2RealTotal() {
		return evaluate2RealTotal;
	}
	public void setEvaluate2RealTotal(Double evaluate2RealTotal) {
		this.evaluate2RealTotal = evaluate2RealTotal;
	}
	public Long getEvaluate3Id() {
		return evaluate3Id;
	}
	public void setEvaluate3Id(Long evaluate3Id) {
		this.evaluate3Id = evaluate3Id;
	}
	public String getEvaluate3Name() {
		return evaluate3Name;
	}
	public void setEvaluate3Name(String evaluate3Name) {
		this.evaluate3Name = evaluate3Name;
	}
	public Double getEvaluate3Total() {
		return evaluate3Total;
	}
	public void setEvaluate3Total(Double evaluate3Total) {
		this.evaluate3Total = evaluate3Total;
	}
	public Double getEvaluate3RealTotal() {
		return evaluate3RealTotal;
	}
	public void setEvaluate3RealTotal(Double evaluate3RealTotal) {
		this.evaluate3RealTotal = evaluate3RealTotal;
	}
	public Long getEvaluate4Id() {
		return evaluate4Id;
	}
	public void setEvaluate4Id(Long evaluate4Id) {
		this.evaluate4Id = evaluate4Id;
	}
	public String getEvaluate4Name() {
		return evaluate4Name;
	}
	public void setEvaluate4Name(String evaluate4Name) {
		this.evaluate4Name = evaluate4Name;
	}
	public Double getEvaluate4Total() {
		return evaluate4Total;
	}
	public void setEvaluate4Total(Double evaluate4Total) {
		this.evaluate4Total = evaluate4Total;
	}
	public Double getEvaluate4RealTotal() {
		return evaluate4RealTotal;
	}
	public void setEvaluate4RealTotal(Double evaluate4RealTotal) {
		this.evaluate4RealTotal = evaluate4RealTotal;
	}
	public Long getEvaluate5Id() {
		return evaluate5Id;
	}
	public void setEvaluate5Id(Long evaluate5Id) {
		this.evaluate5Id = evaluate5Id;
	}
	public String getEvaluate5Name() {
		return evaluate5Name;
	}
	public void setEvaluate5Name(String evaluate5Name) {
		this.evaluate5Name = evaluate5Name;
	}
	public Double getEvaluate5Total() {
		return evaluate5Total;
	}
	public void setEvaluate5Total(Double evaluate5Total) {
		this.evaluate5Total = evaluate5Total;
	}
	public Double getEvaluate5RealTotal() {
		return evaluate5RealTotal;
	}
	public void setEvaluate5RealTotal(Double evaluate5RealTotal) {
		this.evaluate5RealTotal = evaluate5RealTotal;
	}
	public Long getEvaluate6Id() {
		return evaluate6Id;
	}
	public void setEvaluate6Id(Long evaluate6Id) {
		this.evaluate6Id = evaluate6Id;
	}
	public String getEvaluate6Name() {
		return evaluate6Name;
	}
	public void setEvaluate6Name(String evaluate6Name) {
		this.evaluate6Name = evaluate6Name;
	}
	public Double getEvaluate6Total() {
		return evaluate6Total;
	}
	public void setEvaluate6Total(Double evaluate6Total) {
		this.evaluate6Total = evaluate6Total;
	}
	public Double getEvaluate6RealTotal() {
		return evaluate6RealTotal;
	}
	public void setEvaluate6RealTotal(Double evaluate6RealTotal) {
		this.evaluate6RealTotal = evaluate6RealTotal;
	}
	public Integer getEvaluateTimes() {
		return evaluateTimes;
	}
	public void setEvaluateTimes(Integer evaluateTimes) {
		this.evaluateTimes = evaluateTimes;
	}
	public Double getPercentageTotal() {
		return percentageTotal;
	}
	public void setPercentageTotal(Double percentageTotal) {
		this.percentageTotal = percentageTotal;
	}
	public String getSupplyProducts() {
		return supplyProducts;
	}
	public void setSupplyProducts(String supplyProducts) {
		this.supplyProducts = supplyProducts;
	}
	public Double getEvaluate1Weight() {
		return evaluate1Weight;
	}
	public void setEvaluate1Weight(Double evaluate1Weight) {
		this.evaluate1Weight = evaluate1Weight;
	}
	public Double getEvaluate2Weight() {
		return evaluate2Weight;
	}
	public void setEvaluate2Weight(Double evaluate2Weight) {
		this.evaluate2Weight = evaluate2Weight;
	}
	public Double getEvaluate3Weight() {
		return evaluate3Weight;
	}
	public void setEvaluate3Weight(Double evaluate3Weight) {
		this.evaluate3Weight = evaluate3Weight;
	}
	public Double getEvaluate4Weight() {
		return evaluate4Weight;
	}
	public void setEvaluate4Weight(Double evaluate4Weight) {
		this.evaluate4Weight = evaluate4Weight;
	}
	public Double getEvaluate5Weight() {
		return evaluate5Weight;
	}
	public void setEvaluate5Weight(Double evaluate5Weight) {
		this.evaluate5Weight = evaluate5Weight;
	}
	public Double getEvaluate6Weight() {
		return evaluate6Weight;
	}
	public void setEvaluate6Weight(Double evaluate6Weight) {
		this.evaluate6Weight = evaluate6Weight;
	}
}

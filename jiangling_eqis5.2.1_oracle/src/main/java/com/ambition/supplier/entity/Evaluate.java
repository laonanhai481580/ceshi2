package com.ambition.supplier.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;
import com.ambition.util.common.CommonUtil;

/**
 * 供应商评价
 * @author ambition-zhaojun
 *
 */
@Entity
@Table(name = "SUPPLIER_EVALUATE")
public class Evaluate extends IdEntity {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static final long serialVersionUID = 1L;
	private Long estimateModelId;//子模型编号
	private String estimateModelName;//评价模型的名称
	private String materialType;//物料类别
	private String cycle;//评价周期
	private Integer startMonth;//评价的起始月
	private Long parentModelId;//总模型的编号
	private Long supplierId;//供应商的编号
	private String supplierName;//供应商的名称
	private String evaluateNo;//评价表单编号
	@Column(length=300)
	private String supplyProducts;//供应的产品
	private Double totalPoints;//总分
	private Double realTotalPoints;//实际得分
	private Date evaluateDate;//评价日期
	private Integer evaluateYear;//评价年份
	private Integer evaluateMonth;//评价月份
	private Date reportDate;//报告日期
	private String writeMan;//填写人
	private Date writeDate;//填写日期
	private String auditMan;//审核人员
	private Date auditDate;//审核日期
	private String approvalMan;//审批人员
	private Date approvalDate;//审批日期
    private String description;//描述主要表现及改进要求
    private String sqeDepartmentName;//事业部
    
    @OneToMany(mappedBy="evaluate",cascade={CascadeType.ALL})
    @Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<EvaluateDetail> evaluateDetails;//评价的指标明细
	
    private String isSubmit="否";//是否提交
    
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public Integer getStartMonth() {
		return startMonth;
	}
	public void setStartMonth(Integer startMonth) {
		this.startMonth = startMonth;
	}
	public String getSupplyProducts() {
		return supplyProducts;
	}
	public void setSupplyProducts(String supplyProducts) {
		this.supplyProducts = supplyProducts;
	}
	
	public Date getEvaluateDate() {
		return evaluateDate;
	}
	
	@Transient
	public String getEvaluateDateStr(){
		if(evaluateDate == null){
			return "";
		}else{
			return sdf.format(evaluateDate);
		}
	}
	public void setEvaluateDate(Date evaluateDate) {
		this.evaluateDate = evaluateDate;
		if(evaluateDate != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(evaluateDate);
			this.evaluateMonth = calendar.get(Calendar.MONTH)+1;
			this.evaluateYear = calendar.get(Calendar.YEAR);
		}
		//增加年月日季周统计
		if(null != evaluateDate){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(evaluateDate);
			
			this.setYear(CommonUtil.getYear(calendar));
			this.setMonthOfYear(CommonUtil.getMonth(calendar));
			this.setYearAndMonth(CommonUtil.getYearAndMonth(calendar));
			this.setDateOfMonthOfYear(CommonUtil.getDate(calendar));
			this.setYearAndMonthAndDate(CommonUtil.getYearMonthAndDate(calendar));
			this.setWeekOfYear(CommonUtil.getWeek(calendar));
			this.setYearAndWeek(CommonUtil.getYearAndWeek(calendar));
			this.setQuarterOfYear(CommonUtil.getQuarter(calendar));
			this.setYearAndQuarter(CommonUtil.getYearAndQuarter(calendar));
		}
	}
	public Date getReportDate() {
		return reportDate;
	}
	@Transient
	public String getReportDateStr(){
		if(reportDate == null){
			return "";
		}else{
			return sdf.format(reportDate);
		}
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public String getWriteMan() {
		return writeMan;
	}
	public void setWriteMan(String writeMan) {
		this.writeMan = writeMan;
	}
	public Date getWriteDate() {
		return writeDate;
	}
	@Transient
	public String getWriteDateStr(){
		if(writeDate == null){
			return "";
		}else{
			return sdf.format(writeDate);
		}
	}
	public void setWriteDate(Date writeDate) {
		this.writeDate = writeDate;
	}
	public String getAuditMan() {
		return auditMan;
	}
	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	@Transient
	public String getAuditDateStr(){
		if(auditDate == null){
			return "";
		}else{
			return sdf.format(auditDate);
		}
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public String getApprovalMan() {
		return approvalMan;
	}
	public void setApprovalMan(String approvalMan) {
		this.approvalMan = approvalMan;
	}
	public Date getApprovalDate() {
		return approvalDate;
	}
	@Transient
	public String getApprovalDateStr(){
		if(approvalDate == null){
			return "";
		}else{
			return sdf.format(approvalDate);
		}
	}
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEvaluateNo() {
		return evaluateNo;
	}
	public void setEvaluateNo(String evaluateNo) {
		this.evaluateNo = evaluateNo;
	}
	
	public Double getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(Double totalPoints) {
		this.totalPoints = totalPoints;
	}
	public List<EvaluateDetail> getEvaluateDetails() {
		return evaluateDetails;
	}
	public void setEvaluateDetails(List<EvaluateDetail> evaluateDetails) {
		this.evaluateDetails = evaluateDetails;
	}
	public Double getRealTotalPoints() {
		return realTotalPoints;
	}
	public void setRealTotalPoints(Double realTotalPoints) {
		this.realTotalPoints = realTotalPoints;
	}
	public Integer getEvaluateMonth() {
		return evaluateMonth;
	}
	public void setEvaluateMonth(Integer evaluateMonth) {
		
	}
	public Long getEstimateModelId() {
		return estimateModelId;
	}
	public void setEstimateModelId(Long estimateModelId) {
		this.estimateModelId = estimateModelId;
	}
	public String getEstimateModelName() {
		return estimateModelName;
	}
	public void setEstimateModelName(String estimateModelName) {
		this.estimateModelName = estimateModelName;
	}
	public Long getParentModelId() {
		return parentModelId;
	}
	public void setParentModelId(Long parentModelId) {
		this.parentModelId = parentModelId;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public Integer getEvaluateYear() {
		return evaluateYear;
	}
	public void setEvaluateYear(Integer evaluateYear) {
	}
	public String toString(){
		return "供应商质量管理：供应商评价  ID"+this.getId()+" 名称"+this.supplierName+" 总分"+this.totalPoints;
	}
	public String getSqeDepartmentName() {
		return sqeDepartmentName;
	}
	public void setSqeDepartmentName(String sqeDepartmentName) {
		this.sqeDepartmentName = sqeDepartmentName;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public String getIsSubmit() {
		return isSubmit;
	}
	public void setIsSubmit(String isSubmit) {
		this.isSubmit = isSubmit;
	}
	
}

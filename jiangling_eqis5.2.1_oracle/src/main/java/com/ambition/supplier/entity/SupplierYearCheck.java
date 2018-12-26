package com.ambition.supplier.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;
import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import flex.messaging.io.ArrayList;
/**
 * 类名:供应商年度稽核
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月26日 发布
 */
/**
 * 类名:供应商年度稽核
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月26日 发布
 */
@Entity
@Table(name = "SUPPLIER_YEAR_CHECK")
public class SupplierYearCheck extends  WorkflowIdEntity{

		/**
		  *SupplierYearCheck.java2016年10月26日
		 */
	private static final long serialVersionUID = 1L;
	private Long supplierId;
	private String supplierName;
	private String supplierCode;
	private String supplyFactory;
	private String currentTransactor;//当前环节办理人
	private String supplyMaterial;//供应物料
	private Integer januaryPlan;//一月计划检查次数 
	private Integer januaryDesign;//一月实际检查次数
	private Integer februaryPlan;//二月计划检查次数
	private Integer februaryDesign;//二月实际检查次数
	private Integer marchPlan;//三月计划检查次数
	private Integer marchDesign;//三月实际检查次数
	private Integer aprilPlan;//四月计划检查次数
	private Integer aprilDesign;//四月实际检查次数
	private Integer mayPlan;//五月计划检查次数
	private Integer mayDesign;//五月实际检查次数
	private Integer junePlan;//六月计划检查次数
	private Integer juneDesign;//六月实际检查次数
	private Integer julyPlan;//七月计划检查次数
	private Integer julyDesign;//七月实际检查次数
	private Integer augustPlan;//八月计划检查次数
	private Integer augustDesign;//八月实际检查次数
	private Integer septemberPlan;//九月计划检查次数
	private Integer septemberDesign;//九月实际检查次数
	private Integer octoberPlan;//十月计划检查次数
	private Integer octoberDesign;//十月实际检查次数
	private Integer novemberPlan;//十一月计划检查次数
	private Integer novemberDesign;//十一月实际检查次数
	private Integer decemberPlan;//十二月计划检查次数
	private Integer decemberDesign;//十二月实际检查次数
	private String checkType;//审核方式
	private Date firstCheckPlanDate;//首次计划日期
	private Date firstCheckDesignDate;//首次实际日期
	private String firstCheckResult;//首次结果
	private String secondCheckResult;//二次结果
	private Date secondCheckPlanDate;//二次计划日期
	private Date secondCheckDesignDate;//二次实际日期
	private Date firstCheckDate;//首次稽核日期
	private Date secondCheckDate;//二次稽核日期
	private String finalCheckResult;//最终稽核结果
	private String checker;//确认人
	private String checkerLog;
	private String checkFile;//稽核附件
	private String secondCheckFile;//二次稽核附件
	private String improveFile;//问题改善附件
	private String remark;//备注
	private String problemState;//问题状态
	private Date problemCloseDate;//问题关闭日期
//	@OneToMany(mappedBy = "supplierYearCheck",cascade=CascadeType.MERGE)
//    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
//    @JsonIgnore()
//	private List<SupplierYearCheckPlan> supplierCheckPlans;
	
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
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
	public Integer getJanuaryPlan() {
		return januaryPlan;
	}
	public void setJanuaryPlan(Integer januaryPlan) {
		this.januaryPlan = januaryPlan;
	}
	public Integer getJanuaryDesign() {
		return januaryDesign;
	}
	public void setJanuaryDesign(Integer januaryDesign) {
		this.januaryDesign = januaryDesign;
	}
	public Integer getFebruaryPlan() {
		return februaryPlan;
	}
	public void setFebruaryPlan(Integer februaryPlan) {
		this.februaryPlan = februaryPlan;
	}
	public Integer getFebruaryDesign() {
		return februaryDesign;
	}
	public void setFebruaryDesign(Integer februaryDesign) {
		this.februaryDesign = februaryDesign;
	}
	public Integer getMarchPlan() {
		return marchPlan;
	}
	public void setMarchPlan(Integer marchPlan) {
		this.marchPlan = marchPlan;
	}
	public Integer getMarchDesign() {
		return marchDesign;
	}
	public void setMarchDesign(Integer marchDesign) {
		this.marchDesign = marchDesign;
	}
	public Integer getAprilPlan() {
		return aprilPlan;
	}
	public void setAprilPlan(Integer aprilPlan) {
		this.aprilPlan = aprilPlan;
	}
	public Integer getAprilDesign() {
		return aprilDesign;
	}
	public void setAprilDesign(Integer aprilDesign) {
		this.aprilDesign = aprilDesign;
	}
	public Integer getMayPlan() {
		return mayPlan;
	}
	public void setMayPlan(Integer mayPlan) {
		this.mayPlan = mayPlan;
	}
	public Integer getMayDesign() {
		return mayDesign;
	}
	public void setMayDesign(Integer mayDesign) {
		this.mayDesign = mayDesign;
	}
	public Integer getJunePlan() {
		return junePlan;
	}
	public void setJunePlan(Integer junePlan) {
		this.junePlan = junePlan;
	}
	public Integer getJuneDesign() {
		return juneDesign;
	}
	public void setJuneDesign(Integer juneDesign) {
		this.juneDesign = juneDesign;
	}
	public Integer getJulyPlan() {
		return julyPlan;
	}
	public void setJulyPlan(Integer julyPlan) {
		this.julyPlan = julyPlan;
	}
	public Integer getJulyDesign() {
		return julyDesign;
	}
	public void setJulyDesign(Integer julyDesign) {
		this.julyDesign = julyDesign;
	}
	public Integer getAugustPlan() {
		return augustPlan;
	}
	public void setAugustPlan(Integer augustPlan) {
		this.augustPlan = augustPlan;
	}
	public Integer getAugustDesign() {
		return augustDesign;
	}
	public void setAugustDesign(Integer augustDesign) {
		this.augustDesign = augustDesign;
	}
	public Integer getSeptemberPlan() {
		return septemberPlan;
	}
	public void setSeptemberPlan(Integer septemberPlan) {
		this.septemberPlan = septemberPlan;
	}
	public Integer getSeptemberDesign() {
		return septemberDesign;
	}
	public void setSeptemberDesign(Integer septemberDesign) {
		this.septemberDesign = septemberDesign;
	}
	public Integer getOctoberPlan() {
		return octoberPlan;
	}
	public void setOctoberPlan(Integer octoberPlan) {
		this.octoberPlan = octoberPlan;
	}
	public Integer getOctoberDesign() {
		return octoberDesign;
	}
	public void setOctoberDesign(Integer octoberDesign) {
		this.octoberDesign = octoberDesign;
	}
	public Integer getNovemberPlan() {
		return novemberPlan;
	}
	public void setNovemberPlan(Integer novemberPlan) {
		this.novemberPlan = novemberPlan;
	}
	public Integer getNovemberDesign() {
		return novemberDesign;
	}
	public void setNovemberDesign(Integer novemberDesign) {
		this.novemberDesign = novemberDesign;
	}
	public Integer getDecemberPlan() {
		return decemberPlan;
	}
	public void setDecemberPlan(Integer decemberPlan) {
		this.decemberPlan = decemberPlan;
	}
	public Integer getDecemberDesign() {
		return decemberDesign;
	}
	public void setDecemberDesign(Integer decemberDesign) {
		this.decemberDesign = decemberDesign;
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
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
//	public List<SupplierYearCheckPlan> getSupplierCheckPlans() {
//		return supplierCheckPlans;
//	}
//	public void setSupplierCheckPlans(List<SupplierYearCheckPlan> supplierCheckPlans) {
//		this.supplierCheckPlans = supplierCheckPlans;
//	}
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
	public String getImproveFile() {
		return improveFile;
	}
	public void setImproveFile(String improveFile) {
		this.improveFile = improveFile;
	}
	public String getCheckerLog() {
		return checkerLog;
	}
	public void setCheckerLog(String checkerLog) {
		this.checkerLog = checkerLog;
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
	public String getSecondCheckFile() {
		return secondCheckFile;
	}
	public void setSecondCheckFile(String secondCheckFile) {
		this.secondCheckFile = secondCheckFile;
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
	public String getCurrentTransactor() {
		return currentTransactor;
	}
	public void setCurrentTransactor(String currentTransactor) {
		this.currentTransactor = currentTransactor;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	
}

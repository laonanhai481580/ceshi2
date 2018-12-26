package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 类名:供应商月度稽核计划
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月27日 发布
 */
@Entity
@Table(name = "SUPPLIER_MONTH_CHECK")
public class SupplierMonthCheck extends IdEntity{

		/**
		  *SupplierMonthCheck.java2016年10月27日
		 */
	private static final long serialVersionUID = 1L;
	private Long supplierId;
	private Long yearCheckId;
	private String supplierName;
	private String supplierCode;
	private String supplyFactory;
	private String supplyMaterial;//供应物料
	private Date firstDayPlan;
	private Date firstDayDesign;
	
	private Date secondDayPlan;
	private Date secondDayDesign;
	
	private Date thirdDayPlan;
	private Date thirdDayDesign;
	
	private Date forthDayPlan;
	private Date forthDayDesign;
	
	private Date fifthDayPlan;
	private Date fifthDayDesign;
	
	private Date sixthDayPlan;
	private Date sixthDayDesign;
	
	private Date seventhDayPlan;
	private Date seventhDayDesign;
	
	private Date eighthDayPlan;
	private Date eighthDayDesign;
	
	private Date ninthDayPlan;
	private Date ninthDayDesign;
	
	private Date tenthDayPlan;
	private Date tenthDayDesign;
	
	private Date eleventhDayPlan;
	private Date eleventhDayDesign;
	
	private Date twelfthDayPlan;
	private Date twelfthDayDesign;
	
	private Date thirtheenthDayPlan;
	private Date thirtheenthDayDesign;
	
	private Date fourtheenthDayPlan;
	private Date fourtheenthDayDesign;
	
	private Date fiftheenthDayPlan;
	private Date fiftheenthDayDesign;
	
	private Date sixtheenthDayPlan;
	private Date sixtheenthDayDesign;
	
	private Date seventeenthDayPlan;
	private Date seventeenthDayDesign;
	
	private Date eighteenthDayPlan;
	private Date eighteenthDayDesign;
	
	private Date nineteenthDayPlan;
	private Date nineteenthDayDesign;
	
	private Date twentiethDayPlan;
	private Date twentiethDayDesign;
	
	private Date twentyFirstDayPlan;
	private Date twentyFirstDayDesign;
	
	private Date twentySecondDayPlan;
	private Date twentySecondDayDesign;
	
	private Date twentyThirdDayPlan;
	private Date twentyThirdDayDesign;
	
	private Date twentyFourthDayPlan;
	private Date twentyFourthDayDesign;
	
	private Date twentyFifthDayPlan;
	private Date twentyFifthDayDesign;
	
	private Date twentySixthDayPlan;
	private Date twentySixthDayDesign;
	
	private Date twentySeventhDayPlan;
	private Date twentySeventhDayDesign;
	
	private Date twentyEighthDayPlan;
	private Date twentyEighthDayDesign;
	
	private Date twentyNinethDayPlan;
	private Date twentyNinethDayDesign;
	
	private Date thirtiethDayPlan;
	private Date thirtiethDayDesign;
	
	private Date thirtyFirstDayPlan;
	private Date thirtyFirstDayDesign;
	
	private Date firstCheckDate;//首次稽核日期
	private Date secondCheckDate;//二次稽核日期
	private String finalCheckResult;//最终稽核结果
	private String checker;//确认人
	private String checkFile;//稽核附件
	private String remark;//备注
//	@ManyToOne
//    @JsonIgnore
//    @JoinColumn(name="FK_SUPPLIER_YEAR_CHECK_ID")
//    private SupplierYearCheck supplierYearCheck;
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
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
	public Long getYearCheckId() {
		return yearCheckId;
	}
	public void setYearCheckId(Long yearCheckId) {
		this.yearCheckId = yearCheckId;
	}
	public Date getFirstDayPlan() {
		return firstDayPlan;
	}
	public void setFirstDayPlan(Date firstDayPlan) {
		this.firstDayPlan = firstDayPlan;
	}
	public Date getFirstDayDesign() {
		return firstDayDesign;
	}
	public void setFirstDayDesign(Date firstDayDesign) {
		this.firstDayDesign = firstDayDesign;
	}
	public Date getSecondDayPlan() {
		return secondDayPlan;
	}
	public void setSecondDayPlan(Date secondDayPlan) {
		this.secondDayPlan = secondDayPlan;
	}
	public Date getSecondDayDesign() {
		return secondDayDesign;
	}
	public void setSecondDayDesign(Date secondDayDesign) {
		this.secondDayDesign = secondDayDesign;
	}
	public Date getThirdDayPlan() {
		return thirdDayPlan;
	}
	public void setThirdDayPlan(Date thirdDayPlan) {
		this.thirdDayPlan = thirdDayPlan;
	}
	public Date getThirdDayDesign() {
		return thirdDayDesign;
	}
	public void setThirdDayDesign(Date thirdDayDesign) {
		this.thirdDayDesign = thirdDayDesign;
	}
	public Date getForthDayPlan() {
		return forthDayPlan;
	}
	public void setForthDayPlan(Date forthDayPlan) {
		this.forthDayPlan = forthDayPlan;
	}
	public Date getForthDayDesign() {
		return forthDayDesign;
	}
	public void setForthDayDesign(Date forthDayDesign) {
		this.forthDayDesign = forthDayDesign;
	}
	public Date getFifthDayPlan() {
		return fifthDayPlan;
	}
	public void setFifthDayPlan(Date fifthDayPlan) {
		this.fifthDayPlan = fifthDayPlan;
	}
	public Date getFifthDayDesign() {
		return fifthDayDesign;
	}
	public void setFifthDayDesign(Date fifthDayDesign) {
		this.fifthDayDesign = fifthDayDesign;
	}
	public Date getSixthDayPlan() {
		return sixthDayPlan;
	}
	public void setSixthDayPlan(Date sixthDayPlan) {
		this.sixthDayPlan = sixthDayPlan;
	}
	public Date getSixthDayDesign() {
		return sixthDayDesign;
	}
	public void setSixthDayDesign(Date sixthDayDesign) {
		this.sixthDayDesign = sixthDayDesign;
	}
	public Date getSeventhDayPlan() {
		return seventhDayPlan;
	}
	public void setSeventhDayPlan(Date seventhDayPlan) {
		this.seventhDayPlan = seventhDayPlan;
	}
	public Date getSeventhDayDesign() {
		return seventhDayDesign;
	}
	public void setSeventhDayDesign(Date seventhDayDesign) {
		this.seventhDayDesign = seventhDayDesign;
	}
	public Date getEighthDayPlan() {
		return eighthDayPlan;
	}
	public void setEighthDayPlan(Date eighthDayPlan) {
		this.eighthDayPlan = eighthDayPlan;
	}
	public Date getEighthDayDesign() {
		return eighthDayDesign;
	}
	public void setEighthDayDesign(Date eighthDayDesign) {
		this.eighthDayDesign = eighthDayDesign;
	}
	public Date getNinthDayPlan() {
		return ninthDayPlan;
	}
	public void setNinthDayPlan(Date ninthDayPlan) {
		this.ninthDayPlan = ninthDayPlan;
	}
	public Date getNinthDayDesign() {
		return ninthDayDesign;
	}
	public void setNinthDayDesign(Date ninthDayDesign) {
		this.ninthDayDesign = ninthDayDesign;
	}
	public Date getTenthDayPlan() {
		return tenthDayPlan;
	}
	public void setTenthDayPlan(Date tenthDayPlan) {
		this.tenthDayPlan = tenthDayPlan;
	}
	public Date getTenthDayDesign() {
		return tenthDayDesign;
	}
	public void setTenthDayDesign(Date tenthDayDesign) {
		this.tenthDayDesign = tenthDayDesign;
	}
	public Date getEleventhDayPlan() {
		return eleventhDayPlan;
	}
	public void setEleventhDayPlan(Date eleventhDayPlan) {
		this.eleventhDayPlan = eleventhDayPlan;
	}
	public Date getEleventhDayDesign() {
		return eleventhDayDesign;
	}
	public void setEleventhDayDesign(Date eleventhDayDesign) {
		this.eleventhDayDesign = eleventhDayDesign;
	}
	public Date getTwelfthDayPlan() {
		return twelfthDayPlan;
	}
	public void setTwelfthDayPlan(Date twelfthDayPlan) {
		this.twelfthDayPlan = twelfthDayPlan;
	}
	public Date getTwelfthDayDesign() {
		return twelfthDayDesign;
	}
	public void setTwelfthDayDesign(Date twelfthDayDesign) {
		this.twelfthDayDesign = twelfthDayDesign;
	}
	public Date getThirtheenthDayPlan() {
		return thirtheenthDayPlan;
	}
	public void setThirtheenthDayPlan(Date thirtheenthDayPlan) {
		this.thirtheenthDayPlan = thirtheenthDayPlan;
	}
	public Date getThirtheenthDayDesign() {
		return thirtheenthDayDesign;
	}
	public void setThirtheenthDayDesign(Date thirtheenthDayDesign) {
		this.thirtheenthDayDesign = thirtheenthDayDesign;
	}
	public Date getFourtheenthDayPlan() {
		return fourtheenthDayPlan;
	}
	public void setFourtheenthDayPlan(Date fourtheenthDayPlan) {
		this.fourtheenthDayPlan = fourtheenthDayPlan;
	}
	public Date getFourtheenthDayDesign() {
		return fourtheenthDayDesign;
	}
	public void setFourtheenthDayDesign(Date fourtheenthDayDesign) {
		this.fourtheenthDayDesign = fourtheenthDayDesign;
	}
	public Date getFiftheenthDayPlan() {
		return fiftheenthDayPlan;
	}
	public void setFiftheenthDayPlan(Date fiftheenthDayPlan) {
		this.fiftheenthDayPlan = fiftheenthDayPlan;
	}
	public Date getFiftheenthDayDesign() {
		return fiftheenthDayDesign;
	}
	public void setFiftheenthDayDesign(Date fiftheenthDayDesign) {
		this.fiftheenthDayDesign = fiftheenthDayDesign;
	}
	public Date getSixtheenthDayPlan() {
		return sixtheenthDayPlan;
	}
	public void setSixtheenthDayPlan(Date sixtheenthDayPlan) {
		this.sixtheenthDayPlan = sixtheenthDayPlan;
	}
	public Date getSixtheenthDayDesign() {
		return sixtheenthDayDesign;
	}
	public void setSixtheenthDayDesign(Date sixtheenthDayDesign) {
		this.sixtheenthDayDesign = sixtheenthDayDesign;
	}
	public Date getSeventeenthDayPlan() {
		return seventeenthDayPlan;
	}
	public void setSeventeenthDayPlan(Date seventeenthDayPlan) {
		this.seventeenthDayPlan = seventeenthDayPlan;
	}
	public Date getSeventeenthDayDesign() {
		return seventeenthDayDesign;
	}
	public void setSeventeenthDayDesign(Date seventeenthDayDesign) {
		this.seventeenthDayDesign = seventeenthDayDesign;
	}
	public Date getEighteenthDayPlan() {
		return eighteenthDayPlan;
	}
	public void setEighteenthDayPlan(Date eighteenthDayPlan) {
		this.eighteenthDayPlan = eighteenthDayPlan;
	}
	public Date getEighteenthDayDesign() {
		return eighteenthDayDesign;
	}
	public void setEighteenthDayDesign(Date eighteenthDayDesign) {
		this.eighteenthDayDesign = eighteenthDayDesign;
	}
	public Date getNineteenthDayPlan() {
		return nineteenthDayPlan;
	}
	public void setNineteenthDayPlan(Date nineteenthDayPlan) {
		this.nineteenthDayPlan = nineteenthDayPlan;
	}
	public Date getNineteenthDayDesign() {
		return nineteenthDayDesign;
	}
	public void setNineteenthDayDesign(Date nineteenthDayDesign) {
		this.nineteenthDayDesign = nineteenthDayDesign;
	}
	public Date getTwentiethDayPlan() {
		return twentiethDayPlan;
	}
	public void setTwentiethDayPlan(Date twentiethDayPlan) {
		this.twentiethDayPlan = twentiethDayPlan;
	}
	public Date getTwentiethDayDesign() {
		return twentiethDayDesign;
	}
	public void setTwentiethDayDesign(Date twentiethDayDesign) {
		this.twentiethDayDesign = twentiethDayDesign;
	}
	public Date getTwentyFirstDayPlan() {
		return twentyFirstDayPlan;
	}
	public void setTwentyFirstDayPlan(Date twentyFirstDayPlan) {
		this.twentyFirstDayPlan = twentyFirstDayPlan;
	}
	public Date getTwentyFirstDayDesign() {
		return twentyFirstDayDesign;
	}
	public void setTwentyFirstDayDesign(Date twentyFirstDayDesign) {
		this.twentyFirstDayDesign = twentyFirstDayDesign;
	}
	public Date getTwentySecondDayPlan() {
		return twentySecondDayPlan;
	}
	public void setTwentySecondDayPlan(Date twentySecondDayPlan) {
		this.twentySecondDayPlan = twentySecondDayPlan;
	}
	public Date getTwentySecondDayDesign() {
		return twentySecondDayDesign;
	}
	public void setTwentySecondDayDesign(Date twentySecondDayDesign) {
		this.twentySecondDayDesign = twentySecondDayDesign;
	}
	public Date getTwentyThirdDayPlan() {
		return twentyThirdDayPlan;
	}
	public void setTwentyThirdDayPlan(Date twentyThirdDayPlan) {
		this.twentyThirdDayPlan = twentyThirdDayPlan;
	}
	public Date getTwentyThirdDayDesign() {
		return twentyThirdDayDesign;
	}
	public void setTwentyThirdDayDesign(Date twentyThirdDayDesign) {
		this.twentyThirdDayDesign = twentyThirdDayDesign;
	}
	public Date getTwentyFourthDayPlan() {
		return twentyFourthDayPlan;
	}
	public void setTwentyFourthDayPlan(Date twentyFourthDayPlan) {
		this.twentyFourthDayPlan = twentyFourthDayPlan;
	}
	public Date getTwentyFourthDayDesign() {
		return twentyFourthDayDesign;
	}
	public void setTwentyFourthDayDesign(Date twentyFourthDayDesign) {
		this.twentyFourthDayDesign = twentyFourthDayDesign;
	}
	public Date getTwentyFifthDayPlan() {
		return twentyFifthDayPlan;
	}
	public void setTwentyFifthDayPlan(Date twentyFifthDayPlan) {
		this.twentyFifthDayPlan = twentyFifthDayPlan;
	}
	public Date getTwentyFifthDayDesign() {
		return twentyFifthDayDesign;
	}
	public void setTwentyFifthDayDesign(Date twentyFifthDayDesign) {
		this.twentyFifthDayDesign = twentyFifthDayDesign;
	}
	public Date getTwentySixthDayPlan() {
		return twentySixthDayPlan;
	}
	public void setTwentySixthDayPlan(Date twentySixthDayPlan) {
		this.twentySixthDayPlan = twentySixthDayPlan;
	}
	public Date getTwentySixthDayDesign() {
		return twentySixthDayDesign;
	}
	public void setTwentySixthDayDesign(Date twentySixthDayDesign) {
		this.twentySixthDayDesign = twentySixthDayDesign;
	}
	public Date getTwentySeventhDayPlan() {
		return twentySeventhDayPlan;
	}
	public void setTwentySeventhDayPlan(Date twentySeventhDayPlan) {
		this.twentySeventhDayPlan = twentySeventhDayPlan;
	}
	public Date getTwentySeventhDayDesign() {
		return twentySeventhDayDesign;
	}
	public void setTwentySeventhDayDesign(Date twentySeventhDayDesign) {
		this.twentySeventhDayDesign = twentySeventhDayDesign;
	}
	public Date getTwentyEighthDayPlan() {
		return twentyEighthDayPlan;
	}
	public void setTwentyEighthDayPlan(Date twentyEighthDayPlan) {
		this.twentyEighthDayPlan = twentyEighthDayPlan;
	}
	public Date getTwentyEighthDayDesign() {
		return twentyEighthDayDesign;
	}
	public void setTwentyEighthDayDesign(Date twentyEighthDayDesign) {
		this.twentyEighthDayDesign = twentyEighthDayDesign;
	}
	public Date getTwentyNinethDayPlan() {
		return twentyNinethDayPlan;
	}
	public void setTwentyNinethDayPlan(Date twentyNinethDayPlan) {
		this.twentyNinethDayPlan = twentyNinethDayPlan;
	}
	public Date getTwentyNinethDayDesign() {
		return twentyNinethDayDesign;
	}
	public void setTwentyNinethDayDesign(Date twentyNinethDayDesign) {
		this.twentyNinethDayDesign = twentyNinethDayDesign;
	}
	public Date getThirtiethDayPlan() {
		return thirtiethDayPlan;
	}
	public void setThirtiethDayPlan(Date thirtiethDayPlan) {
		this.thirtiethDayPlan = thirtiethDayPlan;
	}
	public Date getThirtiethDayDesign() {
		return thirtiethDayDesign;
	}
	public void setThirtiethDayDesign(Date thirtiethDayDesign) {
		this.thirtiethDayDesign = thirtiethDayDesign;
	}
	public Date getThirtyFirstDayPlan() {
		return thirtyFirstDayPlan;
	}
	public void setThirtyFirstDayPlan(Date thirtyFirstDayPlan) {
		this.thirtyFirstDayPlan = thirtyFirstDayPlan;
	}
	public Date getThirtyFirstDayDesign() {
		return thirtyFirstDayDesign;
	}
	public void setThirtyFirstDayDesign(Date thirtyFirstDayDesign) {
		this.thirtyFirstDayDesign = thirtyFirstDayDesign;
	}

	
	
}

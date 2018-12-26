package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 类名:供应商-产品成分宣告表
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2017年3月3日 发布
 */
@Entity
@Table(name="SUPPLIER_PRODUCT_PART_REPORT")
public class ProductPartReport extends IdEntity {

		/**
		  *ProductPartReport.java2017年3月3日
		 */
	private static final long serialVersionUID = 1L;
	private String reportName;//报告编号
	private String partName;//拆解部位名称
	private String stuffName;//均质材料名称
	private String materialType;//均质材料属性
	private String materialTypeName;//均质材料型号
	private Double materialWeight;//重量
	private String vender;//制造商
	private String substanceName;//化学物质英文全名
	private String casNo;//化学文摘社号
	private Double substanceWeight;//均质材料中化学物质重量
	private String testLab;//测试机构
	private String casNoReachSvhc;//确认
	private String exemption;//豁免条款说明
	private Date completeDate; //最早完成日期
	private Double partRate;//比列 化学物质占材料重量百分比
	private String reportFile;//rohs附件
	private String sdsFile;//SDS（附件）	
	private String remark;//
	private Date testReportDate;//报告测试时间
	private Date cutTime;//报告截至时间
	@ManyToOne
    @JsonIgnore
    @JoinColumn(name="FK_SUPPLIER_DATA_SUPPLY_ID")
    private SupplierDataSupply supplierDataSupply;
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getStuffName() {
		return stuffName;
	}
	public void setStuffName(String stuffName) {
		this.stuffName = stuffName;
	}
	public Date getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}
	public Double getPartRate() {
		return partRate;
	}
	public void setPartRate(Double partRate) {
		this.partRate = partRate;
	}
	public SupplierDataSupply getSupplierDataSupply() {
		return supplierDataSupply;
	}
	public void setSupplierDataSupply(SupplierDataSupply supplierDataSupply) {
		this.supplierDataSupply = supplierDataSupply;
	}
	public String getReportFile() {
		return reportFile;
	}
	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public String getMaterialTypeName() {
		return materialTypeName;
	}
	public void setMaterialTypeName(String materialTypeName) {
		this.materialTypeName = materialTypeName;
	}
	public Double getMaterialWeight() {
		return materialWeight;
	}
	public void setMaterialWeight(Double materialWeight) {
		this.materialWeight = materialWeight;
	}
	public String getVender() {
		return vender;
	}
	public void setVender(String vender) {
		this.vender = vender;
	}
	public String getSubstanceName() {
		return substanceName;
	}
	public void setSubstanceName(String substanceName) {
		this.substanceName = substanceName;
	}
	public String getCasNo() {
		return casNo;
	}
	public void setCasNo(String casNo) {
		this.casNo = casNo;
	}
	public Double getSubstanceWeight() {
		return substanceWeight;
	}
	public void setSubstanceWeight(Double substanceWeight) {
		this.substanceWeight = substanceWeight;
	}
	public String getTestLab() {
		return testLab;
	}
	public void setTestLab(String testLab) {
		this.testLab = testLab;
	}
	public String getCasNoReachSvhc() {
		return casNoReachSvhc;
	}
	public void setCasNoReachSvhc(String casNoReachSvhc) {
		this.casNoReachSvhc = casNoReachSvhc;
	}
	public String getExemption() {
		return exemption;
	}
	public void setExemption(String exemption) {
		this.exemption = exemption;
	}
	public String getSdsFile() {
		return sdsFile;
	}
	public void setSdsFile(String sdsFile) {
		this.sdsFile = sdsFile;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getTestReportDate() {
		return testReportDate;
	}
	public void setTestReportDate(Date testReportDate) {
		this.testReportDate = testReportDate;
	}
	public Date getCutTime() {
		return cutTime;
	}
	public void setCutTime(Date cutTime) {
		this.cutTime = cutTime;
	}
	
	
}

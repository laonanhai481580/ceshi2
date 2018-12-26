package com.ambition.iqc.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 测试频率维护
 * @author ambition-zhaojun
 *
 */
@Entity
@Table(name = "IQC_TEST_FREQUENCY")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class TestFrequency  extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String checkBomCode;//物料编码
	private String checkBomName;//物料名称
	private String supplierName; //供方名称
	private String supplierCode;//供方编码
	private String productStage;//产品阶段
	private String entrustItem;//委托项目
	private Integer hsfTestCycle;//HSF送检周期
	private String isHsf="是";//是否需要送检hsf
	private Integer ortTestCycle;//ORT送检周期
	private String isOrt="是";//是否需要送检ort
	private String remark;//备注
	private String materialType;//材质
	private Date  lastHsfTestDate;//上次HSF送检时间
	private String lastHsfReportId;//上次HSF试验报告ID
	private String lastHsfReportNo;//上次HSF试验报告编号
	private Date  lastOrtTestDate;//上次可靠性送检时间
	private String lastOrtReportId;//上次可靠性试验报告ID
	private String lastOrtReportNo;//上次可靠性试验报告编号
	private String isErp="否";//是否集成数据
	public String getCheckBomCode() {
		return checkBomCode;
	}
	public void setCheckBomCode(String checkBomCode) {
		this.checkBomCode = checkBomCode;
	}
	public String getCheckBomName() {
		return checkBomName;
	}
	public void setCheckBomName(String checkBomName) {
		this.checkBomName = checkBomName;
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
	public String getProductStage() {
		return productStage;
	}
	public void setProductStage(String productStage) {
		this.productStage = productStage;
	}
	public String getEntrustItem() {
		return entrustItem;
	}
	public void setEntrustItem(String entrustItem) {
		this.entrustItem = entrustItem;
	}

	public Integer getHsfTestCycle() {
		return hsfTestCycle;
	}
	public void setHsfTestCycle(Integer hsfTestCycle) {
		this.hsfTestCycle = hsfTestCycle;
	}
	public Integer getOrtTestCycle() {
		return ortTestCycle;
	}
	public void setOrtTestCycle(Integer ortTestCycle) {
		this.ortTestCycle = ortTestCycle;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public Date getLastHsfTestDate() {
		return lastHsfTestDate;
	}
	public void setLastHsfTestDate(Date lastHsfTestDate) {
		this.lastHsfTestDate = lastHsfTestDate;
	}
	public String getLastHsfReportId() {
		return lastHsfReportId;
	}
	public void setLastHsfReportId(String lastHsfReportId) {
		this.lastHsfReportId = lastHsfReportId;
	}
	public String getLastHsfReportNo() {
		return lastHsfReportNo;
	}
	public void setLastHsfReportNo(String lastHsfReportNo) {
		this.lastHsfReportNo = lastHsfReportNo;
	}
	public Date getLastOrtTestDate() {
		return lastOrtTestDate;
	}
	public void setLastOrtTestDate(Date lastOrtTestDate) {
		this.lastOrtTestDate = lastOrtTestDate;
	}
	public String getLastOrtReportId() {
		return lastOrtReportId;
	}
	public void setLastOrtReportId(String lastOrtReportId) {
		this.lastOrtReportId = lastOrtReportId;
	}
	public String getLastOrtReportNo() {
		return lastOrtReportNo;
	}
	public void setLastOrtReportNo(String lastOrtReportNo) {
		this.lastOrtReportNo = lastOrtReportNo;
	}
	public String getIsErp() {
		return isErp;
	}
	public void setIsErp(String isErp) {
		this.isErp = isErp;
	}
	public String getIsHsf() {
		return isHsf;
	}
	public void setIsHsf(String isHsf) {
		this.isHsf = isHsf;
	}
	public String getIsOrt() {
		return isOrt;
	}
	public void setIsOrt(String isOrt) {
		this.isOrt = isOrt;
	}		
	
}

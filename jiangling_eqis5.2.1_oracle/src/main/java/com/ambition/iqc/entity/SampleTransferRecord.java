package com.ambition.iqc.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ambition.product.base.IdEntity;
import com.ambition.supplier.utils.DateUtil;

/**
 * 方案转移记录
 * @author 赵骏
 *
 */
@Entity
@Table(name="IQC_SAMPLE_TRANSFER_RECORD")
public class SampleTransferRecord extends IdEntity{
	private static final long serialVersionUID = -731322666650992256L;
	//状态
	public static String AUDITSTATE_NORMAL = "等待审核";
	public static String AUDITSTATE_PASS = "同意";
	public static String AUDITSTATE_FAIL = "不同意";
	private String sourceRule;//源规则
	private String targetRule;//目标规则
	private String supplierName;//供应商
	private String checkBomMaterialType;//物料类别
	private String supplierCode;//供应商编码
	private String checkBomCode;//物料编码
	private String checkBomName;//物料名称
	private String checkItemName;//检验项目名称
	@Temporal(TemporalType.TIMESTAMP)
	private Date inspectionDate;//检验时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date schemeStartDate;//方案开始时间
	private String auditMan;//审核人员
	@Temporal(TemporalType.TIMESTAMP)
	private Date auditTime;//审核时间
	private String auditState = AUDITSTATE_NORMAL;//审核状态
	private String auditText;//审核意见
	public String getSourceRule() {
		return sourceRule;
	}
	public void setSourceRule(String sourceRule) {
		this.sourceRule = sourceRule;
	}
	public String getTargetRule() {
		return targetRule;
	}
	public void setTargetRule(String targetRule) {
		this.targetRule = targetRule;
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
	public String getCheckItemName() {
		return checkItemName;
	}
	public void setCheckItemName(String checkItemName) {
		this.checkItemName = checkItemName;
	}
	public String getAuditMan() {
		return auditMan;
	}
	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public String getAuditState() {
		return auditState;
	}
	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}
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
	public Date getInspectionDate() {
		return inspectionDate;
	}
	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
	public Date getSchemeStartDate() {
		return schemeStartDate;
	}
	public void setSchemeStartDate(Date schemeStartDate) {
		this.schemeStartDate = schemeStartDate;
	}
	public String getAuditTimeStr() {
		return DateUtil.formateTimeStr(auditTime);
	}
	
	public String getCheckBomMaterialType() {
		return checkBomMaterialType;
	}
	public void setCheckBomMaterialType(String checkBomMaterialType) {
		this.checkBomMaterialType = checkBomMaterialType;
	}		
	public String getAuditText() {
		return auditText;
	}
	public void setAuditText(String auditText) {
		this.auditText = auditText;
	}
	public String toString(){
		return "操作信息：方案转移记录    物料编码"+this.checkBomCode +"  审核人"+this.auditMan+"  审核时间"+this.auditTime;
	}
}

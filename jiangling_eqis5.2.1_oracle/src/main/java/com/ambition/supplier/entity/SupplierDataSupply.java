package com.ambition.supplier.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 类名:gp资料提供
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2016年10月19日 发布
 */
@Entity
@Table(name = "SUPPLIER_DATA_SUPPLY")
public class SupplierDataSupply extends  WorkflowIdEntity{

		/**
		  *SupplierDataSupply.java2016年10月19日
		 */
	private static final long serialVersionUID = 1L;
	private String formNo;
	private String supplierName;
	private String supplierLoginName;//
	private String supplierUserName;//供应商用户名
	private String supplierMails;
	private String productName;//产品名称
	private String declarer;//授权宣告人
	private String materialCode;//欧菲光料号
	private String raiseDept;//提出部门
	private Date raiseDate;//提出日期
	private String instruction;//说明
	private String partNameRohs;//拆解部位名称RoHS
	private String stuffNameRohs;//均质材料名称RoHS
	private Date completeDateRohs;//最早完成日期RoHS
	private String partNameHalogen;//拆解部位名称Halogen
	private String stuffNameHalogen;//均质材料名称Halogen
	private Date completeDateHalogen;//最早完成日期Halogen
	@OneToMany(mappedBy = "supplierDataSupply",cascade=CascadeType.MERGE)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @JsonIgnore()
	private List<ProductPartReport>  productPartReports;//产品成分宣告表
	private String declareFile;//宣告表
	private String productFile;//产品结构图
	private String qsProcesser;//qs办理人
	private String qsProcesserLog;//qs办理人登录名
	private String qsOpinion;//qs意见
	private String bomFile;//文件
	private String surveyFile;//调查文件
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierLoginName() {
		return supplierLoginName;
	}
	public void setSupplierLoginName(String supplierLoginName) {
		this.supplierLoginName = supplierLoginName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getDeclarer() {
		return declarer;
	}
	public void setDeclarer(String declarer) {
		this.declarer = declarer;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getRaiseDept() {
		return raiseDept;
	}
	public void setRaiseDept(String raiseDept) {
		this.raiseDept = raiseDept;
	}
	public Date getRaiseDate() {
		return raiseDate;
	}
	public void setRaiseDate(Date raiseDate) {
		this.raiseDate = raiseDate;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	public String getPartNameRohs() {
		return partNameRohs;
	}
	public void setPartNameRohs(String partNameRohs) {
		this.partNameRohs = partNameRohs;
	}
	public String getStuffNameRohs() {
		return stuffNameRohs;
	}
	public void setStuffNameRohs(String stuffNameRohs) {
		this.stuffNameRohs = stuffNameRohs;
	}
	public Date getCompleteDateRohs() {
		return completeDateRohs;
	}
	public void setCompleteDateRohs(Date completeDateRohs) {
		this.completeDateRohs = completeDateRohs;
	}
	public String getPartNameHalogen() {
		return partNameHalogen;
	}
	public void setPartNameHalogen(String partNameHalogen) {
		this.partNameHalogen = partNameHalogen;
	}
	public String getStuffNameHalogen() {
		return stuffNameHalogen;
	}
	public void setStuffNameHalogen(String stuffNameHalogen) {
		this.stuffNameHalogen = stuffNameHalogen;
	}
	public Date getCompleteDateHalogen() {
		return completeDateHalogen;
	}
	public void setCompleteDateHalogen(Date completeDateHalogen) {
		this.completeDateHalogen = completeDateHalogen;
	}
	public String getDeclareFile() {
		return declareFile;
	}
	public void setDeclareFile(String declareFile) {
		this.declareFile = declareFile;
	}
	public String getProductFile() {
		return productFile;
	}
	public void setProductFile(String productFile) {
		this.productFile = productFile;
	}
	public String getQsProcesser() {
		return qsProcesser;
	}
	public void setQsProcesser(String qsProcesser) {
		this.qsProcesser = qsProcesser;
	}
	public String getQsProcesserLog() {
		return qsProcesserLog;
	}
	public void setQsProcesserLog(String qsProcesserLog) {
		this.qsProcesserLog = qsProcesserLog;
	}
	public String getQsOpinion() {
		return qsOpinion;
	}
	public void setQsOpinion(String qsOpinion) {
		this.qsOpinion = qsOpinion;
	}
	public String getSupplierUserName() {
		return supplierUserName;
	}
	public void setSupplierUserName(String supplierUserName) {
		this.supplierUserName = supplierUserName;
	}
	public String getSupplierMails() {
		return supplierMails;
	}
	public void setSupplierMails(String supplierMails) {
		this.supplierMails = supplierMails;
	}
	public List<ProductPartReport> getProductPartReports() {
		return productPartReports;
	}
	public void setProductPartReports(List<ProductPartReport> productPartReports) {
		this.productPartReports = productPartReports;
	}
	public String getBomFile() {
		return bomFile;
	}
	public void setBomFile(String bomFile) {
		this.bomFile = bomFile;
	}
	public String getSurveyFile() {
		return surveyFile;
	}
	public void setSurveyFile(String surveyFile) {
		this.surveyFile = surveyFile;
	}
	
}

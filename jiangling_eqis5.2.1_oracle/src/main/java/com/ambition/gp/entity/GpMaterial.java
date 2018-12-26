package com.ambition.gp.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:GP资料2.0
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  xjn
 * @version 1.00 2017年10月26日 发布
 */
@Entity
@Table(name="GP_MATERIAL")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class GpMaterial extends  WorkflowIdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "GP_MATERIAL";//实体编码
	public static final String ENTITY_LIST_NAME = "GP资料2.0";//实体名称
	private String formNo;//表单编号
	private String supplierName;//供应商名称
	private String supplierCode;//供应商编码
	private String supplierEmail;//供应商邮箱
	private String declaring;//宣告人
	private Date supplierDate;//声明日期
	private String productName;//产品名称
	private String materialName;//物料名称
	private String materialCode;//物料代码
	private String materialType;//物料类别
	private String supplierPart;//供应商料号
	private String ofPart;//欧菲料号
	private String productWeight;//产品重量
	private String benchmark;//环保基准
	private String attribute;//环保属性
	private String productFile;//产品结构图
	private String bomFile;//BOM附件
	private String surveyFile;//调查附件
	private String declareFile;//申报附件
	private String initiator;//发起人
	private String initiatorLogin;//发起人登录名
	private String confirmDept;//确认人
	private String confirmDeptLoing;//确认人登录名
	private String supplier;//指定供应商
	private String supplierLoginName;//供应商登录名
	private String auditor;//审核人
	private Date auditorDate;//审核日期
	private String auditorLogin;//审核人登录名
	private String currentState;//当前状态
	private String isHarmful="0";//是否审核
	private String detectionReport;//检测报告
	private String matterMaterials;//全物质材料宣告表
	private String approvalId;//材料承认ID
	private String approvalNo;//材料承认单号
	private String remark;//备注
	private Date revertDate;//回复交期
	
	@OneToMany(mappedBy = "gpMaterial",cascade={javax.persistence.CascadeType.ALL})
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	private List<GpMaterialSub> gpMaterialSubs;
	
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
	public String getSupplierEmail() {
		return supplierEmail;
	}
	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}
	public String getDeclaring() {
		return declaring;
	}
	public void setDeclaring(String declaring) {
		this.declaring = declaring;
	}
	public Date getSupplierDate() {
		return supplierDate;
	}
	public void setSupplierDate(Date supplierDate) {
		this.supplierDate = supplierDate;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getCurrentState() {
		return currentState;
	}
	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}
	public String getSupplierPart() {
		return supplierPart;
	}
	public void setSupplierPart(String supplierPart) {
		this.supplierPart = supplierPart;
	}
	public String getOfPart() {
		return ofPart;
	}
	public void setOfPart(String ofPart) {
		this.ofPart = ofPart;
	}
	public String getProductWeight() {
		return productWeight;
	}
	public void setProductWeight(String productWeight) {
		this.productWeight = productWeight;
	}
	public String getProductFile() {
		return productFile;
	}
	public void setProductFile(String productFile) {
		this.productFile = productFile;
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
	public String getDeclareFile() {
		return declareFile;
	}
	public void setDeclareFile(String declareFile) {
		this.declareFile = declareFile;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public String getAuditorLogin() {
		return auditorLogin;
	}
	public void setAuditorLogin(String auditorLogin) {
		this.auditorLogin = auditorLogin;
	}
	public Date getAuditorDate() {
		return auditorDate;
	}
	public void setAuditorDate(Date auditorDate) {
		this.auditorDate = auditorDate;
	}
	
	public List<GpMaterialSub> getGpMaterialSubs() {
		return gpMaterialSubs;
	}
	public void setGpMaterialSubs(List<GpMaterialSub> gpMaterialSubs) {
		this.gpMaterialSubs = gpMaterialSubs;
	}
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getInitiator() {
		return initiator;
	}
	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getSupplierLoginName() {
		return supplierLoginName;
	}
	public void setSupplierLoginName(String supplierLoginName) {
		this.supplierLoginName = supplierLoginName;
	}
	public String getInitiatorLogin() {
		return initiatorLogin;
	}
	public void setInitiatorLogin(String initiatorLogin) {
		this.initiatorLogin = initiatorLogin;
	}
	public String getConfirmDept() {
		return confirmDept;
	}
	public void setConfirmDept(String confirmDept) {
		this.confirmDept = confirmDept;
	}
	public String getConfirmDeptLoing() {
		return confirmDeptLoing;
	}
	public void setConfirmDeptLoing(String confirmDeptLoing) {
		this.confirmDeptLoing = confirmDeptLoing;
	}
	public String getIsHarmful() {
		return isHarmful;
	}
	public void setIsHarmful(String isHarmful) {
		this.isHarmful = isHarmful;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public String getBenchmark() {
		return benchmark;
	}
	public void setBenchmark(String benchmark) {
		this.benchmark = benchmark;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getDetectionReport() {
		return detectionReport;
	}
	public void setDetectionReport(String detectionReport) {
		this.detectionReport = detectionReport;
	}
	public String getMatterMaterials() {
		return matterMaterials;
	}
	public void setMatterMaterials(String matterMaterials) {
		this.matterMaterials = matterMaterials;
	}
	public String getApprovalId() {
		return approvalId;
	}
	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}
	public String getApprovalNo() {
		return approvalNo;
	}
	public void setApprovalNo(String approvalNo) {
		this.approvalNo = approvalNo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getRevertDate() {
		return revertDate;
	}
	public void setRevertDate(Date revertDate) {
		this.revertDate = revertDate;
	}
	public static String getEntityListCode() {
		return ENTITY_LIST_CODE;
	}
	public static String getEntityListName() {
		return ENTITY_LIST_NAME;
	}
	
	
}

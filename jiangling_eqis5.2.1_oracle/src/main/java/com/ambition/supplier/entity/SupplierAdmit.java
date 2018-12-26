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
 * 
 * 类名:材料承认管理TP
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  xjn
 * @version 1.00 2018年3月28日 发布
 */
@Entity
@Table(name="SUPPLIER_SUPPLIER_ADMIT")
public class SupplierAdmit extends WorkflowIdEntity{
	
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "SUPPLIER_SUPPLIER_ADMIT";//实体编码
	public static final String ENTITY_LIST_NAME = "材料承认管理New";//实体名称
	/*材料承认发起 */
	private String formNo;//表单编号
	private String productVersion;//规格型号版本号
	private String productName;//产品名称
	private String materialName;//物料名称
	private String materialCode;//物料编码
	private Date applyDate;//申请日期
	private String supplierName;//供应商名称
	private String supplierCode;//供应商编码
	private String supplierEmail;//供应商邮箱
	private String item;//项目
	private String admitProject;//承认项目
	private String applicat;//申请人
	private String applicatLog;//申请人登录名
	private String consignorDept;//当前部门
	private String materialSort;//材料类别
	/*
	 * 采购部
	 */
	private String purchaseProcesser;//采购部
	private String purchaseProcessLog;//采购部登录名
	private String supplier;//指定供应商
	private String supplierLoginName;//供应商登录名
	private String opinionPurchase;//意见
	/*
	 * 
	 */
	private String vendorNo;//供应商料号
	private String address;//生产地址
	/*
	 * 会签
	 */
	private String checkDeptMans;//会签部门人员
	private String checkDeptMansLog;//会签人登陆名
	private String checkDeptMansLog2;//会签人登陆名
	private String rdChecker;//研发核准
	private String rdCheckerLog;//研发核准人登录名
	private String pmChecker;//pm核准
	private String pmCheckerLog;//pm核准登录名
	private String sqeChecker;//SQE核准
	private String sqeCheckerLog;//SQE核准登录名
	private String qsChecker;//QS核准
	private String qsCheckerLog;//QS核准登录名
	private String npiChecker;//NPI核准
	private String npiCheckerLog;//NPI核准登录名
	private String dqeChecker;//DQE核准
	private String dqeCheckerLog;//DQE核准登录名
	private String projectChecker;//工程核准
	private String projectCheckerLog;//工程核准登录名
	private String eeChecker;//EE核准
	private String eeCheckerLog;//EE核准登录名
	private String elseChecker;//其他核准
	private String elseCheckerLog;//其他核准登录名
	private String elseText;//其他输入
	/*
	 * 承认状态
	 */
	private String rdStatus;//承认状态研发
	private String pmStatus;//承认状态PM
	private String sqeStatus;//承认状态SQE
	private String qsStatus;//承认状态QS
	private String npiStatus;//承认状态NPI
	private String dqeStatus;//承认状态DQE
	private String projectStatus;//承认状态工程
	private String finalStatus;//最终状态
	private String eeStatus;//EE状态
	private String elseStatus;//其他状态
	
	
	/*
	 * 意见
	 */
	private String countersignRD;//会签意见研发
	private String countersignPM;//会签意见PM
	private String countersignSQE;//会签意见SQE
	private String countersignQS;//会签意见QS
	private String countersignNpi;//会签意见NPI
	private String countersignDqe;//会签意见DEQ
	private String countersignProject;//会签意见工程
	private String countersignEE;//会签意见EE
	private String countersignElse;//会签意见其他
	private String fileRD;//研发附件
	private String filePM;//PM附件
	private String fileSQE;//SQE附件
	private String fileQS;//QS附件
	private String fileNpi;//Npi附件
	private String fileDqe;//Dqe附件
	private String filePro;//工程附件
	private String fileEE;//工程附件
	private String fileElse;//工程附件
	/*
	 * 主管
	 */
	private String sqeLead;//SQE主管
	private String sqeLeadLog;//SQE主管登录名
	private String skillLead;//技术主管
	private String skillLeadLog;//技术主管登录名
	private String countersignSqeLead;//会签意见SQE主管
	private String countersignSkill;//会签意见技术主管
	private String sqeLeadStatus;//承认状态SQE主管
	private String skillLeadStatus;//承认状态技术主管
	/*
	 * 文件名称
	 */
	private Date docControlDate;//文控提交时间
	private String endType;//结束状态
	private Long inspectionId;//检查ID
	private String textResult;//试验结果（返回iqc）
	private String inspectionNo;//进货表单编号
	private String gpMaterialNo;//产品宣告编号
	private String gpMaterialId;//产品宣告Id
	private String gpMaterialState;//产品宣告状态
	private String docControl;//文控
	private String docControlLoging;//文控登录名
	private String opiniondoc;//文控意见
	private String copyMan;//抄送人
	private String copyManLogin;//抄送人登录名
	private String adminState="FDL";//承认状态
	
	/*
	 * 附件上传
	 */
	@OneToMany(mappedBy = "supplierAdmit",cascade={CascadeType.ALL})
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	private List<SupplierAdmitFile> supplierAdmitFiles;
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getProductVersion() {
		return productVersion;
	}
	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
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
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
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
	public String getSupplierEmail() {
		return supplierEmail;
	}
	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getAdmitProject() {
		return admitProject;
	}
	public void setAdmitProject(String admitProject) {
		this.admitProject = admitProject;
	}
	public String getApplicat() {
		return applicat;
	}
	public void setApplicat(String applicat) {
		this.applicat = applicat;
	}
	public String getApplicatLog() {
		return applicatLog;
	}
	public void setApplicatLog(String applicatLog) {
		this.applicatLog = applicatLog;
	}
	public String getConsignorDept() {
		return consignorDept;
	}
	public void setConsignorDept(String consignorDept) {
		this.consignorDept = consignorDept;
	}
	public String getMaterialSort() {
		return materialSort;
	}
	public void setMaterialSort(String materialSort) {
		this.materialSort = materialSort;
	}
	public String getPurchaseProcesser() {
		return purchaseProcesser;
	}
	public void setPurchaseProcesser(String purchaseProcesser) {
		this.purchaseProcesser = purchaseProcesser;
	}
	public String getPurchaseProcessLog() {
		return purchaseProcessLog;
	}
	public void setPurchaseProcessLog(String purchaseProcessLog) {
		this.purchaseProcessLog = purchaseProcessLog;
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
	public String getOpinionPurchase() {
		return opinionPurchase;
	}
	public void setOpinionPurchase(String opinionPurchase) {
		this.opinionPurchase = opinionPurchase;
	}
	public String getCheckDeptMans() {
		return checkDeptMans;
	}
	public void setCheckDeptMans(String checkDeptMans) {
		this.checkDeptMans = checkDeptMans;
	}
	public String getCheckDeptMansLog() {
		return checkDeptMansLog;
	}
	public void setCheckDeptMansLog(String checkDeptMansLog) {
		this.checkDeptMansLog = checkDeptMansLog;
	}
	public String getCheckDeptMansLog2() {
		return checkDeptMansLog2;
	}
	public void setCheckDeptMansLog2(String checkDeptMansLog2) {
		this.checkDeptMansLog2 = checkDeptMansLog2;
	}
	public String getRdChecker() {
		return rdChecker;
	}
	public void setRdChecker(String rdChecker) {
		this.rdChecker = rdChecker;
	}
	public String getRdCheckerLog() {
		return rdCheckerLog;
	}
	public void setRdCheckerLog(String rdCheckerLog) {
		this.rdCheckerLog = rdCheckerLog;
	}
	public String getPmChecker() {
		return pmChecker;
	}
	public void setPmChecker(String pmChecker) {
		this.pmChecker = pmChecker;
	}
	public String getPmCheckerLog() {
		return pmCheckerLog;
	}
	public void setPmCheckerLog(String pmCheckerLog) {
		this.pmCheckerLog = pmCheckerLog;
	}
	public String getSqeChecker() {
		return sqeChecker;
	}
	public void setSqeChecker(String sqeChecker) {
		this.sqeChecker = sqeChecker;
	}
	public String getSqeCheckerLog() {
		return sqeCheckerLog;
	}
	public void setSqeCheckerLog(String sqeCheckerLog) {
		this.sqeCheckerLog = sqeCheckerLog;
	}
	public String getQsChecker() {
		return qsChecker;
	}
	public void setQsChecker(String qsChecker) {
		this.qsChecker = qsChecker;
	}
	public String getQsCheckerLog() {
		return qsCheckerLog;
	}
	public void setQsCheckerLog(String qsCheckerLog) {
		this.qsCheckerLog = qsCheckerLog;
	}
	public String getNpiChecker() {
		return npiChecker;
	}
	public void setNpiChecker(String npiChecker) {
		this.npiChecker = npiChecker;
	}
	public String getNpiCheckerLog() {
		return npiCheckerLog;
	}
	public void setNpiCheckerLog(String npiCheckerLog) {
		this.npiCheckerLog = npiCheckerLog;
	}
	public String getDqeChecker() {
		return dqeChecker;
	}
	public void setDqeChecker(String dqeChecker) {
		this.dqeChecker = dqeChecker;
	}
	public String getDqeCheckerLog() {
		return dqeCheckerLog;
	}
	public void setDqeCheckerLog(String dqeCheckerLog) {
		this.dqeCheckerLog = dqeCheckerLog;
	}
	public String getProjectChecker() {
		return projectChecker;
	}
	public void setProjectChecker(String projectChecker) {
		this.projectChecker = projectChecker;
	}
	public String getProjectCheckerLog() {
		return projectCheckerLog;
	}
	public void setProjectCheckerLog(String projectCheckerLog) {
		this.projectCheckerLog = projectCheckerLog;
	}
	public Date getDocControlDate() {
		return docControlDate;
	}
	public void setDocControlDate(Date docControlDate) {
		this.docControlDate = docControlDate;
	}
	public String getEndType() {
		return endType;
	}
	public void setEndType(String endType) {
		this.endType = endType;
	}
	public Long getInspectionId() {
		return inspectionId;
	}
	public void setInspectionId(Long inspectionId) {
		this.inspectionId = inspectionId;
	}
	public String getTextResult() {
		return textResult;
	}
	public void setTextResult(String textResult) {
		this.textResult = textResult;
	}
	public String getInspectionNo() {
		return inspectionNo;
	}
	public void setInspectionNo(String inspectionNo) {
		this.inspectionNo = inspectionNo;
	}
	public String getGpMaterialNo() {
		return gpMaterialNo;
	}
	public void setGpMaterialNo(String gpMaterialNo) {
		this.gpMaterialNo = gpMaterialNo;
	}
	public String getSqeLead() {
		return sqeLead;
	}
	public void setSqeLead(String sqeLead) {
		this.sqeLead = sqeLead;
	}
	public String getSqeLeadLog() {
		return sqeLeadLog;
	}
	public void setSqeLeadLog(String sqeLeadLog) {
		this.sqeLeadLog = sqeLeadLog;
	}
	public String getSkillLead() {
		return skillLead;
	}
	public void setSkillLead(String skillLead) {
		this.skillLead = skillLead;
	}
	public String getSkillLeadLog() {
		return skillLeadLog;
	}
	public void setSkillLeadLog(String skillLeadLog) {
		this.skillLeadLog = skillLeadLog;
	}
	public String getDocControl() {
		return docControl;
	}
	public void setDocControl(String docControl) {
		this.docControl = docControl;
	}
	public String getDocControlLoging() {
		return docControlLoging;
	}
	public void setDocControlLoging(String docControlLoging) {
		this.docControlLoging = docControlLoging;
	}
	public String getOpiniondoc() {
		return opiniondoc;
	}
	public void setOpiniondoc(String opiniondoc) {
		this.opiniondoc = opiniondoc;
	}
	public String getCopyMan() {
		return copyMan;
	}
	public void setCopyMan(String copyMan) {
		this.copyMan = copyMan;
	}
	public String getCopyManLogin() {
		return copyManLogin;
	}
	public void setCopyManLogin(String copyManLogin) {
		this.copyManLogin = copyManLogin;
	}
	public String getCountersignRD() {
		return countersignRD;
	}
	public void setCountersignRD(String countersignRD) {
		this.countersignRD = countersignRD;
	}
	public String getCountersignPM() {
		return countersignPM;
	}
	public void setCountersignPM(String countersignPM) {
		this.countersignPM = countersignPM;
	}
	public String getCountersignSQE() {
		return countersignSQE;
	}
	public void setCountersignSQE(String countersignSQE) {
		this.countersignSQE = countersignSQE;
	}
	public String getCountersignQS() {
		return countersignQS;
	}
	public void setCountersignQS(String countersignQS) {
		this.countersignQS = countersignQS;
	}
	public String getCountersignNpi() {
		return countersignNpi;
	}
	public void setCountersignNpi(String countersignNpi) {
		this.countersignNpi = countersignNpi;
	}
	public String getCountersignDqe() {
		return countersignDqe;
	}
	public void setCountersignDqe(String countersignDqe) {
		this.countersignDqe = countersignDqe;
	}
	public String getCountersignProject() {
		return countersignProject;
	}
	public void setCountersignProject(String countersignProject) {
		this.countersignProject = countersignProject;
	}
	public String getFileRD() {
		return fileRD;
	}
	public void setFileRD(String fileRD) {
		this.fileRD = fileRD;
	}
	public String getFilePM() {
		return filePM;
	}
	public void setFilePM(String filePM) {
		this.filePM = filePM;
	}
	public String getFileSQE() {
		return fileSQE;
	}
	public void setFileSQE(String fileSQE) {
		this.fileSQE = fileSQE;
	}
	public String getFileQS() {
		return fileQS;
	}
	public void setFileQS(String fileQS) {
		this.fileQS = fileQS;
	}
	public String getFileNpi() {
		return fileNpi;
	}
	public void setFileNpi(String fileNpi) {
		this.fileNpi = fileNpi;
	}
	public String getFileDqe() {
		return fileDqe;
	}
	public void setFileDqe(String fileDqe) {
		this.fileDqe = fileDqe;
	}
	public String getFilePro() {
		return filePro;
	}
	public void setFilePro(String filePro) {
		this.filePro = filePro;
	}
	public String getCountersignSqeLead() {
		return countersignSqeLead;
	}
	public void setCountersignSqeLead(String countersignSqeLead) {
		this.countersignSqeLead = countersignSqeLead;
	}
	public String getCountersignSkill() {
		return countersignSkill;
	}
	public void setCountersignSkill(String countersignSkill) {
		this.countersignSkill = countersignSkill;
	}
	public String getVendorNo() {
		return vendorNo;
	}
	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRdStatus() {
		return rdStatus;
	}
	public void setRdStatus(String rdStatus) {
		this.rdStatus = rdStatus;
	}
	public String getPmStatus() {
		return pmStatus;
	}
	public void setPmStatus(String pmStatus) {
		this.pmStatus = pmStatus;
	}
	public String getSqeStatus() {
		return sqeStatus;
	}
	public void setSqeStatus(String sqeStatus) {
		this.sqeStatus = sqeStatus;
	}
	public String getQsStatus() {
		return qsStatus;
	}
	public void setQsStatus(String qsStatus) {
		this.qsStatus = qsStatus;
	}
	public String getNpiStatus() {
		return npiStatus;
	}
	public void setNpiStatus(String npiStatus) {
		this.npiStatus = npiStatus;
	}
	public String getDqeStatus() {
		return dqeStatus;
	}
	public void setDqeStatus(String dqeStatus) {
		this.dqeStatus = dqeStatus;
	}
	public String getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	public String getFinalStatus() {
		return finalStatus;
	}
	public void setFinalStatus(String finalStatus) {
		this.finalStatus = finalStatus;
	}
	public String getSqeLeadStatus() {
		return sqeLeadStatus;
	}
	public void setSqeLeadStatus(String sqeLeadStatus) {
		this.sqeLeadStatus = sqeLeadStatus;
	}
	public String getSkillLeadStatus() {
		return skillLeadStatus;
	}
	public void setSkillLeadStatus(String skillLeadStatus) {
		this.skillLeadStatus = skillLeadStatus;
	}
	public List<SupplierAdmitFile> getSupplierAdmitFiles() {
		return supplierAdmitFiles;
	}
	public void setSupplierAdmitFiles(List<SupplierAdmitFile> supplierAdmitFiles) {
		this.supplierAdmitFiles = supplierAdmitFiles;
	}
	public String getGpMaterialId() {
		return gpMaterialId;
	}
	public void setGpMaterialId(String gpMaterialId) {
		this.gpMaterialId = gpMaterialId;
	}
	public String getGpMaterialState() {
		return gpMaterialState;
	}
	public void setGpMaterialState(String gpMaterialState) {
		this.gpMaterialState = gpMaterialState;
	}
	public String getEeChecker() {
		return eeChecker;
	}
	public void setEeChecker(String eeChecker) {
		this.eeChecker = eeChecker;
	}
	public String getEeCheckerLog() {
		return eeCheckerLog;
	}
	public void setEeCheckerLog(String eeCheckerLog) {
		this.eeCheckerLog = eeCheckerLog;
	}
	public String getElseChecker() {
		return elseChecker;
	}
	public void setElseChecker(String elseChecker) {
		this.elseChecker = elseChecker;
	}
	public String getElseCheckerLog() {
		return elseCheckerLog;
	}
	public void setElseCheckerLog(String elseCheckerLog) {
		this.elseCheckerLog = elseCheckerLog;
	}
	public String getEeStatus() {
		return eeStatus;
	}
	public void setEeStatus(String eeStatus) {
		this.eeStatus = eeStatus;
	}
	public String getElseStatus() {
		return elseStatus;
	}
	public void setElseStatus(String elseStatus) {
		this.elseStatus = elseStatus;
	}
	public String getCountersignEE() {
		return countersignEE;
	}
	public void setCountersignEE(String countersignEE) {
		this.countersignEE = countersignEE;
	}
	public String getCountersignElse() {
		return countersignElse;
	}
	public void setCountersignElse(String countersignElse) {
		this.countersignElse = countersignElse;
	}
	public String getFileEE() {
		return fileEE;
	}
	public void setFileEE(String fileEE) {
		this.fileEE = fileEE;
	}
	public String getFileElse() {
		return fileElse;
	}
	public void setFileElse(String fileElse) {
		this.fileElse = fileElse;
	}
	public String getElseText() {
		return elseText;
	}
	public void setElseText(String elseText) {
		this.elseText = elseText;
	}
	public String getAdminState() {
		return adminState;
	}
	public void setAdminState(String adminState) {
		this.adminState = adminState;
	}
	
}

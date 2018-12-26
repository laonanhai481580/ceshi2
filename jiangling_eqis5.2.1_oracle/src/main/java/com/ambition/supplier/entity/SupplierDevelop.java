package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;
/**
 * 类名:供应商评价流程
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月10日 发布
 */
@Entity
@Table(name = "SUPPLIER_DEVELOP")
public class SupplierDevelop extends  WorkflowIdEntity{
		/**
		  *SupplierDevelop.java2016年11月1日
		 */
	private static final long serialVersionUID = 1L;
	private String formNo;//表单编号
	private String supplierType;//供应商分类
	private String urgencyLevel;//紧急程度
	private String formCreator;//制单人
	private Date formCreateDate;//制单日期
	private String department;//部门
	private String company;//公司
	private Date evaluateDate;//评价日期
	private String supplierCode;//供应商编号 
	private String supplierName;//供应商名称
	private String supplierServices;//提供产品/服务范围
	private String supplierCompanyAddress;//公司地址
	private String businessLicenseCode;//营业执照号码
	private String legalRepresentative;//法人代表
	private String supportTelephone;//业务负责人/电话
	private String qualitySupportTelephone;//品质负责人/电话
	private String companyTelephone;//公司电话
	private String companyFax;//传真
	private String remark;
	private String baseInfoFile;//基础信息附件
	private String threePapersFile;//三证
	private String integrityAgreementFile;//廉洁协议
	private String sampleEvaluateFile;//样品评估附件
	private String factoryAuditFile;//审厂资料
	private String purchasingFile;//采购框架协议
	private String supplierAnalyzeFile;//供应商竞争力分析表
	private String purchaseProcesser;//采购部办理人
	private String purchaseProcesserLog;//
	private String purchaseChecker;//采购中心审核人
	private String purchaseCheckerLog;
	private String purchaseEvaluate;//采购中心评价
	private String bossPurchase;//采购中心部门长
	private String bossPurchaseLog;
	private String developChecker;//工程研发部评价人
	private String developCheckerLog;
	private String developAuditType;//工程研发部审核方式
	private String developAuditLevel;//工程研发部审核等级
	private String bossDevelopChecker;//工程研发部部门长
	private String bossDevelopCheckerLog;
	private Double developQpaScore;//QPA分数
	private Double developQsaScore;//QSA分数
	private String qualityQsChecker;//品质中心QS评价人
	private String qualityQsCheckerLog;
	private String qualityQsAuditType;//品质中心QSL审核方式
	private String qualityQsAuditLevel;//品质中心QS审核等级
	private String bossQsQuality;//品质中心QS部门长审核人
	private String bossQsQualityLog;
	private Double csrScore;//CSR分数
	private String qualitySqeChecker;//品质中心Sqe评价人
	private String qualitySqeCheckerLog;
	private String qualitySqeAuditType;//品质中心QSL审核方式
	private String qualitySqeAuditLevel;//品质中心QS审核等级
	private String bossSqeQuality;//品质中心QS部门长审核人
	private String bossSqeQualityLog;
	private Double qualityQpaScore;////QPA分数
	private String purchaseManageChecker;//采购管理部评价人
	private String purManagerCheckerLog;
	private String purchaseManageType;//采购管理部审核方式
	private String purchaseManageLevel;//采购管理部审核方式
	private String purchaseManageManager;//采购管理部经理或以上评价人
	private String purManagerLog;
	private Double purchaseQsaScore;//QSA分数
	private String bossQuality;//品质中心部门长
	private String bossQualityLog;
	private String bossPurchaseManage;//采购管理部部门长
	private String bossPurchaseManageLog;
	private String bossCoo;//coo
	private String bossCooLog;
	private String manager;//集团总经理
	private String managerLog;
    private String superManager;//高级副总裁
    private String superManagerLog;
    private int toSupperManager=0;//0 否 1 是
    
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierType() {
		return supplierType;
	}
	public void setSupplierType(String supplierType) {
		this.supplierType = supplierType;
	}
	public String getUrgencyLevel() {
		return urgencyLevel;
	}
	public void setUrgencyLevel(String urgencyLevel) {
		this.urgencyLevel = urgencyLevel;
	}
	public String getFormCreator() {
		return formCreator;
	}
	public void setFormCreator(String formCreator) {
		this.formCreator = formCreator;
	}
	public Date getFormCreateDate() {
		return formCreateDate;
	}
	public void setFormCreateDate(Date formCreateDate) {
		this.formCreateDate = formCreateDate;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public Date getEvaluateDate() {
		return evaluateDate;
	}
	public void setEvaluateDate(Date evaluateDate) {
		this.evaluateDate = evaluateDate;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierServices() {
		return supplierServices;
	}
	public void setSupplierServices(String supplierServices) {
		this.supplierServices = supplierServices;
	}
	public String getSupplierCompanyAddress() {
		return supplierCompanyAddress;
	}
	public void setSupplierCompanyAddress(String supplierCompanyAddress) {
		this.supplierCompanyAddress = supplierCompanyAddress;
	}
	public String getBusinessLicenseCode() {
		return businessLicenseCode;
	}
	public void setBusinessLicenseCode(String businessLicenseCode) {
		this.businessLicenseCode = businessLicenseCode;
	}
	public String getLegalRepresentative() {
		return legalRepresentative;
	}
	public void setLegalRepresentative(String legalRepresentative) {
		this.legalRepresentative = legalRepresentative;
	}
	public String getSupportTelephone() {
		return supportTelephone;
	}
	public void setSupportTelephone(String supportTelephone) {
		this.supportTelephone = supportTelephone;
	}
	public String getQualitySupportTelephone() {
		return qualitySupportTelephone;
	}
	public void setQualitySupportTelephone(String qualitySupportTelephone) {
		this.qualitySupportTelephone = qualitySupportTelephone;
	}
	public String getCompanyTelephone() {
		return companyTelephone;
	}
	public void setCompanyTelephone(String companyTelephone) {
		this.companyTelephone = companyTelephone;
	}
	public String getCompanyFax() {
		return companyFax;
	}
	public void setCompanyFax(String companyFax) {
		this.companyFax = companyFax;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBaseInfoFile() {
		return baseInfoFile;
	}
	public void setBaseInfoFile(String baseInfoFile) {
		this.baseInfoFile = baseInfoFile;
	}
	public String getThreePapersFile() {
		return threePapersFile;
	}
	public void setThreePapersFile(String threePapersFile) {
		this.threePapersFile = threePapersFile;
	}
	public String getIntegrityAgreementFile() {
		return integrityAgreementFile;
	}
	public void setIntegrityAgreementFile(String integrityAgreementFile) {
		this.integrityAgreementFile = integrityAgreementFile;
	}
	public String getSampleEvaluateFile() {
		return sampleEvaluateFile;
	}
	public void setSampleEvaluateFile(String sampleEvaluateFile) {
		this.sampleEvaluateFile = sampleEvaluateFile;
	}
	public String getFactoryAuditFile() {
		return factoryAuditFile;
	}
	public void setFactoryAuditFile(String factoryAuditFile) {
		this.factoryAuditFile = factoryAuditFile;
	}
	public String getPurchasingFile() {
		return purchasingFile;
	}
	public void setPurchasingFile(String purchasingFile) {
		this.purchasingFile = purchasingFile;
	}
	public String getSupplierAnalyzeFile() {
		return supplierAnalyzeFile;
	}
	public void setSupplierAnalyzeFile(String supplierAnalyzeFile) {
		this.supplierAnalyzeFile = supplierAnalyzeFile;
	}
	public String getPurchaseChecker() {
		return purchaseChecker;
	}
	public void setPurchaseChecker(String purchaseChecker) {
		this.purchaseChecker = purchaseChecker;
	}
	public String getPurchaseEvaluate() {
		return purchaseEvaluate;
	}
	public void setPurchaseEvaluate(String purchaseEvaluate) {
		this.purchaseEvaluate = purchaseEvaluate;
	}
	public String getBossPurchase() {
		return bossPurchase;
	}
	public void setBossPurchase(String bossPurchase) {
		this.bossPurchase = bossPurchase;
	}
	
	public String getPurchaseCheckerLog() {
		return purchaseCheckerLog;
	}
	public void setPurchaseCheckerLog(String purchaseCheckerLog) {
		this.purchaseCheckerLog = purchaseCheckerLog;
	}
	public String getBossPurchaseLog() {
		return bossPurchaseLog;
	}
	public void setBossPurchaseLog(String bossPurchaseLog) {
		this.bossPurchaseLog = bossPurchaseLog;
	}
	public String getDevelopChecker() {
		return developChecker;
	}
	public void setDevelopChecker(String developChecker) {
		this.developChecker = developChecker;
	}
	public String getDevelopCheckerLog() {
		return developCheckerLog;
	}
	public void setDevelopCheckerLog(String developCheckerLog) {
		this.developCheckerLog = developCheckerLog;
	}
	public String getDevelopAuditType() {
		return developAuditType;
	}
	public void setDevelopAuditType(String developAuditType) {
		this.developAuditType = developAuditType;
	}
	public String getDevelopAuditLevel() {
		return developAuditLevel;
	}
	public void setDevelopAuditLevel(String developAuditLevel) {
		this.developAuditLevel = developAuditLevel;
	}
	public String getBossDevelopChecker() {
		return bossDevelopChecker;
	}
	public void setBossDevelopChecker(String bossDevelopChecker) {
		this.bossDevelopChecker = bossDevelopChecker;
	}
	public String getBossDevelopCheckerLog() {
		return bossDevelopCheckerLog;
	}
	public void setBossDevelopCheckerLog(String bossDevelopCheckerLog) {
		this.bossDevelopCheckerLog = bossDevelopCheckerLog;
	}
	public Double getDevelopQpaScore() {
		return developQpaScore;
	}
	public void setDevelopQpaScore(Double developQpaScore) {
		this.developQpaScore = developQpaScore;
	}
	public Double getDevelopQsaScore() {
		return developQsaScore;
	}
	public void setDevelopQsaScore(Double developQsaScore) {
		this.developQsaScore = developQsaScore;
	}
	public String getQualityQsChecker() {
		return qualityQsChecker;
	}
	public void setQualityQsChecker(String qualityQsChecker) {
		this.qualityQsChecker = qualityQsChecker;
	}
	public String getQualityQsCheckerLog() {
		return qualityQsCheckerLog;
	}
	public void setQualityQsCheckerLog(String qualityQsCheckerLog) {
		this.qualityQsCheckerLog = qualityQsCheckerLog;
	}
	public String getQualityQsAuditType() {
		return qualityQsAuditType;
	}
	public void setQualityQsAuditType(String qualityQsAuditType) {
		this.qualityQsAuditType = qualityQsAuditType;
	}
	public String getQualityQsAuditLevel() {
		return qualityQsAuditLevel;
	}
	public void setQualityQsAuditLevel(String qualityQsAuditLevel) {
		this.qualityQsAuditLevel = qualityQsAuditLevel;
	}
	public String getBossQsQuality() {
		return bossQsQuality;
	}
	public void setBossQsQuality(String bossQsQuality) {
		this.bossQsQuality = bossQsQuality;
	}
	public String getBossQsQualityLog() {
		return bossQsQualityLog;
	}
	public void setBossQsQualityLog(String bossQsQualityLog) {
		this.bossQsQualityLog = bossQsQualityLog;
	}
	public Double getCsrScore() {
		return csrScore;
	}
	public void setCsrScore(Double csrScore) {
		this.csrScore = csrScore;
	}
	public String getQualitySqeChecker() {
		return qualitySqeChecker;
	}
	public void setQualitySqeChecker(String qualitySqeChecker) {
		this.qualitySqeChecker = qualitySqeChecker;
	}
	public String getQualitySqeCheckerLog() {
		return qualitySqeCheckerLog;
	}
	public void setQualitySqeCheckerLog(String qualitySqeCheckerLog) {
		this.qualitySqeCheckerLog = qualitySqeCheckerLog;
	}
	public String getQualitySqeAuditType() {
		return qualitySqeAuditType;
	}
	public void setQualitySqeAuditType(String qualitySqeAuditType) {
		this.qualitySqeAuditType = qualitySqeAuditType;
	}
	public String getQualitySqeAuditLevel() {
		return qualitySqeAuditLevel;
	}
	public void setQualitySqeAuditLevel(String qualitySqeAuditLevel) {
		this.qualitySqeAuditLevel = qualitySqeAuditLevel;
	}
	public String getBossSqeQuality() {
		return bossSqeQuality;
	}
	public void setBossSqeQuality(String bossSqeQuality) {
		this.bossSqeQuality = bossSqeQuality;
	}
	public String getBossSqeQualityLog() {
		return bossSqeQualityLog;
	}
	public void setBossSqeQualityLog(String bossSqeQualityLog) {
		this.bossSqeQualityLog = bossSqeQualityLog;
	}
	public Double getQualityQpaScore() {
		return qualityQpaScore;
	}
	public void setQualityQpaScore(Double qualityQpaScore) {
		this.qualityQpaScore = qualityQpaScore;
	}
	public String getPurchaseManageChecker() {
		return purchaseManageChecker;
	}
	public void setPurchaseManageChecker(String purchaseManageChecker) {
		this.purchaseManageChecker = purchaseManageChecker;
	}
	public String getPurManagerCheckerLog() {
		return purManagerCheckerLog;
	}
	public void setPurManagerCheckerLog(String purManagerCheckerLog) {
		this.purManagerCheckerLog = purManagerCheckerLog;
	}
	public String getPurchaseManageType() {
		return purchaseManageType;
	}
	public void setPurchaseManageType(String purchaseManageType) {
		this.purchaseManageType = purchaseManageType;
	}
	public String getPurchaseManageLevel() {
		return purchaseManageLevel;
	}
	public void setPurchaseManageLevel(String purchaseManageLevel) {
		this.purchaseManageLevel = purchaseManageLevel;
	}
	public String getPurchaseManageManager() {
		return purchaseManageManager;
	}
	public void setPurchaseManageManager(String purchaseManageManager) {
		this.purchaseManageManager = purchaseManageManager;
	}
	public String getPurManagerLog() {
		return purManagerLog;
	}
	public void setPurManagerLog(String purManagerLog) {
		this.purManagerLog = purManagerLog;
	}
	public Double getPurchaseQsaScore() {
		return purchaseQsaScore;
	}
	public void setPurchaseQsaScore(Double purchaseQsaScore) {
		this.purchaseQsaScore = purchaseQsaScore;
	}
	public String getBossQuality() {
		return bossQuality;
	}
	public void setBossQuality(String bossQuality) {
		this.bossQuality = bossQuality;
	}
	public String getBossQualityLog() {
		return bossQualityLog;
	}
	public void setBossQualityLog(String bossQualityLog) {
		this.bossQualityLog = bossQualityLog;
	}
	public String getBossPurchaseManage() {
		return bossPurchaseManage;
	}
	public void setBossPurchaseManage(String bossPurchaseManage) {
		this.bossPurchaseManage = bossPurchaseManage;
	}
	public String getBossPurchaseManageLog() {
		return bossPurchaseManageLog;
	}
	public void setBossPurchaseManageLog(String bossPurchaseManageLog) {
		this.bossPurchaseManageLog = bossPurchaseManageLog;
	}
	public String getBossCoo() {
		return bossCoo;
	}
	public void setBossCoo(String bossCoo) {
		this.bossCoo = bossCoo;
	}
	public String getBossCooLog() {
		return bossCooLog;
	}
	public void setBossCooLog(String bossCooLog) {
		this.bossCooLog = bossCooLog;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getManagerLog() {
		return managerLog;
	}
	public void setManagerLog(String managerLog) {
		this.managerLog = managerLog;
	}
	public String getSuperManager() {
		return superManager;
	}
	public void setSuperManager(String superManager) {
		this.superManager = superManager;
	}
	public String getSuperManagerLog() {
		return superManagerLog;
	}
	public void setSuperManagerLog(String superManagerLog) {
		this.superManagerLog = superManagerLog;
	}
	public int getToSupperManager() {
		return toSupperManager;
	}
	public void setToSupperManager(int toSupperManager) {
		this.toSupperManager = toSupperManager;
	}
	public String getPurchaseProcesser() {
		return purchaseProcesser;
	}
	public void setPurchaseProcesser(String purchaseProcesser) {
		this.purchaseProcesser = purchaseProcesser;
	}
	public String getPurchaseProcesserLog() {
		return purchaseProcesserLog;
	}
	public void setPurchaseProcesserLog(String purchaseProcesserLog) {
		this.purchaseProcesserLog = purchaseProcesserLog;
	}
}

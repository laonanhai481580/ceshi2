package com.ambition.supplier.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ambition.product.base.IdEntity;
import com.ambition.supplier.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.norteksoft.wf.engine.client.ExtendField;
import com.norteksoft.wf.engine.client.FormFlowable;
import com.norteksoft.wf.engine.client.WorkflowInfo;

/**
 * 类名:供应商调查记录
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：供应商调查记录实体</p>
 * @author  赵骏
 * @version 1.00 2013-4-28 发布
 */
@Entity
@Table(name = "SUPPLIER_INVESTIGATE")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class SupplierInvestigate extends IdEntity implements FormFlowable{
	//状态
	public static final String STATE_DEFAULT = "未提交";
	//结果
	public static final String RESULT_PASS = "通过";
	public static final String RESULT_FAIL = "不通过";
	private static final long serialVersionUID = 3903410134826775393L;
	private String code;//调查表编号
	private String enterpriameseName;//企业名称
	private String enterpriseAddress;//企业地址
	private String depositBank;//开户银行
	private String bankAccount;//银行帐号
	private String postCode;//邮政编码
	private String enterpriseType;//企业类型
	private String isEssereCertificato;//是否通过体系认证
	private String certificatoName;//认证名称
	@Temporal(TemporalType.DATE)
	private Date certificatoPassDate;//认证通过日期
	private String isHavePermit;//有无生产许可证
	private String partner;//合作伙伴
	private String matchMethod;//合作方式
	private String isDevelopAbility;//是否有产品开发和生产能力
	private Integer employeeNumber;//员工总人数
	private Integer seniorEngineerNumber;//高级工程师人数
	private Integer engineerNumber;//工程师人数
	private Integer inspectionNumber;//产品质检人数
	private Integer managerNumber;//管理人员人数
	private Integer workNumber;//工人人数
	private Double yearlyCapacity;//年生产能力
	private String productProgram;//生产纲领
	private Double lastYearIncome;//去年销售收入
	private Double yearIncome;//年度销售收入
	private Double nextYearIncome;//预计明年销售收入
	private String adoptSoftware;//采用何种软件
	private String developeSoftware;//开发过何种软件 
	private String applySkill;//目前应用的技术 
	private String haveMouldDesign;//工装模具设计方式
	private String haveMouldMaufacture;//工装模具制造方式(自行)
	private String planning;//企业发展规划
	private String supportAndTechnique;//资金和技术支持情况
	private String program;//产品规划 
	private String newSkill;//引进的新技术
	private String mouldManufacture;//工装模具设计制造方式 
	private String detectionMeans;//引进的新试验设备和检测手段
	private String otherDirection;//其他说明 
	private Integer productTypeNum;//现生产产品种数
	private Integer majorTypeNum;//主要产品种数
	private String otherIntroduction;//其他 方面的介绍
	private String situation;//公司 情况 
	private String situationAttachment;//公司情况附件
	private String linkMan;//联系人
	private String linkPhone;//联系电话
	private String fax;//传真
	private String email;//邮箱地址
	private String evaluateMan;//评价人员
	@Temporal(TemporalType.DATE)
	private Date evaluateDate;//评价日期
	private String evaluateConclusion;//综合评价及结论
	private String auditer;//审核人员
	@Temporal(TemporalType.DATE)
	private Date auditTime;//审核时间
	private String auditeView;//审核意见
	@Column(length=100)
	private String auditResult;//审核结果
	private String investigateResult=RESULT_PASS;//调查结果
	private String investigateState = STATE_DEFAULT;//与流程一致
	@Embedded
	private ExtendField extendField;
	@Embedded
	private WorkflowInfo workflowInfo;
	@JsonIgnore
	@OneToMany(mappedBy="supplierInvestigate",cascade=javax.persistence.CascadeType.ALL)
    @Cascade(value=CascadeType.DELETE_ORPHAN)
	private List<SupplierInvestigateContact> contacts;//联系人信息
	
	@JsonIgnore
	@OneToMany(mappedBy="supplierInvestigate",cascade=javax.persistence.CascadeType.ALL)
    @Cascade(value=CascadeType.DELETE_ORPHAN)
	private List<SupplierInvestigateTestingDevice> testingDevices;//试验设备和检测手段
	
	@JsonIgnore
	@OneToMany(mappedBy="supplierInvestigate",cascade=javax.persistence.CascadeType.ALL)
    @Cascade(value=CascadeType.DELETE_ORPHAN)
	private List<SupplierInvestigateStockProduct> stockProducts;//现有的产品
	
	@JsonIgnore
	@OneToMany(mappedBy="supplierInvestigate",cascade=javax.persistence.CascadeType.ALL)
    @Cascade(value=CascadeType.DELETE_ORPHAN)
	@OrderBy(value="id")
	private List<SupplierInvestigateEvaluateRecord> evaluateRecords;//供应商自评记录

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEnterpriameseName() {
		return enterpriameseName;
	}

	public void setEnterpriameseName(String enterpriameseName) {
		this.enterpriameseName = enterpriameseName;
	}

	public String getEnterpriseAddress() {
		return enterpriseAddress;
	}

	public void setEnterpriseAddress(String enterpriseAddress) {
		this.enterpriseAddress = enterpriseAddress;
	}

	public String getDepositBank() {
		return depositBank;
	}

	public void setDepositBank(String depositBank) {
		this.depositBank = depositBank;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getEnterpriseType() {
		return enterpriseType;
	}

	public void setEnterpriseType(String enterpriseType) {
		this.enterpriseType = enterpriseType;
	}

	public String getIsEssereCertificato() {
		return isEssereCertificato;
	}

	public void setIsEssereCertificato(String isEssereCertificato) {
		this.isEssereCertificato = isEssereCertificato;
	}

	public String getCertificatoName() {
		return certificatoName;
	}

	public void setCertificatoName(String certificatoName) {
		this.certificatoName = certificatoName;
	}

	public Date getCertificatoPassDate() {
		return certificatoPassDate;
	}

	@Transient
	public String getCertificatoPassDateStr() {
		return DateUtil.formateDateStr(certificatoPassDate);
	}
	
	public void setCertificatoPassDate(Date certificatoPassDate) {
		this.certificatoPassDate = certificatoPassDate;
	}

	public String getIsHavePermit() {
		return isHavePermit;
	}

	public void setIsHavePermit(String isHavePermit) {
		this.isHavePermit = isHavePermit;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getMatchMethod() {
		return matchMethod;
	}

	public void setMatchMethod(String matchMethod) {
		this.matchMethod = matchMethod;
	}

	public String getIsDevelopAbility() {
		return isDevelopAbility;
	}

	public void setIsDevelopAbility(String isDevelopAbility) {
		this.isDevelopAbility = isDevelopAbility;
	}

	public Integer getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(Integer employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public Integer getSeniorEngineerNumber() {
		return seniorEngineerNumber;
	}

	public void setSeniorEngineerNumber(Integer seniorEngineerNumber) {
		this.seniorEngineerNumber = seniorEngineerNumber;
	}

	public Integer getEngineerNumber() {
		return engineerNumber;
	}

	public void setEngineerNumber(Integer engineerNumber) {
		this.engineerNumber = engineerNumber;
	}

	public Integer getInspectionNumber() {
		return inspectionNumber;
	}

	public void setInspectionNumber(Integer inspectionNumber) {
		this.inspectionNumber = inspectionNumber;
	}

	public Integer getManagerNumber() {
		return managerNumber;
	}

	public void setManagerNumber(Integer managerNumber) {
		this.managerNumber = managerNumber;
	}

	public Integer getWorkNumber() {
		return workNumber;
	}

	public void setWorkNumber(Integer workNumber) {
		this.workNumber = workNumber;
	}

	public Double getYearlyCapacity() {
		return yearlyCapacity;
	}

	public void setYearlyCapacity(Double yearlyCapacity) {
		this.yearlyCapacity = yearlyCapacity;
	}

	public String getProductProgram() {
		return productProgram;
	}

	public void setProductProgram(String productProgram) {
		this.productProgram = productProgram;
	}

	public Double getLastYearIncome() {
		return lastYearIncome;
	}

	public void setLastYearIncome(Double lastYearIncome) {
		this.lastYearIncome = lastYearIncome;
	}

	public Double getYearIncome() {
		return yearIncome;
	}

	public void setYearIncome(Double yearIncome) {
		this.yearIncome = yearIncome;
	}

	public Double getNextYearIncome() {
		return nextYearIncome;
	}

	public void setNextYearIncome(Double nextYearIncome) {
		this.nextYearIncome = nextYearIncome;
	}

	public String getAdoptSoftware() {
		return adoptSoftware;
	}

	public void setAdoptSoftware(String adoptSoftware) {
		this.adoptSoftware = adoptSoftware;
	}

	public String getDevelopeSoftware() {
		return developeSoftware;
	}

	public void setDevelopeSoftware(String developeSoftware) {
		this.developeSoftware = developeSoftware;
	}

	public String getApplySkill() {
		return applySkill;
	}

	public void setApplySkill(String applySkill) {
		this.applySkill = applySkill;
	}

	public String getHaveMouldDesign() {
		return haveMouldDesign;
	}

	public void setHaveMouldDesign(String haveMouldDesign) {
		this.haveMouldDesign = haveMouldDesign;
	}

	public String getHaveMouldMaufacture() {
		return haveMouldMaufacture;
	}

	public void setHaveMouldMaufacture(String haveMouldMaufacture) {
		this.haveMouldMaufacture = haveMouldMaufacture;
	}

	public String getPlanning() {
		return planning;
	}

	public void setPlanning(String planning) {
		this.planning = planning;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getNewSkill() {
		return newSkill;
	}

	public void setNewSkill(String newSkill) {
		this.newSkill = newSkill;
	}

	public String getMouldManufacture() {
		return mouldManufacture;
	}

	public void setMouldManufacture(String mouldManufacture) {
		this.mouldManufacture = mouldManufacture;
	}

	public String getDetectionMeans() {
		return detectionMeans;
	}

	public void setDetectionMeans(String detectionMeans) {
		this.detectionMeans = detectionMeans;
	}

	public String getOtherDirection() {
		return otherDirection;
	}

	public void setOtherDirection(String otherDirection) {
		this.otherDirection = otherDirection;
	}

	public Integer getProductTypeNum() {
		return productTypeNum;
	}

	public void setProductTypeNum(Integer productTypeNum) {
		this.productTypeNum = productTypeNum;
	}

	public Integer getMajorTypeNum() {
		return majorTypeNum;
	}

	public void setMajorTypeNum(Integer majorTypeNum) {
		this.majorTypeNum = majorTypeNum;
	}

	public String getOtherIntroduction() {
		return otherIntroduction;
	}

	public void setOtherIntroduction(String otherIntroduction) {
		this.otherIntroduction = otherIntroduction;
	}

	public String getSituation() {
		return situation;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}

	public String getSituationAttachment() {
		return situationAttachment;
	}

	public void setSituationAttachment(String situationAttachment) {
		this.situationAttachment = situationAttachment;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEvaluateConclusion() {
		return evaluateConclusion;
	}

	public void setEvaluateConclusion(String evaluateConclusion) {
		this.evaluateConclusion = evaluateConclusion;
	}

	public String getAuditer() {
		return auditer;
	}

	public void setAuditer(String auditer) {
		this.auditer = auditer;
	}

	public Date getAuditTime() {
		return auditTime;
	}
	@Transient
	public String getAuditTimeStr(){
		return DateUtil.formateDateStr(auditTime);
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getAuditeView() {
		return auditeView;
	}

	public void setAuditeView(String auditeView) {
		this.auditeView = auditeView;
	}

	public String getInvestigateState() {
		return investigateState;
	}

	public void setInvestigateState(String investigateState) {
		this.investigateState = investigateState;
	}

	public ExtendField getExtendField() {
		return extendField;
	}

	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}

	public List<SupplierInvestigateContact> getContacts() {
		return contacts;
	}

	public void setContacts(List<SupplierInvestigateContact> contacts) {
		this.contacts = contacts;
	}

	public List<SupplierInvestigateTestingDevice> getTestingDevices() {
		return testingDevices;
	}

	public void setTestingDevices(
			List<SupplierInvestigateTestingDevice> testingDevices) {
		this.testingDevices = testingDevices;
	}

	public List<SupplierInvestigateStockProduct> getStockProducts() {
		return stockProducts;
	}

	public void setStockProducts(List<SupplierInvestigateStockProduct> stockProducts) {
		this.stockProducts = stockProducts;
	}

	public List<SupplierInvestigateEvaluateRecord> getEvaluateRecords() {
		return evaluateRecords;
	}

	public void setEvaluateRecords(
			List<SupplierInvestigateEvaluateRecord> evaluateRecords) {
		this.evaluateRecords = evaluateRecords;
	}

	public String getEvaluateMan() {
		return evaluateMan;
	}

	public void setEvaluateMan(String evaluateMan) {
		this.evaluateMan = evaluateMan;
	}

	public Date getEvaluateDate() {
		return evaluateDate;
	}

	@Transient
	public String getEvaluateDateStr(){
		return DateUtil.formateDateStr(evaluateDate);
	}
	public void setEvaluateDate(Date evaluateDate) {
		this.evaluateDate = evaluateDate;
	}
 
	public String getSupportAndTechnique() {
		return supportAndTechnique;
	}

	public void setSupportAndTechnique(String supportAndTechnique) {
		this.supportAndTechnique = supportAndTechnique;
	}

	public String getInvestigateResult() {
		return investigateResult;
	}

	public void setInvestigateResult(String investigateResult) {
		this.investigateResult = investigateResult;
	}

	@Override
	public WorkflowInfo getWorkflowInfo() {
		return workflowInfo;
	}

	@Override
	public void setWorkflowInfo(WorkflowInfo workflowinfo) {
		this.workflowInfo = workflowinfo;
	}

	public String getAuditResult() {
		return auditResult;
	}

	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}
}

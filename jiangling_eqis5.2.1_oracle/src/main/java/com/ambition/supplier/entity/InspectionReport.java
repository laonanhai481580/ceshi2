package com.ambition.supplier.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;
import com.ambition.supplier.utils.DateUtil;
import com.norteksoft.wf.engine.client.ExtendField;
import com.norteksoft.wf.engine.client.FormFlowable;
import com.norteksoft.wf.engine.client.WorkflowInfo;

/**
 * 考察记录
 * @author 赵骏
 *
 */
@Entity
@Table(name = "SUPPLIER_INSPECTION_REPORT")
public class InspectionReport extends IdEntity implements FormFlowable{
	public static String STATE_DEFAULT = "考察中";
	public static String STATE_COUNTERSIGN = "正在会签";
	public static String STATE_AUDIT = "正在审核";
	public static String STATE_PASS = "同意";
	public static String STATE_FAIL = "不同意";
	private static final long serialVersionUID = -4311216062260502412L;
	
	private String code;//编号
	@ManyToOne
	@JoinColumn(name = "FK_SUPPLIER_ID")
	private Supplier supplier;
	private String address;//地址
	@Temporal(TemporalType.DATE)
	private Date inspectionDate;//考察日期
	private String inspectionMan;//考察人员
	private String inspectionManLoginName;//考察人员登录名
	private String inspectionBomCodes;//考察的物料编码
	private String inspectionBomNames;//考察的物料名称
	private String inspectionMaterialType;//考察的物料类别
	private String inspectionSupplyProductIds;//考察的物料ids
	private String produceProducts;//生产的主要产品
	private String reviewers;//评审人员
	private String reviewerLoginNames;//评审人员登录名
	private String matingProducts;//拟配套产品
	private String matingCompanys;//主要配套厂家
	private String phone;//电话
	private Integer employeeNumber;//员工数量
	private String fax;//传真
	private Integer technologyNumber;//技术人员数量
	private String homepage;//企业网站
	private Integer managerNumber;//管理人员数量
	private String email;//电子邮箱
	private Double factoryFlootArea;//厂区面积
	private Double workshopArea;//厂房面积
	@Temporal(TemporalType.DATE)
	private Date factoryBuildDate;//建厂时间
	private String enterpriseProperty;//企业性质
	private Double buildAbility;//生产能力
	private String assetComposing;//资产组成
	private Double lastOutput;//去年产量
	@Temporal(TemporalType.DATE)
	private Date gaizhiDate;//改制时间
	private Double latOutputValue;//去年产值
	private Double registrationAsset;//注册资金
	private String tradeLocation;//行业地位
	private Double fixedAssets;//固定资产
	private String researchAbility;//研发能力
	private String certification;//体系认证
	private String equipmentState;//设备情况
	private String checkInstrumentality;//检测手段
	private String marketState;//市场状态
	private String developable;//可发展性
	private Double totalFee = 0.0d;//总分
	private Double realFee = 0.0d;//实际得分
	private String comprehensiveEvaluation;//综合评价
	private String evaluationMembers;//评价小组成员
	private String evaluationMemberLoginNames;//评价小组成员登录名
	private String inspectionResult;//考察结果//通过或不通过
	public static final String RESULT_PASS = "通过";
	public static final String RESULT_FAIL = "不通过"; 
	@Temporal(TemporalType.DATE)
	private Date evaluationDate;//评价时间
	private String auditMans;//审核人员
	@Temporal(TemporalType.TIMESTAMP)
	private Date auditDate;//审核时间
	private Double reportVersion = 1.0;//版本
	
	@OneToMany(mappedBy="inspectionReport",cascade=CascadeType.ALL)
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OrderBy("orderNum asc")
	private List<InspectionGradeType> inspectionGradeTypes;//评分项目
	
	private String inspectionState = STATE_DEFAULT;//考察状态
	
	private String isImprovement;//是否改进
	
	@Embedded
	private WorkflowInfo workflowInfo;

	@Embedded
	private ExtendField extendField;
	
	public WorkflowInfo getWorkflowInfo() {
		return workflowInfo;
	}

	public void setWorkflowInfo(WorkflowInfo workflowInfo) {
		this.workflowInfo = workflowInfo;
	}

	public ExtendField getExtendField() {
		return extendField;
	}

	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Date getInspectionDate() {
		return inspectionDate;
	}

	public String getInspectionDateStr() {
		return DateUtil.formateDateStr(inspectionDate);
	}
	
	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public String getInspectionBomCodes() {
		return inspectionBomCodes;
	}

	public void setInspectionBomCodes(String inspectionBomCodes) {
		this.inspectionBomCodes = inspectionBomCodes;
	}

	public String getInspectionBomNames() {
		return inspectionBomNames;
	}

	public void setInspectionBomNames(String inspectionBomNames) {
		this.inspectionBomNames = inspectionBomNames;
	}

	public String getProduceProducts() {
		return produceProducts;
	}

	public void setProduceProducts(String produceProducts) {
		this.produceProducts = produceProducts;
	}

	public String getMatingProducts() {
		return matingProducts;
	}

	public void setMatingProducts(String matingProducts) {
		this.matingProducts = matingProducts;
	}

	public String getMatingCompanys() {
		return matingCompanys;
	}

	public void setMatingCompanys(String matingCompanys) {
		this.matingCompanys = matingCompanys;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(Integer employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Integer getTechnologyNumber() {
		return technologyNumber;
	}

	public void setTechnologyNumber(Integer technologyNumber) {
		this.technologyNumber = technologyNumber;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public Integer getManagerNumber() {
		return managerNumber;
	}

	public void setManagerNumber(Integer managerNumber) {
		this.managerNumber = managerNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getFactoryFlootArea() {
		return factoryFlootArea;
	}

	public void setFactoryFlootArea(Double factoryFlootArea) {
		this.factoryFlootArea = factoryFlootArea;
	}

	public Double getWorkshopArea() {
		return workshopArea;
	}

	public void setWorkshopArea(Double workshopArea) {
		this.workshopArea = workshopArea;
	}

	public Date getFactoryBuildDate() {
		return factoryBuildDate;
	}

	public String getFactoryBuildDateStr() {
		return DateUtil.formateDateStr(factoryBuildDate);
	}
	
	public void setFactoryBuildDate(Date factoryBuildDate) {
		this.factoryBuildDate = factoryBuildDate;
	}

	public List<InspectionGradeType> getInspectionGradeTypes() {
		return inspectionGradeTypes;
	}

	public void setInspectionGradeTypes(
			List<InspectionGradeType> inspectionGradeTypes) {
		this.inspectionGradeTypes = inspectionGradeTypes;
	}

	public String getEnterpriseProperty() {
		return enterpriseProperty;
	}

	public void setEnterpriseProperty(String enterpriseProperty) {
		this.enterpriseProperty = enterpriseProperty;
	}

	public Double getBuildAbility() {
		return buildAbility;
	}

	public void setBuildAbility(Double buildAbility) {
		this.buildAbility = buildAbility;
	}

	public String getAssetComposing() {
		return assetComposing;
	}

	public void setAssetComposing(String assetComposing) {
		this.assetComposing = assetComposing;
	}

	public Double getLastOutput() {
		return lastOutput;
	}

	public void setLastOutput(Double lastOutput) {
		this.lastOutput = lastOutput;
	}

	public Date getGaizhiDate() {
		return gaizhiDate;
	}

	public String getGaizhiDateStr() {
		return DateUtil.formateDateStr(gaizhiDate);
	}
	
	public void setGaizhiDate(Date gaizhiDate) {
		this.gaizhiDate = gaizhiDate;
	}

	public Double getLatOutputValue() {
		return latOutputValue;
	}

	public void setLatOutputValue(Double latOutputValue) {
		this.latOutputValue = latOutputValue;
	}

	public Double getRegistrationAsset() {
		return registrationAsset;
	}

	public void setRegistrationAsset(Double registrationAsset) {
		this.registrationAsset = registrationAsset;
	}

	public String getTradeLocation() {
		return tradeLocation;
	}

	public void setTradeLocation(String tradeLocation) {
		this.tradeLocation = tradeLocation;
	}

	public Double getFixedAssets() {
		return fixedAssets;
	}

	public void setFixedAssets(Double fixedAssets) {
		this.fixedAssets = fixedAssets;
	}

	public String getResearchAbility() {
		return researchAbility;
	}

	public void setResearchAbility(String researchAbility) {
		this.researchAbility = researchAbility;
	}

	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public String getEquipmentState() {
		return equipmentState;
	}

	public void setEquipmentState(String equipmentState) {
		this.equipmentState = equipmentState;
	}

	public String getCheckInstrumentality() {
		return checkInstrumentality;
	}

	public String getAuditMans() {
		return auditMans;
	}

	public void setAuditMans(String auditMans) {
		this.auditMans = auditMans;
	}

	public void setCheckInstrumentality(String checkInstrumentality) {
		this.checkInstrumentality = checkInstrumentality;
	}

	public String getMarketState() {
		return marketState;
	}

	public void setMarketState(String marketState) {
		this.marketState = marketState;
	}

	public String getDevelopable() {
		return developable;
	}

	public void setDevelopable(String developable) {
		this.developable = developable;
	}

	public String getComprehensiveEvaluation() {
		return comprehensiveEvaluation;
	}

	public void setComprehensiveEvaluation(String comprehensiveEvaluation) {
		this.comprehensiveEvaluation = comprehensiveEvaluation;
	}

	public String getEvaluationMembers() {
		return evaluationMembers;
	}

	public void setEvaluationMembers(String evaluationMembers) {
		this.evaluationMembers = evaluationMembers;
	}

	public Date getEvaluationDate() {
		return evaluationDate;
	}
	public String getEvaluationDateStr() {
		return DateUtil.formateDateStr(evaluationDate);
	}
	public void setEvaluationDate(Date evaluationDate) {
		this.evaluationDate = evaluationDate;
	}

	public String getInspectionState() {
		return inspectionState;
	}

	public void setInspectionState(String inspectionState) {
		this.inspectionState = inspectionState;
	}

	public String getIsImprovement() {
		return isImprovement;
	}

	public void setIsImprovement(String isImprovement) {
		this.isImprovement = isImprovement;
	}

	public Double getRealFee() {
		return realFee;
	}

	public void setRealFee(Double realFee) {
		this.realFee = realFee;
	}

	public String getInspectionSupplyProductIds() {
		return inspectionSupplyProductIds;
	}

	public void setInspectionSupplyProductIds(String inspectionSupplyProductIds) {
		this.inspectionSupplyProductIds = inspectionSupplyProductIds;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getInspectionMan() {
		return inspectionMan;
	}

	public void setInspectionMan(String inspectionMan) {
		this.inspectionMan = inspectionMan;
	}

	public String getEvaluationMemberLoginNames() {
		return evaluationMemberLoginNames;
	}

	public void setEvaluationMemberLoginNames(String evaluationMemberLoginNames) {
		this.evaluationMemberLoginNames = evaluationMemberLoginNames;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public String getAuditDateStr() {
		return DateUtil.formateDateStr(auditDate);
	}
	
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public String getInspectionResult() {
		return inspectionResult;
	}

	public void setInspectionResult(String inspectionResult) {
		this.inspectionResult = inspectionResult;
	}
	
	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public Double getReportVersion() {
		return reportVersion;
	}

	public void setReportVersion(Double reportVersion) {
		this.reportVersion = reportVersion;
	}

	public String getInspectionMaterialType() {
		return inspectionMaterialType;
	}

	public void setInspectionMaterialType(String inspectionMaterialType) {
		this.inspectionMaterialType = inspectionMaterialType;
	}

	public String getReviewers() {
		return reviewers;
	}

	public void setReviewers(String reviewers) {
		this.reviewers = reviewers;
	}

	public String getReviewerLoginNames() {
		return reviewerLoginNames;
	}

	public void setReviewerLoginNames(String reviewerLoginNames) {
		this.reviewerLoginNames = reviewerLoginNames;
	}

	public String getInspectionManLoginName() {
		return inspectionManLoginName;
	}

	public void setInspectionManLoginName(String inspectionManLoginName) {
		this.inspectionManLoginName = inspectionManLoginName;
	}

	public String toString(){
		return "供应商质量管理：考察记录    编号"+this.code+"，考察日期"+this.inspectionDate+"，考察人"+this.inspectionMan;
	}
}

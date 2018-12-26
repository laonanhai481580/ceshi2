package com.ambition.supplier.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.norteksoft.wf.engine.client.ExtendField;

/**
 * 供应商资料
 * @author ambition-zhaojun
 *
 */
@Entity
@Table(name = "SUPPLIER_SUPPLIER")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class Supplier extends IdEntity {
	private static final long serialVersionUID = 4518308827240189057L;
	private Integer orderNum;//序号
	private String evaluateState;//评价情况；质量
	private String evaluateDevelopState;//研发
	private String evaluatePurcheState;//采购
	private String evaluateConsoleState;//调达
	private String code;//供应商编号 
	private String name;//供应商名称
	private String supplyMaterial;//供应物料
	private String shareGroup;//共用事业群
	private String materialType;//材料类别
	private String linkMan;//联系人
	private String linkPhone;//联系电话
	private String address;//企业地址 供应商地址
	private String supplierEmail;//供应商邮箱地址
	private Date enterDate;//登录日期
	private String remark;//备注
	@JsonIgnore
	@OneToMany(mappedBy="supplier",cascade=javax.persistence.CascadeType.ALL)
    @Cascade(value=CascadeType.DELETE_ORPHAN)
	private List<SupplyProduct> supplyProducts;//供应产品
	
	@JsonIgnore
	@OneToMany(mappedBy="supplier",cascade=javax.persistence.CascadeType.ALL)
    @Cascade(value=CascadeType.DELETE_ORPHAN)
	private List<Certificate> certificates;//体系证书
	
	private String fullName;//供应商全称
	private String checkUser;//审核人员
	private String helpCode;//助记码
	private String bank;//开户银行
	private String bankAccount;//银行帐户
	private String country;//国家
	private String province;//省份
	private String type;//供应商分类
	private String license;//营业执照
	private String taxNo;//
	private String organizationalNo;//组织机构代码
	private String companyPhone;//公司电话
	@Temporal(TemporalType.DATE)
	private Date regsterDate;//注册日期
	@Temporal(TemporalType.DATE)
	private Date passDate;//批准日期
	@Temporal(TemporalType.DATE)
	private Date inureDate;//生效日期
	@Temporal(TemporalType.DATE)
	private Date abateDate;//失效日期
	private String supplyGrade;//供应商等级
	private String supplyType;//供应商类别
	private String companyType;//公司类别
	private String region;//地区
	private String enterpriseProperty;//企业性质
	private String estimateModelName;//模型名称
	private Long estimateModelId;//模型编号
	private String importance;//重要度
	private String shortName;//简称
	private String postalcode;//邮政编码
	private String fax;//传真
	private String homepage;//主页
	private String mobilephone;//手机
	private String email;//邮箱地址
	private String linkManDepartment;//联系人部门
	private String linkManDuty;//联系人职务
	private String legalPerson;//法人代表
	private String membership;//隶属关系
	private Date createDate;//创建日期
	private Integer headcount;//员工总人数
	private String equityStake;//股权关系
	private Integer managerCount;//管理人员
	private Integer onLineOperationCount;//直接工人
	private Integer indirectOperationCount;//间接工人
	private Integer technologyCount;//技术人员
	private Integer qualityControlCount;//质检人员
	private Double floorArea;//占地面积
	private Double builtUpArea;//建筑面积
	private Double productArea;//生产面积
	private Double buildAbility;//生产能力
	private String tradeExperience;//
	private String firstHalfScale;
	private String purchaseRadius;
	private String certificationInfo;
	private String shortDeliveryTime;
	private String longPayTime;//
	@Column(length=3000)
	private String productInfo;//
	@Temporal(TemporalType.DATE)
	private Date gaizhiDate;//改制时间
	private Double registrationAsset;//注册资金
	private String tradeLocation;//行业地位
	private String researchAbility;//研发能力
	private Double grossAssets;//总资产
	private Double netAssets;//净资产
	private Double totalLiabilities;//负债总额
	private Double fixedAssets;//固定资产
	private Double netFixedAssets;//固定资产净值
	private String manageType;//管理类别
	private Boolean certificationOf3c;//是否通3c认证
	private Boolean certificationOfTS16949;//是否通过TS16949
	private String otherCertification;//其他认证情况
	private String majorProductType;//主要产品类别
	@Column(length=1000)
	private String enterpriseDescription;//企业情况
	private String equipmentDescription;//设备情况
	private String toolingDescription;//工艺装备情况
	private String flowDescription;//流程
	private String qualityAssurance;//质量保证
	private String manageDescription;//管理情况
	private String state = STATE_QUALIFIED;//状态  如:潜在,准供应商,合格,已淘汰
	private String updateFlag = "0";//
	private Integer deleted=0;//1：删除，0：保留
	/**
	 * 合格
	 */
	public static final String STATE_QUALIFIED = "合格";
	/**
	 * 潜在
	 */
	public static final String STATE_POTENTIAL = "潜在";
	/**
	 * 准供应商
	 */
	public static final String STATE_ALLOW = "准供应商";
	/**
	 * 已淘汰
	 */
	public static final String STATE_ELIMINATED = "已淘汰";
	
	private String useState = USESTATE_QUALIFIED;//使用状态
	public static final String USESTATE_QUALIFIED = "使用";
	public static final String USESTATE_DISABLE = "禁用";
	public static final String USESTATE_STOP = "停用";
	
	private Double estimateGoal;//评价得分
	private String estimateDegree;//评价等级
	private String waringSign;//红黄牌标识
	
	@Embedded
	private ExtendField extendField;
//	
//	@JsonIgnore
//	@OneToMany(mappedBy="supplier",cascade=javax.persistence.CascadeType.ALL)
//	@OrderBy(value="inspectionDate desc")
//	private List<InspectionReport> inspectionReports;//检验报告
//	
//	@JsonIgnore
//	@OneToMany(mappedBy="supplier",cascade=javax.persistence.CascadeType.ALL)
//	private List<AppraisalReport> appraisalReports;//鉴定报告
	
	
//	@JsonIgnore
//	@OneToMany(mappedBy="supplier",cascade=javax.persistence.CascadeType.ALL)
//	private List<SupplierGoal> supplierGoals;//供应商得分表

//	@JsonIgnore
//	@OneToMany(mappedBy="supplier",cascade=javax.persistence.CascadeType.ALL)
//    @Cascade(value=CascadeType.DELETE_ORPHAN)
//	private List<SupplierLinkMan> supplierLinkMans;//联系人列表
//	
//	@JsonIgnore
//	@OneToMany(mappedBy="supplier",cascade=javax.persistence.CascadeType.ALL)
//    @Cascade(value=CascadeType.DELETE_ORPHAN)
//	private List<ProductExploitationRecord> productExploitationRecords;//供应商对应的产品开发记录
//	
//	@JsonIgnore
//	@OneToMany(mappedBy="supplier",cascade=javax.persistence.CascadeType.ALL)
//    @Cascade(value=CascadeType.DELETE_ORPHAN)
//	private List<ProductAdmittanceRecord> productAdmittanceRecords;//供应商对应的产品准入记录
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getEnterpriseProperty() {
		return enterpriseProperty;
	}

	public void setEnterpriseProperty(String enterpriseProperty) {
		this.enterpriseProperty = enterpriseProperty;
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

	public String getImportance() {
		return importance;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLinkManDepartment() {
		return linkManDepartment;
	}

	public void setLinkManDepartment(String linkManDepartment) {
		this.linkManDepartment = linkManDepartment;
	}

	public String getLinkManDuty() {
		return linkManDuty;
	}

	public void setLinkManDuty(String linkManDuty) {
		this.linkManDuty = linkManDuty;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getMembership() {
		return membership;
	}

	public void setMembership(String membership) {
		this.membership = membership;
	}

	public Date getCreateDate() {
		return createDate;
	}

	@Transient
	public String getCreateDateStr(){
		if(createDate == null){
			return "";
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(createDate);
		}
	}
	@Transient
	public String getGaizhiDateStr(){
		if(gaizhiDate == null){
			return "";
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(gaizhiDate);
		}
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getHeadcount() {
		return headcount;
	}

	public void setHeadcount(Integer headcount) {
		this.headcount = headcount;
	}

	public String getEquityStake() {
		return equityStake;
	}

	public void setEquityStake(String equityStake) {
		this.equityStake = equityStake;
	}

	public Integer getManagerCount() {
		return managerCount;
	}

	public void setManagerCount(Integer managerCount) {
		this.managerCount = managerCount;
	}

	public Integer getOnLineOperationCount() {
		return onLineOperationCount;
	}

	public void setOnLineOperationCount(Integer onLineOperationCount) {
		this.onLineOperationCount = onLineOperationCount;
	}

	public Integer getIndirectOperationCount() {
		return indirectOperationCount;
	}

	public void setIndirectOperationCount(Integer indirectOperationCount) {
		this.indirectOperationCount = indirectOperationCount;
	}

	public Integer getTechnologyCount() {
		return technologyCount;
	}

	public void setTechnologyCount(Integer technologyCount) {
		this.technologyCount = technologyCount;
	}

	public Integer getQualityControlCount() {
		return qualityControlCount;
	}

	public void setQualityControlCount(Integer qualityControlCount) {
		this.qualityControlCount = qualityControlCount;
	}

	public Double getFloorArea() {
		return floorArea;
	}

	public void setFloorArea(Double floorArea) {
		this.floorArea = floorArea;
	}

	public Double getBuiltUpArea() {
		return builtUpArea;
	}

	public void setBuiltUpArea(Double builtUpArea) {
		this.builtUpArea = builtUpArea;
	}

	public Double getProductArea() {
		return productArea;
	}

	public void setProductArea(Double productArea) {
		this.productArea = productArea;
	}

	public Double getGrossAssets() {
		return grossAssets;
	}

	public void setGrossAssets(Double grossAssets) {
		this.grossAssets = grossAssets;
	}

	public Double getNetAssets() {
		return netAssets;
	}

	public void setNetAssets(Double netAssets) {
		this.netAssets = netAssets;
	}

	public Double getTotalLiabilities() {
		return totalLiabilities;
	}

	public void setTotalLiabilities(Double totalLiabilities) {
		this.totalLiabilities = totalLiabilities;
	}

	public Double getFixedAssets() {
		return fixedAssets;
	}

	public void setFixedAssets(Double fixedAssets) {
		this.fixedAssets = fixedAssets;
	}

	public Double getNetFixedAssets() {
		return netFixedAssets;
	}

	public void setNetFixedAssets(Double netFixedAssets) {
		this.netFixedAssets = netFixedAssets;
	}

	public String getManageType() {
		return manageType;
	}

	public void setManageType(String manageType) {
		this.manageType = manageType;
	}

	public Boolean getCertificationOf3c() {
		return certificationOf3c;
	}

	public void setCertificationOf3c(Boolean certificationOf3c) {
		this.certificationOf3c = certificationOf3c;
	}

	public Boolean getCertificationOfTS16949() {
		return certificationOfTS16949;
	}

	public void setCertificationOfTS16949(Boolean certificationOfTS16949) {
		this.certificationOfTS16949 = certificationOfTS16949;
	}

	public String getOtherCertification() {
		return otherCertification;
	}

	public void setOtherCertification(String otherCertification) {
		this.otherCertification = otherCertification;
	}

	public String getMajorProductType() {
		return majorProductType;
	}

	public void setMajorProductType(String majorProductType) {
		this.majorProductType = majorProductType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEnterpriseDescription() {
		return enterpriseDescription;
	}

	public void setEnterpriseDescription(String enterpriseDescription) {
		this.enterpriseDescription = enterpriseDescription;
	}

	public String getEquipmentDescription() {
		return equipmentDescription;
	}

	public void setEquipmentDescription(String equipmentDescription) {
		this.equipmentDescription = equipmentDescription;
	}

	public String getToolingDescription() {
		return toolingDescription;
	}

	public void setToolingDescription(String toolingDescription) {
		this.toolingDescription = toolingDescription;
	}

	public String getFlowDescription() {
		return flowDescription;
	}

	public void setFlowDescription(String flowDescription) {
		this.flowDescription = flowDescription;
	}

	public String getQualityAssurance() {
		return qualityAssurance;
	}

	public void setQualityAssurance(String qualityAssurance) {
		this.qualityAssurance = qualityAssurance;
	}

	public String getManageDescription() {
		return manageDescription;
	}

	public void setManageDescription(String manageDescription) {
		this.manageDescription = manageDescription;
	}

	public List<SupplyProduct> getSupplyProducts() {
		return supplyProducts;
	}

	public void setSupplyProducts(List<SupplyProduct> supplyProducts) {
		this.supplyProducts = supplyProducts;
	}

	public List<Certificate> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<Certificate> certificates) {
		this.certificates = certificates;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public ExtendField getExtendField() {
		return extendField;
	}

	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}

	public String getEstimateModelName() {
		return estimateModelName;
	}

	public void setEstimateModelName(String estimateModelName) {
		this.estimateModelName = estimateModelName;
	}

	public Long getEstimateModelId() {
		return estimateModelId;
	}

	public void setEstimateModelId(Long estimateModelId) {
		this.estimateModelId = estimateModelId;
	}
	
	public Double getEstimateGoal() {
		return estimateGoal;
	}

	public void setEstimateGoal(Double estimateGoal) {
		this.estimateGoal = estimateGoal;
	}

	public String getEstimateDegree() {
		return estimateDegree;
	}

	public void setEstimateDegree(String estimateDegree) {
		this.estimateDegree = estimateDegree;
	}

	public String getWaringSign() {
		return waringSign;
	}

	public void setWaringSign(String waringSign) {
		this.waringSign = waringSign;
	}

	@Transient
	public String getSupplyProductNames(){
		if(supplyProducts != null){
			Map<String,Boolean> nameMap = new HashMap<String, Boolean>();
			StringBuffer sb = new StringBuffer("");
			for(SupplyProduct supplyProduct : supplyProducts){
				if(StringUtils.isEmpty(supplyProduct.getName())){
					continue;
				}
				if(nameMap.containsKey(supplyProduct.getName())){
					continue;
				}
				nameMap.put(supplyProduct.getName(),true);
				if(sb.length() > 0){
					sb.append(",");
				}
				sb.append(supplyProduct.getName());
			}
			return sb.toString();
		}else{
			return "";
		}
	}


	@Transient
	public String getSupplyProductCodes(){
		if(supplyProducts != null){
			StringBuffer sb = new StringBuffer("");
			for(SupplyProduct supplyProduct : supplyProducts){
				if(StringUtils.isEmpty(supplyProduct.getCode())){
					continue;
				}
				if(sb.length() > 0){
					sb.append(",");
				}
				sb.append(supplyProduct.getCode());
			}
			return sb.toString();
		}else{
			return "";
		}
	}
	
	@Transient
	public String getSupplyProductStr(){
		if(supplyProducts != null){
			Map<String,Boolean> nameMap = new HashMap<String, Boolean>();
			StringBuffer sb = new StringBuffer("");
			for(SupplyProduct supplyProduct : supplyProducts){
				if(StringUtils.isEmpty(supplyProduct.getName())){
					continue;
				}
				if(nameMap.containsKey(supplyProduct.getName())){
					continue;
				}
				nameMap.put(supplyProduct.getName(),true);
				if(sb.length() > 0){
					sb.append(",");
				}
				sb.append(supplyProduct.getName());
			}
			return sb.toString();
		}else{
			return "";
		}
	}

	public String getUseState() {
		return useState;
	}

	public void setUseState(String useState) {
		this.useState = useState;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

//	public List<SupplierGoal> getSupplierGoals() {
//		return supplierGoals;
//	}
//
//	public void setSupplierGoals(List<SupplierGoal> supplierGoals) {
//		this.supplierGoals = supplierGoals;
//	}

	public Double getBuildAbility() {
		return buildAbility;
	}

	public void setBuildAbility(Double buildAbility) {
		this.buildAbility = buildAbility;
	}

	public Date getGaizhiDate() {
		return gaizhiDate;
	}

	public void setGaizhiDate(Date gaizhiDate) {
		this.gaizhiDate = gaizhiDate;
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

	public String getResearchAbility() {
		return researchAbility;
	}

	public void setResearchAbility(String researchAbility) {
		this.researchAbility = researchAbility;
	}
	

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getCheckUser() {
		return checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public String getHelpCode() {
		return helpCode;
	}

	public void setHelpCode(String helpCode) {
		this.helpCode = helpCode;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public Date getRegsterDate() {
		return regsterDate;
	}

	public void setRegsterDate(Date regsterDate) {
		this.regsterDate = regsterDate;
	}

	public Date getPassDate() {
		return passDate;
	}

	public void setPassDate(Date passDate) {
		this.passDate = passDate;
	}

	public Date getAbateDate() {
		return abateDate;
	}

	public void setAbateDate(Date abateDate) {
		this.abateDate = abateDate;
	}

	public String getSupplyGrade() {
		return supplyGrade;
	}

	public void setSupplyGrade(String supplyGrade) {
		this.supplyGrade = supplyGrade;
	}

	public String getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(String supplyType) {
		this.supplyType = supplyType;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public Date getInureDate() {
		return inureDate;
	}

	public void setInureDate(Date inureDate) {
		this.inureDate = inureDate;
	}
	
	public String getUpdateFlag() {
		return updateFlag;
	}
	
	public void setUpdateFlag(String updateFlag) {
		this.updateFlag = updateFlag;
	}

	public String getTaxNo() {
		return taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	public String getOrganizationalNo() {
		return organizationalNo;
	}

	public void setOrganizationalNo(String organizationalNo) {
		this.organizationalNo = organizationalNo;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getTradeExperience() {
		return tradeExperience;
	}

	public void setTradeExperience(String tradeExperience) {
		this.tradeExperience = tradeExperience;
	}

	public String getFirstHalfScale() {
		return firstHalfScale;
	}

	public void setFirstHalfScale(String firstHalfScale) {
		this.firstHalfScale = firstHalfScale;
	}

	public String getPurchaseRadius() {
		return purchaseRadius;
	}

	public void setPurchaseRadius(String purchaseRadius) {
		this.purchaseRadius = purchaseRadius;
	}

	public String getCertificationInfo() {
		return certificationInfo;
	}

	public void setCertificationInfo(String certificationInfo) {
		this.certificationInfo = certificationInfo;
	}

	public String getShortDeliveryTime() {
		return shortDeliveryTime;
	}

	public void setShortDeliveryTime(String shortDeliveryTime) {
		this.shortDeliveryTime = shortDeliveryTime;
	}

	public String getLongPayTime() {
		return longPayTime;
	}

	public void setLongPayTime(String longPayTime) {
		this.longPayTime = longPayTime;
	}

	public String getProductInfo() {
		return productInfo;
	}

	public void setProductInfo(String productInfo) {
		this.productInfo = productInfo;
	}
	
	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getSupplyMaterial() {
		return supplyMaterial;
	}

	public void setSupplyMaterial(String supplyMaterial) {
		this.supplyMaterial = supplyMaterial;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public Date getEnterDate() {
		return enterDate;
	}

	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}
	public String getSupplierEmail() {
		return supplierEmail;
	}

	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}

	public String getShareGroup() {
		return shareGroup;
	}

	public void setShareGroup(String shareGroup) {
		this.shareGroup = shareGroup;
	}

	public String getEvaluateState() {
		return evaluateState;
	}

	public void setEvaluateState(String evaluateState) {
		this.evaluateState = evaluateState;
	}

	public String getEvaluateDevelopState() {
		return evaluateDevelopState;
	}

	public void setEvaluateDevelopState(String evaluateDevelopState) {
		this.evaluateDevelopState = evaluateDevelopState;
	}

	public String getEvaluatePurcheState() {
		return evaluatePurcheState;
	}

	public void setEvaluatePurcheState(String evaluatePurcheState) {
		this.evaluatePurcheState = evaluatePurcheState;
	}

	public String getEvaluateConsoleState() {
		return evaluateConsoleState;
	}

	public void setEvaluateConsoleState(String evaluateConsoleState) {
		this.evaluateConsoleState = evaluateConsoleState;
	}

	public String toString(){
		return "供应商质量管理：供应商档案  "+this.state+" 供应商,ID"+this.getId()+",名称"+this.name+",日期"+this.createDate;
	}
}

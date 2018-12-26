package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ambition.product.base.IdEntity;

/**
 * 类名:供应商评价状态
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2014-1-19 发布
 */
public class SupplierEvaluateState extends IdEntity {
	private static final long serialVersionUID = 4518308827240189057L;
	private String code;//供应商编号 
	private String name;//供应商名称
	private String fullName;//供应商全称
	private String checkUser;//审核人员
	private String helpCode;//助记码
	private String bank;//开户银行
	private String bankAccount;//银行帐户
	private String country;//国家
	private String province;//省份
	private String type;//供应商分类
	private String license;//营业执照
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
	private String linkMan;//联系人
	private String linkPhone;//联系电话
	private String estimateModelName;//模型名称
	private Long estimateModelId;//模型编号
	private String importance;//重要度
	private String address;//企业地址
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
	private String remark;//备注
	@Column(length=1000)
	private String enterpriseDescription;//企业情况
	private String equipmentDescription;//设备情况
	private String toolingDescription;//工艺装备情况
	private String flowDescription;//流程
	private String qualityAssurance;//质量保证
	private String manageDescription;//管理情况
	private String technologicalTransformationDescription;//技术改造情况
	private String state;
	private Long qcdsId;//qcds的ID
	private String qcdsCode;//qcds的编码
	private Double percentageTotal;//得分
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
	public Date getInureDate() {
		return inureDate;
	}
	public void setInureDate(Date inureDate) {
		this.inureDate = inureDate;
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
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
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
	public String getTechnologicalTransformationDescription() {
		return technologicalTransformationDescription;
	}
	public void setTechnologicalTransformationDescription(
			String technologicalTransformationDescription) {
		this.technologicalTransformationDescription = technologicalTransformationDescription;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Long getQcdsId() {
		return qcdsId;
	}
	public void setQcdsId(Long qcdsId) {
		this.qcdsId = qcdsId;
	}
	public Double getPercentageTotal() {
		return percentageTotal;
	}
	public void setPercentageTotal(Double percentageTotal) {
		this.percentageTotal = percentageTotal;
	}
	public String getQcdsCode() {
		return qcdsCode;
	}
	public void setQcdsCode(String qcdsCode) {
		this.qcdsCode = qcdsCode;
	}
}

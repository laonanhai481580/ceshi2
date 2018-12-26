package com.ambition.cost.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 类名: 质量成本
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2014-11-29 发布
 */
@Entity
@Table(name = "COST_COST_RECORD")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class CostRecord extends IdEntity {
	/**成本来源:集成*/
	public static final String SOURCE_TYPE_COLLECTION = "集成";
	/**成本来源:手工录入*/
	public static final String SOURCE_TYPE_INPUT = "手工录入";
	private static final long serialVersionUID = -9181689261077768159L;
	private String formNo="手工录入";//来源单号
	private Integer companyISN;//公司内码
	private String erpCode;//erp中的成本代码
	private Date refDate;//记帐日期
	private String collectionItemName = "";//集成时的项目名称
	private String occurringMonthStr;//月份字符串,手动维护记录时有用到
	private Integer occurringMonth;//发生月份
	private String levelTwoCode;//二级代码
	private String levelTwoCodeSap;//sap二级代码
	private String levelTwoName;//二级成本名称
	private String levelThreeCode;//三级代码
	private String levelThreeCodeSap;//三级代码
	private String levelThreeName;//三级成本名称
	private String code;//末级代码
	private String name;//末级名称
	private String costType;//成本类型
	//重检、拆解损失费用
	private String model;//机种
	private String customer;//客户
	private String type;//类别
	private String storeCode;//存货编码
	private String storeName;//存货名称
	private String checkType;//重检类别
	private Integer amount;//重检数量
	private Integer checkTime;//重检工时
	private Double checkOld;//重检折旧
	private Double checkWater;//重检水电
	
	//材料损失金额
	//private String model;//机种
	private String brand;//品牌
	//private String storeCode;//存货编码
	private String materialName;//物料名称
	private String dealStyle;//事物处理类型
	//private String deptName;//部门名称
	//private Integer amount;//数量
	//private Double budgetValue;//配价
	
	//客户索赔
	//private String model;//机种
	//private String customer;//客户
	//private String storeCode;//存货编码
	private String reason;//原因
	//private Integer amount;//数量
	private Double yuanValue;//原价
	private Double xianValue;//现价

	
	//质量处理费用
	private String evectionMan;//出差人员
	//private String type;//类别
	//private String customer;//客户
	private String address;//地址
	private String workMatters;//工作事项
	
	
	//退货损失统计
	//private String model;//机种
	//private String customer;//客户
	//private String storeCode;//存货编码
	//private String storeName;//存货名称
	//private Integer amount;//退货数量
	private Double dantiValue;//单体费用
	//private String reason;//退货原因
	
	
	//检测设备折旧
	private String fixedAssetCode;//固定资产编号
	private String fixedAssetName;//固定资产名称
	private String specification;//规格型号
	//private String deptName;//使用部门
	private String storePlace;//存放地点
	//private Double yuanValue;//原值
	
	
	
	//品质故障损失
	private Double unitPrice;//设备单价
	//private Integer amount;//数量
	private Integer lossTime;//损失人力工时
	private Integer machineLoss;//机台损失时间
	private Double machineLossValue;//设备损失
	private Double manLossValue;//人力损失
	private Double capacityLossValue;//产能损失
	
	//品保部薪酬福利
	//private String type;//人员类别
	//private String deptName;//部门名称
	//private Integer amount;//人数
	private Double gongzi;//工资
	private Double shebao;//社保
	private Double gongjijin;//公积金
	private Double canfei;//餐费
	
	
	//部门费用统计
	//private String deptName;//部门名称
	//private String remark;//内容
		
	private String deptName;//部门名称
	private String dutyName;//职务名称
	private Double budgetValue;//预算金额
	private Double value;//发生费用
	private String sourceType = SOURCE_TYPE_COLLECTION;
	private String remark;//备注
	private String feeState;//费用状态
    private String itemGroup;//物料组
	private String customerName;//客户
	private String project;//项目
	private String companyName;//公司
	public Integer getOccurringMonth() {
		return occurringMonth;
	}
	public void setOccurringMonth(Integer occurringMonth) {
		this.occurringMonth = occurringMonth;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getLevelTwoCode() {
		return levelTwoCode;
	}
	public void setLevelTwoCode(String levelTwoCode) {
		this.levelTwoCode = levelTwoCode;
	}
	public String getLevelTwoName() {
		return levelTwoName;
	}
	public void setLevelTwoName(String levelTwoName) {
		this.levelTwoName = levelTwoName;
	}
	public String getLevelThreeCode() {
		return levelThreeCode;
	}
	public void setLevelThreeCode(String levelThreeCode) {
		this.levelThreeCode = levelThreeCode;
	}
	public String getLevelThreeName() {
		return levelThreeName;
	}
	public void setLevelThreeName(String levelThreeName) {
		this.levelThreeName = levelThreeName;
	}
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
	public String getOccurringMonthStr() {
		return occurringMonthStr;
	}
	public void setOccurringMonthStr(String occurringMonthStr) {
		this.occurringMonthStr = occurringMonthStr;
	}
	public Double getBudgetValue() {
		return budgetValue;
	}
	public void setBudgetValue(Double budgetValue) {
		this.budgetValue = budgetValue;
	}
	
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public Integer getCompanyISN() {
		return companyISN;
	}
	public void setCompanyISN(Integer companyISN) {
		this.companyISN = companyISN;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDutyName() {
		return dutyName;
	}
	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getItemGroup() {
		return itemGroup;
	}
	public void setItemGroup(String itemGroup) {
		this.itemGroup = itemGroup;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getFeeState() {
		return feeState;
	}
	public void setFeeState(String feeState) {
		this.feeState = feeState;
	}
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getLevelTwoCodeSap() {
		return levelTwoCodeSap;
	}
	public void setLevelTwoCodeSap(String levelTwoCodeSap) {
		this.levelTwoCodeSap = levelTwoCodeSap;
	}
	public String getLevelThreeCodeSap() {
		return levelThreeCodeSap;
	}
	public void setLevelThreeCodeSap(String levelThreeCodeSap) {
		this.levelThreeCodeSap = levelThreeCodeSap;
	}
	public Date getRefDate() {
		return refDate;
	}
	public void setRefDate(Date refDate) {
		this.refDate = refDate;
	}
	public String getCollectionItemName() {
		return collectionItemName;
	}
	public void setCollectionItemName(String collectionItemName) {
		this.collectionItemName = collectionItemName;
	}
	public String getErpCode() {
		return erpCode;
	}
	public void setErpCode(String erpCode) {
		this.erpCode = erpCode;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStoreCode() {
		return storeCode;
	}
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Integer checkTime) {
		this.checkTime = checkTime;
	}
	public Double getCheckOld() {
		return checkOld;
	}
	public void setCheckOld(Double checkOld) {
		this.checkOld = checkOld;
	}
	public Double getCheckWater() {
		return checkWater;
	}
	public void setCheckWater(Double checkWater) {
		this.checkWater = checkWater;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getDealStyle() {
		return dealStyle;
	}
	public void setDealStyle(String dealStyle) {
		this.dealStyle = dealStyle;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Double getYuanValue() {
		return yuanValue;
	}
	public void setYuanValue(Double yuanValue) {
		this.yuanValue = yuanValue;
	}
	public Double getXianValue() {
		return xianValue;
	}
	public void setXianValue(Double xianValue) {
		this.xianValue = xianValue;
	}
	public String getEvectionMan() {
		return evectionMan;
	}
	public void setEvectionMan(String evectionMan) {
		this.evectionMan = evectionMan;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getWorkMatters() {
		return workMatters;
	}
	public void setWorkMatters(String workMatters) {
		this.workMatters = workMatters;
	}
	public Double getDantiValue() {
		return dantiValue;
	}
	public void setDantiValue(Double dantiValue) {
		this.dantiValue = dantiValue;
	}
	public String getFixedAssetCode() {
		return fixedAssetCode;
	}
	public void setFixedAssetCode(String fixedAssetCode) {
		this.fixedAssetCode = fixedAssetCode;
	}
	public String getFixedAssetName() {
		return fixedAssetName;
	}
	public void setFixedAssetName(String fixedAssetName) {
		this.fixedAssetName = fixedAssetName;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getStorePlace() {
		return storePlace;
	}
	public void setStorePlace(String storePlace) {
		this.storePlace = storePlace;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Integer getLossTime() {
		return lossTime;
	}
	public void setLossTime(Integer lossTime) {
		this.lossTime = lossTime;
	}
	public Integer getMachineLoss() {
		return machineLoss;
	}
	public void setMachineLoss(Integer machineLoss) {
		this.machineLoss = machineLoss;
	}
	public Double getMachineLossValue() {
		return machineLossValue;
	}
	public void setMachineLossValue(Double machineLossValue) {
		this.machineLossValue = machineLossValue;
	}
	public Double getManLossValue() {
		return manLossValue;
	}
	public void setManLossValue(Double manLossValue) {
		this.manLossValue = manLossValue;
	}
	public Double getCapacityLossValue() {
		return capacityLossValue;
	}
	public void setCapacityLossValue(Double capacityLossValue) {
		this.capacityLossValue = capacityLossValue;
	}
	public Double getGongzi() {
		return gongzi;
	}
	public void setGongzi(Double gongzi) {
		this.gongzi = gongzi;
	}
	public Double getShebao() {
		return shebao;
	}
	public void setShebao(Double shebao) {
		this.shebao = shebao;
	}
	public Double getGongjijin() {
		return gongjijin;
	}
	public void setGongjijin(Double gongjijin) {
		this.gongjijin = gongjijin;
	}
	public Double getCanfei() {
		return canfei;
	}
	public void setCanfei(Double canfei) {
		this.canfei = canfei;
	}
	public String getCostType() {
		return costType;
	}
	public void setCostType(String costType) {
		this.costType = costType;
	}
	
	
}

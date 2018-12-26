package com.ambition.ecm.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 类名:ECR实体
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  wuxuming
 * @version 1.00 2016年12月5日 发布
 */
@Entity
@Table(name="ECM_ECR_REPORT")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class EcrReport  extends WorkflowIdEntity{
	private static final long serialVersionUID = 1L;
	private String ecrReportNo;
	@Temporal(TemporalType.TIMESTAMP)
	private Date operationTime;//生效日期
	private String distributeDev;//分发部门
	private String machineNo;//机种编号
	private String coustomerNo;//客户编码
	private String coustomerName;//客户名称
	private String causeType;//原因类型
	private String ecrDatas;//变更数据
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date marketSignChange;//变更时机
	private String marketSignNo;//自定单号
	private String marketSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date marketSignTime;//
	
	private String inventoryMode;//库存处理方式
	private String othersDoc;//其他描述
	private String isNoticeCou;//是否通知客户
	private Integer inventoryNum;//库存数量
	
	private String ecrFile;//附件
	private String isNeedRa;//是否需要RA
	private String isNeedPro;//是否生产
	private String proposer;//申请人
	@Temporal(TemporalType.TIMESTAMP)
	private Date proposerTime;//申请时间
	private String pmMan;//PM部门
	private String pmLoginMan;//PM登录部门
	private String eeMan;//EE部门
	private String eeLoginMan;//EE登录部门
	private String meMan;//ME
	private String meLoginMan;//ME登录部门
	private String projectMan;//工程部门
	private String projectLoginMan;//工程登录部门
	
	private String qualityMan;//品保
	private String qualityLoginMan;
	private String marketMan;//市场部
	private String marketLoginMan;
	private String pmcMan;//PMC部门
	private String pmcLoginMan;//PMC登录部门
	private String produceMan;//生产部
	private String produceLoginMan;//生产部登录名
	private String purchaseMan;// 采购部
	private String purchaseLoginMan;//采购部登录名
	private String chargeMan;//主管
	private String chargeLoginMan;//主管登录名
	private String businessGroup;//事业群
	private String businessGroupLogin;//事业群登录名
	
	/**
	 * 主管确认
	 * */
	private String personLiableText;//责任部门意见
	private String personLiableMan;//责任部门确认人
	@Temporal(TemporalType.TIMESTAMP)
	private Date personLiableTime;//责任部门确认时间
	
	private String meSignText;//ME
	private String meSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date meSignTime;//
	
	private String pmSignText;//PM
	private String pmSignMan;//PM办理人
	@Temporal(TemporalType.TIMESTAMP)
	private Date pmSignTime;//PM时间
	
	private String eeSignText;//EE
	private String eeSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date eeSignTime;//
	
	private String projectSignText;//工程
	private String projectSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date projectSignTime;//
	
	private String qualitySignText;//品保
	private String qualitySignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date qualitySignTime;//
	
	private String pmcSignText;//PMC
	private String pmcSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date pmcSignTime;//

	private String marketText;//市场、业务
	private String marketTMan;//
	@Temporal(TemporalType.TIMESTAMP)
	private Date marketTime;//
	
	/**
	 * 生产
	 * */
	private String produceText;
	private String produceAuditMan;//
	@Temporal(TemporalType.TIMESTAMP)
	private Date produceAuditTime;//
	
	/**
	 * 采购
	 * */
	private String purchaseText;
	private String purchaseAuditMan;//
	@Temporal(TemporalType.TIMESTAMP)
	private Date purchaseAuditTime;//
	
	private String businessGroupText;//事业群负责人
	private String businessGroupMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date businessGroupTime;//
	
	@OneToMany(mappedBy = "ecrReport",cascade={CascadeType.ALL})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	private List<EcrReportDetail> ecrReportDetails;
	
	
	public Date getOperationTime() {
		return operationTime;
	}
	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}
	public String getDistributeDev() {
		return distributeDev;
	}
	public void setDistributeDev(String distributeDev) {
		this.distributeDev = distributeDev;
	}
	public String getMachineNo() {
		return machineNo;
	}
	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}
	public String getCoustomerNo() {
		return coustomerNo;
	}
	public void setCoustomerNo(String coustomerNo) {
		this.coustomerNo = coustomerNo;
	}
	public String getCauseType() {
		return causeType;
	}
	public void setCauseType(String causeType) {
		this.causeType = causeType;
	}
	public String getEcrDatas() {
		return ecrDatas;
	}
	public void setEcrDatas(String ecrDatas) {
		this.ecrDatas = ecrDatas;
	}
	public Date getMarketSignChange() {
		return marketSignChange;
	}
	public void setMarketSignChange(Date marketSignChange) {
		this.marketSignChange = marketSignChange;
	}
	public String getMarketSignNo() {
		return marketSignNo;
	}
	public void setMarketSignNo(String marketSignNo) {
		this.marketSignNo = marketSignNo;
	}
	public String getMarketSignMan() {
		return marketSignMan;
	}
	public void setMarketSignMan(String marketSignMan) {
		this.marketSignMan = marketSignMan;
	}
	public Date getMarketSignTime() {
		return marketSignTime;
	}
	public void setMarketSignTime(Date marketSignTime) {
		this.marketSignTime = marketSignTime;
	}
	public String getInventoryMode() {
		return inventoryMode;
	}
	public void setInventoryMode(String inventoryMode) {
		this.inventoryMode = inventoryMode;
	}
	public String getOthersDoc() {
		return othersDoc;
	}
	public void setOthersDoc(String othersDoc) {
		this.othersDoc = othersDoc;
	}
	public String getIsNoticeCou() {
		return isNoticeCou;
	}
	public void setIsNoticeCou(String isNoticeCou) {
		this.isNoticeCou = isNoticeCou;
	}
	public Integer getInventoryNum() {
		return inventoryNum;
	}
	public void setInventoryNum(Integer inventoryNum) {
		this.inventoryNum = inventoryNum;
	}
	public String getEcrFile() {
		return ecrFile;
	}
	public void setEcrFile(String ecrFile) {
		this.ecrFile = ecrFile;
	}
	public String getIsNeedRa() {
		return isNeedRa;
	}
	public void setIsNeedRa(String isNeedRa) {
		this.isNeedRa = isNeedRa;
	}
	public String getIsNeedPro() {
		return isNeedPro;
	}
	public void setIsNeedPro(String isNeedPro) {
		this.isNeedPro = isNeedPro;
	}
	public String getProposer() {
		return proposer;
	}
	public void setProposer(String proposer) {
		this.proposer = proposer;
	}
	public Date getProposerTime() {
		return proposerTime;
	}
	public void setProposerTime(Date proposerTime) {
		this.proposerTime = proposerTime;
	}
	public String getPersonLiableText() {
		return personLiableText;
	}
	public void setPersonLiableText(String personLiableText) {
		this.personLiableText = personLiableText;
	}
	public String getPersonLiableMan() {
		return personLiableMan;
	}
	public void setPersonLiableMan(String personLiableMan) {
		this.personLiableMan = personLiableMan;
	}
	public Date getPersonLiableTime() {
		return personLiableTime;
	}
	public void setPersonLiableTime(Date personLiableTime) {
		this.personLiableTime = personLiableTime;
	}
	public String getMeSignText() {
		return meSignText;
	}
	public void setMeSignText(String meSignText) {
		this.meSignText = meSignText;
	}
	public String getMeSignMan() {
		return meSignMan;
	}
	public void setMeSignMan(String meSignMan) {
		this.meSignMan = meSignMan;
	}
	public Date getMeSignTime() {
		return meSignTime;
	}
	public void setMeSignTime(Date meSignTime) {
		this.meSignTime = meSignTime;
	}
	public String getPmSignText() {
		return pmSignText;
	}
	public void setPmSignText(String pmSignText) {
		this.pmSignText = pmSignText;
	}
	public String getPmSignMan() {
		return pmSignMan;
	}
	public void setPmSignMan(String pmSignMan) {
		this.pmSignMan = pmSignMan;
	}
	public Date getPmSignTime() {
		return pmSignTime;
	}
	public void setPmSignTime(Date pmSignTime) {
		this.pmSignTime = pmSignTime;
	}
	public String getEeSignText() {
		return eeSignText;
	}
	public void setEeSignText(String eeSignText) {
		this.eeSignText = eeSignText;
	}
	public String getEeSignMan() {
		return eeSignMan;
	}
	public void setEeSignMan(String eeSignMan) {
		this.eeSignMan = eeSignMan;
	}
	public Date getEeSignTime() {
		return eeSignTime;
	}
	public void setEeSignTime(Date eeSignTime) {
		this.eeSignTime = eeSignTime;
	}
	public String getProjectSignText() {
		return projectSignText;
	}
	public void setProjectSignText(String projectSignText) {
		this.projectSignText = projectSignText;
	}
	public String getProjectSignMan() {
		return projectSignMan;
	}
	public void setProjectSignMan(String projectSignMan) {
		this.projectSignMan = projectSignMan;
	}
	public Date getProjectSignTime() {
		return projectSignTime;
	}
	public void setProjectSignTime(Date projectSignTime) {
		this.projectSignTime = projectSignTime;
	}
	public String getQualitySignText() {
		return qualitySignText;
	}
	public void setQualitySignText(String qualitySignText) {
		this.qualitySignText = qualitySignText;
	}
	public String getQualitySignMan() {
		return qualitySignMan;
	}
	public void setQualitySignMan(String qualitySignMan) {
		this.qualitySignMan = qualitySignMan;
	}
	public Date getQualitySignTime() {
		return qualitySignTime;
	}
	public void setQualitySignTime(Date qualitySignTime) {
		this.qualitySignTime = qualitySignTime;
	}
	public String getPmcSignText() {
		return pmcSignText;
	}
	public void setPmcSignText(String pmcSignText) {
		this.pmcSignText = pmcSignText;
	}
	public String getPmcSignMan() {
		return pmcSignMan;
	}
	public void setPmcSignMan(String pmcSignMan) {
		this.pmcSignMan = pmcSignMan;
	}
	public Date getPmcSignTime() {
		return pmcSignTime;
	}
	public void setPmcSignTime(Date pmcSignTime) {
		this.pmcSignTime = pmcSignTime;
	}
	public String getMarketText() {
		return marketText;
	}
	public void setMarketText(String marketText) {
		this.marketText = marketText;
	}
	public String getMarketTMan() {
		return marketTMan;
	}
	public void setMarketTMan(String marketTMan) {
		this.marketTMan = marketTMan;
	}
	public Date getMarketTime() {
		return marketTime;
	}
	public void setMarketTime(Date marketTime) {
		this.marketTime = marketTime;
	}
	public String getProduceText() {
		return produceText;
	}
	public void setProduceText(String produceText) {
		this.produceText = produceText;
	}
	public String getProduceAuditMan() {
		return produceAuditMan;
	}
	public void setProduceAuditMan(String produceAuditMan) {
		this.produceAuditMan = produceAuditMan;
	}
	public Date getProduceAuditTime() {
		return produceAuditTime;
	}
	public void setProduceAuditTime(Date produceAuditTime) {
		this.produceAuditTime = produceAuditTime;
	}
	public String getPurchaseText() {
		return purchaseText;
	}
	public void setPurchaseText(String purchaseText) {
		this.purchaseText = purchaseText;
	}
	public String getPurchaseAuditMan() {
		return purchaseAuditMan;
	}
	public void setPurchaseAuditMan(String purchaseAuditMan) {
		this.purchaseAuditMan = purchaseAuditMan;
	}
	public Date getPurchaseAuditTime() {
		return purchaseAuditTime;
	}
	public void setPurchaseAuditTime(Date purchaseAuditTime) {
		this.purchaseAuditTime = purchaseAuditTime;
	}
	public String getBusinessGroupText() {
		return businessGroupText;
	}
	public void setBusinessGroupText(String businessGroupText) {
		this.businessGroupText = businessGroupText;
	}
	public String getBusinessGroupMan() {
		return businessGroupMan;
	}
	public void setBusinessGroupMan(String businessGroupMan) {
		this.businessGroupMan = businessGroupMan;
	}
	public Date getBusinessGroupTime() {
		return businessGroupTime;
	}
	public void setBusinessGroupTime(Date businessGroupTime) {
		this.businessGroupTime = businessGroupTime;
	}
	public String getEcrReportNo() {
		return ecrReportNo;
	}
	public void setEcrReportNo(String ecrReportNo) {
		this.ecrReportNo = ecrReportNo;
	}
	public String getCoustomerName() {
		return coustomerName;
	}
	public void setCoustomerName(String coustomerName) {
		this.coustomerName = coustomerName;
	}
	public String getPmMan() {
		return pmMan;
	}
	public void setPmMan(String pmMan) {
		this.pmMan = pmMan;
	}
	public String getPmLoginMan() {
		return pmLoginMan;
	}
	public void setPmLoginMan(String pmLoginMan) {
		this.pmLoginMan = pmLoginMan;
	}
	public String getEeMan() {
		return eeMan;
	}
	public void setEeMan(String eeMan) {
		this.eeMan = eeMan;
	}
	public String getEeLoginMan() {
		return eeLoginMan;
	}
	public void setEeLoginMan(String eeLoginMan) {
		this.eeLoginMan = eeLoginMan;
	}
	public String getMeMan() {
		return meMan;
	}
	public void setMeMan(String meMan) {
		this.meMan = meMan;
	}
	public String getMeLoginMan() {
		return meLoginMan;
	}
	public void setMeLoginMan(String meLoginMan) {
		this.meLoginMan = meLoginMan;
	}
	public String getProjectMan() {
		return projectMan;
	}
	public void setProjectMan(String projectMan) {
		this.projectMan = projectMan;
	}
	public String getProjectLoginMan() {
		return projectLoginMan;
	}
	public void setProjectLoginMan(String projectLoginMan) {
		this.projectLoginMan = projectLoginMan;
	}
	public String getQualityMan() {
		return qualityMan;
	}
	public void setQualityMan(String qualityMan) {
		this.qualityMan = qualityMan;
	}
	public String getQualityLoginMan() {
		return qualityLoginMan;
	}
	public void setQualityLoginMan(String qualityLoginMan) {
		this.qualityLoginMan = qualityLoginMan;
	}
	public String getMarketMan() {
		return marketMan;
	}
	public void setMarketMan(String marketMan) {
		this.marketMan = marketMan;
	}
	public String getMarketLoginMan() {
		return marketLoginMan;
	}
	public void setMarketLoginMan(String marketLoginMan) {
		this.marketLoginMan = marketLoginMan;
	}
	public String getPmcMan() {
		return pmcMan;
	}
	public void setPmcMan(String pmcMan) {
		this.pmcMan = pmcMan;
	}
	public String getPmcLoginMan() {
		return pmcLoginMan;
	}
	public void setPmcLoginMan(String pmcLoginMan) {
		this.pmcLoginMan = pmcLoginMan;
	}
	public String getProduceMan() {
		return produceMan;
	}
	public void setProduceMan(String produceMan) {
		this.produceMan = produceMan;
	}
	public String getProduceLoginMan() {
		return produceLoginMan;
	}
	public void setProduceLoginMan(String produceLoginMan) {
		this.produceLoginMan = produceLoginMan;
	}
	public String getPurchaseMan() {
		return purchaseMan;
	}
	public void setPurchaseMan(String purchaseMan) {
		this.purchaseMan = purchaseMan;
	}
	public String getPurchaseLoginMan() {
		return purchaseLoginMan;
	}
	public void setPurchaseLoginMan(String purchaseLoginMan) {
		this.purchaseLoginMan = purchaseLoginMan;
	}
	public List<EcrReportDetail> getEcrReportDetails() {
		return ecrReportDetails;
	}
	public void setEcrReportDetails(List<EcrReportDetail> ecrReportDetails) {
		this.ecrReportDetails = ecrReportDetails;
	}
	public String getChargeMan() {
		return chargeMan;
	}
	public void setChargeMan(String chargeMan) {
		this.chargeMan = chargeMan;
	}
	public String getChargeLoginMan() {
		return chargeLoginMan;
	}
	public void setChargeLoginMan(String chargeLoginMan) {
		this.chargeLoginMan = chargeLoginMan;
	}
	public String getBusinessGroup() {
		return businessGroup;
	}
	public void setBusinessGroup(String businessGroup) {
		this.businessGroup = businessGroup;
	}
	public String getBusinessGroupLogin() {
		return businessGroupLogin;
	}
	public void setBusinessGroupLogin(String businessGroupLogin) {
		this.businessGroupLogin = businessGroupLogin;
	}
	
	

}

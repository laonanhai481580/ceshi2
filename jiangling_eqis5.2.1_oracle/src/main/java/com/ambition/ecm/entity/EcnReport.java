package com.ambition.ecm.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
 * 类名:ECN报告
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  wuxuming
 * @version 1.00 2016-11-7 发布
 */
@Entity
@Table(name="ECM_ECN_REPORT")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class EcnReport  extends WorkflowIdEntity{

	private static final long serialVersionUID = 1L;
	private String ecnNo;//报告编号
	private String distributeDev;//分发部门
	private String dcrnType;//变更类型
	private String nuclearSignPeroid;//核签时效
	private String machineNo;//机种编号
	private String coustomerNo;//客户编码
	private String coustomerName;//客户名称
	@Temporal(TemporalType.TIMESTAMP)
	private Date operationTime;//生效日期
	@Column(length=3000)
	private String causeDiscript;//说明原因
	private String causeType;//原因类型
	private String alterationContent;//变更内容 单选
	private String technologyAlteration;//工艺变更
	private String softAlteration;//软件变更
	private String otherAlteration;//其他变更
	private String isNeedRa;//是否需要RA
	private String raReportNo;//RA报告编号
	private String raLaboratory;//RA实验室
	private String raFileAttach;//RA附件
	private String isNeedXrf;//是否需要XRF
	private String xrfReportNo;//RA报告编号
	private String xrfLaboratory;//RA实验室
	private String xrfFileAttach;//RA附件
	private String isNeedtryReport;//是否试做报告
	private String tryFileAttach;//试做报告附件
	private String isCoustomer;//是否客户凭证
	private String coustomeFile;//客户凭证附件
	private String isThridHarmful;//第三方有害物质
	private String harmfulFileAttach;//有害物质附件
	private String harmfulText;//有害物质附件说明
	private String isNoticeCou;//是否通知客户
	private String noticeText;//通知客户说明
	private String proposeDev;//提出人部门
	private String proposedMan;//提出人
	@Temporal(TemporalType.TIMESTAMP)
	private Date proposeTime;//提出时间
	/**
	 * 部门确认
	 * */
	private String personLiableText;//责任部门意见
	private String personLiableMan;//责任部门确认人
	private String personLiableManLogin;//责任部门确认人
	@Temporal(TemporalType.TIMESTAMP)
	private Date personLiableTime;//责任部门确认时间
	private String willNuclearLoginMan;//核决人登录名
	private String willNuclearMan;//核决人
	
	private String pmMan;//PM部门
	private String pmLoginMan;//PM登录部门
	private String eeMan;//EE部门
	private String eeLoginMan;//EE登录部门
	private String meMan;//ME
	private String meLoginMan;//ME登录部门
	private String materialMan;//材料部门
	private String materialLoginMan;//材料登录部门
	private String projectMan;//工程部门
	private String projectLoginMan;//工程登录部门
	private String pmcMan;//PMC部门
	private String pmcLoginMan;//PMC登录部门
	private String seasoningMan;//调达部门
	private String seasoningLoginMan;//调达登录部门
	private String produceMan;//生产部
	private String produceLoginMan;//生产部登录名
	private String purchaseMan;// 采购部
	private String purchaseLoginMan;//采购部登录名
	private String jigMan;//治具部
	private String jigLoginMan;//
	private String equipmentMan;//设备部
	private String equipmentLoginMan;//
	private String ieMan;//IE课
	private String ieLoginMan;
	private String marketMan;//市场部
	private String marketLoginMan;
	private String qsMan;//QS
	private String qsLoginMan;
	private String warehouseMan;//仓库
	private String warehouseLoginMan;
	private String otherMan;//其他
	private String otherLoginMan;
	private String materialsMan;//资材部
	private String materialsLoginMan;//
	private String qualityMan;//品保
	private String qualityLoginMan;
	private String businessUnit;//事业部
	private String businessUnitLogin;//
	private String businessGroup;//事业群
	private String businessGroupLogin;//
	private String guanwuMan;//关务
	private String guanwuLoginMan;
	private String geshuMan;//个数
	private String geshuLoginMan;
	private String docControlMan;//文控
	private String docControlLoginMan;
	private String jointlySignStrs;//会签办理人 
	
	/**
	 * 会签单位
	 * */
	private String pmSignText;//PM
	private String pmSignMan;//PM办理人
	@Temporal(TemporalType.TIMESTAMP)
	private Date pmSignTime;//PM时间
	
	private String eeSignText;//EE
	private String eeSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date eeSignTime;//
	
	private String meSignText;//ME
	private String meSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date meSignTime;//
	
	private String materialSignText;//材料
	private String materialSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date materialSignTime;//
	
	private String projectSignText;//工程
	private String projectSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date projectSignTime;//
	
	private String pmcSignText;//PMC
	private String pmcSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date pmcSignTime;//
	
	private String seasoningSignText;//调达
	private String seasoningSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date seasoningSignTime;//
	
	private String purchaseSignText;//采购
	private String purchaseSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date purchaseSignTime;//
	
	private String choiceJig;//治具处理
	private String oldJigDoc;//旧治具处理说明
	private String otherJigDoc;//其他治具说明
	private String jigSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date jigSignTime;//
	
	private String choiceEquipment;//设备处理方式
	private String oldEquipmentDoc;//设备处理说明
	private String otherEquipmentDoc;//设备治具说明
	private String equipmenSigntMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date equipmentSignTime;//
	
	private String ieSignText;//设备
	private String ieSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date ieSignTime;//
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date marketSignChange;//变更时机
	private String marketSignNo;//自定单号
	private String marketSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date marketSignTime;//
	
	private String qsSignText;//QS
	private String qsOtherDoc;//其他描述
	private String qsSignFile;//QS附件
	private String qsSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date qsSignTime;//
	
	private String warehouseSignText;//仓库
	private String warehouseSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date warehouseSignTime;//

	private String guanwuSignText;//关务
	private String guanwuSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date guanwuSignTime;//
	
	private String geshuSignText;//个数
	private String geshuSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date geshuSignTime;//
	
	private String otherSignText;//其他
	private String otherSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date otherSignTime;//
	
	private String materialsSignText;//资材
	private String materialsSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date materialsSignTime;//
	
	private String qualitySignText;//品保
	private String qualitySignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date qualitySignTime;//
	
	private String docControlSignText;//文控
	private String docControlSignMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date docControlSignTime;//
	
	private String businessUnitText;//事业部负责人
	private String businessUnitMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date businessUnitTime;//
	
	private String businessGroupText;//事业群负责人
	private String businessGroupMan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date businessGroupTime;//
	/**
	 * 调达课
	 * */
	private String seasoningAuditMan;//
	private String seasoningAuditManLogin;//
	@Temporal(TemporalType.TIMESTAMP)
	private Date seasoningAuditTime;//
	/**
	 * 采购
	 * */
	private String purchaseAuditMan;//
	private String purchaseAuditManLogin;//
	@Temporal(TemporalType.TIMESTAMP)
	private Date purchaseAuditTime;//
	/**
	 * 生产
	 * */
	private String produceAuditMan;//
	private String produceAuditManLogin;//
	@Temporal(TemporalType.TIMESTAMP)
	private Date produceAuditTime;//
	/**
	 * 变更效果追踪
	 * */
	private String trackResult;
	private String trackText;
	private String trackMan;
	private String trackManLogin;
	@Temporal(TemporalType.TIMESTAMP)
	private Date trackTime;
	
	
	@OneToMany(mappedBy = "ecnReport",cascade={CascadeType.ALL})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	private List<EcnReportDetail> ecnReportDetails;
	
	public String getDistributeDev() {
		return distributeDev;
	}
	public void setDistributeDev(String distributeDev) {
		this.distributeDev = distributeDev;
	}
	public String getDcrnType() {
		return dcrnType;
	}
	public void setDcrnType(String dcrnType) {
		this.dcrnType = dcrnType;
	}
	public String getNuclearSignPeroid() {
		return nuclearSignPeroid;
	}
	public void setNuclearSignPeroid(String nuclearSignPeroid) {
		this.nuclearSignPeroid = nuclearSignPeroid;
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
	public String getCoustomerName() {
		return coustomerName;
	}
	public void setCoustomerName(String coustomerName) {
		this.coustomerName = coustomerName;
	}
	public Date getOperationTime() {
		return operationTime;
	}
	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}
	public String getCauseDiscript() {
		return causeDiscript;
	}
	public void setCauseDiscript(String causeDiscript) {
		this.causeDiscript = causeDiscript;
	}
	public String getCauseType() {
		return causeType;
	}
	public void setCauseType(String causeType) {
		this.causeType = causeType;
	}
	public String getAlterationContent() {
		return alterationContent;
	}
	public void setAlterationContent(String alterationContent) {
		this.alterationContent = alterationContent;
	}
	public String getTechnologyAlteration() {
		return technologyAlteration;
	}
	public void setTechnologyAlteration(String technologyAlteration) {
		this.technologyAlteration = technologyAlteration;
	}
	public String getSoftAlteration() {
		return softAlteration;
	}
	public void setSoftAlteration(String softAlteration) {
		this.softAlteration = softAlteration;
	}
	public String getOtherAlteration() {
		return otherAlteration;
	}
	public void setOtherAlteration(String otherAlteration) {
		this.otherAlteration = otherAlteration;
	}
	public String getIsNeedRa() {
		return isNeedRa;
	}
	public void setIsNeedRa(String isNeedRa) {
		this.isNeedRa = isNeedRa;
	}
	public String getRaReportNo() {
		return raReportNo;
	}
	public void setRaReportNo(String raReportNo) {
		this.raReportNo = raReportNo;
	}
	public String getRaLaboratory() {
		return raLaboratory;
	}
	public void setRaLaboratory(String raLaboratory) {
		this.raLaboratory = raLaboratory;
	}
	public String getRaFileAttach() {
		return raFileAttach;
	}
	public void setRaFileAttach(String raFileAttach) {
		this.raFileAttach = raFileAttach;
	}
	public String getIsNeedXrf() {
		return isNeedXrf;
	}
	public void setIsNeedXrf(String isNeedXrf) {
		this.isNeedXrf = isNeedXrf;
	}
	public String getXrfReportNo() {
		return xrfReportNo;
	}
	public void setXrfReportNo(String xrfReportNo) {
		this.xrfReportNo = xrfReportNo;
	}
	public String getXrfLaboratory() {
		return xrfLaboratory;
	}
	public void setXrfLaboratory(String xrfLaboratory) {
		this.xrfLaboratory = xrfLaboratory;
	}
	public String getXrfFileAttach() {
		return xrfFileAttach;
	}
	public void setXrfFileAttach(String xrfFileAttach) {
		this.xrfFileAttach = xrfFileAttach;
	}
	public String getIsNeedtryReport() {
		return isNeedtryReport;
	}
	public void setIsNeedtryReport(String isNeedtryReport) {
		this.isNeedtryReport = isNeedtryReport;
	}
	public String getTryFileAttach() {
		return tryFileAttach;
	}
	public void setTryFileAttach(String tryFileAttach) {
		this.tryFileAttach = tryFileAttach;
	}
	public String getIsCoustomer() {
		return isCoustomer;
	}
	public void setIsCoustomer(String isCoustomer) {
		this.isCoustomer = isCoustomer;
	}
	public String getCoustomeFile() {
		return coustomeFile;
	}
	public void setCoustomeFile(String coustomeFile) {
		this.coustomeFile = coustomeFile;
	}
	public String getIsThridHarmful() {
		return isThridHarmful;
	}
	public void setIsThridHarmful(String isThridHarmful) {
		this.isThridHarmful = isThridHarmful;
	}
	public String getHarmfulFileAttach() {
		return harmfulFileAttach;
	}
	public void setHarmfulFileAttach(String harmfulFileAttach) {
		this.harmfulFileAttach = harmfulFileAttach;
	}
	public String getHarmfulText() {
		return harmfulText;
	}
	public void setHarmfulText(String harmfulText) {
		this.harmfulText = harmfulText;
	}
	public String getIsNoticeCou() {
		return isNoticeCou;
	}
	public void setIsNoticeCou(String isNoticeCou) {
		this.isNoticeCou = isNoticeCou;
	}
	public String getNoticeText() {
		return noticeText;
	}
	public void setNoticeText(String noticeText) {
		this.noticeText = noticeText;
	}
	public String getProposeDev() {
		return proposeDev;
	}
	public void setProposeDev(String proposeDev) {
		this.proposeDev = proposeDev;
	}
	public String getProposedMan() {
		return proposedMan;
	}
	public void setProposedMan(String proposedMan) {
		this.proposedMan = proposedMan;
	}
	public Date getProposeTime() {
		return proposeTime;
	}
	public void setProposeTime(Date proposeTime) {
		this.proposeTime = proposeTime;
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
	public String getWillNuclearLoginMan() {
		return willNuclearLoginMan;
	}
	public void setWillNuclearLoginMan(String willNuclearLoginMan) {
		this.willNuclearLoginMan = willNuclearLoginMan;
	}
	public String getWillNuclearMan() {
		return willNuclearMan;
	}
	public void setWillNuclearMan(String willNuclearMan) {
		this.willNuclearMan = willNuclearMan;
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
	public String getMaterialMan() {
		return materialMan;
	}
	public void setMaterialMan(String materialMan) {
		this.materialMan = materialMan;
	}
	public String getMaterialLoginMan() {
		return materialLoginMan;
	}
	public void setMaterialLoginMan(String materialLoginMan) {
		this.materialLoginMan = materialLoginMan;
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
	public String getSeasoningMan() {
		return seasoningMan;
	}
	public void setSeasoningMan(String seasoningMan) {
		this.seasoningMan = seasoningMan;
	}
	public String getSeasoningLoginMan() {
		return seasoningLoginMan;
	}
	public void setSeasoningLoginMan(String seasoningLoginMan) {
		this.seasoningLoginMan = seasoningLoginMan;
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
	public String getJigMan() {
		return jigMan;
	}
	public void setJigMan(String jigMan) {
		this.jigMan = jigMan;
	}
	public String getJigLoginMan() {
		return jigLoginMan;
	}
	public void setJigLoginMan(String jigLoginMan) {
		this.jigLoginMan = jigLoginMan;
	}
	public String getEquipmentMan() {
		return equipmentMan;
	}
	public void setEquipmentMan(String equipmentMan) {
		this.equipmentMan = equipmentMan;
	}
	public String getEquipmentLoginMan() {
		return equipmentLoginMan;
	}
	public void setEquipmentLoginMan(String equipmentLoginMan) {
		this.equipmentLoginMan = equipmentLoginMan;
	}
	public String getIeMan() {
		return ieMan;
	}
	public void setIeMan(String ieMan) {
		this.ieMan = ieMan;
	}
	public String getIeLoginMan() {
		return ieLoginMan;
	}
	public void setIeLoginMan(String ieLoginMan) {
		this.ieLoginMan = ieLoginMan;
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
	public String getQsMan() {
		return qsMan;
	}
	public void setQsMan(String qsMan) {
		this.qsMan = qsMan;
	}
	public String getQsLoginMan() {
		return qsLoginMan;
	}
	public void setQsLoginMan(String qsLoginMan) {
		this.qsLoginMan = qsLoginMan;
	}
	public String getWarehouseMan() {
		return warehouseMan;
	}
	public void setWarehouseMan(String warehouseMan) {
		this.warehouseMan = warehouseMan;
	}
	public String getWarehouseLoginMan() {
		return warehouseLoginMan;
	}
	public void setWarehouseLoginMan(String warehouseLoginMan) {
		this.warehouseLoginMan = warehouseLoginMan;
	}
	public String getOtherMan() {
		return otherMan;
	}
	public void setOtherMan(String otherMan) {
		this.otherMan = otherMan;
	}
	public String getOtherLoginMan() {
		return otherLoginMan;
	}
	public void setOtherLoginMan(String otherLoginMan) {
		this.otherLoginMan = otherLoginMan;
	}
	public String getMaterialsMan() {
		return materialsMan;
	}
	public void setMaterialsMan(String materialsMan) {
		this.materialsMan = materialsMan;
	}
	public String getMaterialsLoginMan() {
		return materialsLoginMan;
	}
	public void setMaterialsLoginMan(String materialsLoginMan) {
		this.materialsLoginMan = materialsLoginMan;
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
	public String getDocControlMan() {
		return docControlMan;
	}
	public void setDocControlMan(String docControlMan) {
		this.docControlMan = docControlMan;
	}
	public String getDocControlLoginMan() {
		return docControlLoginMan;
	}
	public void setDocControlLoginMan(String docControlLoginMan) {
		this.docControlLoginMan = docControlLoginMan;
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
	public String getMaterialSignText() {
		return materialSignText;
	}
	public void setMaterialSignText(String materialSignText) {
		this.materialSignText = materialSignText;
	}
	public String getMaterialSignMan() {
		return materialSignMan;
	}
	public void setMaterialSignMan(String materialSignMan) {
		this.materialSignMan = materialSignMan;
	}
	public Date getMaterialSignTime() {
		return materialSignTime;
	}
	public void setMaterialSignTime(Date materialSignTime) {
		this.materialSignTime = materialSignTime;
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
	public String getSeasoningSignText() {
		return seasoningSignText;
	}
	public void setSeasoningSignText(String seasoningSignText) {
		this.seasoningSignText = seasoningSignText;
	}
	public String getSeasoningSignMan() {
		return seasoningSignMan;
	}
	public void setSeasoningSignMan(String seasoningSignMan) {
		this.seasoningSignMan = seasoningSignMan;
	}
	public Date getSeasoningSignTime() {
		return seasoningSignTime;
	}
	public void setSeasoningSignTime(Date seasoningSignTime) {
		this.seasoningSignTime = seasoningSignTime;
	}
	public String getPurchaseSignText() {
		return purchaseSignText;
	}
	public void setPurchaseSignText(String purchaseSignText) {
		this.purchaseSignText = purchaseSignText;
	}
	public String getPurchaseSignMan() {
		return purchaseSignMan;
	}
	public void setPurchaseSignMan(String purchaseSignMan) {
		this.purchaseSignMan = purchaseSignMan;
	}
	public Date getPurchaseSignTime() {
		return purchaseSignTime;
	}
	public void setPurchaseSignTime(Date purchaseSignTime) {
		this.purchaseSignTime = purchaseSignTime;
	}
	public String getChoiceJig() {
		return choiceJig;
	}
	public void setChoiceJig(String choiceJig) {
		this.choiceJig = choiceJig;
	}
	public String getOldJigDoc() {
		return oldJigDoc;
	}
	public void setOldJigDoc(String oldJigDoc) {
		this.oldJigDoc = oldJigDoc;
	}
	public String getOtherJigDoc() {
		return otherJigDoc;
	}
	public void setOtherJigDoc(String otherJigDoc) {
		this.otherJigDoc = otherJigDoc;
	}
	public String getJigSignMan() {
		return jigSignMan;
	}
	public void setJigSignMan(String jigSignMan) {
		this.jigSignMan = jigSignMan;
	}
	public Date getJigSignTime() {
		return jigSignTime;
	}
	public void setJigSignTime(Date jigSignTime) {
		this.jigSignTime = jigSignTime;
	}
	public String getChoiceEquipment() {
		return choiceEquipment;
	}
	public void setChoiceEquipment(String choiceEquipment) {
		this.choiceEquipment = choiceEquipment;
	}
	public String getOldEquipmentDoc() {
		return oldEquipmentDoc;
	}
	public void setOldEquipmentDoc(String oldEquipmentDoc) {
		this.oldEquipmentDoc = oldEquipmentDoc;
	}
	public String getOtherEquipmentDoc() {
		return otherEquipmentDoc;
	}
	public void setOtherEquipmentDoc(String otherEquipmentDoc) {
		this.otherEquipmentDoc = otherEquipmentDoc;
	}
	public String getEquipmenSigntMan() {
		return equipmenSigntMan;
	}
	public void setEquipmenSigntMan(String equipmenSigntMan) {
		this.equipmenSigntMan = equipmenSigntMan;
	}
	public Date getEquipmentSignTime() {
		return equipmentSignTime;
	}
	public void setEquipmentSignTime(Date equipmentSignTime) {
		this.equipmentSignTime = equipmentSignTime;
	}
	public String getIeSignText() {
		return ieSignText;
	}
	public void setIeSignText(String ieSignText) {
		this.ieSignText = ieSignText;
	}
	public String getIeSignMan() {
		return ieSignMan;
	}
	public void setIeSignMan(String ieSignMan) {
		this.ieSignMan = ieSignMan;
	}
	public Date getIeSignTime() {
		return ieSignTime;
	}
	public void setIeSignTime(Date ieSignTime) {
		this.ieSignTime = ieSignTime;
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
	public String getQsSignText() {
		return qsSignText;
	}
	public void setQsSignText(String qsSignText) {
		this.qsSignText = qsSignText;
	}
	public String getQsOtherDoc() {
		return qsOtherDoc;
	}
	public void setQsOtherDoc(String qsOtherDoc) {
		this.qsOtherDoc = qsOtherDoc;
	}
	public String getQsSignFile() {
		return qsSignFile;
	}
	public void setQsSignFile(String qsSignFile) {
		this.qsSignFile = qsSignFile;
	}
	public String getQsSignMan() {
		return qsSignMan;
	}
	public void setQsSignMan(String qsSignMan) {
		this.qsSignMan = qsSignMan;
	}
	public Date getQsSignTime() {
		return qsSignTime;
	}
	public void setQsSignTime(Date qsSignTime) {
		this.qsSignTime = qsSignTime;
	}
	public String getWarehouseSignText() {
		return warehouseSignText;
	}
	public void setWarehouseSignText(String warehouseSignText) {
		this.warehouseSignText = warehouseSignText;
	}
	public String getWarehouseSignMan() {
		return warehouseSignMan;
	}
	public void setWarehouseSignMan(String warehouseSignMan) {
		this.warehouseSignMan = warehouseSignMan;
	}
	public Date getWarehouseSignTime() {
		return warehouseSignTime;
	}
	public void setWarehouseSignTime(Date warehouseSignTime) {
		this.warehouseSignTime = warehouseSignTime;
	}
	public String getOtherSignText() {
		return otherSignText;
	}
	public void setOtherSignText(String otherSignText) {
		this.otherSignText = otherSignText;
	}
	public String getOtherSignMan() {
		return otherSignMan;
	}
	public void setOtherSignMan(String otherSignMan) {
		this.otherSignMan = otherSignMan;
	}
	public Date getOtherSignTime() {
		return otherSignTime;
	}
	public void setOtherSignTime(Date otherSignTime) {
		this.otherSignTime = otherSignTime;
	}
	public String getMaterialsSignText() {
		return materialsSignText;
	}
	public void setMaterialsSignText(String materialsSignText) {
		this.materialsSignText = materialsSignText;
	}
	public String getMaterialsSignMan() {
		return materialsSignMan;
	}
	public void setMaterialsSignMan(String materialsSignMan) {
		this.materialsSignMan = materialsSignMan;
	}
	public Date getMaterialsSignTime() {
		return materialsSignTime;
	}
	public void setMaterialsSignTime(Date materialsSignTime) {
		this.materialsSignTime = materialsSignTime;
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
	public String getDocControlSignText() {
		return docControlSignText;
	}
	public void setDocControlSignText(String docControlSignText) {
		this.docControlSignText = docControlSignText;
	}
	public String getDocControlSignMan() {
		return docControlSignMan;
	}
	public void setDocControlSignMan(String docControlSignMan) {
		this.docControlSignMan = docControlSignMan;
	}
	public Date getDocControlSignTime() {
		return docControlSignTime;
	}
	public void setDocControlSignTime(Date docControlSignTime) {
		this.docControlSignTime = docControlSignTime;
	}
	public String getSeasoningAuditMan() {
		return seasoningAuditMan;
	}
	public void setSeasoningAuditMan(String seasoningAuditMan) {
		this.seasoningAuditMan = seasoningAuditMan;
	}
	public Date getSeasoningAuditTime() {
		return seasoningAuditTime;
	}
	public void setSeasoningAuditTime(Date seasoningAuditTime) {
		this.seasoningAuditTime = seasoningAuditTime;
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
	public String getTrackResult() {
		return trackResult;
	}
	public void setTrackResult(String trackResult) {
		this.trackResult = trackResult;
	}
	public String getTrackText() {
		return trackText;
	}
	public void setTrackText(String trackText) {
		this.trackText = trackText;
	}
	public String getTrackMan() {
		return trackMan;
	}
	public void setTrackMan(String trackMan) {
		this.trackMan = trackMan;
	}
	public Date getTrackTime() {
		return trackTime;
	}
	public void setTrackTime(Date trackTime) {
		this.trackTime = trackTime;
	}
	
	public String getEcnNo() {
		return ecnNo;
	}
	public void setEcnNo(String ecnNo) {
		this.ecnNo = ecnNo;
	}
	public String getJointlySignStrs() {
		return jointlySignStrs;
	}
	public void setJointlySignStrs(String jointlySignStrs) {
		this.jointlySignStrs = jointlySignStrs;
	}
	public List<EcnReportDetail> getEcnReportDetails() {
		return ecnReportDetails;
	}
	public void setEcnReportDetails(List<EcnReportDetail> ecnReportDetails) {
		this.ecnReportDetails = ecnReportDetails;
	}
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	public String getBusinessUnitMan() {
		return businessUnitMan;
	}
	public void setBusinessUnitMan(String businessUnitMan) {
		this.businessUnitMan = businessUnitMan;
	}
	public Date getBusinessUnitTime() {
		return businessUnitTime;
	}
	public void setBusinessUnitTime(Date businessUnitTime) {
		this.businessUnitTime = businessUnitTime;
	}
	public String getBusinessGroup() {
		return businessGroup;
	}
	public void setBusinessGroup(String businessGroup) {
		this.businessGroup = businessGroup;
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
	public String getBusinessUnitText() {
		return businessUnitText;
	}
	public void setBusinessUnitText(String businessUnitText) {
		this.businessUnitText = businessUnitText;
	}
	public String getBusinessGroupText() {
		return businessGroupText;
	}
	public void setBusinessGroupText(String businessGroupText) {
		this.businessGroupText = businessGroupText;
	}
	public String getBusinessUnitLogin() {
		return businessUnitLogin;
	}
	public void setBusinessUnitLogin(String businessUnitLogin) {
		this.businessUnitLogin = businessUnitLogin;
	}
	public String getBusinessGroupLogin() {
		return businessGroupLogin;
	}
	public void setBusinessGroupLogin(String businessGroupLogin) {
		this.businessGroupLogin = businessGroupLogin;
	}
	public String getSeasoningAuditManLogin() {
		return seasoningAuditManLogin;
	}
	public void setSeasoningAuditManLogin(String seasoningAuditManLogin) {
		this.seasoningAuditManLogin = seasoningAuditManLogin;
	}
	public String getPurchaseAuditManLogin() {
		return purchaseAuditManLogin;
	}
	public void setPurchaseAuditManLogin(String purchaseAuditManLogin) {
		this.purchaseAuditManLogin = purchaseAuditManLogin;
	}
	public String getProduceAuditManLogin() {
		return produceAuditManLogin;
	}
	public void setProduceAuditManLogin(String produceAuditManLogin) {
		this.produceAuditManLogin = produceAuditManLogin;
	}
	public String getTrackManLogin() {
		return trackManLogin;
	}
	public void setTrackManLogin(String trackManLogin) {
		this.trackManLogin = trackManLogin;
	}
	public String getPersonLiableManLogin() {
		return personLiableManLogin;
	}
	public void setPersonLiableManLogin(String personLiableManLogin) {
		this.personLiableManLogin = personLiableManLogin;
	}
	public String getGuanwuMan() {
		return guanwuMan;
	}
	public void setGuanwuMan(String guanwuMan) {
		this.guanwuMan = guanwuMan;
	}
	public String getGuanwuLoginMan() {
		return guanwuLoginMan;
	}
	public void setGuanwuLoginMan(String guanwuLoginMan) {
		this.guanwuLoginMan = guanwuLoginMan;
	}
	public String getGeshuMan() {
		return geshuMan;
	}
	public void setGeshuMan(String geshuMan) {
		this.geshuMan = geshuMan;
	}
	public String getGeshuLoginMan() {
		return geshuLoginMan;
	}
	public void setGeshuLoginMan(String geshuLoginMan) {
		this.geshuLoginMan = geshuLoginMan;
	}
	public String getGuanwuSignText() {
		return guanwuSignText;
	}
	public void setGuanwuSignText(String guanwuSignText) {
		this.guanwuSignText = guanwuSignText;
	}
	public String getGuanwuSignMan() {
		return guanwuSignMan;
	}
	public void setGuanwuSignMan(String guanwuSignMan) {
		this.guanwuSignMan = guanwuSignMan;
	}
	public Date getGuanwuSignTime() {
		return guanwuSignTime;
	}
	public void setGuanwuSignTime(Date guanwuSignTime) {
		this.guanwuSignTime = guanwuSignTime;
	}
	public String getGeshuSignText() {
		return geshuSignText;
	}
	public void setGeshuSignText(String geshuSignText) {
		this.geshuSignText = geshuSignText;
	}
	public String getGeshuSignMan() {
		return geshuSignMan;
	}
	public void setGeshuSignMan(String geshuSignMan) {
		this.geshuSignMan = geshuSignMan;
	}
	public Date getGeshuSignTime() {
		return geshuSignTime;
	}
	public void setGeshuSignTime(Date geshuSignTime) {
		this.geshuSignTime = geshuSignTime;
	}
	
	
}

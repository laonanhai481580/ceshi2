package com.ambition.spc.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;

/**    
 * AbnormalInfo.java
 * @authorBy YUKE
 * 异常消息
 */
@Entity
@Table(name = "SPC_ABNORMAL_INFO")
public class AbnormalInfo extends IdEntity {

	private static final long serialVersionUID = 1L;
	private String name;//名称
	private String num;//子组号
	private String equipmentNo;//机台
	private String dsc;//描述
	private Date occurDate;//发生时间
	private Long byParaID;//参数ID
	private String byParaName;//参数名称
	private String byParaNum;//参数编号
	private Long byProdID;//产品ID
	private String byProdName;//产品名称
	private String byProdNum;//产品编号
	private String byProdType;//产品型号
	private Long byRuleID;//规则ID
	private String byRuleName;//规则名称
	private String byRuleNum;//规则编号
//	@SuppressWarnings("rawtypes")
//	private Set abnorDealwithInitSet;//子 （一对一） 规则处理
	private String byBatch;//批次
	private String byPosition;//发生位置
	private Integer frequency;//频次
	private String currDealMan;//当前处理人
	private String priState;//初步处理标记字段 :1为已处理 , 2为未处理 
	private int newAttr1;//备用
	private String priStateName;//初始状态中文名称 不生成数据库
	private String type;//0 为报警，1为预报警 ,2为dpk预报警，3为dpk报警

	//烟草行业加入的字段，如果其他行业有相应的信息可以使用这些字段
	private Long batchID;//批次ID
	private Long classID;//类别ID
	private String classN;//类别
	private Long workShopID;
	private String workShop;
	private Long workLineID;
	private String workLine;
	private Long workPartID;
	private String workPart;//工段
	private Long workSectionID;
	private String workSection;
	//异常处理状态，不保存到数据库  0：表示未过期，1：表示已过期
	private String handState = "1";
	private String occurTime;//异常发生时间，时分秒
	private String spell;//班组
	private String rank;//班次
    private String ifAlarm;//是否继续报警
    
    private String ifDelete;//是否删除   0：表示已删除   空：表示未删除
    private String ifConvert;//是否转化    0：表示已经转化   空：表示未转化
    
    @ManyToOne
	@JoinColumn(name="FK_QUALITY_FEATURE_ID")
	private QualityFeature qualityFeature;//质量特性
    
    @ManyToOne
   	@JoinColumn(name="FK_SPC_SUB_GROUP_ID")
   	private SpcSubGroup spcSubGroup;//子组表
    
	@OneToMany(mappedBy="abnormalInfo",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<ReasonMeasure> reasonMeasures;//原因措施
    
//	@SuppressWarnings("rawtypes")
//	public Set getAbnorDealwithInitSet() {
//		return abnorDealwithInitSet;
//	}
	
//	@SuppressWarnings("rawtypes")
//	public void setAbnorDealwithInitSet(Set abnorDealwithInitSet) {
//		this.abnorDealwithInitSet = abnorDealwithInitSet;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getDsc() {
		return dsc;
	}

	public void setDsc(String dsc) {
		this.dsc = dsc;
	}

	public Date getOccurDate() {
		return occurDate;
	}

	public void setOccurDate(Date occurDate) {
		this.occurDate = occurDate;
	}

	public Long getByParaID() {
		return byParaID;
	}

	public void setByParaID(Long byParaID) {
		this.byParaID = byParaID;
	}

	public String getByParaName() {
		return byParaName;
	}

	public void setByParaName(String byParaName) {
		this.byParaName = byParaName;
	}

	public String getByParaNum() {
		return byParaNum;
	}

	public void setByParaNum(String byParaNum) {
		this.byParaNum = byParaNum;
	}

	public Long getByProdID() {
		return byProdID;
	}

	public void setByProdID(Long byProdID) {
		this.byProdID = byProdID;
	}

	public String getByProdName() {
		return byProdName;
	}

	public void setByProdName(String byProdName) {
		this.byProdName = byProdName;
	}

	public String getByProdNum() {
		return byProdNum;
	}

	public void setByProdNum(String byProdNum) {
		this.byProdNum = byProdNum;
	}

	public Long getByRuleID() {
		return byRuleID;
	}

	public void setByRuleID(Long byRuleID) {
		this.byRuleID = byRuleID;
	}

	public String getByRuleName() {
		return byRuleName;
	}

	public void setByRuleName(String byRuleName) {
		this.byRuleName = byRuleName;
	}

	public String getByRuleNum() {
		return byRuleNum;
	}

	public void setByRuleNum(String byRuleNum) {
		this.byRuleNum = byRuleNum;
	}

	public String getByBatch() {
		return byBatch;
	}

	public void setByBatch(String byBatch) {
		this.byBatch = byBatch;
	}

	public String getByPosition() {
		return byPosition;
	}

	public void setByPosition(String byPosition) {
		this.byPosition = byPosition;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public String getByProdType() {
		return byProdType;
	}

	public void setByProdType(String byProdType) {
		this.byProdType = byProdType;
	}

	public String getCurrDealMan() {
		return currDealMan;
	}

	public void setCurrDealMan(String currDealMan) {
		this.currDealMan = currDealMan;
	}

	public String getPriState() {
		return priState;
	}

	public void setPriState(String priState) {
		this.priState = priState;
	}

	public int getNewAttr1() {
		return newAttr1;
	}

	public void setNewAttr1(int newAttr1) {
		this.newAttr1 = newAttr1;
	}

	public String getPriStateName() {
		return priStateName;
	}

	public void setPriStateName(String priStateName) {
		this.priStateName = priStateName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getBatchID() {
		return batchID;
	}

	public void setBatchID(Long batchID) {
		this.batchID = batchID;
	}

	public Long getClassID() {
		return classID;
	}

	public void setClassID(Long classID) {
		this.classID = classID;
	}

	public String getClassN() {
		return classN;
	}

	public void setClassN(String classN) {
		this.classN = classN;
	}

	public Long getWorkShopID() {
		return workShopID;
	}

	public void setWorkShopID(Long workShopID) {
		this.workShopID = workShopID;
	}

	public String getWorkShop() {
		return workShop;
	}

	public void setWorkShop(String workShop) {
		this.workShop = workShop;
	}

	public Long getWorkLineID() {
		return workLineID;
	}

	public void setWorkLineID(Long workLineID) {
		this.workLineID = workLineID;
	}

	public String getWorkLine() {
		return workLine;
	}

	public void setWorkLine(String workLine) {
		this.workLine = workLine;
	}

	public Long getWorkPartID() {
		return workPartID;
	}

	public void setWorkPartID(Long workPartID) {
		this.workPartID = workPartID;
	}

	public String getWorkPart() {
		return workPart;
	}

	public void setWorkPart(String workPart) {
		this.workPart = workPart;
	}

	public Long getWorkSectionID() {
		return workSectionID;
	}

	public void setWorkSectionID(Long workSectionID) {
		this.workSectionID = workSectionID;
	}

	public String getWorkSection() {
		return workSection;
	}

	public void setWorkSection(String workSection) {
		this.workSection = workSection;
	}

	public String getHandState() {
		return handState;
	}

	public void setHandState(String handState) {
		this.handState = handState;
	}

	public String getOccurTime() {
		return occurTime;
	}

	public void setOccurTime(String occurTime) {
		this.occurTime = occurTime;
	}

	public String getSpell() {
		return spell;
	}

	public void setSpell(String spell) {
		this.spell = spell;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getIfAlarm() {
		return ifAlarm;
	}

	public void setIfAlarm(String ifAlarm) {
		this.ifAlarm = ifAlarm;
	}

	public String getIfDelete() {
		return ifDelete;
	}

	public void setIfDelete(String ifDelete) {
		this.ifDelete = ifDelete;
	}

	public String getIfConvert() {
		return ifConvert;
	}

	public void setIfConvert(String ifConvert) {
		this.ifConvert = ifConvert;
	}

	public QualityFeature getQualityFeature() {
		return qualityFeature;
	}

	public void setQualityFeature(QualityFeature qualityFeature) {
		this.qualityFeature = qualityFeature;
	}

	public SpcSubGroup getSpcSubGroup() {
		return spcSubGroup;
	}

	public void setSpcSubGroup(SpcSubGroup spcSubGroup) {
		this.spcSubGroup = spcSubGroup;
	}

	public List<ReasonMeasure> getReasonMeasures() {
		return reasonMeasures;
	}

	public void setReasonMeasures(List<ReasonMeasure> reasonMeasures) {
		this.reasonMeasures = reasonMeasures;
	}

	public String getEquipmentNo() {
		return equipmentNo;
	}

	public void setEquipmentNo(String equipmentNo) {
		this.equipmentNo = equipmentNo;
	}
	
	
}

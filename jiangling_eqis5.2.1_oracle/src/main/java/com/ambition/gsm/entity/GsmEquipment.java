package com.ambition.gsm.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 量检具管理(ENTITY)
 * @author 张顺治
 *
 */
@Entity
@Table(name = "GSM_EQUIPMENT")
public class GsmEquipment extends IdEntity {
	private static final long serialVersionUID = 1L;
	/************* 量检具状态 ***************/
	public static final String STATE_DEFAULT_INSTOCK="在库";
	public static final String STATE_INTRANSIT="在途";
	public static final String STATE_SEALUP="封存";
	public static final String STATE_SCRAP="报废";
	public static final String STATE_INUSE="在用";
	public static final String STATE_REPAIR="维修";
	public static final String STATE_CHECK="校验";
	public static final String STATE_MSA="MSA";
	public static final String STATE_BORROW="借出";
	public static final String STATE_BLOCKUP="停用";
	
	//转移状态
	public static final String STATE_TRANSFER_WAIT="可转移";
	public static final String STATE_TRANSFER_CONFIRM="待确认";
	private String companyMain;//公司主体
	private String processSection;//事业部
	private String devName;//使用部门
	private String workProducre;//工序
	private String address;//安装地址
	private String dutyMan;//责任人
	private String dutyLoginMan;//责任人登录名
	private String goalDutyMan;//目标责任人
	private String goalDutyLoginMan;//目标责任人登录名
	@Temporal(TemporalType.TIMESTAMP)
	private Date transferTime;//转移时间
	private String transferRemark;//转移备注
	private String auditTransferMan;//转移确认人
	@Temporal(TemporalType.TIMESTAMP)
	private Date auditTransferTime;//确认转移时间
	private String transferState=STATE_TRANSFER_WAIT;//转移状态
	private String copyMan;//抄送人
	private String copyLoginMan;//抄送登录名
	private String fixedAssets;//固定资产编号
	private String managerAssets;//管理编号
	private String equipmentName;//设备名称
	private String equipmentModel;//设备型号
	private String measuringRange;//测量范围
	private String accuracy;//精度、分度
	private String manufacturer;//生产厂家    
	private String factoryNumber;//机身号
	private String checkMethod;//校检方式
	private String equipmentLevel;//分级
	private String frequency;//频率
	private Date purchaseTime;//采购日期
	private String measurementState;//量具状态 
	@Temporal(TemporalType.TIMESTAMP)
	private Date proofTime;//此次校验日期
	@Temporal(TemporalType.TIMESTAMP)
	private Date nextProofTime;//下次校验日期
	private Integer warnDates;//提前预警天数
	private Integer isWarm;//是否预警
	private String  stocktakingMan;//盘点人
	@Temporal(TemporalType.TIMESTAMP)
	private Date  stocktakingTime;//盘点时间
	private String remark;//盘点备注
	private String calibration;//校验报告
	private String gsmState;//隐藏状态
	private String contact;//联系方式
	private String remark2;//停用备注
	private String director;//主管
	private String directorLogin;//主管登入名
	private String remark3;//备用1
	private String remark4;//备用2
	private String remark5;//备用3
	//校验计划
	@JsonIgnore
	@OneToMany(mappedBy="gsmEquipment",cascade=javax.persistence.CascadeType.ALL)
    @Cascade(value=CascadeType.DELETE_ORPHAN)
	private List<InspectionPlan> inspectionPlans;
	//MSA计划
	@JsonIgnore
	@OneToMany(mappedBy="gsmEquipment",cascade=javax.persistence.CascadeType.ALL)
    @Cascade(value=CascadeType.DELETE_ORPHAN)
	private List<InspectionMsaplan> inspectionMsaplans;	
	public String getCompanyMain() {
		return companyMain;
	}
	public void setCompanyMain(String companyMain) {
		this.companyMain = companyMain;
	}
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getWorkProducre() {
		return workProducre;
	}
	public void setWorkProducre(String workProducre) {
		this.workProducre = workProducre;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDutyMan() {
		return dutyMan;
	}
	public void setDutyMan(String dutyMan) {
		this.dutyMan = dutyMan;
	}
	public String getDutyLoginMan() {
		return dutyLoginMan;
	}
	public void setDutyLoginMan(String dutyLoginMan) {
		this.dutyLoginMan = dutyLoginMan;
	}
	public String getCopyMan() {
		return copyMan;
	}
	public void setCopyMan(String copyMan) {
		this.copyMan = copyMan;
	}
	public String getCopyLoginMan() {
		return copyLoginMan;
	}
	public void setCopyLoginMan(String copyLoginMan) {
		this.copyLoginMan = copyLoginMan;
	}
	public String getFixedAssets() {
		return fixedAssets;
	}
	public void setFixedAssets(String fixedAssets) {
		this.fixedAssets = fixedAssets;
	}
	public String getManagerAssets() {
		return managerAssets;
	}
	public void setManagerAssets(String managerAssets) {
		this.managerAssets = managerAssets;
	}
	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	public String getEquipmentModel() {
		return equipmentModel;
	}
	public void setEquipmentModel(String equipmentModel) {
		this.equipmentModel = equipmentModel;
	}
	public String getMeasuringRange() {
		return measuringRange;
	}
	public void setMeasuringRange(String measuringRange) {
		this.measuringRange = measuringRange;
	}
	public String getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getFactoryNumber() {
		return factoryNumber;
	}
	public void setFactoryNumber(String factoryNumber) {
		this.factoryNumber = factoryNumber;
	}
	public String getCheckMethod() {
		return checkMethod;
	}
	public void setCheckMethod(String checkMethod) {
		this.checkMethod = checkMethod;
	}
	public String getEquipmentLevel() {
		return equipmentLevel;
	}
	public void setEquipmentLevel(String equipmentLevel) {
		this.equipmentLevel = equipmentLevel;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public Date getPurchaseTime() {
		return purchaseTime;
	}
	public void setPurchaseTime(Date purchaseTime) {
		this.purchaseTime = purchaseTime;
	}
	public String getMeasurementState() {
		return measurementState;
	}
	public void setMeasurementState(String measurementState) {
		this.measurementState = measurementState;
	}
	public List<InspectionPlan> getInspectionPlans() {
		return inspectionPlans;
	}
	public void setInspectionPlans(List<InspectionPlan> inspectionPlans) {
		this.inspectionPlans = inspectionPlans;
	}
	public Date getNextProofTime() {
		return nextProofTime;
	}
	public void setNextProofTime(Date nextProofTime) {
		this.nextProofTime = nextProofTime;
	}
	public Integer getWarnDates() {
		return warnDates;
	}
	public void setWarnDates(Integer warnDates) {
		this.warnDates = warnDates;
	}
	public Date getProofTime() {
		return proofTime;
	}
	public void setProofTime(Date proofTime) {
		this.proofTime = proofTime;
	}
	public String getProcessSection() {
		return processSection;
	}
	public void setProcessSection(String processSection) {
		this.processSection = processSection;
	}
	public String getStocktakingMan() {
		return stocktakingMan;
	}
	public void setStocktakingMan(String stocktakingMan) {
		this.stocktakingMan = stocktakingMan;
	}
	public Date getStocktakingTime() {
		return stocktakingTime;
	}
	public void setStocktakingTime(Date stocktakingTime) {
		this.stocktakingTime = stocktakingTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getGoalDutyMan() {
		return goalDutyMan;
	}
	public void setGoalDutyMan(String goalDutyMan) {
		this.goalDutyMan = goalDutyMan;
	}
	public String getGoalDutyLoginMan() {
		return goalDutyLoginMan;
	}
	public void setGoalDutyLoginMan(String goalDutyLoginMan) {
		this.goalDutyLoginMan = goalDutyLoginMan;
	}
	public Date getTransferTime() {
		return transferTime;
	}
	public void setTransferTime(Date transferTime) {
		this.transferTime = transferTime;
	}
	public String getTransferRemark() {
		return transferRemark;
	}
	public void setTransferRemark(String transferRemark) {
		this.transferRemark = transferRemark;
	}
	public Date getAuditTransferTime() {
		return auditTransferTime;
	}
	public void setAuditTransferTime(Date auditTransferTime) {
		this.auditTransferTime = auditTransferTime;
	}
	public String getAuditTransferMan() {
		return auditTransferMan;
	}
	public void setAuditTransferMan(String auditTransferMan) {
		this.auditTransferMan = auditTransferMan;
	}
	public String getTransferState() {
		return transferState;
	}
	public void setTransferState(String transferState) {
		this.transferState = transferState;
	}
	public Integer getIsWarm() {
		return isWarm;
	}
	public void setIsWarm(Integer isWarm) {
		this.isWarm = isWarm;
	}
	public String getCalibration() {
		return calibration;
	}
	public void setCalibration(String calibration) {
		this.calibration = calibration;
	}
	public List<InspectionMsaplan> getInspectionMsaplans() {
		return inspectionMsaplans;
	}
	public void setInspectionMsaplans(List<InspectionMsaplan> inspectionMsaplans) {
		this.inspectionMsaplans = inspectionMsaplans;
	}
	public String getGsmState() {
		return gsmState;
	}
	public void setGsmState(String gsmState) {
		this.gsmState = gsmState;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getDirectorLogin() {
		return directorLogin;
	}
	public void setDirectorLogin(String directorLogin) {
		this.directorLogin = directorLogin;
	}
	public String getRemark3() {
		return remark3;
	}
	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}
	public String getRemark4() {
		return remark4;
	}
	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}
	public String getRemark5() {
		return remark5;
	}
	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}
	
}

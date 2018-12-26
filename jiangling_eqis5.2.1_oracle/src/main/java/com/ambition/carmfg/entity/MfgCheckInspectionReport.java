package com.ambition.carmfg.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.norteksoft.wf.engine.client.ExtendField;
import com.norteksoft.wf.engine.client.FormFlowable;
import com.norteksoft.wf.engine.client.WorkflowInfo;

/**    
 * 制造检验报告
 * @authorBy 赵骏
 *
 */
@Entity
@Table(name = "MFG_CHECK_INSPECTION_REPORT")//
public class MfgCheckInspectionReport extends IdEntity implements FormFlowable {
	private static final long serialVersionUID = 1L;
	/**巡检模式*/
	public final static String SAVE_MODE_PATROL = "patrol";//巡检模式
	/**历史记录模式*/
	public final static String SAVE_MODE_HISTORY = "history";//历史记录模式
	/**检验中*/
	public final static String STATE_DEFAULT = "检验中";//检验中
	/**重新检验*/
	public final static String STATE_RECHECK = "重新检验";//重新检验
	public final static String STATE_AUDIT = "待审核";//待审核
	/**不合格品处理中*/
	public final static String STATE_DEFECTIVE_PROCESS="不合格品处理中";//不合格品处理中
	/**已完成*/
	public final static String STATE_COMPLETE="已完成";//已完成
	private String taskState="否";
	private String inspectionPoint;//检查点名称
	private String qrCode;//二维码编码
	private String processCard;//流程卡编码
	private String workshop; // 车间
	private String customerCode;//客户代码
	private String customerName;//客户名称
	private String customerModel;//客户机种
	private String processSection; // 制程区段
	private String factory; // 工厂
	private String productionLine; // 生产线
	private String section;//工段、工站
	private String orderNo;//订单号
	private String batchNo; // 批次号
	private String productNo;//产品编号
	private String floor;//楼层
	private String inspectionNo;//检验报告编号 
//	@Temporal(TemporalType.TIMESTAMP)
	private Date inspectionDate; // 检验日期
	private String inspector; //检验员
	private String productModel; //产品类型
	private String workProcedure; // 工序
	private String workGroupType; // 班别
	private String checkBomCode;//物料编码/产品型号
	private String checkBomName;//物料名称/产品名称
	private String checkBomModel;//物料型号/产品型号
	private String machineNo;//机种
	private String machineName;//机种名称
	private Integer stockAmount; // 送检数量
	private String amountUnit;//批次数量单位
	private Integer inspectionAmount; // 检验数
	private Integer qualifiedAmount; // 合格数
	private Integer unqualifiedAmount; // 不良数
	private String inspectionConclusion; // 检验判定
	private Float qualifiedRate; // 合格率
	private String processingResult = "合格"; // 处理结果
	private String attachmentFiles;//附件
	private String inspectionDatas;//检验数据附件
	//巡检数据增加字段
	@Temporal(TemporalType.DATE)
	private Date sendDate;//发货日期
	@Column(length=50)
	private String imgFileId;//产品图片ID
    @Column(length=50)
    private String standardVersion;//版本号
	private Integer patrolTimes;//巡检次数
	private Integer patrolQualifiedTimes;//巡检合格次数
	private Integer patrolUnqualifiedTimes;//巡检不合格次数
	@Column(length=500)
	private String spcSampleIds;//spc采集ID--首检
//	private String spcSampleIdsPatrol;////spc采集ID--巡检
//	private String spcSampleIdsComplete;////spc采集ID--末检
	public final static String PATROL_STATE_DEFAULT = "巡检中";
	public final static String PATROL_STATE_COMPLETE = "已完成";
	private String patrolState = PATROL_STATE_DEFAULT;//巡检状态,巡检中,已完工
	private String saveMode = SAVE_MODE_PATROL;//编辑时保存的模式,巡检/历史记录
	@Column(length=30)
	private String reportState = STATE_DEFAULT;//状态,默认为不合格品处理中
	private String choiceAuditMan;//选择审核人
	private String choiceAuditLoinMan;//选择审核人登录名
	private String auditMan;//审核人员
	@Temporal(TemporalType.DATE)
	private Date auditTime;//审核时间
	private String auditLoginMan;//审核人登录名
	private String auditText;//审核意见
	private String lanuchState;//
	@Column(length=500)
	private String auditRemark;//检验单审核备注
	@Embedded
	private PatrolSettings patrolSettings;//巡检周期设置
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date nextPatrolDate=new Date();//计划下次巡检时间
	
	@Enumerated(EnumType.ORDINAL)
	private InspectionPointTypeEnum inspectionPointType = InspectionPointTypeEnum.STORAGEINSPECTION;//采集点类型(检验数据、日报数据等),默认为检查数据
	
	@Enumerated(EnumType.ORDINAL)
	private InspectionType inspectionType;
	@Column(length=100)
	private String dpFormNo;//不合格品处理单号
	@OneToMany(mappedBy="mfgCheckInspectionReport",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	private List<MfgCheckItem> checkItems;//检查项目
	
	@OneToMany(mappedBy="mfgCheckInspectionReport",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<MfgSupplierMessage> mfgSupplierMessages;
	
	@OneToMany(mappedBy="mfgCheckInspectionReport",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OrderBy(value="inspectionDate")
	@JsonIgnore
	private List<MfgPatrolItem> patrolItems;//巡检项目
	
	@OneToMany(mappedBy="mfgCheckInspectionReport",cascade=CascadeType.ALL)
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<MfgManufactureMessage>  manufactureMessages;//生产信息
	
	@OneToMany(mappedBy="mfgCheckInspectionReport",cascade=CascadeType.ALL)
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<MfgPlantParameterItem> mfgPlantParameterItems;//设备信息
	
	private String checkedItemStr;//控制是否编辑
	private String supplierMessageStr;//控制是否编辑
	private String manMessagesStr;//控制是否可编辑
	private String exceptionNo;//异常单号
	private String exceptionId;//异常ID
	private String hasSynced="否";//是否已经同步到spc
	
	@Embedded
	private ExtendField extendField;

	@Embedded
	private WorkflowInfo workflowInfo;
	
	public String getAmountUnit() {
		return amountUnit;
	}

	public void setAmountUnit(String amountUnit) {
		this.amountUnit = amountUnit;
	}

	public String getWorkshop() {
		return workshop;
	}

	public void setWorkshop(String workshop) {
		this.workshop = workshop;
	}

	public String getProductionLine() {
		return productionLine;
	}

	public void setProductionLine(String productionLine) {
		this.productionLine = productionLine;
	}

	public String getWorkProcedure() {
		return workProcedure;
	}

	public void setWorkProcedure(String workProcedure) {
		this.workProcedure = workProcedure;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getInspectionNo() {
		return inspectionNo;
	}

	public void setInspectionNo(String inspectionNo) {
		this.inspectionNo = inspectionNo;
	}

	public String getInspectionPoint() {
		return inspectionPoint;
	}


	public void setInspectionPoint(String inspectionPoint) {
		this.inspectionPoint = inspectionPoint;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
		if(inspectionDate == null){
			this.setYearAndWeek(null);
			this.setYearAndMonth(null);
			this.setDateOfMonthOfYear(null);
		}else{
			this.setYearAndMonth(CommonUtil1.getYearAndMonth(inspectionDate));
			this.setYearAndWeek(CommonUtil1.getYearAndWeek(inspectionDate));
			this.setDateOfMonthOfYear(CommonUtil1.getYearMonthAndDate(inspectionDate));
		}
	}

	@Transient
	public String getInspectionDateStr() {
		return DateUtil.formateTimeStr(inspectionDate);
	}

	@Transient
	public String getInspectionShortDateStr() {
		return DateUtil.formateDateStr(inspectionDate);
	}
	
	public String getProcessSection() {
		return processSection;
	}

	public void setProcessSection(String processSection) {
		this.processSection = processSection;
	}

	public String getInspector() {
		return inspector;
	}

	public void setInspector(String inspector) {
		this.inspector = inspector;
	}

	public String getAuditMan() {
		return auditMan;
	}

	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getProcessCard() {
		return processCard;
	}

	public void setProcessCard(String processCard) {
		this.processCard = processCard;
	}

	public String getCheckBomCode() {
		return checkBomCode;
	}


	public void setCheckBomCode(String checkBomCode) {
		this.checkBomCode = checkBomCode;
	}

	public String getCheckBomName() {
		return checkBomName;
	}


	public void setCheckBomName(String checkBomName) {
		this.checkBomName = checkBomName;
	}


	public Integer getStockAmount() {
		return stockAmount;
	}


	public void setStockAmount(Integer stockAmount) {
		this.stockAmount = stockAmount;
	}


	public Integer getInspectionAmount() {
		return inspectionAmount;
	}


	public void setInspectionAmount(Integer inspectionAmount) {
		this.inspectionAmount = inspectionAmount;
	}


	public Integer getQualifiedAmount() {
		return qualifiedAmount;
	}


	public void setQualifiedAmount(Integer qualifiedAmount) {
		this.qualifiedAmount = qualifiedAmount;
	}


	public Integer getUnqualifiedAmount() {
		return unqualifiedAmount;
	}


	public void setUnqualifiedAmount(Integer unqualifiedAmount) {
		this.unqualifiedAmount = unqualifiedAmount;
	}


	public String getInspectionConclusion() {
		return inspectionConclusion;
	}


	public void setInspectionConclusion(String inspectionConclusion) {
		this.inspectionConclusion = inspectionConclusion;
	}


	public Float getQualifiedRate() {
		return qualifiedRate;
	}


	public void setQualifiedRate(Float qualifiedRate) {
		this.qualifiedRate = qualifiedRate;
	}


	public String getProcessingResult() {
		return processingResult;
	}


	public void setProcessingResult(String processingResult) {
		this.processingResult = processingResult;
	}


	public String getAttachmentFiles() {
		return attachmentFiles;
	}


	public void setAttachmentFiles(String attachmentFiles) {
		this.attachmentFiles = attachmentFiles;
	}


	public String getInspectionDatas() {
		return inspectionDatas;
	}

	public void setInspectionDatas(String inspectionDatas) {
		this.inspectionDatas = inspectionDatas;
	}

	public List<MfgCheckItem> getCheckItems() {
		return checkItems;
	}

	public void setCheckItems(List<MfgCheckItem> checkItems) {
		this.checkItems = checkItems;
	}

	public ExtendField getExtendField() {
		return extendField;
	}

	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}

	public WorkflowInfo getWorkflowInfo() {
		return workflowInfo;
	}

	public void setWorkflowInfo(WorkflowInfo workflowInfo) {
		this.workflowInfo = workflowInfo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getWorkGroupType() {
		return workGroupType;
	}

	public void setWorkGroupType(String workGroupType) {
		this.workGroupType = workGroupType;
	}

	public String getCheckBomModel() {
		return checkBomModel;
	}

	public void setCheckBomModel(String checkBomModel) {
		this.checkBomModel = checkBomModel;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getImgFileId() {
		return imgFileId;
	}

	public void setImgFileId(String imgFileId) {
		this.imgFileId = imgFileId;
	}

	public String getStandardVersion() {
		return standardVersion;
	}

	public void setStandardVersion(String standardVersion) {
		this.standardVersion = standardVersion;
	}

	public Integer getPatrolTimes() {
		return patrolTimes;
	}

	public void setPatrolTimes(Integer patrolTimes) {
		this.patrolTimes = patrolTimes;
	}

	public Integer getPatrolQualifiedTimes() {
		return patrolQualifiedTimes;
	}

	public void setPatrolQualifiedTimes(Integer patrolQualifiedTimes) {
		this.patrolQualifiedTimes = patrolQualifiedTimes;
	}

	public Integer getPatrolUnqualifiedTimes() {
		return patrolUnqualifiedTimes;
	}

	public void setPatrolUnqualifiedTimes(Integer patrolUnqualifiedTimes) {
		this.patrolUnqualifiedTimes = patrolUnqualifiedTimes;
	}

	public String getSpcSampleIds() {
		return spcSampleIds;
	}

	public void setSpcSampleIds(String spcSampleIds) {
		this.spcSampleIds = spcSampleIds;
	}

	public String getPatrolState() {
		return patrolState;
	}

	public void setPatrolState(String patrolState) {
		this.patrolState = patrolState;
	}

	public String getSaveMode() {
		return saveMode;
	}

	public void setSaveMode(String saveMode) {
		this.saveMode = saveMode;
	}

	public String getReportState() {
		return reportState;
	}

	public void setReportState(String reportState) {
		this.reportState = reportState;
	}

	public String getAuditLoginMan() {
		return auditLoginMan;
	}

	public void setAuditLoginMan(String auditLoginMan) {
		this.auditLoginMan = auditLoginMan;
	}

	public String getAuditText() {
		return auditText;
	}

	public void setAuditText(String auditText) {
		this.auditText = auditText;
	}

	public String getAuditRemark() {
		return auditRemark;
	}

	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}

	public PatrolSettings getPatrolSettings() {
		return patrolSettings;
	}

	public void setPatrolSettings(PatrolSettings patrolSettings) {
		this.patrolSettings = patrolSettings;
	}

	public Date getNextPatrolDate() {
		return nextPatrolDate;
	}

	public void setNextPatrolDate(Date nextPatrolDate) {
		this.nextPatrolDate = nextPatrolDate;
	}

	public InspectionPointTypeEnum getInspectionPointType() {
		return inspectionPointType;
	}

	public void setInspectionPointType(InspectionPointTypeEnum inspectionPointType) {
		this.inspectionPointType = inspectionPointType;
	}

	public InspectionType getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
	}

	public String getDpFormNo() {
		return dpFormNo;
	}

	public void setDpFormNo(String dpFormNo) {
		this.dpFormNo = dpFormNo;
	}

	public List<MfgPatrolItem> getPatrolItems() {
		return patrolItems;
	}

	public void setPatrolItems(List<MfgPatrolItem> patrolItems) {
		this.patrolItems = patrolItems;
	}


	public String getMachineNo() {
		return machineNo;
	}

	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}

	public String getLanuchState() {
		return lanuchState;
	}

	public void setLanuchState(String lanuchState) {
		this.lanuchState = lanuchState;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public List<MfgSupplierMessage> getMfgSupplierMessages() {
		return mfgSupplierMessages;
	}

	public void setMfgSupplierMessages(List<MfgSupplierMessage> mfgSupplierMessages) {
		this.mfgSupplierMessages = mfgSupplierMessages;
	}

	public String getCheckedItemStr() {
		return checkedItemStr;
	}

	public void setCheckedItemStr(String checkedItemStr) {
		this.checkedItemStr = checkedItemStr;
	}

	public String getSupplierMessageStr() {
		return supplierMessageStr;
	}

	public void setSupplierMessageStr(String supplierMessageStr) {
		this.supplierMessageStr = supplierMessageStr;
	}

	public String getChoiceAuditMan() {
		return choiceAuditMan;
	}

	public void setChoiceAuditMan(String choiceAuditMan) {
		this.choiceAuditMan = choiceAuditMan;
	}

	public String getChoiceAuditLoinMan() {
		return choiceAuditLoinMan;
	}

	public void setChoiceAuditLoinMan(String choiceAuditLoinMan) {
		this.choiceAuditLoinMan = choiceAuditLoinMan;
	}

	public List<MfgManufactureMessage> getManufactureMessages() {
		return manufactureMessages;
	}

	public void setManufactureMessages(
			List<MfgManufactureMessage> manufactureMessages) {
		this.manufactureMessages = manufactureMessages;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public String getManMessagesStr() {
		return manMessagesStr;
	}

	public void setManMessagesStr(String manMessagesStr) {
		this.manMessagesStr = manMessagesStr;
	}

	public List<MfgPlantParameterItem> getMfgPlantParameterItems() {
		return mfgPlantParameterItems;
	}

	public void setMfgPlantParameterItems(
			List<MfgPlantParameterItem> mfgPlantParameterItems) {
		this.mfgPlantParameterItems = mfgPlantParameterItems;
	}
	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerModel() {
		return customerModel;
	}

	public void setCustomerModel(String customerModel) {
		this.customerModel = customerModel;
	}
	
	public String getExceptionNo() {
		return exceptionNo;
	}

	public void setExceptionNo(String exceptionNo) {
		this.exceptionNo = exceptionNo;
	}

	public String getExceptionId() {
		return exceptionId;
	}

	public void setExceptionId(String exceptionId) {
		this.exceptionId = exceptionId;
	}

	public String getHasSynced() {
		return hasSynced;
	}

	public void setHasSynced(String hasSynced) {
		this.hasSynced = hasSynced;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String toString(){
		return "进货检验管理: 进货检验报告，ID"+this.getId()+"，检验日期"+this.inspectionNo+"，检验日期"+this.inspectionDate;
	}
}

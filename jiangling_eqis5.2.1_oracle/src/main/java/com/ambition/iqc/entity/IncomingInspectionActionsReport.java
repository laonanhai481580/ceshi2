package com.ambition.iqc.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;
import com.ambition.supplier.utils.DateUtil;
import com.ambition.util.common.CommonUtil1;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.norteksoft.wf.engine.client.ExtendField;
import com.norteksoft.wf.engine.client.FormFlowable;
import com.norteksoft.wf.engine.client.WorkflowInfo;

/**    
 * 检验报告
 * @authorBy 赵骏
 *
 */
@Entity
@Table(name = "IQC_IIAR")//
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})//忽略Json不认识的属性，避免转换Json出错
public class IncomingInspectionActionsReport extends IdEntity implements FormFlowable {
	private static final long serialVersionUID = 1L;
	private Integer companyISN;//公司内码
	private String sampleSchemeType="正常";//抽样方案类型,正常,加严,放宽
	private String batchNo; // 批次号
	private String processSection; // 制程区段
	private String inspectionNo;//检验报告编号
	private String managerNo;//管理编号
	private String formType;//单据类型,
	private String storageType;//入库类别
	private String requestCheckNo;//请检单号
	private String fireControlNo;//消防编号
	private String entryId;//分录序号
	private String acceptNo;//接受行号
	private String lotNo;//LotNo
	private String customerCode;//客户代码
	private String erpInspectionNo;//ERP来料单编号
	private Double price;//单价
	@Column(length=40)
	private String units;//单位
	private String orderNo;//订单号
	private Date planIncomingDate;//计划到料日期
	@Temporal(TemporalType.TIMESTAMP)
	private Date inspectionDate; // 检验日期
	@Temporal(TemporalType.TIMESTAMP)
	private Date schemeStartDate;//抽样开始的时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date enterDate; // 到货日期
	private Integer enterYearMonthAndDate;//到货年月日
	private Integer enterYearAndMonth;//到货年月日
	private String inspector; //检验员
	private String inspectorLoginName;//检验员登录名
	private String auditText;//审核意见
	@Temporal(TemporalType.TIMESTAMP)
	private Date recheckTime;//重检时间
	private String recheckText;//重检意见
	private String auditLoginMan;//审核人员登录名
	private String auditMan;//审核人员
	@Temporal(TemporalType.TIMESTAMP)
	private Date auditTime;//审核时间
	private String checkBomCode;//物料编码
	private String checkBomName;//物料名称
	private String checkBomMaterialType;//物料类别（用户自填，用于供应商考评）
	private String materialTypeCode;//物料类别代码
	private String materialType;//物料类别(集成)
	private String modelSpecification;//规格型号
	private String workingProcedure;//检验工序
	private String supplierName; //供方名称
	private String supplierCode;//供方编码
	private String approvedSupplier = "是";//是否合格供应商
	private String is3C;//是否3C
	private String isStandard;//是否标准件
	private String iskeyComponent;//是否关键件
	@Column(length=50)
	private String isFactory="否";
	private String standardVersion;//检验标准的版本
	private Double stockAmount; // 来料数
	private Double erpStockAmount;//erp来料数
	private Double inspectionAmount; // 检验数
	private Double qualifiedAmount; // 合格数  
	private Double destroyAmount=0.0d;//破坏数量
	private Double unqualifiedAmount; // 不良数
	private String acknowledgeState;//承认状态
	private String acknowledgeVersion;//承认版本
	private String productStage;//产品阶段
	private String inspectionConclusion; // 检验判定
	private Float  qualifiedRate; // 合格率
	private Float  qualifiedUnRate; // 特性不良率
	private String functionAmount;//功能合格数
	private String functionUnAmount;//功能不合格数
	private String functionAmountRate;//功能合格率
	private String functionUnAmountRate;//功能不良率
	private String checkAmount;//抽检数
	private String sizeAmount;//尺寸合格数
	private String sizeUnAmount;//尺寸不合格数
	private String sizeAmountRate;//尺寸合格率
	private String sizeUnAmountRate;//尺寸不良率
	private String wgBadRate;//外观不良率
	private String appearanceAmount;//外观合格数
	private String appearanceUnAmount;//外观不合格数
	private String wgconclusion;//外观检验判定
	private String wgBadDescrible;//外观不良现象	
	private String gnBadDescrible;//功能不良现象	
	private String ccBadDescrible;//尺寸不良现象	
	private String appearanceAmountRate;//外观合格率	
	private String processingState = "未处理";//处理状态
	private String processingResult; // 处理结果
	private Double processingBadQty;//判定不良数量
	private Double processingReceiveQty;//判定接收数量
	private String processingMan;//处理人员
	@Temporal(TemporalType.TIMESTAMP)
	private Date processingDate;//处理时间
	private String packing;//包装情况
	private String qualityCertificate = "有";//有无质量证明
	private String usedProducts;//应用产品
	private String dealState;//当前处理状态
	private String dealProcess;//当前处理环节
	@Column(length=500)
	private String spcSampleIds;//spc采集ID
	private String isTest="否";//是否实验
	private String testConclusion="OK";//实验判定
	private String exceptionNo;//进料异常处理单号
	private String exceptionId;//进料异常处理单ID
	private String sqeMrbReportNo;//mrb单号
	private String returnReportNo;//退货通知单单号
	private String attachmentFiles;//附件
	private String currentAuditMan;//当前审核人
	private String currentAuditManLogin;//当前审核人
	private String isDueMail="否";//是否发起过超期邮件
	@Column(length=100)
	private String unReportFormNo;//不合格品处理单号
	@OneToMany(mappedBy="iiar",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore()
	private List<CheckItem> checkItems;//检查项目
	private String approvedBy;//批准人
	private Date approvedTime;//批准日期
	@Temporal(TemporalType.TIMESTAMP)
	private Date sqeReplyTime;//sqe回复日期
	@Temporal(TemporalType.TIMESTAMP)
	private Date sqeCompleteTime;//sqe追踪完成时间
	private String inspectionState=INPECTION_STATE_DEFAULT;//处理状态
	/**待检验**/
	public static final String INPECTION_STATE_DEFAULT = "待检验";
	/**重新检验**/
	public static final String INPECTION_STATE_RE_CHECK = "重新检验";
	/**暂存**/
	public static final String INPECTION_STATE_SAVE_TEMP = "暂存";
	/**待审核**/
	public static final String INPECTION_STATE_SUBMIT = "待审核";
	public static final String INPECTION_STATE_LAST_SUBMIT="上级待审核";
	/**已完成**/
	public static final String INPECTION_STATE_EXCEPTION_CONFIRM = "异常待确认";
	/**已完成**/
	public static final String INPECTION_STATE_AUDIT = "已完成";
	/**商务退货单*/
	public static final String FORM_TYPE_BACK = "商务退货单";
	/**入库单*/
	public static final String FORM_TYPE_INPUT = "入库";
	@Column(length=1000)
	private String remark; // 备注
	/**
	 * 包装检验记录
	 * */
	private String packingFir;//检验内容项
	private String packingFirResult;//检验内容项
	private String packingSec;//检验内容项
	private String packingSecResult;//检验内容项
	private String packingTre;//检验内容项
	private String packingTreResult;//检验内容项
	@Temporal(TemporalType.DATE)
	private Date productDate;//生产日期
	@Temporal(TemporalType.DATE)
	private Date effectiveData;//有效日期
	private String dateTimeResult;//日期是否合格
	private String packingText;//包装描述
	private String packingResult;//包装综合判定
	private String packingInspcetionMan;//检验人
	/**
	 * HSF /ORD 检验结果
	 * */
	@Column(length=3000)
	private String hisInspectionItem;//HIS检验内容
	private String hisPackingResult;//HIS检验结果
	private String hisReportNo;//报告编号
	private String hisReportId;//报告ID
	@Column(length=3000)
	private String hisText;//HIS描述
	@Column(length=3000)
	private String ordInspectionItem;//HIS检验内容
	private String ordPackingResult;//HIS检验结果
	private String ordReportNo;//报告编号
	private String ordReportId;//报告ID
	@Column(length=3000)
	private String ordText;//HIS描述
	
	private String exemptionNo;//免检报告
	private String mrbNo;//mrb单据
	private String returnGoodNo;//退货单
	private String lastStageMan;//上一级审核人
	private String lastStateLoginMan;//上一级审核人登录名
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastStateTime;//上一级审核人审核时间
	private String lastStateText;//上级审核意见

	
	//FPM新增
	private String appearanceInspection;//外观检验
	private String uncheckNo;//免检单号
	private String samplingPlan;//抽样计划
	private String samplingStandard1;//抽样水准1
	private String samplingStandard2;//抽样水准2
	private String samplingStandard3;//抽样水准3
	private String samplingStandard4;//抽样水准4
	private String samplingLevel;//抽样等级
	private String samplingN1;//样本N1
	private String samplingN2;//样本N2
	private String samplingAc1;//样本Ac1
	private String samplingAc2;//样本Ac2
	private String samplingRe1;//样本Re1
	private String samplingRe2;//样本Re2
	private Integer samplingBadCount1;//不良数1
	private Integer samplingBadCount2;//不良数2
	private String badRate1;//不良率1
	private String badRate2;//不良率2
	private String samplingBadItem1;//不良项目1
	private String samplingBadItem2;//不良项目2
	private String determine;//判定
	private String hsfCheckMethod;//HSF检验方法
	private String hsfFormNo;//HSF报告编号  
	private String hsfDetermine;//HSF判定
	private String inspectionBasis;//检验依据
	private String version;//版本
	private String isNewData="是";//新数据格式
	private String isDelete="否";//是否被删除
	private String deleteMan;//删除人员
	private String deleteManLogin;//删除人登录名
	private Date  deleteDate;//删除日期
	@Embedded
	private ExtendField extendField;

	@Embedded
	private WorkflowInfo workflowInfo;
	
	public String getInspectionBasis() {
		return inspectionBasis;
	}


	public void setInspectionBasis(String inspectionBasis) {
		this.inspectionBasis = inspectionBasis;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public String getSampleSchemeType() {
		return sampleSchemeType;
	}


	public Integer getSamplingBadCount1() {
		return samplingBadCount1;
	}


	public void setSamplingBadCount1(Integer samplingBadCount1) {
		this.samplingBadCount1 = samplingBadCount1;
	}


	public Integer getSamplingBadCount2() {
		return samplingBadCount2;
	}


	public void setSamplingBadCount2(Integer samplingBadCount2) {
		this.samplingBadCount2 = samplingBadCount2;
	}


	public void setSampleSchemeType(String sampleSchemeType) {
		this.sampleSchemeType = sampleSchemeType;
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

	public Date getEnterDate() {
		return enterDate;
	}

	@Transient
	public String getEnterDateStr() {
		return DateUtil.formateDateStr(enterDate);
	}
	
	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
		if(enterDate == null){
			this.enterYearMonthAndDate = null;
		}else{
			this.enterYearMonthAndDate = CommonUtil1.getYearMonthAndDate(enterDate);;
		}
	}


	public String getProcessSection() {
		return processSection;
	}

	public void setProcessSection(String processSection) {
		this.processSection = processSection;
	}

	public Integer getEnterYearMonthAndDate() {
		return enterYearMonthAndDate;
	}

	public void setEnterYearMonthAndDate(Integer enterYearMonthAndDate) {
		this.enterYearMonthAndDate = enterYearMonthAndDate;
	}

	public Integer getEnterYearAndMonth() {
		return enterYearAndMonth;
	}

	public void setEnterYearAndMonth(Integer enterYearAndMonth) {
		this.enterYearAndMonth = enterYearAndMonth;
	}

	public String getInspector() {
		return inspector;
	}

	public void setInspector(String inspector) {
		this.inspector = inspector;
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

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public Double getStockAmount() {
		return stockAmount;
	}

	public void setStockAmount(Double stockAmount) {
		this.stockAmount = stockAmount;
	}

	public Double getInspectionAmount() {
		return inspectionAmount;
	}

	public void setInspectionAmount(Double inspectionAmount) {
		this.inspectionAmount = inspectionAmount;
	}

	public Double getQualifiedAmount() {
		return qualifiedAmount;
	}

	public void setQualifiedAmount(Double qualifiedAmount) {
		this.qualifiedAmount = qualifiedAmount;
	}

	public Double getUnqualifiedAmount() {
		return unqualifiedAmount;
	}

	public void setUnqualifiedAmount(Double unqualifiedAmount) {
		this.unqualifiedAmount = unqualifiedAmount;
	}

	public void setErpStockAmount(Double erpStockAmount) {
		this.erpStockAmount = erpStockAmount;
	}

	public void setDestroyAmount(Double destroyAmount) {
		this.destroyAmount = destroyAmount;
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


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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


	public String getAttachmentFiles() {
		return attachmentFiles;
	}

	public void setAttachmentFiles(String attachmentFiles) {
		this.attachmentFiles = attachmentFiles;
	}

	public String getAuditMan() {
		return auditMan;
	}

	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Date getSchemeStartDate() {
		return schemeStartDate;
	}

	public void setSchemeStartDate(Date schemeStartDate) {
		this.schemeStartDate = schemeStartDate;
	}
	
	public String getCheckBomMaterialType() {
		return checkBomMaterialType;
	}

	public void setCheckBomMaterialType(String checkBomMaterialType) {
		this.checkBomMaterialType = checkBomMaterialType;
	}

	public String getSchemeStartDateStr() {
		return DateUtil.formateDateStr(schemeStartDate);
	}
	

	public String getDealState() {
		return dealState;
	}

	public void setDealState(String dealState) {
		this.dealState = dealState;
	}

	public String getDealProcess() {
		return dealProcess;
	}

	public void setDealProcess(String dealProcess) {
		this.dealProcess = dealProcess;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getModelSpecification() {
		return modelSpecification;
	}

	public void setModelSpecification(String modelSpecification) {
		this.modelSpecification = modelSpecification;
	}

	public String getApprovedSupplier() {
		return approvedSupplier;
	}

	public void setApprovedSupplier(String approvedSupplier) {
		this.approvedSupplier = approvedSupplier;
	}

	public String getPacking() {
		return packing;
	}

	public void setPacking(String packing) {
		this.packing = packing;
	}

	public String getQualityCertificate() {
		return qualityCertificate;
	}

	public void setQualityCertificate(String qualityCertificate) {
		this.qualityCertificate = qualityCertificate;
	}

	public String getUsedProducts() {
		return usedProducts;
	}

	public void setUsedProducts(String usedProducts) {
		this.usedProducts = usedProducts;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedTime() {
		return approvedTime;
	}

	public void setApprovedTime(Date approvedTime) {
		this.approvedTime = approvedTime;
	}

	public String getRequestCheckNo() {
		return requestCheckNo;
	}

	public void setRequestCheckNo(String requestCheckNo) {
		this.requestCheckNo = requestCheckNo;
	}

	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getInspectionState() {
		return inspectionState;
	}

	public void setInspectionState(String inspectionState) {
		this.inspectionState = inspectionState;
	}

	public String getErpInspectionNo() {
		return erpInspectionNo;
	}

	public void setErpInspectionNo(String erpInspectionNo) {
		this.erpInspectionNo = erpInspectionNo;
	}

	public String getProcessingMan() {
		return processingMan;
	}

	public void setProcessingMan(String processingMan) {
		this.processingMan = processingMan;
	}

	public Date getProcessingDate() {
		return processingDate;
	}

	public void setProcessingDate(Date processingDate) {
		this.processingDate = processingDate;
	}

	public String getProcessingState() {
		return processingState;
	}

	public void setProcessingState(String processingState) {
		this.processingState = processingState;
	}

	public Double getErpStockAmount() {
		return erpStockAmount;
	}

	public Double getDestroyAmount() {
		return destroyAmount;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public Integer getCompanyISN() {
		return companyISN;
	}

	public void setCompanyISN(Integer companyISN) {
		this.companyISN = companyISN;
	}


	public String getAcceptNo() {
		return acceptNo;
	}

	public void setAcceptNo(String acceptNo) {
		this.acceptNo = acceptNo;
	}

	public String getIsTest() {
		return isTest;
	}

	public void setIsTest(String isTest) {
		this.isTest = isTest;
	}

	public String getTestConclusion() {
		return testConclusion;
	}

	public void setTestConclusion(String testConclusion) {
		this.testConclusion = testConclusion;
	}

	public Double getProcessingBadQty() {
		return processingBadQty;
	}

	public void setProcessingBadQty(Double processingBadQty) {
		this.processingBadQty = processingBadQty;
	}


	public Double getProcessingReceiveQty() {
		return processingReceiveQty;
	}

	public void setProcessingReceiveQty(Double processingReceiveQty) {
		this.processingReceiveQty = processingReceiveQty;
	}
	
	public Date getPlanIncomingDate() {
		return planIncomingDate;
	}

	public void setPlanIncomingDate(Date planIncomingDate) {
		this.planIncomingDate = planIncomingDate;
	}

	public String getIs3C() {
		return is3C;
	}

	public void setIs3C(String is3c) {
		is3C = is3c;
	}

	public String getIsStandard() {
		return isStandard;
	}

	public void setIsStandard(String isStandard) {
		this.isStandard = isStandard;
	}

	public String getIskeyComponent() {
		return iskeyComponent;
	}

	public void setIskeyComponent(String iskeyComponent) {
		this.iskeyComponent = iskeyComponent;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public String getInspectorLoginName() {
		return inspectorLoginName;
	}

	public void setInspectorLoginName(String inspectorLoginName) {
		this.inspectorLoginName = inspectorLoginName;
	}


	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getWorkingProcedure() {
		return workingProcedure;
	}

	public void setWorkingProcedure(String workingProcedure) {
		this.workingProcedure = workingProcedure;
	}

	public String getFireControlNo() {
		return fireControlNo;
	}

	public void setFireControlNo(String fireControlNo) {
		this.fireControlNo = fireControlNo;
	}

	public String getStandardVersion() {
		return standardVersion;
	}

	public void setStandardVersion(String standardVersion) {
		this.standardVersion = standardVersion;
	}


	public String getUnReportFormNo() {
		return unReportFormNo;
	}

	public void setUnReportFormNo(String unReportFormNo) {
		this.unReportFormNo = unReportFormNo;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getPackingFir() {
		return packingFir;
	}

	public void setPackingFir(String packingFir) {
		this.packingFir = packingFir;
	}

	public String getPackingFirResult() {
		return packingFirResult;
	}

	public void setPackingFirResult(String packingFirResult) {
		this.packingFirResult = packingFirResult;
	}

	public String getPackingSec() {
		return packingSec;
	}

	public void setPackingSec(String packingSec) {
		this.packingSec = packingSec;
	}

	public String getPackingSecResult() {
		return packingSecResult;
	}

	public void setPackingSecResult(String packingSecResult) {
		this.packingSecResult = packingSecResult;
	}

	public String getPackingTre() {
		return packingTre;
	}

	public void setPackingTre(String packingTre) {
		this.packingTre = packingTre;
	}

	public String getPackingTreResult() {
		return packingTreResult;
	}

	public void setPackingTreResult(String packingTreResult) {
		this.packingTreResult = packingTreResult;
	}

	public Date getProductDate() {
		return productDate;
	}

	public void setProductDate(Date productDate) {
		this.productDate = productDate;
	}

	public Date getEffectiveData() {
		return effectiveData;
	}

	public void setEffectiveData(Date effectiveData) {
		this.effectiveData = effectiveData;
	}

	public String getDateTimeResult() {
		return dateTimeResult;
	}

	public void setDateTimeResult(String dateTimeResult) {
		this.dateTimeResult = dateTimeResult;
	}

	public String getPackingText() {
		return packingText;
	}

	public void setPackingText(String packingText) {
		this.packingText = packingText;
	}

	public String getPackingResult() {
		return packingResult;
	}

	public void setPackingResult(String packingResult) {
		this.packingResult = packingResult;
	}

	public String getPackingInspcetionMan() {
		return packingInspcetionMan;
	}

	public void setPackingInspcetionMan(String packingInspcetionMan) {
		this.packingInspcetionMan = packingInspcetionMan;
	}

	public String getAuditLoginMan() {
		return auditLoginMan;
	}

	public void setAuditLoginMan(String auditLoginMan) {
		this.auditLoginMan = auditLoginMan;
	}

	public String getHisInspectionItem() {
		return hisInspectionItem;
	}

	public void setHisInspectionItem(String hisInspectionItem) {
		this.hisInspectionItem = hisInspectionItem;
	}

	public String getHisPackingResult() {
		return hisPackingResult;
	}

	public void setHisPackingResult(String hisPackingResult) {
		this.hisPackingResult = hisPackingResult;
	}

	public String getHisReportNo() {
		return hisReportNo;
	}

	public void setHisReportNo(String hisReportNo) {
		this.hisReportNo = hisReportNo;
	}

	public String getHisText() {
		return hisText;
	}

	public void setHisText(String hisText) {
		this.hisText = hisText;
	}

	public String getOrdInspectionItem() {
		return ordInspectionItem;
	}

	public void setOrdInspectionItem(String ordInspectionItem) {
		this.ordInspectionItem = ordInspectionItem;
	}

	public String getOrdPackingResult() {
		return ordPackingResult;
	}

	public void setOrdPackingResult(String ordPackingResult) {
		this.ordPackingResult = ordPackingResult;
	}

	public String getOrdReportNo() {
		return ordReportNo;
	}

	public void setOrdReportNo(String ordReportNo) {
		this.ordReportNo = ordReportNo;
	}

	public String getOrdText() {
		return ordText;
	}

	public void setOrdText(String ordText) {
		this.ordText = ordText;
	}

	public String getAppearanceAmount() {
		return appearanceAmount;
	}

	public void setAppearanceAmount(String appearanceAmount) {
		this.appearanceAmount = appearanceAmount;
	}

	public String getAppearanceUnAmount() {
		return appearanceUnAmount;
	}

	public void setAppearanceUnAmount(String appearanceUnAmount) {
		this.appearanceUnAmount = appearanceUnAmount;
	}

	public String getAppearanceAmountRate() {
		return appearanceAmountRate;
	}

	public void setAppearanceAmountRate(String appearanceAmountRate) {
		this.appearanceAmountRate = appearanceAmountRate;
	}

	public String getAcknowledgeState() {
		return acknowledgeState;
	}

	public void setAcknowledgeState(String acknowledgeState) {
		this.acknowledgeState = acknowledgeState;
	}

	public String getAcknowledgeVersion() {
		return acknowledgeVersion;
	}

	public void setAcknowledgeVersion(String acknowledgeVersion) {
		this.acknowledgeVersion = acknowledgeVersion;
	}

	public String getExemptionNo() {
		return exemptionNo;
	}

	public void setExemptionNo(String exemptionNo) {
		this.exemptionNo = exemptionNo;
	}

	public String getProductStage() {
		return productStage;
	}

	public void setProductStage(String productStage) {
		this.productStage = productStage;
	}

	public String getAuditText() {
		return auditText;
	}

	public void setAuditText(String auditText) {
		this.auditText = auditText;
	}

	public String getMrbNo() {
		return mrbNo;
	}

	public void setMrbNo(String mrbNo) {
		this.mrbNo = mrbNo;
	}

	public String getReturnGoodNo() {
		return returnGoodNo;
	}

	public void setReturnGoodNo(String returnGoodNo) {
		this.returnGoodNo = returnGoodNo;
	}

	public String getLastStageMan() {
		return lastStageMan;
	}

	public void setLastStageMan(String lastStageMan) {
		this.lastStageMan = lastStageMan;
	}

	public String getLastStateLoginMan() {
		return lastStateLoginMan;
	}

	public void setLastStateLoginMan(String lastStateLoginMan) {
		this.lastStateLoginMan = lastStateLoginMan;
	}

	public Date getLastStateTime() {
		return lastStateTime;
	}

	public void setLastStateTime(Date lastStateTime) {
		this.lastStateTime = lastStateTime;
	}

	public List<CheckItem> getCheckItems() {
		return checkItems;
	}

	public void setCheckItems(List<CheckItem> checkItems) {
		this.checkItems = checkItems;
	}

	public String getLastStateText() {
		return lastStateText;
	}

	public void setLastStateText(String lastStateText) {
		this.lastStateText = lastStateText;
	}

	public String getSpcSampleIds() {
		return spcSampleIds;
	}

	public void setSpcSampleIds(String spcSampleIds) {
		this.spcSampleIds = spcSampleIds;
	}

	public String getRecheckText() {
		return recheckText;
	}

	public void setRecheckText(String recheckText) {
		this.recheckText = recheckText;
	}

	public Date getRecheckTime() {
		return recheckTime;
	}

	public void setRecheckTime(Date recheckTime) {
		this.recheckTime = recheckTime;
	}

	public String getFunctionAmount() {
		return functionAmount;
	}

	public void setFunctionAmount(String functionAmount) {
		this.functionAmount = functionAmount;
	}

	public String getFunctionUnAmount() {
		return functionUnAmount;
	}

	public void setFunctionUnAmount(String functionUnAmount) {
		this.functionUnAmount = functionUnAmount;
	}

	public String getFunctionAmountRate() {
		return functionAmountRate;
	}

	public void setFunctionAmountRate(String functionAmountRate) {
		this.functionAmountRate = functionAmountRate;
	}

	public String getCheckAmount() {
		return checkAmount;
	}

	public void setCheckAmount(String checkAmount) {
		this.checkAmount = checkAmount;
	}

	public String getSizeAmount() {
		return sizeAmount;
	}

	public void setSizeAmount(String sizeAmount) {
		this.sizeAmount = sizeAmount;
	}

	public String getSizeUnAmount() {
		return sizeUnAmount;
	}

	public void setSizeUnAmount(String sizeUnAmount) {
		this.sizeUnAmount = sizeUnAmount;
	}

	public String getSizeAmountRate() {
		return sizeAmountRate;
	}

	public void setSizeAmountRate(String sizeAmountRate) {
		this.sizeAmountRate = sizeAmountRate;
	}

	public String getWgBadDescrible() {
		return wgBadDescrible;
	}

	public void setWgBadDescrible(String wgBadDescrible) {
		this.wgBadDescrible = wgBadDescrible;
	}

	public String getGnBadDescrible() {
		return gnBadDescrible;
	}

	public void setGnBadDescrible(String gnBadDescrible) {
		this.gnBadDescrible = gnBadDescrible;
	}

	public String getCcBadDescrible() {
		return ccBadDescrible;
	}

	public void setCcBadDescrible(String ccBadDescrible) {
		this.ccBadDescrible = ccBadDescrible;
	}

	public String getWgBadRate() {
		return wgBadRate;
	}
	
	public void setWgBadRate(String wgBadRate) {
		this.wgBadRate = wgBadRate;
	}

	public String getAppearanceInspection() {
		return appearanceInspection;
	}

	public void setAppearanceInspection(String appearanceInspection) {
		this.appearanceInspection = appearanceInspection;
	}

	public String getSamplingPlan() {
		return samplingPlan;
	}

	public void setSamplingPlan(String samplingPlan) {
		this.samplingPlan = samplingPlan;
	}

	public String getSamplingStandard1() {
		return samplingStandard1;
	}

	public void setSamplingStandard1(String samplingStandard1) {
		this.samplingStandard1 = samplingStandard1;
	}

	public String getSamplingStandard2() {
		return samplingStandard2;
	}

	public void setSamplingStandard2(String samplingStandard2) {
		this.samplingStandard2 = samplingStandard2;
	}

	public String getSamplingStandard3() {
		return samplingStandard3;
	}

	public void setSamplingStandard3(String samplingStandard3) {
		this.samplingStandard3 = samplingStandard3;
	}

	public String getSamplingStandard4() {
		return samplingStandard4;
	}

	public void setSamplingStandard4(String samplingStandard4) {
		this.samplingStandard4 = samplingStandard4;
	}

	public String getSamplingLevel() {
		return samplingLevel;
	}

	public void setSamplingLevel(String samplingLevel) {
		this.samplingLevel = samplingLevel;
	}

	public String getSamplingN1() {
		return samplingN1;
	}

	public void setSamplingN1(String samplingN1) {
		this.samplingN1 = samplingN1;
	}

	public String getSamplingN2() {
		return samplingN2;
	}

	public void setSamplingN2(String samplingN2) {
		this.samplingN2 = samplingN2;
	}

	public String getSamplingAc1() {
		return samplingAc1;
	}

	public void setSamplingAc1(String samplingAc1) {
		this.samplingAc1 = samplingAc1;
	}

	public String getSamplingAc2() {
		return samplingAc2;
	}

	public void setSamplingAc2(String samplingAc2) {
		this.samplingAc2 = samplingAc2;
	}

	public String getSamplingRe1() {
		return samplingRe1;
	}

	public void setSamplingRe1(String samplingRe1) {
		this.samplingRe1 = samplingRe1;
	}

	public String getSamplingRe2() {
		return samplingRe2;
	}

	public void setSamplingRe2(String samplingRe2) {
		this.samplingRe2 = samplingRe2;
	}

	public String getSamplingBadItem1() {
		return samplingBadItem1;
	}

	public void setSamplingBadItem1(String samplingBadItem1) {
		this.samplingBadItem1 = samplingBadItem1;
	}

	public String getSamplingBadItem2() {
		return samplingBadItem2;
	}

	public void setSamplingBadItem2(String samplingBadItem2) {
		this.samplingBadItem2 = samplingBadItem2;
	}

	public String getDetermine() {
		return determine;
	}

	public void setDetermine(String determine) {
		this.determine = determine;
	}

	public String getHsfCheckMethod() {
		return hsfCheckMethod;
	}

	public void setHsfCheckMethod(String hsfCheckMethod) {
		this.hsfCheckMethod = hsfCheckMethod;
	}

	public String getHsfFormNo() {
		return hsfFormNo;
	}

	public void setHsfFormNo(String hsfFormNo) {
		this.hsfFormNo = hsfFormNo;
	}

	public String getHsfDetermine() {
		return hsfDetermine;
	}

	public void setHsfDetermine(String hsfDetermine) {
		this.hsfDetermine = hsfDetermine;
	}

	public String getIsNewData() {
		return isNewData;
	}

	public void setIsNewData(String isNewData) {
		this.isNewData = isNewData;
	}

	public String getWgconclusion() {
		return wgconclusion;
	}


	public void setWgconclusion(String wgconclusion) {
		this.wgconclusion = wgconclusion;
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



	public String getHisReportId() {
		return hisReportId;
	}


	public void setHisReportId(String hisReportId) {
		this.hisReportId = hisReportId;
	}


	public String getOrdReportId() {
		return ordReportId;
	}


	public void setOrdReportId(String ordReportId) {
		this.ordReportId = ordReportId;
	}


	public String toString(){
		return "进货检验管理: 进货检验报告，ID"+this.getId()+"，检验日期"+this.inspectionNo+"，检验日期"+this.inspectionDate;
	}


	public String getSqeMrbReportNo() {
		return sqeMrbReportNo;
	}


	public void setSqeMrbReportNo(String sqeMrbReportNo) {
		this.sqeMrbReportNo = sqeMrbReportNo;
	}


	public String getReturnReportNo() {
		return returnReportNo;
	}


	public void setReturnReportNo(String returnReportNo) {
		this.returnReportNo = returnReportNo;
	}


	public String getCustomerCode() {
		return customerCode;
	}


	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}


	public String getCurrentAuditMan() {
		return currentAuditMan;
	}


	public void setCurrentAuditMan(String currentAuditMan) {
		this.currentAuditMan = currentAuditMan;
	}


	public String getBadRate1() {
		return badRate1;
	}


	public void setBadRate1(String badRate1) {
		this.badRate1 = badRate1;
	}


	public String getBadRate2() {
		return badRate2;
	}


	public void setBadRate2(String badRate2) {
		this.badRate2 = badRate2;
	}


	public String getIsDelete() {
		return isDelete;
	}


	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}


	public String getDeleteMan() {
		return deleteMan;
	}


	public void setDeleteMan(String deleteMan) {
		this.deleteMan = deleteMan;
	}


	public String getDeleteManLogin() {
		return deleteManLogin;
	}


	public void setDeleteManLogin(String deleteManLogin) {
		this.deleteManLogin = deleteManLogin;
	}


	public Date getDeleteDate() {
		return deleteDate;
	}


	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}


	public Float getQualifiedUnRate() {
		return qualifiedUnRate;
	}


	public void setQualifiedUnRate(Float qualifiedUnRate) {
		this.qualifiedUnRate = qualifiedUnRate;
	}


	public String getFunctionUnAmountRate() {
		return functionUnAmountRate;
	}


	public void setFunctionUnAmountRate(String functionUnAmountRate) {
		this.functionUnAmountRate = functionUnAmountRate;
	}


	public String getSizeUnAmountRate() {
		return sizeUnAmountRate;
	}


	public void setSizeUnAmountRate(String sizeUnAmountRate) {
		this.sizeUnAmountRate = sizeUnAmountRate;
	}

	public String getCurrentAuditManLogin() {
		return currentAuditManLogin;
	}


	public void setCurrentAuditManLogin(String currentAuditManLogin) {
		this.currentAuditManLogin = currentAuditManLogin;
	}


	public String getManagerNo() {
		return managerNo;
	}


	public void setManagerNo(String managerNo) {
		this.managerNo = managerNo;
	}


	public String getUncheckNo() {
		return uncheckNo;
	}


	public void setUncheckNo(String uncheckNo) {
		this.uncheckNo = uncheckNo;
	}


	public String getMaterialTypeCode() {
		return materialTypeCode;
	}


	public void setMaterialTypeCode(String materialTypeCode) {
		this.materialTypeCode = materialTypeCode;
	}


	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}


	public Date getSqeReplyTime() {
		return sqeReplyTime;
	}


	public void setSqeReplyTime(Date sqeReplyTime) {
		this.sqeReplyTime = sqeReplyTime;
	}


	public Date getSqeCompleteTime() {
		return sqeCompleteTime;
	}


	public void setSqeCompleteTime(Date sqeCompleteTime) {
		this.sqeCompleteTime = sqeCompleteTime;
	}


	public String getIsDueMail() {
		return isDueMail;
	}


	public void setIsDueMail(String isDueMail) {
		this.isDueMail = isDueMail;
	}


	public String getIsFactory() {
		return isFactory;
	}


	public void setIsFactory(String isFactory) {
		this.isFactory = isFactory;
	}



	
}

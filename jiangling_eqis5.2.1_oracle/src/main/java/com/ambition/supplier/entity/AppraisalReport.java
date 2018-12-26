package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.ambition.product.base.IdEntity;
import com.ambition.supplier.utils.DateUtil;
import com.norteksoft.wf.engine.client.ExtendField;
import com.norteksoft.wf.engine.client.FormFlowable;
import com.norteksoft.wf.engine.client.WorkflowInfo;

/**
 * 鉴定记录
 * @author 赵骏
 *
 */
@Entity
@Table(name = "SUPPLIER_APPRAISAL_REPORT")
public class AppraisalReport extends IdEntity implements FormFlowable{
	public static String STATE_DEFAULT = "未提交";
	public static String STATE_EVALUATE = "会签中";
	public static String STATE_WAITAUDIT = "审核中";
	public static String STATE_PASS = "同意";
	public static String STATE_FAIL = "不同意";
	public static String RESULT_PASS = "合格";
	public static String RESULT_FAIL = "不合格";
	private static final long serialVersionUID = -4311216062260502412L;
	private String code;//编号
	
	@ManyToOne
	@JoinColumn(name = "FK_SUPPLIER_ID")
	private Supplier supplier;
	private String type = TYPE_SAMPLE;//样品鉴定
	public static String TYPE_SAMPLE = "样品鉴定";
	public static String TYPE_SUBLOTS = "小批鉴定";
	
	private Integer timeOfPhase = 1;//第几次小批鉴定
	private Integer timeOfAppraisal = 1;//第几次样件鉴定
	@Temporal(TemporalType.DATE)
	private Date reportDate;//申请日期
	private String reportMan;//申请人员名
	private String reportManLoginName;//申请人员登录名
	private String materialType;//物料类别
	private String importance;
	private String bomNames;//鉴定的物料名称
	private String bomCodes;//鉴定的物料编码
	private String bomModel;//物料规格
//	private String importance;//重要度
//	private String bomLevel;//零件等级
	private String applyBomCodes;//适用产品型号编码
	private String applyBomNames;//适用产品型号名称
	private String historySupplierCode;//原供应商编码
	private String historySupplierName;//原供应商名称
	private String supplierProperty;//试用供应商性质
	private String applyAuditMan;//试用申请审批人员
	private String applyAuditLoginName;//试用申请审批登录名
	private String applyReason;//试用申请原因
	@Temporal(TemporalType.DATE)
	private Date sendDate;//送样日期
	private String supplyNumber;//供货数量
	@Temporal(TemporalType.DATE)
	private Date wantFinishDate;//希望完成日期
	@Temporal(TemporalType.DATE)
	private Date predictFinishDate;//预计完成日期
	@Temporal(TemporalType.DATE)
	private Date realFinishDate;//实际完成日期
	private String receiveMan;//接收人员
	private String isNew = "是";//是否新品
	private String appraisalDescribe;//试用结果描述
	private String appraisalConclusion;//鉴定结论/试用结果建议
	private String testMan;//试用人员
	private String testManLoginName;//试用人员登录名
	private String testManGroup;//试用班组名
	private String appointTestMan;//指定的试用人员
	private String appointTestLoginName;//指定的试用人员登录名
	private String qualityCountersigns;//申请时质保会签人员
	private String resultAuditMan;//结果审核人员
	private String resultApproveMan;//结果批准人员/质量副总
	private String qualityAuditMan;//质保部经理确认
	private String customerOpinion;//客户代表意见
	private String productDescribe;//生产部试装结果简述
	private String applyProductDescribe;//申请时生产部主管意见
	private String applyProductOpinion;//申请请时生产部主管审核
	private String productResponsibleOpinion;//生产部主管确认
	private String productManagerOpinion;//生产经理意见
	private String qualityResponsibleOpinion;//质保主管意见
	private String qualityManagerOpinion;//质保经理意见 
	private String priceInfo;//价格份额
	private String priceOpinion;//价格工程师意见
	private String materialsManagerOpinion;//物流部经理审核
	private String topManagerOpinion;//总经理审核意见
	private String financeOpinion;//财务部审核归档
	
	@Temporal(TemporalType.DATE)
	private Date rectificationDate;//整改期限
	private String rectificationRequest;//整改要求
	private String evaluationMembers;//评价小组成员
	private String evaluationMemberLoginNames;//评价小组成员登录名
	private String auditMan;//审核人
	private String auditManLoginName;//审核人员登录名
	private String approveMan;//申请批准
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date auditDate;//审核时间
	private String appraisalState = STATE_DEFAULT;//鉴定中
	private String appraisalResult=RESULT_FAIL;//考察结果//通过或不通过
	private Boolean isImprovement=false;//是否改进
	private String attachmentFiles;//附件
	private String admittance;//临时的准入状态
	private String isSampleQualified="是";//样件是否确认合格
	private String isInspectionQualified="是";//此批试装物料是否检验合格
	private Boolean isConcessionPass=false;//是否让步通过
	private String concessionPassMan;//让步通过人员
	@Embedded
	private WorkflowInfo workflowInfo;

	@Embedded
	private ExtendField extendField;

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



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public Integer getTimeOfPhase() {
		return timeOfPhase;
	}



	public void setTimeOfPhase(Integer timeOfPhase) {
		this.timeOfPhase = timeOfPhase;
	}



	public Integer getTimeOfAppraisal() {
		return timeOfAppraisal;
	}



	public void setTimeOfAppraisal(Integer timeOfAppraisal) {
		this.timeOfAppraisal = timeOfAppraisal;
	}



	public Date getReportDate() {
		return reportDate;
	}
	@Transient
	public String getReportDateStr() {
		return DateUtil.formateDateStr(reportDate);
	}


	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}



	public String getReportMan() {
		return reportMan;
	}



	public void setReportMan(String reportMan) {
		this.reportMan = reportMan;
	}



	public String getReportManLoginName() {
		return reportManLoginName;
	}



	public void setReportManLoginName(String reportManLoginName) {
		this.reportManLoginName = reportManLoginName;
	}



	public String getMaterialType() {
		return materialType;
	}



	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}



	public String getBomNames() {
		return bomNames;
	}



	public void setBomNames(String bomNames) {
		this.bomNames = bomNames;
	}



	public String getBomCodes() {
		return bomCodes;
	}



	public void setBomCodes(String bomCodes) {
		this.bomCodes = bomCodes;
	}



	public String getBomModel() {
		return bomModel;
	}



	public void setBomModel(String bomModel) {
		this.bomModel = bomModel;
	}



	public String getApplyBomCodes() {
		return applyBomCodes;
	}



	public void setApplyBomCodes(String applyBomCodes) {
		this.applyBomCodes = applyBomCodes;
	}



	public String getApplyBomNames() {
		return applyBomNames;
	}



	public void setApplyBomNames(String applyBomNames) {
		this.applyBomNames = applyBomNames;
	}



	public String getHistorySupplierCode() {
		return historySupplierCode;
	}



	public void setHistorySupplierCode(String historySupplierCode) {
		this.historySupplierCode = historySupplierCode;
	}



	public String getHistorySupplierName() {
		return historySupplierName;
	}



	public void setHistorySupplierName(String historySupplierName) {
		this.historySupplierName = historySupplierName;
	}



	public String getSupplierProperty() {
		return supplierProperty;
	}



	public void setSupplierProperty(String supplierProperty) {
		this.supplierProperty = supplierProperty;
	}



	public String getApplyAuditMan() {
		return applyAuditMan;
	}



	public void setApplyAuditMan(String applyAuditMan) {
		this.applyAuditMan = applyAuditMan;
	}



	public String getApplyAuditLoginName() {
		return applyAuditLoginName;
	}



	public void setApplyAuditLoginName(String applyAuditLoginName) {
		this.applyAuditLoginName = applyAuditLoginName;
	}



	public Date getSendDate() {
		return sendDate;
	}
	@Transient
	public String getSendDateStr() {
		return DateUtil.formateDateStr(sendDate);
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}



	public String getSupplyNumber() {
		return supplyNumber;
	}



	public void setSupplyNumber(String supplyNumber) {
		this.supplyNumber = supplyNumber;
	}



	public Date getWantFinishDate() {
		return wantFinishDate;
	}

	@Transient
	public String getWantFinishDateStr() {
		return DateUtil.formateDateStr(wantFinishDate);
	}

	public void setWantFinishDate(Date wantFinishDate) {
		this.wantFinishDate = wantFinishDate;
	}



	public Date getPredictFinishDate() {
		return predictFinishDate;
	}

	@Transient
	public String getPredictFinishDateStr() {
		return DateUtil.formateDateStr(predictFinishDate);
	}

	public void setPredictFinishDate(Date predictFinishDate) {
		this.predictFinishDate = predictFinishDate;
	}



	public Date getRealFinishDate() {
		return realFinishDate;
	}

	@Transient
	public String getRealFinishDateStr() {
		return DateUtil.formateDateStr(realFinishDate);
	}

	public void setRealFinishDate(Date realFinishDate) {
		this.realFinishDate = realFinishDate;
	}



	public String getReceiveMan() {
		return receiveMan;
	}



	public void setReceiveMan(String receiveMan) {
		this.receiveMan = receiveMan;
	}



	public String getAppraisalDescribe() {
		return appraisalDescribe;
	}



	public void setAppraisalDescribe(String appraisalDescribe) {
		this.appraisalDescribe = appraisalDescribe;
	}



	public String getAppraisalConclusion() {
		return appraisalConclusion;
	}



	public void setAppraisalConclusion(String appraisalConclusion) {
		this.appraisalConclusion = appraisalConclusion;
	}



	public String getTestMan() {
		return testMan;
	}



	public void setTestMan(String testMan) {
		this.testMan = testMan;
	}



	public String getTestManLoginName() {
		return testManLoginName;
	}



	public void setTestManLoginName(String testManLoginName) {
		this.testManLoginName = testManLoginName;
	}



	public Date getRectificationDate() {
		return rectificationDate;
	}



	public void setRectificationDate(Date rectificationDate) {
		this.rectificationDate = rectificationDate;
	}



	public String getRectificationRequest() {
		return rectificationRequest;
	}



	public void setRectificationRequest(String rectificationRequest) {
		this.rectificationRequest = rectificationRequest;
	}



	public String getEvaluationMembers() {
		return evaluationMembers;
	}



	public void setEvaluationMembers(String evaluationMembers) {
		this.evaluationMembers = evaluationMembers;
	}



	public String getEvaluationMemberLoginNames() {
		return evaluationMemberLoginNames;
	}



	public void setEvaluationMemberLoginNames(String evaluationMemberLoginNames) {
		this.evaluationMemberLoginNames = evaluationMemberLoginNames;
	}



	public String getAuditMan() {
		return auditMan;
	}



	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}



	public String getAuditManLoginName() {
		return auditManLoginName;
	}



	public void setAuditManLoginName(String auditManLoginName) {
		this.auditManLoginName = auditManLoginName;
	}



	public Date getAuditDate() {
		return auditDate;
	}



	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}



	public String getAppraisalState() {
		return appraisalState;
	}



	public void setAppraisalState(String appraisalState) {
		this.appraisalState = appraisalState;
	}



	public String getAppraisalResult() {
		return appraisalResult;
	}



	public void setAppraisalResult(String appraisalResult) {
		this.appraisalResult = appraisalResult;
	}



	public Boolean getIsImprovement() {
		return isImprovement;
	}



	public void setIsImprovement(Boolean isImprovement) {
		this.isImprovement = isImprovement;
	}



	public String getAttachmentFiles() {
		return attachmentFiles;
	}



	public void setAttachmentFiles(String attachmentFiles) {
		this.attachmentFiles = attachmentFiles;
	}



	public String getAdmittance() {
		return admittance;
	}



	public void setAdmittance(String admittance) {
		this.admittance = admittance;
	}



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

	public String getApplyReason() {
		return applyReason;
	}

	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	public String getIsSampleQualified() {
		return isSampleQualified;
	}



	public void setIsSampleQualified(String isSampleQualified) {
		this.isSampleQualified = isSampleQualified;
	}



	public String getIsInspectionQualified() {
		return isInspectionQualified;
	}



	public void setIsInspectionQualified(String isInspectionQualified) {
		this.isInspectionQualified = isInspectionQualified;
	}

	public Boolean getIsConcessionPass() {
		return isConcessionPass;
	}

	public void setIsConcessionPass(Boolean isConcessionPass) {
		this.isConcessionPass = isConcessionPass;
	}



	public String getConcessionPassMan() {
		return concessionPassMan;
	}

	public void setConcessionPassMan(String concessionPassMan) {
		this.concessionPassMan = concessionPassMan;
	}

	public String getTestManGroup() {
		return testManGroup;
	}

	public void setTestManGroup(String testManGroup) {
		this.testManGroup = testManGroup;
	}

	public String getImportance() {
		return importance;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}
	
	public String getAppointTestMan() {
		return appointTestMan;
	}

	public void setAppointTestMan(String appointTestMan) {
		this.appointTestMan = appointTestMan;
	}

	public String getAppointTestLoginName() {
		return appointTestLoginName;
	}

	public void setAppointTestLoginName(String appointTestLoginName) {
		this.appointTestLoginName = appointTestLoginName;
	}

	public String getQualityCountersigns() {
		return qualityCountersigns;
	}



	public void setQualityCountersigns(String qualityCountersigns) {
		this.qualityCountersigns = qualityCountersigns;
	}



	public String getResultAuditMan() {
		return resultAuditMan;
	}



	public void setResultAuditMan(String resultAuditMan) {
		this.resultAuditMan = resultAuditMan;
	}



	public String getResultApproveMan() {
		return resultApproveMan;
	}



	public void setResultApproveMan(String resultApproveMan) {
		this.resultApproveMan = resultApproveMan;
	}

	public String getQualityAuditMan() {
		return qualityAuditMan;
	}

	public void setQualityAuditMan(String qualityAuditMan) {
		this.qualityAuditMan = qualityAuditMan;
	}



	public String getCustomerOpinion() {
		return customerOpinion;
	}



	public void setCustomerOpinion(String customerOpinion) {
		this.customerOpinion = customerOpinion;
	}



	public String getProductDescribe() {
		return productDescribe;
	}



	public void setProductDescribe(String productDescribe) {
		this.productDescribe = productDescribe;
	}



	public String getProductResponsibleOpinion() {
		return productResponsibleOpinion;
	}



	public void setProductResponsibleOpinion(String productResponsibleOpinion) {
		this.productResponsibleOpinion = productResponsibleOpinion;
	}



	public String getProductManagerOpinion() {
		return productManagerOpinion;
	}



	public void setProductManagerOpinion(String productManagerOpinion) {
		this.productManagerOpinion = productManagerOpinion;
	}



	public String getQualityResponsibleOpinion() {
		return qualityResponsibleOpinion;
	}



	public void setQualityResponsibleOpinion(String qualityResponsibleOpinion) {
		this.qualityResponsibleOpinion = qualityResponsibleOpinion;
	}



	public String getQualityManagerOpinion() {
		return qualityManagerOpinion;
	}



	public void setQualityManagerOpinion(String qualityManagerOpinion) {
		this.qualityManagerOpinion = qualityManagerOpinion;
	}



	public String getPriceInfo() {
		return priceInfo;
	}



	public void setPriceInfo(String priceInfo) {
		this.priceInfo = priceInfo;
	}



	public String getMaterialsManagerOpinion() {
		return materialsManagerOpinion;
	}



	public void setMaterialsManagerOpinion(String materialsManagerOpinion) {
		this.materialsManagerOpinion = materialsManagerOpinion;
	}



	public String getTopManagerOpinion() {
		return topManagerOpinion;
	}



	public void setTopManagerOpinion(String topManagerOpinion) {
		this.topManagerOpinion = topManagerOpinion;
	}



	public String getFinanceOpinion() {
		return financeOpinion;
	}



	public void setFinanceOpinion(String financeOpinion) {
		this.financeOpinion = financeOpinion;
	}

	public String getApplyProductDescribe() {
		return applyProductDescribe;
	}



	public void setApplyProductDescribe(String applyProductDescribe) {
		this.applyProductDescribe = applyProductDescribe;
	}



	public String getApplyProductOpinion() {
		return applyProductOpinion;
	}



	public void setApplyProductOpinion(String applyProductOpinion) {
		this.applyProductOpinion = applyProductOpinion;
	}



	public String getApproveMan() {
		return approveMan;
	}



	public void setApproveMan(String approveMan) {
		this.approveMan = approveMan;
	}

	public String getPriceOpinion() {
		return priceOpinion;
	}

	public void setPriceOpinion(String priceOpinion) {
		this.priceOpinion = priceOpinion;
	}



	public String toString(){
		return "供应商质量管理：供应商准入，鉴定ID"+this.getId()+",鉴定日期"+this.reportDate+",鉴定方式"+this.type;
	}
}

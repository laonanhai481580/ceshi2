package com.ambition.si.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.norteksoft.wf.engine.client.ExtendField;
import com.norteksoft.wf.engine.client.FormFlowable;
import com.norteksoft.wf.engine.client.WorkflowInfo;

/**    
 * 供应商检验检验报告
 * @authorBy lpf
 *
 */
@Entity
@Table(name = "SI_CHECK_INSPECTION_REPORT")
public class SiCheckInspectionReport extends IdEntity implements FormFlowable {
	private static final long serialVersionUID = 1L;
	private String formNo;//报告编号
	@Temporal(TemporalType.TIMESTAMP)
	private Date inspectionDate; // 检验日期
	private String inspector; //检验员
	private String customer;//客户
	private String customerModel;//客户型号
	private String machineType;//机型
	private String batchNo;//批次号
	private String processSection; // 制程区段
	private Integer stockAmount; // 投入数
	private Integer inspectionAmount; // 检验数
	private Integer qualifiedAmount; // 合格数
	private Integer unqualifiedAmount; // 不良数
	private Integer appearanceInspectionAmount;//外观检验数
	private String appearanceMan;//外观检验员
	private String appearanceManLogin;//外观检验员登录名
	private Integer appearanceAmount;//外观合格数
	private Integer appearanceUnAmount;//外观不合格数
	private Float appearanceAmountRate;//外观不良率
	private String appearanceConclusion;//外观结果
	private Integer sizeInspectionAmount;//尺寸检验数
	private String sizeMan;//尺寸检验员
	private String sizeManLogin;//尺寸检验员登录名
	private Integer sizeAmount;//尺寸合格数
	private Integer sizeUnAmount;//尺寸不合格数
	private Float sizeAmountRate;//尺寸不良率
	private String sizeConclusion;//尺寸结果
	private Integer functionInspectionAmount;//功能检验数
	private String functionMan;//功能检验员
	private String functionManLogin;//功能检验员登录名
	private Integer functionAmount;//功能合格数
	private Integer functionUnAmount;//功能不合格数
	private Float functionAmountRate;//功能不良率
	private String functionConclusion;//功能结果
	private Integer stockLotAmount; // 投入Lot数
	private Integer inspectionLotAmount; // 检验Lot数
	private Integer passLotAmount; // pass Lot数
	private Integer rejectLotAmount; // reject Lot数	
	private String inspectionConclusion; // 检验结果
	private Float lrrRate; // 批退率
	private String attachmentFiles;//附件
	
	@OneToMany(mappedBy="siCheckInspectionReport",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	private List<SiCheckItem> checkItems;//检查项目	
	
	
	@Embedded
	private ExtendField extendField;

	@Embedded
	private WorkflowInfo workflowInfo;

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getMachineType() {
		return machineType;
	}

	public void setMachineType(String machineType) {
		this.machineType = machineType;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	@Override
	public WorkflowInfo getWorkflowInfo() {
		return workflowInfo;
	}

	@Override
	public void setWorkflowInfo(WorkflowInfo workflowInfo) {
		this.workflowInfo = workflowInfo;
	}

	public String getFormNo() {
		return formNo;
	}

	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}

	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public String getInspector() {
		return inspector;
	}

	public void setInspector(String inspector) {
		this.inspector = inspector;
	}

	public String getProcessSection() {
		return processSection;
	}

	public void setProcessSection(String processSection) {
		this.processSection = processSection;
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

	public Integer getAppearanceAmount() {
		return appearanceAmount;
	}

	public void setAppearanceAmount(Integer appearanceAmount) {
		this.appearanceAmount = appearanceAmount;
	}

	public Integer getAppearanceUnAmount() {
		return appearanceUnAmount;
	}

	public void setAppearanceUnAmount(Integer appearanceUnAmount) {
		this.appearanceUnAmount = appearanceUnAmount;
	}

	public Integer getSizeAmount() {
		return sizeAmount;
	}

	public void setSizeAmount(Integer sizeAmount) {
		this.sizeAmount = sizeAmount;
	}

	public Integer getSizeUnAmount() {
		return sizeUnAmount;
	}

	public void setSizeUnAmount(Integer sizeUnAmount) {
		this.sizeUnAmount = sizeUnAmount;
	}

	public Integer getFunctionAmount() {
		return functionAmount;
	}

	public void setFunctionAmount(Integer functionAmount) {
		this.functionAmount = functionAmount;
	}

	public Integer getFunctionUnAmount() {
		return functionUnAmount;
	}

	public void setFunctionUnAmount(Integer functionUnAmount) {
		this.functionUnAmount = functionUnAmount;
	}

	public Integer getStockLotAmount() {
		return stockLotAmount;
	}

	public void setStockLotAmount(Integer stockLotAmount) {
		this.stockLotAmount = stockLotAmount;
	}

	public Integer getInspectionLotAmount() {
		return inspectionLotAmount;
	}

	public void setInspectionLotAmount(Integer inspectionLotAmount) {
		this.inspectionLotAmount = inspectionLotAmount;
	}

	public Integer getPassLotAmount() {
		return passLotAmount;
	}

	public void setPassLotAmount(Integer passLotAmount) {
		this.passLotAmount = passLotAmount;
	}

	public Integer getRejectLotAmount() {
		return rejectLotAmount;
	}

	public void setRejectLotAmount(Integer rejectLotAmount) {
		this.rejectLotAmount = rejectLotAmount;
	}

	public String getInspectionConclusion() {
		return inspectionConclusion;
	}

	public void setInspectionConclusion(String inspectionConclusion) {
		this.inspectionConclusion = inspectionConclusion;
	}

	public Float getLrrRate() {
		return lrrRate;
	}

	public void setLrrRate(Float lrrRate) {
		this.lrrRate = lrrRate;
	}

	public String getAttachmentFiles() {
		return attachmentFiles;
	}

	public void setAttachmentFiles(String attachmentFiles) {
		this.attachmentFiles = attachmentFiles;
	}

	public List<SiCheckItem> getCheckItems() {
		return checkItems;
	}

	public void setCheckItems(List<SiCheckItem> checkItems) {
		this.checkItems = checkItems;
	}

	public ExtendField getExtendField() {
		return extendField;
	}

	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}

	public Float getAppearanceAmountRate() {
		return appearanceAmountRate;
	}

	public void setAppearanceAmountRate(Float appearanceAmountRate) {
		this.appearanceAmountRate = appearanceAmountRate;
	}

	public Float getSizeAmountRate() {
		return sizeAmountRate;
	}

	public void setSizeAmountRate(Float sizeAmountRate) {
		this.sizeAmountRate = sizeAmountRate;
	}

	public Float getFunctionAmountRate() {
		return functionAmountRate;
	}

	public void setFunctionAmountRate(Float functionAmountRate) {
		this.functionAmountRate = functionAmountRate;
	}

	public String getAppearanceConclusion() {
		return appearanceConclusion;
	}

	public void setAppearanceConclusion(String appearanceConclusion) {
		this.appearanceConclusion = appearanceConclusion;
	}

	public String getSizeConclusion() {
		return sizeConclusion;
	}

	public void setSizeConclusion(String sizeConclusion) {
		this.sizeConclusion = sizeConclusion;
	}

	public String getFunctionConclusion() {
		return functionConclusion;
	}

	public void setFunctionConclusion(String functionConclusion) {
		this.functionConclusion = functionConclusion;
	}

	public Integer getAppearanceInspectionAmount() {
		return appearanceInspectionAmount;
	}

	public void setAppearanceInspectionAmount(Integer appearanceInspectionAmount) {
		this.appearanceInspectionAmount = appearanceInspectionAmount;
	}

	public String getAppearanceMan() {
		return appearanceMan;
	}

	public void setAppearanceMan(String appearanceMan) {
		this.appearanceMan = appearanceMan;
	}

	public String getAppearanceManLogin() {
		return appearanceManLogin;
	}

	public void setAppearanceManLogin(String appearanceManLogin) {
		this.appearanceManLogin = appearanceManLogin;
	}

	public Integer getSizeInspectionAmount() {
		return sizeInspectionAmount;
	}

	public void setSizeInspectionAmount(Integer sizeInspectionAmount) {
		this.sizeInspectionAmount = sizeInspectionAmount;
	}

	public String getSizeMan() {
		return sizeMan;
	}

	public void setSizeMan(String sizeMan) {
		this.sizeMan = sizeMan;
	}

	public String getSizeManLogin() {
		return sizeManLogin;
	}

	public void setSizeManLogin(String sizeManLogin) {
		this.sizeManLogin = sizeManLogin;
	}

	public Integer getFunctionInspectionAmount() {
		return functionInspectionAmount;
	}

	public void setFunctionInspectionAmount(Integer functionInspectionAmount) {
		this.functionInspectionAmount = functionInspectionAmount;
	}

	public String getFunctionMan() {
		return functionMan;
	}

	public void setFunctionMan(String functionMan) {
		this.functionMan = functionMan;
	}

	public String getFunctionManLogin() {
		return functionManLogin;
	}

	public void setFunctionManLogin(String functionManLogin) {
		this.functionManLogin = functionManLogin;
	}

	public String getCustomerModel() {
		return customerModel;
	}

	public void setCustomerModel(String customerModel) {
		this.customerModel = customerModel;
	}

	
}

package com.ambition.aftersales.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:FAR解析
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月24日 发布
 */
@Entity
@Table(name = "AFS_FAR_ANALYSIS")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class FarAnalysis extends WorkflowIdEntity{

	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "AFS_FAR_ANALYSIS";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "FAR解析单";//实体_列表_名称
	private String formNo;//表单编号
	private Date happenDate;//发生日期
	private Date confirmDate;//确认日期
	private String workingProcedure;//工序
	private String ofilmModel;//欧菲机型
	private String customerName;//客户名称
	private String customerModel;//客户机型
	private String csMan;//cs负责人
	private String csManLogin;//cs负责人登录名
	private String productionEnterpriseGroup;//生产事业群
	private String businessUnit;//事业部
	private String courierCompany;//快递公司
	private String courierNumber;//快递单号
	private Date sendDate;//寄件日期
	private String receiver;//接受者
	private String receiverLogin;//接受者登录名
	private Date receiptDate;//接收日期
	private String transferMan;//转交人
	private String transferManLogin;//转交人登录名
	private Date transferDate;//转交日期
	//识别问题根因
	private String method;//方法
	private String reason;//原因分类
	private String department;//责任部门
	private String remark1;//
	private String attachment1;//
	//制定纠正措施
	private String remark2;//
	private String attachment2;//
	//回传客户接收状况
	private Date replyDate;//回复日期
	private String closeState;//关闭状态
	private String closeMan;//关闭确认人
	private String closeManLogin;//关闭确认人登录名
	private String remark3;//
	private String attachment3;//
	private Date closeDate;//结案日期
	
	@OneToMany(mappedBy = "farAnalysis",cascade={CascadeType.ALL})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	List<FarAnalysisItem> farAnalysisItems;//不良现象

	public String getFormNo() {
		return formNo;
	}

	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}

	public Date getHappenDate() {
		return happenDate;
	}

	public void setHappenDate(Date happenDate) {
		this.happenDate = happenDate;
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getWorkingProcedure() {
		return workingProcedure;
	}

	public void setWorkingProcedure(String workingProcedure) {
		this.workingProcedure = workingProcedure;
	}

	public String getOfilmModel() {
		return ofilmModel;
	}

	public void setOfilmModel(String ofilmModel) {
		this.ofilmModel = ofilmModel;
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

	public String getCsMan() {
		return csMan;
	}

	public void setCsMan(String csMan) {
		this.csMan = csMan;
	}

	public String getCsManLogin() {
		return csManLogin;
	}

	public void setCsManLogin(String csManLogin) {
		this.csManLogin = csManLogin;
	}

	public String getProductionEnterpriseGroup() {
		return productionEnterpriseGroup;
	}

	public void setProductionEnterpriseGroup(String productionEnterpriseGroup) {
		this.productionEnterpriseGroup = productionEnterpriseGroup;
	}


	public String getCourierCompany() {
		return courierCompany;
	}

	public void setCourierCompany(String courierCompany) {
		this.courierCompany = courierCompany;
	}

	public String getCourierNumber() {
		return courierNumber;
	}

	public void setCourierNumber(String courierNumber) {
		this.courierNumber = courierNumber;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiverLogin() {
		return receiverLogin;
	}

	public void setReceiverLogin(String receiverLogin) {
		this.receiverLogin = receiverLogin;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public String getTransferMan() {
		return transferMan;
	}

	public void setTransferMan(String transferMan) {
		this.transferMan = transferMan;
	}

	public String getTransferManLogin() {
		return transferManLogin;
	}

	public void setTransferManLogin(String transferManLogin) {
		this.transferManLogin = transferManLogin;
	}

	public Date getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getAttachment1() {
		return attachment1;
	}

	public void setAttachment1(String attachment1) {
		this.attachment1 = attachment1;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getAttachment2() {
		return attachment2;
	}

	public void setAttachment2(String attachment2) {
		this.attachment2 = attachment2;
	}

	public Date getReplyDate() {
		return replyDate;
	}

	public void setReplyDate(Date replyDate) {
		this.replyDate = replyDate;
	}

	public String getCloseState() {
		return closeState;
	}

	public void setCloseState(String closeState) {
		this.closeState = closeState;
	}

	public String getCloseMan() {
		return closeMan;
	}

	public void setCloseMan(String closeMan) {
		this.closeMan = closeMan;
	}

	public String getCloseManLogin() {
		return closeManLogin;
	}

	public void setCloseManLogin(String closeManLogin) {
		this.closeManLogin = closeManLogin;
	}

	public String getRemark3() {
		return remark3;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

	public String getAttachment3() {
		return attachment3;
	}

	public void setAttachment3(String attachment3) {
		this.attachment3 = attachment3;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public List<FarAnalysisItem> getFarAnalysisItems() {
		return farAnalysisItems;
	}

	public void setFarAnalysisItems(List<FarAnalysisItem> farAnalysisItems) {
		this.farAnalysisItems = farAnalysisItems;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

}

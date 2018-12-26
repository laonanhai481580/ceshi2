package com.ambition.improve.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:8D改进报告
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月29日 发布
 */
@Entity
@Table(name = "IMP_8D_REPORT")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class ImproveReport extends WorkflowIdEntity{

	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "IMP_8D_REPORT";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "8D改进报告";//实体_列表_名称
	private String formNo;//表单编号
	private String processSection; // 制程区段
	private String model;//机种
	private Date happenDate;//发生日期
	private String sponsor;//发起人
	private String productModel;//产品型号
	private String customerName;//客户名称
	private String hanppenArea;//发生区
	private String productPhase;//产品阶段
	private String problemBelong;//问题归属
	private String lotNo;//lot no
	private String problemSource;//问题来源
	private String problemType;//问题类型
	private String problemDegree;//问题严重度
	private String problemPoint;//问题点
	private String remark1;//问题点说明
	private String unqualifiedRate;//不良率
	private String productionEnterpriseGroup;//生产事业群
	
	//D2
	@Column(length=1000)
	private String problemDescrible;//问题描述
	private String dutyManD2;//D2负责人
	private String dutyManD2Login;//D2负责人登录名
	private String dutyManD2Manager;//D2负责人主管
	private String dutyManD2ManagerLogin;//D2负责人主管登录名
	private String deprtmentD2;//D2部门
	private Date planDateD2;//D2预计完成时间
	
		
	//D3
	private Integer clientStore;//客户端库存
	private Integer onOrder;//在途数量
	private Integer innerStore;//内部成品仓库库存数量
	private Integer innerOnOrder;//内部在线数量
	private Integer rmaStore;//RMA库存数量
	private Integer materialStore;//原材料仓
	@Column(length=1000)
	private String emergencyMeasures;//应急措施
	
	//D4
	private String dutyManD4;//D4负责人
	private String dutyManD4Login;//D4负责人登录名
	private String deprtmentD4;//D4部门
	private Date planDateD4;//D4预计完成时间
	private String method;//方法
	private String reason;//原因分类
	private String dutyDept;//责任部门
	@Column(length=3000)
	private String remarkD4;//D4意见
	private String attachmentD4;//D4附件
	
	//D5
	@Column(length=3000)
	private String remarkD5;//D5意见
	private String attachmentD5;//D5附件
	
	//D6
	private String dutyManD6;//D6负责人
	private String dutyManD6Login;//D6负责人登录名
	private String deprtmentD6;//D6部门
	private Date planDateD6;//D6预计完成时间
	@Column(length=3000)
	private String remarkD6;//D6意见
	private String attachmentD6;//D6附件
	
	//D7
	@Column(length=3000)
	private String remarkD7;//D7意见
	private String attachmentD7;//D7附件aw
	
	//D8
	private Date confirmDate;//确认日期
	private String closeState;//关闭状态
	private String closeMan;//关闭确认人
	private String closeManLogin;//关闭确认人登录名
	private Date closeDate;//结案日期	
	@Column(length=3000)
	private String remarkD8;//D8意见
	private String attachmentD8;//D8附件
	
	@OneToMany(mappedBy = "improveReport",cascade={CascadeType.ALL})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	List<ImproveReportTeam> improveReportTeams;//建立团队

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

	public String getSponsor() {
		return sponsor;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getHanppenArea() {
		return hanppenArea;
	}

	public void setHanppenArea(String hanppenArea) {
		this.hanppenArea = hanppenArea;
	}

	public String getProductPhase() {
		return productPhase;
	}

	public void setProductPhase(String productPhase) {
		this.productPhase = productPhase;
	}

	public String getProblemBelong() {
		return problemBelong;
	}

	public void setProblemBelong(String problemBelong) {
		this.problemBelong = problemBelong;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getProblemSource() {
		return problemSource;
	}

	public void setProblemSource(String problemSource) {
		this.problemSource = problemSource;
	}

	public String getProblemType() {
		return problemType;
	}

	public void setProblemType(String problemType) {
		this.problemType = problemType;
	}

	public String getProblemDegree() {
		return problemDegree;
	}

	public void setProblemDegree(String problemDegree) {
		this.problemDegree = problemDegree;
	}

	public String getProblemPoint() {
		return problemPoint;
	}

	public void setProblemPoint(String problemPoint) {
		this.problemPoint = problemPoint;
	}

	public String getUnqualifiedRate() {
		return unqualifiedRate;
	}

	public void setUnqualifiedRate(String unqualifiedRate) {
		this.unqualifiedRate = unqualifiedRate;
	}

	public String getProductionEnterpriseGroup() {
		return productionEnterpriseGroup;
	}

	public void setProductionEnterpriseGroup(String productionEnterpriseGroup) {
		this.productionEnterpriseGroup = productionEnterpriseGroup;
	}

	public String getProblemDescrible() {
		return problemDescrible;
	}

	public void setProblemDescrible(String problemDescrible) {
		this.problemDescrible = problemDescrible;
	}

	public String getDutyManD2() {
		return dutyManD2;
	}

	public void setDutyManD2(String dutyManD2) {
		this.dutyManD2 = dutyManD2;
	}

	public String getDutyManD2Login() {
		return dutyManD2Login;
	}

	public void setDutyManD2Login(String dutyManD2Login) {
		this.dutyManD2Login = dutyManD2Login;
	}

	public String getDeprtmentD2() {
		return deprtmentD2;
	}

	public void setDeprtmentD2(String deprtmentD2) {
		this.deprtmentD2 = deprtmentD2;
	}

	public Date getPlanDateD2() {
		return planDateD2;
	}

	public void setPlanDateD2(Date planDateD2) {
		this.planDateD2 = planDateD2;
	}

	public Integer getClientStore() {
		return clientStore;
	}

	public void setClientStore(Integer clientStore) {
		this.clientStore = clientStore;
	}

	public Integer getOnOrder() {
		return onOrder;
	}

	public void setOnOrder(Integer onOrder) {
		this.onOrder = onOrder;
	}

	public Integer getInnerStore() {
		return innerStore;
	}

	public void setInnerStore(Integer innerStore) {
		this.innerStore = innerStore;
	}

	public Integer getInnerOnOrder() {
		return innerOnOrder;
	}

	public void setInnerOnOrder(Integer innerOnOrder) {
		this.innerOnOrder = innerOnOrder;
	}

	public Integer getRmaStore() {
		return rmaStore;
	}

	public void setRmaStore(Integer rmaStore) {
		this.rmaStore = rmaStore;
	}

	public Integer getMaterialStore() {
		return materialStore;
	}

	public void setMaterialStore(Integer materialStore) {
		this.materialStore = materialStore;
	}

	public String getEmergencyMeasures() {
		return emergencyMeasures;
	}

	public void setEmergencyMeasures(String emergencyMeasures) {
		this.emergencyMeasures = emergencyMeasures;
	}

	public String getDutyManD4() {
		return dutyManD4;
	}

	public void setDutyManD4(String dutyManD4) {
		this.dutyManD4 = dutyManD4;
	}

	public String getDutyManD4Login() {
		return dutyManD4Login;
	}

	public void setDutyManD4Login(String dutyManD4Login) {
		this.dutyManD4Login = dutyManD4Login;
	}

	public String getDeprtmentD4() {
		return deprtmentD4;
	}

	public void setDeprtmentD4(String deprtmentD4) {
		this.deprtmentD4 = deprtmentD4;
	}

	public Date getPlanDateD4() {
		return planDateD4;
	}

	public void setPlanDateD4(Date planDateD4) {
		this.planDateD4 = planDateD4;
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




	public String getDutyDept() {
		return dutyDept;
	}

	public void setDutyDept(String dutyDept) {
		this.dutyDept = dutyDept;
	}

	public String getRemarkD4() {
		return remarkD4;
	}

	public void setRemarkD4(String remarkD4) {
		this.remarkD4 = remarkD4;
	}

	public String getAttachmentD4() {
		return attachmentD4;
	}

	public void setAttachmentD4(String attachmentD4) {
		this.attachmentD4 = attachmentD4;
	}

	public String getRemarkD5() {
		return remarkD5;
	}

	public void setRemarkD5(String remarkD5) {
		this.remarkD5 = remarkD5;
	}

	public String getAttachmentD5() {
		return attachmentD5;
	}

	public void setAttachmentD5(String attachmentD5) {
		this.attachmentD5 = attachmentD5;
	}

	public String getDutyManD6() {
		return dutyManD6;
	}

	public void setDutyManD6(String dutyManD6) {
		this.dutyManD6 = dutyManD6;
	}

	public String getDutyManD6Login() {
		return dutyManD6Login;
	}

	public void setDutyManD6Login(String dutyManD6Login) {
		this.dutyManD6Login = dutyManD6Login;
	}

	public String getDeprtmentD6() {
		return deprtmentD6;
	}

	public void setDeprtmentD6(String deprtmentD6) {
		this.deprtmentD6 = deprtmentD6;
	}

	public Date getPlanDateD6() {
		return planDateD6;
	}

	public void setPlanDateD6(Date planDateD6) {
		this.planDateD6 = planDateD6;
	}

	public String getRemarkD6() {
		return remarkD6;
	}

	public void setRemarkD6(String remarkD6) {
		this.remarkD6 = remarkD6;
	}

	public String getAttachmentD6() {
		return attachmentD6;
	}

	public void setAttachmentD6(String attachmentD6) {
		this.attachmentD6 = attachmentD6;
	}

	public String getRemarkD7() {
		return remarkD7;
	}

	public void setRemarkD7(String remarkD7) {
		this.remarkD7 = remarkD7;
	}

	public String getAttachmentD7() {
		return attachmentD7;
	}

	public void setAttachmentD7(String attachmentD7) {
		this.attachmentD7 = attachmentD7;
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
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

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public String getRemarkD8() {
		return remarkD8;
	}

	public void setRemarkD8(String remarkD8) {
		this.remarkD8 = remarkD8;
	}

	public String getAttachmentD8() {
		return attachmentD8;
	}

	public void setAttachmentD8(String attachmentD8) {
		this.attachmentD8 = attachmentD8;
	}

	public List<ImproveReportTeam> getImproveReportTeams() {
		return improveReportTeams;
	}

	public void setImproveReportTeams(List<ImproveReportTeam> improveReportTeams) {
		this.improveReportTeams = improveReportTeams;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getDutyManD2Manager() {
		return dutyManD2Manager;
	}

	public void setDutyManD2Manager(String dutyManD2Manager) {
		this.dutyManD2Manager = dutyManD2Manager;
	}

	public String getDutyManD2ManagerLogin() {
		return dutyManD2ManagerLogin;
	}

	public void setDutyManD2ManagerLogin(String dutyManD2ManagerLogin) {
		this.dutyManD2ManagerLogin = dutyManD2ManagerLogin;
	}

	public String getProcessSection() {
		return processSection;
	}

	public void setProcessSection(String processSection) {
		this.processSection = processSection;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	
	
}

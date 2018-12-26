package com.ambition.gsm.entity;

import javax.persistence.Entity;
import javax.persistence.Table;








import com.ambition.product.base.WorkflowIdEntity;
@Entity
@Table(name="GSM_DGPR")
public class NonconformityDispose extends WorkflowIdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "GSM_DGPR";//实体编码
	public static final String ENTITY_LIST_NAME = "不合格品处理";//实体名称
	
	private String formNo; //表单编号
	private String managerAssets;//管理编号
	private String equipmentName;//设备名称
	private String equipmentModel;//设备型号
	private String factoryNumber;//机身号
	private String devName;//使用部门
	private String userPeople;//使用人
	private String userPeopleLogin;//使用人登入名
	private String placeStorage;//存放位置
	private String deviateState;//偏离状态
	private String laboratory;//实验室
	private String EPCdept;//工程部
	private String EPCdeptLogin;//工程部登入名
	private String EPCdeptOpinion;//工程部意见
	private String QAdept;//品保部
	private String QAdeptLogin;//品保部登入名
	private String QAdeptOpinion;//品保部意见
	private String inspectionResult;//检验结果
	private String rootCauses;//原因分析
	private String correctiveActions;//改善对策
	private String checkResults;//效果确认
	private String responsible;//责任人
	private String initiator;//发起人
	private String initiatorLogin;//发起人登入名
	private String responsibleLogin;//责任人登入名
	private String responsibleLead;//责任人领导
	private String responsibleLeadLogin;//责任人领导登入名
	private String laboratoryLead;//实验室主管
	private String laboratoryLeadLogin;//实验室主管登入名
	private String DescriptionState;//状态描述
	private String jointlySignStrs;//会签办理人 
	private String jointlySignStr;//会签办理人 
	
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
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
	public String getFactoryNumber() {
		return factoryNumber;
	}
	public void setFactoryNumber(String factoryNumber) {
		this.factoryNumber = factoryNumber;
	}
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getPlaceStorage() {
		return placeStorage;
	}
	public void setPlaceStorage(String placeStorage) {
		this.placeStorage = placeStorage;
	}
	public String getDeviateState() {
		return deviateState;
	}
	public void setDeviateState(String deviateState) {
		this.deviateState = deviateState;
	}
	public String getLaboratory() {
		return laboratory;
	}
	public void setLaboratory(String laboratory) {
		this.laboratory = laboratory;
	}
	public String getEPCdept() {
		return EPCdept;
	}
	public void setEPCdept(String ePCdept) {
		EPCdept = ePCdept;
	}
	public String getEPCdeptOpinion() {
		return EPCdeptOpinion;
	}
	public void setEPCdeptOpinion(String ePCdeptOpinion) {
		EPCdeptOpinion = ePCdeptOpinion;
	}
	public String getQAdept() {
		return QAdept;
	}
	public void setQAdept(String qAdept) {
		QAdept = qAdept;
	}
	public String getQAdeptOpinion() {
		return QAdeptOpinion;
	}
	public void setQAdeptOpinion(String qAdeptOpinion) {
		QAdeptOpinion = qAdeptOpinion;
	}
	public String getInspectionResult() {
		return inspectionResult;
	}
	public void setInspectionResult(String inspectionResult) {
		this.inspectionResult = inspectionResult;
	}
	public String getRootCauses() {
		return rootCauses;
	}
	public void setRootCauses(String rootCauses) {
		this.rootCauses = rootCauses;
	}
	public String getCorrectiveActions() {
		return correctiveActions;
	}
	public void setCorrectiveActions(String correctiveActions) {
		this.correctiveActions = correctiveActions;
	}
	public String getResponsible() {
		return responsible;
	}
	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
	public String getResponsibleLead() {
		return responsibleLead;
	}
	public void setResponsibleLead(String responsibleLead) {
		this.responsibleLead = responsibleLead;
	}
	public String getLaboratoryLead() {
		return laboratoryLead;
	}
	public void setLaboratoryLead(String laboratoryLead) {
		this.laboratoryLead = laboratoryLead;
	}
	public String getEPCdeptLogin() {
		return EPCdeptLogin;
	}
	public void setEPCdeptLogin(String ePCdeptLogin) {
		EPCdeptLogin = ePCdeptLogin;
	}
	public String getQAdeptLogin() {
		return QAdeptLogin;
	}
	public void setQAdeptLogin(String qAdeptLogin) {
		QAdeptLogin = qAdeptLogin;
	}
	public String getResponsibleLogin() {
		return responsibleLogin;
	}
	public void setResponsibleLogin(String responsibleLogin) {
		this.responsibleLogin = responsibleLogin;
	}
	public String getLaboratoryLeadLogin() {
		return laboratoryLeadLogin;
	}
	public void setLaboratoryLeadLogin(String laboratoryLeadLogin) {
		this.laboratoryLeadLogin = laboratoryLeadLogin;
	}
	public static String getEntityListCode() {
		return ENTITY_LIST_CODE;
	}
	public static String getEntityListName() {
		return ENTITY_LIST_NAME;
	}
	public String getCheckResults() {
		return checkResults;
	}
	public void setCheckResults(String checkResults) {
		this.checkResults = checkResults;
	}
	public String getDescriptionState() {
		return DescriptionState;
	}
	public void setDescriptionState(String descriptionState) {
		DescriptionState = descriptionState;
	}
	public String getResponsibleLeadLogin() {
		return responsibleLeadLogin;
	}
	public void setResponsibleLeadLogin(String responsibleLeadLogin) {
		this.responsibleLeadLogin = responsibleLeadLogin;
	}
	public String getInitiator() {
		return initiator;
	}
	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}
	public String getInitiatorLogin() {
		return initiatorLogin;
	}
	public void setInitiatorLogin(String initiatorLogin) {
		this.initiatorLogin = initiatorLogin;
	}
	public String getUserPeople() {
		return userPeople;
	}
	public void setUserPeople(String userPeople) {
		this.userPeople = userPeople;
	}
	public String getUserPeopleLogin() {
		return userPeopleLogin;
	}
	public void setUserPeopleLogin(String userPeopleLogin) {
		this.userPeopleLogin = userPeopleLogin;
	}
	public String getJointlySignStrs() {
		return jointlySignStrs;
	}
	public void setJointlySignStrs(String jointlySignStrs) {
		this.jointlySignStrs = jointlySignStrs;
	}
	public String getJointlySignStr() {
		return jointlySignStr;
	}
	public void setJointlySignStr(String jointlySignStr) {
		this.jointlySignStr = jointlySignStr;
	}
	
}

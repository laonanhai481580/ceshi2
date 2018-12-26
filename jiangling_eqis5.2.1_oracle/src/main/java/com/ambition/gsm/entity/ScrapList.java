package com.ambition.gsm.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;
@Entity
@Table(name="GSM_SCRAPLIST")
public class ScrapList extends WorkflowIdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "GSM_SCRAPLIST";//实体编码
	public static final String ENTITY_LIST_NAME = "报废处理";//实体名称
	
	private String formNo;//表单编号
	private String managerAssets;//管理编号
	private String equipmentName;//设备名称
	private String equipmentModel;//设备型号
	private String manufacturer;//生产厂家    
	private String factoryNumber;//出厂编号
	private String devName;//使用部门
	private String responsible;//责任人
	private String equipmentGuarantee;//设备担当
	private Date acquisitionDate;//购置日期
	private Date usingDate;//启用日期
	private Date disableDate;//停用日期
	private String assetNo;//资产编号
	private String placeStorage;//存放地点
	private String scrapReason;//报废原因
	private String proposer;//申请人
	private Date proposerDate;//申请日期
	private String scrapAudit;//报废审核
	private Date scrapAuditDate;//报废审核日期
	private String laboratorycheck;//实验室验证
	private String confirmor;//确认人
	private Date confirmorDate;//确认日期
	private String labAudit;//实验审核
	private Date labAuditDate;//实验审核日期
	private String userDept;//使用部门主管
	private String deptAudit;//部门审核
	private Date deptAuditDate;//部门审核日期
	private String adminDept;//行政部
	private String adminAudit;//行政审核
	private Date adminAuditDate;//行政审核日期
	private int skip=0;
	private String financeDept;//财务部
	private String financeAudit;//财务审核
	private Date financeAuditDate;//财务审核日期
	private String laboratoryConfirmor;//实验室确认
	private String labconfirmor;//实验确认
	private Date labconfirmorDate;//实验确认日期
	private String scrapAuditLogin;//报废审核登入名
	private String confirmorLogin;//确认人登入名
	private String labAuditLogin;//实验审核登入名
	private String deptAuditLogin;//部门审核登入名
	private String adminAuditLogin;//行政审核登入名
	private String financeAuditLogin;//财务审核登入名
	private String labconfirmorLogin;//实验确认登入名
	
	public static String getEntityListCode() {
		return ENTITY_LIST_CODE;
	}
	public static String getEntityListName() {
		return ENTITY_LIST_NAME;
	}
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
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getResponsible() {
		return responsible;
	}
	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
	public String getEquipmentGuarantee() {
		return equipmentGuarantee;
	}
	public void setEquipmentGuarantee(String equipmentGuarantee) {
		this.equipmentGuarantee = equipmentGuarantee;
	}
	public Date getAcquisitionDate() {
		return acquisitionDate;
	}
	public void setAcquisitionDate(Date acquisitionDate) {
		this.acquisitionDate = acquisitionDate;
	}
	public Date getUsingDate() {
		return usingDate;
	}
	public void setUsingDate(Date usingDate) {
		this.usingDate = usingDate;
	}
	public Date getDisableDate() {
		return disableDate;
	}
	public void setDisableDate(Date disableDate) {
		this.disableDate = disableDate;
	}
	public String getAssetNo() {
		return assetNo;
	}
	public void setAssetNo(String assetNo) {
		this.assetNo = assetNo;
	}
	public String getPlaceStorage() {
		return placeStorage;
	}
	public void setPlaceStorage(String placeStorage) {
		this.placeStorage = placeStorage;
	}
	public String getScrapReason() {
		return scrapReason;
	}
	public void setScrapReason(String scrapReason) {
		this.scrapReason = scrapReason;
	}
	public String getProposer() {
		return proposer;
	}
	public void setProposer(String proposer) {
		this.proposer = proposer;
	}
	public Date getProposerDate() {
		return proposerDate;
	}
	public void setProposerDate(Date proposerDate) {
		this.proposerDate = proposerDate;
	}
	public String getScrapAudit() {
		return scrapAudit;
	}
	public void setScrapAudit(String scrapAudit) {
		this.scrapAudit = scrapAudit;
	}
	public Date getScrapAuditDate() {
		return scrapAuditDate;
	}
	public void setScrapAuditDate(Date scrapAuditDate) {
		this.scrapAuditDate = scrapAuditDate;
	}
	public String getLaboratorycheck() {
		return laboratorycheck;
	}
	public void setLaboratorycheck(String laboratorycheck) {
		this.laboratorycheck = laboratorycheck;
	}
	public String getConfirmor() {
		return confirmor;
	}
	public void setConfirmor(String confirmor) {
		this.confirmor = confirmor;
	}
	public Date getConfirmorDate() {
		return confirmorDate;
	}
	public void setConfirmorDate(Date confirmorDate) {
		this.confirmorDate = confirmorDate;
	}
	public String getLabAudit() {
		return labAudit;
	}
	public void setLabAudit(String labAudit) {
		this.labAudit = labAudit;
	}
	public Date getLabAuditDate() {
		return labAuditDate;
	}
	public void setLabAuditDate(Date labAuditDate) {
		this.labAuditDate = labAuditDate;
	}
	public String getUserDept() {
		return userDept;
	}
	public void setUserDept(String userDept) {
		this.userDept = userDept;
	}
	public String getDeptAudit() {
		return deptAudit;
	}
	public void setDeptAudit(String deptAudit) {
		this.deptAudit = deptAudit;
	}
	public Date getDeptAuditDate() {
		return deptAuditDate;
	}
	public void setDeptAuditDate(Date deptAuditDate) {
		this.deptAuditDate = deptAuditDate;
	}
	public String getAdminDept() {
		return adminDept;
	}
	public void setAdminDept(String adminDept) {
		this.adminDept = adminDept;
	}
	public String getAdminAudit() {
		return adminAudit;
	}
	public void setAdminAudit(String adminAudit) {
		this.adminAudit = adminAudit;
	}
	public Date getAdminAuditDate() {
		return adminAuditDate;
	}
	public void setAdminAuditDate(Date adminAuditDate) {
		this.adminAuditDate = adminAuditDate;
	}
	public String getFinanceDept() {
		return financeDept;
	}
	public void setFinanceDept(String financeDept) {
		this.financeDept = financeDept;
	}
	public String getFinanceAudit() {
		return financeAudit;
	}
	public void setFinanceAudit(String financeAudit) {
		this.financeAudit = financeAudit;
	}
	public Date getFinanceAuditDate() {
		return financeAuditDate;
	}
	public void setFinanceAuditDate(Date financeAuditDate) {
		this.financeAuditDate = financeAuditDate;
	}
	public String getLaboratoryConfirmor() {
		return laboratoryConfirmor;
	}
	public void setLaboratoryConfirmor(String laboratoryConfirmor) {
		this.laboratoryConfirmor = laboratoryConfirmor;
	}
	public String getLabconfirmor() {
		return labconfirmor;
	}
	public void setLabconfirmor(String labconfirmor) {
		this.labconfirmor = labconfirmor;
	}
	public Date getLabconfirmorDate() {
		return labconfirmorDate;
	}
	public void setLabconfirmorDate(Date labconfirmorDate) {
		this.labconfirmorDate = labconfirmorDate;
	}
	public String getScrapAuditLogin() {
		return scrapAuditLogin;
	}
	public void setScrapAuditLogin(String scrapAuditLogin) {
		this.scrapAuditLogin = scrapAuditLogin;
	}
	public String getConfirmorLogin() {
		return confirmorLogin;
	}
	public void setConfirmorLogin(String confirmorLogin) {
		this.confirmorLogin = confirmorLogin;
	}
	public String getLabAuditLogin() {
		return labAuditLogin;
	}
	public void setLabAuditLogin(String labAuditLogin) {
		this.labAuditLogin = labAuditLogin;
	}
	public String getDeptAuditLogin() {
		return deptAuditLogin;
	}
	public void setDeptAuditLogin(String deptAuditLogin) {
		this.deptAuditLogin = deptAuditLogin;
	}
	public String getAdminAuditLogin() {
		return adminAuditLogin;
	}
	public void setAdminAuditLogin(String adminAuditLogin) {
		this.adminAuditLogin = adminAuditLogin;
	}
	public String getFinanceAuditLogin() {
		return financeAuditLogin;
	}
	public void setFinanceAuditLogin(String financeAuditLogin) {
		this.financeAuditLogin = financeAuditLogin;
	}
	public String getLabconfirmorLogin() {
		return labconfirmorLogin;
	}
	public void setLabconfirmorLogin(String labconfirmorLogin) {
		this.labconfirmorLogin = labconfirmorLogin;
	}
	public int getSkip() {
		return skip;
	}
	public void setSkip(int skip) {
		this.skip = skip;
	}

	
}

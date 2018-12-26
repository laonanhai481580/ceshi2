package com.ambition.gsm.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:外校报告
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  XJN
 * @version 1.00 2017年5月13日 发布
 */
@Entity
@Table(name="GSM_FOREIGN_REPORT")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class GsmForeignReport extends IdEntity{
	
		/**
		  *GsmForeignReport.java2017年5月13日
		 */
	private static final long serialVersionUID = 1L;
	private String formNo;//报告编号
	private String equipmentName;//仪器名称
	private String equipmentModel;//规格型号
	private String manufacturer;//制造厂商 
	private String managementNo;//管理编号
	private String departMent;//使用部门
	private String installPlace;//安装地点
	private String foreignFile;//外校附件
	private String remark;//备注
	
	private String planId;//计划ID
	private String checkMan;//校验人
	private String checkManLogin;//校验人
	private Date checkDate;//校验日期
	private Date nextCheckDate;//下次校验日期
	private String hasNext;//是否生成过下次校验计划
	private String checkResult;//校验结果
	private String gsmState;//隐藏状态
	private String dutyMan;//责任人
	private String dutyLoginMan;//责任人登录名
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
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
	public String getManagementNo() {
		return managementNo;
	}
	public void setManagementNo(String managementNo) {
		this.managementNo = managementNo;
	}
	public String getDepartMent() {
		return departMent;
	}
	public void setDepartMent(String departMent) {
		this.departMent = departMent;
	}
	public String getInstallPlace() {
		return installPlace;
	}
	public void setInstallPlace(String installPlace) {
		this.installPlace = installPlace;
	}
	public String getForeignFile() {
		return foreignFile;
	}
	public void setForeignFile(String foreignFile) {
		this.foreignFile = foreignFile;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCheckMan() {
		return checkMan;
	}
	public void setCheckMan(String checkMan) {
		this.checkMan = checkMan;
	}
	public String getCheckManLogin() {
		return checkManLogin;
	}
	public void setCheckManLogin(String checkManLogin) {
		this.checkManLogin = checkManLogin;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public Date getNextCheckDate() {
		return nextCheckDate;
	}
	public void setNextCheckDate(Date nextCheckDate) {
		this.nextCheckDate = nextCheckDate;
	}
	public String getHasNext() {
		return hasNext;
	}
	public void setHasNext(String hasNext) {
		this.hasNext = hasNext;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	public String getGsmState() {
		return gsmState;
	}
	public void setGsmState(String gsmState) {
		this.gsmState = gsmState;
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
}

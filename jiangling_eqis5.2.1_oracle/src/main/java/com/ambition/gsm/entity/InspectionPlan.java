package com.ambition.gsm.entity;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * 校准计划(ENTITY)
 * @author 张顺治
 *
 */
@Entity
@Table(name = "GSM_INSPECTION_PLAN")
public class InspectionPlan extends IdEntity implements Cloneable{
	private static final long serialVersionUID = 1L;
	/*********** 送检状态 **********/
	public final static String STATE_CHECk_WAIT = "待校验";
	public final static String STATE_CHECk_IN = "校验中";
	public final static String STATE_CHECk_OUT = "校验完";
	private String companyMain;//公司主体
	private String processSection;//制程区段
	private String devName;//使用部门
	private String workProducre;//工序
	private String address;//安装地址
	private String dutyMan;//责任人
	private String dutyLoginMan;//责任人登录名
	private String managerAssets;//管理编号
	private String equipmentName;//设备名称
	private String equipmentModel;//设备型号
	private String manufacturer;//生产厂家    
	private String checkMethod;//校检方式
	private String frequency;//频率
	private String notifierLoginName;
	private String remark;//备注
	@Temporal(TemporalType.TIMESTAMP)
	private Date inspectionLastedDate;//上次校检日期
	@Temporal(TemporalType.TIMESTAMP)
	private Date inspectionPlanDate;//计划校检日期
	@Temporal(TemporalType.TIMESTAMP)
	private Date actualInspectionDate;//实际校验日期
	private String inspectionPeople;//校验人
	private String attachment;//附件
	private Boolean yesOrNo;// 是否为计划校验
	private String light;//红警灯 
	private String inspectionState;// 送检状态:待校验;校验中;完成校验
	private String checkState;//检查状态
	private String isIntime;//是否及时完成
	private String checkResult;//校验结果
	private String plan1;
	private String do1;
	private String plan2;
	private String do2;
	private String plan3;
	private String do3;
	private String plan4;
	private String do4;
	private String plan5;
	private String do5;
	private String plan6;
	private String do6;
	private String plan7;
	private String do7;
	private String plan8;
	private String do8;
	private String plan9;
	private String do9;
	private String plan10;
	private String do10;
	private String plan11;
	private String do11;
	private String plan12;
	private String do12;
	private String gsmState;//隐藏状态
	@Embedded
	private GsmMailSetting gsmMailSetting;
	@OneToMany(mappedBy="inspectionPlan",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	List<GsmInspectionRecord> inspectionRecords;//检查项目
	@OneToMany(mappedBy="inspectionPlan",cascade={CascadeType.ALL})
//	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	List<InspectionWarnUser> inspectionWarnUsers;//检定计划邮件发送人
	@ManyToOne
	@JoinColumn(name="FK_GSM_EQUIPMENT_ID")
	private GsmEquipment gsmEquipment;
	
	public String getCheckState() {
		return checkState;
	}
	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}
	public String getLight() {
		return light;
	}
	public void setLight(String light) {
		this.light = light;
	}
	public Date getActualInspectionDate() {
		return actualInspectionDate;
	}
	public void setActualInspectionDate(Date actualInspectionDate) throws InstantiationException, IllegalAccessException, Exception {
		this.actualInspectionDate = actualInspectionDate;
		if(actualInspectionDate != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(actualInspectionDate);
			Integer month = calendar.get(Calendar.MONTH)+1;
			switch (month) {
			case 1:
				this.do1 = "D";
				break;
			case 2:
				this.do2 = "D";
				break;
			case 3:
				this.do3 = "D";
				break;
			case 4:
				this.do4 = "D";
				break;
			case 5:
				this.do5 = "D";
				break;
			case 6:
				this.do6 = "D";
				break;
			case 7:
				this.do7 = "D";
				break;
			case 8:
				this.do8 = "D";
				break;
			case 9:
				this.do9 = "D";
				break;
			case 10:
				this.do10 = "D";
				break;
			case 11:
				this.do11 = "D";
				break;
			case 12:
				this.do12 = "D";
				break;
			default:
				break;
			}
		}
	}
	public String getInspectionPeople() {
		return inspectionPeople;
	}
	public void setInspectionPeople(String inspectionPeople) {
		this.inspectionPeople = inspectionPeople;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public Boolean getYesOrNo() {
		return yesOrNo;
	}
	public void setYesOrNo(Boolean yesOrNo) {
		this.yesOrNo = yesOrNo;
	}
	public void setNotifierLoginName(String notifierLoginName) {
		this.notifierLoginName = notifierLoginName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getInspectionLastedDate() {
		return inspectionLastedDate;
	}
	public void setInspectionLastedDate(Date inspectionLastedDate) {
		this.inspectionLastedDate = inspectionLastedDate;
	}
	public Date getInspectionPlanDate() {
		return inspectionPlanDate;
	}
	public void setInspectionPlanDate(Date inspectionPlanDate) throws InstantiationException, IllegalAccessException, Exception {
		this.inspectionPlanDate = inspectionPlanDate;
		if(inspectionPlanDate != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(inspectionPlanDate);
			Integer month = calendar.get(Calendar.MONTH)+1;
			switch (month) {
			case 1:
				this.plan1 = "P";
				break;
			case 2:
				this.plan2 = "P";
				break;
			case 3:
				this.plan3 = "P";
				break;
			case 4:
				this.plan4 = "P";
				break;
			case 5:
				this.plan5 = "P";
				break;
			case 6:
				this.plan6 = "P";
				break;
			case 7:
				this.plan7 = "P";
				break;
			case 8:
				this.plan8 = "P";
				break;
			case 9:
				this.plan9 = "P";
				break;
			case 10:
				this.plan10 = "P";
				break;
			case 11:
				this.plan11 = "P";
				break;
			case 12:
				this.plan12 = "P";
				break;
			default:
				break;
			}
		}
	}
	public String getInspectionState() {
		return inspectionState;
	}
	public void setInspectionState(String inspectionState) {
		this.inspectionState = inspectionState;
	}
	public GsmEquipment getGsmEquipment() {
		return gsmEquipment;
	}
	public void setGsmEquipment(GsmEquipment gsmEquipment) {
		this.gsmEquipment = gsmEquipment;
	}
	public String getNotifierLoginName() {
		return notifierLoginName;
	}
	public String getPlan1() {
		return plan1;
	}
	public void setPlan1(String plan1) {
		this.plan1 = plan1;
	}
	public String getDo1() {
		return do1;
	}
	public void setDo1(String do1) {
		this.do1 = do1;
	}
	public String getPlan2() {
		return plan2;
	}
	public void setPlan2(String plan2) {
		this.plan2 = plan2;
	}
	public String getDo2() {
		return do2;
	}
	public void setDo2(String do2) {
		this.do2 = do2;
	}
	public String getPlan3() {
		return plan3;
	}
	public void setPlan3(String plan3) {
		this.plan3 = plan3;
	}
	public String getDo3() {
		return do3;
	}
	public void setDo3(String do3) {
		this.do3 = do3;
	}
	public String getPlan4() {
		return plan4;
	}
	public void setPlan4(String plan4) {
		this.plan4 = plan4;
	}
	public String getDo4() {
		return do4;
	}
	public void setDo4(String do4) {
		this.do4 = do4;
	}
	public String getPlan5() {
		return plan5;
	}
	public void setPlan5(String plan5) {
		this.plan5 = plan5;
	}
	public String getDo5() {
		return do5;
	}
	public void setDo5(String do5) {
		this.do5 = do5;
	}
	public String getPlan6() {
		return plan6;
	}
	public void setPlan6(String plan6) {
		this.plan6 = plan6;
	}
	public String getDo6() {
		return do6;
	}
	public void setDo6(String do6) {
		this.do6 = do6;
	}
	public String getPlan7() {
		return plan7;
	}
	public void setPlan7(String plan7) {
		this.plan7 = plan7;
	}
	public String getDo7() {
		return do7;
	}
	public void setDo7(String do7) {
		this.do7 = do7;
	}
	public String getPlan8() {
		return plan8;
	}
	public void setPlan8(String plan8) {
		this.plan8 = plan8;
	}
	public String getDo8() {
		return do8;
	}
	public void setDo8(String do8) {
		this.do8 = do8;
	}
	public String getPlan9() {
		return plan9;
	}
	public void setPlan9(String plan9) {
		this.plan9 = plan9;
	}
	public String getDo9() {
		return do9;
	}
	public void setDo9(String do9) {
		this.do9 = do9;
	}
	public String getPlan10() {
		return plan10;
	}
	public void setPlan10(String plan10) {
		this.plan10 = plan10;
	}
	public String getDo10() {
		return do10;
	}
	public void setDo10(String do10) {
		this.do10 = do10;
	}
	public String getPlan11() {
		return plan11;
	}
	public void setPlan11(String plan11) {
		this.plan11 = plan11;
	}
	public String getDo11() {
		return do11;
	}
	public void setDo11(String do11) {
		this.do11 = do11;
	}
	public String getPlan12() {
		return plan12;
	}
	public void setPlan12(String plan12) {
		this.plan12 = plan12;
	}
	public String getDo12() {
		return do12;
	}
	public void setDo12(String do12) {
		this.do12 = do12;
	}
	public String getCompanyMain() {
		return companyMain;
	}
	public void setCompanyMain(String companyMain) {
		this.companyMain = companyMain;
	}
	public String getProcessSection() {
		return processSection;
	}
	public void setProcessSection(String processSection) {
		this.processSection = processSection;
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
	public String getCheckMethod() {
		return checkMethod;
	}
	public void setCheckMethod(String checkMethod) {
		this.checkMethod = checkMethod;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getDutyLoginMan() {
		return dutyLoginMan;
	}
	public void setDutyLoginMan(String dutyLoginMan) {
		this.dutyLoginMan = dutyLoginMan;
	}
	public GsmMailSetting getGsmMailSetting() {
		return gsmMailSetting;
	}
	public void setGsmMailSetting(GsmMailSetting gsmMailSetting) {
		this.gsmMailSetting = gsmMailSetting;
	}
	public List<GsmInspectionRecord> getInspectionRecords() {
		return inspectionRecords;
	}
	public void setInspectionRecords(List<GsmInspectionRecord> inspectionRecords) {
		this.inspectionRecords = inspectionRecords;
	}
	public List<InspectionWarnUser> getInspectionWarnUsers() {
		return inspectionWarnUsers;
	}
	public void setInspectionWarnUsers(List<InspectionWarnUser> inspectionWarnUsers) {
		this.inspectionWarnUsers = inspectionWarnUsers;
	}
	public String getIsIntime() {
		return isIntime;
	}
	public void setIsIntime(String isIntime) {
		this.isIntime = isIntime;
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
	
}
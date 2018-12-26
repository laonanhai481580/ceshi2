package com.ambition.gsm.entity;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;



/**
 * MSA校验计划(ENTITY)
 * @author 张顺志
 *
 */
@Entity
@Table(name = "GSM_MSAINSPECTION_PLAN")
public class InspectionMsaplan extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String equimentType;//仪器类别
	private String analysisWay;//分析方式
	private String levelUp;//规格上限
	private String levelDown;//规格下限
	private String unit;//单位
	private String evStr;//EV%
	private String avStr;//AV%
	private String grrStr;//GR&R%
	private String grrResult;//GR&R结论
	private String inspectionTo;//校准目标	
	private String inspectionResult;//校准结果
	private Date firstExecDate;//第一次执行日期
	private Date secondExecDate;//第二次执行日期
	private Date msaDate;//测试日期
	private String msaState;//测试状况
	private String remark;//备注
	private String attachment;//报告
	
	
	@ManyToOne
	@JoinColumn(name="FK_GSM_EQUIPMENT_ID")
	private GsmEquipment gsmEquipment;


	public String getEquimentType() {
		return equimentType;
	}


	public void setEquimentType(String equimentType) {
		this.equimentType = equimentType;
	}


	public String getAnalysisWay() {
		return analysisWay;
	}


	public void setAnalysisWay(String analysisWay) {
		this.analysisWay = analysisWay;
	}


	public String getLevelUp() {
		return levelUp;
	}


	public void setLevelUp(String levelUp) {
		this.levelUp = levelUp;
	}


	public String getLevelDown() {
		return levelDown;
	}


	public void setLevelDown(String levelDown) {
		this.levelDown = levelDown;
	}


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}


	public String getEvStr() {
		return evStr;
	}


	public void setEvStr(String evStr) {
		this.evStr = evStr;
	}


	public String getAvStr() {
		return avStr;
	}


	public void setAvStr(String avStr) {
		this.avStr = avStr;
	}


	public String getGrrStr() {
		return grrStr;
	}


	public void setGrrStr(String grrStr) {
		this.grrStr = grrStr;
	}


	public String getGrrResult() {
		return grrResult;
	}


	public void setGrrResult(String grrResult) {
		this.grrResult = grrResult;
	}


	public String getInspectionTo() {
		return inspectionTo;
	}


	public void setInspectionTo(String inspectionTo) {
		this.inspectionTo = inspectionTo;
	}


	public String getInspectionResult() {
		return inspectionResult;
	}


	public void setInspectionResult(String inspectionResult) {
		this.inspectionResult = inspectionResult;
	}


	public Date getMsaDate() {
		return msaDate;
	}


	public void setMsaDate(Date msaDate) {
		this.msaDate = msaDate;
	}


	public String getMsaState() {
		return msaState;
	}


	public void setMsaState(String msaState) {
		this.msaState = msaState;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getAttachment() {
		return attachment;
	}


	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}


	public GsmEquipment getGsmEquipment() {
		return gsmEquipment;
	}


	public void setGsmEquipment(GsmEquipment gsmEquipment) {
		this.gsmEquipment = gsmEquipment;
	}


	public Date getFirstExecDate() {
		return firstExecDate;
	}


	public void setFirstExecDate(Date firstExecDate) {
		this.firstExecDate = firstExecDate;
	}


	public Date getSecondExecDate() {
		return secondExecDate;
	}


	public void setSecondExecDate(Date secondExecDate) {
		this.secondExecDate = secondExecDate;
	}
	

}

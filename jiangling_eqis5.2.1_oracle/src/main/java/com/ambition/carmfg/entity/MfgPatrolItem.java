package com.ambition.carmfg.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ambition.product.base.IdEntity;

/**
 * 类名:巡检项目记录
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2013-8-6 发布
 */
@Entity
@Table(name="MFG_PATROL_ITEM")
public class MfgPatrolItem extends IdEntity{
	private static final long serialVersionUID = 1L;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inspectionDate;//检验日期
	private String checkItemName;//检查项目名称
	private String countType;//统计类型
	private Integer inspectionAmount;//检验数量
	private String result;//巡检记录
	private String spcSampleIds;//spc采集ID
	
	private String oneLevelMateriel;//一级物料
	
	private String secondLevelMateriel;//二级物料
	
	private String threeLevelMateriel;//三级物料
//	private String countResult;//计数统计结果
//	private String calculateResult;//计量统计结果
//	@Column(length=4)
//	private String countResult1;//计数统计1
//	@Column(length=4)
//	private String countResult2;//计数统计2
//	@Column(length=4)
//	private String countResult3;//计数统计3
//	@Column(length=4)
//	private String countResult4;//计数统计4
//	@Column(length=4)
//	private String countResult5;//计数统计5
//	@Column(length=4)
//	private String countResult6;//计数统计6
//	@Column(length=4)
//	private String countResult7;//计数统计7
//	@Column(length=4)
//	private String countResult8;//计数统计8
//	@Column(length=4)
//	private String countResult9;//计数统计9
//	@Column(length=4)
//	private String countResult10;//计数统计10
//	@Column(length=4)
//	private String countResult11;//计数统计11
//	@Column(length=4)
//	private String countResult12;//计数统计12
//	@Column(length=4)
//	private String countResult13;//计数统计13
//	@Column(length=4)
//	private String countResult14;//计数统计14
//	@Column(length=4)
//	private String countResult15;//计数统计15
//	@Column(length=4)
//	private String countResult16;//计数统计16
//	@Column(length=4)
//	private String countResult17;//计数统计17
//	@Column(length=4)
//	private String countResult18;//计数统计18
//	@Column(length=4)
//	private String countResult19;//计数统计19
//	@Column(length=4)
//	private String countResult20;//计数统计20
//	@Column(length=4)
//	private String countResult21;//计数统计21
//	@Column(length=4)
//	private String countResult22;//计数统计22
//	@Column(length=4)
//	private String countResult23;//计数统计23
//	@Column(length=4)
//	private String countResult24;//计数统计24
//	@Column(length=4)
//	private String countResult25;//计数统计25
//	@Column(length=4)
//	private String countResult26;//计数统计26
//	@Column(length=4)
//	private String countResult27;//计数统计27
//	@Column(length=4)
//	private String countResult28;//计数统计28
//	@Column(length=4)
//	private String countResult29;//计数统计29
//	@Column(length=4)
//	private String countResult30;//计数统计30
//	
//	private Double result1;//结果1
//	private Double result2;//结果2
//	private Double result3;//结果3
//	private Double result4;//结果4
//	private Double result5;//结果5
//	private Double result6;//结果6
//	private Double result7;//结果7
//	private Double result8;//结果8
//	private Double result9;//结果9
//	private Double result10;//结果10
//	private Double result11;//结果11
//	private Double result12;//结果12
//	private Double result13;//结果13
//	private Double result14;//结果14
//	private Double result15;//结果15
//	private Double result16;//结果16
//	private Double result17;//结果17
//	private Double result18;//结果18
//	private Double result19;//结果19
//	private Double result20;//结果20
//	private Double result21;//结果21
//	private Double result22;//结果22
//	private Double result23;//结果23
//	private Double result24;//结果24
//	private Double result25;//结果25
//	private Double result26;//结果26
//	private Double result27;//结果27
//	private Double result28;//结果28
//	private Double result29;//结果29
//	private Double result30;//结果30
	private String conclusion="OK";//结论
	@Column(length=300)
	private String remark;//备注
	
	@ManyToOne
	@JoinColumn(name="FK_MFG_REPORT_ID")
	private MfgCheckInspectionReport mfgCheckInspectionReport;//检验报告

	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public String getCheckItemName() {
		return checkItemName;
	}

	public void setCheckItemName(String checkItemName) {
		this.checkItemName = checkItemName;
	}

	public String getCountType() {
		return countType;
	}

	public void setCountType(String countType) {
		this.countType = countType;
	}

	public Integer getInspectionAmount() {
		return inspectionAmount;
	}

	public void setInspectionAmount(Integer inspectionAmount) {
		this.inspectionAmount = inspectionAmount;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public MfgCheckInspectionReport getMfgCheckInspectionReport() {
		return mfgCheckInspectionReport;
	}

	public void setMfgCheckInspectionReport(
			MfgCheckInspectionReport mfgCheckInspectionReport) {
		this.mfgCheckInspectionReport = mfgCheckInspectionReport;
	}

	public String getSpcSampleIds() {
		return spcSampleIds;
	}

	public void setSpcSampleIds(String spcSampleIds) {
		this.spcSampleIds = spcSampleIds;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOneLevelMateriel() {
		return oneLevelMateriel;
	}

	public void setOneLevelMateriel(String oneLevelMateriel) {
		this.oneLevelMateriel = oneLevelMateriel;
	}

	public String getSecondLevelMateriel() {
		return secondLevelMateriel;
	}

	public void setSecondLevelMateriel(String secondLevelMateriel) {
		this.secondLevelMateriel = secondLevelMateriel;
	}

	public String getThreeLevelMateriel() {
		return threeLevelMateriel;
	}

	public void setThreeLevelMateriel(String threeLevelMateriel) {
		this.threeLevelMateriel = threeLevelMateriel;
	}
	
}
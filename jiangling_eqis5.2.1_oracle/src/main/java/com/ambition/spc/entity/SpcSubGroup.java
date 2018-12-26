package com.ambition.spc.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;

/**    
 * SpcSubgroup.java
 * @authorBy YUKE
 *
 */
@Entity
@Table(name = "SPC_SUB_GROUP")
public class SpcSubGroup extends IdEntity {

	/**
	 * 子组表
	 */
	private static final long serialVersionUID = 1L;
	private String subGroupNo;//子组编号
	private Integer subGroupOrderNum;//子组顺序号
	private Integer subGroupSize;//样本容量
	private Integer actualSmapleNum;//实际输入样本数
	private Integer batchSize;//批量
	private Double sgValue;//取值
	private Double sigma;//标准差
	private Double rangeDiff;//极差/移动极差
	private Double maxValue;//最大值
	private Double minValue;//最小值
	private String workStation;//采集站点
	@Temporal(TemporalType.TIMESTAMP)
	private Date entryDate;//录入日期
	private String entryUser;//录入用户
	private String batchNo;//批次
	private int judgeState;//标记数据是否已经判断了  1 表示判断过了，0表示没有判断
	private String info1;
	private String info2;
	private String info3;
	private String info4;
	private String info5;
	private String info6;
	private String info7;
	private String info8;
	private String info9;
	private String info10;
	private String info11;
	private String info12;
	private String info13;
	private String info14;
	private String info15;
	private String info16;
	private String info17;
	private String info18;
	private String info19;
	private String info20;
	private String remark;//备注
	
	@ManyToOne
	@JoinColumn(name = "FK_QUALITY_FEATURE_ID")
	private QualityFeature qualityFeature;//质量特性
	private String secondLevel;//上级
	private String topLevel;//上上级
	//ofilm
	private String prosessPointName;//过程定义名称
	private String inspectionType;//检验类别
	@OneToMany(mappedBy="spcSubGroup",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OrderBy(value="createdTime")
	private List<SpcSgSample> spcSgSamples;//子组样本表
	
	@OneToMany(mappedBy="spcSubGroup",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<SpcSgTag> spcSgTags;//子组附属信息表
	
	@OneToMany(mappedBy="spcSubGroup",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<AbnormalInfo> abnormalInfos;//异常消息

	public String getSubGroupNo() {
		return subGroupNo;
	}

	public void setSubGroupNo(String subGroupNo) {
		this.subGroupNo = subGroupNo;
	}

	public Integer getSubGroupOrderNum() {
		return subGroupOrderNum;
	}

	public void setSubGroupOrderNum(Integer subGroupOrderNum) {
		this.subGroupOrderNum = subGroupOrderNum;
	}

	public Integer getSubGroupSize() {
		return subGroupSize;
	}

	public void setSubGroupSize(Integer subGroupSize) {
		this.subGroupSize = subGroupSize;
	}

	public Integer getActualSmapleNum() {
		return actualSmapleNum;
	}

	public void setActualSmapleNum(Integer actualSmapleNum) {
		this.actualSmapleNum = actualSmapleNum;
	}

	public Integer getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(Integer batchSize) {
		this.batchSize = batchSize;
	}

	public Double getSgValue() {
		return sgValue;
	}

	public void setSgValue(Double sgValue) {
		this.sgValue = sgValue;
	}

	public Double getSigma() {
		return sigma;
	}

	public void setSigma(Double sigma) {
		this.sigma = sigma;
	}

	public Double getRangeDiff() {
		return rangeDiff;
	}

	public void setRangeDiff(Double rangeDiff) {
		this.rangeDiff = rangeDiff;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public Double getMinValue() {
		return minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public String getWorkStation() {
		return workStation;
	}

	public void setWorkStation(String workStation) {
		this.workStation = workStation;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public String getEntryUser() {
		return entryUser;
	}

	public void setEntryUser(String entryUser) {
		this.entryUser = entryUser;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public int getJudgeState() {
		return judgeState;
	}

	public void setJudgeState(int judgeState) {
		this.judgeState = judgeState;
	}

	public String getInfo1() {
		return info1;
	}

	public void setInfo1(String info1) {
		this.info1 = info1;
	}

	public String getInfo2() {
		return info2;
	}

	public void setInfo2(String info2) {
		this.info2 = info2;
	}

	public String getInfo3() {
		return info3;
	}

	public void setInfo3(String info3) {
		this.info3 = info3;
	}

	public String getInfo4() {
		return info4;
	}

	public void setInfo4(String info4) {
		this.info4 = info4;
	}

	public String getInfo5() {
		return info5;
	}

	public void setInfo5(String info5) {
		this.info5 = info5;
	}

	public String getInfo6() {
		return info6;
	}

	public void setInfo6(String info6) {
		this.info6 = info6;
	}

	public String getInfo7() {
		return info7;
	}

	public void setInfo7(String info7) {
		this.info7 = info7;
	}

	public String getInfo8() {
		return info8;
	}

	public void setInfo8(String info8) {
		this.info8 = info8;
	}

	public String getInfo9() {
		return info9;
	}

	public void setInfo9(String info9) {
		this.info9 = info9;
	}

	public String getInfo10() {
		return info10;
	}

	public void setInfo10(String info10) {
		this.info10 = info10;
	}

	public String getInfo11() {
		return info11;
	}

	public void setInfo11(String info11) {
		this.info11 = info11;
	}

	public String getInfo12() {
		return info12;
	}

	public void setInfo12(String info12) {
		this.info12 = info12;
	}

	public String getInfo13() {
		return info13;
	}

	public void setInfo13(String info13) {
		this.info13 = info13;
	}

	public String getInfo14() {
		return info14;
	}

	public void setInfo14(String info14) {
		this.info14 = info14;
	}

	public String getInfo15() {
		return info15;
	}

	public void setInfo15(String info15) {
		this.info15 = info15;
	}

	public String getInfo16() {
		return info16;
	}

	public void setInfo16(String info16) {
		this.info16 = info16;
	}

	public String getInfo17() {
		return info17;
	}

	public void setInfo17(String info17) {
		this.info17 = info17;
	}

	public String getInfo18() {
		return info18;
	}

	public void setInfo18(String info18) {
		this.info18 = info18;
	}

	public String getInfo19() {
		return info19;
	}

	public void setInfo19(String info19) {
		this.info19 = info19;
	}

	public String getInfo20() {
		return info20;
	}

	public void setInfo20(String info20) {
		this.info20 = info20;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public QualityFeature getQualityFeature() {
		return qualityFeature;
	}

	public void setQualityFeature(QualityFeature qualityFeature) {
		this.qualityFeature = qualityFeature;
	}

	public List<SpcSgSample> getSpcSgSamples() {
		return spcSgSamples;
	}

	public void setSpcSgSamples(List<SpcSgSample> spcSgSamples) {
		this.spcSgSamples = spcSgSamples;
	}

	public List<SpcSgTag> getSpcSgTags() {
		return spcSgTags;
	}

	public void setSpcSgTags(List<SpcSgTag> spcSgTags) {
		this.spcSgTags = spcSgTags;
	}

	public List<AbnormalInfo> getAbnormalInfos() {
		return abnormalInfos;
	}

	public void setAbnormalInfos(List<AbnormalInfo> abnormalInfos) {
		this.abnormalInfos = abnormalInfos;
	}

	public String getSecondLevel() {
		return secondLevel;
	}

	public void setSecondLevel(String secondLevel) {
		this.secondLevel = secondLevel;
	}

	public String getTopLevel() {
		return topLevel;
	}

	public void setTopLevel(String topLevel) {
		this.topLevel = topLevel;
	}

	public String getProsessPointName() {
		return prosessPointName;
	}

	public void setProsessPointName(String prosessPointName) {
		this.prosessPointName = prosessPointName;
	}

	public String getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}
	
	
}

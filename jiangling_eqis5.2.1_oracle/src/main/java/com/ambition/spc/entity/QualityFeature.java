package com.ambition.spc.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;

/**    
 * QualityFeature.java
 * @authorBy YUKE
 *
 */
@Entity
@Table(name = "SPC_QUALITY_FEATURE")
public class QualityFeature extends IdEntity {
	/**
	 * 质量特性
	 */
	private static final long serialVersionUID = 1L;
	private Integer orderNum = 0;//排序
	//基本信息
	private String name;//名称
	private String code;//简码
	private String paramType;//特性类型
	private String specificationType;//规格类型
	private Double targeValue;//规格限-目标值
	private Double upperTarge;//目标值上限
	private Double lowerTarge;//目标值下限
	private Integer sampleCapacity;//样本容量
	private Integer effectiveCapacity;//有效容量
	private String controlChart;//默认控制图
	private Integer rangeInterval;//移动极差间距
	private String precs;//显示精度
	private String unit;//计量单位
	private Double upperLimit;//合理上限
	private Double lowerLimit;//合理下限
	private Boolean isNoAccept;//是否不接受
	private String isAutoCl;//是否自动计算控制限
	//控制限
	private Integer multiple;//控制标准差倍数
	private String state;//质量特性状态
	private String method;//控制限确定方法
	private Double u;//μ值
	private Double cpk;//CPK值
	private Double ppk;//PPK值
	private Double ucl1;//UCL-X值
	private Double ucl2;//UCL-R值
	private Double cl1;//CL-X值
	private Double cl2;//CL-R值
	private Double lcl1;//LCL-X值
	private Double lcl2;//LCL-R值
	private Double uclMin;//UCL最小值
	private Double uclMax;//UCL最大值
	private Double lclMin;//LCL最小值
	private Double lclMax;//LCL最大值
	private Double uclCurrent1;//当前UCL-X值
	private Double uclCurrent2;//当前UCL-R值
	private Double clCurrent1;//当前CL-X值
	private Double clCurrent2;//当前CL-R值
	private Double lclCurrent1;//当前LCL-X值
	private Double lclCurrent2;//当前LCL-R值
	//判准准则
	private String criterionName;//准则名称
	private Boolean isAuto;//自动生成名称
	private String criterionChart;//准则控制图
	private String criterionType;//准则类型
	private String tendType;//趋势型
	private String runType;//运行型
	private Double point1;
	private Double point2;
	private Double point2_1;
	private Double point3;
	private Double point4;
	private Double point4_1;
	//层别信息
	private String levelName;//层别名称
	private String levelCode;//层别简码
	
	private Float predictDbk;//dbk设定预警规则，小于此dbk值的则报dbk异常，如果dbk为空则不进行dbk判异
	private Float recordDbkl;//dbk设定报警规则，小于此dbk值的则报dbk异常，如果dbk为空则不进行dbk判异
	
	private String targetTableName;//保存表的名称
	private String isMigration="否";//是否迁移
	@ManyToOne
	@JoinColumn(name="FK_PROCESS_POINT_ID")
	private ProcessPoint processPoint;//过程节点
	
	@OneToMany(mappedBy="qualityFeature",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<FeatureRules> featureRules;//判断准则
	
	@OneToMany(mappedBy="qualityFeature",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<FeatureLayer> featureLayers;//层别信息
	
	@OneToMany(mappedBy="qualityFeature",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<SpcSubGroup> spcSubGroups;//子组信息
	
	@OneToMany(mappedBy="qualityFeature",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<ControlLimit> controlLimits;//控制限
	
	@OneToMany(mappedBy="qualityFeature",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<AbnormalInfo> abnormalInfos;//异常消息
	
	@OneToMany(mappedBy="qualityFeature",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<ReasonMeasure> reasonMeasures;//原因措施
	
	@OneToMany(mappedBy="qualityFeature",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<FeaturePerson> featurePersons;//异常通知人员

	@OneToMany(mappedBy="qualityFeature",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<FeatureTableColumn> featureTableColumns;//质量特性列信息
	
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getSpecificationType() {
		return specificationType;
	}

	public void setSpecificationType(String specificationType) {
		this.specificationType = specificationType;
	}

	public Double getTargeValue() {
		return targeValue;
	}

	public void setTargeValue(Double targeValue) {
		this.targeValue = targeValue;
	}

	public Double getUpperTarge() {
		return upperTarge;
	}

	public void setUpperTarge(Double upperTarge) {
		this.upperTarge = upperTarge;
	}

	public Double getLowerTarge() {
		return lowerTarge;
	}

	public void setLowerTarge(Double lowerTarge) {
		this.lowerTarge = lowerTarge;
	}

	/**
	  * 默认5 
	 */
	public Integer getSampleCapacity() {
		if(sampleCapacity == null){
			sampleCapacity = 5;
		}
		return sampleCapacity;
	}

	public void setSampleCapacity(Integer sampleCapacity) {
		this.sampleCapacity = sampleCapacity;
	}
	/**
	  * 默认5 
	 */
	public Integer getEffectiveCapacity() {
		if(effectiveCapacity == null){
			effectiveCapacity = 5;
		}
		return effectiveCapacity;
	}

	public void setEffectiveCapacity(Integer effectiveCapacity) {
		this.effectiveCapacity = effectiveCapacity;
	}

	public String getControlChart() {
		return controlChart;
	}

	public void setControlChart(String controlChart) {
		this.controlChart = controlChart;
	}

	public Integer getRangeInterval() {
		return rangeInterval;
	}

	public void setRangeInterval(Integer rangeInterval) {
		this.rangeInterval = rangeInterval;
	}

	public String getPrecs() {
		return precs;
	}

	public void setPrecs(String precs) {
		this.precs = precs;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(Double upperLimit) {
		this.upperLimit = upperLimit;
	}

	public Double getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(Double lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public Boolean getIsNoAccept() {
		return isNoAccept;
	}

	public void setIsNoAccept(Boolean isNoAccept) {
		this.isNoAccept = isNoAccept;
	}

	public Integer getMultiple() {
		return multiple;
	}

	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Double getU() {
		return u;
	}

	public void setU(Double u) {
		this.u = u;
	}

	public Double getCpk() {
		return cpk;
	}

	public void setCpk(Double cpk) {
		this.cpk = cpk;
	}

	public Double getPpk() {
		return ppk;
	}

	public void setPpk(Double ppk) {
		this.ppk = ppk;
	}

	public Double getUcl1() {
		return ucl1;
	}

	public void setUcl1(Double ucl1) {
		this.ucl1 = ucl1;
	}

	public Double getUcl2() {
		return ucl2;
	}

	public void setUcl2(Double ucl2) {
		this.ucl2 = ucl2;
	}

	public Double getCl1() {
		return cl1;
	}

	public void setCl1(Double cl1) {
		this.cl1 = cl1;
	}

	public Double getCl2() {
		return cl2;
	}

	public void setCl2(Double cl2) {
		this.cl2 = cl2;
	}

	public Double getLcl1() {
		return lcl1;
	}

	public void setLcl1(Double lcl1) {
		this.lcl1 = lcl1;
	}

	public Double getLcl2() {
		return lcl2;
	}

	public void setLcl2(Double lcl2) {
		this.lcl2 = lcl2;
	}

	public Double getUclMin() {
		return uclMin;
	}

	public void setUclMin(Double uclMin) {
		this.uclMin = uclMin;
	}

	public Double getUclMax() {
		return uclMax;
	}

	public void setUclMax(Double uclMax) {
		this.uclMax = uclMax;
	}

	public Double getLclMin() {
		return lclMin;
	}

	public void setLclMin(Double lclMin) {
		this.lclMin = lclMin;
	}

	public Double getLclMax() {
		return lclMax;
	}

	public void setLclMax(Double lclMax) {
		this.lclMax = lclMax;
	}

	public Double getUclCurrent1() {
		return uclCurrent1;
	}

	public void setUclCurrent1(Double uclCurrent1) {
		this.uclCurrent1 = uclCurrent1;
	}

	public Double getUclCurrent2() {
		return uclCurrent2;
	}

	public void setUclCurrent2(Double uclCurrent2) {
		this.uclCurrent2 = uclCurrent2;
	}

	public Double getClCurrent1() {
		return clCurrent1;
	}

	public void setClCurrent1(Double clCurrent1) {
		this.clCurrent1 = clCurrent1;
	}

	public Double getClCurrent2() {
		return clCurrent2;
	}

	public void setClCurrent2(Double clCurrent2) {
		this.clCurrent2 = clCurrent2;
	}

	public Double getLclCurrent1() {
		return lclCurrent1;
	}

	public void setLclCurrent1(Double lclCurrent1) {
		this.lclCurrent1 = lclCurrent1;
	}

	public Double getLclCurrent2() {
		return lclCurrent2;
	}

	public void setLclCurrent2(Double lclCurrent2) {
		this.lclCurrent2 = lclCurrent2;
	}

	public String getCriterionName() {
		return criterionName;
	}

	public void setCriterionName(String criterionName) {
		this.criterionName = criterionName;
	}

	public Boolean getIsAuto() {
		return isAuto;
	}

	public void setIsAuto(Boolean isAuto) {
		this.isAuto = isAuto;
	}

	public String getCriterionChart() {
		return criterionChart;
	}

	public void setCriterionChart(String criterionChart) {
		this.criterionChart = criterionChart;
	}

	public String getCriterionType() {
		return criterionType;
	}

	public void setCriterionType(String criterionType) {
		this.criterionType = criterionType;
	}

	public String getTendType() {
		return tendType;
	}

	public void setTendType(String tendType) {
		this.tendType = tendType;
	}

	public String getRunType() {
		return runType;
	}

	public void setRunType(String runType) {
		this.runType = runType;
	}

	public Double getPoint1() {
		return point1;
	}

	public void setPoint1(Double point1) {
		this.point1 = point1;
	}

	public Double getPoint2() {
		return point2;
	}

	public void setPoint2(Double point2) {
		this.point2 = point2;
	}

	public Double getPoint3() {
		return point3;
	}

	public void setPoint3(Double point3) {
		this.point3 = point3;
	}

	public Double getPoint4() {
		return point4;
	}

	public void setPoint4(Double point4) {
		this.point4 = point4;
	}

	public Double getPoint2_1() {
		return point2_1;
	}

	public void setPoint2_1(Double point2_1) {
		this.point2_1 = point2_1;
	}

	public Double getPoint4_1() {
		return point4_1;
	}

	public void setPoint4_1(Double point4_1) {
		this.point4_1 = point4_1;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getLevelCode() {
		return levelCode;
	}

	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	public ProcessPoint getProcessPoint() {
		return processPoint;
	}

	public void setProcessPoint(ProcessPoint processPoint) {
		this.processPoint = processPoint;
	}

	public List<FeatureRules> getFeatureRules() {
		return featureRules;
	}

	public void setFeatureRules(List<FeatureRules> featureRules) {
		this.featureRules = featureRules;
	}

	public List<FeatureLayer> getFeatureLayers() {
		return featureLayers;
	}

	public void setFeatureLayers(List<FeatureLayer> featureLayers) {
		this.featureLayers = featureLayers;
	}


	public List<SpcSubGroup> getSpcSubGroups() {
		return spcSubGroups;
	}

	public void setSpcSubGroups(List<SpcSubGroup> spcSubGroups) {
		this.spcSubGroups = spcSubGroups;
	}

	public List<ControlLimit> getControlLimits() {
		return controlLimits;
	}

	public void setControlLimits(List<ControlLimit> controlLimits) {
		this.controlLimits = controlLimits;
	}

	public String getIsAutoCl() {
		return isAutoCl;
	}

	public void setIsAutoCl(String isAutoCl) {
		this.isAutoCl = isAutoCl;
	}

	public List<AbnormalInfo> getAbnormalInfos() {
		return abnormalInfos;
	}

	public void setAbnormalInfos(List<AbnormalInfo> abnormalInfos) {
		this.abnormalInfos = abnormalInfos;
	}

	public Float getPredictDbk() {
		return predictDbk;
	}

	public void setPredictDbk(Float predictDbk) {
		this.predictDbk = predictDbk;
	}

	public Float getRecordDbkl() {
		return recordDbkl;
	}

	public void setRecordDbkl(Float recordDbkl) {
		this.recordDbkl = recordDbkl;
	}

	public List<ReasonMeasure> getReasonMeasures() {
		return reasonMeasures;
	}

	public void setReasonMeasures(List<ReasonMeasure> reasonMeasures) {
		this.reasonMeasures = reasonMeasures;
	}

	public List<FeaturePerson> getFeaturePersons() {
		return featurePersons;
	}

	public void setFeaturePersons(List<FeaturePerson> featurePersons) {
		this.featurePersons = featurePersons;
	}

	public String getTargetTableName() {
		return targetTableName;
	}

	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
	}

	public String getIsMigration() {
		return isMigration;
	}

	public void setIsMigration(String isMigration) {
		this.isMigration = isMigration;
	}

	public List<FeatureTableColumn> getFeatureTableColumns() {
		return featureTableColumns;
	}

	public void setFeatureTableColumns(List<FeatureTableColumn> featureTableColumns) {
		this.featureTableColumns = featureTableColumns;
	}
}

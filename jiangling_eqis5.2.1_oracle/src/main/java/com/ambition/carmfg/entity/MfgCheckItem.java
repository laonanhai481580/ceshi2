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
 * 制造检查项目记录明细
 * @author 赵骏
 *
 */
@Entity
@Table(name="MFG_CHECK_ITEM")
public class MfgCheckItem extends IdEntity{
	private static final long serialVersionUID = 1L;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inspectionDate;//检验日期
	private String checkBomCode;//物料编码
	private String checkBomName;//物料名称
	private String workProcedure;//工序
	private String inspectionLevel;//检验级别
	private String codeLetter;//字码
	private String aql;//aql标准
	private Integer aqlAc;//接受数
	private Integer aqlRe;//拒收数
	private String inspectionType;//检验类型,对应检验项目顶级
	private String parentItemName;//项目父类名称
	private Integer parentRowSpan=new Integer(1);//项目父类合并数量
	@Column(length=1000)
	private String checkItemName;//检查项目名称
	private String countType;//统计类型
	private String checkMethod;//检查方法
	private Integer inspectionAmount;//检验数量
	@Column(length=1000)
	private String specifications;//规格
	private String unit;//单位
	private Double maxlimit;//上限
	private Double minlimit;//下限
	private String equipmentNo;//设备编号 机台编号
	private String testEquipmentNo;//测量仪器编号
	private String featureId;//质量特性
	private String spcSampleIds;
//	private String spcSampleIdsPatrol;////spc采集ID--巡检
//	private String spcSampleIdsComplete;////spc采集ID--末检
	private String remark;//备注
	private String inspector;//检验员
	private String isJnUnit="否";//是否IPQC测试项
	private String isInEquipment="否";//是否集成设备
	private String itemStatus="未领取";//检验项目状态
	
	@Column(length=1000)
	private String results;//数据
	private Double result1;//结果1
	private Double result2;//结果2
	private Double result3;//结果3
	private Double result4;//结果4
	private Double result5;//结果5
	private Double result6;//结果6
	private Double result7;//结果7
	private Double result8;//结果8
	private Double result9;//结果9
	private Double result10;//结果10
	private Double result11;//结果11
	private Double result12;//结果12
	private Double result13;//结果13
	private Double result14;//结果14
	private Double result15;//结果15
	private Double result16;//结果16
	private Double result17;//结果17
	private Double result18;//结果18
	private Double result19;//结果19
	private Double result20;//结果20
	private Double result21;//结果21
	private Double result22;//结果22
	private Double result23;//结果23
	private Double result24;//结果24
	private Double result25;//结果25
	private Double result26;//结果26
	private Double result27;//结果27
	private Double result28;//结果28
	private Double result29;//结果29
	private Double result30;//结果30
	private Double result31;//结果31
	private Double result32;//结果32
	private Double result33;//结果33
	private Double result34;//结果34
	private Double result35;//结果35
	private Double result36;//结果1
	private Double result37;//结果2
	private Double result38;//结果3
	private Double result39;//结果4
	private Double result40;//结果5
	private Double result41;//结果6
	private Double result42;//结果7
	private Double result43;//结果8
	private Double result44;//结果9
	private Double result45;//结果10
	private Double result46;//结果11
	private Double result47;//结果12
	private Double result48;//结果13
	private Double result49;//结果14
	private Double result50;//结果15
	private Double result51;//结果16
	private Double result52;//结果17
	private Double result53;//结果18
	private Double result54;//结果19
	private Double result55;//结果20
	private Double result56;//结果21
	private Double result57;//结果22
	private Double result58;//结果23
	private Double result59;//结果24
	private Double result60;//结果25
	private Double result61;//结果26
	private Double result62;//结果27
	private Double result63;//结果28
	private Double result64;//结果29
	private Double result65;//结果30
	private Double result66;//结果31
	private Double result67;//结果32
	private Double result68;//结果33
	private Double result69;//结果34
	private Double result70;//结果35
	private Double result71;//结果26
	private Double result72;//结果27
	private Double result73;//结果28
	private Double result74;//结果29
	private Double result75;//结果30
	private Double result76;//结果31
	private Double result77;//结果32
	private Double result78;//结果33
	private Double result79;//结果34
	private Double result80;//结果35
	private Integer qualifiedAmount;//合格数
	private Integer unqualifiedAmount;//不良数
	private Float qualifiedRate=(float) 100.0; // 合格率
	private String attachmentFiles;//附件
	private String conclusion;//结论
	private InspectionPointTypeEnum inspectionPointType = InspectionPointTypeEnum.STORAGEINSPECTION;//采集点类型,默认为检查数据
	
	@ManyToOne
	@JoinColumn(name="FK_MFG_REPORT_ID")
	private MfgCheckInspectionReport mfgCheckInspectionReport;//检验报告

	public Float getQualifiedRate() {
		return qualifiedRate;
	}

	public void setQualifiedRate(Float qualifiedRate) {
		this.qualifiedRate = qualifiedRate;
	}

	public String getParentItemName() {
		return parentItemName;
	}

	public void setParentItemName(String parentItemName) {
		this.parentItemName = parentItemName;
	}

	public Integer getParentRowSpan() {
		return parentRowSpan;
	}

	public void setParentRowSpan(Integer parentRowSpan) {
		this.parentRowSpan = parentRowSpan;
	}

	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public String getCheckBomCode() {
		return checkBomCode;
	}

	public void setCheckBomCode(String checkBomCode) {
		this.checkBomCode = checkBomCode;
	}

	public String getCheckBomName() {
		return checkBomName;
	}

	public void setCheckBomName(String checkBomName) {
		this.checkBomName = checkBomName;
	}

	public String getInspectionLevel() {
		return inspectionLevel;
	}

	public void setInspectionLevel(String inspectionLevel) {
		this.inspectionLevel = inspectionLevel;
	}

	public String getAql() {
		return aql;
	}

	public void setAql(String aql) {
		this.aql = aql;
	}

	public Integer getAqlAc() {
		return aqlAc;
	}

	public void setAqlAc(Integer aqlAc) {
		this.aqlAc = aqlAc;
	}

	public Integer getAqlRe() {
		return aqlRe;
	}

	public void setAqlRe(Integer aqlRe) {
		this.aqlRe = aqlRe;
	}

	public String getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
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

	public String getCheckMethod() {
		return checkMethod;
	}

	public void setCheckMethod(String checkMethod) {
		this.checkMethod = checkMethod;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getMaxlimit() {
		return maxlimit;
	}

	public void setMaxlimit(Double maxlimit) {
		this.maxlimit = maxlimit;
	}

	public Double getMinlimit() {
		return minlimit;
	}

	public void setMinlimit(Double minlimit) {
		this.minlimit = minlimit;
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public Integer getQualifiedAmount() {
		return qualifiedAmount;
	}

	public void setQualifiedAmount(Integer qualifiedAmount) {
		this.qualifiedAmount = qualifiedAmount;
	}

	public Integer getUnqualifiedAmount() {
		return unqualifiedAmount;
	}

	public void setUnqualifiedAmount(Integer unqualifiedAmount) {
		this.unqualifiedAmount = unqualifiedAmount;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public String getCodeLetter() {
		return codeLetter;
	}

	public void setCodeLetter(String codeLetter) {
		this.codeLetter = codeLetter;
	}

	public Integer getInspectionAmount() {
		return inspectionAmount;
	}

	public void setInspectionAmount(Integer inspectionAmount) {
		this.inspectionAmount = inspectionAmount;
	}

	public Double getResult1() {
		return result1;
	}

	public void setResult1(Double result1) {
		this.result1 = result1;
	}

	public Double getResult2() {
		return result2;
	}

	public void setResult2(Double result2) {
		this.result2 = result2;
	}

	public Double getResult3() {
		return result3;
	}

	public void setResult3(Double result3) {
		this.result3 = result3;
	}

	public Double getResult4() {
		return result4;
	}

	public void setResult4(Double result4) {
		this.result4 = result4;
	}

	public Double getResult5() {
		return result5;
	}

	public void setResult5(Double result5) {
		this.result5 = result5;
	}

	public Double getResult6() {
		return result6;
	}

	public void setResult6(Double result6) {
		this.result6 = result6;
	}

	public Double getResult7() {
		return result7;
	}

	public void setResult7(Double result7) {
		this.result7 = result7;
	}

	public Double getResult8() {
		return result8;
	}

	public void setResult8(Double result8) {
		this.result8 = result8;
	}

	public Double getResult9() {
		return result9;
	}

	public void setResult9(Double result9) {
		this.result9 = result9;
	}

	public Double getResult10() {
		return result10;
	}

	public void setResult10(Double result10) {
		this.result10 = result10;
	}

	public Double getResult11() {
		return result11;
	}

	public void setResult11(Double result11) {
		this.result11 = result11;
	}

	public Double getResult12() {
		return result12;
	}

	public void setResult12(Double result12) {
		this.result12 = result12;
	}

	public Double getResult13() {
		return result13;
	}

	public void setResult13(Double result13) {
		this.result13 = result13;
	}

	public Double getResult14() {
		return result14;
	}

	public void setResult14(Double result14) {
		this.result14 = result14;
	}

	public Double getResult15() {
		return result15;
	}

	public void setResult15(Double result15) {
		this.result15 = result15;
	}

	public Double getResult16() {
		return result16;
	}

	public void setResult16(Double result16) {
		this.result16 = result16;
	}

	public Double getResult17() {
		return result17;
	}

	public void setResult17(Double result17) {
		this.result17 = result17;
	}

	public Double getResult18() {
		return result18;
	}

	public void setResult18(Double result18) {
		this.result18 = result18;
	}

	public Double getResult19() {
		return result19;
	}

	public void setResult19(Double result19) {
		this.result19 = result19;
	}

	public Double getResult20() {
		return result20;
	}

	public void setResult20(Double result20) {
		this.result20 = result20;
	}

	public Double getResult21() {
		return result21;
	}

	public void setResult21(Double result21) {
		this.result21 = result21;
	}

	public Double getResult22() {
		return result22;
	}

	public void setResult22(Double result22) {
		this.result22 = result22;
	}

	public Double getResult23() {
		return result23;
	}

	public void setResult23(Double result23) {
		this.result23 = result23;
	}

	public Double getResult24() {
		return result24;
	}

	public void setResult24(Double result24) {
		this.result24 = result24;
	}

	public Double getResult25() {
		return result25;
	}

	public void setResult25(Double result25) {
		this.result25 = result25;
	}

	public Double getResult26() {
		return result26;
	}

	public void setResult26(Double result26) {
		this.result26 = result26;
	}

	public Double getResult27() {
		return result27;
	}

	public void setResult27(Double result27) {
		this.result27 = result27;
	}

	public Double getResult28() {
		return result28;
	}

	public void setResult28(Double result28) {
		this.result28 = result28;
	}

	public Double getResult29() {
		return result29;
	}

	public void setResult29(Double result29) {
		this.result29 = result29;
	}

	public Double getResult30() {
		return result30;
	}

	public void setResult30(Double result30) {
		this.result30 = result30;
	}

	public String getWorkProcedure() {
		return workProcedure;
	}

	public void setWorkProcedure(String workProcedure) {
		this.workProcedure = workProcedure;
	}

	public MfgCheckInspectionReport getMfgCheckInspectionReport() {
		return mfgCheckInspectionReport;
	}

	public void setMfgCheckInspectionReport(
			MfgCheckInspectionReport mfgCheckInspectionReport) {
		this.mfgCheckInspectionReport = mfgCheckInspectionReport;
	}

	public String getAttachmentFiles() {
		return attachmentFiles;
	}

	public void setAttachmentFiles(String attachmentFiles) {
		this.attachmentFiles = attachmentFiles;
	}

	public InspectionPointTypeEnum getInspectionPointType() {
		return inspectionPointType;
	}

	public void setInspectionPointType(InspectionPointTypeEnum inspectionPointType) {
		this.inspectionPointType = inspectionPointType;
	}

	public String getSpcSampleIds() {
		return spcSampleIds;
	}

	public void setSpcSampleIds(String spcSampleIds) {
		this.spcSampleIds = spcSampleIds;
	}


	public Double getResult31() {
		return result31;
	}

	public void setResult31(Double result31) {
		this.result31 = result31;
	}

	public Double getResult32() {
		return result32;
	}

	public void setResult32(Double result32) {
		this.result32 = result32;
	}

	public Double getResult33() {
		return result33;
	}

	public void setResult33(Double result33) {
		this.result33 = result33;
	}

	public Double getResult34() {
		return result34;
	}

	public void setResult34(Double result34) {
		this.result34 = result34;
	}

	public Double getResult35() {
		return result35;
	}

	public void setResult35(Double result35) {
		this.result35 = result35;
	}

	public Double getResult36() {
		return result36;
	}

	public void setResult36(Double result36) {
		this.result36 = result36;
	}

	public Double getResult37() {
		return result37;
	}

	public void setResult37(Double result37) {
		this.result37 = result37;
	}

	public Double getResult38() {
		return result38;
	}

	public void setResult38(Double result38) {
		this.result38 = result38;
	}

	public Double getResult39() {
		return result39;
	}

	public void setResult39(Double result39) {
		this.result39 = result39;
	}

	public Double getResult40() {
		return result40;
	}

	public void setResult40(Double result40) {
		this.result40 = result40;
	}

	public Double getResult41() {
		return result41;
	}

	public void setResult41(Double result41) {
		this.result41 = result41;
	}

	public Double getResult42() {
		return result42;
	}

	public void setResult42(Double result42) {
		this.result42 = result42;
	}

	public Double getResult43() {
		return result43;
	}

	public void setResult43(Double result43) {
		this.result43 = result43;
	}

	public Double getResult44() {
		return result44;
	}

	public void setResult44(Double result44) {
		this.result44 = result44;
	}

	public Double getResult45() {
		return result45;
	}

	public void setResult45(Double result45) {
		this.result45 = result45;
	}

	public Double getResult46() {
		return result46;
	}

	public void setResult46(Double result46) {
		this.result46 = result46;
	}

	public Double getResult47() {
		return result47;
	}

	public void setResult47(Double result47) {
		this.result47 = result47;
	}

	public Double getResult48() {
		return result48;
	}

	public void setResult48(Double result48) {
		this.result48 = result48;
	}

	public Double getResult49() {
		return result49;
	}

	public void setResult49(Double result49) {
		this.result49 = result49;
	}

	public Double getResult50() {
		return result50;
	}

	public void setResult50(Double result50) {
		this.result50 = result50;
	}

	public Double getResult51() {
		return result51;
	}

	public void setResult51(Double result51) {
		this.result51 = result51;
	}

	public Double getResult52() {
		return result52;
	}

	public void setResult52(Double result52) {
		this.result52 = result52;
	}

	public Double getResult53() {
		return result53;
	}

	public void setResult53(Double result53) {
		this.result53 = result53;
	}

	public Double getResult54() {
		return result54;
	}

	public void setResult54(Double result54) {
		this.result54 = result54;
	}

	public Double getResult55() {
		return result55;
	}

	public void setResult55(Double result55) {
		this.result55 = result55;
	}

	public Double getResult56() {
		return result56;
	}

	public void setResult56(Double result56) {
		this.result56 = result56;
	}

	public Double getResult57() {
		return result57;
	}

	public void setResult57(Double result57) {
		this.result57 = result57;
	}

	public Double getResult58() {
		return result58;
	}

	public void setResult58(Double result58) {
		this.result58 = result58;
	}

	public Double getResult59() {
		return result59;
	}

	public void setResult59(Double result59) {
		this.result59 = result59;
	}

	public Double getResult60() {
		return result60;
	}

	public void setResult60(Double result60) {
		this.result60 = result60;
	}

	public Double getResult61() {
		return result61;
	}

	public void setResult61(Double result61) {
		this.result61 = result61;
	}

	public Double getResult62() {
		return result62;
	}

	public void setResult62(Double result62) {
		this.result62 = result62;
	}

	public Double getResult63() {
		return result63;
	}

	public void setResult63(Double result63) {
		this.result63 = result63;
	}

	public Double getResult64() {
		return result64;
	}

	public void setResult64(Double result64) {
		this.result64 = result64;
	}

	public Double getResult65() {
		return result65;
	}

	public void setResult65(Double result65) {
		this.result65 = result65;
	}

	public Double getResult66() {
		return result66;
	}

	public void setResult66(Double result66) {
		this.result66 = result66;
	}

	public Double getResult67() {
		return result67;
	}

	public void setResult67(Double result67) {
		this.result67 = result67;
	}

	public Double getResult68() {
		return result68;
	}

	public void setResult68(Double result68) {
		this.result68 = result68;
	}

	public Double getResult69() {
		return result69;
	}

	public void setResult69(Double result69) {
		this.result69 = result69;
	}

	public Double getResult70() {
		return result70;
	}

	public void setResult70(Double result70) {
		this.result70 = result70;
	}

	public Double getResult71() {
		return result71;
	}

	public void setResult71(Double result71) {
		this.result71 = result71;
	}

	public Double getResult72() {
		return result72;
	}

	public void setResult72(Double result72) {
		this.result72 = result72;
	}

	public Double getResult73() {
		return result73;
	}

	public void setResult73(Double result73) {
		this.result73 = result73;
	}

	public Double getResult74() {
		return result74;
	}

	public void setResult74(Double result74) {
		this.result74 = result74;
	}

	public Double getResult75() {
		return result75;
	}

	public void setResult75(Double result75) {
		this.result75 = result75;
	}

	public Double getResult76() {
		return result76;
	}

	public void setResult76(Double result76) {
		this.result76 = result76;
	}

	public Double getResult77() {
		return result77;
	}

	public void setResult77(Double result77) {
		this.result77 = result77;
	}

	public Double getResult78() {
		return result78;
	}

	public void setResult78(Double result78) {
		this.result78 = result78;
	}

	public Double getResult79() {
		return result79;
	}

	public void setResult79(Double result79) {
		this.result79 = result79;
	}

	public Double getResult80() {
		return result80;
	}

	public void setResult80(Double result80) {
		this.result80 = result80;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getInspector() {
		return inspector;
	}

	public void setInspector(String inspector) {
		this.inspector = inspector;
	}

	public String getIsJnUnit() {
		return isJnUnit;
	}

	public void setIsJnUnit(String isJnUnit) {
		this.isJnUnit = isJnUnit;
	}

	public String getIsInEquipment() {
		return isInEquipment;
	}

	public void setIsInEquipment(String isInEquipment) {
		this.isInEquipment = isInEquipment;
	}

	public String getEquipmentNo() {
		return equipmentNo;
	}

	public void setEquipmentNo(String equipmentNo) {
		this.equipmentNo = equipmentNo;
	}

	public String getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}

	public String getTestEquipmentNo() {
		return testEquipmentNo;
	}

	public void setTestEquipmentNo(String testEquipmentNo) {
		this.testEquipmentNo = testEquipmentNo;
	}
	
}
package com.ambition.carmfg.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 类名:设备参数检验记录
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  吴旭明
 * @version 1.00 2016-9-10 发布
 */
@Entity
@Table(name = "MFG_PLANT_ITEM")
public class MfgPlantParameterItem extends IdEntity  {

	private static final long serialVersionUID = 1L;
	
	private String itemName;//设备名称
	private String parameterName;//参数名称
	private String parameterSpc;//参数规格
	private String countType;//计数类型
	private Double parameterMin;//下限
	private Double parameterMax;//上限
	private String conclusion;//检验结果
	private String remark;//描述
	private String inspector;//检验员
	private String results;//
	private Double result;//检验记录
	private Double result1;//检验记录
	private Double result2;//检验记录
	private Double result3;//检验记录
	private Double result4;//检验记录
	private Double result5;//检验记录
	private Double result6;//检验记录
	private Double result7;//检验记录
	private Double result8;//检验记录
	private Double result9;//检验记录
	private Double result10;//检验记录
	private Double result11;//检验记录
	private Double result12;//检验记录
	private Double result13;//检验记录
	private Double result14;//检验记录
	private Double result15;//检验记录
	private Double result16;//检验记录
	private Double result17;//检验记录
	private Double result18;//检验记录
	private Double result19;//检验记录
	private Double result20;//检验记录
	private Double result21;//检验记录
	private Double result22;//检验记录
	private Double result23;//检验记录
	private Double result24;//检验记录
	private Double result25;//检验记录
	private Double result26;//检验记录
	private Double result27;//检验记录
	private Double result28;//检验记录
	private Double result29;//检验记录
	@ManyToOne
	@JoinColumn(name="FK_MFG_REPORT_ID")
	private MfgCheckInspectionReport mfgCheckInspectionReport;//检验记录
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getParameterSpc() {
		return parameterSpc;
	}
	public void setParameterSpc(String parameterSpc) {
		this.parameterSpc = parameterSpc;
	}
	public Double getParameterMin() {
		return parameterMin;
	}
	public void setParameterMin(Double parameterMin) {
		this.parameterMin = parameterMin;
	}
	public Double getParameterMax() {
		return parameterMax;
	}
	public void setParameterMax(Double parameterMax) {
		this.parameterMax = parameterMax;
	}
	public Double getResult() {
		return result;
	}
	public void setResult(Double result) {
		this.result = result;
	}
	public String getConclusion() {
		return conclusion;
	}
	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
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
	public MfgCheckInspectionReport getMfgCheckInspectionReport() {
		return mfgCheckInspectionReport;
	}
	public void setMfgCheckInspectionReport(
			MfgCheckInspectionReport mfgCheckInspectionReport) {
		this.mfgCheckInspectionReport = mfgCheckInspectionReport;
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
	public String getCountType() {
		return countType;
	}
	public void setCountType(String countType) {
		this.countType = countType;
	}
	public String getResults() {
		return results;
	}
	public void setResults(String results) {
		this.results = results;
	}

}

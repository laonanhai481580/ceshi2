package com.ambition.gp.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;




import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:均值材料
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  xjn
 * @version 1.00 2017年10月26日 发布
 */
@Entity
@Table(name="GP_AVERAGE_MATERIAL")  //
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class GpAverageMaterial extends IdEntity{
	private static final long serialVersionUID = 1L;
	
	private String supplierName;//供应商名称
	private String supplierCode;//供应商编码
	private String supplierEmail;//供应商邮箱
	private String auditMan;//审核人
	private String auditManLogin;//审核人登录名
	private String declaring;//宣告人
	private Date supplierDate;//声明日期
	private String productName;//产品名称
	private String averageMaterialName;//均值材料名称
	private String averageMaterialModel;//均值材料型号
	private String averageMaterialWeight;//均值材料重量
	private String averageMaterialAttribute;//均值材料属性
	private String unit;//单位
	private String partName;//拆解部件名称
	private String manufacturer;//制造商
	private String testReportNo;//测试报告编号
	private Date testReportDate;//测试报告日期
	private Date testReportExpire;//测试报告到期
	private String testReportFile;//测试报告文件
	private String testReportDepart;//测试报告机构
	private String msdsFile;//SDS附件
	private String exemption;//豁免 
	private String text1;//文本Cd
	private String text2;//文本Pd
	private String text3;//文本Hg
	private String text4;//文本Cr+6
	private String text5;//文本PBBs
	private String text6;//文本PBDEs
	private String text7;//文本DEHP
	private String text8;//文本BBP
	private String text9;//文本DBP
	private String text10;//文本DIBP
	private String text11;//文本Br
	private String text12;//文本Cl
	private String text13;//文本Sb
	private String text14;//文本Be
	private String text15;//文本As
	private String text16;//文本PFOS
	private String text17;//文本PFOA
	private String remark;//备注
	private String isHarmful=STATE_SUBMIT;//状态
	private String substancesFile;//附件
	private String updateStatus;//更新状态
	public static final String STATE_QUALIFIED = "合格";
	public static final String STATE_SUBMIT = "待提交";
	public static final String STATE_PENDING = "待审核";
	public static final String STATE_OVERDUE = "过期";
	public String averageId;//均值Id
	private String factorySupply;//供应厂区
	@JsonIgnore
	@OneToMany(mappedBy="gpAverageMaterial",cascade=javax.persistence.CascadeType.ALL)
    @Cascade(value=CascadeType.DELETE_ORPHAN)
	private List<GpSubstance> gpSubstances;//管控物质
	
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierEmail() {
		return supplierEmail;
	}
	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}
	public String getDeclaring() {
		return declaring;
	}
	public void setDeclaring(String declaring) {
		this.declaring = declaring;
	}
	public Date getSupplierDate() {
		return supplierDate;
	}
	public void setSupplierDate(Date supplierDate) {
		this.supplierDate = supplierDate;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getAverageMaterialName() {
		return averageMaterialName;
	}
	public void setAverageMaterialName(String averageMaterialName) {
		this.averageMaterialName = averageMaterialName;
	}
	public String getAverageMaterialModel() {
		return averageMaterialModel;
	}
	public void setAverageMaterialModel(String averageMaterialModel) {
		this.averageMaterialModel = averageMaterialModel;
	}
	public String getAverageMaterialWeight() {
		return averageMaterialWeight;
	}
	public void setAverageMaterialWeight(String averageMaterialWeight) {
		this.averageMaterialWeight = averageMaterialWeight;
	}
	public String getAverageMaterialAttribute() {
		return averageMaterialAttribute;
	}
	public void setAverageMaterialAttribute(String averageMaterialAttribute) {
		this.averageMaterialAttribute = averageMaterialAttribute;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getTestReportNo() {
		return testReportNo;
	}
	public void setTestReportNo(String testReportNo) {
		this.testReportNo = testReportNo;
	}
	public Date getTestReportDate() {
		return testReportDate;
	}
	public void setTestReportDate(Date testReportDate) {
		this.testReportDate = testReportDate;
	}
	public Date getTestReportExpire() {
		return testReportExpire;
	}
	public void setTestReportExpire(Date testReportExpire) {
		this.testReportExpire = testReportExpire;
	}
	public String getTestReportFile() {
		return testReportFile;
	}
	public void setTestReportFile(String testReportFile) {
		this.testReportFile = testReportFile;
	}
	public String getTestReportDepart() {
		return testReportDepart;
	}
	public void setTestReportDepart(String testReportDepart) {
		this.testReportDepart = testReportDepart;
	}
	public String getText1() {
		return text1;
	}
	public void setText1(String text1) {
		this.text1 = text1;
	}
	public String getText2() {
		return text2;
	}
	public void setText2(String text2) {
		this.text2 = text2;
	}
	public String getText3() {
		return text3;
	}
	public void setText3(String text3) {
		this.text3 = text3;
	}
	public String getText4() {
		return text4;
	}
	public void setText4(String text4) {
		this.text4 = text4;
	}
	public String getText5() {
		return text5;
	}
	public void setText5(String text5) {
		this.text5 = text5;
	}
	public String getText6() {
		return text6;
	}
	public void setText6(String text6) {
		this.text6 = text6;
	}
	public String getText7() {
		return text7;
	}
	public void setText7(String text7) {
		this.text7 = text7;
	}
	public String getText8() {
		return text8;
	}
	public void setText8(String text8) {
		this.text8 = text8;
	}
	public String getText9() {
		return text9;
	}
	public void setText9(String text9) {
		this.text9 = text9;
	}
	public String getText10() {
		return text10;
	}
	public void setText10(String text10) {
		this.text10 = text10;
	}
	public String getText11() {
		return text11;
	}
	public void setText11(String text11) {
		this.text11 = text11;
	}
	public String getText12() {
		return text12;
	}
	public void setText12(String text12) {
		this.text12 = text12;
	}
	public String getText13() {
		return text13;
	}
	public void setText13(String text13) {
		this.text13 = text13;
	}
	public String getText14() {
		return text14;
	}
	public void setText14(String text14) {
		this.text14 = text14;
	}
	public String getText15() {
		return text15;
	}
	public void setText15(String text15) {
		this.text15 = text15;
	}
	public String getText16() {
		return text16;
	}
	public void setText16(String text16) {
		this.text16 = text16;
	}
	public String getText17() {
		return text17;
	}
	public void setText17(String text17) {
		this.text17 = text17;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getMsdsFile() {
		return msdsFile;
	}
	public void setMsdsFile(String msdsFile) {
		this.msdsFile = msdsFile;
	}
	public String getExemption() {
		return exemption;
	}
	public void setExemption(String exemption) {
		this.exemption = exemption;
	}
	public String getIsHarmful() {
		return isHarmful;
	}
	public void setIsHarmful(String isHarmful) {
		this.isHarmful = isHarmful;
	}
	public List<GpSubstance> getGpSubstances() {
		return gpSubstances;
	}
	public void setGpSubstances(List<GpSubstance> gpSubstances) {
		this.gpSubstances = gpSubstances;
	}
	public String getSubstancesFile() {
		return substancesFile;
	}
	public void setSubstancesFile(String substancesFile) {
		this.substancesFile = substancesFile;
	}
	public String getUpdateStatus() {
		return updateStatus;
	}
	public void setUpdateStatus(String updateStatus) {
		this.updateStatus = updateStatus;
	}
	public String getAverageId() {
		return averageId;
	}
	public void setAverageId(String averageId) {
		this.averageId = averageId;
	}
	public String getFactorySupply() {
		return factorySupply;
	}
	public void setFactorySupply(String factorySupply) {
		this.factorySupply = factorySupply;
	}
	public String getAuditMan() {
		return auditMan;
	}
	public void setAuditMan(String auditMan) {
		this.auditMan = auditMan;
	}
	public String getAuditManLogin() {
		return auditManLogin;
	}
	public void setAuditManLogin(String auditManLogin) {
		this.auditManLogin = auditManLogin;
	}
	
	
}

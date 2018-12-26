package com.ambition.carmfg.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:OQC检验
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年8月31日 发布
 */
@Entity
@Table(name = "MFG_OQC_INSPECTION")//MFG_OQC_INSPECTION
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class OqcInspection extends IdEntity{
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "MFG_OQC_INSPECTION";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "OQC检验";//实体_列表_名称
	private String enterpriseGroup;//事业群
	private String processSection; // 制程区段
	private String factory; //工厂
	//private String businessUnit;//所属事业部
	private Date inspectionDate;//日期
	private String classGroup;//班别
	private String inspectionBatchNo;//检验批号
	private String workingProcedure;//--工序
	private String model;//机种	
	private String customer;//客户
	private Integer count;//数量
	private Integer samplingCount;//抽检数量
	private Integer unQualityCount;//不良数
	private String  unQualityRate;//不良率
	private String  judgeResult;//判定
	private String inspectionMan;//检验人员
	private String dutyMan;//责任领班登录名
	private String dutyManLogin;//责任领班
	private String qeMan;//QE确认人
	private String qeManLogin;//QE确认人登录名
	private String dealWay;//批处理方式
	private String remark;//备注
	private String isSendMail;//是否发送邮件
	private String qualityType;//是否重工品
	private Integer hasSendMail=0;//是否已经发送邮件
	@OneToMany(mappedBy = "oqcInspection",cascade={CascadeType.ALL})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	List<OqcDefectiveItem> oqcDefectiveItems;

	private String oqcDefectiveItemStr;//不良串组合；
	private String defectiveItem1;
	private String defectiveItem2;
	private String defectiveItem3;
	private String defectiveItem4;
	private String defectiveItem5;
	public String getProcessSection() {
		return processSection;
	}
	public void setProcessSection(String processSection) {
		this.processSection = processSection;
	}
	public Date getInspectionDate() {
		return inspectionDate;
	}
	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
	public String getClassGroup() {
		return classGroup;
	}
	public void setClassGroup(String classGroup) {
		this.classGroup = classGroup;
	}
	public String getInspectionBatchNo() {
		return inspectionBatchNo;
	}
	public void setInspectionBatchNo(String inspectionBatchNo) {
		this.inspectionBatchNo = inspectionBatchNo;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getSamplingCount() {
		return samplingCount;
	}
	public void setSamplingCount(Integer samplingCount) {
		this.samplingCount = samplingCount;
	}
	public Integer getUnQualityCount() {
		return unQualityCount;
	}
	public void setUnQualityCount(Integer unQualityCount) {
		this.unQualityCount = unQualityCount;
	}
	public String getUnQualityRate() {
		return unQualityRate;
	}
	public void setUnQualityRate(String unQualityRate) {
		this.unQualityRate = unQualityRate;
	}
	public String getJudgeResult() {
		return judgeResult;
	}
	public void setJudgeResult(String judgeResult) {
		this.judgeResult = judgeResult;
	}
	public String getInspectionMan() {
		return inspectionMan;
	}
	public void setInspectionMan(String inspectionMan) {
		this.inspectionMan = inspectionMan;
	}
	public String getDutyMan() {
		return dutyMan;
	}
	public void setDutyMan(String dutyMan) {
		this.dutyMan = dutyMan;
	}
	public String getQeMan() {
		return qeMan;
	}
	public void setQeMan(String qeMan) {
		this.qeMan = qeMan;
	}
	public String getDealWay() {
		return dealWay;
	}
	public void setDealWay(String dealWay) {
		this.dealWay = dealWay;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<OqcDefectiveItem> getOqcDefectiveItems() {
		return oqcDefectiveItems;
	}
	public void setOqcDefectiveItems(List<OqcDefectiveItem> oqcDefectiveItems) {
		this.oqcDefectiveItems = oqcDefectiveItems;
	}
	public String getEnterpriseGroup() {
		return enterpriseGroup;
	}
	public void setEnterpriseGroup(String enterpriseGroup) {
		this.enterpriseGroup = enterpriseGroup;
	}
	public String getIsSendMail() {
		return isSendMail;
	}
	public void setIsSendMail(String isSendMail) {
		this.isSendMail = isSendMail;
	}
	public String getDutyManLogin() {
		return dutyManLogin;
	}
	public void setDutyManLogin(String dutyManLogin) {
		this.dutyManLogin = dutyManLogin;
	}
	public String getQeManLogin() {
		return qeManLogin;
	}
	public void setQeManLogin(String qeManLogin) {
		this.qeManLogin = qeManLogin;
	}
	public Integer getHasSendMail() {
		return hasSendMail;
	}
	public void setHasSendMail(Integer hasSendMail) {
		this.hasSendMail = hasSendMail;
	}
	public String getOqcDefectiveItemStr() {
		return oqcDefectiveItemStr;
	}
	public void setOqcDefectiveItemStr(String oqcDefectiveItemStr) {
		this.oqcDefectiveItemStr = oqcDefectiveItemStr;
	}
	public String getDefectiveItem1() {
		return defectiveItem1;
	}
	public void setDefectiveItem1(String defectiveItem1) {
		this.defectiveItem1 = defectiveItem1;
	}
	public String getDefectiveItem2() {
		return defectiveItem2;
	}
	public void setDefectiveItem2(String defectiveItem2) {
		this.defectiveItem2 = defectiveItem2;
	}
	public String getDefectiveItem3() {
		return defectiveItem3;
	}
	public void setDefectiveItem3(String defectiveItem3) {
		this.defectiveItem3 = defectiveItem3;
	}
	public String getDefectiveItem4() {
		return defectiveItem4;
	}
	public void setDefectiveItem4(String defectiveItem4) {
		this.defectiveItem4 = defectiveItem4;
	}
	public String getDefectiveItem5() {
		return defectiveItem5;
	}
	public void setDefectiveItem5(String defectiveItem5) {
		this.defectiveItem5 = defectiveItem5;
	}
	public String getQualityType() {
		return qualityType;
	}
	public void setQualityType(String qualityType) {
		this.qualityType = qualityType;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getWorkingProcedure() {
		return workingProcedure;
	}
	public void setWorkingProcedure(String workingProcedure) {
		this.workingProcedure = workingProcedure;
	}

}

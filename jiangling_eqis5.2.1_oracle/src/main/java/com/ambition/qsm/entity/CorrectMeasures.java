package com.ambition.qsm.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:不符合与纠正措施报告
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年10月27日 发布
 */
@Entity
@Table(name = "QSM_CORRECT_MEASURES")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class CorrectMeasures extends WorkflowIdEntity{

	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "QSM_CORRECT_MEASURES";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "不符合与纠正措施报告";//实体_列表_名称
	private String formNo;//表单编号
	private String enterpriseGroup;//事业群
	private String auditDept;//受审核部门
	private String departmentLeader;//部门负责人
	private String departmentLeaderLogin;//部门负责人登录名
	private String setMan;//发起人
	private Date auditDate;//审核日期
	private String auditType;//审核类型
	//问题描述
	private String remark1;//不符合事实陈述
	private String attachment1;//附件
	private String uploadMan1;//上传人
	private String inconformityType;//不符合类型
	private String auditMan1;//审核人
	private String auditMan1Login;//审核人登录名
	private Date  auditMan1Date;//审核日期
	private Date  departmentLeader1Date;//审核日期
	//原因分析
	private String remark2;//意见
	private String auditMan2;//改善担当
	private String auditMan2Login;//改善担当登录名
	private Date  auditMan2Date;//审核日期
	//纠正措施
	private String remark3;//意见
	private String attachment3;//附件
	private String uploadMan3;//上传人
	private String auditMan3;//改善担当
	private String auditMan3Login;//改善担当登录名
	private Date  auditMan3Date;//审核日期
	private String departmentLeader3;//审核员
	private String departmentLeader3Login;//审核员登录名
	private Date  departmentLeader3Date;//预计完成时间
	
	//措施完成情况
	private String remark4;//意见
	private String attachment4;//附件
	private String uploadMan4;//上传人
	private String auditMan4;//改善担当
	private String auditMan4Login;//改善担当登录名
	private Date  auditMan4Date;//审核日期
	private String departmentLeader4;//部门负责人
	private String departmentLeader4Login;//部门负责人登录名
	private Date  departmentLeader4Date;//审核日期
	
	//措施验证
	private String remark5;//意见
	private String attachment5;//附件
	private String uploadMan5;//上传人
	private String auditMan5;//审核员
	private String auditMan5Login;//审核员登录名
	private Date  auditMan5Date;//审核日期
	
	//问题关闭
	private String remark6;//意见
	private String attachment6;//附件
	private String uploadMan6;//上传人
	private String auditMan6;//组长
	private String auditMan6Login;//组长登录名
	private Date  auditMan6Date;//审核日期
	
	@OneToMany(mappedBy = "correctMeasures",cascade={CascadeType.ALL})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	List<CorrectMeasuresItem> correctMeasuresItems;//不符合条款
	
	@OneToMany(mappedBy = "correctMeasures",cascade={CascadeType.ALL})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	List<SignReasonItem> signReasonItems;//原因分析会签内容
	
	@OneToMany(mappedBy = "correctMeasures",cascade={CascadeType.ALL})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	List<SignMeasureItem> signMeasureItems;//纠正措施会签内容
	
	@OneToMany(mappedBy = "correctMeasures",cascade={CascadeType.ALL})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	List<SignCompleteItem> signCompleteItems;//完成情况会签内容
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getEnterpriseGroup() {
		return enterpriseGroup;
	}
	public void setEnterpriseGroup(String enterpriseGroup) {
		this.enterpriseGroup = enterpriseGroup;
	}
	public String getAuditDept() {
		return auditDept;
	}
	public void setAuditDept(String auditDept) {
		this.auditDept = auditDept;
	}
	public String getDepartmentLeader() {
		return departmentLeader;
	}
	public void setDepartmentLeader(String departmentLeader) {
		this.departmentLeader = departmentLeader;
	}
	public String getSetMan() {
		return setMan;
	}
	public void setSetMan(String setMan) {
		this.setMan = setMan;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public String getAttachment1() {
		return attachment1;
	}
	public void setAttachment1(String attachment1) {
		this.attachment1 = attachment1;
	}
	public String getInconformityType() {
		return inconformityType;
	}
	public void setInconformityType(String inconformityType) {
		this.inconformityType = inconformityType;
	}
	public String getAuditMan1() {
		return auditMan1;
	}
	public void setAuditMan1(String auditMan1) {
		this.auditMan1 = auditMan1;
	}
	public String getAuditMan1Login() {
		return auditMan1Login;
	}
	public void setAuditMan1Login(String auditMan1Login) {
		this.auditMan1Login = auditMan1Login;
	}
	public Date getAuditMan1Date() {
		return auditMan1Date;
	}
	public void setAuditMan1Date(Date auditMan1Date) {
		this.auditMan1Date = auditMan1Date;
	}

	public String getDepartmentLeaderLogin() {
		return departmentLeaderLogin;
	}
	public void setDepartmentLeaderLogin(String departmentLeaderLogin) {
		this.departmentLeaderLogin = departmentLeaderLogin;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Date getDepartmentLeader1Date() {
		return departmentLeader1Date;
	}
	public void setDepartmentLeader1Date(Date departmentLeader1Date) {
		this.departmentLeader1Date = departmentLeader1Date;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public String getAuditMan2() {
		return auditMan2;
	}
	public void setAuditMan2(String auditMan2) {
		this.auditMan2 = auditMan2;
	}
	public String getAuditMan2Login() {
		return auditMan2Login;
	}
	public void setAuditMan2Login(String auditMan2Login) {
		this.auditMan2Login = auditMan2Login;
	}
	public Date getAuditMan2Date() {
		return auditMan2Date;
	}
	public void setAuditMan2Date(Date auditMan2Date) {
		this.auditMan2Date = auditMan2Date;
	}
	public String getRemark3() {
		return remark3;
	}
	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}
	public String getAttachment3() {
		return attachment3;
	}
	public void setAttachment3(String attachment3) {
		this.attachment3 = attachment3;
	}
	public String getAuditMan3() {
		return auditMan3;
	}
	public void setAuditMan3(String auditMan3) {
		this.auditMan3 = auditMan3;
	}
	public String getAuditMan3Login() {
		return auditMan3Login;
	}
	public void setAuditMan3Login(String auditMan3Login) {
		this.auditMan3Login = auditMan3Login;
	}
	public Date getAuditMan3Date() {
		return auditMan3Date;
	}
	public void setAuditMan3Date(Date auditMan3Date) {
		this.auditMan3Date = auditMan3Date;
	}
	public String getDepartmentLeader3() {
		return departmentLeader3;
	}
	public void setDepartmentLeader3(String departmentLeader3) {
		this.departmentLeader3 = departmentLeader3;
	}
	public String getDepartmentLeader3Login() {
		return departmentLeader3Login;
	}
	public void setDepartmentLeader3Login(String departmentLeader3Login) {
		this.departmentLeader3Login = departmentLeader3Login;
	}
	public Date getDepartmentLeader3Date() {
		return departmentLeader3Date;
	}
	public void setDepartmentLeader3Date(Date departmentLeader3Date) {
		this.departmentLeader3Date = departmentLeader3Date;
	}
	public String getRemark4() {
		return remark4;
	}
	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}
	public String getAttachment4() {
		return attachment4;
	}
	public void setAttachment4(String attachment4) {
		this.attachment4 = attachment4;
	}
	public String getAuditMan4() {
		return auditMan4;
	}
	public void setAuditMan4(String auditMan4) {
		this.auditMan4 = auditMan4;
	}
	public String getAuditMan4Login() {
		return auditMan4Login;
	}
	public void setAuditMan4Login(String auditMan4Login) {
		this.auditMan4Login = auditMan4Login;
	}
	public Date getAuditMan4Date() {
		return auditMan4Date;
	}
	public void setAuditMan4Date(Date auditMan4Date) {
		this.auditMan4Date = auditMan4Date;
	}
	public String getDepartmentLeader4() {
		return departmentLeader4;
	}
	public void setDepartmentLeader4(String departmentLeader4) {
		this.departmentLeader4 = departmentLeader4;
	}
	public String getDepartmentLeader4Login() {
		return departmentLeader4Login;
	}
	public void setDepartmentLeader4Login(String departmentLeader4Login) {
		this.departmentLeader4Login = departmentLeader4Login;
	}
	public Date getDepartmentLeader4Date() {
		return departmentLeader4Date;
	}
	public void setDepartmentLeader4Date(Date departmentLeader4Date) {
		this.departmentLeader4Date = departmentLeader4Date;
	}
	public String getRemark5() {
		return remark5;
	}
	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}
	public String getAttachment5() {
		return attachment5;
	}
	public void setAttachment5(String attachment5) {
		this.attachment5 = attachment5;
	}
	public String getAuditMan5() {
		return auditMan5;
	}
	public void setAuditMan5(String auditMan5) {
		this.auditMan5 = auditMan5;
	}
	public String getAuditMan5Login() {
		return auditMan5Login;
	}
	public void setAuditMan5Login(String auditMan5Login) {
		this.auditMan5Login = auditMan5Login;
	}
	public Date getAuditMan5Date() {
		return auditMan5Date;
	}
	public void setAuditMan5Date(Date auditMan5Date) {
		this.auditMan5Date = auditMan5Date;
	}
	public String getRemark6() {
		return remark6;
	}
	public void setRemark6(String remark6) {
		this.remark6 = remark6;
	}
	public String getAttachment6() {
		return attachment6;
	}
	public void setAttachment6(String attachment6) {
		this.attachment6 = attachment6;
	}
	public String getAuditMan6() {
		return auditMan6;
	}
	public void setAuditMan6(String auditMan6) {
		this.auditMan6 = auditMan6;
	}
	public String getAuditMan6Login() {
		return auditMan6Login;
	}
	public void setAuditMan6Login(String auditMan6Login) {
		this.auditMan6Login = auditMan6Login;
	}
	public Date getAuditMan6Date() {
		return auditMan6Date;
	}
	public void setAuditMan6Date(Date auditMan6Date) {
		this.auditMan6Date = auditMan6Date;
	}
	public List<CorrectMeasuresItem> getCorrectMeasuresItems() {
		return correctMeasuresItems;
	}
	public void setCorrectMeasuresItems(
			List<CorrectMeasuresItem> correctMeasuresItems) {
		this.correctMeasuresItems = correctMeasuresItems;
	}
	public String getUploadMan1() {
		return uploadMan1;
	}
	public void setUploadMan1(String uploadMan1) {
		this.uploadMan1 = uploadMan1;
	}
	public String getUploadMan3() {
		return uploadMan3;
	}
	public void setUploadMan3(String uploadMan3) {
		this.uploadMan3 = uploadMan3;
	}
	public String getUploadMan4() {
		return uploadMan4;
	}
	public void setUploadMan4(String uploadMan4) {
		this.uploadMan4 = uploadMan4;
	}
	public String getUploadMan5() {
		return uploadMan5;
	}
	public void setUploadMan5(String uploadMan5) {
		this.uploadMan5 = uploadMan5;
	}
	public String getUploadMan6() {
		return uploadMan6;
	}
	public void setUploadMan6(String uploadMan6) {
		this.uploadMan6 = uploadMan6;
	}

	public List<SignReasonItem> getSignReasonItems() {
		return signReasonItems;
	}
	public void setSignReasonItems(List<SignReasonItem> signReasonItems) {
		this.signReasonItems = signReasonItems;
	}
	public List<SignMeasureItem> getSignMeasureItems() {
		return signMeasureItems;
	}
	public void setSignMeasureItems(List<SignMeasureItem> signMeasureItems) {
		this.signMeasureItems = signMeasureItems;
	}
	public List<SignCompleteItem> getSignCompleteItems() {
		return signCompleteItems;
	}
	public void setSignCompleteItems(List<SignCompleteItem> signCompleteItems) {
		this.signCompleteItems = signCompleteItems;
	}

}

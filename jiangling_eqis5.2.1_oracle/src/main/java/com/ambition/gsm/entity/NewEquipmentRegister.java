package com.ambition.gsm.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * 类名:NewEquipmentRegister
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：新设备申请登记</p>
 * @author  肖美华
 * @version 1.00 2016-11-16 发布
 */
@Entity
@Table(name="GSM_EQUIPMENT_REGISTER")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class NewEquipmentRegister extends WorkflowIdEntity{
	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY_LIST_CODE = "GSM_EQUIPMENT_REGISTER";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "新进检测设备申请登记表";//实体_列表_名称
	
	private String code;//表单编号
	private String businessDivision;//事业部
	private String applyDepartment;//申请部门
	private String proposer;//申请人
	private String proposerLogin;//申请人登录名
	private String jobNumber;//工号
	private Date applyDate;//申请日期
	private String opinion;//意见
	private String confirmor;//确认人
	private String confirmorLogin;//确认人登录名
	private Date affirmDate;//确认时间
	private String isMSA;//是否生成MSA
	//  workflowInfo.
	@OneToMany(mappedBy = "newEquipmentRegister",cascade=CascadeType.MERGE,fetch=FetchType.EAGER )
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @JsonIgnore()
    private List<NewEquipmentSublist> newEquipmentSublists;
	
	
	public String getProposerLogin() {
		return proposerLogin;
	}
	public void setProposerLogin(String proposerLogin) {
		this.proposerLogin = proposerLogin;
	}
	public String getConfirmorLogin() {
		return confirmorLogin;
	}
	public void setConfirmorLogin(String confirmorLogin) {
		this.confirmorLogin = confirmorLogin;
	}
	public List<NewEquipmentSublist> getNewEquipmentSublists() {
		return newEquipmentSublists;
	}
	public void setNewEquipmentSublists(
			List<NewEquipmentSublist> newEquipmentSublists) {
		this.newEquipmentSublists = newEquipmentSublists;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getBusinessDivision() {
		return businessDivision;
	}
	public void setBusinessDivision(String businessDivision) {
		this.businessDivision = businessDivision;
	}
	public String getApplyDepartment() {
		return applyDepartment;
	}
	public void setApplyDepartment(String applyDepartment) {
		this.applyDepartment = applyDepartment;
	}
	public String getProposer() {
		return proposer;
	}
	public void setProposer(String proposer) {
		this.proposer = proposer;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	public String getConfirmor() {
		return confirmor;
	}
	public void setConfirmor(String confirmor) {
		this.confirmor = confirmor;
	}
	public Date getAffirmDate() {
		return affirmDate;
	}
	public void setAffirmDate(Date affirmDate) {
		this.affirmDate = affirmDate;
	}
	public String getIsMSA() {
		return isMSA;
	}
	public void setIsMSA(String isMSA) {
		this.isMSA = isMSA;
	}
}



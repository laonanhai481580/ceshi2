package com.ambition.qsm.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:内审员备选库
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年10月31日 发布
 */
@Entity
@Table(name = "QSM_AUDITOR_LIBRARY")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class AuditorLibrary extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String enterpriseGroup;//事业群
	private String workNumber;//工号
	private String name;//姓名
	private String department;//部门
	private String position;//职务
	private String education;//学历
	private String workAge;//工作年限
	private String workExperience;//审核经验
	private String systemName;//体系名称
	private String certifyingAuthority;//发证机构
	private Date certifyingDate;//发证日期
	private String certificateNo;//证书编号
	private String certificate;//证书
	private String state;//状态
	private String remark;//备注
	public String getWorkNumber() {
		return workNumber;
	}
	public void setWorkNumber(String workNumber) {
		this.workNumber = workNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getWorkAge() {
		return workAge;
	}
	public void setWorkAge(String workAge) {
		this.workAge = workAge;
	}
	public String getWorkExperience() {
		return workExperience;
	}
	public void setWorkExperience(String workExperience) {
		this.workExperience = workExperience;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getCertifyingAuthority() {
		return certifyingAuthority;
	}
	public void setCertifyingAuthority(String certifyingAuthority) {
		this.certifyingAuthority = certifyingAuthority;
	}
	public Date getCertifyingDate() {
		return certifyingDate;
	}
	public void setCertifyingDate(Date certifyingDate) {
		this.certifyingDate = certifyingDate;
	}
	public String getCertificateNo() {
		return certificateNo;
	}
	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}
	public String getCertificate() {
		return certificate;
	}
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getEnterpriseGroup() {
		return enterpriseGroup;
	}
	public void setEnterpriseGroup(String enterpriseGroup) {
		this.enterpriseGroup = enterpriseGroup;
	}
	
	
}

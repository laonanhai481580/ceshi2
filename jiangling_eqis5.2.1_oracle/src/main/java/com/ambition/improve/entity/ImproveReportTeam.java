package com.ambition.improve.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 类名:8D改进报告团队
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月29日 发布
 */
@Entity
@Table(name = "IMP_8D_REPORT_TEAM")
public class ImproveReportTeam extends IdEntity {

	private static final long serialVersionUID = 1L;
	private String dutyMan;//负责人
	private String deprtment;//部门
	private String remark;//备注
	@ManyToOne
	@JoinColumn(name = "IMP_8D_REPORT_ID")
	@JsonIgnore()
	private ImproveReport improveReport;
	public String getDutyMan() {
		return dutyMan;
	}
	public void setDutyMan(String dutyMan) {
		this.dutyMan = dutyMan;
	}
	public String getDeprtment() {
		return deprtment;
	}
	public void setDeprtment(String deprtment) {
		this.deprtment = deprtment;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public ImproveReport getImproveReport() {
		return improveReport;
	}
	public void setImproveReport(ImproveReport improveReport) {
		this.improveReport = improveReport;
	}
	
	
	

	
	
	
	
}

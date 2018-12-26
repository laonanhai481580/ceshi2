package com.ambition.carmfg.entity;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ambition.product.base.IdEntity;

/**
 * 类名:巡检监控数据
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2013-9-5 发布
 */
@Entity
@Table(name = "MFG_PATROL_MONITOR")
public class PatrolMonitor extends IdEntity {
	private static final long serialVersionUID = -2246728528411244293L;
	private Long patrolInspectionReportId;//巡检报告名称
	private String inspectionNo;//检验报告编号
	private String workShop;//车间
	private String workProcedure;//发生工序
	private String bomCode;//产品编码
	private String bomName;//产品名称
	private String checker;//巡检员
	@Embedded
	private PatrolSettings patrolSettings;
	@Temporal(TemporalType.TIMESTAMP)
	private Date shouldPatrolDate;//应该检验的时间
	
	public Long getPatrolInspectionReportId() {
		return patrolInspectionReportId;
	}
	public void setPatrolInspectionReportId(Long patrolInspectionReportId) {
		this.patrolInspectionReportId = patrolInspectionReportId;
	}
	public Date getShouldPatrolDate() {
		return shouldPatrolDate;
	}
	public void setShouldPatrolDate(Date shouldPatrolDate) {
		this.shouldPatrolDate = shouldPatrolDate;
	}
	public String getWorkShop() {
		return workShop;
	}
	public void setWorkShop(String workShop) {
		this.workShop = workShop;
	}
	public String getWorkProcedure() {
		return workProcedure;
	}
	public void setWorkProcedure(String workProcedure) {
		this.workProcedure = workProcedure;
	}
	public String getBomCode() {
		return bomCode;
	}
	public void setBomCode(String bomCode) {
		this.bomCode = bomCode;
	}
	public String getBomName() {
		return bomName;
	}
	public void setBomName(String bomName) {
		this.bomName = bomName;
	}
	public PatrolSettings getPatrolSettings() {
		return patrolSettings;
	}
	public void setPatrolSettings(PatrolSettings patrolSettings) {
		this.patrolSettings = patrolSettings;
	}
	public String getChecker() {
		return checker;
	}
	public void setChecker(String checker) {
		this.checker = checker;
	}
	public String getInspectionNo() {
		return inspectionNo;
	}
	public void setInspectionNo(String inspectionNo) {
		this.inspectionNo = inspectionNo;
	}
	
}

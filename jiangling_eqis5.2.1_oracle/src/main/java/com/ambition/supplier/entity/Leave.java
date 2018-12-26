package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ambition.product.base.IdEntity;
import com.ambition.supplier.utils.DateUtil;
import com.norteksoft.wf.engine.client.ExtendField;
import com.norteksoft.wf.engine.client.FormFlowable;
import com.norteksoft.wf.engine.client.WorkflowInfo;

/**
 * 请假(流程测试)
 * @author 赵骏
 *
 */
@Entity
@Table(name = "SUPPLIER_LEAVE")
public class Leave extends IdEntity implements FormFlowable{
	private static final long serialVersionUID = -9026500363557759006L;
	private String leaveUser;//请假人员
	@Temporal(TemporalType.TIMESTAMP)
	private Date leaveTime;//请假时间
	private Double leaveDays;//请假天数
	private String result;//结果
	private String attachments;//附件
	@Embedded
	private WorkflowInfo workflowInfo;

	@Embedded
	private ExtendField extendField;
	
	public String getLeaveUser() {
		return leaveUser;
	}
	public void setLeaveUser(String leaveUser) {
		this.leaveUser = leaveUser;
	}
	public Date getLeaveTime() {
		return leaveTime;
	}
	public void setLeaveTime(Date leaveTime) {
		this.leaveTime = leaveTime;
	}
	public Double getLeaveDays() {
		return leaveDays;
	}
	
	public void setLeaveDays(Double leaveDays) {
		this.leaveDays = leaveDays;
	}
	
	public String getLeaveTimeStr() {
		return DateUtil.formateTimeStr(leaveTime);
	}
	public WorkflowInfo getWorkflowInfo() {
		return workflowInfo;
	}
	public ExtendField getExtendField() {
		return extendField;
	}
	public void setWorkflowInfo(WorkflowInfo workflowinfo) {
		this.workflowInfo = workflowinfo;
	}
	public void setExtendField(ExtendField extendfield) {
		this.extendField = extendfield;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getAttachments() {
		return attachments;
	}
	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}
	
}

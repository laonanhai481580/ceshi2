package com.ambition.product.workflow;

import java.util.Date;

import javax.persistence.Column;

import com.ambition.product.base.WorkflowIdEntity;

/**
 * 类名:节点超期统计供前台显示的模型
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2015-4-20 发布
 */
public class AmbWorkflowTask {
	private Long id;//ID
	private Long formId;//表单ID
	private String formNo;//表单编号 
	private Date beginDate;//创建日期/开始日期
	private String transactor;//执行人
	private Date transactDate;//执行日期
	private Date planDate;//计划日期
	private String taskName;//任务名称
	private Integer overdueHour;//超期小时
	private Integer overdueDay;//超期天数
	private Integer duedate;//限定天数
	@Column(length=10)
	private Integer ontimeState=WorkflowIdEntity.ONTIMESTATE_CONDUCT;//是否按时完成状态，0:逾期未完成；1:逾期完成；2:按时完成；3:进行中
	private Integer isOverdue=WorkflowIdEntity.ISOVERDUE_YES;//是否超期
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getFormId() {
		return formId;
	}
	public void setFormId(Long formId) {
		this.formId = formId;
	}
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public Date getTransactDate() {
		return transactDate;
	}
	public void setTransactDate(Date transactDate) {
		this.transactDate = transactDate;
	}
	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Integer getOntimeState() {
		return ontimeState;
	}
	public void setOntimeState(Integer ontimeState) {
		this.ontimeState = ontimeState;
	}
	public Integer getOverdueHour() {
		return overdueHour;
	}
	public void setOverdueHour(Integer overdueHour) {
		this.overdueHour = overdueHour;
	}
	public Integer getOverdueDay() {
		return overdueDay;
	}
	public void setOverdueDay(Integer overdueDay) {
		this.overdueDay = overdueDay;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public String getTransactor() {
		return transactor;
	}
	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}
	public Integer getDuedate() {
		return duedate;
	}
	public void setDuedate(Integer duedate) {
		this.duedate = duedate;
	}
	public Integer getIsOverdue() {
		return isOverdue;
	}
	public void setIsOverdue(Integer isOverdue) {
		this.isOverdue = isOverdue;
	}
}

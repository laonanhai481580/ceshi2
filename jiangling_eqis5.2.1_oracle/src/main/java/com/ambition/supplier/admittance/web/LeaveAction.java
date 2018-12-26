package com.ambition.supplier.admittance.web;

import java.util.Date;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.admittance.service.LeaveManager;
import com.ambition.supplier.entity.Leave;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.product.api.entity.WorkflowTask;

/**
 * 供应商考察记录
 * @author 赵骏
 *
 */
@Namespace("/supplier/admittance/leave")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "supplier/admittance/leave", type = "redirectAction") })
public class LeaveAction extends CrudActionSupport<Leave> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;

	// 工作流任务id
	private Long taskId;
	// 点击的按钮
	private TaskProcessingResult taskTransact;
	
	// 字段权限
	private String fieldPermission;
	
	private WorkflowTask task;

	private Leave leave;
	
	@Autowired
	private LeaveManager leaveManager;

	private Page<Leave> page;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public TaskProcessingResult getTaskTransact() {
		return taskTransact;
	}

	public void setTaskTransact(TaskProcessingResult taskTransact) {
		this.taskTransact = taskTransact;
	}

	public String getFieldPermission() {
		return fieldPermission;
	}

	public void setFieldPermission(String fieldPermission) {
		this.fieldPermission = fieldPermission;
	}

	public WorkflowTask getTask() {
		return task;
	}

	public void setTask(WorkflowTask task) {
		this.task = task;
	}

	public Leave getLeave() {
		return leave;
	}

	public void setLeave(Leave leave) {
		this.leave = leave;
	}

	public Page<Leave> getPage() {
		return page;
	}

	public void setPage(Page<Leave> page) {
		this.page = page;
	}

	@Override
	public Leave getModel() {
		return leave;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			leave = new Leave();
			leave.setCompanyId(ContextUtils.getCompanyId());
			leave.setCreatedTime(new Date());
			leave.setCreator(ContextUtils.getUserName());
			leave.setLastModifier(ContextUtils.getUserName());
			leave.setBusinessUnitName(ContextUtils.getSubCompanyName());
			leave.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			leave = leaveManager.getLeave(id);
		}
		fieldPermission = leaveManager.getFieldPermission(taskId);
	}

	@Action("input")
	@Override
	public String input() throws Exception {
		if(id == null){
			leave.setLeaveUser(ContextUtils.getUserName());
			leave.setLeaveTime(new Date());
		}
		return SUCCESS;
	}
	@Action("view")
	public String view() throws Exception {
		return SUCCESS;
	}
	@Action("save")
	@Override
	public String save() throws Exception {
		try {
			leaveManager.saveLeave(leave);
			renderText(leave.getId().toString());
		} catch (Exception e) {
			e.printStackTrace();
			renderText("{\"error\":\"true\",\"message\":\"保存失败,"+e.getMessage()+"\"}");
		}
		return null;
	}

	public void prepareProcessTask() throws Exception {
		if (id != null) {
			leave = leaveManager.getLeave(id);
		} else if (taskId != null) {
			leave = leaveManager.getLeaveByTaskId(taskId);
		}
		// 需我办理的任务
		if (taskId == null) {
			task = leaveManager.getMyTask(leave,
					ContextUtils.getLoginName());
			taskId = task.getId();
		} else {
			task = leaveManager.getWorkflowTask(taskId);
		}
	}

	// 任务办理界面
	@Action("process-task")
	public String processTask() throws Exception {
		return SUCCESS;
	}

	public void prepareCompleteTask() throws Exception {
		prepareProcessTask();
	}

	// 完成任务
	@Action("complete-task")
	public String completeTask() throws Exception {
		leaveManager.completeTask(leave, taskId, taskTransact);
		addActionMessage("提交成功！");
		return "process-task";
	}

	public void prepareSubmitProcess() throws Exception {
		prepareModel();
	}

	// 第一次提交
	@Action("submit-process")
	public String submitProcess() throws Exception {
		leaveManager.submitProcess(leave);
		//得到task控制页面上显示取回按钮
		task = leaveManager.getWorkflowTask(leave
				.getWorkflowInfo().getFirstTaskId());
		//控制页面上的字段都不能编辑
		fieldPermission = leaveManager.getFieldPermission(leave
				.getWorkflowInfo().getFirstTaskId());
		addActionMessage("提交成功！");
		return "input";
	}

	@Action("delete")
	@Override
	public String delete() throws Exception {
		try {
			leaveManager.deleteLeaves(deleteIds);
		} catch (Exception e) {
			e.printStackTrace();
			renderText("删除失败:" + e.getMessage());
		}
		return null;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Action("list-datas")
	public String getListDatas() throws Exception {
		try {
			page = leaveManager.search(page);
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}

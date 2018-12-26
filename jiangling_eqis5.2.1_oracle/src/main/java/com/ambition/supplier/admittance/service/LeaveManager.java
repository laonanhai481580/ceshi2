package com.ambition.supplier.admittance.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.admittance.dao.LeaveDao;
import com.ambition.supplier.entity.Leave;
import com.norteksoft.product.orm.Page;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.wf.engine.client.FormFlowableDeleteInterface;
import com.norteksoft.wf.engine.client.OnExecutingTransation;

@Service
@Transactional
public class LeaveManager implements FormFlowableDeleteInterface,OnExecutingTransation{
	
	@Autowired
	private LeaveDao leaveDao;
	
	/**
	 * 查询供应商考察记录
	 * @param page
	 * @return
	 */
	public Page<Leave> search(Page<Leave> page){
		return leaveDao.search(page);
	}
	
	public Leave getLeave(Long id){
		return leaveDao.get(id);
	}
	/**
	 * 保存考察报告
	 * @param inspectionReport
	 * @param params
	 */
	public void saveLeave(Leave leave){
		leaveDao.save(leave);
	}
	public void deleteLeaves(String deleteIds){
		for(String id : deleteIds.split(",")){
			if(StringUtils.isNotEmpty(id)){
				Leave leave = getLeave(Long.valueOf(id));
				try {
					if(leave.getWorkflowInfo() != null){
						ApiFactory.getInstanceService()
						.deleteInstance(leave);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				leaveDao.delete(leave);
			}
		}
	}
	@Transactional
	public void submitProcess(Leave leave) {
		long processId=ApiFactory.getDefinitionService().getEnabledHighestVersionWorkflowDefinition("supplier-leave").getId();
		try {
			ApiFactory.getInstanceService().submitInstance(processId,leave);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	public void completeTask(Leave leave, Long taskId,
			TaskProcessingResult taskTransact) {
		ApiFactory.getTaskService().completeWorkflowTask(taskId,
				taskTransact);
	}

	/**
	 * 获得任务是否已完成，已取消，已指派，他人已领取状态
	 * 
	 * @param task
	 * @return
	 */
	private boolean isTaskComplete(WorkflowTask task) {
		return task.getActive().equals(2) || task.getActive().equals(3)
		|| task.getActive().equals(5) || task.getActive().equals(7);
	}

	public String getFieldPermission(Long taskId) {
		if (taskId == null) {
			Long processId = ApiFactory.getDefinitionService()
			.getWorkflowDefinitionsByCode("supplier-leave").get(0)
			.getId();
			return ApiFactory.getFormService()
			.getFieldPermissionNotStarted(
					processId);
		} else {
			WorkflowTask task = ApiFactory.getTaskService().getTask(taskId);
			if (isTaskComplete(task)) {
				return ApiFactory.getFormService()
				.getFieldPermission(false);
			} else {
				return ApiFactory.getFormService().getFieldPermission(
						taskId);
			}
		}
	}

	/**
	 * 根据任务获取实例
	 * @param taskId
	 * @return
	 */
	public Leave getLeaveByTaskId(Long taskId) {
		return getLeave(ApiFactory.getFormService()
				.getFormFlowableIdByTask(taskId));
	}

	/**
	 * 获得loginName用户的该实例的当前任务
	 * 
	 * @param taskId
	 * @return
	 */
	public WorkflowTask getMyTask(Leave leave, String loginName) {
		return ApiFactory.getTaskService().getActiveTaskByLoginName(leave,
				loginName);
	}

	/**
	 * 获得任务
	 * 
	 * @param taskId
	 * @return
	 */
	public WorkflowTask getWorkflowTask(Long taskId) {
		return ApiFactory.getTaskService().getTask(taskId);
	}

	/*
	 * 删除流程实例时的回调方法（在流程参数中配置了beanName）
	 * 
	 * @see com.norteksoft.wf.engine.client.FormFlowableDeleteInterface#
	 * deleteFormFlowable(java.lang.Long)
	 */
	@Override
	public void deleteFormFlowable(Long id) {
		leaveDao.delete(id);
	}
	
	public void execute(Long taskId) {
		Leave leave = getLeave(taskId);
		@SuppressWarnings("unused")
		String name = leave.getWorkflowInfo().getProcessState().name();
//		WorkflowTask task = workflowEngine.getTaskService().getTask(taskId);
//		
//		processInstance.getProcessInstance().
//		workflowEngine.getTaskService().
//		workflowEngine.
//		if(task.getCode() != null){
//			if("edit".equals(task.getCode())){
//				inspectionReport.setInspectionState(InspectionReport.STATE_COUNTERSIGN);
//				inspectionReportDao.save(inspectionReport);
//			}else if("countersign".equals(task.getCode())){
//				inspectionReport.setInspectionState(InspectionReport.STATE_AUDIT);
//				inspectionReportDao.save(inspectionReport);
//			}else if("audit".equals(task.getCode())){
//				if(taskTransact.getName().equals(TaskTransact.APPROVE.getName())){
//					inspectionReport.setInspectionState(InspectionReport.STATE_PASS);
//					if(Supplier.STATE_POTENTIAL.equals(inspectionReport.getSupplier().getState())){//如果是潜在供应商,则改为准供应商
//						inspectionReport.getSupplier().setState(Supplier.STATE_ALLOW);
//						supplierDao.save(inspectionReport.getSupplier());
//					}
//				}else{
//					inspectionReport.setInspectionState(InspectionReport.STATE_FAIL);
//				}
//				inspectionReportDao.save(inspectionReport);
//				
//				//更新供应产品的状态
//				String productIds = inspectionReport.getInspectionSupplyProductIds();
//				for(String supplyProductId : productIds.split(",")){
//					if(StringUtils.isNotEmpty(supplyProductId)){
//						SupplyProduct supplyProduct = supplyProductDao.get(Long.valueOf(supplyProductId));
//						if(taskTransact.getName().equals(TaskTransact.APPROVE.getName())){
//							supplyProduct.setApplyState(SupplyProduct.APPLYSTATE_INSPECTPASS);
//						}else{
//							supplyProduct.setApplyState(SupplyProduct.APPLYSTATE_INSPECTFAIL);
//						}
//						supplyProductDao.save(supplyProduct);
//					}
//				}
//			}
//		}
	}
}

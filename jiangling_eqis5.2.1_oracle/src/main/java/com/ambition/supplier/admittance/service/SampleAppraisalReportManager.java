package com.ambition.supplier.admittance.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.supplier.admittance.dao.AppraisalReportDao;
import com.ambition.supplier.archives.dao.SupplierDao;
import com.ambition.supplier.entity.AppraisalReport;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.manager.service.ProductExploitationRecordManager;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.engine.client.FormFlowableDeleteInterface;

@Service
@Transactional
public class SampleAppraisalReportManager implements FormFlowableDeleteInterface{
	/**样件鉴定流程编码*/                                              
	public static final String SAMPLE_APPRAISAL_DEFINITION_CODE = "sample-appraisal-report";
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	
	@Autowired
	private AppraisalReportDao appraisalReportDao;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	@Autowired
	private UseFileManager useFileManager;
	
	@Autowired
	private SupplierDao supplierDao;
	
	@Autowired
	private ProductExploitationRecordManager productExploitationRecordManager;
	
	/**
	 * 查询供应商鉴定记录
	 * @param page
	 * @return
	 */
	public Page<AppraisalReport> search(Page<AppraisalReport> page){
		return appraisalReportDao.search(page,AppraisalReport.TYPE_SAMPLE);
	}
	
	public AppraisalReport getAppraisalReport(Long id){
		return appraisalReportDao.get(id);
	}
	public AppraisalReport getAppraisalReportById(Long id){
		List<AppraisalReport> reports = appraisalReportDao.find("from AppraisalReport a where a.id = ?", id);
		if(reports.isEmpty()){
			return null;
		}else{
			return reports.get(0);
		}
	}
	/**
	 * 保存样件鉴定
	 * @param appraisalReport
	 */
	public void saveAppraisalReport(AppraisalReport appraisalReport){
		if(appraisalReport.getId()==null){
//			appraisalReport.setCode(formCodeGenerated.generateInspectionReportCode());
		}
		//设置试用人员字符串
		appraisalReport.setAppointTestLoginName(CommonUtil.getSimpleName(appraisalReport.getAppointTestLoginName()));
		appraisalReport.setTestMan(appraisalReport.getAppointTestMan());
		//试用结果会签
		appraisalReport.setEvaluationMemberLoginNames(CommonUtil.getSimpleName(appraisalReport.getEvaluationMemberLoginNames()));
		//设置审核人员字符串
		appraisalReport.setAuditManLoginName(CommonUtil.getSimpleName(appraisalReport.getAuditManLoginName()));
		//设置试用申请审核人员
		appraisalReport.setApplyAuditLoginName(CommonUtil.getSimpleName(appraisalReport.getApplyAuditLoginName()));
		
		appraisalReport.setLastModifiedTime(new Date());
		appraisalReport.setLastModifier(ContextUtils.getUserName());
		appraisalReportDao.save(appraisalReport);
		//更新文件附件的状态,不使用的文件,并设置新的文件为使用
		useFileManager.useAndCancelUseFiles(Struts2Utils.getParameter("hisAttachmentFiles"),appraisalReport.getAttachmentFiles());
		//设置物料开发状态为正在样件鉴定
		productExploitationRecordManager.updateApplyStateBySupplierAndProductBom(appraisalReport.getSupplier(),appraisalReport.getBomCodes());
	}
	
	public void deleteAppraisalReports(String deleteIds){
		for(String id : deleteIds.split(",")){
			if(StringUtils.isNotEmpty(id)){
				AppraisalReport appraisalReport = getAppraisalReportById(Long.valueOf(id));
				if(appraisalReport != null){
					ApiFactory.getInstanceService().deleteInstance(appraisalReport);
					//删除使用的文件
					useFileManager.useAndCancelUseFiles(appraisalReport.getAttachmentFiles(),null);
					//设置物料开发状态为正在样件鉴定
					productExploitationRecordManager.updateApplyStateBySupplierAndProductBom(appraisalReport.getSupplier(),appraisalReport.getBomCodes());
				}
				//日志
				logUtilDao.debugLog("删除", appraisalReport.toString());
			}
		}
	}
	
	
	public void submitProcess(AppraisalReport appraisalReport) {
		saveAppraisalReport(appraisalReport);
		Long processId=ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(SAMPLE_APPRAISAL_DEFINITION_CODE).get(0).getId();
		ApiFactory.getInstanceService().submitInstance(processId,
				appraisalReport);
	}
	
	public void completeTask(AppraisalReport appraisalReport, Long taskId,
			TaskProcessingResult taskTransact) {
		saveAppraisalReport(appraisalReport);
		ApiFactory.getTaskService().completeWorkflowTask(taskId,
				taskTransact);
		//替换不同意等编码
		appraisalReport.setQualityCountersigns(CommonUtil.replaceTaskResult(appraisalReport.getQualityCountersigns()));
		appraisalReport.setResultAuditMan(CommonUtil.replaceTaskResult(appraisalReport.getResultAuditMan()));
		appraisalReport.setResultApproveMan(CommonUtil.replaceTaskResult(appraisalReport.getResultApproveMan()));
		appraisalReportDao.save(appraisalReport);
		
		String opinion = Struts2Utils.getParameter("opinion");
		Opinion opinionParameter = new Opinion();
		opinionParameter.setCustomField(Struts2Utils.getParameter("operateName"));
		opinionParameter.setOpinion(opinion);
		opinionParameter.setTaskId(taskId);
		opinionParameter.setAddOpinionDate(new Date());
		ApiFactory.getOpinionService().saveOpinion(opinionParameter);
		
		//结果完成
		if(ApiFactory.getInstanceService().isInstanceComplete(appraisalReport)){
			if(TaskProcessingResult.APPROVE.name().equals(taskTransact.name())){
				appraisalReport.setAppraisalState(AppraisalReport.STATE_PASS);
			}else{
				appraisalReport.setAppraisalState(AppraisalReport.STATE_FAIL);
			}
			appraisalReport.setAuditDate(new Date());
			appraisalReportDao.save(appraisalReport);
			//更新开发状态
			productExploitationRecordManager.updateApplyStateBySupplierAndProductBom(appraisalReport.getSupplier(),appraisalReport.getBomCodes());
			
			if(AppraisalReport.STATE_PASS.equals(appraisalReport.getAppraisalState())&&AppraisalReport.RESULT_PASS.equals(appraisalReport.getAppraisalResult())){
				Supplier supplier = appraisalReport.getSupplier();
				if(Supplier.STATE_POTENTIAL.equals(supplier.getState())){
					supplier.setState(Supplier.STATE_ALLOW);
					supplierDao.save(supplier);
				}
			}
		}
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
			.getWorkflowDefinitionsByCode(SAMPLE_APPRAISAL_DEFINITION_CODE).get(0)
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
	public AppraisalReport getAppraisalReportByTaskId(Long taskId) {
		return getAppraisalReport(ApiFactory.getFormService()
				.getFormFlowableIdByTask(taskId));
	}

	/**
	 * 获得loginName用户的该实例的当前任务
	 * 
	 * @param taskId
	 * @return
	 */
	public WorkflowTask getMyTask(AppraisalReport appraisalReport, String loginName) {
		return ApiFactory.getTaskService().getActiveTaskByLoginName(appraisalReport,
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
		AppraisalReport report = getAppraisalReportById(id);
		if(report != null){
			appraisalReportDao.delete(report);
		}
	}
	
	public void deleteReportByTaskId(Long taskId){
		WorkflowTask task = getWorkflowTask(taskId);
		if(task != null){
			ApiFactory.getInstanceService().deleteInstance(task.getProcessInstanceId());
		}
	}
	/**
	 * 转换json
	 * @param params
	 * @return
	 */
	public JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		if(params == null){
			return resultJson;
		}
		for(Object key : params.keySet()){
			resultJson.put(key,params.getJSONArray(key.toString()).get(0));
		}
		return resultJson;
	}
	
	/**
	 * 获取考察报告的状态列表
	 * @return
	 */
	public List<Option> getAppraisStateOptions(){
		List<Option> options = new ArrayList<Option>();
		Option option = new Option();
		option.setName(AppraisalReport.STATE_DEFAULT);
		option.setValue(AppraisalReport.STATE_DEFAULT);
		options.add(option);
		
		option = new Option();
		option.setName(AppraisalReport.STATE_WAITAUDIT);
		option.setValue(AppraisalReport.STATE_WAITAUDIT);
		options.add(option);
		
		option = new Option();
		option.setName(AppraisalReport.STATE_PASS);
		option.setValue(AppraisalReport.STATE_PASS);
		options.add(option);
		
		option = new Option();
		option.setName(AppraisalReport.STATE_FAIL);
		option.setValue(AppraisalReport.STATE_FAIL);
		options.add(option);
		
		return options;
	}
}

package com.ambition.supplier.admittance.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.carmfg.bom.service.ProductBomManager;
import com.ambition.carmfg.entity.ProductBom;
import com.ambition.supplier.admittance.dao.AppraisalReportDao;
import com.ambition.supplier.admittance.dao.ProductAdmittanceRecordDao;
import com.ambition.supplier.archives.dao.SupplierDao;
import com.ambition.supplier.archives.service.SupplierManager;
import com.ambition.supplier.entity.AppraisalReport;
import com.ambition.supplier.entity.ProductAdmittanceRecord;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.manager.service.ProductExploitationRecordManager;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.exception.AmbFrameException;
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

/**
 * 小批鉴定阶段Manager
 * @author 赵骏
 *
 */
@Service
@Transactional
public class SublotsAppraisalReportManager implements FormFlowableDeleteInterface{
	public static final String SUBLOTS_APPRAISAL_DEFINITION_CODE = "sublots-appraisal-report1";
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	
	@Autowired
	private AppraisalReportDao appraisalReportDao;
	
	@Autowired
	private ProductBomManager bomManager;
	@Autowired
	private LogUtilDao logUtilDao;
	
	@Autowired
	private UseFileManager useFileManager;
	
	@Autowired
	private ProductExploitationRecordManager productExploitationRecordManager;
	
	@Autowired
	private ProductAdmittanceRecordDao productAdmittanceRecordDao;
	
	@Autowired
	private SupplierDao supplierDao;
	
	@Autowired
	private SupplierManager supplierManager;
	/**
	 * 查询供应商鉴定记录
	 * @param page
	 * @return
	 */
	public Page<AppraisalReport> search(Page<AppraisalReport> page){
		return appraisalReportDao.search(page,AppraisalReport.TYPE_SUBLOTS);
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
	 * 保存小批鉴定
	 * @param appraisalReport
	 */
	public void saveAppraisalReport(AppraisalReport appraisalReport){
		if(appraisalReport.getId()==null){
			appraisalReport.setType(AppraisalReport.TYPE_SUBLOTS);
//			appraisalReport.setCode(formCodeGenerated.generateSublotsAppraisalReportCode());
		}
		//设置会签人员字符串
		appraisalReport.setTestManLoginName(CommonUtil.getSimpleName(appraisalReport.getTestManLoginName()));
		//试装结果会签
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
		//日志
		logUtilDao.debugLog("保存", appraisalReport.toString());
	}
	
	public void deleteAppraisalReports(String deleteIds){
		for(String id : deleteIds.split(",")){
			if(StringUtils.isNotEmpty(id)){
				AppraisalReport appraisalReport = getAppraisalReport(Long.valueOf(id));
				if(appraisalReport.getWorkflowInfo() != null){
					ApiFactory.getInstanceService().deleteInstance(appraisalReport);
				}
				appraisalReportDao.delete(appraisalReport);
				//删除使用的文件
				useFileManager.useAndCancelUseFiles(appraisalReport.getAttachmentFiles(),null);
				//设置物料开发状态为正在样件鉴定
				productExploitationRecordManager.updateApplyStateBySupplierAndProductBom(appraisalReport.getSupplier(),appraisalReport.getBomCodes());
				//日志
				logUtilDao.debugLog("删除", appraisalReport.toString());
			}
		}
	}
	/**
	 * 根据供应商编号和物料编码查询最新的小批鉴定阶段
	 * @param supplierId
	 * @param bomCode
	 * @return
	 */
	public Integer getTimeOfPhase(Long supplierId,String bomCode){
		return getTimeOfPhase(supplierId, bomCode,AppraisalReport.TYPE_SUBLOTS);
	}
	
	/**
	 * 根据供应商编号和物料编码查询最新的小批鉴定阶段
	 * @param supplierId
	 * @param bomCode
	 * @return
	 */
	public Integer getTimeOfPhase(Long supplierId,String bomCode,String type){
		String hql = "from AppraisalReport a where a.type = ? and a.companyId = ? and a.supplier.id = ? and a.bomCodes = ? order by a.timeOfPhase desc";
		List<AppraisalReport> appraisalReports = appraisalReportDao.find(hql,type,ContextUtils.getCompanyId(),supplierId,bomCode);
		if(appraisalReports.isEmpty()){
			return 1;
		}else{
			if(appraisalReports.get(0).getTimeOfPhase() == null){
				return 1;
			}else{
				return appraisalReports.get(0).getTimeOfPhase()+1;
			}
		}
	}
	
	/**
	 * 根据供应商编号和物料编码查询准入的情况,ok表示可以准入,为空时表示不可以准入,其他情况表示已经准入
	 * @param supplierId
	 * @param bomCode
	 * @return
	 */
	public String getAdmittanceState(Long supplierId,String bomCode){
		ProductAdmittanceRecord productAdmittanceRecord = getProductAdmittanceRecordBySupplier(supplierId,bomCode);
		if(productAdmittanceRecord==null){
			ProductBom bom = bomManager.getProductBomByBomCode(bomCode);
			String importance = bom == null?"":bom.getImportance();
			Map<String,Object> map = new HashMap<String, Object>();
			supplierManager.setSupplyProductState(supplierManager.getSupplier(supplierId), importance, bomCode,"admittance",map);
			return map.get("errorMessage").toString();
		}else{
			return "already";
		}
	}
	
	/**
	 * 根据小批鉴定的编号准入物料
	 * @param sublotsAppraisalReportId
	 */
	public void admittance(Long sublotsAppraisalReportId){
		AppraisalReport sublotsAppraisalReport = appraisalReportDao.get(sublotsAppraisalReportId);
		ProductAdmittanceRecord productAdmittanceRecord = getProductAdmittanceRecordBySupplier(sublotsAppraisalReport.getSupplier().getId(),sublotsAppraisalReport.getBomCodes());
		if(productAdmittanceRecord != null){
			throw new RuntimeException("该物料已准入!");
		}else{
			Map<String,Object> map = new HashMap<String, Object>();
			supplierManager.setSupplyProductState(sublotsAppraisalReport.getSupplier(),sublotsAppraisalReport.getImportance(),sublotsAppraisalReport.getBomCodes(),"admittance", map);
			String errorMessage = map.get("errorMessage").toString();
			if(StringUtils.isNotEmpty(errorMessage)){
				throw new AmbFrameException("该物料不能准入," + errorMessage);
			}
			productAdmittanceRecord = new ProductAdmittanceRecord();
			productAdmittanceRecord.setCompanyId(ContextUtils.getCompanyId());
			productAdmittanceRecord.setCreatedTime(new Date());
			productAdmittanceRecord.setCreator(ContextUtils.getUserName());
			productAdmittanceRecord.setLastModifiedTime(productAdmittanceRecord.getCreatedTime());
			productAdmittanceRecord.setLastModifier(productAdmittanceRecord.getCreator());
			productAdmittanceRecord.setSupplier(sublotsAppraisalReport.getSupplier());
			productAdmittanceRecord.setCode(sublotsAppraisalReport.getBomCodes());
			productAdmittanceRecord.setName(sublotsAppraisalReport.getBomNames());
//			productAdmittanceRecord.setImportance(sublotsAppraisalReport.getBomLevel());
			productAdmittanceRecord.setMaterialType(sublotsAppraisalReport.getMaterialType());
			productAdmittanceRecord.setAdmissionDate(new Date());
			productAdmittanceRecordDao.save(productAdmittanceRecord);
			//更新开发状态
			productExploitationRecordManager.updateApplyStateBySupplierAndProductBom(sublotsAppraisalReport.getSupplier(),sublotsAppraisalReport.getBomCodes());
			
			//如果供应商的状态为准供应商,则将其列入合格供应商
			if(Supplier.STATE_ALLOW.equals(sublotsAppraisalReport.getSupplier().getState())){
				Supplier supplier = sublotsAppraisalReport.getSupplier();
				supplier.setState(Supplier.STATE_QUALIFIED);
				supplierDao.save(supplier);
			}
		}
	}
	
	/**
	 * 根据供应商编号和物料编码取消准入
	 * @param supplierId
	 * @param bomCode
	 */
	public void cancelAdmittance(Long sublotsAppraisalReportId){
		AppraisalReport sublotsAppraisalReport = appraisalReportDao.get(sublotsAppraisalReportId);
		ProductAdmittanceRecord productAdmittanceRecord = getProductAdmittanceRecordBySupplier(sublotsAppraisalReport.getSupplier().getId(),sublotsAppraisalReport.getBomCodes());
		if(productAdmittanceRecord != null){
			productAdmittanceRecordDao.delete(productAdmittanceRecord);
			//更新开发状态
			productExploitationRecordManager.updateApplyStateBySupplierAndProductBom(sublotsAppraisalReport.getSupplier(),sublotsAppraisalReport.getBomCodes());
			//如果供应商的状态为合格供应商,则检查其是否还有其他的准入产品,如果没有,则纳入准供应商
			if(Supplier.STATE_QUALIFIED.equals(sublotsAppraisalReport.getSupplier().getState())){
				Supplier supplier = sublotsAppraisalReport.getSupplier();
				String hql = "from ProductAdmittanceRecord p where p.supplier = ?";
				@SuppressWarnings("unchecked")
				List<ProductAdmittanceRecord> productAdmittanceRecords = productAdmittanceRecordDao.createQuery(hql,supplier).list();
				if(productAdmittanceRecords.isEmpty()){
					supplier.setState(Supplier.STATE_ALLOW);
					supplierDao.save(supplier);
				}
			}
		}
	}
	
	/**
	 * 查询正在进行小批鉴定的物料
	 * @param supplier
	 * @param bomCode
	 * @return
	 */
	public List<AppraisalReport> getProcessSublotsReport(Supplier supplier,String bomCode){
		String hql = "from AppraisalReport a where a.type = ? and a.companyId = ? and a.supplier = ? and a.bomCodes = ? and a.appraisalState <> ? and a.appraisalState <> ?";
		return appraisalReportDao.find(hql,AppraisalReport.TYPE_SUBLOTS,ContextUtils.getCompanyId(),supplier,bomCode,AppraisalReport.STATE_FAIL,AppraisalReport.STATE_PASS);
	}
	
	/**
	 * 查询供应商小批鉴定记录
	 * @param page
	 * @return
	 */
	public List<AppraisalReport> queryOtherSublotsAppraisalReports(Long id,Long supplierId,String bomCode){
		String hql = "from AppraisalReport a where a.type = ? and a.supplier.id = ? and a.bomCodes = ? and a.id <> ? order by a.timeOfPhase";
		return appraisalReportDao.find(hql,AppraisalReport.TYPE_SUBLOTS,supplierId,bomCode,id);
	}
	
	/**
	 * 查询供应商鉴定记录
	 * @param page
	 * @return
	 */
	public ProductAdmittanceRecord getProductAdmittanceRecordBySupplier(Long supplierId,String bomCode){
		String hql = "from ProductAdmittanceRecord p where p.supplier.id = ? and p.code = ?";
		List<ProductAdmittanceRecord> productAdmittanceRecords = appraisalReportDao.find(hql,supplierId,bomCode);
		if(productAdmittanceRecords.isEmpty()){
			return null;
		}else{
			return productAdmittanceRecords.get(0);
		}
	}
	
	public void submitProcess(AppraisalReport appraisalReport) {
		saveAppraisalReport(appraisalReport);
		try {
			Long processId=ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(SUBLOTS_APPRAISAL_DEFINITION_CODE).get(0).getId();
			ApiFactory.getInstanceService().submitInstance(processId,
					appraisalReport);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void completeTask(AppraisalReport appraisalReport, Long taskId,
			TaskProcessingResult taskTransact) {
		saveAppraisalReport(appraisalReport);
		ApiFactory.getTaskService().completeWorkflowTask(taskId,
				taskTransact);
		//设置处理意见的转换
		appraisalReport.setQualityAuditMan(CommonUtil.replaceTaskResult(appraisalReport.getQualityAuditMan()));
		appraisalReport.setApplyAuditMan(CommonUtil.replaceTaskResult(appraisalReport.getApplyAuditMan()));
		appraisalReport.setApproveMan(CommonUtil.replaceTaskResult(appraisalReport.getApproveMan()));
		appraisalReport.setCustomerOpinion(CommonUtil.replaceTaskResult(appraisalReport.getCustomerOpinion()));
		appraisalReport.setProductManagerOpinion(CommonUtil.replaceTaskResult(appraisalReport.getProductManagerOpinion()));
		appraisalReport.setQualityResponsibleOpinion(CommonUtil.replaceTaskResult(appraisalReport.getQualityResponsibleOpinion()));
		appraisalReport.setQualityManagerOpinion(CommonUtil.replaceTaskResult(appraisalReport.getQualityManagerOpinion()));
		appraisalReport.setResultApproveMan(CommonUtil.replaceTaskResult(appraisalReport.getResultApproveMan()));
		appraisalReport.setMaterialsManagerOpinion(CommonUtil.replaceTaskResult(appraisalReport.getMaterialsManagerOpinion()));
		appraisalReport.setTopManagerOpinion(CommonUtil.replaceTaskResult(appraisalReport.getTopManagerOpinion()));
		appraisalReport.setFinanceOpinion(CommonUtil.replaceTaskResult(appraisalReport.getFinanceOpinion()));
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
			.getWorkflowDefinitionsByCode(SUBLOTS_APPRAISAL_DEFINITION_CODE).get(0)
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

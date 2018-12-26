package com.ambition.supplier.admittance.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.supplier.admittance.service.SampleAppraisalReportManager;
import com.ambition.supplier.admittance.service.SublotsAppraisalReportManager;
import com.ambition.supplier.archives.service.SupplierManager;
import com.ambition.supplier.entity.AppraisalReport;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.mms.base.utils.view.DynamicColumnDefinition;
import com.norteksoft.mms.form.enumeration.DataType;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.opensymphony.xwork2.ActionContext;

/**
 * 供应商小批鉴定
 * @author 赵骏
 *
 */
@Namespace("/supplier/admittance/sublots-appraisal-report")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "supplier/admittance/sublots-appraisal-report", type = "redirectAction"),
	@Result(name = "mobile-task", location = "../../../mobile/supplier-sublots/process-task.jsp")})
public class SublotsAppraisalReportAction extends CrudActionSupport<AppraisalReport> {
	private Logger logger = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;

	// 工作流任务id
	private Long taskId;
	// 点击的按钮
	private TaskProcessingResult taskTransact;
	// 字段权限
	private String fieldPermission = "[]";
	
	private JSONObject params;
	
	private WorkflowTask task;

	private AppraisalReport appraisalReport;
	
	@Autowired
	private SampleAppraisalReportManager sampleAppraisalReportManager;

	@Autowired
	private SublotsAppraisalReportManager sublotsAppraisalReportManager;
	
	@Autowired
	private SupplierManager supplierManager;
	
	@Autowired
	private FormCodeGenerated formCodeGenerated;

	@Autowired
	private LogUtilDao logUtilDao;
	
	private List<DynamicColumnDefinition> dynamicColumnDefinitions = new ArrayList<DynamicColumnDefinition>();
	
	public List<DynamicColumnDefinition> getDynamicColumnDefinitions() {
		return dynamicColumnDefinitions;
	}

	public void setDynamicColumnDefinitions(
			List<DynamicColumnDefinition> dynamicColumnDefinitions) {
		this.dynamicColumnDefinitions = dynamicColumnDefinitions;
	}

	private Page<AppraisalReport> page;

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
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

	public WorkflowTask getTask() {
		return task;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setFieldPermission(String fieldPermission) {
		this.fieldPermission = fieldPermission;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public AppraisalReport getAppraisalReport() {
		return appraisalReport;
	}

	public void setAppraisalReport(AppraisalReport appraisalReport) {
		this.appraisalReport = appraisalReport;
	}

	public Page<AppraisalReport> getPage() {
		return page;
	}

	public void setPage(Page<AppraisalReport> page) {
		this.page = page;
	}

	@Override
	public AppraisalReport getModel() {
		return appraisalReport;
	}

	@Override
	protected void prepareModel() throws Exception {
		//如果是直接启动的鉴定则检查是否有还在小批鉴定的物料
		String lastAppraisalReportId = Struts2Utils.getParameter("appraisalReportId");
		if(StringUtils.isNotEmpty(lastAppraisalReportId)){
			AppraisalReport lastAppraisalReport = sampleAppraisalReportManager.getAppraisalReport(Long.valueOf(lastAppraisalReportId));
			List<AppraisalReport> processAppraisalReports = sublotsAppraisalReportManager.getProcessSublotsReport(lastAppraisalReport.getSupplier(),lastAppraisalReport.getBomCodes());
			if(processAppraisalReports.isEmpty()){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("importance",lastAppraisalReport.getImportance());
				map.put("code",lastAppraisalReport.getBomCodes());
				supplierManager.setSupplyProductState(lastAppraisalReport.getSupplier(),"sublots-appraisal", map);
				String errorMessage = map.get("errorMessage").toString();
				appraisalReport=new AppraisalReport();
				appraisalReport.setCompanyId(ContextUtils.getCompanyId());
				appraisalReport.setCreatedTime(new Date());
				appraisalReport.setCreator(ContextUtils.getUserName());
				appraisalReport.setType(AppraisalReport.TYPE_SUBLOTS);
				appraisalReport.setSupplier(lastAppraisalReport.getSupplier());
				appraisalReport.setHistorySupplierCode(lastAppraisalReport.getHistorySupplierCode());
				appraisalReport.setHistorySupplierName(lastAppraisalReport.getHistorySupplierName());
				appraisalReport.setSupplierProperty(lastAppraisalReport.getSupplierProperty());
				if("是".equals(lastAppraisalReport.getIsNew())){
					appraisalReport.setIsNew("新品");
				}else{
					appraisalReport.setIsNew("量产品");
				}
				appraisalReport.setIsInspectionQualified(lastAppraisalReport.getIsInspectionQualified());
				appraisalReport.setIsSampleQualified(lastAppraisalReport.getIsSampleQualified());
				appraisalReport.setApplyBomCodes(lastAppraisalReport.getApplyBomCodes());
				appraisalReport.setApplyBomNames(lastAppraisalReport.getApplyBomNames());
				appraisalReport.setMaterialType(lastAppraisalReport.getMaterialType());
				if(StringUtils.isEmpty(errorMessage)){
					appraisalReport.setBomCodes(lastAppraisalReport.getBomCodes());
					appraisalReport.setBomNames(lastAppraisalReport.getBomNames());
					appraisalReport.setBomModel(lastAppraisalReport.getBomModel());
					appraisalReport.setTimeOfPhase(sublotsAppraisalReportManager.getTimeOfPhase(lastAppraisalReport.getSupplier().getId(),lastAppraisalReport.getBomCodes()));
				}else{
					addActionMessage("<b>【" + lastAppraisalReport.getBomCodes() + "】"+errorMessage+",不符合小批鉴定的条件!</b>");
				}
			}else{
				appraisalReport = processAppraisalReports.get(0);
				id = appraisalReport.getId();
			}
		}else if(id==null){
			appraisalReport=new AppraisalReport();
			appraisalReport.setCompanyId(ContextUtils.getCompanyId());
			appraisalReport.setCreatedTime(new Date());
			appraisalReport.setCreator(ContextUtils.getUserName());
			appraisalReport.setType(AppraisalReport.TYPE_SUBLOTS);
			appraisalReport.setBusinessUnitName(ContextUtils.getSubCompanyName());
			appraisalReport.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			appraisalReport = sublotsAppraisalReportManager.getAppraisalReport(id);
		}
		fieldPermission = sublotsAppraisalReportManager.getFieldPermission(taskId);
	}

	@Action("input")
	@Override
	public String input() throws Exception {
		if(id == null){
//			appraisalReport.setCode(formCodeGenerated.generateSublotsAppraisalReportCode());
			appraisalReport.setReportMan(ContextUtils.getUserName());
			appraisalReport.setReportDate(new Date());
		}
		prepareFormValues();
		//获取未开始流程时的权限
		Long processId = ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(SublotsAppraisalReportManager.SUBLOTS_APPRAISAL_DEFINITION_CODE).get(0).getId();
		fieldPermission = ApiFactory.getFormService().getFieldPermissionNotStarted(processId);
		return SUCCESS;
	}

//	@Action("save")
//	@Override
//	public String save() throws Exception {
//		try {
//			String supplierId = Struts2Utils.getParameter("supplierId");
//			if(StringUtils.isNotEmpty(supplierId)){
//				appraisalReport.setSupplier(supplierManager.getSupplier(Long.valueOf(supplierId)));
//			}else{
//				appraisalReport.setSupplier(null);
//			}
//			sampleAppraisalReportManager.saveAppraisalReport(appraisalReport);
//			renderText(appraisalReport.getId().toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	@Action("save")
	@Override
	public String save() throws Exception {
		try {
			String supplierId = Struts2Utils.getParameter("supplierId");
			if(StringUtils.isNotEmpty(supplierId)){
				appraisalReport.setSupplier(supplierManager.getSupplier(Long.valueOf(supplierId)));
			}else{
				appraisalReport.setSupplier(null);
			}
			sublotsAppraisalReportManager.saveAppraisalReport(appraisalReport);
			addActionMessage("保存成功!");
			prepareFormValues();
		} catch (Exception e) {
			logger.error("保存小批鉴定失败!",e);
			addActionMessage("保存失败:" + e.getMessage());
			prepareFormValues();
			if(id == null){
				appraisalReport.setId(null);
			}
		}
		//获取未开始流程时的权限
		Long processId = ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(SublotsAppraisalReportManager.SUBLOTS_APPRAISAL_DEFINITION_CODE).get(0).getId();
		fieldPermission = ApiFactory.getFormService().getFieldPermissionNotStarted(processId);
		return "input";
	}
	@Action("view")
	public String view() throws Exception {
		if(id != null){
			appraisalReport = sublotsAppraisalReportManager.getAppraisalReport(id);
			ActionContext.getContext().getValueStack().push(appraisalReport);
		}
		prepareFormValues();
		return SUCCESS;
	}
	public void prepareHistory() throws Exception{
		appraisalReport = sublotsAppraisalReportManager.getAppraisalReport(id);
	}
	
	@Action("history")
	public String history() throws Exception{
		return "history";
	}
	
	public void prepareProcessTask() throws Exception {
		if (id != null) {
			appraisalReport = sublotsAppraisalReportManager.getAppraisalReportById(id);
		} else if (taskId != null) {
			appraisalReport = sublotsAppraisalReportManager.getAppraisalReportByTaskId(taskId);
		}
		ActionContext.getContext().getValueStack().push(appraisalReport);
		// 需我办理的任务
		if (taskId == null) {
			task = sublotsAppraisalReportManager.getMyTask(appraisalReport,
					ContextUtils.getLoginName());
			taskId = task.getId();
		} else {
			task = sublotsAppraisalReportManager.getWorkflowTask(taskId);
		}
		ActionContext.getContext().put("taskCode",task.getCode());
	}

	// 任务办理界面
	@Action("process-task")
	public String processTask() throws Exception {
		if(appraisalReport != null){
			//消息
			List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(appraisalReport);
			Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
		}else if(taskId != null){
			sublotsAppraisalReportManager.deleteReportByTaskId(taskId);
		}
		prepareFormValues();
		fieldPermission = ApiFactory.getFormService().getFieldPermission(taskId);
		if("yes".equals(Struts2Utils.getParameter("is_mobile"))){
			return mobileTask();
		}
		return SUCCESS;
	}
	public String mobileTask() throws Exception{
		return "mobile-task";
	}
	public void prepareCompleteTask() throws Exception {
		prepareProcessTask();
	}

	private void prepareFormValues(){
		//试用物料分类
		List<Option> materialTypes = ApiFactory.getSettingService()
				.getOptionsByGroupCode("supply_materialType");
		ActionContext.getContext().put("materialTypes",materialTypes);
		//试用供应商性质
		List<Option> applyPropertys = ApiFactory.getSettingService()
				.getOptionsByGroupCode("supplier_apply_property");
		ActionContext.getContext().put("applyPropertys",applyPropertys);
		
		//试用人员班组
		List<Option> testManGroups = ApiFactory.getSettingService()
				.getOptionsByGroupCode("mfg_work_group");
		ActionContext.getContext().put("testManGroups",testManGroups);
	}
	// 完成任务
	@Action("complete-task")
	public String completeTask() throws Exception {
		boolean isSuccess = false;
		try {
			if(task != null && task.getActive() != null && task.getActive().equals(0)){
				sublotsAppraisalReportManager.completeTask(appraisalReport, taskId, taskTransact);
			}
			isSuccess = true;
			addActionMessage("提交成功！");
		} catch (Exception e) {
			addActionMessage("保存出错了！" + e.getMessage());
			e.printStackTrace();
		}
		if("yes".equals(Struts2Utils.getParameter("is_mobile"))){
			JSONObject result = new JSONObject();
			if(!isSuccess){
				result.put("error",true);
				result.put("message","保存出错!");
			}
			renderText(result.toString());
			return null;
		}
		prepareFormValues();
		//消息
		List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(appraisalReport);
		Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
		fieldPermission = ApiFactory.getFormService().getFieldPermission(taskId);
		return "process-task";
	}

	public void prepareSubmitProcess() throws Exception {
		prepareModel();
	}

	// 第一次提交
	@Action("submit-process")
	public String submitProcess() throws Exception {
		String supplierId = Struts2Utils.getParameter("supplierId");
		if(StringUtils.isNotEmpty(supplierId)){
			appraisalReport.setSupplier(supplierManager.getSupplier(Long.valueOf(supplierId)));
		}else{
			appraisalReport.setSupplier(null);
		}
		sublotsAppraisalReportManager.submitProcess(appraisalReport);
		// 得到task控制页面上显示取回按钮
		task = sublotsAppraisalReportManager.getWorkflowTask(appraisalReport
				.getWorkflowInfo().getFirstTaskId());
		// 控制页面上的字段都不能编辑
		fieldPermission = sublotsAppraisalReportManager.getFieldPermission(appraisalReport
				.getWorkflowInfo().getFirstTaskId());
		addActionMessage("提交成功！");
		
		prepareFormValues();
		return "input";
	}

	@Action("delete")
	@Override
	public String delete() throws Exception {
		try {
			sublotsAppraisalReportManager.deleteAppraisalReports(deleteIds);
		} catch (Exception e) {
			e.printStackTrace();
			renderText("删除失败:" + e.getMessage());
		}
		return null;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		DynamicColumnDefinition dynamicColumnDefinition = new DynamicColumnDefinition();
		dynamicColumnDefinition.setName("admittance");
		dynamicColumnDefinition.setColName("admittance");
		dynamicColumnDefinition.setVisible(false);
		dynamicColumnDefinition.setType(DataType.TEXT);
		dynamicColumnDefinitions.add(dynamicColumnDefinition);
		return SUCCESS;
	}

	@Action("list-datas")
	public String getListDatas() throws Exception {
		try {
			page = sublotsAppraisalReportManager.search(page);
			for(AppraisalReport appraisalReport : page.getResult()){
				String isAdmittance = sublotsAppraisalReportManager.getAdmittanceState(appraisalReport.getSupplier().getId(), appraisalReport.getBomCodes());
				if("already".equals(isAdmittance)){
					appraisalReport.setAdmittance("already");
				}else{
					if(StringUtils.isNotEmpty(isAdmittance)){
						appraisalReport.setAdmittance(isAdmittance+",不可准入");
					}else{
						appraisalReport.setAdmittance("");
					}
				}
			}
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logUtilDao.debugLog("查询", "供应商质量管理：供应商准入-小批鉴定");
		return null;
	}
	
	@Action("view-other-datas")
	public String getViewOtherDatas() throws Exception {
		try {
			List<AppraisalReport> otherAppraisalReports = sublotsAppraisalReportManager.queryOtherSublotsAppraisalReports(id,Long.valueOf(Struts2Utils.getParameter("supplierId")),Struts2Utils.getParameter("bomCode"));
			page = new Page<AppraisalReport>(Integer.MAX_VALUE);
			page.setPageNo(1);
			page.setTotalCount(otherAppraisalReports.size());
			page.setPageSize(15);
			page.setResult(otherAppraisalReports);
			//查询准入状态
			for(AppraisalReport appraisalReport : page.getResult()){
				String isAdmittance = sublotsAppraisalReportManager.getAdmittanceState(appraisalReport.getSupplier().getId(), appraisalReport.getBomCodes());
				if("already".equals(isAdmittance)){
					appraisalReport.setAdmittance("already");
				}else{
					if(StringUtils.isNotEmpty(isAdmittance)){
						appraisalReport.setAdmittance(isAdmittance+",不可准入");
					}else{
						appraisalReport.setAdmittance("");
					}
				}
			}
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logUtilDao.debugLog("查询", "供应商质量管理：供应商准入-样品鉴定");
		return null;
	}
	
	@Action("get-time-of-phase")
	public String getTimeOfPhase() throws Exception{
		try {
			String appraisalType = Struts2Utils.getParameter("appraisalType");
			if(StringUtils.isEmpty(appraisalType)){
				renderText("{\"timeOfPhase\":\""+sublotsAppraisalReportManager.getTimeOfPhase(Long.valueOf(Struts2Utils.getParameter("supplierId")),Struts2Utils.getParameter("bomCode"))+"\"}");
			}else{
				renderText("{\"timeOfPhase\":\""+sublotsAppraisalReportManager.getTimeOfPhase(Long.valueOf(Struts2Utils.getParameter("supplierId")),Struts2Utils.getParameter("bomCode"),appraisalType)+"\"}");
			}
		} catch (Exception e) {
			renderText("{\"error\":\"true\",\"message\":\""+e.getMessage()+"\"}");
		}
		return null;
	}
	
	/**
	 * 准入
	 * @return
	 * @throws Exception
	 */
	@Action("admittance")
	public String admittance() throws Exception{
		try {
			sublotsAppraisalReportManager.admittance(Long.valueOf(Struts2Utils.getParameter("sublotsAppraisalReportId")));
			renderText("{\"message\":\"准入成功!\"}");
		} catch (Exception e) {
			e.printStackTrace();
			renderText("{\"error\":\"true\",\"message\":\""+e.getMessage()+"\"}");
		}
		return null;
	}
	/**
	 * 取消准入
	 * @return
	 * @throws Exception
	 */
	@Action("cancel-admittance")
	public String cancelAdmittance() throws Exception{
		try {
			sublotsAppraisalReportManager.cancelAdmittance(Long.valueOf(Struts2Utils.getParameter("sublotsAppraisalReportId")));
			renderText("{\"message\":\"操作成功!\"}");
		} catch (Exception e) {
			renderText("{\"error\":\"true\",\"message\":\""+e.getMessage()+"\"}");
		}
		return null;
	}
	@Action("export")
	public String export() throws Exception {
		Page<AppraisalReport> page = new Page<AppraisalReport>(100000);
		page = sublotsAppraisalReportManager.search(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"SUPPLIER_APPRAISAL_REPORT"),"小批鉴定"));
		return null;
	}
}

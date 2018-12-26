package com.ambition.supplier.admittance.web;

import java.util.ArrayList;
import java.util.Date;
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
import com.ambition.supplier.admittance.service.InspectionReportManager;
import com.ambition.supplier.admittance.service.SampleAppraisalReportManager;
import com.ambition.supplier.archives.service.SupplierManager;
import com.ambition.supplier.entity.AppraisalReport;
import com.ambition.supplier.entity.InspectionReport;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.mms.base.DynamicColumnValues;
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
 * 供应商鉴定记录
 * @author 赵骏
 *
 */
@Namespace("/supplier/admittance/sample-appraisal-report")
@ParentPackage("default")
@Results({ 
	@Result(name = CrudActionSupport.RELOAD, location = "supplier/admittance/sample-appraisal-report", type = "redirectAction"),
	@Result(name = "mobile-task", location = "../../../mobile/supplier-sample/process-task.jsp")
})
public class SampleAppraisalReportAction extends CrudActionSupport<AppraisalReport> {
	private Logger logger = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;

	// 工作流任务id
	private Long taskId;
	// 点击的按钮
	private TaskProcessingResult taskTransact;
	// 字段权限
	private String fieldPermission="[]";
	
	private JSONObject params;
	
	private WorkflowTask task;

	private AppraisalReport appraisalReport;
	
	@Autowired
	private SampleAppraisalReportManager sampleAppraisalReportManager;

	@Autowired
	private InspectionReportManager inspectionReportManager;
	
	@Autowired
	private SupplierManager supplierManager;
	
	@Autowired
	private FormCodeGenerated formCodeGenerated;

	@Autowired
	private LogUtilDao logUtilDao;
	
	private Page<AppraisalReport> page;

	private List<DynamicColumnDefinition> dynamicColumnDefinitions = new ArrayList<DynamicColumnDefinition>();
	
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

	public List<DynamicColumnDefinition> getDynamicColumnDefinitions() {
		return dynamicColumnDefinitions;
	}

	public void setDynamicColumnDefinitions(
			List<DynamicColumnDefinition> dynamicColumnDefinitions) {
		this.dynamicColumnDefinitions = dynamicColumnDefinitions;
	}

	@Override
	public AppraisalReport getModel() {
		return appraisalReport;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			appraisalReport=new AppraisalReport();
			appraisalReport.setCompanyId(ContextUtils.getCompanyId());
			appraisalReport.setCreatedTime(new Date());
			appraisalReport.setCreator(ContextUtils.getUserName());
			appraisalReport.setLastModifier(ContextUtils.getUserName());
			appraisalReport.setBusinessUnitName(ContextUtils.getSubCompanyName());
			appraisalReport.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			appraisalReport = sampleAppraisalReportManager.getAppraisalReport(id);
		}
		fieldPermission = sampleAppraisalReportManager.getFieldPermission(taskId);
	}

	@Action("input")
	@Override
	public String input() throws Exception {
		String inspectionReportId = Struts2Utils.getParameter("inspectionReportId");
		if(StringUtils.isNotEmpty(inspectionReportId)){
			InspectionReport inspectionReport = inspectionReportManager.getInspectionReport(Long.valueOf(inspectionReportId));
//			appraisalReport.setCode(formCodeGenerated.generateSampleAppraisalReportCode());
			appraisalReport.setSupplier(inspectionReport.getSupplier());
			appraisalReport.setReportMan(ContextUtils.getUserName());
			appraisalReport.setReportDate(new Date());
		}else if(id == null){
//			appraisalReport.setCode(formCodeGenerated.generateSampleAppraisalReportCode());
			appraisalReport.setReportMan(ContextUtils.getUserName());
			appraisalReport.setReportDate(new Date());
		}
		prepareFormValues();
		//获取未开始流程时的权限
		Long processId = ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(SampleAppraisalReportManager.SAMPLE_APPRAISAL_DEFINITION_CODE).get(0).getId();
		fieldPermission = ApiFactory.getFormService().getFieldPermissionNotStarted(processId);
		return SUCCESS;
	}

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
			sampleAppraisalReportManager.saveAppraisalReport(appraisalReport);
			addActionMessage("保存成功!");
			prepareFormValues();
		} catch (Exception e) {
			logger.error("保存样件鉴定报告失败",e);
			addActionMessage("保存失败:" + e.getMessage());
			prepareFormValues();
			if(id == null){
				appraisalReport.setId(null);
			}
		}
		Long processId = ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(SampleAppraisalReportManager.SAMPLE_APPRAISAL_DEFINITION_CODE).get(0).getId();
		fieldPermission = ApiFactory.getFormService().getFieldPermissionNotStarted(processId);
		return "input";
	}
	@Action("view")
	public String view() throws Exception {
		if(id != null){
			appraisalReport = sampleAppraisalReportManager.getAppraisalReport(id);
			ActionContext.getContext().getValueStack().push(appraisalReport);
		}
		prepareFormValues();
		return SUCCESS;
	}
	public void prepareHistory() throws Exception{
		appraisalReport = sampleAppraisalReportManager.getAppraisalReport(id);
	}
	
	@Action("history")
	public String history() throws Exception{
		return "history";
	}
	
	public void prepareProcessTask() throws Exception {
		if (id != null) {
			appraisalReport = sampleAppraisalReportManager.getAppraisalReport(id);
		} else if (taskId != null) {
			appraisalReport = sampleAppraisalReportManager.getAppraisalReportByTaskId(taskId);
		}
		// 需我办理的任务
		if (taskId == null) {
			task = sampleAppraisalReportManager.getMyTask(appraisalReport,
					ContextUtils.getLoginName());
			taskId = task.getId();
		} else {
			task = sampleAppraisalReportManager.getWorkflowTask(taskId);
		}
		ActionContext.getContext().getValueStack().push(appraisalReport);
		ActionContext.getContext().put("taskCode",task.getCode());
	}

	// 任务办理界面
	@Action("process-task")
	public String processTask() throws Exception {
		//消息
		if(appraisalReport != null){
			List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(appraisalReport);
			Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
		}else if(taskId != null){
			sampleAppraisalReportManager.deleteReportByTaskId(taskId);
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
	}
	// 完成任务
	@Action("complete-task")
	public String completeTask() throws Exception {
//		String supplierId = Struts2Utils.getParameter("supplierId");
//		if(StringUtils.isNotEmpty(supplierId)){
//			appraisalReport.setSupplier(supplierManager.getSupplier(Long.valueOf(supplierId)));
//		}else{
//			appraisalReport.setSupplier(null);
//		}
		boolean isSuccess = false;
		try {
			if(task != null && task.getActive() != null && task.getActive().equals(0)){
				sampleAppraisalReportManager.completeTask(appraisalReport, taskId, taskTransact);
			}
			isSuccess = true;
		} catch (Exception e) {
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
		addActionMessage("提交成功！");
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
		sampleAppraisalReportManager.submitProcess(appraisalReport);
		// 得到task控制页面上显示取回按钮
		task = sampleAppraisalReportManager.getWorkflowTask(appraisalReport
				.getWorkflowInfo().getFirstTaskId());
		// 控制页面上的字段都不能编辑
		fieldPermission = sampleAppraisalReportManager.getFieldPermission(appraisalReport
				.getWorkflowInfo().getFirstTaskId());
		addActionMessage("提交成功！");
		prepareFormValues();
		return "input";
	}

	@Action("delete")
	@Override
	public String delete() throws Exception {
		try {
			sampleAppraisalReportManager.deleteAppraisalReports(deleteIds);
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
		dynamicColumnDefinition.setName("errorMessage");
		dynamicColumnDefinition.setColName("errorMessage");
		dynamicColumnDefinition.setVisible(false);
		dynamicColumnDefinition.setType(DataType.TEXT);
		dynamicColumnDefinitions.add(dynamicColumnDefinition);
		return SUCCESS;
	}

	@Action("list-datas")
	public String getListDatas() throws Exception {
		try {
			page = sampleAppraisalReportManager.search(page);
			renderText(PageUtils.dynamicPageToJson(page,new DynamicColumnValues(){
				public void addValuesTo(List<Map<String, Object>> result) {
					for(Map<String, Object> map:result){
						String importance = map.get("importance")+"";
						String bomCodes = map.get("bomCodes")+"";
						supplierManager.setSupplyProductState(supplierManager.getSupplier(Long.valueOf(map.get("supplier.id")+"")),importance,bomCodes,"sublots-appraisal",map);
					}
				}
			}));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logUtilDao.debugLog("查询", "供应商质量管理：供应商准入-样品鉴定");
		return null;
	}
	
	@Action("export")
	public String export() throws Exception {
		Page<AppraisalReport> page = new Page<AppraisalReport>(100000);
		page = sampleAppraisalReportManager.search(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"SUPPLIER_APPRAISAL_REPORT"),"样件检定"));
		return null;
	}
}

package com.ambition.supplier.admittance.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.supplier.admittance.dao.InspectionGradeTypeDao;
import com.ambition.supplier.admittance.service.InspectionReportManager;
import com.ambition.supplier.archives.service.SupplierManager;
import com.ambition.supplier.entity.CheckGradeType;
import com.ambition.supplier.entity.InspectionGradeType;
import com.ambition.supplier.entity.InspectionReport;
import com.ambition.supplier.supervision.service.CheckGradeTypeManager;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
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
 * 供应商考察记录
 * @author 赵骏
 *
 */
@Namespace("/supplier/admittance/inspection-report")
@ParentPackage("default")
@Results({ 
	@Result(name = CrudActionSupport.RELOAD, location = "supplier/admittance/inspection-report", type = "redirectAction"),
	@Result(name = "mobile-task", location = "../../../mobile/supplier-audit/process-task.jsp")
	})
public class InspectionReportAction extends CrudActionSupport<InspectionReport> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private Long nodeid;
	// 工作流任务id
	private Long taskId;
	// 点击的按钮
	private TaskProcessingResult taskTransact;
	// 字段权限
	private String fieldPermission;
	
	private JSONObject params;
	
	private WorkflowTask task;

	private InspectionReport inspectionReport;
	@Autowired
	private InspectionReportManager inspectionReportManager;

	@Autowired
	private CheckGradeTypeManager checkGradeTypeManager;
	
	@Autowired
	private InspectionGradeTypeDao inspectionGradeTypeDao;
	
	@Autowired
	private SupplierManager supplierManager;
	
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	private Page<InspectionReport> page;

	public Long getNodeid() {
		return nodeid;
	}

	public void setNodeid(Long nodeid) {
		this.nodeid = nodeid;
	}

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

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public InspectionReport getInspectionReport() {
		return inspectionReport;
	}

	public void setInspectionReport(InspectionReport inspectionReport) {
		this.inspectionReport = inspectionReport;
	}

	public Page<InspectionReport> getPage() {
		return page;
	}

	public void setPage(Page<InspectionReport> page) {
		this.page = page;
	}

	@Override
	public InspectionReport getModel() {
		return inspectionReport;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			inspectionReport=new InspectionReport();
			inspectionReport.setCompanyId(ContextUtils.getCompanyId());
			inspectionReport.setCreatedTime(new Date());
			inspectionReport.setCreator(ContextUtils.getUserName());
			inspectionReport.setLastModifier(ContextUtils.getUserName());
			inspectionReport.setBusinessUnitName(ContextUtils.getSubCompanyName());
			inspectionReport.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			inspectionReport = inspectionReportManager.getInspectionReport(id);
			Struts2Utils.getRequest().setAttribute("inspectionSupplyProductIds",inspectionReport.getInspectionSupplyProductIds());
		}
		if(inspectionReport.getReportVersion()==null){
			inspectionReport.setReportVersion(1.0);
		}
		fieldPermission = inspectionReportManager.getFieldPermission(taskId);
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		if(id == null){
//			inspectionReport.setCode(formCodeGenerated.generateInspectionReportCode());
			inspectionReport.setInspectionMan(ContextUtils.getUserName());
			inspectionReport.setInspectionDate(new Date());
			inspectionReport.setInspectionManLoginName(ContextUtils.getLoginName());
		}
		return SUCCESS;
	}

	@Action("inspection-grades")
	public String inspectionGrades() throws Exception{
		if(id == null){
			inspectionReport = new InspectionReport();
		}else{
			inspectionReport = inspectionReportManager.getInspectionReport(id);
		}
		String inspectionGradeTypeId = Struts2Utils.getParameter("inspectionGradeTypeId");
		if(inspectionGradeTypeId != null){
			
		}else{
			Struts2Utils.getRequest().setAttribute("inspectionGradeTypes",inspectionReportManager.getInspectionGradeTypesByUser(inspectionReport,ContextUtils.getLoginName()));
		}
		return SUCCESS;
	}
	
	@Action("view")
	public String view() throws Exception {
		if(id != null){
			inspectionReport = inspectionReportManager.getInspectionReport(id);
		}
		ActionContext.getContext().put("inspectionGradeHtml",inspectionReportManager.getInspectionGradeHtml(inspectionReport));
		ActionContext.getContext().put("otherInspectionReports",inspectionReportManager.queryOtherInspectionReport(inspectionReport));
		return SUCCESS;
	}
	
	@Action("save")
	@Override
	public String save() throws Exception {
		try {
			String supplierId = Struts2Utils.getParameter("supplierId");
			if(StringUtils.isNotEmpty(supplierId)){
				inspectionReport.setSupplier(supplierManager.getSupplier(Long.valueOf(supplierId)));
			}else{
				inspectionReport.setSupplier(null);
			}
			JSONArray jsonArray = null;
			String paramsStr = Struts2Utils.getParameter("paramsStr");
			if(StringUtils.isNotEmpty(paramsStr)){
				jsonArray = JSONArray.fromObject(paramsStr);
			}
			String idTarget = Struts2Utils.getParameter("idTarget");
			inspectionReportManager.saveInspectionReport(inspectionReport,jsonArray,idTarget);
			addActionMessage("保存成功!");
		} catch (Exception e) {
			e.printStackTrace();
			addActionMessage("保存失败:" + e.getMessage());
			if(id == null){
				inspectionReport.setId(null);
			}
		}
		return "input";
	} 

	public void prepareProcessTask() throws Exception {
		if (id != null) {
			inspectionReport = inspectionReportManager.getInspectionReportById(id);
		} else if (taskId != null) {
			inspectionReport = inspectionReportManager.getInpectionReportByTaskId(taskId);
		}
		// 需我办理的任务
		if (taskId == null) {
			task = inspectionReportManager.getMyTask(inspectionReport,
					ContextUtils.getLoginName());
			taskId = task.getId();
		} else {
			task = inspectionReportManager.getWorkflowTask(taskId);
		}
	}

	// 任务办理界面
	@Action("process-task")
	public String processTask() throws Exception {
		if(inspectionReport != null){
			List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(inspectionReport);
			Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
		}else if(taskId != null){
			//实例不存在,删除垃圾数据
			inspectionReportManager.deleteInspectionReortByTaskId(taskId);
		}
//		if("yes".equals(Struts2Utils.getParameter("is_mobile"))){
//			return mobileTask();
//		}
		return SUCCESS;
	}
	public String mobileTask() throws Exception{
		return "mobile-task";
	}
	public void prepareCompleteTask() throws Exception {
		prepareProcessTask();
	}

	// 完成任务
	@Action("complete-task")
	public String completeTask() throws Exception {
		prepareProcessTask();
		inspectionReportManager.completeTask(inspectionReport, taskId, taskTransact);
		ActionContext.getContext().put("message","提交成功！");
		addActionMessage("提交成功！");
		//历史意见
		List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(inspectionReport);
		Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
		return "process-task";
	}
	
	// 完成考察的任务
	@Action("complete-task-grade")
	public String completeTaskGrade() throws Exception {
		prepareProcessTask();
		JSONArray jsonArray = null;
		String paramsStr = Struts2Utils.getParameter("paramsStr");
		if(StringUtils.isNotEmpty(paramsStr)){
			paramsStr = paramsStr.replaceAll("\r\n","");
			jsonArray = JSONArray.fromObject(paramsStr);
		}
		inspectionReportManager.completeTaskGrade(inspectionReport,jsonArray,taskId);
		ActionContext.getContext().put("message","提交成功！");
		addActionMessage("提交成功！");
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
			inspectionReport.setSupplier(supplierManager.getSupplier(Long.valueOf(supplierId)));
		}else{
			inspectionReport.setSupplier(null);
		}

		JSONArray jsonArray = null;
		String paramsStr = Struts2Utils.getParameter("paramsStr");
		if(StringUtils.isNotEmpty(paramsStr)){
			jsonArray = JSONArray.fromObject(paramsStr);
		}
		String idTarget = Struts2Utils.getParameter("idTarget");
		inspectionReportManager.submitProcess(inspectionReport,jsonArray,idTarget);
		//得到task控制页面上显示取回按钮
		task = inspectionReportManager.getWorkflowTask(inspectionReport
				.getWorkflowInfo().getFirstTaskId());
		//控制页面上的字段都不能编辑
		fieldPermission = inspectionReportManager.getFieldPermission(inspectionReport
				.getWorkflowInfo().getFirstTaskId());
		ActionContext.getContext().put("message","提交成功！");
		addActionMessage("提交成功！");
		return "input";
	}

	@Action("delete")
	@Override
	public String delete() throws Exception {
		try {
			inspectionReportManager.deleteInspectionReports(deleteIds);
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
			page = inspectionReportManager.search(page);
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logUtilDao.debugLog("查询", "供应商质量管理：供应商准入-供应商考察");
		return null;
	}
	
	public void prepareHistory() throws Exception{
		inspectionReport=inspectionReportManager.getInspectionReport(id);
	}
	
	@Action("history")
	public String history() throws Exception{
		return "history";
	}
	
	@Action("create-question-report")
	public String createQuestionReport() throws Exception {
		try {
			if(id != null){
				InspectionReport inspectionReport = inspectionReportManager.getInspectionReport(id);
				inspectionReportManager.createQuestionReport(inspectionReport);
			}
		} catch (Exception e) {
			e.printStackTrace();
			renderText("生成失败:" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 编辑评价指标的数据
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Action("query-inspection-grade-types")
	public String queryInspectionGradeTypes() throws Exception {
		Page page = new Page();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(id == null){
			List<CheckGradeType> checkGradeTypes = null;
			Map<Long,Double> weightMap = new HashMap<Long, Double>();
			if(nodeid == null){
				checkGradeTypes = checkGradeTypeManager.getTopCheckGradeTypes(CheckGradeType.TYPE_SURVEY);
				double total = 100.0;
				DecimalFormat df = new DecimalFormat("0.0");
				List<Long> nullList = new ArrayList<Long>();
				for(CheckGradeType checkGradeType : checkGradeTypes){
					if(checkGradeType.getWeight()!=null){
						total-= checkGradeType.getWeight();
						weightMap.put(checkGradeType.getId(),checkGradeType.getWeight());
					}else{
						nullList.add(checkGradeType.getId());
					}
				}
				if(total < 0){//权重总大于100时,按全部为空计算
					nullList.clear();
					total = 100.0;
					for(CheckGradeType checkGradeType : checkGradeTypes){
						nullList.add(checkGradeType.getId());
					}
				}else if(total > 0 && nullList.isEmpty()){//如果总的权重<100
					double rate = 100.0/(100.0-total);
					double totalWeight = 0.0;
					int count=0;
					for(Long id : weightMap.keySet()){
						count++;
						if(count == weightMap.keySet().size()){
							weightMap.put(id,Double.valueOf(df.format(100.0-totalWeight)));
						}else{
							double weight = Double.valueOf(df.format(weightMap.get(id)*rate));
							totalWeight += weight;
							weightMap.put(id,weight);
						}
					}
				}
				
				if(nullList.size()>0){
					for(int i=0;i<nullList.size();i++){
						if(i+1==nullList.size()){
							weightMap.put(nullList.get(i),Double.valueOf(df.format(total)));
						}else{
							double weight = Double.valueOf(df.format(total/(nullList.size()-i)));
							total -= weight;
							weightMap.put(nullList.get(i),weight);
						}
					}
				}
			}else{
				checkGradeTypes = checkGradeTypeManager.getCheckGradeType(nodeid).getChildren();
			}
			for(CheckGradeType checkGradeType : checkGradeTypes){
				convertCheckGradeType(checkGradeType, list,weightMap);
			}
		}else{
			inspectionReport = inspectionReportManager.getInspectionReport(id);
			List<InspectionGradeType> inspectionGradeTypes = null;
			if(nodeid == null){
				inspectionGradeTypes = inspectionReport.getInspectionGradeTypes();
			}else{
				inspectionGradeTypes = inspectionGradeTypeDao.get(nodeid).getChildren();
			}
			for(InspectionGradeType inspectionGradeType : inspectionGradeTypes){
				convertInspectionGradeType(inspectionGradeType, list);
			}
		}
		page.setResult(list);
		renderText("{\"rows\":"+JSONArray.fromObject(list).toString()+"}");
		return null;
	}
	
	@Action("grade-detail")
	public String gradeDetail() throws Exception{
		if(id == null){
			inspectionReport = new InspectionReport();
		}else{
			inspectionReport = inspectionReportManager.getInspectionReport(id);
		}
		String inspectionGradeTypeId = Struts2Utils.getParameter("inspectionGradeTypeId");
		if(inspectionGradeTypeId != null){
			InspectionGradeType inspectionGradeType = inspectionGradeTypeDao.get(Long.valueOf(inspectionGradeTypeId));
			Struts2Utils.getRequest().setAttribute("inspectionGradeType",inspectionGradeType);
		}
//			Struts2Utils.getRequest().setAttribute("inspectionGradeTypes",inspectionReportManager.getInspectionGradeTypesByUser(inspectionReport,ContextUtils.getLoginName()));
//		}
		return SUCCESS;
	}
	
	@Action("view-detail")
	public String viewDetail() throws Exception{
		if(id == null){
			inspectionReport = new InspectionReport();
		}else{
			inspectionReport = inspectionReportManager.getInspectionReport(id);
		}
		String inspectionGradeTypeId = Struts2Utils.getParameter("inspectionGradeTypeId");
		if(inspectionGradeTypeId != null){
			InspectionGradeType inspectionGradeType = inspectionGradeTypeDao.get(Long.valueOf(inspectionGradeTypeId));
			Struts2Utils.getRequest().setAttribute("inspectionGradeType",inspectionGradeType);
		}
		return SUCCESS;
	}
	
	@Action("export")
	public String export() throws Exception {
		Page<InspectionReport> page = new Page<InspectionReport>(100000);
		page = inspectionReportManager.search(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"SUPPLIER_INSPECTION_REPORT"),"潜在供应商考察报告"));
		return null;
	}
	
	@SuppressWarnings("unused")
	private boolean checkHasReviewer(InspectionGradeType inspectionGradeType){
		if(inspectionGradeType.getChildren().isEmpty()){
			if(inspectionGradeType.getReviewerLoginName()==null){
				return false;
			}else{
				return true;
			}
		}else{
			for(InspectionGradeType child : inspectionGradeType.getChildren()){
				boolean  hasSet = checkHasReviewer(child);
				if(hasSet){
					return true;
				}
			}
			return false;
		}
	}
	
	/**
	 * 转换InspectionGradeType至json对象
	 * @param inspectionGradeType
	 * @param list
	 */
	private void convertInspectionGradeType(InspectionGradeType inspectionGradeType,List<Map<String,Object>> list){
		Boolean isLeaf = inspectionGradeType.getChildren().isEmpty();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id",inspectionGradeType.getId());
		map.put("name",inspectionGradeType.getName());
		map.put("level",inspectionGradeType.getLevel()-1);
		map.put("parent",inspectionGradeType.getParent()==null?"":inspectionGradeType.getParent().getId());
		map.put("isLeaf",isLeaf);
		map.put("reviewer",inspectionGradeType.getReviewer());
		map.put("totalFee",inspectionGradeType.getTotalFee());
		map.put("reviewerLoginName",inspectionGradeType.getReviewerLoginName());
		if(inspectionGradeType.getWeight() != null){
			map.put("weight",inspectionGradeType.getWeight());
		}
		list.add(map);
		if(!isLeaf){
			map.put("expanded",false);
			map.put("loaded",false);
		}else{
			map.put("loaded",true);
		}
	}
	
	/**
	 * 转换InspectionGradeType至json对象
	 * @param inspectionGradeType
	 * @param list
	 */
	private void convertCheckGradeType(CheckGradeType checkGradeType,List<Map<String,Object>> list,Map<Long,Double> weightMap){
		Boolean isLeaf = checkGradeType.getChildren().isEmpty();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id",checkGradeType.getId());
		map.put("name",checkGradeType.getName());
		map.put("level",checkGradeType.getLevel()-1);
		map.put("parent",checkGradeType.getParent()==null?"":checkGradeType.getParent().getId());
		map.put("isLeaf",isLeaf);
		if(weightMap.containsKey(checkGradeType.getId())){
			map.put("weight",weightMap.get(checkGradeType.getId()));
		}
		map.put("totalFee",checkGradeType.getTotalFee());
		list.add(map);
		if(!isLeaf){
			map.put("expanded",false);
			map.put("loaded",false);
		}else{
			map.put("loaded",true);
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
}

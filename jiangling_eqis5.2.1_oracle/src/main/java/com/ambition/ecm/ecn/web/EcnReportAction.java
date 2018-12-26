package com.ambition.ecm.ecn.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.ambition.ecm.ecn.service.EcnReportManager;
import com.ambition.ecm.entity.EcnReport;
import com.ambition.ecm.entity.EcnReportDetail;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.acs.web.authorization.JsTreeUtil1;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.product.web.wf.WorkflowActionSupport;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.opensymphony.xwork2.ActionContext;

/**
 * 类名:DcrnReportAction.java
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  wuxuming
 * @version 1.00 2016-11-7 发布
 */
@Namespace("/ecm/ecn")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "ecm/ecn", type = "redirectAction") })
public class EcnReportAction extends WorkflowActionSupport<EcnReport>{

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private String loginDev;//当前用户归属部门
	private String ids;
	private String nowTaskName;
	@Autowired
	private EcnReportManager ecnReportManager;
	@Autowired
	private AcsUtils acsUtils;
	@Autowired
	private LogUtilDao logUtilDao;
	private Long id;
	private String deleteIds;// 删除的ids
    private Long taskId;//工作流任务id
    private TaskProcessingResult taskTransact; //点击的按钮
    private String fieldPermission; // 字段权限
    private WorkflowTask task;
    private String assignee;// 指派人
    private String workCode;
    private EcnReport ecnReport;
    private String addSignPerson;//加签人员
	private String removeSignPerson;//减签人员
	private String copyPerson;//抄送人员
	private List<String[]> handerList = new ArrayList<String[]>();//减签环节办理人list
	protected String submitResult;//任务提交结果
	protected JSONObject params;
	private Page<EcnReport> page;
    protected List<String> returnableTaskNames = new ArrayList<String>();//驳回流转任务
	public void initForm(){
		ActionContext.getContext().put("mfg_result",ApiFactory.getSettingService().getOptionsByGroupCode("ecm-mfg-result"));
		ActionContext.getContext().put("ecm_dev",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_dev"));
		ActionContext.getContext().put("ecm_dcrn_type",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_dcrn_type"));
		ActionContext.getContext().put("ecm_dcrn_peroid",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_dcrn_peroid"));
		ActionContext.getContext().put("ecm_dcrn_cause",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_dcrn_cause"));
		ActionContext.getContext().put("ecm_ecm_context",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_ecm_context"));
		ActionContext.getContext().put("track_results",ApiFactory.getSettingService().getOptionsByGroupCode("track_results"));
		ActionContext.getContext().put("ecm_yes_no",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_yes_or_no"));
	}
	
	/**
	 * 获取权限
	 */
	public void getRight(Long taskId,String defCode) {
		if(taskId==null){
			fieldPermission =ecnReportManager.getFieldPermission(defCode);//禁止或必填字段
		}else{
			fieldPermission = ecnReportManager.getFieldPermissionByTaskId(taskId);//禁止或必填字段
		}
	}
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getNowTaskName() {
		return nowTaskName;
	}
	public void setNowTaskName(String nowTaskName) {
		this.nowTaskName = nowTaskName;
	}

	@Override
	public EcnReport getModel() {
		return ecnReport;
	}

	@Override
	public String abandonReceive() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 加签
	 */
	@Override
	@Action("addsigner")
	@LogInfo(optType="加签",message="加签人员")
	public String addSigner() {
		String[] strs = addSignPerson.split(",");
		List<String> lists = new ArrayList<String>();
		if("all_user".equals(addSignPerson)){
			List<User> users = ApiFactory.getAcsService().getUsersByCompanyCode(ContextUtils.getCompanyCode());
			for(User u:users){
				lists.add(u.getLoginName());
			}
		}else{
			for (String str : strs) {
				lists.add(str);
			}
		}
		ecnReportManager.addSigner(taskId, lists);
		renderText("加签成功！");
		return null;
	}

	@Override
	public String completeInteractiveTask() {
		// TODO Auto-generated method stub
		return null;
	}
    public void prepareCompleteTask() throws Exception {
        prepareModel();
    }
	@Action("complete-task")
	@Override
	public String completeTask() {
		try{
			List<JSONObject> checkItems = getReportArrays();
			ecnReportManager.completeTaskCode(ecnReport,checkItems, taskId, taskTransact);
			task = ecnReportManager.getWorkflowTask(ecnReport.getWorkflowInfo().getFirstTaskId());
			fieldPermission = ecnReportManager.getFieldPermissionByTaskId(ecnReport.getWorkflowInfo().getFirstTaskId());
			ActionContext.getContext().put("fieldPermission", fieldPermission==null?"":fieldPermission);
			//意见处理
			List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(ecnReport);
			if(opinionParameters!=null && !opinionParameters.equals("") && opinionParameters.size()!=0){
				Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
			}else{
				Struts2Utils.getRequest().setAttribute("opinionParameters", new ArrayList<Opinion>());
			}
			addActionMessage("操作成功");
		}catch(Exception e){
			logUtilDao.debugLog("流程提交失败", "流程提交失败"+e.getMessage());
		}
		reInput();
		return "process-task";
	}
	public void prepareProcessTask() throws Exception {
        prepareModel();
    }
	@Action("process-task")
	public String processTask(){
		if(ecnReport != null){
            List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(ecnReport);
            Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
        }else if(taskId != null){
        	ecnReportManager.deleteFormFlowable(taskId);
        }
        workCode=task.getCode();
        reInput();
        return SUCCESS;
	}
	/** 
	  * 方法名:获取请求参数 
	  * @return
	 */
	public static List<JSONObject> getRequestCheckItems(){
		String flagIds = Struts2Utils.getParameter("flagIds");
		if(StringUtils.isEmpty(flagIds)){
			return null;
		}
		String[] flags = flagIds.split(",");
		Map<String,JSONObject> flagMaps = new HashMap<String, JSONObject>();
		for(String flag : flags){
			if(StringUtils.isNotEmpty(flag)){
				JSONObject obj = new JSONObject();
				obj.put("flagIndex",flag.substring(1));
				flagMaps.put(flag,obj);	
			}
		}
		Map<String,String> paramMap = Struts2Utils.getRequest().getParameterMap();
		for(String key : paramMap.keySet()){
			if(key.indexOf("_")>0){
				String flag = key.substring(0,key.indexOf("_"));
				String fieldName = key.substring(key.indexOf("_")+1);
				if(flagMaps.containsKey(flag)){
					flagMaps.get(flag).put(fieldName,Struts2Utils.getParameter(key));
				}
			}
		}
		List<JSONObject> arrays = new ArrayList<JSONObject>();
		for(JSONObject json : flagMaps.values()){
			arrays.add(json);
		}
		Collections.sort(arrays,new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject o1, JSONObject o2) {
				if(o1.getInt("flagIndex")<o2.getInt("flagIndex")){
					return 0;
				}else if(o1.getInt("flagIndex")==o2.getInt("flagIndex")){
					return -1;
				}else{
					return 1;
				}
			}
		});
		return arrays;
	}
	@Override
	public String drawTask() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String fillOpinion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String processEmergency() {
		// TODO Auto-generated method stub
		return null;
	}
	public void prepareSubmitProcess() throws Exception {
        prepareModel();
    }
	/**
	 * 减签
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@Override
	@Action("removesigner")
	@LogInfo(optType="减签",message="减签人员")
	public String removeSigner() {
		String[] strs = removeSignPerson.split(",");
		List<String> lists = new ArrayList<String>();
		for (String str : strs) {
			lists.add(str);
		}
		ecnReportManager.removeSigner(taskId, lists);
		renderText("减签成功！");
		return null;
	}
	
	/**
	 * 选择减签人员
	 * @return
	 * @throws Exception 
	 */
	@Action("cutsigntree")
	public String cutsignTree() throws Exception{
		prepareModel();
		handerList = ecnReportManager.getTaskHander(ecnReport);
		String userLoginName = ContextUtils.getLoginName();
		StringBuilder tree = new StringBuilder();
		tree.append("[");
		for (Object[] arr : handerList) {
			if(userLoginName.equals((String)arr[0])) continue;
			tree.append(JsTreeUtil1.generateJsTreeNodeNew((String)arr[0] , "", (String) arr[1],"folder")).append(",");
		}
		renderText(tree.toString().substring(0, tree.length()-1)+"]");
		return null;
	}
	
	/**
	 * 指派
	 * @return
	 */
	@Action("assign")
	@LogInfo(optType="指派",message="指派任务")
	public String assign(){
		try {
			ecnReportManager.assign(taskId, assignee);
			renderText("指派完成");
		} catch (Exception e) {
			renderText("指派失败!");
			logger.error("指派办理人员失败!",e);
		}
		return null;
	}
	@Override
	@Action("retrievetask")
	@LogInfo(optType="取回任务",message="取回任务")
	public String retrieveTask() {
		try {
			ecnReport = ecnReportManager.getEntityByTaskId(taskId);
			Boolean is = ecnReportManager.retrieveTask(ecnReport);
			if(is){
				String msg=ecnReportManager.retrieve(taskId);
				renderText(msg);
			}else{
				renderText("您没有些操作权限！");
			}
		} catch (Exception e) {
			logger.error("取回任务失败!",e);
			renderText("取回任务失败," + e.getMessage());
		}
		return null;
	}
	@Action("show-history")
	@Override
	public String showHistory() {
		try {
			 ecnReport = ecnReportManager.getEntityByTaskId(taskId);
            task = ecnReportManager.getWorkflowTask(taskId);
            opinions = ApiFactory.getOpinionService().getOpinions(ecnReport);
            ActionContext.getContext().getValueStack().push(ecnReport);
        } catch (Exception e) {
        	logUtilDao.debugLog("查看流转历史出错", e.getMessage());
        }
		return "history";
	}
	@Action("submit-process")
	@Override
	public String submitProcess() {
		List<JSONObject> checkItems = getReportArrays();
		try {
			ecnReportManager.submitProcess(ecnReport,checkItems);
			taskId = ecnReport.getWorkflowInfo().getFirstTaskId();
			task = ApiFactory.getTaskService().getTask(taskId);
			addActionMessage("提交成功!");
	      	id=ecnReport.getId();
		} catch (Exception e) {
            e.printStackTrace();
            addActionMessage("提交失败:" + e.getMessage());
        }
        reInput();
        return "input";
	}
	
	
	private void reInput(){
		getRight(taskId,EcnReportManager.Ecn_CODE);
		if(ecnReport.getEcnReportDetails().isEmpty()){
			EcnReportDetail dd=new EcnReportDetail();
			ecnReport.getEcnReportDetails().add(dd);
		}
		Map<String, String> map=new HashMap<String, String>();
		String pmMan=ecnReport.getPmcLoginMan();
		if(pmMan!=null&&!"".equals(pmMan)){
			map.put(pmMan,"PM");
		}
		String eeMan=ecnReport.getEeLoginMan();
		if(eeMan!=null&&!"".equals(eeMan)){
			map.put(eeMan,"EE");
		}
		String meMan=ecnReport.getMeLoginMan();
		if(meMan!=null&&!"".equals(meMan)){
			map.put(meMan,"ME");
		}
		String materialMan=ecnReport.getMaterialLoginMan();
		if(materialMan!=null&&!"".equals(materialMan)){
			map.put(materialMan,"material");
		}
		String projectMan=ecnReport.getProjectLoginMan();
		if(projectMan!=null&&!"".equals(projectMan)){
			map.put(projectMan,"project");
		}
		String pmcMan=ecnReport.getPmcLoginMan();
		if(pmcMan!=null&&!"".equals(pmcMan)){
			map.put(pmcMan,"PMC");
		}
		String seasoningMan=ecnReport.getSeasoningLoginMan();
		if(seasoningMan!=null&&!"".equals(seasoningMan)){
			map.put(seasoningMan,"seasoning");
		}
		String produceMan=ecnReport.getProduceLoginMan();
		if(produceMan!=null&&!"".equals(produceMan)){
			map.put(produceMan,"produce");
		}
		String purchaseMan=ecnReport.getPurchaseMan();
		if(purchaseMan!=null&&!"".equals(purchaseMan)){
			map.put(purchaseMan,"purchase");
		}
		String jigMan=ecnReport.getJigLoginMan();
		if(jigMan!=null&&!"".equals(jigMan)){
			map.put(jigMan,"jig");
		}
		String equipmentMan=ecnReport.getEquipmentLoginMan();
		if(equipmentMan!=null&&!"".equals(equipmentMan)){
			map.put(equipmentMan,"equipment");
		}
		String ieMan=ecnReport.getIeLoginMan();
		if(ieMan!=null&&!"".equals(ieMan)){
			map.put(ieMan,"IE");
		}
		String marketMan=ecnReport.getMarketLoginMan();
		if(marketMan!=null&&!"".equals(marketMan)){
			map.put(marketMan,"market");
		}
		String qsMan=ecnReport.getQsLoginMan();
		if(qsMan!=null&&!"".equals(qsMan)){
			map.put(qsMan,"QS");
		}
		String warehouseMan=ecnReport.getWarehouseLoginMan();
		if(warehouseMan!=null&&!"".equals(warehouseMan)){
			map.put(warehouseMan,"warehouse");
		}
		String otherMan=ecnReport.getOtherLoginMan();
		if(otherMan!=null&&!"".equals(otherMan)){
			map.put(otherMan,"other");
		}
		String materialsMan=ecnReport.getMaterialsLoginMan();
		if(materialsMan!=null&&!"".equals(materialsMan)){
			map.put( materialsMan,"materials");
		}
		String qualityMan=ecnReport.getQualityLoginMan();
		if(qualityMan!=null&&!"".equals(qualityMan)){
			map.put( qualityMan,"quality");
		}
		String businessUnit=ecnReport.getBusinessUnitLogin();
		if(businessUnit!=null&&!"".equals(businessUnit)){
			map.put( businessUnit,"businessUnit");
		}
		String businessGroup=ecnReport.getBusinessGroupLogin();		
		if(businessGroup!=null&&!"".equals(businessGroup)){
			map.put( businessGroup,"businessGroup");
		}
		String guanwuMan=ecnReport.getGuanwuLoginMan();
		if(guanwuMan!=null&&!"".equals(guanwuMan)){
			map.put(guanwuMan,"guanwu");
		}
		String geshuMan=ecnReport.getGeshuLoginMan();
		if(geshuMan!=null&&!"".equals(geshuMan)){
			map.put(geshuMan,"geshu");
		}
		String docControlMan=ecnReport.getDocControlLoginMan();
		if(docControlMan!=null&&!"".equals(docControlMan)){
			map.put(docControlMan,"docControl");
		}
		ActionContext.getContext().put("signMan",map);
		ActionContext.getContext().put("mfg_result",ApiFactory.getSettingService().getOptionsByGroupCode("ecm-mfg-result"));
		ActionContext.getContext().put("ecm_dev",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_dev"));
		ActionContext.getContext().put("ecm_dcrn_type",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_dcrn_type"));
		ActionContext.getContext().put("ecm_dcrn_peroid",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_dcrn_peroid"));
		ActionContext.getContext().put("ecm_dcrn_cause",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_dcrn_cause"));
		ActionContext.getContext().put("ecm_ecm_context",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_ecm_context"));
		ActionContext.getContext().put("track_results",ApiFactory.getSettingService().getOptionsByGroupCode("track_results"));
		ActionContext.getContext().put("ecm_yes_no",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_yes_or_no"));
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		try {
			ecnReportManager.deleteEntity(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据成功!");
		} catch (Exception e) {
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据失败!");
			renderText("删除失败!"+ e.getMessage());
			logger.error("删除失败!", e);
		}
		return null;
	}
	@Action("input")
	@Override
	public String input() throws Exception {
		reInput();
		if(ecnReport.getWorkflowInfo()==null){
			workCode="stage1";
		}
		return SUCCESS;
	}
	@Action("list")
	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String listDatas(){
		try{
			page = ecnReportManager.searchPage(page);
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			logger.error("查询失败!",e);
		}
		return null;
	}
	

	@Override
	protected void prepareModel() throws Exception {
		loginDev=acsUtils.getDepartmentsByUser(ContextUtils.getCompanyId(), ContextUtils.getUserId()).get(0).getName();
		if(taskId!=null){
			ecnReport = ecnReportManager.getEntityByTaskId(taskId);
	    	id=ecnReport.getId();
	    	task = ecnReportManager.getWorkflowTask(taskId);
	    	ApiFactory.getFormService().fillEntityByTask(ecnReport, taskId);
	    }else if(id!=null){
	    	ecnReport = ecnReportManager.getEntity(id);
	    	task = ecnReportManager.getActiveTaskByTransactorId(ecnReport, ContextUtils.getUserId());
			if(task==null&&ecnReport.getWorkflowInfo()!=null){
				task =ecnReportManager.getWorkflowTask(ecnReport.getWorkflowInfo().getFirstTaskId());
			}
			if(task!=null){
				taskId = task.getId();
			}
			if(taskId != null){
				ApiFactory.getFormService().fillEntityByTask(ecnReport, taskId);
			}
	    }else if(id==null){
	    	ecnReport =new EcnReport();
	    	ecnReport.setCompanyId(ContextUtils.getCompanyId());
	    	ecnReport.setSubCompanyId(ContextUtils.getSubCompanyId());
	    	ecnReport.setDepartmentId(ContextUtils.getDepartmentId());
	    	ecnReport.setCreatedTime(new Date());
	    	ecnReport.setCreator(ContextUtils.getLoginName());
	    	ecnReport.setCreatorName(ContextUtils.getUserName());
	    	ecnReport.setBusinessUnitName(ContextUtils.getSubCompanyName());
	    	ecnReport.setEcnNo(formCodeGenerated.generateEcnReportCode());
	    	ecnReport.setProposeDev(loginDev);
	    	ecnReport.setProposedMan(ContextUtils.getUserName());
	    	ecnReport.setProposeTime(new Date());
	    	ecnReport.setEcnReportDetails(new ArrayList<EcnReportDetail>());
	    }
		//未办理,并且办理人为当前用户才可以驳回
		if(task != null && task.getTransactDate() == null && task.getTransactor().equals(ContextUtils.getLoginName())){
			returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
		}
		
	}
	@Action("save")
	@Override
	public String save() throws Exception {
		try{
			List<JSONObject> reportArrays = getReportArrays();
			ecnReportManager.saveEcnReport(ecnReport, reportArrays);
			addActionMessage("保存成功!");
		}catch(Exception e){
			logger.debug("保存失败："+e.getMessage());
		}
		reInput();
		if(ecnReport.getWorkflowInfo()==null){
			workCode="stage1";
		}else{
			workCode=task.getCode();
		}
		if(StringUtils.isEmpty(Struts2Utils.getParameter("processTaskSave"))){
			return "input";
		}else{
			return "process-task";
		}
		
	}
	/**
	 * 表单详情
	 * @return
	 * @throws Exception
	 */
	@Action("view-info")
    public String viewInfo() throws Exception{
		if(id != null){
			ecnReport = ecnReportManager.getEntity(id);
		}
		task = ecnReportManager.getActiveTaskByTransactorId(ecnReport, ContextUtils.getUserId());
		if(task==null&&ecnReport.getWorkflowInfo()!=null){
			task =ecnReportManager.getWorkflowTask(ecnReport.getWorkflowInfo().getFirstTaskId());
		}
		if(task!=null){
			taskId = task.getId();
		}
		initForm();
        fieldPermission = "[{request:\"false\",readonly:\"true\",controlType:\"allReadolny\"}]";
        ActionContext.getContext().getValueStack().push(ecnReport);
        if(ecnReport != null && ecnReport.getWorkflowInfo() != null){
        	List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(ecnReport);
    		if(!opinionParameters.isEmpty() && opinionParameters.size()!=0){
    			Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
    		}else{
    			Struts2Utils.getRequest().setAttribute("opinionParameters", new ArrayList<Opinion>());
    		}
        }
        reInput();
        return SUCCESS;
    }
	private List<JSONObject> getReportArrays(){
		String flagIds = Struts2Utils.getParameter("flagIds");
		if(StringUtils.isEmpty(flagIds)){
			return null;
		}
		String[] flags = flagIds.split(",");
		Map<String,JSONObject> flagMaps = new HashMap<String, JSONObject>();
		for(String flag : flags){
			if(StringUtils.isNotEmpty(flag)){
				JSONObject obj = new JSONObject();
				obj.put("flagIndex",flag.substring(4));
				flagMaps.put(flag,obj);	
			}
		}
		Map<String,String> paramMap = Struts2Utils.getRequest().getParameterMap();
		for(String key : paramMap.keySet()){
			if(key.indexOf("_")>0){
				String flag = key.substring(0,key.indexOf("_"));
				String fieldName = key.substring(key.indexOf("_")+1);
				if(flagMaps.containsKey(flag)){
					flagMaps.get(flag).put(fieldName,Struts2Utils.getParameter(key));
				}
			}
		}
		List<JSONObject> arrays = new ArrayList<JSONObject>();
		for(JSONObject json : flagMaps.values()){
			arrays.add(json);
		}
		Collections.sort(arrays,new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject o1, JSONObject o2) {
				if(o1.getInt("flagIndex")<o2.getInt("flagIndex")){
					return 0;
				}else if(o1.getInt("flagIndex")==o2.getInt("flagIndex")){
					return -1;
				}else{
					return 1;
				}
			}
		});
		return arrays;
	}
	/**
	 * 导出台账
	 * @return
	 * @throws Exception
	 */
	@Action("export")
	@LogInfo(optType="导出",message="导出数据")
	public String export() throws Exception {
		Page<EcnReport> page = new Page<EcnReport>(65535);
		page = ecnReportManager.searchPage(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"ECM_ECN_REPORT"),"ECN报告"));
		return null;
	}
	/**
     * 方法名:导出Excel文件
     * <p>
     * 功能说明：
     * </p>
     * @throws IOException
     */
    @Action("download-report")
    @LogInfo(optType = "导出", message = "导出变更单")
    public void exportReport() throws Exception {
        try {
            prepareModel();
            ecnReportManager.exportReport(ecnReport);
        } catch (Exception e) {
            e.printStackTrace();
            renderText("导出失败:" + e.getMessage());
        }
    }
    
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

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}


	public String getAddSignPerson() {
		return addSignPerson;
	}

	public void setAddSignPerson(String addSignPerson) {
		this.addSignPerson = addSignPerson;
	}

	public String getRemoveSignPerson() {
		return removeSignPerson;
	}

	public void setRemoveSignPerson(String removeSignPerson) {
		this.removeSignPerson = removeSignPerson;
	}

	public String getCopyPerson() {
		return copyPerson;
	}

	public void setCopyPerson(String copyPerson) {
		this.copyPerson = copyPerson;
	}

	public List<String[]> getHanderList() {
		return handerList;
	}

	public void setHanderList(List<String[]> handerList) {
		this.handerList = handerList;
	}

	public String getSubmitResult() {
		return submitResult;
	}

	public void setSubmitResult(String submitResult) {
		this.submitResult = submitResult;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public List<String> getReturnableTaskNames() {
		return returnableTaskNames;
	}

	public void setReturnableTaskNames(List<String> returnableTaskNames) {
		this.returnableTaskNames = returnableTaskNames;
	}

	public Page<EcnReport> getPage() {
		return page;
	}

	public void setPage(Page<EcnReport> page) {
		this.page = page;
	}

	public String getLoginDev() {
		return loginDev;
	}

	public void setLoginDev(String loginDev) {
		this.loginDev = loginDev;
	}
	
	
}

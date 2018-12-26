package com.ambition.gp.gpmaterial.web;

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
import com.ambition.emailtemplate.service.EmailTemplateManager;
import com.ambition.gp.entity.GpMaterial;
import com.ambition.gp.entity.GpMaterialSub;
import com.ambition.gp.gpmaterial.services.GpMaterialManager;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/gp/gpmaterial")
@ParentPackage("default")
@Results({@Result(name=CrudActionSupport.RELOAD, location = "/gp/gpmaterial", type = "redirectAction")})
public class GpMaterialAction extends AmbWorkflowActionBase<GpMaterial>{

	private static final long serialVersionUID = 1L;
	@Autowired
	private GpMaterialManager gpMaterialManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Autowired
	private AcsUtils acsUtils;
	@Autowired
	private EmailTemplateManager emailTemplateManager;
	private String ids;
	private GpMaterial gpMaterial;
	private String EmailTemplate;
	private String str;
	private String currentActivityName;//当前流程环节名称
	Logger log = Logger.getLogger(this.getClass());
	
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public GpMaterial getGpMaterial() {
		return gpMaterial;
	}

	public void setGpMaterial(GpMaterial gpMaterial) {
		this.gpMaterial = gpMaterial;
	}

	public String getCurrentActivityName() {
		return currentActivityName;
	}

	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}
	
	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	@Override
	protected AmbWorkflowManagerBase<GpMaterial> getAmbWorkflowBaseManager() {
		// TODO Auto-generated method stub
		return gpMaterialManager;
	}
	public void initForm(){
//		emailTemplateManager.getEmailTemplate(taskId);
		if(getId() == null&&getReport().getId()==null){
			getReport().setFormNo(formCodeGenerated.generateGpMaterialNo());
			getReport().setTaskCreatedTime(new Date());
			getReport().setTransactorName(ContextUtils.getUserName());
			getReport().setTransactor(ContextUtils.getLoginName());
			getReport().setInitiator(ContextUtils.getUserName());
			getReport().setSupplierDate(new Date());
		}else{
			if(getReport().getWorkflowInfo()!=null){
				currentActivityName=getReport().getWorkflowInfo().getCurrentActivityName();
			}
		}
		List<GpMaterialSub> gpMaterialSubs= getReport().getGpMaterialSubs();
		
		if(gpMaterialSubs == null||gpMaterialSubs.size()==0){
			gpMaterialSubs = new ArrayList<GpMaterialSub>();
			GpMaterialSub item = new GpMaterialSub();
			gpMaterialSubs.add(item);
		}
		ActionContext.getContext().put("_gpMaterialSubs", gpMaterialSubs);
		ActionContext.getContext().put("benchmarks",ApiFactory.getSettingService().getOptionsByGroupCode("benchmarks"));
		ActionContext.getContext().put("attributes",ApiFactory.getSettingService().getOptionsByGroupCode("attributes"));
		ActionContext.getContext().put("meanTypes",ApiFactory.getSettingService().getOptionsByGroupCode("gp_mean_type"));
	}
	
	/**
	 * 列表页面
	 */
	@Override
	@Action("list")
	public String list() throws Exception {
		return SUCCESS;
	}
	@Action("list-pending")
	public String listPending() throws Exception {
		return SUCCESS;
	}
	/**
	 * 列表数据
	 */
	@Action("list-datas")
	@LogInfo(optType="查询",message="查询数据")
	public String getListDatas() throws Exception {
		try{
			String dept =acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName();
			String type = Struts2Utils.getParameter("type");
			String code = ContextUtils.getLoginName();
			if("供应商".equals(dept)){
				page = gpMaterialManager.listState(page,code,type);
				renderText(PageUtils.pageToJson(page));
			}else{
				page = gpMaterialManager.listState(page,null,type);
				renderText(PageUtils.pageToJson(page));
			}
//			logUtilDao.debugLog("查询","查询数据",5240l);
		}catch(Exception e){
			getLogger().error("查询失败!",e);
		}
		return null;
	}
	/**
	 * 创建返回消息
	 * 
	 * @param error
	 * @param message
	 * @return
	 */
	public void createErrorMessage(String message) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("error", true);
		map.put("message", message);
		renderText(JSONObject.fromObject(map).toString());
	}
	private void createSupplierUser(GpMaterial report) {
		   //查出供应商部门
		   Department dept = gpMaterialManager.searchSupplierDept();
		   //检查是否存在供应商，不存在就插入
		   gpMaterialManager.saveUser(report,dept);
			
	  }
	/**
	 * 启动并提交流程
	 */
	@Override
	@Action("submit-process")
	@LogInfo(optType="启动并提交",message="启动并提交流程")
	public String submitProcess() {
		boolean hasError = false;
		try{
				//report = getAmbWorkflowBaseManager().getEntity(id);
				beforeSubmitCallback();
				//子表信息
				Map<String, List<JSONObject>> childMaps = getChildrenInfos();
				CompleteTaskTipType completeTaskTipType =  getAmbWorkflowBaseManager().submitProcess(report,childMaps);
				if(report.getSupplierLoginName()!=null){
					createSupplierUser(report);
				}
				submitResult = getAmbWorkflowBaseManager().getCompleteTaskTipType(completeTaskTipType,report);
				Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "启动并提交流程成功!");
				addActionMessage("提交成功!");
				
				emailTemplateManager.triggerTaskEmail(report,gpMaterialManager.getEntityInstanceClass(),null);
				afterSubmitCallback();
				//成功标志
				ActionContext.getContext().put("_successFlag",true);
		}catch(AmbFrameException e){
			hasError = true;
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "启动并提交流程失败!");
			addActionMessage("提交失败!" + e.getMessage());
			getLogger().error("启动并提交流程失败!", e);
			if(id != null){
				report = getAmbWorkflowBaseManager().getEntity(id);
			}else if(taskId != null){
				report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
				returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
			}
		} catch (Exception e) {
			hasError = true;
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "启动并提交流程失败!设置特殊字段值出错");
			addActionMessage("提交失败!设置特殊字段值出错");
			getLogger().error("启动并提交流程失败!设置特殊字段值出错", e);
			if(id != null){
				report = getAmbWorkflowBaseManager().getEntity(id);
			}else if(taskId != null){
				report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
			}
		}
		if(!hasError){
			task = getAmbWorkflowBaseManager().getActiveTaskByTransactorId(report,ContextUtils.getUserId());
			if(task!=null)taskId = task.getId();
			if(task==null&&report.getWorkflowInfo()!=null){
				taskId = report.getWorkflowInfo().getFirstTaskId();
				task = getAmbWorkflowBaseManager().getWorkflowTask(taskId);
		    	//ApiFactory.getFormService().fillEntityByTask(qrqcReport, taskId);
			}
			try {
				getAmbWorkflowBaseManager().updateDueDate(report);
			} catch (Exception e) {
				getLogger().error("更新催办期限失败!",e);
			}
		}
		// 控制页面上的字段都不能编辑
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		initForm();
		//自定义返回页面时返自定义的地址
		String customInputTypeFormUrl = Struts2Utils.getParameter("customInputTypeFormUrl");
		if(StringUtils.isNotEmpty(customInputTypeFormUrl)){
			return customInputTypeFormUrl;
		}
		return "input";
	}
	@Action("release")
	public String release() throws Exception {
		String params = Struts2Utils.getParameter("par");
		String formNo = Struts2Utils.getParameter("formNo");
		String url =Struts2Utils.getParameter("url");
		String id =Struts2Utils.getParameter("id");
		try {
			if(params!=null&&!"".equals(params)){
				gpMaterialManager.releaseEmail1(params,url,formNo,id);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 导出台账
	 * @return
	 * @throws Exception
	 */
	@Action("export")
	@LogInfo(optType="导出",message="导出产品成份宣告表数据")
	public String export() throws Exception {
		Page<GpMaterial> page = new Page<GpMaterial>(65535);
		String dept =acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName();
		String type = Struts2Utils.getParameter("type");
		String code = ContextUtils.getLoginName();
		if("供应商".equals(dept)){
			page = gpMaterialManager.listState(page,code,type);
		}else{
			page = gpMaterialManager.listState(page,null,type);
		}
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,getAmbWorkflowBaseManager().getEntityListCode()),getAmbWorkflowBaseManager().getWorkflowDefinitionName()));
		return null;
	}
	public void afterCompleteCallback() throws Exception{
		String id =Struts2Utils.getParameter("id");
		String formNo = Struts2Utils.getParameter("formNo");
		String supplierEmail =Struts2Utils.getParameter("supplierEmail");
		String supplierId =Struts2Utils.getParameter("approvalId");
		String name=ApiFactory.getTaskService().getTask(taskId).getNextTasks();
		if("供应商填写资料".equals(name)){
			gpMaterialManager.releaseEmail1(supplierEmail,null,formNo,id);
		}
		if("process_is_end".equals(name)){
			if(supplierId!=null){
				gpMaterialManager.setGpMaterialState(id);
			}else{
				gpMaterialManager.findSupplierMaterial(Long.valueOf(id));
			}
		}
	}
	/**
	 * 完成任务
	 */
	@Override
	@Action("complete-task")
	@LogInfo(optType="同意或者提交",message="完成任务")
	public String completeTask() {
		@SuppressWarnings("unused")
		CompleteTaskTipType completeTaskTipType=null;
		String errorMessage = null;
		try{
			beforeCompleteCallback();
			//子表信息
			Map<String, List<JSONObject>> childMaps = getChildrenInfos();
			completeTaskTipType =  getAmbWorkflowBaseManager().completeTask(report,childMaps,taskId, taskTransact);
			addActionMessage("流程处理成功!");
			emailTemplateManager.triggerTaskEmail(report,gpMaterialManager.getEntityInstanceClass(),null);
			afterCompleteCallback();
			//成功标志
			ActionContext.getContext().put("_successFlag",true);
		}catch(RuntimeException e){
			getLogger().error("流程处理失败！", e);
			addActionMessage("流程处理失败!");
			errorMessage = "处理失败," + e.getMessage();
			if(id != null){
				report = getAmbWorkflowBaseManager().getEntity(id);
			}else if(taskId != null){
				report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
				returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
			}
		} catch (Exception e) {
			getLogger().error("流程处理失败！设置特殊字段值出错", e);
			addActionMessage("流程处理失败!设置特殊字段值出错");
			errorMessage = "处理失败,设置特殊字段值出错," + e.getMessage();
			if(id != null){
				report = getAmbWorkflowBaseManager().getEntity(id);
			}else if(taskId != null){
				report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
				returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
			}
		}
		try {
			if(StringUtils.isEmpty(errorMessage)){
				getAmbWorkflowBaseManager().updateDueDate(report);
			}
		} catch (Exception e) {
			getLogger().error("更新催办期限失败!",e);
		}
		// 控制页面上的字段都不能编辑
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		initForm();
		
		List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(report);
		if(opinionParameters!=null && !opinionParameters.equals("") && opinionParameters.size()!=0){
			Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
		}else{
			Struts2Utils.getRequest().setAttribute("opinionParameters", new ArrayList<Opinion>());
		}
		//自定义返回页面时返自定义的地址
		String customInputTypeFormUrl = Struts2Utils.getParameter("customInputTypeFormUrl");
		if(StringUtils.isNotEmpty(customInputTypeFormUrl)){
			return customInputTypeFormUrl;
		}
		String returnurl = "inputform".equals(Struts2Utils.getParameter("inputformortaskform"))?"input":"process-task";
		return returnurl;
	}
	@Override
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		try {
			String str=gpMaterialManager.deleteEntity(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据:单号:"+str);
		} catch (Exception e) {
			// TODO: handle exception
			renderText("删除失败:" + e.getMessage());
			log.error("删除数据信息失败",e);
		}
		return null;
	}
	@Action("find-supplier-material")
	@LogInfo(optType="关联材料状态")
	public String findSupplierMaterial(){
		JSONObject result = new JSONObject();
		String ids =Struts2Utils.getParameter("ids");
		try {
			String[] Ids = ids.split(",");
			for(String id : Ids){
				gpMaterialManager.findSupplierMaterial(Long.valueOf(id));
			}
			result.put("message", "操作成功!");
			result.put("error", false);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.put("message", "操作失败!");
			result.put("error", false);
		}
		renderText(result.toString());
		return null;
	}
}

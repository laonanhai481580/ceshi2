package com.ambition.epm.exception.web;

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
import com.ambition.epm.entity.EntrustHsfSublist;
import com.ambition.epm.entity.EntrustOrtSublist;
import com.ambition.epm.entity.ExceptionSingle;
import com.ambition.epm.entrustHsf.services.EntrustHsfSublistManager;
import com.ambition.epm.entrustOrt.services.EntrustOrtSublistManager;
import com.ambition.epm.exception.service.ExceptionManager;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.opensymphony.xwork2.ActionContext;
@Namespace("/epm/exception-single")
@ParentPackage("default")
@Results({@Result(name=CrudActionSupport.RELOAD, location = "/epm/exception-single", type = "redirectAction")})
public class ExceptionAction extends AmbWorkflowActionBase<ExceptionSingle>{
	private static final long serialVersionUID = 1L;
	@Autowired
	private ExceptionManager exceptionManager;
	@Autowired
	private EntrustHsfSublistManager entrustHsfSublistManager;
	@Autowired
	private EntrustOrtSublistManager entrustOrtSublistManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	Logger log = Logger.getLogger(this.getClass());
	private String ids;
	private ExceptionSingle exceptionSingle;
	
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public ExceptionSingle getExceptionSingle() {
		return exceptionSingle;
	}

	public void setExceptionSingle(ExceptionSingle exceptionSingle) {
		this.exceptionSingle = exceptionSingle;
	}
	
	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	@Override
	protected AmbWorkflowManagerBase<ExceptionSingle> getAmbWorkflowBaseManager() {
		return exceptionManager;
	}
	public void initForm(){
		if(getId() == null){
			if(getReport().getFormNo()==null){
				getReport().setFormNo(formCodeGenerated.generateExceptionSingleNo());
			}
			getReport().setTaskCreatedTime(new Date());
			getReport().setTransactorName(ContextUtils.getUserName());
			getReport().setTransactor(ContextUtils.getLoginName());
			getReport().setTestEngineer(ContextUtils.getUserName());
			getReport().setSubmitDate(new Date());
//			getReport().setResultsPeople(getReport().getTemporary());
//			getReport().setResultsPeopleLogin(getReport().getTemporaryLogin());
			getReport().setExceptionState("N");
			addExceptionSingle();
		}
		ActionContext.getContext().put("severitys",ApiFactory.getSettingService().getOptionsByGroupCode("epm_severity"));
	}
	public void addExceptionSingle(){
		String formNo=Struts2Utils.getParameter("formId");
		String bd= Struts2Utils.getParameter("type");
		if(formNo!=null&&!"".equals(formNo)&&bd.equals("hsf")){
			List<EntrustHsfSublist> list  = entrustHsfSublistManager.getByHsfId(Long.valueOf(formNo));
			if(list.size()>0){
				EntrustHsfSublist entrustHsfSublist=list.get(0);
				getReport().setCompanyId(ContextUtils.getCompanyId());
				getReport().setDepartmentId(ContextUtils.getDepartmentId());
				getReport().setCreatedTime(new Date());
				getReport().setCreator(ContextUtils.getLoginName());
				getReport().setCreatorName(ContextUtils.getUserName());
				
				getReport().setCustomerNo(entrustHsfSublist.getClient());//客户
				getReport().setTestSend(entrustHsfSublist.getEntrustHsf().getConsignor());//送测人
				getReport().setTestSendLogin(entrustHsfSublist.getEntrustHsf().getConsignorLogin());//送测人
				getReport().setLabPeople(ContextUtils.getUserName());//结案人
				getReport().setLabPeopleLogin(ContextUtils.getLoginName());//结案人登录名
				getReport().setTestDepartment(entrustHsfSublist.getEntrustHsf().getConsignorDept());//送测部门
				getReport().setItemNo(entrustHsfSublist.getEntrustHsf().getFormNo());//表单编号
				getReport().setLotNo(entrustHsfSublist.getLotNo());//集中
				getReport().setModel(entrustHsfSublist.getModel());//规格型号
				getReport().setQuantity(entrustHsfSublist.getAmount());//样品数量
				getReport().setSupplier(entrustHsfSublist.getSupplier());//供应商
//				getReport().setTemporary(entrustHsfSublist.getEntrustHsf().getConfirmDept());//临时办理人
//				getReport().setTemporaryLogin(entrustHsfSublist.getEntrustHsf().getConfirmDeptLoing());//临时办理人的登录名
				getReport().setHsfNo(formNo);
				getReport().setExceptionState("N");
				getReport().setHiddenState(entrustHsfSublist.getEntrustHsf().getHiddenState());
				User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
				String subName=user.getSubCompanyName();
				getReport().setFactoryClassify(subName);
				getReport().setEntrustId(entrustHsfSublist.getId());
				getReport().setDefectNumber(entrustHsfSublist.getDefectNumber());//不良数
				if(entrustHsfSublist.getDefectRate()!=null){
					getReport().setDefection(entrustHsfSublist.getDefectRate().toString());//不良率
				}
				getReport().setInvalidNumber(entrustHsfSublist.getInvalidNumber());//无效数
				if(!"欧菲科技-CCM".equals(ContextUtils.getCompanyName())){
					getReport().setManagerAssets(entrustHsfSublist.getEntrustHsf().getManagerAssets());//管理编号
					getReport().setTestEngineer(entrustHsfSublist.getEntrustHsf().getTester());
				}
			}
		}
		if(formNo!=null&&!"".equals(formNo)&&bd.equals("ort")){
			List<EntrustOrtSublist> list = entrustOrtSublistManager.getByOrtId(Long.valueOf(formNo));
			if(list.size()>0){
				EntrustOrtSublist entrustOrtSublist=list.get(0);
				getReport().setCompanyId(ContextUtils.getCompanyId());
				getReport().setDepartmentId(ContextUtils.getDepartmentId());
				getReport().setCreatedTime(new Date());
				getReport().setCreator(ContextUtils.getLoginName());
				getReport().setCreatorName(ContextUtils.getUserName());
				
				getReport().setCustomerNo(entrustOrtSublist.getEntrustOrt().getCustomerNo());//客户
				getReport().setTestSend(entrustOrtSublist.getEntrustOrt().getConsignor());//送测人
				getReport().setTestSendLogin(entrustOrtSublist.getEntrustOrt().getConsignorLogin());//送测人登录名
				getReport().setTestDepartment(entrustOrtSublist.getEntrustOrt().getConsignorDept());//送测部门
				getReport().setItemNo(entrustOrtSublist.getEntrustOrt().getFormNo());//表单编号
				getReport().setQuantity(entrustOrtSublist.getTestNumber());//样品数量
				getReport().setLotNo(entrustOrtSublist.getEntrustOrt().getLotNo());//批号
				getReport().setSampleType(entrustOrtSublist.getEntrustOrt().getSampleType());//样品类别
				getReport().setProductNo(entrustOrtSublist.getEntrustOrt().getProductNo());//机种
//				getReport().setTemporary(entrustOrtSublist.getEntrustOrt().getConfirmDept());//临时办理人
//				getReport().setTemporaryLogin(entrustOrtSublist.getEntrustOrt().getConfirmDeptLoing());//临时办理人的登录名
				getReport().setOrtNo(formNo);
				getReport().setExceptionState("N");
				getReport().setProductStage(entrustOrtSublist.getEntrustOrt().getCategory());
				getReport().setLabPeople(ContextUtils.getUserName());//结案人
				getReport().setLabPeopleLogin(ContextUtils.getLoginName());//结案人登录名
				getReport().setEntrustId(entrustOrtSublist.getId());//17439187
				getReport().setDefectNumber(entrustOrtSublist.getDefectNumber());//不良数
				getReport().setDefection(entrustOrtSublist.getDefectRate());//不良率
				getReport().setInvalidNumber(entrustOrtSublist.getInvalidNumber());//无效数
				getReport().setHiddenState(entrustOrtSublist.getEntrustOrt().getHiddenState());
				User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
				String subName=user.getSubCompanyName();
				getReport().setFactoryClassify(subName);
				if(!"欧菲科技-CCM".equals(ContextUtils.getCompanyName())){
					getReport().setTestEngineer(entrustOrtSublist.getEntrustOrt().getTester());
					getReport().setManagerAssets(entrustOrtSublist.getEntrustOrt().getManagerAssets());//管理编号
				}
			}
		}
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		ActionContext.getContext().getValueStack().push(getReport());
	}
	@Action("alter-exception")
	public void alterException() {
		String entrustId=Struts2Utils.getParameter("entrustId");
		String bd= Struts2Utils.getParameter("type");
		if(entrustId!=null&&!"".equals(entrustId)&&bd.equals("hsf")){
			entrustHsfSublistManager.alterEntrustHsfSublist(Long.valueOf(entrustId));
			exceptionManager.deleteEntrust(Long.valueOf(entrustId));
//			exceptionManager.deleteEntity(entrustId);
		}
		if(entrustId!=null&&!"".equals(entrustId)&&bd.equals("ort")){
			entrustOrtSublistManager.alterEntrustOrtSublist(Long.valueOf(entrustId));
			exceptionManager.deleteEntrust(Long.valueOf(entrustId));
//			exceptionManager.deleteEntity(entrustId);
		}
	} 
	/**
	 * 启动并提交流程
	 */
	@Override
	@Action("submit-process")
	@LogInfo(optType="启动并提交",message="启动并提交异常流程")
	public String submitProcess() {
		boolean hasError = false;
		try{
			beforeSubmitCallback();
			//子表信息
			Map<String, List<JSONObject>> childMaps = getChildrenInfos();
			CompleteTaskTipType completeTaskTipType =  getAmbWorkflowBaseManager().submitProcess(report,childMaps);
			submitResult = getAmbWorkflowBaseManager().getCompleteTaskTipType(completeTaskTipType,report);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "启动并提交流程成功!");
			addActionMessage("提交成功!");
//			emailTemplateManager.triggerTaskEmail(report,exceptionManager.getEntityInstanceClass(),null);
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
		if(!hasError){
			try {
				exceptionManager.writeNo(report);
			} catch (Exception e) {
				getLogger().error("回写编号出错!",e);
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
	/**
	 * 导出表单
	 * */
	@Action("export-form")
	public String exportReportForm(){
		try{
			exceptionManager.exportReport(id);
		}catch(Exception e){
			createErrorMessage("导出表单失败:" + e.getMessage());
			log.error("导出失败："+e.getMessage());
		}
		return null;
	}
	/**
	 * 导出台账
	 * @return
	 * @throws Exception
	 */
	@Action("export")
	@LogInfo(optType="导出",message="导出数据")
	public String export() throws Exception {
		String type = Struts2Utils.getParameter("type");
		String state = Struts2Utils.getParameter("state");
		User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
		String subName=user.getSubCompanyName();
		String companyName =PropUtils.getProp("companyName");
		Integer weight =user.getWeight();
		Page<ExceptionSingle> page = new Page<ExceptionSingle>(65535);
		if(state==null){
			state="N";
		}
		if(!"TP".equals(companyName)){
			weight=3;
		}
		if(weight==3){
			page = exceptionManager.listState(page, state,null,type);
		}else{
			page = exceptionManager.listState(page, state,subName,type);
		}
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,getAmbWorkflowBaseManager().getEntityListCode()),getAmbWorkflowBaseManager().getWorkflowDefinitionName()));
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
	/**
	 * 删除
	 */
	@Override
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		getLogger().info("删除记录");
		try {
			exceptionManager.deleteFormNo(deleteIds);
			String str=exceptionManager.deleteEntity(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据成功! 单号:"+str);
		} catch (Exception e) {
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据失败!");
			renderText("删除失败!"+ e.getMessage());
			getLogger().error("删除失败!", e);
		}
		return null;
	}
	/**
	 * 列表数据
	 */
	@Action("list-type")
	@LogInfo(optType="查询",message="查询数据")
	public String getListStates() throws Exception {
		String type = Struts2Utils.getParameter("type");
		String state = Struts2Utils.getParameter("state");
//		String processSection = Struts2Utils.getParameter("processSection");
		User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
		String subName=user.getSubCompanyName();
		String companyName =PropUtils.getProp("companyName");
		Integer weight =user.getWeight();
		try{
			if(state==null){
				state="N";
			}
			if(!"TP".equals(companyName)){
				weight=3;
			}
			if(weight==3){
				page = exceptionManager.listState(page, state,null,type);
			}else{
				page = exceptionManager.listState(page, state,subName,type);
			}
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			log.error("查询失败!",e);
		}
		return null;
	}
	@Action("hide")
	@LogInfo(optType="修改")
	public String hide(){
		String eid = Struts2Utils.getParameter("id");
		String state = Struts2Utils.getParameter("state");
		try {
			exceptionManager.hide(eid,state);
			addActionMessage("操作成功!");
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "隐藏数据成功!"+ExceptionSingle.ENTITY_LIST_NAME);
		} catch (Exception e) {
			renderText("修改失败!"+ e.getMessage());
		}
		return null;
	}
}
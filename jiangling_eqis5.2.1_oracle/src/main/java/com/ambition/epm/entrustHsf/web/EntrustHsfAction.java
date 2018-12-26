package com.ambition.epm.entrustHsf.web;

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
import com.ambition.epm.entity.EntrustHsf;
import com.ambition.epm.entity.EntrustHsfSublist;
import com.ambition.epm.entrustHsf.services.EntrustHsfManager;
import com.ambition.epm.entrustHsf.services.EntrustHsfSublistManager;
import com.ambition.epm.exception.service.ExceptionManager;
import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.iqc.entity.TestFrequency;
import com.ambition.iqc.inspectionbase.service.TestFrequencyManager;
import com.ambition.iqc.inspectionreport.service.IncomingInspectionActionsReportManager;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/epm/entrust-hsf")
@ParentPackage("default")
@Results({@Result(name=CrudActionSupport.RELOAD, location = "/epm/entrust-hsf", type = "redirectAction")})
public class EntrustHsfAction extends AmbWorkflowActionBase<EntrustHsf>{

	private static final long serialVersionUID = 1L;
	@Autowired
	private EntrustHsfManager entrustHsfManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Autowired
	private EntrustHsfSublistManager entrustHsfSublistManager;
	@Autowired
	private TestFrequencyManager testFrequencyManager;
	@Autowired
	private ExceptionManager exceptionManager;
	@Autowired
	private IncomingInspectionActionsReportManager iiarManager;
	@Autowired
	private AcsUtils acsUtils;
	private Boolean workFlowCanSubmit=true;
	private String ids;
	private EntrustHsf entrustHsf;
	private String currentActivityName;
	private String sunId;
	private String str;
	Logger log = Logger.getLogger(this.getClass());
	
	public Boolean getWorkFlowCanSubmit() {
		return workFlowCanSubmit;
	}
	public void setWorkFlowCanSubmit(Boolean workFlowCanSubmit) {
		this.workFlowCanSubmit = workFlowCanSubmit;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public EntrustHsf getEntrustHsf() {
		return entrustHsf;
	}
	public void setEntrustHsf(EntrustHsf entrustHsf) {
		this.entrustHsf = entrustHsf;
	}
	public Logger getLog() {
		return log;
	}
	public void setLog(Logger log) {
		this.log = log;
	}
	public String getSunId() {
		return sunId;
	}
	public void setSunId(String sunId) {
		this.sunId = sunId;
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
	protected AmbWorkflowManagerBase<EntrustHsf> getAmbWorkflowBaseManager() {
		// TODO Auto-generated method stub
		return entrustHsfManager;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(sunId!=null){
			EntrustHsfSublist entrustHsfSublist = entrustHsfSublistManager.getEntrustHsfSublist(Long.valueOf(sunId));
		    if(entrustHsfSublist!=null){
		    	id = entrustHsfSublist.getEntrustHsf().getId();
		    }
		}
		if(taskId!=null){
	    	report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
	    	id=report.getId();
	    	task = getAmbWorkflowBaseManager().getWorkflowTask(taskId);
	    	ApiFactory.getFormService().fillEntityByTask(report, taskId);
	    }else if(id!=null){
	    	report = getAmbWorkflowBaseManager().getEntity(id);
	    	task = getAmbWorkflowBaseManager().getActiveTaskByTransactorId(report, ContextUtils.getUserId());
			if(task==null&&report.getWorkflowInfo()!=null){
				task = getAmbWorkflowBaseManager().getWorkflowTask(report.getWorkflowInfo().getFirstTaskId());
			}
			if(task!=null){
				taskId = task.getId();
			}
			if("生成样品管理".equals(task.getName())){
				ApiFactory.getTaskService().completeWorkflowTask(taskId,TaskProcessingResult.SUBMIT);
			}
			if(taskId != null){
				ApiFactory.getFormService().fillEntityByTask(report, taskId);
			}
	    }else if(id==null&&sunId==null){
	    	report = getAmbWorkflowBaseManager().getEntityInstanceClass().newInstance();
	    	report.setCompanyId(ContextUtils.getCompanyId());
	    	report.setSubCompanyId(ContextUtils.getSubCompanyId());
	    	report.setDepartmentId(ContextUtils.getDepartmentId());
	    	report.setCreatedTime(new Date());
	    	report.setCreator(ContextUtils.getLoginName());
	    	report.setCreatorName(ContextUtils.getUserName());
	    	report.setBusinessUnitName(ContextUtils.getSubCompanyName());
	    	report.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
			User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
			String subName=user.getSubCompanyName();
			report.setFactoryClassify(subName);
	    }
		//未办理,并且办理人为当前用户才可以驳回
		if(task != null && task.getTransactDate() == null && task.getTransactor().equals(ContextUtils.getLoginName())){
			returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
		}
	}
	public void initForm(){
		sunId = Struts2Utils.getParameter("sunId");
		if(sunId!=null){
			try {
				prepareModel();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(getId() == null&&sunId==null){
			getReport().setFormNo(formCodeGenerated.generateEntrustHsfNo());
			getReport().setTaskCreatedTime(new Date());
			getReport().setTransactorName(ContextUtils.getUserName());
			getReport().setTransactor(ContextUtils.getLoginName());
			getReport().setConsignor(ContextUtils.getUserName());
			getReport().setConsignorLogin(ContextUtils.getLoginName());
			getReport().setConsignorDept(acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName());
			getReport().setConsignableDate(new Date());
//			getReport().setEpmState("N");
		}else{
			if(getReport().getWorkflowInfo()!=null){
				currentActivityName=getReport().getWorkflowInfo().getCurrentActivityName();
			}
		}
		List<EntrustHsfSublist> entrustHsfSublists= getReport().getEntrustHsfSublists();
		User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
		String subName=user.getSubCompanyName();
		if(entrustHsfSublists == null){
			entrustHsfSublists = new ArrayList<EntrustHsfSublist>();
			EntrustHsfSublist item = new EntrustHsfSublist();
			item.setFactoryClassify(subName);
			entrustHsfSublists.add(item);
		}
		ActionContext.getContext().put("_entrustHsfSublists", entrustHsfSublists);
		ActionContext.getContext().put("categorys",ApiFactory.getSettingService().getOptionsByGroupCode("epm_categorysH"));
		ActionContext.getContext().put("categoryTexts",ApiFactory.getSettingService().getOptionsByGroupCode("epm_categoryTests"));
		ActionContext.getContext().put("testItems",ApiFactory.getSettingService().getOptionsByGroupCode("epm_testItems"));
		ActionContext.getContext().put("testResults",ApiFactory.getSettingService().getOptionsByGroupCode("epm_testResults"));
		ActionContext.getContext().put("severitys",ApiFactory.getSettingService().getOptionsByGroupCode("epm-urgency"));
		ActionContext.getContext().put("riskGrades",ApiFactory.getSettingService().getOptionsByGroupCode("epm_riskGrades"));
		ActionContext.getContext().put("sampleSorts",ApiFactory.getSettingService().getOptionsByGroupCode("epm_sampleSorts"));
		ActionContext.getContext().put("customerNeeds",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_yes_or_no"));
		workFlowCanSubmit = exceptionManager.findUnCompleted("HSF");
	}
	/**
	 * 子表详情
	 * @return
	 */
	@Action("child-table-input")
	public String childTableInput(){
		try {
			if(id != null){
				EntrustHsf entrustHsf = entrustHsfManager.getEntity(id);
				if(entrustHsf != null){
					ActionContext.getContext().getValueStack().push(entrustHsf);
					//初始化子表
					List<EntrustHsfSublist> entrustHsfSublists = entrustHsf.getEntrustHsfSublists();
					if(entrustHsfSublists==null || entrustHsfSublists.size() == 0){
						entrustHsfSublists = new ArrayList<EntrustHsfSublist>();
						entrustHsfSublists.add(new EntrustHsfSublist());						
					}
					ActionContext.getContext().put("_entrustHsfSublists",entrustHsfSublists);
				}else{
					throw new AmbFrameException("数据出错！");
				}
			}else{
				throw new AmbFrameException("参数缺失！");
			}
		} catch (Exception e) {
			ActionContext.getContext().put("errorMessage", "参数缺失！");
			logger.error("加载外校委托-台账子表详情出错："+ e.getMessage(), e);
		}
		return SUCCESS;
	}
	/**
	 * 新建页面
	 */
	@Override
	@Action("input")
	public String input() throws Exception {
		str = Struts2Utils.getParameter("str");
		if(str==null){
			getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		}
		initForm();
		if(!"流程结束".equals(str)){
			addEntrustHsf();
		}
		if (report != null && report.getWorkflowInfo() != null) {
			List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(report);
			if(!opinionParameters.isEmpty() && opinionParameters.size() != 0) {
				Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
			} else {
				Struts2Utils.getRequest().setAttribute("opinionParameters",	new ArrayList<Opinion>());
			}
		}
		return SUCCESS;
	}
	/**
	 * 导出表单
	 * */
	@Action("export-form")
	public String exportReportForm() {
		try{
			entrustHsfManager.exportReport(id);
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
		String companyName =PropUtils.getProp("companyName");
		User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
		String subName=user.getSubCompanyName();
		Integer weight =user.getWeight();
		Page<EntrustHsf> page = new Page<EntrustHsf>(65535);
		if(weight==3){
			page = entrustHsfManager.listState(page, "N",null);
		}else{
			page = entrustHsfManager.listState(page, "N",subName);
		}
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,getAmbWorkflowBaseManager().getEntityListCode()),getAmbWorkflowBaseManager().getWorkflowDefinitionName()));
		return null;
	}
	public void addEntrustHsf(){
		String formNo=Struts2Utils.getParameter("formId");
		if(formNo!=null&&!"".equals(formNo)){
			IncomingInspectionActionsReport iiar=iiarManager.getIncomingInspectionActionsReport(Long.valueOf(formNo));
			if(iiar!=null){
				getReport().setInspectionId(iiar.getId());
				getReport().setInspectionNo(iiar.getInspectionNo());
			}			
		}
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		ActionContext.getContext().getValueStack().push(getReport());
	}
	/**
	 * 列表数据
	 */
	@Action("list-state")
	@LogInfo(optType="查询",message="查询数据")
	public String getListStates() throws Exception {
		String type = Struts2Utils.getParameter("type");
		User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
		String subName=user.getSubCompanyName();
		Integer weight =user.getWeight();
		try{
			if(type==null){
				type="N";
			}
//			if(!"欧菲-TP".equals(ContextUtils.getCompanyName())){
//				weight=3;
//			}
			if(weight==3){
				page = entrustHsfManager.listState(page, type,null);
			}else{
				page = entrustHsfManager.listState(page, type,subName);
			}
			renderText(PageUtils.pageToJson(page));
//			logUtilDao.debugLog("查询","查询数据",5240l);
		}catch(Exception e){
			getLogger().error("查询失败!",e);
		}
		return null;
	}
	@Action("epmHide")
	@LogInfo(optType="修改",message="隐藏HSF委托数据")
	public String epmHide(){
		String eid = Struts2Utils.getParameter("id");
		String type = Struts2Utils.getParameter("type");
		try {
			entrustHsfManager.epmHide(eid,type);
			addActionMessage("操作成功!");
		} catch (Exception e) {
			// TODO: handle exception
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
	/**
	  * 方法名: 打印页面
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("print-page")
	@LogInfo(optType="显示",message="打印报告页面")
	public String printPage() throws Exception {
		try {
			String formNo = Struts2Utils.getParameter("formNo");
//			Object result =  entrustOrtManager.findReportByFormNo(formNo);
			EntrustHsf entrustHsf = entrustHsfManager.findReportByFormNo(formNo);
			ActionContext.getContext().getValueStack().push(entrustHsf);
			List<EntrustHsfSublist> entrustHsfSublists= entrustHsf.getEntrustHsfSublists();
			if(entrustHsfSublists == null){
				entrustHsfSublists = new ArrayList<EntrustHsfSublist>();
				EntrustHsfSublist item = new EntrustHsfSublist();
				entrustHsfSublists.add(item);
			}
			ActionContext.getContext().put("_entrustHsfSublists", entrustHsfSublists);
		} catch (Exception e) {
			log.error("打印报告初始化失败!",e);
		}
		return SUCCESS;
	}
	
	/**
	 * 完成任务
	 */
	@Override
	@Action("complete-task")
	@LogInfo(optType="同意或者提交",message="完成HSF任务")
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
			if(report.getTextResult()!=null&&!"".equals(report.getTextResult())&&report.getInspectionId()!=null){
				IncomingInspectionActionsReport iiar=iiarManager.getIncomingInspectionActionsReport(report.getInspectionId());
				if(iiar!=null){
					if(iiar.getHisReportNo()==null||"".equals(iiar.getHisReportNo())||!iiar.getHisReportNo().equals(report.getFormNo())){
						iiar.setHisPackingResult(report.getTextResult());
						iiar.setHisReportNo(report.getFormNo());
						iiar.setHisReportId(report.getId().toString());
						//查询测试频率并更新
						TestFrequency testFrequency=null;
						if("欧菲-TP".equals(ContextUtils.getCompanyName())){
							String businessUnitName = CommonUtil.getDataByLogin();
							//testFrequency=testFrequencyManager.searchFrequencyTP(iiar.getSupplierCode(), iiar.getCheckBomCode(),businessUnitName);
							if(testFrequency!=null){
								String materialType=testFrequency.getMaterialType();
								if(materialType!=null&&!"".equals(materialType)){
									//testFrequency=testFrequencyManager.searchFrequencyMaterial(iiar.getSupplierCode(),materialType ,businessUnitName);
								}
							}
						}else{
							testFrequency=testFrequencyManager.searchFrequency(iiar.getSupplierCode(), iiar.getCheckBomCode());
						}						
						if(testFrequency!=null&&report.getCategory()!=null&&!report.getCategory().equals("样品")){
							if(testFrequency.getLastHsfReportId()!=null&&!"".equals(testFrequency.getLastHsfReportId())){
								Long preId=Long.valueOf(testFrequency.getLastHsfReportId());
								if(preId<report.getId()){
									testFrequency.setLastHsfTestDate(report.getConsignableDate());
									testFrequency.setLastHsfReportId(report.getId().toString());
									testFrequency.setLastHsfReportNo(report.getFormNo());
									testFrequencyManager.saveTestFrequency(testFrequency);
								}
							}else{
								testFrequency.setLastHsfTestDate(report.getConsignableDate());
								testFrequency.setLastHsfReportId(report.getId().toString());
								testFrequency.setLastHsfReportNo(report.getFormNo());
								testFrequencyManager.saveTestFrequency(testFrequency);
							}							
						}
					}								
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
	 * 启动并提交流程
	 */
	@Override
	@Action("submit-process")
	@LogInfo(optType="启动并提交",message="启动HSF并提交流程")
	public String submitProcess() {
		boolean hasError = false;
		try{
			beforeSubmitCallback();
			//子表信息
			Map<String, List<JSONObject>> childMaps = getChildrenInfos();
			CompleteTaskTipType completeTaskTipType =  getAmbWorkflowBaseManager().submitProcess(report,childMaps);
			submitResult = getAmbWorkflowBaseManager().getCompleteTaskTipType(completeTaskTipType,report);
			entrustHsfManager.isfromNo(report);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "启动并提交流程成功!");
			addActionMessage("提交成功!");
			
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
	@Override
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		try {
			String str=entrustHsfManager.deleteEntity(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据:单号:"+str);
		} catch (Exception e) {
			// TODO: handle exception
			renderText("删除失败:" + e.getMessage());
			log.error("删除数据信息失败",e);
		}
		return null;
	}
}

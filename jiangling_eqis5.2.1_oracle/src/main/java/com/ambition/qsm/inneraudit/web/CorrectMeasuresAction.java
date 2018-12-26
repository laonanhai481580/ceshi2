package com.ambition.qsm.inneraudit.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.qsm.baseinfo.service.DefectionClauseManager;
import com.ambition.qsm.entity.CorrectMeasures;
import com.ambition.qsm.entity.CorrectMeasuresItem;
import com.ambition.qsm.entity.SignCompleteItem;
import com.ambition.qsm.entity.SignMeasureItem;
import com.ambition.qsm.entity.SignReasonItem;
import com.ambition.qsm.inneraudit.service.CorrectMeasuresManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CheckMobileUtil;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.opensymphony.xwork2.ActionContext;
/**
 * 类名:不符合与纠正措施报告Action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年10月25日 发布
 */
@Namespace("/qsm/inner-audit/correct-measures")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/qsm/inner-audit/correct-measures", type = "redirectAction"), 
			@Result(name = "mobile-input", location = "mobile/measures-input.jsp",type="dispatcher")})
public class CorrectMeasuresAction extends AmbWorkflowActionBase<CorrectMeasures>{

	private static final long serialVersionUID = 1L;
	public static final String MOBILEINPUT = "mobile-input";
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Autowired
	private DefectionClauseManager defectionClauseManager;
	private String ids;
	private String nowTaskName;
	private String userName;
	private String loginName;
	private CorrectMeasures correctMeasures;
	private String currentActivityName;
	@Autowired
	private CorrectMeasuresManager correctMeasuresManager;
	
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


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getLoginName() {
		return loginName;
	}


	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}


	public CorrectMeasures getCorrectMeasures() {
		return correctMeasures;
	}


	public void setCorrectMeasures(CorrectMeasures correctMeasures) {
		this.correctMeasures = correctMeasures;
	}


	public String getCurrentActivityName() {
		return currentActivityName;
	}


	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}

	@Override
	protected AmbWorkflowManagerBase<CorrectMeasures> getAmbWorkflowBaseManager() {
		return correctMeasuresManager;
	}
	/**
	  * 方法名: 初始化的参数在这里写
	  * <p>功能说明：</p>
	 */
	public void initForm(){
		if(getId()==null&&getReport().getFormNo()==null){
			getReport().setFormNo(formCodeGenerated.generateCorrectMeasuresNo());
			getReport().setAuditDate(new Date());
			getReport().setSetMan(ContextUtils.getUserName());
		}			
		List<CorrectMeasuresItem> correctMeasuresItems = new ArrayList<CorrectMeasuresItem>();
		if(report.getCorrectMeasuresItems()!=null){//加载子表
			correctMeasuresItems.addAll(report.getCorrectMeasuresItems());
		}
		if(report.getCorrectMeasuresItems()==null){//加载子表
			CorrectMeasuresItem correctMeasuresItem=new CorrectMeasuresItem();
			correctMeasuresItems.add(correctMeasuresItem);
		}
		List<SignReasonItem> signReasonItems=report.getSignReasonItems();
		if(signReasonItems!=null){
			String auditMan2=getReport().getAuditMan2();
			String auditMan2Login=getReport().getAuditMan2Login();
			if(auditMan2!=null&&auditMan2Login!=null&&!"".equals(auditMan2)&&!"".equals(auditMan2Login)){
				String a[]=auditMan2.split(",");
				String b[]=auditMan2Login.split(",");
				boolean isEmpty=false;
				for (SignReasonItem signItem : signReasonItems) {
					 if(signItem.getSignMan()==null||"".equals(signItem.getSignMan())){
						 isEmpty=true; 
					 }
				}
				if(isEmpty||signReasonItems.size()!=a.length){
					signReasonItems.clear();
					for (int i = 0; i < a.length; i++) { 
						SignReasonItem item=new SignReasonItem();
						item.setSignMan(a[i]);
						item.setSignManLogin(b[i]);	
						signReasonItems.add(item);
						}
					}
			}else{
				signReasonItems.clear();
			}			
		}	
		
		List<SignMeasureItem> signMeasureItems=report.getSignMeasureItems();
		if(signMeasureItems!=null){
			String auditMan3=getReport().getAuditMan3();
			String auditMan3Login=getReport().getAuditMan3Login();
			if(auditMan3!=null&&auditMan3Login!=null&&!"".equals(auditMan3)&&!"".equals(auditMan3Login)){
				String a[]=auditMan3.split(",");
				String b[]=auditMan3Login.split(",");
				boolean isEmpty=false;
				for (SignMeasureItem signMeasureItem : signMeasureItems) {
					 if(signMeasureItem.getSignMan()==null||"".equals(signMeasureItem.getSignMan())){
						 isEmpty=true; 
					 }
				}
				if(isEmpty||signMeasureItems.size()!=a.length){
					signMeasureItems.clear();
					for (int i = 0; i < a.length; i++) { 
						SignMeasureItem item=new SignMeasureItem();
						item.setSignMan(a[i]);
						item.setSignManLogin(b[i]);	
						signMeasureItems.add(item);
						}
					}
			}else{
				signMeasureItems.clear();
			}			
		}
		
		List<SignCompleteItem> signCompleteItems=report.getSignCompleteItems();
		if(signCompleteItems!=null){
			String auditMan4=getReport().getAuditMan4();
			String auditMan4Login=getReport().getAuditMan4Login();
			if(auditMan4!=null&&auditMan4Login!=null&&!"".equals(auditMan4)&&!"".equals(auditMan4Login)){
				String a[]=auditMan4.split(",");
				String b[]=auditMan4Login.split(",");
				boolean isEmpty=false;
				for (SignCompleteItem signCompleteItem : signCompleteItems) {
					 if(signCompleteItem.getSignMan()==null||"".equals(signCompleteItem.getSignMan())){
						 isEmpty=true; 
					 }
				}
				if(isEmpty||signCompleteItems.size()!=a.length){
					signCompleteItems.clear();
					for (int i = 0; i < a.length; i++) { 
						SignCompleteItem item=new SignCompleteItem();
						item.setSignMan(a[i]);
						item.setSignManLogin(b[i]);	
						signCompleteItems.add(item);
						}
					}
			}else{
				signCompleteItems.clear();
			}			
		}			
		userName=ContextUtils.getUserName();
		loginName=ContextUtils.getLoginName();
		ActionContext.getContext().put("_correctMeasuresItems",correctMeasuresItems);
		ActionContext.getContext().put("_signReasonItems",signReasonItems);
		ActionContext.getContext().put("_signMeasureItems",signMeasureItems);
		ActionContext.getContext().put("_signCompleteItems",signCompleteItems);
		ActionContext.getContext().put("businessUnits", ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits"));
		ActionContext.getContext().put("inconformityTypes", ApiFactory.getSettingService().getOptionsByGroupCode("qsm_inconformity_type"));
		ActionContext.getContext().put("enterpriseGroups", ApiFactory.getSettingService().getOptionsByGroupCode("qsm_enterprise_group"));	
		ActionContext.getContext().put("systemNames", ApiFactory.getSettingService().getOptionsByGroupCode("qsm_system_name"));	
		ActionContext.getContext().put("auditTypes", ApiFactory.getSettingService().getOptionsByGroupCode("qsm_audit_type"));	
	}
	
	/**
	 * 模糊查询不符合条款
	 */
	@Action("search-clause-name")
	public String seachMainProductBoms() throws Exception {
		String searchParams = Struts2Utils.getParameter("searchParams");
		JSONObject params = null;
		if(StringUtils.isNotEmpty(searchParams)){
			params = JSONObject.fromObject(searchParams);
		}
		String label = Struts2Utils.getParameter("label");
		List<?> searchList = defectionClauseManager.searchClauseNnames(params);
		StringBuffer sb = new StringBuffer("");
		for(Object obj : searchList){
			Object[] objs = (Object[])obj;
			if(sb.length()>0){
				sb.append(",");
			}
			String value1 = objs[0]==null?"":objs[0].toString(),value2 = objs[1]==null?"":objs[1].toString();
			value1 = value1.replace("\n","");//体系名称
			value2 = value2.replace("\n","");//条款名称
			value2 = value2.replace("\"","");
			if("clauseName".equals(label)){
				sb.append("{\"value\":\""+value2+"\",\"label\":\""+value1+"\"}");
			}else{
				sb.append("{\"value\":\""+value1+"\",\"label\":\""+value2+"\"}");
			}
		}
		sb.insert(0,"[").append("]");
		renderText(sb.toString());
		return null;
	}
	/**
	 * 导出表单
	 * */
	@Action("export-report")
	@LogInfo(optType="导出表单",message="不符合与纠正措施报告")
	public String exportReport() {
		try{
			correctMeasuresManager.exportReport(id);
		}catch(Exception e){
			createMessage("导出表单失败:" + e.getMessage());
		}
		return null;
	}
    /**
     * 创建返回消息
     * @param error
     * @param message
     * @return
     */
    public void createMessage(String message){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("error",false);
        map.put("message",message);
        renderText(JSONObject.fromObject(map).toString());
    }
    /**
	 * 新建页面
	 */
	@Override
	@Action("input")
	public String input() throws Exception {
		super.input();
		//判断是否手机端发起的请求
		if(CheckMobileUtil.isMobile(Struts2Utils.getRequest())){
			ActionContext.getContext().put("userTreeHtml",generateDepartmentUserTree());
			return MOBILEINPUT;
		}else{
			return SUCCESS;
		}
	}
	/**
	 * 办理任务页面
	 * @return
	 */
	@Action("process-task")
	@LogInfo(optType="办理",message="办理任务")
	public String task() throws Exception {
		Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "办理任务");
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		initForm();
		//办理前自动填写域设值
		if(taskId!=null){
			ApiFactory.getFormService().fillEntityByTask(report, taskId);
			returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
		}
		List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(report);
		if(!opinionParameters.isEmpty() && opinionParameters.size()!=0){
			Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
		}else{
			Struts2Utils.getRequest().setAttribute("opinionParameters", new ArrayList<Opinion>());
		}
		//判断是否手机端发起的请求
		if(CheckMobileUtil.isMobile(Struts2Utils.getRequest())){
			ActionContext.getContext().put("userTreeHtml",generateDepartmentUserTree());
			return MOBILEINPUT;
		}else{
			return SUCCESS;
		}
	}
	/**
	 * 保存
	 */
	@Override
	@Action("save")
	public String save() throws Exception {
		//设置
		try {
			beforeSaveCallback();
			//子表信息
			Map<String, List<JSONObject>> childMaps = getChildrenInfos();
			getAmbWorkflowBaseManager().saveEntity(report,childMaps);
			if(taskId == null && report.getWorkflowInfo() != null){
				taskId = report.getWorkflowInfo().getFirstTaskId();
				task = getAmbWorkflowBaseManager().getWorkflowTask(taskId);
			}
			addActionMessage("保存成功!");
			//修改数据来源的数据状态 2016-08-29
		} catch (Exception e) {
			getLogger().error("保存"+getAmbWorkflowBaseManager().getWorkflowDefinitionName()+"失败!",e);
			addActionMessage("保存"+getAmbWorkflowBaseManager().getWorkflowDefinitionName()+"失败," + e.getMessage());
			if(id != null){
				report = getAmbWorkflowBaseManager().getEntity(id);
			}else if(taskId != null){
				report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
			}
		}
		initForm();
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		String returnurl = Struts2Utils.getParameter("inputformortaskform").equals("inputform")?"input":"process-task";
		if("process-task".equals(returnurl)){
			List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(report);
			if(opinionParameters!=null && !opinionParameters.equals("") && opinionParameters.size()!=0){
				Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
			}else{
				Struts2Utils.getRequest().setAttribute("opinionParameters", new ArrayList<Opinion>());
			}
		}
		//判断是否手机端发起的请求
		if(CheckMobileUtil.isMobile(Struts2Utils.getRequest())){
			ActionContext.getContext().put("userTreeHtml",generateDepartmentUserTree());
			return MOBILEINPUT;
		}else{
			return returnurl;
		}
	}
	/**
	 * 生成部门用户
	 * @return
	 */
	public StringBuffer generateDepartmentUserTree(){
		List<com.norteksoft.acs.entity.organization.Department> allDepartments = correctMeasuresManager.queryAllDepartments();
		List<User> allUsers = correctMeasuresManager.queryAllUsers();
		StringBuffer userHtml = new StringBuffer();
		List<com.norteksoft.acs.entity.organization.Department> parentDepts = queryChildrens(allDepartments,null);
		for(com.norteksoft.acs.entity.organization.Department dept : parentDepts){
			generateHtml(userHtml, dept, allDepartments, allUsers);
		}
		//无部门用户
//		generateHtml(userHtml,null, allDepartments, allUsers);
		return userHtml;
	}
	private void generateHtml(StringBuffer html,com.norteksoft.acs.entity.organization.Department dept,
			List<com.norteksoft.acs.entity.organization.Department> allDepartments,
			List<User> allUsers){
		List<User> users = queryUsersByDeptId(allUsers,dept==null?null:dept.getId());
		List<com.norteksoft.acs.entity.organization.Department> children = queryChildrens(allDepartments,dept==null?null:dept.getId());
		if(users.isEmpty()&&children.isEmpty()){
			//html.append("<p>"+dept.getName()+"</p>");
		}else{
			html.append("<li style=\"margin-left:20px;\">");
			html.append("<label><a href=\"javascript:;\" onclick=\"showdiv('"+(dept==null?"noId":dept.getName())+"')\" >"+(dept==null?"无部门用户":dept.getName())+"</a></label>");
			html.append("<div style=\"display:none;\" id="+(dept==null?"noId":dept.getName())+"><ul class=\"two\" style=\"margin-left:30px;\">");
			for(User user : users){
				html.append("<li><label><input  type=\"checkbox\" name='"+user.getName()+"' deptName="+(dept==null?"noId":dept.getName())+"  value='"+user.getLoginName()+"'><a  href=\"javascript:;\" >"+user.getName()+"</a></label></li>");
				
			}
			for(com.norteksoft.acs.entity.organization.Department child : children){
				generateHtml(html,child,allDepartments,allUsers);
			}
			html.append("</ul></div>");
			html.append("</li>");
		}
	}
	private List<com.norteksoft.acs.entity.organization.Department> queryChildrens(List<com.norteksoft.acs.entity.organization.Department> allDepartments,Long parentId){
		List<com.norteksoft.acs.entity.organization.Department> children = new ArrayList<com.norteksoft.acs.entity.organization.Department>();
		for(com.norteksoft.acs.entity.organization.Department d : allDepartments){
			if(parentId==null){
				if(d.getParent()==null){
					children.add(d);
				}
			}else{
				if(d.getParent()!=null&&d.getParent().getId().equals(parentId)){
					children.add(d);
				}
			}
		}
		return children;
	}
	private List<User> queryUsersByDeptId(List<User> allUsers,Long deptId){
		List<User> users = new ArrayList<User>();
		for(User u : allUsers){
			if(deptId==null){
				if(u.getMainDepartmentId()==null){
					users.add(u);
				}
			}else{
				if(u.getMainDepartmentId()!=null&&u.getMainDepartmentId().equals(deptId)){
					users.add(u);
				}
			}
		}
		return users;
	}
	 /**
	    * 启动并提交流程
	    */
	   @Override
	   @Action("submit-process")
	   @LogInfo(optType="启动并提交流程")
	   public String submitProcess() {
	       boolean hasError = false;
	       try{
	           beforeSubmitCallback();
	           if(report.getFormNo()==null){
	           	  report.setFormNo(formCodeGenerated.generateCorrectMeasuresNo());
				}
	           //子表信息
	           Map<String, List<JSONObject>> childMaps = getChildrenInfos();
	           CompleteTaskTipType completeTaskTipType =  getAmbWorkflowBaseManager().submitProcess(report,childMaps);
	           submitResult = getAmbWorkflowBaseManager().getCompleteTaskTipType(completeTaskTipType,report);
	           Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "启动并提交流程成功!");
	           addActionMessage("提交成功!");
	           id = report.getId();
	       }catch(RuntimeException e){
	           hasError = true;
	           Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "启动并提交流程失败!");
	           addActionMessage("提交失败!");
	           getLogger().error("启动并提交流程失败!", e);
	           if(id != null){
	               report = getAmbWorkflowBaseManager().getEntity(id);
	           }else if(taskId != null){
	               report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
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
	       //判断是否手机端发起的请求
			if (CheckMobileUtil.isMobile(Struts2Utils.getRequest())) {
				ActionContext.getContext().put("userTreeHtml",
						generateDepartmentUserTree());
				return MOBILEINPUT;
			} else {
				return "input";
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
			//判断是否手机端发起的请求
			if(CheckMobileUtil.isMobile(Struts2Utils.getRequest())){
				ActionContext.getContext().put("userTreeHtml",generateDepartmentUserTree());
				isComplete = true;
				return MOBILEINPUT;
			}else{
				return returnurl;
			}
		}
		
	 @Action("return-to-task")
	   public String returnToTask() throws Exception {
	       JSONObject result = new JSONObject();
	       try {
	           String returnTaskName = Struts2Utils.getParameter("returnTaskName");
	           String opinion = Struts2Utils.getParameter("opinion");
	           //记录操作日志
	           WorkflowTask task = ApiFactory.getTaskService().getTask(taskId);
	           //保存记录
	           Opinion opinionParameter = new Opinion();
	           opinionParameter.setCustomField("驳回");
	           opinionParameter.setOpinion(opinion);
	           opinionParameter.setTransactor(ContextUtils.getLoginName());
	           opinionParameter.setTransactorName(ContextUtils.getUserName());
	           opinionParameter.setTaskName(task.getName());
	           opinionParameter.setTaskId(taskId);
	           opinionParameter.setAddOpinionDate(new Date());
	           ApiFactory.getOpinionService().saveOpinion(opinionParameter);
	           //驳回操作
	           ApiFactory.getTaskService().returnTaskTo(taskId,returnTaskName);
	       } catch (Exception e) {
	           //e.printStackTrace();
	           result.put("error","驳回任务出错!请联系系统管理员!");
	           logger.error("驳回任务出错!",e);
	       }
	       renderText(result.toString());
	       return null;
	   }
	   
	   @Action("monitor-list")
	   public String listMonitor() throws Exception {
	       // TODO Auto-generated method stub
	       return SUCCESS;
	   }
	   @Action("monitor-list-datas")
	   public String listMonitorDatas() throws Exception {
	       try{
	           page = getAmbWorkflowBaseManager().search(page);
	           renderText(PageUtils.pageToJson(page));
	       }catch(Exception e){
	           getLogger().error("流程跟踪查询失败!",e);
	       }
	       return null;
	   }
	   /**
		 * 领取任务
		 */
		@Override
		@Action("drawtask")
		@LogInfo(optType="领取",message="领取任务")
		public String drawTask() {
			getAmbWorkflowBaseManager().drawTask(taskId);
			task = getAmbWorkflowBaseManager().getWorkflowTask(taskId);
			return "process-task";
		}
	   private List<String> returnableTaskNames = new ArrayList<String>();
	   
	   public List<String> getReturnableTaskNames() {
	       returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
	       return returnableTaskNames;
	   }
	   
	   public void setReturnableTaskNames(List<String> returnableTaskNames) {
	       this.returnableTaskNames = returnableTaskNames;
	   }
}

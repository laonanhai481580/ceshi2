package com.ambition.product.workflow;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.ort.service.OrtTestEntrustManager;
import com.ambition.product.base.WorkflowIdEntity;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CheckMobileUtil;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.acs.web.authorization.JsTreeUtil1;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.wf.WorkflowActionSupport;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.opensymphony.xwork2.ActionContext;

import edu.emory.mathcs.backport.java.util.Arrays;
import flex.messaging.util.URLDecoder;
/**
 * 类名:工作流管理后台基类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2015-4-20 发布
 */
public abstract class AmbWorkflowActionBase<T extends WorkflowIdEntity> extends WorkflowActionSupport<T> {
	protected Logger logger = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	public static final String MOBILEINPUT = "mobile-input";
	protected Long id;
	protected String deleteIds;
	protected String formNo;//表单编号
	protected Boolean isComplete=false;//流程是否成功办理--app
	protected T report;//对象
	protected Page<T> page;
	private Page<AmbWorkflowTask> taskPage;
	private Page<AmbOnTimeCloseRate> ontimeClosePage;//按时完成率
	private String addSignPerson;//加签人员
	private String removeSignPerson;//减签人员
	private String copyPerson;//抄送人员
	private List<String[]> handerList = new ArrayList<String[]>();//减签环节办理人list
	private String assignee; //指派人
	protected String submitResult;//任务提交结果
	protected JSONObject params;
	@Autowired
	private OrtTestEntrustManager ortTestEntrustManager;
	@Autowired
	private LogUtilDao logUtilDao;
	
	public Boolean getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}
	protected List<String> returnableTaskNames = new ArrayList<String>();//驳回流转任务
	
	/**
	  * 方法名: 获取Service类
	  * <p>功能说明：</p>
	  * @return
	 */
	protected abstract AmbWorkflowManagerBase<T> getAmbWorkflowBaseManager();
	
	/**
	  * 方法名: 初始化表单元素需要的值
	  * <p>功能说明：</p>
	 */
	public void initForm(){
		
	}
	
	@Override
	public T getModel() {
		return report;
	}
	
	@Override
	protected void prepareModel() throws Exception {
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
			if(taskId != null){
				ApiFactory.getFormService().fillEntityByTask(report, taskId);
			}
	    }else if(id==null){
	    	report = getAmbWorkflowBaseManager().getEntityInstanceClass().newInstance();
	    	report.setCompanyId(ContextUtils.getCompanyId());
	    	report.setSubCompanyId(ContextUtils.getSubCompanyId());
	    	report.setDepartmentId(ContextUtils.getDepartmentId());
	    	report.setCreatedTime(new Date());
	    	report.setCreator(ContextUtils.getLoginName());
	    	report.setCreatorName(ContextUtils.getUserName());
	    	report.setBusinessUnitName(ContextUtils.getSubCompanyName());
	    	report.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
	    }
		//未办理,并且办理人为当前用户才可以驳回
		if(task != null && task.getTransactDate() == null && task.getTransactor().equals(ContextUtils.getLoginName())){
			returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
		}
	}
	
	/**
	 * 预处理工作流
	 * @throws Exception
	 */
	public void prepareTask() throws Exception {
		prepareModel();
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
		if(CheckMobileUtil.isMobile(Struts2Utils.getRequest())){
			ActionContext.getContext().put("userTreeHtml",generateDepartmentUserTree());
			return MOBILEINPUT;
		}
		return SUCCESS;
	}
	/**
	 * 生成部门用户
	 * @return
	 */
	public StringBuffer generateDepartmentUserTree(){
		List<com.norteksoft.acs.entity.organization.Department> allDepartments = ortTestEntrustManager.queryAllDepartments();
		List<com.norteksoft.acs.entity.organization.User> allUsers = ortTestEntrustManager.queryAllUsers();
		StringBuffer userHtml = new StringBuffer();
		List<com.norteksoft.acs.entity.organization.Department> parentDepts = queryChildrens(allDepartments,null);
		for(com.norteksoft.acs.entity.organization.Department dept : parentDepts){
			generateHtml(userHtml, dept, allDepartments, allUsers);
		}
		//无部门用户
//		generateHtml(userHtml,null, allDepartments, allUsers);
		return userHtml;
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
	private void generateHtml(StringBuffer html,com.norteksoft.acs.entity.organization.Department dept,
			List<com.norteksoft.acs.entity.organization.Department> allDepartments,
			List<com.norteksoft.acs.entity.organization.User> allUsers){
		List<com.norteksoft.acs.entity.organization.User> users = queryUsersByDeptId(allUsers,dept==null?null:dept.getId());
		List<com.norteksoft.acs.entity.organization.Department> children = queryChildrens(allDepartments,dept==null?null:dept.getId());
		if(users.isEmpty()&&children.isEmpty()){
			//html.append("<p>"+dept.getName()+"</p>");
		}else{
			html.append("<li style=\"margin-left:20px;\">");
			html.append("<label><a href=\"javascript:;\" onclick=\"showdiv('"+(dept==null?"noId":dept.getName())+"')\" >"+(dept==null?"无部门用户":dept.getName())+"</a></label>");
			html.append("<div style=\"display:none;\" id="+(dept==null?"noId":dept.getName())+"><ul class=\"two\" style=\"margin-left:30px;\">");
			for(com.norteksoft.acs.entity.organization.User user : users){
				html.append("<li><label><input  type=\"checkbox\" name='"+user.getName()+"' deptName="+(dept==null?"noId":dept.getName())+"  value='"+user.getLoginName()+"'><a  href=\"javascript:;\" >"+user.getName()+"</a></label></li>");
				
			}
			for(com.norteksoft.acs.entity.organization.Department child : children){
				generateHtml(html,child,allDepartments,allUsers);
			}
			html.append("</ul></div>");
			html.append("</li>");
		}
	}
	private List<com.norteksoft.acs.entity.organization.User> queryUsersByDeptId(List<com.norteksoft.acs.entity.organization.User> allUsers,Long deptId){
		List<com.norteksoft.acs.entity.organization.User> users = new ArrayList<com.norteksoft.acs.entity.organization.User>();
		for(com.norteksoft.acs.entity.organization.User u : allUsers){
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
	 * 抄送
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Action("copytask")
	@LogInfo(optType="抄送",message="抄送任务")
	public String copyTasks(){
		List<String> loginNames=new ArrayList<String>();
		if("all_user".equals(copyPerson)){
			List<User> users=ApiFactory.getAcsService().getAllUsersByBranch(ContextUtils.getSubCompanyId());
			for(User u:users){
				loginNames.add(u.getLoginName());
			}
		}else{
			loginNames=Arrays.asList(copyPerson.split(","));
		}
		try {
			getAmbWorkflowBaseManager().createCopyTasks(taskId, loginNames, null, null);
			renderText("已抄送");
		} catch (Exception e) {
			renderText("抄送失败!");
			getLogger().error("抄送失败！"+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 退回
	 * @return
	 */
	@Action("goback")
	@LogInfo(optType="退回",message="退回任务")
	public String goback(){
		String msg=getAmbWorkflowBaseManager().goback(taskId);
		task=getAmbWorkflowBaseManager().getWorkflowTask(taskId);
		try {
			report=getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
		} catch (Exception e) {
			getLogger().error("退回失败！",e);
		}
		renderText(msg);
		return null;
	}
	
	/**
	 * 放弃领取任务
	 */
	@Override
	@Action("abandonreceive")
	@LogInfo(optType="放弃",message="放弃领取任务")
	public String abandonReceive() {
		getAmbWorkflowBaseManager().abandonReceive(taskId);
		task=getAmbWorkflowBaseManager().getWorkflowTask(taskId);
		return "process-task";
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
		getAmbWorkflowBaseManager().addSigner(taskId, lists);
		renderText("加签成功！");
		return null;
	}
	
	/**
	 * 完成交互任务：用于选人、选环节、填意见
	 */
	@Override
	@Action("completeinteractivetask")
	public String completeInteractiveTask() {
		return null;
	}
	
	/**
	  * 方法名: 完成前
	  * <p>功能说明：</p>
	 * @throws Exception 
	 */
	public void beforeCompleteCallback() throws Exception{
//		CommonUtil.setProperty(report);
	}
	
	/**
	  * 方法名: 完成后
	  * <p>功能说明：</p>
	 * @throws Exception 
	 */
	public void afterCompleteCallback() throws Exception{
		
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
	 * 绑定完成任务
	 */
	
	public void prepareCompleteTask() throws Exception{
		prepareModel();
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
	
	/**
	 * 填写意见
	 */
	@Override
	@Action("fillopinion")
	public String fillOpinion() {
		return null;
	}
	
	/**
	 * 流程监控中应急处理功能
	 */
	@Override
	@Action("processemergency")
	public String processEmergency() {
		return null;
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
		getAmbWorkflowBaseManager().removeSigner(taskId, lists);
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
		handerList = getAmbWorkflowBaseManager().getTaskHander(report);
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
			getAmbWorkflowBaseManager().assign(taskId, assignee);
			renderText("指派完成");
		} catch (Exception e) {
			renderText("指派失败!");
			getLogger().error("指派办理人员失败!",e);
		}
		return null;
	}
	
	/**
	 * 取回任务
	 */
	@Override
	@Action("retrievetask")
	@LogInfo(optType="取回任务",message="取回任务")
	public String retrieveTask() {
		try {
			report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
			Boolean is = getAmbWorkflowBaseManager().retrieveTask(report);
			if(is){
				String msg=getAmbWorkflowBaseManager().retrieve(taskId);
				renderText(msg);
			}else{
				renderText("您没有些操作权限！");
			}
		} catch (Exception e) {
			getLogger().error("取回任务失败!",e);
			renderText("取回任务失败," + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 显示流转历史
	 */
	@Override
	@Action("showhistory")
	public String showHistory() {
		report=getAmbWorkflowBaseManager().getEntity(id);
		return "history";
	}
	
	/**
	 * 绑定流转历史
	 */
	public void prepareShowHistory() throws Exception {
		prepareModel();
	}
	
	/**
	 * 绑定提交流程
	 */
	public void prepareSubmitProcess() throws Exception {
		prepareModel();
	}
	
	/**
	  * 方法名: 提交前
	  * <p>功能说明：</p>
	 * @throws Exception 
	 */
	public void beforeSubmitCallback() throws Exception{
//		CommonUtil.setProperty(report);
	}
	
	/**
	  * 方法名: 提交后
	  * <p>功能说明：</p>
	 * @throws Exception 
	 */
	public void afterSubmitCallback() throws Exception{
		
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
			beforeSubmitCallback();
			//子表信息
			Map<String, List<JSONObject>> childMaps = getChildrenInfos();
			CompleteTaskTipType completeTaskTipType =  getAmbWorkflowBaseManager().submitProcess(report,childMaps);
			submitResult = getAmbWorkflowBaseManager().getCompleteTaskTipType(completeTaskTipType,report);
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
	
	/**
	 * 删除
	 */
	@Override
	@Action("delete")
	@LogInfo(optType="删除",message="删除数据")
	public String delete() throws Exception {
		getLogger().info("删除记录");
		try {
			getAmbWorkflowBaseManager().deleteEntity(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据成功!");
		} catch (Exception e) {
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据失败!");
			renderText("删除失败!"+ e.getMessage());
			getLogger().error("删除失败!", e);
		}
		return null;
	}
	
	/**
	 * 新建页面
	 */
	@Override
	@Action("input")
	public String input() throws Exception {
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		initForm();
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
	 * 表单详情
	 * @return
	 * @throws Exception
	 */
	@Action("view-info")
    public String viewInfo() throws Exception{
		if(id != null){
			report = getAmbWorkflowBaseManager().getEntity(id);
		}else if(StringUtils.isNotEmpty(formNo)){
			report = getAmbWorkflowBaseManager().findReportByFormNo(formNo);
			if(report == null){
	        	addActionMessage("表单编号为["+formNo+"]的单据不存在!");
	        }
		}else{
			addActionMessage("表单不存在!");
		}
		initForm();
        fieldPermission = "[{request:\"false\",readonly:\"true\",controlType:\"allReadolny\"}]";
        ActionContext.getContext().getValueStack().push(report);
        if(report != null && report.getWorkflowInfo() != null){
        	List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(report);
    		if(!opinionParameters.isEmpty() && opinionParameters.size()!=0){
    			Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
    		}else{
    			Struts2Utils.getRequest().setAttribute("opinionParameters", new ArrayList<Opinion>());
    		}
        }
        return SUCCESS;
    }
	
	/**
	 * 列表页面
	 */
	@Override
	@Action("list")
	public String list() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 列表页面-流程监控
	 */
	@Action("list-monitor")
	public String listMonitor() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 列表数据
	 */
	@Action("list-datas")
	@LogInfo(optType="查询",message="查询数据")
	public String getListDatas() throws Exception {
		try{
			page = getAmbWorkflowBaseManager().search(page);
			renderText(PageUtils.pageToJson(page));
//			logUtilDao.debugLog("查询","查询数据",5240l);
		}catch(Exception e){
			getLogger().error("查询失败!",e);
		}
		return null;
	}

	/**
	  * 方法名: QRQC节点超期统计
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("task-list")
	public String taskList() throws Exception {
		try {
			getAmbWorkflowBaseManager().updateOverdueHours();
		} catch (Exception e) {
			getLogger().error("更新超时时间出错",e);
		}
		return SUCCESS;
	}
	
	/**
	 * 列表数据
	 */
	@Action("task-list-datas")
	@LogInfo(optType="查询",message="查询节点超期统计数据")
	public String getTaskListDatas() throws Exception {
		try{
			taskPage = getAmbWorkflowBaseManager().searchTaskDatas(taskPage);
			renderText(PageUtils.pageToJson(taskPage));
		}catch(Exception e){
			getLogger().error("查询任务数据失败!",e);
		}
		return null;
	}
	
		/**
	 * 方法名: 导出节点超期任务数据
	 * <p>
	 * 功能说明：
	 * </p>
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("export-task-datas")
	@LogInfo(optType="导出",message="导出节点超期统计数据")
	public String exportTaskDatas() throws Exception {
		Page<AmbWorkflowTask> taskPage = new Page<AmbWorkflowTask>(Integer.MAX_VALUE);
		taskPage = getAmbWorkflowBaseManager().searchTaskDatas(taskPage);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(taskPage, Struts2Utils.getParameter("_list_code")), "节点超期列表"));
		return null;
	}
	
	/**
	  * 方法名: 按时关闭率统计页面
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("ontime-close-rate")
	public String ontimeCloseRate() throws Exception {
		return SUCCESS;
	}
	
	/**
	  * 方法名: 按时关闭率统计数据
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("ontime-close-rate-datas")
	@LogInfo(optType="查询",message="查询按时关闭率统计数据")
	public String getOntimeCloseRateDatas() throws Exception {
		try{
			ontimeClosePage = getAmbWorkflowBaseManager().searchOntimeCloseDatas(ontimeClosePage,false);
			renderText(PageUtils.pageToJson(ontimeClosePage));
		}catch(Exception e){
			getLogger().error("查询按时关闭率失败!",e);
		}
		return null;
	}
	
	/**
	  * 方法名: 导出按时完成率数据
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("export-ontime-close-rate")
	@LogInfo(optType="导出",message="导出按时关闭率统计数据")
	public String exportOntimeCloseRate() throws Exception {
		Page<AmbOnTimeCloseRate> page = new Page<AmbOnTimeCloseRate>(Integer.MAX_VALUE);
		page = getAmbWorkflowBaseManager().searchOntimeCloseDatas(page,true);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"按时完成率"));
		return null;
	}
	
	/**
	  * 方法名: 保存前
	  * <p>功能说明：</p>
	 * @throws Exception 
	 */
	public void beforeSaveCallback() throws Exception{
		//CommonUtil.setProperty(report);
	}
	
	/**
	  * 方法名: 保存后
	  * <p>功能说明：</p>
	 * @throws Exception 
	 */
	public void afterSaveCallback() throws Exception{
		
	}
	
	/**
	 * 保存
	 */
	@Override
	@Action("save")
	@LogInfo(optType="保存",message="保存数据")
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
				returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
			}
			addActionMessage("保存成功!");
			
			afterSaveCallback();
			//成功标志
			ActionContext.getContext().put("_successFlag",true);
			//只是保存
			ActionContext.getContext().put("_isSave",true);
		} catch (Exception e) {
			getLogger().error("保存"+getAmbWorkflowBaseManager().getWorkflowDefinitionName()+"失败!",e);
			addActionMessage("保存"+getAmbWorkflowBaseManager().getWorkflowDefinitionName()+"失败," + e.getMessage());
			if(id != null){
				report = getAmbWorkflowBaseManager().getEntity(id);
			}else if(taskId != null){
				report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
				returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
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
		//自定义返回页面时返自定义的地址
		String customInputTypeFormUrl = Struts2Utils.getParameter("customInputTypeFormUrl");
		if(StringUtils.isNotEmpty(customInputTypeFormUrl)){
			return customInputTypeFormUrl;
		}
		return returnurl;
	}
	
	/**
	  * 方法名: 获取子表信息
	  * <p>功能说明：</p>
	  * @return
	 */
	public Map<String,List<JSONObject>> getChildrenInfos(){
		String _childrenInfos = Struts2Utils.getParameter("_childrenInfos");
		if(StringUtils.isEmpty(_childrenInfos)){
			return null;
		}
		_childrenInfos = URLDecoder.decode(_childrenInfos);
		JSONArray childrenInfos = JSONArray.fromObject(_childrenInfos);
		Map<String,List<JSONObject>> childMaps = new HashMap<String, List<JSONObject>>();
		String flagKey = "__flag";
		for(Object obj : childrenInfos){
			JSONObject childInfo = (JSONObject)obj;
			String flags = childInfo.getString("flags");
			List<JSONObject> items = new ArrayList<JSONObject>();
			String[] flagStrs = flags.split(",");
			Map<String,JSONObject> itemMap = new HashMap<String, JSONObject>();
			for(String flag : flagStrs){
				if(StringUtils.isNotEmpty(flag)&&!itemMap.containsKey(flag)){
					JSONObject json = new JSONObject();
					json.put(flagKey,flag);
					json.put("entityClass",childInfo.getString("entityClass"));
					itemMap.put(flag,json);
					items.add(json);
				}
			}
			@SuppressWarnings("unchecked")
			Map<String,String> paramMap = Struts2Utils.getRequest().getParameterMap();
			for(String key : paramMap.keySet()){
				if(key.indexOf("_")>0){
					String name = key.substring(0,key.indexOf("_"));
					if(itemMap.containsKey(name)){
						String fieldName = key.substring(key.indexOf("_")+1,key.length());
						itemMap.get(name).put(fieldName,Struts2Utils.getParameter(key));
					}
				}
			}
			int size = items.size();
			for(int i=0;i<size;i++){
				JSONObject his = items.get(i);
				items.set(i,itemMap.get(his.getString(flagKey)));
				his.remove(flagKey);
			}
			String childrenFieldName = childInfo.getString("fieldName");//子集的字段名称
			childMaps.put(childrenFieldName,items);
		}
		return childMaps;
	}
	/**
	 * 获取权限
	 */
	public void getRight(Long taskId,String defCode) {
		if(taskId==null){
			fieldPermission = getAmbWorkflowBaseManager().getFieldPermission(defCode);//禁止或必填字段
			//taskPermission = qrqcReportManager.getActivityPermission(defCode);
			//Long processId = ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(defCode).get(0).getId();
			//taskPermission = qrqcReportManager.getActivityPermission(processId);
		}else{
			fieldPermission = getAmbWorkflowBaseManager().getFieldPermissionByTaskId(taskId);//禁止或必填字段
			//taskPermission = qrqcReportManager.getActivityPermission(taskId);
			//Long processId = ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(defCode).get(0).getId();
			//taskPermission = qrqcReportManager.getActivityPermission(processId);
		}
	}
	
	/**
	 * 导出台账
	 * @return
	 * @throws Exception
	 */
	@Action("export")
	@LogInfo(optType="导出",message="导出数据")
	public String export() throws Exception {
		Page<T> page = new Page<T>(65535);
		page = getAmbWorkflowBaseManager().search(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,getAmbWorkflowBaseManager().getEntityListCode()),getAmbWorkflowBaseManager().getWorkflowDefinitionName()));
		return null;
	}
	
	/**
	  * 方法名:按时关闭率详情页面
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("ontime-close-detail")
	public String ontimeCloseDetail(){
		params = CommonUtil1.convertJsonObject(params);
		return SUCCESS;
	}
	
	/**
	  * 方法名: 按时完成率详情台帐数据
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("ontime-close-detail-datas")
	public String getOntimeCloseDetailDatas() throws Exception {
		try {
			page = getAmbWorkflowBaseManager().searchForOntimeRateParams(page,CommonUtil1.convertJsonObject(params));
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			renderText("{}");
			getLogger().error(e);
		}
		return null;
	}

	/**
	  * 方法名: 导出
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("export-excel-report")
	@LogInfo(optType="导出",message="导出报告")
	public String exportReportForExcel() throws Exception{
		try {
			getAmbWorkflowBaseManager().exportReport(id);
	    } catch (Exception e) {
	   	   getLogger().error("导出失败!",e);
	       renderText("导出失败:" + e.getMessage());
	    }
		return null;
	}
	
	/**
	 * 驳回流转任务
	 * @return
	 * @throws Exception
	 */
	@Action("return-to-task")
	@LogInfo(optType = "驳回", message = "驳回流转任务到任意环节")
	public String returnToTask() throws Exception {
		JSONObject result = new JSONObject();
		try {
			String returnTaskName = Struts2Utils.getParameter("returnTaskName");
			String opinion = Struts2Utils.getParameter("opinion");
			getAmbWorkflowBaseManager().returnToTask(taskId, returnTaskName,opinion);
			prepareModel();//驳回前预处理对象
			afterCompleteCallback();//驳回属于办理任务完成的一个功能点
		} catch (Exception e) {
			getLogger().error("驳回任务出错：",e);
			result.put("error","驳回任务出错："+e.getMessage());
		}
		renderText(result.toString());
		return null;
	}
	
	public Logger getLogger() {
		return logger;
	}
	public void setLogger(Logger logger) {
		this.logger = logger;
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
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public T getReport() {
		return report;
	}
	public void setReport(T report) {
		this.report = report;
	}
	public Page<T> getPage() {
		return page;
	}
	public void setPage(Page<T> page) {
		this.page = page;
	}
	public Page<AmbWorkflowTask> getTaskPage() {
		return taskPage;
	}
	public void setTaskPage(Page<AmbWorkflowTask> taskPage) {
		this.taskPage = taskPage;
	}
	public Page<AmbOnTimeCloseRate> getOntimeClosePage() {
		return ontimeClosePage;
	}
	public void setOntimeClosePage(Page<AmbOnTimeCloseRate> ontimeClosePage) {
		this.ontimeClosePage = ontimeClosePage;
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
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
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
	
}

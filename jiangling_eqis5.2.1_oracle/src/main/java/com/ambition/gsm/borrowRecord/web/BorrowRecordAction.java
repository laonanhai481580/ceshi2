package com.ambition.gsm.borrowRecord.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import com.ambition.carmfg.ort.service.OrtTestEntrustManager;
import com.ambition.gsm.borrowRecord.service.BorrowRecordManager;
import com.ambition.gsm.entity.BorrowRecord;
import com.ambition.gsm.entity.BorrowRecordSublist;
import com.ambition.gsm.entity.Entrust;
import com.ambition.gsm.entity.EntrustSublist;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CheckMobileUtil;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/gsm/gsmUseRecord/borrowRecord")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/gsm/gsmUseRecord/borrowRecord", type = "redirectAction"),
	@Result(name = "mobile-input", location = "mobile/input.jsp",type="dispatcher")})
public class BorrowRecordAction extends AmbWorkflowActionBase<BorrowRecord>{
	@Autowired
	private  BorrowRecordManager borrowRecordManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	public static final String MOBILEINPUT = "mobile-input";
	@Autowired
	private OrtTestEntrustManager ortTestEntrustManager;
	@Autowired
	private AcsUtils acsUtils;
//	private BorrowRecord borrowRecord;
	
	private static final long serialVersionUID = 1L;
	@Override
	protected AmbWorkflowManagerBase<BorrowRecord> getAmbWorkflowBaseManager() {
		// TODO Auto-generated method stub
		return borrowRecordManager;
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
			if("BGRQR".equals(task.getCode())){
				report.setPreserverDept(acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName());
			}
			if("SYSQR".equals(task.getCode())){
				report.setConfirmorDept(acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName());
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
	public void initForm(){
		if(getId() == null){
			getReport().setFormNo(formCodeGenerated.generateGsmBorrowRecordNo());
			getReport().setBorrowDate(new Date());
			getReport().setBorrower(ContextUtils.getUserName());
			getReport().setBorrowerDept(acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName());
		}
//		if(getReport().getPreserver() !=null || getReport().getPreserver() == ContextUtils.getUserName()){
//			getReport().setPreserverDept(acsUtils.getManDepartment(getReport().getPreserverLogin(), ContextUtils.getCompanyId()).getName());
//		}
//		if(getReport().getConfirmor() !=null || getReport().getConfirmor() == ContextUtils.getUserName()){
//			getReport().setConfirmorDept(acsUtils.getManDepartment(getReport().getConfirmorLogin(), ContextUtils.getCompanyId()).getName());
//		}
		
		List <BorrowRecordSublist> borrowRecordSublists = getReport().getBorrowRecordSublists();
		if(borrowRecordSublists == null){
			borrowRecordSublists = new ArrayList<BorrowRecordSublist>();
			BorrowRecordSublist item = new BorrowRecordSublist();
			borrowRecordSublists.add(item);
		}
		
//		System.out.println(getReport().getPreserverLogin());
//		System.out.println("222");
		ActionContext.getContext().put("_borrowRecordSublists",borrowRecordSublists);
		ActionContext.getContext().put("businessDivisions",ApiFactory.getSettingService().getOptionsByGroupCode("_businessDivision"));
	}
	
	
	/**
	 * 
	 * @return
	 */
	@Action("child-table-input")
	public String childTableInput(){
		try {
			if(id != null){
				BorrowRecord borrowRecord = borrowRecordManager.getEntity(id);
				if(borrowRecord != null){
					ActionContext.getContext().getValueStack().push(borrowRecord);
					//初始化子表
					List<BorrowRecordSublist> borrowRecordSublists = borrowRecord.getBorrowRecordSublists();
					if(borrowRecordSublists==null || borrowRecordSublists.size() == 0){
						borrowRecordSublists = new ArrayList<BorrowRecordSublist>();
						borrowRecordSublists.add(new BorrowRecordSublist());						
					}
					ActionContext.getContext().put("_borrowRecordSublists",borrowRecordSublists);
				}else{
					throw new AmbFrameException("数据出错！");
				}
			}else{
				throw new AmbFrameException("参数缺失！");
			}
		} catch (Exception e) 	{
			ActionContext.getContext().put("errorMessage", "参数缺失！");
			logger.error("加载外校委托-台账子表详情出错："+ e.getMessage(), e);
		}
		return SUCCESS;
	}
	@Override
	@Action("input")
	public String input() throws Exception {
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		initForm();
		if (report != null && report.getWorkflowInfo() != null) {
			List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(report);
			if (!opinionParameters.isEmpty() && opinionParameters.size() != 0) {
				Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
			} else {
				Struts2Utils.getRequest().setAttribute("opinionParameters",	new ArrayList<Opinion>());
			}
		}
		if(CheckMobileUtil.isMobile(Struts2Utils.getRequest())){
			ActionContext.getContext().put("userTreeHtml",generateDepartmentUserTree());
			return MOBILEINPUT;
		}
		return SUCCESS;
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
			if(CheckMobileUtil.isMobile(Struts2Utils.getRequest())){
				String childParams=Struts2Utils.getParameter("_childrenInfos");
				borrowRecordManager.saveChild(report,childParams);
			}else{
				Map<String, List<JSONObject>> childMaps = getChildrenInfos();
				getAmbWorkflowBaseManager().saveEntity(report,childMaps);
			}
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
			if(CheckMobileUtil.isMobile(Struts2Utils.getRequest())){
				String childParams=Struts2Utils.getParameter("_childrenInfos");
				borrowRecordManager.saveChild(report,childParams);
				completeTaskTipType =  getAmbWorkflowBaseManager().completeTask(report,null,taskId, taskTransact);
			}else{
				Map<String, List<JSONObject>> childMaps = getChildrenInfos();
				completeTaskTipType =  getAmbWorkflowBaseManager().completeTask(report,childMaps,taskId, taskTransact);
			}
            getAmbWorkflowBaseManager().saveEntity(report);
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
		}//判断是否手机端发起的请求
		if(CheckMobileUtil.isMobile(Struts2Utils.getRequest())){
			ActionContext.getContext().put("userTreeHtml",generateDepartmentUserTree());
			isComplete = true;
			return MOBILEINPUT;
		}else{
			String returnurl = "inputform".equals(Struts2Utils.getParameter("inputformortaskform"))?"input":"process-task";
			return returnurl;
		}
	}
	/**
	 * 生成部门用户
	 * @return
	 */
	public StringBuffer generateDepartmentUserTree(){
		List<com.norteksoft.acs.entity.organization.Department> allDepartments = ortTestEntrustManager.queryAllDepartments();
		List<User> allUsers = ortTestEntrustManager.queryAllUsers();
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
}

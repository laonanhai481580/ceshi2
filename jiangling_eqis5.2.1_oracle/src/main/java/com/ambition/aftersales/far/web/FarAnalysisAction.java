package com.ambition.aftersales.far.web;

import java.util.ArrayList;
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

import com.ambition.aftersales.entity.FarAnalysis;
import com.ambition.aftersales.entity.FarAnalysisItem;
import com.ambition.aftersales.far.service.FarAnalysisManager;
import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.develop.services.SupplierDevelopManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CheckMobileUtil;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.opensymphony.xwork2.ActionContext;
/**
 * 类名:FAR解析Action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月6日 发布
 */
@Namespace("/aftersales/far")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/aftersales/far", type = "redirectAction") ,
			@Result(name = "mobile-input", location = "mobile/far-input.jsp",type="dispatcher")})
public class FarAnalysisAction extends AmbWorkflowActionBase<FarAnalysis>{
	public static final String MOBILEINPUT = "mobile-input";
	private static final long serialVersionUID = 1L;
	@Autowired
	private SupplierDevelopManager supplierDevelopManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private String ids;
	private String nowTaskName;
	private FarAnalysis farAnalysis;
	private String currentActivityName;
	@Autowired
	private FarAnalysisManager farAnalysisManager;
	
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


	public FarAnalysis getFarAnalysis() {
		return farAnalysis;
	}


	public void setFarAnalysis(FarAnalysis farAnalysis) {
		this.farAnalysis = farAnalysis;
	}


	public String getCurrentActivityName() {
		return currentActivityName;
	}


	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}

	@Override
	protected AmbWorkflowManagerBase<FarAnalysis> getAmbWorkflowBaseManager() {
		return farAnalysisManager;
	}
	/**
	  * 方法名: 初始化的参数在这里写
	  * <p>功能说明：</p>
	 */
	public void initForm(){
		if(getId()==null&&getReport().getFormNo()==null){
			getReport().setFormNo(formCodeGenerated.generateFarAnalysisNo());
		}		
		
		List<FarAnalysisItem> farAnalysisItems = getReport().getFarAnalysisItems();
		if(farAnalysisItems==null){
			farAnalysisItems = new ArrayList<FarAnalysisItem>();
			FarAnalysisItem item = new FarAnalysisItem();
			farAnalysisItems.add(item);
		}
		ActionContext.getContext().put("_farAnalysisItems",farAnalysisItems);
		ActionContext.getContext().put("productionEnterpriseGroups", ApiFactory.getSettingService().getOptionsByGroupCode("productionEnterpriseGroup"));
		ActionContext.getContext().put("workingProcedures", ApiFactory.getSettingService().getOptionsByGroupCode("afs_far_working_procedure"));	
		ActionContext.getContext().put("reasons", ApiFactory.getSettingService().getOptionsByGroupCode("afs_far_reason"));	
		ActionContext.getContext().put("departments", ApiFactory.getSettingService().getOptionsByGroupCode("afs_far_department"));	
		ActionContext.getContext().put("closeStates", ApiFactory.getSettingService().getOptionsByGroupCode("afs_far_close_state"));	
		ActionContext.getContext().put("businessUnits", ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits"));
		ActionContext.getContext().put("methods", ApiFactory.getSettingService().getOptionsByGroupCode("afs_far_method"));
	}
	/**
	 * 导出表单
	 * */
	@Action("export-report")
	public String exportReport() {
		try{
			farAnalysisManager.exportReport(id);
		}catch(Exception e){
			createMessage("导出表单失败:" + e.getMessage());
		}
		return null;
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
				farAnalysisManager.saveChild(report,childParams);
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
			Map<String, List<JSONObject>> childMaps = getChildrenInfos();
			completeTaskTipType =  getAmbWorkflowBaseManager().completeTask(report,childMaps,taskId, taskTransact);
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
  		if (CheckMobileUtil.isMobile(Struts2Utils.getRequest())) {
  			ActionContext.getContext().put("userTreeHtml",
  					generateDepartmentUserTree());
  			return MOBILEINPUT;
  		} else {
  			return SUCCESS;
  		}
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
	           	  report.setFormNo(formCodeGenerated.generateFarAnalysisNo());
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
		 * 生成部门用户
		 * @return
		 */
		public StringBuffer generateDepartmentUserTree(){
			List<com.norteksoft.acs.entity.organization.Department> allDepartments = supplierDevelopManager.queryAllDepartments();
			List<User> allUsers = supplierDevelopManager.queryAllUsers();
			StringBuffer userHtml = new StringBuffer();
			List<com.norteksoft.acs.entity.organization.Department> parentDepts = queryChildrens(allDepartments,null);
			for(com.norteksoft.acs.entity.organization.Department dept : parentDepts){
				generateHtml(userHtml, dept, allDepartments, allUsers);
			}
			//无部门用户
//				generateHtml(userHtml,null, allDepartments, allUsers);
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
}

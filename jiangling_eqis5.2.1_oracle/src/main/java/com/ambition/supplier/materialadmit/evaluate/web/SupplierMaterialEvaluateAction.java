package com.ambition.supplier.materialadmit.evaluate.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.entity.SupplierMaterialEvaluate;
import com.ambition.supplier.materialadmit.evaluate.services.SupplierMaterialEvaluateManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CheckMobileUtil;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.opensymphony.xwork2.ActionContext;
/**
 * 类名:样件评估流程
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author linshaowei
 * @version 1.00 2016年10月19日 发布
 */
@Namespace("/supplier/material-admit/evaluate")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/material-admit/evaluate", type = "redirectAction"),
	@Result(name = "mobile-input", location = "mobile/evaluate-input.jsp",type="dispatcher")})
public class SupplierMaterialEvaluateAction  extends AmbWorkflowActionBase<SupplierMaterialEvaluate>{

	private static final long serialVersionUID = 1L;
	@Autowired
	private SupplierMaterialEvaluateManager supplierMaterialEvaluateManager;
	private String ids;
    @Autowired
    private FormCodeGenerated formCodeGenerated;
    @Autowired
    private AcsUtils acsUtils;
    boolean isCurrent = false;
    private String nowTaskName;
    public static final String MOBILEINPUT = "mobile-input";
    
    @Override
    protected AmbWorkflowManagerBase<SupplierMaterialEvaluate> getAmbWorkflowBaseManager() {
    	// TODO Auto-generated method stub
    	return supplierMaterialEvaluateManager;
    }
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public boolean isCurrent() {
		return isCurrent;
	}
	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}
	public String getNowTaskName() {
		 nowTaskName = report.getWorkflowInfo().getCurrentActivityName();  
	     return nowTaskName;
	}
	public void setNowTaskName(String nowTaskName) {
		this.nowTaskName = nowTaskName;
	}
	 /**
     * 方法名: 初始化的参数在这里写
     * <p>功能说明：</p>
    */
   public void initForm(){
       if(getId()==null){
    	   report.setCreatedTime(new Date());
    	   report.setFormNo(formCodeGenerated.getSupplierMaterialEvaluateCode());
    	   report.setCreator(ContextUtils.getLoginName());
    	   report.setCreatorName(ContextUtils.getUserName());
    	  /* report.setBusinessUnitName(ContextUtils.getSubCompanyName());*/
    	   report.setSendInspectionDate(new Date());
    	   if(ApiFactory.getAcsService().getDepartmentsByUserId(ContextUtils.getUserId()).size()!=0){
    		   String departMentName = ApiFactory.getAcsService().getDepartmentsByUserId(ContextUtils.getUserId()).get(0).getName();
        	   report.setDept(departMentName);
    	   }
       }
       if(task!=null){
			if(ContextUtils.getLoginName().equals(task.getTransactor())){
				isCurrent = true;
				ActionContext.getContext().put("isCurrent",isCurrent);
			}
		}
       HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
       if(report.getMaterialEvaluate()!=null){
    	   String[] materialEvaluateArr = report.getMaterialEvaluate().split(",");
    	   List<String> list = new ArrayList<String>();  
    	   for(int i = 0;i < materialEvaluateArr.length; i++){  
    	       list.add(materialEvaluateArr[i].trim());  
    	   }  
    	   request.setAttribute("materialEvaluateList", list); 
       }
       if(report.getSendReason()!=null){
    	   String[] sendReasonArr = report.getSendReason().split(",");
    	   List<String> list = new ArrayList<String>();  
    	   for(int i = 0;i < sendReasonArr.length; i++){  
    	       list.add(sendReasonArr[i].trim());  
    	   }  
    	   request.setAttribute("sendReasonList", list); 
       }
       if(report.getSampleEvaluateProject()!=null){
    	   String[] sampleEvaluateProjectArr = report.getSampleEvaluateProject().split(",");
    	   List<String> list = new ArrayList<String>();  
    	   for(int i = 0;i < sampleEvaluateProjectArr.length; i++){  
    	       list.add(sampleEvaluateProjectArr[i].trim());  
    	   }  
    	   request.setAttribute("sampleEvaluateProjectList", list); 
       }
     ActionContext.getContext().put("urgencyLevels",ApiFactory.getSettingService().getOptionsByGroupCode("supplier_urgency_level"));
     ActionContext.getContext().put("sendReasons",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-send-reasons"));
	 ActionContext.getContext().put("evaluates",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-material-evaluate"));
	 ActionContext.getContext().put("inspectionResults",ApiFactory.getSettingService().getOptionsByGroupCode("supplier_inspection_result"));
	 ActionContext.getContext().put("outInspectionResults",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-out-inspection-result"));
	 ActionContext.getContext().put("ifTests",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-rd-iftest"));
	 ActionContext.getContext().put("evaluateProjects",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-evaluate-project"));
	 ActionContext.getContext().put("materialUseinfos",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-material-useinfo"));
	 ActionContext.getContext().put("labIfTests",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-lab-iftest"));
	 ActionContext.getContext().put("labIfTests",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-lab-iftest"));
	 ActionContext.getContext().put("testContents",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-lab-testcontent"));
	 ActionContext.getContext().put("labTestResults",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-lab-test-result"));
	 ActionContext.getContext().put("okInfos",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-okinfo"));
	 ActionContext.getContext().put("ngInfos",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-nginfo"));
	 ActionContext.getContext().put("hsfValues",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-hsf-value"));
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
		if(CheckMobileUtil.isMobile(Struts2Utils.getRequest())){
			ActionContext.getContext().put("userTreeHtml",generateDepartmentUserTree());
			return MOBILEINPUT;
		}else{
			return SUCCESS;
		}
	}
 	/**
	 * 生成部门用户
	 * @return
	 */
	public StringBuffer generateDepartmentUserTree(){
		List<com.norteksoft.acs.entity.organization.Department> allDepartments = supplierMaterialEvaluateManager.queryAllDepartments();
		List<User> allUsers = supplierMaterialEvaluateManager.queryAllUsers();
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
           	  report.setFormNo(formCodeGenerated.getSupplierMaterialEvaluateCode());
			}
           if(report.getSupplierCode()!=null){
        	   createSupplierUser(report);
           }
           //子表信息
           Map<String, List<JSONObject>> childMaps = getChildrenInfos();
           CompleteTaskTipType completeTaskTipType =  getAmbWorkflowBaseManager().submitProcess(report,childMaps);
           List<String[]> list = getAmbWorkflowBaseManager().getTaskHander(report);
           report.setWorkFlowStageTime(new Date());
	       Object[] taskInfo=null;
           if(list.size()>0){
               taskInfo = list.get(0);
               report.setCurrentTransactor(taskInfo[1].toString());
           }
           getAmbWorkflowBaseManager().saveEntity(report);
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
     		if(CheckMobileUtil.isMobile(Struts2Utils.getRequest())){
     			ActionContext.getContext().put("userTreeHtml",generateDepartmentUserTree());
     			return MOBILEINPUT;
     		}else{
     			 return "input";
     		}
   }
   
   private void createSupplierUser(SupplierMaterialEvaluate report) {
	// TODO Auto-generated method stub
	   //查出供应商部门
	   Department dept = supplierMaterialEvaluateManager.searchSupplierDept();
	   //检查是否存在供应商，不存在就插入
	   supplierMaterialEvaluateManager.saveUser(report,dept);
	
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
			List<String[]> list = getAmbWorkflowBaseManager().getTaskHander(report);
			report.setWorkFlowStageTime(new Date());
	        Object[] taskInfo=null;
            if(list.size()>0){
                taskInfo = list.get(0);
                report.setCurrentTransactor(taskInfo[1].toString());
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
	/**
	 * 导出表单
	 * */
	@Action("export-report")
	public String exportReport() {
		try{
			supplierMaterialEvaluateManager.exportReport(id);
		}catch(Exception e){
			createMessage("导出表单失败:" + e.getMessage());
		}
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
   private List<String> returnableTaskNames = new ArrayList<String>();
   
   public List<String> getReturnableTaskNames() {
       returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
       return returnableTaskNames;
   }
   
   public void setReturnableTaskNames(List<String> returnableTaskNames) {
       this.returnableTaskNames = returnableTaskNames;
   }

}

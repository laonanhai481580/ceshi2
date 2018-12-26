package com.ambition.supplier.audit.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

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
import com.ambition.supplier.audit.services.SupplierAuditImproveManager;
import com.ambition.supplier.entity.SupplierAuditImprove;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.opensymphony.xwork2.ActionContext;
@Namespace("/supplier/audit/improve")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/supplier/audit/improve", type = "redirectAction") })
public class SupplierAuditImproveAction extends AmbWorkflowActionBase<SupplierAuditImprove>{

		/**
		  *SupplierAuditImproveAction.java2016年11月1日
		 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private SupplierAuditImproveManager supplierAuditImproveManager;
	
	private String ids;
    @Autowired
    private FormCodeGenerated formCodeGenerated;
    @Autowired
    private AcsUtils acsUtils;
    boolean isCurrent = false;
    private String nowTaskName;


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

	private List<String> returnableTaskNames = new ArrayList<String>();

	public List<String> getReturnableTaskNames() {
		returnableTaskNames = ApiFactory.getTaskService()
				.getReturnableTaskNames(taskId);
		return returnableTaskNames;
	}

	public void setReturnableTaskNames(List<String> returnableTaskNames) {
		this.returnableTaskNames = returnableTaskNames;
	}
	@Override
	protected AmbWorkflowManagerBase<SupplierAuditImprove> getAmbWorkflowBaseManager() {
		// TODO Auto-generated method stub
		return supplierAuditImproveManager;
	}
	/**
     * 方法名: 初始化的参数在这里写
     * <p>功能说明：</p>
    */
   public void initForm(){
		if (getId() == null) {
			report.setCreatedTime(new Date());
			report.setFormNo(formCodeGenerated.getSupplierAuditImproveCode());
			report.setCreator(ContextUtils.getLoginName());
			report.setCreatorName(ContextUtils.getUserName());
			report.setBusinessUnitName(ContextUtils.getSubCompanyName());
			report.setEvaluateDate(new Date());
			report.setEvaluateMan(ContextUtils.getUserName());
			report.setEvaluateManLog(ContextUtils.getLoginName());
		}
		if (task != null) {
			if (ContextUtils.getLoginName().equals(task.getTransactor())) {
				isCurrent = true;
				ActionContext.getContext().put("isCurrent", isCurrent);
			}
		}
   }


   
   /**
    * 创建返回消息
    * @param error
    * @param message
    * @return
    */
   public void createErrorMessage(String message){
       Map<String,Object> map = new HashMap<String, Object>();
       map.put("error",true);
       map.put("message",message);
       renderText(JSONObject.fromObject(map).toString());
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
           	  report.setFormNo(formCodeGenerated.getSupplierAuditImproveCode());
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
       return "input";
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
}

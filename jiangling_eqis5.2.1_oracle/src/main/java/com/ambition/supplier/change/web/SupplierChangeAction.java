package com.ambition.supplier.change.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.change.services.SupplierChangeManager;
import com.ambition.supplier.entity.SupplierChange;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.opensymphony.xwork2.ActionContext;

/**
 * 类名:供应商变更
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月12日 发布
 */
@Namespace("/supplier/change")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/change", type = "redirectAction") })
public class SupplierChangeAction extends AmbWorkflowActionBase<SupplierChange>{

		/**
		  *SupplierChangeAction.java2016年10月12日
		 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private SupplierChangeManager supplierChangeManager;
	private String ids;
    @Autowired
    private FormCodeGenerated formCodeGenerated;
    private List<String> returnableTaskNames = new ArrayList<String>();
    @Autowired
    private AcsUtils acsUtils;
    boolean isCurrent = false;
    private String nowTaskName;
    private JSONObject result = new JSONObject();
	@Override
	protected AmbWorkflowManagerBase<SupplierChange> getAmbWorkflowBaseManager() {
		// TODO Auto-generated method stub
		return supplierChangeManager;
	}


	public List<String> getReturnableTaskNames() {
		returnableTaskNames = ApiFactory.getTaskService()
				.getReturnableTaskNames(taskId);
		return returnableTaskNames;
	}

	public void setReturnableTaskNames(List<String> returnableTaskNames) {
		this.returnableTaskNames = returnableTaskNames;
	}
	
	public JSONObject getResult() {
		return result;
	}


	public void setResult(JSONObject result) {
		this.result = result;
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
    	   report.setFormNo(formCodeGenerated.getSupplierChangeCode());
    	   report.setCreator(ContextUtils.getLoginName());
    	   report.setCreatorName(ContextUtils.getUserName());
    	   report.setBusinessUnitName(ContextUtils.getSubCompanyName());
       }
       if(task!=null){
			if(ContextUtils.getLoginName().equals(task.getTransactor())){
				isCurrent = true;
				ActionContext.getContext().put("isCurrent",isCurrent);
			}
		}
     ActionContext.getContext().put("levels",ApiFactory.getSettingService().getOptionsByGroupCode("supplier_level_change"));
     ActionContext.getContext().put("businessUnits", ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits"));
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
           	  report.setFormNo(formCodeGenerated.getSupplierEvaluateCode());
			}
           report.setBusinessUnitName(ContextUtils.getSubCompanyName());
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
   
   /**
	 * 列表页面
	 */
	@Override
	@Action("list")
	public String list() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 列表数据
	 */
	@Action("list-datas")
	@LogInfo(optType="查询",message="查询数据")
	public String getListDatas() throws Exception {
		String name = ContextUtils.getLoginName();
		String dept =acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName();
		try{ 
			if("供应商".equals(dept)){
				page = supplierChangeManager.listPageByParams(page, name);
			}else{
				page = supplierChangeManager.search(page);
			}
			page.getResult().size();
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			getLogger().error("查询失败!",e);
		}
		return null;
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
   //匹配变更等级
   @Action("select-level")
   public String selectLevel() throws Exception {
       try{
    	   String valueStr = Struts2Utils.getParameter("valueStr");
           List<Option> options = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_level_change");
           for(Option o : options){
        	   if(valueStr.equals(o.getName())){
        		   result.put("level",o.getValue());
        		   result.put("error",false);
        	   }
           }
       }catch(Exception e){
       	   result.put("error",true);
           result.put("message","找不到对应等级!");
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
			supplierChangeManager.exportReport(id);
		}catch(Exception e){
			createMessage("导出表单失败:" + e.getMessage());
		}
		return null;
	}
}

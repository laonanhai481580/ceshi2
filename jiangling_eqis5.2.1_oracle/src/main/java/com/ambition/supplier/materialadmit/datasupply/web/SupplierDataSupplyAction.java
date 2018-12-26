package com.ambition.supplier.materialadmit.datasupply.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.improve.entity.ImproveReportTeam;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.entity.ProductPartReport;
import com.ambition.supplier.entity.SupplierDataSupply;
import com.ambition.supplier.entity.SupplierMaterialAdmit;
import com.ambition.supplier.materialadmit.datasupply.manager.SupplierDataSupplyManager;
import com.ambition.supplier.materialadmit.evaluate.services.SupplierMaterialEvaluateManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.tools.StringUtils;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.util.AsyncMailUtils;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/supplier/material-admit/data-supply")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/material-admit/data-supply", type = "redirectAction") })
public class SupplierDataSupplyAction extends AmbWorkflowActionBase<SupplierDataSupply>{
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SupplierDataSupplyManager supplierDataSupplyManager;
	private String ids;
    @Autowired
    private FormCodeGenerated formCodeGenerated;
    @Autowired
    private AcsUtils acsUtils;
    boolean isCurrent = false;
    private String nowTaskName;
	@Override
	protected AmbWorkflowManagerBase<SupplierDataSupply> getAmbWorkflowBaseManager() {
		// TODO Auto-generated method stub
		return supplierDataSupplyManager;
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

	private List<String> returnableTaskNames = new ArrayList<String>();

	public List<String> getReturnableTaskNames() {
		returnableTaskNames = ApiFactory.getTaskService()
				.getReturnableTaskNames(taskId);
		return returnableTaskNames;
	}

	public void setReturnableTaskNames(List<String> returnableTaskNames) {
		this.returnableTaskNames = returnableTaskNames;
	}
	 /**
     * 方法名: 初始化的参数在这里写
     * <p>功能说明：</p>
    */
   public void initForm(){
       if(getId()==null){
    	   report.setCreatedTime(new Date());
    	   report.setFormNo(formCodeGenerated.getSupplierDataSupplyCode());
    	   report.setCreator(ContextUtils.getLoginName());
    	   report.setCreatorName(ContextUtils.getUserName());
    	   report.setBusinessUnitName(ContextUtils.getSubCompanyName());
    	   report.setRaiseDate(new Date());
    	   if(ApiFactory.getAcsService().getDepartmentsByUserId(ContextUtils.getUserId()).size()!=0){
    		   String departMentName = ApiFactory.getAcsService().getDepartmentsByUserId(ContextUtils.getUserId()).get(0).getName();
        	   report.setRaiseDept(departMentName);
    	   }
       }
       List<ProductPartReport> productPartReports = getReport().getProductPartReports();
		if(productPartReports==null||productPartReports.size()==0){
			productPartReports = new ArrayList<ProductPartReport>();
			ProductPartReport item = new ProductPartReport();
			productPartReports.add(item);
		}
		ActionContext.getContext().put("_productPartReports",productPartReports);
       if(task!=null){
			if(ContextUtils.getLoginName().equals(task.getTransactor())){
				isCurrent = true;
				ActionContext.getContext().put("isCurrent",isCurrent);
			}
		}
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
	 * 导出表单
	 * */
	@Action("export-report")
	public String exportReport() {
		try{
			supplierDataSupplyManager.exportReport(id);
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
   private void createSupplierUser(SupplierDataSupply report) {
	   //查出供应商部门
	   Department dept = supplierDataSupplyManager.searchSupplierDept();
	   //检查是否存在供应商，不存在就插入
	   supplierDataSupplyManager.saveUser(report,dept);
		
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
           	  report.setFormNo(formCodeGenerated.getSupplierDataSupplyCode());
			}
           if(report.getSupplierLoginName()!=null){
        	   createSupplierUser(report);
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
       try{
    	   String [] b = report.getSupplierMails().split("/");
    	   for(int i=0;i<b.length;i++){
    		   String email = b[i];
	    	   StringBuffer requPath = ServletActionContext.getRequest().getRequestURL();
	    	   String url = requPath.toString().substring(0, requPath.toString().lastIndexOf("/"))+"/input.htm?id=" +report.getId();
	
			   if(StringUtils.isNotEmpty(email)){
					AsyncMailUtils.sendMail(email,"GP资料提供",report.getProductName()+"需要您提供资料,请登录系统查看。您的QIS账号为:"+report.getSupplierLoginName()+",初始密码同账号"+url);
			   }
    	   }
//           ApiFactory.getPortalService().addMessage("supplier", "systemAdmin", "ofilm.systemAdmin",ContextUtils.getLoginName(), "进料异常纠正措施单", report.getSupplierName()+"检验单需要审核", "/improve/input.htm?id="+report.getId());
       }catch(Exception e){
           log.error("自动发送邮件给供应商失败!",e);
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
}

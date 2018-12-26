package com.ambition.supplier.audit.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.archives.service.SupplierManager;
import com.ambition.supplier.audit.services.SupplierYearCheckManager;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.entity.SupplierDevelop;
import com.ambition.supplier.entity.SupplierYearCheck;
import com.ambition.supplier.entity.SupplierYearCheckPlan;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CheckMobileUtil;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.opensymphony.xwork2.ActionContext;

/**
 * 类名:供应商年度稽核
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月27日 发布
 */
@Namespace("/supplier/audit/year")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/supplier/audit/year", type = "redirectAction") })
public class SupplierYearCheckAction extends AmbWorkflowActionBase<SupplierYearCheck>{
		/**
		  *SupplierYearCheckAction.java2016年10月27日
		 */
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	private Long supplierId;
    private String saveSucc;
    @Autowired
    private SupplierYearCheckManager supplierYearCheckManager;
    private Integer year;
    @Autowired
    private SupplierManager supplierManager;
    @Autowired
    private LogUtilDao logUtilDao;
	@Autowired
    private AcsUtils acsUtils;
    boolean isCurrent = false;
    private String nowTaskName;
	@Override
	protected AmbWorkflowManagerBase<SupplierYearCheck> getAmbWorkflowBaseManager() {
		// TODO Auto-generated method stub
		return supplierYearCheckManager;
	}
	public List<String> getReturnableTaskNames() {
       returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
       return returnableTaskNames;
    }
	   
	public void setReturnableTaskNames(List<String> returnableTaskNames) {
	   this.returnableTaskNames = returnableTaskNames;
	}
	public String getNowTaskName() {
		 nowTaskName = report.getWorkflowInfo().getCurrentActivityName();  
	     return nowTaskName;
	}
	public void setNowTaskName(String nowTaskName) {
		this.nowTaskName = nowTaskName;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getSaveSucc() {
		return saveSucc;
	}

	public void setSaveSucc(String saveSucc) {
		this.saveSucc = saveSucc;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}


//	@Override
//	@Action("input")
//	public String input() throws Exception {
//		// TODO Auto-generated method stub
//		return SUCCESS;
//	}

	@Override
	@Action("history-list")
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return SUCCESS;
	}
	@Action("history-list-datas")
    public String listdatas() throws Exception{
        try {
            page = supplierYearCheckManager.getSupplierYearCheckPage(page);
            renderText(PageUtils.pageToJson(page));
        } catch (Exception e) {
            logUtilDao.debugLog("查询失败", "查询失败"+e.getMessage());
        }
        return null;
    }
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		if(taskId!=null){
			report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
	    	id=report.getId();
	    	task = getAmbWorkflowBaseManager().getWorkflowTask(taskId);
	    	ApiFactory.getFormService().fillEntityByTask(report, taskId);
	    }else if(supplierId!=null&&id==null){
			report = supplierYearCheckManager.getSupplierYearCheckBySupplierId(supplierId,year);
			if(report==null){
				report = new SupplierYearCheck();
				report.setCreatedTime(new Date());
				Calendar cal = Calendar.getInstance();
				report.setYear(year);
				report.setSupplierId(supplierId);
				report.setCompanyId(ContextUtils.getCompanyId());
				report.setCreatorName(ContextUtils.getUserName());
				report.setCreator(ContextUtils.getLoginName());
				report.setLastModifiedTime(new Date());
				report.setLastModifier(ContextUtils.getUserName());
				report.setBusinessUnitName(ContextUtils.getSubCompanyName());
				report.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
//				supplierLevelScore.setBusinessUnitName("");//事业部
				Supplier supplier = supplierManager.getSupplier(supplierId);
				report.setYear(year);
				List<SupplierYearCheckPlan> supplierYearCheckPlans = new ArrayList<SupplierYearCheckPlan>();
	        	SupplierYearCheckPlan supplierYearCheckPlan = new SupplierYearCheckPlan();
	        	supplierYearCheckPlans.add(supplierYearCheckPlan);
				if(supplier!=null){
					report.setSupplierName(supplier.getName());
					report.setSupplierCode(supplier.getCode());
					report.setSupplyMaterial(supplier.getSupplyMaterial());
				}
				 ActionContext.getContext().put("_supplierYearCheckPlans",supplierYearCheckPlans);
			}else{
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
			}
		}else if(id!=null){
			report = supplierYearCheckManager.getEntity(id);
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
		}
		//未办理,并且办理人为当前用户才可以驳回
		if(task != null && task.getTransactDate() == null && task.getTransactor().equals(ContextUtils.getLoginName())){
			returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
		}
		 if(task!=null){
				if(ContextUtils.getLoginName().equals(task.getTransactor())){
					isCurrent = true;
					ActionContext.getContext().put("isCurrent",isCurrent);
				}
			} 
        ActionContext.getContext().put("finalCheckResults",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-lab-test-result"));
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
		}
		String returnurl = "inputform".equals(Struts2Utils.getParameter("inputformortaskform"))?"input":"process-task";
		return returnurl;
	}
	/**
	 * 显示流转历史
	 */
	@Override
	@Action("showhistory")
	public String showHistory() {
		 report=getAmbWorkflowBaseManager().getEntity(id);
		 ActionContext.getContext().put("finalCheckResults",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-lab-test-result"));
		return "history";
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
        ActionContext.getContext().put("finalCheckResults",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-lab-test-result"));
        return SUCCESS;
    }
	
}

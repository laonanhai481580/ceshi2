package com.ambition.gsm.entrust.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.gsm.entity.Entrust;
import com.ambition.gsm.entity.EntrustSublist;
import com.ambition.gsm.entrust.service.EntrustManager;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/gsm/entrust")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/gsm/entrust", type = "redirectAction") })
public class EntrustAction extends AmbWorkflowActionBase<Entrust>{
	private static final long serialVersionUID = 1L;
	@Autowired
	public EntrustManager entrustManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Autowired
	private AcsUtils acsUtils;
	@Override
	protected AmbWorkflowManagerBase<Entrust> getAmbWorkflowBaseManager() {
		// TODO Auto-generated method stub
		return entrustManager;
	}
	public void initForm(){
		if(getId() == null){
			getReport().setFormNo(formCodeGenerated.generateGsmEntrustNo());
			getReport().setUseResponsible(ContextUtils.getUserName());
			getReport().setConfirmorDate(new Date());
			getReport().setUseDepartment(acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName());
		}
		
		List<EntrustSublist> entrustSublists = getReport().getEntrustSublists();
		if(entrustSublists == null){
			entrustSublists = new ArrayList<EntrustSublist>();
			EntrustSublist item = new EntrustSublist();
			entrustSublists.add(item);
		}
		ActionContext.getContext().put("_entrustSublists",entrustSublists);
		ActionContext.getContext().put("companybys",ApiFactory.getSettingService().getOptionsByGroupCode("companybys"));
		ActionContext.getContext().put("businessDivisions",ApiFactory.getSettingService().getOptionsByGroupCode("_businessDivision"));
	}
//	
//	@Override
//	public void prepareModel() throws Exception {
//		String sunId = Struts2Utils.getParameter("sunId");
//		if(sunId!=null){
//			EntrustSublist sun = entrustSublistManager.getEntrustSublist(Long.valueOf(sunId));
//			id = sun.getEntrust().getId();
//		}	
//		if(taskId!=null){
//			report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
//	    	id=report.getId();
//	    	task = getAmbWorkflowBaseManager().getWorkflowTask(taskId);
//	    	ApiFactory.getFormService().fillEntityByTask(report, taskId);
//		}else if(id!=null){
//			report = getAmbWorkflowBaseManager().getEntity(id);
//			task = getAmbWorkflowBaseManager().getActiveTaskByTransactorId(report, ContextUtils.getUserId());
//			if(task==null&&report.getWorkflowInfo()!=null){
//				task = getAmbWorkflowBaseManager().getWorkflowTask(report.getWorkflowInfo().getFirstTaskId());
//			}
//			if(task!=null){
//				taskId = task.getId();
//			}
//			if(taskId != null){
//				ApiFactory.getFormService().fillEntityByTask(report, taskId);
//			}
//		}else if(id==null){
//	    	report = getAmbWorkflowBaseManager().getEntityInstanceClass().newInstance();
//	    	report.setCompanyId(ContextUtils.getCompanyId());
//	    	report.setSubCompanyId(ContextUtils.getSubCompanyId());
//	    	report.setDepartmentId(ContextUtils.getDepartmentId());
//	    	report.setCreatedTime(new Date());
//	    	report.setCreator(ContextUtils.getLoginName());
//	    	report.setCreatorName(ContextUtils.getUserName());
//	    	report.setBusinessUnitName(ContextUtils.getSubCompanyName());
//	    }
//		//未办理,并且办理人为当前用户才可以驳回
//		if(task != null && task.getTransactDate() == null && task.getTransactor().equals(ContextUtils.getLoginName())){
//			returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
//		}
//	}
//	
	/**
	 * 外校委托子表详情
	 * @return
	 */
	@Action("child-table-input")
	public String childTableInput(){
		try {
			if(id != null){
				Entrust entrust = entrustManager.getEntity(id);
				if(entrust != null){
					ActionContext.getContext().getValueStack().push(entrust);
					//初始化子表
					List<EntrustSublist> entrustSublists = entrust.getEntrustSublists();
					if(entrustSublists==null || entrustSublists.size() == 0){
						entrustSublists = new ArrayList<EntrustSublist>();
						entrustSublists.add(new EntrustSublist());						
					}
					ActionContext.getContext().put("_entrustSublists",entrustSublists);
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
}

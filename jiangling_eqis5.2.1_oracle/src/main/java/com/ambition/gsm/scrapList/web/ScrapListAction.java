package com.ambition.gsm.scrapList.web;

import java.util.Date;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.gsm.scrapList.service.ScrapListManager;
import com.ambition.gsm.entity.ScrapList;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

@Namespace("/gsm/scrap")
@ParentPackage("default")
@Results({@Result(name=CrudActionSupport.RELOAD, location = "/gsm/scrap", type = "redirectAction")})
public class ScrapListAction extends AmbWorkflowActionBase<ScrapList>{
	private static final int AD=1;
	private static final int FI=2;
	private static final long serialVersionUID = 1L;
	@Autowired
	private ScrapListManager scrapListManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Override
	protected AmbWorkflowManagerBase<ScrapList> getAmbWorkflowBaseManager() {
		// TODO Auto-generated method stub
		return scrapListManager;
	}
	public void initForm(){
		if(getId() == null){
			getReport().setFormNo(formCodeGenerated.generateGsmScrapNo());
			getReport().setTaskCreatedTime(new Date());
			getReport().setProposer(ContextUtils.getUserName());
			getReport().setProposerDate(new Date());
		}
		System.out.println(	report.getSkip());
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
		if(report.getAdminDept() == null&&report.getFinanceAudit() != null){//""  //false
			report.setSkip(AD);
	        }
		if("行政确认".equals(task.getName())){
			if(report.getFinanceAudit()==null){
				report.setSkip(FI);
			}
		}
	}
}

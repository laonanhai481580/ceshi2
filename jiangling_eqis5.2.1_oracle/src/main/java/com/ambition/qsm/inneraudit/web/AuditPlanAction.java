package com.ambition.qsm.inneraudit.web;

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
import com.ambition.qsm.entity.AuditPlan;
import com.ambition.qsm.inneraudit.service.AuditPlanManager;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;
/**
 * 类名:内审计划与实施Action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年10月25日 发布
 */
@Namespace("/qsm/inner-audit/audit-plan")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/qsm/inner-audit/audit-plan", type = "redirectAction") })
public class AuditPlanAction extends AmbWorkflowActionBase<AuditPlan>{

	private static final long serialVersionUID = 1L;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private String ids;
	private String nowTaskName;
	private AuditPlan yearAudit;
	private String currentActivityName;
	@Autowired
	private AuditPlanManager auditPlanManager;
	
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


	public AuditPlan getAuditPlan() {
		return yearAudit;
	}


	public void setAuditPlan(AuditPlan yearAudit) {
		this.yearAudit = yearAudit;
	}


	public String getCurrentActivityName() {
		return currentActivityName;
	}


	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}

	@Override
	protected AmbWorkflowManagerBase<AuditPlan> getAmbWorkflowBaseManager() {
		return auditPlanManager;
	}
	/**
	  * 方法名: 初始化的参数在这里写
	  * <p>功能说明：</p>
	 */
	public void initForm(){
		if(getId()==null&&getReport().getFormNo()==null){
			getReport().setFormNo(formCodeGenerated.generateAuditPlanNo());
			getReport().setAuditDate(new Date());
			getReport().setSubmitter1(ContextUtils.getUserName());
		}	
       HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
       if(report.getSystemType()!=null){
    	   String[] systemTypeArr = report.getSystemType().split(",");
    	   List<String> list = new ArrayList<String>();  
    	   for(int i = 0;i < systemTypeArr.length; i++){  
    	       list.add(systemTypeArr[i].trim());  
    	   }  
    	   request.setAttribute("systemTypeList", list); 
       }		
		ActionContext.getContext().put("companyNames", ApiFactory.getSettingService().getOptionsByGroupCode("qsm_company_name"));	
		ActionContext.getContext().put("systemNames", ApiFactory.getSettingService().getOptionsByGroupCode("qsm_system_name"));	
		ActionContext.getContext().put("auditTypes", ApiFactory.getSettingService().getOptionsByGroupCode("qsm_audit_type"));	
	}
	/**
	 * 导出表单
	 * */
	@Action("export-report")
	@LogInfo(optType="导出表单",message="内审计划与实施")
	public String exportReport() {
		try{
			auditPlanManager.exportReport(id);
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
    public void createMessage(String message){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("error",false);
        map.put("message",message);
        renderText(JSONObject.fromObject(map).toString());
    }
}

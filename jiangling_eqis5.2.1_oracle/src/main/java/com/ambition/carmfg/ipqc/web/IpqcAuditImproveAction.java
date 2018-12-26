package com.ambition.carmfg.ipqc.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.carmfg.entity.IpqcAudit;
import com.ambition.carmfg.entity.IpqcAuditImprove;
import com.ambition.carmfg.ipqc.service.IpqcAuditImproveManager;
import com.ambition.carmfg.ipqc.service.IpqcAuditManager;
import com.ambition.carmfg.ort.service.OrtInspectionItemManager;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;
/**
 * 类名:IPQC稽核问题点改善Action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月9日 发布
 */
@Namespace("/carmfg/ipqc/ipqc-improve")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/carmfg/ipqc/ipqc-improve", type = "redirectAction") })
public class IpqcAuditImproveAction extends AmbWorkflowActionBase<IpqcAuditImprove>{

		/**
		  *IpqcAuditImproveAction.java2016年9月6日
		 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private String ids;
	private String nowTaskName;
	private IpqcAuditImprove ipqcAuditImprove;
	private String currentActivityName;
	@Autowired
	private IpqcAuditImproveManager ipqcAuditImproveManager;
	@Autowired
	private	IpqcAuditManager ipqcAuditManager;
	@Autowired
	private OrtInspectionItemManager ortInspectionItemManager;
	
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


	public IpqcAuditImprove getIpqcAuditImprove() {
		return ipqcAuditImprove;
	}


	public void setIpqcAuditImprove(IpqcAuditImprove ipqcAuditImprove) {
		this.ipqcAuditImprove = ipqcAuditImprove;
	}


	public String getCurrentActivityName() {
		return currentActivityName;
	}


	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}

	@Override
	protected AmbWorkflowManagerBase<IpqcAuditImprove> getAmbWorkflowBaseManager() {
		return ipqcAuditImproveManager;
	}
	
	/**
	 * 删除
	 */
	@Override
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		getLogger().info("删除记录");
		try {
			String str=ipqcAuditImproveManager.deleteEntity(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据成功!单号为："+str);
		} catch (Exception e) {
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据失败!");
			renderText("删除失败!"+ e.getMessage());
			getLogger().error("删除失败!", e);
		}
		return null;
	} 	
	
	
	/**
	  * 方法名: 初始化的参数在这里写
	  * <p>功能说明：</p>
	 */
	public void initForm(){
		if(getId()==null&&getReport().getFormNo()==null){
			getReport().setFormNo(formCodeGenerated.generateIpqcAuditImproveNo());
			getReport().setOperator(ContextUtils.getUserName());
			getReport().setOperatorLogin(ContextUtils.getLoginName());
		}	
		ActionContext.getContext().put("classGroups", ApiFactory.getSettingService().getOptionsByGroupCode("mfg_class_group"));
		ActionContext.getContext().put("stations", ApiFactory.getSettingService().getOptionsByGroupCode("mfg_station"));
		ActionContext.getContext().put("auditTypes", ApiFactory.getSettingService().getOptionsByGroupCode("mfg_audit_type"));
		ActionContext.getContext().put("missing_items", ApiFactory.getSettingService().getOptionsByGroupCode("mfg_missing_items"));
		ActionContext.getContext().put("businessUnitNames", ApiFactory.getSettingService().getOptionsByGroupCode("mfg_business_unit_name"));
		ActionContext.getContext().put("ofilmModels", ApiFactory.getSettingService().getOptionsByGroupCode("mfg_ofilm_Model"));
	}
	
	/**
	 * 新建页面
	 */
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
		String auditId=Struts2Utils.getParameter("auditId");
		if(auditId!=null&&auditId!=""){
			IpqcAudit ipqcAudit=ipqcAuditManager.getIpqcAudit(Long.valueOf(auditId));
			report.setAuditDate(ipqcAudit.getActualDate());
			report.setClassGroup(ipqcAudit.getClassGroup());
			report.setDepartment(ipqcAudit.getDepartment());
			report.setStation(ipqcAudit.getStation());
			report.setAuditMan(ipqcAudit.getOperator());
			report.setAuditType(ipqcAudit.getAuditType());
			report.setMissingItems(ipqcAudit.getMissingItems());
			report.setPlanDate(ipqcAudit.getPlanDate());
			report.setProblemDescribe(ipqcAudit.getProblemDescribe());
		}
		return SUCCESS;
	}
}

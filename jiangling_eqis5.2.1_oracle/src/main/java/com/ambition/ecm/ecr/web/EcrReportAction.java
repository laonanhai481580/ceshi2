package com.ambition.ecm.ecr.web;

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
import com.ambition.ecm.ecr.service.EcrReportManager;
import com.ambition.ecm.entity.EcrReport;
import com.ambition.ecm.entity.EcrReportDetail;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;
/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  wuxuming
 * @version 1.00 2016-10-24 发布
 */
@Namespace("/ecm/ecr")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "ecm/ecr", type = "redirectAction") })
public class EcrReportAction  extends AmbWorkflowActionBase<EcrReport>{
	
	private static final long serialVersionUID = 1L;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private String ids;
	private String nowTaskName;
	@Autowired
	private EcrReportManager ecrReportManager;
	private String workCode;
	@Autowired
	private AcsUtils acsUtils;
	private WorkflowTask task;
	@Override
	protected AmbWorkflowManagerBase<EcrReport> getAmbWorkflowBaseManager() {
		return ecrReportManager;
	}
	
	/**
	  * 方法名: 初始化的参数在这里写
	  * <p>功能说明：</p>
	 */
	public void initForm(){
		if(getId()==null&&getReport().getId()==null){
			getReport().setProposer(ContextUtils.getUserName());
			getReport().setProposerTime(new Date());
			getReport().setEcrReportDetails(new ArrayList<EcrReportDetail>());
		}
		if(getId()==null&&getReport().getEcrReportNo()==null){
			getReport().setEcrReportNo(formCodeGenerated.generateEcrReportCode());
		}
	/*	if(getReport().getEcrReportDetails().isEmpty()){//加载子表
			EcrReportDetail erd= new EcrReportDetail();
			report.getEcrReportDetails().add(erd);
		}*/
		List<EcrReportDetail> ecrReportDetails = new ArrayList<EcrReportDetail>();
		if(report.getEcrReportDetails().isEmpty()){//加载子表
			EcrReportDetail ecrReportDetail=new EcrReportDetail();
			ecrReportDetails.add(ecrReportDetail);
		}else{
			ecrReportDetails.addAll(report.getEcrReportDetails());
		}
		ActionContext.getContext().put("_ecrReportDetails",ecrReportDetails);
		if(report.getWorkflowInfo()==null){
			workCode="stage1";
		}else{
			task = getAmbWorkflowBaseManager().getActiveTaskByTransactorId(getReport(), ContextUtils.getUserId());
			if(task==null&&getReport().getWorkflowInfo()!=null){
				task = getAmbWorkflowBaseManager().getWorkflowTask(getReport().getWorkflowInfo().getFirstTaskId());
			}
			workCode=task.getCode();
		}
		//分发
		ActionContext.getContext().put("businessUnits", ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits"));
		ActionContext.getContext().put("ecm_ecr_dev",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_ecr_dev"));
		//变更原因
		ActionContext.getContext().put("ecm_ecr_tyep",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_ecr_tyep"));
		//变更数据
		ActionContext.getContext().put("ecm_ecr_datas",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_ecr_datas"));
		//库存处理方式
		ActionContext.getContext().put("ecm_ecr_inventory",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_ecr_inventory"));		
		ActionContext.getContext().put("ecm_yes_or_no",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_yes_or_no"));
		List<Option> emergencyDegree = ApiFactory.getSettingService().getOptionsByGroupCode("emergency-egree");
		ActionContext.getContext().put("emergency_degree", emergencyDegree);
	}
	/**
	 * 导出表单
	 * */
	@Action("export-report")
	public String exportReport() {
		try{
			ecrReportManager.exportReport(id);
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

	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}
	
}

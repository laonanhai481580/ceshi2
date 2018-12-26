package com.ambition.improve.exceptionreport.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.improve.entity.QualityExceptionReport;
import com.ambition.improve.exceptionreport.service.QualityExceptionReportManager;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;
/**
 * 类名:品质异常联络单Action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年10月25日 发布
 */
@Namespace("/improve/exception")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/improve/exception", type = "redirectAction") })
public class QualityExceptionReportAction extends AmbWorkflowActionBase<QualityExceptionReport>{

	private static final long serialVersionUID = 1L;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private String ids;
	private String nowTaskName;
	private QualityExceptionReport qualityExceptionReport;
	private String currentActivityName;
	@Autowired
	private QualityExceptionReportManager qualityExceptionReportManager;
	
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

	public QualityExceptionReport getQualityExceptionReport() {
		return qualityExceptionReport;
	}


	public void setQualityExceptionReport(
			QualityExceptionReport qualityExceptionReport) {
		this.qualityExceptionReport = qualityExceptionReport;
	}


	public String getCurrentActivityName() {
		return currentActivityName;
	}


	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}

	@Override
	protected AmbWorkflowManagerBase<QualityExceptionReport> getAmbWorkflowBaseManager() {
		return qualityExceptionReportManager;
	}
	/**
	  * 方法名: 初始化的参数在这里写
	  * <p>功能说明：</p>
	 */
	public void initForm(){
		if(getId()==null&&getReport().getFormNo()==null){
			getReport().setFormNo(formCodeGenerated.generateQualityExceptionReportNo());
			/*getReport().setExceptionDate(new Date());*/
			getReport().setPresentMan(ContextUtils.getUserName());
			getReport().setPresentManLogin(ContextUtils.getLoginName());
		}		
		ActionContext.getContext().put("problemDegrees", ApiFactory.getSettingService().getOptionsByGroupCode("imp_problem_degree"));
		ActionContext.getContext().put("exceptionTypes", ApiFactory.getSettingService().getOptionsByGroupCode("imp_exception_type"));
		ActionContext.getContext().put("exceptionBelongs", ApiFactory.getSettingService().getOptionsByGroupCode("imp_exception_belong"));
	}
	
	/**
	 * 导出表单
	 * */
	@Action("export-report")
	@LogInfo(optType="导出表单",message="品质异常联络单")
	public String exportReport() {
		try{
			qualityExceptionReportManager.exportReport(id);
		}catch(Exception e){
			createMessage("导出表单失败:" + e.getMessage());
		}
		return null;
	}
	
	@Action("return-to-task")
	@Override
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
	 * 删除
	 */
	@Override
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		getLogger().info("删除记录");
		try {
			String str=getAmbWorkflowBaseManager().deleteEntity(deleteIds);
			createMessage(str);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据成功!");
		} catch (Exception e) {
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据失败!");
			renderText("删除失败!"+ e.getMessage());
			getLogger().error("删除失败!", e);
		}
		return null;
	} 
	
	@Action("product-list")
	public String productList() throws Exception {
		return SUCCESS;
	}	
	@Action("quality-list")
	public String qualityList() throws Exception {
		return SUCCESS;
	}	
	@Action("product-list-datas")
	public String getProductListDatas() throws Exception {
		try {
			page = qualityExceptionReportManager.searchPageProduct(page);
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	@Action("quality-list-datas")
	public String getQualityListDatas() throws Exception {
		try {
			page = qualityExceptionReportManager.searchPageQuality(page);
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	/**
	 * 导出台账
	 * @return
	 * @throws Exception
	 */
	@Action("quality-export")
	@LogInfo(optType="导出",message="导出品质异常单数据")
	public String qualityExport() throws Exception {
		Page<QualityExceptionReport> page = new Page<QualityExceptionReport>(65535);
		page = qualityExceptionReportManager.searchPageQuality(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,getAmbWorkflowBaseManager().getEntityListCode()),getAmbWorkflowBaseManager().getWorkflowDefinitionName()));
		return null;
	}	
	
	/**
	 * 导出台账
	 * @return
	 * @throws Exception
	 */
	@Action("product-export")
	@LogInfo(optType="导出",message="导出品质异常单数据")
	public String productExport() throws Exception {
		Page<QualityExceptionReport> page = new Page<QualityExceptionReport>(65535);
		page = qualityExceptionReportManager.searchPageProduct(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,getAmbWorkflowBaseManager().getEntityListCode()),getAmbWorkflowBaseManager().getWorkflowDefinitionName()));
		return null;
	}	
}

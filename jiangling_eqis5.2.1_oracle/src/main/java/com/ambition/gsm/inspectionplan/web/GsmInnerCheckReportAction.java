package com.ambition.gsm.inspectionplan.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import com.ambition.gsm.base.service.CheckStandardManager;
import com.ambition.gsm.entity.CheckReportDetail;
import com.ambition.gsm.entity.CheckReportItem;
import com.ambition.gsm.entity.CheckStandard;
import com.ambition.gsm.entity.CheckStandardDetail;
import com.ambition.gsm.entity.GsmInnerCheckReport;
import com.ambition.gsm.inspectionplan.service.GsmInnerCheckReportManager;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.util.annotation.LogInfo;
import com.ibm.icu.util.Calendar;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;


/**    
 * GsmInnerCheckReportAction.java
 * @authorBy lpf
 *
 */
@Namespace("/gsm/inspectionplan/inner-report")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/gsm/inspectionplan/inner-report", type = "redirectAction") })
public class GsmInnerCheckReportAction extends AmbWorkflowActionBase<GsmInnerCheckReport>{
	private static final long serialVersionUID = 1L;
	private GsmInnerCheckReport gsmInnerCheckReport;
	@Autowired
	private CheckStandardManager checkStandardManager;
	@Autowired
	private GsmInnerCheckReportManager gsmInnerCheckReportManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Autowired
	private AcsUtils acsUtils;
	private String currentActivityName;
	private String ids;
	private String nowTaskName;
	@Override
	protected AmbWorkflowManagerBase<GsmInnerCheckReport> getAmbWorkflowBaseManager() {
		return gsmInnerCheckReportManager;
	}
	/**
	  * 方法名: 初始化的参数在这里写
	  * <p>功能说明：</p>
	 */
	public void initForm(){
		if(getId()==null&&getReport().getFormNo()==null){
			getReport().setFormNo(formCodeGenerated.generateGsmInnerCheckReportFormNo());
			getReport().setCheckMan(ContextUtils.getUserName());
			getReport().setCheckManLogin(ContextUtils.getLoginName());
		}		
		List<CheckReportItem> checkReportItems=getReport().getCheckReportItems();
		if (checkReportItems == null||checkReportItems.size()==0) {
			checkReportItems = new ArrayList<CheckReportItem>();
			CheckReportItem item = new CheckReportItem();
			checkReportItems.add(item);
		}
		List<CheckReportDetail> checkReportDetails=	getReport().getCheckReportDetails();
		if (checkReportDetails == null||checkReportDetails.size()==0) {
			checkReportDetails = new ArrayList<CheckReportDetail>();
			CheckReportDetail item = new CheckReportDetail();
			checkReportDetails.add(item);
		}
		ActionContext.getContext().put("_checkReportDetails",checkReportDetails);
		ActionContext.getContext().put("_checkReportItems", checkReportItems);
		ActionContext.getContext().put("passOrFails", ApiFactory.getSettingService().getOptionsByGroupCode("gsm_pass_or_fail"));		
		ActionContext.getContext().put("problemDegrees", ApiFactory.getSettingService().getOptionsByGroupCode("imp_problem_degree"));
	}	
	@Action("test-items")
	public String getCheckItems() throws Exception {
		try {
			String measurementName = Struts2Utils.getParameter("measurementName");
			String measurementSpecification = Struts2Utils.getParameter("measurementSpecification");
			String manufacturer = Struts2Utils.getParameter("manufacturer");
			String id = Struts2Utils.getParameter("id");
			List<CheckReportDetail> reportDetails=null;
			if(id!=null&&!id.equals("")){
				GsmInnerCheckReport report=gsmInnerCheckReportManager.getEntity(Long.valueOf(id));
				reportDetails= report.getCheckReportDetails();
			}else{
				reportDetails=new ArrayList<CheckReportDetail>();
			}						
			if(measurementName!=null&&measurementSpecification!=null&&manufacturer!=null){
				if(reportDetails.size()==0){					
					CheckStandard checkStandard=checkStandardManager.getCheckStandard(measurementName,measurementSpecification,manufacturer);
					 List<CheckStandardDetail> checkStandardDetails=checkStandard.getCheckItems();
						for (CheckStandardDetail checkStandardDetail : checkStandardDetails) {
							CheckReportDetail detail  = new CheckReportDetail();
							detail.setItemName(checkStandardDetail.getItemName());
							detail.setAllowableError(checkStandardDetail.getAllowableError());
							detail.setStandardValue(checkStandardDetail.getStandardValue());
							reportDetails.add(detail);
						}
					}
				}else{
					CheckReportDetail detail  = new CheckReportDetail();
					reportDetails.add(detail);
				}
			ActionContext.getContext().put("_checkReportDetails",reportDetails);
		} catch (Exception e) {
			addActionError("获取ORT测试项目失败"+e.getMessage());
		}
		ActionContext.getContext().put("passOrFails", ApiFactory.getSettingService().getOptionsByGroupCode("gsm_pass_or_fail"));
		return SUCCESS;
	}
	/**
     * 方法名:导出Excel文件
     * <p>
     * 功能说明：
     * </p>
     * @throws IOException
     */
    @Action("download-report")
    @LogInfo(optType = "导出", message = "导出內校报告")
    public void exportReport() throws Exception {
        try {
            gsmInnerCheckReportManager.exportReport(id);
        } catch (Exception e) {
            e.printStackTrace();
            renderText("导出失败:" + e.getMessage());
        }
    }
	/**
	 * 自动计算计划下次校验日期
	 */
	@Action("check-date-change")
	public String seachMainProductBoms() throws Exception {
		String checkDate = Struts2Utils.getParameter("checkDate");
		String str = Struts2Utils.getParameter("frequency");
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		Date date=null;
		String inspectionPlanDateNext=null;
		StringBuffer sb = new StringBuffer("");
		if(checkDate!=null&&!"".equals(checkDate)){
			date=sdf.parse(checkDate);
			Calendar c = Calendar.getInstance();//获得一个日历的实例
			c.setTime(date);//设置日历时间			
	        if(str!=null&&!"".equals(str)){
	           Integer frequency=Integer.valueOf(str);
	    	   c.add(Calendar.MONTH,frequency);//在日历的月份上增加频率(月)	
	    	   c.add(5, -1);
	    	   inspectionPlanDateNext=sdf.format(c.getTime());	
	    	   sb.append("{\"date\":\""+inspectionPlanDateNext+"\"}");
	        }	
		}
		sb.insert(0,"[").append("]");
		renderText(sb.toString());
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
	
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
	
	/**
	 * 列表数据
	 */
	@Action("list-state")
	@LogInfo(optType="查询",message="查询数据")
	public String getListStates() throws Exception {
		String type = Struts2Utils.getParameter("type");
		try{
			if("Y".equals(type)){
				page = gsmInnerCheckReportManager.listState(page, type);
			}else{
				page = gsmInnerCheckReportManager.listState(page, type);
			}
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			logger.error("查询失败!",e);
		}
		return null;
	}
	public GsmInnerCheckReport getGsmInnerCheckReport() {
		return gsmInnerCheckReport;
	}

	public void setGsmInnerCheckReport(
			GsmInnerCheckReport gsmInnerCheckReport) {
		this.gsmInnerCheckReport = gsmInnerCheckReport;
	}
	public String getCurrentActivityName() {
		return currentActivityName;
	}

	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
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
	
}

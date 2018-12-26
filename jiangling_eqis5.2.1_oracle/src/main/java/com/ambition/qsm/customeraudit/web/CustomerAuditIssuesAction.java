package com.ambition.qsm.customeraudit.web;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.qsm.customeraudit.service.CustomerAuditIssuesManager;
import com.ambition.qsm.entity.CustomerAuditIssues;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * 类名:客户审核问题点履历action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Namespace("/qsm/customer-audit/issues")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/qsm/customer-audit/issues", type = "redirectAction") })
public class CustomerAuditIssuesAction extends AmbWorkflowActionBase<CustomerAuditIssues>{

	private static final long serialVersionUID = 1L;
	private CustomerAuditIssues customerAuditIssues;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private Logger log=Logger.getLogger(this.getClass());
	private File myFile;
	private String currentActivityName;//当前流程环节名称
	@Autowired
	private CustomerAuditIssuesManager customerAuditIssuesManager;
	@Override
	protected AmbWorkflowManagerBase<CustomerAuditIssues> getAmbWorkflowBaseManager() {
		// TODO Auto-generated method stub
		return customerAuditIssuesManager;
	}
	public CustomerAuditIssues getCustomerAuditIssues() {
		return customerAuditIssues;
	}
	public void setCustomerAuditIssues(CustomerAuditIssues customerAuditIssues) {
		this.customerAuditIssues = customerAuditIssues;
	}
	public LogUtilDao getLogUtilDao() {
		return logUtilDao;
	}
	public void setLogUtilDao(LogUtilDao logUtilDao) {
		this.logUtilDao = logUtilDao;
	}
	public Logger getLog() {
		return log;
	}
	public void setLog(Logger log) {
		this.log = log;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	public CustomerAuditIssuesManager getCustomerAuditIssuesManager() {
		return customerAuditIssuesManager;
	}
	public void setCustomerAuditIssuesManager(
			CustomerAuditIssuesManager customerAuditIssuesManager) {
		this.customerAuditIssuesManager = customerAuditIssuesManager;
	}
	public String getCurrentActivityName() {
		return currentActivityName;
	}
	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}
	public void initForm(){
		if(getId() == null&&getReport().getId()==null){
			getReport().setFormNo(formCodeGenerated.generateCustomerAuditNo());
			getReport().setTaskCreatedTime(new Date());
			getReport().setTransactorName(ContextUtils.getUserName());
			getReport().setTransactor(ContextUtils.getLoginName());
			getReport().setConsignableDate(new Date());
		}else{
			if(getReport().getWorkflowInfo()!=null){
				currentActivityName=getReport().getWorkflowInfo().getCurrentActivityName();
			}
		}
		ActionContext.getContext().put("enterpriseGroups",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_enterprise_group"));
		ActionContext.getContext().put("auditTypes",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_audit_type"));
		ActionContext.getContext().put("issuesTypes",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_issues_type"));
		ActionContext.getContext().put("degrees",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_degree"));
		ActionContext.getContext().put("closeStates",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_close_state"));
	}
	@Action("export")
	@LogInfo(optType="导出", message="客户审核问题点履历")
	public String export() throws Exception {
		try {
			Page<CustomerAuditIssues> page = new Page<CustomerAuditIssues>(65535);
			page = customerAuditIssuesManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"客户审核问题点履历台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出客户审核问题点履历失败",e);
		}
		return null;
	}
	@Action("import")
	public String imports() throws Exception {
		return SUCCESS;
	}
	
	@Action("import-datas")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(customerAuditIssuesManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	/**
	  * 方法名: 下载检设备参数模板
	  * <p>功能说明：下载检验标准的模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download-template")
	@LogInfo(optType="下载",message="下载客户审核问题点履历模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/qsm-customer-issues.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "客户审核问题点履历模板.xls";
			byte byname[] = fileName.getBytes("gbk");
			fileName = new String(byname, "8859_1");
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append(fileName).append("\"")
					.toString());
			book.write(response.getOutputStream());
		}catch (Exception e) {
			log.error("导出失败!",e);
		} finally{
			if(inputStream != null){
				inputStream.close();
			}
		}
		return null;
	}	
	/**
	 * 创建返回消息
	 * 
	 * @param error
	 * @param message
	 * @return
	 */
	public void createErrorMessage(String message) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("error", true);
		map.put("message", message);
		renderText(JSONObject.fromObject(map).toString());
	}
}

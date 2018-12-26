package com.ambition.qsm.inneraudit.web;

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
import com.ambition.qsm.entity.Problems;
import com.ambition.qsm.inneraudit.service.ProblemsManager;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/qsm/inner-audit/problems")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/qsm/inner-audit/problems", type = "redirectAction") })
public class ProblemsAction extends AmbWorkflowActionBase<Problems>{

		/**
		  *ProblemsAction.java2017年5月11日
		 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private  ProblemsManager problemsManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private Logger log=Logger.getLogger(this.getClass());
	private File myFile;
	
	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	@Override
	protected AmbWorkflowManagerBase<Problems> getAmbWorkflowBaseManager() {
		// TODO Auto-generated method stub
		return problemsManager;
	}
	
	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public void initForm(){
		if(getId() == null&&getReport().getId()==null){
			getReport().setFormNo(formCodeGenerated.generateProblemsNo());
			getReport().setTaskCreatedTime(new Date());
			getReport().setTransactorName(ContextUtils.getUserName());
			getReport().setTransactor(ContextUtils.getLoginName());
			getReport().setConsignableDate(new Date());
			getReport().setConsignor(ContextUtils.getUserName());
			getReport().setDepartment(ContextUtils.getSubCompanyName());
		}
		ActionContext.getContext().put("auditTypes",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_audit_type"));
		ActionContext.getContext().put("issuesTypes",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_issues_type"));
		ActionContext.getContext().put("degrees",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_degree"));
		ActionContext.getContext().put("closeStates",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_close_state"));
	}
	@Action("import")
	public String imports() throws Exception {
		return SUCCESS;
	}
	
	@Action("import-datas")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(problemsManager.importDatas(myFile));
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
	@LogInfo(optType="下载",message="下载内审问题模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/qsm-inner-audit-problems.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "内审问题模板.xls";
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

package com.ambition.iqc.taskmonitor.web;

import java.util.Date;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.iqc.entity.InspectionTaskEmail;
import com.ambition.iqc.taskmonitor.service.InspectionTaskEmailManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Namespace("/iqc/taskmonitor")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "iqc/taskmonitor", type = "redirectAction") })
public class InspectionTaskEmailAction extends CrudActionSupport<InspectionTaskEmail>{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String deleteIds;
	private InspectionTaskEmail inspectionTaskEmail;
	@Autowired
	private LogUtilDao logUtilDao;
	private Page<InspectionTaskEmail> page;
	@Autowired
	private InspectionTaskEmailManager inspectionTaskEmailManager;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDeleteIds() {
		return deleteIds;
	}
	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}
	
	public Page<InspectionTaskEmail> getPage() {
		return page;
	}
	public void setPage(Page<InspectionTaskEmail> page) {
		this.page = page;
	}
	@Override
	public InspectionTaskEmail getModel() {
		return inspectionTaskEmail;
	}
	@Action("task-delete")
	@Override
	public String delete() throws Exception {
		inspectionTaskEmailManager.deleteInspectionTaskEmail(deleteIds);
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}
	@Action("task-list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	@Action("task-list-datas")
	public String taskListDatas(){
		page=inspectionTaskEmailManager.getPage(page);
		renderText(PageUtils.pageToJson(page,"IQC_INSPECTION_TASK_EMAIL"));
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			inspectionTaskEmail =new InspectionTaskEmail();
			inspectionTaskEmail.setCreatedTime(new Date());
			inspectionTaskEmail.setCreatorName(ContextUtils.getUserName());
			inspectionTaskEmail.setDepartmentId(ContextUtils.getDepartmentId());
			inspectionTaskEmail.setBusinessUnitName(ContextUtils.getSubCompanyName());
			inspectionTaskEmail.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			inspectionTaskEmail=inspectionTaskEmailManager.getInspectionTaskEmail(id);
		}
		
	}
	@Action("task-save")
	@Override
	public String save() throws Exception {
		inspectionTaskEmailManager.saveInspectionTaskEmail(inspectionTaskEmail);
		this.renderText(JsonParser.getRowValue(inspectionTaskEmail));
		return null;
	}
	
	@Action("task-export")
	public String export() throws Exception {
		try {
			Page<InspectionTaskEmail> page = new Page<InspectionTaskEmail>(100000);
			page=inspectionTaskEmailManager.getPage(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"IQC_INSPECTION_TASK_EMAIL"),"检验任务邮件通知维护"));
		} catch (Exception e) {
			logUtilDao.debugLog("检验任务邮件通知维护失败!",e.getMessage());
		}
		return null;
	}

}

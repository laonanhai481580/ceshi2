package com.ambition.iqc.taskmonitor.web;

import java.util.Date;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.iqc.entity.InspectionFromOverdueEmail;
import com.ambition.iqc.entity.InspectionTaskEmail;
import com.ambition.iqc.taskmonitor.service.InspectionFromOverdueEmailManager;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

@Namespace("/iqc/taskmonitor")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "iqc/taskmonitor", type = "redirectAction") })
public class InspectionFromOverdueEmailAction extends CrudActionSupport<InspectionFromOverdueEmail>{
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private InspectionFromOverdueEmail inspectionFromOverdueEmail;
	private Page<InspectionFromOverdueEmail> page;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private InspectionFromOverdueEmailManager overdueEmailManager;
	
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

	public Page<InspectionFromOverdueEmail> getPage() {
		return page;
	}

	public void setPage(Page<InspectionFromOverdueEmail> page) {
		this.page = page;
	}

	@Override
	public InspectionFromOverdueEmail getModel() {
		return inspectionFromOverdueEmail;
	}
	@Action("over-delete")
	@Override
	public String delete() throws Exception {
		overdueEmailManager.deleteInspectionFromOverdueEmail(deleteIds);
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}
	@Action("overdue-list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Action("overdue-list-datas")
	public String taskListDatas(){
		page=overdueEmailManager.getInspectionFromOverdueEmailPage(page);
		renderText(PageUtils.pageToJson(page,"IQC_INSPECTION_OVERDUE_EMAIL"));
		return null;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			inspectionFromOverdueEmail =new InspectionFromOverdueEmail();
			inspectionFromOverdueEmail.setCreatedTime(new Date());
			inspectionFromOverdueEmail.setCreatorName(ContextUtils.getUserName());
			inspectionFromOverdueEmail.setDepartmentId(ContextUtils.getDepartmentId());
			inspectionFromOverdueEmail.setBusinessUnitName(ContextUtils.getSubCompanyName());
			inspectionFromOverdueEmail.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			inspectionFromOverdueEmail=overdueEmailManager.getInspectionFromOverdueEmail(id);
		}
	}
	@Action("overdue-save")
	@Override
	public String save() throws Exception {
		overdueEmailManager.saveInspectionFromOverdueEmail(inspectionFromOverdueEmail);
		this.renderText(JsonParser.getRowValue(inspectionFromOverdueEmail));
		return null;
	}
	
	@Action("overdue-export")
	public String export() throws Exception {
		try {
			Page<InspectionFromOverdueEmail> page = new Page<InspectionFromOverdueEmail>(100000);
			page=overdueEmailManager.getInspectionFromOverdueEmailPage(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"IQC_INSPECTION_TASK_EMAIL"),"检验任务邮件通知维护"));
		} catch (Exception e) {
			logUtilDao.debugLog("检验任务邮件通知维护失败!",e.getMessage());
		}
		return null;
	}

}

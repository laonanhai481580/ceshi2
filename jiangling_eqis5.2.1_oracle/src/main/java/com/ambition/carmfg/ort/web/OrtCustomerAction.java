package com.ambition.carmfg.ort.web;

import java.util.Date;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.entity.OrtCustomer;
import com.ambition.carmfg.ort.service.OrtCustomerManager;
import com.ambition.product.BaseAction;
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

@Namespace("/carmfg/ort/ort-base")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "si/base-info/defection-code", type = "redirectAction") })
public class OrtCustomerAction extends BaseAction<OrtCustomer>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private String operate;
	private String businessUnit;//所属事业部
	private String processSection;//制程区段
	private OrtCustomer ortCustomer;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private OrtCustomerManager ortCustomerManager;
	private Page<OrtCustomer> page;
	
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
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public OrtCustomer getOrtCustomer() {
		return ortCustomer;
	}
	public void setOrtCustomer(OrtCustomer ortCustomer) {
		this.ortCustomer = ortCustomer;
	}
	public Page<OrtCustomer> getPage() {
		return page;
	}
	public void setPage(Page<OrtCustomer> page) {
		this.page = page;
	}

	public String getProcessSection() {
		return processSection;
	}
	public void setProcessSection(String processSection) {
		this.processSection = processSection;
	}
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	@Override
	public OrtCustomer getModel() {
		return ortCustomer;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			ortCustomer = new OrtCustomer();
			ortCustomer.setCompanyId(ContextUtils.getCompanyId());
			ortCustomer.setCreatedTime(new Date());
			ortCustomer.setCreator(ContextUtils.getUserName());
			ortCustomer.setLastModifiedTime(new Date());
			ortCustomer.setLastModifier(ContextUtils.getUserName());
			ortCustomer.setBusinessUnitName(ContextUtils.getSubCompanyName());
			ortCustomer.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			ortCustomer = ortCustomerManager.getOrtCustomer(id);
		}
	}
	@Action("customer-delete")
	@LogInfo(optType="删除",message="客户信息")
	@Override
	public String delete() throws Exception {
		try{
			ortCustomerManager.deleteOrtCustomer(deleteIds);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	@Action("customer-input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("customer-select")
	public String customerSelect() throws Exception {
		return SUCCESS;
	}
	@Action("customer-list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	@Action("customer-save")
	@LogInfo(optType="保存",message="客户信息")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			ortCustomer.setLastModifiedTime(new Date());
			ortCustomer.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", ortCustomer.toString());
		}else{
			logUtilDao.debugLog("保存", ortCustomer.toString());
		}
		try{
			ortCustomerManager.saveOrtCustomer(ortCustomer);
			this.renderText(JsonParser.object2Json(ortCustomer));
		}catch(Exception e){
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}	
	@Action("customer-list-datas")
	public String getListDatas() throws Exception {
		page = ortCustomerManager.list(page);
		this.renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "制造质量管理：基础设置-客户信息维护");
		return null;
	}
	@Action("exportCode")
	@LogInfo(optType="导出",message="客户信息")
	public String export() throws Exception {
		Page<OrtCustomer> page = new Page<OrtCustomer>(100000);
		page = ortCustomerManager.list(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_ORT_CUSTOMER"),"ortCustomer"));
		logUtilDao.debugLog("导出", "制造质量管理：基础设置-客户信息维护");
		return null;
	}
}

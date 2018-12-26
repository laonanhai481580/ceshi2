package com.ambition.epm.baseinfo.web;

import java.util.Date;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.epm.baseinfo.service.EpmOrtIndicatorManager;
import com.ambition.epm.entity.EpmOrtIndicator;
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

@Namespace("/epm/base-info/ort-item")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/epm/base-info/ort-item", type = "redirectAction") })
public class EpmOrtIndicatorAction extends BaseAction<EpmOrtIndicator>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private String operate;
	private String businessUnit;//所属事业部
	private String processSection;//制程区段
	private EpmOrtIndicator ortIndicator;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private EpmOrtIndicatorManager ortIndicatorManager;
	private Page<EpmOrtIndicator> page;
	
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
	public EpmOrtIndicator getOrtIndicator() {
		return ortIndicator;
	}
	public void setOrtIndicator(EpmOrtIndicator ortIndicator) {
		this.ortIndicator = ortIndicator;
	}
	public Page<EpmOrtIndicator> getPage() {
		return page;
	}
	public void setPage(Page<EpmOrtIndicator> page) {
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
	public EpmOrtIndicator getModel() {
		return ortIndicator;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			ortIndicator = new EpmOrtIndicator();
			ortIndicator.setCompanyId(ContextUtils.getCompanyId());
			ortIndicator.setCreatedTime(new Date());
			ortIndicator.setCreator(ContextUtils.getUserName());
			ortIndicator.setLastModifiedTime(new Date());
			ortIndicator.setLastModifier(ContextUtils.getUserName());
			ortIndicator.setBusinessUnitName(ContextUtils.getSubCompanyName());
			ortIndicator.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			ortIndicator = ortIndicatorManager.getOrtIndicator(id);
		}
	}
	@Action("ort-indicator-delete")
	@LogInfo(optType="删除",message="ORT测试标准")
	@Override
	public String delete() throws Exception {
		try{
			ortIndicatorManager.deleteOrtIndicator(deleteIds);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	@Action("ort-indicator-input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("ort-indicator-list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	@Action("ort-indicator-save")
	@LogInfo(optType="保存",message="ORT测试标准")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			ortIndicator.setLastModifiedTime(new Date());
			ortIndicator.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", ortIndicator.toString());
		}else{
			logUtilDao.debugLog("保存", ortIndicator.toString());
		}
		try{
			ortIndicatorManager.saveOrtIndicator(ortIndicator);
			this.renderText(JsonParser.getRowValue(ortIndicator));
		}catch(Exception e){
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}	
	@Action("ort-indicator-list-datas")
	public String getListDatas() throws Exception {
		page = ortIndicatorManager.list(page);
		this.renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", " ORT测试标准维护");
		return null;
	}
	@Action("export2")
	@LogInfo(optType="导出",message=" ORT测试标准")
	public String export() throws Exception {
		Page<EpmOrtIndicator> page = new Page<EpmOrtIndicator>(100000);
		page = ortIndicatorManager.list(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"EPM_ORT_INDICATOR"),"ortIndicator"));
		logUtilDao.debugLog("导出", " ORT测试标准维护");
		return null;
	}
}

package com.ambition.si.defectioncode.web;

import java.util.Date;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.product.BaseAction;
import com.ambition.si.defectioncode.service.SiDefectionTypeManager;
import com.ambition.si.entity.SiDefectionType;
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

@Namespace("/si/base-info/defection-code")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "si/base-info/defection-code", type = "redirectAction") })
public class SiDefectionTypeAction extends BaseAction<SiDefectionType>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private String operate;
	private String businessUnit;//所属事业部
	private String processSection;//制程区段
	private SiDefectionType defectionType;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private SiDefectionTypeManager defectionTypeManager;
	private Page<SiDefectionType> page;
	
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
	public SiDefectionType getDefectionType() {
		return defectionType;
	}
	public void setDefectionType(SiDefectionType defectionType) {
		this.defectionType = defectionType;
	}
	public Page<SiDefectionType> getPage() {
		return page;
	}
	public void setPage(Page<SiDefectionType> page) {
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
	public SiDefectionType getModel() {
		return defectionType;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			defectionType = new SiDefectionType();
			defectionType.setCompanyId(ContextUtils.getCompanyId());
			defectionType.setCreatedTime(new Date());
			defectionType.setCreator(ContextUtils.getUserName());
			defectionType.setLastModifiedTime(new Date());
			defectionType.setLastModifier(ContextUtils.getUserName());
			defectionType.setBusinessUnitName(ContextUtils.getSubCompanyName());
			defectionType.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			defectionType = defectionTypeManager.getDefectionType(id);
		}
	}
	@Action("defection-type-delete")
	@LogInfo(optType="删除",message="不良类别")
	@Override
	public String delete() throws Exception {
		try{
			defectionTypeManager.deleteDefectionType(deleteIds);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	@Action("defection-type-input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("defection-type-list")
	@Override
	public String list() throws Exception {
		renderMenu();
		//制程区段
/*		processSection = Struts2Utils.getParameter("processSection");
		List<Option> processSections = ApiFactory.getSettingService().getOptionsByGroupCode("processSections");
		if(StringUtils.isEmpty(processSection)){
			if(processSections.size()>0){
				processSection = processSections.get(0).getValue();
			}
		}
		ActionContext.getContext().put("processSections",processSections);*/
		return SUCCESS;
	}
	@Action("defection-type-save")
	@LogInfo(optType="保存",message="不良类别")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			defectionType.setLastModifiedTime(new Date());
			defectionType.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", defectionType.toString());
		}else{
			logUtilDao.debugLog("保存", defectionType.toString());
		}
		try{
			//defectionType.setProcessSection(processSection);
			defectionTypeManager.saveDefectionType(defectionType);
			this.renderText(JsonParser.object2Json(defectionType));
		}catch(Exception e){
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}	
	@Action("defection-type-list-datas")
	public String getListDatas() throws Exception {
/*		processSection = Struts2Utils.getParameter("processSection");
		List<Option> processSections = ApiFactory.getSettingService().getOptionsByGroupCode("processSections");
		if(StringUtils.isEmpty(processSection)){
			if(processSections.size()>0){
				processSection = processSections.get(0).getValue();
			}
		}*/
		page = defectionTypeManager.list(page);
		this.renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "供应商检验：基础设置-不良类别维护");
		return null;
	}
	@Action("exportCode")
	@LogInfo(optType="导出",message="不良类别")
	public String export() throws Exception {
		Page<SiDefectionType> page = new Page<SiDefectionType>(100000);
		page = defectionTypeManager.list(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"SI_DEFECTION_TYPE"),"defectionType"));
		logUtilDao.debugLog("导出", "供应商检验：基础设置-不良类别维护");
		return null;
	}
}

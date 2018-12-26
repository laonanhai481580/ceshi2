package com.ambition.carmfg.defectioncode.web;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.defectioncode.service.DefectionTypeManager;
import com.ambition.carmfg.entity.DefectionType;
import com.ambition.product.BaseAction;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/carmfg/base-info/defection-code")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "carmfg/base-info/defection-code", type = "redirectAction") })
public class DefectionTypeAction extends BaseAction<DefectionType>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private String operate;
	private String businessUnit;//所属事业部
	private DefectionType defectionType;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private DefectionTypeManager defectionTypeManager;
	private Page<DefectionType> page;
	
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
	public DefectionType getDefectionType() {
		return defectionType;
	}
	public void setDefectionType(DefectionType defectionType) {
		this.defectionType = defectionType;
	}
	public Page<DefectionType> getPage() {
		return page;
	}
	public void setPage(Page<DefectionType> page) {
		this.page = page;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	@Override
	public DefectionType getModel() {
		return defectionType;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			defectionType = new DefectionType();
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
	@LogInfo(optType="删除",message="不良类别维护")
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
		//所属事业部
		businessUnit = Struts2Utils.getParameter("businessUnit");
		List<Option> businessUnits = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits");
		if(StringUtils.isEmpty(businessUnit)){
			if(businessUnits.size()>0){
				businessUnit = businessUnits.get(0).getValue();
			}
		}
		ActionContext.getContext().put("businessUnits",businessUnits);
		return SUCCESS;
	}
	@Action("defection-type-save")
	@LogInfo(optType="保存",message="不良类别维护")
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
			defectionType.setBusinessUnitName(businessUnit);
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
		businessUnit = Struts2Utils.getParameter("businessUnit");
		List<Option> businessUnits = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits");
		if(StringUtils.isEmpty(businessUnit)){
			if(businessUnits.size()>0){
				businessUnit = businessUnits.get(0).getValue();
			}
		}
		page = defectionTypeManager.list(page,businessUnit);
		this.renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "制造质量管理：基础设置-不良类别维护");
		return null;
	}
	@Action("exportCode")
	@LogInfo(optType="导出",message="不良类别维护")
	public String export() throws Exception {
		Page<DefectionType> page = new Page<DefectionType>(100000);
		page = defectionTypeManager.list(page,businessUnit);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_DEFECTION_TYPE"),"defectionType"));
		logUtilDao.debugLog("导出", "制造质量管理：基础设置-不良类别维护");
		return null;
	}
}

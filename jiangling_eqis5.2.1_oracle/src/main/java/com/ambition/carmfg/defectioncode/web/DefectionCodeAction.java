package com.ambition.carmfg.defectioncode.web;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.defectioncode.service.DefectionCodeManager;
import com.ambition.carmfg.defectioncode.service.DefectionTypeManager;
import com.ambition.carmfg.entity.DefectionCode;
import com.ambition.carmfg.entity.DefectionType;
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
import com.norteksoft.product.web.struts2.Struts2Utils;

@Namespace("/carmfg/base-info/defection-code")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "carmfg/base-info/defection-code", type = "redirectAction") })
public class DefectionCodeAction extends BaseAction<DefectionCode>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private DefectionCode defectionCode;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private DefectionCodeManager defectionCodeManager;
	private Page<DefectionCode> page;
	private List<DefectionCode> list;
	private File myFile;
 	//不良类别Id
	private Long defectionTypeId;
	@Autowired
	private DefectionTypeManager defectionTypeManager;
	private DefectionType defectionType;
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

	public DefectionCode getDefectionCode() {
		return defectionCode;
	}

	public void setDefectionCode(DefectionCode defectionCode) {
		this.defectionCode = defectionCode;
	}

	public Page<DefectionCode> getPage() {
		return page;
	}

	public void setPage(Page<DefectionCode> page) {
		this.page = page;
	}

	public List<DefectionCode> getList() {
		return list;
	}

	public void setList(List<DefectionCode> list) {
		this.list = list;
	}

	public Long getDefectionTypeId() {
		return defectionTypeId;
	}

	public void setDefectionTypeId(Long defectionTypeId) {
		this.defectionTypeId = defectionTypeId;
	}

	public DefectionType getDefectionType() {
		return defectionType;
	}

	public void setDefectionType(DefectionType defectionType) {
		this.defectionType = defectionType;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	@Override
	public DefectionCode getModel() {
		return defectionCode;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			defectionCode = new DefectionCode();
			if(defectionTypeId != null && defectionTypeId != 0){
				defectionType = defectionTypeManager.getDefectionType(defectionTypeId);
			}else{
				defectionType = null;
			}
			defectionCode.setCompanyId(ContextUtils.getCompanyId());
			defectionCode.setCreatedTime(new Date());
			defectionCode.setCreator(ContextUtils.getUserName());
			defectionCode.setDefectionType(defectionType);
			defectionCode.setLastModifiedTime(new Date());
			defectionCode.setLastModifier(ContextUtils.getUserName());
			defectionCode.setBusinessUnitName(ContextUtils.getSubCompanyName());
			defectionCode.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			defectionCode = defectionCodeManager.getDefectionCode(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("save")
	@LogInfo(optType="保存",message="不良代码维护")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			defectionCode.setLastModifiedTime(new Date());
			defectionCode.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", defectionCode.toString());
		}else{
			logUtilDao.debugLog("保存", defectionCode.toString());
		}
		try {
			defectionCodeManager.saveDefectionCode(defectionCode);
			renderText(JsonParser.object2Json(defectionCode));
		} catch (Exception e) {
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="不良代码维护")
	@Override
	public String delete() throws Exception {
		defectionCodeManager.deleteDefectionCode(deleteIds);
		return null;
	}

	@Action("delete-subs")
	@LogInfo(optType="删除级联",message="不良代码维护")
	public String deleteSubs() throws Exception {
		if(defectionTypeId != null && defectionTypeId != 0){
			defectionType = defectionTypeManager.getDefectionType(defectionTypeId);
		}else{
			defectionType = null;
		}
		list = defectionCodeManager.listAll(defectionType);
		for(DefectionCode defectionCode: list){
			defectionCodeManager.deleteDefectionCode(defectionCode);
		}
		return null;
	}
	
	@Action("search-subs")
	public String searchSubs() throws Exception {
		if(defectionTypeId != null && defectionTypeId != 0){
			defectionType = defectionTypeManager.getDefectionType(defectionTypeId);
		}else{
			defectionType = null;
		}
		list = defectionCodeManager.listAll(defectionType);
		if(list.size() >= 1){
			renderText("have");
		}else{
			renderText("no");
		}
		return null;
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		renderMenu();
		if(defectionTypeId != null && defectionTypeId != 0){
			defectionType = defectionTypeManager.getDefectionType(defectionTypeId);
		}else{
			defectionType = null;
		}
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception{
		String myId = Struts2Utils.getParameter("defectionTypeId");
		if(myId != null && !myId.equals("")){
			defectionTypeId = Long.valueOf(myId);
			defectionType = defectionTypeManager.getDefectionType(defectionTypeId);
		}
		String code = null;
		try {
			code = Struts2Utils.getParameter("defectionCode");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(code != null && !code.equals("")){
			page = defectionCodeManager.list(page, code);
		}else{
			page = defectionCodeManager.list(page, defectionType);
		}		
		renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "制造质量管理：基础设置-不良代码维护");
		return null;
	}
	@Action("exportCode2")
	@LogInfo(optType="导出",message="不良代码维护")
	public String export() throws Exception {
		Page<DefectionCode> page = new Page<DefectionCode>(100000);
		String myId = Struts2Utils.getParameter("defectionTypeId");
		if(myId != null && !myId.equals("")){
			defectionTypeId = Long.valueOf(myId);
			defectionType = defectionTypeManager.getDefectionType(defectionTypeId);
		}
		page = defectionCodeManager.list(page, defectionType);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_DEFECTION_CODE"),"defectionCode"));
		logUtilDao.debugLog("导出", "制造质量管理：基础设置-不良代码维护");
		return null;
	}
	@Action("imports")
	public String imports() throws Exception {
		return "imports";
	}
	
	@Action("import-excel-datas")
	@LogInfo(optType="导入",message="不良代码数据")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(defectionCodeManager.importFile(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	
}

package com.ambition.aftersales.baseinfo.web;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.aftersales.baseinfo.service.DefectionClassManager;
import com.ambition.aftersales.baseinfo.service.DefectionItemManager;
import com.ambition.aftersales.entity.DefectionClass;
import com.ambition.aftersales.entity.DefectionItem;
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

@Namespace("/aftersales/base-info/defection-item")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/aftersales/base-info/defection-item", type = "redirectAction") })
public class DefectionItemAction extends BaseAction<DefectionItem>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private DefectionItem defectionItem;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private DefectionItemManager defectionItemManager;
	private Page<DefectionItem> page;
	private List<DefectionItem> list;
	private File myFile;
 	//不良类别Id
	private Long defectionClassId;
	@Autowired
	private DefectionClassManager defectionClassManager;
	private DefectionClass defectionClass;
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

	public DefectionItem getDefectionItem() {
		return defectionItem;
	}

	public void setDefectionItem(DefectionItem defectionItem) {
		this.defectionItem = defectionItem;
	}

	public Page<DefectionItem> getPage() {
		return page;
	}

	public void setPage(Page<DefectionItem> page) {
		this.page = page;
	}

	public List<DefectionItem> getList() {
		return list;
	}

	public void setList(List<DefectionItem> list) {
		this.list = list;
	}

	public Long getDefectionClassId() {
		return defectionClassId;
	}

	public void setDefectionClassId(Long defectionClassId) {
		this.defectionClassId = defectionClassId;
	}

	public DefectionClass getDefectionClass() {
		return defectionClass;
	}

	public void setDefectionClass(DefectionClass defectionClass) {
		this.defectionClass = defectionClass;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	@Override
	public DefectionItem getModel() {
		return defectionItem;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			defectionItem = new DefectionItem();
			if(defectionClassId != null && defectionClassId != 0){
				defectionClass = defectionClassManager.getDefectionClass(defectionClassId);
			}else{
				defectionClass = null;
			}
			defectionItem.setCompanyId(ContextUtils.getCompanyId());
			defectionItem.setCreatedTime(new Date());
			defectionItem.setCreator(ContextUtils.getUserName());
			defectionItem.setDefectionClass(defectionClass);
			defectionItem.setLastModifiedTime(new Date());
			defectionItem.setLastModifier(ContextUtils.getUserName());
			defectionItem.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			defectionItem = defectionItemManager.getDefectionItem(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("save")
	@LogInfo(optType="保存",message="保存数据")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			defectionItem.setLastModifiedTime(new Date());
			defectionItem.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", defectionItem.toString());
		}else{
			logUtilDao.debugLog("保存", defectionItem.toString());
		}
		try {
			defectionItemManager.saveDefectionItem(defectionItem);
			renderText(JsonParser.object2Json(defectionItem));
		} catch (Exception e) {
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="不良项目维护")
	@Override
	public String delete() throws Exception {
		defectionItemManager.deleteDefectionItem(deleteIds);
		return null;
	}

	@Action("delete-subs")
	@LogInfo(optType="删除",message="不良项目维护")
	public String deleteSubs() throws Exception {
		if(defectionClassId != null && defectionClassId != 0){
			defectionClass = defectionClassManager.getDefectionClass(defectionClassId);
		}else{
			defectionClass = null;
		}
		list = defectionItemManager.listAll(defectionClass);
		for(DefectionItem defectionItem: list){
			defectionItemManager.deleteDefectionItem(defectionItem);
		}
		return null;
	}
	
	@Action("search-subs")
	public String searchSubs() throws Exception {
		if(defectionClassId != null && defectionClassId != 0){
			defectionClass = defectionClassManager.getDefectionClass(defectionClassId);
		}else{
			defectionClass = null;
		}
		list = defectionItemManager.listAll(defectionClass);
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
		if(defectionClassId != null && defectionClassId != 0){
			defectionClass = defectionClassManager.getDefectionClass(defectionClassId);
		}else{
			defectionClass = null;
		}
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception{
		String myId = Struts2Utils.getParameter("defectionClassId");
		if(myId != null && !myId.equals("")){
			defectionClassId = Long.valueOf(myId);
			defectionClass = defectionClassManager.getDefectionClass(defectionClassId);
		}
		String code = null;
		try {
			code = Struts2Utils.getParameter("defectionItem");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(code != null && !code.equals("")){
			page = defectionItemManager.list(page, code);
		}else{
			page = defectionItemManager.list(page, defectionClass);
		}		
		renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "售后质量管理：基础设置-不良项目维护");
		return null;
	}
	@Action("exportCode2")
	@LogInfo(optType="导出",message="不良项目维护")
	public String export() throws Exception {
		Page<DefectionItem> page = new Page<DefectionItem>(100000);
		String myId = Struts2Utils.getParameter("defectionClassId");
		if(myId != null && !myId.equals("")){
			defectionClassId = Long.valueOf(myId);
			defectionClass = defectionClassManager.getDefectionClass(defectionClassId);
		}
		page = defectionItemManager.list(page, defectionClass);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"AFS_DEFECTION_ITEM"),"defectionItem"));
		logUtilDao.debugLog("导出", "售后质量管理：基础设置-不良项目维护");
		return null;
	}
	@Action("imports")
	public String imports() throws Exception {
		return "imports";
	}
	
	@Action("import-excel-datas")
	@LogInfo(optType="导入数据",message="不良项目维护")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(defectionItemManager.importFile(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	
}

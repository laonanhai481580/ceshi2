package com.ambition.si.defectioncode.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.product.BaseAction;
import com.ambition.si.defectioncode.service.SiDefectionCodeManager;
import com.ambition.si.defectioncode.service.SiDefectionTypeManager;
import com.ambition.si.entity.SiDefectionCode;
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
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/si/base-info/defection-code")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "si/base-info/defection-code", type = "redirectAction") })
public class SiDefectionCodeAction extends BaseAction<SiDefectionCode>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private SiDefectionCode defectionCode;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private SiDefectionCodeManager defectionCodeManager;
	private Page<SiDefectionCode> page;
	private List<SiDefectionCode> list;
	private File myFile;
 	//不良类别Id
	private Long defectionTypeId;
	@Autowired
	private SiDefectionTypeManager defectionTypeManager;
	private SiDefectionType defectionType;
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

	public SiDefectionCode getDefectionCode() {
		return defectionCode;
	}

	public void setDefectionCode(SiDefectionCode defectionCode) {
		this.defectionCode = defectionCode;
	}

	public Page<SiDefectionCode> getPage() {
		return page;
	}

	public void setPage(Page<SiDefectionCode> page) {
		this.page = page;
	}

	public List<SiDefectionCode> getList() {
		return list;
	}

	public void setList(List<SiDefectionCode> list) {
		this.list = list;
	}

	public Long getDefectionTypeId() {
		return defectionTypeId;
	}

	public void setDefectionTypeId(Long defectionTypeId) {
		this.defectionTypeId = defectionTypeId;
	}

	public SiDefectionType getDefectionType() {
		return defectionType;
	}

	public void setDefectionType(SiDefectionType defectionType) {
		this.defectionType = defectionType;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	@Override
	public SiDefectionCode getModel() {
		return defectionCode;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			defectionCode = new SiDefectionCode();
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
	@LogInfo(optType="保存",message="不良项目")
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
	@LogInfo(optType="删除",message="不良项目")
	@Override
	public String delete() throws Exception {
		defectionCodeManager.deleteDefectionCode(deleteIds);
		return null;
	}

	@Action("delete-subs")
	@LogInfo(optType="删除子级",message="不良项目")
	public String deleteSubs() throws Exception {
		if(defectionTypeId != null && defectionTypeId != 0){
			defectionType = defectionTypeManager.getDefectionType(defectionTypeId);
		}else{
			defectionType = null;
		}
		list = defectionCodeManager.listAll(defectionType);
		for(SiDefectionCode defectionCode: list){
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
	@LogInfo(optType="导出",message="不良项目")
	public String export() throws Exception {
		Page<SiDefectionCode> page = new Page<SiDefectionCode>(100000);
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
		defectionTypeId = Long.valueOf(Struts2Utils.getParameter("defectionTypeId"));
		return SUCCESS;
	}
	
	@Action("import-excel-datas")
	@LogInfo(optType="导入",message="不良项目")
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
	@Action("defection-code-multi-select")
	public String codeBomMultiSelect() throws Exception {
		List<SiDefectionType> defectionTypes = defectionTypeManager.listAll();
		List<Map<String,Object>> defectionTypeMaps = new ArrayList<Map<String,Object>>();
		for(SiDefectionType defectionType : defectionTypes){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("data",defectionType.getDefectionTypeName());
			map.put("isLeaf",true);
			Map<String,Object> attrMap = new HashMap<String, Object>();
			attrMap.put("id",defectionType.getId());
			attrMap.put("defectionTypeName",defectionType.getDefectionTypeName());
			attrMap.put("defectionTypeNo",defectionType.getDefectionTypeNo());
			map.put("attr",attrMap);
			defectionTypeMaps.add(map);
		}
		ActionContext.getContext().put("defectionTypeMaps",JSONArray.fromObject(defectionTypeMaps).toString());	
		return SUCCESS;
	}
	
	@Action("code-list-datas")
	public String getCodeListByParent() throws Exception {		
		if(defectionTypeId != null && defectionTypeId != 0){
			defectionType = defectionTypeManager.getDefectionType(defectionTypeId);
		}else{
			defectionType = null;
		}
		String searchParams = Struts2Utils.getParameter("searchParams");
		JSONObject params = null;
		if(StringUtils.isNotEmpty(searchParams)){
			params = JSONObject.fromObject(searchParams);
		}
		if(params!=null){
			page = defectionCodeManager.getCodeByParams(page, params,defectionType);
		}else{
			page = defectionCodeManager.list(page, defectionType);
		}
		try {
			Page<Object> resultPage = new Page<Object>();
			resultPage.setOrder(page.getOrder());
			resultPage.setOrderBy(page.getOrderBy());
			resultPage.setPageNo(page.getPageNo());
			resultPage.setPageSize(page.getPageSize());
			resultPage.setTotalCount(page.getTotalCount());
			List<Object> list = new ArrayList<Object>();
			for(SiDefectionCode defectionCode : page.getResult()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id",defectionCode.getId());
				map.put("defectionCodeNo",defectionCode.getDefectionCodeNo());
				map.put("defectionCodeName",defectionCode.getDefectionCodeName());
				map.put("defectionTypeName", defectionCode.getDefectionType().getDefectionTypeName());
				map.put("defectionTypeNo",defectionCode.getDefectionType().getDefectionTypeNo());
				list.add(map);
			}
			resultPage.setResult(list);
			renderText(PageUtils.pageToJson(resultPage));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

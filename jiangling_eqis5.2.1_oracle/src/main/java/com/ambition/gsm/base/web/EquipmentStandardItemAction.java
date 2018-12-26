package com.ambition.gsm.base.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.gsm.base.service.EquipmentStandardItemManager;
import com.ambition.gsm.base.service.EquipmentStandardManager;
import com.ambition.gsm.entity.EquipmentStandard;
import com.ambition.gsm.entity.EquipmentStandardItem;
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

@Namespace("/gsm/base/equipment-standard")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/gsm/base-info/equipment-standard", type = "redirectAction") })
public class EquipmentStandardItemAction extends BaseAction<EquipmentStandardItem>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private EquipmentStandardItem standardItem;
	@Autowired
	private LogUtilDao logUtilDao;
	private JSONObject params;
	private String measurementName;
	@Autowired
	private EquipmentStandardItemManager standardItemManager;
	private Page<EquipmentStandardItem> page;
	private List<EquipmentStandardItem> list;
	private File myFile;
	private Long standardId;
	@Autowired
	private EquipmentStandardManager equipmentStandardManager;
	private EquipmentStandard equipmentStandard;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public String getMeasurementName() {
		return measurementName;
	}

	public void setMeasurementName(String measurementName) {
		this.measurementName = measurementName;
	}

	public EquipmentStandardItem getEquipmentStandardItem() {
		return standardItem;
	}

	public void setEquipmentStandardItem(EquipmentStandardItem standardItem) {
		this.standardItem = standardItem;
	}

	public Page<EquipmentStandardItem> getPage() {
		return page;
	}

	public void setPage(Page<EquipmentStandardItem> page) {
		this.page = page;
	}

	public List<EquipmentStandardItem> getList() {
		return list;
	}

	public void setList(List<EquipmentStandardItem> list) {
		this.list = list;
	}

	public Long getStandardId() {
		return standardId;
	}

	public void setStandardId(Long standardId) {
		this.standardId = standardId;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	public EquipmentStandardItem getStandardItem() {
		return standardItem;
	}

	public void setStandardItem(EquipmentStandardItem standardItem) {
		this.standardItem = standardItem;
	}

	public EquipmentStandard getEquipmentStandard() {
		return equipmentStandard;
	}

	public void setEquipmentStandard(EquipmentStandard equipmentStandard) {
		this.equipmentStandard = equipmentStandard;
	}

	@Override
	public EquipmentStandardItem getModel() {
		return standardItem;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			standardItem = new EquipmentStandardItem();
			if(standardId != null && standardId != 0){
				equipmentStandard = equipmentStandardManager.getEquipmentStandard(standardId);
			}else{
				equipmentStandard = null;
			}
			standardItem.setCompanyId(ContextUtils.getCompanyId());
			standardItem.setCreatedTime(new Date());
			standardItem.setCreator(ContextUtils.getUserName());
			standardItem.setEquipmentStandard(equipmentStandard);
			standardItem.setLastModifiedTime(new Date());
			standardItem.setLastModifier(ContextUtils.getUserName());
			standardItem.setBusinessUnitName(ContextUtils.getSubCompanyName());
			standardItem.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			standardItem = standardItemManager.getEquipmentStandardItem(id);
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
			standardItem.setLastModifiedTime(new Date());
			standardItem.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", standardItem.toString());
		}else{
			logUtilDao.debugLog("保存", standardItem.toString());
		}
		try {
			standardItemManager.saveEquipmentStandardItem(standardItem);
			renderText(JsonParser.object2Json(standardItem));
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
		standardItemManager.deleteEquipmentStandardItem(deleteIds);
		return null;
	}

	@Action("delete-subs")
	@LogInfo(optType="删除子级",message="不良项目")
	public String deleteSubs() throws Exception {
		if(standardId != null && standardId != 0){
			equipmentStandard = equipmentStandardManager.getEquipmentStandard(standardId);
		}else{
			equipmentStandard = null;
		}
		list = standardItemManager.listAll(equipmentStandard);
		for(EquipmentStandardItem standardItem: list){
			standardItemManager.deleteEquipmentStandardItem(standardItem);
		}
		return null;
	}
	
	@Action("search-subs")
	public String searchSubs() throws Exception {
		if(standardId != null && standardId != 0){
			equipmentStandard = equipmentStandardManager.getEquipmentStandard(standardId);
		}else{
			equipmentStandard = null;
		}
		list = standardItemManager.listAll(equipmentStandard);
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
		if(standardId != null && standardId != 0){
			equipmentStandard = equipmentStandardManager.getEquipmentStandard(standardId);
		}else{
			equipmentStandard = null;
		}
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception{
		String myId = Struts2Utils.getParameter("standardId");
		if(myId != null && !myId.equals("")){
			standardId = Long.valueOf(myId);
			equipmentStandard = equipmentStandardManager.getEquipmentStandard(standardId);
		}
		String code = null;
		try {
			code = Struts2Utils.getParameter("standardItem");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(code != null && !code.equals("")){
			page = standardItemManager.list(page, code);
		}else{
			page = standardItemManager.list(page, equipmentStandard);
		}		
		renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "计量器具管理：基础设置-仪器标准件维护");
		return null;
	}
	@Action("exportCode2")
	@LogInfo(optType="导出",message="不良项目")
	public String export() throws Exception {
		Page<EquipmentStandardItem> page = new Page<EquipmentStandardItem>(100000);
		String myId = Struts2Utils.getParameter("standardId");
		if(myId != null && !myId.equals("")){
			standardId = Long.valueOf(myId);
			equipmentStandard = equipmentStandardManager.getEquipmentStandard(standardId);
		}
		page = standardItemManager.list(page, equipmentStandard);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"GSM_EQUIPMENT_STANDARD_ITEM"),"standardItem"));
		logUtilDao.debugLog("导出", "计量器具管理：基础设置-仪器标准件维护");
		return null;
	}
	@Action("imports")
	public String imports() throws Exception {
		return "imports";
	}
	@Action("standard-item-multi-select")
	public String standardItemSelect() throws Exception {
		String measurementName=Struts2Utils.getParameter("measurementName");
		this.setMeasurementName(measurementName);
		return SUCCESS;
	}
	
	@Action("select-list-datas")
	public String getCodeListByParent() throws Exception {		
		params = convertJsonObject(params);
		String measurementName=Struts2Utils.getParameter("measurementName");
		page = standardItemManager.listByParams(page,measurementName);
		try {
			Page<Object> resultPage = new Page<Object>();
			resultPage.setOrder(page.getOrder());
			resultPage.setOrderBy(page.getOrderBy());
			resultPage.setPageNo(page.getPageNo());
			resultPage.setPageSize(page.getPageSize());
			resultPage.setTotalCount(page.getTotalCount());
			List<Object> list = new ArrayList<Object>();
			for(EquipmentStandardItem standardItem : page.getResult()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id",standardItem.getId());
				map.put("standardName",standardItem.getStandardName());
				map.put("standardNo",standardItem.getStandardNo());
				map.put("validityDate",standardItem.getValidityDate());
				map.put("certificateNo",standardItem.getCertificateNo());
				list.add(map);
			}
			resultPage.setResult(list);
			renderText(PageUtils.pageToJson(resultPage));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
     * 转换json格式
     */
    private JSONObject convertJsonObject(JSONObject params){
        JSONObject resultJson = new JSONObject();
        if(params == null){
            return resultJson;
        }else{
            for(Object key : params.keySet()){
                resultJson.put(key,params.getJSONArray(key.toString()).get(0));
            }
            return resultJson;
        }
    }	
}

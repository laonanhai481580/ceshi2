package com.ambition.carmfg.plantparameter.web;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.entity.PlantItem;
import com.ambition.carmfg.plantparameter.service.PlantItemManager;
import com.ambition.product.BaseAction;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.web.struts2.CrudActionSupport;

/**
 * 
 * 类名:设备信息Action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月3日 发布
 */

@Namespace("/carmfg/plant-item")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/carmfg/plant-item", type = "redirectAction") })
public class PlantItemAction extends BaseAction<PlantItem>{

		/**
		  *PlantItemAction.java2016年9月3日
		 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;//删除的编号 
	private JSONObject params;//检验项目指标对象
	private File myFile;
	private String myFileFileName;//导入文件名称
	private Page<PlantItem> page;
	private PlantItem plantItem;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private PlantItemManager plantItemManager;
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
	public JSONObject getParams() {
		return params;
	}
	public void setParams(JSONObject params) {
		this.params = params;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	public String getMyFileFileName() {
		return myFileFileName;
	}
	public void setMyFileFileName(String myFileFileName) {
		this.myFileFileName = myFileFileName;
	}
	public Page<PlantItem> getPage() {
		return page;
	}
	public void setPage(Page<PlantItem> page) {
		this.page = page;
	}
	public PlantItem getPlantItem() {
		return plantItem;
	}
	public void setPlantItem(PlantItem plantItem) {
		this.plantItem = plantItem;
	}
	@Override
	public PlantItem getModel() {
		return plantItem;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			plantItem = new PlantItem();
			plantItem.setCreatedTime(new Date());
			plantItem.setCompanyId(ContextUtils.getCompanyId());
			plantItem.setCreator(ContextUtils.getUserName());
			plantItem.setLastModifiedTime(new Date());
			plantItem.setLastModifier(ContextUtils.getUserName());
			plantItem.setBusinessUnitName(ContextUtils.getSubCompanyName());
			plantItem.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			plantItem = plantItemManager.getPlantItem(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return null;
	}
	@Action("save")
	@LogInfo(optType="保存",message="设备信息维护")
	@Override
	public String save() throws Exception {
		try {
			if(id == null){
				plantItemManager.savePlantItem(plantItem);
				logUtilDao.debugLog("保存", plantItem.toString());
			}else{
				plantItem.setLastModifiedTime(new Date());
				plantItem.setLastModifier(ContextUtils.getUserName());
				plantItemManager.savePlantItem(plantItem);
				logUtilDao.debugLog("修改", plantItem.toString());
			}
			this.renderText(JsonParser.getRowValue(plantItem));
		} catch (Exception e) {
			createErrorMessage("保存失败:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="设备信息维护")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				plantItemManager.deletePlantItem(deleteIds);
				//createMessage("删除成功!");
			} catch (Exception e) {
				createErrorMessage("删除失败:" + e.getMessage());
			}
		}
		return null;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		renderMenu();
		return SUCCESS;
	}
	private String getValue(Object obj){
		if(obj==null){
			return "";
		}else{
			return obj+"";
		}
	}
	private String formatPlantItem(PlantItem plantItem){
		StringBuffer row = new StringBuffer("{");
		row.append("\"plantName\":\"" + getValue(plantItem.getPlantName()) + "\"")
		.append(",\"id\":\"" + getValue(plantItem.getId()) + "\"")
		.append(",\"parameterName\":\"" + getValue(plantItem.getParameterName()) + "\"")
		.append(",\"parameterStandard\":\"" + getValue(plantItem.getParameterStandard()) + "\"")
		.append(",\"unit\":\"" + getValue(plantItem.getUnit()) + "\"")
		.append(",\"remark\":\"" + getValue(plantItem.getRemark()) + "\"")
		.append("}");
		return row.toString();
	}
	@Action("list-datas")
	public String getBomListByParent() throws Exception {
		try {
			page = plantItemManager.search(page);
			StringBuilder json = new StringBuilder();
			json.append("{\"page\":\"");
			json.append(getValue(page.getPageNo()));
			json.append("\",\"total\":\"");
			json.append(getValue(page.getTotalPages()));
			json.append("\",\"records\":\"");
			json.append(getValue(page.getTotalCount()));
			json.append("\",\"rows\":");
			StringBuffer rows = new StringBuffer("[");
			for(PlantItem plantItem : page.getResult()){
				if(rows.length()>1){
					rows.append(",");
				}
				rows.append(formatPlantItem(plantItem));
			}
			json.append(rows.append("]"));
			json.append("}");
			renderText(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出",message="设备信息维护")
	public String export() throws Exception {
		try {
			Page<PlantItem> page = new Page<PlantItem>(Integer.MAX_VALUE);
			page = plantItemManager.search(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_PLANT_ITEM"),"设备信息"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	private void createMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
}

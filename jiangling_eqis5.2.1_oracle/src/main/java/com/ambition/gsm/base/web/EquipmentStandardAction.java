package com.ambition.gsm.base.web;

import java.util.Date;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.gsm.base.service.EquipmentStandardManager;
import com.ambition.gsm.entity.EquipmentStandard;
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

@Namespace("/gsm/base/equipment-standard")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "gsm/base-info/equipment-standard", type = "redirectAction") })
public class EquipmentStandardAction extends BaseAction<EquipmentStandard>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private String operate;
	private EquipmentStandard equipmentStandard;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private EquipmentStandardManager equipmentStandardManager;
	private Page<EquipmentStandard> page;
	
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
	public EquipmentStandard getEquipmentStandard() {
		return equipmentStandard;
	}
	public void setEquipmentStandard(EquipmentStandard equipmentStandard) {
		this.equipmentStandard = equipmentStandard;
	}
	public Page<EquipmentStandard> getPage() {
		return page;
	}
	public void setPage(Page<EquipmentStandard> page) {
		this.page = page;
	}
	@Override
	public EquipmentStandard getModel() {
		return equipmentStandard;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			equipmentStandard = new EquipmentStandard();
			equipmentStandard.setCompanyId(ContextUtils.getCompanyId());
			equipmentStandard.setCreatedTime(new Date());
			equipmentStandard.setCreator(ContextUtils.getUserName());
			equipmentStandard.setLastModifiedTime(new Date());
			equipmentStandard.setLastModifier(ContextUtils.getUserName());
			equipmentStandard.setBusinessUnitName(ContextUtils.getSubCompanyName());
			equipmentStandard.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			equipmentStandard = equipmentStandardManager.getEquipmentStandard(id);
		}
	}
	@Action("standard-delete")
	@LogInfo(optType="删除",message="设备信息")
	@Override
	public String delete() throws Exception {
		try{
			equipmentStandardManager.deleteEquipmentStandard(deleteIds);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	@Action("standard-input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("standard-list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	@Action("standard-save")
	@LogInfo(optType="保存",message="设备信息")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			equipmentStandard.setLastModifiedTime(new Date());
			equipmentStandard.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", equipmentStandard.toString());
		}else{
			logUtilDao.debugLog("保存", equipmentStandard.toString());
		}
		try{
			equipmentStandardManager.saveEquipmentStandard(equipmentStandard);
			this.renderText(JsonParser.object2Json(equipmentStandard));
		}catch(Exception e){
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}	
	@Action("standard-list-datas")
	public String getListDatas() throws Exception {
		page = equipmentStandardManager.list(page);
		this.renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "计量器具管理：基础设置-设备信息维护");
		return null;
	}
	@Action("exportCode")
	@LogInfo(optType="导出",message="设备信息")
	public String export() throws Exception {
		Page<EquipmentStandard> page = new Page<EquipmentStandard>(100000);
		page = equipmentStandardManager.list(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"GSM_EQUIPMENT_STANDARD"),"equipmentStandard"));
		logUtilDao.debugLog("导出", "计量器具管理：基础设置-设备信息维护");
		return null;
	}
}

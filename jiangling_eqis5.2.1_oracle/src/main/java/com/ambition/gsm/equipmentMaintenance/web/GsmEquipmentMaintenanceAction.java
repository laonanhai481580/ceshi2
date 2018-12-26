package com.ambition.gsm.equipmentMaintenance.web;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.gsm.entity.GsmEquipmentMaintenance;
import com.ambition.gsm.equipment.service.GsmEquipmentManager;
import com.ambition.gsm.equipmentMaintenance.service.GsmEquipmentMaintenanceManager;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;

/**
 * 量检具维修管理(ACTION)
 * @author 张顺志
 *
 */
@Namespace("/gsm/equipment-maintenance")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "gsm/equipment-maintenance", type = "redirectAction") })
public class GsmEquipmentMaintenanceAction extends CrudActionSupport<GsmEquipmentMaintenance>{
	private static final long serialVersionUID = 1L;
	private Logger logger  = Logger.getLogger(this.getClass());
	private Long id;
	private String ids;
	private String deleteIds;
	private GsmEquipmentMaintenance gsmEquipmentMaintenance;
	private Page<GsmEquipmentMaintenance> page;
	private JSONObject params;
	
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private GsmEquipmentMaintenanceManager gsmEquipmentMaintenanceManager;
	@Autowired
	private GsmEquipmentManager  gsmEquipmentManager;

	/**
	 * 获取对象
	 */
	@Override
	public GsmEquipmentMaintenance getModel() {
		return gsmEquipmentMaintenance;
	}
	
	/**
	 * 保存对象
	 */
	@Action("save")
	@LogInfo(optType="保存",message="量检具维修管理")
	public String save()  throws Exception {		
		try{
			gsmEquipmentMaintenanceManager.saveGsmEquipmentMaintenance(gsmEquipmentMaintenance);
			renderText(JsonParser.getRowValue(gsmEquipmentMaintenance));
		}catch(Exception e){
			logger.error("量检具维修保存失败：", e);
		}
		return null;
	}
	 
	/**
	 * 删除对象
	 */
	@Action("delete")
	@LogInfo(optType="删除",message="量检具维修管理")
	@Override
	public String delete() throws Exception { 
		try {
			gsmEquipmentMaintenanceManager.deleteGsmEquipmentMaintenance(deleteIds);
		} catch (Exception e) {
			logger.error("量检具维修删除失败：", e);
			createErrorMessage("量检具维修删除失败："+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 表单对象
	 */
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}

	/**
	 * 列表页面
	 */
	@Action("list")
	@Override
	public String list() throws Exception { 
		return SUCCESS;
	}
	
	/**
	 * 列表数据
	 * @return
	 * @throws Exception
	 */
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try {
			page = gsmEquipmentMaintenanceManager.getPage(page, GsmEquipmentMaintenance.STATE_MAINTENANCE_OUT, "notin");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "计量器具管理：计量器具台帐");
		return null;
	}
	
	/**
	 * 列表页面
	 * @return
	 * @throws Exception
	 */
	@Action("list-over")
	public String listOver() throws Exception { 
		return SUCCESS;
	}
	
	/**
	 * 列表数据
	 * @return
	 * @throws Exception
	 */
	@Action("list-over-datas")
	public String getListOverDatas() throws Exception {
		try {
			page = gsmEquipmentMaintenanceManager.getPage(page, GsmEquipmentMaintenance.STATE_MAINTENANCE_OUT, "in");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "计量器具管理：计量器具台帐");
		return null;
	}
	
	/**
	 * 导出数据
	 * @return
	 * @throws Exception
	 */
	@Action("export")
	@LogInfo(optType="导出",message="量检具维修管理")
	public String export() throws Exception {
		Page<GsmEquipmentMaintenance> page = new Page<GsmEquipmentMaintenance>(100000);
		page = gsmEquipmentMaintenanceManager.getPage(page, null, null);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MEASUREMENT_EQUIPMENT_MAINTENANCE"),"量检具维修台账"));
		return null;
	}
	
	/**
	 * 送修
	 * @return
	 * @throws Exception
	 */
	@Action("sendMaintenance")
	@LogInfo(optType="送修",message="量检具维修管理")
	public String sendMaintenance() throws Exception {
		try {
			gsmEquipmentMaintenanceManager.sendMaintenance(ids);
	 		createMessage("送修成功");
		} catch (Exception e) {
			logger.error("送修失败：", e);
	 		createErrorMessage("送修失败："+e.getMessage());
		}
		return null;
	}	
	
	/**
	 * 入库
	 * @return
	 * @throws Exception
	 */
	@Action("putinMaintenance")
	@LogInfo(optType="入库",message="量检具维修管理")
	public String putinMaintenance() throws Exception {
		try {
			gsmEquipmentMaintenanceManager.putinMaintenance(ids);
	 		createMessage("入库成功");
		} catch (Exception e) {
			logger.error("入库失败：", e);
	 		createErrorMessage("入库失败："+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 预处理对象
	 */
	@Override
	protected void prepareModel() throws Exception {
		if (id == null) {
			gsmEquipmentMaintenance=new GsmEquipmentMaintenance();
			gsmEquipmentMaintenance.setCompanyId(ContextUtils.getCompanyId());
			gsmEquipmentMaintenance.setCreatedTime(new Date());
			gsmEquipmentMaintenance.setCreator(ContextUtils.getLoginName());
			gsmEquipmentMaintenance.setCreatorName(ContextUtils.getUserName());
			gsmEquipmentMaintenance.setModifiedTime(new Date());
			gsmEquipmentMaintenance.setModifier(ContextUtils.getLoginName());
			gsmEquipmentMaintenance.setModifierName(ContextUtils.getUserName());
			gsmEquipmentMaintenance.setBusinessUnitName(ContextUtils.getSubCompanyName());
			gsmEquipmentMaintenance.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		} else {
			gsmEquipmentMaintenance=gsmEquipmentMaintenanceManager.getGsmEquipmentMaintenance(id);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public GsmEquipmentMaintenance getGsmEquipmentMaintenance() {
		return gsmEquipmentMaintenance;
	}

	public void setGsmEquipmentMaintenance(
			GsmEquipmentMaintenance gsmEquipmentMaintenance) {
		this.gsmEquipmentMaintenance = gsmEquipmentMaintenance;
	}

	public Page<GsmEquipmentMaintenance> getPage() {
		return page;
	}

	public void setPage(Page<GsmEquipmentMaintenance> page) {
		this.page = page;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}
	
}
package com.ambition.gsm.equipment.web;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.gsm.codeRules.service.GsmCodeRulesManager;
import com.ambition.gsm.entity.GsmCodeRules;
import com.ambition.gsm.entity.GsmCodeSecRules;
import com.ambition.gsm.entity.GsmEquipment;
import com.ambition.gsm.entity.GsmEquipmentMaintenance;
import com.ambition.gsm.entity.GsmMailSendUsers;
import com.ambition.gsm.entity.GsmMailSettings;
import com.ambition.gsm.entity.InspectionMsaplan;
import com.ambition.gsm.entity.InspectionPlan;
import com.ambition.gsm.equipment.service.GsmEquipmentManager;
import com.ambition.gsm.equipmentMaintenance.service.GsmEquipmentMaintenanceManager;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.supplier.utils.DateUtil;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.mms.base.utils.view.DynamicColumnDefinition;
import com.norteksoft.mms.base.utils.view.GridColumnInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 计量器具管理(ACTION)
 * @author 张顺治
 *
 */
@Namespace("/gsm/equipment")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/gsm/equipment", type = "redirectAction") })
public class GsmEquipmentAction extends	CrudActionSupport<GsmEquipment> {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	private Long id;
	private String ids;
	private String deleteIds;
	private GsmEquipment gsmEquipment;
	private Page<GsmEquipment> page;
	private JSONObject params;
	private List<Option> deptOption;
	private List<Option> typeOption;
	private String useDept;
	private String type;
	private File file;
	private String fileName;
	private GridColumnInfo personGridColumnInfo;
	private String businessCode;
	private GsmEquipmentMaintenance gsmEquipmentMaintenance;
	private InspectionMsaplan inspectionMsaPlan;
	private InspectionPlan inspectionPlan;
	private Boolean multiselect = false;
	private Integer pagegsmSize;//仪器总项数
	private Integer pageGsmNum;//校准报警项
	private String colCode;//列代号
	private List<DynamicColumnDefinition> dynamicColumn=new ArrayList<DynamicColumnDefinition>();
	
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	FormCodeGenerated formCodeGenerated;
	@Autowired
	private GsmEquipmentManager gsmEquipmentManager;
	@Autowired
	private GsmEquipmentMaintenanceManager gsmEquipmentMaintenanceManager;
	@Autowired
	private GsmCodeRulesManager gsmCodeRulesManager;
	
	
	/**
	 * 获取对象
	 */
	@Override
	public GsmEquipment getModel() {
		return gsmEquipment;
	}

	/**
	 * 预处理对象
	 */
	@Override
	protected void prepareModel() throws Exception {
		if (id == null) {
			gsmEquipment = new GsmEquipment();
			gsmEquipment.setCompanyId(ContextUtils.getCompanyId());
			gsmEquipment.setCreatedTime(new Date());
			gsmEquipment.setCreator(ContextUtils.getLoginName());
			gsmEquipment.setCreatorName(ContextUtils.getUserName());
			gsmEquipment.setBusinessUnitName(ContextUtils.getSubCompanyName());
			gsmEquipment.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
			gsmEquipment.setMeasurementState(GsmEquipment.STATE_DEFAULT_INSTOCK);//此状态用与区分是 在途 还是 在库 状态(非计量器具状态)
			gsmEquipment.setGsmState("N");
		} else {
			gsmEquipment = gsmEquipmentManager.getGsmEquipment(id);
		}
	}

	/**
	 * 保存对象
	 */
	@Action("savegsm")
	@LogInfo(optType="保存",message="计量器具管理")
	@Override
	public String save() throws Exception {
		try {
			String saveType = Struts2Utils.getParameter("saveType");
			gsmEquipment.setModifiedTime(new Date());
			gsmEquipment.setModifier(ContextUtils.getLoginName());
			gsmEquipment.setModifierName(ContextUtils.getUserName());
			if(id!=null){
				gsmEquipment.setLastModifiedTime(new Date());
				gsmEquipment.setLastModifier(ContextUtils.getUserName());
			}
 			gsmEquipmentManager.saveGsmEquipment(gsmEquipment);
			addActionMessage("保存成功");
			if("list".equals(saveType)){
				this.renderText(JsonParser.getRowValue(gsmEquipment));
				return null;
			}
		} catch (Exception e) {
			logger.error("计量器具管理保存失败", e);
			addActionError("计量器具管理保存失败"+e.getMessage());
			createErrorMessage("计量器具管理保存失败"+e.getMessage());
		}
		renderInput();
		return "input";
	}
	@Action("update-state-record")
	@LogInfo(optType="盘点保存",message="计量器具管理")
	public String updateRecord() throws Exception{
		String inventoryStr=Struts2Utils.getParameter("inventoryStr");
		JSONArray inventoryArray=null;
		if(inventoryStr!=null){
			inventoryArray=JSONArray.fromObject(inventoryStr);
		}
		for(int i=0;i<inventoryArray.size();i++){
			JSONObject json=inventoryArray.getJSONObject(i);
			for(Object key : json.keySet()){
				String value = json.getString(key.toString());
				if(key.toString().equals("id")){
					gsmEquipment=gsmEquipmentManager.getGsmEquipment(Long.valueOf(value));
				}else{
					setProperty(gsmEquipment, key.toString(),value);
				}
			}
			gsmEquipmentManager.saveGsmEquipment(gsmEquipment);
		}
		JSONObject obj= new JSONObject();
		obj.put("message", "保存成功!");
		renderText(obj.toString());
		return null;
	}
	@Action("save-transfer")
	@LogInfo(optType="转移保存",message="责任人转移")
	public String saveTransfer() throws Exception{
		String inventoryStr=Struts2Utils.getParameter("inventoryStr");
		JSONArray inventoryArray=null;
		if(inventoryStr!=null){
			inventoryArray=JSONArray.fromObject(inventoryStr);
		}
		for(int i=0;i<inventoryArray.size();i++){
			JSONObject json=inventoryArray.getJSONObject(i);
			for(Object key : json.keySet()){
				String value = json.getString(key.toString());
				if(key.toString().equals("id")){
					gsmEquipment=gsmEquipmentManager.getGsmEquipment(Long.valueOf(value));
				}else{
					setProperty(gsmEquipment, key.toString(),value);
				}
			}		
			gsmEquipment.setTransferState(GsmEquipment.STATE_TRANSFER_CONFIRM);
			gsmEquipmentManager.saveGsmEquipment(gsmEquipment);
		}
		JSONObject obj= new JSONObject();
		obj.put("message", "保存成功!");
		renderText(obj.toString());
		return null;
	}
	@Action("save-transfer-input")
	@LogInfo(optType="转移确认",message="责任人转移")
	public String saveTransferInput() throws Exception{
		String inventoryStr=Struts2Utils.getParameter("inventoryStr");
		JSONArray inventoryArray=null;
		if(inventoryStr!=null){
			inventoryArray=JSONArray.fromObject(inventoryStr);
		}
		for(int i=0;i<inventoryArray.size();i++){
			JSONObject json=inventoryArray.getJSONObject(i);
			for(Object key : json.keySet()){
				String value = json.getString(key.toString());
				if(key.toString().equals("id")){
					gsmEquipment=gsmEquipmentManager.getGsmEquipment(Long.valueOf(value));
				}else{
					setProperty(gsmEquipment, key.toString(),value);
				}
			}
			gsmEquipment.setDutyMan(gsmEquipment.getGoalDutyMan());
			gsmEquipment.setDutyLoginMan(gsmEquipment.getGoalDutyLoginMan());
			gsmEquipment.setGoalDutyMan(null);
			gsmEquipment.setGoalDutyLoginMan(null);
			gsmEquipment.setTransferTime(null);
			gsmEquipment.setAuditTransferTime(null);
			gsmEquipment.setTransferState(GsmEquipment.STATE_TRANSFER_WAIT);
			gsmEquipmentManager.saveGsmEquipment(gsmEquipment);
		}
		JSONObject obj= new JSONObject();
		obj.put("message", "保存成功!");
		renderText(obj.toString());
		return null;
	}
	private void setProperty(Object obj, String property, Object value) throws Exception {
		String fieldName = property;
		Class<?> type = PropertyUtils.getPropertyType(obj, fieldName);
		if (type != null) {
			if (value == null || StringUtils.isEmpty(value.toString())) {
				PropertyUtils.setProperty(obj, fieldName, null);
			} else {
				if(type.toString().indexOf("Date")>=0){
					PropertyUtils.setProperty(obj, fieldName,DateUtil.parseDateTime(value.toString()));
				}else if (String.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName, value.toString());
				} else if (Integer.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Integer.valueOf(value.toString()));
				} else if (Double.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Double.valueOf(value.toString()));
				} else if (Float.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Float.valueOf(value.toString()));
				} else if (Boolean.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Boolean.valueOf(value.toString()));
				} else {
					PropertyUtils.setProperty(obj, fieldName, value);
				}
			}
		}
	}
	
	/**
	  * 方法名:盘点页面
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("inventory-input")
	public String inventoryInput() throws Exception{
		String ids= Struts2Utils.getParameter("ids");
		ActionContext.getContext().put("ids", ids);
		ActionContext.getContext().put("processSections",ApiFactory.getSettingService().getOptionsByGroupCode("_businessDivision"));
		return SUCCESS;
	}
	
	/**
	  * 方法名:盘点数据
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("inventory-input-datas")
	public String inventoryInputDatas() throws Exception{
		//String ids= Struts2Utils.getParameter("ids");
		try{
			page=gsmEquipmentManager.getPageOfInventory(page);
			this.renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			logger.error("加载待盘点数据失败", e);
		}
		return null;
	}
	/**
	 * 转移台账
	 * @return
	 */
	@Action("transfer-list")
	public String transferList() {
		return SUCCESS;
	}
	/**
	  * 方法名:转移台账数据数据
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("transfer-list-datas")
	public String transferListDatas() throws Exception{
		try{
			page=gsmEquipmentManager.getPageOfTransferDatas(page);
			this.renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			logger.error("加载可转移数据失败", e);
		}
		return null;
	}
	@Action("transfer-input")
	public String transferInput() throws Exception{
		String ids= Struts2Utils.getParameter("ids");
		ActionContext.getContext().put("ids", ids);
		return SUCCESS;
	}
	@Action("transfer")
	public String transfer() throws Exception{
		String ids= Struts2Utils.getParameter("ids");
		ActionContext.getContext().put("ids", ids);
		return SUCCESS;
	}
	/**
	  * 方法名:转移数据
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("transfer-datas")
	public String transferDatas() throws Exception{
		try{
			page=gsmEquipmentManager.getPageInTransfer(page);
			this.renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			logger.error("加载可转移数据失败", e);
		}
		return null;
	}
	/**
	  * 方法名:转移确认数据
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("transfer-input-datas")
	public String transferInputDatas() throws Exception{
		try{
			page=gsmEquipmentManager.getPageOfTransferConfirm(page);
			this.renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			logger.error("加载待盘点数据失败", e);
		}
		return null;
	}
	/**
	 * 列表页面
	 * @return
	 */
	@Action("maintenance-list")
	public String getmaintenanceList() {
		return SUCCESS;
	}

	/**
	 * 列表数据
	 * @return
	 */
	@Action("maintenance-list-datas")
	public String getmaintenanceListDatas() {
		try {
			page = gsmEquipmentManager.getPageByGsmEquipment(page, null);
			logUtilDao.debugLog("查询", "计量器具管理：计量器具台帐");
		}catch (Exception e) {
			logger.error("计量器具管理数据查询失败", e);
			addActionError("计量器具管理数据查询失败"+e.getMessage());
			createErrorMessage("计量器具管理数据查询失败"+e.getMessage());
		}
		this.renderText(PageUtils.pageToJson(page));
		return null;
	}

	/**
	 * 复制对象
	 * @return
	 * @throws Exception
	 */
	@Action("frow")
	public String frow() throws Exception{ 
		try {
			String[] reslut = ids.split(",");
			for (int i = 0; i < reslut.length; i++) {
				String id = reslut[i].toString();
				GsmEquipment gsmEquipment = gsmEquipmentManager.getGsmEquipment(Long.valueOf(id));// 获得要复制的内容 
				GsmEquipment gEquipment = new GsmEquipment(); 
				gsmEquipmentManager.saveGsmEquipment(gEquipment);
				createMessage("复制成功");
			}
		} catch (Exception e) {
			logger.error("计量器具管理复制失败", e);
			addActionError("计量器具管理复制失败"+e.getMessage());
			createErrorMessage("计量器具管理复制失败"+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 生成报修计划
	 * @return
	 * @throws Exception
	 */
	@Action("createEquipmentMaintenance") 
	@LogInfo(optType="保修",message="计量器具报修")
	public String createEquipmentMaintenance() throws Exception{ 
		try{
			gsmEquipmentManager.createEquipmentMaintenance(ids); 
			createMessage("报修成功");
		} catch (Exception e) {
			logger.error("报修失败：", e);
			addActionError("报修失败："+e.getMessage());
			createErrorMessage("报修失败："+e.getMessage()); 
		}
		return null;
	}
	
	/**
	 * 删除对象
	 */
	@Action("delete")
	@LogInfo(optType="删除",message="计量器具")
	@Override
	public String delete() throws Exception {
		try {
			String equipmentNames = gsmEquipmentManager.deleteGsmEquipment(deleteIds);
			if(equipmentNames.length()>0){
				renderText("所删除的计量器具:"+ equipmentNames + "在校验计划或MSA计划中存在校验记录,不能删除");
			}
		} catch (Exception e) {
			logger.error("计量器具管理删除失败", e);
			addActionError("计量器具管理删除失败"+e.getMessage());
			createErrorMessage("计量器具管理删除失败"+e.getMessage()); 
		}
		return null;
	}

	/**
	 * 列表页面
	 */
	@Action("list")
	@Override
	public String list() throws Exception {
//		Calendar calendar = Calendar.getInstance();
//		Integer year = calendar.get(Calendar.YEAR);
//		
//		StringBuffer colCodeBydefection = new StringBuffer();//列代号
//		for (int i = year-2; i <= year+2; i++) {
//			String colName = i+"年度",name = "item_" + i;
//			DynamicColumnDefinition dynamicColumnDefinition =new DynamicColumnDefinition();
//			dynamicColumnDefinition.setColName(colName);
//			dynamicColumnDefinition.setName(name);
//			dynamicColumnDefinition.setEditable(false);
//			dynamicColumnDefinition.setColWidth("110");
//			dynamicColumnDefinition.setEdittype(EditControlType.TEXT);
//			dynamicColumnDefinition.setType(DataType.DATE);
//			dynamicColumnDefinition.setExportable(true);
//			dynamicColumnDefinition.setVisible(true);
//			dynamicColumnDefinition.setIsTotal(true);
//			dynamicColumn.add(dynamicColumnDefinition);
//			/****************拼装动态列代号(备用)****************/
//			colCodeBydefection.append(name+",");
//		}
//		this.setColCode(colCodeBydefection.toString());
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
			page = gsmEquipmentManager.getPageByGsmEquipment(page, gsmEquipment);
/*			List<GsmEquipment> list=page.getResult();
			pageGsmNum=0;pagegsmSize=0;
			for (GsmEquipment gsmEquipment : list) {
				Integer isWarm=gsmEquipment.getIsWarm();
				if(isWarm==1){
					pageGsmNum++;
				}
				pagegsmSize++;
			}*/
			this.renderText(gsmEquipmentManager.getResultJson(page));
			logUtilDao.debugLog("查询", "计量器具管理：计量器具台帐");
		} catch (Exception e) {
			logger.error("计量器具管理数据查询失败", e);
			addActionError("计量器具管理数据查询失败"+e.getMessage());
			createErrorMessage("计量器具管理数据查询失败"+e.getMessage()); 
		}
		return null;
	}

	/**
	 * 导出数据
	 * @return
	 * @throws Exception
	 */
	@Action("export")
	@LogInfo(optType="导出",message="计量器具")
	public String export() throws Exception {
		Page<GsmEquipment> page = new Page<GsmEquipment>(100000);
		GsmEquipment gsmEquipment = new GsmEquipment();
		page = gsmEquipmentManager.getPageByGsmEquipment(page, gsmEquipment);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page, "GSM_EQUIPMENT"), "计量器具台账"));
		return null;
	}
	
	/**
	 * 获取上级选项组
	 */
	private void getTopBotOptions(){
		List<GsmCodeRules> topGsmCodeRuless = gsmCodeRulesManager.getGsmCodeRules();
    	List<Option> topOptions = new ArrayList<Option>();
    	List<Option> botOptions = new ArrayList<Option>();
    	for (GsmCodeRules topGsmCodeRules : topGsmCodeRuless) {
    		Option topOption = new Option();
    		topOption.setName(topGsmCodeRules.getId().toString());
    		topOption.setValue(topGsmCodeRules.getMeasurementType());
    		topOptions.add(topOption);
    		getBotOptions(botOptions, topGsmCodeRules);
    	}
    	ActionContext.getContext().put("topOptions", topOptions);
    	ActionContext.getContext().put("botOptions", botOptions);
	}
	
	/**
	 * 获取下级选项组
	 * @param botOptions
	 * @param gsmCodeRules
	 */
	private void getBotOptions(List<Option> botOptions, GsmCodeRules gsmCodeRules){
		//List<GsmCodeSecRules> botGsmCodeSecRuless = gsmCodeSecRulesManager.getGsmCodeSecRules(gsmCodeRules);
		List<GsmCodeSecRules> botGsmCodeSecRuless = gsmCodeRules.getGsmCodeSecRuless();
		if(botGsmCodeSecRuless == null || botGsmCodeSecRuless.size() == 0){
			//Option botOption = new Option();
			//botOption.setName(gsmCodeRules.getId().toString());
			//botOption.setValue(gsmCodeRules.getMeasurementType());
    		//botOptions.add(botOption);
		}else{
			for (GsmCodeSecRules gsmCodeSecRules : botGsmCodeSecRuless) {
				Option botOption = new Option();
				botOption.setName(gsmCodeSecRules.getId().toString());
				botOption.setValue(gsmCodeSecRules.getSecondaryClassification());
	    		botOptions.add(botOption);
			}
		}
	}
	
	/**
	 * 渲染表单
	 */
	private void renderInput(){
//		List<Option> measurementTypes = ApiFactory.getSettingService().getOptionsByGroupCode("measurement_measurementType");
		//校验方式
		List<Option> checkForms = ApiFactory.getSettingService().getOptionsByGroupCode("measurement_checkForm");
//		List<Option> precisionGrades = ApiFactory.getSettingService().getOptionsByGroupCode("measurement_precisionGrade");
//		List<Option> managementTypes = ApiFactory.getSettingService().getOptionsByGroupCode("measurement_managementType");
		//周期
		List<Option> testCycles = ApiFactory.getSettingService().getOptionsByGroupCode("testCycle");
		//分级
		List<Option> measurement_level = ApiFactory.getSettingService().getOptionsByGroupCode("measurement_level");
		List<Option> measurementStates = ApiFactory.getSettingService().getOptionsByGroupCode("measurement_measurementState");
		ActionContext.getContext().put("checkForms", checkForms);
		ActionContext.getContext().put("testCycles", testCycles);
		//事业部
		ActionContext.getContext().put("processSections",ApiFactory.getSettingService().getOptionsByGroupCode("_businessDivision"));
		ActionContext.getContext().put("measurementStates", measurementStates);
		ActionContext.getContext().put("measurement_level", measurement_level);
//		ActionContext.getContext().put("inspectionPlans",gsmEquipment.getInspectionPlans());
//		ActionContext.getContext().put("inspectionMsaplans",gsmEquipment.getInspectionPlans());
//		ActionContext.getContext().put("formCodeGenerated",formCodeGenerated);
		//加载一级二级级联选项组
//		getTopBotOptions();
	}

	/**
	 * 表单页面
	 */
	@Action("input")
	@Override
	public String input() throws Exception {
		renderInput();
		return SUCCESS;
	}
	@Action("download-excel-format")
	@LogInfo(optType="下载",message="计量器具台账导入模板")
	public String downloadExcelFormat()throws Exception {
		String fileName = "计量器具台账导入模板.xls";
		byte byname[] = fileName.getBytes("gbk");
		fileName = new String(byname, "8859_1");
		HttpServletResponse response = Struts2Utils.getResponse();
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", (new StringBuilder(
				"attachment; filename=\"")).append(fileName).append("\"")
				.toString());
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/gsm-equipment.xlsx");
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes);
		response.getOutputStream().write(bytes);
		inputStream.close();
		return null;
	}

	/**
	 * 在途列表页面
	 */
	@Action("intransited-list")
	public String intransitedList() throws Exception {
		return SUCCESS;
	}

	/**
	 * 在途列表数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("intransited-list-datas")
	public String getIntransitedListDatas() throws Exception {
		try {
			page = gsmEquipmentManager.getPageByGsmEquipment(page,null);
		} catch (Exception e) {
			logger.error("计量器具管理数据查询失败", e);
			addActionError("计量器具管理数据查询失败"+e.getMessage());
			createErrorMessage("计量器具管理数据查询失败"+e.getMessage()); 
		}
		this.renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "计量器具管理：计量器具台帐");
		return null;
	}

	/**
	 * 生成校验计划
	 * @return
	 * @throws Exception
	 */
	@Action("create-equipment-plan")
	@LogInfo(optType="生成计划",message="计量器具生成校验计划")
	public String createEquipmentPlan() throws Exception {
		try {
			gsmEquipmentManager.createEquipmentPlan(ids);
 			createMessage("生成校验成功");
		} catch (Exception e) {
			logger.error("生成校验失败：", e);
			addActionError("生成校验失败");
			createErrorMessage("生成校验失败："+e.getMessage()); 
		}
		return null;
	}

	/**
	 * 生成MSA计划
	 * @return
	 * @throws Exception
	 */
	@Action("create-equipment-msaplan")
	@LogInfo(optType="生成计划",message="计量器具生成MSA计划")
	public String createEquipmentmsaPlan() throws Exception {
		try{
			gsmEquipmentManager.createEquipmentmsaPlan(ids); 
			createMessage("生成MSA校验成功");
		} catch (Exception e) {
			logger.error("生成MSA校验失败：", e);
			addActionError("生成MSA校验失败:"+e.getMessage());
			createErrorMessage("生成MSA校验失败:"+e.getMessage()); 
		}
		return null;
	}
	  

	/**
	 * 列表页面
	 * @return
	 * @throws Exception
	 */
	@Action("inspectionplan-list")
	public String inspectionplanList() throws Exception {
		return SUCCESS;
	}

	/**
	 * 列表数据
	 * @return
	 * @throws Exception
	 */
	@Action("inspectionplan-list-datas")
	public String getInspectionplanListDatas() throws Exception {
		try {
			page = gsmEquipmentManager.getPageByGsmEquipment(page,null);
		} catch (Exception e) {
			logger.error("计量器具管理数据查询失败", e);
			addActionError("计量器具管理数据查询失败"+e.getMessage());
			createErrorMessage("计量器具管理数据查询失败"+e.getMessage()); 
		}
		this.renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "校验计划：校验计划台账");
		return null;
	}

	/**
	 * 列表页面
	 * @return
	 * @throws Exception
	 */
	@Action("inspectionmsaplan-list")
	public String inspectionmsaplanList() throws Exception {
		return SUCCESS;
	}

	/**
	 * 列表数据
	 * @return
	 * @throws Exception
	 */
	@Action("inspectionmsaplan-list-datas")
	public String getInspectionmsaplanListDatas() throws Exception {
		try {
			page = gsmEquipmentManager.getPageByGsmEquipment(page,null);
		} catch (Exception e) {
			logger.error("计量器具管理数据查询失败", e);
			addActionError("计量器具管理数据查询失败"+e.getMessage());
			createErrorMessage("计量器具管理数据查询失败"+e.getMessage()); 
		}
		this.renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "MSA计划：MSA计划台账");
		return null;
	}

	/**
	 * 查看页面
	 * @return
	 * @throws Exception
	 */
	@Action("view")
	public String viewInfo() throws Exception {
		if (id != null && id != 0) {
			gsmEquipment = gsmEquipmentManager.getGsmEquipment(id);
			ActionContext.getContext().put("equipment", gsmEquipment);
		}
		return SUCCESS;
	}

	/**
	 * 部门列表页面
	 * @return
	 * @throws Exception
	 */
	@Action("dept-list")
	public String deptList() throws Exception {
		useDept = Struts2Utils.getParameter("dept");
		List<GsmEquipment> list = gsmEquipmentManager.listAll();
        deptOption = converExceptionLevelToList(list);
		//deptOption = ApiFactory.getSettingService().getOptionsByGroupCode("useDept");
		this.setDeptOption(deptOption);
		Struts2Utils.getRequest().setAttribute("deptOption", deptOption);
		return SUCCESS;
	}
	    /**
	     * 方法名: 
	     * <p>功能说明：</p>
	     * @param calendar
	     * @return
	    */
	public List<Option> converExceptionLevelToList(List<GsmEquipment> list){
	   List<Option> options = new ArrayList<Option>();
	   List<String> arraylist = new ArrayList<String>();
	   for(GsmEquipment equipment : list){
	       Option option = new Option();
	       String name = equipment.getDevName().toString();
	       String value = equipment.getDevName().toString();
	       option.setName(name==null?"":name.replaceAll("\n","").replaceAll(",","，"));
	       option.setValue(value==null?"":value.replaceAll("\n","").replaceAll(",","，"));
	       if(!arraylist.contains(name)){
	    	   arraylist.add(name);
	    	   options.add(option);
	       }	      
	   }
	   return options;
	}
	/**
	 * 部门列表数据
	 * @return
	 * @throws Exception
	 */
	@Action("dept-list-datas")
	public String getDeptListDatas() throws Exception {
		String useDept = Struts2Utils.getParameter("useDept");
		try {
			page = gsmEquipmentManager.getPageByUseDept(page, useDept);
		} catch (Exception e) {
			logger.error("计量器具管理数据查询失败", e);
			addActionError("计量器具管理数据查询失败"+e.getMessage());
			createErrorMessage("计量器具管理数据查询失败"+e.getMessage()); 
		}
		this.renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "计量器具管理：部门计量器具台帐");
		return null;
	}

	/**
	 * 类别列表页面
	 * @return
	 * @throws Exception
	 */
	@Action("type-list")
	public String typeList() throws Exception {
		type = Struts2Utils.getParameter("type");
		//typeOption = ApiFactory.getSettingService().getOptionsByGroupCode("measurement_measurementType");
		//this.setTypeOption(typeOption);
		List<GsmCodeRules> gsmCodeRuless = gsmCodeRulesManager.getGsmCodeRules();
		List<Option> typeOption = new ArrayList<Option>();
		for (GsmCodeRules gsmCodeRules : gsmCodeRuless) {
			Option option = new Option();
			option.setId(gsmCodeRules.getId());
			option.setName(gsmCodeRules.getMeasurementType());
			typeOption.add(option);
		}
		Struts2Utils.getRequest().setAttribute("typeOption", typeOption);
		return SUCCESS;
	}

	/**
	 * 类别列表数据
	 * @return
	 * @throws Exception
	 */
	@Action("type-list-datas")
	public String getTypeListDatas() throws Exception {
		String type = Struts2Utils.getParameter("type");
		try {
			if (type == null || type.equals("")) {
				typeOption = ApiFactory.getSettingService().getOptionsByGroupCode("measurement_measurementType");
				type = typeOption.get(0).getValue().toString();
			}
			GsmEquipment gsmEquipment =new GsmEquipment();
//			gsmEquipment.setMeasurementType(type);
			page = gsmEquipmentManager.getPageByGsmEquipment(page, gsmEquipment);
		} catch (Exception e) {
			logger.error("计量器具管理数据查询失败", e);
			addActionError("计量器具管理数据查询失败"+e.getMessage());
			createErrorMessage("计量器具管理数据查询失败"+e.getMessage()); 
		}
		this.renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "计量器具管理：类别计量器具台帐");
		return null;
	}


	/**
	 * 导入数据
	 * @return
	 * @throws Exception
	 */
	@Action("imports")
	@LogInfo(optType="导入",message="计量器具导入数据")
	public String imports() throws Exception {
		try {
			if(file != null){
				renderHtml(gsmEquipmentManager.importGsmEquipment(file));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}

	/**
	 * 导入页面
	 * @return
	 */
	@Action("import-form")
	public String importForm() {
		return SUCCESS;
	}
	
	/**
	 * 邮件设置
	 * @return
	 */
	@Action("mail-settings")
	@LogInfo(optType="证书失效邮件提醒设置")
	public String mailSettings(){
		try {
			GsmMailSettings gsmMailSettings = gsmEquipmentManager.getGsmMailSettingsByBusinessCode(businessCode);
			ActionContext.getContext().getValueStack().push(gsmMailSettings==null?new GsmMailSettings():gsmMailSettings);
			
			personGridColumnInfo = ApiFactory.getMmsService().getGridColumnInfo("MEASUREMENT_EQUIPMENT_MAIL_SEND_USERS");
			JSONArray jsonArray = JSONArray.fromObject(personGridColumnInfo.getColModel());
			personGridColumnInfo.setColModel(jsonArray.toString());
			jsonArray = JSONArray.fromObject(personGridColumnInfo.getColNames());
			personGridColumnInfo.setColNames(jsonArray.toString());
		} catch (Exception e) {
			logger.error("计量器具管理数据查询失败", e);
			addActionError("计量器具管理数据查询失败"+e.getMessage());
			createErrorMessage("计量器具管理数据查询失败"+e.getMessage()); 
		}
		return SUCCESS;
	}
	
	
	/**
	 * 预警人员
	 * @return
	 * @throws Exception
	 */
	@Action("warn-user-datas")
	public String getPersonDatas() throws Exception {
		try {
			if(businessCode != null){
				GsmMailSettings gsmMailSettings= gsmEquipmentManager.getGsmMailSettingsByBusinessCode(businessCode);
				Page<GsmMailSendUsers> pageUsers = new Page<GsmMailSendUsers>(Integer.MAX_VALUE);
				pageUsers.setResult(gsmMailSettings.getGsmMailSendUserss());
				pageUsers.setPageNo(1);
				pageUsers.setTotalCount(pageUsers.getResult().size());
				pageUsers.setPageSize(Integer.MAX_VALUE);
				renderText(PageUtils.pageToJson(pageUsers,"MEASUREMENT_EQUIPMENT_MAIL_SEND_USERS"));
			}
		} catch (Exception e) {
			logger.error("计量器具管理数据查询失败", e);
			addActionError("计量器具管理数据查询失败"+e.getMessage());
			createErrorMessage("计量器具管理数据查询失败"+e.getMessage()); 
		}
		return null;
	}
	
	/**
	 * 完成预警
	 * @return
	 */
	@Action("mail-settings-over")
	@LogInfo(optType="证书失效邮件提醒设置")
	public String mailSettingsOver(){
		try {
			GsmMailSettings gsmMailSettings = gsmEquipmentManager.getGsmMailSettingsByBusinessCode(businessCode);
			ActionContext.getContext().getValueStack().push(gsmMailSettings==null?new GsmMailSettings():gsmMailSettings);
			
			personGridColumnInfo = ApiFactory.getMmsService().getGridColumnInfo("MEASUREMENT_EQUIPMENT_MAIL_SEND_USERS");
			JSONArray jsonArray = JSONArray.fromObject(personGridColumnInfo.getColModel());
			personGridColumnInfo.setColModel(jsonArray.toString());
			jsonArray = JSONArray.fromObject(personGridColumnInfo.getColNames());
			personGridColumnInfo.setColNames(jsonArray.toString());
		} catch (Exception e) {
			logger.error("计量器具管理数据查询失败", e);
			addActionError("计量器具管理数据查询失败"+e.getMessage());
			createErrorMessage("计量器具管理数据查询失败"+e.getMessage()); 
		}
		return SUCCESS;
	}
	
	/**
	 * 保存对象
	 * @return
	 */
	@Action("save-mail-settings")
	public String SaveMailSetting(){
		try {
			GsmMailSettings gsmMailSettings = new GsmMailSettings();
			if(businessCode != null)
				gsmMailSettings= gsmEquipmentManager.getGsmMailSettingsByBusinessCode(businessCode);  
			gsmEquipmentManager.saveMailSetting(gsmMailSettings);
			createMessage("保存成功！");
		} catch (Exception e) {
			logger.error("计量器具管理数据查询失败", e);
			addActionError("计量器具管理数据查询失败"+e.getMessage());
			createErrorMessage("计量器具管理数据查询失败"+e.getMessage()); 
		}
		return null;
	}
	/**
	 * 匹配最大管理编号
		  * 方法名: 
		  * <p>功能说明：</p>
		  * @return
	 */
	@Action("select-max")
	public String selectMax(){
		JSONObject result = new JSONObject();
		String managerAssets = Struts2Utils.getParameter("managerAssets");
		try {
			String max = gsmEquipmentManager.selectMaxManagerAssets(managerAssets); 
			if(max==null){
				result.put("error", true);
				result.put("message", "您输入的为新为则,可以随意录入");
			}else{
				result.put("error", false);
				result.put("value", max);
			}
		} catch (Exception e) {
			result.put("error", true);
			result.put("message", "匹配失败");
		}
		this.renderText(result.toString());
		return null;
	}
	/**
	 * 导入表单
	 * @return
	 * @throws Exception
	 */
	@Action("list-import-form")
	public String listImportForm() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 导入数据
	 * @return
	 * @throws Exception
	 */
	@Action("import")
	public String listImports() throws Exception {
		try {
			if(file != null){
				renderHtml(gsmEquipmentManager.importGsmEquipment(file));
			}
		} catch (Exception e) {
			logger.error("计量器具导入失败", e);
			addActionError("计量器具管理导入失败"+e.getMessage());
			createErrorMessage("计量器具管理导入失败"+e.getMessage());
			renderHtml("导入失败:"+"<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	/**
	 * 列表数据
	 */
	@Action("list-state")
	@LogInfo(optType="查询",message="查询数据")
	public String getListStates() throws Exception {
		String type = Struts2Utils.getParameter("type");
		try{
			if("Y".equals(type)){
				page = gsmEquipmentManager.listState(page, type);
			}else{
				page = gsmEquipmentManager.listState(page, type);
			}
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			logger.error("查询失败!",e);
		}
		return null;
	}
	@Action("hideState")
	public String epmHide(){
		String eid = Struts2Utils.getParameter("id");
		String type = Struts2Utils.getParameter("type");
		try {
			gsmEquipmentManager.gsmHide(eid,type);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			renderText("修改失败");
			return null;
		}
		renderText("修改成功");
		return null;
	}
//	@Action("list-view")
//    public String listView() throws Exception {
//        // TODO Auto-generated method stub
//		 String managerAssets = Struts2Utils.getParameter("managerAssets");
//		 HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//         request.getSession().setAttribute("managerAssets", managerAssets);
//         ids = Struts2Utils.getParameter("ids");
//         request.getSession().setAttribute("ids", ids);
//        return SUCCESS;
//    }
//    @Action("list-view-datas")
//    public String listViewDatas() throws Exception {
//        try{
//        	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//            String managerAssets =  (String) request.getSession().getAttribute("managerAssets");
//            ids = (String) request.getSession().getAttribute("ids");
//        	if(managerAssets!=null){
//        		page = gsmEquipmentManager.searchPageByManagerAssets(page,managerAssets);
//        	}
////        	else if(ids!=null){
////        		page = gsmEquipmentManager.searchPageByIds(page,ids);
////        	} 
//            renderText(PageUtils.pageToJson(page));
//        }catch(Exception e){
//        	logger.error("查询失败!",e);
//        }
//        return null;
//    }

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

	public GsmEquipment getGsmEquipment() {
		return gsmEquipment;
	}

	public void setGsmEquipment(GsmEquipment gsmEquipment) {
		this.gsmEquipment = gsmEquipment;
	}

	public Page<GsmEquipment> getPage() {
		return page;
	}

	public void setPage(Page<GsmEquipment> page) {
		this.page = page;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public List<Option> getDeptOption() {
		return deptOption;
	}

	public void setDeptOption(List<Option> deptOption) {
		this.deptOption = deptOption;
	}

	public List<Option> getTypeOption() {
		return typeOption;
	}

	public void setTypeOption(List<Option> typeOption) {
		this.typeOption = typeOption;
	}

	public String getUseDept() {
		return useDept;
	}

	public void setUseDept(String useDept) {
		this.useDept = useDept;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public GridColumnInfo getPersonGridColumnInfo() {
		return personGridColumnInfo;
	}

	public void setPersonGridColumnInfo(GridColumnInfo personGridColumnInfo) {
		this.personGridColumnInfo = personGridColumnInfo;
	}

	public GsmEquipmentMaintenance getGsmEquipmentMaintenance() {
		return gsmEquipmentMaintenance;
	}

	public void setGsmEquipmentMaintenance(
			GsmEquipmentMaintenance gsmEquipmentMaintenance) {
		this.gsmEquipmentMaintenance = gsmEquipmentMaintenance;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public InspectionMsaplan getInspectionMsaPlan() {
		return inspectionMsaPlan;
	}

	public void setInspectionMsaPlan(InspectionMsaplan inspectionMsaPlan) {
		this.inspectionMsaPlan = inspectionMsaPlan;
	}

	public InspectionPlan getInspectionPlan() {
		return inspectionPlan;
	}

	public void setInspectionPlan(InspectionPlan inspectionPlan) {
		this.inspectionPlan = inspectionPlan;
	}

	public String getColCode() {
		return colCode;
	}

	public void setColCode(String colCode) {
		this.colCode = colCode;
	}

	public List<DynamicColumnDefinition> getDynamicColumn() {
		return dynamicColumn;
	}

	public void setDynamicColumn(List<DynamicColumnDefinition> dynamicColumn) {
		this.dynamicColumn = dynamicColumn;
	}

	public Boolean getMultiselect() {
		return multiselect;
	}

	public void setMultiselect(Boolean multiselect) {
		this.multiselect = multiselect;
	}

	public Integer getPagegsmSize() {
		return pagegsmSize;
	}

	public void setPagegsmSize(Integer pagegsmSize) {
		this.pagegsmSize = pagegsmSize;
	}

	public Integer getPageGsmNum() {
		return pageGsmNum;
	}

	public void setPageGsmNum(Integer pageGsmNum) {
		this.pageGsmNum = pageGsmNum;
	}

	
}
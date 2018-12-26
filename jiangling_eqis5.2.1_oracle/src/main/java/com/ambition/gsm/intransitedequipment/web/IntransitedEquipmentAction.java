package com.ambition.gsm.intransitedequipment.web; 
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import com.ambition.gsm.codeSecRules.service.GsmCodeSecRulesManager;
import com.ambition.gsm.entity.GsmCodeRules;
import com.ambition.gsm.entity.GsmCodeSecRules;
import com.ambition.gsm.entity.GsmEquipment;
import com.ambition.gsm.entity.GsmMailSendUsers;
import com.ambition.gsm.entity.GsmMailSettings;
import com.ambition.gsm.equipment.service.GsmEquipmentManager;
import com.ambition.gsm.intransitedequipment.service.IntransitedEquipmentManager;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.bs.options.entity.Option;
import com.norteksoft.mms.base.utils.view.GridColumnInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 在途量检具管理(ACTION)
 * @author 张顺治
 *
 */
@Namespace("/gsm/intransited-equipment")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/gsm/intransited-equipment", type = "redirectAction") })
public class IntransitedEquipmentAction extends	CrudActionSupport<GsmEquipment> {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	private Long id;
	private String ids;
	private String deleteIds;
	private GsmEquipment intransitedEquipment;
	private Page<GsmEquipment> page;
	private JSONObject params;
	private String businessCode;
	private GridColumnInfo personGridColumnInfo;
	
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	FormCodeGenerated formCodeGenerated;
	@Autowired
	private GsmEquipmentManager gsmEquipmentManager;
	@Autowired
	private IntransitedEquipmentManager intransitedEquipmentManager; 
	@Autowired
	private GsmCodeRulesManager gsmCodeRulesManager;
	@Autowired
	private GsmCodeSecRulesManager gsmCodeSecRulesManager;
	
	/**
	 * 获取对象
	 */
	@Override
	public GsmEquipment getModel() {
		return intransitedEquipment;
	}

	/**
	 * 删除对象
	 */
	@Action("delete")
	@Override
	public String delete() throws Exception {
		try {
			intransitedEquipmentManager.deleteIntransitedEquipment(deleteIds);
		} catch (Exception e) {
			logger.error("在途量检具管理删除失败", e);
			addActionError("在途量检具管理删除失败"+e.getMessage());
			createErrorMessage("在途量检具管理删除失败"+e.getMessage());
		}
		return null;
	}

	/**
	 * 表单页面
	 */
	@Action("input")
	@Override
	public String input() throws Exception {
		getTopBotOptions();
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
			GsmEquipment gsmEquipment = new GsmEquipment();
//			gsmEquipment.setState(GsmEquipment.STATE_INTRANSIT);
			page = gsmEquipmentManager.getPageByGsmEquipment(page, gsmEquipment);
			this.renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			logger.error("在途量检具管理数据查询失败", e);
			addActionError("在途量检具管理数据查询失败"+e.getMessage());
			createErrorMessage("在途量检具管理数据查询失败"+e.getMessage());
		}
		return null;
	}
 
	/**
	 * 导出数据
	 * @return
	 * @throws Exception
	 */
	@Action("export")
	public String export() throws Exception {
		try{
			Page<GsmEquipment> page = new Page<GsmEquipment>(100000);
			GsmEquipment gsmEquipment = new GsmEquipment();
//			gsmEquipment.setState(GsmEquipment.STATE_INTRANSIT);
			page = gsmEquipmentManager.getPageByGsmEquipment(page, gsmEquipment);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page, "MEASUREMENT_INTRANSITED_EQUIPMENT"),"在途量检具台帐"));
		}catch(Exception e){
			logger.error("在途量检具管理导出失败", e);
			addActionError("在途量检具管理导出失败"+e.getMessage());
			createErrorMessage("在途量检具管理导出失败"+e.getMessage());
		}
		return null;
	}

	/**
	 * 预处理对象
	 */
	@Override
	protected void prepareModel() throws Exception {
		if (id == null) {
			intransitedEquipment = new GsmEquipment();
			intransitedEquipment.setCompanyId(ContextUtils.getCompanyId());
			intransitedEquipment.setCreatedTime(new Date());
			intransitedEquipment.setCreator(ContextUtils.getLoginName());
			intransitedEquipment.setCreatorName(ContextUtils.getUserName());
			intransitedEquipment.setBusinessUnitName(ContextUtils.getSubCompanyName());
//			intransitedEquipment.setMeasurementNo(formCodeGenerated.generateMeasurementNoCode());
			intransitedEquipment.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
//			intransitedEquipment.setState(GsmEquipment.STATE_INTRANSIT);//此状态用与区分是 在途 还是 在库 状态(非量检具状态)
		} else {
			intransitedEquipment = intransitedEquipmentManager.getIntransitedEquipment(id);
		}
	}

	/**
	 * 保存对象
	 */
	@Action("save")
	@Override
	public String save() throws Exception {
		try {
			String saveType = Struts2Utils.getParameter("saveType");
			intransitedEquipment.setModifiedTime(new Date());
			intransitedEquipment.setModifier(ContextUtils.getLoginName());
			intransitedEquipment.setModifier(ContextUtils.getUserName());
			gsmEquipmentManager.saveGsmEquipment(intransitedEquipment);
			addActionMessage("保存成功");
			getTopBotOptions();
			if("list".equals(saveType)){
				this.renderText(JsonParser.getRowValue(intransitedEquipment));
				return null;
			}
			if("input".equals(saveType)){
				return "input";
			}
		} catch (Exception e) {
			logger.error("在途量检具管理保存失败", e);
			addActionError("在途量检具管理保存失败"+e.getMessage());
			createErrorMessage("在途量检具管理保存失败"+e.getMessage());
		}
		return "input";
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
	 * 获取计量编号规则级联选项组
	 * @return
	 * @throws Exception
	 */
	@Action("top-bot-options-gsmCodeSecRules")
	public String getTopNextBotOptions() throws Exception{
		try{
			Map<String,List<Option>> map = new HashMap<String, List<Option>>();
			List<Option> botOptions = new ArrayList<Option>();
			if(id != null){
				GsmCodeRules gsmCodeRules = gsmCodeRulesManager.getGsmCodeRules(id);
				getBotOptions(botOptions, gsmCodeRules);
			}
			map.put("botOptions",botOptions);
			renderText(JSONObject.fromObject(map).toString());
		}catch(Exception e){
			logger.error("获取计量编号规则级联选项组失败!", e);
			addActionError("获取计量编号规则级联选项组失败!"+e.getMessage());
			createErrorMessage("获取计量编号规则级联选项组失败!"+e.getMessage());
	    }
    	return null;
	}

	/**
	 * 复制对象
	 * @return
	 */
	@Action("frow")
	public String frow() {
		try {
			intransitedEquipmentManager.createIntransitedEquipment(ids);
			addActionMessage("复制成功");
		} catch (Exception e) {
			logger.error("在途量检具管理复制失败", e);
			addActionError("在途量检具管理复制失败"+e.getMessage());
			createErrorMessage("在途量检具管理复制失败"+e.getMessage());
		}
		return null;
	}

	/**
	 * 确认收货
	 * @return
	 * @throws Exception
	 */
	@Action("confirm-delivery") 
	public String confirmDelivery() throws Exception {
		try {
			intransitedEquipmentManager.confirmDelivery(ids);
 			createMessage("确认收货");
		} catch (Exception e) {
			logger.error("在途量检具管理确认收货失败", e);
			addActionError("在途量检具管理确认收货失败"+e.getMessage());
			createErrorMessage("在途量检具管理确认收货失败"+e.getMessage());
		}
		return null;
	} 

	/**
	 * 发送邮件
	 * @return
	 */
	@Action("send")
	public String send() {
		try {
			if (StringUtils.isNotEmpty(ids)) {
				for (String id : ids.split(",")) {
					// 获得要复制的内容
					GsmEquipment intransitedEquipment = intransitedEquipmentManager.getIntransitedEquipment(Long.valueOf(id));
					intransitedEquipmentManager.saveIntransitedEquipment(intransitedEquipment);
				}
			}
		} catch (Exception e) {
			logger.error("在途量检具管理发送邮件失败", e);
			addActionError("在途量检具管理发送邮件失败"+e.getMessage());
			createErrorMessage("在途量检具管理发送邮件失败"+e.getMessage());
		}
		return null;
	}

	/**
	 * 邮件设置
	 * @return
	 */
	@Action("mail-settings")
	@LogInfo(optType="证书失效邮件提醒设置")
	public String mailSettings(){
		try {
			GsmMailSettings gsmMailSettings = intransitedEquipmentManager.getGsmMailSettingsByBusinessCode(businessCode);
			ActionContext.getContext().getValueStack().push(gsmMailSettings==null?new GsmMailSettings():gsmMailSettings);
			
			personGridColumnInfo = ApiFactory.getMmsService().getGridColumnInfo("MEASUREMENT_EQUIPMENT_MAIL_SEND_USERS");
			JSONArray jsonArray = JSONArray.fromObject(personGridColumnInfo.getColModel());
			personGridColumnInfo.setColModel(jsonArray.toString());
			jsonArray = JSONArray.fromObject(personGridColumnInfo.getColNames());
			personGridColumnInfo.setColNames(jsonArray.toString());
		} catch (Exception e) {
			logger.error("在途量检具管理邮件设置失败", e);
			addActionError("在途量检具管理邮件设置失败"+e.getMessage());
			createErrorMessage("在途量检具管理邮件设置失败"+e.getMessage());
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
				GsmMailSettings gsmMailSettings= intransitedEquipmentManager.getGsmMailSettingsByBusinessCode(businessCode);
				Page<GsmMailSendUsers> pageUsers = new Page<GsmMailSendUsers>(Integer.MAX_VALUE);
				pageUsers.setResult(gsmMailSettings.getGsmMailSendUserss());
				pageUsers.setPageNo(1);
				pageUsers.setTotalCount(pageUsers.getResult().size());
				pageUsers.setPageSize(Integer.MAX_VALUE);
				renderText(PageUtils.pageToJson(pageUsers,"MEASUREMENT_EQUIPMENT_MAIL_SEND_USERS"));
			}
		} catch (Exception e) {
			logger.error("在途量检具管理预警人员失败", e);
			addActionError("在途量检具管理预警人员失败"+e.getMessage());
			createErrorMessage("在途量检具管理预警人员失败"+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 邮件设置完成
	 * @return
	 */
	@Action("mail-settings-over")
	@LogInfo(optType="证书失效邮件提醒设置")
	public String mailSettingsOver(){
		try {
			GsmMailSettings gsmMailSettings = intransitedEquipmentManager.getGsmMailSettingsByBusinessCode(businessCode);
			ActionContext.getContext().getValueStack().push(gsmMailSettings==null?new GsmMailSettings():gsmMailSettings);
			
			personGridColumnInfo = ApiFactory.getMmsService().getGridColumnInfo("MEASUREMENT_EQUIPMENT_MAIL_SEND_USERS");
			JSONArray jsonArray = JSONArray.fromObject(personGridColumnInfo.getColModel());
			personGridColumnInfo.setColModel(jsonArray.toString());
			jsonArray = JSONArray.fromObject(personGridColumnInfo.getColNames());
			personGridColumnInfo.setColNames(jsonArray.toString());
		} catch (Exception e) {
			logger.error("在途量检具管理邮件设置完成失败", e);
			addActionError("在途量检具管理邮件设置完成失败"+e.getMessage());
			createErrorMessage("在途量检具管理邮件设置完成失败"+e.getMessage());
		}
		return SUCCESS;
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

	public GsmEquipment getIntransitedEquipment() {
		return intransitedEquipment;
	}

	public void setIntransitedEquipment(GsmEquipment intransitedEquipment) {
		this.intransitedEquipment = intransitedEquipment;
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

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public GridColumnInfo getPersonGridColumnInfo() {
		return personGridColumnInfo;
	}

	public void setPersonGridColumnInfo(GridColumnInfo personGridColumnInfo) {
		this.personGridColumnInfo = personGridColumnInfo;
	}

}

package com.ambition.gsm.inspectionplan.web; 
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.gsm.entity.GsmMailSendUsers;
import com.ambition.gsm.entity.GsmMailSetting;
import com.ambition.gsm.entity.GsmMailSettings;
import com.ambition.gsm.entity.InspectionPlan;
import com.ambition.gsm.equipment.service.GsmEquipmentManager;
import com.ambition.gsm.inspectionplan.service.InspectionPlanManager;
import com.ambition.gsm.inspectionplan.service.InspectionPlanThread;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.mms.base.DynamicColumnValues;
import com.norteksoft.mms.base.utils.view.DynamicColumnDefinition;
import com.norteksoft.mms.base.utils.view.GridColumnInfo;
import com.norteksoft.mms.form.enumeration.DataType;
import com.norteksoft.mms.form.enumeration.EditControlType;
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
 * 校准计划(ACTION)
 * @author 张顺治
 *
 */
@Namespace("/gsm/inspectionplan")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location ="/gsm/inspectionplan", type = "redirectAction") })
public class InspectionPalnAction extends  CrudActionSupport<InspectionPlan> {
	Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	private Long id;
	private String ids;
	private String deleteIds;
	private InspectionPlan inspectionPlan;
	private Page<InspectionPlan> page;
	private JSONObject params;
	
	private List<Option> listOption;//选项组集合
	private List<DynamicColumnDefinition> dynamicColumn = new ArrayList<DynamicColumnDefinition>();//动态列集合
	private String groupNames;//组列列名
	
	private GridColumnInfo personGridColumnInfo;
	private String businessCode;
	private String fileFileName;
    private String fileContentType;
    
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private InspectionPlanManager inspectionPlanManager;
	@Autowired
	private GsmEquipmentManager gsmEquipmentManager;
	
	/**
	 * 获取对象
	 */
	@Override
	public InspectionPlan getModel() {
		return inspectionPlan;
	}

	/**
	 * 预处理对象
	 */
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			inspectionPlan=new InspectionPlan();
			inspectionPlan.setCompanyId(ContextUtils.getCompanyId());
			inspectionPlan.setCreatedTime(new Date());
			inspectionPlan.setCreator(ContextUtils.getLoginName());
			inspectionPlan.setCreatorName(ContextUtils.getUserName());
			inspectionPlan.setModifiedTime(new Date());
			inspectionPlan.setModifier(ContextUtils.getLoginName());
			inspectionPlan.setModifierName(ContextUtils.getUserName());
			inspectionPlan.setBusinessUnitName(ContextUtils.getSubCompanyName());
			inspectionPlan.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			inspectionPlan=inspectionPlanManager.getInspectionPlan(id);
		}
	}
	
	/**
	 * 保存
	 */
	@Action("save")
	@LogInfo(optType="保存",message="校验计划")
	@Override
	public String save() throws Exception {	
		try{ 
			  inspectionPlanManager.saveInspectionPlan(inspectionPlan);
			  renderText(JsonParser.getRowValue(inspectionPlan));
			  //检定计划邮件提醒
			  InspectionPlanThread myThread = new InspectionPlanThread(inspectionPlan.getId().toString(),ContextUtils.getCompanyId(),inspectionPlanManager);
			  new Thread(myThread,"扫描检定计划，查看是否有超期或者要提醒的记录！").start();
			  createMessage("保存成功！");
		}catch(Exception e){
			logger.error("保存失败：", e);
		}
		return null;
	}
	
	/**
	 * 删除
	 */
	@Action("delete")
	@LogInfo(optType="删除",message="校验计划")
	@Override
	public String delete() throws Exception {
		try {
			inspectionPlanManager.deleteInspectionPlan(deleteIds);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}

	/**
	 * 列表
	 */
	@Action("list")
	@Override
	public String list() throws Exception {
		renderList();
		return SUCCESS;
	}
	/**
	 * 填充台账
	 * @param page
	 * @return
	 */
	public String PageUtilsDynamicPageToJson(Page<InspectionPlan> page){
		return PageUtils.dynamicPageToJson(page,new DynamicColumnValues(){
			public void addValuesTo(List<Map<String, Object>> result) {
				for(Map<String, Object> map : result){
					Long id = Long.valueOf(map.get("id").toString());
					InspectionPlan inspectionPlan = inspectionPlanManager.getInspectionPlan(id);
					List<Option> listOptions = ApiFactory.getSettingService().getOptionsByGroupCode("gsm_month");
					for (Option option : listOptions) {
						String value = option.getValue().replace(".","__");
						switch (Integer.valueOf(value)) {
						case 1:
							map.put("计划_1", inspectionPlan.getPlan1());
							map.put("执行_1", inspectionPlan.getDo1());
							break;
						case 2:
							map.put("计划_2", inspectionPlan.getPlan2());
							map.put("执行_2", inspectionPlan.getDo2());
							break;
						case 3:
							map.put("计划_3", inspectionPlan.getPlan3());
							map.put("执行_3", inspectionPlan.getDo3());
							break;
						case 4:
							map.put("计划_4", inspectionPlan.getPlan4());
							map.put("执行_4", inspectionPlan.getDo4());
							break;
						case 5:
							map.put("计划_5", inspectionPlan.getPlan5());
							map.put("执行_5", inspectionPlan.getDo5());
							break;
						case 6:
							map.put("计划_6", inspectionPlan.getPlan6());
							map.put("执行_6", inspectionPlan.getDo6());
							break;
						case 7:
							map.put("计划_7", inspectionPlan.getPlan7());
							map.put("执行_7", inspectionPlan.getDo7());
							break;
						case 8:
							map.put("计划_8", inspectionPlan.getPlan8());
							map.put("执行_8", inspectionPlan.getDo8());
							break;
						case 9:
							map.put("计划_9", inspectionPlan.getPlan9());
							map.put("执行_9", inspectionPlan.getDo9());
							break;
						case 10:
							map.put("计划_10", inspectionPlan.getPlan10());
							map.put("执行_10", inspectionPlan.getDo10());
							break;
						case 11:
							map.put("计划_11", inspectionPlan.getPlan11());
							map.put("执行_11", inspectionPlan.getDo11());
							break;
						case 12:
							map.put("计划_12", inspectionPlan.getPlan12());
							map.put("执行_12", inspectionPlan.getDo12());
							break;
						default:
							break;
						}
					}
				}
			}
		});
	}	
	/**
	 * 列表数据
	 * @return
	 * @throws Exception
	 */
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try {
			page = inspectionPlanManager.getPage(page, InspectionPlan.STATE_CHECk_OUT, "notin");
			this.renderText(PageUtilsDynamicPageToJson(page));
		} catch (Exception e) {
			logger.error("校验计划检索数据失败：", e);
		}
		return null;
	}

	/**
	 * 已完成列表
	 * @return
	 * @throws Exception
	 */
	@Action("list-over") 
	public String listOver() throws Exception {
		renderList();
		return SUCCESS;
	}
	
	/**
	 * 已完成列表数据
	 * @return
	 * @throws Exception
	 */
	@Action("list-over-datas")
	public String getlistOverDatas() throws Exception {
		try {
			page = inspectionPlanManager.getPage(page, InspectionPlan.STATE_CHECk_OUT, "in");
			this.renderText(PageUtilsDynamicPageToJson(page));
		} catch (Exception e) {
			logger.error("已完成校验计划检索数据失败：", e);
		}
		return null;
	}
	
	/**
	 * 表单页面
	 */
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}

	/**
	 * 导出
	 * @return
	 * @throws Exception
	 */
	@Action("export")
	@LogInfo(optType="导出",message="校验计划")
	public String export() throws Exception {
		Page<InspectionPlan> page = new Page<InspectionPlan>(100000);
		page = inspectionPlanManager.getPage(page, null, null);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MEASUREMENT_INSPECTION_PLAN"),"校验计划台账"));
		return null;
	}

	/**
	 * 送校
	 * @return
	 * @throws Exception
	 */
	@Action("test")
	@LogInfo(optType="送校",message="校验计划")
	public String frow() throws Exception {
		try {
			String bd= Struts2Utils.getParameter("type");
			inspectionPlanManager.test(ids,bd);
 			createMessage("送校成功");
		} catch (Exception e) {
			 logger.error("送校失败：", e);
 			 createErrorMessage("送校失败："+e.getMessage());
		}
		return null;
	} 
	
	/**
	 * 邮件提醒设置
	 * @return
	 */
	@Action("mail-settings")
	@LogInfo(optType="检定计划邮件提醒设置")
	public String mailSettings(){
		try {
			GsmMailSettings gsmMailSettings = inspectionPlanManager.getGsmMailSettingsByBusinessCode(businessCode);
			ActionContext.getContext().getValueStack().push(gsmMailSettings==null?new GsmMailSettings():gsmMailSettings);
			
			personGridColumnInfo = ApiFactory.getMmsService().getGridColumnInfo("MEASUREMENT_EQUIPMENT_MAIL_SEND_USERS");
			JSONArray jsonArray = JSONArray.fromObject(personGridColumnInfo.getColModel());
			personGridColumnInfo.setColModel(jsonArray.toString());
			jsonArray = JSONArray.fromObject(personGridColumnInfo.getColNames());
			personGridColumnInfo.setColNames(jsonArray.toString());
		} catch (Exception e) {
			logger.error(e);
		}
		return SUCCESS;
	}
	
	/**
	 * 预警人员列表数据
	 * @return
	 * @throws Exception
	 */
	@Action("warn-user-datas")
	public String getPersonDatas() throws Exception {
		try {
			if(businessCode != null){
				GsmMailSettings gsmMailSettings= inspectionPlanManager.getGsmMailSettingsByBusinessCode(businessCode);
				Page<GsmMailSendUsers> pageUsers = new Page<GsmMailSendUsers>(Integer.MAX_VALUE);
				pageUsers.setResult(gsmMailSettings.getGsmMailSendUserss());
				pageUsers.setPageNo(1);
				pageUsers.setTotalCount(pageUsers.getResult().size());
				pageUsers.setPageSize(Integer.MAX_VALUE);
				renderText(PageUtils.pageToJson(pageUsers,"MEASUREMENT_EQUIPMENT_MAIL_SEND_USERS"));
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
	/**
	 * 方法名: saveMailSettings 
	 * <p>功能说明：保存检定邮件提醒设置</p>
	 * @return String
	 * @throws
	 */
	@Action("save-mail-settings")
	@LogInfo(optType="修改",message="保存检定邮件提醒设置")
	public String saveMailSettings(){
		GsmMailSetting gsmMailSetting = new GsmMailSetting();
		gsmMailSetting.setAdvanceRemindSwitch(Boolean.valueOf(Struts2Utils.getParameter("advanceRemindSwitch")));
		gsmMailSetting.setAdvanceRemindTime(Float.valueOf(Struts2Utils.getParameter("advanceRemindTime")==null?"0":Struts2Utils.getParameter("advanceRemindTime")));
		gsmMailSetting.setDelayRemindSwitch(Boolean.valueOf(Struts2Utils.getParameter("delayRemindSwitch")));
		gsmMailSetting.setDelayRemindTime(Float.valueOf(Struts2Utils.getParameter("delayRemindTime")==null?"0":Struts2Utils.getParameter("delayRemindTime")));
		try {
			String resultIds = inspectionPlanManager.saveGsmMailSettings(gsmMailSetting,Struts2Utils.getParameter("selFlag"), page);
			//检定计划提前或者超期未提醒预警
			InspectionPlanThread myThread = new InspectionPlanThread(resultIds,ContextUtils.getCompanyId(),inspectionPlanManager);
			new Thread(myThread,"扫描检定计划，查看是否有超期或者要提醒的记录！").start();
			createMessage("设置成功!");
		} catch (Exception e) {
			createErrorMessage("保存失败:" + e.getMessage());
			log.error("保存检定邮件配置出错!",e);
		}
		return null;
	}
	
	/**
	 * 已完成邮件提醒设置
	 * @return
	 */
	@Action("mail-settings-over")
	@LogInfo(optType="证书失效邮件提醒设置")
	public String mailSettingsOver(){
		try {
			GsmMailSettings gsmMailSettings = inspectionPlanManager.getGsmMailSettingsByBusinessCode(businessCode);
			ActionContext.getContext().getValueStack().push(gsmMailSettings==null?new GsmMailSettings():gsmMailSettings);
			
			personGridColumnInfo = ApiFactory.getMmsService().getGridColumnInfo("MEASUREMENT_EQUIPMENT_MAIL_SEND_USERS");
			JSONArray jsonArray = JSONArray.fromObject(personGridColumnInfo.getColModel());
			personGridColumnInfo.setColModel(jsonArray.toString());
			jsonArray = JSONArray.fromObject(personGridColumnInfo.getColNames());
			personGridColumnInfo.setColNames(jsonArray.toString());
		} catch (Exception e) {
			logger.error(e);
		}
		return SUCCESS;
	}
	@Action("view-info")
	public String viewInfo() throws Exception {
		List<Option> measurementTypes = ApiFactory.getSettingService().getOptionsByGroupCode("measurement_measurementType");
		List<Option> checkForms = ApiFactory.getSettingService().getOptionsByGroupCode("measurement_checkForm");
		List<Option> precisionGrades = ApiFactory.getSettingService().getOptionsByGroupCode("measurement_precisionGrade");
		List<Option> managementTypes = ApiFactory.getSettingService().getOptionsByGroupCode("measurement_managementType");
		List<Option> testCycles = ApiFactory.getSettingService().getOptionsByGroupCode("measurement_testCycle");
		List<Option> measurementStates = ApiFactory.getSettingService().getOptionsByGroupCode("measurement_measurementState");
		ActionContext.getContext().put("measurementTypes", measurementTypes);
		ActionContext.getContext().put("checkForms", checkForms);
		ActionContext.getContext().put("precisionGrades", precisionGrades);
		ActionContext.getContext().put("managementTypes", managementTypes);
		ActionContext.getContext().put("testCycles", testCycles);
		ActionContext.getContext().put("measurementStates", measurementStates);
		if(id!=null){
			inspectionPlan=inspectionPlanManager.getInspectionPlan(id);
			System.out.print(inspectionPlan.getInspectionRecords().size());
			ActionContext.getContext().put("equipment", inspectionPlan.getGsmEquipment());
			ActionContext.getContext().put("id", id);
			ActionContext.getContext().put("inspectionRecords", inspectionPlan.getInspectionRecords());
	}
			return SUCCESS;
	}
	/**
	 * 渲染台账
	 */
	public void renderList(){
		//拼接列表
		StringBuilder colNamesByMonth = new StringBuilder("");
		List<Option> listOptions = ApiFactory.getSettingService().getOptionsByGroupCode("gsm_month");
		this.setListOption(listOptions);
		for(Option option : listOption){
			//拼接colName
			if(colNamesByMonth.length() > 0){
				colNamesByMonth.append(",");
			}
			String colName = "计划_" + option.getValue().replace(".","__");
			colNamesByMonth.append("{startColumnName: '" + colName + "', numberOfColumns: 2, titleText: '"+option.getName()+"'}");
			DynamicColumnDefinition dynamicFieldOptions =new DynamicColumnDefinition();
			dynamicFieldOptions.setColName("计划");
			dynamicFieldOptions.setName(colName);
			dynamicFieldOptions.setVisible(true);
			dynamicFieldOptions.setColWidth("50");
			dynamicFieldOptions.setEditable(false);
			//dynamicFieldOptions.setEditRules("number:true,min:0");
			dynamicFieldOptions.setEdittype(EditControlType.TEXT);
			dynamicFieldOptions.setType(DataType.TEXT);
			dynamicColumn.add(dynamicFieldOptions);

			colName = "执行_" + option.getValue().replace(".","__");
			dynamicFieldOptions =new DynamicColumnDefinition();
			dynamicFieldOptions.setColName("执行");
			dynamicFieldOptions.setName(colName);
			dynamicFieldOptions.setVisible(true);
			dynamicFieldOptions.setColWidth("50");
			dynamicFieldOptions.setEditable(false);
			//dynamicFieldOptions.setEditRules("number:true,min:0");
			dynamicFieldOptions.setEdittype(EditControlType.TEXT);
			dynamicFieldOptions.setType(DataType.TEXT);
			dynamicColumn.add(dynamicFieldOptions);
		}
		colNamesByMonth.insert(0,"[").append("]");
		this.setGroupNames(colNamesByMonth.toString());
	}
	/**
	 * 列表数据
	 */
	@Action("list-state")
	@LogInfo(optType="查询",message="查询数据")
	public String getListStates() throws Exception {
		String type = Struts2Utils.getParameter("type");
		try{
			if("N".equals(type)){
				page = inspectionPlanManager.listState(page, type);
			}
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			logger.error("查询失败!",e);
		} 
		return null;
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

	public InspectionPlan getInspectionPlan() {
		return inspectionPlan;
	}

	public void setInspectionPlan(InspectionPlan inspectionPlan) {
		this.inspectionPlan = inspectionPlan;
	}

	public Page<InspectionPlan> getPage() {
		return page;
	}

	public void setPage(Page<InspectionPlan> page) {
		this.page = page;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public List<Option> getListOption() {
		return listOption;
	}

	public void setListOption(List<Option> listOption) {
		this.listOption = listOption;
	}

	public List<DynamicColumnDefinition> getDynamicColumn() {
		return dynamicColumn;
	}

	public void setDynamicColumn(List<DynamicColumnDefinition> dynamicColumn) {
		this.dynamicColumn = dynamicColumn;
	}

	public String getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}

	public GridColumnInfo getPersonGridColumnInfo() {
		return personGridColumnInfo;
	}

	public void setPersonGridColumnInfo(GridColumnInfo personGridColumnInfo) {
		this.personGridColumnInfo = personGridColumnInfo;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}
		
}
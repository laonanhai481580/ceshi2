package com.ambition.gsm.inspectionmsaplan.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.ambition.gsm.entity.GsmMailSettings;
import com.ambition.gsm.entity.InspectionMsaplan;
import com.ambition.gsm.equipment.service.GsmEquipmentManager;
import com.ambition.gsm.inspectionmsaplan.service.InspectionMsaplanManager;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
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
 * MSA校验计划(ACTION)
 * @author 张顺治
 *
 */
@Namespace("/gsm/equipment-msaplan")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/gsm/equipment-msaplan", type = "redirectAction") })
public class InspectionMsaplanAction extends CrudActionSupport<InspectionMsaplan> {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	private Long id;
	private String ids;
	private String deleteIds;
	private InspectionMsaplan inspectionMsaplan;
	private Page<InspectionMsaplan> page;
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
	private InspectionMsaplanManager inspectionMsaplanManager;
	@Autowired
	private GsmEquipmentManager gsmEquipmentManager;
	
	/**
	 * 获取对象
	 */
	@Override
	public InspectionMsaplan getModel() {
		return inspectionMsaplan;
	}
	
	/**
	 * 预处理对象
	 */
	@Override
	protected void prepareModel() throws Exception {
		if (id == null) {
			inspectionMsaplan = new InspectionMsaplan();
			inspectionMsaplan.setCompanyId(ContextUtils.getCompanyId());
			inspectionMsaplan.setCreatedTime(new Date());
			inspectionMsaplan.setCreator(ContextUtils.getUserName());
			inspectionMsaplan.setBusinessUnitName(ContextUtils.getSubCompanyName());
			inspectionMsaplan.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
			inspectionMsaplan.setMsaState("待校验");
		} else {
			inspectionMsaplan = inspectionMsaplanManager.getInspectionMsaplan(id);
		}
	}

	/**
	 * 保存
	 */
	@Action("save")
	@LogInfo(optType="保存",message="量检具MSA计划")
	@Override
	public String save() throws Exception {	
		try{ 
			String attachment = Struts2Utils.getParameter("attachmentFiles");
			inspectionMsaplan.setAttachment(attachment);
			inspectionMsaplanManager.saveInspectionMsaplan(inspectionMsaplan);
			this.renderText(JsonParser.getRowValue(inspectionMsaplan));
		}catch(Exception e){
			logger.error("MSA校验保存失败：", e);
		}
		return null;
	}

	/**
	 * 删除
	 */
	@Action("delete")
	@LogInfo(optType="删除",message="量检具MSA计划")
	@Override
	public String delete() throws Exception {
		inspectionMsaplanManager.deleteInspectionPlan(deleteIds);
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
	public String getListOverDatas() throws Exception {
		try {
			page = inspectionMsaplanManager.getPage(page);
			this.renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			logger.error("MSA校验检索数据失败：", e);
		}
		return null;
	}

	/**
	 * 列表页面
	 */
	@Action("list")
	@Override
	public String list() throws Exception {
		renderList();
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
			page = inspectionMsaplanManager.getPage(page);
			this.renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			logger.error("MSA校验检索数据失败：", e);
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
	@LogInfo(optType="导出",message="量检具MSA计划")
	public String export() throws Exception {
		Page<InspectionMsaplan> page = new Page<InspectionMsaplan>(100000);
		page = inspectionMsaplanManager.getPage(page, null, null);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page, "MEASUREMENT_MSAINSPECTION_PLAN"),"MSA计划台账"));
		return null;
	}

	/**
	 * 送MSA校验
	 * @return
	 * @throws Exception
	 */
	@Action("sendMsa")
	@LogInfo(optType="送MSA校验",message="量检具MSA计划")
	public String sendMsa() throws Exception {
		try {
			inspectionMsaplanManager.sendMsa(ids);
 			createMessage("送校成功");
		} catch (Exception e) {
			logger.error("送校失败：", e);
 			createErrorMessage("送校失败："+e.getMessage());
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
			GsmMailSettings gsmMailSettings = inspectionMsaplanManager.getGsmMailSettingsByBusinessCode(businessCode);
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
				GsmMailSettings gsmMailSettings= inspectionMsaplanManager.getGsmMailSettingsByBusinessCode(businessCode);
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
	 * 已完成邮件提醒
	 * @return
	 */
	@Action("mail-settings-over")
	@LogInfo(optType="证书失效邮件提醒设置")
	public String mailSettingsOver(){
		try {
			GsmMailSettings gsmMailSettings = inspectionMsaplanManager.getGsmMailSettingsByBusinessCode(businessCode);
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

	public InspectionMsaplan getInspectionMsaplan() {
		return inspectionMsaplan;
	}

	public void setInspectionMsaplan(InspectionMsaplan inspectionMsaplan) {
		this.inspectionMsaplan = inspectionMsaplan;
	}

	public Page<InspectionMsaplan> getPage() {
		return page;
	}

	public void setPage(Page<InspectionMsaplan> page) {
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
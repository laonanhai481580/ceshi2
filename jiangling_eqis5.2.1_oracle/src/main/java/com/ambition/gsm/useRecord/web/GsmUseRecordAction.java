package com.ambition.gsm.useRecord.web;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.gsm.entity.GsmEquipment;
import com.ambition.gsm.entity.GsmMailSendUsers;
import com.ambition.gsm.entity.GsmMailSettings;
import com.ambition.gsm.entity.GsmUseRecord;
import com.ambition.gsm.equipment.service.GsmEquipmentManager;
import com.ambition.gsm.useRecord.service.GsmUseRecordManager;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.ibm.icu.text.SimpleDateFormat;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
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
 * 计量器具用户使用记录(ACTION)
 * @author 张顺治
 *
 */
@Namespace("/gsm/gsmUseRecord")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/gsm/gsmUseRecord", type = "redirectAction") })
public class GsmUseRecordAction extends CrudActionSupport<GsmUseRecord> {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private GsmUseRecord gsmUseRecord;
	private Page<GsmUseRecord> page; 
	private String params;
	private GsmEquipment gsmEquipment;
	private Page<GsmEquipment> gsmPage;
	private GridColumnInfo personGridColumnInfo;
	private String businessCode;
	
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private GsmUseRecordManager gsmUseRecordManager;
	@Autowired
	private GsmEquipmentManager gsmEquipmentManager;

	/**
	 * 获取对象
	 */
	@Override
	public GsmUseRecord getModel() {
		return gsmUseRecord;
	}

	/**
	 * 预处理对象
	 */
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			gsmUseRecord = new GsmUseRecord();
			gsmUseRecord.setCompanyId(ContextUtils.getCompanyId());
			gsmUseRecord.setCreatedTime(new Date());
			gsmUseRecord.setCreator(ContextUtils.getLoginName());
			gsmUseRecord.setCreatorName(ContextUtils.getUserName());
			gsmUseRecord.setModifiedTime(new Date());
			gsmUseRecord.setModifier(ContextUtils.getLoginName());
			gsmUseRecord.setModifierName(ContextUtils.getUserName());
			gsmUseRecord.setBusinessUnitName(ContextUtils.getSubCompanyName());
			gsmUseRecord.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			gsmUseRecord= gsmUseRecordManager.getGsmUseRecord(id);
		}
	}
	
	/**
	 * 删除对象
	 */
	@Action("delete")
	@Override
	public String delete() throws Exception {
		try {
			gsmUseRecordManager.deleteGsmUseRecord(deleteIds);
		} catch (Exception e) {
			logger.error("删除借还登记失败：", e);
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
	public String listDatas() throws Exception{
		try{
			page=gsmUseRecordManager.getPage(page);
			logUtilDao.debugLog("查询", "计量器具借还管理：计量器具借还台帐");
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			logger.error("计量器具借还管理：计量器具借还台帐", e);
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
		Page<GsmUseRecord> page = new Page<GsmUseRecord>(100000);
		page = gsmUseRecordManager.getPage(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MEASUREMENT_USE_RECORD"),"借还台账"));
		return null;
	}
 
	/**
	 * 借出页面
	 * @return
	 * @throws Exception
	 */
	@Action("borrow-input")
	public String borrowInput() throws Exception {
		List<Option> useDepts = ApiFactory.getSettingService().getOptionsByGroupCode("useDept");
		ActionContext.getContext().put("useDepts",useDepts);
		return SUCCESS;
	}
	
	/**
	 * 归还页面
	 * @return
	 * @throws Exception
	 */
	@Action("return-input")
	public String returnInput() throws Exception {
		List<Option> useDepts = ApiFactory.getSettingService().getOptionsByGroupCode("useDept");
		ActionContext.getContext().put("useDepts",useDepts);
		return SUCCESS;
	}
	
	/**
	 * 归还数据
	 * @return
	 * @throws Exception
	 */
	@Action("return-datas")
	public String getReturnDatas() throws Exception {
		try {
			String useDept = Struts2Utils.getParameter("useDept");
			if(useDept != null){
				gsmPage = gsmEquipmentManager.getPageByUseDept(gsmPage, useDept);
			}
			String result = gsmEquipmentManager.getResultJson(gsmPage);
			this.renderText(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 保存对象
	 */
	@Action("save")
	@Override
	public String save () throws Exception {
		JSONArray json = JSONArray.fromObject(params);
		String useDept = Struts2Utils.getParameter("useDept");
		String borrower = Struts2Utils.getParameter("borrower");
		String borrowDate = Struts2Utils.getParameter("borrowDate");
		String remark = Struts2Utils.getParameter("remark");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(int i=0;i<json.size();i++){
			GsmEquipment gsmEquipment = gsmEquipmentManager.getGsmEquipment(Long.valueOf(json.getJSONObject(i).get("id").toString()));
//			gsmEquipment.setUseDept(useDept);
			gsmEquipment.setMeasurementState(GsmEquipment.STATE_BORROW);
			gsmEquipmentManager.updateGsmEquipment(gsmEquipment);

			gsmUseRecord = new GsmUseRecord();
			gsmUseRecord.setCompanyId(ContextUtils.getCompanyId());
			gsmUseRecord.setCreatedTime(new Date());
			gsmUseRecord.setCreator(ContextUtils.getLoginName());
			gsmUseRecord.setCreatorName(ContextUtils.getUserName());
			gsmUseRecord.setModifiedTime(new Date());
			gsmUseRecord.setModifier(ContextUtils.getUserName());
			gsmUseRecord.setModifierName(ContextUtils.getUserName());
			gsmUseRecord.setDistributionPeople(ContextUtils.getUserName());
 			if(!useDept.isEmpty()){
				gsmUseRecord.setUseDept(useDept);
			}
			if(!borrowDate.isEmpty()){
				gsmUseRecord.setBorrowDate(sdf.parse(borrowDate));
			}
			if(!borrower.isEmpty()){
				gsmUseRecord.setBorrower(borrower);
			}
			if(json.getJSONObject(i).get("returnDate") != null && !"".equals(json.getJSONObject(i).get("returnDate").toString())){
				gsmUseRecord.setReturnDate(sdf.parse(json.getJSONObject(i).get("returnDate").toString()));
			}
			if(!remark.isEmpty()){
				gsmUseRecord.setBorrowedRemark(remark);
			} 
			gsmUseRecord.setMeasurementSerialNo(json.getJSONObject(i).get("measurementSerialNo").toString());
			gsmUseRecord.setMeasurementName(json.getJSONObject(i).get("measurementName").toString());
			gsmUseRecord.setMeasurementSpecification(json.getJSONObject(i).get("measurementSpecification").toString());
			
			try {
				gsmUseRecordManager.saveGsmUseRecord(gsmUseRecord);
				this.renderText(JsonParser.object2Json(gsmUseRecord));
			} catch (Exception e) {
				logger.error("借用登记失败：", e);
			}
		}
		return null;
	}

	/**
	 * 更新记录
	 * @return
	 * @throws Exception
	 */
	@Action("update-record")
	public String updateRecord() throws Exception {
		JSONArray json = JSONArray.fromObject(params);
		String remark = Struts2Utils.getParameter("remark"); 
		String returner = Struts2Utils.getParameter("returner");
		for(int i=0;i<json.size();i++){
			GsmEquipment gsmEquipment = gsmEquipmentManager.getGsmEquipment(Long.valueOf(json.getJSONObject(i).get("id").toString()));
//			gsmEquipment.setUseDept(null);
			gsmEquipment.setMeasurementState(GsmEquipment.STATE_DEFAULT_INSTOCK);
			gsmEquipmentManager.updateGsmEquipment(gsmEquipment);
			
			//一个量具多次被借
//			gsmUseRecord = gsmUseRecordManager.getGsmUseRecordByNo(gsmEquipment.getMeasurementSerialNo());
			//确认人
			gsmUseRecord.setBorrowedConfirmPeople(ContextUtils.getUserName());
			//归还日期
			gsmUseRecord.setRealReturnDate(new Date());
			//归还人
			gsmUseRecord.setReturner(returner);
			if(!remark.isEmpty()){
				gsmUseRecord.setBorrowedRemark(remark);
			}
			gsmUseRecord.setBorrowedConfirmPeople(ContextUtils.getUserName());
			try {
				gsmUseRecordManager.saveGsmUseRecord(gsmUseRecord);
				this.renderText(JsonParser.object2Json(gsmUseRecord));
			} catch (Exception e) {
				logger.error("归还登记失败：", e);
			}
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
			GsmMailSettings gsmMailSettings = gsmUseRecordManager.getGsmMailSettingsByBusinessCode(businessCode);
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
	 * 预警人员
	 * @return
	 * @throws Exception
	 */
	@Action("warn-user-datas")
	public String getPersonDatas() throws Exception {
		try {
			if(businessCode != null){
				GsmMailSettings gsmMailSettings= gsmUseRecordManager.getGsmMailSettingsByBusinessCode(businessCode);
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
	 * 已完成邮件设置
	 * @return
	 */
	@Action("mail-settings-over")
	@LogInfo(optType="证书失效邮件提醒设置")
	public String mailSettingsOver(){
		try {
			GsmMailSettings gsmMailSettings = gsmUseRecordManager.getGsmMailSettingsByBusinessCode(businessCode);
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

	public GsmUseRecord getGsmUseRecord() {
		return gsmUseRecord;
	}

	public void setGsmUseRecord(GsmUseRecord gsmUseRecord) {
		this.gsmUseRecord = gsmUseRecord;
	}

	public Page<GsmUseRecord> getPage() {
		return page;
	}

	public void setPage(Page<GsmUseRecord> page) {
		this.page = page;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public GsmEquipment getGsmEquipment() {
		return gsmEquipment;
	}

	public void setGsmEquipment(GsmEquipment gsmEquipment) {
		this.gsmEquipment = gsmEquipment;
	}

	public Page<GsmEquipment> getGsmPage() {
		return gsmPage;
	}

	public void setGsmPage(Page<GsmEquipment> gsmPage) {
		this.gsmPage = gsmPage;
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
	
}
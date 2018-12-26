package com.ambition.gsm.equipment.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.codeRules.service.GsmCodeRulesManager;
import com.ambition.gsm.codeSecRules.service.GsmCodeSecRulesManager;
import com.ambition.gsm.entity.GsmCodeRules;
import com.ambition.gsm.entity.GsmCodeSecRules;
import com.ambition.gsm.entity.GsmEquipment;
import com.ambition.gsm.entity.GsmForeignReport;
import com.ambition.gsm.entity.GsmInnerCheckReport;
import com.ambition.gsm.entity.GsmMailSendUsers;
import com.ambition.gsm.entity.GsmMailSettings;
import com.ambition.gsm.entity.InspectionMsaplan;
import com.ambition.gsm.entity.InspectionPlan;
import com.ambition.gsm.equipment.dao.GsmEquipmentDao;
import com.ambition.gsm.equipment.dao.GsmMailSettingsDao;
import com.ambition.gsm.equipmentMaintenance.dao.GsmEquipmentMaintenanceDao;
import com.ambition.gsm.inspectionmsaplan.dao.InspectionMsaplanDao;
import com.ambition.gsm.inspectionplan.dao.GsmForeignReportDao;
import com.ambition.gsm.inspectionplan.dao.GsmInnerCheckReportDao;
import com.ambition.gsm.inspectionplan.dao.InspectionPlanDao;
import com.ambition.gsm.inspectionplan.dao.InspectionWarnUserDao;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.mms.base.utils.view.ComboxValues;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.opensymphony.xwork2.ActionContext;
 
/**
 * 量检具管理(SERVICE)
 * @author 张顺治
 *
 */
@Service
@Transactional
public class GsmEquipmentManager implements ComboxValues{
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private GsmEquipmentDao gsmEquipmentDao;
	@Autowired
	private GsmCodeRulesManager gsmCodeRulesManager; 
	@Autowired
	private GsmCodeSecRulesManager gsmCodeSecRulesManager;
	@Autowired
	private InspectionPlanDao inspectionPlanDao;
	@Autowired
	private InspectionMsaplanDao inspectionMsaplanDao;
	@Autowired
	private GsmEquipmentMaintenanceDao gsmEquipmentMaintenanceDao;
	@Autowired
	private InspectionWarnUserDao inspectionWarnUserDao;
	@Autowired
	private GsmMailSettingsDao gsmMailSettingsDao;
	@Autowired
	private GsmForeignReportDao gsmForeignReportDao;
	@Autowired
	private GsmInnerCheckReportDao gsmInnerCheckReportDao;
	/**
	 * 获取对象
	 * @param id
	 * @return
	 */
	public GsmEquipment getGsmEquipment(Long id){
		return gsmEquipmentDao.get(id);
	}
	public List<GsmEquipment> listAll(){
		return gsmEquipmentDao.getAllEquipment();
	}
	/**
	 * 保存对象
	 * @param gsmEquipment
	 */
	public void saveGsmEquipment(GsmEquipment gsmEquipment) {
		//根据一级和二级自动生成量检具编号
//		String gsmCodeRulesId = gsmEquipment.getMeasurementType();
//		if(StringUtils.isNotEmpty(gsmCodeRulesId)){
//			GsmCodeRules gsmCodeRules = gsmCodeRulesManager.getGsmCodeRules(Long.valueOf(gsmCodeRulesId));//一级不包含二级
//			String newGsmCodeRules = gsmCodeRules.getTypeCode() + (StringUtils.isNotEmpty(gsmCodeRules.getConnector()) ? gsmCodeRules.getConnector() : "");
//			String gsmCodeSecRulesId = gsmEquipment.getSecondaryClassification();
//			if(StringUtils.isNotEmpty(gsmCodeSecRulesId)){
//				GsmCodeSecRules gsmCodeSecRules = gsmCodeSecRulesManager.getGsmCodeSecRules(Long.valueOf(gsmCodeSecRulesId));//二级包含一级
//				if(gsmCodeSecRules != null){
//					String typeCode = gsmCodeSecRules.getGsmCodeRules().getTypeCode();
//					String typeCodeConnector =gsmCodeSecRules.getGsmCodeRules().getConnector();
//					String secTypeCode = gsmCodeSecRules.getSecTypeCode();
//					String secTypeCodeConnector = gsmCodeSecRules.getConnector();
//					newGsmCodeRules = typeCode + (StringUtils.isNotEmpty(typeCodeConnector) ? typeCodeConnector : "") + 
//							          secTypeCode + (StringUtils.isNotEmpty(secTypeCodeConnector)?secTypeCodeConnector : "");
//				}
//			}
//			if(!newGsmCodeRules.equals(gsmEquipment.getGsmCodeRules())){
//				Integer serialNo = gsmEquipmentDao.getMaxSerialNoByGsmCodeRules(newGsmCodeRules)+1;
//				gsmEquipment.setMeasurementSerialNo(newGsmCodeRules + serialNo);
//				gsmEquipment.setGsmCodeRules(newGsmCodeRules);
//				gsmEquipment.setSerialNo(serialNo);
//			}
//		}
		if(isExistEquipment(gsmEquipment.getId(),gsmEquipment.getManagerAssets())){
			throw new RuntimeException("已存在相同的量检具管理编号!");
		}		
		gsmEquipmentDao.save(gsmEquipment);
	}
	//验证并保存记录
	private boolean isExistEquipment(Long id, String managerAssets) {
		String hql = "select count(*) from GsmEquipment d where d.companyId = ? and d.managerAssets = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(managerAssets);
		if(id != null){
			hql += "and d.id <> ?";
			params.add(id);
		}
		Query query = gsmEquipmentDao.getSession().createQuery(hql);
		for(int i = 0;i < params.size();i++){
			query.setParameter(i, params.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if(Integer.valueOf(list.get(0).toString()) > 0){
			return true;
		}else{
			return false;
		}
	}	
	/**
	 * 更新量检具信息
	 * @param gsmEquipment
	 */
	public void updateGsmEquipment(GsmEquipment gsmEquipment) {
		gsmEquipment.setModifiedTime(new Date());
		gsmEquipment.setModifier(ContextUtils.getLoginName());
		gsmEquipment.setModifierName(ContextUtils.getUserName());
		gsmEquipmentDao.save(gsmEquipment);
	}
	
	/**
	 * 删除单对象
	 * @param gsmEquipment
	 */
	public void deleteGsmEquipment(GsmEquipment gsmEquipment){
		gsmEquipmentDao.delete(gsmEquipment);
	}
	
	/**
	 * 删除多对象
	 * @param deleteIds
	 * @return
	 */
	public String deleteGsmEquipment(String deleteIds){
		String[] ids = deleteIds.split(",");
		StringBuffer equipmentNames = new StringBuffer(); 
		for(String id: ids){			
			GsmEquipment gsmEquipment = gsmEquipmentDao.get(Long.valueOf(id));
			List<InspectionPlan> inspectionPlans=gsmEquipment.getInspectionPlans();
				if(inspectionPlans!=null&&inspectionPlans.size()>0){
					equipmentNames.append(gsmEquipment.getEquipmentName());
					equipmentNames.append(",");
					continue;
				}
			List<InspectionMsaplan> inspectionMsaplans=gsmEquipment.getInspectionMsaplans();		
			if(inspectionMsaplans!=null&&inspectionMsaplans.size()>0){
				equipmentNames.append(gsmEquipment.getEquipmentName());
				equipmentNames.append(",");
				continue;
			}				
			gsmEquipmentDao.delete(Long.valueOf(id));
				logUtilDao.debugLog("删除", gsmEquipment.toString());
		}
		return equipmentNames.toString()==null?"":equipmentNames.toString();
	}
	
	/**
	 * 分页对象
	 * @param page
	 * @return
	 */
	public Page<GsmEquipment> getPageByGsmEquipment(Page<GsmEquipment>page,GsmEquipment gsmEquipment){
		return gsmEquipmentDao.getPageByGsmEquipment(page,gsmEquipment);
	}
	/**
	  * 方法名:根据ID 取到盘点相关数据
	  * <p>功能说明：</p>
	  * @return
	 */
	public Page<GsmEquipment> getPageOfInventory(Page<GsmEquipment>page){
		return gsmEquipmentDao.getPageOfInventory(page);
	}
	/**
	  * 方法名:查询需转移人确认的数据
	  * <p>功能说明：</p>
	  * @return
	 */
	public Page<GsmEquipment> getPageOfTransferConfirm(Page<GsmEquipment>page){
		return gsmEquipmentDao.getPageOfTransferConfirm(page);
	}
	/**
	  * 方法名:查询可转移责任人的数据
	  * <p>功能说明：</p>
	  * @return
	 */
	public Page<GsmEquipment> getPageInTransfer(Page<GsmEquipment>page){
		return gsmEquipmentDao.getPageInTransfer(page);
	}
	/**
	  * 方法名:转移台账数据
	  * <p>功能说明：</p>
	  * @return
	 */
	public Page<GsmEquipment> getPageOfTransferDatas(Page<GsmEquipment>page){
		return gsmEquipmentDao.getPageOfTransferDatas(page);
	}
    // 封装出货异常结果数据集的JSON格式
    public String getResultJson(Page<GsmEquipment> page) {
        List<JSONObject> list = new ArrayList<JSONObject>();
        int warnSize=0;
        for (GsmEquipment cp : page.getResult()) {
        	JSONObject json = JSONObject.fromObject(JsonParser.object2Json(cp));
        	Calendar cal= Calendar.getInstance();
        	if(cp.getWarnDates()!=null){
        		cal.set(Calendar.DAY_OF_MONTH,cp.getWarnDates());
        	}
        	Date warnDate=cal.getTime();
        	if(cp.getNextProofTime()!=null&&warnDate.getTime()>=cp.getNextProofTime().getTime()){
        		warnSize++;
        		json.put("isWarn", "是");
        	}else{
        		json.put("isWarn", "否");
        	}
            list.add(json);
        }
        // 添加jqGrid所需的页信息
        ActionContext.getContext().put("warnSize",warnSize);
        StringBuilder json = new StringBuilder();
        json.append("{\"page\":\"");
        json.append(page.getPageNo());
        json.append("\",\"total\":");
        json.append(page.getTotalPages());
        json.append(",\"records\":\"");
        json.append(page.getTotalCount());
        json.append("\",\"rows\":");
        json.append(JSONArray.fromObject(list).toString());
        json.append("}");
        return json.toString();
    }     
    
	
	/**
	 * 分页对象
	 * @param page
	 * @return
	 */
	public Page<GsmEquipment> getPageByUseDept(Page<GsmEquipment>page,String useDept){
		return gsmEquipmentDao.getPageByUseDept(page, useDept);
	}
	
	/**
	 * 集合对象
	 * @param gsmEquipment
	 * @return
	 */
	public List<GsmEquipment> getListByGsmEquipment(GsmEquipment gsmEquipment){
		return gsmEquipmentDao.getListByGsmEquipment(gsmEquipment);
	}
	
	/**
	 * 生成校验计划
	 * @param ids
	 * @throws Exception
	 */
	public void createEquipmentPlan(String ids) throws Exception{  
		for(String id : ids.split(",")){ 
			GsmEquipment gsmEquipment = gsmEquipmentDao.get(Long.valueOf(id));
			if(!(GsmEquipment.STATE_INUSE.equals(gsmEquipment.getMeasurementState()) || GsmEquipment.STATE_DEFAULT_INSTOCK.equals(gsmEquipment.getMeasurementState()))){
				throw new AmbFrameException("量具【" + gsmEquipment.getMeasurementState() + "】中,不能生成校验计划");
			}
			List<InspectionPlan> list=inspectionPlanDao.getInspectionPlanByGsm(gsmEquipment);
			if(list.size()>0){
				throw new AmbFrameException("设备：" + gsmEquipment.getEquipmentName() + "已经有计划存在，不能再次生成计划！");
			}
			InspectionPlan inspectionPlan = new InspectionPlan();
			inspectionPlan.setCompanyId(ContextUtils.getCompanyId());
			inspectionPlan.setCreatedTime(new Date());
			inspectionPlan.setCreator(ContextUtils.getLoginName());
			inspectionPlan.setCreatorName(ContextUtils.getUserName());
			inspectionPlan.setInspectionState(InspectionPlan.STATE_CHECk_WAIT);
			inspectionPlan.setYesOrNo(true);
			inspectionPlan.setAddress(gsmEquipment.getAddress());
			inspectionPlan.setProcessSection(gsmEquipment.getProcessSection());
			inspectionPlan.setEquipmentName(gsmEquipment.getEquipmentName());
			inspectionPlan.setEquipmentModel(gsmEquipment.getEquipmentModel());
			inspectionPlan.setCompanyMain(gsmEquipment.getCompanyMain());
			inspectionPlan.setDevName(gsmEquipment.getDevName());
			inspectionPlan.setWorkProducre(gsmEquipment.getWorkProducre());
			inspectionPlan.setDutyMan(gsmEquipment.getDutyMan());
			inspectionPlan.setDutyLoginMan(gsmEquipment.getDutyLoginMan());
			inspectionPlan.setManagerAssets(gsmEquipment.getManagerAssets());
			inspectionPlan.setManufacturer(gsmEquipment.getManufacturer());
			inspectionPlan.setCheckMethod(gsmEquipment.getCheckMethod());
			inspectionPlan.setFrequency(gsmEquipment.getFrequency());
			inspectionPlan.setInspectionPlanDate(new Date());
			inspectionPlan.setGsmEquipment(gsmEquipment);
			inspectionPlan.setGsmState(gsmEquipment.getGsmState());
			inspectionPlanDao.save(inspectionPlan);
			//更新量检具生成校验计划状态
//			gsmEquipment.setGenerateState(inspectionPlan.getInspectionState());
//			gsmEquipment.setNextProofTime(new Date());
//			gsmEquipmentDao.save(gsmEquipment);
		}
 	}
	/**
	 * 生成校验计划
	 * @param session 
	 * @param ids
	 * @throws Exception
	 */
	public InspectionPlan createEquipmentPlanZD(GsmEquipment gsmEquipment) throws Exception{  
		List<InspectionPlan> list=inspectionPlanDao.getInspectionPlanByGsm(gsmEquipment);
		if(list.size()==0){
			if(gsmEquipment.getMeasurementState().equals(GsmEquipment.STATE_INUSE)||gsmEquipment.getMeasurementState().equals(GsmEquipment.STATE_DEFAULT_INSTOCK)){
				InspectionPlan inspectionPlan = new InspectionPlan();
				inspectionPlan.setCompanyId(gsmEquipment.getCompanyId());
				inspectionPlan.setCreatedTime(new Date());
				inspectionPlan.setCreator(gsmEquipment.getCreator());
				inspectionPlan.setCreatorName(gsmEquipment.getCreatorName());
				inspectionPlan.setInspectionState(InspectionPlan.STATE_CHECk_WAIT);
				inspectionPlan.setYesOrNo(true);
				inspectionPlan.setAddress(gsmEquipment.getAddress());
				inspectionPlan.setProcessSection(gsmEquipment.getProcessSection());
				inspectionPlan.setEquipmentName(gsmEquipment.getEquipmentName());
				inspectionPlan.setEquipmentModel(gsmEquipment.getEquipmentModel());
				inspectionPlan.setCompanyMain(gsmEquipment.getCompanyMain());
				inspectionPlan.setDevName(gsmEquipment.getDevName());
				inspectionPlan.setWorkProducre(gsmEquipment.getWorkProducre());
				inspectionPlan.setDutyMan(gsmEquipment.getDutyMan());
				inspectionPlan.setDutyLoginMan(gsmEquipment.getDutyLoginMan());
				inspectionPlan.setManagerAssets(gsmEquipment.getManagerAssets());
				inspectionPlan.setManufacturer(gsmEquipment.getManufacturer());
				inspectionPlan.setCheckMethod(gsmEquipment.getCheckMethod());
				inspectionPlan.setFrequency(gsmEquipment.getFrequency());
				inspectionPlan.setInspectionPlanDate(new Date());
				inspectionPlan.setGsmEquipment(gsmEquipment);
				inspectionPlan.setGsmState(gsmEquipment.getGsmState());
				
	//			gsmEquipment.setNextProofTime(new Date());
				inspectionPlanDao.getSession().save(inspectionPlan);
				return inspectionPlan;
			}
		}
		return list.get(0);
	}
	/**
	 * 生成MSA计划
	 * @param ids
	 * @throws Exception
	 */
	public void createEquipmentmsaPlan(String ids) throws Exception{
		for (String id : ids.split(",")) { 
			GsmEquipment gsmEquipment = gsmEquipmentDao.get(Long.valueOf(id));
			
//			if(StringUtils.isNotEmpty(gsmEquipment.getGenerateMsaState())){
//				throw new AmbFrameException("已存在MSA校验计划，不能重复生成");
//			}
//			if(null == (gsmEquipment.getMsaCycle())){
//				 throw new AmbFrameException("MSA周期为空，不能生成MSA计划");  
//			}
			if(!(GsmEquipment.STATE_INUSE.equals(gsmEquipment.getMeasurementState()) || GsmEquipment.STATE_DEFAULT_INSTOCK.equals(gsmEquipment.getMeasurementState()))){ 
				throw new AmbFrameException("量检具状态【" + gsmEquipment.getMeasurementState() + "】不能生成MSA计划");
			}
			InspectionMsaplan inspectionMsaplan = new InspectionMsaplan();
			inspectionMsaplan.setCompanyId(ContextUtils.getCompanyId());
			inspectionMsaplan.setCreatedTime(new Date());
			inspectionMsaplan.setCreator(ContextUtils.getLoginName());
			inspectionMsaplan.setCreatorName(ContextUtils.getUserName());
			/*inspectionMsaplan.setMsaState(InspectionMsaplan.STATE_CHECK_WAIT);*/
			inspectionMsaplan.setGsmEquipment(gsmEquipment);
			//gsmEquipment.getInspectionMsaplans().add(inspectionMsaplan);
			inspectionMsaplanDao.save(inspectionMsaplan);
			//更新量检具生成MSA校验计划状态
//			gsmEquipment.setGenerateMsaState(inspectionMsaplan.getMsaState());
			//gsmEquipmentDao.save(gsmEquipment);
		}
	}
	
	/**
	 * 生成报修计划
	 * @param ids
	 * @throws Exception
	 */
	public void createEquipmentMaintenance(String ids) throws Exception {
		for(String id : ids.split(",")){
			GsmEquipment gsmEquipment = gsmEquipmentDao.get(Long.valueOf(id));
			
//			if(StringUtils.isNotEmpty(gsmEquipment.getSendState())){
//				throw new AmbFrameException("已报修，不能重复报修");
//			}
//			GsmEquipmentMaintenance gsmEquipmentMaintenanceOld=gsmEquipmentMaintenanceDao.getmaintenanceMaxDate(gsmEquipment.getMeasurementNo());
//			Long maintenanceCount = gsmEquipmentMaintenanceDao.countMaintenance(gsmEquipment.getMeasurementNo());
//			GsmEquipmentMaintenance gsmEquipmentMaintenance = new GsmEquipmentMaintenance();
//			gsmEquipmentMaintenance.setCompanyId(ContextUtils.getCompanyId());
//			gsmEquipmentMaintenance.setCreatedTime(new Date());
//			gsmEquipmentMaintenance.setCreator(ContextUtils.getLoginName());
//			gsmEquipmentMaintenance.setCreatorName(ContextUtils.getUserName());
//			gsmEquipmentMaintenance.setMaintenanceState(GsmEquipmentMaintenance.STATE_MAINTENANCE_WAIT);
//			gsmEquipmentMaintenance.setMaintenanceDate(new Date());
//			if(gsmEquipmentMaintenanceOld != null){
//				gsmEquipmentMaintenance.setLastedMaintenanceDate(gsmEquipmentMaintenanceOld.getRepairDate());
//			}
//			gsmEquipmentMaintenance.setSendTimes(maintenanceCount.intValue());
//			gsmEquipmentMaintenance.setGsmEquipment(gsmEquipment);
//			gsmEquipmentMaintenanceDao.save(gsmEquipmentMaintenance);
//			//更新量检具生成MSA校验计划状态
//			gsmEquipment.setSendState(gsmEquipmentMaintenance.getMaintenanceState());
			gsmEquipmentDao.save(gsmEquipment);
		}
	}
	
 
	/**
	 * 方法名: saveGsmMailSettings 
	 * <p>功能说明：保存检定邮件提醒规则</p>
	 * @return void
	 * @throws
	 */
	public String saveGsmMailSettings(GsmMailSettings gsmMailSetting,String selFlag,Page<GsmEquipment> page){
		
		String resultIds="";
		List<GsmEquipment> gsmEquipments = null;
		if(selFlag!=null && selFlag.equals("select")){
			gsmEquipments = new ArrayList<GsmEquipment>();
			String[] ids = Struts2Utils.getParameter("ids").split(",");
			for(String id:ids){
				if(StringUtils.isNotEmpty(id)){
					gsmEquipments.add(gsmEquipmentDao.get(Long.valueOf(id)));
				}
			}
		}else{
			page.setPageNo(1);
			page.setPageSize(Integer.MAX_VALUE);
			page = getPageByGsmEquipment(page, null);
			gsmEquipments = page.getResult();
		}
		
		for(GsmEquipment gsmEquipment :gsmEquipments){ 
			if(Struts2Utils.getParameter("personStrs")!=null && !Struts2Utils.getParameter("personStrs").equals("")){
				String personStrs = Struts2Utils.getParameter("personStrs");
				//JSONArray personArray = null;
				if(StringUtils.isNotEmpty(personStrs)){
					//personArray = JSONArray.fromObject(personStrs);
//					if(personArray.size()!=0){
//						//删除历史的预警人员数据
//						for(InspectionWarnUser inspectionWarnUser:gsmEquipment.getInspectionWarnUsers()){
//							inspectionWarnUserDao.delete(inspectionWarnUser);
//						} 
//						gsmEquipment.setInspectionWarnUsers(new ArrayList<InspectionWarnUser>());
//						for(int i = 0;i<personArray.size();i++){
//							InspectionWarnUser user = new InspectionWarnUser();
//							user.setCompanyId(ContextUtils.getCompanyId());
//							user.setCreatedTime(new Date());
//							user.setCreator(ContextUtils.getUserName());
//							user.setModifiedTime(new Date());
//							user.setModifier(ContextUtils.getUserName());
//							user.setName(personArray.getJSONObject(i).get("name").toString());
//							user.setUserId(personArray.getJSONObject(i).get("userId").toString());
//							user.setGsmEquipment(gsmEquipment);
//							gsmEquipment.getInspectionWarnUsers().add(user);
//						}
//					}else{
//						gsmEquipment.getInspectionWarnUsers().clear();
//					}
				}
			}   
			gsmEquipmentDao.save(gsmEquipment);
			resultIds += gsmEquipment.getId().toString()+",";
		} 
		return resultIds;
	}
	
	public GsmMailSettings getGsmMailSettingsByBusinessCode(String businessCode){
		return gsmMailSettingsDao.getByBusinessCode(businessCode);
	}
	
	public void saveMailSetting(GsmMailSettings gsmMailSettings){
		String businessCode = Struts2Utils.getParameter("businessCode");
		String enabled = Struts2Utils.getParameter("enabled");
		String days = Struts2Utils.getParameter("days");
		String usesStr = Struts2Utils.getParameter("usesStr");
		
		if(gsmMailSettings==null){
			gsmMailSettings = new GsmMailSettings();
			gsmMailSettings.setCreatedTime(new Date());
			gsmMailSettings.setCreator(ContextUtils.getUserName());
			gsmMailSettings.setBusinessCode(businessCode);
		}else{
			gsmMailSettings.setModifiedTime(new Date());
			gsmMailSettings.setModifier(ContextUtils.getUserName());
		}
		gsmMailSettings.setEnabled((enabled!=null&&!enabled.equals(""))?Boolean.valueOf(enabled):false);
		gsmMailSettings.setDays((days!=null&&!days.equals("")?Integer.valueOf(days):0));
		
		
		if(usesStr!=null && !usesStr.equals("")){
			JSONArray personArray = null;
			if(StringUtils.isNotEmpty(usesStr)){
				personArray = JSONArray.fromObject(usesStr);
				//删除历史的预警人员数据
				if(gsmMailSettings.getGsmMailSendUserss()!=null){
					gsmMailSettings.getGsmMailSendUserss().clear();
				}else{
					gsmMailSettings.setGsmMailSendUserss(new ArrayList<GsmMailSendUsers>()); 
				}
				
				for(int i = 0;i<personArray.size();i++){
					GsmMailSendUsers user = new GsmMailSendUsers();
					user.setCompanyId(ContextUtils.getCompanyId());
					user.setCreatedTime(new Date());
					user.setCreator(ContextUtils.getUserName());
					user.setModifiedTime(new Date());
					user.setModifier(ContextUtils.getUserName());
					user.setUserName(personArray.getJSONObject(i).get("userName").toString());
					user.setUserLoginName(personArray.getJSONObject(i).get("userLoginName").toString());
					user.setGsmMailSettings(gsmMailSettings);
					gsmMailSettings.getGsmMailSendUserss().add(user);
				}
			}
			gsmMailSettingsDao.save(gsmMailSettings);
		}
	}
	
	/**
	 * 导入量检具信息
	 * @param file
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	/*public String importGsmEquipment(File file,String fileName) throws Exception{
		InputStream inStream=new FileInputStream(file);
		Workbook workbook=WorkbookFactory.create(inStream) ;
		int totalSheets=workbook.getNumberOfSheets();
		Map<String,Cell> cellMap = new HashMap<String, Cell>();
		for(int i=0;i<totalSheets;i++){
			Sheet sheet=workbook.getSheetAt(i);
			if(workbook.isSheetHidden(i)){
				continue;
			}
			Row row = sheet.getRow(0);
			if(row == null){
				continue;
			}
			cellMap.clear();
			Iterator<Row> iterator=sheet.rowIterator();
			while(iterator.hasNext()){
				row = iterator.next();
				Iterator<Cell> cellIterator=row.cellIterator();
				Cell cell=null;
				while(cellIterator.hasNext()){
					cell = cellIterator.next();
					Object value = ExcelUtil.getCellValue(cell);
					if(value != null){
						String key = value.toString().replaceAll("\\n","").replaceAll(" ","").replaceAll("　","");
						if(!cellMap.containsKey(key)){
							cellMap.put(key,cell);
						}
					}
				}
			}
			Cell itemItitleCell = cellMap.get("公司主体");
			Integer itemTitleRowNum = itemItitleCell.getRow().getRowNum();
			while(iterator.hasNext()){
				row = iterator.next();
				if(row.getRowNum() <= itemTitleRowNum){
					continue;
				}
				Cell cell = row.getCell(itemItitleCell.getColumnIndex());
				if(cell == null){
					continue;
				}
				 String companyMain,businessName, devName,workProducre,address, dutyMan,dutyLoginMan,copyMan;//抄送人
				 String copyLoginMan,fixedAssets,managerAssets,equipmentName,equipmentModel,measuringRange;//测量范围
				 String accuracy,manufacturer,factoryNumber,checkMethod,equipmentLevel;//分级
				 String frequency,measurementState;//量具状态 
				 Date purchaseTime = null;
				 companyMain=(ExcelUtil.getCellValue(cell)+"").trim();
				 cell= cellMap.get("所属事业部");
				 businessName=ExcelUtil.getCellValue(cell).toString().trim();
				 cell= cellMap.get("使用部门");
				 if(cell==null){
						throw new AmbFrameException("SHEET"+i+"资料格式不正确!使用部门未填写!&nbsp;&nbsp;</br>");
					}
				 devName=ExcelUtil.getCellValue(cell).toString().trim();
				 cell= cellMap.get("工序");
				 if(cell==null){
						throw new AmbFrameException("SHEET"+i+"资料格式不正确!工序未填写!&nbsp;&nbsp;</br>");
					}
				 workProducre=ExcelUtil.getCellValue(cell).toString().trim();
				 cell= cellMap.get("安装地址");
				 if(cell==null){
						throw new AmbFrameException("SHEET"+i+"资料格式不正确!安装地址未填写!&nbsp;&nbsp;</br>");
					}
				 address=ExcelUtil.getCellValue(cell).toString().trim();
				 cell= cellMap.get("责任人");
				 if(cell==null){
						throw new AmbFrameException("SHEET"+i+"资料格式不正确!责任人未填写!&nbsp;&nbsp;</br>");
					}
				 dutyMan=ExcelUtil.getCellValue(cell).toString().trim();
				 cell= cellMap.get("抄送人");
				 if(cell==null){
						throw new AmbFrameException("SHEET"+i+"资料格式不正确!抄送人未填写!&nbsp;&nbsp;</br>");
					}
				 copyMan=ExcelUtil.getCellValue(cell).toString().trim();
				 cell= cellMap.get("固定资产编号");
				 if(cell==null){
						throw new AmbFrameException("SHEET"+i+"资料格式不正确!固定资产编号未填写!&nbsp;&nbsp;</br>");
				}
				 fixedAssets=ExcelUtil.getCellValue(cell).toString().trim();
				 cell= cellMap.get("管理编号");
				 managerAssets=ExcelUtil.getCellValue(cell).toString().trim();
				 cell= cellMap.get("设备名称");
				 equipmentName=ExcelUtil.getCellValue(cell).toString().trim();
				 cell= cellMap.get("规格型号");
				 equipmentModel=ExcelUtil.getCellValue(cell).toString().trim();
				 cell= cellMap.get("测量范围");
				 measuringRange=ExcelUtil.getCellValue(cell).toString().trim();
				 cell= cellMap.get("精度/分度");
				 accuracy=ExcelUtil.getCellValue(cell).toString().trim();
				 cell= cellMap.get("制造商");
				 manufacturer=ExcelUtil.getCellValue(cell).toString().trim();
				 cell= cellMap.get("机身号");
				 factoryNumber=ExcelUtil.getCellValue(cell).toString().trim();
				 cell= cellMap.get("校准方式");
				 checkMethod=ExcelUtil.getCellValue(cell).toString().trim();
				 cell= cellMap.get("分级");
				 equipmentLevel=ExcelUtil.getCellValue(cell).toString().trim();
				 cell= cellMap.get("频率");
				 frequency=ExcelUtil.getCellValue(cell).toString().trim();
				 cell= cellMap.get("采购日期");
				 if(cell!=null){
					 short format = cell.getCellStyle().getDataFormat();  
				     SimpleDateFormat sdf = null;  
				     if(format == 14 || format == 31 || format == 57 || format == 58){  
				        //日期  
				        sdf = new SimpleDateFormat("yyyy-MM-dd");  
				     }else if (format == 20 || format == 32) {  
				        //时间  
				        sdf = new SimpleDateFormat("HH:mm");  
				     }  
				     double value = cell.getNumericCellValue();  
				     purchaseTime = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);  
				 }
				 cell= cellMap.get("盘点状态");
				 measurementState=ExcelUtil.getCellValue(cell).toString().trim();
				 GsmEquipment ge=new GsmEquipment();
				 ge.setCreatedTime(new Date());
				 ge.setCompanyMain(companyMain);
				 ge.setCreator(ContextUtils.getUserName());
				 ge.setCompanyId(ContextUtils.getCompanyId());
				 ge.setBusinessUnitName(businessName);
				 ge.setDevName(devName);
				 ge.setWorkProducre(workProducre);
				 ge.setAddress(address);
				 ge.setDutyMan(dutyMan);
				 ge.setCopyMan(copyMan);
				 ge.setFixedAssets(fixedAssets);
				 ge.setManagerAssets(managerAssets);
				 ge.setEquipmentName(equipmentName);
				 ge.setEquipmentModel(equipmentModel);
				 ge.setMeasuringRange(measuringRange);ge.setAccuracy(accuracy);ge.setAccuracy(accuracy);
				 ge.setManufacturer(manufacturer);ge.setFactoryNumber(factoryNumber);ge.setCheckMethod(checkMethod);
				 ge.setEquipmentLevel(equipmentLevel);ge.setFrequency(frequency);ge.setPurchaseTime(purchaseTime);
				 ge.setMeasurementState(GsmEquipment.STATE_DEFAULT_INSTOCK);
				 gsmEquipmentDao.save(ge);
				 
			}
			
		}
		return null;
	}*/
	/**
	  * 方法名:获取字段映射 
	  * <p>功能说明：</p>
	  * @return
	 */
	public Map<String,String> getFieldMap(String listCode){
		Map<String,String> fieldMap = new HashMap<String, String>();
		ListView columns = ApiFactory.getMmsService().getListViewByCode(listCode);
		for(ListColumn column: columns.getColumns()){
			if(column.getVisible()){
				fieldMap.put(column.getHeaderName(), column.getTableColumn().getName());
			}
		}
		return fieldMap;
	}
	/**
	 * 导入台帐数据
	 * @param file
	 * @param parent
	 * @throws Exception
	 */
	public String importGsmEquipment(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		//表单字段
		Map<String,String> fieldMap = this.getFieldMap("GSM_EQUIPMENT");
		Workbook book = WorkbookFactory.create(new FileInputStream(file));
		
		Sheet sheet = book.getSheetAt(0);
		Row row = sheet.getRow(0);
		if(row == null){
			throw new RuntimeException("第一行不能为空!");
		}
		
		Map<String,Integer> columnMap = new HashMap<String,Integer>();
		for(int i=0;;i++){
			Cell cell = row.getCell(i);
			if(cell==null){
				break;
			}
			String value = cell.getStringCellValue();
			if(fieldMap.containsKey(value)){
				columnMap.put(value,i);
			}
		}
		/*if(columnMap.keySet().size() != fieldMap.keySet().size()){
			throw new AmbFrameException("Excel格式不正确!");
		}*/
		
		DecimalFormat df = new DecimalFormat("#.##");
		Iterator<Row> rows = sheet.rowIterator();
		rows.next();//标题行
		
		int i = 0;
		while(rows.hasNext()){
			row = rows.next();
			try {
				int k = 0;
				Map<String,Object> objMap = new HashMap<String, Object>();
				for(String columnName : columnMap.keySet()){
					Cell cell = row.getCell(columnMap.get(columnName));
					if(cell != null){
						Object value = null;
						if(Cell.CELL_TYPE_STRING == cell.getCellType()){
							value = cell.getStringCellValue();
						}else if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								value = cell.getDateCellValue();
							} else {
								value = df.format(cell.getNumericCellValue());
							}
						}else if(Cell.CELL_TYPE_FORMULA == cell.getCellType()){
							value = cell.getCellFormula();
						}
						objMap.put(fieldMap.get(columnName),value);
					}
				}
				if(k == 1){
					i++;
					continue;
				}
				GsmEquipment gsmEquipment = null;
				String managerAssets = (String)objMap.get("managerAssets");
				if(managerAssets==null||managerAssets.equals("")){
					 sb.append("管理编号不能为空");
					 break;
				}
				List<GsmEquipment> list = gsmEquipmentDao.getGsmEquipmentByCode(managerAssets);//判断是否已经存在
				boolean isNew = false;
				if(list.isEmpty()){
					gsmEquipment = new GsmEquipment();
					gsmEquipment.setCompanyId(ContextUtils.getCompanyId());
					gsmEquipment.setCreatedTime(new Date());
					gsmEquipment.setCreator(ContextUtils.getUserName());
					isNew = true;
				}else{
					gsmEquipment = (GsmEquipment)list.get(0);
				}
				gsmEquipment.setMeasurementState(GsmEquipment.STATE_DEFAULT_INSTOCK);
				gsmEquipment.setModifiedTime(new Date());
				gsmEquipment.setModifier(ContextUtils.getUserName());
				gsmEquipment.setGsmState("N");
				for(String key : objMap.keySet()){
					CommonUtil1.setProperty(gsmEquipment,key, objMap.get(key));
				}				
			   this.saveGsmEquipment(gsmEquipment);
			   if(isNew){
				   sb.append("第" + (i+1) + "行保存成功!<br/>");
			   }else{
				   sb.append("第" + (i+1) + "行更新成功!<br/>");
			   }
			} catch (Exception e) {
				sb.append("第" + (i+1) + "行保存失败:<font color=red>" + e.getMessage() + "</font><br/>");
			}
			i++;
		}
		file.delete();
		return sb.toString();
	}
	
	@Override
	public Map<String, String> getValues(Object arg0) {
		Map<String, String> map = new HashMap<String, String>();
		StringBuilder stringBuilder1 = new StringBuilder("");
		StringBuilder stringBuilder2 = new StringBuilder("");
		List<GsmCodeRules> gsmCodeRuless = gsmCodeRulesManager.getGsmCodeRules();
		for (GsmCodeRules gsmCodeRules: gsmCodeRuless) {
			if(stringBuilder1.length() > 0){
				stringBuilder1.append(",");
			}
			stringBuilder1.append("'"+gsmCodeRules.getId()+"':'"+gsmCodeRules.getMeasurementType()+"'");
			List<GsmCodeSecRules> gsmCodeSecRuless = gsmCodeRules.getGsmCodeSecRuless();
			for (GsmCodeSecRules gsmCodeSecRules: gsmCodeSecRuless) {
				if(stringBuilder2.length() > 0){
					stringBuilder2.append(",");
				}
				stringBuilder2.append("'"+gsmCodeSecRules.getId()+"':'"+gsmCodeSecRules.getSecondaryClassification()+"'");
			}
		}
		map.put("measurementType", stringBuilder1.toString());
		map.put("secondaryClassification", stringBuilder2.toString());
		return map;
	}
	public Page<GsmEquipment> searchPageByManagerAssets(Page<GsmEquipment> page,String managerAssets) {
		return gsmEquipmentDao.searchPageByHql(page, "from GsmEquipment c where c.managerAssets=? and c.companyId=?", managerAssets,ContextUtils.getCompanyId());
	}
	public String selectMaxManagerAssets(String managerAssets) {
		String hql = " from GsmEquipment g where g.managerAssets like '"+managerAssets+"%' order by managerAssets desc";
		List<GsmEquipment> list = gsmEquipmentDao.find(hql);
		if(list.size()==0){
			return null;
		}else{
			return list.get(0).getManagerAssets();
		}
	}
	public Page<GsmEquipment> listState(Page<GsmEquipment> page,String state){
		String hql = " from GsmEquipment e where e.gsmState=?";
		return gsmEquipmentDao.searchPageByHql(page, hql, state);
	}
	public void gsmHide(String hideId,String type){
		String[] ids = hideId.split(",");
		for(String id : ids){
			GsmEquipment gsmEquipment = gsmEquipmentDao.get(Long.valueOf(id));
	//		Long a = gsmEquipment.getInspectionPlans().get(0);
			InspectionPlan inspectionPlan = null;
			GsmForeignReport gsmForeignReport = null;
			GsmInnerCheckReport gsmInnerCheckReport = null;
			if(gsmEquipment!=null){
				if(gsmEquipment.getInspectionPlans().size()!=0){
					inspectionPlan = gsmEquipment.getInspectionPlans().get(0);
					inspectionPlan.getCheckMethod();
					if("外校".equals(inspectionPlan.getCheckMethod())){
						gsmForeignReport = gsmForeignReportDao.listPlan(inspectionPlan.getId().toString());
					}
					if("内校".equals(inspectionPlan.getCheckMethod())){
						gsmInnerCheckReport = gsmInnerCheckReportDao.listPlan(inspectionPlan.getId().toString());
					}
				}
			}
			if("Y".equals(type)){
				gsmEquipment.setGsmState("N");
				if(inspectionPlan!=null){
					inspectionPlan.setGsmState("N");
					if(gsmForeignReport != null){
						gsmForeignReport.setGsmState("N");
					}
					if(gsmInnerCheckReport != null){
						gsmInnerCheckReport.setGsmState("N");
					}
				}
			}else{
				gsmEquipment.setGsmState("Y");
				if(inspectionPlan!=null){
					inspectionPlan.setGsmState("Y");
					if(gsmForeignReport != null){
						gsmForeignReport.setGsmState("Y");
					}
					if(gsmInnerCheckReport != null){
						gsmInnerCheckReport.setGsmState("Y");
					}
				}
			}
			gsmEquipmentDao.getSession().save(gsmEquipment);
			if(inspectionPlan != null){
				gsmEquipmentDao.getSession().save(inspectionPlan);
			}
			if(gsmForeignReport != null){
				gsmEquipmentDao.getSession().save(gsmForeignReport);
			}
			if(gsmInnerCheckReport != null){
				gsmEquipmentDao.getSession().save(gsmInnerCheckReport);
			}
		}
	}
	 
}
package com.ambition.gsm.intransitedequipment.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.codeRules.service.GsmCodeRulesManager;
import com.ambition.gsm.codeSecRules.service.GsmCodeSecRulesManager;
import com.ambition.gsm.entity.GsmCodeRules;
import com.ambition.gsm.entity.GsmCodeSecRules;
import com.ambition.gsm.entity.GsmEquipment;
import com.ambition.gsm.entity.GsmMailSendUsers;
import com.ambition.gsm.entity.GsmMailSettings;
import com.ambition.gsm.equipment.dao.GsmMailSettingsDao;
import com.ambition.gsm.intransitedequipment.dao.IntransitedEquipmentDao;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.product.util.ContextUtils;

/**
 * 在途量检具管理(DAO)
 * @author 张顺治
 *
 */
@Service
@Transactional
public class IntransitedEquipmentManager {
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private IntransitedEquipmentDao intransitedEquipmentDao;
	@Autowired
	private GsmCodeRulesManager gsmCodeRulesManager;
	@Autowired
	private GsmCodeSecRulesManager gsmCodeSecRulesManager;
	@Autowired
	private GsmMailSettingsDao gsmMailSettingsDao;
	
	/**
	 * 获取对象
	 * @param id
	 * @return
	 */
	public GsmEquipment getIntransitedEquipment(Long id){
		return intransitedEquipmentDao.get(id);
	}

	/**
	 * 保存对象
	 * @param intransitedEquipment
	 * @throws Exception
	 */
	public void saveIntransitedEquipment(GsmEquipment intransitedEquipment){
		//根据一级和二级自动生成量检具编号
//		String gsmCodeRulesId = intransitedEquipment.getMeasurementType();
//		if(StringUtils.isNotEmpty(gsmCodeRulesId)){
//			GsmCodeRules gsmCodeRules = gsmCodeRulesManager.getGsmCodeRules(Long.valueOf(gsmCodeRulesId));//一级不包含二级
//			String newGsmCodeRules = gsmCodeRules.getTypeCode() + (StringUtils.isNotEmpty(gsmCodeRules.getConnector()) ? gsmCodeRules.getConnector() : "");
//			String gsmCodeSecRulesId = intransitedEquipment.getSecondaryClassification();
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
//			if(!newGsmCodeRules.equals(intransitedEquipment.getGsmCodeRules())){
//				Integer serialNo = intransitedEquipmentDao.getMaxSerialNoByGsmCodeRules(newGsmCodeRules)+1;
//				intransitedEquipment.setMeasurementSerialNo(newGsmCodeRules + serialNo);
//				intransitedEquipment.setGsmCodeRules(newGsmCodeRules);
//				intransitedEquipment.setSerialNo(serialNo);
//			}
//		}
		intransitedEquipmentDao.save(intransitedEquipment);
    }
	
	/**
	 * 删除对象
	 * @param intransitedEquipment
	 * @throws Exception
	 */
	public void deleteIntransitedEquipment(GsmEquipment intransitedEquipment){
		intransitedEquipmentDao.delete(intransitedEquipment); 
	}
	
	/**
	 * 删除多对象
	 * @param deleteIds
	 * @return
	 */
	public String deleteIntransitedEquipment(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id: ids){
			GsmEquipment intransitedEquipment = intransitedEquipmentDao.get(Long.valueOf(id));
			intransitedEquipmentDao.delete(intransitedEquipment);
		}
		return "共"+ids.length+"条删除成功！";
	}
	
	/**
	 * 根据对象获取对象 
	 * @param measurementType
	 * @return
	 */
	public List<GsmEquipment> getIntransitedEquipment(GsmEquipment intransitedEquipment){
		 return intransitedEquipmentDao.getIntransitedEquipments(intransitedEquipment);
	}
	
	/**
	 * 复制对象
	 * @param ids
	 * @throws Exception
	 */
	public void createIntransitedEquipment(String ids) throws Exception{
		for (String id : ids.split(",")) {
			GsmEquipment gsmEquipment = getIntransitedEquipment(Long.valueOf(id));// 获得要复制的内容 
			GsmEquipment gEquipment = new GsmEquipment(); 
//			//sagEquipment.setMeasurementNo(formCodeGenerated.generateMeasurementNoCode());
//			gEquipment.setState(gsmEquipment.getState());
//			gEquipment.setMeasurementSerialNo(gsmEquipment.getMeasurementSerialNo());
//			gEquipment.setGsmCodeRules(gsmEquipment.getGsmCodeRules());
//			gEquipment.setSerialNo(gsmEquipment.getSerialNo());
//			gEquipment.setMeasurementName(gsmEquipment.getMeasurementName());
//			gEquipment.setTypeAndClassificationNO(gsmEquipment.getTypeAndClassificationNO());
//			gEquipment.setMeasurementType(gsmEquipment.getMeasurementType());
//			gEquipment.setSecondaryClassification(gsmEquipment.getSecondaryClassification());
//			gEquipment.setCheckForm(gsmEquipment.getCheckForm());
//			gEquipment.setIsPlan(gsmEquipment.getIsPlan());
//			gEquipment.setMeasurementSpecification(gsmEquipment.getMeasurementSpecification());
//			gEquipment.setManufacturer(gsmEquipment.getManufacturer());
//			gEquipment.setUseDept(gsmEquipment.getUseDept());
//			gEquipment.setTestCycle(gsmEquipment.getTestCycle());
//			gEquipment.setSpecialTestCycle(gsmEquipment.getSpecialTestCycle());
//			gEquipment.setMsaPlan(gsmEquipment.getMsaPlan());
//			gEquipment.setFactoryNumber(gsmEquipment.getFactoryNumber());
//			gEquipment.setMsaCycle(gsmEquipment.getMsaCycle());
//			gEquipment.setMeasurementState(gsmEquipment.getMeasurementState());
//			gEquipment.setGenerateState(gsmEquipment.getGenerateState());
//			gEquipment.setGenerateMsaState(gsmEquipment.getGenerateMsaState());
//			gEquipment.setSendState(gsmEquipment.getSendState());
//			gEquipment.setScrapDate(gsmEquipment.getScrapDate());
//			gEquipment.setScrapType(gsmEquipment.getScrapType());
//			gEquipment.setScrapReason(gsmEquipment.getScrapReason());
//			gEquipment.setRemark(gsmEquipment.getRemark());
//			gEquipment.setConfirmPeople(gsmEquipment.getConfirmPeople());
//			gEquipment.setDateOfPurchase(gsmEquipment.getDateOfPurchase());
//			gEquipment.setAgreedDeliveryDate(gsmEquipment.getAgreedDeliveryDate());
//			gEquipment.setBrand(gsmEquipment.getBrand());
//			gEquipment.setConfirmDeliveryDate(gsmEquipment.getConfirmDeliveryDate());
//			gEquipment.setBorrower(gsmEquipment.getBorrower());
//			gEquipment.setAmount(gsmEquipment.getAmount());
//			gEquipment.setBorrowDate(gsmEquipment.getBorrowDate());
//			gEquipment.setReturnDept(gsmEquipment.getReturnDept());
//			gEquipment.setReturner(gsmEquipment.getReturner());
//			gEquipment.setReturnDate(gsmEquipment.getReturnDate());
//			gEquipment.setRealReturnDate(gsmEquipment.getRealReturnDate());
//			gEquipment.setBorrowedConfirmPeople(gsmEquipment.getBorrowedConfirmPeople());
//			gEquipment.setBorrowedRemark(gsmEquipment.getBorrowedRemark());
			this.saveIntransitedEquipment(gEquipment); 
		}
	}
		
	/**
	 * 确认收货
	 * @param ids
	 * @throws Exception
	 */
	public void confirmDelivery(String ids) throws Exception {  
		for (String id : ids.split(",")) {
			GsmEquipment intransitedEquipment = this.getIntransitedEquipment(Long.valueOf(id));// 获得要复制的内容
//			if(intransitedEquipment.getConfirmDeliveryDate()==null){ 
//				intransitedEquipment.setConfirmDeliveryDate(new Date());
//				intransitedEquipment.setConfirmPeople(ContextUtils.getUserName());
//				intransitedEquipment.setModifier(ContextUtils.getUserName());
//				intransitedEquipment.setState(GsmEquipment.STATE_DEFAULT_INSTOCK);
//				this.saveIntransitedEquipment(intransitedEquipment);
//			}else{
//				throw new AmbFrameException("已在"+intransitedEquipment.getAgreedDeliveryDate()+"确认收货了！");
//			}   
		}
	}
	
	/**
	 * 根据业务代码获取邮件发送设置
	 * @param businessCode
	 * @return
	 */
	public GsmMailSettings getGsmMailSettingsByBusinessCode(String businessCode) {
		return gsmMailSettingsDao.getByBusinessCode(businessCode);
	}
	
	/**
	 * 保存邮件设置
	 * @param gsmMailSettings
	 */
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
}	 

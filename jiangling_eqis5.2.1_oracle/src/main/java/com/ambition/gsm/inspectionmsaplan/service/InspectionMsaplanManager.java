package com.ambition.gsm.inspectionmsaplan.service;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.entity.GsmEquipment;
import com.ambition.gsm.entity.GsmMailSettings;
import com.ambition.gsm.entity.InspectionMsaplan;
import com.ambition.gsm.equipment.dao.GsmEquipmentDao;
import com.ambition.gsm.equipment.dao.GsmMailSettingsDao;
import com.ambition.gsm.inspectionmsaplan.dao.InspectionMsaplanDao;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * MSA校验计划(SERVICE)
 * @author 刘承斌
 *
 */
@Service
@Transactional
public class InspectionMsaplanManager {
	@Autowired
	private LogUtilDao logUtilDao; 
	@Autowired
	private InspectionMsaplanDao inspectionMsaplanDao;
	@Autowired
	private GsmEquipmentDao gsmEquipmentDao;
	@Autowired
	private UseFileManager useFileManager;
	@Autowired
	private GsmMailSettingsDao gsmMailSettingsDao;
	
	/**
	 * 获取对象
	 * @param id
	 * @return
	 */
	public InspectionMsaplan getInspectionMsaplan(Long id){
		return inspectionMsaplanDao.get(id);
	} 
	
	/**
	 * 保存对象
	 * @param inspectionMsaplan
	 * @throws Exception
	 */
	public void saveInspectionMsaplan(InspectionMsaplan inspectionMsaplan) throws Exception{ 
		inspectionMsaplanDao.save(inspectionMsaplan);
		useFileManager.useAndCancelUseFiles(Struts2Utils.getParameter("hisAttachmentFiles"),inspectionMsaplan.getAttachment());
	} 

	/**
	 * 删除对象
	 * @param deleteIds
	 */
	public void deleteInspectionPlan(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			InspectionMsaplan inspectionMsaplan = inspectionMsaplanDao.get(Long.valueOf(id));
			if(inspectionMsaplan != null){
				GsmEquipment gsmEquipment = inspectionMsaplan.getGsmEquipment();
				//修改MSA生成状态
//				gsmEquipment.setGenerateMsaState(null);
				if(gsmEquipment!=null){
					gsmEquipmentDao.save(gsmEquipment);
				}
				inspectionMsaplanDao.delete(inspectionMsaplan);
				useFileManager.useAndCancelUseFiles(inspectionMsaplan.getAttachment(),null);
			}
		}
	}
	
	/**
	 * 分页对象
	 * @param page
	 * @param msaState
	 * @param operator
	 * @return
	 */
	public Page<InspectionMsaplan> getPage(Page<InspectionMsaplan>page,String msaState,String operator){
		return inspectionMsaplanDao.getPage(page, msaState, operator);
	}
	/**
	 * 分页对象
	 * @param page
	 * @param msaState
	 * @param operator
	 * @return
	 */
	public Page<InspectionMsaplan> getPage(Page<InspectionMsaplan>page){
		return inspectionMsaplanDao.getPage(page);
	}
	/**
	 * 送MSA
	 * @param ids
	 * @throws Exception
	 */
	public void sendMsa(String ids) throws Exception{ 
		for(String id : ids.split(",")){ 
			InspectionMsaplan inspectionMsaplan = inspectionMsaplanDao.get(Long.valueOf(id));
			
			/*if(!(InspectionMsaplan.STATE_CHECK_WAIT.equals(inspectionMsaplan.getMsaState()))){
				 throw new AmbFrameException("MSA状态【"+inspectionMsaplan.getMsaState()+"】不能重复送校"); 
			}*/
			if(!(GsmEquipment.STATE_DEFAULT_INSTOCK.equals(inspectionMsaplan.getGsmEquipment().getMeasurementState()) || GsmEquipment.STATE_INUSE.equals(inspectionMsaplan.getGsmEquipment().getMeasurementState()))){
				throw new AmbFrameException("量检具状态【"+inspectionMsaplan.getGsmEquipment().getMeasurementState()+"】不能送校"); 
			}
			inspectionMsaplan.setModifiedTime(new Date());
			inspectionMsaplan.setModifier(ContextUtils.getLoginName());
			inspectionMsaplan.setModifierName(ContextUtils.getUserName());
			//inspectionMsaplan.setMsaState(InspectionMsaplan.STATE_CHECK_IN);
			inspectionMsaplanDao.save(inspectionMsaplan);
			//相对更新量检具校验状态
			GsmEquipment gsmEquipment = inspectionMsaplan.getGsmEquipment();
			gsmEquipment.setMeasurementState(GsmEquipment.STATE_MSA);
			gsmEquipmentDao.save(gsmEquipment);
		} 
	}
	
	public GsmMailSettings getGsmMailSettingsByBusinessCode(String businessCode) {
		return gsmMailSettingsDao.getByBusinessCode(businessCode);
	}  
}
	 
	 
	 
		  
			
			 
	
	 
	
	 
	
	 
	
	 

package com.ambition.gsm.equipmentMaintenance.service;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.entity.GsmEquipment;
import com.ambition.gsm.entity.GsmEquipmentMaintenance;
import com.ambition.gsm.equipment.dao.GsmEquipmentDao;
import com.ambition.gsm.equipmentMaintenance.dao.GsmEquipmentMaintenanceDao;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 量检具维修管理(SERVICE)
 * @author 张顺志
 *
 */
@Service
@Transactional
public class GsmEquipmentMaintenanceManager {
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private GsmEquipmentMaintenanceDao gsmEquipmentMaintenanceDao;
	@Autowired
	private GsmEquipmentDao gsmEquipmentDao;
	@Autowired
	private UseFileManager useFileManager;
	
	/**
	 * 保存对象
	 * @param gsmEquipmentMaintenance
	 * @throws Exception
	 */
	public void saveGsmEquipmentMaintenance(GsmEquipmentMaintenance gsmEquipmentMaintenance) throws Exception{
		gsmEquipmentMaintenanceDao.save(gsmEquipmentMaintenance);
		useFileManager.useAndCancelUseFiles(Struts2Utils.getParameter("hisAttachmentFiles"),gsmEquipmentMaintenance.getAttachment());
	}
	
	/**
	 * 删除对象
	 * @param gsmEquipmentMaintenance
	 */
	public void deleteGsmEquipmentMaintenance(GsmEquipmentMaintenance gsmEquipmentMaintenance){
		gsmEquipmentMaintenanceDao.delete(gsmEquipmentMaintenance);
	}
	
	/**
	 * 删除对象
	 * @param deleteIds
	 * @return
	 */
	public String deleteGsmEquipmentMaintenance(String deleteIds){
		String[] ids = deleteIds.split(",");
		StringBuffer measurementNos = new StringBuffer(); 
		for(String id: ids){
			GsmEquipmentMaintenance gsmEquipmentMaintenance = gsmEquipmentMaintenanceDao.get(Long.valueOf(id));
			gsmEquipmentMaintenanceDao.delete(gsmEquipmentMaintenance);
			useFileManager.useAndCancelUseFiles(gsmEquipmentMaintenance.getAttachment(),null);
			///删除时修改状态
			if(gsmEquipmentMaintenance.getGsmEquipment() !=null ){
				GsmEquipment gsmEquipment = gsmEquipmentDao.getListByGsmEquipment(null).get(0);//(gsmEquipmentMaintenance.getGsmEquipment().getMeasurementNo());
//				gsmEquipment.setSendState(null);
				gsmEquipment.setMeasurementState(GsmEquipment.STATE_DEFAULT_INSTOCK);
				gsmEquipmentDao.save(gsmEquipment);
			}
			logUtilDao.debugLog("删除", gsmEquipmentMaintenance.toString());
		}
		return measurementNos.toString()==null?"":measurementNos.toString();
	}
	
	/**
	 * 分页对象
	 * @param page
	 * @return
	 */
	public Page<GsmEquipmentMaintenance> getPage(Page<GsmEquipmentMaintenance>page,String maintenanceState,String operator){
		return gsmEquipmentMaintenanceDao.getPage(page, maintenanceState, operator);
	}
	
	/**
	 * 获取对象
	 * @param id
	 * @return
	 */
	public GsmEquipmentMaintenance getGsmEquipmentMaintenance(Long id) {
		return gsmEquipmentMaintenanceDao.get(id);
	}
	
	/**
	 * 送修
	 * @param ids
	 * @throws Exception
	 */
	public void sendMaintenance(String ids) throws Exception{  
		for(String id : ids.split(",")){
			GsmEquipmentMaintenance gsmEquipmentMaintenance = gsmEquipmentMaintenanceDao.get(Long.valueOf(id));
			 
			if(!(GsmEquipmentMaintenance.STATE_MAINTENANCE_WAIT.equals(gsmEquipmentMaintenance.getMaintenanceState()))){
				throw new AmbFrameException("校验状态【"+gsmEquipmentMaintenance.getMaintenanceState()+"】不能重复送修"); 
			}
			if(!(GsmEquipment.STATE_DEFAULT_INSTOCK.equals(gsmEquipmentMaintenance.getGsmEquipment().getMeasurementState()) || GsmEquipment.STATE_INUSE.equals(gsmEquipmentMaintenance.getGsmEquipment().getMeasurementState()))){
				throw new AmbFrameException("量检具状态【"+gsmEquipmentMaintenance.getGsmEquipment().getMeasurementState()+"】不能送修"); 
			}
			gsmEquipmentMaintenance.setModifiedTime(new Date());
			gsmEquipmentMaintenance.setModifier(ContextUtils.getLoginName());
			gsmEquipmentMaintenance.setModifierName(ContextUtils.getUserName());
			gsmEquipmentMaintenance.setMaintenanceState(GsmEquipmentMaintenance.STATE_MAINTENANCE_IN);
			gsmEquipmentMaintenance.setMaintenanceDate(new Date());
			gsmEquipmentMaintenanceDao.save(gsmEquipmentMaintenance);
			//相对更新量检具校验状态
			GsmEquipment gsmEquipment = gsmEquipmentMaintenance.getGsmEquipment();
//			gsmEquipment.setSendState(gsmEquipmentMaintenance.getMaintenanceState());
			gsmEquipment.setMeasurementState(gsmEquipmentMaintenance.getMaintenanceState());
			gsmEquipmentDao.save(gsmEquipment);
		}
		
	}
	
	/**
	 * 维修后入库
	 * @param ids
	 * @throws Exception
	 */
	public void putinMaintenance(String ids) throws Exception{  
		for(String id : ids.split(",")){
			GsmEquipmentMaintenance gsmEquipmentMaintenance = gsmEquipmentMaintenanceDao.get(Long.valueOf(id));
			
			if(!(GsmEquipmentMaintenance.STATE_MAINTENANCE_IN.equals(gsmEquipmentMaintenance.getMaintenanceState()))){
				throw new AmbFrameException("维修状态【"+gsmEquipmentMaintenance.getMaintenanceState()+"】不能入库"); 
			}
			gsmEquipmentMaintenance.setMaintenanceState(GsmEquipmentMaintenance.STATE_MAINTENANCE_OUT);
			gsmEquipmentMaintenanceDao.save(gsmEquipmentMaintenance);
			//更新相应的量检具信息状态
			GsmEquipment gsmEquipment = gsmEquipmentMaintenance.getGsmEquipment();
//			gsmEquipment.setSendState(gsmEquipmentMaintenance.getMaintenanceState());
			gsmEquipment.setMeasurementState(GsmEquipment.STATE_INUSE);
			gsmEquipmentDao.save(gsmEquipment);
		}
	}	 
}

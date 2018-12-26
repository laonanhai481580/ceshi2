package com.ambition.gsm.maintain.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.entity.LarData;
import com.ambition.gsm.entity.GsmEquipment;
import com.ambition.gsm.entity.InspectionPlan;
import com.ambition.gsm.entity.Maintain;
import com.ambition.gsm.inspectionplan.dao.InspectionPlanDao;
import com.ambition.gsm.maintain.dao.MaintainDao;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

@Service
@Transactional
public class MaintainManager {
	@Autowired
	private MaintainDao maintainDao;
	@Autowired
	private InspectionPlanDao inspectionPlanDao;
	
	public Maintain getMaintain(Long id){
		return maintainDao.get(id);
	}
	public void saveMaintain(Maintain maintain){
		maintainDao.save(maintain);
	}
	
	public Page<Maintain> list(Page<Maintain> page){
		return maintainDao.list(page);
	}
	
	public List<Maintain> listAll(){
		return maintainDao.getMaintain();
	}
	
	
	public void deleteMaintain(String deleteIds){
		String[] ids = deleteIds.split(",");
 		for(String id : ids){
			Maintain maintain=maintainDao.get(Long.valueOf(id));
			if(maintain.getId() != null){
				maintainDao.delete(maintain);
			}
		}
	}
	
	public Page<Maintain> search(Page<Maintain> page){
		return maintainDao.search(page);
	}
	/**
	 * 生成校验计划
	 * @param ids
	 * @throws Exception
	 */
	
	public void createMan(String ids) throws Exception{  
		for(String id : ids.split(",")){ 
			Maintain maintain = maintainDao.get(Long.valueOf(id));
			
			List<InspectionPlan> list=inspectionPlanDao.getInspectionPlanByMan(maintain.getEquipmentName());
			if(list.size()>0){
				throw new AmbFrameException("设备：" + maintain.getEquipmentName() + "已经有计划存在，不能再次生成计划！");
			}
			InspectionPlan inspectionPlan = new InspectionPlan();
			inspectionPlan.setCompanyId(ContextUtils.getCompanyId());
			inspectionPlan.setCreatedTime(new Date());
			inspectionPlan.setCreator(ContextUtils.getLoginName());
			inspectionPlan.setCreatorName(ContextUtils.getUserName());
			inspectionPlan.setInspectionState(InspectionPlan.STATE_CHECk_WAIT);
			inspectionPlan.setYesOrNo(true);
			inspectionPlan.setEquipmentName(maintain.getEquipmentName());
			inspectionPlan.setEquipmentModel(maintain.getEquipmentModel());
			inspectionPlan.setCompanyMain(maintain.getCompanyMain());
			inspectionPlan.setDevName(maintain.getDevName());
			inspectionPlan.setDutyMan(maintain.getResponsible());
			inspectionPlan.setDutyLoginMan(maintain.getResponsibleLogin());
			inspectionPlan.setManagerAssets(maintain.getManagerAssets());
			inspectionPlan.setManufacturer(maintain.getManufacturer());
			inspectionPlan.setCheckMethod(maintain.getCheckMethod());
			inspectionPlan.setInspectionPlanDate(new Date());
			inspectionPlanDao.save(inspectionPlan);
			//更新量检具生成校验计划状态
//			gsmEquipment.setGenerateState(inspectionPlan.getInspectionState());
//			maintainDao.getSession().save(maintain);
		}
 	}
}

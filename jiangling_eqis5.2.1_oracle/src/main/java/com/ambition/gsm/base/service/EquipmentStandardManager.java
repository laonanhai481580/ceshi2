package com.ambition.gsm.base.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.base.dao.EquipmentStandardDao;
import com.ambition.gsm.entity.EquipmentStandard;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class EquipmentStandardManager {
	@Autowired
	private EquipmentStandardDao equipmentStandardDao;	
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	
	//获取记录
	public EquipmentStandard getEquipmentStandard(Long id){
		return equipmentStandardDao.get(id);
	}
	//验证并保存记录
	public boolean isExistEquipmentStandard(Long id, String managerAssets, String equipmentName){
		String hql = "select count(*) from EquipmentStandard d where d.companyId =? and d.managerAssets=? and d.equipmentName = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());		
		params.add(managerAssets);
		params.add(equipmentName);
		if(id != null){
			hql += " and d.id <> ?";
			params.add(id);
		}
		Query query = equipmentStandardDao.getSession().createQuery(hql);
		for(int i = 0;i < params.size(); i++){
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
	public void saveEquipmentStandard(EquipmentStandard equipmentStandard){
		if(StringUtils.isEmpty(equipmentStandard.getManagerAssets())){
			throw new RuntimeException("管理编号不能为空!");
		}
		if(StringUtils.isEmpty(equipmentStandard.getEquipmentName())){
			throw new RuntimeException("设备名称不能为空!");
		}
		if(isExistEquipmentStandard(equipmentStandard.getId(), equipmentStandard.getManagerAssets(), equipmentStandard.getEquipmentName())){
			throw new RuntimeException("已经存在相同的设备!");
		}
		equipmentStandardDao.save(equipmentStandard);
	}
	public void saveExcelEquipmentStandard(EquipmentStandard equipmentStandard){
		equipmentStandardDao.save(equipmentStandard);
	}
	
	//删除记录
	public void deleteEquipmentStandard(String ids){
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(equipmentStandardDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", equipmentStandardDao.get(Long.valueOf(id)).toString());
			}
			equipmentStandardDao.delete(Long.valueOf(id));
		}
	}
	
	//删除对象
	public void deleteEquipmentStandard(EquipmentStandard equipmentStandard){
		logUtilDao.debugLog("删除", equipmentStandard.toString());
		equipmentStandardDao.delete(equipmentStandard);
	}
	public Page<EquipmentStandard> list(Page<EquipmentStandard> page){
		return equipmentStandardDao.list(page);
	}	
	//返回所有对象列表
	public List<EquipmentStandard> listAll(){
		return equipmentStandardDao.getAllEquipmentStandard();
	}
	/**
	 * 获取所有的设备
	 * @return
	 */
	public List<Option> listAllForOptions(){
		List<EquipmentStandard> equipmentStandards = listAll();
		List<Option> options = new ArrayList<Option>();
		for(EquipmentStandard equipmentStandard : equipmentStandards){
			Option option = new Option();
			option.setName(equipmentStandard.getEquipmentName());
			option.setValue(equipmentStandard.getEquipmentName());
			options.add(option);
		}
		return options;
	}
}

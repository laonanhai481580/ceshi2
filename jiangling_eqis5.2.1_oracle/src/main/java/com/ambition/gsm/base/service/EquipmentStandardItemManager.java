package com.ambition.gsm.base.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.base.dao.EquipmentStandardItemDao;
import com.ambition.gsm.entity.EquipmentStandard;
import com.ambition.gsm.entity.EquipmentStandardItem;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class EquipmentStandardItemManager {
	@Autowired
	private EquipmentStandardItemDao equipmentStandardItemDao;
	@Autowired
	private EquipmentStandardManager equipmentStandardManager;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	//获取记录
	public EquipmentStandardItem getEquipmentStandardItem(Long id){
		return equipmentStandardItemDao.get(id);
	}
	
	public EquipmentStandardItem getByCode(String standardNo){
		return equipmentStandardItemDao.getByCode(standardNo);
	}
	
	//验证并保存记录
	private boolean isExistEquipmentStandardItem(Long id, String standardName, String standardNo, EquipmentStandard equipmentStandard) {
		String hql = "select count(*) from EquipmentStandardItem d where d.companyId = ? and d.equipmentStandard = ? and (d.standardName =? or d.standardNo =?)";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(equipmentStandard);
		params.add(standardName);
		params.add(standardNo);
		if(id != null){
			hql += "and d.id <> ?";
			params.add(id);
		}
		Query query = equipmentStandardItemDao.getSession().createQuery(hql);
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
	public void saveEquipmentStandardItem(EquipmentStandardItem item){
		if(StringUtils.isEmpty(item.getStandardName())){
			throw new RuntimeException("标准件名称不为空!");
		}
		if(StringUtils.isEmpty(item.getStandardNo())){
			throw new RuntimeException("标准件编号不为空!");
		}
		if(isExistEquipmentStandardItem(item.getId(),item.getStandardNo(),item.getStandardName(),item.getEquipmentStandard())){
			throw new RuntimeException("已存在相同的标准件!");
		}
		equipmentStandardItemDao.save(item);
	}	
	public void saveExcelEquipmentStandardItem(EquipmentStandardItem item){
		equipmentStandardItemDao.save(item);
	}	
	//删除记录
	public void deleteEquipmentStandardItem(String ids){
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(equipmentStandardItemDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", equipmentStandardItemDao.get(Long.valueOf(id)).toString());
			}
			equipmentStandardItemDao.delete(Long.valueOf(id));
		}
	}
	//删除对象
	public void deleteEquipmentStandardItem(EquipmentStandardItem item){
		logUtilDao.debugLog("删除", item.toString());
		equipmentStandardItemDao.delete(item);
	}
	
	//返回页面对象列表
	public Page<EquipmentStandardItem> list(Page<EquipmentStandardItem> page,EquipmentStandard equipmentStandard){
		return equipmentStandardItemDao.list(page, equipmentStandard);
	}
	
	public Page<EquipmentStandardItem> listByParent(Page<EquipmentStandardItem> page,Long parentId){
		return equipmentStandardItemDao.listByParent(page, parentId);
	}
	public Page<EquipmentStandardItem> listByParams(Page<EquipmentStandardItem> page ,String measurementName){
		return equipmentStandardItemDao.listByParams(page,measurementName);
	}		
	public Page<EquipmentStandardItem> getCodeByParams(Page<EquipmentStandardItem> page, JSONObject params,EquipmentStandard equipmentStandard){
		return equipmentStandardItemDao.getCodeByParams(page, params,equipmentStandard);
	}
	
	public Page<EquipmentStandardItem> list(Page<EquipmentStandardItem> page,String code){
		return equipmentStandardItemDao.list(page, code);
	}
	//返回所有对象列表
	public List<EquipmentStandardItem> listAll(EquipmentStandard equipmentStandard){
		return equipmentStandardItemDao.getAllEquipmentStandardItems(equipmentStandard);
	}	
	public List<EquipmentStandardItem> listEquipmentStandardItem(String code){
		return equipmentStandardItemDao.getEquipmentStandardItem(code);
	}
}

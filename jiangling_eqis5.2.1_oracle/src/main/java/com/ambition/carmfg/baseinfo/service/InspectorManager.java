package com.ambition.carmfg.baseinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.dao.InspectorDao;
import com.ambition.carmfg.entity.InspectionPointTypeEnum;
import com.ambition.carmfg.entity.InspectionType;
import com.ambition.carmfg.entity.Inspector;
import com.norteksoft.product.orm.Page;

@Service
@Transactional
public class InspectorManager {
	
	@Autowired
	private InspectorDao inspectorDao;
	
	
	public Inspector getInspector(Long id){
		return inspectorDao.get(id);
	}
	public  void saveInspector(Inspector inspector){
		inspectorDao.save(inspector);
	}
	
	public void deleteInspector(Long id){
		inspectorDao.delete(id);
	}
	
	public void deleteInspector(Inspector inspector){
		inspectorDao.delete(inspector);
	}
	
	public void deleteInspector(String ids){
		String[] deleteIds = ids.split(",");
		for(String id:deleteIds){
			inspectorDao.delete(Long.valueOf(id));
		}
	}
	public Page<Inspector> list(Page<Inspector> page,Long inspectionPointId){
		return inspectorDao.list(page, inspectionPointId);
	}
	
	public List<Inspector> listAll(){
		return inspectorDao.getAllInspector();
	}
	
	public List<Inspector> getInspectorByInspectionType(InspectionType inspectionType){
		return inspectorDao.getInspectorByInspectionType(inspectionType);
	}
	
	public List<Inspector> getInspectorByWorkshop(String workshop){
		return inspectorDao.getInspectorByWorkshop(workshop);
	}
	
	public List<Inspector> getInspectorByWorkshopAndType(String workshop,InspectionPointTypeEnum listType){
		return inspectorDao.getInspectorByWorkshopAndType(workshop, listType);
	}
	public List<Inspector> getInspectorByType(InspectionPointTypeEnum listType){
		return inspectorDao.getInspectorByType(listType);
	}
}

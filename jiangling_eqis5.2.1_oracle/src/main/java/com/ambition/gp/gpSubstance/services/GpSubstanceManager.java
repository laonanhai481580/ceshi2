package com.ambition.gp.gpSubstance.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gp.entity.GpSubstance;
import com.ambition.gp.gpSubstance.dao.GpSubstanceDao;
import com.ambition.gsm.inspectionplan.dao.InspectionPlanDao;
import com.norteksoft.product.orm.Page;

@Service
@Transactional
public class GpSubstanceManager {
	@Autowired
	private GpSubstanceDao gpSubstanceDao;
	@Autowired
	private InspectionPlanDao inspectionPlanDao;
	
	public GpSubstance getGpSubstance(Long id){
		return gpSubstanceDao.get(id);
	}
	public void saveGpSubstance(GpSubstance gpSubstance){
		gpSubstanceDao.save(gpSubstance);
	}
	
	public Page<GpSubstance> list(Page<GpSubstance> page){
		return gpSubstanceDao.list(page);
	}
	
	public List<GpSubstance> listAll(){
		return gpSubstanceDao.getGpSubstance();
	}
	
	
	public void deleteGpSubstance(String deleteIds){
		String[] ids = deleteIds.split(",");
 		for(String id : ids){
			GpSubstance gpSubstance=gpSubstanceDao.get(Long.valueOf(id));
			if(gpSubstance.getId() != null){
				gpSubstanceDao.delete(gpSubstance);
			}
		}
	}
	
	public Page<GpSubstance> search(Page<GpSubstance> page,Long id){
		return gpSubstanceDao.search(page,id);
	}
}

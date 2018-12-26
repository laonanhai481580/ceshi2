package com.ambition.iqc.interval.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.InspectionInterval;
import com.ambition.iqc.interval.dao.InspectionIntervalDao;
import com.norteksoft.product.orm.Page;

@Transactional
@Service
public class InspectionIntervalManager {
	@Autowired
	private InspectionIntervalDao inspectionIntervalDao;
	
	
	public Page<InspectionInterval> search(Page<InspectionInterval> page){
		return inspectionIntervalDao.searchPage(page);
	}
	
	
	public InspectionInterval getInspectionInterval(Long id){
		return inspectionIntervalDao.get(id);
	}
	
	public void saveInspectionInterval(InspectionInterval nspectionInterval){
		inspectionIntervalDao.save(nspectionInterval);
	}
	
	public void deleteInspectionInterval(String deleteIds){
		String [] ids=deleteIds.split(",");
		for(String id:ids){
			inspectionIntervalDao.delete(Long.valueOf(id));
		}
	}
	
	
	
	
}

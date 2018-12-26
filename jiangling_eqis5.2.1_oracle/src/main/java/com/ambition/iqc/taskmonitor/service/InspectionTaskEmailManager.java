package com.ambition.iqc.taskmonitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.InspectionTaskEmail;
import com.ambition.iqc.taskmonitor.dao.InspectionTaskEmailDao;
import com.norteksoft.product.orm.Page;

@Transactional
@Service
public class InspectionTaskEmailManager {
	@Autowired
	private InspectionTaskEmailDao  inspectionTaskEmailDao;
	
	public InspectionTaskEmail getInspectionTaskEmail(Long id){
		return inspectionTaskEmailDao.get(id);
	}
	
	public Page<InspectionTaskEmail> getPage(Page<InspectionTaskEmail> page){
		return inspectionTaskEmailDao.getPage(page);
	}
	
	public void saveInspectionTaskEmail(InspectionTaskEmail taskEmail){
		inspectionTaskEmailDao.save(taskEmail);
	}
	
	public void deleteInspectionTaskEmail(String deleteIds){
		String [] deleteId=deleteIds.split(",");
		for(String id:deleteId){
			inspectionTaskEmailDao.delete(Long.valueOf(id));
		}
	}
	
}

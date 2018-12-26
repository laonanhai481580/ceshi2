package com.ambition.iqc.taskmonitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.InspectionFromOverdueEmail;
import com.ambition.iqc.taskmonitor.dao.InspectionFromOverdueEmailDao;
import com.norteksoft.product.orm.Page;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2016-10-21 发布
 */
@Transactional
@Service
public class InspectionFromOverdueEmailManager {
	@Autowired
	private InspectionFromOverdueEmailDao inspectionFromOverdueEmailDao;
	
	public InspectionFromOverdueEmail getInspectionFromOverdueEmail(Long id){
		return inspectionFromOverdueEmailDao.get(id);
	}
	
	public void deleteInspectionFromOverdueEmail(String deleteIds){
		String [] ids=deleteIds.split(",");
		for(String id:ids){
			inspectionFromOverdueEmailDao.delete(Long.valueOf(id));
		}
	}
	
	
	public Page<InspectionFromOverdueEmail> getInspectionFromOverdueEmailPage(Page<InspectionFromOverdueEmail> page){
		return inspectionFromOverdueEmailDao.getPage(page);
	}
	
	public void saveInspectionFromOverdueEmail(InspectionFromOverdueEmail inspectionFromOverdueEmail){
		inspectionFromOverdueEmailDao.save(inspectionFromOverdueEmail);
	}
}

package com.ambition.gsm.inspectionrecordattach.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.entity.InspectionRecordAttach;
import com.ambition.gsm.inspectionrecordattach.dao.InspectionRecordAttachDao;

/**
 * 
 * 类名: InspectionRecordAttachManager 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：检定计划附件service</p>
 * @author  刘承斌
 * @version 1.00 2014-5-21 下午2:46:39  发布
 */

@Service
@Transactional
public class InspectionRecordAttachManager {
	@Autowired
	private InspectionRecordAttachDao inspectionRecordAttachDao;
	
	public void saveInspectionRecordAttach(InspectionRecordAttach inspectionRecordAttach){
		inspectionRecordAttachDao.save(inspectionRecordAttach);
	}
	
	public InspectionRecordAttach getInspectionRecordAttach(Long id){
		return inspectionRecordAttachDao.get(id);
	}
	
	public void deleteInspectionRecordAttach(Long id){
		inspectionRecordAttachDao.delete(id);
	}
}

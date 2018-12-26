package com.ambition.spc.annormalreason.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.spc.annormalreason.dao.AbnormalReasonDao;
import com.ambition.spc.entity.AbnormalReason;
import com.ambition.spc.entity.AbnormalReasonType;
import com.norteksoft.product.orm.Page;

/**    
 * AbnormalReasonManager.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class AbnormalReasonManager {
	@Autowired
	private AbnormalReasonDao abnormalReasonDao;
	
	public AbnormalReason getAbnormalReason(Long id){
		return abnormalReasonDao.get(id);
	}
	public void deleteAbnormalReason(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id:ids){
			abnormalReasonDao.delete(Long.valueOf(id));
		}
	}
	public void saveAbnormalReason(AbnormalReason abnormalReason){
		abnormalReasonDao.save(abnormalReason);
	}
	public Page<AbnormalReason> getPage(Page<AbnormalReason> page,AbnormalReasonType type){
		return abnormalReasonDao.getPage(page,type);
	}
}

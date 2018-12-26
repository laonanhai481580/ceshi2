package com.ambition.spc.jlanalyse.service;

import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.spc.entity.ControlLimit;
import com.ambition.spc.jlanalyse.dao.ControlLimitDao;



/**    
* ControlLimitManager.java
* @authorBy wlongfeng
*
*/
@Service
public class ControlLimitManager {
	@Autowired
	private  ControlLimitDao controlLimitDao;
	@Transactional
	public void saveControlLimit(ControlLimit controlLimit){
		controlLimitDao.save(controlLimit);
	}
	
	public List<ControlLimit> getControlLimitDesc(Long featureId,Session session){
		if(session==null){
			session = controlLimitDao.getSession();
		}
		return controlLimitDao.getControlLimitDesc(featureId,session);
	}
}

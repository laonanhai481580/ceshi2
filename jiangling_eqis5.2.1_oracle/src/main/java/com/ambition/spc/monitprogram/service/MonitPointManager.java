package com.ambition.spc.monitprogram.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.spc.entity.MonitPoint;
import com.ambition.spc.entity.MonitProgram;
import com.ambition.spc.entity.MonitQualityFeature;
import com.ambition.spc.monitprogram.dao.MointPointDao;
import com.ambition.spc.monitprogram.dao.MonitQualityFeatureDao;
import com.norteksoft.product.orm.Page;

/**    
 * MonitPointManager.java
 * @authorBy wanglf
 *
 */
@Service
@Transactional
public class MonitPointManager {
	@Autowired
	private MointPointDao mointPointDao;
	
	@Autowired
	private MonitQualityFeatureDao monitQualityFeatureDao;
	
	public Page<MonitPoint> listByMonitProgram(Page<MonitPoint> page,MonitProgram monitProgram){
		return mointPointDao.listByMonitProgram(page, monitProgram);
	}
	public List<MonitPoint> getAllMonitPointsByMonitProgram(MonitProgram monitProgram){
		return mointPointDao.getAllMonitPointsByMonitProgram(monitProgram);
	}
	
	public MonitPoint getMonitPoint(Long id){
		return mointPointDao.get(id);
	}
	
	public void saveMonitQualityFeature(MonitQualityFeature monitQualityFeature){
		monitQualityFeatureDao.save(monitQualityFeature);
	}
	
	public void saveMonitPoint(MonitPoint monitPoint){
		mointPointDao.save(monitPoint);
	}
	public void deleteMonitPoint(String  deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id:ids){
			MonitPoint m=	mointPointDao.get(Long.parseLong(id));
			mointPointDao.delete(m);
		}
	}
	
	public void deleteMonitQualityFeature(String  deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id:ids){
			MonitQualityFeature m=	monitQualityFeatureDao.get(Long.parseLong(id));
			monitQualityFeatureDao.delete(m);
		}
	}
	public List<MonitQualityFeature> listMonitQualityFeature(Long monitPointId){
		return monitQualityFeatureDao.listMonitQualityFeature(monitPointId);
	}
	
}

package com.ambition.spc.monitprogram.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.MonitQualityFeature;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * MonitQualityFeatureDao.java
 * @authorBy wanglf
 *
 */
@Repository
public class MonitQualityFeatureDao extends HibernateDao<MonitQualityFeature, Long> {
	
	public List<MonitQualityFeature> listMonitQualityFeature(Long monitPointId){
		return find("from MonitQualityFeature m where m.companyId = ? and m.monitPoint.id=?", ContextUtils.getCompanyId(),monitPointId);
	}
}

package com.ambition.spc.jlanalyse.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.ControlLimit;
import com.norteksoft.product.orm.hibernate.HibernateDao;

/**    
 * ControlLimitDao.java
 * @authorBy wanglf
 *
 */
@Repository
public class ControlLimitDao extends HibernateDao<ControlLimit,Long> {
	
	@SuppressWarnings("unchecked")
	public List<ControlLimit> getControlLimitDesc(Long featureId,Session session) {
		String hql="select c from ControlLimit  c  where  c.qualityFeature.id=? order by c.createdTime desc";
		return session.createQuery(hql)
			.setParameter(0,featureId)
			.list();
	} 
	
	@SuppressWarnings("unchecked")
	public List<ControlLimit> getControlLimitDesc(String featureIds,Session session) {
		String hql="select c from ControlLimit  c  where  c.qualityFeature.id in ("+featureIds+") order by c.createdTime desc";
		return session.createQuery(hql)
			.list();
	} 
	
}

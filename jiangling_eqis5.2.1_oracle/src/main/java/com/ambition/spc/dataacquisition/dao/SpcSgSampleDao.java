package com.ambition.spc.dataacquisition.dao;

import java.util.List;


import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.SpcSgSample;
import com.norteksoft.product.orm.hibernate.HibernateDao;

/**    
 * SpcSgSampleDao.java
 * @authorBy wanglf
 *
 */
@Repository
public class SpcSgSampleDao extends HibernateDao< SpcSgSample, Long> {
	
	public List<SpcSgSample> spcSgSamplList(Long featureId) {
		String hql="select spcSgSample from SpcSgSample  spcSgSample right outer join spcSgSample.spcSubGroup spcSubGroup where  spcSubGroup.id>=? group by spcSgSample.sampleOrderNum";
		return find(hql,featureId);
	} 
	
}

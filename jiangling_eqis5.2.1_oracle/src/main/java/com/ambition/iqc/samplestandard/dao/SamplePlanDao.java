package com.ambition.iqc.samplestandard.dao;

import org.springframework.stereotype.Repository;

import com.ambition.iqc.entity.SampleScheme;
import com.norteksoft.product.orm.hibernate.HibernateDao;

/**    
 * SamplePlanDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class SamplePlanDao extends HibernateDao<SampleScheme,Long> {
}

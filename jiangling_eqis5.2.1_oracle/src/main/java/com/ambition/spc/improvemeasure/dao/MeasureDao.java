package com.ambition.spc.improvemeasure.dao;

import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.ImprovementMeasure;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * MeasureDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class MeasureDao extends HibernateDao<ImprovementMeasure, Long> {
	public Page<ImprovementMeasure> search(Page<ImprovementMeasure> page){
		return searchPageByHql(page, "from ImprovementMeasure i where i.companyId = ?", ContextUtils.getCompanyId());
	}
}

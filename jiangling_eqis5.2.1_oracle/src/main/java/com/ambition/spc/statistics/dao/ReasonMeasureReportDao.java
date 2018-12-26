package com.ambition.spc.statistics.dao;

import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.ReasonMeasure;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * ReasonMeasureReportDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class ReasonMeasureReportDao extends HibernateDao<ReasonMeasure, Long> {
	public Page<ReasonMeasure> search(Page<ReasonMeasure> page){
		return searchPageByHql(page, "from ReasonMeasure r where r.companyId = ?", ContextUtils.getCompanyId());
	}
}

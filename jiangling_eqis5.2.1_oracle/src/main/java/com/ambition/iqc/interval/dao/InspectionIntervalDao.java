package com.ambition.iqc.interval.dao;

import org.springframework.stereotype.Repository;

import com.ambition.iqc.entity.InspectionInterval;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
@Repository
public class InspectionIntervalDao extends HibernateDao<InspectionInterval, Long>{
	
	public Page<InspectionInterval> searchPage(Page<InspectionInterval> page){
		String hql=" from InspectionInterval inspectionInterval where inspectionInterval.companyId=? ";
		return this.searchPageByHql(page, hql,ContextUtils.getCompanyId());
	}
	

	
}

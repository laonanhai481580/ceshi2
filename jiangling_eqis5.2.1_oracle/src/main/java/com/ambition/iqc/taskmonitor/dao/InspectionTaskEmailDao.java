package com.ambition.iqc.taskmonitor.dao;

import org.springframework.stereotype.Repository;

import com.ambition.iqc.entity.InspectionTaskEmail;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class InspectionTaskEmailDao extends HibernateDao< InspectionTaskEmail,Long> {
	
	public Page<InspectionTaskEmail> getPage(Page<InspectionTaskEmail> page){
		String hql="from InspectionTaskEmail task where task.companyId=? ";
		return this.searchPageByHql(page, hql,ContextUtils.getCompanyId());
	}
}

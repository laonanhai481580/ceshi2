package com.ambition.ecm.ecn.dao;


import org.springframework.stereotype.Repository;

import com.ambition.ecm.entity.DcrnReport;
import com.ambition.ecm.entity.EcnReport;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class EcnReportDao extends HibernateDao<EcnReport, Long>{
	
	public Page<EcnReport> searchPage(Page<EcnReport> page){
		String hql="from EcnReport report where report.companyId=? ";
		return this.searchPageByHql(page, hql,ContextUtils.getCompanyId());
	}
}

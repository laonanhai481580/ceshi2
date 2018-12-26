package com.ambition.ecm.dcrn.dao;


import org.springframework.stereotype.Repository;

import com.ambition.ecm.entity.DcrnReport;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class DcrnReportDao extends HibernateDao<DcrnReport, Long>{
	
	public Page<DcrnReport> searchPage(Page<DcrnReport> page){
		String hql="from DcrnReport report where report.companyId=? ";
		return this.searchPageByHql(page, hql,ContextUtils.getCompanyId());
	}
}

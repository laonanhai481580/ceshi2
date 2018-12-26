package com.ambition.supplier.supervision.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.CheckReport;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class CheckReportDao extends HibernateDao<CheckReport, Long> {
	
	/**
	 * 查询监察报告
	 * @param page
	 * @return
	 */
	public Page<CheckReport> list(Page<CheckReport> page){
		String hql = "from CheckReport c where c.companyId = ?";
		return searchPageByHql(page, hql, ContextUtils.getCompanyId());
	}
}

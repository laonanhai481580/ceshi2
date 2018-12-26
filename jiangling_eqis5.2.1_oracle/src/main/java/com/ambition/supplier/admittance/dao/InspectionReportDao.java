package com.ambition.supplier.admittance.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.InspectionReport;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class InspectionReportDao extends HibernateDao<InspectionReport, Long> {
	/**
	 * 查询供应商考察记录
	 * @param page
	 * @return
	 */
	public Page<InspectionReport> search(Page<InspectionReport> page){
		return searchPageByHql(page, "from InspectionReport i where i.companyId = ?", ContextUtils.getCompanyId());
	}
	
}

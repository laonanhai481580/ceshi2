package com.ambition.supplier.admittance.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.AppraisalReport;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class AppraisalReportDao extends HibernateDao<AppraisalReport, Long> {
	/**
	 * 查询供应商鉴定记录
	 * @param page
	 * @return
	 */
	public Page<AppraisalReport> search(Page<AppraisalReport> page,String type){
		return searchPageByHql(page, "from AppraisalReport i where i.companyId = ? and i.type = ?", ContextUtils.getCompanyId(),type);
	}
	
}

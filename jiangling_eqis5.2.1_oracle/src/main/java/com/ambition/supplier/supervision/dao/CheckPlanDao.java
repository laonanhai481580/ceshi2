package com.ambition.supplier.supervision.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.CheckPlan;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class CheckPlanDao extends HibernateDao<CheckPlan, Long> {
	
	/**
	 * 查询稽查计划
	 * @param page
	 * @return
	 */
	public Page<CheckPlan> list(Page<CheckPlan> page){
		String hql = "from CheckPlan c where c.companyId = ?";
		return searchPageByHql(page, hql, ContextUtils.getCompanyId());
	}
}

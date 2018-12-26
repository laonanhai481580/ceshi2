package com.ambition.supplier.admittance.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.Leave;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class LeaveDao extends HibernateDao<Leave, Long> {
	
	public Page<Leave> search(Page<Leave> page){
		return searchPageByHql(page, "from Leave i where i.companyId = ?", ContextUtils.getCompanyId());
	}
	
}

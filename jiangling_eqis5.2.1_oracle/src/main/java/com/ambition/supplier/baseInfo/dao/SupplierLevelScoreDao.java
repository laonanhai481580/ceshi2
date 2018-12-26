package com.ambition.supplier.baseInfo.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierLevelScore;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class SupplierLevelScoreDao extends HibernateDao<SupplierLevelScore, Long>{

	public Page<SupplierLevelScore> listDatas(Page<SupplierLevelScore> page) {
		// TODO Auto-generated method stub
		return searchPageByHql(page, " from SupplierLevelScore t where t.companyId=?", ContextUtils.getCompanyId());
	}

}

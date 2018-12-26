package com.ambition.supplier.manager.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierImprove;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * ManagerDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class ManagerDao extends HibernateDao<SupplierImprove, Long> {
	/**
	 * 查询供应商改进记录
	 * @param page
	 * @return
	 */
	public Page<SupplierImprove> search(Page<SupplierImprove> page){
		return searchPageByHql(page, "from SupplierImprove s where s.companyId = ?", ContextUtils.getCompanyId());
	}

}

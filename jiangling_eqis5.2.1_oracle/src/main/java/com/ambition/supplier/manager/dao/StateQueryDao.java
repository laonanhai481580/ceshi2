package com.ambition.supplier.manager.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplyProduct;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * StateQueryDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class StateQueryDao extends HibernateDao<SupplyProduct,Long>{
	
	public Page<SupplyProduct> search(Page<SupplyProduct> page){
		return searchPageByHql(page, "from SupplyProduct s where s.companyId = ?", ContextUtils.getCompanyId());
	}
}

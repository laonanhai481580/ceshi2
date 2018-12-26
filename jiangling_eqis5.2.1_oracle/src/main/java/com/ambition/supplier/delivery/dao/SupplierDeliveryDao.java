package com.ambition.supplier.delivery.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierDelivery;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * SupplierDeliveryDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class SupplierDeliveryDao extends HibernateDao<SupplierDelivery, Long>{
	public Page<SupplierDelivery> search(Page<SupplierDelivery> page){
		return searchPageByHql(page, "from SupplierDelivery s where s.companyId = ?", ContextUtils.getCompanyId());
	}
}

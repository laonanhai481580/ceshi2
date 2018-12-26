package com.ambition.supplier.delivery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.delivery.dao.SupplierDeliveryDao;
import com.ambition.supplier.entity.SupplierDelivery;
import com.norteksoft.product.orm.Page;

/**    
 * SupplierDeliveryManager.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class SupplierDeliveryManager {
	@Autowired
	private SupplierDeliveryDao supplierDeliveryDao;

	public Page<SupplierDelivery> search(Page<SupplierDelivery> page){
		return supplierDeliveryDao.search(page);
	}
}

package com.ambition.supplier.archives.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.archives.dao.SupplierDao;
import com.ambition.supplier.archives.dao.SupplyProductDao;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.entity.SupplyProduct;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:供应商供应商产品
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2016年10月25日 发布
 */
@Service
@Transactional
public class SupplyProductManager {
	@Autowired
	private SupplyProductDao supplyProductDao;

	public Page<SupplyProduct> searchByPage(Page<SupplyProduct> page, Long id) {
		// TODO Auto-generated method stub
		String hql = " from SupplyProduct s where s.companyId=? and s.supplier.id=?";
		
		return supplyProductDao.searchPageByHql(page, hql, ContextUtils.getCompanyId(),id);
	}

	public SupplyProduct getSupplyProduct(Long id) {
		// TODO Auto-generated method stub
		return supplyProductDao.get(id);
	}
}

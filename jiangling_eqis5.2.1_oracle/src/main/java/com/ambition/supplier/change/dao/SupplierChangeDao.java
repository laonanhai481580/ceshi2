package com.ambition.supplier.change.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierChange;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author linshaowei
 * @version 1.00 2016年10月12日 发布
 */
@Repository
public class SupplierChangeDao extends   HibernateDao<SupplierChange,Long>{
	public Page<SupplierChange> search(Page<SupplierChange> page) {
		String hql = "from SupplierChange o where o.companyId=? ";
		return searchPageByHql(page, hql,ContextUtils.getCompanyId());
	}
}

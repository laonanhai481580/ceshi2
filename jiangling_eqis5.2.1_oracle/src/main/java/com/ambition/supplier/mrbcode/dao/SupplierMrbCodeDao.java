package com.ambition.supplier.mrbcode.dao;


import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierMrbCode;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Repository
public class SupplierMrbCodeDao extends HibernateDao<SupplierMrbCode,Long>{
	public Page<SupplierMrbCode> search(Page<SupplierMrbCode> page){
		return searchPageByHql(page,"from SupplierMrbCode s");
	}
}

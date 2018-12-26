package com.ambition.supplier.archives.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplyProduct;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Repository
public class SupplyProductDao extends HibernateDao<SupplyProduct, Long> {
}

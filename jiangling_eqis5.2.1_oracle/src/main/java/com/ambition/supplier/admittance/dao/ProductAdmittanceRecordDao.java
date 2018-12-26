package com.ambition.supplier.admittance.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.ProductAdmittanceRecord;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Repository
public class ProductAdmittanceRecordDao extends HibernateDao<ProductAdmittanceRecord, Long> {
}

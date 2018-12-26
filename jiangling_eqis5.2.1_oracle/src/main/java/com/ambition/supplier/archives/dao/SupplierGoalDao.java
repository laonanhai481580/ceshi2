package com.ambition.supplier.archives.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierGoal;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Repository
public class SupplierGoalDao extends HibernateDao<SupplierGoal, Long> {
}

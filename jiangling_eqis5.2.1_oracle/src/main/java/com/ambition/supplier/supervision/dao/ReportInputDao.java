package com.ambition.supplier.supervision.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.ReportInput;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Repository
public class ReportInputDao extends HibernateDao<ReportInput, Long> {
}

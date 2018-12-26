package com.ambition.supplier.admittance.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.InspectionGradeType;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Repository
public class InspectionGradeTypeDao extends HibernateDao<InspectionGradeType, Long> {
}

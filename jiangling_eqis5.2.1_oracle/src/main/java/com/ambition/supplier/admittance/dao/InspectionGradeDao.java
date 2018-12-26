package com.ambition.supplier.admittance.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.InspectionGrade;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Repository
public class InspectionGradeDao extends HibernateDao<InspectionGrade, Long> {
}

package com.ambition.supplier.archives.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.Certificate;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Repository
public class CertificateDao extends HibernateDao<Certificate, Long> {
}

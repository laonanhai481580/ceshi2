package com.ambition.epm.sample.dao;

import org.springframework.stereotype.Repository;

import com.ambition.epm.entity.Sample;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Repository
public class SampleDao extends HibernateDao<Sample,Long>{
	
}

package com.ambition.epm.exception.dao;

import org.springframework.stereotype.Repository;

import com.ambition.epm.entity.ExceptionSingle;
import com.norteksoft.product.orm.hibernate.HibernateDao;
@Repository
public class ExceptionDao extends HibernateDao<ExceptionSingle,Long>{
	
}

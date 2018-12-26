package com.ambition.gsm.maintain.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.Maintain;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class MaintainDao extends HibernateDao<Maintain,Long>{
	public Page<Maintain> list(Page<Maintain> page){
		return findPage(page,"from Maintain m");
	}
	public List<Maintain> getMaintain(){
		return find("from Maintain m where m.companyId=?", ContextUtils.getCompanyId());
	}
	public Page<Maintain> search(Page<Maintain> page){
		return searchPageByHql(page,"from Maintain m");
	}
} 

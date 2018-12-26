package com.ambition.gp.baseinfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gp.entity.Exemption;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class ExemptionDao extends HibernateDao<Exemption,Long>{
	public Page<Exemption> list(Page<Exemption> page){
		return findPage(page,"from Exemption m where m.companyId=?",ContextUtils.getCompanyId());
	}
	public List<Exemption> getExemption(){
		return find("from Exemption m where m.companyId=?", ContextUtils.getCompanyId());
	}
	public Page<Exemption> search(Page<Exemption> page){
		return searchPageByHql(page,"from Exemption m where m.companyId=?",ContextUtils.getCompanyId());
	}
}

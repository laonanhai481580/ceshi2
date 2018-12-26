package com.ambition.gp.gpSubstance.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gp.entity.GpSubstance;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class GpSubstanceDao extends HibernateDao<GpSubstance,Long>{
	public Page<GpSubstance> list(Page<GpSubstance> page){
		return findPage(page,"from GpSubstance m");
	}
	public List<GpSubstance> getGpSubstance(){
		return find("from GpSubstance m where m.companyId=?", ContextUtils.getCompanyId());
	}
	public Page<GpSubstance> search(Page<GpSubstance> page,Long id){
		return searchPageByHql(page,"from GpSubstance m where m.gpAverageMaterial.id= ? ",id);
		
	}
}

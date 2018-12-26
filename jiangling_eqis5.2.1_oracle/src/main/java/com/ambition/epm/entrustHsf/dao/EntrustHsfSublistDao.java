package com.ambition.epm.entrustHsf.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.epm.entity.EntrustHsfSublist;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
@Repository
public class EntrustHsfSublistDao extends HibernateDao<EntrustHsfSublist,Long>{
	public Page<EntrustHsfSublist> list(Page<EntrustHsfSublist> page){
		return findPage(page,"from EntrustHsfSublist e where e.companyId=?", ContextUtils.getCompanyId());
	}
	
	public List<EntrustHsfSublist> getEntrustHsfSublist(){
		return find("from EntrustHsfSublist e where e.companyId=?", ContextUtils.getCompanyId());
	}
	public List<EntrustHsfSublist> getByHsfId(Long id){
		List<EntrustHsfSublist> list= find("from EntrustHsfSublist e where e.companyId=? and e.hsfId=? ", ContextUtils.getCompanyId(),id.toString());
		return list;
	}
	public Page<EntrustHsfSublist> search(Page<EntrustHsfSublist> page){
		return searchPageByHql(page,"from EntrustHsfSublist e where e.companyId=?", ContextUtils.getCompanyId());
	}
	public List<EntrustHsfSublist> listOutOrIn(String subName){
		 String hql ="from EntrustHsfSublist e where e.companyId=? and ( e.testAfterOut is not null or e.supplierIn is not null or e.supplierOut is not null) ";
		 List<Object> searchParams = new ArrayList<Object>();
		 searchParams.add(ContextUtils.getCompanyId());
		 if(subName!=null ){
			hql=hql+" and e.factoryClassify=?";
			searchParams.add(subName);
		 }
		 return this.find(hql,searchParams.toArray());
	}
}

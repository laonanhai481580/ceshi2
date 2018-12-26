package com.ambition.epm.entrustOrt.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.epm.entity.EntrustOrtSublist;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class EntrustOrtSublistDao extends HibernateDao<EntrustOrtSublist,Long>{
	public Page<EntrustOrtSublist> list(Page<EntrustOrtSublist> page){
		return findPage(page,"from EntrustOrtSublist e where e.companyId=?",ContextUtils.getCompanyId());
	}
	public List<EntrustOrtSublist> getEntrustOrtSublist(){
		return find("from EntrustOrtSublist e where e.companyId=?", ContextUtils.getCompanyId());
	}
	public Page<EntrustOrtSublist> search(Page<EntrustOrtSublist> page){
		return searchPageByHql(page,"from EntrustOrtSublist e where e.companyId=?",ContextUtils.getCompanyId());
	}
	public List<EntrustOrtSublist> getByOrtId(Long id){
		List<EntrustOrtSublist> list= find("from EntrustOrtSublist e where e.companyId=? and e.ortId=? ", ContextUtils.getCompanyId(),id.toString());
		return list;
	}
	public List<EntrustOrtSublist> listOutOrIn(String subName){
		 String hql ="from EntrustOrtSublist e where e.companyId=? and ( e.defectNumberIn is not null or e.testAfterOut is not null or e.defectNumberOut is not null) ";
		 List<Object> searchParams = new ArrayList<Object>();
		 searchParams.add(ContextUtils.getCompanyId());
		 if(subName!=null ){
			hql=hql+" and e.factoryClassify=?";
			searchParams.add(subName);
		 }
		 return this.find(hql,searchParams.toArray());
	}
}

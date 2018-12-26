package com.ambition.epm.sample.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.epm.entity.SampleSublist;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class SampleSublistDao extends HibernateDao<SampleSublist,Long>{
	public Page<SampleSublist> list(Page<SampleSublist> page){
		return findPage(page,"from SampleSublist e");
	}
	
	public List<SampleSublist> getSampleSublist(){
		return find("from SampleSublist e where e.companyId=?", ContextUtils.getCompanyId());
	}
	public List<SampleSublist> getByHsfId(Long id){
		List<SampleSublist> list= find("from SampleSublist e where e.companyId=? and e.hsfId=? ", ContextUtils.getCompanyId(),id.toString());
		return list;
	}
	public Page<SampleSublist> search(Page<SampleSublist> page){
		return searchPageByHql(page,"from SampleSublist e");
	}
}

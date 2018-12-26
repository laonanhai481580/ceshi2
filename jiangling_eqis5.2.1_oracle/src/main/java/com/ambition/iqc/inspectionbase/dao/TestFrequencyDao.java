package com.ambition.iqc.inspectionbase.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.iqc.entity.TestFrequency;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class TestFrequencyDao extends HibernateDao<TestFrequency, Long> {
	public Page<TestFrequency> list(Page<TestFrequency> page){
		return findPage(page, "from TestFrequency d where d.companyId=?",ContextUtils.getCompanyId());
	}
	
	public List<TestFrequency> getAllTestFrequency(){
		return find("from TestFrequency d where d.companyId=?",ContextUtils.getCompanyId());
	}

    public Page<TestFrequency> search(Page<TestFrequency> page) {
        return searchPageByHql(page, "from TestFrequency d  where d.companyId=?",ContextUtils.getCompanyId());
    }   
}
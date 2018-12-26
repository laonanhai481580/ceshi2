package com.ambition.gp.gpSurvey.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gp.entity.GpSurvey;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class GpSurveyDao extends HibernateDao<GpSurvey,Long>{
	public Page<GpSurvey> list(Page<GpSurvey> page){
		return findPage(page,"from GpSurvey m where m.companyId=?",ContextUtils.getCompanyId());
	}
	public List<GpSurvey> getGpSurvey(){
		return find("from GpSurvey m where m.companyId=?", ContextUtils.getCompanyId());
	}
	public Page<GpSurvey> search(Page<GpSurvey> page){
		return searchPageByHql(page,"from GpSurvey m where m.companyId=?",ContextUtils.getCompanyId());
	}
}

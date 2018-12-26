package com.ambition.gsm.inspectionplan.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.GsmForeignReport;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class GsmForeignReportDao extends HibernateDao<GsmForeignReport,Long>{
	public Page<GsmForeignReport> list(Page<GsmForeignReport> page){
		return findPage(page,"from GsmForeignReport t");
	}
	public List<GsmForeignReport> getGsmForeignReport(){
		return find("from GsmForeignReport t where t.companyId=?", ContextUtils.getCompanyId());
	}
	public Page<GsmForeignReport> search(Page<GsmForeignReport> page){
		return searchPageByHql(page,"from GsmForeignReport t");
	}
	public GsmForeignReport listPlan(String pId){
		String hql = "from GsmForeignReport t where t.planId=?";
		List<GsmForeignReport> list = createQuery(hql,pId).list();
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
}

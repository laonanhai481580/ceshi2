package com.ambition.gsm.inspectionplan.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.GsmForeignReport;
import com.ambition.gsm.entity.GsmInnerCheckReport;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * GsmInnerCheckReportDao.java
 * @authorBy LPF
 *
 */
@Repository
public class GsmInnerCheckReportDao extends HibernateDao<GsmInnerCheckReport, Long>{
	
	public Page<GsmInnerCheckReport> search(Page<GsmInnerCheckReport> page){
		return searchPageByHql(page, "from GsmInnerCheckReport i where i.companyId = ?", ContextUtils.getCompanyId());
	}
	public GsmInnerCheckReport listPlan(String pId){
		String hql = "from GsmInnerCheckReport t where t.planId=?";
		List<GsmInnerCheckReport> list = createQuery(hql,pId).list();
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
}

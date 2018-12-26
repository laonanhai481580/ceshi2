package com.ambition.iqc.taskmonitor.dao;

import org.springframework.stereotype.Repository;

import com.ambition.iqc.entity.InspectionFromOverdueEmail;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class InspectionFromOverdueEmailDao extends HibernateDao<InspectionFromOverdueEmail, Long>{
	
	public Page<InspectionFromOverdueEmail> getPage(Page<InspectionFromOverdueEmail> page){
		String hql="from InspectionFromOverdueEmail email where email.companyId=? ";
		return this.searchPageByHql(page, hql,ContextUtils.getCompanyId());
	}
	
}

package com.ambition.util.useFile.dao;

import org.springframework.stereotype.Repository;

import com.ambition.util.useFile.entity.UseFile;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class UseFileDao extends HibernateDao<UseFile, Long> {
	
	public UseFile findById(Long id){
		String hql = "from UseFile i where i.id = ? and i.companyId = ?";
		return this.findUnique(hql, new Object[]{id,ContextUtils.getCompanyId()});
	}
}

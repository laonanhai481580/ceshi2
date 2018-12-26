package com.ambition.gsm.codeRules.dao;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.GsmCodeRules;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 计量编号规则一级类别(DAO)
 * @author 张顺治
 *
 */
@Repository
public class GsmCodeRulesDao extends HibernateDao<GsmCodeRules, Long> {
	
	/**
	 * 分页对象
	 * @param page
	 * @return
	 */
	public Page<GsmCodeRules> getPage(Page<GsmCodeRules> page){
		return this.searchPageByHql(page, "from GsmCodeRules g where g.companyId= ? ",ContextUtils.getCompanyId());
	}

	/**
	 * 根据对象获取对象
	 * @param type
	 * @return
	 */
	public List<GsmCodeRules> getGsmCodeRules(GsmCodeRules gsmCodeRules) { 
		StringBuffer sbHql = new StringBuffer("from GsmCodeRules g where g.companyId = ? and (g.measurementType = ? or g.typeCode = ?)");
		List<Object> searchParams =new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(gsmCodeRules.getMeasurementType());
		searchParams.add(gsmCodeRules.getTypeCode());
		if(gsmCodeRules.getId() != null){
			sbHql.append(" and g.id <> ? ");
			searchParams.add(gsmCodeRules.getId());
		}
		return this.find(sbHql.toString(),searchParams.toArray());
	}
}

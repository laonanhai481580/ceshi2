package com.ambition.gsm.codeSecRules.dao;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.GsmCodeRules;
import com.ambition.gsm.entity.GsmCodeSecRules;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 计量编号规则二级分类(DAO)
 * @author 张顺治
 *
 */
@Repository
public class GsmCodeSecRulesDao extends HibernateDao<GsmCodeSecRules, Long> {
	
	/**
	 * 分页对象
	 * @param page
	 * @return
	 */
	public Page<GsmCodeSecRules> getPage(Page<GsmCodeSecRules> page){
		return this.searchPageByHql(page, "from GsmCodeSecRules g where g.companyId= ?  ",ContextUtils.getCompanyId());
	}
	
	/**
	 * 根据一级对象获取分页对象
	 * @param page
	 * @param gsmCodeRules
	 * @return
	 */
	public Page<GsmCodeSecRules> getPage(Page<GsmCodeSecRules> page,GsmCodeRules gsmCodeRules) {
		StringBuffer sbHql =new StringBuffer("from GsmCodeSecRules g where g.companyId = ?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(gsmCodeRules != null){
			sbHql = sbHql.append(" and g.gsmCodeRules = ? ");
			searchParams.add(gsmCodeRules);
		}
		return this.searchPageByHql(page, sbHql.toString(), searchParams.toArray());
	}
	
	/**
	 * 根据对象获取对象
	 * @param gsmCodeSecRules
	 * @return
	 */
	public List<GsmCodeSecRules> getGsmCodeSecRules(GsmCodeSecRules gsmCodeSecRules) {
		StringBuffer sbHql =new StringBuffer("from GsmCodeSecRules g where g.companyId = ?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(StringUtils.isNotEmpty(gsmCodeSecRules.getSecondaryClassification()) || StringUtils.isNotEmpty(gsmCodeSecRules.getSecTypeCode())){
			sbHql = sbHql.append(" and (g.secondaryClassification = ? or g.secTypeCode = ?) ");
			searchParams.add(gsmCodeSecRules.getSecondaryClassification());
			searchParams.add(gsmCodeSecRules.getSecTypeCode());
		}
		if(gsmCodeSecRules.getGsmCodeRules() != null){
			sbHql = sbHql.append(" and g.gsmCodeRules = ? ");
			searchParams.add(gsmCodeSecRules.getGsmCodeRules());
		}
		if(gsmCodeSecRules.getId() != null){
			sbHql = sbHql.append(" and g.id <> ? ");
			searchParams.add(gsmCodeSecRules.getId());
		}
		return this.find(sbHql.toString(), searchParams.toArray());
	} 
	
	/**
	 * 根据一级对象获取对象
	 * @param gsmCodeRules
	 * @return
	 */
	public List<GsmCodeSecRules> getGsmCodeSecRules(GsmCodeRules gsmCodeRules){
		StringBuffer sbHql =new StringBuffer("from GsmCodeSecRules g where g.companyId = ?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(gsmCodeRules != null){
			sbHql = sbHql.append(" and g.gsmCodeRules = ? ");
			searchParams.add(gsmCodeRules);
		}
		return this.find(sbHql.toString(), searchParams.toArray());
	} 
}

package com.ambition.carmfg.baseinfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.FormCodingRule;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class FormCodeRuleDao extends HibernateDao<FormCodingRule,Long>{
	public List<FormCodingRule> getAllCodingRules(){
		return find("from FormCodingRule c where c.companyId=?",ContextUtils.getCompanyId());
	}
	public List<FormCodingRule> getAllCodingRules(Long companyId){
		return find("from FormCodingRule c where c.companyId=?",companyId);
	}
}

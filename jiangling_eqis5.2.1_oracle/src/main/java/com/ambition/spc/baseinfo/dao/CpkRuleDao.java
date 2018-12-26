package com.ambition.spc.baseinfo.dao;

import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.CpkRule;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 
 * 类名:CPK规则表(com.ambition.spc.baseinfo.dao)
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2016年7月5日 发布
 */
@Repository
public class CpkRuleDao extends HibernateDao<CpkRule, Long> {
	
	public Page<CpkRule> list(Page<CpkRule> page){
		 return searchPageByHql(page, "from CpkRule cpkRule where cpkRule.companyId=?", ContextUtils.getCompanyId());
	}
}

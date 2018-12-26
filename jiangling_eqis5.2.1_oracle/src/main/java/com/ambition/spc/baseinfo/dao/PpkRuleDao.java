package com.ambition.spc.baseinfo.dao;

import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.PpkRule;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;

/**
 * 类名:PPK规则表(com.ambition.spc.baseinfo.dao)
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2017年5月2日 发布
 */
@Repository
public class PpkRuleDao extends HibernateDao<PpkRule, Long> {
	
	public Page<PpkRule> list(Page<PpkRule> page){
		 return searchPageByHql(page, "from PpkRule ppkRule order by trim(translate(ppkRule.name, trim(translate(ppkRule.name, '1234567890', ' ')), ' ')) asc");
	}
}

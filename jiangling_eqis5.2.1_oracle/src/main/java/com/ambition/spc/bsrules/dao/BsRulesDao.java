package com.ambition.spc.bsrules.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.BsRules;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * LayerTypeDao.java
 * @authorBy wanglf
 *
 */
@Repository
public class BsRulesDao extends HibernateDao<BsRules, Long> {
	
	
	public Page<BsRules> list(Page<BsRules> page){
		 return searchPageByHql(page, "from BsRules bsRules where bsRules.companyId=?", ContextUtils.getCompanyId());
	}
	
	public List<BsRules> getBsRules(){
		return find("from BsRules bsRules where companyId=?", ContextUtils.getCompanyId());
	}
	public Page<BsRules> search(Page<BsRules> page) {
      return searchPageByHql(page, "from BsRules bsRules where bsRules.companyId=?", ContextUtils.getCompanyId());
  }
	

}

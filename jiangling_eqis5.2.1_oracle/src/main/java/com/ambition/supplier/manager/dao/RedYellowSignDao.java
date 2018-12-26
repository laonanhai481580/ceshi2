package com.ambition.supplier.manager.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierGoal;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**    
 * RedYellowSignDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class RedYellowSignDao extends HibernateDao<SupplierGoal,Long>{
	/**
	 * 查询供应商红黄牌
	 * @param page
	 * @return
	 */
	public Page<SupplierGoal> search(Page<SupplierGoal> page){
		return searchPageByHql(page, "from SupplierGoal s where s.companyId = ?", ContextUtils.getCompanyId());
	}
	public List<SupplierGoal> getSupplierGoal(int year,String businessUnitCode,String importance) {
		String hql = "from SupplierGoal s where s.companyId = ? and s.evaluateYear=? and s.importance = ?";
		return find(hql,ContextUtils.getCompanyId(),year,importance);
	}
	public List<SupplierGoal> getEvaluateGrade(int year,String businessUnitCode,String evaluateGrade) {
		String hql = "from SupplierGoal s where s.companyId = ? and s.evaluateYear=? and s.evaluateGrade=?";
		return find(hql,ContextUtils.getCompanyId(),year,evaluateGrade);
	}
	public Page<SupplierGoal> getSupplierGoalPage(Page<SupplierGoal> page,int year, String importance) {
		String hql = "from SupplierGoal s where s.companyId = ? and s.evaluateYear=? and s.evaluateGrade=?";
		return searchPageByHql(page,hql,ContextUtils.getCompanyId(),year,importance);
	}
}

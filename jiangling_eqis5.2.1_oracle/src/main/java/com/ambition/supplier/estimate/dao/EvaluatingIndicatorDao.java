package com.ambition.supplier.estimate.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.EvaluatingIndicator;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class EvaluatingIndicatorDao extends HibernateDao<EvaluatingIndicator, Long> {
	/**
	 * 获取顶级指标
	 * @return
	 */
	public List<EvaluatingIndicator> getTopEvaluatingIndicators(){
		return find("from EvaluatingIndicator e where e.companyId=? and e.parent is null",new Object[]{ContextUtils.getCompanyId()});
	}
}

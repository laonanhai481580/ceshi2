package com.ambition.iqc.samplestandard.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.iqc.entity.SampleTransitionRule;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 抽样方案转移规则
 * @author 赵骏
 *
 */
@Repository
public class SampleTransitionRuleDao extends HibernateDao<SampleTransitionRule,Long>{
	/**
	 * 查询所有的转移规则
	 * @return
	 */
	public List<SampleTransitionRule> list(){
		return find("from SampleTransitionRule s where s.companyId = ?",ContextUtils.getCompanyId());
	}
}

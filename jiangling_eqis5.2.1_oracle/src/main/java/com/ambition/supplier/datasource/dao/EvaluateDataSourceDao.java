package com.ambition.supplier.datasource.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.EvaluateDataSource;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 类名:供应商业绩评价自动数据来源DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：自动数据来源的数据库访问,查询,新增,删除</p>
 * @author  赵骏
 * @version 1.00 2013-4-19 发布
 */
@Repository
public class EvaluateDataSourceDao extends HibernateDao<EvaluateDataSource, Long> {
	/**
	  * 方法名:查询数据来源 
	  * <p>功能说明：</p>
	  * @param page
	  * @return Page
	 */
	public Page<EvaluateDataSource> search(Page<EvaluateDataSource> page){
		return searchPageByHql(page, "from EvaluateDataSource d where d.companyId = ?", ContextUtils.getCompanyId());
	}
	
	/**
	  * 方法名:获取数据来源 
	  * <p>功能说明：根据规则编码获取数据来源对象</p>
	  * @param ruleCode
	  * @return GradeRule
	 */
	public EvaluateDataSource getEvaluateDataSourceByCode(String code){
		String hql = "from EvaluateDataSource d where d.companyId = ? and d.code = ? ";
		List<EvaluateDataSource> gradeRules = find(hql,new Object[]{ContextUtils.getCompanyId(),code});
		if(gradeRules.isEmpty()){
			return null;
		}else{
			return gradeRules.get(0);
		}
	}
}

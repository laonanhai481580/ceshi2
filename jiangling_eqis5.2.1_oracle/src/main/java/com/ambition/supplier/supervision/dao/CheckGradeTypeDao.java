package com.ambition.supplier.supervision.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.CheckGradeType;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class CheckGradeTypeDao extends HibernateDao<CheckGradeType, Long> {
	/**
	 * 查询顶级稽查评分项目类型
	 * @return
	 */
	public List<CheckGradeType> getTopCheckGradeTypes(String type){
		String hql = "from CheckGradeType c where c.parent is null and c.companyId=? and c.type = ? order by c.orderNum";
		return find(hql,ContextUtils.getCompanyId(),type);
	}
}

package com.ambition.supplier.admittance.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierInvestigate;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:供应商调查DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2013-4-28 发布
 */
@Repository
public class SupplierInvestigateDao extends HibernateDao<SupplierInvestigate, Long> {
	/**
	 * 查询供应商调查记录
	 * @param page
	 * @return
	 */
	public Page<SupplierInvestigate> search(Page<SupplierInvestigate> page){
		return searchPageByHql(page, "from SupplierInvestigate s where s.companyId = ?", ContextUtils.getCompanyId());
	}
	
}

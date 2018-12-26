package com.ambition.supplier.manager.dao;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierQcds;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
/**
 * 类名:供应商评价qcds汇总
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2013-6-18 发布
 */
@Repository
public class SupplierQcdsDao extends HibernateDao<SupplierQcds, Long> {
	/**
	 * 方法名:供应商评价QCDS查询 
	  * <p>功能说明：</p>
	  * @param page
	  * @param evaluateYear
	  * @param evaluateMonth
	  * @return
	 */
	public Page<SupplierQcds> search(Page<SupplierQcds> page,Integer evaluateYear,Integer evaluateMonth){
		String hql = "from SupplierQcds i where i.evaluateYear = ? and i.evaluateMonth = ?";
		return searchPageByHql(page,hql, evaluateYear,evaluateMonth);
	}
}

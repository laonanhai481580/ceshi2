package com.ambition.cost.collection.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.cost.entity.CostRecord;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:成本数据DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2014-11-16 发布
 */
@Repository
public class CostCollectionDao extends HibernateDao<CostRecord, Long> {
	public Page<CostRecord> list(Page<CostRecord> page,String costType){
		return searchPageByHql(page,"from CostRecord g where g.companyId =? and g.costType=?",ContextUtils.getCompanyId(),costType);
	}
	public List<CostRecord> getAllCostRecords(Integer year){
		return find("from CostRecord g where g.companyId =?",new Object[]{ContextUtils.getCompanyId()});
	}
}

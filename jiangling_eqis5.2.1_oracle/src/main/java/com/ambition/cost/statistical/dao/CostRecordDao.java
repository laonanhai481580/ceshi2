package com.ambition.cost.statistical.dao;

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
 * @author  赵骏
 * @version 1.00 2014-11-29 发布
 */
@Repository
public class CostRecordDao extends HibernateDao<CostRecord, Long> {
	public Page<CostRecord> list(Page<CostRecord> page,String sourceType){
		return searchPageByHql(page,"from CostRecord g where g.companyId =? and g.sourceType=?",ContextUtils.getCompanyId(),sourceType);
	}
	public List<CostRecord> getAllCostRecords(Integer year){
		return find("from CostRecord g where g.companyId =?",new Object[]{ContextUtils.getCompanyId()});
	}
}

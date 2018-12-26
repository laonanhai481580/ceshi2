package com.ambition.cost.composingdetail.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.cost.entity.CostConvert;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:集成时成本转换规则DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2014-11-29 发布
 */
@Repository
public class CostConvertDao extends HibernateDao<CostConvert, Long> {
	public Page<CostConvert> list(Page<CostConvert> page){
		return searchPageByHql(page,"from CostConvert g where g.companyId =?",ContextUtils.getCompanyId());
	}
	public List<CostConvert> getAllCostConverts(Integer year){
		return find("from CostConvert g where g.companyId =?",new Object[]{ContextUtils.getCompanyId()});
	}
}

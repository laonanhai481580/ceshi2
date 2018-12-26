package com.ambition.supplier.evaluate.dao;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.PerformanceEvaluate;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
/**
 * 类名: PerformanceEvaluateDao 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：供应商评价排名</p>
 * @author  刘承斌
 * @version 1.00  2015-3-23 下午5:51:49  发布
 */
@Repository
public class PerformanceEvaluateDao extends HibernateDao<PerformanceEvaluate, Long> {
		
	public Page<PerformanceEvaluate> list(Page<PerformanceEvaluate> page){
		return findPage(page, "from PerformanceEvaluate t order by t.id desc");
	}
	
	public List<PerformanceEvaluate> getAllPerformanceEvaluate(){
		return find("from PerformanceEvaluate t  order by t.id desc");
	}

    public Page<PerformanceEvaluate> search(Page<PerformanceEvaluate> page,String where) {
        return searchPageByHql(page, "from PerformanceEvaluate t "+where+" order by t.id desc");
    }
}

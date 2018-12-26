package com.ambition.carmfg.ort.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.OrtPlan;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
/**
 * 
 * 类名:ORT计划Dao
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author LPF
 * @version 1.00 2016年8月31日 发布
 */
@Repository
public class OrtPlanDao extends HibernateDao<OrtPlan,Long>{
	public Page<OrtPlan> list(Page<OrtPlan> page){
		return findPage(page, "from OrtPlan d");
	}
	
	public List<OrtPlan> getAllOrtPlan(){
		return find("from OrtPlan d");
	}

    public Page<OrtPlan> search(Page<OrtPlan> page) {
        return searchPageByHql(page, "from OrtPlan d ");
    }
}

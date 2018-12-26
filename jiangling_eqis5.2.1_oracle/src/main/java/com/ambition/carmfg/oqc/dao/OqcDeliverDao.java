package com.ambition.carmfg.oqc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.OqcDeliver;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:OQC出货报告DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月1日 发布
 */
@Repository
public class OqcDeliverDao extends HibernateDao<OqcDeliver,Long>{
	public Page<OqcDeliver> list(Page<OqcDeliver> page){
		return findPage(page, "from OqcDeliver d");
	}
	
	public List<OqcDeliver> getAllOqcDeliver(){
		return find("from OqcDeliver d");
	}

    public Page<OqcDeliver> search(Page<OqcDeliver> page,String businessUnit) {
        return searchPageByHql(page, "from OqcDeliver d where d.companyId=? and d.businessUnitName=? ",ContextUtils.getCompanyId(),businessUnit);
    }
}

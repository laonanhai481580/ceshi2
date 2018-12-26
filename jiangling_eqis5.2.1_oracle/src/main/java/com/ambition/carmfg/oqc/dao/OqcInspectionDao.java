package com.ambition.carmfg.oqc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.OqcInspection;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:OQC检验DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年8月31日 发布
 */
@Repository
public class OqcInspectionDao extends HibernateDao<OqcInspection,Long>{
	public Page<OqcInspection> list(Page<OqcInspection> page){
		return findPage(page, "from OqcInspection d");
	}
	
	public List<OqcInspection> getAllOqcInspection(){
		return find("from OqcInspection d");
	}

    public Page<OqcInspection> search(Page<OqcInspection> page) {
        return searchPageByHql(page, "from OqcInspection d ");
    }
    
	public Page<OqcInspection> searchByBusinessUnit(Page<OqcInspection> page,String businessUnit){
		return searchPageByHql(page,"from OqcInspection o where o.companyId=? and o.businessUnitName=?  ",ContextUtils.getCompanyId(),businessUnit);
	}
}

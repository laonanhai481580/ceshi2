package com.ambition.aftersales.vlrr.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.aftersales.entity.VlrrData;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:VLRR数据DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Repository
public class VlrrDataDao extends HibernateDao<VlrrData,Long>{
	public Page<VlrrData> list(Page<VlrrData> page){
		return findPage(page, "from VlrrData d where d.companyId=?",ContextUtils.getCompanyId());
	}
	
	public List<VlrrData> getAllVlrrData(){
		return find("from VlrrData d where d.companyId=?",ContextUtils.getCompanyId());
	}

    public Page<VlrrData> search(Page<VlrrData> page) {
        return searchPageByHql(page, "from VlrrData d where d.companyId=?",ContextUtils.getCompanyId());
    }
    
	public Page<VlrrData> searchByBusinessUnit(Page<VlrrData> page,String businessUnit){
		return searchPageByHql(page,"from VlrrData o where o.companyId=? and o.businessUnitName=? ",ContextUtils.getCompanyId(),businessUnit);
	}
}

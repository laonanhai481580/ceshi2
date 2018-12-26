package com.ambition.aftersales.lar.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.aftersales.entity.LarData;
import com.ambition.aftersales.entity.VlrrData;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:LAR批次合格率DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Repository
public class LarDataDao extends HibernateDao<LarData,Long>{
	public Page<LarData> list(Page<LarData> page){
		return findPage(page, "from LarData d where d.companyId=?",ContextUtils.getCompanyId());
	}
	
	public List<LarData> getAllLarData(){
		return find("from LarData d where d.companyId=?",ContextUtils.getCompanyId());
	}

    public Page<LarData> search(Page<LarData> page) {
        return searchPageByHql(page, "from LarData d where d.companyId=?",ContextUtils.getCompanyId());
    }
    public Page<LarData> searchByBusinessUnit(Page<LarData> page,String businessUnit){
		return searchPageByHql(page,"from LarData o where o.companyId=? and o.businessUnitName=? ",ContextUtils.getCompanyId(),businessUnit);
	}
}

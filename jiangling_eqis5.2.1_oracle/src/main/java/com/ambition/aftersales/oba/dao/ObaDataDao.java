package com.ambition.aftersales.oba.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.aftersales.entity.ObaData;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:OBA数据DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Repository
public class ObaDataDao extends HibernateDao<ObaData,Long>{
	public Page<ObaData> list(Page<ObaData> page){
		return findPage(page, "from ObaData d where d.companyId=?",ContextUtils.getCompanyId());
	}
	
	public List<ObaData> getAllObaData(){
		return find("from ObaData d where d.companyId=?",ContextUtils.getCompanyId());
	}

    public Page<ObaData> search(Page<ObaData> page) {
        return searchPageByHql(page, "from ObaData d where d.companyId=?",ContextUtils.getCompanyId());
    }
    
	public Page<ObaData> searchByBusinessUnit(Page<ObaData> page,String businessUnit){
		return searchPageByHql(page,"from ObaData o where o.companyId=? and o.businessUnitName=? ",ContextUtils.getCompanyId(),businessUnit);
	}
}

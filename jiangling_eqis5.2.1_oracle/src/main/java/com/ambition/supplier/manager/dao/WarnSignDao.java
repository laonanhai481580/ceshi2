package com.ambition.supplier.manager.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.WarnSign;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * WarnSignDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class WarnSignDao extends HibernateDao<WarnSign, Long> {
	/**
	 * 查询供应商红黄牌
	 * @param page
	 * @return
	 */
	public Page<WarnSign> search(Page<WarnSign> page){
		return searchPageByHql(page, "from WarnSign w where w.companyId = ?", ContextUtils.getCompanyId());
	}
	public Page<WarnSign> list(Page<WarnSign> page){
		return searchPageByHql(page,"from WarnSign w where w.companyId=?",ContextUtils.getCompanyId());
	}
	public List<WarnSign> getAllWarSing() {
        return find("from WarnSign w where w.companyId=? order by w.id",ContextUtils.getCompanyId());
    }
	
}

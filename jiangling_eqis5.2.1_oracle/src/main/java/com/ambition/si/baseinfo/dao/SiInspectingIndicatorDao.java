package com.ambition.si.baseinfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.si.entity.SiInspectingIndicator;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * InspectionIndicatorDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class SiInspectingIndicatorDao extends HibernateDao<SiInspectingIndicator,Long>{
	
	/**
	 * 获取顶级检验项目指标
	 * @return
	 */
	public List<SiInspectingIndicator> getTopInspectingIndicators(){
		return find("from SiInspectingIndicator i where i.companyId=? and i.parent is null",new Object[]{ContextUtils.getCompanyId()});
	}
	
	/**
	 * 根据分页查询最新版本的检验标准
	 * @param page
	 * @return
	 */
	public Page<SiInspectingIndicator> listMaxVersion(Page<SiInspectingIndicator> page){
		return searchPageByHql(page, "from SiInspectingIndicator i where i.companyId=? and i.isMax=?",ContextUtils.getCompanyId(),true);
	}
}

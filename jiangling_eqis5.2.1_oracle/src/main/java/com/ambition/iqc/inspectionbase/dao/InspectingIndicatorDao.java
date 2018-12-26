package com.ambition.iqc.inspectionbase.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.iqc.entity.InspectingIndicator;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * InspectionIndicatorDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class InspectingIndicatorDao extends HibernateDao<InspectingIndicator,Long>{
	
	/**
	 * 获取顶级检验项目指标
	 * @return
	 */
	public List<InspectingIndicator> getTopInspectingIndicators(){
		return find("from InspectingIndicator i where i.companyId=? and i.parent is null",new Object[]{ContextUtils.getCompanyId()});
	}
	
	/**
	 * 根据分页查询最新版本的检验标准
	 * @param page
	 * @return
	 */
	public Page<InspectingIndicator> listMaxVersion(Page<InspectingIndicator> page){
		return searchPageByHql(page, "from InspectingIndicator i where i.companyId=? and i.isMax=?",ContextUtils.getCompanyId(),true);
	}
	
}

package com.ambition.iqc.inspectionbase.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.iqc.entity.InspectingItem;
import com.ambition.iqc.entity.ItemIndicator;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * ItemIndicatorDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class ItemIndicatorDao extends HibernateDao<ItemIndicator,Long>{
	
	/**
	 * 查询所有已经设置的模型指标
	 * @return
	 */
	public List<ItemIndicator> getAllItemIndicators(Long parentItemIndicatorId){
		return find("from ItemIndicator i where i.companyId=? and i.inspectingIndicator.id=? order by i.orderNum",new Object[]{ContextUtils.getCompanyId(),parentItemIndicatorId});
	}
	
	/**
	 * 查询所有已经设置的模型指标
	 * @return
	 */
	public List<ItemIndicator> getAllItemIndicators(){
		return find("from ItemIndicator i where i.companyId=?",new Object[]{ContextUtils.getCompanyId()});
	}
	
	/**
	 * 根据检验项目指标查询已经设置的检验项目
	 * @param evaluatingIndicator
	 * @return
	 */
	public List<ItemIndicator> getItemIndicatorsByInspectingItem(InspectingItem inspectingItem){
		return find("from ItemIndicator i where i.companyId = ? and i.inspectingItem = ?",new Object[]{ContextUtils.getCompanyId(),inspectingItem});
		
	}
}

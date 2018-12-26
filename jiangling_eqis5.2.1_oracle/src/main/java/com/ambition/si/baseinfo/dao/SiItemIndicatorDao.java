package com.ambition.si.baseinfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.si.entity.SiInspectingItem;
import com.ambition.si.entity.SiItemIndicator;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * ItemIndicatorDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class SiItemIndicatorDao extends HibernateDao<SiItemIndicator,Long>{
	
	/**
	 * 查询所有已经设置的模型指标
	 * @return
	 */
	public List<SiItemIndicator> getAllItemIndicators(Long parentItemIndicatorId){
		return find("from SiItemIndicator i where i.companyId=? and i.siInspectingIndicator.id=? order by i.orderNum",new Object[]{ContextUtils.getCompanyId(),parentItemIndicatorId});
	}
	
	/**
	 * 查询所有已经设置的模型指标
	 * @return
	 */
	public List<SiItemIndicator> getAllItemIndicators(){
		return find("from SiItemIndicator i where i.companyId=?",new Object[]{ContextUtils.getCompanyId()});
	}
	
	/**
	 * 根据检验项目指标查询已经设置的检验项目
	 * @param siInspectingItem
	 * @return
	 */
	public List<SiItemIndicator> getItemIndicatorsByInspectingItem(SiInspectingItem siInspectingItem){
		return find("from SiItemIndicator i where i.companyId = ? and i.siInspectingItem = ?",new Object[]{ContextUtils.getCompanyId(),siInspectingItem});
		
	}
	
	/**
	 * 根据分页查询
	 * @param page
	 * @return
	 */
	public Page<SiItemIndicator> list(Page<SiItemIndicator> page){
		return searchPageByHql(page, "from SiItemIndicator i where i.companyId=?",ContextUtils.getCompanyId());
	}
	
	/**
	 * 分页查询质量特性
	 * @param page
	 * @return
	 */
	public Page<SiItemIndicator> listFeatures(Page<SiItemIndicator> page){
		return searchPageByHql(page, "from SiItemIndicator i where i.companyId=? and length(i.featureId)>0",ContextUtils.getCompanyId());
	}
}

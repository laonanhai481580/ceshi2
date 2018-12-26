package com.ambition.carmfg.inspectionbase.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.MfgInspectingItem;
import com.ambition.carmfg.entity.MfgItemIndicator;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * ItemIndicatorDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class MfgItemIndicatorDao extends HibernateDao<MfgItemIndicator,Long>{
	
	/**
	 * 查询所有已经设置的模型指标
	 * @return
	 */
	public List<MfgItemIndicator> getAllItemIndicators(Long parentItemIndicatorId){
		return find("from MfgItemIndicator i where i.companyId=? and i.mfgInspectingIndicator.id=? order by i.orderNum",new Object[]{ContextUtils.getCompanyId(),parentItemIndicatorId});
	}
	
	/**
	 * 查询所有已经设置的模型指标
	 * @return
	 */
	public List<MfgItemIndicator> getAllItemIndicators(){
		return find("from MfgItemIndicator i where i.companyId=?",new Object[]{ContextUtils.getCompanyId()});
	}
	
	/**
	 * 根据检验项目指标查询已经设置的检验项目
	 * @param mfgInspectingItem
	 * @return
	 */
	public List<MfgItemIndicator> getItemIndicatorsByInspectingItem(MfgInspectingItem mfgInspectingItem){
		return find("from MfgItemIndicator i where i.companyId = ? and i.mfgInspectingItem = ?",new Object[]{ContextUtils.getCompanyId(),mfgInspectingItem});
		
	}
	
	/**
	 * 根据分页查询
	 * @param page
	 * @return
	 */
	public Page<MfgItemIndicator> list(Page<MfgItemIndicator> page){
		return searchPageByHql(page, "from MfgItemIndicator i where i.companyId=?",ContextUtils.getCompanyId());
	}
	
	/**
	 * 分页查询质量特性
	 * @param page
	 * @return
	 */
	public Page<MfgItemIndicator> listFeatures(Page<MfgItemIndicator> page){
		return searchPageByHql(page, "from MfgItemIndicator i where i.companyId=? and length(i.featureId)>0",ContextUtils.getCompanyId());
	}
}

package com.ambition.supplier.estimate.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.EvaluatingIndicator;
import com.ambition.supplier.entity.ModelIndicator;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class ModelIndicatorDao extends HibernateDao<ModelIndicator, Long> {
	/**
	 * 查询所有已经设置的模型指标
	 * @return
	 */
	public List<ModelIndicator> getAllModelIndicators(Long parentModelIndicatorId){
		return find("from ModelIndicator m where m.companyId=? and (m.estimateModel.id=? or m.estimateModel.parentEstimateModelId=?)",new Object[]{ContextUtils.getCompanyId(),parentModelIndicatorId,parentModelIndicatorId});
	}
	
	/**
	 * 查询所有已经设置的模型指标
	 * @return
	 */
	public List<ModelIndicator> getAllModelIndicators(){
		return find("from ModelIndicator m where m.companyId=?",new Object[]{ContextUtils.getCompanyId()});
	}
	
	/**
	 * 根据评价指标查询已经设置的模型指标
	 * @param evaluatingIndicator
	 * @return
	 */
	public List<ModelIndicator> getModelIndicatorsByEvaluatingIndicator(EvaluatingIndicator evaluatingIndicator){
		return find("from ModelIndicator m where m.companyId = ? and m.evaluatingIndicator = ?",new Object[]{ContextUtils.getCompanyId(),evaluatingIndicator});
		
	}
}

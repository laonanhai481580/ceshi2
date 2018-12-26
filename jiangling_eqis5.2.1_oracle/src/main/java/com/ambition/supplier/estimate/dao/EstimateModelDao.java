package com.ambition.supplier.estimate.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.EstimateModel;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class EstimateModelDao extends HibernateDao<EstimateModel, Long> {
	/**
	 * 获取顶级评价模型分类
	 * @return
	 */
	public List<EstimateModel> getTopEstimateModels(){
		return find("from EstimateModel e where e.companyId=? and e.parent is null",new Object[]{ContextUtils.getCompanyId()});
	}
	
	/**
	 * 根据名称和上级查询评价模型
	 * @param estimateModelName,parent
	 * @return
	 */
	public EstimateModel getEstimateModelByName(String estimateModelName,EstimateModel parent){
		String hql = "from EstimateModel e where e.companyId = ? and e.name = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(estimateModelName);
		if(parent == null){
			hql += " and e.parent is null";
		}else{
			hql += " and e.parent = ?";
			params.add(parent);
		}
		List<EstimateModel> estimateModels = find(hql,params.toArray());
		if(estimateModels.isEmpty()){
			return null;
		}else{
			return estimateModels.get(0);
		}
	}
	
}

package com.ambition.spc.improvemeasure.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.spc.entity.ImprovementMeasure;
import com.ambition.spc.improvemeasure.dao.MeasureDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**    
 * MeasureManager.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class MeasureManager {
	@Autowired
	private MeasureDao measureDao;
	
	private Boolean isExistImprovementMeasure(Long id,String measureNo){
		String hql = "select count(*) from ImprovementMeasure i where i.companyId = ? and i.measureNo = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(measureNo);
		if(id != null){
			hql += " and i.id <> ?";
			params.add(id);
		}
		Query query = measureDao.getSession().createQuery(hql);
		for(int i=0;i<params.size();i++){
			query.setParameter(i, params.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if(Integer.valueOf(list.get(0).toString()) > 0){
			return true;
		}else{
			return false;
		}
	}
	
	public ImprovementMeasure getImprovementMeasure(Long id){
		return measureDao.get(id);
	}
	
	public void deleteImprovementMeasure(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id:ids){
			measureDao.delete(Long.valueOf(id));
		}
	}
	
	public void saveImprovementMeasure(ImprovementMeasure improvementMeasure){
		if(StringUtils.isEmpty(improvementMeasure.getMeasureNo())){
			throw new RuntimeException("措施代码不能为空!");
		}
		if(StringUtils.isEmpty(improvementMeasure.getMeasure())){
			throw new RuntimeException("改善措施不能为空!");
		}
		if(isExistImprovementMeasure(improvementMeasure.getId(),improvementMeasure.getMeasureNo())){
			throw new RuntimeException("已存在相同的改善措施！");
		}
		measureDao.save(improvementMeasure);
	}
	
	public Page<ImprovementMeasure> getPage(Page<ImprovementMeasure> page){
		return measureDao.search(page);
	}
}

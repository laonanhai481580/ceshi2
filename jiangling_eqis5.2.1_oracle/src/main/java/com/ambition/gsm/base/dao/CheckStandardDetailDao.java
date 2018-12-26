package com.ambition.gsm.base.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.CheckStandardDetail;
import com.ambition.gsm.entity.GsmCheckItem;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * CheckStandardDetailDao.java
 * @authorBy LPF
 *
 */
@Repository
public class CheckStandardDetailDao extends HibernateDao<CheckStandardDetail,Long>{
	/**
	 * 查询所有已经设置的项目
	 * @return
	 */
	public List<CheckStandardDetail> getAllDetail(Long checkStandardId){
		return find("from CheckStandardDetail i where i.companyId=? and i.checkStandard.id=? ",new Object[]{ContextUtils.getCompanyId(),checkStandardId});
	}
	/**
	 * 根据检验项目查询已经设置的检验项目
	 * @param mfgInspectingItem
	 * @return
	 */
	public List<CheckStandardDetail> getDetailByCheckItem(GsmCheckItem checkItem){
		return find("from CheckStandardDetail i where i.companyId = ? and i.checkItem = ?",new Object[]{ContextUtils.getCompanyId(),checkItem});
		
	}
    
    public Page<CheckStandardDetail> searchByParams(Page<CheckStandardDetail> page, String measurementName,String measurementSpecification,String manufacturer){
		List<Object> searchParams = new ArrayList<Object>();
		String hql ="from CheckStandardDetail i where i.companyId = ? ";
		searchParams.add(ContextUtils.getCompanyId());
		if(measurementName!=null&&measurementName!=""){
			hql=hql+" and i.checkStandard.measurementName = ? ";
			searchParams.add(measurementName);
		}
		if(measurementSpecification!=null&&measurementSpecification!=""){
			hql=hql+" and i.checkStandard.measurementSpecification = ? ";
			searchParams.add(measurementSpecification);
		}
		if(manufacturer!=null&&manufacturer!=""){
			hql=hql+" and i.checkStandard.manufacturer = ? ";
			searchParams.add(manufacturer);
		}		
		return searchPageByHql(page, hql.toString(), searchParams.toArray());
    }   	
	
	
}

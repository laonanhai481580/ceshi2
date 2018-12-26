package com.ambition.spc.abnormal.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.AbnormalInfo;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * AbnormalInfoDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class AbnormalInfoDao extends HibernateDao<AbnormalInfo,Long>{
	
	public Page<AbnormalInfo> getPage(Page<AbnormalInfo> page){
		return searchPageByHql(page, "from AbnormalInfo a where a.companyId = ?", ContextUtils.getCompanyId());
	}
	
	public Page<AbnormalInfo> getPage(Page<AbnormalInfo> page,Long qualityFeatureId,Date startDate,Date endDate,Integer lastAmount){
		String hql = "from AbnormalInfo a where a.companyId = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		if(qualityFeatureId != null){
			hql += " and a.qualityFeature.id = ?";
			params.add(qualityFeatureId);
		}
		if(startDate != null){
			hql += " and a.occurDate >= ?";
			params.add(startDate);
		}
		if(endDate != null){
			hql += " and a.occurDate <= ?";
			params.add(endDate);
		}
		return searchPageByHql(page, hql,params.toArray());
	}
	public List<AbnormalInfo> getAbnormalInfo(Long qualityFeatureId,Date startDate,Date endDate){
		return find( "from AbnormalInfo a where a.companyId = ? and a.qualityFeature.id=? and occurDate>=? and occurDate<?", ContextUtils.getCompanyId(),qualityFeatureId,startDate,endDate);
	}
	public List<AbnormalInfo> getHandleAbnormalInfo(Long qualityFeatureId,Date startDate,Date endDate,String priState){
		return find( "from AbnormalInfo a where a.companyId = ? and a.qualityFeature.id=? and occurDate>=? and occurDate<? and priState=?", ContextUtils.getCompanyId(),qualityFeatureId,startDate,endDate,priState);
	}
}

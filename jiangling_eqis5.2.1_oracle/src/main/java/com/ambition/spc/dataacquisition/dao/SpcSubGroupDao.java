package com.ambition.spc.dataacquisition.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.entity.SpcSubGroup;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * SpcSubGroupDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class SpcSubGroupDao extends HibernateDao<SpcSubGroup, Long> {
	public SpcSubGroup getSpcSubGroupByFeature(QualityFeature qualityFeature){
		String hql = "from SpcSubGroup s where s.companyId = ? and s.qualityFeature = ?";
		List<SpcSubGroup> list = this.find(hql, ContextUtils.getCompanyId(),qualityFeature);
		if(list.size() != 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SpcSubGroup> spcSubGroupList(Long featureId,Date startDate,Date endDate) {
		String hql="select spcSubGroup from SpcSubGroup  spcSubGroup  where  spcSubGroup.qualityFeature.id=? ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(featureId);
		if(startDate!=null){
			hql=hql+" and spcSubGroup.createdTime>=?";
			searchParams.add(startDate);
		}
		if(endDate!=null){
			hql=hql+"  and spcSubGroup.createdTime<?";
			searchParams.add(endDate);
		}
		List list=null;
		try {
			list = find(hql,searchParams.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	} 

	public List<SpcSubGroup> spcSubGroupListByNo(Long featureId,int  beginNo,int  endNo) {
		String hql = "from SpcSubGroup s where s.companyId = ? and s.qualityFeature.id = ? and s.subGroupOrderNum>=? and s.subGroupOrderNum<=? order by subGroupOrderNum asc";
		return  find(hql, ContextUtils.getCompanyId(),featureId,beginNo,endNo);
	} 
	
	@SuppressWarnings({ "unchecked" })
	public List<SpcSubGroup> spcSubGroupListByLastAmout(String featureIds,int lastAmout) {
		String hql="from SpcSubGroup s where s.qualityFeature.id in ("+featureIds+")";
		List<Object> searchParams = new ArrayList<Object>();
		hql=hql+" order by s.subGroupOrderNum desc";
		Query query=createQuery(hql,searchParams.toArray());
		query.setFirstResult(0);
		query.setMaxResults(lastAmout);
		List<SpcSubGroup> list = query.list();
		Collections.sort(list,new Comparator<SpcSubGroup>() {
			public int compare(SpcSubGroup o1, SpcSubGroup o2) {
				if(o1.getSubGroupOrderNum()<o2.getSubGroupOrderNum()){
					return -1;
				}else if(o1.getSubGroupOrderNum().equals(o2.getSubGroupOrderNum())){
					return 0;
				}else{
					return 1;
				}
			}
		});
		return list;
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<SpcSubGroup> getSpcSubGroupListByCodeAndValue(Long featureId,Date startDate,Date endDate,String tag_code,String tag_value,int effectiveCapacity) {
		String hql="select spcSubGroup from SpcSubGroup  spcSubGroup  where  spcSubGroup.qualityFeature.id=? and spcSubGroup.actualSmapleNum>=? and spcSubGroup.createdTime>=? and spcSubGroup.createdTime<?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(featureId);
		searchParams.add(effectiveCapacity);
		searchParams.add(startDate);
		searchParams.add(endDate);
		if(tag_code!=null){
			hql=hql+" and spcSubGroup."+tag_code+" = ?";
			searchParams.add(tag_value);
		}	
		hql=hql+" order by modifiedTime desc ";
		Query query=createQuery(hql,searchParams.toArray());
		return query.list();
	}
	
	public Integer getGroupNumByQualityFeature(QualityFeature qualityFeature){
		String hql="select count(*) from SpcSubGroup s where s.companyId = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(qualityFeature != null){
			searchParams.add(qualityFeature);
			hql = hql + " and s.qualityFeature = ?";
		}
		Query query = this.getSession().createQuery(hql.toString());
		for(int i = 0;i < searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if(list.get(0) != null){
			return Integer.valueOf(list.get(0).toString());
		}else{
			return 0;
		}
	}
	
	public SpcSubGroup getSpcSubGroupByQualityFeatureAndEntryDate(QualityFeature qualityFeature ,Date createdTime){
		String hql="from SpcSubGroup s where s.companyId = ? and s.qualityFeature = ? and s.createdTime = ?";
		List<SpcSubGroup> list = find(hql, ContextUtils.getCompanyId(), qualityFeature, createdTime);
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
}

package com.ambition.spc.processdefine.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.ProcessPoint;
import com.ambition.spc.entity.QualityFeature;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * QualityFeatureDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class QualityFeatureDao extends HibernateDao<QualityFeature, Long> {
	public List<QualityFeature> getQualityFeatures(){
		String hql = "from QualityFeature q where q.companyId=? order by q.orderNum";
		return find(hql,ContextUtils.getCompanyId());
	}
	public Page<QualityFeature> list(Page<QualityFeature> page, ProcessPoint processPoint){
		if(processPoint != null){
			return searchPageByHql(page,"from QualityFeature q where q.companyId = ? and q.processPoint = ?",new Object[]{ContextUtils.getCompanyId(),processPoint});
		}else{
			return searchPageByHql(page,"from QualityFeature q where q.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public Page<QualityFeature> list(Page<QualityFeature> page, ProcessPoint processPoint,JSONObject params){
		if(StringUtils.isNotEmpty(page.getOrderBy())){
			page.setOrder("asc");
			page.setOrderBy("orderNum");
		}
		StringBuffer sbSql=new StringBuffer("from QualityFeature q where q.companyId = ? ");
		List<Object> searchParams=new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(null != params){
			for(Object key : params.keySet()){
				if(StringUtils.isNotEmpty(key+"")){
					if("name".equals(key.toString())){
						sbSql.append(" and q.name like ? ");
						searchParams.add("%"+params.getString(key.toString())+"%");
					}
					if("code".equals(key.toString())){
						sbSql.append(" and q.code like ? ");
						searchParams.add("%"+params.getString(key.toString())+"%");
					}
					if("processPoint_name".equals(key.toString())){
						sbSql.append(" and q.processPoint.name like ? ");
						searchParams.add("%"+params.getString(key.toString())+"%");
					}
					if("processPoint_code".equals(key.toString())){
						sbSql.append(" and q.processPoint.code like ? ");
						searchParams.add("%"+params.getString(key.toString())+"%");
					}
				}
			}
		}else if(null != processPoint){
			return searchPageByHql(page,"from QualityFeature q where q.companyId = ? and q.processPoint = ?",new Object[]{ContextUtils.getCompanyId(),processPoint});
		}
		return searchPageByHql(page, sbSql.toString(), searchParams.toArray());
	}
	public List<QualityFeature> list(){
			return find("from QualityFeature q where q.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
	}
	public Page<QualityFeature> listByMonit(Page<QualityFeature> page, String monitPointId){
			return searchPageByHql(page,"from QualityFeature q where q.monitPoint.id = ?",new Object[]{Long.parseLong(monitPointId)});
	}
	
	public QualityFeature getQualityFeatureByCode(String code){
		List<QualityFeature> list= find("from QualityFeature q where q.companyId = ? and q.code = ?",new Object[]{ContextUtils.getCompanyId(),code});
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
}

package com.ambition.spc.statistics.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.ProcessPoint;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/** 
 * 类名:PPK统计报表(com.ambition.spc.statistics.dao)
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2017年5月2日 发布
 */
@Repository
public class SpcPpkDao extends HibernateDao<ProcessPoint, Long> {
	
	public Integer countFeatureNumByPoint(String processIds){
		String hql="select count(*) from spc_quality_feature as q where q.company_Id = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		ArrayList<Long> ids = new ArrayList<Long>();
		if(processIds != null){
			String[] myids = processIds.split(",");
			for(String id:myids){
				ids.add(Long.valueOf(id));
			}
		}
		if(ids != null){
			hql = hql + " and q.fk_process_point_id in (:ids)";
		}
		Query query = this.getSession().createSQLQuery(hql.toString());
		for(int i = 0;i < searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		if(ids != null){
			query.setParameterList("ids", ids);
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if(list.get(0) != null){
			return Integer.valueOf(list.get(0).toString());
		}else{
			return 0;
		}
	}
	
	public Integer countSubGroupByPoint(String processIds){
		String hql="select count(*) from spc_sub_group as s join spc_quality_feature as q on s.fk_quality_feature_id = q.id "+
				" where s.company_Id = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		ArrayList<Long> ids = new ArrayList<Long>();
		if(processIds != null){
			String[] myids = processIds.split(",");
			for(String id:myids){
				ids.add(Long.valueOf(id));
			}
		}
		if(ids != null){
			hql = hql + " and q.fk_process_point_id in (:ids)";
		}
		Query query = this.getSession().createSQLQuery(hql.toString());
		for(int i = 0;i < searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		if(ids != null){
			query.setParameterList("ids", ids);
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if(list.get(0) != null){
			return Integer.valueOf(list.get(0).toString());
		}else{
			return 0;
		}
	}
	
}

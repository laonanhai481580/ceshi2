package com.ambition.carmfg.inspectionbase.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.MfgInspectingIndicator;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * InspectionIndicatorDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class MfgInspectingIndicatorDao extends HibernateDao<MfgInspectingIndicator,Long>{
	
	/**
	 * 获取顶级检验项目指标
	 * @return
	 */
	public List<MfgInspectingIndicator> getTopInspectingIndicators(){
		return find("from MfgInspectingIndicator i where i.companyId=? and i.parent is null",new Object[]{ContextUtils.getCompanyId()});
	}
	
	/**
	 * 根据分页查询最新版本的检验标准
	 * @param page
	 * @return
	 */
	public Page<MfgInspectingIndicator> listMaxVersion(Page<MfgInspectingIndicator> page){
		return searchPageByHql(page, "from MfgInspectingIndicator i where i.companyId=? and i.isMax=?",ContextUtils.getCompanyId(),true);
	}

	/**
	 * 根据产品结构查询物料BOM
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MfgInspectingIndicator> searchMachineNo(JSONObject params){
		StringBuffer hql = new StringBuffer("from MfgInspectingIndicator p where p.companyId=?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params.getString("machineNo") != null){
			hql.append(" and upper(p.model) like ?");
			searchParams.add("%"+params.getString("machineNo").toUpperCase() + "%");
		}
		hql.append(" order by p.model");
		Query query = createQuery(hql.toString(),searchParams.toArray());
		query.setMaxResults(20);
		return query.list();
	}
}

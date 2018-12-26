package com.ambition.qsm.baseinfo.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ambition.qsm.entity.DefectionClause;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:不符合条款DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月26日 发布
 */
@Repository
public class DefectionClauseDao extends HibernateDao<DefectionClause,Long>{
	public Page<DefectionClause> list(Page<DefectionClause> page){
		return findPage(page, "from DefectionClause d");
	}
	
	public List<DefectionClause> getAllDefectionClause(){
		return find("from DefectionClause d");
	}

    public Page<DefectionClause> search(Page<DefectionClause> page) {
        return searchPageByHql(page, "from DefectionClause d ");
    }

	public List<?> searchClauseNnames(JSONObject params) {
		StringBuffer hql = new StringBuffer("select distinct d.systemName,d.clauseName from DefectionClause d where d.companyId=?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params != null){
			for(Object pro : params.keySet()){
				hql.append(" and d." + pro + " like ?");
					searchParams.add("%" + params.getString(pro.toString()) + "%");
			}
		}
		hql.append(" order by d.clauseName");
		Query query = createQuery(hql.toString(),searchParams.toArray());
		query.setMaxResults(10);
		return query.list();
	}
}

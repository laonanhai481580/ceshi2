package com.ambition.iqc.inspectionbase.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.iqc.entity.InspectingItem;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**    
 * InspectingItemDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class InspectingItemDao extends HibernateDao<InspectingItem,Long>{
	
	/**
	 * 查询
	 * @param page,parentId
	 * @return
	 */
	public Page<InspectingItem> search(Page<InspectingItem> page,Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from InspectingItem i where i.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		String customSearch = Struts2Utils.getParameter("customSearch");
		if(customSearch != null){
			hql.append(" and i.itemName like ?");
			params.add("%" + customSearch.trim() + "%");
		}else if(parentId == null){
			hql.append(" and i.itemParent is null");
		}else{
			hql.append(" and i.itemParent.id = ?");
			params.add(parentId);
		}
		hql.append(" order by i.orderNum asc");
		return searchPageByHql(page, hql.toString(), params.toArray());
	}
	
	/**
	 * 查询
	 * @param page
	 * @return
	 */
	public Page<InspectingItem> searchByParams(Page<InspectingItem> page){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from InspectingItem i where i.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		hql.append(" order by i.orderNum asc");
		return searchPageByHql(page, hql.toString(), params.toArray());
	}
	
	/**
	 * 获取顶级项目模型分类
	 * @return
	 */
	public List<InspectingItem> getTopInspectingItems(){
		return find("from InspectingItem i where i.companyId=? and i.itemParent is null",new Object[]{ContextUtils.getCompanyId()});
	}
	
	/**
	 * 根据名称和上级查询项目模型
	 * @param name,parent
	 * @return
	 */
	public InspectingItem getInspectingItemByName(String inspectingItemName,InspectingItem parent){
		String hql = "from InspectingItem i where i.companyId = ? and i.itemName = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(inspectingItemName);
		if(parent == null){
			hql += " and i.itemParent is null";
		}else{
			hql += " and i.itemParent = ?";
			params.add(parent);
		}
		List<InspectingItem> inspectingItems = find(hql,params.toArray());
		if(inspectingItems.isEmpty()){
			return null;
		}else{
			return inspectingItems.get(0);
		}
	}
}

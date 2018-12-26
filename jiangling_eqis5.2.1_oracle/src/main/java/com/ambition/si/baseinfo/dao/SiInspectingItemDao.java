package com.ambition.si.baseinfo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.si.entity.SiInspectingItem;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * InspectingItemDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class SiInspectingItemDao extends HibernateDao<SiInspectingItem,Long>{
	
	/**
	 * 查询
	 * @param page,parentId
	 * @return
	 */
	public Page<SiInspectingItem> search(Page<SiInspectingItem> page,Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from SiInspectingItem i where i.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		if(parentId == null){
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
	public Page<SiInspectingItem> searchByParams(Page<SiInspectingItem> page){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from SiInspectingItem i where i.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		hql.append(" order by i.orderNum asc");
		Page<SiInspectingItem> page1=null;
		try {
			page1=searchPageByHql(page, hql.toString(), params.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page1;
	}
	/**
	 * 获取顶级项目模型分类
	 * @return
	 */
	public List<SiInspectingItem> getTopInspectingItems(){
		return find("from SiInspectingItem i where i.companyId=? and i.itemParent is null",new Object[]{ContextUtils.getCompanyId()});
	}
	
	/**
	 * 根据名称和上级查询项目模型
	 * @param name,parent
	 * @return
	 */
	public SiInspectingItem getInspectingItemByName(String inspectingItemName,SiInspectingItem parent){
		String hql = "from SiInspectingItem i where i.companyId = ? and i.itemName = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(inspectingItemName);
		if(parent == null){
			hql += " and i.itemParent is null";
		}else{
			hql += " and i.itemParent = ?";
			params.add(parent);
		}
		List<SiInspectingItem> inspectingItems = find(hql,params.toArray());
		if(inspectingItems.isEmpty()){
			return null;
		}else{
			return inspectingItems.get(0);
		}
	}
}

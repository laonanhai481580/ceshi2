package com.ambition.carmfg.inspectionbase.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.MfgInspectingItem;
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
public class MfgInspectingItemDao extends HibernateDao<MfgInspectingItem,Long>{
	
	/**
	 * 查询
	 * @param page,parentId
	 * @return
	 */
	public Page<MfgInspectingItem> search(Page<MfgInspectingItem> page,Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from MfgInspectingItem i where i.companyId = ?");
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
	public Page<MfgInspectingItem> searchByParams(Page<MfgInspectingItem> page){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from MfgInspectingItem i where i.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		hql.append(" order by i.orderNum asc");
		Page<MfgInspectingItem> page1=null;
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
	public List<MfgInspectingItem> getTopInspectingItems(){
		return find("from MfgInspectingItem i where i.companyId=? and i.itemParent is null",new Object[]{ContextUtils.getCompanyId()});
	}
	
	/**
	 * 根据名称和上级查询项目模型
	 * @param name,parent
	 * @return
	 */
	public MfgInspectingItem getInspectingItemByName(String inspectingItemName,MfgInspectingItem parent){
		String hql = "from MfgInspectingItem i where i.companyId = ? and i.itemName = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(inspectingItemName);
		if(parent == null){
			hql += " and i.itemParent is null";
		}else{
			hql += " and i.itemParent = ?";
			params.add(parent);
		}
		List<MfgInspectingItem> inspectingItems = find(hql,params.toArray());
		if(inspectingItems.isEmpty()){
			return null;
		}else{
			return inspectingItems.get(0);
		}
	}
}

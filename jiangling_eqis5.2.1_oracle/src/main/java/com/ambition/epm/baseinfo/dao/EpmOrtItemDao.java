package com.ambition.epm.baseinfo.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.ambition.epm.entity.EpmOrtIndicator;
import com.ambition.epm.entity.EpmOrtItem;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Repository
public class EpmOrtItemDao extends HibernateDao<EpmOrtItem,Long>{
	
	public Page<EpmOrtItem> list(Page<EpmOrtItem> page,EpmOrtIndicator ortIndicator){
		if(ortIndicator != null){
			return searchPageByHql(page,"from EpmOrtItem d where d.companyId = ? and d.ortIndicator = ?",new Object[]{ContextUtils.getCompanyId(),ortIndicator});
		}else{
			return searchPageByHql(page,"from EpmOrtItem d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public Page<EpmOrtItem> listByParent(Page<EpmOrtItem> page,Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from EpmOrtItem d where d.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		String customSearch = Struts2Utils.getParameter("customSearch");
		if(customSearch != null){
			hql.append(" and d.itemName like ");
			params.add("%" + customSearch.trim() + "%");
		}else if(parentId != null){
			hql.append(" and d.ortIndicator.id = ?");
			params.add(parentId);
		}
		hql.append(" order by d.itemName desc");
		return searchPageByHql(page, hql.toString(), params.toArray());
	}
	public Page<EpmOrtItem> getCodeByParams(Page<EpmOrtItem> page,JSONObject params,EpmOrtIndicator ortIndicator){
		StringBuffer hql = new StringBuffer("from EpmOrtItem d where d.companyId=?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params != null){
			for(Object pro : params.keySet()){
				hql.append(" and d." + pro + " like ?");
				searchParams.add("%" + params.getString(pro.toString()) + "%");
			}
		}
		if(ortIndicator!=null){
			hql.append(" and d.ortIndicator = ?");
			searchParams.add(ortIndicator);
		}
		hql.append(" order by d.ortItemNo");
		return searchPageByHql(page,hql.toString(),searchParams.toArray());
	}
	public Page<EpmOrtItem> list(Page<EpmOrtItem> page, String itemName){
		if(itemName != null){
			return searchPageByHql(page,"from EpmOrtItem d where d.companyId = ? and d.itemName like ?",new Object[]{ContextUtils.getCompanyId(),"%"+itemName+"%"});
		}else{
			return searchPageByHql(page,"from EpmOrtItem d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<EpmOrtItem> getAllOrtItems(EpmOrtIndicator ortIndicator){
		if(ortIndicator != null){
			return find("from EpmOrtItem d where d.companyId = ? and d.ortIndicator = ?",new Object[]{ContextUtils.getCompanyId(),ortIndicator});
		}else{
			return find("from EpmOrtItem d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<EpmOrtItem> getOrtItem(String itemName) {
		return find("from EpmOrtItem d where d.itemName = ?",new Object[]{itemName});
	}
	public  EpmOrtItem getOrtItemByItemName(String itemName) {
		String hql = "from EpmOrtItem d where d.itemName = ?";
		List<EpmOrtItem> ortItems = this.find(hql,itemName);
		if(ortItems.size()>0){
			return ortItems.get(0);
		}else{
			return null;
		}
	}
}

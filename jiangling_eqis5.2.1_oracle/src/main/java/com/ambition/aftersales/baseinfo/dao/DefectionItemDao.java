package com.ambition.aftersales.baseinfo.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.ambition.aftersales.entity.DefectionClass;
import com.ambition.aftersales.entity.DefectionItem;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Repository
public class DefectionItemDao extends HibernateDao<DefectionItem,Long>{
	
	public Page<DefectionItem> list(Page<DefectionItem> page,DefectionClass defectionClass){
		if(defectionClass != null){
			return searchPageByHql(page,"from DefectionItem d where d.companyId = ? and d.defectionClass = ?",new Object[]{ContextUtils.getCompanyId(),defectionClass});
		}else{
			return searchPageByHql(page,"from DefectionItem d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public Page<DefectionItem> listByParent(Page<DefectionItem> page,Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from DefectionItem d where d.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		String customSearch = Struts2Utils.getParameter("customSearch");
		if(customSearch != null){
			hql.append(" and (d.defectionItemName like ? or d.defectionItemNo like ?)");
			params.add("%" + customSearch.trim() + "%");
			params.add("%" + customSearch.trim() + "%");
		}else if(parentId != null){
			hql.append(" and d.defectionClass.id = ?");
			params.add(parentId);
		}
		hql.append(" order by d.defectionItemNo desc");
		return searchPageByHql(page, hql.toString(), params.toArray());
	}
	public Page<DefectionItem> getCodeByParams(Page<DefectionItem> page,JSONObject params){
		StringBuffer hql = new StringBuffer("from DefectionItem d where d.companyId=?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params != null){
			for(Object pro : params.keySet()){
				hql.append(" and d." + pro + " like ?");
				searchParams.add("%" + params.getString(pro.toString()) + "%");
			}
		}
		hql.append(" order by d.defectionItemNo");
		return searchPageByHql(page,hql.toString(),searchParams.toArray());
	}
	public Page<DefectionItem> list(Page<DefectionItem> page, String code){
		if(code != null){
			return searchPageByHql(page,"from DefectionItem d where d.companyId = ? and d.defectionItemNo like ?",new Object[]{ContextUtils.getCompanyId(),"%"+code+"%"});
		}else{
			return searchPageByHql(page,"from DefectionItem d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<DefectionItem> getAllDefectionItems(DefectionClass defectionClass){
		if(defectionClass != null){
			return find("from DefectionItem d where d.companyId = ? and d.defectionClass = ?",new Object[]{ContextUtils.getCompanyId(),defectionClass});
		}else{
			return find("from DefectionItem d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<DefectionItem> getDefectionItem(String code) {
		return find("from DefectionItem d where d.defectionItemNo = ?",new Object[]{code});
	}
	public String getDefectionItemNameByCode(String code){
		String hql = "from DefectionItem d where d.defectionItemNo = ?";
		List<DefectionItem> defectionItems = this.find(hql,code);
		if(defectionItems.isEmpty()){
			return null;
		}else{
			return defectionItems.get(0).getDefectionItemName();
		}
	}
	public  DefectionItem getDefectionByCode(String code) {
		String hql = "from DefectionItem d where d.defectionItemNo = ?";
		List<DefectionItem> defectionItems = this.find(hql,code);
		if(defectionItems.size()>0){
			return defectionItems.get(0);
		}else{
			return null;
		}
	}
}

package com.ambition.si.defectioncode.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.ambition.si.entity.SiDefectionCode;
import com.ambition.si.entity.SiDefectionType;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Repository
public class SiDefectionCodeDao extends HibernateDao<SiDefectionCode,Long>{
	
	public Page<SiDefectionCode> list(Page<SiDefectionCode> page,SiDefectionType defectionType){
		if(defectionType != null){
			return searchPageByHql(page,"from SiDefectionCode d where d.companyId = ? and d.defectionType = ?",new Object[]{ContextUtils.getCompanyId(),defectionType});
		}else{
			return searchPageByHql(page,"from SiDefectionCode d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public Page<SiDefectionCode> listByParent(Page<SiDefectionCode> page,Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from SiDefectionCode d where d.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		String customSearch = Struts2Utils.getParameter("customSearch");
		if(customSearch != null){
			hql.append(" and (d.defectionCodeName like ? or d.defectionCodeNo like ?)");
			params.add("%" + customSearch.trim() + "%");
			params.add("%" + customSearch.trim() + "%");
		}else if(parentId != null){
			hql.append(" and d.defectionType.id = ?");
			params.add(parentId);
		}
		hql.append(" order by d.defectionCodeNo desc");
		return searchPageByHql(page, hql.toString(), params.toArray());
	}
	public Page<SiDefectionCode> getCodeByParams(Page<SiDefectionCode> page,JSONObject params,SiDefectionType defectionType){
		StringBuffer hql = new StringBuffer("from SiDefectionCode d where d.companyId=?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params != null){
			for(Object pro : params.keySet()){
				hql.append(" and d." + pro + " like ?");
				searchParams.add("%" + params.getString(pro.toString()) + "%");
			}
		}
		if(defectionType!=null){
			hql.append(" and d.defectionType = ?");
			searchParams.add(defectionType);
		}
		hql.append(" order by d.defectionCodeNo");
		return searchPageByHql(page,hql.toString(),searchParams.toArray());
	}
	public Page<SiDefectionCode> list(Page<SiDefectionCode> page, String code){
		if(code != null){
			return searchPageByHql(page,"from SiDefectionCode d where d.companyId = ? and d.defectionCodeNo like ?",new Object[]{ContextUtils.getCompanyId(),"%"+code+"%"});
		}else{
			return searchPageByHql(page,"from SiDefectionCode d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<SiDefectionCode> getAllDefectionCodes(SiDefectionType defectionType){
		if(defectionType != null){
			return find("from SiDefectionCode d where d.companyId = ? and d.defectionType = ?",new Object[]{ContextUtils.getCompanyId(),defectionType});
		}else{
			return find("from SiDefectionCode d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<SiDefectionCode> getDefectionCode(String code) {
		return find("from SiDefectionCode d where d.defectionCodeNo = ?",new Object[]{code});
	}
	public String getDefectionCodeNameByCode(String code){
		String hql = "from SiDefectionCode d where d.defectionCodeNo = ?";
		List<SiDefectionCode> defectionCodes = this.find(hql,code);
		if(defectionCodes.isEmpty()){
			return null;
		}else{
			return defectionCodes.get(0).getDefectionCodeName();
		}
	}
	public  SiDefectionCode getDefectionByCode(String code) {
		String hql = "from SiDefectionCode d where d.defectionCodeNo = ?";
		List<SiDefectionCode> defectionCodes = this.find(hql,code);
		if(defectionCodes.size()>0){
			return defectionCodes.get(0);
		}else{
			return null;
		}
	}
}

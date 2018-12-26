package com.ambition.carmfg.defectioncode.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.DefectionCode;
import com.ambition.carmfg.entity.DefectionType;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Repository
public class DefectionCodeDao extends HibernateDao<DefectionCode,Long>{
	
	public Page<DefectionCode> list(Page<DefectionCode> page,DefectionType defectionType){
		if(defectionType != null){
			return searchPageByHql(page,"from DefectionCode d where d.companyId = ? and d.defectionType = ?",new Object[]{ContextUtils.getCompanyId(),defectionType});
		}else{
			return searchPageByHql(page,"from DefectionCode d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public Page<DefectionCode> listByParent(Page<DefectionCode> page,Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from DefectionCode d where d.companyId = ?");
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
	public Page<DefectionCode> getCodeByParams(Page<DefectionCode> page,JSONObject params){
		StringBuffer hql = new StringBuffer("from DefectionCode d where d.companyId=?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params != null){
			for(Object pro : params.keySet()){
				hql.append(" and d." + pro + " like ?");
				searchParams.add("%" + params.getString(pro.toString()) + "%");
			}
		}
		hql.append(" order by d.defectionCodeNo");
		return searchPageByHql(page,hql.toString(),searchParams.toArray());
	}
	public Page<DefectionCode> list(Page<DefectionCode> page, String code){
		if(code != null){
			return searchPageByHql(page,"from DefectionCode d where d.companyId = ? and d.defectionCodeNo like ?",new Object[]{ContextUtils.getCompanyId(),"%"+code+"%"});
		}else{
			return searchPageByHql(page,"from DefectionCode d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<DefectionCode> getAllDefectionCodes(DefectionType defectionType){
		if(defectionType != null){
			return find("from DefectionCode d where d.companyId = ? and d.defectionType = ?",new Object[]{ContextUtils.getCompanyId(),defectionType});
		}else{
			return find("from DefectionCode d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<DefectionCode> getDefectionCode(String code) {
		return find("from DefectionCode d where d.defectionCodeNo = ?",new Object[]{code});
	}
	public String getDefectionCodeNameByCode(String code){
		String hql = "from DefectionCode d where d.defectionCodeNo = ?";
		List<DefectionCode> defectionCodes = this.find(hql,code);
		if(defectionCodes.isEmpty()){
			return null;
		}else{
			return defectionCodes.get(0).getDefectionCodeName();
		}
	}
	
	public String getDefectionCodeByName(String name,String typeName,String businessUnit){
		String hql = "from DefectionCode d where d.defectionCodeName = ? and d.defectionType.defectionTypeName=? and d.defectionType.businessUnitName=? ";
		List<DefectionCode> defectionCodes = this.find(hql,name,typeName,businessUnit);
		if(defectionCodes.isEmpty()){
			return null;
		}else{
			return defectionCodes.get(0).getDefectionCodeNo();
		}
	}	
	public  DefectionCode getDefectionByCode(String code) {
		String hql = "from DefectionCode d where d.defectionCodeNo = ?";
		List<DefectionCode> defectionCodes = this.find(hql,code);
		if(defectionCodes.size()>0){
			return defectionCodes.get(0);
		}else{
			return null;
		}
	}
}

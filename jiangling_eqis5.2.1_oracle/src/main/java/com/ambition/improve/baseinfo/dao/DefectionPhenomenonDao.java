package com.ambition.improve.baseinfo.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.ambition.improve.entity.DefectionPhenomenon;
import com.ambition.improve.entity.ProblemDescrible;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Repository
public class DefectionPhenomenonDao extends HibernateDao<DefectionPhenomenon,Long>{
	
	public Page<DefectionPhenomenon> list(Page<DefectionPhenomenon> page,ProblemDescrible problemDescrible){
		if(problemDescrible != null){
			return searchPageByHql(page,"from DefectionPhenomenon d where d.companyId = ? and d.problemDescrible = ?",new Object[]{ContextUtils.getCompanyId(),problemDescrible});
		}else{
			return searchPageByHql(page,"from DefectionPhenomenon d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public Page<DefectionPhenomenon> listByParent(Page<DefectionPhenomenon> page,Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from DefectionPhenomenon d where d.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		String customSearch = Struts2Utils.getParameter("customSearch");
		if(customSearch != null){
			hql.append(" and d.defectionPhenomenonName like ? ");
			params.add("%" + customSearch.trim() + "%");
		}else if(parentId != null){
			hql.append(" and d.problemDescrible.id = ?");
			params.add(parentId);
		}
		hql.append(" order by d.defectionPhenomenonName desc");
		return searchPageByHql(page, hql.toString(), params.toArray());
	}
	public Page<DefectionPhenomenon> getCodeByParams(Page<DefectionPhenomenon> page,JSONObject params){
		StringBuffer hql = new StringBuffer("from DefectionPhenomenon d where d.companyId=?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params != null){
			for(Object pro : params.keySet()){
				hql.append(" and d." + pro + " like ?");
				searchParams.add("%" + params.getString(pro.toString()) + "%");
			}
		}
		hql.append(" order by d.defectionPhenomenonName");
		return searchPageByHql(page,hql.toString(),searchParams.toArray());
	}
	public Page<DefectionPhenomenon> list(Page<DefectionPhenomenon> page, String code){
		if(code != null){
			return searchPageByHql(page,"from DefectionPhenomenon d where d.companyId = ? and d.defectionPhenomenonName like ?",new Object[]{ContextUtils.getCompanyId(),"%"+code+"%"});
		}else{
			return searchPageByHql(page,"from DefectionPhenomenon d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<DefectionPhenomenon> getAllDefectionPhenomenons(ProblemDescrible problemDescrible){
		if(problemDescrible != null){
			return find("from DefectionPhenomenon d where d.companyId = ? and d.problemDescrible = ?",new Object[]{ContextUtils.getCompanyId(),problemDescrible});
		}else{
			return find("from DefectionPhenomenon d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<DefectionPhenomenon> getDefectionPhenomenon(String code) {
		return find("from DefectionPhenomenon d where d.defectionPhenomenonName = ?",new Object[]{code});
	}
	public String getDefectionPhenomenonNameByCode(String code){
		String hql = "from DefectionPhenomenon d where d.defectionPhenomenonName = ?";
		List<DefectionPhenomenon> defectionCodes = this.find(hql,code);
		if(defectionCodes.isEmpty()){
			return null;
		}else{
			return defectionCodes.get(0).getDefectionPhenomenonName();
		}
	}
	public  DefectionPhenomenon getDefectionByCode(String code) {
		String hql = "from DefectionPhenomenon d where d.defectionPhenomenonName = ?";
		List<DefectionPhenomenon> defectionCodes = this.find(hql,code);
		if(defectionCodes.size()>0){
			return defectionCodes.get(0);
		}else{
			return null;
		}
	}
	
	public Page<DefectionPhenomenon> listByParams(Page<DefectionPhenomenon> page,String businessUnitName,String problemType,String model){
		String hql="from DefectionPhenomenon d where d.companyId = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(businessUnitName!=null&&businessUnitName!=""){
				hql=hql+" and d.problemDescrible.businessUnitName = ?";
				searchParams.add(businessUnitName);
		}
		if(problemType!=null&&problemType!=""){
			hql=hql+" and d.problemDescrible.defectionType = ?";
			searchParams.add(problemType);
		}
		if(model!=null&&model!=""){
			hql=hql+" and d.problemDescrible.model = ?";
			searchParams.add(model);
		}
		return searchPageByHql(page,hql,searchParams.toArray());
	}
}

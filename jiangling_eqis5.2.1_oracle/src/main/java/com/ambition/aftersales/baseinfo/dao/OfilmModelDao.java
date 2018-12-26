package com.ambition.aftersales.baseinfo.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.ambition.aftersales.entity.CustomerList;
import com.ambition.aftersales.entity.OfilmModel;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Repository
public class OfilmModelDao extends HibernateDao<OfilmModel,Long>{
	
	public Page<OfilmModel> list(Page<OfilmModel> page,CustomerList customerList){
		if(customerList != null){
			return searchPageByHql(page,"from OfilmModel d where d.companyId = ? and d.customerList = ?",new Object[]{ContextUtils.getCompanyId(),customerList});
		}else{
			return searchPageByHql(page,"from OfilmModel d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public Page<OfilmModel> listByParent(Page<OfilmModel> page,Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from OfilmModel d where d.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		String customSearch = Struts2Utils.getParameter("customSearch");
		if(customSearch != null){
			hql.append(" and (d.ofilmModel like ? or d.customerModel like ?)");
			params.add("%" + customSearch.trim() + "%");
			params.add("%" + customSearch.trim() + "%");
		}else if(parentId != null){
			hql.append(" and d.customerList.id = ?");
			params.add(parentId);
		}
		hql.append(" order by d.ofilmModel desc");
		return searchPageByHql(page, hql.toString(), params.toArray());
	}
	public Page<OfilmModel> getCodeByParams(Page<OfilmModel> page,JSONObject params){
		StringBuffer hql = new StringBuffer("from OfilmModel d where d.companyId=?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params != null){
			for(Object pro : params.keySet()){
				hql.append(" and d." + pro + " like ?");
				searchParams.add("%" + params.getString(pro.toString()) + "%");
			}
		}
		hql.append(" order by d.ofilmModelNo");
		return searchPageByHql(page,hql.toString(),searchParams.toArray());
	}
	public Page<OfilmModel> list(Page<OfilmModel> page, String code){
		if(code != null){
			return searchPageByHql(page,"from OfilmModel d where d.companyId = ? and d.ofilmModel like ?",new Object[]{ContextUtils.getCompanyId(),"%"+code+"%"});
		}else{
			return searchPageByHql(page,"from OfilmModel d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<OfilmModel> getAllOfilmModels(CustomerList customerList){
		if(customerList != null){
			return find("from OfilmModel d where d.companyId = ? and d.customerList = ?",new Object[]{ContextUtils.getCompanyId(),customerList});
		}else{
			return find("from OfilmModel d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<OfilmModel> getOfilmModel(String code) {
		return find("from OfilmModel d where d.ofilmModel = ?",new Object[]{code});
	}
	public String getOfilmModelNameByCode(String code){
		String hql = "from OfilmModel d where d.ofilmModel = ?";
		List<OfilmModel> ofilmModels = this.find(hql,code);
		if(ofilmModels.isEmpty()){
			return null;
		}else{
			return ofilmModels.get(0).getOfilmModel();
		}
	}
	public  OfilmModel getDefectionByCode(String code) {
		String hql = "from OfilmModel d where d.ofilmModel = ?";
		List<OfilmModel> ofilmModels = this.find(hql,code);
		if(ofilmModels.size()>0){
			return ofilmModels.get(0);
		}else{
			return null;
		}
	}
	public Page<OfilmModel> listByParams(Page<OfilmModel> page,String customerName){
		String hql="from OfilmModel d where d.companyId = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(customerName!=null&&customerName!=""){
				hql=hql+" and d.customerList.customerName = ?";
				searchParams.add(customerName);
		}
		return searchPageByHql(page,hql,searchParams.toArray());
	}
}

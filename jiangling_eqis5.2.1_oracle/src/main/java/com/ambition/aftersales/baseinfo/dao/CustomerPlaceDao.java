package com.ambition.aftersales.baseinfo.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.ambition.aftersales.entity.CustomerList;
import com.ambition.aftersales.entity.CustomerPlace;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Repository
public class CustomerPlaceDao extends HibernateDao<CustomerPlace,Long>{
	
	public Page<CustomerPlace> list(Page<CustomerPlace> page,CustomerList customerList){
		if(customerList != null){
			return searchPageByHql(page,"from CustomerPlace d where d.companyId = ? and d.customerList = ?",new Object[]{ContextUtils.getCompanyId(),customerList});
		}else{
			return searchPageByHql(page,"from CustomerPlace d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public Page<CustomerPlace> listByParent(Page<CustomerPlace> page,Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from CustomerPlace d where d.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		String customSearch = Struts2Utils.getParameter("customSearch");
		if(customSearch != null){
			hql.append(" and d.customerPlace like ");
			params.add("%" + customSearch.trim() + "%");
			params.add("%" + customSearch.trim() + "%");
		}else if(parentId != null){
			hql.append(" and d.customerList.id = ?");
			params.add(parentId);
		}
		hql.append(" order by d.customerPlace desc");
		return searchPageByHql(page, hql.toString(), params.toArray());
	}
	public Page<CustomerPlace> getCodeByParams(Page<CustomerPlace> page,JSONObject params){
		StringBuffer hql = new StringBuffer("from CustomerPlace d where d.companyId=?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params != null){
			for(Object pro : params.keySet()){
				hql.append(" and d." + pro + " like ?");
				searchParams.add("%" + params.getString(pro.toString()) + "%");
			}
		}
		hql.append(" order by d.customerPlace");
		return searchPageByHql(page,hql.toString(),searchParams.toArray());
	}
	public Page<CustomerPlace> list(Page<CustomerPlace> page, String code){
		if(code != null){
			return searchPageByHql(page,"from CustomerPlace d where d.companyId = ? and d.ofilmModel like ?",new Object[]{ContextUtils.getCompanyId(),"%"+code+"%"});
		}else{
			return searchPageByHql(page,"from CustomerPlace d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<CustomerPlace> getAllCustomerPlaces(CustomerList customerList){
		if(customerList != null){
			return find("from CustomerPlace d where d.companyId = ? and d.customerList = ?",new Object[]{ContextUtils.getCompanyId(),customerList});
		}else{
			return find("from CustomerPlace d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<CustomerPlace> getCustomerPlace(String code) {
		return find("from CustomerPlace d where d.ofilmModel = ?",new Object[]{code});
	}
	
	public List<CustomerPlace> getPlaces(){
		return find("from CustomerPlace c where c.companyId=? ",new Object[]{ContextUtils.getCompanyId()});
	}	
	public String getCustomerPlaceNameByCode(String code){
		String hql = "from CustomerPlace d where d.ofilmModel = ?";
		List<CustomerPlace> ofilmModels = this.find(hql,code);
		if(ofilmModels.isEmpty()){
			return null;
		}else{
			return ofilmModels.get(0).getCustomerPlace();
		}
	}
	public Page<CustomerPlace> listByParams(Page<CustomerPlace> page,String customerName){
		String hql="from CustomerPlace d where d.companyId = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(customerName!=null&&customerName!=""){
				hql=hql+" and d.customerList.customerName = ?";
				searchParams.add(customerName);
		}
		return searchPageByHql(page,hql,searchParams.toArray());
	}
}

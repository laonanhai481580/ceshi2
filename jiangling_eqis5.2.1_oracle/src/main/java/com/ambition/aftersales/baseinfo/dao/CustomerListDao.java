package com.ambition.aftersales.baseinfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.aftersales.entity.CustomerList;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class CustomerListDao extends HibernateDao<CustomerList,Long>{
	
	public Page<CustomerList> list(Page<CustomerList> page){
		return searchPageByHql(page,"from CustomerList d where d.companyId=? ",ContextUtils.getCompanyId());
	}
	public List<CustomerList> getAllCustomerList(){
		return find("from CustomerList d where d.companyId=?",ContextUtils.getCompanyId());
	}
	public List<CustomerList> getCustomerListByBusinessUnit(String businessUnit){
		return find("from CustomerList d where d.companyId = ?  ", ContextUtils.getCompanyId());
	}
	public String getCustomerListNameById(Long id) {
		String hql = "from CustomerList d where d.id = ?";
		List<CustomerList> list = this.find(hql,id);
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0).getCustomerNo();
		}		
	}
	public CustomerList getCustomerListByCode(String code) {
		String hql = "from CustomerList d where d.defectionTypeNo = ?";
		List<CustomerList> list = this.find(hql,code);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}		
	}
	
	public CustomerList listByCustomerName(String customerName) {
		String hql = "from CustomerList d where d.customerName = ?";
		List<CustomerList> list = this.find(hql,customerName);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}		
	}

}

package com.ambition.carmfg.ort.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.OrtCustomer;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class OrtCustomerDao extends HibernateDao<OrtCustomer,Long>{
	
	public Page<OrtCustomer> listByProcessSection(Page<OrtCustomer> page,String processSection){
		return searchPageByHql(page,"from OrtCustomer d where d.companyId=? and d.processSection=? ",ContextUtils.getCompanyId(),processSection);
	}
	public Page<OrtCustomer> list(Page<OrtCustomer> page){
		return searchPageByHql(page,"from OrtCustomer d where d.companyId=?  ",ContextUtils.getCompanyId());
	}
	public List<OrtCustomer> getAllOrtCustomer(){
		return find("from OrtCustomer d where d.companyId=?",ContextUtils.getCompanyId());
	}
	public String getOrtCustomerNameById(Long id) {
		String hql = "from OrtCustomer d where d.id = ?";
		List<OrtCustomer> list = this.find(hql,id);
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0).getCustomerName();
		}		
	}
	public OrtCustomer getOrtCustomerByCode(String code) {
		String hql = "from OrtCustomer d where d.customerNo = ?";
		List<OrtCustomer> list = this.find(hql,code);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}		
	}
}

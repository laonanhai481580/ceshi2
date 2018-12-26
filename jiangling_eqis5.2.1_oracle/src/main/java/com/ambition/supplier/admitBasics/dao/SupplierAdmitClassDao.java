package com.ambition.supplier.admitBasics.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierAdmitClass;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class SupplierAdmitClassDao extends HibernateDao<SupplierAdmitClass,Long>{
	
	public Page<SupplierAdmitClass> list(Page<SupplierAdmitClass> page,String businessUnit){
		return searchPageByHql(page,"from SupplierAdmitClass d where d.companyId=? and d.businessUnitName=? ",ContextUtils.getCompanyId(),businessUnit);
	}
	public List<SupplierAdmitClass> getAllSupplierAdmitClass(){
		return find("from SupplierAdmitClass d where d.companyId=?",ContextUtils.getCompanyId());
	}
	public List<SupplierAdmitClass> getSupplierAdmitClassByBusinessUnit(String businessUnit){
		return find("from SupplierAdmitClass d where d.companyId = ? and d.businessUnitName = ? ", ContextUtils.getCompanyId(),businessUnit);
	}
	
	public String getSupplierAdmitClassNameById(Long id) {
		String hql = "from SupplierAdmitClass d where d.id = ?";
		List<SupplierAdmitClass> list = this.find(hql,id);
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0).getSupplierAdmitClass();
		}		
	}
	public SupplierAdmitClass getSupplierAdmitClassByCode(String materialSort) {
		String hql = "from SupplierAdmitClass d where d.supplierAdmitClass = ?";
		List<SupplierAdmitClass> list = this.find(hql,materialSort);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}		
	}
}

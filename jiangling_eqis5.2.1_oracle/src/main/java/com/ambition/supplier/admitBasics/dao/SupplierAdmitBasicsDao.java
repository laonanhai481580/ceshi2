package com.ambition.supplier.admitBasics.dao;

import java.util.List;


import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierAdmitBasics;
import com.ambition.supplier.entity.SupplierAdmitClass;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class SupplierAdmitBasicsDao extends HibernateDao<SupplierAdmitBasics,Long>{
	public Page<SupplierAdmitBasics> list(Page<SupplierAdmitBasics> page,SupplierAdmitClass defectionClass){
		if(defectionClass != null){
			return searchPageByHql(page,"from SupplierAdmitBasics d where d.companyId = ? and d.supplierAdmitClass = ?",new Object[]{ContextUtils.getCompanyId(),defectionClass});
		}else{
			return searchPageByHql(page,"from SupplierAdmitBasics d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public Page<SupplierAdmitBasics> getCodeByParams(Page<SupplierAdmitBasics> page,String materialSort){
		String hql="from SupplierAdmitBasics d where d.companyId = ? and d.materialSort=?";
		return searchPageByHql(page,hql,ContextUtils.getCompanyId(),materialSort);
	}
	public Page<SupplierAdmitBasics> list(Page<SupplierAdmitBasics> page, String code){
		if(code != null){
			return searchPageByHql(page,"from SupplierAdmitBasics d where d.companyId = ? and d.supplierDefectionItemNo like ?",new Object[]{ContextUtils.getCompanyId(),"%"+code+"%"});
		}else{
			return searchPageByHql(page,"from SupplierAdmitBasics d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<SupplierAdmitBasics> getAllSupplierAdmitBasicss(SupplierAdmitClass defectionClass){
		if(defectionClass != null){
			return find("from SupplierAdmitBasics d where d.companyId = ? and d.supplierDefectionClass = ?",new Object[]{ContextUtils.getCompanyId(),defectionClass});
		}else{
			return find("from SupplierAdmitBasics d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public String getSupplierAdmitBasicsNameByCode(String code){
		String hql = "from SupplierAdmitBasics d where d.supplierDefectionItemNo = ?";
		List<SupplierAdmitBasics> defectionItems = this.find(hql,code);
		if(defectionItems.isEmpty()){
			return null;
		}else{
			return defectionItems.get(0).getMaterialName();
		}
	}
} 

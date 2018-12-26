package com.ambition.aftersales.baseinfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.aftersales.entity.DefectionClass;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class DefectionClassDao extends HibernateDao<DefectionClass,Long>{
	
	public Page<DefectionClass> list(Page<DefectionClass> page,String businessUnit){
		return searchPageByHql(page,"from DefectionClass d where d.companyId=? and d.businessUnitName=? ",ContextUtils.getCompanyId(),businessUnit);
	}
	public List<DefectionClass> getAllDefectionClass(){
		return find("from DefectionClass d where d.companyId=?",ContextUtils.getCompanyId());
	}
	public List<DefectionClass> getDefectionClassByBusinessUnit(String businessUnit){
		return find("from DefectionClass d where d.companyId = ? and d.businessUnitName = ? ", ContextUtils.getCompanyId(),businessUnit);
	}
	public String getDefectionClassNameById(Long id) {
		String hql = "from DefectionClass d where d.id = ?";
		List<DefectionClass> list = this.find(hql,id);
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0).getDefectionClass();
		}		
	}
	public DefectionClass getDefectionClassByCode(String code) {
		String hql = "from DefectionClass d where d.defectionTypeNo = ?";
		List<DefectionClass> list = this.find(hql,code);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}		
	}
}

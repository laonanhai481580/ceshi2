package com.ambition.carmfg.defectioncode.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.DefectionCode;
import com.ambition.carmfg.entity.DefectionType;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class DefectionTypeDao extends HibernateDao<DefectionType,Long>{
	
	public Page<DefectionType> list(Page<DefectionType> page,String businessUnit){
		return searchPageByHql(page,"from DefectionType d where d.companyId=? and d.businessUnitName=? ",ContextUtils.getCompanyId(),businessUnit);
	}
	public List<DefectionType> getAllDefectionType(){
		return find("from DefectionType d where d.companyId=?",ContextUtils.getCompanyId());
	}
	public List<DefectionType> getDefectionTypeByBusinessUnit(String businessUnit){
		return find("from DefectionType d where d.companyId = ? and d.businessUnitName = ?  ", ContextUtils.getCompanyId(),businessUnit);
	}
	public String getDefectionTypeNoByName(String name,String businessUnit){
		String hql = "from DefectionType d where d.defectionTypeName = ? and  businessUnitName= ? ";
		List<DefectionType> defectionTypes = this.find(hql,name,businessUnit);
		if(defectionTypes.isEmpty()){
			return null;
		}else{
			return defectionTypes.get(0).getDefectionTypeNo();
		}
	}		
	public String getDefectionTypeNameById(Long id) {
		String hql = "from DefectionType d where d.id = ?";
		List<DefectionType> list = this.find(hql,id);
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0).getDefectionTypeName();
		}		
	}
	public DefectionType getDefectionTypeByCode(String code) {
		String hql = "from DefectionType d where d.defectionTypeNo = ?";
		List<DefectionType> list = this.find(hql,code);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}		
	}
}

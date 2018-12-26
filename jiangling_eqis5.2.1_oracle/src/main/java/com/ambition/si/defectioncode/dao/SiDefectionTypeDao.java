package com.ambition.si.defectioncode.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.si.entity.SiDefectionType;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class SiDefectionTypeDao extends HibernateDao<SiDefectionType,Long>{
	
	public Page<SiDefectionType> listByProcessSection(Page<SiDefectionType> page,String processSection){
		return searchPageByHql(page,"from SiDefectionType d where d.companyId=? and d.processSection=? ",ContextUtils.getCompanyId(),processSection);
	}
	public Page<SiDefectionType> list(Page<SiDefectionType> page){
		return searchPageByHql(page,"from SiDefectionType d where d.companyId=?  ",ContextUtils.getCompanyId());
	}
	public List<SiDefectionType> getAllDefectionType(){
		return find("from SiDefectionType d where d.companyId=?",ContextUtils.getCompanyId());
	}
	public List<SiDefectionType> getDefectionTypeByBusinessUnit(String businessUnit,String processSection){
		return find("from SiDefectionType d where d.companyId = ? and d.businessUnitName = ? and d.processSection=? ", ContextUtils.getCompanyId(),businessUnit,processSection);
	}
	public String getDefectionTypeNameById(Long id) {
		String hql = "from SiDefectionType d where d.id = ?";
		List<SiDefectionType> list = this.find(hql,id);
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0).getDefectionTypeName();
		}		
	}
	public SiDefectionType getDefectionTypeByCode(String code) {
		String hql = "from SiDefectionType d where d.defectionTypeNo = ?";
		List<SiDefectionType> list = this.find(hql,code);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}		
	}
}

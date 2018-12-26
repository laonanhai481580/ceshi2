package com.ambition.gsm.base.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.EquipmentStandard;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class EquipmentStandardDao extends HibernateDao<EquipmentStandard,Long>{
	
	public Page<EquipmentStandard> list(Page<EquipmentStandard> page){
		return searchPageByHql(page,"from EquipmentStandard d where d.companyId=?  ",ContextUtils.getCompanyId());
	}
	public List<EquipmentStandard> getAllEquipmentStandard(){
		return find("from EquipmentStandard d where d.companyId=?",ContextUtils.getCompanyId());
	}
	
}

package com.ambition.epm.baseinfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.epm.entity.EpmOrtIndicator;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class EpmOrtIndicatorDao extends HibernateDao<EpmOrtIndicator,Long>{
	
	public Page<EpmOrtIndicator> list(Page<EpmOrtIndicator> page){
		return searchPageByHql(page,"from EpmOrtIndicator d where d.companyId=?  ",ContextUtils.getCompanyId());
	}
	public List<EpmOrtIndicator> getAllOrtIndicator(){
		return find("from EpmOrtIndicator d where d.companyId=?",ContextUtils.getCompanyId());
	}

	public EpmOrtIndicator getOrtIndicatorBySth(String customerNo,String model,String samplType) {
		String hql = "from EpmOrtIndicator d where d.customerNo = ? and d.model=? and d.samplType=? ";
		List<EpmOrtIndicator> list = this.find(hql,customerNo,model,samplType);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}		
	}
}

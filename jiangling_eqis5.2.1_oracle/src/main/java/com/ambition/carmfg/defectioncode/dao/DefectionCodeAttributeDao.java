package com.ambition.carmfg.defectioncode.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.DefectionCodeAttribute;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class DefectionCodeAttributeDao extends HibernateDao<DefectionCodeAttribute,Long>{
	
	public Page<DefectionCodeAttribute> list(Page<DefectionCodeAttribute> page){
		return searchPageByHql(page,"from DefectionCodeAttribute d where d.companyId =?",new Object[]{ContextUtils.getCompanyId()});
	}
	public List<DefectionCodeAttribute> getAllDefectionCodeAttribute(){
		return find("from DefectionCodeAttribute d where d.companyId =?",new Object[]{ContextUtils.getCompanyId()});
	}
	public Page<DefectionCodeAttribute> search(Page<DefectionCodeAttribute> page) {
        return searchPageByHql(page, "from DefectionCodeAttribute defectionCodeAttribute");
    }
	public DefectionCodeAttribute getDefectionCodeAttributeByName(String positionName,String directionName,String defectionName){
		String hql = "from DefectionCodeAttribute d where d.companyId =? and d.positionCodeName = ? and d.directionCodeName=? and d.defectionCodeName=?";
		List<DefectionCodeAttribute> dcas=null;
		try {
			dcas= this.find(hql,new Object[]{ContextUtils.getCompanyId(),positionName,directionName,defectionName});
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(dcas.isEmpty()){
			return null;
		}else{
			return dcas.get(0);
		}
	}
}

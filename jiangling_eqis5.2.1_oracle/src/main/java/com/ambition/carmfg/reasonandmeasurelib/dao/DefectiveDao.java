package com.ambition.carmfg.reasonandmeasurelib.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.Defective;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 原因措施经验不良基础设置dao
 * @author wlongfeng
 *
 */

@Repository
public class DefectiveDao extends HibernateDao<Defective, Long> {
		
	public Page<Defective> list(Page<Defective> page){
		 return searchPageByHql(page, "from Defective defective");
	}
	
	public List<Defective> getAllDefective(){
		return find("from Defective defective where companyId=?", ContextUtils.getCompanyId());
	}
	public Page<Defective> search(Page<Defective> page) {
        return searchPageByHql(page, "from Defective defective");
    }

	public Defective getDefectiveByNameAndCode(String defectiveCode,String defectiveName) {
		Defective defective=new Defective();
		List<Defective> defectiveList=find("from Defective defective where companyId=? and code=? and name=?", ContextUtils.getCompanyId(),defectiveCode,defectiveName);
		if(defectiveList.size()!=0){
			 defective=defectiveList.get(0);
		}
			return defective;
		
	}
}

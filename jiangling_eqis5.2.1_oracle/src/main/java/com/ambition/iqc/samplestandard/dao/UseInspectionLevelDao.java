package com.ambition.iqc.samplestandard.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.iqc.entity.UseInspectionLevel;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 
 * @author 赵骏
 *
 */
@Repository
public class UseInspectionLevelDao extends HibernateDao<UseInspectionLevel,Long>{
	
	public UseInspectionLevel getUseInspectionLevel(){
		String hql = "from UseInspectionLevel u where u.companyId = ?";
		List<UseInspectionLevel> useInspectionLevels = find(hql,ContextUtils.getCompanyId());
		if(!useInspectionLevels.isEmpty()){
			return useInspectionLevels.get(0);
		}else{
			return null;
		}
	}
}

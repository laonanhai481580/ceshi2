package com.ambition.carmfg.reasonandmeasurelib.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.DefectiveType;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 原因措施经验不良分类基础设置dao
 * @author wlongfeng
 *
 */

@Repository
public class DefectiveTypeDao extends HibernateDao<DefectiveType, Long> {
		
	public Page<DefectiveType> list(Page<DefectiveType> page){
		 return searchPageByHql(page, "from DefectiveType defectiveType");
	}
	
	public List<DefectiveType> getAllDefectiveType(){
		return find("from DefectiveType defectiveType where companyId=?", ContextUtils.getCompanyId());
	}
	public Page<DefectiveType> search(Page<DefectiveType> page) {
        return searchPageByHql(page, "from DefectiveType defectiveType");
    }
}

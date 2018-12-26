package com.ambition.gsm.inspectionplan.dao;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.InspectionWarnUser;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Repository
public class InspectionWarnUserDao extends HibernateDao<InspectionWarnUser, Long> {
		
	public Page<InspectionWarnUser> list(Page<InspectionWarnUser> page){
		return findPage(page, "from InspectionWarnUser t");
	}
	
	public List<InspectionWarnUser> getAllInspectionWarnUser(){
		return find("from InspectionWarnUser t");
	}

    public Page<InspectionWarnUser> search(Page<InspectionWarnUser> page) {
        return searchPageByHql(page, "from InspectionWarnUser t ");
    }
}

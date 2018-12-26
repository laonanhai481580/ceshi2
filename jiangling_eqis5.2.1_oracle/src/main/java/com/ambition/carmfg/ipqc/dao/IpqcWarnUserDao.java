package com.ambition.carmfg.ipqc.dao;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.IpqcWarnUser;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Repository
public class IpqcWarnUserDao extends HibernateDao<IpqcWarnUser, Long> {
		
	public Page<IpqcWarnUser> list(Page<IpqcWarnUser> page){
		return findPage(page, "from IpqcWarnUser t");
	}
	
	public List<IpqcWarnUser> getAllIpqcWarnUser(){
		return find("from IpqcWarnUser t");
	}

    public Page<IpqcWarnUser> search(Page<IpqcWarnUser> page) {
        return searchPageByHql(page, "from IpqcWarnUser t ");
    }
}

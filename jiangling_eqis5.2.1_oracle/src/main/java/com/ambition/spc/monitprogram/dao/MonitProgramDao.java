package com.ambition.spc.monitprogram.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.MonitProgram;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * MonitProgramDao.java
 * @authorBy wanglf
 *
 */
@Repository
public class MonitProgramDao extends HibernateDao<MonitProgram, Long> {
	public MonitProgram getFirstLevelMonitProgram(){
		String hql = "from MonitProgram m where m.companyId=? and m.parent is null order by m.id asc";
		List<MonitProgram> list = this.find(hql,ContextUtils.getCompanyId());
		if(list.size() != 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	public List<MonitProgram> getAllMonitProgram(){
		return find("from MonitProgram m where m.companyId = ?", ContextUtils.getCompanyId());
	}
	public List<MonitProgram> list(){
		return find("from MonitProgram m where m.companyId = ? and m.parent is null", ContextUtils.getCompanyId());
	}
}

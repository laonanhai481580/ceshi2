package com.ambition.spc.monitprogram.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.MonitPoint;
import com.ambition.spc.entity.MonitProgram;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * MointPointDao.java
 * @authorBy wanglf
 *
 */
@Repository
public class MointPointDao extends HibernateDao<MonitPoint, Long> {
	public Page<MonitPoint> listByMonitProgram(Page<MonitPoint> page,MonitProgram monitProgram){
		  return searchPageByHql(page, "from MonitPoint m where m.companyId = ? and m.monitProgram = ?",ContextUtils.getCompanyId(),monitProgram);
	}
	public List<MonitPoint> getAllMonitPointsByMonitProgram(MonitProgram monitProgram){
		return find("from MonitPoint m where m.companyId = ? and m.monitProgram = ?",ContextUtils.getCompanyId(),monitProgram);
	}
}

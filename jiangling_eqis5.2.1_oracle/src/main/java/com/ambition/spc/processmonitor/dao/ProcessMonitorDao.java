package com.ambition.spc.processmonitor.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.AbnormalInfo;
import com.ambition.spc.entity.ReasonMeasure;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * ProcessMonitorDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class ProcessMonitorDao extends HibernateDao<ReasonMeasure,Long>{
	public ReasonMeasure queryReasonMeasureByAbnormalInfo(AbnormalInfo abnormalInfo){
		String hql = "from ReasonMeasure r where r.companyId = ? and r.abnormalInfo = ?";
		List<ReasonMeasure> list = find(hql, new Object[]{ContextUtils.getCompanyId(),abnormalInfo});
		if(!list.isEmpty()){
			return list.get(0);
		}else{
			return null;
		}
	}
}

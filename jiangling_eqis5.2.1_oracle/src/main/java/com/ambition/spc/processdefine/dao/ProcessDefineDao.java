package com.ambition.spc.processdefine.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.ProcessPoint;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * ProcessDefineDao.java
 * @authorBy YUKE
 *
 */
@Repository
public class ProcessDefineDao extends HibernateDao<ProcessPoint, Long> {
	public ProcessPoint getFirstLevelProcessPoint(){
		String hql = "from ProcessPoint p where p.companyId=? and p.parent is null order by p.createdTime asc";
		List<ProcessPoint> list = this.find(hql,ContextUtils.getCompanyId());
		if(list.size() != 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	public List<ProcessPoint> getAllProcessPoint(){
		return find("from ProcessPoint p where p.companyId = ?", ContextUtils.getCompanyId());
	}
	public List<ProcessPoint> list(){
		return find("from ProcessPoint p where p.companyId = ? and p.parent is null", ContextUtils.getCompanyId());
	}
	public List<ProcessPoint> queryProcessPointByDate(Date startDate,Date endDate){
		return find("from ProcessPoint p where p.companyId = ? and p.createdTime between ? and ?", new Object[]{ContextUtils.getCompanyId(),startDate,endDate});
	}
}

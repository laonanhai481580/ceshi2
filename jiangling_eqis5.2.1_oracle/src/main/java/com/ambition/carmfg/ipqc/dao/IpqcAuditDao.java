package com.ambition.carmfg.ipqc.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.IpqcAudit;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:IPQC出货报告DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月8日 发布
 */
@Repository
public class IpqcAuditDao extends HibernateDao<IpqcAudit,Long>{
	public Page<IpqcAudit> list(Page<IpqcAudit> page){
		return findPage(page, "from IpqcAudit d");
	}
	
	public List<IpqcAudit> getAllIpqcAudit(){
		return find("from IpqcAudit d");
	}

    public Page<IpqcAudit> search(Page<IpqcAudit> page) {
        return searchPageByHql(page, "from IpqcAudit d where d.companyId=?  ",ContextUtils.getCompanyId());
    }
    
    public Page<IpqcAudit> searchMiss(Page<IpqcAudit> page) {
        return searchPageByHql(page, "from IpqcAudit d where d.companyId=?  and d.hiddenState='N' and d.isMiss='是' ",ContextUtils.getCompanyId());
    } 
    
	public List<IpqcAudit> getIpqcAudits(Date auditDate,String businessUnitName,String factory, String station,String classGroup) {
		String hql = "from IpqcAudit a where a.companyId = ? and a.auditDate=? and a.businessUnitName = ? and a.factory = ?  and a.station=? and a.classGroup=? ";
		return this.find(hql, new Object[]{ContextUtils.getCompanyId(),auditDate,businessUnitName, factory, station, classGroup});
	}
    public Page<IpqcAudit> listDetail(Page<IpqcAudit> page,String businessUnitName, String factory, String station,String classGroup,String auditType,String department,String auditMan,String name, String group,String groupValue, Date startDate, Date endDate) {
		String hql=" from IpqcAudit d where ";
		List<Object> searchParams = new ArrayList<Object>();
		if(group.equals("year")){
			hql+="  to_char(d.auditDate,'yyyy')=? ";			
		}else if(group.equals("month")){
			hql+="  to_char(d.auditDate,'yyyy-MM')=? ";
		}else if(group.equals("day")){
			hql+="  to_char(d.auditDate,'yyyyMMdd')=? ";
		}else if(group.equals("week")){
			hql+="  to_char(d.auditDate,'yyyy_iw')=? ";
		}else if(group.equals("station")){
			hql+="  d.station=? ";
		}else if(group.equals("classGroup")){
			hql+="  d.classGroup=? ";
		}else if(group.equals("auditType")){
			hql+="  d.auditType=? ";
		}else if(group.equals("problemDegree")){
			hql+="  d.problemDegree=? ";
		}
		searchParams.add(groupValue);
		if(businessUnitName!=null&&!businessUnitName.equals("")){
			hql=hql+" and d.businessUnitName=? ";
			searchParams.add(businessUnitName);
		}
		if(factory!=null&&!factory.equals("")){
			hql=hql+" and d.factory=? ";
			searchParams.add(factory);
		}
		if(station!=null&&!station.equals("")){
			hql=hql+" and d.station=? ";
			searchParams.add(station);
		}	
		if(!classGroup.equals("")){
			hql=hql+" and d.classGroup=? ";
			searchParams.add(classGroup);
		}
		if(!auditType.equals("")){
			hql=hql+" and d.auditType=? ";
			searchParams.add(auditType);
		}
		if(!department.equals("")){
			hql=hql+" and d.department like ? ";
			searchParams.add("%"+department+"%");
		}
		if(!auditMan.equals("")){
			hql=hql+" and d.auditMman like ? ";
			searchParams.add("%"+auditMan+"%");
		}
		hql+="  and d.auditDate between ? and ? ";
		searchParams.add(startDate);
		searchParams.add(endDate);
		if(name.equals("问题件数")){
			hql+="  and (d.isMiss='否' or d.isMiss is null ) ";
		}
		return searchPageByHql(page, hql,searchParams.toArray());
	} 
    
}

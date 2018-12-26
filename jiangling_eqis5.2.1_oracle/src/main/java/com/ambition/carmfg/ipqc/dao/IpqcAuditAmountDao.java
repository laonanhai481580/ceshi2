package com.ambition.carmfg.ipqc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.IpqcAuditAmount;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:IPQC稽核件数维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2018年2月1日 发布
 */
@Repository
public class IpqcAuditAmountDao extends HibernateDao<IpqcAuditAmount,Long>{
	public Page<IpqcAuditAmount> list(Page<IpqcAuditAmount> page){
		return findPage(page, "from IpqcAuditAmount d");
	}
	
	public List<IpqcAuditAmount> getAllIpqcAuditAmount(){
		return find("from IpqcAuditAmount d");
	}

    public Page<IpqcAuditAmount> search(Page<IpqcAuditAmount> page) {
        return searchPageByHql(page, "from IpqcAuditAmount d ");
    }
    
	public IpqcAuditAmount serachWarming(String businessUnitName,String factory,String station,String problemDegree,String auditType,String classGroup){
		String hql = "from IpqcAuditAmount d where d.companyId =? and d.businessUnitName = ? and (d.factory=? or d.factory is null) and ( d.station=? or d.station is null) and ( d.problemDegree=? or d.problemDegree is null) and d.auditType=? and (d.classGroup=? or d.classGroup is null) ";
		List<IpqcAuditAmount> list=null;
		try {
			list= this.find(hql,new Object[]{ContextUtils.getCompanyId(),businessUnitName,factory,station,problemDegree,auditType,classGroup});
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}    
}

package com.ambition.carmfg.ipqc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.IpqcAuditWarming;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:IPQC稽核预警DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月20日 发布
 */
@Repository
public class IpqcAuditWarmingDao extends HibernateDao<IpqcAuditWarming,Long>{
	public Page<IpqcAuditWarming> list(Page<IpqcAuditWarming> page){
		return findPage(page, "from IpqcAuditWarming d where d.companyId=?",ContextUtils.getCompanyId());
	}
	
	public List<IpqcAuditWarming> getAllIpqcAuditWarming(){
		return find("from IpqcAuditWarming d where d.companyId=?",ContextUtils.getCompanyId());
	}

    public Page<IpqcAuditWarming> search(Page<IpqcAuditWarming> page) {
        return searchPageByHql(page, "from IpqcAuditWarming d where d.companyId=?",ContextUtils.getCompanyId());
    }
    
	public IpqcAuditWarming serachWarming(String businessUnitName,String station,String problemDegree,String missingItems){
		String hql = "from IpqcAuditWarming d where d.companyId =? and d.businessUnitName = ? and d.station=? and d.problemDegree=?  and d.missingItems=?";
		List<IpqcAuditWarming> list=null;
		try {
			list= this.find(hql,new Object[]{ContextUtils.getCompanyId(),businessUnitName,station,problemDegree,missingItems});
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

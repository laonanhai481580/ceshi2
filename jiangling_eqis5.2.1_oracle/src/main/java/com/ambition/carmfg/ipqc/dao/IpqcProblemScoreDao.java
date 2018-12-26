package com.ambition.carmfg.ipqc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.IpqcProblemScore;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:IPQC问题严重度分数维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2018年7月17日 发布
 */
@Repository
public class IpqcProblemScoreDao extends HibernateDao<IpqcProblemScore,Long>{
	public Page<IpqcProblemScore> list(Page<IpqcProblemScore> page){
		return findPage(page, "from IpqcProblemScore d where d.companyId=? ",ContextUtils.getCompanyId());
	}
	
	public List<IpqcProblemScore> getAllIpqcProblemScore(){
		return find("from IpqcProblemScore d where d.companyId=? ",ContextUtils.getCompanyId());
	}

    public Page<IpqcProblemScore> search(Page<IpqcProblemScore> page) {
        return searchPageByHql(page, "from IpqcProblemScore d where d.companyId=? ",ContextUtils.getCompanyId());
    }
    
	public IpqcProblemScore serach(String problemDegree){
		String hql = "from IpqcProblemScore d where d.companyId =? and  d.problemDegree=? ";
		List<IpqcProblemScore> list=null;
		try {
			list= this.find(hql,new Object[]{ContextUtils.getCompanyId(),problemDegree});
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

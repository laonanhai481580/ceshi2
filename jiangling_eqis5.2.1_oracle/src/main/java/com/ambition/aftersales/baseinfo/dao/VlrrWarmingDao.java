package com.ambition.aftersales.baseinfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.aftersales.entity.VlrrWarming;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:VLRR机种+目标值维护DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月21日 发布
 */
@Repository
public class VlrrWarmingDao extends HibernateDao<VlrrWarming,Long>{
	public Page<VlrrWarming> list(Page<VlrrWarming> page){
		return findPage(page, "from VlrrWarming d where d.companyId=?",ContextUtils.getCompanyId());
	}
	
	public List<VlrrWarming> getAllVlrrWarming(){
		return find("from VlrrWarming d where d.companyId=?",ContextUtils.getCompanyId());
	}

    public Page<VlrrWarming> search(Page<VlrrWarming> page) {
        return searchPageByHql(page, "from VlrrWarming d where d.companyId=?",ContextUtils.getCompanyId());
    }

	public VlrrWarming getByModel(String ofilmModel) {
		String hql = "from VlrrWarming d where d.model = ?";
		List<VlrrWarming> vlrrWarmings = this.find(hql,ofilmModel);
		if(vlrrWarmings.isEmpty()){
			return null;
		}else{
			return vlrrWarmings.get(0);
		}
	}
}

package com.ambition.aftersales.baseinfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.aftersales.entity.LarTarget;
import com.ambition.aftersales.entity.VlrrWarming;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:Lar目标值维护DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月21日 发布
 */
@Repository
public class LarTargetDao extends HibernateDao<LarTarget,Long>{
	public Page<LarTarget> list(Page<LarTarget> page){
		return findPage(page, "from LarTarget d where d.companyId=?",ContextUtils.getCompanyId());
	}
	
	public List<LarTarget> getAllLarTarget(){
		return find("from LarTarget d where d.companyId=?",ContextUtils.getCompanyId());
	}

    public Page<LarTarget> search(Page<LarTarget> page) {
        return searchPageByHql(page, "from LarTarget d where d.companyId=?",ContextUtils.getCompanyId());
    }
    //新增
	public LarTarget getByModel(String ofilmModel) {
		String hql = "from LarTarget d where d.model = ?";
		List<LarTarget> larTarget = this.find(hql,ofilmModel);
		if(larTarget.isEmpty()){
			return null;
		}else{
			return larTarget.get(0);
		}
	}
}

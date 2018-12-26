package com.ambition.gsm.base.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.CheckStandard;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
/**
 * 
 * 类名:基础维护-检验标准Dao
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年11月24日 发布
 */
@Repository
public class CheckStandardDao extends HibernateDao<CheckStandard, Long>{
	public Page<CheckStandard> list(Page<CheckStandard> page){
		return findPage(page, "from CheckStandard c");
	}
	
	public List<CheckStandard> getAllCheckStandard(){
		return find("from CheckStandard c");
	}

    public Page<CheckStandard> search(Page<CheckStandard> page) {
        return searchPageByHql(page, "from CheckStandard c ");
    }
    
    public CheckStandard getCheckStandard(String workProcedure,String machineNo){
    	String hql=" from CheckStandard c where c.model=? and c.workingProcedure=?";
    	List<CheckStandard> pl=this.find(hql, new Object[]{machineNo,workProcedure});
    	if(!pl.isEmpty()){
    		return pl.get(0);
    	}else{
    		return null;
    	}
    }
    
}

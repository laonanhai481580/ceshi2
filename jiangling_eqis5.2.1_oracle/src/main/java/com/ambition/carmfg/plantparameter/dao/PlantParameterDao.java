package com.ambition.carmfg.plantparameter.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.PlantParameter;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:设备参数Dao
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月3日 发布
 */
@Repository
public class PlantParameterDao extends HibernateDao<PlantParameter, Long>{
	public Page<PlantParameter> list(Page<PlantParameter> page){
		return findPage(page, "from PlantParameter d where d.companyId=?",ContextUtils.getCompanyId());
	}
	
	public List<PlantParameter> getAllPlantParameter(){
		return find("from PlantParameter d where d.companyId=?",ContextUtils.getCompanyId());
	}

    public Page<PlantParameter> search(Page<PlantParameter> page) {
        return searchPageByHql(page, "from PlantParameter d where d.companyId=?",ContextUtils.getCompanyId());
    }
    
    public PlantParameter getPlantParameter(String workProcedure,String machineNo){
    	String hql=" from PlantParameter pp where pp.model=? and pp.workingProcedure=?";
    	List<PlantParameter> pl=this.find(hql, new Object[]{machineNo,workProcedure});
    	if(!pl.isEmpty()){
    		return pl.get(0);
    	}else{
    		return null;
    	}
    }
    
}

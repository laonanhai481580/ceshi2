package com.ambition.carmfg.plantparameter.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.PlantItem;
import com.ambition.carmfg.entity.PlantParameterDetail;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * PlantParameterDetailDao.java
 * @authorBy LPF
 *
 */
@Repository
public class PlantParameterDetailDao extends HibernateDao<PlantParameterDetail,Long>{
	/**
	 * 查询所有已经设置的设备
	 * @return
	 */
	public List<PlantParameterDetail> getAllDetail(Long plantParameterDetailId){
		return find("from PlantParameterDetail i where i.companyId=? and i.plantParameter.id=? ",new Object[]{ContextUtils.getCompanyId(),plantParameterDetailId});
	}
	/**
	 * 根据设备信息查询已经设置的设备
	 * @param mfgInspectingItem
	 * @return
	 */
	public List<PlantParameterDetail> getDetailByPlantItem(PlantItem plantItem){
		return find("from PlantParameterDetail i where i.companyId = ? and i.plantItem = ?",new Object[]{ContextUtils.getCompanyId(),plantItem});
		
	}
}

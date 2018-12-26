package com.ambition.carmfg.plantparameter.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.PlantAttach;
import com.norteksoft.product.orm.hibernate.HibernateDao;

/**
 * 类名:设备参数附件Dao
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016-9-3 发布
 */
@Repository
public class PlantAttachDao extends HibernateDao<PlantAttach,Long> {
	
	/**
	  * 方法名: 根据ID设备参数附件
	  * <p>功能说明：</p>
	  * @param id
	  * @return
	 */
	public PlantAttach getPlantAttachById(Long id){
		String hql = "from PlantAttach i where i.id = ?";
		List<PlantAttach> attachs = find(hql,id);
		if(attachs.isEmpty()){
			return null;
		}else{
			return attachs.get(0);
		}
	}
}

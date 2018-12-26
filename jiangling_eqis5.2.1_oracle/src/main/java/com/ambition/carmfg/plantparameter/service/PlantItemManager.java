package com.ambition.carmfg.plantparameter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.PlantItem;
import com.ambition.carmfg.plantparameter.dao.PlantItemDao;
import com.norteksoft.product.orm.Page;

/**
 * 
 * 类名:设备信息Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author LPF
 * @version 1.00 2016年9月3日 发布
 */
@Service
@Transactional
public class PlantItemManager {
	@Autowired
	private PlantItemDao plantItemDao;
	
	public PlantItem getPlantItem(Long id){
		return plantItemDao.get(id);
	}
	
	/**
	  * 方法名: 保存设备信息
	  * <p>功能说明：</p>
	  * @param mfgInspectingIndicator
	 */
	public void savePlantItem(PlantItem plantItem){
		plantItemDao.save(plantItem);
	}
	
	public void deletePlantItem(PlantItem oqcInspection){
		plantItemDao.delete(oqcInspection);
	}

	public Page<PlantItem> search(Page<PlantItem>page){
		return plantItemDao.search(page);
	}

	public List<PlantItem> listAll(){
		return plantItemDao.getAllPlantItem();
	}
		
	public void deletePlantItem(Long id){
		plantItemDao.delete(id);
	}
	public void deletePlantItem(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			PlantItem  oqcInspection = plantItemDao.get(Long.valueOf(id));
			if(oqcInspection.getId() != null){
				plantItemDao.delete(oqcInspection);
			}
		}
	}
}

package com.ambition.spc.layertype.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.spc.entity.LayerDetail;
import com.ambition.spc.entity.LayerType;
import com.ambition.spc.layertype.dao.LayerDetailDao;
import com.norteksoft.product.orm.Page;

/**    
 * LayerDetailManager.java
 * @authorBy YUKE
 *
 */
@Service
@Transactional
public class LayerDetailManager {
	@Autowired
	private LayerDetailDao layerDetailDao;
	
	public Page<LayerDetail> listByType(Page<LayerDetail> page,LayerType type){
		return layerDetailDao.listByType(page, type);
	}
	public List<LayerDetail> getAllLayerDetailsByType(LayerType type){
		return layerDetailDao.getAllLayerDetailsByType(type);
	}
	public List<LayerDetail> getLayerDetailsByCode(String code){
		return layerDetailDao.getLayerDetailsByCode(code);
	}
	public List<LayerDetail> getLayerDetailsByName(String name){
		return layerDetailDao.getLayerDetailsByName(name);
	}
	public LayerDetail getLayerDetail(Long id){
		return layerDetailDao.get(id);
	}
}

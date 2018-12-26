package com.ambition.spc.layertype.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.LayerDetail;
import com.ambition.spc.entity.LayerType;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * LayerDetailDao.java
 * @authorBy wanglf
 *
 */
@Repository
public class LayerDetailDao extends HibernateDao<LayerDetail, Long> {
	public Page<LayerDetail> listByType(Page<LayerDetail> page,LayerType type){
		  return searchPageByHql(page, "from LayerDetail c where c.companyId = ? and c.layerType = ?",ContextUtils.getCompanyId(),type);
	}
	public List<LayerDetail> getAllLayerDetailsByType(LayerType type){
		return find("from LayerDetail c where c.companyId = ? and c.layerType = ?",ContextUtils.getCompanyId(),type);
	}
	public List<LayerDetail> getLayerDetailsByCode(String code){
		return find("from LayerDetail c where c.companyId = ? and c.detailCode = ?",ContextUtils.getCompanyId(),code);
	}
	public List<LayerDetail> getLayerDetailsByName(String detailName){
		return find("from LayerDetail c where c.companyId = ? and c.detailName = ?",ContextUtils.getCompanyId(),detailName);
	}
}

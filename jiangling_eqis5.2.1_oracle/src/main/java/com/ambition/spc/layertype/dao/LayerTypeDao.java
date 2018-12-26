package com.ambition.spc.layertype.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.spc.entity.LayerType;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * LayerTypeDao.java
 * @authorBy wanglf
 *
 */
@Repository
public class LayerTypeDao extends HibernateDao<LayerType, Long> {
	//根据类别名称取得 层别
	public LayerType getLayerType(String name){
		String hql = "from LayerType l where l.companyId = ? and l.typeName = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(name);
		List<LayerType> list = this.find(hql,params.toArray());
		if(!list.isEmpty()&&list.size()!=0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public LayerType getFirstLevelLayerType(){
		String hql = "from LayerType l where l.companyId=? and l.parent is null order by l.id asc";
		List<LayerType> list = this.find(hql,ContextUtils.getCompanyId());
		if(list.size() != 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	//根据类别名称取得 层别
	public List<LayerType> getLayerTypeCode(){
		String hql = "from LayerType l";
		return find(hql);
	}
	
	//获得所有的 层别
	public List<LayerType> getLayerTypes(){
		String hql = "from LayerType l";
		return find(hql);
	}
}

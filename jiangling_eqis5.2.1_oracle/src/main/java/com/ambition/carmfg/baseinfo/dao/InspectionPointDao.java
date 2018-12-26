package com.ambition.carmfg.baseinfo.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.ambition.carmfg.entity.InspectionPoint;
import com.ambition.carmfg.entity.InspectionPointTypeEnum;
import com.ambition.util.common.CommonUtil1;

@Repository
public class InspectionPointDao extends HibernateDao<InspectionPoint, Long> {
		
	public Page<InspectionPoint> list(Page<InspectionPoint> page){
		String hql = "from InspectionPoint p";
		return findPage(page, hql);
	}
	
	public List<InspectionPoint> getAllInspectionPoint(){
		String hql = "from InspectionPoint p";
		return find(hql);
	}
	
	public Page<InspectionPoint> search(Page<InspectionPoint> page){
		String hql = "from InspectionPoint p";
		return searchPageByHql(page, hql);
	}
	
	public List<InspectionPoint> getInspectionPointByWorkshop(String workshop){
		String hql = "from InspectionPoint p where  workshop=?";
		return find(hql, workshop);
	}
	
	//根据检查点名称获取检查点
	public List<InspectionPoint> getInspectionPointByName(String name){
		String hql = "from InspectionPoint p where inspectionPointName=?";
		return find(hql, name);
	}
	
	/**
	  * 方法名: 查询该用户拥有操作权限的车间
	  * <p>功能说明：</p>
	  * @param inspectorId
	  * @return
	 */
	public List<Object> getInspectionPointWorkshops(Long inspectorId){
		String hql = "select distinct i.workshop,i.listType from InspectionPoint i inner join i.inspectors spec where spec.userId = ?";
		return find(hql,inspectorId);
	}
	
	public List<InspectionPoint> getInspectionPointByWorkshop(String workshop,Long inspectorId){
		String hql = "select distinct i from InspectionPoint i inner join i.inspectors spec  where  i.workshop = ? and spec.userId = ?";
		return find(hql,workshop,inspectorId);
	}
	
	public List<InspectionPoint> getInspectionPointByInspector(Long inspectorId){
		String hql = "select distinct i from InspectionPoint i inner join i.inspectors spec  where spec.userId = ?";
		return find(hql,inspectorId);
	}
	public List<InspectionPoint> getInspectionPointByInspector(Long inspectorId,InspectionPointTypeEnum typeNum){
		String hql = "select distinct i from InspectionPoint i inner join i.inspectors spec  where spec.userId = ? and i.listType = ?";
		return find(hql,inspectorId,typeNum);
	}
	public List<InspectionPoint> getInspectionPointByWorkshop(Long inspectorId){
		String hql = "select distinct i from InspectionPoint i inner join i.inspectors spec  where spec.userId = ?";
		return find(hql,inspectorId);
	}
	
	public List<InspectionPoint> getInspectionPointExcludeSelf(String name,Long id){
		String hql = "from InspectionPoint i where  inspectionPointName=?";
		List<Object> params = new ArrayList<Object>();
		
		params.add(name);
		
		if(id!=null){
			hql += " and id <> ?";
			params.add(id);
		}
		return find(hql, params.toArray());
	}
}

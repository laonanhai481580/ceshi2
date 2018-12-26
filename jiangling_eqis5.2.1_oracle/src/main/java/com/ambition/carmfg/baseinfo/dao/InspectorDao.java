package com.ambition.carmfg.baseinfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.InspectionPointTypeEnum;
import com.ambition.carmfg.entity.InspectionType;
import com.ambition.carmfg.entity.Inspector;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class InspectorDao extends HibernateDao<Inspector,Long>{
	
	public Page<Inspector> list(Page<Inspector> page,Long inspectionPointId){
		return findPage(page, "from Inspector inspector where companyId=? and inspector.inspectionPoint.id=?",ContextUtils.getCompanyId(),inspectionPointId);
	}
	
	public List<Inspector> getAllInspector(){
		return find("from Inspector inspector where companyId=?", ContextUtils.getCompanyId());
	}
	
	public List<Inspector> getInspectorByInspectionType(InspectionType inspectionType){
		return find("from Inspector inspector where companyId=? and userId=? and inspector.inspectionPoint.inspectionType=?", ContextUtils.getCompanyId(),ContextUtils.getUserId(),inspectionType);
	}
	
	//根据人员、车间匹配登入人员是否为对应车间检验人员
	public List<Inspector> getInspectorByWorkshop(String workshop){
		return find("from Inspector inspector where companyId=? and userId=? and inspector.inspectionPoint.workshop=?", ContextUtils.getCompanyId(),ContextUtils.getUserId(),workshop);
	}
	//根据人员、车间匹配登入人员是否为对应车间检验人员
		public List<Inspector> getInspectorByWorkshopAndType(String workshop,InspectionPointTypeEnum listType){
			return find("from Inspector inspector where companyId=? and userId=? and inspector.inspectionPoint.workshop=? and inspector.inspectionPoint.listType=?", ContextUtils.getCompanyId(),ContextUtils.getUserId(),workshop,listType);
		}
	//根据人员、车间匹配登入人员是否为对应车间检验人员
		public List<Inspector> getInspectorByType(InspectionPointTypeEnum listType){
			return find("from Inspector inspector where companyId=? and userId=? and  inspector.inspectionPoint.listType=?", ContextUtils.getCompanyId(),ContextUtils.getUserId(),listType);
		}
}

package com.ambition.gsm.base.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.EquipmentStandard;
import com.ambition.gsm.entity.EquipmentStandardItem;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Repository
public class EquipmentStandardItemDao extends HibernateDao<EquipmentStandardItem,Long>{
	
	public Page<EquipmentStandardItem> list(Page<EquipmentStandardItem> page,EquipmentStandard equipmentStandard){
		if(equipmentStandard != null){
			return searchPageByHql(page,"from EquipmentStandardItem d where d.companyId = ? and d.equipmentStandard = ?",new Object[]{ContextUtils.getCompanyId(),equipmentStandard});
		}else{
			return searchPageByHql(page,"from EquipmentStandardItem d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public Page<EquipmentStandardItem> listByParent(Page<EquipmentStandardItem> page,Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from EquipmentStandardItem d where d.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		String customSearch = Struts2Utils.getParameter("customSearch");
		if(customSearch != null){
			hql.append(" and (d.standardName like ? or d.standardNo like ?)");
			params.add("%" + customSearch.trim() + "%");
			params.add("%" + customSearch.trim() + "%");
		}else if(parentId != null){
			hql.append(" and d.equipmentStandard.id = ?");
			params.add(parentId);
		}
		hql.append(" order by d.standardNo desc");
		return searchPageByHql(page, hql.toString(), params.toArray());
	}
	public Page<EquipmentStandardItem> getCodeByParams(Page<EquipmentStandardItem> page,JSONObject params,EquipmentStandard equipmentStandard){
		StringBuffer hql = new StringBuffer("from EquipmentStandardItem d where d.companyId=?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params != null){
			for(Object pro : params.keySet()){
				hql.append(" and d." + pro + " like ?");
				searchParams.add("%" + params.getString(pro.toString()) + "%");
			}
		}
		if(equipmentStandard!=null){
			hql.append(" and d.equipmentStandard = ?");
			searchParams.add(equipmentStandard);
		}
		hql.append(" order by d.defectionCodeNo");
		return searchPageByHql(page,hql.toString(),searchParams.toArray());
	}
	public Page<EquipmentStandardItem> list(Page<EquipmentStandardItem> page, String standardNo){
		if(standardNo != null){
			return searchPageByHql(page,"from EquipmentStandardItem d where d.companyId = ? and d.standardNo like ?",new Object[]{ContextUtils.getCompanyId(),"%"+standardNo+"%"});
		}else{
			return searchPageByHql(page,"from EquipmentStandardItem d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<EquipmentStandardItem> getAllEquipmentStandardItems(EquipmentStandard equipmentStandard){
		if(equipmentStandard != null){
			return find("from EquipmentStandardItem d where d.companyId = ? and d.equipmentStandard = ?",new Object[]{ContextUtils.getCompanyId(),equipmentStandard});
		}else{
			return find("from EquipmentStandardItem d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<EquipmentStandardItem> getEquipmentStandardItem(String standardNo) {
		return find("from EquipmentStandardItem d where d.standardNo = ?",new Object[]{standardNo});
	}
	public String getStandardName(String standardNo){
		String hql = "from EquipmentStandardItem d where d.standardNo = ?";
		List<EquipmentStandardItem> equipmentStandardItems = this.find(hql,standardNo);
		if(equipmentStandardItems.isEmpty()){
			return null;
		}else{
			return equipmentStandardItems.get(0).getStandardName();
		}
	}
	public  EquipmentStandardItem getByCode(String standardNo) {
		String hql = "from EquipmentStandardItem d where d.standardNo = ?";
		List<EquipmentStandardItem> equipmentStandardItems = this.find(hql,standardNo);
		if(equipmentStandardItems.size()>0){
			return equipmentStandardItems.get(0);
		}else{
			return null;
		}
	}
	public Page<EquipmentStandardItem> listByParams(Page<EquipmentStandardItem> page,String measurementName){
		String hql="from EquipmentStandardItem d where d.companyId = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(measurementName!=null&&measurementName!=""){
				hql=hql+" and d.equipmentStandard.equipmentName = ?";
				searchParams.add(measurementName);
		}
		return searchPageByHql(page,hql,searchParams.toArray());
	}		
}

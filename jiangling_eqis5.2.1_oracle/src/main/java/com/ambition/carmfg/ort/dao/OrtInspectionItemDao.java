package com.ambition.carmfg.ort.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.ambition.aftersales.entity.OfilmModel;
import com.ambition.carmfg.entity.OrtCustomer;
import com.ambition.carmfg.entity.OrtInspectionItem;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:ORT计划Dao
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author LPF
 * @version 1.00 2016年8月31日 发布
 */
@Repository
public class OrtInspectionItemDao extends HibernateDao<OrtInspectionItem,Long>{
	public Page<OrtInspectionItem> list(Page<OrtInspectionItem> page,OrtCustomer customer){
		if(customer != null){
			return searchPageByHql(page,"from OrtInspectionItem d where d.companyId = ? and d.ortCustomer = ?",new Object[]{ContextUtils.getCompanyId(),customer});
		}else{
			return searchPageByHql(page,"from OrtInspectionItem d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public Page<OrtInspectionItem> listByParent(Page<OrtInspectionItem> page,Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from OrtInspectionItem d where d.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		if(parentId != null){
			hql.append(" and d.ortCustomer.id = ?");
			params.add(parentId);
		}
		hql.append(" order by d.testItem desc");
		return searchPageByHql(page, hql.toString(), params.toArray());
	}
	public Page<OrtInspectionItem> getCodeByParams(Page<OrtInspectionItem> page,JSONObject params,OrtCustomer customer){
		StringBuffer hql = new StringBuffer("from OrtInspectionItem d where d.companyId=?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params != null){
			for(Object pro : params.keySet()){
				hql.append(" and d." + pro + " like ?");
				searchParams.add("%" + params.getString(pro.toString()) + "%");
			}
		}
		if(customer!=null){
			hql.append(" and d.ortCustomer = ?");
			searchParams.add(customer);
		}
		hql.append(" order by d.testItem");
		return searchPageByHql(page,hql.toString(),searchParams.toArray());
	}
	public Page<OrtInspectionItem> list(Page<OrtInspectionItem> page, String testItem){
		if(testItem != null){
			return searchPageByHql(page,"from OrtInspectionItem d where d.companyId = ? and d.testItem like ?",new Object[]{ContextUtils.getCompanyId(),"%"+testItem+"%"});
		}else{
			return searchPageByHql(page,"from OrtInspectionItem d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<OrtInspectionItem> getAllInspectionItems(OrtCustomer customer){
		if(customer != null){
			return find("from OrtInspectionItem d where d.companyId = ? and d.ortCustomer = ?",new Object[]{ContextUtils.getCompanyId(),customer});
		}else{
			return find("from OrtInspectionItem d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public  OrtInspectionItem getDefectionByItem(String testItem) {
		String hql = "from OrtInspectionItem d where d.testItem = ?";
		List<OrtInspectionItem> inspectionItems = this.find(hql,testItem);
		if(inspectionItems.size()>0){
			return inspectionItems.get(0);
		}else{
			return null;
		}
	}
	public Page<OrtInspectionItem> listByParams(Page<OrtInspectionItem> page,String customerNo){
		String hql="from OrtInspectionItem d where d.companyId = ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(customerNo!=null&&customerNo!=""){
				hql=hql+" and d.ortCustomer.customerNo = ?";
				searchParams.add(customerNo);
		}
		return searchPageByHql(page,hql,searchParams.toArray());
	}	
}

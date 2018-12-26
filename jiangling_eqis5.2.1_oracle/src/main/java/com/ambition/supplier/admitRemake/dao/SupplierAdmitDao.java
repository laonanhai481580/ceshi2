package com.ambition.supplier.admitRemake.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierAdmit;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:材料承认Dao
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  Janam
 * @version 1.00 2017年9月11日 发布
 */
@Repository
public class SupplierAdmitDao extends HibernateDao<SupplierAdmit, Long>{
	public List<SupplierAdmit> getSupplierAdmitMaterialCode(String materialCode,String supplierCode,Long id){
		String hql="from SupplierAdmit s where s.companyId=? and s.materialCode=? and s.supplierCode=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(materialCode);
		searchParams.add(supplierCode);
		if(id!=null){
			hql+=" and s.id<> ? ";
			searchParams.add(id);
		}
		hql+="and adminState = ?";
		searchParams.add("DL");
		return find(hql,searchParams.toArray());
	}
}

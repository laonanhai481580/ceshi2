package com.ambition.supplier.archives.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.Supplier;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class SupplierDao extends HibernateDao<Supplier, Long> {
	/**
	 * 查询供应商
	 * @param page
	 * @return
	 */
	public Page<Supplier> search(Page<Supplier> page){
		return searchPageByHql(page, "from Supplier s where s.companyId = ?", ContextUtils.getCompanyId());
	}
	
	/**
	 * 根据供应商名称查询供应商
	 * @param supplierName
	 * @return
	 */
	public Supplier getSupplierByName(String supplierName){
		String hql = "from Supplier s where s.companyId = ? and s.name = ? ";
		List<Supplier> suppliers = find(hql,new Object[]{ContextUtils.getCompanyId(),supplierName});
		if(suppliers.isEmpty()){
			return null;
		}else{
			return suppliers.get(0);
		}
	}
	/**
	 * 根据供应商编号查询供应商
	 * @param supplierName
	 * @return
	 */
	public Supplier getSupplierByCode(String supplierCode){
		return getSupplierByCode(supplierCode,ContextUtils.getCompanyId());
	}
	/**
	 * 根据供应商编号查询供应商
	 * @param supplierName
	 * @return
	 */
	public Supplier getSupplierByCode(String supplierCode,Long companyId){
		String hql = "from Supplier s where s.companyId = ? and s.code = ? ";
		List<Supplier> suppliers = find(hql,new Object[]{companyId,supplierCode});
		if(suppliers.isEmpty()){
			return null;
		}else{
			return suppliers.get(0);
		}
	}
	/**
	 * 随机获取第一个供应商
	 * @return
	 */
	public Supplier getSupplerByRandom() {
		String hql = "from Supplier s where s.companyId = ?";
		List<Supplier> suppliers = find(hql,new Object[]{ContextUtils.getCompanyId()});
		if(suppliers.isEmpty()){
			return null;
		}else{
			return suppliers.get(0);
		}
	}
	/**
	 * 根据编号/名称模糊获取供应商
	 * @return
	 */
	public List<?> searchSupplier(JSONObject params){
		StringBuffer hql = new StringBuffer("select s.code,s.name,s.id from Supplier s where s.companyId=?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		hql.append(" and (s.name like ? or s.code like ?)");
		searchParams.add("%"+params.getString("name")+"%");
		searchParams.add("%"+params.getString("code")+"%");
		hql.append(" order by s.code");
		Query query = createQuery(hql.toString(),searchParams.toArray());
		
		query.setMaxResults(20);
		return query.list();
	}
}

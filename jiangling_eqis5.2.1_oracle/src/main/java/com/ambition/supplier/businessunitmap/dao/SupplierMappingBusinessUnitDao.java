package com.ambition.supplier.businessunitmap.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierMappingBusinessUnit;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:SupplierMappingBusinessUnitDao.java
 * 中文类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * <p>
 * @author 作者 :wuxuming
 * @version 1.0 创建时间：2016-7-6 下午2:02:02
 * </p>
 */
@Repository
public class SupplierMappingBusinessUnitDao extends  HibernateDao<SupplierMappingBusinessUnit, Long>{
	
	public  Page<SupplierMappingBusinessUnit> search(Page<SupplierMappingBusinessUnit> page){
		String hql="from SupplierMappingBusinessUnit unit where unit.companyId=?";
		return this.searchPageByHql(page, hql, ContextUtils.getCompanyId());
	}
	
	public  List<Object> getBusinessCodeByParentCode(String businessUnitCode){
		String hql="select unit.childBusinessCode from SupplierMappingBusinessUnit unit where unit.parentBusinessCode=?";
		return this.find(hql, new Object[]{businessUnitCode});
	}
	
}

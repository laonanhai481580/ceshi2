package com.ambition.supplier.audit.services;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.audit.dao.SupplierMonthCheckDao;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.entity.SupplierMonthCheck;
import com.ambition.supplier.entity.SupplierYearCheck;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月27日 发布
 */
@Service
@Transactional
public class SupplierMonthCheckManager {

	@Autowired
	private SupplierMonthCheckDao supplierMonthCheckDao;
	public Page<SupplierMonthCheck> getSupplierMonthCheckPage(
			Page<SupplierMonthCheck> page) {
		// TODO Auto-generated method stub
//		String hql = "from SupplierMonthCheck s where s.companyId=? ";
//		return supplierMonthCheckDao.searchPageByHql(page, hql,ContextUtils.getCompanyId());
		String hql = "select s from SupplierMonthCheck s ,Supplier p where s.supplierId=p.id and p.state=?";
		return supplierMonthCheckDao.searchPageByHql(page, hql,Supplier.STATE_QUALIFIED);
	}
	public SupplierMonthCheck findSupplierMonthCheck(SupplierYearCheck supplierYearCheck,Integer month) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// TODO Auto-generated method stub
		String hql = " from SupplierMonthCheck s where s.companyId=? and s.supplierId=? and s.year=?  and s.yearCheckId=? and s.monthOfYear=?";
		List<SupplierMonthCheck> lists = supplierMonthCheckDao.find(hql,ContextUtils.getCompanyId(),supplierYearCheck.getSupplierId(),supplierYearCheck.getYear(),supplierYearCheck.getId(),month);
		if(lists.size()==0){
			return null;
		}else{
			return lists.get(0);
		}
	}

}

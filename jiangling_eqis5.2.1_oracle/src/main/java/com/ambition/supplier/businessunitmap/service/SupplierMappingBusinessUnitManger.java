package com.ambition.supplier.businessunitmap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.businessunitmap.dao.SupplierMappingBusinessUnitDao;
import com.ambition.supplier.entity.SupplierMappingBusinessUnit;
import com.norteksoft.product.orm.Page;

/**
 * 类名:SupplierMappingBusinessUnitManger.java
 * 中文类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * <p>
 * @author 作者 :wuxuming
 * @version 1.0 创建时间：2016-7-6 下午2:01:55
 * </p>
 */
@Service
@Transactional
public class SupplierMappingBusinessUnitManger {
	@Autowired
	private SupplierMappingBusinessUnitDao supplierMappingBusinessUnitDao;
	
	public SupplierMappingBusinessUnit getSupplierMappingBusinessUnit(Long id){
		return supplierMappingBusinessUnitDao.get(id);
	}
	
	public void save(SupplierMappingBusinessUnit supplierMappingBusinessUnit){
		supplierMappingBusinessUnitDao.save(supplierMappingBusinessUnit);
	}

	
	public Page<SupplierMappingBusinessUnit> search(Page<SupplierMappingBusinessUnit> page){
		return supplierMappingBusinessUnitDao.search(page);
	}

	public void deleteMappingBusinessUnt(Long id){
		supplierMappingBusinessUnitDao.delete(id);
	}
	
	public String getBusinessCodeByParentCode(String businessUnitCode){
		List<Object> objs=supplierMappingBusinessUnitDao.getBusinessCodeByParentCode(businessUnitCode);
		String codeStr="";
		for(Object o:objs){
			codeStr+=o.toString()+",";
		}
		return codeStr;
	}
	
}

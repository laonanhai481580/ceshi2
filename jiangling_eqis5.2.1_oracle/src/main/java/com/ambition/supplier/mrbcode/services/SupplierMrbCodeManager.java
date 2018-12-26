package com.ambition.supplier.mrbcode.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.entity.SupplierMrbCode;
import com.ambition.supplier.mrbcode.dao.SupplierMrbCodeDao;
import com.norteksoft.product.orm.Page;

@Service
@Transactional
public class SupplierMrbCodeManager {
	@Autowired
	private SupplierMrbCodeDao supplierMrbCodeDao;
	public SupplierMrbCode getSupplierMrbCode(Long id){
		return supplierMrbCodeDao.get(id);
	}
	public void saveSupplierMrbCode(SupplierMrbCode supplierMrbCode){
		supplierMrbCodeDao.save(supplierMrbCode);
	}
	public void deleteSupplierMrbCode(String deleteIds){
		String[] Ids = deleteIds.split(",");
		for(String id : Ids){
			SupplierMrbCode supplierMrbCode = supplierMrbCodeDao.get(Long.valueOf(id));
			if(supplierMrbCode.getId() != null){
				supplierMrbCodeDao.delete(supplierMrbCode);
			}
		}
	}
	public Page<SupplierMrbCode> search(Page<SupplierMrbCode> page){
		return supplierMrbCodeDao.search(page);
	}
}

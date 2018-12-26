package com.ambition.supplier.archives.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.archives.dao.SupplierCancleDatasDao;
import com.ambition.supplier.entity.SupplierCancleDatas;
import com.ambition.supplier.entity.SupplierEvaluateTotal;
import com.norteksoft.product.orm.Page;

@Service
@Transactional
public class SupplierCancleDatasManager {

	@Autowired
	private SupplierCancleDatasDao supplierCancleDatasDao;
	public Page<SupplierCancleDatas> searchByPage(Page<SupplierCancleDatas> page) {
		// TODO Auto-generated method stub
		return supplierCancleDatasDao.searchPageByHql(page, "from SupplierCancleDatas s");
	}
	public SupplierCancleDatas searchDatas(
			SupplierEvaluateTotal supplierEvaluateTotal) {
		// TODO Auto-generated method stub
		String hql = " from SupplierCancleDatas s where s.supplierId=? and s.materialType=? and s.evaluateYear=?";
		List<SupplierCancleDatas> lists = supplierCancleDatasDao.find(hql, supplierEvaluateTotal.getSupplierId(),supplierEvaluateTotal.getMaterialType(),supplierEvaluateTotal.getEvaluateYear());
		if(lists.size()==0){
			return null;
		}else{
			return lists.get(0);
		}
	}
	public void deleteDatas(SupplierCancleDatas cancleDatas) {
		// TODO Auto-generated method stub
		supplierCancleDatasDao.delete(cancleDatas);
	}

}

package com.ambition.supplier.archives.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.archives.dao.SupplierCancleDao;
import com.ambition.supplier.archives.dao.SupplierDao;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.entity.SupplierCancle;
import com.norteksoft.wf.engine.client.EndInstanceInterface;
/**
 * 类名:合格供应商取消申请流程结束事件
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2016年10月26日 发布
 */
@Service
@Transactional
public class SupplierCancleEndManager implements EndInstanceInterface{

	@Autowired
	private SupplierCancleDao supplierCancleDao;
	@Autowired
	private SupplierDao supplierDao;
	@Override
	public void endInstanceExecute(Long id) {
		// TODO Auto-generated method stub
		 SupplierCancle supplierCancle = supplierCancleDao.get(id);
		 Supplier supplier = supplierDao.get(supplierCancle.getSupplierId());
		 String  managerIdeal = supplierCancle.getManagerIdeal();
		 if("同意".equals(managerIdeal)){
			 supplier.setState(Supplier.STATE_ELIMINATED);
			 supplierDao.save(supplier);
		 }else{
			 supplierCancle.setFormState("作废");
			 supplierCancleDao.save(supplierCancle);
		 }
	}

}

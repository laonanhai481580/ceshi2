package com.ambition.supplier.develop.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.supplier.archives.dao.SupplierDao;
import com.ambition.supplier.archives.service.SupplierManager;
import com.ambition.supplier.develop.dao.SupplierDevelopDao;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.entity.SupplierDevelop;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.engine.client.AfterTaskCompleted;
@Service
public class SupplierDevelopConsentManager implements AfterTaskCompleted {
	@Autowired
	private SupplierDevelopDao supplierDevelopDao;
	@Autowired
	private SupplierManager supplierManager;
	@Autowired
	private SupplierDao supplierDao;

	@Override
	public void execute(Long arg0, TaskProcessingResult transact) {
		// TODO Auto-generated method stub
		SupplierDevelop report = supplierDevelopDao.get(arg0);
		if(report.getSupplierCode()!=null){
			Supplier systemSupplier =  supplierManager.getSupplier(report.getSupplierCode());
			if(systemSupplier==null){
				Supplier supplier = new Supplier();
				supplier.setCompanyId(ContextUtils.getCompanyId());//公司ID
				supplier.setCreateDate(new Date());
				supplier.setCreator(ContextUtils.getLoginName());
				supplier.setCreatorName(ContextUtils.getUserName());
				supplier.setModifiedTime(new Date());
				supplier.setModifier(ContextUtils.getUserName());
				supplier.setName(report.getSupplierCode());//供应商名称
				supplier.setName(report.getSupplierName());//供应商名称
				supplier.setSupplyMaterial(report.getSupplierServices());//物料
				supplier.setAddress(report.getSupplierCompanyAddress());//地址
				supplier.setLinkMan(report.getLegalRepresentative());//联系人
				supplier.setLinkPhone(report.getCompanyTelephone());//联系电话
				supplierDevelopDao.getSession().save(supplier);
			}
		}
	}
	
}

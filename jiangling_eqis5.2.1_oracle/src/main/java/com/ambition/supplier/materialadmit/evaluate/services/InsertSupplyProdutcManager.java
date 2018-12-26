package com.ambition.supplier.materialadmit.evaluate.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.entity.SupplierMaterialEvaluate;
import com.ambition.supplier.entity.SupplyProduct;
import com.ambition.supplier.materialadmit.evaluate.dao.SupplierMaterialEvaluateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.engine.client.AfterTaskCompleted;
/**
 * 类名:
 * <p>amb</p>样件评价插入供应商物料供应表
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  林少伟
 * @version 1.00 2014-6-6 发布
 */
@Service
public class InsertSupplyProdutcManager implements AfterTaskCompleted {
	@Autowired
	private SupplierMaterialEvaluateDao supplierMaterialEvaluateDao;
	@Override
	public void execute(Long dataId, TaskProcessingResult transact) {
		// TODO Auto-generated method stub
		SupplierMaterialEvaluate  supplierMaterialEvaluate = supplierMaterialEvaluateDao.get(dataId);
		String suplierHql = " from Supplier s where s.code=?";
		@SuppressWarnings("unchecked")
		List<Supplier> suppliers = supplierMaterialEvaluateDao.getSession().createQuery(suplierHql).setParameter(0, supplierMaterialEvaluate.getSupplierCode()).list();
		if(suppliers.size()!=0){
			Supplier supplier = suppliers.get(0);
			String hql = " from SupplyProduct s where s.code=? and s.supplier=?";
			@SuppressWarnings("unchecked")
			List<SupplyProduct> supplyProducts = supplierMaterialEvaluateDao.getSession().createQuery(hql)
			                                      .setParameter(0, supplierMaterialEvaluate.getMaterialCode())
			                                      .setParameter(1, supplier).list();
			if(supplyProducts.size()==0){
				SupplyProduct supplyProduct = new SupplyProduct();
				supplyProduct.setCreator(ContextUtils.getLoginName());
				supplyProduct.setCreatedTime(new Date());
				supplyProduct.setCreatorName(ContextUtils.getUserName());
				supplyProduct.setMaterialType(supplierMaterialEvaluate.getMaterialType());
				supplyProduct.setCode(supplierMaterialEvaluate.getMaterialCode());
				supplyProduct.setName(supplierMaterialEvaluate.getMaterialName());
				supplyProduct.setSupplier(supplier);
				supplier.getSupplyProducts().add(supplyProduct);
				supplierMaterialEvaluateDao.getSession().save(supplier);
			}
		}
		
	}

}

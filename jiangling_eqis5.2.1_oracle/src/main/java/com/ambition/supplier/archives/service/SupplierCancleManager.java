package com.ambition.supplier.archives.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.archives.dao.SupplierCancleDao;
import com.ambition.supplier.entity.SupplierCancle;
import com.norteksoft.product.orm.hibernate.HibernateDao;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月26日 发布
 */
@Service
@Transactional
public class SupplierCancleManager extends AmbWorkflowManagerBase<SupplierCancle>{

	@Autowired
	private SupplierCancleDao supplierCancleDao;
	@Override
	public HibernateDao<SupplierCancle, Long> getHibernateDao() {
		// TODO Auto-generated method stub
		return supplierCancleDao;
	}

	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return "SUPPLIER_CANCLE";
	}

	@Override
	public Class<SupplierCancle> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return SupplierCancle.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return "supplier-cancle";
	}

	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "合格供应商取消流程";
	}
	/**
     * 方法名: 导出报告
     * <p>功能说明：</p>
     * @return
    */
   public void exportReport(Long entityId) throws IOException {
       exportReport(entityId,"supplier-cancle.xls","合格供应商取消");
   }
}

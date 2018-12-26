package com.ambition.supplier.audit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.audit.dao.SupplierAuditImproveDao;
import com.ambition.supplier.entity.SupplierAuditImprove;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Service
@Transactional
public class SupplierAuditImproveManager extends AmbWorkflowManagerBase<SupplierAuditImprove>{

	@Autowired
	private SupplierAuditImproveDao supplierAuditImproveDao;
	@Override
	public HibernateDao<SupplierAuditImprove, Long> getHibernateDao() {
		// TODO Auto-generated method stub
		return supplierAuditImproveDao;
	}

	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return "SUPPLIER_AUDIT_IMPROVE";
	}

	@Override
	public Class<SupplierAuditImprove> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return SupplierAuditImprove.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return "supplier-audit-improve";
	}

	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "供应商问题改善";
	}

}

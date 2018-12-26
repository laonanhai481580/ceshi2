package com.ambition.gsm.nonconformity.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.entity.NonconformityDispose;
import com.ambition.gsm.nonconformity.dao.NonconformityDisposeDao;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Service
@Transactional
public class NonconformityDisposeManager extends AmbWorkflowManagerBase<NonconformityDispose>{
	
	@Autowired
	private NonconformityDisposeDao noconformityDisposeDao;
	
	public NonconformityDisposeDao getNonconformityDispose(){
		return noconformityDisposeDao;
	}
	public NonconformityDispose getNonconformityDispose(Long id){
		return noconformityDisposeDao.get(id);
	}
	
	@Override
	public HibernateDao<NonconformityDispose, Long> getHibernateDao() {
		// TODO Auto-generated method stub
		return noconformityDisposeDao;
	}
	
	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return NonconformityDispose.ENTITY_LIST_CODE;
	}

	@Override
	public Class<NonconformityDispose> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return NonconformityDispose.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return "gsm_nonconformity";
	}

	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "不合格品处理流程";
	}
	 /**
     * 方法名: 导出报告
     * <p>功能说明：</p>
     * @return
    */
   public void exportReport(Long entityId) throws IOException {
       exportReport(entityId,"gsm_nonconformity.xls","不合格处理单");
   }

}

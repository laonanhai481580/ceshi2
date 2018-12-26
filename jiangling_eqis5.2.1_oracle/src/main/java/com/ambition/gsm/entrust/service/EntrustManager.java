package com.ambition.gsm.entrust.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.entity.Entrust;
import com.ambition.gsm.entrust.dao.EntrustDao;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Service
@Transactional
public class EntrustManager extends AmbWorkflowManagerBase<Entrust>{
	@Autowired
	private EntrustDao entrustDao;
	
	@Override
	public HibernateDao<Entrust, Long> getHibernateDao() {
		// TODO Auto-generated method stub
		return entrustDao;
	}

	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return Entrust.ENTITY_LIST_CODE;
	}

	@Override
	public Class<Entrust> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return Entrust.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return "gsm_entrust";
	}

	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "外校委托流程";
	}
	 /**
     * 方法名: 导出报告
     * <p>功能说明：</p>
     * @return
    */
   public void exportReport(Long entityId) throws IOException {
       exportReport(entityId,"gsm_entrust.xls","外校委托单");
   }
	public void deleteEntity(Entrust entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			//先删除子表
			Long reportId = entity.getId();
			String sql31 = "delete from GSM_ENTRUST_SUBLIST where GSM_SUBLIST_ID = ?";	
			getHibernateDao().getSession().createSQLQuery(sql31).setParameter(0,reportId).executeUpdate();
			String sql = "delete from product_task_all_his where execution_id = ?";
			getHibernateDao().getSession().createSQLQuery(sql).setParameter(0,workflowId).executeUpdate();
			getHibernateDao().delete(entity);
		}else{
			getHibernateDao().delete(entity);
		}
		
	}
}

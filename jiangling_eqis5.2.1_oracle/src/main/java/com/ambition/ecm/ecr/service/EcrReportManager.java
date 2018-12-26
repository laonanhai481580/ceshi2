package com.ambition.ecm.ecr.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.ecm.ecr.dao.EcrReportDao;
import com.ambition.ecm.entity.EcrReport;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.qsm.entity.CorrectMeasures;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.hibernate.HibernateDao;
@Service
@Transactional
public class EcrReportManager extends AmbWorkflowManagerBase<EcrReport>{
	
	@Autowired
	private EcrReportDao ecrReportDao;
	private static final String code="ecm-ecr";
	
	@Override
	public HibernateDao<EcrReport, Long> getHibernateDao() {
		return ecrReportDao;
	}

	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return "ECM_ECN_REPORT";
	}

	@Override
	public Class<EcrReport> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return EcrReport.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return code;
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "ecr-report.xlsx", "ECR变更单");
	}
	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "ECR变更单";
	}
	/**
	 * 删除实体，流程相关文件都删除
	 * @param entity 删除的对象
	 */
	@Override
	public void deleteEntity(EcrReport entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			//先删除子表
			Long reportId = entity.getId();
			String sql31 = "delete from ECM_ECR_REPORT_DETAIL where FK_ECR_REPORT = ?";	
			
			getHibernateDao().getSession().createSQLQuery(sql31)
			.setParameter(0,reportId)
			.executeUpdate();

			String sql = "delete from product_task_all_his where execution_id = ?";
			getHibernateDao().getSession().createSQLQuery(sql)
			.setParameter(0,workflowId)
			.executeUpdate();
			getHibernateDao().delete(entity);
			ApiFactory.getInstanceService().deleteInstance(entity);
		}else{
			getHibernateDao().delete(entity);
		}
	}
}

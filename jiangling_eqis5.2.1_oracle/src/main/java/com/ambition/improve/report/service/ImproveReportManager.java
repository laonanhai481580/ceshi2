package com.ambition.improve.report.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.improve.entity.ImproveReport;
import com.ambition.improve.report.dao.ImproveReportDao;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 类名:8D改进报告Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月29日 发布
 */
@Service
@Transactional
public class ImproveReportManager extends AmbWorkflowManagerBase<ImproveReport>{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private ImproveReportDao improveReportDao;
	@Override
	public Class<ImproveReport> getEntityInstanceClass() {
		return ImproveReport.class;
	}

	@Override
	public String getEntityListCode() {
		return ImproveReport.ENTITY_LIST_CODE;
	}

	@Override
	public HibernateDao<ImproveReport, Long> getHibernateDao() {
		return improveReportDao;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "improve-report";
	}

	@Override
	public String getWorkflowDefinitionName() {
		return "8D改进报告流程";
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "improve-report.xlsx", ImproveReport.ENTITY_LIST_NAME);
	}
	/**
	 * 删除实体，流程相关文件都删除
	 * @param entity 删除的对象
	 */
	@Override
	public void deleteEntity(ImproveReport entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			//先删除子表
			Long reportId = entity.getId();
			String sql31 = "delete from IMP_8D_REPORT_TEAM where IMP_8D_REPORT_ID = ?";	
			
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
	
	/**
	  * 方法名: 保存对象
	  * <p>功能说明：</p>
	  * @param report
	  * @param childMaps 子表对象
	  * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 */
	public void saveEntity(ImproveReport report,Map<String,List<JSONObject>> childMaps){
		//数据处理
		if(report.getProblemDescrible()!=null){
			report.setProblemDescrible(report.getProblemDescrible().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getEmergencyMeasures()!=null){
			report.setEmergencyMeasures(report.getEmergencyMeasures().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getEmergencyMeasures()!=null){
			report.setEmergencyMeasures(report.getEmergencyMeasures().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getRemarkD4()!=null){
			report.setRemarkD4(report.getRemarkD4().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getRemarkD5()!=null){
			report.setRemarkD5(report.getRemarkD5().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getRemarkD6()!=null){
			report.setRemarkD6(report.getRemarkD6().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getRemarkD7()!=null){
			report.setRemarkD7(report.getRemarkD7().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getRemarkD8()!=null){
			report.setRemarkD8(report.getRemarkD8().replaceAll("", "").replaceAll("", ""));
		}
		report.setModifiedTime(new Date());
		report.setModifier(ContextUtils.getLoginName());
		report.setModifierName(ContextUtils.getUserName());
		getHibernateDao().save(report);
		//设置子表的值
		setChildItems(report,childMaps);
		
	}	
	
}

package com.ambition.improve.exceptionreport.service;

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

import com.ambition.improve.entity.QualityExceptionReport;
import com.ambition.improve.exceptionreport.dao.QualityExceptionReportDao;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 类名:品质异常联络单Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2017年4月22日 发布
 */
@Service
@Transactional
public class QualityExceptionReportManager extends AmbWorkflowManagerBase<QualityExceptionReport>{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private QualityExceptionReportDao qualityExceptionReportDao;
	@Override
	public Class<QualityExceptionReport> getEntityInstanceClass() {
		return QualityExceptionReport.class;
	}

	@Override
	public String getEntityListCode() {
		return QualityExceptionReport.ENTITY_LIST_CODE;
	}

	@Override
	public HibernateDao<QualityExceptionReport, Long> getHibernateDao() {
		return qualityExceptionReportDao;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "exception-report";
	}

	@Override
	public String getWorkflowDefinitionName() {
		return "品质异常联络单";
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "exception-report.xlsx", QualityExceptionReport.ENTITY_LIST_NAME);
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
	public void saveEntity(QualityExceptionReport report,Map<String,List<JSONObject>> childMaps){
		//数据处理
		if(report.getExceptionDescrible()!=null){
			report.setExceptionDescrible(report.getExceptionDescrible().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getExceptionDescribleConfirm()!=null){
			report.setExceptionDescribleConfirm(report.getExceptionDescribleConfirm().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getEmergencyMeasures()!=null){
			report.setEmergencyMeasures(report.getEmergencyMeasures().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getReasonAnalysis()!=null){
			report.setReasonAnalysis(report.getReasonAnalysis().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getImprovementMeasures()!=null){
			report.setImprovementMeasures(report.getImprovementMeasures().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getEffectConfirm()!=null){
			report.setEffectConfirm(report.getEffectConfirm().replaceAll("", "").replaceAll("", ""));
		}
		report.setModifiedTime(new Date());
		report.setModifier(ContextUtils.getLoginName());
		report.setModifierName(ContextUtils.getUserName());
		getHibernateDao().save(report);
		//设置子表的值
		setChildItems(report,childMaps);
		
	}	
	
	/**
	 * 删除实体，流程相关文件都删除
	 * @param ids
	 */
	public String deleteEntity(String ids) {
		String[] deleteIds = ids.split(",");
		int deleteNum=0,failNum=0;
		for (String id : deleteIds) {
			QualityExceptionReport report = getEntity(Long.valueOf(id));
			if(report.getCreator()!=null&&report.getCreator().equals(ContextUtils.getLoginName())){
				deleteEntity(report);
				deleteNum++;
			}else{
				failNum++;
			}			
		}
		return deleteNum+" 条数据成功删除，"+failNum+" 条数据没有权限删除！";
	}  	
	
	public Page<QualityExceptionReport> searchPageProduct(
			Page<QualityExceptionReport> page) {
		return qualityExceptionReportDao.searchPageProduct(page);
	}	
	public Page<QualityExceptionReport> searchPageQuality(
			Page<QualityExceptionReport> page) {
		return qualityExceptionReportDao.searchPageQuality(page);
	}
}

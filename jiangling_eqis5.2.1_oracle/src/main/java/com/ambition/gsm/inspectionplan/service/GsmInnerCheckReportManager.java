package com.ambition.gsm.inspectionplan.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.entity.GsmEquipment;
import com.ambition.gsm.entity.GsmInnerCheckReport;
import com.ambition.gsm.entity.InspectionPlan;
import com.ambition.gsm.equipment.service.GsmEquipmentManager;
import com.ambition.gsm.inspectionplan.dao.GsmInnerCheckReportDao;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.util.common.CommonUtil1;
import com.ibm.icu.util.Calendar;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**    
 * GsmInnerCheckReportManager.java
 * @authorBy lpf
 *
 */
@Service
@Transactional
public class GsmInnerCheckReportManager extends AmbWorkflowManagerBase<GsmInnerCheckReport>{
	@Autowired
	private GsmInnerCheckReportDao gsmInnerCheckReportDao;	
	@Autowired
	private GsmEquipmentManager gsmEquipmentManager;
	@Autowired
	private InspectionPlanManager inspectionPlanManager;
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
	public void saveEntity(GsmInnerCheckReport report,Map<String,List<JSONObject>> childMaps){
		report.setModifiedTime(new Date());
		report.setModifier(ContextUtils.getLoginName());
		report.setModifierName(ContextUtils.getUserName());
		String currentActivityName=null;
		if(report.getWorkflowInfo()!=null){
			currentActivityName=report.getWorkflowInfo().getCurrentActivityName();
		}		
		if(currentActivityName!=null&&currentActivityName.equals("审核")){
			saveGsmInnerCheckReport(report);
		}
		getHibernateDao().save(report);
		//设置子表的值
		setChildItems(report,childMaps);
		
	}	
	public void saveGsmInnerCheckReport(GsmInnerCheckReport gsmInnerCheckReport) {
		//回写计划日期
		if(gsmInnerCheckReport.getCheckDate()!=null&&gsmInnerCheckReport.getPlanId()!=null&&!gsmInnerCheckReport.getPlanId().equals("")){
			InspectionPlan	inspectionPlan=inspectionPlanManager.getInspectionPlan(Long.valueOf(gsmInnerCheckReport.getPlanId()));
			try {
				inspectionPlan.setActualInspectionDate(gsmInnerCheckReport.getCheckDate());
				inspectionPlan.setInspectionState(InspectionPlan.STATE_CHECk_OUT);
				inspectionPlan.setCheckResult(gsmInnerCheckReport.getCheckResult());
				inspectionPlan.setInspectionPeople(gsmInnerCheckReport.getCheckMan());
				if(gsmInnerCheckReport.getCheckDate().getTime()<=inspectionPlan.getInspectionPlanDate().getTime()){
					inspectionPlan.setIsIntime("是");
				}else{
					inspectionPlan.setIsIntime("否");
				}
				 inspectionPlanManager.saveInspectionPlan(inspectionPlan);
				  //修改原来的量检具信息状态
				  GsmEquipment gsmEquipment = inspectionPlan.getGsmEquipment();
				  gsmEquipment.setMeasurementState(GsmEquipment.STATE_INUSE);
				  gsmEquipment.setProofTime(gsmInnerCheckReport.getCheckDate());	
				  gsmEquipment.setNextProofTime(gsmInnerCheckReport.getNextCheckDate());
				//生成下一条计划
				  
/*				  if(gsmInnerCheckReport.getHasNext()==null||!gsmInnerCheckReport.getHasNext().equals("是")){
					  InspectionPlan inspectionPlanNew = new InspectionPlan();
					  inspectionPlanNew.setCompanyId(ContextUtils.getCompanyId());
					  inspectionPlanNew.setCreatedTime(new Date());
					  inspectionPlanNew.setCreator(ContextUtils.getLoginName());
					  inspectionPlanNew.setCreatorName(ContextUtils.getUserName());
					  inspectionPlanNew.setModifiedTime(new Date());
					  inspectionPlanNew.setModifier(ContextUtils.getLoginName());
					  inspectionPlanNew.setModifierName(ContextUtils.getUserName());
					  inspectionPlanNew.setBusinessUnitName(ContextUtils.getSubCompanyName());
					  inspectionPlanNew.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
					  inspectionPlanNew.setYesOrNo(true);
					  inspectionPlanNew.setAddress(gsmEquipment.getAddress());
					  inspectionPlanNew.setProcessSection(gsmEquipment.getProcessSection());
					  inspectionPlanNew.setEquipmentName(gsmEquipment.getEquipmentName());
					  inspectionPlanNew.setEquipmentModel(gsmEquipment.getEquipmentModel());
					  inspectionPlanNew.setCompanyMain(gsmEquipment.getCompanyMain());
					  inspectionPlanNew.setDevName(gsmEquipment.getDevName());
					  inspectionPlanNew.setWorkProducre(gsmEquipment.getWorkProducre());
					  inspectionPlanNew.setDutyMan(gsmEquipment.getDutyMan());
					  inspectionPlanNew.setDutyLoginMan(gsmEquipment.getDutyLoginMan());
					  inspectionPlanNew.setManagerAssets(gsmEquipment.getManagerAssets());
					  inspectionPlanNew.setCheckMethod(gsmEquipment.getCheckMethod());
					  inspectionPlanNew.setFrequency(gsmEquipment.getFrequency());
					  Date doDate = inspectionPlan.getActualInspectionDate();//实际校检日期
					  inspectionPlanNew.setInspectionState(InspectionPlan.STATE_CHECk_WAIT);//校验状态
					  inspectionPlanNew.setInspectionLastedDate(doDate);//上次校检日期
					  inspectionPlanNew.setGsmEquipment(gsmEquipment);
					  inspectionPlanNew.setGsmState("N");
					  //inspectionPlanNew.setActualInspectionDate(null); //实际校验日期
					   Calendar c = Calendar.getInstance();//获得一个日历的实例
				       c.setTime(doDate);//设置日历时间
				       Integer frequency=Integer.valueOf(inspectionPlan.getFrequency());
				       if(frequency!=null){
				    	   c.add(Calendar.MONTH,frequency);//在日历的月份上增加频率(月)	
				    	   Date inspectionPlanDateNext=c.getTime();
					       inspectionPlanNew.setInspectionPlanDate(inspectionPlanDateNext);
					       gsmEquipment.setNextProofTime(inspectionPlanDateNext);
					       Long nowDates=new Date().getTime();
					       Long warnDates=inspectionPlanDateNext.getTime();
					       if(nowDates>warnDates){
							   gsmEquipment.setIsWarm(1);
						   }else{
							   Long intervalMilli = warnDates - nowDates;
							   int 	days=(int)(intervalMilli/(24 * 60 * 60 * 1000));
							   if(days<=10){
								   gsmEquipment.setIsWarm(1); 
							   }else{
								   gsmEquipment.setIsWarm(0);
							   }
						   }	
				       }
				      gsmEquipmentManager.saveGsmEquipment(gsmEquipment);
					  inspectionPlanManager.saveInspectionPlan(inspectionPlanNew);//生成下次校验计划
					  gsmInnerCheckReport.setHasNext("是");
				  }*/
				} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public HibernateDao<GsmInnerCheckReport, Long> getHibernateDao() {
		return gsmInnerCheckReportDao;
	}

	@Override
	public String getEntityListCode() {
		return GsmInnerCheckReport.ENTITY_LIST_CODE;
	}

	@Override
	public Class<GsmInnerCheckReport> getEntityInstanceClass() {
		return GsmInnerCheckReport.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "inner-check-report";
	}

	@Override
	public String getWorkflowDefinitionName() {
		return "內校报告";
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "gsm_inspectionplan-inner-report.xls", GsmInnerCheckReport.ENTITY_LIST_NAME);
	}	
	
	/**
	 * 删除实体，流程相关文件都删除
	 * @param entity 删除的对象
	 */
	@Override
	public void deleteEntity(GsmInnerCheckReport entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			//先删除子表
			Long reportId = entity.getId();
			String sql1 = "delete from GSM_CHECK_REPORT_ITEM where FK_CHECK_REPORT_ID = ?";	
			
			getHibernateDao().getSession().createSQLQuery(sql1)
			.setParameter(0,reportId)
			.executeUpdate();
			String sql2 = "delete from GSM_CHECK_REPORT_DETAIL where FK_CHECK_REPORT_ID = ?";	
			
			getHibernateDao().getSession().createSQLQuery(sql2)
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
	public Page<GsmInnerCheckReport> listState(Page<GsmInnerCheckReport> page,String state){
		String hql = " from GsmInnerCheckReport e where e.gsmState=?";
		return gsmInnerCheckReportDao.searchPageByHql(page, hql, state);
	}
}

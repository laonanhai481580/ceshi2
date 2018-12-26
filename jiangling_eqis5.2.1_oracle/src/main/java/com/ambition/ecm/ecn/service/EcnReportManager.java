package com.ambition.ecm.ecn.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.ecm.ecn.dao.EcnReportDao;
import com.ambition.ecm.entity.EcnReport;
import com.ambition.ecm.entity.EcnReportDetail;
import com.ambition.supplier.utils.DateUtil;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exportexcel.ExportExcelFormatter;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.product.web.wf.WorkflowManagerSupport;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.engine.client.EndInstanceInterface;
import com.norteksoft.wf.engine.client.FormFlowableDeleteInterface;
import com.norteksoft.wf.engine.client.RetrieveTaskInterface;
@Service
@Transactional
public class EcnReportManager extends WorkflowManagerSupport<EcnReport> implements FormFlowableDeleteInterface,RetrieveTaskInterface,
EndInstanceInterface{
	
	public static final String Ecn_CODE="ecm-ecn";
	
	@Autowired
	private EcnReportDao ecnReportDao;
	Logger log = Logger.getLogger(this.getClass());
	
	public Page<EcnReport> searchPage(Page<EcnReport> page){
		return ecnReportDao.searchPage(page);
	}
	/**
	  * 方法名: 获取对象当前的办理人员
	  * <p>功能说明：</p>
	  * @param id 
	  * @return
	 */
	public List<String[]> getTaskHander(EcnReport entity) {
	  return ApiFactory.getTaskService().getActivityTaskTransactors(entity);
	}
	/**
	 * 获取取回权限
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Boolean retrieveTask(EcnReport entity){
		String sql = "select p.name,p.transactor from product_task p inner join workflow_task t on p.id = t.id where t.process_instance_id = '"+entity.getWorkflowInfo().getWorkflowId()+"' and t.processing_mode <> 'TYPE_READ' and p.transact_date is not null order by p.transact_date";
		List<Object> list  = ecnReportDao.getSession().createSQLQuery(sql).list();
		if(list.size()>0){
			Object [] one = (Object[])list.get(list.size()-1);
			String taskName = one[0].toString();
			String loginName = ContextUtils.getLoginName();
			for (int i = list.size(); i > 0; i--) {
				Object [] obj = (Object[])list.get(i-1);
				if(!taskName.equals(obj[0])){
					return false;
				}else{
					if(loginName.equals(obj[1].toString())){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void submitProcess(EcnReport report ,List<JSONObject> reportArrays) throws Exception{
		saveEcnReport(report ,reportArrays);
		Long processId = ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(Ecn_CODE).get(0).getId();
		ApiFactory.getInstanceService().submitInstance(processId,report);
	}
	
	/**
	  * 方法名:流程办理
	  * <p>功能说明：</p>
	  * @return
	 */
	public void completeTaskCode(EcnReport report ,List<JSONObject> reportArrays,Long taskId,TaskProcessingResult taskTransact) throws Exception{
		saveEcnReport(report ,reportArrays);
		ApiFactory.getTaskService().completeWorkflowTask(taskId,taskTransact);
		String opinion = Struts2Utils.getParameter("opinion");
//		if(StringUtils.isNotEmpty(opinion)){
			WorkflowTask task = ApiFactory.getTaskService().getTask(taskId);
			//保存记录
			Opinion opinionParameter = new Opinion();
	        opinionParameter.setOpinion(opinion);
	        opinionParameter.setTransactor(ContextUtils.getLoginName());
	        opinionParameter.setTransactorName(ContextUtils.getUserName());
	        opinionParameter.setTaskName(task.getName());
	        opinionParameter.setTaskId(taskId);
	        opinionParameter.setAddOpinionDate(new Date());
	        ApiFactory.getOpinionService().saveOpinion(opinionParameter);
//		}
	}
	
	public void saveEcnReport(EcnReport report ,List<JSONObject> reportArrays) throws Exception{
		if(report.getId()==null){
			report.setEcnReportDetails(new ArrayList<EcnReportDetail>());
		}
		if(reportArrays!=null){
			report.getEcnReportDetails().clear();
			for(JSONObject obj:reportArrays){
				EcnReportDetail dd = new EcnReportDetail();
				dd.setCompanyId(ContextUtils.getCompanyId());
				dd.setCreatedTime(new Date());
				dd.setCreator(ContextUtils.getUserName());
				dd.setLastModifiedTime(new Date());
				dd.setLastModifier(ContextUtils.getUserName());
				for(Object key : obj.keySet()){
					String value = obj.getString(key.toString());
					setProperty(dd, key.toString(),value);
				}
				dd.setEcnReport(report);
				report.getEcnReportDetails().add(dd);
			}
		}
		ecnReportDao.save(report);
	}
	
	private void setProperty(Object obj, String property, Object value) throws Exception {
		String fieldName = property,customType = null;
		if(property.indexOf("_")>0){
			String[] strs = property.split("_");
			fieldName = strs[0];
			customType = strs[1];
		}
		Class<?> type = PropertyUtils.getPropertyType(obj, fieldName);
		if (type != null) {
			if (value == null || StringUtils.isEmpty(value.toString())) {
				PropertyUtils.setProperty(obj, fieldName, null);
			} else {
				if("timestamp".equals(customType)){
					PropertyUtils.setProperty(obj, fieldName,DateUtil.parseDateTime(value.toString()));
				}else if (String.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName, value.toString());
				} else if (Integer.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Integer.valueOf(value.toString()));
				} else if (Double.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Double.valueOf(value.toString()));
				} else if (Float.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Float.valueOf(value.toString()));
				} else if (Boolean.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Boolean.valueOf(value.toString()));
				} else if (Date.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,DateUtil.parseDateTime(value+""));
				} else {
					PropertyUtils.setProperty(obj, fieldName, value);
				}
			}
		}
	}
	/**
	 * 删除实体，流程相关文件都删除
	 * @param entity 删除的对象
	 */
	public void deleteEntity(EcnReport entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			//先删除子表
			Long reportId = entity.getId();
			String sql31 = "delete from ECM_ECN_REPORT_DETAIL where FK_ECN_REPORT = ?";	
			
			ecnReportDao.getSession().createSQLQuery(sql31)
			.setParameter(0,reportId)
			.executeUpdate();

			String sql = "delete from product_task_all_his where execution_id = ?";
			ecnReportDao.getSession().createSQLQuery(sql)
			.setParameter(0,workflowId)
			.executeUpdate();
			ecnReportDao.delete(entity);
			ApiFactory.getInstanceService().deleteInstance(entity);
		}else{
			ecnReportDao.delete(entity);
		}
	}

	/**
	 * 删除实体，流程相关文件都删除
	 * @param ids
	 */
	public String deleteEntity(String ids) {
		String[] deleteIds = ids.split(",");
		int deleteNum=0,failNum=0;
		for (String id : deleteIds) {
			EcnReport report = getEntity(Long.valueOf(id));
			deleteEntity(report);
			deleteNum++;
		}
		return deleteNum+" 条数据成功删除，"+failNum+" 条数据没有权限删除！";
	}
	
	@Override
	public void endInstanceExecute(Long arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void retrieveTaskExecute(Long arg0, Long arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteFormFlowable(Long arg0) {
		ecnReportDao.delete(arg0);
	}
	@Override
	public EcnReport getEntity(Long id) {
		return ecnReportDao.get(id);
	}
	@Override
	protected void saveEntity(EcnReport arg0) {
		// TODO Auto-generated method stub
		
	}
	 /**
     * 方法名:导出Excel文件 
     * <p>功能说明：</p>
     * @param incomingInspectionActionsReport
     * @throws IOException
    */
   public void exportReport(EcnReport s) throws IOException{
       InputStream inputStream = null;
       try {
    	   EcnReport report = s;                                                
           inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/ecm_ecn.xls");
           Map<String,ExportExcelFormatter> formatterMap = new HashMap<String, ExportExcelFormatter>();
           String exportFileName = "ECN变更单";
           ExcelUtil.exportToExcel(inputStream, exportFileName, report, formatterMap);
       }catch (Exception e) {
           log.error("导出失败!",e);
       } finally{
           if(inputStream != null){
               inputStream.close();
           }
       }
   }
}

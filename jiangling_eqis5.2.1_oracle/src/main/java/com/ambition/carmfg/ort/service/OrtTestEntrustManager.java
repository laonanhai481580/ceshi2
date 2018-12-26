package com.ambition.carmfg.ort.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.entity.FarAnalysisItem;
import com.ambition.carmfg.entity.OrtPlan;
import com.ambition.carmfg.entity.OrtTestEntrust;
import com.ambition.carmfg.entity.OrtTestItem;
import com.ambition.carmfg.ort.dao.OrtPlanDao;
import com.ambition.carmfg.ort.dao.OrtTestEntrustDao;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
/**
 * 类名:ORT实验委托Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月6日 发布
 */
@Service
@Transactional
public class OrtTestEntrustManager extends AmbWorkflowManagerBase<OrtTestEntrust>{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private OrtTestEntrustDao ortTestEntrustDao;
	@Autowired
	private OrtPlanDao ortPlanDao;
	@Override
	public Class<OrtTestEntrust> getEntityInstanceClass() {
		return OrtTestEntrust.class;
	}

	@Override
	public String getEntityListCode() {
		return OrtTestEntrust.ENTITY_LIST_CODE;
	}

	@Override
	public HibernateDao<OrtTestEntrust, Long> getHibernateDao() {
		return ortTestEntrustDao;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "ort-test-entrust";
	}

	@Override
	public String getWorkflowDefinitionName() {
		return "ORT实验委托流程";
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "ort-test-entrust.xls", OrtTestEntrust.ENTITY_LIST_NAME);
	}
	@Override
	public CompleteTaskTipType completeTask(OrtTestEntrust report,
			Map<String, List<JSONObject>> childMaps, Long taskId,
			TaskProcessingResult result) {
			String currentActivityName=report.getWorkflowInfo().getCurrentActivityName();
			CompleteTaskTipType type=null;
			if(currentActivityName.equals("主管审核")){
				report.setEntrustState("Close");
				String planId=report.getPlanId();
				if(planId!=null&&!"".equals(planId)){
					OrtPlan ortPlan=ortPlanDao.get(Long.valueOf(planId));
						ortPlan.setActualDate(report.getShenheDate());
						ortPlan.setTestResult(report.getTestResult());
				}	
			}
			type =  super.completeTask(report, childMaps, taskId, result);	
			return type;
	}
	/**
	  * 方法名:提交流程
	  * <p>功能说明：</p>
	  * @param report
	  * @return
	 */
	@Override
	public CompleteTaskTipType submitProcess(OrtTestEntrust report,Map<String,List<JSONObject>> childMaps){
			report.setEntrustState("Open");
			//保存实体的值
			saveEntity(report, childMaps);
			String planId=report.getPlanId();
			if(planId!=null&&!"".equals(planId)){
				OrtPlan ortPlan=ortPlanDao.get(Long.valueOf(planId));
				if(ortPlan.getIsTest()==null||ortPlan.getIsTest()!=1){
					ortPlan.setTestEntrustCode(report.getFormNo());
					ortPlan.setTestEntrustId(report.getId());
					ortPlan.setIsTest(1);
				}
			}					
		
		Long processId = ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(getWorkflowDefinitionCode()).get(0).getId();
		CompleteTaskTipType taskTipType = submitProcess(report,"发起",processId);
		
		//流程提交时间
		report.getWorkflowInfo().setSubmitTime(new Date());
		//更新新任务的环节状态
		//updateTaskStage(report);

		return taskTipType;
	}
	
	/**
	 * 删除实体，流程相关文件都删除
	 * @param entity 删除的对象
	 */
	@Override
	public void deleteEntity(OrtTestEntrust entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			//先删除子表
			Long reportId = entity.getId();
			String sql31 = "delete from MFG_ORT_TEST_ITEM where MFG_TEST_ENTRUST_ID = ?";	
			
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
	 * 查询所有部门
	 * @return
	 */
	public List<Department> queryAllDepartments(){
		String hql = "from Department d where d.deleted = 0 order by d.parent desc,weight";
		List<Department> list = ortTestEntrustDao.createQuery(hql).list();
		return list;
	}
	
	/**
	 * 查询所有用户
	 * @return
	 */
	public List<User> queryAllUsers() {
		// TODO Auto-generated method stub
		String hql = "from User d where d.deleted = 0 order by d.weight";
		List<User> list = ortTestEntrustDao.createQuery(hql).list();
		return list;
	}

	public void saveChild(OrtTestEntrust report, String childParams) {
		// TODO Auto-generated method stub
		 JSONArray itemStrArray=null;
         if(!childParams.isEmpty()){
             itemStrArray=JSONArray.fromObject(childParams);
			if (!itemStrArray.isEmpty()) {
				if (report.getOrtTestItems() == null) {
					report.setOrtTestItems(new ArrayList<OrtTestItem>());
				} else {
					report.getOrtTestItems().clear();
				}
				for (int i = 0; i < itemStrArray.size(); i++) {
					JSONObject jso = itemStrArray.getJSONObject(i);
					OrtTestItem item = new OrtTestItem();
					item.setCompanyId(ContextUtils.getCompanyId());
					item.setCreatedTime(new Date());
					item.setCreatorName(ContextUtils.getUserName());
					item.setCreator(ContextUtils.getLoginName());
					for (Object key : jso.keySet()) {
						String value = jso.getString(key.toString());
						try {
							setProperty(item, key.toString(), value);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}

					item.setOrtTestEntrust(report);
					report.getOrtTestItems().add(item);
				}
			}
         }
         ortTestEntrustDao.save(report);
	}
	 /**
     * 方法名: setProperty 
     * <p>功能说明：设置属性</p>
     * @return void
     * @throws   
      */
     private void setProperty(Object obj,String property,Object value) throws Exception{
         Class<?> type = PropertyUtils.getPropertyType(obj,property);
         if(type != null){
             if(value==null||StringUtils.isEmpty(value.toString())){
                 PropertyUtils.setProperty(obj,property,null);
             }else{
                  if(String.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,value.toString());
                 }else if(Integer.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Integer.valueOf(value.toString()));
                 }else if(Double.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Double.valueOf(value.toString()));
                 }else if(Float.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Float.valueOf(value.toString()));
                 }else if(Boolean.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Boolean.valueOf(value.toString()));
                 }else if(Date.class.getName().equals(type.getName())){
                     if(Date.class.getName().equals(value.getClass().getName())){
                         PropertyUtils.setProperty(obj,property,value);
                     }else if(String.class.getName().equals(value.getClass().getName())&&value.toString().length()==10){
                         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                         PropertyUtils.setProperty(obj,property,sdf.parse(value.toString()));
                     }
                 }else{
                     PropertyUtils.setProperty(obj,property,value);
                 }
             }
         }
     }
}

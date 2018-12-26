package com.ambition.product.workflow;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.ambition.product.base.WorkflowIdEntity;
import com.ambition.util.common.CheckMobileUtil;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.DateUtil;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.exportexcel.ExportExcelFormatter;
import com.norteksoft.mms.form.entity.TableColumn;
import com.norteksoft.mms.form.enumeration.DataType;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.orm.IdEntity;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.product.web.wf.WorkflowManagerSupport;
import com.norteksoft.task.base.enumeration.TaskProcessingMode;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.norteksoft.wf.base.utils.Dom4jUtils;
import com.norteksoft.wf.engine.client.EndInstanceInterface;
import com.norteksoft.wf.engine.client.FormFlowable;
import com.norteksoft.wf.engine.client.FormFlowableDeleteInterface;
import com.norteksoft.wf.engine.client.OnStartingSubProcess;
import com.norteksoft.wf.engine.client.RetrieveTaskInterface;
import com.norteksoft.wf.engine.client.WorkflowInfo;
import com.norteksoft.wf.engine.entity.WorkflowDefinition;
import com.norteksoft.wf.engine.entity.WorkflowDefinitionFile;

/**
 * 类名:工作流管理封装的基类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2015-4-20 发布
 */
public abstract class AmbWorkflowManagerBase<T extends WorkflowIdEntity> extends WorkflowManagerSupport<T> implements FormFlowableDeleteInterface,RetrieveTaskInterface,EndInstanceInterface,OnStartingSubProcess {
	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	  * 方法名: 获取DAO
	  * <p>功能说明：</p>
	  * @return
	 */
	public abstract HibernateDao<T, Long> getHibernateDao();
	
	/**
	  * 方法名:获取实体在系统元数据中定义的列表编码 
	  * <p>功能说明：</p>
	  * @return
	 */
	public abstract String getEntityListCode();
	
	/**
	  * 方法名: 获取实体在系统元数据中定义的台帐名称
	  * <p>功能说明：</p>
	  * @return
	 */
	public String getEntityListName(){
		return getWorkflowDefinitionName();
	}
	
	/**
	  * 方法名: 获取实例的Class
	  * <p>功能说明：</p>
	  * @return
	 */
	public abstract Class<T> getEntityInstanceClass();
	/**
	  * 方法名:获取实体对应的数据表名
	  * <p>功能说明：</p>
	  * @return
	 */
	public String getEntityTableName(){
		return getEntityListCode();
	}

	/**
	 * 获取工作流定义的编号
	 */
	public abstract String getWorkflowDefinitionCode();
	
	/**
	  * 获取工作流定义的名称
	 */
	public abstract String getWorkflowDefinitionName();
	/**
	  * 方法名: 根据表单编号查询对象
	  * <p>功能说明：</p>
	  * @param cla 对象
	  * @param formNo 编号
	  * @return
	 */
	public T findReportByFormNo(String formNo){
		return findReportByFormNo(formNo,"formNo");
	}
	
	/**
	  * 方法名: 根据表单编号查询对象
	  * <p>功能说明：</p>
	  * @param cla 对象
	  * @param formNo 编号
	  * @param formNoFieldName 编号对应的字段
	  * @return
	 */
	@SuppressWarnings("unchecked")
	public T findReportByFormNo(String formNo,String formNoFieldName){
		String hql = "from "+getEntityInstanceClass().getName()+" f where f."+formNoFieldName+" = ?";
		List<?> list = getHibernateDao().createQuery(hql,formNo).list();
		if(list.size()>0){
			return (T) list.get(0);
		}else{
			return null;
		}
	}

	/**
	  * 方法名: 设置子表的值
	  * <p>功能说明：</p>
	  * @param report
	  * @param childMaps
	  * @throws Exception
	 */
	public void setChildItems(T report,Map<String,List<JSONObject>> childMaps){
		try {
			if(childMaps != null){
				//子表中主表的字段名
				String reportFieldName = getEntityInstanceClass().getSimpleName();
				reportFieldName = reportFieldName.substring(0,1).toLowerCase() + reportFieldName.substring(1);
				for(String fieldName : childMaps.keySet()){
					List<JSONObject> arrays = childMaps.get(fieldName);
					//检查是否有对象属性
					Class<?> claType = PropertyUtils.getPropertyType(report, fieldName);
					System.out.println("fieldName："+fieldName);
					if(claType==null){
						throw new AmbFrameException(report.getClass() + "没有名称为[" + fieldName + "]的字段!");
					}
					if(!List.class.getName().equals(claType.getName())){
						throw new AmbFrameException(report.getClass() + "名称为[" + fieldName + "]的字段必须是List类型!");
					}
					@SuppressWarnings("unchecked")
					List<Object> items = (List<Object>) PropertyUtils.getProperty(report,fieldName);
					if(items==null){
						List<Object> objs = new ArrayList<Object>();
						PropertyUtils.setProperty(report,fieldName,objs);
						items = objs;
					}else{
						items.clear();//清除
					}
					for(JSONObject json : arrays){
						String entityClass = json.getString("entityClass").trim();
						json.remove("entityClass");
						IdEntity item = (IdEntity) Class.forName(entityClass).newInstance();
						
						item.setCompanyId(report.getCompanyId());
						item.setCreatedTime(report.getCreatedTime());
						item.setCreator(report.getCreator());
						item.setCreatorName(report.getCreatorName());
						item.setDepartmentId(report.getDepartmentId());
						item.setModifier(report.getModifier());
						item.setModifiedTime(report.getModifiedTime());
						item.setModifierName(report.getModifierName());
						//设置主表
						PropertyUtils.setProperty(item,reportFieldName,report);
						//设置属性
						for(Object key : json.keySet()){
							CommonUtil1.setProperty(item,key.toString(),json.get(key));
						}
						getHibernateDao().getSession().save(item);
						items.add(item);
					}
				}
			}
		} catch (Exception e) {
			if(e instanceof AmbFrameException){
				throw (AmbFrameException)e;
			}else{
				e.printStackTrace();
				throw new AmbFrameException("设置子表的值时失败!",e);
			}
		}
	}
	
	/**
	  * 方法名: 保存对象
	  * <p>功能说明：</p>
	  * @param report
	  * @return
	 */
	public void saveEntity(T report) {
		try {
			saveEntity(report,null);
		} catch (Exception e) {
			throw new AmbFrameException("保存对象失败!",e);
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
	public void saveEntity(T report,Map<String,List<JSONObject>> childMaps){
		report.setModifiedTime(new Date());
		report.setModifier(ContextUtils.getLoginName());
		report.setModifierName(ContextUtils.getUserName());
		getHibernateDao().save(report);
		//设置子表的值
		setChildItems(report,childMaps);
		
	}
	

	/**
	  * 方法名: 分页查询 是否延期
	  * <p>功能说明：</p>
	  * @param page
	  * @return
	 */
	public Page<T> search(Page<T> page){
		String hql = "from "+getEntityInstanceClass().getName()+" t";
		hql=hql+" where t.companyId=?";
		try {
			page = getHibernateDao().searchPageByHql(page,hql,ContextUtils.getCompanyId());
		} catch (Exception e) {
			throw new AmbFrameException("分页查询失败!",e);
		}
		//节点超期
		//searchExceedStatistics(page);
		return page;
	}
	
	public Page<T> oKsearch(Page<T> page){
		String hql = "from "+getEntityInstanceClass().getName()+" t";
		hql=hql+" where t.workflowInfo.state=? and t.companyId=?";
		try {
			page = getHibernateDao().searchPageByHql(page,hql,"流程已结束",ContextUtils.getCompanyId());
		} catch (Exception e) {
			throw new AmbFrameException("分页查询失败!",e);
		}
		//节点超期
		//searchExceedStatistics(page);
		return page;
	}
	
	
	

	/**
	  * 方法名: 查询按时完成率的台帐数据
	  * <p>功能说明：</p>
	  * @param page
	  * @param params
	  * @return
	 */
	public Page<T> searchForOntimeRateParams(Page<T> page,JSONObject params){
		String hql = "from "+getEntityInstanceClass().getName()+" t where t.workflowInfo.workflowId is not null";
		List<Object> searchParams = new ArrayList<Object>();
		if(params.containsKey("name")){
			hql += " and t.occurDept = ?";
			searchParams.add(params.getString("name"));
		}
		if(params.containsKey("ontimeState")){
			String ontimeState = params.getString("ontimeState");
			if(WorkflowIdEntity.ONTIMESTATE_ONTIME_COMPLETE.toString().equals(ontimeState)){
				hql += " and t.ontimeState in ("+ontimeState+","+WorkflowIdEntity.ONTIMESTATE_OVERDUE_COMPLETE+")";
			}else{
				hql += " and t.ontimeState = ?";
				searchParams.add(params.getInt("ontimeState"));
			}
		}
		if(params.containsKey("lastState")){
			hql += " and t.lastState = ?";
			searchParams.add(params.getString("lastState"));
		}
		page = getHibernateDao().searchPageByHql(page, hql,searchParams.toArray());
//		节点超期状态
//		searchExceedStatistics(page);
		return page;
	}
	
	/**
	  * 方法名: 查询任务超期情况
	  * <p>功能说明：</p>
	  * @param page
	  * @param entityTableName 实体对象的表名
	  * @param workflowDefinitionName 流程定义的名称
	  * @return
	  * @throws InstantiationException
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  * @throws NoSuchMethodException
	 */
	public Page<AmbWorkflowTask> searchTaskDatas(Page<AmbWorkflowTask> page) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		String sql = "select t.id,r.id as form_id,r.form_no,t.created_time as begin_date,"
				+ " t.transact_date,t.transactor,t.plan_date,t.name as task_name,t.overdue_day,t.duedate,"
				+ " t.overdue_hour,t.ontime_state,t.is_overdue from product_task_all_his t"
				+ " inner join "+getEntityTableName()+" r"
				+ " on t.group_name = ? and t.execution_id = r.workflow_id";
		return executeTaskSql(page,AmbWorkflowTask.class,sql,getWorkflowDefinitionName());
	}
	
	/**
	  * 方法名: 查询按时完成率
	  * <p>功能说明：</p>
	  * @param page
	  * @param isExpandAll
	  * @return
	  * @throws InstantiationException
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  * @throws NoSuchMethodException
	 */
	public Page<AmbOnTimeCloseRate> searchOntimeCloseDatas(Page<AmbOnTimeCloseRate> page,boolean isExpandAll) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		return searchOntimeCloseDatas(page,getEntityTableName(),isExpandAll);
	}
	
 	/*
	 * 删除流程实例时的回调方法（在流程参数中配置了beanName）
	 * 
	 * @see com.norteksoft.wf.engine.client.FormFlowableDeleteInterface#
	 * deleteFormFlowable(java.lang.Long)
	 * 2015-06-10
	 */
	//@Override
	public void deleteFormFlowable(Long id) {}
	
	/**
	 * 取回任务业务补偿
	 */
	//2015-06-10
	//@Override
	public void retrieveTaskExecute(Long entityId,Long taskId) {}

	/**
	 * 流程正常结束时的业务补偿
	 */
	//2015-06-10
	//@Override
	public void endInstanceExecute(Long entityId) {}
	
	/**
	  * 方法名: 根据ID获取对象
	  * <p>功能说明：</p>
	  * @param id 
	  * @return
	 */
	@Override
	public T getEntity(Long id) {
		return getHibernateDao().get(id);
		//return getHibernateDao(). .getById(id);
	}
	
	//2015-06-10
	//@Override
	public FormFlowable getRequiredSubEntity(Map<String, Object> param) {
		return null;
	}
	
	public String goback(Long taskId){
		return ApiFactory.getTaskService().returnTask(taskId);
	}
	
	/**
	  * 方法名: 获取对象当前的办理人员
	  * <p>功能说明：</p>
	  * @param id 
	  * @return
	 */
	public List<String[]> getTaskHander(T entity) {
	  return ApiFactory.getTaskService().getActivityTaskTransactors(entity);
	}
	
	/**
	  * 方法名: 根据任务ID查询对象
	  * <p>功能说明：</p>
	  * @param taskId 
	  * @return
	 */
	public T getEntityByTaskId(Long taskId) {
		if(taskId==null)return null;
		return getEntity(ApiFactory.getFormService().getFormFlowableIdByTask(taskId));
	}
	
	/**
	 * 删除实体，流程相关文件都删除
	 * @param entity 删除的对象
	 */
	public void deleteEntity(T entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			ApiFactory.getInstanceService().deleteInstance(entity);
			//删除关联的办理期限
//			String sql = "delete from product_task_all_his where execution_id = ?";
//			getHibernateDao().getSession().createSQLQuery(sql)
//				.setParameter(0,workflowId)
//				.executeUpdate();
			//删除对象
			//getHibernateDao().delete(entity);
		}else{
			getHibernateDao().delete(entity);
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
			T report = getEntity(Long.valueOf(id));
			deleteEntity(report);
			deleteNum++;
		}
		return deleteNum+" 条数据成功删除，"+failNum+" 条数据没有权限删除！";
	}
	
	/**
	  * 方法名:提交流程
	  * <p>功能说明：</p>
	  * @param report
	  * @return
	 */
	public CompleteTaskTipType submitProcess(T report,Map<String,List<JSONObject>> childMaps){
		//设置子表的值
		setChildItems(report,childMaps);
		this.saveEntity(report);//zsz
		Long processId = ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(getWorkflowDefinitionCode()).get(0).getId();
		CompleteTaskTipType taskTipType = submitProcess(report,"发起",processId);
		//流程提交时间
		report.getWorkflowInfo().setSubmitTime(new Date());
		//更新新任务的环节状态
		updateTaskStage(report);
		return taskTipType;
	}
	
	/**
	 * 方法名:办理任务
	 * @param report 
	 * @param childMaps 子表信息
	 * @param taskId 任务ID
	 * @param taskTransact 操作结果
	 * @return
	 */
	public CompleteTaskTipType completeTask(T report,Map<String,List<JSONObject>> childMaps,Long taskId,TaskProcessingResult result) {
		
		saveEntity(report,childMaps);
        //同意列表
        String opinion = Struts2Utils.getParameter("opinion");
        Opinion opinionParameter = new Opinion();
        if(null != result){
        	opinionParameter.setCustomField(Struts2Utils.getParameter(result.name()));
        }
        String processResult = "";
        if("REFUSE".equals(result.name())){
        	processResult = "不同意";
        }else  if("SUBMIT".equals(result.name())){
        	processResult = "提交";
        }else if("APPROVE".equals(result.name())){
        	processResult = "同意";
        }else{
        	processResult = "";
        }
        opinionParameter.setCustomField(processResult);
        opinionParameter.setOpinion(opinion);
        opinionParameter.setTaskId(taskId);
        opinionParameter.setAddOpinionDate(new Date());
        ApiFactory.getOpinionService().saveOpinion(opinionParameter);
        CompleteTaskTipType type= ApiFactory.getTaskService().completeWorkflowTask(taskId,result);
        
	    //更新新任务的环节状态
	    //updateTaskStage(report);
        //0:逾期未完成；1:逾期完成；2:按时完成；3:进行中
        if(ApiFactory.getInstanceService().isInstanceComplete(report)
        		&&report.getCompleteDate()==null){
        	report.setCompleteDate(new Date());
    		if(report.getRequiredDate() == null){
    			//按时完成
    			report.setOntimeState(WorkflowIdEntity.ONTIMESTATE_ONTIME_COMPLETE);
    		}else{
    			if(!report.getRequiredDate().after(report.getCompleteDate())){
    				//逾期完成
    				report.setOntimeState(WorkflowIdEntity.ONTIMESTATE_OVERDUE_COMPLETE);
    			}else{
    				//按时完成
    				report.setOntimeState(WorkflowIdEntity.ONTIMESTATE_ONTIME_COMPLETE);
    			}
    		}
    		getHibernateDao().save(report);
        }
		return type;
	}
	
	/**
	 * 保存并初始化流程
	 * @param definitionCode 流程编号
	 * @param t
	 */
	public void saveInstance(T report,Long taskId) {
		//保存实例
		saveEntity(report);
        //同意列表
		String opinion = Struts2Utils.getParameter("opinion");
        if(taskId != null && StringUtils.isNotEmpty(opinion)){
            Opinion opinionParameter = new Opinion();
//            opinionParameter.setCustomField(Struts2Utils.getParameter(result.name()));
            opinionParameter.setOpinion(opinion);
            opinionParameter.setTaskId(taskId);
            opinionParameter.setAddOpinionDate(new Date());
            ApiFactory.getOpinionService().saveOpinion(opinionParameter);	
        }
	}
	
	/**
	  * 方法名:导出Excel文件 
	  * <p>功能说明：</p>
	  * @param qrqcReport
	  * @throws IOException
	 */
	public void exportReport(Long entityId) throws IOException{
		
	}
	
	/**
	  * 方法名:导出Excel文件 
	  * <p>功能说明：</p>
	  * @param qrqcReport
	  * @throws IOException
	 */
	public void exportReport(Long entityId,String templateName,String exportFileName) throws IOException{
		exportReport(entityId, templateName, exportFileName,null);
	}
	
	/**
	  * 方法名:导出Excel文件 
	  * <p>功能说明：</p>
	  * @param qrqcReport
	  * @throws IOException
	 */
	public void exportReport(Long entityId,String templateName,String exportFileName,Map<String,ExportExcelFormatter> formatterMap) throws IOException{
		InputStream inputStream = null;
		try {
			T report = getEntity(entityId);
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/" + templateName);
			ExcelUtil.exportToExcel(inputStream, exportFileName, report,formatterMap);
		}catch (Exception e) {
			log.error("导出失败!",e);
		} finally{
			if(inputStream != null){
				inputStream.close();
			}
		}
	}
	
	public Page<T> searchExceedStatistics(Page<T> pageExceed){
		for(T t : pageExceed.getResult()){
			FormFlowable wft=(FormFlowable)t;
			if(wft.getWorkflowInfo()==null){
				continue;
			}
			//从流程历史节点中查询环节状态
			String sql = "select name,stage_name from product_task_all_his where execution_id = ? and is_overdue = ? and stage_name is not null order by created_time";
			List<?> list = getHibernateDao().getSession().createSQLQuery(sql)
							.setParameter(0,t.getWorkflowInfo().getWorkflowId())
							.setParameter(1,WorkflowIdEntity.ISOVERDUE_YES)
							.list();
			//超期办理的节点
			String overdueStages = ",";
			for(Object obj : list){
				Object[] objs = (Object[])obj;
				overdueStages += objs[1]+",";
			}
			t.setOverdueStages(overdueStages);
		}
		return pageExceed;
	}
	
	/**
	  * 方法名:绑定任务办理期限
	  * <p>功能说明：取得当前的待办任务,从流程配置中获取办理期限,
	  *    存储到任务表中并计算出计划完成的日期</p>
	  * @param formflowable
	 */
	@SuppressWarnings("unchecked")
	public void updateDueDate(FormFlowable formflowable){
		WorkflowInfo workflowInfo = formflowable.getWorkflowInfo();
		if(workflowInfo == null){
			return;
		}
		List<WorkflowTask> activeTasks = ApiFactory.getTaskService().getActivityTasks(formflowable);
		if(activeTasks.isEmpty()){
			return;
		}
		String hql = "from WorkflowDefinition w where w.processId = ?";
		List<?> list = getHibernateDao().createQuery(hql,workflowInfo.getWorkflowDefinitionId()).list();
		if(list.isEmpty()){
			return;
		}
		WorkflowDefinition df = (WorkflowDefinition)list.get(0);
		hql = "from WorkflowDefinitionFile f where f.wfDefinitionId = ?";
		list = getHibernateDao().createQuery(hql,df.getId()).list();
		if(list.isEmpty()){
			return;
		}
		WorkflowDefinitionFile wdFile = (WorkflowDefinitionFile)list.get(0);
		if(StringUtils.isEmpty(wdFile.getDocument())){
			return;
		}
		Document document = Dom4jUtils.getDocument(wdFile.getDocument());
        Element root = document.getRootElement();
        List<Element> taskChildren = root.elements("task");
        Map<String,Integer> duedateMap = new HashMap<String, Integer>();
        for(Element element : taskChildren){
        	String taskName = element.attributeValue("name");
        	if(StringUtils.isEmpty(taskName)){
        		continue;
        	}
        	List<Element> extendElements = element.elements("extend");
        	if(extendElements.isEmpty()){
        		continue;
        	}
        	List<Element> reminderElements = extendElements.get(0).elements("reminder");
        	if(reminderElements.isEmpty()){
        		continue;
        	}
        	Element reminder = reminderElements.get(0);
        	String duedate = reminder.attributeValue("duedate");
        	if(CommonUtil1.isInteger(duedate)){
        		duedateMap.put(taskName,Integer.valueOf(duedate));
        	}
        }
        Calendar calendar = Calendar.getInstance();
        Calendar temp = Calendar.getInstance();
        for(WorkflowTask task : activeTasks){
        	String taskName = task.getName();
        	Integer duedate = duedateMap.get(taskName);
        	if(duedate==null){
        		continue;
        	}
            //计算计划完成日期,根据假期的设置算出应该的日期
        	calendar.add(Calendar.DATE,(duedate/5+1)*5);
        	Map<String,List<Date>> holidayMap = ApiFactory.getSettingService().getHolidaySettingDays(task.getCreatedTime(), calendar.getTime());
        	List<Date> workDates = holidayMap.get("workDate");
        	if(workDates.isEmpty()){
        		continue;
        	}
        	Date planDate = null;
        	if(duedate<workDates.size()){
        		planDate = workDates.get(duedate);
        	}else{
        		planDate = workDates.get(workDates.size()-1);
        	}
        	calendar.setTime(planDate);
        	temp.setTime(task.getCreatedTime());
        	calendar.set(Calendar.HOUR_OF_DAY,temp.get(Calendar.HOUR_OF_DAY));
        	calendar.set(Calendar.MINUTE,temp.get(Calendar.MINUTE));
        	calendar.set(Calendar.SECOND,temp.get(Calendar.SECOND));
        	String sql = "update product_task_all_his set plan_date = ?,duedate = ? where id = ?";
        	getHibernateDao().getSession()
        		.createSQLQuery(sql)
        		.setParameter(0,calendar.getTime())
        		.setParameter(1,duedate)
        		.setParameter(2,task.getId())
        	.executeUpdate();
        }
	}
	
	/**
	  * 方法名:更新超期的小时数
	  * <p>功能说明：</p>
	 */
	public void updateOverdueHours(){
		String sql = "update product_task_all_his set overdue_hour = datediff(hour,plan_date,?),overdue_day = datediff(day,plan_date,?)+1,ontime_state=?,is_overdue=? where plan_date < ? and transact_date is null";
		Date currentDate = new Date();
		getHibernateDao().getSession().createSQLQuery(sql)
			.setParameter(0,currentDate)
			.setParameter(1,currentDate)
			.setParameter(2,WorkflowIdEntity.ONTIMESTATE_OVERDUE)
			.setParameter(3,WorkflowIdEntity.ISOVERDUE_YES)
			.setParameter(4, currentDate)
			.executeUpdate();
	}
	
	/**
	  * 方法名: 更新任务表所在的环节
	  * <p>功能说明：</p>
	  * @param t
	 */
	public void updateTaskStage(T t){
		if(t.getWorkflowInfo() == null){
			return;
		}
		String sql = "update product_task_all_his set stage_name = ? where execution_id = ? and transact_date is null";
		try {
			getHibernateDao().getSession().createSQLQuery(sql)
			.setParameter(0,t.getLastState())
			.setParameter(1,t.getWorkflowInfo().getWorkflowId())
			.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();		
		}
		
	}
	
	/**
	  * 方法名: 根据SQL查询对象
	  * <p>功能说明：</p>
	  * @param page
	  * @param sql
	  * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 */
	@SuppressWarnings("unchecked")
	public <K extends Object> Page<K> executeTaskSql(Page<K> page,Class<K> objClass,String sql,Object... searchValues) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<Object> searchParams = new ArrayList<Object>();
		if(searchValues != null){
			for(Object val : searchValues){
				searchParams.add(val);
			}
		}
		StringBuffer sqlSb = new StringBuffer("from (" + sql + ") t ");
		String searchParameters = Struts2Utils.getParameter("searchParameters");
		if(StringUtils.isNotEmpty(searchParameters)){
			JSONArray jsonParams = JSONArray.fromObject(searchParameters);
			boolean isFirst = true;
			for(Object obj : jsonParams){
				JSONObject p = (JSONObject)obj;
				String dbName = p.getString("dbName");
				String propValue = p.getString("propValue");
				String dataType = p.getString("dataType");
				String optSign = p.getString("optSign");
				Object value = propValue;
				if(DataType.DATE.getCode().equals(dataType)){
					value = DateUtil.parseDate(propValue,"yyyy-MM-dd HH:mm:ss");
				}else if(DataType.INTEGER.getCode().equals(dataType)){
					if(CommonUtil1.isInteger(propValue)){
						continue;
					}
					value = Integer.valueOf(propValue);
				}
				if(isFirst){
					isFirst = false;
					sqlSb.append(" where ");
				}else{
					sqlSb.append(" and ");
				}
				sqlSb.append("t." + dbName);
				if("like".equals(optSign)){
					sqlSb.append( " like ?");
					searchParams.add("%" + value + "%");
				}else{
					sqlSb.append(" " + optSign + " ?");
					searchParams.add(value);
				}
			}
		}
		
		//查询对应的字段映射
		String tableColumnSql = "select c.db_column_name,c.name,c.data_type from mms_table_column c "
					+ " inner join mms_data_table t "
					+ " on c.fk_data_table_id = t.id and t.name = ?";
		List<?> columns = getHibernateDao().getSession().createSQLQuery(tableColumnSql)
					.setParameter(0, Struts2Utils.getParameter("_list_code"))
					.list();
		Map<String,TableColumn> columnMap = new HashMap<String, TableColumn>();
		String orderBy = null;
		for(Object obj : columns){
			Object[] objs = (Object[])obj;
			String columnName = objs[0]+"";
			String fieldName = objs[1]+"";
			String dataType = objs[2]+"";
			TableColumn tableColumn = new TableColumn();
			tableColumn.setDataType(DataType.valueOf(dataType));
			tableColumn.setDbColumnName(columnName);
			tableColumn.setName(fieldName);
			columnMap.put(columnName,tableColumn);
			//转换Order by
			if(fieldName.equals(page.getOrderBy())){
				orderBy = columnName;
			}
		}
		//统计总数
		Query query = getHibernateDao().getSession().createSQLQuery("select count(*) " + sqlSb.toString());
		for(int index=0;index<searchParams.size();index++){
			query.setParameter(index,searchParams.get(index));
		}
		page.setTotalCount(Long.valueOf(query.list().get(0).toString()));
		//统计当前页
		if(StringUtils.isNotEmpty(page.getOrder())&&StringUtils.isNotEmpty(orderBy)){
			sqlSb.append(" order by " + orderBy + " " + page.getOrder());
		}
		query = getHibernateDao().getSession().createSQLQuery("select * " + sqlSb.toString());
		for(int index=0;index<searchParams.size();index++){
			query.setParameter(index,searchParams.get(index));
		}
		query.setFirstResult(page.getFirst()-1);
		query.setMaxResults(page.getPageSize());
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<?> list = query.list();
		List<K> results = new ArrayList<K>();
		for(Object obj : list){
			K entity = objClass.newInstance();
			Map<String,Object> objMap = (Map<String,Object>)obj;
			for(String columnName : objMap.keySet()){
				TableColumn tableColumn = columnMap.get(columnName);
				if(tableColumn==null){
					continue;
				}
				Object val = objMap.get(columnName);
				if(val==null){
					continue;
				}
				String fieldName = tableColumn.getName();
				Class<?> fieldClass = PropertyUtils.getPropertyType(entity, fieldName);
				if(fieldClass==null){
					continue;
				}
				if(fieldClass.getName().equals(Long.class.getName())){
					val = Long.valueOf(val.toString());
				}else if(fieldClass.getName().equals(Integer.class.getName())){
					val = Integer.valueOf(val.toString());
				}
				PropertyUtils.setProperty(entity, fieldName,val);
			}
			results.add(entity);
		}
		page.setResult(results);
		return page;
	}
	
	/**
	  * 方法名: 查询按时完成率
	  * <p>功能说明：</p>
	  * @param page 页面
	  * @param tableName 表名
	  * @return
	  * @throws InstantiationException
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  * @throws NoSuchMethodException
	 */
	public Page<AmbOnTimeCloseRate> searchOntimeCloseDatas(Page<AmbOnTimeCloseRate> page,String tableName) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		return searchOntimeCloseDatas(page, tableName, "dept_name",false);
	}
	
	/**
	  * 方法名: 查询按时完成率
	  * <p>功能说明：</p>
	  * @param page 页面
	  * @param tableName 表名
	  * @param isExportAll 是否导出全部
	  * @return
	  * @throws InstantiationException
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  * @throws NoSuchMethodException
	 */
	public Page<AmbOnTimeCloseRate> searchOntimeCloseDatas(Page<AmbOnTimeCloseRate> page,String tableName,boolean isExportAll) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		return searchOntimeCloseDatas(page, tableName, "occur_dept",isExportAll);
	}
	
	/**
	  * 方法名:查询按时完成率
	  * <p>功能说明：</p>
	  * @param page
	  * @return
	  * @throws InstantiationException
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  * @throws NoSuchMethodException
	 */
	public Page<AmbOnTimeCloseRate> searchOntimeCloseDatas(Page<AmbOnTimeCloseRate> page,String tableName,String nameTableField,boolean isExportAll) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<Object> searchParams = new ArrayList<Object>();
		StringBuffer whereSb = new StringBuffer(" where workflow_id is not null ");//流程已经启动
		String searchParameters = Struts2Utils.getParameter("searchParameters");
		if(StringUtils.isNotEmpty(searchParameters)){
			JSONArray jsonParams = JSONArray.fromObject(searchParameters);
			for(Object obj : jsonParams){
				JSONObject p = (JSONObject)obj;
				String dbName = p.getString("dbName");
				String propValue = p.getString("propValue");
				String dataType = p.getString("dataType");
				String optSign = p.getString("optSign");
				Object value = propValue;
				if(DataType.DATE.getCode().equals(dataType)){
					value = DateUtil.parseDate(propValue,"yyyy-MM-dd HH:mm:ss");
				}else if(DataType.INTEGER.getCode().equals(dataType)){
					if(CommonUtil1.isInteger(propValue)){
						continue;
					}
					value = Integer.valueOf(propValue);
				}
				if(whereSb.length()>0){
					whereSb.append(" and ");
				}
				if("name".equals(dbName)){
					dbName = nameTableField;
				}
				whereSb.append("t." + dbName);
				if("like".equals(optSign)){
					whereSb.append( " like ?");
					searchParams.add("%" + value + "%");
				}else{
					whereSb.append(" " + optSign + " ?");
					searchParams.add(value);
				}
			}
		}
		//如果是导出全部时,不需要考虑统计总数
		if(!isExportAll){
			//统计总数
			Query query = getHibernateDao().getSession().createSQLQuery("select count(*) from " + tableName + " " + whereSb.toString() + " group by "+nameTableField);
			for(int index=0;index<searchParams.size();index++){
				query.setParameter(index,searchParams.get(index));
			}
			if(query.list() != null && query.list().size() > 0){
				page.setTotalCount(Long.valueOf(query.list().get(0).toString()));
			}
		}
		//统计当前页的总数
		String selectSql = "select "+nameTableField+",count(*) total,sum(case ontime_state when ? then 1 else 0 end) close," +
				"sum(case ontime_state when ? then 1 else 0 end) overdueClose from " + tableName + whereSb + " group by "+nameTableField;
		searchParams.add(AmbOnTimeCloseRate.ONTIMESTATE_ONTIME_COMPLETE);
		searchParams.add(AmbOnTimeCloseRate.ONTIMESTATE_OVERDUE_COMPLETE);
		if(StringUtils.isNotEmpty(page.getOrder())){
			String orderBy = page.getOrderBy();
			if("allForms".equals(orderBy)){
				orderBy = "_total";
			}else if("onTimeClosed".equals(orderBy)){
				orderBy = "_close";
			}else if("overTimeClosed".equals(orderBy)){
				orderBy = "_overdue_close";
			}else{
				orderBy = nameTableField;
			}
			selectSql += " order by " + orderBy + " " + page.getOrder();
		}
		Query query = getHibernateDao().getSession().createSQLQuery(selectSql);
		for(int index=0;index<searchParams.size();index++){
			query.setParameter(index,searchParams.get(index));
		}
		//如果是导出全部时,不需要考虑分页
		if(!isExportAll){
			query.setFirstResult(page.getFirst()-1);
			query.setMaxResults(page.getPageSize());
		}
		List<?> list = query.list();
		List<AmbOnTimeCloseRate> results = new ArrayList<AmbOnTimeCloseRate>();
		Long id = 1l;
		Map<String,AmbOnTimeCloseRate> ontimeCloseMap = new HashMap<String, AmbOnTimeCloseRate>();
		StringBuffer nameStrs = new StringBuffer();
		for(Object obj : list){
			Object[] objs = (Object[])obj;
			AmbOnTimeCloseRate onTimeCloseRate = new AmbOnTimeCloseRate();
			onTimeCloseRate.setId(id++);
			onTimeCloseRate.setName(objs[0]+"");
			//总数
			if(objs[1] != null){
				onTimeCloseRate.setAllForms(Integer.valueOf(objs[1].toString()));
			}
			//按时完成数
			if(objs[2] != null){
				onTimeCloseRate.setOnTimeClosed(Integer.valueOf(objs[2].toString()));
			}
			//超期完成数
			if(objs[3] != null){
				onTimeCloseRate.setOverTimeClosed(Integer.valueOf(objs[3].toString()));
			}
			if(onTimeCloseRate.getAllForms() != null && onTimeCloseRate.getAllForms()>0){
				Integer allComplete = null;
				if(onTimeCloseRate.getOnTimeClosed() != null){
					//按时关闭率
					onTimeCloseRate.setOnTimeClosedRate(onTimeCloseRate.getOnTimeClosed()*1.0f/onTimeCloseRate.getAllForms());
					allComplete = onTimeCloseRate.getOnTimeClosed();
				}
				if(onTimeCloseRate.getOverTimeClosed() != null){
					if(allComplete == null){
						allComplete = onTimeCloseRate.getOverTimeClosed();
					}else{
						allComplete += onTimeCloseRate.getOverTimeClosed();
					}
				}
				if(allComplete != null){
					onTimeCloseRate.setClosedRate(allComplete*1.0f/onTimeCloseRate.getAllForms());
				}
				
				//已关闭数量
				onTimeCloseRate.setYesClosed(onTimeCloseRate.getOnTimeClosed()+onTimeCloseRate.getOverTimeClosed());
				//未关闭数量
				onTimeCloseRate.setNotClosed(onTimeCloseRate.getAllForms()-onTimeCloseRate.getYesClosed());
			}
			results.add(onTimeCloseRate);
			//缓存对应的集合
			ontimeCloseMap.put(onTimeCloseRate.getName(),onTimeCloseRate);
			if(nameStrs.length()>0){
				nameStrs.append(",");
			}
			nameStrs.append(objs[0]);
		}
		//查询每个表单当前所在的环节的数量
		//清除不是条件的对象
		searchParams.remove(searchParams.size()-1);
		searchParams.remove(searchParams.size()-1);
		if(!isExportAll){
			whereSb.append(" and " + nameTableField + " in ('" + nameStrs.toString().replaceAll(",","','") + "')");
		}
		String stageSql = "select "+nameTableField+",last_state,count(*) total from " + tableName + whereSb + " group by "+nameTableField + ",last_state";
		query = getHibernateDao().getSession().createSQLQuery(stageSql);
		for(int index=0;index<searchParams.size();index++){
			query.setParameter(index,searchParams.get(index));
		}
		list = query.list();
		for(Object obj : list){
			Object[] objs = (Object[])obj;
			if(objs[2]==null){
				continue;
			}
			String name = objs[0]+"";
			AmbOnTimeCloseRate onTimeCloseRate = ontimeCloseMap.get(name);
			if(onTimeCloseRate==null){
				continue;
			}
			String stageName = objs[1]+"";
			Integer num = Integer.valueOf(objs[2].toString());
			Class<?> cla = PropertyUtils.getPropertyType(onTimeCloseRate, stageName);
			if(cla == null){
				continue;
			}
			PropertyUtils.setProperty(onTimeCloseRate, stageName,num+"");
		}
		ontimeCloseMap.clear();
		page.setResult(results);
		return page;
	}
	
	/**
	 * 驳回流转任务
	 * @param taskId
	 * @param returnTaskName
	 * @param opinion
	 */
	public void returnToTask(Long taskId,String returnTaskName,String opinion) {
		//记录操作日志
		WorkflowTask task = ApiFactory.getTaskService().getTask(taskId);
		//保存记录
		Opinion opinionParameter = new Opinion();
        opinionParameter.setCustomField("驳回流转任务");
        opinionParameter.setOpinion(opinion);
        opinionParameter.setTransactor(ContextUtils.getLoginName());
        opinionParameter.setTransactorName(ContextUtils.getUserName());
        opinionParameter.setTaskName(task.getName());
        opinionParameter.setTaskId(taskId);
        opinionParameter.setAddOpinionDate(new Date());
        ApiFactory.getOpinionService().saveOpinion(opinionParameter);
        //驳回操作
		ApiFactory.getTaskService().returnTaskTo(taskId,returnTaskName);
	}
	
	/**
	 * 获取取回权限
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Boolean retrieveTask(T entity){
		String sql = "select p.name,p.transactor from product_task p inner join workflow_task t on p.id = t.id where t.process_instance_id = '"+entity.getWorkflowInfo().getWorkflowId()+"' and t.processing_mode <> 'TYPE_READ' and p.transact_date is not null order by p.transact_date";
		List<Object> list  = getHibernateDao().getSession().createSQLQuery(sql).list();
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
	/**
	  * 方法名: 获取可驳回的所有节点
	  * <p>功能说明：</p>
	  * @param report
	  * @return
	 */
	public List<Map<String,String>> queryReturnTasks(T report){
		List<Map<String,String>> returnTaskMaps = new ArrayList<Map<String,String>>();
		if(report.getWorkflowInfo()==null){
			return returnTaskMaps;
		}
		String sql = "select distinct t.transactor,t.transactor_name,wt.code,t.name from workflow_task wt "
					+" inner join product_task t on wt.id = t.id where wt.execution_id = ? and "
					+" wt.task_processing_result is not null and wt.processing_mode != ?";
		List<?> list = getHibernateDao().getSession().createSQLQuery(sql)
							.setParameter(0,report.getWorkflowInfo().getWorkflowId())
							.setParameter(1,TaskProcessingMode.TYPE_READ.name())
							.list();
		for(Object obj : list){
			Object[] objs = (Object[])obj;
			String loginName = objs[0]+"";
			String userName = objs[1]+"";
			String taskCode = objs[2]+"";
			String taskName = objs[3]+"";
			Map<String,String> map = new HashMap<String, String>();
			map.put("loginName",loginName);
			map.put("userName",userName);
			map.put("taskCode",taskCode);
			map.put("taskName",taskName);
			returnTaskMaps.add(map);
		}
		return returnTaskMaps;
	}
}


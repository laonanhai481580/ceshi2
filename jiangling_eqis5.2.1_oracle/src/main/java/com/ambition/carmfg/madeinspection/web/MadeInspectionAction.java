package com.ambition.carmfg.madeinspection.web;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.aftersales.baseinfo.service.CustomerListManager;
import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.carmfg.checkinspection.service.MfgCheckInspectionReportManager;
import com.ambition.carmfg.checkinspection.web.MfgCheckInspectionReportAction;
import com.ambition.carmfg.entity.InspectionPointTypeEnum;
import com.ambition.carmfg.entity.MfgCheckInspectionReport;
import com.ambition.carmfg.entity.MfgCheckItem;
import com.ambition.carmfg.entity.MfgManufactureMessage;
import com.ambition.carmfg.entity.MfgPlantParameterItem;
import com.ambition.carmfg.entity.MfgSupplierMessage;
import com.ambition.carmfg.madeinspection.service.MadeInspectionManager;
import com.ambition.iqc.entity.CheckItem;
import com.ambition.iqc.entity.MfgToMes;
import com.ambition.iqc.samplestandard.service.SampleCodeLetterManager;
import com.ambition.iqc.samplestandard.service.SampleSchemeManager;
import com.ambition.supplier.utils.DateUtil;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.webservice.QisToBackService;
import com.ibm.icu.text.SimpleDateFormat;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.product.web.wf.WorkflowActionSupport;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.opensymphony.xwork2.ActionContext;

/**
 * 类名:首检、巡检、末检 流程ACTION
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  吴旭明
 * @version 1.00 2016-9-3 发布
 */
@Namespace("/carmfg/inspection/made-inspection")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "carmfg/inspection/made-inspection", type = "redirectAction") })
public class MadeInspectionAction extends  WorkflowActionSupport<MfgCheckInspectionReport> {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private LogUtilDao logUtilDao;
	private MfgCheckInspectionReport mfgCheckInspectionReport;
	@Autowired
	private CustomerListManager customerListManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private Long id;
	private String deleteIds;// 删除的ids
    private Long taskId;//工作流任务id
    private TaskProcessingResult taskTransact; //点击的按钮
    private String fieldPermission; // 字段权限
    private WorkflowTask task;
    private String assignee;// 指派人
    private String workCode;
    private String currentActivityName;//当前流程环节名称
	@Autowired
    private MadeInspectionManager madeInspectionManager;
	@Autowired
	private SampleCodeLetterManager sampleCodeLetterManager;
	@Autowired
	private SampleSchemeManager sampleSchemeManager;
	@Autowired
	private MfgCheckInspectionReportManager mfgCheckInspectionReportManager;
	private Page<MfgCheckInspectionReport> page;
	
	private Page<MfgManufactureMessage> mmPage;
	
	private Page<MfgSupplierMessage> smPage;
	@Autowired
	private QisToBackService qisToBackService;
	@Override
	public MfgCheckInspectionReport getModel() {
		return mfgCheckInspectionReport;
	}

	@Override
	public String abandonReceive() {
		return null;
	}

	@Override
	public String addSigner() {
		return null;
	}

	@Override
	public String completeInteractiveTask() {
		return null;
	}
	@Action("complete-task")
	@Override
	public String completeTask() {
		try{
			String dateStr = Struts2Utils.getParameter("inspectionDate");
			mfgCheckInspectionReport.setInspectionDate(DateUtil.parseDate(dateStr,"yyyy-MM-dd HH:mm"));
			madeInspectionManager.completeTaskCode(mfgCheckInspectionReport, taskId, taskTransact);
			task = madeInspectionManager.getWorkflowTask(mfgCheckInspectionReport.getWorkflowInfo().getFirstTaskId());
			fieldPermission = madeInspectionManager.getFieldPermissionByTaskId(mfgCheckInspectionReport.getWorkflowInfo().getFirstTaskId());
			ActionContext.getContext().put("fieldPermission", fieldPermission==null?"":fieldPermission);
			//意见处理
			List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(mfgCheckInspectionReport);
			if(opinionParameters!=null && !opinionParameters.equals("") && opinionParameters.size()!=0){
				Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
			}else{
				Struts2Utils.getRequest().setAttribute("opinionParameters", new ArrayList<Opinion>());
			}
			addActionMessage("操作成功");
		}catch(Exception e){
			logUtilDao.debugLog("流程提交失败", "流程提交失败"+e.getMessage());
			addActionMessage("流程提交失败"+e.getMessage());
			mfgCheckInspectionReport=madeInspectionManager.getMfgCheckInspectionReport(id);
			e.printStackTrace();
		}
		reInput();
		return "process-task";
	}
	 /**
     * 方法名:导出Excel文件
     * <p>
     * 功能说明：
     * </p>
     * 
     * @param incomingInspectionActionsReport
     * @throws IOException
     */
    @Action("download-report")
    public void exportReport() throws Exception {
        try {
            prepareModel();
            madeInspectionManager.exportReport(mfgCheckInspectionReport);
        } catch (Exception e) {
            e.printStackTrace();
            renderText("导出失败:" + e.getMessage());
        }
    }

	@Override
	public String drawTask() {
		return null;
	}

	@Override
	public String fillOpinion() {
		return null;
	}

	@Override
	public String processEmergency() {
		return null;
	}

	@Override
	public String removeSigner() {
		return null;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public TaskProcessingResult getTaskTransact() {
		return taskTransact;
	}

	public void setTaskTransact(TaskProcessingResult taskTransact) {
		this.taskTransact = taskTransact;
	}

	public String getFieldPermission() {
		return fieldPermission;
	}

	public void setFieldPermission(String fieldPermission) {
		this.fieldPermission = fieldPermission;
	}

	public WorkflowTask getTask() {
		return task;
	}

	public void setTask(WorkflowTask task) {
		this.task = task;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getCurrentActivityName() {
		return currentActivityName;
	}

	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}

	public Page<MfgCheckInspectionReport> getPage() {
		return page;
	}

	public void setPage(Page<MfgCheckInspectionReport> page) {
		this.page = page;
	}

	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	public Page<MfgManufactureMessage> getMmPage() {
		return mmPage;
	}

	public void setMmPage(Page<MfgManufactureMessage> mmPage) {
		this.mmPage = mmPage;
	}

	public Page<MfgSupplierMessage> getSmPage() {
		return smPage;
	}

	public void setSmPage(Page<MfgSupplierMessage> smPage) {
		this.smPage = smPage;
	}

	@Action("retrieve")
	@Override
	public String retrieveTask() {
		String msg = madeInspectionManager.retrieve(taskId);
		this.renderText(msg);
		return null;
	}
	@Action("history")
	@Override
	public String showHistory() {
        try {
        	mfgCheckInspectionReport = madeInspectionManager.getEntityByTaskId(taskId);
            task = madeInspectionManager.getWorkflowTask(taskId);
            opinions = ApiFactory.getOpinionService().getOpinions(mfgCheckInspectionReport);
            ActionContext.getContext().getValueStack().push(mfgCheckInspectionReport);
        } catch (Exception e) {
        	logUtilDao.debugLog("查看流转历史出错", e.getMessage());
        }
        return SUCCESS;
	}
    public void prepareReceiveTask() throws Exception {
        prepareModel();
    }
    public void prepareCompleteTask() throws Exception {
        prepareModel();
        
    }
    public void prepareProcessTask() throws Exception {
        prepareModel();
    }
    public void prepareProcessEmergency() throws Exception {
        prepareModel();
    }
    
    public void prepareDistributeTask() throws Exception {
        prepareModel();
    }
    public void prepareSubmitProcess() throws Exception {
        prepareModel();
    }
	@SuppressWarnings("unchecked")
	@Action("submit-process")
	@Override
	public String submitProcess() {
        if(id != null){
        	mfgCheckInspectionReport.setLastModifiedTime(new Date());
        	mfgCheckInspectionReport.setLastModifier(ContextUtils.getUserName());
        }
        try {
        	String dateStr = Struts2Utils.getParameter("inspectionDate");
			mfgCheckInspectionReport.setInspectionDate(DateUtil.parseDate(dateStr,"yyyy-MM-dd HH:mm"));
        	List<JSONObject> checkItems = MfgCheckInspectionReportAction.getRequestCheckItems();
        	List<JSONObject> parameterItems =MfgCheckInspectionReportAction.getRequestPlantParamters();
    		String inspectionPointType=Struts2Utils.getParameter("inspectionPointType");
    		InspectionPointTypeEnum inEnum=null;
    		if("FIRSTINSPECTION".equals(inspectionPointType)){
    			inEnum=InspectionPointTypeEnum.FIRSTINSPECTION;
    		}else if("PATROLINSPECTION".equals(inspectionPointType)){
    			inEnum=InspectionPointTypeEnum.PATROLINSPECTION;
    		}else{
    			inEnum=InspectionPointTypeEnum.COMPLETEINSPECTION;
    		}
    		String mfgSupplierMessagesStr=Struts2Utils.getParameter("mfgSupplierMessagesStr");
    		String manufactureMessageStr=Struts2Utils.getParameter("manufactureMessageStr");
    		JSONArray manufactureArray=null;//生产信息
			JSONArray messagesArray=null;
			/*if(StringUtils.isNotEmpty(mfgSupplierMessagesStr)){
				messagesArray = JSONArray.fromObject(mfgSupplierMessagesStr);
			}
			if(StringUtils.isNotEmpty(manufactureMessageStr)){
				manufactureArray = JSONArray.fromObject(manufactureMessageStr);
			}*/
            boolean isOver= madeInspectionManager.submitProcess(mfgCheckInspectionReport,checkItems,InspectionPointTypeEnum.STORAGEINSPECTION,messagesArray,manufactureArray,parameterItems);
            if( mfgCheckInspectionReport.getWorkflowInfo()!=null){
            	 taskId = mfgCheckInspectionReport.getWorkflowInfo().getFirstTaskId();
                 task = ApiFactory.getTaskService().getTask(taskId);
            }            
            if(isOver){
            	addActionMessage("提交成功!");
            }else{
            	addActionMessage("有未领取检验项，系统默认为暂存!");
            }
            
            id=mfgCheckInspectionReport.getId();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("流程提交失败"+e.getMessage(),e);
            addActionMessage("提交失败:" + e.getMessage());
        }
        reInput();
        getRight(taskId,MadeInspectionManager.MFG_CODE);
        return "input";
	}
	@Action("delete")
	@Override
	public String delete() throws Exception {
		try{
			String message = madeInspectionManager.deleteMfgCheckInspectionReport(deleteIds);
			String str1=message.split("~")[0];
			String str2=message.split("~")[1];
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"删除制程报告，编号为:"+str1);
			renderText(str2);
        }catch(Exception e){
        	e.printStackTrace();
            renderText("删除失败:" + e.getMessage());
        }
		return null;
	}
	@Action("input")
	@Override
	public String input() throws Exception {
	    if(mfgCheckInspectionReport.getWorkflowInfo()==null||mfgCheckInspectionReport.getWorkflowInfo().getFirstTaskId()==null){
            taskId = null;
        }else{
            taskId = mfgCheckInspectionReport.getWorkflowInfo().getFirstTaskId();
            currentActivityName=mfgCheckInspectionReport.getWorkflowInfo().getCurrentActivityName();
        }
	    List<MfgCheckItem> checkItems = null;
	    List<MfgPlantParameterItem> mfgPlantParameterItems = null;
	    if(id!=null){
			checkItems = mfgCheckInspectionReport.getCheckItems();
			mfgPlantParameterItems =mfgCheckInspectionReport.getMfgPlantParameterItems();
		}else{
			checkItems = new ArrayList<MfgCheckItem>();
			mfgPlantParameterItems= new ArrayList<MfgPlantParameterItem>();
		}
	    Struts2Utils.getRequest().setAttribute("checkItems",checkItems);
	    Struts2Utils.getRequest().setAttribute("plantDetails",mfgPlantParameterItems);
	    reInput();
        getRight(taskId,MadeInspectionManager.MFG_CODE);
        return SUCCESS;
	}
	@Action("input-spc")
	public String inputSpc() throws Exception {
		JSONObject result = new JSONObject();
	    String spcSampleId = Struts2Utils.getParameter("spcSampleId");
	    try{
	    	 String reportId = madeInspectionManager.getReportIdBySpcSmapleId(spcSampleId);
	    	 result.put("error", false);
	    	 result.put("reportId", reportId);
	    }catch(Exception e){
	    	result.put("error", true);
	    	result.put("message", "找不到对应表单");
	    }
	    renderText(result.toString());
        return null;
	}
	 /**
		 * 抄送
		 * @return
		 */
	@Action("copy-tasks")
	public String copyTasks(){
		madeInspectionManager.createCopyTasks(taskId,Arrays.asList(assignee.split(",")), null, null);
		renderText("已抄送");
		return null;
	}
	/**
     * 指派
     * @return
     */
    @Action("assign")
    public String assign(){
    	try {
    		madeInspectionManager.assign(taskId, assignee);
		} catch (Exception e) {
			renderText("指派失败!" + e.getMessage());
			logUtilDao.debugLog("指派失败!",e.getMessage());
		}
        return null;
    }
	@SuppressWarnings("unchecked")
	@Action("check-items")
	public String getCheckItems() throws Exception {
		try {
			Date inspectionDate = DateUtil.parseDate(Struts2Utils.getParameter("inspectionDate"));
			String workProcedure = Struts2Utils.getParameter("workProcedure");
			String checkBomCode = Struts2Utils.getParameter("checkBomCode");
			Integer stockAmount = Integer.valueOf(Struts2Utils.getParameter("stockAmount"));
			List<JSONObject> checkItemArrays = MfgCheckInspectionReportAction.getRequestCheckItems();
			String inspectionPointType=Struts2Utils.getParameter("inspectionPointType");
			Map<String,Object> resultMap = mfgCheckInspectionReportManager.getCheckItems(workProcedure,checkBomCode,stockAmount, inspectionDate,checkItemArrays, inspectionPointType);
			
			List<MfgCheckItem> checkItems = (List<MfgCheckItem>)resultMap.get("checkItems");
			Struts2Utils.getRequest().setAttribute("checkItems",checkItems);
			Struts2Utils.getRequest().setAttribute("inspectionLevels",sampleCodeLetterManager.getInspectionLevelOptions(sampleSchemeManager.getUseBaseType().getBaseType()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	/**
	  * 方法名: 待检验的项目
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("wait-checked-items")
	public String getWaitCheckedItems() throws Exception{
//		if(id!=null){
//			mfgCheckInspectionReport=mfgCheckInspectionReportManager.getMfgCheckInspectionReport(id);
//		}
		List<MfgCheckItem> checkItems=null;
//		if(mfgCheckInspectionReport!=null&&!mfgCheckInspectionReport.getCheckItems().isEmpty()){
//			checkItems=mfgCheckInspectionReport.getCheckItems();
//		}
//		if(checkItems==null){
			Date inspectionDate = DateUtil.parseDate(Struts2Utils.getParameter("inspectionDate"));
			String workProcedure = Struts2Utils.getParameter("workProcedure");
			String checkBomCode = Struts2Utils.getParameter("checkBomCode");
			Integer stockAmount = Integer.valueOf(Struts2Utils.getParameter("stockAmount"));
			List<JSONObject> checkItemArrays = MfgCheckInspectionReportAction.getRequestCheckItems();
			String inspectionPointType=Struts2Utils.getParameter("inspectionPointType");
			Map<String,Object> resultMap = mfgCheckInspectionReportManager.getCheckItems(workProcedure,checkBomCode,stockAmount, inspectionDate,checkItemArrays, inspectionPointType);
			checkItems = (List<MfgCheckItem>)resultMap.get("checkItems");
//		}
		if(checkItems.size()!=0){
			Struts2Utils.getRequest().setAttribute("checkItems",checkItems);
		}else{
			Struts2Utils.getRequest().setAttribute("checkItems",new ArrayList<CheckItem>());
		}
		return SUCCESS;
	}
	
	/**
	  * 方法名: 验证项目状态
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("validate-item-status")
	public String getItemStatus(){
		String items=Struts2Utils.getParameter("items");
		String  itemNameStr="";
		String reportId=Struts2Utils.getParameter("reportId");
		id=reportId==""?null:Long.valueOf(reportId);
		if(id!=null){
			mfgCheckInspectionReport=madeInspectionManager.getMfgCheckInspectionReport(id);
			if(mfgCheckInspectionReport!=null&&mfgCheckInspectionReport.getCheckItems()!=null){
				for(MfgCheckItem item:mfgCheckInspectionReport.getCheckItems()){
					if(item.getCheckItemName().indexOf(items)>=0){
						itemNameStr+=item.getCheckItemName()+",";
					}
				}
			}
		}
		JSONObject jsonObj=new JSONObject();
		jsonObj.put("itemNameStr", itemNameStr);
		renderText(jsonObj.toString());
		return null;
	}
	
	
	@Action("plant-parameter-items")
	public String getPlantParameterItems() throws Exception{
		try {
			String workProcedure = Struts2Utils.getParameter("workProcedure");
			String machineNo = Struts2Utils.getParameter("machineNo");
			List<MfgPlantParameterItem> plantDetail=mfgCheckInspectionReportManager.getMfgPlantParameterItem(workProcedure,machineNo);
			if(plantDetail==null){
				plantDetail=new ArrayList<MfgPlantParameterItem>();
			}
			Struts2Utils.getRequest().setAttribute("plantDetails",plantDetail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
    /**
     * 任务办理界面
     * @return
     * @throws Exception
     */
    @Action("process-task")
    public String processTask() throws Exception {
        if(mfgCheckInspectionReport != null){
            List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(mfgCheckInspectionReport);
            Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
        }else if(taskId != null){
        	madeInspectionManager.deleteFormFlowable(taskId);
        }
        workCode=task.getCode();
        reInput();
        this.getRight(taskId,MadeInspectionManager.MFG_CODE);
        return SUCCESS;
    }
	@Override
	public String list() throws Exception {
		return null;
	}
	@Action("first-list")
	public String firstList(){
		return SUCCESS;
	}
	@Action("first-list-datas")
	public String firstListData(){
		page = mfgCheckInspectionReportManager.list(page,InspectionPointTypeEnum.FIRSTINSPECTION);
		this.renderText(PageUtils.pageToJson(page));
		return null;
	}
	@Action("wait-audit-list")
	public String waitAudit(){
		return SUCCESS;
	}
	@Action("wait-audit-list-datas")
	public String waitAuditListData(){
		page = mfgCheckInspectionReportManager.waitAuditList(page);
		this.renderText(PageUtils.pageToJson(page));
		return null;
	}	

	@Action("recheck-list")
	public String recheck(){
		return SUCCESS;
	}
	@Action("recheck-list-datas")
	public String recheckListData(){
		page = mfgCheckInspectionReportManager.recheckList(page);
		this.renderText(PageUtils.pageToJson(page));
		return null;
	}		
	/**
	  * 方法名:重检导出 
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("wait-audit-export")
	public String waitAuditExport() throws Exception {
		try {
			Page<MfgCheckInspectionReport> page = new Page<MfgCheckInspectionReport>(100000);
			page = mfgCheckInspectionReportManager.recheckList(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_RECHECK_INSPECTION_REPORT"),"检验报告"));
		} catch (Exception e) {
			logUtilDao.debugLog("重检导出失败", e.getMessage());
		}
		return null;
	}	
	/**
	  * 方法名:待审核导出 
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("recheck-export")
	public String recheckExport() throws Exception {
		try {
			Page<MfgCheckInspectionReport> page = new Page<MfgCheckInspectionReport>(100000);
			page = mfgCheckInspectionReportManager.waitAuditList(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_RECHECK_INSPECTION_REPORT"),"检验报告"));
		} catch (Exception e) {
			logUtilDao.debugLog("待审核导出失败", e.getMessage());
		}
		return null;
	}		
	@Action("patrol-list")
	public String patrolList(){
		return SUCCESS;
	}
	@Action("patrol-list-datas")
	public String patrolListData(){
		page = mfgCheckInspectionReportManager.list(page,InspectionPointTypeEnum.PATROLINSPECTION);
		this.renderText(PageUtils.pageToJson(page));
		return null;
	}
	@Action("end-list")
	public String endList(){
		return SUCCESS;
	}
	@Action("end-list-datas")
	public String endListData(){
		page = mfgCheckInspectionReportManager.list(page,InspectionPointTypeEnum.COMPLETEINSPECTION);
		this.renderText(PageUtils.pageToJson(page));
		return null;
	}
	
	/**
	  * 方法名:首检导出 
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("first-export")
	public String firstExport() throws Exception {
		try {
			Page<MfgCheckInspectionReport> page = new Page<MfgCheckInspectionReport>(100000);
			page = mfgCheckInspectionReportManager.list(page,InspectionPointTypeEnum.FIRSTINSPECTION);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_FIRST_INSPECTION_REPORT"),"检验报告"));
		} catch (Exception e) {
			logUtilDao.debugLog("首检导出失败", e.getMessage());
		}
		return null;
	}
	/**
	  * 方法名:巡检导出 
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("patrol-exports")
	public String patrolExport() throws Exception {
		try {
			Page<MfgCheckInspectionReport> page = new Page<MfgCheckInspectionReport>(100000);
			page = mfgCheckInspectionReportManager.list(page,InspectionPointTypeEnum.PATROLINSPECTION);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_PATROL_INSPECTION_REPORT"),"检验报告"));
		} catch (Exception e) {
			logUtilDao.debugLog("巡检导出失败", e.getMessage());
		}		
		return null;
		
	}
	/**
	  * 方法名:末检导出 
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("end-export")
	public String endExport() throws Exception {
		try {
			Page<MfgCheckInspectionReport> page = new Page<MfgCheckInspectionReport>(100000);
			page = mfgCheckInspectionReportManager.list(page,InspectionPointTypeEnum.COMPLETEINSPECTION);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_END_INSPECTION_REPORT"),"检验报告"));
		} catch (Exception e) {
			logUtilDao.debugLog("末检导出失败", e.getMessage());
		}			
		return null;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(taskId!=null){
			mfgCheckInspectionReport = madeInspectionManager.getMfgCheckInspectionReportByTaskId(taskId);
            task = madeInspectionManager.getWorkflowTask(taskId);
            opinions = madeInspectionManager.getOpinions(mfgCheckInspectionReport);
            ApiFactory.getFormService().fillEntityByTask(mfgCheckInspectionReport, taskId);
        }else if(id!=null){
        	mfgCheckInspectionReport=madeInspectionManager.getMfgCheckInspectionReport(id);
            task = madeInspectionManager.getMyTask(mfgCheckInspectionReport,ContextUtils.getLoginName());
            if(task!=null)taskId = task.getId();
            if(task==null) taskId = mfgCheckInspectionReport.getWorkflowInfo().getFirstTaskId();
            if(mfgCheckInspectionReport.getWorkflowInfo()!=null) 
            opinions = madeInspectionManager.getOpinions(mfgCheckInspectionReport);   
        }else if(id==null){
        	mfgCheckInspectionReport=new MfgCheckInspectionReport();
        	mfgCheckInspectionReport.setCompanyId(ContextUtils.getCompanyId());
        	mfgCheckInspectionReport.setCreatedTime(new Date());
        	mfgCheckInspectionReport.setCreator(ContextUtils.getUserName());
//        	mfgCheckInspectionReport.setInspectionDate(new Date());
        	mfgCheckInspectionReport.setInspector(ContextUtils.getUserName());
        	mfgCheckInspectionReport.setMfgSupplierMessages(new ArrayList<MfgSupplierMessage>());
        	mfgCheckInspectionReport.setManufactureMessages(new ArrayList<MfgManufactureMessage>());
        	mfgCheckInspectionReport.setInspectionNo(formCodeGenerated.generateMFGode());
        	mfgCheckInspectionReport.setBusinessUnitName(ContextUtils.getSubCompanyName());
        	mfgCheckInspectionReport.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
        }
	}
	public static void main(String[] args) throws ParseException {
		String dateStr = "2017-06-16 05:00";
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
		System.out.println(DateUtil.parseDate(dateStr,"yyyy-MM-dd HH:mm"));
	}
	@Action("save")
	@Override
	public String save() throws Exception {
		try {
			String dateStr = Struts2Utils.getParameter("inspectionDate");
			mfgCheckInspectionReport.setInspectionDate(DateUtil.parseDate(dateStr,"yyyy-MM-dd HH:mm"));
			List<JSONObject> checkItems = MfgCheckInspectionReportAction.getRequestCheckItems();
			List<JSONObject> parameterItems =MfgCheckInspectionReportAction.getRequestPlantParamters();
			String inspectionPointType=Struts2Utils.getParameter("inspectionPointType");
			InspectionPointTypeEnum inEnum=null;
			if("FIRSTINSPECTION".equals(inspectionPointType)){
				inEnum=InspectionPointTypeEnum.FIRSTINSPECTION;
			}else if("PATROLINSPECTION".equals(inspectionPointType)){
				inEnum=InspectionPointTypeEnum.PATROLINSPECTION;
			}else{
				inEnum=InspectionPointTypeEnum.COMPLETEINSPECTION;
			}
			String method = Struts2Utils.getParameter("acquisitionMethod");
			String mfgSupplierMessagesStr=Struts2Utils.getParameter("mfgSupplierMessagesStr");
			String manufactureMessageStr=Struts2Utils.getParameter("manufactureMessageStr");
			JSONArray messagesArray=null;//物料信息
			JSONArray manufactureArray=null;//生产信息
			/*if(StringUtils.isNotEmpty(mfgSupplierMessagesStr)){
				messagesArray = JSONArray.fromObject(mfgSupplierMessagesStr);
			}
			if(StringUtils.isNotEmpty(manufactureMessageStr)){
				manufactureArray = JSONArray.fromObject(manufactureMessageStr);
			}*/
			madeInspectionManager.saveMadeInspection(mfgCheckInspectionReport,checkItems,inEnum,messagesArray,manufactureArray,parameterItems);
			if(Struts2Utils.getParameter("isLedger") != null){
				renderText(JsonParser.getRowValue(mfgCheckInspectionReport));
				return null;
			}else{
				addActionMessage("保存成功!");
				Struts2Utils.getRequest().setAttribute("accordionMenu","input");
				Struts2Utils.getRequest().setAttribute("inspectionLevels",sampleCodeLetterManager.getInspectionLevelOptions(sampleSchemeManager.getUseBaseType().getBaseType()));
				reInput();
				getRight(taskId,MadeInspectionManager.MFG_CODE);
				return "input";
			}
		} catch (Exception e) {
			logUtilDao.debugLog("保存失败",e.getMessage());
			mfgCheckInspectionReport.setId(id);
			addActionMessage("保存失败:" + e.getMessage());
			reInput();
			return "input";
		}
	}
	@Action("view-info")
    public String viewInfo(){
        if(id != null){ 
        	mfgCheckInspectionReport = madeInspectionManager.getMfgCheckInspectionReport(id);
            if(mfgCheckInspectionReport.getWorkflowInfo()!=null){
                taskId=mfgCheckInspectionReport.getWorkflowInfo().getFirstTaskId();
            }
        }else{
            addActionMessage("没有符合条件的检验报告单!");
        }
        getRight(taskId,MadeInspectionManager.MFG_CODE);
        reInput();
        fieldPermission = "[{request:\"false\",readonly:\"true\",controlType:\"allReadolny\"}]";
        if(mfgCheckInspectionReport != null && mfgCheckInspectionReport.getWorkflowInfo()!=null){
            List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(mfgCheckInspectionReport);
            if(opinionParameters!=null && !opinionParameters.equals("") && opinionParameters.size()!=0){
                Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
            }else{
                Struts2Utils.getRequest().setAttribute("opinionParameters", new ArrayList<Opinion>());
            }
        }
        ActionContext.getContext().getValueStack().push(mfgCheckInspectionReport);
        return SUCCESS;
    }
    /**
     * 获取权限
     * @param taskId
     * @param defCode
     */
    private void getRight(Long taskId,String defCode) {
        if(taskId==null){
            fieldPermission = madeInspectionManager.getFieldPermission(defCode);//禁止或必填字段
        }else{
            fieldPermission = madeInspectionManager.getFieldPermissionByTaskId(taskId);//禁止或必填字段
            taskPermission = madeInspectionManager.getActivityPermission(taskId); 
        }
    }
    private void reInput(){
    	//事业部
    	ActionContext.getContext().put("businessUnits",ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits"));
    	//处理方式
    	ActionContext.getContext().put("mfg_processing_result",ApiFactory.getSettingService().getOptionsByGroupCode("mfg_processing_result"));
    	//检验类型
    	ActionContext.getContext().put("inspection_models",ApiFactory.getSettingService().getOptionsByGroupCode("inspection_models"));
    	// 产品类别
    	ActionContext.getContext().put("mfg_category",ApiFactory.getSettingService().getOptionsByGroupCode("mfg_category"));
    	ActionContext.getContext().put("processSections",ApiFactory.getSettingService().getOptionsByGroupCode("processSections"));
    	//工序
    	ActionContext.getContext().put("mfg_process_bus",ApiFactory.getSettingService().getOptionsByGroupCode("mfg_work_procedure"));
    	//工序
    	ActionContext.getContext().put("mfg_business_unit_name",ApiFactory.getSettingService().getOptionsByGroupCode("mfg_business_unit_name"));
    	//批次数量单位
    	ActionContext.getContext().put("amountUnits",ApiFactory.getSettingService().getOptionsByGroupCode("mfg_amount_unit"));
    	List<Option> customers=customerListManager.listAllForOptions();
        ActionContext.getContext().put("customers",customers);
    	//班别
    	ActionContext.getContext().put("mfg_work_group_type",ApiFactory.getSettingService().getOptionsByGroupCode("mfg_work_group_type"));
    	if("欧菲科技-CCM".equals(ContextUtils.getCompanyName())){
    		ActionContext.getContext().put("mfg_audit_mans",ApiFactory.getSettingService().getOptionsByGroupCode("mfg_audit_mans"));
    	}
    	List<MfgCheckItem> mfgCheckItems=null;
    	List<MfgSupplierMessage> mfgSupplierMessages = mfgCheckInspectionReport.getMfgSupplierMessages();
    	if(mfgSupplierMessages.isEmpty()&&mfgSupplierMessages.size()==0){
			MfgSupplierMessage mm=new MfgSupplierMessage();
			mfgCheckInspectionReport.getMfgSupplierMessages().add(mm);
		}
    	List<MfgManufactureMessage> manufactureMessages = mfgCheckInspectionReport.getManufactureMessages();
		if(manufactureMessages!=null&&manufactureMessages.size()==0){
			MfgManufactureMessage mm=new MfgManufactureMessage();
			mfgCheckInspectionReport.getManufactureMessages().add(mm);
		}
    	if(mfgCheckInspectionReport.getCheckItems()==null){
			mfgCheckItems = new ArrayList<MfgCheckItem>();
		}else{
			mfgCheckItems=mfgCheckInspectionReport.getCheckItems();
		}
	    Struts2Utils.getRequest().setAttribute("checkItems",mfgCheckItems);
	    List<MfgPlantParameterItem> parameterItems=null;
	    if(mfgCheckInspectionReport.getMfgPlantParameterItems()==null){
	    	parameterItems = new ArrayList<MfgPlantParameterItem>();
	    }else{
	    	parameterItems=mfgCheckInspectionReport.getMfgPlantParameterItems();
	    }
	    Struts2Utils.getRequest().setAttribute("plantDetails",parameterItems);
    }
    
    /**
	  * 方法名: 物料信息
	  * <p>功能说明：</p>
	  * @return
	 */
    @Action("bom-traceability-list")
    public String getBomTraceabilityList(){
    	return SUCCESS;
    }
    
    @Action("bom-traceability-list-datas")
    public String getBomTraceabilityListDatas(){
    	try{
    		smPage=madeInspectionManager.searchMfgSupplierMessagePage(smPage);
    	this.renderText(PageUtils.pageToJson(smPage));
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
    /**
	  * 方法名: 生产信息
	  * <p>功能说明：</p>
	  * @return
	 */
    @Action("product-traceability-list")
    public String getProductTraceabilityList(){
    	return SUCCESS;
    }
    
    @Action("product-traceability-list-datas")
    public String getProductTraceabilityListDatas(){
    	mmPage=madeInspectionManager.searchMfgManufactureMessagePage(mmPage);
    	this.renderText(PageUtils.pageToJson(mmPage));
    	return null;
    }
    @Action("get-process-card")
    public String getReportInfomation(){
    	String processCard=Struts2Utils.getParameter("processCard");
    	String workProcedure=Struts2Utils.getParameter("workProcedure");
    	MfgToMes mes= new MfgToMes();
    	mes.setProcessCard(processCard);
    	mes.setWorkProcedure(workProcedure);
    	QisToBackService qbs=new QisToBackService();
    	String returnMessage="";
    	JSONObject obj=new JSONObject();
    	try {
    		returnMessage=madeInspectionManager.mfgToBarch(qbs,mes);
    		obj.put("message", returnMessage);
		} catch (Exception e) {
			logUtilDao.debugLog("获取数据失败", returnMessage);
		}
    	renderText(obj.toString());
    	return null;
    }
    
}

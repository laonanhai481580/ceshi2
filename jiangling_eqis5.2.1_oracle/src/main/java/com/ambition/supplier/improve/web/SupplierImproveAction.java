package com.ambition.supplier.improve.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.iqc.inspectionreport.service.IncomingInspectionActionsReportManager;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.archives.service.SupplierManager;
import com.ambition.supplier.baseInfo.services.SupplierMaterialTypeGoalManager;
import com.ambition.supplier.develop.services.SupplierDevelopManager;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.entity.SupplierImprove;
import com.ambition.supplier.entity.SupplierMaterialTypeGoal;
import com.ambition.supplier.improve.services.SupplierImproveManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CheckMobileUtil;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.tools.StringUtils;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.AsyncMailUtils;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.opensymphony.xwork2.ActionContext;
@Namespace("/supplier/improve")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/improve", type = "redirectAction") ,
			@Result(name = "mobile-input", location = "mobile/improve-input.jsp",type="dispatcher")})
public class SupplierImproveAction extends AmbWorkflowActionBase<SupplierImprove>{
		/**
		  *SupplierImproveAction.java2016年11月4日
		 */
	public static final String MOBILEINPUT = "mobile-input";
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SupplierDevelopManager supplierDevelopManager;
	@Autowired
	private SupplierImproveManager supplierImproveManager;
	@Autowired
	private IncomingInspectionActionsReportManager iiarManager;
	private String ids;
    @Autowired
    private FormCodeGenerated formCodeGenerated;
    @Autowired
    private AcsUtils acsUtils;
    boolean isCurrent = false;
    private String nowTaskName;
    private String isNewData;
    private String selectType;
    private String assignee; //指派人
    @Autowired
	private SupplierManager supplierManager;
	@Override
	protected AmbWorkflowManagerBase<SupplierImprove> getAmbWorkflowBaseManager() {
		return supplierImproveManager;
	}
	public String getNowTaskName() {
		 nowTaskName = report.getWorkflowInfo().getCurrentActivityName();  
	     return nowTaskName;
	}
	public void setNowTaskName(String nowTaskName) {
		this.nowTaskName = nowTaskName;
	}
	@Autowired
	private SupplierMaterialTypeGoalManager supplierMaterialTypeGoalManager;
	private List<String> returnableTaskNames = new ArrayList<String>();

	public List<String> getReturnableTaskNames() {
		returnableTaskNames = ApiFactory.getTaskService()
				.getReturnableTaskNames(taskId);
		return returnableTaskNames;
	}

	public void setReturnableTaskNames(List<String> returnableTaskNames) {
		this.returnableTaskNames = returnableTaskNames;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public boolean isCurrent() {
		return isCurrent;
	}
	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}
	
	public String getSelectType() {
		return selectType;
	}
	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}	
	
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getIsNewData() {
		return isNewData;
	}
	public void setIsNewData(String isNewData) {
		this.isNewData = isNewData;
	}
	/**
     * 方法名: 初始化的参数在这里写
     * <p>功能说明：</p>
    */
   public void initForm(){
       if(getId()==null){
    	   report.setCreatedTime(new Date());
    	   report.setFormNo(formCodeGenerated.getSupplierImproveCode());
    	   report.setCreator(ContextUtils.getLoginName());
    	   report.setCreatorName(ContextUtils.getUserName());
    	   report.setInspector(ContextUtils.getUserName());
    	   report.setBusinessUnitName(ContextUtils.getCompanyName());
    	   report.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
    	   report.setHappenSpace("进料检验");
    		  isNewData="是";  	   
       }else{
    	   isNewData=report.getIsNewData();
       }
       if(getId()==null){
    	   addImprove();
		}
       if(task!=null){
			if(ContextUtils.getLoginName().equals(task.getTransactor())){
				isCurrent = true;
				ActionContext.getContext().put("isCurrent",isCurrent);
			}
			 List<String[]> list=getAmbWorkflowBaseManager().getTaskHander(report);
			 if(list.size()>0){
				 Object objs[] = (Object[])list.get(0);
				 report.setCurrentManLog(objs[0].toString());
				 report.setCurrentMan(objs[1].toString());
			 }
		}
      //selectMaterialType();
     ActionContext.getContext().put("mrbApplys",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-improve-mrb-applys"));
     ActionContext.getContext().put("dealOpinions",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-improve-deal-opinions"));
     ActionContext.getContext().put("happenSpaces",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-improve-happenSpaces"));
     ActionContext.getContext().put("productStages",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-product-stage"));
     ActionContext.getContext().put("labTestResults",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-lab-test-result"));
     ActionContext.getContext().put("exceptionTypes",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-exception_type"));
   }
	/**
	 * 判定是否发起过改进
	 */
	@Action("is-exist-improve")
	public String isExistImprove() throws Exception {
		JSONObject obj = new JSONObject();
		String formNo=Struts2Utils.getParameter("id");	
		boolean flag=supplierImproveManager.isExistIqcSupplierImprove(Long.valueOf(formNo));
		if(flag){
			obj.put("error",true);
		}		
		this.renderText(obj.toString());
		return null;
	}	
	public void addImprove(){
		String formNo=Struts2Utils.getParameter("formId");
		if(formNo!=null&&!"".equals(formNo)){
			IncomingInspectionActionsReport iiar=iiarManager.getIncomingInspectionActionsReport(Long.valueOf(formNo));
			if(iiar!=null){
				getReport().setBusinessUnitName(iiar.getBusinessUnitName());
				getReport().setReportChecker(iiar.getAuditMan());
				getReport().setReportCheckerLog(iiar.getAuditLoginMan());
				getReport().setApprovaler(iiar.getLastStageMan());
				getReport().setApprovalerLog(iiar.getLastStateLoginMan());
				//getReport().setSqeChecker(iiar.getLastStageMan());
				//getReport().setSqeCheckerLog(iiar.getLastStateLoginMan());
				getReport().setHappenSpace("进料检验");
				getReport().setBillingArea("IQC");
				getReport().setLotNo(iiar.getLotNo());
				getReport().setOrderNo(iiar.getOrderNo());
				getReport().setProductStage(iiar.getProductStage());
				getReport().setSupplierName(iiar.getSupplierName());
				getReport().setSupplierCode(iiar.getSupplierCode());
				getReport().setInspectionFormNo(iiar.getInspectionNo());
				getReport().setInspectionId(iiar.getId());
				if(StringUtils.isNotEmpty(iiar.getSupplierCode())){
					Supplier supplier = supplierManager.getSupplier(iiar.getSupplierCode());
					if(supplier!=null){
						getReport().setSupplierEmail(supplier.getSupplierEmail());
					}
				}
				getReport().setBomName(iiar.getCheckBomName());
				getReport().setBomCode(iiar.getCheckBomCode());
				getReport().setInspectionDate(iiar.getInspectionDate());
				getReport().setInspector(iiar.getInspector());
				if(iiar.getCheckAmount()!=null){
					getReport().setCheckAmount(Double.valueOf(iiar.getCheckAmount()));	
				}				
				if(iiar.getStockAmount()!=null){
					getReport().setIncomingAmount(iiar.getStockAmount());
				}
				getReport().setMaterialType(iiar.getCheckBomMaterialType());
				getReport().setUnits(iiar.getUnits());
				//外观
				if(iiar.getAppearanceAmountRate()!=null&&iiar.getAppearanceAmount()!=null&&iiar.getAppearanceUnAmount()!=null){
					if(iiar.getAppearanceAmount().equals("0")&&iiar.getAppearanceUnAmount().equals("0")){
						getReport().setSurfaceBadRate(0.0);
						getReport().setSurfaceBad("否");
					}else{
						Double a=100-Double.valueOf(iiar.getAppearanceAmountRate());
						BigDecimal   b   =   new   BigDecimal(a);  
						Double  c   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						getReport().setSurfaceBadRate(c);
						getReport().setSurfaceBad("是");
					}
				}else{
					getReport().setSurfaceBadRate(0.0);
					getReport().setSurfaceBad("否");
				}
				//尺寸
				if(iiar.getSizeAmountRate()!=null&&iiar.getSizeUnAmount()!=null&&iiar.getSizeAmount()!=null){
					if(iiar.getSizeAmount().equals("0")&&iiar.getSizeUnAmount().equals("0")){
						getReport().setSizeBadRate(0.0);
						getReport().setSizeBad("否");
					}else{
						Double a=100-Double.valueOf(iiar.getSizeAmountRate());
						BigDecimal   b   =   new   BigDecimal(a);  
						Double  c   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						getReport().setSizeBadRate(c);
						getReport().setSizeBad("是");
					}
				}else{
					getReport().setSizeBadRate(0.0);
					getReport().setSizeBad("否");
				}
				//功能
				if(iiar.getFunctionAmountRate()!=null&&iiar.getFunctionAmount()!=null&&iiar.getFunctionUnAmount()!=null){
					if(iiar.getFunctionAmount().equals("0")&&iiar.getFunctionUnAmount().equals("0")){
						getReport().setFunctionBadRate(0.0);
						getReport().setFunctionBad("否");
					}else{
						Double a=100-Double.valueOf(iiar.getFunctionAmountRate());
						BigDecimal   b   =   new   BigDecimal(a);  
						Double  c   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						getReport().setFunctionBadRate(c);
						getReport().setFunctionBad("是");
					}
				}else{
					getReport().setFunctionBadRate(0.0);
					getReport().setFunctionBad("否");
				}
				//特性
				if(iiar.getQualifiedRate()!=null&&iiar.getQualifiedAmount()!=null&&iiar.getQualifiedAmount()!=null){
					if(iiar.getQualifiedAmount().equals("0")&&iiar.getQualifiedAmount().equals("0")){
						getReport().setFeaturesBadRate(0.0);
						getReport().setFeaturesBad("否");
					}else{
						Double a=100-Double.valueOf(iiar.getQualifiedRate());
						BigDecimal   b   =   new   BigDecimal(a);  
						Double  c   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						getReport().setFeaturesBadRate(c);
						getReport().setFeaturesBad("是");
					}
				}else{
					getReport().setFeaturesBadRate(0.0);
					getReport().setFeaturesBad("否");
				}
				String badDesc="";
				String wgBadDescrible=iiar.getWgBadDescrible();
				String gnBadDescrible=iiar.getGnBadDescrible();
				String ccBadDescrible=iiar.getCcBadDescrible();
				if(wgBadDescrible!=null&&!wgBadDescrible.equals("OK")){
					badDesc+="外观:{"+wgBadDescrible+"},";
				}
				
				if(ccBadDescrible!=null&&!ccBadDescrible.equals("OK")){
					badDesc+="尺寸:{"+ccBadDescrible+"},";
				}
				if(gnBadDescrible!=null&&!gnBadDescrible.equals("OK")){
					badDesc+="特性:{"+gnBadDescrible+"}";
				}
				getReport().setBadDesc(badDesc);
			}
		}
		if(isNewData!=null&&isNewData.equals("是")){
			getRight(taskId,supplierImproveManager.getWorkflowDefinitionCodeNew());
		}else{
			getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		}
		ActionContext.getContext().getValueStack().push(getReport());
}
	/**
	 * 完成任务
	 */
	@Override
	@Action("complete-task")
	public String completeTask() {
		@SuppressWarnings("unused")
		CompleteTaskTipType completeTaskTipType=null;
		String errorMessage = null;
		try{
			beforeCompleteCallback();
			if(StringUtils.isNotEmpty(report.getSqeApprovalerLog1())){
        	   report.setToApproval(0);
            }else{
            	report.setToApproval(1);
            }
			if(report.getSupplierCode()!=null&&("SQE确认".equals(report.getWorkflowInfo().getCurrentActivityName()))||"MQE确认异常".equals(report.getWorkflowInfo().getCurrentActivityName())){
	        	   createSupplierUser(report);
	           }
		    //回写结果到IQC报告
		    if(report.getInspectionId()!=null){
		    	IncomingInspectionActionsReport iiar=iiarManager.getIncomingInspectionActionsReport(report.getInspectionId());
		    	if(iiar!=null&&report.getSqeReplyTime()!=null){
		    		iiar.setSqeReplyTime(report.getSqeReplyTime());
		    	}
		    	if(iiar!=null&&report.getSqeFinishDate()!=null){
		    		iiar.setSqeCompleteTime(report.getSqeFinishDate());
		    	}
		    	if(iiar!=null&&(report.getReturnReportNo()!=null||report.getSqeMrbReportNo()!=null)){
		    		iiar.setProcessingResult(report.getSqeProcessOpinion());
					iiar.setExceptionNo(report.getFormNo());
					iiar.setExceptionId(report.getId().toString());
					iiar.setReturnReportNo(report.getReturnReportNo());
					iiar.setSqeMrbReportNo(report.getSqeMrbReportNo());
					if(iiar.getInspectionState().equals(IncomingInspectionActionsReport.INPECTION_STATE_LAST_SUBMIT)){
						iiar.setInspectionState(IncomingInspectionActionsReport.INPECTION_STATE_AUDIT);
					}				
					iiar.setLastStateText(report.getQualityOpinion());
					if(iiar.getLastStateTime()==null){
						iiar.setLastStateTime(new Date());
					}
		    	}
		    }		    
			//子表信息
			Map<String, List<JSONObject>> childMaps = getChildrenInfos();
			completeTaskTipType =  getAmbWorkflowBaseManager().completeTask(report,childMaps,taskId, taskTransact);
			addActionMessage("流程处理成功!");
			if(report.getWorkflowInfo().getCurrentActivityName()!=null&&report.getWorkflowInfo().getCurrentActivityName().indexOf("供应商")>-1){
				sendEmail(report);
			}
			afterCompleteCallback();
			//成功标志
			ActionContext.getContext().put("_successFlag",true);
		}catch(RuntimeException e){
			getLogger().error("流程处理失败！", e);
			addActionMessage("流程处理失败!");
			errorMessage = "处理失败," + e.getMessage();
			if(id != null){
				report = getAmbWorkflowBaseManager().getEntity(id);
			}else if(taskId != null){
				report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
				returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
			}
		} catch (Exception e) {
			getLogger().error("流程处理失败！设置特殊字段值出错", e);
			addActionMessage("流程处理失败!设置特殊字段值出错");
			errorMessage = "处理失败,设置特殊字段值出错," + e.getMessage();
			if(id != null){
				report = getAmbWorkflowBaseManager().getEntity(id);
			}else if(taskId != null){
				report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
				returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
			}
		}		
		try {
			if(StringUtils.isEmpty(errorMessage)){
				getAmbWorkflowBaseManager().updateDueDate(report);
			}
		} catch (Exception e) {
			getLogger().error("更新催办期限失败!",e);
		}
		// 控制页面上的字段都不能编辑
		initForm();
		if(isNewData!=null&&isNewData.equals("是")){
			getRight(taskId,supplierImproveManager.getWorkflowDefinitionCodeNew());
		}else{
			getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		}
		List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(report);
		if(opinionParameters!=null && !opinionParameters.equals("") && opinionParameters.size()!=0){
			Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
		}else{
			Struts2Utils.getRequest().setAttribute("opinionParameters", new ArrayList<Opinion>());
		}
		//自定义返回页面时返自定义的地址
		String customInputTypeFormUrl = Struts2Utils.getParameter("customInputTypeFormUrl");
		if(StringUtils.isNotEmpty(customInputTypeFormUrl)){
			return customInputTypeFormUrl;
		}
		if(CheckMobileUtil.isMobile(Struts2Utils.getRequest())){
			ActionContext.getContext().put("userTreeHtml",generateDepartmentUserTree());
			isComplete = true;
			return MOBILEINPUT;
		}else{
			String returnurl = "inputform".equals(Struts2Utils.getParameter("inputformortaskform"))?"input":"process-task";
			return returnurl;
		}
	}
	
 //物料类别加载
   public void selectMaterialType(){
		List<SupplierMaterialTypeGoal> types = supplierMaterialTypeGoalManager.getAllType();
		List<Option> options = new ArrayList<Option>();
		for(SupplierMaterialTypeGoal s : types){
			Option o = new Option();
			o.setName(s.getMaterialType());
			o.setValue(s.getMaterialType());
			options.add(o);
		}
		ActionContext.getContext().put("materialTypes",options);
	}
   /**
    * 创建返回消息
    * @param error
    * @param message
    * @return
    */
   public void createErrorMessage(String message){
       Map<String,Object> map = new HashMap<String, Object>();
       map.put("error",true);
       map.put("message",message);
       renderText(JSONObject.fromObject(map).toString());
   }
   /**
    * 创建返回消息
    * @param error
    * @param message
    * @return
    */
   public void createMessage(String message){
       Map<String,Object> map = new HashMap<String, Object>();
       map.put("error",false);
       map.put("message",message);
       renderText(JSONObject.fromObject(map).toString());
   }
	/**
	 * 新建页面
	 */
	@Override
	@Action("input")
	public String input() throws Exception {
		initForm();
		if(isNewData!=null&&isNewData.equals("是")){
			getRight(taskId,supplierImproveManager.getWorkflowDefinitionCodeNew());
		}else{
			getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		}
		if (report != null && report.getWorkflowInfo() != null) {
			List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(report);
			if(!opinionParameters.isEmpty() && opinionParameters.size() != 0) {
				Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
			} else {
				Struts2Utils.getRequest().setAttribute("opinionParameters",	new ArrayList<Opinion>());
			}
		}
		//判断是否手机端发起的请求
		if(CheckMobileUtil.isMobile(Struts2Utils.getRequest())){
			ActionContext.getContext().put("userTreeHtml",generateDepartmentUserTree());
			return MOBILEINPUT;
		}else{
			return SUCCESS;
		}
	} 
	/**
	 * 保存
	 */
	@Override
	@Action("save")
	public String save() throws Exception {
		//设置
		try {
			beforeSaveCallback();
			//子表信息
			Map<String, List<JSONObject>> childMaps = getChildrenInfos();
			getAmbWorkflowBaseManager().saveEntity(report,childMaps);
			if(taskId == null && report.getWorkflowInfo() != null){
				taskId = report.getWorkflowInfo().getFirstTaskId();
				task = getAmbWorkflowBaseManager().getWorkflowTask(taskId);
			}
			addActionMessage("保存成功!");
			//修改数据来源的数据状态 2016-08-29
		} catch (Exception e) {
			getLogger().error("保存"+getAmbWorkflowBaseManager().getWorkflowDefinitionName()+"失败!",e);
			addActionMessage("保存"+getAmbWorkflowBaseManager().getWorkflowDefinitionName()+"失败," + e.getMessage());
			if(id != null){
				report = getAmbWorkflowBaseManager().getEntity(id);
			}else if(taskId != null){
				report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
			}
		}
		initForm();
		if(isNewData!=null&&isNewData.equals("是")){
			getRight(taskId,supplierImproveManager.getWorkflowDefinitionCodeNew());
		}else{
			getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		}
		String returnurl = Struts2Utils.getParameter("inputformortaskform").equals("inputform")?"input":"process-task";
		if("process-task".equals(returnurl)){
			List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(report);
			if(opinionParameters!=null && !opinionParameters.equals("") && opinionParameters.size()!=0){
				Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
			}else{
				Struts2Utils.getRequest().setAttribute("opinionParameters", new ArrayList<Opinion>());
			}
		}
		//判断是否手机端发起的请求
		if(CheckMobileUtil.isMobile(Struts2Utils.getRequest())){
			ActionContext.getContext().put("userTreeHtml",generateDepartmentUserTree());
			return MOBILEINPUT;
		}else{
			return returnurl;
		}
	}
	/**
	 * 办理任务页面
	 * @return
	 */
	@Action("process-task")
	public String task() throws Exception {
		Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "办理任务");		
		initForm();
		if(isNewData!=null&&isNewData.equals("是")){
			getRight(taskId,supplierImproveManager.getWorkflowDefinitionCodeNew());
		}else{
			getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		}
		//办理前自动填写域设值
		if(taskId!=null){
			ApiFactory.getFormService().fillEntityByTask(report, taskId);
			returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
		}
		List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(report);
		if(!opinionParameters.isEmpty() && opinionParameters.size()!=0){
			Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
		}else{
			Struts2Utils.getRequest().setAttribute("opinionParameters", new ArrayList<Opinion>());
		}
       //判断是否手机端发起的请求
  		if (CheckMobileUtil.isMobile(Struts2Utils.getRequest())) {
  			ActionContext.getContext().put("userTreeHtml",
  					generateDepartmentUserTree());
  			return MOBILEINPUT;
  		} else {
  			return SUCCESS;
  		}
	}		
	
	/**
	 * 生成部门用户
	 * @return
	 */
	public StringBuffer generateDepartmentUserTree(){
		List<com.norteksoft.acs.entity.organization.Department> allDepartments = supplierDevelopManager.queryAllDepartments();
		List<User> allUsers = supplierDevelopManager.queryAllUsers();
		StringBuffer userHtml = new StringBuffer();
		List<com.norteksoft.acs.entity.organization.Department> parentDepts = queryChildrens(allDepartments,null);
		for(com.norteksoft.acs.entity.organization.Department dept : parentDepts){
			generateHtml(userHtml, dept, allDepartments, allUsers);
		}
		//无部门用户
//		generateHtml(userHtml,null, allDepartments, allUsers);
		return userHtml;
	}
	private List<com.norteksoft.acs.entity.organization.Department> queryChildrens(List<com.norteksoft.acs.entity.organization.Department> allDepartments,Long parentId){
		List<com.norteksoft.acs.entity.organization.Department> children = new ArrayList<com.norteksoft.acs.entity.organization.Department>();
		for(com.norteksoft.acs.entity.organization.Department d : allDepartments){
			if(parentId==null){
				if(d.getParent()==null){
					children.add(d);
				}
			}else{
				if(d.getParent()!=null&&d.getParent().getId().equals(parentId)){
					children.add(d);
				}
			}
		}
		return children;
	}
	private void generateHtml(StringBuffer html,com.norteksoft.acs.entity.organization.Department dept,
			List<com.norteksoft.acs.entity.organization.Department> allDepartments,
			List<User> allUsers){
		List<User> users = queryUsersByDeptId(allUsers,dept==null?null:dept.getId());
		List<com.norteksoft.acs.entity.organization.Department> children = queryChildrens(allDepartments,dept==null?null:dept.getId());
		if(users.isEmpty()&&children.isEmpty()){
			//html.append("<p>"+dept.getName()+"</p>");
		}else{
			html.append("<li style=\"margin-left:20px;\">");
			html.append("<label><a href=\"javascript:;\" onclick=\"showdiv('"+(dept==null?"noId":dept.getName())+"')\" >"+(dept==null?"无部门用户":dept.getName())+"</a></label>");
			html.append("<div style=\"display:none;\" id="+(dept==null?"noId":dept.getName())+"><ul class=\"two\" style=\"margin-left:30px;\">");
			for(User user : users){
				html.append("<li><label><input  type=\"checkbox\" name='"+user.getName()+"' deptName="+(dept==null?"noId":dept.getName())+"  value='"+user.getLoginName()+"'><a  href=\"javascript:;\" >"+user.getName()+"</a></label></li>");
				
			}
			for(com.norteksoft.acs.entity.organization.Department child : children){
				generateHtml(html,child,allDepartments,allUsers);
			}
			html.append("</ul></div>");
			html.append("</li>");
		}
	}
	private List<User> queryUsersByDeptId(List<User> allUsers,Long deptId){
		List<User> users = new ArrayList<User>();
		for(User u : allUsers){
			if(deptId==null){
				if(u.getMainDepartmentId()==null){
					users.add(u);
				}
			}else{
				if(u.getMainDepartmentId()!=null&&u.getMainDepartmentId().equals(deptId)){
					users.add(u);
				}
			}
		}
		return users;
	}
	 private void sendEmail(SupplierImprove report) {
	       try{
	    	   String [] b = report.getSupplierEmail().split(";");
	    	   for(int i=0;i<b.length;i++){
	    		   String email = b[i];
		    	   StringBuffer requPath = ServletActionContext.getRequest().getRequestURL();
		    	   String url = requPath.toString().substring(0, requPath.toString().lastIndexOf("/"))+"/input.htm?id=" +report.getId();
				   if(StringUtils.isNotEmpty(email)){
						AsyncMailUtils.sendMail(email,"欧菲光邮件:进料异常纠正措施单","物料【"+report.getBomCode()+"】检验异常,请登录系统查看。检验单号:【"+report.getFormNo()+"】;异常描述:【"+report.getBadDesc()+"】。您的QIS账号为:"+report.getSupplierCode()+"初始密码与账号一致。登录网址："+url);
				   }
	    	   }
	       }catch(Exception e){
	           log.error("自动发送邮件!",e);
	       }
	   }	
	 private void createSupplierUser(SupplierImprove report) {
		   //查出供应商部门
		   Department dept = supplierImproveManager.searchSupplierDept();
		   //检查是否存在供应商，不存在就插入
		   supplierImproveManager.saveUser(report,dept);
			
	   }
   /**
    * 启动并提交流程
    */
   @Override
   @Action("submit-process")
   public String submitProcess() {
       boolean hasError = false;
       try{
           beforeSubmitCallback();
           if(report.getFormNo()==null){
           	  report.setFormNo(formCodeGenerated.getSupplierImproveCode());
			}
           if(StringUtils.isNotEmpty(report.getSqeApprovalerLog1())){
        	   report.setToApproval(0);
            }else{
            	report.setToApproval(1);
            }
//           if(report.getSupplierCode()!=null){
//        	   createSupplierUser(report);
//           }
           //子表信息
           Map<String, List<JSONObject>> childMaps = getChildrenInfos();
           CompleteTaskTipType completeTaskTipType =  getAmbWorkflowBaseManager().submitProcess(report,childMaps);
           submitResult = getAmbWorkflowBaseManager().getCompleteTaskTipType(completeTaskTipType,report);
           Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "启动并提交流程成功!");
           addActionMessage("提交成功!");
           id = report.getId();
       }catch(RuntimeException e){
           hasError = true;
           Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "启动并提交流程失败!");
           addActionMessage("提交失败!");
           getLogger().error("启动并提交流程失败!", e);
           if(id != null){
               report = getAmbWorkflowBaseManager().getEntity(id);
           }else if(taskId != null){
               report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
           }
       } catch (Exception e) {
           hasError = true;
           Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "启动并提交流程失败!设置特殊字段值出错");
           addActionMessage("提交失败!设置特殊字段值出错");
           getLogger().error("启动并提交流程失败!设置特殊字段值出错", e);
           if(id != null){
               report = getAmbWorkflowBaseManager().getEntity(id);
           }else if(taskId != null){
               report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
           }
       }
		//回写异常单号到IQC报告
		if(report.getInspectionId()!=null){
			IncomingInspectionActionsReport iiar=iiarManager.getIncomingInspectionActionsReport(report.getInspectionId());
			if(iiar!=null){
				iiar.setExceptionNo(report.getFormNo());
				iiar.setExceptionId(report.getId().toString());				
			}
		}
       if(!hasError){
           task = getAmbWorkflowBaseManager().getActiveTaskByTransactorId(report,ContextUtils.getUserId());
           if(task!=null)taskId = task.getId();
           if(task==null&&report.getWorkflowInfo()!=null){
               taskId = report.getWorkflowInfo().getFirstTaskId();
               task = getAmbWorkflowBaseManager().getWorkflowTask(taskId);
               //ApiFactory.getFormService().fillEntityByTask(qrqcReport, taskId);
           }

           try {
               getAmbWorkflowBaseManager().updateDueDate(report);
           } catch (Exception e) {
        	   getLogger().error("更新催办期限失败!",e);
           }
       }
       try{
    	   String [] b = report.getSupplierEmail().split(";");
    	   for(int i=0;i<b.length;i++){
    		   String email = b[i];
	    	   StringBuffer requPath = ServletActionContext.getRequest().getRequestURL();
	    	   String url = requPath.toString().substring(0, requPath.toString().lastIndexOf("/"))+"/input.htm?id=" +report.getId();
			   if(StringUtils.isNotEmpty(email)){
					AsyncMailUtils.sendMail(email,"进料异常纠正措施单",report.getBomCode()+"检验异常,请登录系统查看。您的QIS账号为:"+report.getSupplierCode()+"初始密码同账号"+url);
			   }
    	   }
//           ApiFactory.getPortalService().addMessage("supplier", "systemAdmin", "ofilm.systemAdmin",ContextUtils.getLoginName(), "进料异常纠正措施单", report.getSupplierName()+"检验单需要审核", "/improve/input.htm?id="+report.getId());
       }catch(Exception e){
           log.error("自动抄送失败!",e);
       }
       // 控制页面上的字段都不能编辑
       initForm();
       if(isNewData!=null&&isNewData.equals("是")){
			getRight(taskId,supplierImproveManager.getWorkflowDefinitionCodeNew());
		}else{
			getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		}
       //判断是否手机端发起的请求
  		if (CheckMobileUtil.isMobile(Struts2Utils.getRequest())) {
  			ActionContext.getContext().put("userTreeHtml",
  					generateDepartmentUserTree());
  			return MOBILEINPUT;
  		} else {
  			return "input";
  		}
   }
   
   

   @Action("return-to-task")
   public String returnToTask() throws Exception {
       JSONObject result = new JSONObject();
       try {
           String returnTaskName = Struts2Utils.getParameter("returnTaskName");
           String opinion = Struts2Utils.getParameter("opinion");
           //记录操作日志
           WorkflowTask task = ApiFactory.getTaskService().getTask(taskId);
           //保存记录
           Opinion opinionParameter = new Opinion();
           opinionParameter.setCustomField("驳回");
           opinionParameter.setOpinion(opinion);
           opinionParameter.setTransactor(ContextUtils.getLoginName());
           opinionParameter.setTransactorName(ContextUtils.getUserName());
           opinionParameter.setTaskName(task.getName());
           opinionParameter.setTaskId(taskId);
           opinionParameter.setAddOpinionDate(new Date());
           ApiFactory.getOpinionService().saveOpinion(opinionParameter);
           //驳回操作
           ApiFactory.getTaskService().returnTaskTo(taskId,returnTaskName);
       } catch (Exception e) {
           //e.printStackTrace();
           result.put("error","驳回任务出错!请联系系统管理员!");
           logger.error("驳回任务出错!",e);
       }
       renderText(result.toString());
       return null;
   }
   @Action("monitor-list")
   public String listMonitor() throws Exception {
       // TODO Auto-generated method stub
       return SUCCESS;
   }
   @Action("monitor-list-datas")
   public String listMonitorDatas() throws Exception {
       try{
           page = getAmbWorkflowBaseManager().search(page);
           renderText(PageUtils.pageToJson(page));
       }catch(Exception e){
           getLogger().error("流程跟踪查询失败!",e);
       }
       return null;
   }
   
	/**
	 * 列表页面
	 */
	@Override
	@Action("list")
	public String list() throws Exception {
		selectType = Struts2Utils.getParameter("selectType");
		
		List<Option> selectTypes = ApiFactory.getSettingService().getOptionsByGroupCode("iqc-select-type");
		if(StringUtils.isEmpty(selectType)){
			if(selectTypes.size()>0){
				selectType = selectTypes.get(0).getValue();
			}
		}
		String department=CommonUtil.getMainDepartMent();
		if(department!=null&&"供应商".equals(department)){
			selectType="single";
		}
		ActionContext.getContext().put("systemCodes", ApiFactory.getSettingService().getOptionsByGroupCode("supplier-system-code"));
		ActionContext.getContext().put("selectTypes",selectTypes);
		return SUCCESS;
	}
	
	/**
	 * 列表数据
	 */
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try{
			selectType = Struts2Utils.getParameter("selectType");
		
			String department=CommonUtil.getMainDepartMent();
			if(department!=null&&"供应商".equals(department)){
				selectType="single";
				page = supplierImproveManager.searchSupplierSingle(page);
			}else{
				if(selectType!=null&&selectType.equals("single")){
					page = supplierImproveManager.searchSingle(page);	
				}else{
					page = getAmbWorkflowBaseManager().search(page);
				}
			}						
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			getLogger().error("查询失败!",e);
		}
		return null;
	}
	
	
	/**
	 * 列表页面
	 */
	@Action("oklist")
	public String oKlist() throws Exception {
		selectType = Struts2Utils.getParameter("selectType");
		System.out.println("list-"+selectType);
		List<Option> selectTypes = ApiFactory.getSettingService().getOptionsByGroupCode("iqc-select-type");
		if(StringUtils.isEmpty(selectType)){
			if(selectTypes.size()>0){
				selectType = selectTypes.get(0).getValue();
			}
		}
		String department=CommonUtil.getMainDepartMent();
		if(department!=null&&"供应商".equals(department)){
			selectType="single";
		}
		ActionContext.getContext().put("systemCodes", ApiFactory.getSettingService().getOptionsByGroupCode("supplier-system-code"));
		ActionContext.getContext().put("selectTypes",selectTypes);
		return SUCCESS;
	}
	/**
	 * 列表数据
	 */
	@Action("list-okdatas")
	public String getListOkDatas() throws Exception {
		try{
			selectType = Struts2Utils.getParameter("selectType");
			System.out.println("list-datas  "+selectType);
			String department=CommonUtil.getMainDepartMent();
			if(department!=null&&"供应商".equals(department)){
				selectType="single";
				page = supplierImproveManager.searchOkSupplierSingle(page);
			}else{
				if(selectType!=null&&selectType.equals("single")){
					page = supplierImproveManager.searchOkSingle(page);	
				}else{
					page=supplierImproveManager.oklist(page);
				}
			}						
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			getLogger().error("查询失败!",e);
		}
		return null;
	}
	
	
	
	
	@Override
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		
		try {
			String str=supplierImproveManager.deleteEntity(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据:单号:"+str);
		} catch (Exception e) {
			
			renderText("删除失败:" + e.getMessage());
			log.error("删除数据信息失败",e);
		}
		return null;
	}
	
	/**
	 * 导出台账
	 * @return
	 * @throws Exception
	 */
	@Action("export")
	@LogInfo(optType="导出",message="导出数据")
	public String export() throws Exception {
		Page<SupplierImprove> page = new Page<SupplierImprove>(65535);
		String department=CommonUtil.getMainDepartMent();
		if(department!=null&&"供应商".equals(department)){
			selectType="single";
			page = supplierImproveManager.searchSupplierSingle(page);
		}else{
			if(selectType!=null&&selectType.equals("single")){
				page = supplierImproveManager.searchSingle(page);	
			}else{
				page = supplierImproveManager.search(page);
			}
		}
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,getAmbWorkflowBaseManager().getEntityListCode()),getAmbWorkflowBaseManager().getWorkflowDefinitionName()));
		return null;
	}
	
	/**
	 * 导出已完成台账
	 * @return
	 * @throws Exception
	 */
	@Action("okexport")
	@LogInfo(optType="导出",message="导出数据")
	public String okexport() throws Exception {
		Page<SupplierImprove> page = new Page<SupplierImprove>(65535);
		String department=CommonUtil.getMainDepartMent();
		if(department!=null&&"供应商".equals(department)){
			selectType="single";
			page = supplierImproveManager.searchOkSupplierSingle(page);
		}else{
			if(selectType!=null&&selectType.equals("single")){
				page = supplierImproveManager.searchOkSingle(page);	
			}else{
				page = supplierImproveManager.oKsearch(page);
			}
		}
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,getAmbWorkflowBaseManager().getEntityListCode()),getAmbWorkflowBaseManager().getWorkflowDefinitionName()));
		return null;
	}
	
	
	
	
	
	/**
	 * 指派
	 * @return
	 */
	@Action("assign")
	@LogInfo(optType="指派",message="指派任务")
	public String assign(){
		try {
			getAmbWorkflowBaseManager().assign(taskId, assignee);
			System.out.println("assignee:"+assignee);
			renderText("指派完成");
		} catch (Exception e) {
			renderText("指派失败!");
			getLogger().error("指派办理人员失败!",e);
		}
		return null;
	}
	
	/**
	 * 导出表单
	 * */
	@Action("export-report")
	public String exportReport() {
		try{
			supplierImproveManager.exportReport(id);
		}catch(Exception e){
			createMessage("导出表单失败:" + e.getMessage());
		}
		return null;
	}
}

package com.ambition.supplier.approval.web;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.gp.gpmaterial.services.GpMaterialManager;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.approval.service.SupplierApprovalManager;
import com.ambition.supplier.entity.SupplierApproval;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CheckMobileUtil;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.opensymphony.xwork2.ActionContext;

import edu.emory.mathcs.backport.java.util.Arrays;
/**
 * 
 * 类名:材料承认Action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：一期事业群(CCM,SQ,FPM)材料承认</p>
 * @author  Janam
 * @version 1.00 2017年9月11日 发布
 */
@Namespace("/supplier/material-admit/approval")
@ParentPackage("default")
@Results({@Result(name=CrudActionSupport.RELOAD, location = "/supplier/material-admit/approval", type = "redirectAction")})
public class SupplierApprovalAction extends AmbWorkflowActionBase<SupplierApproval>{

	private static final long serialVersionUID = 1L;
	public static final String MOBILEINPUT = "mobile-input";
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Autowired
	private AcsUtils acsUtils;
	private String ids;
	private SupplierApproval supplierApproval;
	private String currentActivityName;
	@Autowired
	private SupplierApprovalManager supplierApprovalManager;
	@Autowired
	private GpMaterialManager gpMaterialManager;
	private Logger log=Logger.getLogger(this.getClass());
	
	public String getIds() {
		return ids;
	}


	public void setIds(String ids) {
		this.ids = ids;
	}

	public SupplierApproval getSupplierApproval() {
		return supplierApproval;
	}


	public void setSupplierApproval(SupplierApproval supplierApproval) {
		this.supplierApproval = supplierApproval;
	}


	public String getCurrentActivityName() {
		return currentActivityName;
	}


	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}

	@Override
	protected AmbWorkflowManagerBase<SupplierApproval> getAmbWorkflowBaseManager() {
		return supplierApprovalManager;
	}
	/**
	  * 方法名: 初始化的参数在这里写
	  * <p>功能说明：</p>
	 */
	public void initForm(){
		String productFileName = null;
		String surveyFileNmae = null;
		if (getId() == null) {
			report.setFormNo(formCodeGenerated.generateApprovalNo());
			report.setCreatedTime(new Date());
			report.setCreator(ContextUtils.getLoginName());
			report.setCreatorName(ContextUtils.getUserName());
			report.setBusinessUnitName(ContextUtils.getCompanyName());
			report.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
			report.setApplicat(ContextUtils.getUserName());
			report.setApplicatLog(ContextUtils.getLoginName());
			report.setApplyDate(new Date());
			report.setDocControl("DCC");
			report.setDocControlLoging("DCC");
			productFileName = materials("产品成分宣告");
			surveyFileNmae = materials("调查表");
			if(productFileName!=null){
				report.setProductFileName(productFileName);
			}
			if(surveyFileNmae!=null){
				report.setSurveyFileNmae(surveyFileNmae);
			}
			report.setConsignorDept(acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName());
		} else {
			if (getReport().getWorkflowInfo() != null) {
				currentActivityName = getReport().getWorkflowInfo().getCurrentActivityName();
				report.setConsignorDept(acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName());
			}
		}
		ActionContext.getContext().put("admitProjects",ApiFactory.getSettingService().getOptionsByGroupCode("supplier_admit_status"));
		ActionContext.getContext().put("countersigns",ApiFactory.getSettingService().getOptionsByGroupCode("supplier_admit_countersigns"));
	}
	/**
	 * 办理任务页面
	 * @return
	 */
	@Action("process-task")
	@LogInfo(optType="办理",message="办理任务")
	public String task() throws Exception {
		Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "办理任务");
		task=getAmbWorkflowBaseManager().getWorkflowTask(taskId);
		if(task==null){
			HttpServletResponse response= ServletActionContext.getResponse();
			response.setContentType("text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			PrintWriter out = response.getWriter();
			out.println("<script language='javascript'>");
			out.println("alert('该任务已经失效!')");
			out.println("window.location.href='"+PropUtils.getProp("taskAdress")+"';");
			out.println("</script>");
			out.flush();
			out.close();
		}
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		initForm();
		//办理前自动填写域设值
		if(taskId!=null){
			ApiFactory.getFormService().fillEntityByTask(report, taskId);
			returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
		}else if(id!=null){
	    	report = getAmbWorkflowBaseManager().getEntity(id);
	    	task = getAmbWorkflowBaseManager().getActiveTaskByTransactorId(report, ContextUtils.getUserId());
			if(task==null&&report.getWorkflowInfo()!=null){
				task = getAmbWorkflowBaseManager().getWorkflowTask(report.getWorkflowInfo().getFirstTaskId());
			}
			if(task!=null){
				taskId = task.getId();
			}if("CGBL".equals(task.getCode())){
				 if(report.getSupplierLoginName()!=null){
		        	   createSupplierUser(report);
		           }
			}
		}
		List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(report);
		if(!opinionParameters.isEmpty() && opinionParameters.size()!=0){
			Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
		}else{
			Struts2Utils.getRequest().setAttribute("opinionParameters", new ArrayList<Opinion>());
		}
  		return SUCCESS;
	}
	/**
	 * 完成任务
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Action("complete-task")
	@LogInfo(optType="同意或者提交",message="完成任务")
	public String completeTask() {
		@SuppressWarnings("unused")
		CompleteTaskTipType completeTaskTipType=null;
		String errorMessage = null;
		try{
			beforeCompleteCallback();
			//子表信息
			if(id!=null){
				report = getAmbWorkflowBaseManager().getEntity(id);
				task = getAmbWorkflowBaseManager().getActiveTaskByTransactorId(report, ContextUtils.getUserId());
				if(task==null&&report.getWorkflowInfo()!=null){
					task = getAmbWorkflowBaseManager().getWorkflowTask(report.getWorkflowInfo().getFirstTaskId());
				}
				if(task!=null){
					taskId = task.getId();
				}
				if("CGBL".equals(task.getCode())){
					if(report.getSupplierLoginName()!=null){
						createSupplierUser(report);
					}
				}
				if("WK".equals(task.getCode())&&report.getCopyManLogin()!=null){
					List<String> loginNames=new ArrayList<String>();
					loginNames=Arrays.asList(report.getCopyManLogin().split(","));
					getAmbWorkflowBaseManager().createCopyTasks(taskId, loginNames, null, null);
				}
			}
			Map<String, List<JSONObject>> childMaps = getChildrenInfos();
			completeTaskTipType =  getAmbWorkflowBaseManager().completeTask(report,childMaps,taskId, taskTransact);
			addActionMessage("流程处理成功!");
			
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
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		initForm();
		
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
	
	private void createSupplierUser(SupplierApproval report) {
		   //查出供应商部门
		   Department dept = supplierApprovalManager.searchSupplierDept();
		   //检查是否存在供应商，不存在就插入
		   supplierApprovalManager.saveUser(report,dept);
			
	  }
	public void afterCompleteCallback() throws Exception{
		String id =Struts2Utils.getParameter("id");
		String formNo = Struts2Utils.getParameter("formNo");
		String qsChecker =Struts2Utils.getParameter("qsChecker");
		String gpMaterialNo = Struts2Utils.getParameter("gpMaterialNo");
		String supplierEmail =Struts2Utils.getParameter("supplierEmail");
		String url =Struts2Utils.getParameter("url");
		String name=ApiFactory.getTaskService().getTask(taskId).getNextTasks();
		if("供应商填写资料".equals(name)){
			gpMaterialManager.releaseEmail(qsChecker,gpMaterialNo);
			supplierApprovalManager.releaseEmail(supplierEmail,formNo,url);
		}
	}
		/**
		 * 列表数据
		 */
		@Action("list-datas")
		@LogInfo(optType="查询",message="查询数据")
		public String getListDatas() throws Exception {
			try{
				String dept =acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName();
//				String userName = ContextUtils.getCompanyName();
				String code = ContextUtils.getLoginName();
				if("供应商".equals(dept)){
					page = supplierApprovalManager.listState(page,code);
					renderText(PageUtils.pageToJson(page));
				}else{
					page = supplierApprovalManager.listState(page,null);
					renderText(PageUtils.pageToJson(page));
				}
	//			logUtilDao.debugLog("查询","查询数据",5240l);
			}catch(Exception e){
				getLogger().error("查询失败!",e);
			}
			return null;
		}
		/**
		 * 获取材料承认附件
		 */
		public String materials(String name){
			return supplierApprovalManager.getMaterialsFile(name);
		}
		/**
		 * 导出台账
		 * @return
		 * @throws Exception
		 */
		@Action("export")
		@LogInfo(optType="导出",message="导出数据")
		public String export() throws Exception {
			String code = ContextUtils.getLoginName();
			String dept =acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName();
			Page<SupplierApproval> page = new Page<SupplierApproval>(100000);
			if("供应商".equals(dept)){
				page = supplierApprovalManager.listState(page,code);
			}else{
				page = supplierApprovalManager.listState(page,null);
			}
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,getAmbWorkflowBaseManager().getEntityListCode()),getAmbWorkflowBaseManager().getWorkflowDefinitionName()));
			return null;
		}
		@Override
		@Action("delete")
		@LogInfo(optType="删除")
		public String delete() throws Exception {
			// TODO Auto-generated method stub
			try {
				String str=supplierApprovalManager.deleteEntity(deleteIds);
				Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据:单号:"+str);
			} catch (Exception e) {
				// TODO: handle exception
				renderText("删除失败:" + e.getMessage());
				log.error("删除数据信息失败",e);
			}
			return null;
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
}

package com.ambition.supplier.supervision.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.carmfg.bom.service.ProductBomManager;
import com.ambition.product.BaseAction;
import com.ambition.supplier.entity.CheckPlan;
import com.ambition.supplier.entity.CheckReport;
import com.ambition.supplier.supervision.service.CheckPlanManager;
import com.ambition.supplier.supervision.service.CheckReportManager;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 监察报告
 * @author 赵骏
 *
 */
@Namespace("/supplier/supervision/check-report")
@ParentPackage("default")
public class CheckReportAction  extends BaseAction<CheckReport> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;//删除的编号 
	
	private CheckReport checkReport;//监察报告
	
	private Page<CheckReport> page;
	
 	private JSONObject params;
 	
 	@Autowired
 	private CheckReportManager checkReportManager;
 	
 	@Autowired
 	private CheckPlanManager checkPlanManager;

 	@Autowired
 	private FormCodeGenerated formCodeGenerated;
 	
 	@Autowired
 	private ProductBomManager productBomManager;
 	
 	@Autowired
	private LogUtilDao logUtilDao;
 	
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

	

	public CheckReport getCheckReport() {
		return checkReport;
	}

	public void setCheckReport(CheckReport checkReport) {
		this.checkReport = checkReport;
	}

	public Page<CheckReport> getPage() {
		return page;
	}

	public void setPage(Page<CheckReport> page) {
		this.page = page;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public CheckReport getModel() {
		return checkReport;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			checkReport = new CheckReport();
			checkReport.setCreatedTime(new Date());
			checkReport.setCompanyId(ContextUtils.getCompanyId());
			checkReport.setCreator(ContextUtils.getUserName());
			checkReport.setLastModifiedTime(new Date());
			checkReport.setLastModifier(ContextUtils.getUserName());
			checkReport.setBusinessUnitName(ContextUtils.getSubCompanyName());
			checkReport.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			checkReport = checkReportManager.getCheckReport(id);
		}
	}
	
	@Action("save")
	@Override
	public String save() throws Exception {
		try{
			String items = Struts2Utils.getParameter("items");
			JSONArray jsonArray = JSONArray.fromObject(items);
			checkReportManager.storeCheckReport(checkReport,params,jsonArray);
			this.renderText("{\"id\":"+checkReport.getId()+"}");
		}catch(Exception e){
			e.printStackTrace();
			createErrorMessage("保存失败：" + e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				checkReportManager.deleteCheckReport(deleteIds);
			} catch (Exception e) {
				createErrorMessage("删除失败:" + e.getMessage());
			}
		}
		return null;
	}

	@Action("input")
	public String input() throws Exception {
		Long checkPlanId=null;
		if(Struts2Utils.getParameter("checkPlanId")!=null){
				checkPlanId=Long.parseLong(Struts2Utils.getParameter("checkPlanId"));
		}
		Date planDate=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(checkPlanId!=null){
			CheckPlan checkPlan=checkPlanManager.getCheckPlan(checkPlanId);
			ActionContext.getContext().put("checkPlanId",checkPlanId);
			ActionContext.getContext().put("supplierName",checkPlan.getSupplierName());
			ActionContext.getContext().put("supplierId",checkPlan.getSupplierId());
			ActionContext.getContext().put("supplierCode",checkPlan.getSupplierCode());
			ActionContext.getContext().put("planCode",checkPlan.getPlanCode());
			planDate=checkPlan.getPlanDate();
			ActionContext.getContext().put("checkBomName",checkPlan.getCheckBomName());
			ActionContext.getContext().put("checkBomCode",checkPlan.getCheckBomCode());
			String auditReason="";
			if(checkPlan.getCheckReport()!=null){
			ActionContext.getContext().put("modelSpecification",checkPlan.getCheckReport().getModelSpecification());
			ActionContext.getContext().put("id",checkPlan.getCheckReport().getId());
			if(checkPlan.getCheckReport().getId()!=null){
				checkReport=checkReportManager.getCheckReport(checkPlan.getCheckReport().getId());
			}
			//审核理由
			 auditReason = StringUtils.isEmpty(checkPlan.getCheckReport().getAuditReason())?"":checkPlan.getCheckReport().getAuditReason();
			}
			List<Option> options = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_check_grade_audit_reason");
			List<Object> auditReasons = new ArrayList<Object>();
			for(Option option : options){
				Map<String,Object> obj = new HashMap<String, Object>();
				obj.put("name",option.getName());
				obj.put("value",option.getValue());
				obj.put("checked",auditReason.indexOf(option.getName())>-1?1:0);
				auditReasons.add(obj);
			
			ActionContext.getContext().put("auditReasons",auditReasons);
			}
		}
		if(checkReport.getId() != null&&checkPlanId==null&&checkReport.getCheckPlan()!=null){
			ActionContext.getContext().put("checkPlanId",checkReport.getCheckPlan().getId());
		}
		if(planDate!=null){
			ActionContext.getContext().put("checkDateString",sdf.format(planDate));
		}
		if(checkReport.getId() == null){
//			checkReport.setCode(formCodeGenerated.generateCheckReportCode());
		}
		if(checkPlanId==null){
		//审核理由
		List<Option> options = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_check_grade_audit_reason");
		List<Object> auditReasons = new ArrayList<Object>();
		String auditReason = StringUtils.isEmpty(checkReport.getAuditReason())?"":checkReport.getAuditReason();
		for(Option option : options){
			Map<String,Object> obj = new HashMap<String, Object>();
			obj.put("name",option.getName());
			obj.put("value",option.getValue());
			obj.put("checked",auditReason.indexOf(option.getName())>-1?1:0);
			auditReasons.add(obj);
		}
		ActionContext.getContext().put("auditReasons",auditReasons);
		}
		//机型
		ActionContext.getContext().put("modelSpecifications",productBomManager.getModelSpecificationToOptions());
		
		//稽查评分
		ActionContext.getContext().put("reportGradeStr",checkReportManager.createCheckGradeTable(checkReport));
		return SUCCESS;
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getCheckReportDatas() throws Exception {
		try {
			page = checkReportManager.list(page);
			this.renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logUtilDao.debugLog("查询", "供应商质量管理：供应商监察报告");
		return null;
	}
	
	/**
	 * 查看监察报告
	 * @return
	 * @throws Exception
	 */
	@Action("view-info")
	public String viewInfo() throws Exception {
		if(id==null){
			checkReport = new CheckReport();
			checkReport.setCreatedTime(new Date());
			checkReport.setCompanyId(ContextUtils.getCompanyId());
			checkReport.setCreator(ContextUtils.getUserName());
			checkReport.setLastModifiedTime(new Date());
			checkReport.setLastModifier(ContextUtils.getUserName());
		}else {
			checkReport = checkReportManager.getCheckReport(id);
		}
		//审核理由
		List<Option> options = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_check_grade_audit_reason");
		List<Object> auditReasons = new ArrayList<Object>();
		String auditReason = StringUtils.isEmpty(checkReport.getAuditReason())?"":checkReport.getAuditReason();
		for(Option option : options){
			Map<String,Object> obj = new HashMap<String, Object>();
			obj.put("name",option.getName());
			obj.put("value",option.getValue());
			obj.put("checked",auditReason.indexOf(option.getName())>-1?1:0);
			auditReasons.add(obj);
		}
		ActionContext.getContext().put("auditReasons",auditReasons);

		ActionContext.getContext().put("checkReport",checkReport);
		
		//机型
		ActionContext.getContext().put("modelSpecifications",productBomManager.getModelSpecificationToOptions());
		
		//稽查评分
		ActionContext.getContext().put("reportGradeStr",checkReportManager.createCheckReportTable(checkReport));
		return SUCCESS;
	}
	
	/**
	 * 获取最新的监察报告的编号
	 * @return
	 * @throws Exception
	 */
	@Action("generate-check-report-code")
	public String getCheckReportCode() throws Exception {
//		renderText(formCodeGenerated.generateCheckReportCode());
		return null;
	}
}

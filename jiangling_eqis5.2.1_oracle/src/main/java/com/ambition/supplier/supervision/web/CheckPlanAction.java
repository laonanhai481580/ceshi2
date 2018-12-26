package com.ambition.supplier.supervision.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.product.BaseAction;
import com.ambition.supplier.entity.CheckPlan;
import com.ambition.supplier.supervision.service.CheckPlanManager;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.mms.base.DynamicColumnValues;
import com.norteksoft.mms.base.utils.view.DynamicColumnDefinition;
import com.norteksoft.mms.form.enumeration.DataType;
import com.norteksoft.mms.form.enumeration.EditControlType;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;

/**
 * 稽查计划
 * @author 赵骏
 *
 */
@Namespace("/supplier/supervision/check-plan")
@ParentPackage("default")
@Results({ @Result(name = CheckPlanAction.RELOAD, location = "/supplier/supervision/check-plan", type = "redirectAction") })
public class CheckPlanAction  extends BaseAction<CheckPlan> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;//删除的编号 
	
	private CheckPlan checkPlan;//稽查计划
	
	private Page<CheckPlan> page;
	
 	private JSONObject params;
 	
	private List<DynamicColumnDefinition> dynamicColumn=new ArrayList<DynamicColumnDefinition>();
 	
 	@Autowired
 	private CheckPlanManager checkPlanManager;
 	
 	@Autowired
	private LogUtilDao logUtilDao;
 	
 	@Autowired
 	private FormCodeGenerated formCodeGenerated;

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

	public CheckPlan getCheckPlan() {
		return checkPlan;
	}

	public void setCheckPlan(CheckPlan checkPlan) {
		this.checkPlan = checkPlan;
	}

	public Page<CheckPlan> getPage() {
		return page;
	}

	public void setPage(Page<CheckPlan> page) {
		this.page = page;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public CheckPlan getModel() {
		return checkPlan;
	}

	public List<DynamicColumnDefinition> getDynamicColumn() {
		return dynamicColumn;
	}

	public void setDynamicColumn(List<DynamicColumnDefinition> dynamicColumn) {
		this.dynamicColumn = dynamicColumn;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			checkPlan = new CheckPlan();
			checkPlan.setCreatedTime(new Date());
			checkPlan.setCompanyId(ContextUtils.getCompanyId());
			checkPlan.setCreator(ContextUtils.getUserName());
			checkPlan.setLastModifiedTime(new Date());
			checkPlan.setLastModifier(ContextUtils.getUserName());
			checkPlan.setBusinessUnitName(ContextUtils.getSubCompanyName());
			checkPlan.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			checkPlan = checkPlanManager.getCheckPlan(id);
		}
	}
	
	@Action("save")
	@Override
	public String save() throws Exception {
		try{
			checkPlanManager.storeCheckPlan(checkPlan); 
			JSONObject json = JSONObject.fromObject(JsonParser.getRowValue(checkPlan));
			Long id=checkPlan.getId();
			json.put("operate1", "<div style='text-align:center;'><button title='制作监察表' type='button' class='small-button-bg' onclick='showInfo("+id+")'><span class='ui-icon ui-icon-info' style='cursor:pointer;'></span></button></div>");
			json.put("id",id);
			if(checkPlan.getCheckReport()!=null){
			json.put("checkReport.code",checkPlan.getCheckReport().getCode());
			}
			json.put("checkBomName",checkPlan.getCheckBomName());
			this.renderText(json.toString());
		}catch(Exception e){
			createErrorMessage("保存失败：" + e.getMessage());
		}
		return null;
	}
	Logger log = Logger.getLogger(this.getClass());
	@Action("delete")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				checkPlanManager.deleteCheckPlan(deleteIds);
			} catch (Exception e) {
				log.error("删除监察计划失败!",e);
				createErrorMessage("删除失败:" + e.getMessage());
			}
		}
		return null;
	}

	@Action("input")
	public String input() throws Exception {
		createErrorMessage("操作失败!");
		return null;
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		DynamicColumnDefinition dynamicFieldOptions =new DynamicColumnDefinition();
		dynamicFieldOptions.setColName("制作监察表 ");
		dynamicFieldOptions.setName("operate");
		dynamicFieldOptions.setVisible(true);
		dynamicFieldOptions.setEditable(false);
		dynamicFieldOptions.setEdittype(EditControlType.TEXT);
		dynamicFieldOptions.setType(DataType.TEXT);
		dynamicColumn.add(dynamicFieldOptions);
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getCheckPlanDatas() throws Exception {
		try {
			page = checkPlanManager.list(page);
			this.renderText(PageUtils.dynamicPageToJson(page,new DynamicColumnValues(){
				public void addValuesTo(List<Map<String, Object>> result) {
					for(Map<String, Object> map:result){
						Long id=Long.valueOf(map.get("id").toString());
						map.put("operate", "<div style='text-align:center;'><a title='制作监察表' type='button' class='small-button-bg' onclick='showInfo("+id+")'><span class='ui-icon ui-icon-info' style='cursor:pointer;'></span></a></div>");
					}
				}
			}));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logUtilDao.debugLog("查询", "供应商质量管理：供应商监察-监察计划管理台账");
		return null;
	}
	
	/**
	 * 获取最新的计划的编号
	 * @return
	 * @throws Exception
	 */
	@Action("generate-check-plan-code")
	public String getCheckReportCode() throws Exception {
//		renderText(formCodeGenerated.generateCheckPlanCode());
		return null;
	}
	
	/**
	 * 选择计划
	 * @return
	 * @throws Exception
	 */
	@Action("select-check-plan")
	public String selectCheckPlan() throws Exception {
		DynamicColumnDefinition dynamicFieldOptions =new DynamicColumnDefinition();
		dynamicFieldOptions.setColName("制作稽查表 ");
		dynamicFieldOptions.setName("operate");
		dynamicFieldOptions.setEditable(false);
		dynamicFieldOptions.setEdittype(EditControlType.TEXT);
		dynamicFieldOptions.setType(DataType.TEXT);
		dynamicColumn.add(dynamicFieldOptions);
		return SUCCESS;
	}

	/**
	 * 选择计划
	 * @return
	 * @throws Exception
	 */
	@Action("import-check-plan")
	public String importCheckPlan() throws Exception {
		checkPlanManager.importCheckPlan(deleteIds);
		return "list";
	}
	
	@Action("exports")
	public String exports() throws Exception {
		Page<CheckPlan> page = new Page<CheckPlan>(100000);
		page = checkPlanManager.list(page);
	this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page, "SUPPLIER_CHECK_PLAN"),"监察计划管理台帐"));
		return null;
	}
}

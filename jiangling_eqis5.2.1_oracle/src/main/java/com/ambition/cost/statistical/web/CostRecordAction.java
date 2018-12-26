package com.ambition.cost.statistical.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.cost.entity.CostRecord;
import com.ambition.cost.statistical.service.CostRecordManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

/**
 * 类名:成本数据
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2014-11-29 发布
 */
@Namespace("/cost/cost-record")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "cost/cost-record", type = "redirectAction") })
public class CostRecordAction extends CrudActionSupport<CostRecord> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private Page<CostRecord> page;
	private CostRecord costRecord;
	private String sourceType;//来源类型
	@Autowired
	CostRecordManager costRecordManager;
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

	public Page<CostRecord> getPage() {
		return page;
	}

	public void setPage(Page<CostRecord> page) {
		this.page = page;
	}

	public CostRecord getCostRecord() {
		return costRecord;
	}

	public void setCostRecord(CostRecord costRecord) {
		this.costRecord = costRecord;
	}

	
	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	@Override
	public CostRecord getModel() {
		return costRecord;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			costRecord = new CostRecord();
			costRecord.setCompanyId(ContextUtils.getCompanyId());
			costRecord.setCreatedTime(new Date());
			costRecord.setCreator(ContextUtils.getUserName());
			costRecord.setSubCompanyId(ContextUtils.getSubCompanyId());
			costRecord.setDepartmentId(ContextUtils.getDepartmentId());
			costRecord.setBusinessUnitName(ContextUtils.getSubCompanyName());
			costRecord.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			costRecord = costRecordManager.getCostRecord(id);
		}
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="删除成本数据")
	public String delete() throws Exception {
		costRecordManager.deleteCostRecord(deleteIds);
		return null;
	}

	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	@Action("list-datas")
	@LogInfo(optType="数据",message="成本数据")
	public String getListDatas() throws Exception {
		page = costRecordManager.list(page,sourceType);
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存成本数据")
	public String save() throws Exception {
		try {
			costRecordManager.saveCostRecord(costRecord);
			this.renderText(JsonParser.getRowValue(costRecord));
		} catch (Exception e) {
			createErrorMessage("保存失败："+e.getMessage());
		}
		return null;
	}
	
	@Action("export")
	@LogInfo(optType="导出",message="手工录入成本数据")
	public String exportInputs() throws Exception {
		Page<CostRecord> page = new Page<CostRecord>(100000);
		page = costRecordManager.list(page,CostRecord.SOURCE_TYPE_INPUT);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page, "COST_COST_RECORD"),"手工录入成本数据"));
		return null;
	}
	@Action("export-collection")
	@LogInfo(optType="导出",message="集成成本数据")
	public String exportCollections() throws Exception {
		Page<CostRecord> page = new Page<CostRecord>(100000);
		page = costRecordManager.list(page,CostRecord.SOURCE_TYPE_COLLECTION);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page, "COST_COST_RECORD"),"集成成本数据"));
		return null;
	}
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		this.renderText(JSONObject.fromObject(map).toString());
	}
}

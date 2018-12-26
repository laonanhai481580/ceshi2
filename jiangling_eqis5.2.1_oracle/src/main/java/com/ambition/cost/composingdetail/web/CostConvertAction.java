package com.ambition.cost.composingdetail.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.cost.composingdetail.service.CostConvertManager;
import com.ambition.cost.entity.CostConvert;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

/**
 * 类名:集成成本时转换规则
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2014-11-29 发布
 */
@Namespace("/cost/cost-convert")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "cost/cost-convert", type = "redirectAction") })
public class CostConvertAction extends CrudActionSupport<CostConvert> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private Page<CostConvert> page;
	private CostConvert costConvert;
	@Autowired
	private CostConvertManager costConvertManager;
	
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

	public Page<CostConvert> getPage() {
		return page;
	}

	public void setPage(Page<CostConvert> page) {
		this.page = page;
	}

	public CostConvert getCostConvert() {
		return costConvert;
	}

	public void setCostConvert(CostConvert costConvert) {
		this.costConvert = costConvert;
	}

	@Override
	public CostConvert getModel() {
		return costConvert;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			costConvert = new CostConvert();
			costConvert.setCompanyId(ContextUtils.getCompanyId());
			costConvert.setCreatedTime(new Date());
			costConvert.setCreator(ContextUtils.getUserName());
			costConvert.setSubCompanyId(ContextUtils.getSubCompanyId());
			costConvert.setDepartmentId(ContextUtils.getDepartmentId());
		}else {
			costConvert = costConvertManager.getCostConvert(id);
		}
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="删除集成成本时转换规则")
	public String delete() throws Exception {
		try {
			costConvertManager.deleteCostConvert(deleteIds);
		} catch (Exception e) {
			renderText("删除失败!");
			log.error("删除规则失败!",e);
		}
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
	@LogInfo(optType="数据",message="集成成本时转换规则")
	public String getListDatas() throws Exception {
		page = costConvertManager.list(page);
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存集成成本时转换规则")
	public String save() throws Exception {
		try {
			costConvertManager.saveCostConvert(costConvert);
			this.renderText(JsonParser.getRowValue(costConvert));
		} catch (Exception e) {
			createErrorMessage("保存失败："+e.getMessage());
			log.error("保存集成成本时转换规则失败!",e);
		}
		return null;
	}
	
	@Action("export")
	@LogInfo(optType="导出",message="导出集成成本时转换规则")
	public String exportInputs() throws Exception {
		Page<CostConvert> page = new Page<CostConvert>(100000);
		page = costConvertManager.list(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page, "COST_COST_CONVERT"),"集成成本时转换规则"));
		return null;
	}
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		this.renderText(JSONObject.fromObject(map).toString());
	}
}

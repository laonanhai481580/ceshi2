package com.ambition.gsm.codeRules.web;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.gsm.codeRules.service.GsmCodeRulesManager;
import com.ambition.gsm.entity.GsmCodeRules;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;

/**
 * 计量编号规则一级类别(ACTION)
 * @author 张顺治
 *
 */
@Namespace("/gsm/code-rules")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/gsm/code-rules", type = "redirectAction") })
public class GsmCodeRulesAction extends CrudActionSupport<GsmCodeRules>{
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(this.getClass());
	private Long id;
	private String ids;
	private String deleteIds;
	private GsmCodeRules gsmCodeRules;
	private Page<GsmCodeRules> page;
	private JSONObject params;
	
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private GsmCodeRulesManager gsmCodeRulesManager;
	
	/**
	 * 对象
	 */
	@Override
	public GsmCodeRules getModel() {
		return gsmCodeRules;
	}
	
	/**
	 * 预处理
	 */
	@Override
	protected void prepareModel() throws Exception {
		if (id == null) {
			gsmCodeRules = new GsmCodeRules();
			gsmCodeRules.setCompanyId(ContextUtils.getCompanyId());
			gsmCodeRules.setCreatedTime(new Date());
			gsmCodeRules.setCreator(ContextUtils.getLoginName()); 
			gsmCodeRules.setCreatorName(ContextUtils.getUserName());
			gsmCodeRules.setBusinessUnitName(ContextUtils.getSubCompanyName());
			gsmCodeRules.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		} else {
			gsmCodeRules = gsmCodeRulesManager.getGsmCodeRules(id);
		}
	}
	
	/**
	 * 删除
	 */
	@Action("delete")
	@LogInfo(optType="删除",message="计量编号规则一级类别")
	@Override
	public String delete() throws Exception {
		try{
			createMessage(gsmCodeRulesManager.deleteGsmCodeRules(deleteIds));
		}catch(Exception e){
			logger.error("计量编号规则一级类别删除失败：", e);
			addActionError("一级类别删除失败：" + e.getMessage());
			createErrorMessage("一级类别删除失败：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 表单
	 */
	@Action("input")
	@Override
	public String input() throws Exception{
		return SUCCESS;
	}
	
	/**
	 * 保存
	 */
	@Action("save")
	@LogInfo(optType="保存",message="计量编号规则一级类别")
	public String save() throws Exception {  
		try{
			gsmCodeRules.setModifiedTime(new Date());
			gsmCodeRules.setModifier(ContextUtils.getLoginName());
			gsmCodeRules.setModifierName(ContextUtils.getUserName());
			gsmCodeRulesManager.saveGsmCodeRules(gsmCodeRules);
			logUtilDao.debugLog("保存", gsmCodeRules.toString());
			this.renderText(JsonParser.object2Json(gsmCodeRules));
		}catch(Exception e){
			logger.error("计量编号规则一级类别保存失败：", e);
			addActionError("一级类别保存失败：" + e.getMessage());
			createErrorMessage("一级类别保存失败：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 列表
	 */
	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 数据
	 * @return
	 * @throws Exception
	 */
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try {
			page = gsmCodeRulesManager.getPage(page);
			logUtilDao.debugLog("计量编号规则一级类别数据", "计量器具管理：计量器具台帐");
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			logger.error("计量编号规则一级类别数据查询失败：", e);
			addActionError("一级类别数据查询失败：" + e.getMessage());
			createErrorMessage("一级类别数据查询失败：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 导出
	 * @return
	 * @throws Exception
	 */
	@Action("export")
	@LogInfo(optType="导出",message="计量编号规则一级类别")
	public String export() throws Exception {
		try {
			Page<GsmCodeRules> page = new Page<GsmCodeRules>(100000);
			page = gsmCodeRulesManager.getPage(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MEASUREMENT_CODE_RULES"),"计量编号规则一级类别"));
		} catch (Exception e) {
			logger.error("计量编号规则一级类别导出失败：", e);
			addActionError("一级类别导出失败：" + e.getMessage());
			createErrorMessage("一级类别导出失败：" + e.getMessage());
		}
		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public GsmCodeRules getGsmCodeRules() {
		return gsmCodeRules;
	}

	public void setGsmCodeRules(GsmCodeRules gsmCodeRules) {
		this.gsmCodeRules = gsmCodeRules;
	}

	public Page<GsmCodeRules> getPage() {
		return page;
	}

	public void setPage(Page<GsmCodeRules> page) {
		this.page = page;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}
	
}

package com.ambition.gsm.codeSecRules.web;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.gsm.codeRules.service.GsmCodeRulesManager;
import com.ambition.gsm.codeSecRules.service.GsmCodeSecRulesManager;
import com.ambition.gsm.entity.GsmCodeRules;
import com.ambition.gsm.entity.GsmCodeSecRules;
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
 * 计量编号规则二级分类(ACTION)
 * @author 张顺治
 *
 */
@Namespace("/gsm/code-sec-rules")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/gsm/code-sec-rules", type = "redirectAction") })
public class GsmCodeSecRulesAction extends CrudActionSupport<GsmCodeSecRules>{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(this.getClass());
	private Long id;
	private String ids;
	private String deleteIds;
	private GsmCodeSecRules gsmCodeSecRules;
	private List<GsmCodeSecRules> list; 
	private Page<GsmCodeSecRules> page;
	private JSONObject params;
	private Long gsmCodeRulesId;//一级类别Id
	private GsmCodeRules gsmCodeRules;//以及类别对象
	
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private GsmCodeSecRulesManager gsmCodeSecRulesManager;
	@Autowired
	private GsmCodeRulesManager gsmCodeRulesManager;
	
	/**
	 * 对象
	 */
	@Override
	public GsmCodeSecRules getModel() {
		return gsmCodeSecRules;
	}
	
	/**
	 * 预处理
	 */
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			gsmCodeSecRules = new GsmCodeSecRules();
			gsmCodeSecRules.setCompanyId(ContextUtils.getCompanyId());
			gsmCodeSecRules.setCreatedTime(new Date());
			gsmCodeSecRules.setCreator(ContextUtils.getLoginName()); 
			gsmCodeSecRules.setCreatorName(ContextUtils.getUserName()); 
			gsmCodeSecRules.setBusinessUnitName(ContextUtils.getSubCompanyName());
			gsmCodeSecRules.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
			if(gsmCodeRulesId != null){
				gsmCodeRules = gsmCodeRulesManager.getGsmCodeRules(gsmCodeRulesId);
			}
			gsmCodeSecRules.setGsmCodeRules(gsmCodeRules);
		}else{
			gsmCodeSecRules = gsmCodeSecRulesManager.getGsmCodeSecRules(id);
		}
	}

	/**
	 * 表单
	 */
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 保存
	 */
	@Action("save")
	@LogInfo(optType="保存",message="计量编号规则二级分类")
	@Override
	public String save() throws Exception {
		try {
			gsmCodeSecRules.setModifiedTime(new Date());
			gsmCodeSecRules.setModifier(ContextUtils.getLoginName());
			gsmCodeSecRules.setModifierName(ContextUtils.getUserName());
			gsmCodeSecRulesManager.saveGsmCodeSecRules(gsmCodeSecRules);
			renderText(JsonParser.getRowValue(gsmCodeSecRules));
		} catch (Exception e) {
			logger.error("计量编号规则二级分类保存失败：", e);
			addActionError("二级分类保存失败："+e.getMessage());
			createErrorMessage("二级分类保存失败："+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 删除
	 */
	@Action("delete")
	@LogInfo(optType="删除",message="计量编号规则二级分类")
	@Override
	public String delete() throws Exception {
		try{
			gsmCodeSecRulesManager.deleteGsmSecCodeRules(deleteIds);
		}catch(Exception e){
			logger.error("计量编号规则二级分类删除失败：", e);
			addActionError("计量编号规则二级分类删除失败" + e.getMessage());
			createErrorMessage("计量编号规则二级分类删除失败" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 删除子级
	 * @return
	 * @throws Exception
	 */
	@Action("delete-subs")
	@LogInfo(optType="删除子级",message="计量编号规则二级分类")
	public String deleteSubs() throws Exception {
		try{
			int index = 0;
			if(gsmCodeRulesId != null){
				gsmCodeRules = gsmCodeRulesManager.getGsmCodeRules(gsmCodeRulesId);
				list = gsmCodeSecRulesManager.getGsmCodeSecRules(gsmCodeRules);
				if(list != null && list.size() > 0){
					for(GsmCodeSecRules gsmCodeSecRules: list){
						gsmCodeSecRulesManager.deleteGsmCodeSecRules(gsmCodeSecRules);
						index++;
					}
				}
			}
			createMessage("共"+index+"条 删除成功！");
		}catch(Exception e){
			logger.error("计量编号规则所有二级分类删除失败：",e);
			addActionError("所有二级分类删除失败："+e.getMessage());
			createErrorMessage("所有二级分类删除失败："+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 查询子级
	 * @return
	 * @throws Exception
	 */
	@Action("search-subs")
	public String searchSubs() throws Exception {
		String renderText = "no";
		if(gsmCodeRulesId != null){
			gsmCodeRules = gsmCodeRulesManager.getGsmCodeRules(gsmCodeRulesId);
			list = gsmCodeSecRulesManager.getGsmCodeSecRules(gsmCodeRules);
			if(list != null && list.size() > 0){
				renderText = "have";
			}
		}
		renderText(renderText);
		return null;
	}
	
	/**
	 * 列表
	 */
	@Action("list")
	@Override
	public String list() throws Exception {
		if(gsmCodeRulesId != null){
			gsmCodeRules = gsmCodeRulesManager.getGsmCodeRules(gsmCodeRulesId);
		}
		return SUCCESS;
	}
	
	/**
	 * 数据
	 * @return
	 * @throws Exception
	 */
	@Action("list-datas")
	public String getListDatas() throws Exception{
		try {
			if(gsmCodeSecRules != null){
				page = gsmCodeSecRulesManager.getPage(page);
			}else if(gsmCodeRulesId != null){
				gsmCodeRules = gsmCodeRulesManager.getGsmCodeRules(gsmCodeRulesId);
				page = gsmCodeSecRulesManager.getPage(page, gsmCodeRules);
			}
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			logger.error("计量编号规则二级分类保存失败：", e);
			addActionError("二级分类保存失败："+e.getMessage());
			createErrorMessage("二级分类保存失败："+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 导出
	 * @return
	 * @throws Exception
	 */
	@Action("export")
	@LogInfo(optType="导出",message="计量编号规则二级分类")
	public String export() throws Exception {
		try{
			Page<GsmCodeSecRules> page = new Page<GsmCodeSecRules>(100000);
			page = gsmCodeSecRulesManager.getPage(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MEASUREMENT_CODE_SECRULES"),"二级分类"));
		}catch(Exception e){
			logger.error("计量编号规则二级分类导出失败：", e);
			addActionError("二级分类导出失败："+e.getMessage());
			createErrorMessage("二级分类导出失败："+e.getMessage());
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

	public GsmCodeSecRules getGsmCodeSecRules() {
		return gsmCodeSecRules;
	}

	public void setGsmCodeSecRules(GsmCodeSecRules gsmCodeSecRules) {
		this.gsmCodeSecRules = gsmCodeSecRules;
	}

	public List<GsmCodeSecRules> getList() {
		return list;
	}

	public void setList(List<GsmCodeSecRules> list) {
		this.list = list;
	}

	public Page<GsmCodeSecRules> getPage() {
		return page;
	}

	public void setPage(Page<GsmCodeSecRules> page) {
		this.page = page;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public Long getGsmCodeRulesId() {
		return gsmCodeRulesId;
	}

	public void setGsmCodeRulesId(Long gsmCodeRulesId) {
		this.gsmCodeRulesId = gsmCodeRulesId;
	}

	public GsmCodeRules getGsmCodeRules() {
		return gsmCodeRules;
	}

	public void setGsmCodeRules(GsmCodeRules gsmCodeRules) {
		this.gsmCodeRules = gsmCodeRules;
	}
	
}

package com.ambition.spc.baseinfo.web;

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

import com.ambition.spc.baseinfo.service.PpkRuleManager;
import com.ambition.spc.entity.PpkRule;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

/**
 * 类名:PPK规则表(com.ambition.spc.baseinfo.web)
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2017年5月2日 发布
 */
@Namespace("/spc/base-info/ppk-rule")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "spc/base-info/ppk-rule", type = "redirectAction") })
public class PpkRuleAction extends CrudActionSupport<PpkRule> {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private PpkRule ppkRule;
	private Page<PpkRule> page= new Page<PpkRule>(Page.EACH_PAGE_TEN, true);
	@Autowired
	private PpkRuleManager ppkRuleManager;
	@Autowired
	private LogUtilDao logUtilDao;
	/**
	* 方法名:获取对象 
	* <p>功能说明：</p>
	* @return
	* @throws Exception
	 */
	@Override
	public PpkRule getModel() {
		return ppkRule;
	}
	@Override
	protected void prepareModel() throws Exception {
		if (id == null) {
			ppkRule = new PpkRule();
			//公司编号
			ppkRule.setCompanyId(ContextUtils.getCompanyId());
			//创建时间
			ppkRule.setCreatedTime(new Date());
			ppkRule.setCreator(ContextUtils.getUserName());
			ppkRule.setModifiedTime(new Date());
			ppkRule.setModifier(ContextUtils.getUserName());
		} else {
			ppkRule = ppkRuleManager.getPpkRule(id);
		}
	}
	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Action("list-datas")
	@LogInfo(optType="数据",message="PPK准则列表数据")
	public String getListDatas() throws Exception {
		page = ppkRuleManager.list(page);
		this.renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "SPC：PPK规则");
		return null;
	}
	@Action("save")
	@Override
	@LogInfo(optType="保存",message="保存PPK准则")
	public String save() throws Exception {
		try {
			ppkRuleManager.savePpkRule(ppkRule);
			//更新PPK
			
			this.renderText(JsonParser.object2Json(ppkRule));
		} catch (Exception e) {
			log.error("保存PPK准则：",e);
			createErrorMessage("保存失败："+e.getMessage());
		}
		return null;
	}
	@Action("delete")
	@Override
	@LogInfo(optType="删除",message="删除PPK准则")
	public String delete() throws Exception {
		try {
			ppkRuleManager.deleteBsRulesDao(deleteIds);
		} catch (Exception e) {
			log.error("删除PPK准则失败",e);
			this.renderText("删除失败:" + e.getMessage());
		}
		return null;
		
	}
	/**
	 * 创建返回消息
	 * @param message
	 */
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("error", true);
		map.put("message", message);
		this.renderText(JSONObject.fromObject(map).toString());
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
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

	public PpkRule getPpkRule() {
		return ppkRule;
	}

	public void setPpkRule(PpkRule ppkRule) {
		this.ppkRule = ppkRule;
	}

	public Page<PpkRule> getPage() {
		return page;
	}

	public void setPage(Page<PpkRule> page) {
		this.page = page;
	}

	public PpkRuleManager getPpkRuleManager() {
		return ppkRuleManager;
	}

	public void setPpkRuleManager(PpkRuleManager ppkRuleManager) {
		this.ppkRuleManager = ppkRuleManager;
	}

	public LogUtilDao getLogUtilDao() {
		return logUtilDao;
	}

	public void setLogUtilDao(LogUtilDao logUtilDao) {
		this.logUtilDao = logUtilDao;
	}
	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

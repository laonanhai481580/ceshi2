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

import com.ambition.spc.baseinfo.service.CpkRuleManager;
import com.ambition.spc.entity.CpkRule;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

/**
 * 
 * 类名:CPK规则表(com.ambition.spc.baseinfo.web)
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2016年7月5日 发布
 */
@Namespace("/spc/base-info/cpk-rule")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "spc/base-info/cpk-rule", type = "redirectAction") })
public class CpkRuleAction extends CrudActionSupport<CpkRule> {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private CpkRule cpkRule;
	private Page<CpkRule> page= new Page<CpkRule>(Page.EACH_PAGE_TEN, true);
	@Autowired
	private CpkRuleManager cpkRuleManager;
	@Autowired
	private LogUtilDao logUtilDao;
	/**
	* 方法名:获取对象 
	* <p>功能说明：</p>
	* @return
	* @throws Exception
	 */
	@Override
	public CpkRule getModel() {
		return cpkRule;
	}
	@Override
	protected void prepareModel() throws Exception {
		if (id == null) {
			cpkRule = new CpkRule();
			//公司编号
			cpkRule.setCompanyId(ContextUtils.getCompanyId());
			//创建时间
			cpkRule.setCreatedTime(new Date());
			cpkRule.setCreator(ContextUtils.getUserName());
			cpkRule.setModifiedTime(new Date());
			cpkRule.setModifier(ContextUtils.getUserName());
		} else {
			cpkRule = cpkRuleManager.getCpkRule(id);
		}
	}
	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Action("list-datas")
	@LogInfo(optType="数据",message="CPK准则列表数据")
	public String getListDatas() throws Exception {
		page = cpkRuleManager.list(page);
		this.renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "SPC：CPK规则");
		return null;
	}
	@Action("save")
	@Override
	@LogInfo(optType="保存",message="保存CPK准则")
	public String save() throws Exception {
		try {
			cpkRuleManager.saveCpkRule(cpkRule);
			//更新CPK
			
			this.renderText(JsonParser.object2Json(cpkRule));
		} catch (Exception e) {
			log.error("保存CPK准则：",e);
			createErrorMessage("保存失败："+e.getMessage());
		}
		return null;
	}
	@Action("delete")
	@Override
	@LogInfo(optType="删除",message="删除CPK准则")
	public String delete() throws Exception {
		try {
			cpkRuleManager.deleteBsRulesDao(deleteIds);
		} catch (Exception e) {
			log.error("删除CPK准则失败",e);
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

	public CpkRule getCpkRule() {
		return cpkRule;
	}

	public void setCpkRule(CpkRule cpkRule) {
		this.cpkRule = cpkRule;
	}

	public Page<CpkRule> getPage() {
		return page;
	}

	public void setPage(Page<CpkRule> page) {
		this.page = page;
	}

	public CpkRuleManager getCpkRuleManager() {
		return cpkRuleManager;
	}

	public void setCpkRuleManager(CpkRuleManager cpkRuleManager) {
		this.cpkRuleManager = cpkRuleManager;
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

package com.ambition.supplier.estimate.web;

import java.util.Date;
import java.util.HashMap;
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

import com.ambition.supplier.entity.EvaluatingGradeRule;
import com.ambition.supplier.estimate.service.EvaluatingGradeRuleManager;
import com.ambition.supplier.estimate.service.EvaluatingIndicatorManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

/**
 * 类名:评分规则Action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：自动评分规则的增删改查</p>
 * @author  赵骏
 * @version 1.00 2013-4-20 发布
 */
@Namespace("/supplier/estimate/indicator/quarter")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "supplier/estimate/indicator/quarter", type = "redirectAction") })
public class EvaluatingGradeRuleAction  extends CrudActionSupport<EvaluatingGradeRule> {
	private Logger logger = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long indicatorId;//指标ID
	private String deleteIds;//删除的编号 
	
	private EvaluatingGradeRule evaluatingGradeRule;
	
 	@Autowired
	private EvaluatingGradeRuleManager gradeRuleManager;

 	@Autowired
 	private EvaluatingIndicatorManager evaluatingIndicatorManager;
 	
	public EvaluatingGradeRule getEvaluatingGradeRule() {
		return evaluatingGradeRule;
	}

	public void setEvaluatingGradeRule(EvaluatingGradeRule evaluatingGradeRule) {
		this.evaluatingGradeRule = evaluatingGradeRule;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIndicatorId() {
		return indicatorId;
	}

	public void setIndicatorId(Long indicatorId) {
		this.indicatorId = indicatorId;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public EvaluatingGradeRule getModel() {
		return evaluatingGradeRule;
	}
	
	@Action("rule-input")
	@LogInfo(optType="自动评分规则维护页面")
	public String ruleInput() throws Exception {
		return SUCCESS;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			evaluatingGradeRule = new EvaluatingGradeRule();
			evaluatingGradeRule.setCreatedTime(new Date());
			evaluatingGradeRule.setCompanyId(ContextUtils.getCompanyId());
			evaluatingGradeRule.setCreator(ContextUtils.getUserName());
			evaluatingGradeRule.setModifiedTime(new Date());
			evaluatingGradeRule.setModifier(ContextUtils.getUserName());
			evaluatingGradeRule.setBusinessUnitName(ContextUtils.getSubCompanyName());
			evaluatingGradeRule.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
			evaluatingGradeRule.setEvaluatingIndicator(evaluatingIndicatorManager.getEvaluatingIndicator(indicatorId));
		}else {
			evaluatingGradeRule = gradeRuleManager.getGradeRule(id);
		}
	}
	
	@Action("save-rule")
	@Override
	@LogInfo(optType="保存自动评分规则")
	public String save() throws Exception {
		try {
			gradeRuleManager.saveGradeRule(evaluatingGradeRule);
			if(id == null){
				Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"新增自动评分规则成功!");
			}else{
				Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"保存自动评分规则成功!");
			}
			this.renderText(JsonParser.getRowValue(evaluatingGradeRule));
		} catch (Exception e) {
			if(e instanceof AmbFrameException){
				logger.debug("保存自动评分规则出错",e);
			}else{
				logger.error("保存自动评分规则出错",e);
			}
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"保存自动评分规则失败!" + e.getMessage());
			createErrorMessage("保存评分规则失败:" + e.getMessage());
		}
		return null;
	}
	
	@Action("delete-rule")
	@Override
	@LogInfo(optType="删除评分规则")
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				gradeRuleManager.deleteGradeRule(deleteIds);
				renderText("已发起过！");
				Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"删除成功,deleteIds:" + deleteIds);
			} catch (Exception e) {
				renderText("删除失败:" + e.getMessage());
				logger.error("删除评分规则失败!",e);
				Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"删除失败," + e.getMessage());
			}
		}
		return null;
	}
	
	@Action("list-rule-datas")
	@LogInfo(optType="查询",message="查询评分规则")
	public String getGradeRuleByIndicatior() throws Exception {
		Page<EvaluatingGradeRule> page = new Page<EvaluatingGradeRule>();
		List<EvaluatingGradeRule> gradeRules = gradeRuleManager.listAll(indicatorId);
		page.setResult(gradeRules);
		page.setTotalCount(gradeRules.size());
		renderText(PageUtils.pageToJson(page,"SUPPLIER_EVALUATING_GRADE_RULE"));
		return null;
	}
	
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	@SuppressWarnings("unused")
	private void createMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	private void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}

	@Override
	public String list() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}
}

package com.ambition.iqc.samplestandard.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.iqc.entity.SampleTransitionRule;
import com.ambition.iqc.entity.UseBaseType;
import com.ambition.iqc.samplestandard.service.SampleSchemeManager;
import com.ambition.iqc.samplestandard.service.SampleTransitionRuleManager;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**    
 * SampleCodeLetterAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/iqc/sample-standard/transition-rule")
@ParentPackage("default")
public class SampleTransitionRuleAction extends CrudActionSupport<SampleTransitionRule> {
	private static final long serialVersionUID = -6215661857799052614L;
	@Autowired
	private SampleTransitionRuleManager sampleTransitionRuleManager;
	
	@Autowired
	private SampleSchemeManager sampleSchemeManager;
	
	@Action("save")
	@LogInfo(optType="保存",message="抽样方案转移规则")
	@Override
	public String save() throws Exception {
		try {
			String baseType = Struts2Utils.getParameter("baseType");
			JSONArray jsonArray = JSONArray.fromObject(Struts2Utils.getParameter("transtionRuleStrs"));
			sampleTransitionRuleManager.saveSampleTransitionRule(jsonArray,baseType);
			createMessage("保存成功!");
		} catch (Exception e) {
			createErrorMessage("保存失败：" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	

	@Action("input")
	@Override
	public String input() throws Exception {
		ActionContext.getContext().put("statisticalMthodOptions",sampleTransitionRuleManager.getStatisticalMthodOptions());
		ActionContext.getContext().put("comparisonOperatorsOptions",sampleTransitionRuleManager.getComparisonOperatorsOptions());
		List<SampleTransitionRule> sampleTransitionRules = sampleTransitionRuleManager.list();
		Map<String,SampleTransitionRule> transitionRuleMap = new HashMap<String, SampleTransitionRule>();
		for(SampleTransitionRule transitionRule : sampleTransitionRules){
			transitionRuleMap.put(transitionRule.getSourceRule() + "_" + transitionRule.getTargetRule(),transitionRule);
		}
		Struts2Utils.getRequest().setAttribute("transitionRuleMap", transitionRuleMap);
		UseBaseType useBaseType = sampleSchemeManager.getUseBaseType();
		Struts2Utils.getRequest().setAttribute("useBaseType", useBaseType.getBaseType());
		return SUCCESS;
	}
	
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
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
	@Override
	public SampleTransitionRule getModel() {
		return null;
	}
	@Override
	public String delete() throws Exception {
		return null;
	}
	@Override
	public String list() throws Exception {
		return null;
	}
	@Override
	protected void prepareModel() throws Exception {
	}
}

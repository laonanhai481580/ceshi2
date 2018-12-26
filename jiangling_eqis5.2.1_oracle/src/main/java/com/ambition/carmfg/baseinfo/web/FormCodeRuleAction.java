package com.ambition.carmfg.baseinfo.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeRuleManager;
import com.ambition.carmfg.entity.FormCodingRule;
import com.ambition.product.BaseAction;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**    
 * SampleCodeLetterAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/carmfg/base-info/form-coding-rule")
@ParentPackage("default")
public class FormCodeRuleAction extends BaseAction<FormCodingRule> {
	private static final long serialVersionUID = -6215661857799052614L;
	@Autowired
	private FormCodeRuleManager formCodingRuleManager;
	
	@Action("save")
	@Override
	public String save() throws Exception {
		try {
			JSONArray jsonArray = JSONArray.fromObject(Struts2Utils.getParameter("formCodingRuleStrs"));
			formCodingRuleManager.saveFormCodingRules(jsonArray);
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
		List<FormCodingRule> formCodingRules = formCodingRuleManager.getAllFormCodingRules();
		Struts2Utils.getRequest().setAttribute("formCodingRules", formCodingRules);
		renderMenu();
		return SUCCESS;
	}
	
	@Action("preview")
	public String preview() throws Exception {
		try {
			String formCode = formCodingRuleManager.generatedTempCodeByRule(Struts2Utils.getParameter("rule"));
			createMessage(formCode);
		} catch (Exception e) {
			createErrorMessage("生成编码失败：" + e.getMessage());
			e.printStackTrace();
		}
		return null;
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
	public FormCodingRule getModel() {
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

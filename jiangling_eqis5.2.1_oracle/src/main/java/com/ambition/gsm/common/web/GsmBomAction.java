package com.ambition.gsm.common.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.gsm.codeRules.service.GsmCodeRulesManager;
import com.ambition.gsm.common.service.GsmBomManager;
import com.ambition.gsm.entity.GsmCodeRules;
import com.ambition.gsm.entity.GsmEquipment;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 计量器具编码(ACTION)
 * @author 张顺治
 *
 */
@Namespace("/gsm/common")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "gsm/common", type = "redirectAction") })
public class GsmBomAction extends CrudActionSupport<GsmEquipment> {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	private Long id;
	private String ids;
	private GsmEquipment gsmEquipment;
	private List<GsmEquipment> list;
	private Page<GsmEquipment> page;
	private JSONObject params;
	private GsmCodeRules gsmCodeRules;
	
	@Autowired
	private GsmBomManager gsmBomManager;
	@Autowired
	private GsmCodeRulesManager gsmCodeRulesManager;

	@Override
	public GsmEquipment getModel() {
		return gsmEquipment;
	}

	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		
		return null;
	}

	@Override
	public String list() throws Exception {
		
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		
		
	}

	@Override
	public String save() throws Exception {
		return null;
	}
	
	@Action("gsm-bom-select")
	public String gsmBomSelect() throws Exception {
		List<Map<String,Object>> gsmTypeMaps = new ArrayList<Map<String,Object>>();
		List<GsmCodeRules> gsmCodeRuless = gsmCodeRulesManager.getGsmCodeRules();
		for (GsmCodeRules gsmCodeRules : gsmCodeRuless) {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("data",gsmCodeRules.getMeasurementType());
			map.put("isLeaf",true);
			Map<String,Object> attrMap = new HashMap<String, Object>();
			attrMap.put("id",gsmCodeRules.getId());
			attrMap.put("typeName",gsmCodeRules.getMeasurementType());
			attrMap.put("typeValue",gsmCodeRules.getTypeCode());
			map.put("attr",attrMap);
			gsmTypeMaps.add(map);
		}
		ActionContext.getContext().put("gsmTypeMaps",JSONArray.fromObject(gsmTypeMaps).toString());
		return SUCCESS;
	}
	
	@Action("gsm-bom-datas")
	public String getGsmBomDatas() throws Exception {
		if(id != null && id != 0){
			gsmCodeRules = gsmCodeRulesManager.getGsmCodeRules(id);
		}
		String searchParams = Struts2Utils.getParameter("searchParams");
		if(StringUtils.isNotEmpty(searchParams)){
			params = JSONObject.fromObject(searchParams);
		}
		if(params != null){
			page = gsmBomManager.getPageByParams(page, params);
		}else{
			page = gsmBomManager.getPageByGsmCodeRules(page, gsmCodeRules);
		}
		try {
			Page<Object> resultPage = new Page<Object>();
			resultPage.setOrder(page.getOrder());
			resultPage.setOrderBy(page.getOrderBy());
			resultPage.setPageNo(page.getPageNo());
			resultPage.setPageSize(page.getPageSize());
			resultPage.setTotalCount(page.getTotalCount());
			List<Object> list = new ArrayList<Object>();
			for(GsmEquipment gsm : page.getResult()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id",gsm.getId());
//				map.put("measurementNo",gsm.getMeasurementNo());
//				map.put("measurementSerialNo",gsm.getMeasurementSerialNo());
//				map.put("measurementName",gsm.getMeasurementName());
//				map.put("measurementSpecification", gsm.getMeasurementSpecification());
				list.add(map);
			}
			resultPage.setResult(list);
			renderText(PageUtils.pageToJson(resultPage));
		} catch (Exception e) {
			logger.error("计量器具检索数据失败：", e);
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

	public GsmEquipment getGsmEquipment() {
		return gsmEquipment;
	}

	public void setGsmEquipment(GsmEquipment gsmEquipment) {
		this.gsmEquipment = gsmEquipment;
	}

	public List<GsmEquipment> getList() {
		return list;
	}

	public void setList(List<GsmEquipment> list) {
		this.list = list;
	}

	public Page<GsmEquipment> getPage() {
		return page;
	}

	public void setPage(Page<GsmEquipment> page) {
		this.page = page;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public GsmCodeRules getGsmCodeRules() {
		return gsmCodeRules;
	}

	public void setGsmCodeRules(GsmCodeRules gsmCodeRules) {
		this.gsmCodeRules = gsmCodeRules;
	}
	
}

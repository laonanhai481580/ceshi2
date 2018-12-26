package com.ambition.supplier.manager.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.entity.SupplierGoal;
import com.ambition.supplier.entity.WarnSign;
import com.ambition.supplier.manager.service.ManagerManager;
import com.ambition.supplier.manager.service.WarnSignManager;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;

/**    
 * WarnSignAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/supplier/manager/degree")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "supplier/manager/degree", type = "redirectAction") })
public class WarnSignAction extends CrudActionSupport<WarnSign> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private Page<WarnSign> page;
	private WarnSign warnSign;
	private JSONObject params;
	@Autowired
	private WarnSignManager warnSignManager;
	@Autowired
	private ManagerManager managerManager;
	@Autowired
	private LogUtilDao logUtilDao;

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

	public Page<WarnSign> getPage() {
		return page;
	}

	public void setPage(Page<WarnSign> page) {
		this.page = page;
	}

	public WarnSign getWarnSign() {
		return warnSign;
	}

	public void setWarnSign(WarnSign warnSign) {
		this.warnSign = warnSign;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	@Override
	public WarnSign getModel() {
		return warnSign;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			warnSign = new WarnSign();
			warnSign.setCreatedTime(new Date());
			warnSign.setCompanyId(ContextUtils.getCompanyId());
			warnSign.setCreator(ContextUtils.getUserName());
			warnSign.setLastModifiedTime(new Date());
			warnSign.setLastModifier(ContextUtils.getUserName());
			warnSign.setBusinessUnitName(ContextUtils.getSubCompanyName());
			warnSign.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			warnSign = warnSignManager.getWarnSign(id);
		}
	}
	
	private void renderinput(){
		List<Option> options = new ArrayList<Option>();
		for(int i=0;i<=100;i++){
			Option option = new Option();
			option.setName(i+".0");
			option.setValue(i+".0");
			options.add(option);
		}
		ActionContext.getContext().put("scores",options);
	}
	
	@Action("degree-input")
	@Override
	public String input() throws Exception {
		renderinput();
		return SUCCESS;
	}
	
	@Action("save")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			warnSign.setLastModifiedTime(new Date());
			warnSign.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", warnSign.toString());
		}else{
			logUtilDao.debugLog("保存", warnSign.toString());
		}
		try {
			warnSignManager.saveWarnSign(warnSign);
			addActionMessage("保存成功!");
		} catch (Exception e) {
			e.printStackTrace();
			warnSign.setId(id);
			addActionMessage("保存失败："+ e.getMessage());
		}
		renderinput();
		return "degree-input";
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			renderText("删除的对象不存在!");
		}else{
			try {
				warnSignManager.deleteWarnSign(deleteIds);
			} catch (Exception e) {
				renderText("删除失败:" + e.getMessage());
			}
		}
		return null;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Action("degree-list")
	public String degreeList() throws Exception {
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception {
		page = warnSignManager.list(page);
		renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "供应商质量管理：基础设置-等级及红黄牌规则");
		return null;
	}
	
	@Action("supplier-estimate-stat-distribution")
	public String estimateStatDistribution() throws Exception {
		return SUCCESS;
	}
	
	@Action("supplier-estimate-stat-distribution-datas")
	public String estimateStatDistributionDatas() throws Exception {
		params = convertJsonObject(params);
		String[] importances = new String[]{"A类","B类","C类","D类"};
		List<String> categories = new ArrayList<String>();
		Map<String,Object> result = new HashMap<String, Object>();
		List<WarnSign> warnSingList=warnSignManager.list();
		Map<String,Boolean> warnSignMap = new HashMap<String, Boolean>();
		List<Object> colModels = new ArrayList<Object>();
		JSONObject modelJson = new JSONObject();
		modelJson.put("name","supplierType");
		modelJson.put("label","供应商类别");
		modelJson.put("index","supplierType");
		colModels.add(modelJson);
		for(WarnSign warnSign : warnSingList){
			if(warnSignMap.containsKey(warnSign.getEstimateDegree())){
				continue;
			}
			warnSignMap.put(warnSign.getEstimateDegree(),true);
			categories.add(warnSign.getEstimateDegree()+"供应商");	
			modelJson = new JSONObject();
			modelJson.put("name",warnSign.getEstimateDegree());
			modelJson.put("label",warnSign.getEstimateDegree()+"供应商");
			modelJson.put("index",warnSign.getEstimateDegree());
			colModels.add(modelJson);
		}
		modelJson = new JSONObject();
		modelJson.put("name","heji");
		modelJson.put("label","合计");
		modelJson.put("index","heji");
		colModels.add(modelJson);
		
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> tabledata = new ArrayList<Map<String,Object>>();
		result.put("title", params.getString("evaluateYear")+"供应商评价等级分布");
		for(String importance : importances){
			Map<String,Object> dataMap = new HashMap<String, Object>();
			dataMap.put("supplierType",importance+"类供应商");
			List<SupplierGoal> supplierGoalList=managerManager.list(Integer.parseInt(params.getString("evaluateYear")),params.getString("businessUnitCode"),importance);
			for(int j=0;j<supplierGoalList.size();j++){
				SupplierGoal s=supplierGoalList.get(j);
				String type = "other";
				if(warnSignMap.containsKey(s.getEvaluateGrade())){
					type = s.getEvaluateGrade();
				}
				if(dataMap.containsKey(type)){
					dataMap.put(type,(Integer)dataMap.get(type)+1);
				}else{
					dataMap.put(type,1);
				}
			}
			for(WarnSign warnSign : warnSingList){
				if(!dataMap.containsKey(warnSign.getEstimateDegree())){
					dataMap.put(warnSign.getEstimateDegree(), 0);
				}
			}
			dataMap.put("heji",supplierGoalList.size());
			tabledata.add(dataMap);
		}
		Map<String,Object> hjMap = new HashMap<String, Object>();
		hjMap.put("supplierType", "合计");
		int total=0;
		Map<String,Boolean> existMap = new HashMap<String, Boolean>();
		for(WarnSign warnSign : warnSingList){
			String evaluateGrade=warnSign.getEstimateDegree();
			if(existMap.containsKey(evaluateGrade)){
				continue;
			}
			existMap.put(evaluateGrade,true);
			
			List<SupplierGoal> supplierGoalList=managerManager.listDstimateDegree(Integer.parseInt(params.getString("evaluateYear")),params.getString("businessUnitCode"),evaluateGrade);
			hjMap.put(evaluateGrade, supplierGoalList.size());
			total=total+supplierGoalList.size();
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("y", supplierGoalList.size());
			map.put("importance", evaluateGrade);
			data.add(map);
		}
		hjMap.put("heji", total);
		tabledata.add(hjMap);
		result.put("yAxisTitle1","供<br/>应<br/>商<br/>数<br/>量");
		Map<String,Object> series1 = new HashMap<String, Object>();
		series1.put("name", "供应商");
		series1.put("data",data);
		result.put("series1", series1);
		result.put("max", 99.9);
		result.put("categories", categories);
		result.put("colModel",colModels);
		result.put("tabledata", tabledata);
		renderText(JSONObject.fromObject(result).toString());
		logUtilDao.debugLog("查询", "供应商质量管理：供应商评价-供应商评级分布");
		return null;
	}
	
	/**
	 * 转换json
	 * @param params
	 * @return
	 */
	private JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		if(params == null){
			return resultJson;
		}
		for(Object key : params.keySet()){
			resultJson.put(key,params.getJSONArray(key.toString()).get(0));
		}
		return resultJson;
	}	
	
}

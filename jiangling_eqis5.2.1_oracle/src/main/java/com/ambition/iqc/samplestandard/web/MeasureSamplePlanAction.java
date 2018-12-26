package com.ambition.iqc.samplestandard.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.iqc.entity.AcceptanceQualityLimit;
import com.ambition.iqc.entity.SampleCodeLetter;
import com.ambition.iqc.entity.SampleScheme;
import com.ambition.iqc.samplestandard.service.SampleSchemeManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.mms.base.DynamicColumnValues;
import com.norteksoft.mms.base.utils.view.DynamicColumnDefinition;
import com.norteksoft.mms.form.enumeration.DataType;
import com.norteksoft.mms.form.enumeration.EditControlType;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

/**    
 * SamplePlanAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/iqc/sample-standard/measure-sample")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "iqc/sample-standard/measure-sample", type = "redirectAction") })
public class MeasureSamplePlanAction extends CrudActionSupport<SampleScheme> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private SampleScheme sampleScheme;
	private Page<SampleScheme> page;
	private JSONObject params;
	@Autowired
	private SampleSchemeManager sampleSchemeManager;
	@Autowired
	private LogUtilDao logUtilDao;
	private List<Option> listOption;
	private List<DynamicColumnDefinition> dynamicColumn = new ArrayList<DynamicColumnDefinition>();
	//组列
	private String groupNames;//列名
	
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

	public SampleScheme getSampleScheme() {
		return sampleScheme;
	}

	public void setSampleScheme(SampleScheme sampleScheme) {
		this.sampleScheme = sampleScheme;
	}

	public Page<SampleScheme> getPage() {
		return page;
	}

	public void setPage(Page<SampleScheme> page) {
		this.page = page;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public List<Option> getListOption() {
		return listOption;
	}

	public void setListOption(List<Option> listOption) {
		this.listOption = listOption;
	}

	public List<DynamicColumnDefinition> getDynamicColumn() {
		return dynamicColumn;
	}

	public void setDynamicColumn(List<DynamicColumnDefinition> dynamicColumn) {
		this.dynamicColumn = dynamicColumn;
	}

	public String getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}

	@Override
	public SampleScheme getModel() {
		return sampleScheme;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			sampleScheme = new SampleScheme();
			sampleScheme.setType(SampleScheme.ORDINARY_TYPE);
			sampleScheme.setBaseType(SampleCodeLetter.MIL_TYPE);
			sampleScheme.setCountType(SampleScheme.MEASURE_TYPE);
			sampleScheme.setCompanyId(ContextUtils.getCompanyId());
			sampleScheme.setCreatedTime(new Date());
			sampleScheme.setCreator(ContextUtils.getUserName());
			sampleScheme.setLastModifiedTime(new Date());
			sampleScheme.setLastModifier(ContextUtils.getUserName());
			sampleScheme.setBusinessUnitName(ContextUtils.getSubCompanyName());
			sampleScheme.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			sampleScheme = sampleSchemeManager.getSampleScheme(id);
		}
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="抽样方案")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			sampleScheme.setLastModifiedTime(new Date());
			sampleScheme.setLastModifier(ContextUtils.getUserName());
			sampleScheme.getAcceptanceQualityLimits().clear();
			logUtilDao.debugLog("修改", sampleScheme.toString());
		}else{
			sampleScheme.setAcceptanceQualityLimits(new ArrayList<AcceptanceQualityLimit>());
			logUtilDao.debugLog("保存", sampleScheme.toString());
		}
		try {
			sampleSchemeManager.saveSampleScheme(sampleScheme,convertJsonObject(params));
			JSONObject jsonObject = JSONObject.fromObject(JsonParser.getRowValue(sampleScheme));
			for(AcceptanceQualityLimit aql : sampleScheme.getAcceptanceQualityLimits()){
				jsonObject.put("params." + aql.getAql(), aql.getAmount());
			}
			//加严
			List<SampleScheme> sampleSchemes = sampleSchemeManager.querySampleScheme(SampleCodeLetter.MIL_TYPE,SampleScheme.TIGHTEN_TYPE,sampleScheme.getCode(),SampleScheme.MEASURE_TYPE);
			if(!sampleSchemes.isEmpty()){
				jsonObject.put("params.tighten",sampleSchemes.get(0).getAmount());
			}
			//减量
			sampleSchemes = sampleSchemeManager.querySampleScheme(SampleCodeLetter.MIL_TYPE,SampleScheme.RELAX_TYPE,sampleScheme.getCode(),SampleScheme.MEASURE_TYPE);
			if(!sampleSchemes.isEmpty()){
				jsonObject.put("params.relax",sampleSchemes.get(0).getAmount());
			}
			jsonObject.put("id",sampleScheme.getId());
			this.renderText(jsonObject.toString());
		} catch (Exception e) {
			createErrorMessage("保存失败：" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="抽样方案")
	@Override
	public String delete() throws Exception {
		try{
			sampleSchemeManager.deleteSampleScheme(deleteIds,"1916");
		}catch(Exception e){
			e.printStackTrace();
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
		//拼接列表
		StringBuffer colNamesByAql = new StringBuffer();
		String[] mitAqls = SampleScheme.getMitAQLs();
		List<String> columns = new ArrayList<String>();
		columns.add("params.tighten|加严");
		for(String aql : mitAqls){
			columns.add("params." + aql);
		}
		columns.add("params.relax|减量");
		for(String name : columns){
			String colName = name.split("\\|")[1],index=name.split("\\|")[0];
			DynamicColumnDefinition dynamicFieldOptions =new DynamicColumnDefinition();
			dynamicFieldOptions.setColName(colName);
			dynamicFieldOptions.setName(index);
			dynamicFieldOptions.setColWidth("100");
			dynamicFieldOptions.setVisible(true);
			dynamicFieldOptions.setEditable(true);
			dynamicFieldOptions.setEditRules("number:true,min:0");
			dynamicFieldOptions.setEdittype(EditControlType.TEXT);
			dynamicFieldOptions.setType(DataType.TEXT);
			dynamicColumn.add(dynamicFieldOptions);
		}
		colNamesByAql.append("[{startColumnName: 'params.validateLevel7', numberOfColumns: 7, titleText: '验证水平(VL)'}]");
		this.setGroupNames(colNamesByAql.toString());
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try {
			page = sampleSchemeManager.getListDatasByCountType(page,SampleScheme.ORDINARY_TYPE,SampleCodeLetter.MIL_TYPE,SampleScheme.MEASURE_TYPE);
			this.renderText(PageUtils.dynamicPageToJson(page,new DynamicColumnValues(){
				public void addValuesTo(List<Map<String, Object>> result) {
					for(Map<String, Object> map : result){
						Long id = Long.valueOf(map.get("id").toString());
						SampleScheme sampleScheme = sampleSchemeManager.getSampleScheme(id);
						for(AcceptanceQualityLimit aql : sampleScheme.getAcceptanceQualityLimits()){
							map.put("params." + aql.getAql(), aql.getAmount());
						}
						//加严
						List<SampleScheme> sampleSchemes = sampleSchemeManager.querySampleScheme(SampleCodeLetter.MIL_TYPE,SampleScheme.TIGHTEN_TYPE,sampleScheme.getCode(),SampleScheme.MEASURE_TYPE);
						if(!sampleSchemes.isEmpty()){
							map.put("params.tighten",sampleSchemes.get(0).getAmount());
						}
						//减量
						sampleSchemes = sampleSchemeManager.querySampleScheme(SampleCodeLetter.MIL_TYPE,SampleScheme.RELAX_TYPE,sampleScheme.getCode(),SampleScheme.MEASURE_TYPE);
						if(!sampleSchemes.isEmpty()){
							map.put("params.relax",sampleSchemes.get(0).getAmount());
						}
					}
				}
			}));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logUtilDao.debugLog("查询", "进货检验管理：抽样方案维护-计数值抽样计划");
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

package com.ambition.iqc.samplestandard.web;

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
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**    
 * SampleSchemeAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/iqc/sample-standard/relax-sample")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "iqc/sample-standard/relax-sample", type = "redirectAction") })
public class RelaxSampleAction extends CrudActionSupport<SampleScheme> {
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
			sampleScheme.setBaseType(SampleCodeLetter.GB_TYPE);
			sampleScheme.setType(SampleScheme.RELAX_TYPE);
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
			@SuppressWarnings("unchecked")
			Map<String,String> paramterMap = Struts2Utils.getRequest().getParameterMap();
			Map<String,Map<String,String>> aqlMap = new HashMap<String, Map<String,String>>();
			for(String key : paramterMap.keySet()){
				if(key.toString().indexOf("_")==-1){
					continue;
				}
				key = key.replace("__",".");
				String[] strs = key.split("_");
				Map<String,String> param = null;
				if(aqlMap.containsKey(strs[1])){
					param = aqlMap.get(strs[1]);
				}else{
					param = new HashMap<String, String>();
					aqlMap.put(strs[1],param);
				}
				param.put(strs[0],Struts2Utils.getParameter(key.replace(".","__")));
			}
			for(String aql:aqlMap.keySet()){
				Map<String,String> param = aqlMap.get(aql);
				String ac = param.get("ac"),re=param.get("re"),amountStr = param.get("amount");
				if(StringUtils.isEmpty(ac)||StringUtils.isEmpty(re)||StringUtils.isEmpty(amountStr)){
					continue;
				}
				AcceptanceQualityLimit acceptanceQualityLimit = new AcceptanceQualityLimit();
				acceptanceQualityLimit.setAql(aql);
				if(StringUtils.isNotEmpty(ac)&&StringUtils.isNumeric(ac)){
					acceptanceQualityLimit.setAc(Integer.valueOf(ac));
				}
				if(StringUtils.isNotEmpty(re)&&StringUtils.isNumeric(re)){
					acceptanceQualityLimit.setRe(Integer.valueOf(re));
				}
				acceptanceQualityLimit.setCompanyId(ContextUtils.getCompanyId());
				acceptanceQualityLimit.setCreatedTime(new Date());
				acceptanceQualityLimit.setCreator(ContextUtils.getUserName());
				acceptanceQualityLimit.setLastModifiedTime(new Date());
				acceptanceQualityLimit.setLastModifier(ContextUtils.getUserName());
				acceptanceQualityLimit.setCode(sampleScheme.getCode());
				acceptanceQualityLimit.setCountType(sampleScheme.getCountType());
				acceptanceQualityLimit.setAmount(Integer.valueOf(amountStr));
				acceptanceQualityLimit.setBaseType(sampleScheme.getBaseType());
				acceptanceQualityLimit.setType(sampleScheme.getType());
				acceptanceQualityLimit.setSampleScheme(sampleScheme);
				sampleScheme.getAcceptanceQualityLimits().add(acceptanceQualityLimit);
			}
			sampleSchemeManager.saveSampleScheme(sampleScheme);
			JSONObject jsonObject = JSONObject.fromObject(JsonParser.getRowValue(sampleScheme));
			for(AcceptanceQualityLimit aql : sampleScheme.getAcceptanceQualityLimits()){
				String str = aql.getAql().replace(".","__");
				jsonObject.put("ac_" + str, aql.getAc());
				jsonObject.put("re_" + str, aql.getRe());
				jsonObject.put("amount_" + str, aql.getAmount());
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
			sampleSchemeManager.deleteSampleScheme(deleteIds);
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
		List<Option> listOptions = ApiFactory.getSettingService().getOptionsByGroupCode("iqc_acceptance_quality_limit");
		this.setListOption(listOptions);
		for(Option option : listOption){
			//拼接colName
			if(colNamesByAql.length() > 0){
				colNamesByAql.append(",");
			}
			String colName = "amount_" + option.getValue().replace(".","__");
			colNamesByAql.append("{startColumnName: '" + colName + "', numberOfColumns: 3, titleText: '"+option.getName()+"'}");
			DynamicColumnDefinition dynamicFieldOptions =new DynamicColumnDefinition();
			dynamicFieldOptions.setColName("样本量");
			dynamicFieldOptions.setName(colName);
			dynamicFieldOptions.setVisible(true);
			dynamicFieldOptions.setColWidth("50");
			dynamicFieldOptions.setEditable(true);
			dynamicFieldOptions.setEditRules("number:true,min:0,required:true");
			dynamicFieldOptions.setEdittype(EditControlType.TEXT);
			dynamicFieldOptions.setType(DataType.TEXT);
			dynamicColumn.add(dynamicFieldOptions);
			
			colName = "ac_" + option.getValue().replace(".","__");
			dynamicFieldOptions =new DynamicColumnDefinition();
			dynamicFieldOptions.setColName("Ac");
			dynamicFieldOptions.setName(colName);
			dynamicFieldOptions.setVisible(true);
			dynamicFieldOptions.setColWidth("50");
			dynamicFieldOptions.setEditable(true);
			dynamicFieldOptions.setEditRules("number:true,min:0,required:true");
			dynamicFieldOptions.setEdittype(EditControlType.TEXT);
			dynamicFieldOptions.setType(DataType.TEXT);
			dynamicColumn.add(dynamicFieldOptions);

			colName = "re_" + option.getValue().replace(".","__");
			dynamicFieldOptions =new DynamicColumnDefinition();
			dynamicFieldOptions.setColName("Re");
			dynamicFieldOptions.setEditRules("number:true,min:0,required:true");
			dynamicFieldOptions.setName(colName);
			dynamicFieldOptions.setVisible(true);
			dynamicFieldOptions.setColWidth("50");
			dynamicFieldOptions.setEditable(true);
			dynamicFieldOptions.setEdittype(EditControlType.TEXT);
			dynamicFieldOptions.setType(DataType.TEXT);
			dynamicColumn.add(dynamicFieldOptions);
		}
		colNamesByAql.insert(0,"[").append("]");
		this.setGroupNames(colNamesByAql.toString());
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try {
			page = sampleSchemeManager.getListDatas(page,SampleCodeLetter.GB_TYPE,SampleScheme.RELAX_TYPE);
			this.renderText(PageUtils.dynamicPageToJson(page,new DynamicColumnValues(){
				public void addValuesTo(List<Map<String, Object>> result) {
					for(Map<String, Object> map : result){
						Long id = Long.valueOf(map.get("id").toString());
						SampleScheme sampleScheme = sampleSchemeManager.getSampleScheme(id);
						for(AcceptanceQualityLimit aql : sampleScheme.getAcceptanceQualityLimits()){
							String str = aql.getAql().replace(".","__");
							map.put("amount_" + str, aql.getAmount());
							map.put("ac_" + str, aql.getAc());
							map.put("re_" + str, aql.getRe());
						}
					}
					
				}
			}));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logUtilDao.debugLog("查询", "进货检验管理：物料抽样标准维护-放宽检验抽样维护");
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

}

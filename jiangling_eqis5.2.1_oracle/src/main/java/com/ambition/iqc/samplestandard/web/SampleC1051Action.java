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

import com.ambition.iqc.entity.SampleC1051CodeLetter;
import com.ambition.iqc.entity.SampleC1051Scheme;
import com.ambition.iqc.entity.SampleScheme;
import com.ambition.iqc.samplestandard.service.SampleC1051CodeLetterManager;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.mms.base.DynamicColumnValues;
import com.norteksoft.mms.base.utils.view.DynamicColumnDefinition;
import com.norteksoft.mms.form.enumeration.DataType;
import com.norteksoft.mms.form.enumeration.EditControlType;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 类名:1051抽样计划
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2013-5-30 发布
 */
@Namespace("/iqc/sample-standard/c1051")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "iqc/sample-standard/c1051", type = "redirectAction") })
public class SampleC1051Action extends CrudActionSupport<SampleC1051CodeLetter> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private SampleC1051CodeLetter sampleC1051CodeLetter;
	private Page<SampleC1051CodeLetter> page;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private SampleC1051CodeLetterManager sampleC1051CodeLetterManager;
	private List<DynamicColumnDefinition> dynamicColumn = new ArrayList<DynamicColumnDefinition>();
	
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

	public SampleC1051CodeLetter getSampleC1051CodeLetter() {
		return sampleC1051CodeLetter;
	}

	public void setSampleC1051CodeLetter(SampleC1051CodeLetter sampleC1051CodeLetter) {
		this.sampleC1051CodeLetter = sampleC1051CodeLetter;
	}

	public Page<SampleC1051CodeLetter> getPage() {
		return page;
	}

	public void setPage(Page<SampleC1051CodeLetter> page) {
		this.page = page;
	}

	@Override
	public SampleC1051CodeLetter getModel() {
		return sampleC1051CodeLetter;
	}

	public List<DynamicColumnDefinition> getDynamicColumn() {
		return dynamicColumn;
	}

	public void setDynamicColumn(List<DynamicColumnDefinition> dynamicColumn) {
		this.dynamicColumn = dynamicColumn;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			sampleC1051CodeLetter = new SampleC1051CodeLetter();
			sampleC1051CodeLetter.setCompanyId(ContextUtils.getCompanyId());
			sampleC1051CodeLetter.setCreatedTime(new Date());
			sampleC1051CodeLetter.setCreator(ContextUtils.getUserName());
			sampleC1051CodeLetter.setLastModifiedTime(new Date());
			sampleC1051CodeLetter.setLastModifier(ContextUtils.getUserName());
		}else{
			sampleC1051CodeLetter = sampleC1051CodeLetterManager.getSampleC1051CodeLetter(id);
		}
	}
	
	@Action("save")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			sampleC1051CodeLetter.setLastModifiedTime(new Date());
			sampleC1051CodeLetter.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", sampleC1051CodeLetter.toString());
		}else{
			logUtilDao.debugLog("保存", sampleC1051CodeLetter.toString());
		}
		try {
			JSONObject jsonObject = new JSONObject();
			@SuppressWarnings("unchecked")
			Map<String,String> paramMap = Struts2Utils.getRequest().getParameterMap();
			for(String key : paramMap.keySet()){
				if(key.startsWith("params.")){
					jsonObject.put(key.substring(7), Struts2Utils.getParameter(key));
				}
			}
			sampleC1051CodeLetterManager.saveSampleC1051CodeLetter(sampleC1051CodeLetter,jsonObject);
			jsonObject = JSONObject.fromObject(JsonParser.getRowValue(sampleC1051CodeLetter));
			for(SampleC1051Scheme scheme : sampleC1051CodeLetter.getSampleC1051Schemes()){
				jsonObject.put("params." + scheme.getAql(), scheme.getAmount());
			}
			jsonObject.put("id",sampleC1051CodeLetter.getId());
			this.renderText(jsonObject.toString());
		} catch (Exception e) {
			createErrorMessage("保存失败：" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		try{
			sampleC1051CodeLetterManager.deleteSampleC1051CodeLetter(deleteIds);
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
		String[] mitAqls = SampleScheme.getMit1051AQLs();
		String firstName = null;
		for(String colName : mitAqls){
			String name = "params." + colName;
			if(firstName==null){
				firstName = name;
			}
			DynamicColumnDefinition dynamicFieldOptions =new DynamicColumnDefinition();
			dynamicFieldOptions.setColName(colName);
			dynamicFieldOptions.setColWidth("100");
			dynamicFieldOptions.setName(name);
			dynamicFieldOptions.setVisible(true);
			dynamicFieldOptions.setEditable(true);
			dynamicFieldOptions.setEditRules("number:true,min:0");
			dynamicFieldOptions.setEdittype(EditControlType.TEXT);
			dynamicFieldOptions.setType(DataType.TEXT);
			dynamicColumn.add(dynamicFieldOptions);
		}
		colNamesByAql.append("[{startColumnName: 'batchSize1', numberOfColumns: 2, titleText: '批量'},");
		colNamesByAql.append("{startColumnName: '"+firstName+"', numberOfColumns: "+mitAqls.length+", titleText: '样本量'}]");
		ActionContext.getContext().put("groupHeaders", colNamesByAql);
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try {
			page = sampleC1051CodeLetterManager.getListDatas(page);
			this.renderText(PageUtils.dynamicPageToJson(page,new DynamicColumnValues(){
				public void addValuesTo(List<Map<String, Object>> result) {
					for(Map<String, Object> map : result){
						Long id = Long.valueOf(map.get("id").toString());
						SampleC1051CodeLetter codeLetter = sampleC1051CodeLetterManager.getSampleC1051CodeLetter(id);
						for(SampleC1051Scheme sampleScheme : codeLetter.getSampleC1051Schemes()){
							map.put("params." + sampleScheme.getAql(), sampleScheme.getAmount());
						}
					}
				}
			}));
		} catch (Exception e) {
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
	 * 转换json
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unused")
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

package com.ambition.spc.processdefine.web;

import java.util.ArrayList;
import java.util.Date;
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

import com.ambition.spc.entity.FeatureLayer;
import com.ambition.spc.entity.FeaturePerson;
import com.ambition.spc.entity.FeatureRules;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.processdefine.service.ProcessDefineManager;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**    
 * QualityFeatureActon.java
 * @authorBy YUKE
 *
 */
@Namespace("/spc/base-info/process-define")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "spc/base-info/process-define", type = "redirectAction") })
public class QualityFeatureAction extends com.ambition.product.base.CrudActionSupport<QualityFeature> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long processId;
	private String name;
	private Integer orderNum;
	private String position;
	private String deleteIds;//删除的编号 
	private QualityFeature qualityFeature;//质量特性
	private Page<QualityFeature> page;
 	private JSONObject params;
 	
 	@Autowired
 	private QualityFeatureManager qualityFeatureManager;
 	@Autowired
 	private ProcessDefineManager processDefineManager;
 	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getProcessId() {
		return processId;
	}
	
	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getOrderNum() {
		return orderNum;
	}
	
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	
	public String getPosition() {
		return position;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}
	
	public String getDeleteIds() {
		return deleteIds;
	}
	
	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}
	
	public QualityFeature getQualityFeature() {
		return qualityFeature;
	}
	
	public void setQualityFeature(QualityFeature qualityFeature) {
		this.qualityFeature = qualityFeature;
	}
	
	public Page<QualityFeature> getPage() {
		return page;
	}
	
	public void setPage(Page<QualityFeature> page) {
		this.page = page;
	}
	
	public JSONObject getParams() {
		return params;
	}
	
	public void setParams(JSONObject params) {
		this.params = params;
	}
	
	@Override
	public QualityFeature getModel() {
		return qualityFeature;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			qualityFeature = new QualityFeature();
			qualityFeature.setCreatedTime(new Date());
			qualityFeature.setCompanyId(ContextUtils.getCompanyId());
			qualityFeature.setCreator(ContextUtils.getUserName());
			qualityFeature.setModifiedTime(new Date());
			qualityFeature.setModifier(ContextUtils.getUserName());
			if(processId != null){
				qualityFeature.setProcessPoint(processDefineManager.getProcessPoint(processId));
			}
		}else {
			qualityFeature = qualityFeatureManager.getQualityFeature(id);
		}
	}
	
	@Action("quality-feature-datas")
	public String getQualityFeatureDatas() throws Exception {
		String processId = Struts2Utils.getParameter("processId");
		List<QualityFeature> qualityFeatures = null;
		if(StringUtils.isNotEmpty(processId)){
			qualityFeatures = processDefineManager.getProcessPoint(Long.valueOf(processId)).getQualityFeatures();
		}else if(id == null || id < 0){
//			qualityFeatures = qualityFeatureManager.getQualityFeatures();
		}
		List<Object> results = new ArrayList<Object>();
		if(qualityFeatures!=null){
			for(QualityFeature qualityFeature : qualityFeatures){
				results.add(qualityFeatureManager.convertQualityFeatureToMap(qualityFeature));
			}
		}
		
		renderText(JSONArray.fromObject(results).toString());
		return null;
	}
	
	@Action("save-quality-feature")
	public String saveQualityFeature() throws Exception {
		if(id != null){
			qualityFeature = qualityFeatureManager.getQualityFeature(id);
		}else{
			qualityFeature = new QualityFeature();
			qualityFeature.setCreatedTime(new Date());
			qualityFeature.setCompanyId(ContextUtils.getCompanyId());
			qualityFeature.setCreator(ContextUtils.getUserName());
		}
		if(processId != null){
			qualityFeature.setProcessPoint(processDefineManager.getProcessPoint(processId));
		}
		qualityFeature.setName(name);
		qualityFeature.setModifiedTime(new Date());
		qualityFeature.setModifier(ContextUtils.getUserName());
		try {
			qualityFeatureManager.saveQualityFeature(qualityFeature);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("id",qualityFeature.getId());
			map.put("orderNum",qualityFeature.getOrderNum());
			createMessage("操作成功!",map);
		} catch (Exception e) {
			e.printStackTrace();
			createErrorMessage("保存失败:" + e.getMessage());
		}
		return null;
	}
	
	@Action("save")
	public String save() throws Exception {
		if(id == null){
			createErrorMessage("请先选择某一质量特性！");
		}else{
			qualityFeature.setModifiedTime(new Date());
			qualityFeature.setModifier(ContextUtils.getUserName());
			try {
				qualityFeatureManager.saveQualityFeature(qualityFeature);
				createMessage("保存成功!");
			} catch (Exception e) {
				e.printStackTrace();
				createErrorMessage("保存失败:" + e.getMessage());
			}
		}
		return null;
	}
	
	@Action("delete-quality-feature")
	public String deleteQualityFeature() throws Exception {
		try {
			qualityFeatureManager.deleteQualityFeature(deleteIds);
			createMessage("删除成功!");
		} catch (Exception e) {
			createErrorMessage("删除失败:" + e.getMessage());
		}
		return null;
	}
	
	//判断准则
	@Action("rule-datas")
	public String getRuleDatas() throws Exception {
		try {
			if(id != null){
				qualityFeature = qualityFeatureManager.getQualityFeature(id);
				Page<FeatureRules> page = new Page<FeatureRules>(Integer.MAX_VALUE);
				page.setResult(qualityFeature.getFeatureRules());
				page.setPageNo(1);
				page.setTotalCount(page.getResult().size());
				page.setPageSize(Integer.MAX_VALUE);
				renderText(PageUtils.pageToJson(page,"SPC_FEATURE_RULES"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//层别信息
	@Action("level-datas")
	public String getLevelDatas() throws Exception {
		try {
			if(id != null){
				qualityFeature = qualityFeatureManager.getQualityFeature(id);
				Page<FeatureLayer> page = new Page<FeatureLayer>(Integer.MAX_VALUE);
				page.setResult(qualityFeature.getFeatureLayers());
				page.setPageNo(1);
				page.setTotalCount(page.getResult().size());
				page.setPageSize(Integer.MAX_VALUE);
				renderText(PageUtils.pageToJson(page,"SPC_FEATURE_LAYER"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//异常通知人员
	@Action("person-datas")
	public String getPersonDatas() throws Exception {
		try {
			if(id != null){
				qualityFeature = qualityFeatureManager.getQualityFeature(id);
				Page<FeaturePerson> page = new Page<FeaturePerson>(Integer.MAX_VALUE);
				page.setResult(qualityFeature.getFeaturePersons());
				page.setPageNo(1);
				page.setTotalCount(page.getResult().size());
				page.setPageSize(Integer.MAX_VALUE);
				renderText(PageUtils.pageToJson(page,"SPC_FEATURE_PERSON"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Action("list-qualityfeature-datas")
	public String getListQualityfeatureDatas() throws Exception {
		String monitPointId =Struts2Utils.getParameter("monitPointId");
		try {
			page= qualityFeatureManager.getPageByMoint(page, monitPointId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	
	/**
	  * 方法名: 复制特性
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("copy-quality-feature")
	public String copyQualityFeature() throws Exception {
		try {
			String returnMessage = qualityFeatureManager.copyQualityFeature(id,JSONArray.fromObject(Struts2Utils.getParameter("itemValues")));
			createMessage(returnMessage);
		} catch (Exception e) {
			log.error("复制质量特性失败!",e);
			createErrorMessage("复制失败:" + e.getMessage());
		}
		return null;
	}
	
	/*private void createMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}*/
	
	/*private void createMessage(String message,Object obj){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		map.put("obj",obj);
		renderText(JSONObject.fromObject(map).toString());
	}*/
}

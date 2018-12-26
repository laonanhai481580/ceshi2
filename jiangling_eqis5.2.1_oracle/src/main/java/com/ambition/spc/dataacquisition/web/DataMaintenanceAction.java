package com.ambition.spc.dataacquisition.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.dataacquisition.service.BatchMigrationManager;
import com.ambition.spc.dataacquisition.service.SpcSubGroupManager;
import com.ambition.spc.entity.LayerType;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.entity.SpcSgSample;
import com.ambition.spc.entity.SpcSgTag;
import com.ambition.spc.entity.SpcSubGroup;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**    
 * DataMaintenanceAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/spc/data-acquisition")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "spc/data-acquisition", type = "redirectAction") })
public class DataMaintenanceAction extends CrudActionSupport<SpcSubGroup> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private SpcSubGroup spcSubGroup;
	private Page<SpcSubGroup> page;
	private Page<Object> featurePage;
	private JSONObject params;
	private LayerType layerType;
	private QualityFeature qualityFeature;
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private SpcSubGroupManager spcSubGroupManager;  
	@Autowired
	private BatchMigrationManager batchMigrationManager;
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

	public SpcSubGroup getSpcSubGroup() {
		return spcSubGroup;
	}

	public void setSpcSubGroup(SpcSubGroup spcSubGroup) {
		this.spcSubGroup = spcSubGroup;
	}

	public Page<SpcSubGroup> getPage() {
		return page;
	}

	public void setPage(Page<SpcSubGroup> page) {
		this.page = page;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public LayerType getLayerType() {
		return layerType;
	}

	public void setLayerType(LayerType layerType) {
		this.layerType = layerType;
	}

	public QualityFeature getQualityFeature() {
		return qualityFeature;
	}

	public void setQualityFeature(QualityFeature qualityFeature) {
		this.qualityFeature = qualityFeature;
	}

	@Override
	public SpcSubGroup getModel() {
		return spcSubGroup;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			spcSubGroup = new SpcSubGroup();
			spcSubGroup.setCompanyId(ContextUtils.getCompanyId());
			spcSubGroup.setCreator(ContextUtils.getUserName());
			spcSubGroup.setCreatedTime(new Date());
			spcSubGroup.setModifier(ContextUtils.getUserName());
			spcSubGroup.setModifiedTime(new Date());
		}else{
			spcSubGroup = spcSubGroupManager.getSpcSubGroup(id);
		}
	}

	@Action("maintenance-delete")
	@Override
	public String delete() throws Exception {
		JSONObject result = new JSONObject();
		try {
			String groupIds = Struts2Utils.getParameter("groupIds");
			String dataIds = Struts2Utils.getParameter("dataIds");
			Long featureId = Long.valueOf(Struts2Utils.getParameter("featureId"));
			spcSubGroupManager.deleteSpcSubGroup(groupIds,dataIds,featureId);
		} catch (Exception e) {
			log.error("删除特性数据失败!",e);
			result.put("error",true);
			result.put("message", "删除特性数据失败,"+e.getMessage());
		}
		renderText(result.toString());
		return null;
	}

	/**
	 * 子组信息修改页面
	 */
	@Action("maintenance-input")
	@Override
	public String input() throws Exception {
		Long featureId = Long.valueOf(Struts2Utils.getParameter("featureId"));
		QualityFeature feature = qualityFeatureManager.getQualityFeatureFromCache(featureId,null);
		ActionContext.getContext().put("featureLayers",feature.getFeatureLayers());
		
		String groupId = Struts2Utils.getParameter("groupId");
		if(CommonUtil1.isInteger(groupId)){
			spcSubGroup = spcSubGroupManager.getSpcSubGroup(Long.valueOf(groupId));
			qualityFeature = spcSubGroup.getQualityFeature();
			Double sum=0.0,avg=0.0;
			DecimalFormat df = new DecimalFormat("#.00");
			if(spcSubGroup.getSpcSgSamples()!=null&&spcSubGroup.getSpcSgSamples().size()!=0){
				for(SpcSgSample sample:spcSubGroup.getSpcSgSamples()){
					sum += sample.getSamValue()==null?0.0:sample.getSamValue();
				}
				avg = sum/spcSubGroup.getSpcSgSamples().size();
			}
			ActionContext.getContext().put("spcSubGroup",spcSubGroup);
			ActionContext.getContext().put("qualityFeature",qualityFeature);
			ActionContext.getContext().put("sigma",df.format(avg));
			
			List<SpcSgTag> sgTagList = spcSubGroup.getSpcSgTags();
			List<Map<String,Object>> layerItems = new ArrayList<Map<String,Object>>();
			for(SpcSgTag sgTag : sgTagList){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("tagName", sgTag.getTagName());
				map.put("tagCode",sgTag.getTagCode());
				map.put("tagValue", sgTag.getTagValue());
				layerItems.add(map);
			}
			ActionContext.getContext().put("layerItems",layerItems);
		}else{
			String dataIds = Struts2Utils.getParameter("dataIds");
			List<Map<String,Object>> dataMaps = spcSubGroupManager.queryDataValues(feature, dataIds);
			ActionContext.getContext().put("dataMaps", dataMaps);
			ActionContext.getContext().put("qualityFeature",feature);
		}
		return SUCCESS;
	}
	
	@Action("maintenance-save")
	@Override
	public String save() throws Exception {
//		params = spcSubGroupManager.convertJsonObject(params);
//		String featureId = Struts2Utils.getParameter("featureId");
//		if(id != null && id != 0){
//			spcSubGroup.setModifiedTime(new Date());
//			spcSubGroup.setModifier(ContextUtils.getUserName());
//			spcSubGroup.getSpcSgSamples().clear();
//			spcSubGroup.getSpcSgTags().clear();
//		}else{
//			spcSubGroup.setSpcSgSamples(new ArrayList<SpcSgSample>());
//			spcSubGroup.setSpcSgTags(new ArrayList<SpcSgTag>());
//		}
//		if(featureId != null && !featureId.isEmpty()){
//			qualityFeature  = qualityFeatureManager.getQualityFeature(Long.valueOf(featureId));
//			spcSubGroup.setQualityFeature(qualityFeature);
//		}
//		for(Object key : params.keySet()){
//			if(key.toString().indexOf("_")==-1){
//				if(StringUtils.isNotEmpty(params.getString(key+""))){
//					//样本数据
//					if(key.toString().indexOf("sample")>=0){
//						SpcSgSample sample = new SpcSgSample();
//						sample.setCompanyId(ContextUtils.getCompanyId());
//						sample.setCreatedTime(new Date(System.currentTimeMillis()));
//						sample.setCreator(ContextUtils.getUserName());
//						sample.setModifiedTime(new Date(System.currentTimeMillis()));
//						sample.setModifier(ContextUtils.getUserName());
//						sample.setSampleNo(params.getString(key+""));
//						if(params.containsKey(key + "_value") && params.getString(key + "_value") != null){
//							sample.setSamValue(params.getDouble(key + "_value"));
//						}
//						sample.setSpcSubGroup(spcSubGroup);
//						spcSubGroup.getSpcSgSamples().add(sample);
//					}
//					//层别信息
//					if(key.toString().indexOf("layer")>=0){
//						SpcSgTag sgTag = new SpcSgTag();
//						sgTag.setCompanyId(ContextUtils.getCompanyId());
//						sgTag.setCreatedTime(new Date(System.currentTimeMillis()));
//						sgTag.setCreator(ContextUtils.getUserName());
//						sgTag.setModifiedTime(new Date(System.currentTimeMillis()));
//						sgTag.setModifier(ContextUtils.getUserName());
//						sgTag.setTagName(params.getString(key + ""));
//						if(params.containsKey(key + "_lvalue") && params.getString(key + "_lvalue") != null){
//							sgTag.setTagValue(params.getString(key + "_lvalue"));
//						}
//						if(params.containsKey(key + "_code") && params.getString(key + "_code") != null){
//							sgTag.setTagCode(params.getString(key + "_code"));
//						}
//						if(params.containsKey(key + "_method") && params.getString(key + "_method") != null){
//							sgTag.setMethod(params.getString(key + "_method"));
//						}
//						if(params.containsKey(key + "_code") && params.containsKey(key + "_lvalue")){
//							spcSubGroupManager.setLayerInfo(spcSubGroup,params.getString(key + "_code"),params.getString(key + "_lvalue"));
//						}
//						sgTag.setSpcSubGroup(spcSubGroup);	
//						spcSubGroup.getSpcSgTags().add(sgTag);
//					}
//				}
//			}
//		}
		JSONObject result = new JSONObject();
		try {
			List<JSONObject> dataItems = CommonUtil1.getRequestCheckItems("flags");
			Long featureId = Long.valueOf(Struts2Utils.getParameter("featureId"));
			spcSubGroupManager.saveForDataItems(dataItems,featureId);
		} catch (Exception e) {
			log.error("修改数据失败!",e);
			result.put("error",true);
			result.put("message","保存失败," + e.getMessage());
		}
		this.renderText(result.toString());
		return null;
	}

	/**
	 * 数据维护 页面
	 */
	@Action("maintenance-list")
	@Override
	public String list() throws Exception {
		//质量特性 
		List<QualityFeature> qualityFeatures = qualityFeatureManager.getList();
		ActionContext.getContext().put("qualityFeatures",spcSubGroupManager.convertListToOptions(qualityFeatures));
//		StringBuffer colModelSb = new StringBuffer("");
//		colModelSb.append("[{label:'id',name:'id',index:'id',hidden:'true'},");
//		colModelSb.append("{label:'质量特性',name:'name',index:'name',width:120},");
//		colModelSb.append("{label:'子组号',name:'subGroupOrderNum',index:'subGroupOrderNum',width:90},");
//		colModelSb.append("{label:'采集时间',name:'createdTime',index:'createdTime',width:100},");
//		colModelSb.append("{label:'均值',name:'sigma',index:'sigma',width:90},");
//		colModelSb.append("{label:'最大值',name:'maxValue',index:'maxValue',width:90},");
//		colModelSb.append("{label:'最小值',name:'minValue',index:'minValue',width:90},");
//		colModelSb.append("{label:'极差',name:'rangeDiff',index:'rangeDiff',width:90}]");
//		ActionContext.getContext().put("colModel",colModelSb.toString());
		return SUCCESS;
	}
	
	/**
	 * 数据维护 列表数据
	 */
	@Action("maintenance-list-datas")
	public String maintenanceListDatas() throws Exception {
		try{
			JSONObject	params1 = convertJsonObject(params);
			Long featureId = 0l;
			if(params1.containsKey("qualityFeatures") && params1.getString("qualityFeatures") != null){
				featureId = Long.valueOf(params1.getString("qualityFeatures"));
			}
			JSONObject pageJson = spcSubGroupManager.searchSpcGroups(page, params, featureId);
			this.renderText(pageJson.toString());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		if(params == null){
			return resultJson;
		}else{
			for(Object key : params.keySet()){
				resultJson.put(key,params.getJSONArray(key.toString()).get(0));
			}
			return resultJson;
		}
	}
	/**
	  * 方法名:左边质量特性列表数据
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("feature-list-datas")
	public String featureListDatas() throws Exception {
		try{
			JSONObject pageJson = qualityFeatureManager.queryQualityFeatures(featurePage,Struts2Utils.getParameter("featureName"));
			this.renderText(pageJson.toString());
		}catch (Exception e) {
			log.error("查询左边质量特性列表数据出错",e);
			renderText("查询左边质量特性列表数据出错," + e.getMessage());
		}
		return null;
	}
	
	/**
	  * 方法名:右边质量特性列表数据
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("spcgroup-list-datas")
	public String spcGroupListDatas() throws Exception {
		try{
			String featureIdStr = Struts2Utils.getParameter("featureId");
			Long featureId = 0l;
			if(CommonUtil1.isInteger(featureIdStr)){
				featureId = Long.valueOf(featureIdStr);
			}
			JSONObject pageJson = spcSubGroupManager.searchSpcGroups(page, params, featureId);
			this.renderText(pageJson.toString());
		}catch (Exception e) {
			log.error("查询数据出错",e);
			renderText("查询数据出错," + e.getMessage());
		}
		return null;
	}
	
	/**
	  * 方法名: 导出文件
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("export-maintenance-list")
	public String exportMaintenanceListDatas() throws Exception {
		JSONObject result = new JSONObject();
		try{
			String featureIdStr = Struts2Utils.getParameter("featureId");
			Long featureId = 0l;
			if(CommonUtil1.isInteger(featureIdStr)){
				featureId = Long.valueOf(featureIdStr);
			}
			String resultStr = spcSubGroupManager.exportDatas(params,featureId);
			result.put("fileName",resultStr);
		}catch (Exception e) {
			result.put("error",true);
			result.put("message",e.getMessage());
			log.error("导出SPC数据失败!",e);
		}
		this.renderText(result.toString());
		return null;
	}
	
	/**
	  * 方法名:批量迁移数据
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("batch-migration")
	public String batchMigration() throws Exception {
		JSONObject result = new JSONObject();
		try{
			String executeFlag = batchMigrationManager.beginMigration(Struts2Utils.getParameter("groupIds"));
			result.put("executeFlag",executeFlag);
		}catch (Exception e) {
			result.put("error",true);
			result.put("message",e.getMessage());
			log.error("导出SPC数据失败!",e);
		}
		this.renderText(result.toString());
		return null;
	}
	
	/**
	  * 方法名:批量迁移数据
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("query-batch-migration-status")
	public String queryBatchMigration() throws Exception {
		JSONObject result = batchMigrationManager.queryCurrentStatus(Struts2Utils.getParameter("executeFlag"));
		this.renderText(result.toString());
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

	public Page<Object> getFeaturePage() {
		return featurePage;
	}

	public void setFeaturePage(Page<Object> featurePage) {
		this.featurePage = featurePage;
	}
}

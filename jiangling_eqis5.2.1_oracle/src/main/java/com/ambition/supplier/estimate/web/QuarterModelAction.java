package com.ambition.supplier.estimate.web;

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

import com.ambition.supplier.entity.EstimateModel;
import com.ambition.supplier.entity.EvaluatingIndicator;
import com.ambition.supplier.entity.ModelIndicator;
import com.ambition.supplier.estimate.service.EstimateModelManager;
import com.ambition.supplier.estimate.service.EvaluatingIndicatorManager;
import com.ambition.supplier.estimate.service.ModelIndicatorManager;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.bs.options.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;


@Namespace("/supplier/estimate/model/quarter")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/estimate/model/quarter", type = "redirectAction") })
public class QuarterModelAction  extends CrudActionSupport<EstimateModel> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long estimateModelId;//评价模型编号
	private Long nodeid;
	private Long parentId;
	private String expandIds;
	private String isEstimate;//是否评价
	private String deleteIds;//删除的编号 
	private JSONObject params;//模型指标对象
	private EstimateModel estimateModel;
	
	@Autowired 
	private LogUtilDao logUtilDao;

	@Autowired
	private EstimateModelManager estimateModelManager;
 	
 	@Autowired
 	private EvaluatingIndicatorManager evaluatingIndicatorManager;
 	
 	@Autowired
 	private ModelIndicatorManager modelIndicatorManager;

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}
	
	public String getExpandIds() {
		return expandIds;
	}

	public void setExpandIds(String expandIds) {
		this.expandIds = expandIds;
	}
	
	public String getIsEstimate() {
		return isEstimate;
	}

	public void setIsEstimate(String isEstimate) {
		this.isEstimate = isEstimate;
	}

	public Long getEstimateModelId() {
		return estimateModelId;
	}

	public void setEstimateModelId(Long estimateModelId) {
		this.estimateModelId = estimateModelId;
	}

	public Long getNodeid() {
		return nodeid;
	}

	public void setNodeid(Long nodeid) {
		this.nodeid = nodeid;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	public EstimateModel getModel() {
		return estimateModel;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			estimateModel = new EstimateModel();
			estimateModel.setCreatedTime(new Date());
			estimateModel.setCompanyId(ContextUtils.getCompanyId());
			estimateModel.setCreator(ContextUtils.getUserName());
			estimateModel.setModifiedTime(new Date());
			estimateModel.setModifier(ContextUtils.getUserName());
			estimateModel.setBusinessUnitName(ContextUtils.getSubCompanyName());
			estimateModel.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
			if(parentId != null){
				EstimateModel parent = estimateModelManager.getEstimateModel(parentId);
				if(parent != null){
					estimateModel.setParent(parent);
					estimateModel.setLevel(parent.getLevel()+1);
					estimateModel.setParentEstimateModelId(parent.getParentEstimateModelId()==null?parent.getId():parent.getParentEstimateModelId());
				}
			}
		}else {
			estimateModel = estimateModelManager.getEstimateModel(id);
			if(estimateModel.getParent()!=null){
				estimateModel.setParentEstimateModelId(estimateModel.getParent().getParentEstimateModelId()==null?estimateModel.getParent().getId():estimateModel.getParent().getParentEstimateModelId());
			}
		}
	}
	
	@SuppressWarnings("static-access")
	@Action("input")
	@Override
	public String input() throws Exception {
		//周期
		ActionContext.getContext().put("modelCycles",estimateModel.getCycleOptionsForSelect());
		
		//开始月份
		List<Option> options = new ArrayList<Option>();
		for(int i=1;i<13;i++){
			Option option = new Option();
			option.setName(i+"");
			option.setValue(i+"");
			options.add(option);
		}
		ActionContext.getContext().put("startMonths",options);
		return SUCCESS;
	}
	@Action("save")
	public String save() throws Exception {
		try {
			if(id == null){
				estimateModelManager.saveEstimateModel(estimateModel);
				logUtilDao.debugLog("保存", estimateModel.toString());
			}else{
				if(estimateModel != null){
					estimateModel.setModifiedTime(new Date());
					estimateModel.setModifier(ContextUtils.getUserName());
					estimateModelManager.saveEstimateModel(estimateModel);
					logUtilDao.debugLog("修改", estimateModel.toString());
				}else{
					throw new RuntimeException("保存失败,评价模型为空!");
				}
			}
			renderText("{\"id\":"+estimateModel.getId()+"}");
		} catch (Exception e) {
			createErrorMessage("保存失败：" + e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				estimateModelManager.deleteEstimateModel(deleteIds);
				createMessage("删除成功!");
			} catch (Exception e) {
				createErrorMessage("删除失败");
			}
		}
		return null;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Action("list-datas")
	public String getEstimateModelByParent() throws Exception {
		try {
			Page page = new Page();
			List<EstimateModel> parents = null;
			if(nodeid == null){
				parents = estimateModelManager.getTopEstimateModels();
			}else{
				EstimateModel parent = estimateModelManager.getEstimateModel(nodeid);
				if(parent != null){
					parents = parent.getChildren();
				}else{
					parents = new ArrayList<EstimateModel>();
				}
			}
			Map<String,Boolean> expandMap = new HashMap<String, Boolean>();
			if(StringUtils.isNotEmpty(expandIds)){
				String[] ids = expandIds.split(",");
				for(String id : ids){
					if(StringUtils.isNotEmpty(id)){
						expandMap.put(id,true);
					}
				}
			}
			//获取所有的评价模型
			List<ModelIndicator> modelIndicators = modelIndicatorManager.getAllModelIndicators();
			Map<EstimateModel,List<ModelIndicator>> modelMap = new HashMap<EstimateModel, List<ModelIndicator>>();
			List<EstimateModel> models = new ArrayList<EstimateModel>();
			for(ModelIndicator modelIndicator : modelIndicators){
				if(!EstimateModel.STATE_ISUSE.equals(modelIndicator.getEstimateModel().getState())
						||!modelIndicator.getEstimateModel().getChildren().isEmpty()
						||!modelIndicator.getEvaluatingIndicator().getChildren().isEmpty()
						||modelIndicator.getTotalPoints()==null){
					continue;
				}
				List<ModelIndicator> indicators = null;
				if(modelMap.containsKey(modelIndicator.getEstimateModel())){
					indicators = modelMap.get(modelIndicator.getEstimateModel());
				}else{
					indicators = new ArrayList<ModelIndicator>();
					modelMap.put(modelIndicator.getEstimateModel(),indicators);
				}
				indicators.add(modelIndicator);
				models.add(modelIndicator.getEstimateModel());
			}
			//分别递归出所有对应的指标
			for(EstimateModel model : models){
				if(model.getParent() != null){
					sumTotalPoints(model.getParent(), modelMap.get(model),modelMap);
				}
			}
			//计算总分
			Map<Long,Double> allTotalMap = new HashMap<Long, Double>();
			for(EstimateModel model : modelMap.keySet()){
				double totalPoints = 0.0;
				for(ModelIndicator modelIndicator : modelMap.get(model)){
					totalPoints += modelIndicator.getTotalPoints();
				}
				allTotalMap.put(model.getId(),totalPoints);
			}
			
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(EstimateModel estimateModel : parents){
				convertEstimateModel(estimateModel,list,allTotalMap,expandMap);
			}
			page.setResult(list);
//			renderText(PageUtils.pageToJson(page));
			String result = evaluatingIndicatorManager.getResultJson(page);
			renderText(JSONObject.fromObject(result).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		logUtilDao.debugLog("查询", "供应商质量管理：基础维护-评价模型维护");
		return null;
	}
	
	/**
	 * 保存复制的模型
	 * @return
	 * @throws Exception
	 */
	@Action("save-copy-model")
	public String saveCopyModel() throws Exception {
		try {
			estimateModelManager.saveCopyModel(params);
			createMessage("保存成功!");
		} catch (Exception e) {
			createErrorMessage("保存失败:" + e.getMessage());
		}
		return null;
	}
	
	private void sumTotalPoints(EstimateModel parent,List<ModelIndicator> childIndicators,Map<EstimateModel,List<ModelIndicator>> allTotalPointMap){
		List<ModelIndicator> modelIndicators = null;
		if(allTotalPointMap.containsKey(parent)){
			modelIndicators = allTotalPointMap.get(parent);
		}else{
			modelIndicators = new ArrayList<ModelIndicator>();
			allTotalPointMap.put(parent, modelIndicators);
		}
		for(ModelIndicator childIndicator : childIndicators){
			if(!modelIndicators.contains(childIndicator)){
				modelIndicators.add(childIndicator);
			}
		}
		if(parent.getParent() != null){
			sumTotalPoints(parent.getParent(),modelIndicators,allTotalPointMap);
		}
	}
	
	/**
	 * 编辑评价指标
	 * @return
	 * @throws Exception
	 */
	@Action("edit-indicator")
	public String editIndicator() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 编辑评价指标的数据
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Action("edit-indicator-datas")
	public String editIndicatorDatas() throws Exception {
		estimateModel = estimateModelManager.getEstimateModel(estimateModelId);
		Page page = new Page();
		if(estimateModel == null){
			renderText(PageUtils.pageToJson(page));
		}else{
			//所有已设置的评价指标
			List<ModelIndicator> modelIndicators = modelIndicatorManager.getAllModelIndicators(estimateModel.getParentEstimateModelId() == null?estimateModel.getId():estimateModel.getParentEstimateModelId());
			Map<Long,ModelIndicator> selfModelIndicatorMap = new HashMap<Long, ModelIndicator>();
			Map<Long,Boolean> otherModelIndicatorMap = new HashMap<Long, Boolean>();
			for(ModelIndicator modelIndicator : modelIndicators){
				EstimateModel model = modelIndicator.getEstimateModel();
				EvaluatingIndicator indicator = modelIndicator.getEvaluatingIndicator();
				if(!EstimateModel.STATE_ISUSE.equals(model.getState())
						||!model.getChildren().isEmpty()
						||!indicator.getChildren().isEmpty()){
					continue;
				}
				if(estimateModel.getId().toString().equals(model.getId().toString())){
					selfModelIndicatorMap.put(indicator.getId(),modelIndicator);
				}else{
					otherModelIndicatorMap.put(indicator.getId(), true);
				}
			}
			
			List<EvaluatingIndicator> parents = null;
			if(nodeid == null){
				parents = evaluatingIndicatorManager.getTopEvaluatingIndicators();
			}else{
				EvaluatingIndicator parent = evaluatingIndicatorManager.getEvaluatingIndicator(nodeid);
				if(parent != null){
					parents = parent.getChildren();
				}else{
					parents = new ArrayList<EvaluatingIndicator>();
				}
			}
			
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(EvaluatingIndicator evaluatingIndicator : parents){
				convertEvaluatingIndicator(evaluatingIndicator,list,selfModelIndicatorMap,otherModelIndicatorMap);
			}
			page.setResult(list);
			renderText(PageUtils.pageToJson(page));
		}
		return null;
	}
	
	/**
	 * 保存编辑的评价指标
	 * @return
	 * @throws Exception
	 */
	@Action("save-indicator")
	public String saveIndicator() throws Exception {
		try{
			EstimateModel estimateModel = estimateModelManager.getEstimateModel(estimateModelId);
			EvaluatingIndicator evaluatingIndicator = evaluatingIndicatorManager.getEvaluatingIndicator(id);
			if(isEstimate != null){
				modelIndicatorManager.setModelIndicator(estimateModel, evaluatingIndicator, isEstimate,params);
				createMessage("保存成功！");
			}else{
				modelIndicatorManager.saveModelIndicator(estimateModel, evaluatingIndicator, params);
				createMessage("保存成功！");
			}
		}catch (Exception e) {
			e.printStackTrace();
			createErrorMessage("保存失败:" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 转换物料结构至json对象
	 * @param estimateModel
	 * @return
	 */
	private void convertEstimateModel(EstimateModel estimateModel,List<Map<String,Object>> list,Map<Long,Double> allTotalMap,Map<String,Boolean> expandMap){
		Boolean isLeaf = estimateModel.getChildren().isEmpty();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id",estimateModel.getId());
		map.put("name",estimateModel.getName());
		map.put("remark",estimateModel.getRemark());
		map.put("level",estimateModel.getLevel()-1);
		map.put("parent",estimateModel.getParent()==null?"":estimateModel.getParent().getId());
		map.put("isLeaf",isLeaf);
		map.put("totalPoints",allTotalMap.get(estimateModel.getId()));
		list.add(map);
		if(!isLeaf){
			if(expandMap.containsKey(estimateModel.getId().toString())){
				map.put("expanded",true);
				map.put("loaded",true);
				expandMap.remove(estimateModel.getId().toString());
				for(EstimateModel child : estimateModel.getChildren()){
					convertEstimateModel(child,list,allTotalMap,expandMap);
				}
			}else{
				map.put("expanded",false);
				map.put("loaded",false);
			}
		}else{
			map.put("cycle",estimateModel.getCycle());
			map.put("startMonth",estimateModel.getStartMonth());
			map.put("loaded",true);
		}
	}
	/**
	 * 转换物料结构至json对象
	 * @param evaluatingIndicator
	 * @return
	 */
	private void convertEvaluatingIndicator(EvaluatingIndicator evaluatingIndicator,List<Map<String,Object>> list,Map<Long,ModelIndicator> selfModelIndicatorMap,Map<Long,Boolean> otherModelIndicatorMap){
		Boolean isLeaf = evaluatingIndicator.getChildren().isEmpty();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id",evaluatingIndicator.getId());
		map.put("name",evaluatingIndicator.getName());
		map.put("level",evaluatingIndicator.getLevel()-1);
		map.put("parent",evaluatingIndicator.getParent()==null?"":evaluatingIndicator.getParent().getId());
		map.put("isLeaf",isLeaf);
		list.add(map);
		if(!isLeaf){
			map.put("expanded",true);
			map.put("loaded",true);
			for(EvaluatingIndicator child : evaluatingIndicator.getChildren()){
				convertEvaluatingIndicator(child,list,selfModelIndicatorMap,otherModelIndicatorMap);
			}
		}else{
			map.put("loaded",true);
			if(selfModelIndicatorMap.containsKey(evaluatingIndicator.getId())){
				ModelIndicator modelIndicator = selfModelIndicatorMap.get(evaluatingIndicator.getId());
				map.put("params.totalPoints",modelIndicator.getTotalPoints());
				map.put("params.levela",modelIndicator.getLevela());
				map.put("params.levelb",modelIndicator.getLevelb());
				map.put("params.levelc",modelIndicator.getLevelc());
				map.put("params.leveld",modelIndicator.getLeveld());
				map.put("params.levele",modelIndicator.getLevele());
//				if(modelIndicator.getLevelaMax()!=null||modelIndicator.getLevelaMin()!=null){
//					if(modelIndicator.getLevelaMax()==modelIndicator.getLevelaMin()){
//						map.put("params.levela",modelIndicator.getLevelaMin() + "-");
//					}else{
//						map.put("params.levela",modelIndicator.getLevelaMin() + "-" + modelIndicator.getLevelaMax());
//					}
//				}
//				if(modelIndicator.getLevelbMax()!=null||modelIndicator.getLevelbMin()!=null){
//					if(modelIndicator.getLevelbMax()==modelIndicator.getLevelbMin()){
//						map.put("params.levelb",modelIndicator.getLevelbMin() + "-");
//					}else{
//						map.put("params.levelb",modelIndicator.getLevelbMin() + "-" + modelIndicator.getLevelbMax());
//					}
//				}
//				if(modelIndicator.getLevelcMax()!=null||modelIndicator.getLevelcMin()!=null){
//					if(modelIndicator.getLevelcMax()==modelIndicator.getLevelcMin()){
//						map.put("params.levelc",modelIndicator.getLevelcMin() + "-");
//					}else{
//						map.put("params.levelc",modelIndicator.getLevelcMin() + "-" + modelIndicator.getLevelcMax());
//					}
//				}
//				if(modelIndicator.getLeveldMax()!=null||modelIndicator.getLeveldMin()!=null){
//					if(modelIndicator.getLeveldMax()==modelIndicator.getLeveldMin()){
//						map.put("params.leveld",modelIndicator.getLeveldMin() + "-");
//					}else{
//						map.put("params.leveld",modelIndicator.getLeveldMin() + "-" + modelIndicator.getLeveldMax());
//					}
//				}
//				if(modelIndicator.getLeveleMax()!=null||modelIndicator.getLeveleMin()!=null){
//					if(modelIndicator.getLeveleMax()==modelIndicator.getLeveleMin()){
//						map.put("params.levele",modelIndicator.getLeveleMin() + "-");
//					}else{
//						map.put("params.levele",modelIndicator.getLeveleMin() + "-" + modelIndicator.getLeveleMax());
//					}
//				}
				map.put("params.remark",modelIndicator.getRemark());
				map.put("canUse","yes");
				map.put("isEstimate","yes");
			}else{
				if(otherModelIndicatorMap.containsKey(evaluatingIndicator.getId())){
					map.put("canUse","no");
					map.put("isEstimate","yes");
				}else{
					map.put("canUse","yes");
					map.put("isEstimate","no");
				}
			}
		}
	}
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
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
}

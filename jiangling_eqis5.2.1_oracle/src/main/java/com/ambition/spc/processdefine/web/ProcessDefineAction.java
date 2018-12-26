package com.ambition.spc.processdefine.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.entity.ProcessPoint;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.processdefine.service.ProcessDefineManager;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.mms.base.utils.view.GridColumnInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**    
 * ProcessDefineAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/spc/base-info/process-define")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "spc/base-info/process-define", type = "redirectAction") })
public class ProcessDefineAction extends com.ambition.product.base.CrudActionSupport<ProcessPoint> {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long nodeid;
	private Long parentId;
	private Long structureId;//结构编号
	private String expandIds;//刷新时展开的节点
	private String deleteIds;
	private Page<ProcessPoint> page;
	private ProcessPoint processPoint;
	private QualityFeature qualityFeature;
	@Autowired
	private AcsUtils acsUtils;
 	@Autowired
	private ProcessDefineManager processDefineManager;
 	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private LogUtilDao logUtilDao;
 	private GridColumnInfo judgeGridColumnInfo;
 	private GridColumnInfo levelGridColumnInfo;
 	private GridColumnInfo personGridColumnInfo;
 	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNodeid() {
		return nodeid;
	}

	public void setNodeid(Long nodeid) {
		this.nodeid = nodeid;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getStructureId() {
		return structureId;
	}

	public void setStructureId(Long structureId) {
		this.structureId = structureId;
	}

	public String getExpandIds() {
		return expandIds;
	}

	public void setExpandIds(String expandIds) {
		this.expandIds = expandIds;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public Page<ProcessPoint> getPage() {
		return page;
	}

	public void setPage(Page<ProcessPoint> page) {
		this.page = page;
	}

	public ProcessPoint getProcessPoint() {
		return processPoint;
	}

	public void setProcessPoint(ProcessPoint processPoint) {
		this.processPoint = processPoint;
	}
	
	public QualityFeature getQualityFeature() {
		return qualityFeature;
	}

	public void setQualityFeature(QualityFeature qualityFeature) {
		this.qualityFeature = qualityFeature;
	}

	public GridColumnInfo getJudgeGridColumnInfo() {
		return judgeGridColumnInfo;
	}

	public void setJudgeGridColumnInfo(GridColumnInfo judgeGridColumnInfo) {
		this.judgeGridColumnInfo = judgeGridColumnInfo;
	}

	public GridColumnInfo getLevelGridColumnInfo() {
		return levelGridColumnInfo;
	}

	public void setLevelGridColumnInfo(GridColumnInfo levelGridColumnInfo) {
		this.levelGridColumnInfo = levelGridColumnInfo;
	}

	public GridColumnInfo getPersonGridColumnInfo() {
		return personGridColumnInfo;
	}

	public void setPersonGridColumnInfo(GridColumnInfo personGridColumnInfo) {
		this.personGridColumnInfo = personGridColumnInfo;
	}

	public ProcessPoint getModel() {
		return processPoint;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			processPoint = new ProcessPoint();
			processPoint.setCreatedTime(new Date());
			processPoint.setCompanyId(ContextUtils.getCompanyId());
			processPoint.setCreator(ContextUtils.getUserName());
			processPoint.setEditer(ContextUtils.getUserName());
			processPoint.setModifiedTime(new Date());
			processPoint.setModifier(ContextUtils.getUserName());
			processPoint.setQualityFeatures(new ArrayList<QualityFeature>());
			if(parentId != null){
				ProcessPoint parentPoint = processDefineManager.getProcessPoint(parentId);
				if(parentPoint != null){
					processPoint.setParent(parentPoint);
					processPoint.setLevel(parentPoint.getLevel()+1);
				}
			}
		}else {
			processPoint = processDefineManager.getProcessPoint(id);
		}
	}
	
	@SuppressWarnings("unused")
	@Action("copy-product")
	public String copuProduct() throws Exception {
		prepareModel();
		String copyId = Struts2Utils.getParameter("copyId");
		ProcessPoint copyPoint = processDefineManager.getProcessPoint(Long.valueOf(copyId));
		ProcessPoint parentPoint = copyPoint.getParent();
		parentId = parentPoint.getId();
		ActionContext.getContext().put("copyId",copyId);
		if(id==null&&parentId!=null){
			if(parentPoint == null){
				addActionMessage("父级物料为空!");
			}else{
				processPoint.setParent(parentPoint);
			}
		}else if(parentId==null){
			ProcessPoint parent = new ProcessPoint();
			parent.setName("产品列表");
			processPoint.setParent(parent);
		}
		ActionContext.getContext().getValueStack().push(processPoint);
		return SUCCESS;
	}	

	@Action("copy-point-save")
	public String copyPointSave() throws Exception {
		prepareModel();
		if(id == null){
			ProcessPoint parentPoint = null;
			processPoint.setCode(Struts2Utils.getParameter("code"));
			processPoint.setName(Struts2Utils.getParameter("name"));
			processPoint.setRemark(Struts2Utils.getParameter("remark"));
			Long copyId = Long.valueOf(Struts2Utils.getParameter("copyId"));
			if(parentId != null){
				parentPoint = processDefineManager.getProcessPoint(parentId);
				processPoint.setParent(parentPoint);
				processPoint.setLevel(parentPoint.getLevel() + 1);
				ProcessPoint copyPoint = processDefineManager.getProcessPoint(copyId);
				if(copyPoint.getChildren().size()>0){
					addActionMessage("只能复制最后一级机种!");
					return "input";
				}else{
					processDefineManager.copyPointInfo(processPoint,copyPoint);
				}
			}
			try{
				logUtilDao.debugLog("保存", processPoint.toString());
				processDefineManager.saveProcessPoint(processPoint);
				addActionMessage("保存成功!");
				return "input";
			}catch(Exception e){
				e.printStackTrace();
				addActionMessage("保存失败:" + e.getMessage());
				return "input";
			}
		}
		return "input";	
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		if(id==null&&parentId!=null){
			ProcessPoint parentPoint = processDefineManager.getProcessPoint(parentId);
			if(parentPoint == null){
				addActionMessage("父级物料为空!");
			}else{
				processPoint.setParent(parentPoint);
			}
		}else if(parentId==null){
			ProcessPoint parent = new ProcessPoint();
			parent.setName("产品列表");
			processPoint.setParent(parent);
		}
		return SUCCESS;
	}
	
	@Action("point-save")
	@Override
	public String save() throws Exception {
		if(id == null){
			ProcessPoint parentPoint = null;
			if(parentId != null){
				parentPoint = processDefineManager.getProcessPoint(parentId);
				processPoint.setParent(parentPoint);
				processPoint.setLevel(parentPoint.getLevel() + 1);
			}
			try{
				logUtilDao.debugLog("保存", processPoint.toString());
				processDefineManager.saveProcessPoint(processPoint);
				addActionMessage("保存成功!");
				return "input";
			}catch(Exception e){
				e.printStackTrace();
				addActionMessage("保存失败:" + e.getMessage());
				return "input";
			}
		}else{
			if(processPoint != null){
				processPoint.setModifiedTime(new Date());
				processPoint.setModifier(ContextUtils.getUserName());
				try{
					logUtilDao.debugLog("修改", processPoint.toString());
					processDefineManager.saveProcessPoint(processPoint);
					addActionMessage("保存成功!");
					return "input";
				}catch(Exception e){
					addActionMessage("保存失败:" + e.getMessage());
					return "input";
				}
			}else{
				addActionMessage("保存过程节点失败,过程节点为空!");
				return "input";
			}
		}	
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		if(id == null){
			createErrorMessage("删除的对象不存在!");
		}else{
			processPoint = processDefineManager.getProcessPoint(id);
			try {
				if(!processPoint.getChildren().isEmpty()){
					createErrorMessage("还有子节点,不能删除!");
				}else{
					processDefineManager.deleteProcessPoint(String.valueOf(id));
					createMessage("删除成功!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				createErrorMessage("删除失败:" + e.getMessage());
			}
		}
		return null;
	}
	@Action("point-list")
	public String pointList() throws Exception {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<ProcessPoint> processPoints = processDefineManager.getProcessPoints(parentId);
		for(ProcessPoint processPoint : processPoints){
			boolean flag=	acsUtils.isAuthority("spc_all", ContextUtils.getUserId(), ContextUtils.getCompanyId());
			if(!flag&&ContextUtils.getSubCompanyName()!=null){
				if(processPoint.getParent()==null&&ContextUtils.getSubCompanyName().equals(processPoint.getName())){
					resultList.add(processDefineManager.convertProcessPoint(processPoint));
				}
			}else{
				//非事业部的
				resultList.add(processDefineManager.convertProcessPoint(processPoint));
			}
		}
		renderText(JSONArray.fromObject(resultList).toString());
		logUtilDao.debugLog("查询", "SPC:过程定义-产品列表");
		return null;
	}
	@Action("list")
	@Override
	public String list() throws Exception {
		processPoint = processDefineManager.getFirstLevelProcessPoint();
		try {
			judgeGridColumnInfo = ApiFactory.getMmsService().getGridColumnInfo("SPC_FEATURE_RULES");
			JSONArray jsonArray = JSONArray.fromObject(judgeGridColumnInfo.getColModel());
			judgeGridColumnInfo.setColModel(jsonArray.toString());
			jsonArray = JSONArray.fromObject(judgeGridColumnInfo.getColNames());
			judgeGridColumnInfo.setColNames(jsonArray.toString());
			
			levelGridColumnInfo = ApiFactory.getMmsService().getGridColumnInfo("SPC_FEATURE_LAYER");
			jsonArray = JSONArray.fromObject(levelGridColumnInfo.getColModel());
			levelGridColumnInfo.setColModel(jsonArray.toString());
			jsonArray = JSONArray.fromObject(levelGridColumnInfo.getColNames());
			levelGridColumnInfo.setColNames(jsonArray.toString());
			
			personGridColumnInfo = ApiFactory.getMmsService().getGridColumnInfo("SPC_FEATURE_PERSON");
			jsonArray = JSONArray.fromObject(personGridColumnInfo.getColModel());
			personGridColumnInfo.setColModel(jsonArray.toString());
			jsonArray = JSONArray.fromObject(personGridColumnInfo.getColNames());
			personGridColumnInfo.setColNames(jsonArray.toString());
			
			List<Option> units = ApiFactory.getSettingService().getOptionsByGroupCode("measure_unit");
			ActionContext.getContext().put("units",units);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	@Action("list-datas")
	public String getListDatas() throws Exception {
		String featureId = Struts2Utils.getParameter("featureId");
		if(featureId != null){
			qualityFeature = qualityFeatureManager.getQualityFeature(Long.valueOf(featureId));
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id", qualityFeature.getId());
		map.put("name",qualityFeature.getName());
		map.put("code", qualityFeature.getCode());
		map.put("orderNum", qualityFeature.getOrderNum()==null?"":qualityFeature.getOrderNum());
		map.put("paramType", qualityFeature.getParamType());
		map.put("specificationType", qualityFeature.getSpecificationType());
		map.put("targeValue", qualityFeature.getTargeValue());
		map.put("upperTarge", qualityFeature.getUpperTarge());
		map.put("lowerTarge", qualityFeature.getLowerTarge());
		map.put("sampleCapacity", qualityFeature.getSampleCapacity());
		map.put("effectiveCapacity", qualityFeature.getEffectiveCapacity());
		map.put("controlChart", qualityFeature.getControlChart());
		map.put("rangeInterval", qualityFeature.getRangeInterval());
		map.put("precs", qualityFeature.getPrecs());
		map.put("unit", qualityFeature.getUnit());
		map.put("upperLimit", qualityFeature.getUpperLimit());
		map.put("lowerLimit", qualityFeature.getLowerLimit());
		map.put("isNoAccept", qualityFeature.getIsNoAccept());
		map.put("multiple", qualityFeature.getMultiple());
		map.put("state", qualityFeature.getState());
		map.put("method", qualityFeature.getMethod());
		map.put("u", qualityFeature.getU());
		map.put("cpk", qualityFeature.getCpk());
		map.put("ucl1", qualityFeature.getUcl1());
		map.put("ucl2", qualityFeature.getUcl2());
		map.put("cl1", qualityFeature.getCl1());
		map.put("cl2", qualityFeature.getCl2());
		map.put("lcl1", qualityFeature.getLcl1());
		map.put("lcl2", qualityFeature.getLcl2());
		map.put("uclMin", qualityFeature.getUclMin());
		map.put("uclMax", qualityFeature.getUclMax());
		map.put("lclMin", qualityFeature.getLclMin());
		map.put("lclMax", qualityFeature.getLclMax());
		map.put("uclCurrent1", qualityFeature.getUclCurrent1());
		map.put("uclCurrent2", qualityFeature.getUclCurrent2());
		map.put("clCurrent1", qualityFeature.getClCurrent1());
		map.put("clCurrent2", qualityFeature.getClCurrent2());
		map.put("lclCurrent1", qualityFeature.getLclCurrent1());
		map.put("lclCurrent2", qualityFeature.getLclCurrent2());
		map.put("isAuto", qualityFeature.getIsAuto());
		map.put("isAutoCl", qualityFeature.getIsAutoCl());
		renderText(JSONObject.fromObject(map).toString());
		return null;
	}
	
	@Action("set-person")
	public String setPerson() throws Exception{
		String parentId = Struts2Utils.getParameter("parentId");
		String userIds = Struts2Utils.getParameter("userIds");
		String userNames = Struts2Utils.getParameter("userNames");
		JSONObject result = new JSONObject();
		try{
			processDefineManager.setPerson(parentId,userIds,userNames);
			result.put("error", false);
		}catch(Exception e){
			result.put("error", true);
			result.put("message", "设置失败");
			e.printStackTrace();
		}
		this.renderText(result.toString());
		return null;
		
	}
	
	@Action("bath-set-rules")
	public String bathSetRules() throws Exception{
		String parentId = Struts2Utils.getParameter("parentId");
		String ruleIds = Struts2Utils.getParameter("ruleIds");
		JSONObject result = new JSONObject();
		try{
			processDefineManager.bathSetRules(parentId,ruleIds);
			result.put("error", false);
		}catch(Exception e){
			result.put("error", true);
			result.put("message", "设置失败");
			e.printStackTrace();
		}
		this.renderText(result.toString());
		return null;
		
	}
	
	@Action("bath-set-layers")
	public String bathSetLayers() throws Exception{
		String parentId = Struts2Utils.getParameter("parentId");
		String layerIds = Struts2Utils.getParameter("layerIds");
		JSONObject result = new JSONObject();
		try{
			processDefineManager.bathSetLayers(parentId,layerIds);
			result.put("error", false);
		}catch(Exception e){
			result.put("error", true);
			result.put("message", "设置失败");
			e.printStackTrace();
		}
		this.renderText(result.toString());
		return null;
		
	}
	
	
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	/*private void createMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}*/
}

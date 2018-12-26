package com.ambition.spc.layertype.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.entity.LayerDetail;
import com.ambition.spc.entity.LayerType;
import com.ambition.spc.layertype.service.LayerDetailManager;
import com.ambition.spc.layertype.service.LayerTypeManager;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**    
 * LayerTypeAction.java
 * @authorBy wanglf
 *
 */
@Namespace("/spc/base-info/layer-type")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/spc/base-info/layer-type", type = "redirectAction") })
public class LayerTypeAction extends com.ambition.product.base.CrudActionSupport<LayerType> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long nodeid;
	private Long structureId;//结构编号
	private Long parentId;
	private String expandIds;//刷新时展开的节点
	private String deleteIds;
	private Page<LayerType> page;
	private LayerType layerType;
	
 	@Autowired
	private LayerTypeManager layerTypeManager;
 	
 	@Autowired
	private LayerDetailManager layerDetailManager;
 	
 	@Autowired
	private LogUtilDao logUtilDao;
 	
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

	public Page<LayerType> getPage() {
		return page;
	}

	public void setPage(Page<LayerType> page) {
		this.page = page;
	}

	public LayerType getLayerType() {
		return layerType;
	}

	public void setLayerType(LayerType layerType) {
		this.layerType = layerType;
	}

	public LayerType getModel() {
		return layerType;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			layerType = new LayerType();
			layerType.setCreatedTime(new Date());
			layerType.setCompanyId(ContextUtils.getCompanyId());
			layerType.setCreator(ContextUtils.getUserName());
			layerType.setModifiedTime(new Date());
			layerType.setModifier(ContextUtils.getUserName());
			if(parentId != null){
				LayerType parentLayerType = layerTypeManager.getLayerType(parentId);
				if(parentLayerType != null){
					layerType.setParent(parentLayerType);
					layerType.setLevel(parentLayerType.getLevel()+1);
				}
			}
		}else {
			layerType = layerTypeManager.getLayerType(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		ActionContext.getContext().put("sampleMethods",ApiFactory.getSettingService().getOptionsByGroupCode("spc_sample_method"));
		if(id==null&&parentId!=null){
			LayerType parentLayerType = layerTypeManager.getLayerType(parentId);
			if(parentLayerType == null){
				addActionMessage("父级物料为空!");
			}else{
				layerType.setParent(parentLayerType);
			}
		}else if(parentId==null){
			LayerType parent = new LayerType();
			parent.setTypeName("层级类别");
			layerType.setParent(parent);
		}
		if(id==null){
		//层别信息的类别编码保存
		List<String> initTypeCode=new ArrayList<String>();//初始 typecode
		initTypeCode.add("info1");initTypeCode.add("info2");initTypeCode.add("info3");initTypeCode.add("info4");initTypeCode.add("info5");
		initTypeCode.add("info6");initTypeCode.add("info7");initTypeCode.add("info8");initTypeCode.add("info9");initTypeCode.add("info10");
		initTypeCode.add("info11");initTypeCode.add("info12");initTypeCode.add("info13");initTypeCode.add("info14");initTypeCode.add("info15");
		initTypeCode.add("info16");initTypeCode.add("info17");initTypeCode.add("info17");initTypeCode.add("info19");initTypeCode.add("info20");
		//与原有的typecode进行比较得出可用的typeCode的LIST
		List<LayerType> layerTypes=layerTypeManager.getLayerTypeCodes();
		for(int i=0;i<layerTypes.size();i++){
			LayerType  l=layerTypes.get(i);
			String typeCode=l.getTypeCode();
			for(int j=0;j<initTypeCode.size();j++){
				String code=initTypeCode.get(j);
				if(typeCode.equals(code)){
					initTypeCode.remove(j);
					break;
				}
			}
		}
		if(initTypeCode.size()>0){
			String typeCode=initTypeCode.get(0);
		ActionContext.getContext().put("typeCode", typeCode);
		}else{
			addActionMessage("本版本只能保存20个层别信息");
		}
		}
		return SUCCESS;
	}
	
	@Action("save")
	@Override
	public String save() throws Exception {
		if(id == null){
			LayerType parentlayerType = null;
			if(parentId != null){
				parentlayerType = layerTypeManager.getLayerType(parentId);
				layerType.setParent(parentlayerType);
			}
			if(parentlayerType != null){
				layerType.setLevel(parentlayerType.getLevel() + 1);
			}
			try{
				logUtilDao.debugLog("保存", layerType.toString());
				layerTypeManager.saveLayerType(layerType,true);
				addActionMessage("保存成功!");
				return "input";
			}catch(Exception e){
				addActionMessage("保存失败：" + e.getMessage());
				return "input";
			}
		}else{
			if(layerType != null){
				layerType.setModifiedTime(new Date());
				layerType.setModifier(ContextUtils.getUserName());
				try{
					logUtilDao.debugLog("修改", layerType.toString());
					layerTypeManager.saveLayerType(layerType,false);
					addActionMessage("保存成功!");
					return "input";
				}catch(Exception e){
					addActionMessage("保存失败：" + e.getMessage());
					return "input";
				}
			}else{
				addActionMessage("保存产品列表失败,产品结构为空!");
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
			layerType = layerTypeManager.getLayerType(id);
			try {
				if(!layerType.getChildren().isEmpty()){
					createErrorMessage("还有子节点,不能删除!");
				}else{
					layerTypeManager.deleteLayerType(String.valueOf(id));
					createMessage("删除成功!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				createErrorMessage("删除失败:" + e.getMessage());
			}
		}
		return null;
	}
	
	@Action("list")
	public String list() throws Exception {
		layerType = layerTypeManager.getFirstLevelLayerType();
		ActionContext.getContext().put("layerType", layerType);
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String listData() throws Exception {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<LayerType> layerTypes =layerTypeManager.getLayerTypes(parentId);
		for(LayerType layerType : layerTypes){
			resultList.add(layerTypeManager.convertLayerType(layerType));
		}
		renderText(JSONArray.fromObject(resultList).toString());
		logUtilDao.debugLog("查询", "SPC:层级类别");
		return null;
	}
	
	@Action("detial-list-datas")
	public String detailListData() throws Exception {
		try{
			if(parentId != null){
				layerType = layerTypeManager.getLayerType(parentId);
			}
			String result = layerTypeManager.getDetailResultJson(page, layerType);	
			renderText(JSONObject.fromObject(result).toString());
			logUtilDao.debugLog("查询", "SPC:层级类别");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Action("save-detail")
	public String saveDetail() throws Exception {
		try{
			String detailParentId=Struts2Utils.getParameter("detailParentId");
			String detailName=Struts2Utils.getParameter("detailName");
			String detailCode=Struts2Utils.getParameter("detailCode");
			if(StringUtils.isEmpty(detailParentId)){
				renderText("{'error':true,'message':'请先新建层别类别'}");
			}else if(layerDetailManager.getLayerDetailsByCode(detailCode).size()>0&&id==null){
				renderText("{'error':true,'message':'编码已存在'}");
			}else if(layerDetailManager.getLayerDetailsByCode(detailName).size()>0&&id==null){
				renderText("{'error':true,'message':'名称已存在'}");
			}else if(id==null){
				LayerType layerType =layerTypeManager.getLayerType(Long.parseLong(detailParentId));
				LayerDetail laydetail=new LayerDetail();
				laydetail.setDetailName(detailName);
				laydetail.setDetailCode(detailCode);
				laydetail.setLayerType(layerType);
				layerTypeManager.saveLayerDetail(laydetail);
				renderText(JsonParser.getRowValue(laydetail));
			}else{
				LayerType layerType =layerTypeManager.getLayerType(Long.parseLong(detailParentId));
				LayerDetail laydetail=layerDetailManager.getLayerDetail(id);
				laydetail.setDetailName(detailName);
				laydetail.setDetailCode(detailCode);
				laydetail.setLayerType(layerType);
				layerTypeManager.saveLayerDetail(laydetail);
				renderText(JsonParser.getRowValue(laydetail));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Action("delete-detail")
	public String deleteDetail() throws Exception {
		layerTypeManager.deleteLayerDetail(deleteIds);
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

package com.ambition.carmfg.inspectionbase.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.ambition.carmfg.entity.MfgInspectingItem;
import com.ambition.carmfg.inspectionbase.service.MfgInspectingItemManager;
import com.ambition.product.BaseAction;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;
/**    
 * InspectingItemAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/carmfg/inspection-base/item")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "carmfg/inspection-base/item", type = "redirectAction") })
public class MfgInspectingItemAction extends BaseAction<MfgInspectingItem> {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long inspectingItemId;//检验项目编号
	private Long selParentId;//选择的父级ID
	private Long nodeid;
	private Long parentId;
	private String expandIds;
	private String deleteIds;//删除的编号 
	private JSONObject params;//项目指标对象
	private MfgInspectingItem mfgInspectingItem;
	private Page<MfgInspectingItem> page;

	public Long getSelParentId() {
		return selParentId;
	}

	public void setSelParentId(Long selParentId) {
		this.selParentId = selParentId;
	}

	public Page<MfgInspectingItem> getPage() {
		return page;
	}

	public void setPage(Page<MfgInspectingItem> page) {
		this.page = page;
	}

	@Autowired
	private MfgInspectingItemManager mfgInspectingItemManager;
 	
 	@Autowired
	private LogUtilDao logUtilDao;

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public Long getNodeid() {
		return nodeid;
	}

	public void setNodeid(Long nodeid) {
		this.nodeid = nodeid;
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
	
	public Long getInspectingItemId() {
		return inspectingItemId;
	}

	public void setInspectingItemId(Long inspectingItemId) {
		this.inspectingItemId = inspectingItemId;
	}

	public MfgInspectingItem getMfgInspectingItem() {
		return mfgInspectingItem;
	}

	public void setMfgInspectingItem(MfgInspectingItem mfgInspectingItem) {
		this.mfgInspectingItem = mfgInspectingItem;
	}

	@Override
	public MfgInspectingItem getModel() {
		return mfgInspectingItem;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			mfgInspectingItem = new MfgInspectingItem();
			mfgInspectingItem.setCreatedTime(new Date());
			mfgInspectingItem.setCompanyId(ContextUtils.getCompanyId());
			mfgInspectingItem.setCreator(ContextUtils.getUserName());
			mfgInspectingItem.setLastModifiedTime(new Date());
			mfgInspectingItem.setLastModifier(ContextUtils.getUserName());
			mfgInspectingItem.setBusinessUnitName(ContextUtils.getSubCompanyName());
			mfgInspectingItem.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
			if(selParentId != null){
				MfgInspectingItem parent = mfgInspectingItemManager.getInspectingItem(selParentId);
				if(parent != null){
					mfgInspectingItem.setItemParent(parent);
					mfgInspectingItem.setItemLevel(parent.getItemLevel()+1);
				}
			}
		}else {
			mfgInspectingItem = mfgInspectingItemManager.getInspectingItem(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		ActionContext.getContext().put("countTypeOptions",mfgInspectingItemManager.getCountTypeOptions());
		return SUCCESS;
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="检验项目")
	@Override
	public String save() throws Exception {
		try {
			if(id==null){
				mfgInspectingItemManager.saveInspectingItem(mfgInspectingItem);
				logUtilDao.debugLog("保存", mfgInspectingItem.toString());
			}else{
				mfgInspectingItem.setLastModifiedTime(new Date());
				mfgInspectingItem.setLastModifier(ContextUtils.getUserName());
				mfgInspectingItemManager.saveInspectingItem(mfgInspectingItem);
				logUtilDao.debugLog("修改", mfgInspectingItem.toString());
			}
			renderText(formatInspectingItem(mfgInspectingItem));
		} catch (Exception e) {
			e.printStackTrace();
			createErrorMessage("保存失败：" + e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="检验项目")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				mfgInspectingItemManager.deleteInspectingItem(deleteIds);
				createMessage("删除成功!");
			} catch (Exception e) {
				createErrorMessage("删除失败:" + e.getMessage());
			}
		}
		return null;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		renderMenu();
		return SUCCESS;
	}
	
	private String getValue(Object obj){
		if(obj==null){
			return "";
		}else{
			return obj+"";
		}
	}
	@Action("list-datas")
	public String getInspectingItemByParent() throws Exception {
		try {
			if(Struts2Utils.getRequest().getParameter("searchParameters") != null){
				selParentId = null;
				page = mfgInspectingItemManager.searchByParams(page);
			}else{
				page = mfgInspectingItemManager.search(page,selParentId);
			}
			StringBuilder json = new StringBuilder();
			json.append("{\"page\":\"");
			json.append(getValue(page.getPageNo()));
			json.append("\",\"total\":\"");
			json.append(getValue(page.getTotalPages()));
			json.append("\",\"records\":\"");
			json.append(getValue(page.getTotalCount()));
			json.append("\",\"rows\":");
			StringBuffer rows = new StringBuffer("[");
			for(MfgInspectingItem inspectingItem : page.getResult()){
				if(rows.length()>1){
					rows.append(",");
				}
				rows.append(formatInspectingItem(inspectingItem));
			}
			json.append(rows.append("]"));
			json.append("}");
			renderText(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		logUtilDao.debugLog("查询", "进货检验管理：检验标准维护-检验项目维护");
		return null;
	}
	
	private String formatInspectingItem(MfgInspectingItem inspectingItem){
		@SuppressWarnings("unused")
		int level = inspectingItem.getItemLevel();
		StringBuffer row = new StringBuffer("{");
		row.append("\"itemName\":\"" + getValue(inspectingItem.getItemName()) + "\"")
		.append(",\"id\":\"" + getValue(inspectingItem.getId()) + "\"")
		.append(",\"method\":\"" + getValue(inspectingItem.getMethod()) + "\"")
		.append(",\"specifications\":\"" + getValue(inspectingItem.getSpecifications()) + "\"")
		.append(",\"countType\":\"" + getValue(inspectingItem.getCountType()) + "\"")
		.append(",\"unit\":\"" + getValue(inspectingItem.getUnit()) + "\"")
		.append(",\"remark\":\"" + getValue(inspectingItem.getRemark()) + "\"")
		.append(",\"orderNum\":\"" + getValue(inspectingItem.getOrderNum()) + "\"")
		.append(",\"hasChild\":\"" + (inspectingItem.getItemChildren()==null||inspectingItem.getItemChildren().isEmpty()?"false":"true") + "\"")
		.append("}");
		return row.toString();
	}
	@Action("list-structure")
	public String listStructure() throws Exception {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<MfgInspectingItem> parents = null;
		parents = mfgInspectingItemManager.getInspectingItemTypes(selParentId);
		for(MfgInspectingItem mfgInspectingItem : parents){
			if(!mfgInspectingItem.getItemChildren().isEmpty()){
				resultList.add(convertInspectingItemForTree(mfgInspectingItem));
			}
		}
		renderText(JSONArray.fromObject(resultList).toString());
		logUtilDao.debugLog("查询", "进货检验管理：检验标准维护-检验项目维护-检验类型树");
		return null;
	}
	
	private List<Object> getAllParent(MfgInspectingItem mfgInspectingItem){
		List<Object> list = new ArrayList<Object>();
		if(mfgInspectingItem.getItemParent() != null){
			list.addAll(getAllParent(mfgInspectingItem.getItemParent()));
		}
		Map<String,Object> obj = new HashMap<String, Object>();
		obj.put("id",mfgInspectingItem.getId());
		obj.put("name",mfgInspectingItem.getItemName().replaceAll("\n",""));
		list.add(obj);
		return list;
	}
	@Action("get-path")
	public String getPath() throws Exception {
		MfgInspectingItem mfgInspectingItem = mfgInspectingItemManager.getInspectingItem(selParentId);
		if(mfgInspectingItem != null){
			List<Object> list = getAllParent(mfgInspectingItem);
			renderText(JSONArray.fromObject(list).toString());
		}else{
			List<Object> list = new ArrayList<Object>();
			renderText(JSONArray.fromObject(list).toString());
		}
		return null;
	}
	@Action("move-items")
	public String moveInspectingItems() throws Exception {
		try {
			mfgInspectingItemManager.moveInspectingItems(deleteIds,selParentId);
			createMessage("操作成功!");
		} catch (Exception e) {
			createErrorMessage("操作失败:" + e.getMessage());
		}
		return null;
	}
	
	@Action("list-move-structure")
	public String listMoveStructure() throws Exception {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<MfgInspectingItem> inspectingItems = mfgInspectingItemManager.getInspectingItemTypes(selParentId);
		if(deleteIds==null){
			deleteIds = "";
		}
		deleteIds = "," + deleteIds + ",";
		for(MfgInspectingItem inspectingItem : inspectingItems){
			if(deleteIds.indexOf("," + inspectingItem.getId()+",")==-1){
				resultList.add(convertInspectingItemForTree(inspectingItem));
			}
		}
		renderText(JSONArray.fromObject(resultList).toString());
		logUtilDao.debugLog("查询", "进货检验管理：检验标准维护-检验类型结构");
		return null;
	}
//	/**
//	 * 转换物料结构至json对象
//	 * @param estimateModel
//	 * @return
//	 */
//	private void convertInspectingItem(InspectingItem inspectingItem,List<Map<String,Object>> list,Map<String,Boolean> expandMap){
//		Boolean isLeaf = inspectingItem.getChildren().isEmpty();
//		Map<String,Object> map = new HashMap<String, Object>();
//		map.put("id",inspectingItem.getId());
//		map.put("name",inspectingItem.getName());
//		map.put("countType",inspectingItem.getCountType());
//		map.put("method",inspectingItem.getMethod());
//		map.put("standards",inspectingItem.getStandards());
//		map.put("unit",inspectingItem.getUnit());
//		map.put("remark",inspectingItem.getRemark());
//		map.put("level",inspectingItem.getLevel()-1);
//		map.put("parent",inspectingItem.getParent()==null?"":inspectingItem.getParent().getId());
//		map.put("isLeaf",isLeaf);
//		list.add(map);
//		if(!isLeaf){
//			if(expandMap.containsKey(inspectingItem.getId().toString())){
//				map.put("expanded",true);
//				map.put("loaded",true);
//				expandMap.remove(inspectingItem.getId().toString());
//				for(InspectingItem child : inspectingItem.getChildren()){
//					convertInspectingItem(child,list,expandMap);
//				}
//			}else{
//				map.put("expanded",false);
//				map.put("loaded",false);
//			}
//		}else{
//			map.put("loaded",true);
//		}
//	}
	/**
	 * 转换树结构的检验类型
	 * @param inspectingItem
	 * @return
	 */
	private Map<String,Object> convertInspectingItemForTree(MfgInspectingItem inspectingItem){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("data",inspectingItem.getItemName());
		Map<String,Object> attrMap = new HashMap<String, Object>();
		attrMap.put("id",inspectingItem.getId());
		attrMap.put("level",inspectingItem.getItemLevel());
		attrMap.put("name",inspectingItem.getItemName());
		map.put("attr", attrMap);
		if(!inspectingItem.getItemChildren().isEmpty()){
			map.put("state","closed");
		}
		return map;
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

}

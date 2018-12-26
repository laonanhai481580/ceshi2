package com.ambition.si.baseinfo.web;

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

import com.ambition.product.BaseAction;
import com.ambition.si.baseinfo.service.SiInspectingItemManager;
import com.ambition.si.entity.SiInspectingItem;
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
@Namespace("/si/base-info/item")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "si/base-info/item", type = "redirectAction") })
public class SiInspectingItemAction extends BaseAction<SiInspectingItem> {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long inspectingItemId;//检验项目编号
	private Long selParentId;//选择的父级ID
	private Long nodeid;
	private Long parentId;
	private String expandIds;
	private String deleteIds;//删除的编号 
	private JSONObject params;//项目指标对象
	private SiInspectingItem siInspectingItem;
	private Page<SiInspectingItem> page;

	public Long getSelParentId() {
		return selParentId;
	}

	public void setSelParentId(Long selParentId) {
		this.selParentId = selParentId;
	}

	public Page<SiInspectingItem> getPage() {
		return page;
	}

	public void setPage(Page<SiInspectingItem> page) {
		this.page = page;
	}

	@Autowired
	private SiInspectingItemManager siInspectingItemManager;
 	
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

	public SiInspectingItem getSiInspectingItem() {
		return siInspectingItem;
	}

	public void setSiInspectingItem(SiInspectingItem siInspectingItem) {
		this.siInspectingItem = siInspectingItem;
	}

	@Override
	public SiInspectingItem getModel() {
		return siInspectingItem;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			siInspectingItem = new SiInspectingItem();
			siInspectingItem.setCreatedTime(new Date());
			siInspectingItem.setCompanyId(ContextUtils.getCompanyId());
			siInspectingItem.setCreator(ContextUtils.getUserName());
			siInspectingItem.setLastModifiedTime(new Date());
			siInspectingItem.setLastModifier(ContextUtils.getUserName());
			siInspectingItem.setBusinessUnitName(ContextUtils.getSubCompanyName());
			siInspectingItem.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
			if(selParentId != null){
				SiInspectingItem parent = siInspectingItemManager.getInspectingItem(selParentId);
				if(parent != null){
					siInspectingItem.setItemParent(parent);
					siInspectingItem.setItemLevel(parent.getItemLevel()+1);
				}
			}
		}else {
			siInspectingItem = siInspectingItemManager.getInspectingItem(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		ActionContext.getContext().put("countTypeOptions",siInspectingItemManager.getCountTypeOptions());
		return SUCCESS;
	}
	
	@Action("save")
	@Override
	public String save() throws Exception {
		try {
			if(id==null){
				siInspectingItemManager.saveInspectingItem(siInspectingItem);
				logUtilDao.debugLog("保存", siInspectingItem.toString());
			}else{
				siInspectingItem.setLastModifiedTime(new Date());
				siInspectingItem.setLastModifier(ContextUtils.getUserName());
				siInspectingItemManager.saveInspectingItem(siInspectingItem);
				logUtilDao.debugLog("修改", siInspectingItem.toString());
			}
			renderText(formatInspectingItem(siInspectingItem));
		} catch (Exception e) {
			e.printStackTrace();
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
				siInspectingItemManager.deleteInspectingItem(deleteIds);
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
				page = siInspectingItemManager.searchByParams(page);
			}else{
				page = siInspectingItemManager.search(page,selParentId);
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
			for(SiInspectingItem inspectingItem : page.getResult()){
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
	
	private String formatInspectingItem(SiInspectingItem inspectingItem){
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
		List<SiInspectingItem> parents = null;
		parents = siInspectingItemManager.getInspectingItemTypes(selParentId);
		for(SiInspectingItem siInspectingItem : parents){
			if(!siInspectingItem.getItemChildren().isEmpty()){
				resultList.add(convertInspectingItemForTree(siInspectingItem));
			}
		}
		renderText(JSONArray.fromObject(resultList).toString());
		logUtilDao.debugLog("查询", "进货检验管理：检验标准维护-检验项目维护-检验类型树");
		return null;
	}
	
	private List<Object> getAllParent(SiInspectingItem siInspectingItem){
		List<Object> list = new ArrayList<Object>();
		if(siInspectingItem.getItemParent() != null){
			list.addAll(getAllParent(siInspectingItem.getItemParent()));
		}
		Map<String,Object> obj = new HashMap<String, Object>();
		obj.put("id",siInspectingItem.getId());
		obj.put("name",siInspectingItem.getItemName().replaceAll("\n",""));
		list.add(obj);
		return list;
	}
	@Action("get-path")
	public String getPath() throws Exception {
		SiInspectingItem siInspectingItem = siInspectingItemManager.getInspectingItem(selParentId);
		if(siInspectingItem != null){
			List<Object> list = getAllParent(siInspectingItem);
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
			siInspectingItemManager.moveInspectingItems(deleteIds,selParentId);
			createMessage("操作成功!");
		} catch (Exception e) {
			createErrorMessage("操作失败:" + e.getMessage());
		}
		return null;
	}
	
	@Action("list-move-structure")
	public String listMoveStructure() throws Exception {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<SiInspectingItem> inspectingItems = siInspectingItemManager.getInspectingItemTypes(selParentId);
		if(deleteIds==null){
			deleteIds = "";
		}
		deleteIds = "," + deleteIds + ",";
		for(SiInspectingItem inspectingItem : inspectingItems){
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
	private Map<String,Object> convertInspectingItemForTree(SiInspectingItem inspectingItem){
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

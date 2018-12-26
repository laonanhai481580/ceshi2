	package com.ambition.carmfg.bom.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import com.ambition.carmfg.bom.service.ProductBomManager;
import com.ambition.carmfg.entity.ProductBom;
import com.ambition.gsm.entity.GsmCheckItem;
import com.ambition.product.BaseAction;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;


@Namespace("/carmfg/base-info/bom")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "carmfg/base-info/bom", type = "redirectAction") })
public class BomAction extends BaseAction<ProductBom> {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long nodeid;
	private Long selParentId;
	private Long structureId;//结构编号
	private String expandIds;//刷新时展开的节点
	private String deleteIds;//删除的BOM编号 
	private Page<ProductBom> page;
	
	
	
	private File myFile;
	
	private ProductBom productBom;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
 	@Autowired
	private ProductBomManager productBomManager;
 	
 	
	public Page<ProductBom> getPage() {
		return page;
	}

	public void setPage(Page<ProductBom> page) {
		this.page = page;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getSelParentId() {
		return selParentId;
	}

	public void setSelParentId(Long selParentId) {
		this.selParentId = selParentId;
	}

	public ProductBom getProductBom() {
		return productBom;
	}
	public void setProductBom(ProductBom productBom) {
		this.productBom = productBom;
	}
	public ProductBom getModel() {
		return productBom;
	}
	

    @Override
	protected void prepareModel() throws Exception {
		if(id==null){
			productBom = new ProductBom();
			productBom.setCreatedTime(new Date());
			productBom.setCompanyId(ContextUtils.getCompanyId());
			productBom.setCreator(ContextUtils.getUserName());
			productBom.setLastModifiedTime(new Date());
			productBom.setLastModifier(ContextUtils.getUserName());
			productBom.setBusinessUnitName(ContextUtils.getSubCompanyName());
			productBom.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
			if(selParentId != null){
				ProductBom parentBom = productBomManager.getProductBom(selParentId);
				if(parentBom != null){
					productBom.setBomParent(parentBom);
					productBom.setMaterielLevel(parentBom.getMaterielLevel()+1);
				}
			}
		}else {
			productBom = productBomManager.getProductBom(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		if(id==null&&selParentId!=null){
			ProductBom parentBom = productBomManager.getProductBom(selParentId);
			if(parentBom == null){
				addActionMessage("父级物料为空!");
			}else{
				productBom.setBomParent(parentBom);
			}
		}
		
		List<Option> materialTypes = ApiFactory.getSettingService().getOptionsByGroupCode("supply_materialType");
		ActionContext.getContext().put("materialTypes",materialTypes);
		
		List<Option> ascendTypes = ApiFactory.getSettingService().getOptionsByGroupCode("ascendType");
		ActionContext.getContext().put("ascendTypes",ascendTypes);
		
		ActionContext.getContext().put("importances",productBomManager.getImportanceOptions());
		
		List<Map<String,Object>> ss = new ArrayList<Map<String,Object>>();
		Collections.sort(ss,new Comparator<Map<String,Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return 0;
			}
		});
		
		return SUCCESS;
	}
	@Action("save")
	@LogInfo(optType="保存",message="产品BOM")
	@Override
	public String save() throws Exception {
		try {
			String isRefresh = "0";
			if(productBom.getBomParent() != null){
				isRefresh = productBom.getBomParent().getHasChild()+"";
			}
			if(id == null){
				productBomManager.saveProductBom(productBom);
				logUtilDao.debugLog("修改", productBom.toString());
			}else{
				if(productBom != null){
					productBom.setLastModifiedTime(new Date());
					productBom.setLastModifier(ContextUtils.getUserName());
					productBomManager.saveProductBom(productBom);
					logUtilDao.debugLog("保存", productBom.toString());
				}else{
					createErrorMessage("保存物料BOM失败,物料BOM为空!");
				}
			}
			JSONObject jsonObject = JSONObject.fromObject(JsonParser.getRowValue(productBom));
			if(productBom.getBomParent()==null){
				jsonObject.put("isRefresh","0");
			}else{
				if(isRefresh.equals(productBom.getBomParent().getHasChild()+"")){
					jsonObject.put("isRefresh","0");
				}else{
					jsonObject.put("isRefresh","1");
				}
			}
			jsonObject.put("id",productBom.getId());
			this.renderText(jsonObject.toString());
		} catch (Exception e) {
			createErrorMessage("保存失败：" + e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="产品BOM")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				productBomManager.deleteProductBom(deleteIds);
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
	private List<Object> getAllParent(ProductBom productBom){
		List<Object> list = new ArrayList<Object>();
		if(productBom.getBomParent() != null){
			list.addAll(getAllParent(productBom.getBomParent()));
		}
		Map<String,Object> obj = new HashMap<String, Object>();
		obj.put("id",productBom.getId());
		obj.put("name",productBom.getMaterielName().replaceAll("\n",""));
		list.add(obj);
		return list;
	}
	@Action("get-path")
	public String getPath() throws Exception {
		ProductBom productBom = productBomManager.getProductBom(selParentId);
		if(productBom != null){
			List<Object> list = getAllParent(productBom);
			renderText(JSONArray.fromObject(list).toString());
		}else{
			List<Object> list = new ArrayList<Object>();
			renderText(JSONArray.fromObject(list).toString());
		}
		return null;
	}
	
	
	@Action("list-datas")
	public String getBomListByParent() throws Exception {
		try {
			if(Struts2Utils.getRequest().getParameter("searchParameters") != null){
				selParentId = null;
				page = productBomManager.searchByParams(page);
			}else{
				page = productBomManager.search(page,selParentId);
			}
			for(ProductBom productBom : page.getResult()){
//				if(productBom.getChildren().isEmpty()){
					productBom.setHasChild(false);
//				}else{
//					productBom.setHasChild(true);
//				}
			}
			renderText(PageUtils.pageToJson(page).replaceAll("\n","").replaceAll("\r\n",""));
		} catch (Exception e) {
			e.printStackTrace();
		}
//		Page page = new Page();
//		if(structureId == null){
//			page.setResult(new ArrayList<Object>());
//		}else{
//			String searchParams = Struts2Utils.getParameter("searchParams");
//			JSONObject params = null;
//			if(StringUtils.isNotEmpty(searchParams)){
//				params = JSONObject.fromObject(searchParams);
//			}
//			Map<String,Boolean> expandMap = new HashMap<String, Boolean>();
//			if(StringUtils.isNotEmpty(expandIds)){
//				String[] ids = expandIds.split(",");
//				for(String id : ids){
//					if(StringUtils.isNotEmpty(id)){
//						expandMap.put(id,true);
//					}
//				}
//			}
//			List<ProductBom> bomParents = null;
//			if(nodeid == null){
//				bomParents = productBomManager.getProductBomParentsByStructure(structureId,params);
//			}else{
//				bomParents = productBomManager.searchProductBomByParent(nodeid, params);
//			}
//			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//			for(ProductBom productBom : bomParents){
//				convertProductBom(productBom,expandMap,list);
//			}
//			page.setResult(list);
//		}
//		renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "制造质量管理：基础设置-产品物料");
		return null;
	}
	@Action("list-prodct-datas")
	public String getProductListByParent() throws Exception {
		try {
			String type = Struts2Utils.getParameter("type");
			if(Struts2Utils.getRequest().getParameter("searchParameters") != null){
				selParentId = null;
				page = productBomManager.searchProductByParams(page,type);
			}else{
				page = productBomManager.searchProduct(page,selParentId,type);
			}
			for(ProductBom productBom : page.getResult()){
					productBom.setHasChild(false);
			}
			renderText(PageUtils.pageToJson(page).replaceAll("\n","").replaceAll("\r\n",""));
		} catch (Exception e) {
			e.printStackTrace();
		}

		logUtilDao.debugLog("查询", "制造质量管理：基础设置-产品物料");
		return null;
	}
	
	@Action("model-datas")
	public String getBomModelByParent() throws Exception {
		try {
			if(Struts2Utils.getRequest().getParameter("searchParameters") != null){
				selParentId = null;
				page = productBomManager.searchModelByParams(page);
			}else{
				page = productBomManager.searchModel(page,selParentId);
			}
			for(ProductBom productBom : page.getResult()){
				if(productBom.getBomChildren().isEmpty()){
					productBom.setHasChild(false);
				}else{
					productBom.setHasChild(true);
				}
			}
			renderText(PageUtils.pageToJson(page).replaceAll("\n","").replaceAll("\r\n",""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logUtilDao.debugLog("查询", "制造质量管理：基础设置-选择产品型号");
		return null;
	}
	
	@Action("list-structure")
	public String listStructure() throws Exception {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
//		List<ProductBom> productBoms = productBomManager.getProductStructures(selParentId);
//		for(ProductBom productStructure : productBoms){
//			if(!productStructure.getChildren().isEmpty()){
//				resultList.add(convertProductStructure(productStructure));
//			}
//		}
		renderText(JSONArray.fromObject(resultList).toString());
		logUtilDao.debugLog("查询", "制造质量管理：基础设置-产品BOM结构");
		return null;
	}
	
	@Action("model-structure")
	public String modelStructure() throws Exception {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<ProductBom> productBoms = productBomManager.getProductStructures(selParentId);
		for(ProductBom productStructure : productBoms){
			if(!productStructure.getBomChildren().isEmpty()){
				resultList.add(convertModelStructure(productStructure));
			}
		}
		renderText(JSONArray.fromObject(resultList).toString());
		logUtilDao.debugLog("查询", "制造质量管理：基础设置-产品BOM结构");
		return null;
	}
	
	@Action("list-move-structure")
	public String listMoveStructure() throws Exception {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<ProductBom> productBoms = productBomManager.getProductBoms(selParentId);
		if(deleteIds==null){
			deleteIds = "";
		}
		deleteIds = "," + deleteIds + ",";
		for(ProductBom productStructure : productBoms){
			if(deleteIds.indexOf("," + productStructure.getId()+",")==-1){
				resultList.add(convertProductStructure(productStructure));
			}
		}
		renderText(JSONArray.fromObject(resultList).toString());
		logUtilDao.debugLog("查询", "制造质量管理：基础设置-产品BOM结构");
		return null;
	}
	@Action("import-bom-form")
	public String importProductBomForm() throws Exception {
		if(selParentId != null){
			ActionContext.getContext().put("parent",productBomManager.getProductBom(selParentId));
		}else{
			ActionContext.getContext().put("parent",null);
		}
		return SUCCESS;
	}
	
	@Action("import-bom")
	@LogInfo(optType="导入",message="产品BOM")
	public String importBom() throws Exception {
		try {
			if(myFile != null){
				ProductBom parent = null;
				if(selParentId != null){
					parent = productBomManager.getProductBom(selParentId);
				}
				renderHtml(productBomManager.importBom(myFile,parent));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	
	@Action("download-template")
	public String downloadTemplate() throws Exception {
		productBomManager.createTemplate();
		return null;
	}
	
	@Action("exports")
	@LogInfo(optType="导出",message="产品BOM")
	public String exports() throws Exception {
		ProductBom parent = null;
		if(selParentId != null){
			parent = productBomManager.getProductBom(selParentId);
		}
		productBomManager.exports(parent);
		return null;
	}
	
	@Action("move-boms")
	public String moveProductBoms() throws Exception {
		try {
			productBomManager.moveProductBoms(deleteIds,selParentId);
			createMessage("操作成功!");
		} catch (Exception e) {
			createErrorMessage("操作失败:" + e.getMessage());
		}
		return null;
	}
	
	@Action("upate-all-parent-ids-and-level")
	public String upateAllParentIdsAndLevel() throws Exception {
		try {
			productBomManager.upateAllParentIds();
			createMessage("操作成功!");
		} catch (Exception e) {
			createErrorMessage("操作失败:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 转换产品结构至json对象
	 * @param productStructure
	 * @return
	 */
	private Map<String,Object> convertProductStructure(ProductBom productStructure){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("data",productStructure.getMaterielName());
		Map<String,Object> attrMap = new HashMap<String, Object>();
		attrMap.put("id",productStructure.getId());
		attrMap.put("level",productStructure.getMaterielLevel());
		attrMap.put("name",productStructure.getMaterielName());
		map.put("attr", attrMap);
		map.put("state","closed");
		return map;
	}
	
	private Map<String,Object> convertModelStructure(ProductBom productStructure){
		Map<String,Object> map = new HashMap<String, Object>();
		if(productStructure.getMaterielLevel() != 3){
			map.put("data",productStructure.getMaterielName());
			Map<String,Object> attrMap = new HashMap<String, Object>();
			attrMap.put("id",productStructure.getId());
			attrMap.put("level",productStructure.getMaterielLevel());
			attrMap.put("name",productStructure.getMaterielName());
			map.put("attr", attrMap);
			map.put("state","closed");
		}
		return map;
	}
	@Action("select-list-datas")
	public String getBomListDatas() throws Exception {
		try {
			String materielName=Struts2Utils.getRequest().getParameter("materielName");
			String materielCode=Struts2Utils.getRequest().getParameter("materielCode");
			page = productBomManager.searchByParams(page,materielName,materielCode);			
			StringBuilder json = new StringBuilder();
			json.append("{\"page\":\"");
			json.append(getValue(page.getPageNo()));
			json.append("\",\"total\":\"");
			json.append(getValue(page.getTotalPages()));
			json.append("\",\"records\":\"");
			json.append(getValue(page.getTotalCount()));
			json.append("\",\"rows\":");
			StringBuffer rows = new StringBuffer("[");
			for(ProductBom productBom : page.getResult()){
				if(rows.length()>1){
					rows.append(",");
				}
				rows.append(formatProductBom(productBom));
			}
			json.append(rows.append("]"));
			json.append("}");
			renderText(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String getValue(Object obj){
		if(obj==null){
			return "";
		}else{
			return obj+"";
		}
	}
	private String formatProductBom(ProductBom productBom){
		if(productBom.getMaterielName().indexOf("\"")>-1){
			productBom.setMaterielName(productBom.getMaterielName().replace("\"", " ")) ; 
			}
		StringBuffer row = new StringBuffer("{");
		row.append("\"materielCode\":\"" + getValue(productBom.getMaterielCode()) + "\"")
		.append(",\"id\":\"" + getValue(productBom.getId()) + "\"")
		.append(",\"materielName\":\"" + getValue(productBom.getMaterielName()) + "\"")
		.append(",\"materialType\":\"" + getValue(productBom.getMaterialType()) + "\"")
		.append(",\"materialTypeCode\":\"" + getValue(productBom.getMaterialTypeCode()) + "\"")
		.append(",\"materielModel\":\"" + getValue(productBom.getMaterielModel()) + "\"")
		.append(",\"units\":\"" + getValue(productBom.getUnits()) + "\"")
		.append(",\"remark\":\"" + getValue(productBom.getRemark()) + "\"")
		.append("}");
		return row.toString();
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

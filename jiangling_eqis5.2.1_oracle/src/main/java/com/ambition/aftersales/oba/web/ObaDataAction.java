package com.ambition.aftersales.oba.web;

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

import com.ambition.aftersales.baseinfo.service.CustomerListManager;
import com.ambition.aftersales.baseinfo.service.CustomerPlaceManager;
import com.ambition.aftersales.baseinfo.service.DefectionClassManager;
import com.ambition.aftersales.baseinfo.service.DefectionItemManager;
import com.ambition.aftersales.entity.CustomerList;
import com.ambition.aftersales.entity.CustomerPlace;
import com.ambition.aftersales.entity.DefectionClass;
import com.ambition.aftersales.entity.DefectionItem;
import com.ambition.aftersales.entity.ObaData;
import com.ambition.aftersales.entity.ObaDefectiveItem;
import com.ambition.aftersales.oba.service.ObaDataManager;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.supplier.develop.services.SupplierDevelopManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CheckMobileUtil;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exportexcel.ExcelListDynamicColumnValue;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.mms.base.DynamicColumnValues;
import com.norteksoft.mms.base.utils.view.DynamicColumnDefinition;
import com.norteksoft.mms.form.enumeration.DataType;
import com.norteksoft.mms.form.enumeration.EditControlType;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * 类名:OBA检验action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年8月31日 发布
 */
@Namespace("/aftersales/oba")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/aftersales/oba", type = "redirectAction")  ,
		   @Result(name = "mobile-input", location = "input.jsp",type="dispatcher")})
public class ObaDataAction extends BaseAction<ObaData>{
	public static final String MOBILEINPUT = "mobile-input";
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;//删除ID
	private Page<ObaData> page;
	private Page<ObaData> dynamicPage;
	private ObaData obaData;
	private String businessUnit;//所属事业部
	private String colCode;//列代号
	private List<DynamicColumnDefinition> dynamicColumn=new ArrayList<DynamicColumnDefinition>();
	@Autowired
	private SupplierDevelopManager supplierDevelopManager;	
	@Autowired
	private ObaDataManager obaDataManager;
	@Autowired
	private DefectionItemManager defectionItemManager;
	@Autowired
	private DefectionClassManager defectionClassManager;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
    private CustomerListManager customerListManager;
	@Autowired
	private CustomerPlaceManager customerPlaceManager;
	private Logger log=Logger.getLogger(this.getClass());
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
	public Page<ObaData> getPage() {
		return page;
	}
	public void setPage(Page<ObaData> page) {
		this.page = page;
	}
	public Page<ObaData> getDynamicPage() {
		return dynamicPage;
	}
	public void setDynamicPage(Page<ObaData> dynamicPage) {
		this.dynamicPage = dynamicPage;
	}
	public List<DynamicColumnDefinition> getDynamicColumn() {
		return dynamicColumn;
	}
	public void setDynamicColumn(List<DynamicColumnDefinition> dynamicColumn) {
		this.dynamicColumn = dynamicColumn;
	}
	
	public ObaData getObaData() {
		return obaData;
	}
	public void setObaData(ObaData obaData) {
		this.obaData = obaData;
	}
	
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	
	public String getColCode() {
		return colCode;
	}
	public void setColCode(String colCode) {
		this.colCode = colCode;
	}
	@Override
	public ObaData getModel() {
		return obaData;
	}
	@Action("delete")
	@LogInfo(optType="删除",message="保存OBA数据")
	public String delete() throws Exception {
		obaDataManager.deleteObaData(deleteIds);
		return null;
	}

	/**
	 * 新建页面
	 */
	@Override
	@Action("input")
	public String input() throws Exception {
		businessUnit = Struts2Utils.getParameter("businessUnit");
		List<ObaDefectiveItem> obaDefectiveItems=obaData.getObaDefectiveItems();
		Map<String, Map<String, String>> defectionMap=new HashMap<String, Map<String, String>>();
		Map<String,Object > valueMap=new HashMap<String, Object>();
		List<Map<String,String>> defectionList= new ArrayList<Map<String,String>>();
		for (ObaDefectiveItem obaDefectiveItem : obaDefectiveItems) {
			valueMap.put(obaDefectiveItem.getDefectionItemNo(), obaDefectiveItem.getDefectionItemValue());
		}
		List<Map<String,Object>> definitions = obaDataManager.queryDefectionsByBusinessUnit(businessUnit);
		for(Map<String,Object> map : definitions){
			if(defectionMap.containsKey(map.get("typeName").toString())){
				defectionMap.get(map.get("typeName").toString()).put(map.get("itemCode").toString(), map.get("itemName").toString());
			}else{
				Map<String, String> map1=new HashMap<String, String>();
				map1.put(map.get("itemCode").toString(), map.get("itemName").toString());
				map1.put("typeName", map.get("typeName").toString());
				defectionMap.put(map.get("typeName").toString(), map1);
			}						
		}
		for (Map<String, String> map : defectionMap.values()) {
			defectionList.add(map);
		}
		ActionContext.getContext().put("businessUnit", businessUnit);
		ActionContext.getContext().put("defectionList", defectionList);
		ActionContext.getContext().put("valueMap", valueMap);
		List<CustomerList> customerList = customerListManager.listAll();
        List<Option> customerListOptions = customerListManager.converExceptionLevelToList(customerList);		
        ActionContext.getContext().put("customerNames",customerListOptions);
        List<CustomerPlace> customerPlaceList = customerPlaceManager.getPlaces();
        List<Option> customerPlaceOptions = customerPlaceManager.converThirdLevelToList(customerPlaceList);
        ActionContext.getContext().put("customerPlaces",customerPlaceOptions);
        ActionContext.getContext().put("productStructures", ApiFactory.getSettingService().getOptionsByGroupCode("afs-product-structure"));
		//判断是否手机端发起的请求
		if(CheckMobileUtil.isMobile(Struts2Utils.getRequest())){
			ActionContext.getContext().put("userTreeHtml",generateDepartmentUserTree());
			return MOBILEINPUT;
		}else{
			return null;
		}
	} 
	/**
	 * 编辑保存
	 */
	@Action("edit-save")
	public String editSave() throws Exception {
		String vlrrItems = Struts2Utils.getParameter("vlrrItems");
		if(StringUtils.isNotEmpty(vlrrItems)){
			JSONArray childrenInfo = JSONArray.fromObject(vlrrItems);
			if(obaData.getObaDefectiveItems()==null){
				obaData.setObaDefectiveItems(new ArrayList<ObaDefectiveItem>());
			}else{
				obaData.getObaDefectiveItems().clear();
			}
			JSONObject js = childrenInfo.getJSONObject(0);
			for(Object key : js.keySet()){
				ObaDefectiveItem tr = new ObaDefectiveItem();
				tr.setCompanyId(ContextUtils.getCompanyId());
				tr.setCreatedTime(new Date());
				tr.setCreator(ContextUtils.getLoginName());
				tr.setCreatorName(ContextUtils.getUserName());
				tr.setModifiedTime(new Date());
				tr.setModifier(ContextUtils.getLoginName());
				tr.setModifierName(ContextUtils.getUserName());
				tr.setObaData(obaData);
				String value=js.get(key).toString();
				tr.setDefectionItemNo(key.toString());
				if(value!=null&&!"".equals(value)){
					tr.setDefectionItemValue(Integer.valueOf(value));
				}
				List<DefectionItem> list=defectionItemManager.listDefectionItem(key.toString());
				DefectionItem item=list.get(0);
				tr.setDefectionItemName(item.getDefectionItemName());
				tr.setDefectionClass(item.getDefectionClass().getDefectionClass());
				obaData.getObaDefectiveItems().add(tr);
			}
			obaData.setBusinessUnitName(businessUnit);
			obaDataManager.saveObaData(obaData);
		}
		//判断是否手机端发起的请求
		if(CheckMobileUtil.isMobile(Struts2Utils.getRequest())){
			ActionContext.getContext().put("userTreeHtml",generateDepartmentUserTree());
			input();
			return MOBILEINPUT;
		}else{
			return null;
		}
	} 

	@Action("list")
	@LogInfo(optType="新建",message="新建OBA数据台账")
	public String list() throws Exception {
		//所属事业部
		businessUnit = Struts2Utils.getParameter("businessUnit");
		List<Option> businessUnits = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits");
		if(StringUtils.isEmpty(businessUnit)){
			if(businessUnits.size()>0){
				businessUnit = businessUnits.get(0).getValue();
			}
		}
		ActionContext.getContext().put("businessUnits",businessUnits);
		StringBuffer colCodeBydefection = new StringBuffer();//列代号
		List<Map<String,Object>> definitions = obaDataManager.queryDefectionsByBusinessUnit(businessUnit);
		JSONArray groupHeaders = new JSONArray();
		JSONObject lastHeader = null;
		String lastTypeName = "";
		Integer typeCount = 0;
		for(Map<String,Object> map : definitions){
			DynamicColumnDefinition dynamicColumnDefinition =new DynamicColumnDefinition();
			String fieldName = "a" + map.get("itemCode");
			dynamicColumnDefinition.setColName((String)map.get("itemName"));
			dynamicColumnDefinition.setName(fieldName);
			dynamicColumnDefinition.setEditable(true);
			dynamicColumnDefinition.setVisible(true);
			dynamicColumnDefinition.setEdittype(EditControlType.TEXT);
			dynamicColumnDefinition.setType(DataType.INTEGER);
			dynamicColumn.add(dynamicColumnDefinition);
			dynamicColumnDefinition.setColWidth("100px");
			if(lastTypeName.equals(map.get("typeName"))){
				typeCount++;
			}else{
				lastTypeName = map.get("typeName").toString();
				if(lastHeader != null){
					lastHeader.put("numberOfColumns", typeCount);
					groupHeaders.add(lastHeader);
				}
				lastHeader = new JSONObject();
				lastHeader.put("startColumnName", fieldName);
				lastHeader.put("titleText",lastTypeName);
				typeCount=1;
			}
			//-------------拼装动态列代号（备用）-------------------
			colCodeBydefection.append(dynamicColumnDefinition.getName()+",");
		}
		if(lastHeader != null && typeCount > 0){
			lastHeader.put("numberOfColumns", typeCount);
			groupHeaders.add(lastHeader);
		}
		ActionContext.getContext().put("groupHeaders", groupHeaders);
		this.setColCode(colCodeBydefection.toString());
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			obaData=new ObaData();
			obaData.setCompanyId(ContextUtils.getCompanyId());
			obaData.setCreatedTime(new Date());
			obaData.setCreator(ContextUtils.getUserName());
			obaData.setModifiedTime(new Date());
			obaData.setModifier(ContextUtils.getLoginName());
			obaData.setModifierName(ContextUtils.getUserName());
			obaData.setObaDefectiveItems(new ArrayList<ObaDefectiveItem>());
			obaData.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			obaData=obaDataManager.getObaData(id);
			
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存OBA数据")
	public String save() throws Exception {
		obaData.getObaDefectiveItems().clear();
		List<DefectionClass> lists = defectionClassManager.getDefectionClassByBusinessUnit(businessUnit);
		int totalDefectiveItemValue = 0;//不良项累加值
		HashMap<String,Object> hs = new HashMap<String,Object>();
		//保存不良项值
		for(DefectionClass defectionClass :lists){
			List<DefectionItem> defectionCodes=defectionClass.getDefectionItems();
			for (DefectionItem defectionCode : defectionCodes) {
				ObaDefectiveItem defectiveItem = new ObaDefectiveItem();
				String defectiveItemValue = Struts2Utils.getParameter("a" + defectionCode.getDefectionItemNo());
				if(defectiveItemValue==null){
					continue;
				}
				if(defectiveItemValue!=null&&!"".equals(defectiveItemValue)){
					defectiveItem.setDefectionItemValue(Integer.valueOf(defectiveItemValue));
					totalDefectiveItemValue = totalDefectiveItemValue+Integer.valueOf(defectiveItemValue);//累加计算不良项值
				}
				defectiveItem.setDefectionClass(defectionClass.getDefectionClass());
				defectiveItem.setDefectionItemNo(defectionCode.getDefectionItemNo());
				defectiveItem.setDefectionItemName(defectionCode.getDefectionItemName());
				defectiveItem.setCompanyId(ContextUtils.getCompanyId());
				defectiveItem.setObaData(obaData);;					
				hs.put(defectiveItem.getDefectionItemNo(), defectiveItem.getDefectionItemName());
				obaData.getObaDefectiveItems().add(defectiveItem);
			}		
		}
		if(obaData.getUnqualifiedCount()!=totalDefectiveItemValue){//判断不良数值是否与不良项累加值相等
			createErrorMessage("数据有误，不良数量与不良项值累加数量不相等！");
		}else{
			obaData.setBusinessUnitName(businessUnit);
			obaDataManager.saveObaData(obaData);
			renderText(convertToJson(obaData, hs));	
		}			
		return null;
	}
	
	//返回json串
	@SuppressWarnings("rawtypes")
	public String convertToJson(ObaData obaData,HashMap hs){
		StringBuffer sb = new StringBuffer();
		sb.append(JsonParser.object2Json(obaData));
		sb.delete(sb.length()-1, sb.length());
		sb.append(",");
		sb.append(JsonParser.object2Json(hs).substring(1,JsonParser.object2Json(hs).length()));
		return sb.toString();
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		dynamicPage = obaDataManager.getListByBusinessUnit(dynamicPage, businessUnit);
		this.renderText(PageUtils.dynamicPageToJson(dynamicPage,new DynamicColumnValues(){
			public void addValuesTo(List<Map<String, Object>> result) {
				for(Map<String, Object> map:result){
					Long obaDataId=Long.valueOf(map.get("id").toString());
					ObaData obaData = obaDataManager.getObaData(obaDataId);
					for(ObaDefectiveItem oqcDefectiveItem:obaData.getObaDefectiveItems()){
						map.put("a" + oqcDefectiveItem.getDefectionItemNo(),oqcDefectiveItem.getDefectionItemValue());
					}
				}
			}
			
		}));
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出", message="OBA数据")
	public String export() throws Exception {
		Page<ObaData> page = new Page<ObaData>(65535);
		page = obaDataManager.getListByBusinessUnit(page,businessUnit);
		//缓存对应的不良明细
		final Map<String,String> itemMap = new HashMap<String, String>();
		obaDataManager.setDefectiveValuesForExport(itemMap);
		List<Map<String,Object>> definitions = obaDataManager.queryDefectionsByBusinessUnit(businessUnit);
		List<DynamicColumnDefinition>  dynamicColumnDefinitions  = new ArrayList<DynamicColumnDefinition>();
		for(Map<String,Object> map : definitions){
			DynamicColumnDefinition dynamicColumnDefinition =new DynamicColumnDefinition();
			String fieldName = "a" + map.get("itemCode");
			dynamicColumnDefinition.setColName((String)map.get("itemName"));
			dynamicColumnDefinition.setName(fieldName);
			dynamicColumnDefinition.setEditable(true);
			dynamicColumnDefinition.setVisible(true);
			dynamicColumnDefinition.setEdittype(EditControlType.TEXT);
			dynamicColumnDefinition.setType(DataType.INTEGER);
			dynamicColumnDefinitions.add(dynamicColumnDefinition);
		}
		//不良PPM格式化
		try {
			String fileFlags = ExcelUtil.exportListToExcel(page.getResult(), "OBA数据台账",
					Struts2Utils.getParameter("_list_code"), 
					dynamicColumnDefinitions, new ExcelListDynamicColumnValue() {
						public Map<String, String> getDynamicColumnValue(Object value, int rowNum,
								Map<String, String> valueMap) {
							ObaData report = (ObaData)value;
							String str = itemMap.get(report.getId()+"");
							if(str!=null){
								String strs[] = str.split(",");
								for(String ss : strs){
									String vals[] = ss.split(":");
									if(vals.length<2){
										continue;
									}
									valueMap.put(vals[0],vals[1]==null?"":vals[1].equals("null")?"":vals[1]);
								}
							}
							return valueMap;
						}
					}, obaDataManager.getObaDataDao().getSession());
			this.renderText(fileFlags);
		} catch (Exception e) {
			log.error("OBA数据台账导出失败!",e);
		}
		return null;
	}	
	public void prepareEditSave() throws Exception{
		prepareModel();
	}
 /**
	 * 生成部门用户
	 * @return
	 */
	public StringBuffer generateDepartmentUserTree(){
		List<com.norteksoft.acs.entity.organization.Department> allDepartments = supplierDevelopManager.queryAllDepartments();
		List<User> allUsers = supplierDevelopManager.queryAllUsers();
		StringBuffer userHtml = new StringBuffer();
		List<com.norteksoft.acs.entity.organization.Department> parentDepts = queryChildrens(allDepartments,null);
		for(com.norteksoft.acs.entity.organization.Department dept : parentDepts){
			generateHtml(userHtml, dept, allDepartments, allUsers);
		}
		//无部门用户
//					generateHtml(userHtml,null, allDepartments, allUsers);
		return userHtml;
	}
	private List<com.norteksoft.acs.entity.organization.Department> queryChildrens(List<com.norteksoft.acs.entity.organization.Department> allDepartments,Long parentId){
		List<com.norteksoft.acs.entity.organization.Department> children = new ArrayList<com.norteksoft.acs.entity.organization.Department>();
		for(com.norteksoft.acs.entity.organization.Department d : allDepartments){
			if(parentId==null){
				if(d.getParent()==null){
					children.add(d);
				}
			}else{
				if(d.getParent()!=null&&d.getParent().getId().equals(parentId)){
					children.add(d);
				}
			}
		}
		return children;
	}
	private void generateHtml(StringBuffer html,com.norteksoft.acs.entity.organization.Department dept,
			List<com.norteksoft.acs.entity.organization.Department> allDepartments,
			List<User> allUsers){
		List<User> users = queryUsersByDeptId(allUsers,dept==null?null:dept.getId());
		List<com.norteksoft.acs.entity.organization.Department> children = queryChildrens(allDepartments,dept==null?null:dept.getId());
		if(users.isEmpty()&&children.isEmpty()){
			//html.append("<p>"+dept.getName()+"</p>");
		}else{
			html.append("<li style=\"margin-left:20px;\">");
			html.append("<label><a href=\"javascript:;\" onclick=\"showdiv('"+(dept==null?"noId":dept.getName())+"')\" >"+(dept==null?"无部门用户":dept.getName())+"</a></label>");
			html.append("<div style=\"display:none;\" id="+(dept==null?"noId":dept.getName())+"><ul class=\"two\" style=\"margin-left:30px;\">");
			for(User user : users){
				html.append("<li><label><input  type=\"checkbox\" name='"+user.getName()+"' deptName="+(dept==null?"noId":dept.getName())+"  value='"+user.getLoginName()+"'><a  href=\"javascript:;\" >"+user.getName()+"</a></label></li>");
				
			}
			for(com.norteksoft.acs.entity.organization.Department child : children){
				generateHtml(html,child,allDepartments,allUsers);
			}
			html.append("</ul></div>");
			html.append("</li>");
		}
	}
	private List<User> queryUsersByDeptId(List<User> allUsers,Long deptId){
		List<User> users = new ArrayList<User>();
		for(User u : allUsers){
			if(deptId==null){
				if(u.getMainDepartmentId()==null){
					users.add(u);
				}
			}else{
				if(u.getMainDepartmentId()!=null&&u.getMainDepartmentId().equals(deptId)){
					users.add(u);
				}
			}
		}
		return users;
	}		
	/**
	 * 
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

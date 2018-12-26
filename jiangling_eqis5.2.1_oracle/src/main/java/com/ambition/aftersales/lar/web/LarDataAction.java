package com.ambition.aftersales.lar.web;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.aftersales.baseinfo.service.CustomerListManager;
import com.ambition.aftersales.baseinfo.service.CustomerPlaceManager;
import com.ambition.aftersales.baseinfo.service.DefectionClassManager;
import com.ambition.aftersales.baseinfo.service.LarTargetManager;
import com.ambition.aftersales.entity.CustomerList;
import com.ambition.aftersales.entity.CustomerPlace;
import com.ambition.aftersales.entity.DefectionClass;
import com.ambition.aftersales.entity.DefectionItem;
import com.ambition.aftersales.entity.LarData;
import com.ambition.aftersales.entity.LarDefectiveItem;
import com.ambition.aftersales.entity.VlrrDefectiveItem;
import com.ambition.aftersales.lar.service.LarDataManager;
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
 * 类名:LAR批次合格率action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Namespace("/aftersales/lar")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/aftersales/lar", type = "redirectAction")
,@Result(name = "mobile-input", location = "input.jsp",type="dispatcher") })//新增
public class LarDataAction extends BaseAction<LarData>{
	public static final String MOBILEINPUT = "mobile-input";//新增
	private static final long serialVersionUID = 1L;
	private LarData larData;
	private File myFile;
	private String businessUnit;//所属事业部
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private SupplierDevelopManager supplierDevelopManager;
	@Autowired
	private DefectionClassManager defectionClassManager;
	@Autowired
	private CustomerPlaceManager customerPlaceManager;
	@Autowired
	private CustomerListManager customerListManager;
	@Autowired
	private LarTargetManager larTargetManager;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private Page<LarData> page;
	private Page<LarData> dynamicPage;
	private String colCode;//列代号
	private List<DynamicColumnDefinition> dynamicColumn=new ArrayList<DynamicColumnDefinition>();
	@Autowired
	private LarDataManager larDataManager;
	public String getColCode() {
		return colCode;
	}
	public Page<LarData> getDynamicPage() {
		return dynamicPage;
	}
	public void setDynamicPage(Page<LarData> dynamicPage) {
		this.dynamicPage = dynamicPage;
	}
	public void setColCode(String colCode) {
		this.colCode = colCode;
	}

	@Override
	public LarData getModel() {
		return larData;
	}
	@Action("delete")
	@LogInfo(optType="删除",message="删除LAR数据")
	public String delete() throws Exception {
		try {
			larDataManager.deleteLarData(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除LAR数据，编号："+deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除LAR数据信息失败",e);
		}
		return null;
	}

	@Override
	@Action("input")
	public String input() throws Exception {
		/*String editId = Struts2Utils.getParameter("editId");
		if(editId!=null&&!"".equals(editId)){
			vlrrData=vlrrDataManager.getVlrrData(Long.valueOf(editId));
		}*/
		businessUnit = Struts2Utils.getParameter("businessUnit");
		List<LarDefectiveItem> larDefectiveItems=larData.getLarDefectiveItems();
		Map<String, Map<String, String>> defectionMap=new HashMap<String, Map<String, String>>();
		Map<String,Object > valueMap=new HashMap<String, Object>();
		List<Map<String,String>> defectionList= new ArrayList<Map<String,String>>();
		for (LarDefectiveItem larDefectiveItem : larDefectiveItems) {
			valueMap.put(larDefectiveItem.getDefectionItemNo(), larDefectiveItem.getDefectionItemValue());
		}
		List<Map<String,Object>> definitions = larDataManager.queryDefectionsByBusinessUnit(businessUnit);
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
	public StringBuffer generateDepartmentUserTree(){
		List<com.norteksoft.acs.entity.organization.Department> allDepartments = supplierDevelopManager.queryAllDepartments();
		List<User> allUsers = supplierDevelopManager.queryAllUsers();
		StringBuffer userHtml = new StringBuffer();
		List<com.norteksoft.acs.entity.organization.Department> parentDepts = queryChildrens(allDepartments,null);
		for(com.norteksoft.acs.entity.organization.Department dept : parentDepts){
			generateHtml(userHtml, dept, allDepartments, allUsers);
		}
		//无部门用户
//				generateHtml(userHtml,null, allDepartments, allUsers);
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
	
	
	
	
	
	
	
	
	
	
	
	@Action("list")
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
		List<Map<String,Object>> definitions = larDataManager.queryDefectionsByBusinessUnit(businessUnit);
		JSONArray groupHeaders = new JSONArray();
		JSONObject lastHeader = null;
		String lastTypeName = "";
		Integer typeCount = 0;
		for(Map<String,Object> map : definitions){
			DynamicColumnDefinition dynamicColumnDefinition =new DynamicColumnDefinition();
			String fieldName = "a" + map.get("itemCode").toString().trim();
			dynamicColumnDefinition.setColName((String)map.get("itemName"));
			dynamicColumnDefinition.setName(fieldName);
			dynamicColumnDefinition.setEditable(true);
			dynamicColumnDefinition.setVisible(true);
			dynamicColumnDefinition.setEdittype(EditControlType.TEXT);
			dynamicColumnDefinition.setType(DataType.INTEGER);
			dynamicColumn.add(dynamicColumnDefinition);
			dynamicColumnDefinition.setColWidth("55px");
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
			larData=new LarData();
			larData.setCompanyId(ContextUtils.getCompanyId());
			larData.setCreatedTime(new Date());
			larData.setCreator(ContextUtils.getUserName());
			larData.setModifiedTime(new Date());
			larData.setModifier(ContextUtils.getLoginName());
			larData.setModifierName(ContextUtils.getUserName());
			larData.setBusinessUnitName(ContextUtils.getSubCompanyName());
			larData.setLarDefectiveItems(new ArrayList<LarDefectiveItem>());
			larData.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			larData=larDataManager.getLarData(id);
		}
		
	}
	
	//新增
	@Action("save")
	@LogInfo(optType="保存",message="保存LAR数据信息")
	public String save() throws Exception {
		larData.getLarDefectiveItems().clear();
		List<DefectionClass> lists = defectionClassManager.getDefectionClassByBusinessUnit(businessUnit);
		int totalDefectiveItemValue = 0;//不良项累加值
		HashMap<String,Object> hs = new HashMap<String,Object>();
		//保存不良项值
		for(DefectionClass defectionClass :lists){
			List<DefectionItem> defectionCodes=defectionClass.getDefectionItems();
			for (DefectionItem defectionCode : defectionCodes) {
				LarDefectiveItem defectiveItem = new LarDefectiveItem();
				String defectiveItemValue = Struts2Utils.getParameter("a" + defectionCode.getDefectionItemNo());
				if(defectiveItemValue==null||"".equals(defectiveItemValue)){
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
				defectiveItem.setLarData(larData);;					
				hs.put(defectiveItem.getDefectionItemNo(), defectiveItem.getDefectionItemName());
				larData.getLarDefectiveItems().add(defectiveItem);
			}		
		}
		if(larData.getUnqualifiedCount()!=totalDefectiveItemValue){//判断不良数值是否与不良项累加值相等
			createErrorMessage("数据有误，不良数量与不良项值累加数量不相等！");
		}else{
			larData.setBusinessUnitName(businessUnit);
			larDataManager.saveLarData(larData);
			renderText(convertToJson(larData, hs));
		}				
		return null;
	}
	
	//返回json串
	@SuppressWarnings("rawtypes")
	public String convertToJson(LarData larData,HashMap hs){
		StringBuffer sb = new StringBuffer();
		sb.append(JsonParser.object2Json(larData));
		sb.delete(sb.length()-1, sb.length());
		sb.append(",");
		sb.append(JsonParser.object2Json(hs).substring(1,JsonParser.object2Json(hs).length()));
		return sb.toString();
	}
	
	
	//新增
	@Action("list-datas")
	public String listDatas() throws Exception {
		dynamicPage = larDataManager.getListByBusinessUnit(dynamicPage, businessUnit);
		this.renderText(PageUtils.dynamicPageToJson(dynamicPage,new DynamicColumnValues(){
			public void addValuesTo(List<Map<String, Object>> result) {
				for(Map<String, Object> map:result){
					Long larDataId=Long.valueOf(map.get("id").toString());
					LarData larData =larDataManager.getLarData(larDataId);
					for(LarDefectiveItem oqcDefectiveItem:larData.getLarDefectiveItems()){
						map.put("a" + oqcDefectiveItem.getDefectionItemNo(),oqcDefectiveItem.getDefectionItemValue());
					}
				}
			}
			
		}));
		return null;
	}
	
	//新加
	  @Action("import")
	  public String imports() throws Exception
	  {
	    return SUCCESS;
	  }
	  
	  @Action("import-datas")
	  @LogInfo(optType="导入", message="导入LAR数据")
	  public String importDatas() throws Exception {
		  //System.out.println("1111");
	    try {
	      if (this.myFile != null) {
	        String businessUnit = Struts2Utils.getParameter("businessUnit");
	        renderHtml(this.larDataManager.importDatas(this.myFile, businessUnit));
	      }
	    } catch (Exception e) {
	      renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
	    
	    }
	    return null;
	  }
	  
	  
	//新增
	  @Action("download-template")
	  @LogInfo(optType="下载", message="下载LAR数据导入模板")
	  public String downloadTemplate()
	    throws Exception
	  {
	    InputStream inputStream = null;
	    try {
	      inputStream = getClass().getClassLoader().getResourceAsStream("template/report/afs-lar-template.xlsx");
	      Workbook book = WorkbookFactory.create(inputStream);
	      String fileName = "LAR数据导入模板.xls";
	      byte[] byname = fileName.getBytes("gbk");
	      fileName = new String(byname, "8859_1");
	      HttpServletResponse response = Struts2Utils.getResponse();
	      response.reset();
	      response.setContentType("application/vnd.ms-excel");
	      response.setHeader("Content-Disposition", 
	        "attachment; filename=\"" + fileName + "\"");

	      book.write(response.getOutputStream());
	    } catch (Exception e) {
	      this.log.error("下载LAR数据导入模板失败!", e);
	    } finally {
	      if (inputStream != null) {
	        inputStream.close();
	      }
	    }
	    return null;
	  }
	
/*	@Action("export")
	@LogInfo(optType="导出", message="LAR数据")
	public String export() throws Exception {
		try {
			Page<LarData> page = new Page<LarData>(65535);
			page = larDataManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"LAR数据台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出LAR数据信息失败",e);
		}
		return null;
	}*/
	
		@Action("export")
		@LogInfo(optType="导出", message="LAR数据")
		public String export() throws Exception {
			try {
				Page<LarData> page = new Page<LarData>(65535);
				Map<String,String> fieldMap = new HashMap<String, String>();
				page = larDataManager.search(page);
				List<Map<String,Object>> definitions = larDataManager.queryDefectionsByBusinessUnit(businessUnit);
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
			    String fileFlags = ExcelUtil.exportListToExcel(page.getResult(),"LAR数据台账",Struts2Utils.getParameter("_list_code"), 
			    		dynamicColumnDefinitions, new ExcelListDynamicColumnValue(){
							@Override
							public Map<String, String> getDynamicColumnValue(
									Object obj, int rowNum,
									Map<String, String> map) {
								LarData larData = (LarData) obj;
								for(LarDefectiveItem oqcDefectiveItem:larData.getLarDefectiveItems()){
									map.put("a" + oqcDefectiveItem.getDefectionItemNo(),oqcDefectiveItem.getDefectionItemValue()==null?"":oqcDefectiveItem.getDefectionItemValue().toString());
								}
								return map;
							}
			    	
			    }, larDataManager.getLarDataDao().getSession());
	            this.renderText(fileFlags);
			} catch (Exception e) {
				createErrorMessage(e.getMessage());
				log.error("导出VLRR数据失败",e);
			}
			return null;
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
	public Page<LarData> getPage() {
		return page;
	}
	public void setPage(Page<LarData> page) {
		this.page = page;
	}
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	public List<DynamicColumnDefinition> getDynamicColumn() {
		return dynamicColumn;
	}
	public void setDynamicColumn(List<DynamicColumnDefinition> dynamicColumn) {
		this.dynamicColumn = dynamicColumn;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	
}

package com.ambition.carmfg.oqc.web;

import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.defectioncode.service.DefectionTypeManager;
import com.ambition.carmfg.entity.DefectionCode;
import com.ambition.carmfg.entity.DefectionType;
import com.ambition.carmfg.entity.OqcDefectiveItem;
import com.ambition.carmfg.entity.OqcInspection;
import com.ambition.carmfg.oqc.service.OqcInspectionManager;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.supplier.entity.ProductPartReport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exportexcel.ExcelListDynamicColumnValue;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.mms.base.DynamicColumnValues;
import com.norteksoft.mms.base.utils.view.DynamicColumnDefinition;
import com.norteksoft.mms.form.enumeration.DataType;
import com.norteksoft.mms.form.enumeration.EditControlType;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * 类名:OQC检验action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年8月31日 发布
 */
@Namespace("/carmfg/oqc")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/carmfg/oqc", type = "redirectAction") })
public class OqcInspectionAction extends BaseAction<OqcInspection>{

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;//删除ID
	private Page<OqcInspection> page;
	private Page<OqcInspection> dynamicPage;
	private OqcInspection oqcInspection;
	private String businessUnit;//所属事业部
	private String processSection;//制程区段
	private String colCode;//列代号
	private File myFile;
	private List<DynamicColumnDefinition> dynamicColumn=new ArrayList<DynamicColumnDefinition>();
	@Autowired
	private OqcInspectionManager oqcInspectionManager;
	@Autowired
	private DefectionTypeManager defectionTypeManager;
	@Autowired
	private LogUtilDao logUtilDao;
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
	public Page<OqcInspection> getPage() {
		return page;
	}
	public void setPage(Page<OqcInspection> page) {
		this.page = page;
	}
	public Page<OqcInspection> getDynamicPage() {
		return dynamicPage;
	}
	public void setDynamicPage(Page<OqcInspection> dynamicPage) {
		this.dynamicPage = dynamicPage;
	}
	public List<DynamicColumnDefinition> getDynamicColumn() {
		return dynamicColumn;
	}
	public void setDynamicColumn(List<DynamicColumnDefinition> dynamicColumn) {
		this.dynamicColumn = dynamicColumn;
	}
	
	public OqcInspection getOqcInspection() {
		return oqcInspection;
	}
	public void setOqcInspection(OqcInspection oqcInspection) {
		this.oqcInspection = oqcInspection;
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
	
	
	public String getProcessSection() {
		return processSection;
	}
	public void setProcessSection(String processSection) {
		this.processSection = processSection;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	@Override
	public OqcInspection getModel() {
		return oqcInspection;
	}
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		String deleteNos=oqcInspectionManager.deleteOqcInspection(deleteIds);
		//日志消息,方便跟踪删除记录
		Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"删除OQC检验数据,机种为【" + deleteNos + "】!");
		return null;
	}

	@Action("input")
	public String editInput() throws Exception {
		oqcInspection = oqcInspectionManager.getOqcInspection(id);
//		List<OqcDefectiveItem> oqcDefectiveItems = oqcInspection.getOqcDefectiveItems();
		List<OqcDefectiveItem> oqcDefectiveItems = oqcInspectionManager.getOqcDefecitveItemsByValue(oqcInspection);
		if(oqcDefectiveItems==null||oqcDefectiveItems.size()==0){
			OqcDefectiveItem item = new OqcDefectiveItem();
			oqcDefectiveItems.add(item);
		}
		ActionContext.getContext().put("oqcDefectiveItems",oqcDefectiveItems);
		return SUCCESS;
	}
	@Action("edit-save")
	@LogInfo(optType="保存",message="保存OQC检验")
	public String editSave() throws Exception {
		oqcInspection = oqcInspectionManager.getOqcInspection(id);
		try {
			oqcInspectionManager.saveOqcInspectionByParams(oqcInspection);
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
		}	
		oqcInspection = oqcInspectionManager.getOqcInspection(id);
//		List<OqcDefectiveItem> oqcDefectiveItems = oqcInspection.getOqcDefectiveItems();
		List<OqcDefectiveItem> oqcDefectiveItems = oqcInspectionManager.getOqcDefecitveItemsByValue(oqcInspection);
		if(oqcDefectiveItems==null||oqcDefectiveItems.size()==0){
			OqcDefectiveItem item = new OqcDefectiveItem();
			oqcDefectiveItems.add(item);
		}
		ActionContext.getContext().put("oqcDefectiveItems",oqcDefectiveItems);
		return "input";
	}
	@Action("list")
	public String list() throws Exception {
		//所属事业部
		businessUnit = Struts2Utils.getParameter("businessUnit");
		processSection = Struts2Utils.getParameter("processSection");
		List<Option> businessUnits = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits");
		List<Option> processSections =ApiFactory.getSettingService().getOptionsByGroupCode("processSections");
		if(StringUtils.isEmpty(businessUnit)){
			if(businessUnits.size()>0){
				businessUnit = businessUnits.get(0).getValue();
			}
		}
		if(StringUtils.isEmpty(processSection)){
			if(processSections.size()>0){
				processSection = processSections.get(0).getValue();
			}
		}
		ActionContext.getContext().put("businessUnits",businessUnits);
		ActionContext.getContext().put("processSections",ApiFactory.getSettingService().getOptionsByGroupCode("processSections"));
		StringBuffer colCodeBydefection = new StringBuffer();//列代号
		List<Map<String,Object>> definitions = oqcInspectionManager.queryDefectionsByBusinessUnit(businessUnit);
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
		HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        request.getSession().setAttribute("dynamicColumn", dynamicColumn);
		return SUCCESS;
	}
	@Action("select-product-list")
	public String selectList() throws Exception {
		//所属事业部
		businessUnit = Struts2Utils.getParameter("businessUnit");
		processSection = Struts2Utils.getParameter("processSection");
		List<Option> businessUnits = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits");
		List<Option> processSections =ApiFactory.getSettingService().getOptionsByGroupCode("processSections");
		if(StringUtils.isEmpty(businessUnit)){
			if(businessUnits.size()>0){
				businessUnit = businessUnits.get(0).getValue();
			}
		}
		if(StringUtils.isEmpty(processSection)){
			if(processSections.size()>0){
				processSection = processSections.get(0).getValue();
			}
		}
		ActionContext.getContext().put("businessUnits",businessUnits);
		ActionContext.getContext().put("processSections",ApiFactory.getSettingService().getOptionsByGroupCode("processSections"));
		return SUCCESS;
	}
	@Action("list-select-datas")
	public String listSelectDatas() throws Exception {
		try{
			page = oqcInspectionManager.searchByPage(page,businessUnit);
			renderText(PageUtils.pageToJson(page,"MFG_OQC_INSPECTION"));
		}catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			oqcInspection=new OqcInspection();
			oqcInspection.setCompanyId(ContextUtils.getCompanyId());
			oqcInspection.setCreatedTime(new Date());
			oqcInspection.setCreator(ContextUtils.getUserName());
			oqcInspection.setModifiedTime(new Date());
			oqcInspection.setModifier(ContextUtils.getLoginName());
			oqcInspection.setModifierName(ContextUtils.getUserName());
			oqcInspection.setOqcDefectiveItems(new ArrayList<OqcDefectiveItem>());
			oqcInspection.setBusinessUnitName(ContextUtils.getSubCompanyName());
			oqcInspection.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			oqcInspection=oqcInspectionManager.getOqcInspection(id);
			oqcInspection.getOqcDefectiveItems().clear();
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存OQC检验")
	public String save() throws Exception {
		List<DefectionType> lists = defectionTypeManager.getDefectionTypeByBusinessUnit(businessUnit);
		int totalDefectiveItemValue = 0;//不良项累加值
		HashMap<String,Object> hs = new HashMap<String,Object>();
		//List<OqcDefectiveItem> list=new ArrayList<OqcDefectiveItem>();
		//保存不良项值
		for(DefectionType defectionType :lists){
			List<DefectionCode> defectionCodes=defectionType.getDefectionCodes();
			for (DefectionCode defectionCode : defectionCodes) {
				OqcDefectiveItem defectiveItem = new OqcDefectiveItem();
				String defectiveItemValue = Struts2Utils.getParameter("a" + defectionCode.getDefectionCodeNo());
				if(defectiveItemValue==null){
					continue;
				}
				if(defectiveItemValue!=null&&!"".equals(defectiveItemValue)){
					defectiveItem.setDefectionCodeValue(Integer.valueOf(defectiveItemValue));
					totalDefectiveItemValue = totalDefectiveItemValue+Integer.valueOf(defectiveItemValue);//累加计算不良项值
				}
				defectiveItem.setDefectionTypeNo(defectionType.getDefectionTypeNo());
				defectiveItem.setDefectionTypeName(defectionType.getDefectionTypeName());
				defectiveItem.setDefectionCodeNo(defectionCode.getDefectionCodeNo());
				defectiveItem.setDefectionCodeName(defectionCode.getDefectionCodeName());
				defectiveItem.setCompanyId(ContextUtils.getCompanyId());
				defectiveItem.setOqcInspection(oqcInspection);;					
				hs.put(defectiveItem.getDefectionCodeNo(), defectiveItem.getDefectionCodeName());
				oqcInspection.getOqcDefectiveItems().add(defectiveItem);
			}		
		}
		if("欧菲科技-CCM".equals(ContextUtils.getCompanyName())){
			if(oqcInspection.getUnQualityCount()!=totalDefectiveItemValue){//判断不良数值是否与不良项累加值相等
				createErrorMessage("数据有误，不良数量与不良项值累加数量不相等！");
			}else{
				oqcInspection.setBusinessUnitName(businessUnit);
				//oqcInspection.setProcessSection(processSection);
				try {
					oqcInspectionManager.saveOqcInspection(oqcInspection);
					renderText(convertToJson(oqcInspection, hs));	
				} catch (Exception e) {
					createErrorMessage(e.getMessage());
				}	
			}
		}else{
			oqcInspection.setBusinessUnitName(businessUnit);
			//oqcInspection.setProcessSection(processSection);
			try {
				oqcInspectionManager.saveOqcInspection(oqcInspection);
				renderText(convertToJson(oqcInspection, hs));	
			} catch (Exception e) {
				createErrorMessage(e.getMessage());
			}		
		}
		
//		if(oqcInspection.getUnQualityCount()!=totalDefectiveItemValue){//判断不良数值是否与不良项累加值相等
//			createErrorMessage("数据有误，不良数量与不良项值累加数量不相等！");
//		}else{
//			oqcInspection.setBusinessUnitName(businessUnit);
//			//oqcInspection.setProcessSection(processSection);
//			try {
//				oqcInspectionManager.saveOqcInspection(oqcInspection);
//				renderText(convertToJson(oqcInspection, hs));	
//			} catch (Exception e) {
//				createErrorMessage(e.getMessage());
//			}						
//		}	
		return null;
	}
	
	//返回json串
	@SuppressWarnings("rawtypes")
	public String convertToJson(OqcInspection oqcInspection,HashMap hs){
		StringBuffer sb = new StringBuffer();
		sb.append(JsonParser.object2Json(oqcInspection));
		sb.delete(sb.length()-1, sb.length());
		sb.append(",");
		sb.append(JsonParser.object2Json(hs).substring(1,JsonParser.object2Json(hs).length()));
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	@Action("list-datas")
	public String listDatas() throws Exception {
		dynamicPage = oqcInspectionManager.getListByBusinessUnit(dynamicPage, businessUnit);
		HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
		dynamicColumn =  (List<DynamicColumnDefinition>) request.getSession().getAttribute("dynamicColumn");
		//数据转换
		if(StringUtils.isNotEmpty(dynamicColumn.toString())){//有扩展列
			String str = convertReportPageDatas(dynamicPage);
			this.renderText(str.replaceAll("\\n",""));
		}else{
			renderText(PageUtils.pageToJson(dynamicPage));
		}
		/*this.renderText(PageUtils.dynamicPageToJson(dynamicPage,new DynamicColumnValues(){
			public void addValuesTo(List<Map<String, Object>> result) {
				for(Map<String, Object> map:result){
					Long oqcInspectionId=Long.valueOf(map.get("id").toString());
					OqcInspection oqcInspection = oqcInspectionManager.getOqcInspection(oqcInspectionId);
					for(OqcDefectiveItem oqcDefectiveItem:oqcInspection.getOqcDefectiveItems()){
						String defectionCodeNo=oqcDefectiveItem.getDefectionCodeNo();
						defectionCodeNo=defectionCodeNo.trim();
						map.put("a" + defectionCodeNo,oqcDefectiveItem.getDefectionCodeValue());
					}
				}
			}
			
		}));*/
		return null;
	}
	/**
	  * 方法名: 处理对象在台帐查询数据时不良项目数据格式化
	  * <p>功能说明：</p>
	  * @return
	 */
	private String convertReportPageDatas(Page<OqcInspection> reportPage){
		List<String> idStrs = new ArrayList<String>();
		String idStr = "";
		for(OqcInspection report : reportPage.getResult()){
			if(idStr.length()>0){
				idStr += ",";
			}
			idStr += report.getId();
			if(idStr.length()>1000){
				idStrs.add(idStr);
				idStr = "";
			}
		}
		if(idStr.length()>0){
			idStrs.add(idStr);
		}
		final Map<String,String> itemMap = new HashMap<String, String>();
		for(String ids : idStrs){
			oqcInspectionManager.setDefectiveValues(ids, itemMap);
		}
		return PageUtils.dynamicPageToJson(reportPage, new DynamicColumnValues() {
			@Override
			public void addValuesTo(List<Map<String, Object>> list) {
				for(Map<String,Object> objMap : list){
					String itemStrs = itemMap.get(objMap.get("id")+"");
					if(StringUtils.isNotEmpty(itemStrs)){
						String strs[] = itemStrs.split(",");
						for(String str : strs){
							String[] flags = str.split(":");
							String key = flags[0];
							String value = flags[1]==null?"":flags[1];
							objMap.put(key,value.equals("null")?"":value);
						}
					}
				}
			}
		});
	}
	@Action("export")
	public String export() throws Exception {
		/*try {
			Page<OqcInspection> page = new Page<OqcInspection>(65535);
			page = oqcInspectionManager.search(page);
			List<Map<String,Object>> definitions = oqcInspectionManager.queryDefectionsByBusinessUnit(businessUnit);
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
		    String fileFlags = ExcelUtil.exportListToExcel(page.getResult(),"OQC检验台账",Struts2Utils.getParameter("_list_code"), 
		    		dynamicColumnDefinitions, new ExcelListDynamicColumnValue(){
						@Override
						public Map<String, String> getDynamicColumnValue(
								Object obj, int rowNum,
								Map<String, String> map) {
							OqcInspection oqcInspection = (OqcInspection) obj;
							for(OqcDefectiveItem oqcDefectiveItem:oqcInspection.getOqcDefectiveItems()){
								map.put("a" + oqcDefectiveItem.getDefectionCodeNo(),oqcDefectiveItem.getDefectionCodeValue()==null?"":oqcDefectiveItem.getDefectionCodeValue().toString());
							}
							return map;
						}
		    	
		    }, oqcInspectionManager.getOqcInspectionDao().getSession());
            this.renderText(fileFlags);
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出ORT计划失败",e);
		}*/
		Page<OqcInspection> page = new Page<OqcInspection>(65535);
		page = oqcInspectionManager.getListByBusinessUnit(page,businessUnit);
		//缓存对应的不良明细
		final Map<String,String> itemMap = new HashMap<String, String>();
		StringBuffer ids = new StringBuffer();
		oqcInspectionManager.setDefectiveValuesForExport(itemMap);
		List<Map<String,Object>> definitions = oqcInspectionManager.queryDefectionsByBusinessUnit(businessUnit);
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
			String fileFlags = ExcelUtil.exportListToExcel(page.getResult(), "OQC检验台账",
					Struts2Utils.getParameter("_list_code"), 
					dynamicColumnDefinitions, new ExcelListDynamicColumnValue() {
						public Map<String, String> getDynamicColumnValue(Object value, int rowNum,
								Map<String, String> valueMap) {
							OqcInspection report = (OqcInspection)value;
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
					}, oqcInspectionManager.getOqcInspectionDao().getSession());
			Date lastDate = new Date();
			this.renderText(fileFlags);
		} catch (Exception e) {
			log.error("OQC检验台账导出失败!",e);
		}
		return null;
	}
	@Action("export-most")
	public String exportMost() throws Exception {
		try {
			Page<OqcInspection> page = new Page<OqcInspection>(65535);
			page = oqcInspectionManager.getListByBusinessUnit(page, businessUnit);
			List<OqcDefectiveItem> suns = new ArrayList<OqcDefectiveItem>();
			List<OqcInspection> lists = page.getResult();
			for(int i=0;i<lists.size();i++){
				OqcInspection o = lists.get(i);
				/*suns = o.getOqcDefectiveItems();
				int k=1;
				for(int j=1;j<=suns.size();j++){
					if(k>5){
						break;
					}
					if(suns.get(j-1).getDefectionCodeValue()!=null){
						setProperty(o, "defectiveItem"+k, suns.get(j-1).getDefectionCodeName());
						k++;
					}
				}*/
				suns = o.getOqcDefectiveItems();
				String itemAll = "";
				for(OqcDefectiveItem sun : suns){
					if(sun.getDefectionCodeValue()!=null){
						if(itemAll.length()==0){
							itemAll = sun.getDefectionTypeName() + "-"+ sun.getDefectionCodeName() + ":" + sun.getDefectionCodeValue();
						}else{
							itemAll += ";"+sun.getDefectionTypeName() + "-"+ sun.getDefectionCodeName() + ":" + sun.getDefectionCodeValue();
						}
						
					}
				}
				o.setOqcDefectiveItemStr(itemAll);
			}
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page, "MFG_OQC_INSPECTION"),"OQC检验台账"));
		} catch (Exception e) {
			log.error("OQC检验台账导出失败!",e);
		}
		return null;
	}
	@Action("import")
	public String imports() throws Exception {
		return SUCCESS;
	}
	
	@Action("import-datas")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				String businessUnit = Struts2Utils.getParameter("businessUnit");
				renderHtml(oqcInspectionManager.importDatas(myFile,businessUnit));				
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	public void prepareEditSave() throws Exception{
		prepareModel();
	}
	/**
	  * 方法名: 下载检设备参数模板
	  * <p>功能说明：下载检验标准的模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download-template")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			if("欧菲科技-CCM".equals(ContextUtils.getCompanyName())){
				inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/mfg-oqc-template.xlsx");
			}else{
				inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/mfg-oqc-template-sq.xlsx");
			}			
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "OQC检验台账导入模板.xls";
			byte byname[] = fileName.getBytes("gbk");
			fileName = new String(byname, "8859_1");
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append(fileName).append("\"")
					.toString());
			book.write(response.getOutputStream());
		}catch (Exception e) {
			log.error("导出失败!",e);
		} finally{
			if(inputStream != null){
				inputStream.close();
			}
		}
		return null;
	}
	/**
	 * 复制OQC台账
	 * @return
	 * @throws Exception
	 */
	@Action("copy")
	@LogInfo(optType="复制",message="复制OQC台账")
	public String copy() throws Exception {
		try {
			oqcInspection=oqcInspectionManager.getOqcInspection(id);
			OqcInspection newOqcInspection=new OqcInspection();
			newOqcInspection.setEnterpriseGroup(oqcInspection.getEnterpriseGroup());
			newOqcInspection.setProcessSection(oqcInspection.getProcessSection());
			newOqcInspection.setInspectionDate(oqcInspection.getInspectionDate());
			newOqcInspection.setClassGroup(oqcInspection.getClassGroup());
			newOqcInspection.setInspectionBatchNo(oqcInspection.getInspectionBatchNo());
			newOqcInspection.setModel(oqcInspection.getModel());
			newOqcInspection.setCustomer(oqcInspection.getCustomer());
			newOqcInspection.setInspectionMan(ContextUtils.getUserName());
			newOqcInspection.setIsSendMail("否");
			newOqcInspection.setBusinessUnitName(oqcInspection.getBusinessUnitName());
			newOqcInspection.setBusinessUnitCode(oqcInspection.getBusinessUnitCode());
			newOqcInspection.setCompanyId(ContextUtils.getCompanyId());
			newOqcInspection.setCreatedTime(new Date());
			newOqcInspection.setCreator(ContextUtils.getUserName());
			newOqcInspection.setModifiedTime(new Date());
			newOqcInspection.setModifier(ContextUtils.getLoginName());
			newOqcInspection.setModifierName(ContextUtils.getUserName());
			newOqcInspection.setOqcDefectiveItems(new ArrayList<OqcDefectiveItem>());
			oqcInspectionManager.saveOqcInspection(newOqcInspection);
 			createMessage("复制OQC台账成功");
		} catch (Exception e) {
			logger.error("复制OQC台账失败：", e);
			addActionError("复制OQC台账失败");
			createErrorMessage("复制OQC台账失败："+e.getMessage()); 
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
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	public void createMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}	
}

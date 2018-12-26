package com.ambition.carmfg.checkinspection.web;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.carmfg.baseinfo.service.InspectionPointManager;
import com.ambition.carmfg.bom.service.ProductBomManager;
import com.ambition.carmfg.checkinspection.service.MfgCheckInspectionReportManager;
import com.ambition.carmfg.entity.InspectionPoint;
import com.ambition.carmfg.entity.InspectionPointTypeEnum;
import com.ambition.carmfg.entity.MfgCheckInspectionReport;
import com.ambition.carmfg.entity.MfgCheckItem;
import com.ambition.carmfg.entity.MfgInspectingItem;
import com.ambition.carmfg.entity.MfgPlantParameterItem;
import com.ambition.carmfg.entity.ProductBom;
import com.ambition.iqc.entity.CheckItem;
import com.ambition.iqc.samplestandard.service.SampleCodeLetterManager;
import com.ambition.iqc.samplestandard.service.SampleSchemeManager;
import com.ambition.product.BaseAction;
import com.ambition.spc.dataacquisition.utils.CharacterConverter;
import com.ambition.spc.entity.FeatureLayer;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.supplier.utils.DateUtil;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.mms.base.utils.view.DynamicColumnDefinition;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**    
 * 检验报告ACTION
 * @authorBy 赵骏
 *
 */
@Namespace("/carmfg/check-inspection")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "carmfg/check-inspection", type = "redirectAction") })
public class MfgCheckInspectionReportAction extends BaseAction<MfgCheckInspectionReport> {
	private static final String CUSTOM_COUNT_NAME_FREX = "_count";
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private MfgCheckInspectionReport mfgCheckInspectionReport;
	private Long inspectionPointId;
	private JSONObject params;
	private JSONObject badItems;//不良项目
	private File myFile;
	private File spcDataFile;
	private String workshop;
	@Autowired
	private MfgCheckInspectionReportManager mfgCheckInspectionReportManager;

	@Autowired
	private SampleCodeLetterManager sampleCodeLetterManager;
	
	@Autowired
	private SampleSchemeManager sampleSchemeManager;
	
	@Autowired
	private InspectionPointManager inspectionPointManager;
	
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	
	public Long getInspectionPointId() {
		return inspectionPointId;
	}

	public void setInspectionPointId(Long inspectionPointId) {
		this.inspectionPointId = inspectionPointId;
	}

	public String getWorkshop() {
		return workshop;
	}

	public void setWorkshop(String workshop) {
		this.workshop = workshop;
	}

	public File getSpcDataFile() {
		return spcDataFile;
	}

	public void setSpcDataFile(File spcDataFile) {
		this.spcDataFile = spcDataFile;
	}

	private Page<MfgCheckInspectionReport> page;
	
	private List<MfgCheckInspectionReport> list;
	private List<Option> listOption;
	@Autowired
	private LogUtilDao logUtilDao;
	private ProductBom productBom;	
	@Autowired
	private ProductBomManager productBomManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private List<DynamicColumnDefinition> dynamicColumn=new ArrayList<DynamicColumnDefinition>();
	//组装列表
	private String colNames;//列名
	private String colModel;//列信息
	private String colCode;//列代号
	private String code;//物料代码
	private Integer totalYear;
	private Integer startMonth;
	private Integer endMonth;
	
	public List<MfgCheckInspectionReport> getList() {
		return list;
	}

	public void setList(List<MfgCheckInspectionReport> list) {
		this.list = list;
	}

	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public String getColNames() {
		return colNames;
	}

	public void setColNames(String colNames) {
		this.colNames = colNames;
	}

	public String getColModel() {
		return colModel;
	}

	public void setColModel(String colModel) {
		this.colModel = colModel;
	}

	public String getColCode() {
		return colCode;
	}

	public void setColCode(String colCode) {
		this.colCode = colCode;
	}

	public List<Option> getListOption() {
		return listOption;
	}

	public void setListOption(List<Option> listOption) {
		this.listOption = listOption;
	}

	public void setPage(Page<MfgCheckInspectionReport> page) {
		this.page = page;
	}
	
	public Page<MfgCheckInspectionReport> getPage() {
		return page;
	}
	
	public MfgCheckInspectionReport getModel() {
		return mfgCheckInspectionReport;
	}
	
	public List<DynamicColumnDefinition> getDynamicColumn() {
		return dynamicColumn;
	}

	public ProductBom getProductBom() {
		return productBom;
	}

	public void setProductBom(ProductBom productBom) {
		this.productBom = productBom;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getTotalYear() {
		return totalYear;
	}

	public void setTotalYear(Integer totalYear) {
		this.totalYear = totalYear;
	}

	public Integer getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(Integer startMonth) {
		this.startMonth = startMonth;
	}

	public Integer getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(Integer endMonth) {
		this.endMonth = endMonth;
	}
	
	public JSONObject getBadItems() {
		return badItems;
	}

	public void setBadItems(JSONObject badItems) {
		this.badItems = badItems;
	}

	public MfgCheckInspectionReport getMfgCheckInspectionReport() {
		return mfgCheckInspectionReport;
	}

	public void setMfgCheckInspectionReport(
			MfgCheckInspectionReport mfgCheckInspectionReport) {
		this.mfgCheckInspectionReport = mfgCheckInspectionReport;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			mfgCheckInspectionReport=new MfgCheckInspectionReport();
			mfgCheckInspectionReport.setCreatedTime(new Date());
			mfgCheckInspectionReport.setCompanyId(ContextUtils.getCompanyId());
//			mfgCheckInspectionReport.setInspectionPointType(InspectionPointTypeEnum.STORAGEINSPECTION);//检查数据类型
			mfgCheckInspectionReport.setCreator(ContextUtils.getUserName());
			mfgCheckInspectionReport.setLastModifiedTime(new Date());
			mfgCheckInspectionReport.setLastModifier(ContextUtils.getUserName());
			mfgCheckInspectionReport.setBusinessUnitName(ContextUtils.getSubCompanyName());
			mfgCheckInspectionReport.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			mfgCheckInspectionReport=mfgCheckInspectionReportManager.getMfgCheckInspectionReport(id);
		}
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		renderMenu();
		Struts2Utils.getRequest().setAttribute("accordionMenu","input");
		InspectionPoint inspectionPoint = null;
		if(inspectionPointId == null){
			@SuppressWarnings("unchecked")
			List<InspectionPoint> points = (List<InspectionPoint>)Struts2Utils.getRequest().getAttribute("checkInspectionPointList");
			if(points!=null&&points.size()>0){
				inspectionPoint = points.get(0);
				inspectionPointId = inspectionPoint.getId();
			}
		}
		if(inspectionPoint==null&&inspectionPointId != null){
			inspectionPoint = inspectionPointManager.getInspectionPoint(inspectionPointId);
		}
		if(inspectionPoint != null){
			Struts2Utils.getRequest().setAttribute("workProcedure", inspectionPoint.getWorkProcedure());
		}
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try {
			page = mfgCheckInspectionReportManager.list(page,workshop,Struts2Utils.getParameter("workProcedure"),InspectionPointTypeEnum.STORAGEINSPECTION);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.renderText(PageUtils.pageToJson(page));
//		this.renderText(PageUtils.dynamicPageToJson(page,new DynamicColumnValues(){
//			public void addValuesTo(List<Map<String, Object>> result) {
//				for(Map<String, Object> map:result){
//					Long inspectionId=Long.valueOf(map.get("id").toString());
//					incomingInspectionActionsReport=incomingInspectionActionsReportManager.getIncomingInspectionActionsReport(inspectionId);
//				}
//			}
//		}));
		logUtilDao.debugLog("查询", "制造检验管理：检验报告台帐");
		return null;
	}
	@Action("get-inspection-no")
	public String getInspectionNO() throws Exception {
//		renderText(formCodeGenerated.generateIncomingInspectionReportCode());
		return null;
	}
	@Action("input")
	@Override
	public String input() throws Exception {
		try {
			renderMenu();
			Struts2Utils.getRequest().setAttribute("accordionMenu","input");
			
			if(mfgCheckInspectionReport.getId() == null){
				mfgCheckInspectionReport.setInspector(ContextUtils.getUserName());
				mfgCheckInspectionReport.setInspectionDate(new Date());
//				mfgCheckInspectionReport.setInspectionNo(formCodeGenerated.generateMfgCheckInspectionReportCode());
				InspectionPoint inspectionPoint = null;
				if(inspectionPointId == null){
					@SuppressWarnings("unchecked")
					List<InspectionPoint> points = (List<InspectionPoint>)Struts2Utils.getRequest().getAttribute("checkInspectionPointList");
					if(points!=null&&points.size()>0){
						inspectionPoint = points.get(0);
						inspectionPointId = inspectionPoint.getId();
					}
				}
				if(inspectionPoint==null&&inspectionPointId != null){
					inspectionPoint = inspectionPointManager.getInspectionPoint(inspectionPointId);
				}
				if(inspectionPoint != null){
					mfgCheckInspectionReport.setWorkshop(inspectionPoint.getWorkshop());
					mfgCheckInspectionReport.setWorkProcedure(inspectionPoint.getWorkProcedure());
					mfgCheckInspectionReport.setProductionLine(inspectionPoint.getProductionLine());
				}
			}
			List<MfgCheckItem> checkItems = null;
			if(id!=null){
				checkItems = mfgCheckInspectionReport.getCheckItems();
				Struts2Utils.getRequest().setAttribute("inspectionLevels",sampleCodeLetterManager.getInspectionLevelOptions(sampleSchemeManager.getUseBaseType().getBaseType()));
				ActionContext.getContext().put("incomingInspectionActionsReport", mfgCheckInspectionReport);
			}else{
				checkItems = new ArrayList<MfgCheckItem>();
			}
			Struts2Utils.getRequest().setAttribute("checkItems",checkItems);
			// 产品类型
			Map<String,Object> map = productBomManager.getProductModels();
			ActionContext.getContext().put("productModelOptions",convertToOptionList(map));
			// 产品型号
			map = productBomManager.getModelSpecifications();
			ActionContext.getContext().put("modelSpecifications",
					convertToOptionList(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	@Action("analyse-view-info")
	public String viewInfo() throws Exception {
		try {
			mfgCheckInspectionReport=mfgCheckInspectionReportManager.getMfgCheckInspectionReport(id);
			//renderMenu();
			//获取当前车间的所有检查数据采集点
			Struts2Utils.getRequest().setAttribute("checkInspectionPointList",inspectionPointManager.getInspectionPointByWorkshopAndType(workshop,InspectionPointTypeEnum.STORAGEINSPECTION));
			Struts2Utils.getRequest().setAttribute("accordionMenu","input");
			
			if(mfgCheckInspectionReport.getId() == null){
				mfgCheckInspectionReport.setInspector(ContextUtils.getUserName());
				mfgCheckInspectionReport.setInspectionDate(new Date());
//				mfgCheckInspectionReport.setInspectionNo(formCodeGenerated.generateMfgCheckInspectionReportCode());
				if(inspectionPointId != null){
					InspectionPoint inspectionPoint = inspectionPointManager.getInspectionPoint(inspectionPointId);
					mfgCheckInspectionReport.setWorkshop(inspectionPoint.getWorkshop());
					mfgCheckInspectionReport.setWorkProcedure(inspectionPoint.getWorkProcedure());
					mfgCheckInspectionReport.setProductionLine(inspectionPoint.getProductionLine());
				}
			}
			List<MfgCheckItem> checkItems = null;
			if(id!=null){
				checkItems = mfgCheckInspectionReport.getCheckItems();
				Struts2Utils.getRequest().setAttribute("inspectionLevels",sampleCodeLetterManager.getInspectionLevelOptions(sampleSchemeManager.getUseBaseType().getBaseType()));
				ActionContext.getContext().put("incomingInspectionActionsReport", mfgCheckInspectionReport);
				StringBuffer badItemStr = new StringBuffer();
			}else{
				checkItems = new ArrayList<MfgCheckItem>();
			}
			Struts2Utils.getRequest().setAttribute("checkItems",checkItems);
			
			// 产品类型
			Map<String,Object> map = productBomManager.getProductModels();
			ActionContext.getContext().put("productModelOptions",convertToOptionList(map));
			// 产品型号
			map = productBomManager.getModelSpecifications();
			ActionContext.getContext().put("modelSpecifications",
					convertToOptionList(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	/**
	 * 把map转换为OptionsList
	 * @param map
	 * @return
	 */
	private List<Option> convertToOptionList(Map<String,Object> map){
		List<Option> options = new ArrayList<Option>();
		for(String key : map.keySet()){
			Option option = new Option();
			option.setName(key);
			option.setValue(map.get(key).toString());
			options.add(option);
		}
		return options;
	}
	@Action("check-items")
	public String getCheckItems() throws Exception {
		try {
			Date inspectionDate = DateUtil.parseDateTime(Struts2Utils.getParameter("inspectionDate"));
			String workProcedure = Struts2Utils.getParameter("workProcedure");
			String checkBomCode = Struts2Utils.getParameter("checkBomCode");
			Integer stockAmount = Integer.valueOf(Struts2Utils.getParameter("stockAmount"));
			List<JSONObject> checkItemsArray = getRequestCheckItems();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public static List<JSONObject> getRequestPlantParamters(){
		String flagIds = Struts2Utils.getParameter("planFlagIds");
		if(StringUtils.isEmpty(flagIds)){
			return null;
		}
		String[] flags = flagIds.split(",");
		Map<String,JSONObject> flagMaps = new HashMap<String, JSONObject>();
		for(String flag : flags){
			if(StringUtils.isNotEmpty(flag)){
				JSONObject obj = new JSONObject();
				obj.put("flagIndex",flag.substring(1));
				flagMaps.put(flag,obj);	
			}
		}
		Map<String,String> paramMap = Struts2Utils.getRequest().getParameterMap();
		for(String key : paramMap.keySet()){
			if(key.startsWith("_")&&key.indexOf("_")>=0){
				String flag = key.substring(1,key.lastIndexOf("_"));
				String fieldName = key.substring(key.lastIndexOf("_")+1);
				if(flagMaps.containsKey(flag)){
					flagMaps.get(flag).put(fieldName,Struts2Utils.getParameter(key));
				}
			}
		}
		List<JSONObject> arrays = new ArrayList<JSONObject>();
		for(JSONObject json : flagMaps.values()){
			arrays.add(json);
		}
		Collections.sort(arrays,new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject o1, JSONObject o2) {
				if(o1.getInt("flagIndex")<o2.getInt("flagIndex")){
					return 0;
				}else if(o1.getInt("flagIndex")==o2.getInt("flagIndex")){
					return -1;
				}else{
					return 1;
				}
			}
		});
		return arrays;
	}
	/** 
	  * 方法名:获取请求参数 
	  * @return
	 */
	public static List<JSONObject> getRequestCheckItems(){
		String flagIds = Struts2Utils.getParameter("flagIds");
		if(StringUtils.isEmpty(flagIds)){
			return null;
		}
		String[] flags = flagIds.split(",");
		Map<String,JSONObject> flagMaps = new HashMap<String, JSONObject>();
		for(String flag : flags){
			if(StringUtils.isNotEmpty(flag)){
				JSONObject obj = new JSONObject();
				obj.put("flagIndex",flag.substring(1));
				flagMaps.put(flag,obj);	
			}
		}
		Map<String,String> paramMap = Struts2Utils.getRequest().getParameterMap();
		for(String key : paramMap.keySet()){
			if(key.indexOf("_")>0){
				String flag = key.substring(0,key.indexOf("_"));
				String fieldName = key.substring(key.indexOf("_")+1);
				if(flagMaps.containsKey(flag)){
					flagMaps.get(flag).put(fieldName,Struts2Utils.getParameter(key));
				}
			}
		}
		List<JSONObject> arrays = new ArrayList<JSONObject>();
		for(JSONObject json : flagMaps.values()){
			arrays.add(json);
		}
		Collections.sort(arrays,new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject o1, JSONObject o2) {
				if(o1.getInt("flagIndex")<o2.getInt("flagIndex")){
					return 0;
				}else if(o1.getInt("flagIndex")==o2.getInt("flagIndex")){
					return -1;
				}else{
					return 1;
				}
			}
		});
		return arrays;
	}
	@Action("view-info")
	public String view() throws Exception {
		try {
			String batchNo=Struts2Utils.getParameter("batchNo");
			if(mfgCheckInspectionReportManager.getIncomingInspectionActionsReportListByBatchNo(batchNo).size()==0&&batchNo!=null){
				Struts2Utils.getRequest().setAttribute("checkItems",new ArrayList<CheckItem>());
				addActionMessage("批次号不存在！");
			}else{
				List<MfgCheckItem> checkItems = null;
				if(batchNo!=null){
					mfgCheckInspectionReport=mfgCheckInspectionReportManager.getMfgCheckInspectionReportByBatchNo(batchNo);	
					checkItems = mfgCheckInspectionReport.getCheckItems();
					ActionContext.getContext().put("incomingInspectionActionsReport", mfgCheckInspectionReport);
				}
				if(id!=null){
					mfgCheckInspectionReport = mfgCheckInspectionReportManager.getMfgCheckInspectionReport(id);	
					checkItems = mfgCheckInspectionReport.getCheckItems();
					ActionContext.getContext().put("incomingInspectionActionsReport", mfgCheckInspectionReport);
				}else{
					checkItems = new ArrayList<MfgCheckItem>();
				}
				Struts2Utils.getRequest().setAttribute("checkItems",checkItems);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	  * 方法名:打印预览页面 
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("print")
	@LogInfo(optType="页面",message="打印预览页面")
	public String print() throws Exception {
		mfgCheckInspectionReport = mfgCheckInspectionReportManager.getMfgCheckInspectionReport(id);
		ActionContext.getContext().getValueStack().push(mfgCheckInspectionReport);
		Struts2Utils.getRequest().setAttribute("inspectionLevels",sampleCodeLetterManager.getInspectionLevelOptions(sampleSchemeManager.getUseBaseType().getBaseType()));
		return SUCCESS;
	}
	@Action("save")
	@LogInfo(optType="保存",message="检验报告")
	@Override
	public String save() throws Exception {
		//转换时间
//		mfgCheckInspectionReport.setInspectionDate(DateUtil.parseDate(Struts2Utils.getParameter("inspectionDate")));
		String inspectionPointId=Struts2Utils.getParameter("inspectionPointId");
		InspectionPoint inspectionPointObj = inspectionPointManager.getInspectionPoint(Long.parseLong(inspectionPointId));
		mfgCheckInspectionReport.setInspectionType(inspectionPointObj.getInspectionType());
		mfgCheckInspectionReport.setFactory(inspectionPointObj.getFactory());
		mfgCheckInspectionReport.setWorkGroupType(inspectionPointObj.getWorkGroupType());
		mfgCheckInspectionReport.setInspectionPoint(inspectionPointObj.getInspectionPointName());
		String checkItemStrs = Struts2Utils.getParameter("checkItemStrs");
		JSONArray checkItemArray = null;
		if(StringUtils.isNotEmpty(checkItemStrs)){
			checkItemArray = JSONArray.fromObject(checkItemStrs);
		}
		if(id != null){
			mfgCheckInspectionReport.setLastModifiedTime(new Date());
			mfgCheckInspectionReport.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", mfgCheckInspectionReport.toString());
		}else{
			mfgCheckInspectionReport.setCheckItems(new ArrayList<MfgCheckItem>());
			logUtilDao.debugLog("保存", mfgCheckInspectionReport.toString());
		}
		try {
			String method = Struts2Utils.getParameter("acquisitionMethod");
			mfgCheckInspectionReportManager.saveIncomingInspectionActionsReport(mfgCheckInspectionReport,checkItemArray,convertJsonObject(badItems),method,InspectionPointTypeEnum.STORAGEINSPECTION);
			//是否发起改进
//			incomingInspectionActionsReportManager.launchImprove(incomingInspectionActionsReport);
			if(Struts2Utils.getParameter("isLedger") != null){
				renderText(JsonParser.getRowValue(mfgCheckInspectionReport));
				return null;
			}else{
				addActionMessage("保存成功!");
				
				renderMenu();
				Struts2Utils.getRequest().setAttribute("accordionMenu","input");
				Struts2Utils.getRequest().setAttribute("inspectionLevels",sampleCodeLetterManager.getInspectionLevelOptions(sampleSchemeManager.getUseBaseType().getBaseType()));
				
				// 产品类型
				Map<String,Object> map = productBomManager.getProductModels();
				ActionContext.getContext().put("productModelOptions",convertToOptionList(map));
				// 产品型号
				map = productBomManager.getModelSpecifications();
				ActionContext.getContext().put("modelSpecifications",
						convertToOptionList(map));
				
				return "input";
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(Struts2Utils.getParameter("isLedger") != null){
				renderText("{'error':true,'message':'保存失败:"+e.getMessage()+"'}");
				return null;
			}else{
				mfgCheckInspectionReport.setId(id);
				addActionMessage("保存失败:" + e.getMessage());

				renderMenu();
				Struts2Utils.getRequest().setAttribute("accordionMenu","input");
				Struts2Utils.getRequest().setAttribute("inspectionLevels",sampleCodeLetterManager.getInspectionLevelOptions(sampleSchemeManager.getUseBaseType().getBaseType()));
				
				// 产品类型
				Map<String,Object> map = productBomManager.getProductModels();
				ActionContext.getContext().put("productModelOptions",convertToOptionList(map));
				// 产品型号
				map = productBomManager.getModelSpecifications();
				ActionContext.getContext().put("modelSpecifications",
						convertToOptionList(map));
				return "input";
			}
		}
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="检验报告")
	@Override
	public String delete() throws Exception {
		mfgCheckInspectionReportManager.deleteIncomingInspectionActionsReport(deleteIds);
		return null;
	}
	
	/**
	 * 原料供应商合格率页面
	 */
	@Action("inspection-chart")
	public String chart() throws Exception {
		List<Option> productType = productBomManager.getModelSpecificationToOptions();
		ActionContext.getContext().put("productTypes", productType);
		List<Option> importance = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_importance");
		ActionContext.getContext().put("importances", importance);
		List<Option> materialType = ApiFactory.getSettingService().getOptionsByGroupCode("supply_materialType");
		ActionContext.getContext().put("materialTypes", materialType);
		return SUCCESS;
	}
	
	/**
	 * 原料供应商合格率数据
	 */
	@Action("inspection-chart-datas")
	public String getchartDatas() throws Exception {
		params = convertJsonObject(params);
		if(params.get("myType") != null && params.get("myType").toString().equals("date")){
			String materiel="";
			String dutySupplier="";
			String checkBomMaterialType="";
			String importance="";
			if(params.containsKey("itemdutyPart_equals")){
				materiel=params.get("itemdutyPart_equals").toString();
			}
			if(params.containsKey("dutySupplier")){
				dutySupplier=params.get("dutySupplier").toString();
			}
			if(params.containsKey("checkBomMaterialType")){
				checkBomMaterialType=params.get("checkBomMaterialType").toString();
			}
			if(params.containsKey("importance")){
				importance=params.get("importance").toString();
			}
			Date startDate=new Date();
			Date endDate=new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(params.get("endDate_le_date")!=null){
				startDate=sdf.parse(params.get("startDate_ge_date").toString());
				endDate=sdf.parse(params.get("endDate_le_date").toString());
			}
			//获取天数
			Calendar startCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();
			startCal.setTime(startDate);
			endCal.setTime(endDate);
			Map<String,Object> result = new HashMap<String, Object>();
			if(dutySupplier.isEmpty()&&materiel.isEmpty()){
				result.put("title", "合格批数推移图");
			}else if(!dutySupplier.isEmpty()&&materiel.isEmpty()){
				result.put("title", dutySupplier+"_"+"合格批数推移图");
			}else if(dutySupplier.isEmpty()&&!materiel.isEmpty()){
				result.put("title", materiel+"_"+"合格批数推移图");
			}else{
				result.put("title", dutySupplier+"_"+materiel+"_"+"合格批数推移图");
			}
			result.put("ppmtitle", materiel+"_"+"合格批数推移图");
			result.put("subtitle","(" + sdf.format(startDate) + "-" + sdf.format(endDate) + ")");
			List<Integer> categories = new ArrayList<Integer>();
			result.put("categories", categories);
			result.put("yAxisTitle1","批<br/>次<br/>数");
			result.put("yAxisTitle2","合<br/>格<br/>率");

			List<Integer> data = new ArrayList<Integer>();
			//检验批数
			Map<String,Object> series1 = new HashMap<String, Object>();
			series1.put("name", "检验批数");
			List<Map<String,Object>> data1 = new ArrayList<Map<String,Object>>();
			
			//合格批数
			Map<String,Object> series2 = new HashMap<String, Object>();
			series2.put("name", "合格批数");
			List<Map<String,Object>> data2 = new ArrayList<Map<String,Object>>();
			
			//批次合格率
			Map<String,Object> series3 = new HashMap<String, Object>();
			series3.put("name", "合格率");
			List<Double> data3 = new ArrayList<Double>();
			
			List<MfgCheckInspectionReport> incomingInspectionActionsReport = new ArrayList<MfgCheckInspectionReport>();
			double jianyan = 0, hege = 0;
			Calendar startCal1 = Calendar.getInstance();
			startCal1.setTime(startDate);
			while(startCal1.getTimeInMillis() <= endDate.getTime()){
				Date startdate = startCal1.getTime();
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("date", sdf.format(startdate));
				//横坐标天数的参数
				categories.add(startCal1.get(Calendar.DAY_OF_MONTH));
				startCal1.add(Calendar.DAY_OF_YEAR, 1);
				Date enddate = startCal1.getTime();
				incomingInspectionActionsReport = mfgCheckInspectionReportManager.listAll(startdate,enddate,materiel,dutySupplier,checkBomMaterialType,importance);
				map.put("name","检验批数");
				map.put("y", incomingInspectionActionsReport.size());
				data1.add(map);
				data.add(incomingInspectionActionsReport.size());
				jianyan = incomingInspectionActionsReport.size();
				
				data = new ArrayList<Integer>();
				map = new HashMap<String, Object>();
				map.put("date", sdf.format(startdate));
				incomingInspectionActionsReport = mfgCheckInspectionReportManager.listQualified(startdate, enddate,"OK",materiel,dutySupplier,checkBomMaterialType,importance);
				map.put("name","合格批数");
				map.put("in","合格批数");
				map.put("y", incomingInspectionActionsReport.size());
				data2.add(map);
				data.add(incomingInspectionActionsReport.size());
				hege = incomingInspectionActionsReport.size();
				
				double rate = 0.00;
				if(jianyan != 0){
					rate = (hege/jianyan)*100;
				}
				data3.add(new Double(new DecimalFormat( "0.00" ).format(rate)));
			}
			ActionContext.getContext().put("categorieslist", categories);
			result.put("tableHeaderList", categories);
			result.put("firstColName","日期");
			series1.put("data",data1);
			result.put("series1", series1);
			
			series2.put("data",data2);
			result.put("series2", series2);
			
			series3.put("data",data3);
			result.put("series3", series3);
			
			result.put("max", 100);
			this.renderText(JSONObject.fromObject(result).toString());
		}else{
			String materiel="", dutySupplier="";
			if(params.containsKey("itemdutyPart_equals")){
				 materiel=params.get("itemdutyPart_equals").toString();
			}
			if(params.containsKey("dutySupplier")){
				 dutySupplier=params.get("dutySupplier").toString();
			}
			String checkBomMaterialType="";
			String importance="";
			if(params.containsKey("checkBomMaterialType")){
				checkBomMaterialType=params.get("checkBomMaterialType").toString();
			}
			if(params.containsKey("importance")){
				importance=params.get("importance").toString();
			}
			
			Calendar startCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();
			int startyear = 0, startmonth = 0, endyear = 0, endmonth = 0, endMonthNumber = 0;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			if(params.get("endDate_le_date")!=null && params.get("startDate_ge_date")!=null){
				startyear =  Integer.valueOf(params.get("startDate_ge_date").toString().substring(0, 4));
				startmonth = Integer.valueOf(params.get("startDate_ge_date").toString().substring(5, 7));
				endyear = Integer.valueOf(params.get("endDate_le_date").toString().substring(0, 4));
				endmonth = Integer.valueOf(params.get("endDate_le_date").toString().substring(5, 7));
			}
			
			startCal.set(Calendar.YEAR,startyear);
			startCal.set(Calendar.MONTH,startmonth-1);
			startCal.set(Calendar.DATE,1);
			
			endCal.set(Calendar.YEAR,endyear);
			endCal.set(Calendar.MONTH,endmonth);
			endCal.set(Calendar.DATE,1);
			endCal.add(Calendar.DATE,-1);
			endMonthNumber = getYearAndMonthNumber(endCal);
			
			Map<String,Object> result = new HashMap<String, Object>();
			if(dutySupplier.isEmpty()&&materiel.isEmpty()){
				result.put("title", "合格批数推移图");
			}else if(!dutySupplier.isEmpty()&&materiel.isEmpty()){
				result.put("title", dutySupplier+"_"+"合格批数推移图");
			}else if(dutySupplier.isEmpty()&&!materiel.isEmpty()){
				result.put("title", materiel+"_"+"合格批数推移图");
			}else{
				result.put("title", dutySupplier+"_"+materiel+"_"+"合格批数推移图");
			}
			result.put("ppmtitle", materiel+"_"+"合格批数推移图");
			result.put("subtitle","(" + startyear + "-" + startmonth + " 至 " + endyear + "-" + endmonth + ")");
			List<Integer> categories = new ArrayList<Integer>();
			result.put("categories", categories);
			result.put("yAxisTitle1","批<br/>次<br/>数");
			result.put("yAxisTitle2","合<br/>格<br/>率");

			List<Integer> data = new ArrayList<Integer>();
			//检验批数
			Map<String,Object> series1 = new HashMap<String, Object>();
			series1.put("name", "检验批数");
			List<Map<String,Object>> data1 = new ArrayList<Map<String,Object>>();
			
			//合格批数
			Map<String,Object> series2 = new HashMap<String, Object>();
			series2.put("name", "合格批数");
			List<Map<String,Object>> data2 = new ArrayList<Map<String,Object>>();
			
			//批次合格率
			Map<String,Object> series3 = new HashMap<String, Object>();
			series3.put("name", "合格率");
			List<Double> data3 = new ArrayList<Double>();
			
			List<MfgCheckInspectionReport> incomingInspectionActionsReport = new ArrayList<MfgCheckInspectionReport>();
			double total =0, qualified = 0;
			while(getYearAndMonthNumber(startCal) <= endMonthNumber){
				//横坐标月份的参数
				categories.add(startCal.get(Calendar.MONTH)+1);
				Date startdate1 = startCal.getTime();
				Calendar endCalendar = Calendar.getInstance();
				endCalendar.setTime(startdate1);
				endCalendar.add(Calendar.MONTH, 1);
				endCalendar.add(Calendar.DATE, -1);
				Date enddate1 = endCalendar.getTime();
				
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("date", sdf.format(startdate1));
				incomingInspectionActionsReport = mfgCheckInspectionReportManager.listAll(startdate1,enddate1,materiel,dutySupplier,checkBomMaterialType,importance);
				map.put("name","检验批数");
				map.put("y", incomingInspectionActionsReport.size());
				data1.add(map);
				data.add(incomingInspectionActionsReport.size());
				total = incomingInspectionActionsReport.size();
				
				data = new ArrayList<Integer>();
				map = new HashMap<String, Object>();
				map.put("date", sdf.format(startdate1));
				incomingInspectionActionsReport = mfgCheckInspectionReportManager.listQualified(startdate1, enddate1,"OK",materiel,dutySupplier,checkBomMaterialType,importance);
				map.put("name","合格批数");
				map.put("in","合格批数");
				map.put("y", incomingInspectionActionsReport.size());
				data2.add(map);
				data.add(incomingInspectionActionsReport.size());
				qualified = incomingInspectionActionsReport.size();
				
				double rate=0.00;
				if(total!=0){
					rate=(qualified/total)*100;
				}
				data3.add(new Double(new DecimalFormat( "0.00" ).format(rate)));
				
				startCal.add(Calendar.MONTH, 1);
			}
			ActionContext.getContext().put("categorieslist", categories);
			result.put("tableHeaderList", categories);
			result.put("firstColName","月份");
			
			series1.put("data",data1);
			result.put("series1", series1);
			
			series2.put("data",data2);
			result.put("series2", series2);
			
			series3.put("data",data3);
			result.put("series3", series3);
			
			result.put("max", 100);
			this.renderText(JSONObject.fromObject(result).toString());
		}
		return null;	
	}
	
	/**
	 * 原料供应商合格率图表链接页面
	 */
	@SuppressWarnings("static-access")
	@Action("inspection-report-detail")
	public String getchartDatasDetail() throws Exception {
		params= convertJsonObject(params);
		//拼接列表
		StringBuffer colNamesByItem = new StringBuffer();
		StringBuffer colModelByItem = new StringBuffer();
		StringBuffer colCodeByItem = new StringBuffer();
		String colNames = "检验报告编号,检验日期,到货日期,批次号,检验员,物料名称,供方,来料数,检验数,合格数,不良数, 检验判定, 合格率,发生次数,不良类别," +
						  "不良等级,处理结果,备注," ;
		colNamesByItem.append(colNames);
		String colModel = 
					"{name:'inspectionNo',index:'inspectionNo',width:'180',editable:true,formatter:click},"+
			        "{name:'inspectionDate',index:'inspectionDate',width:'100',editable:true},"+
			        "{name:'enterDate',index:'enterDate',width:'100',editable:true},"+
			        "{name:'batchNo',index:'batchNo',editable:true},"+
			        "{name:'inspector',index:'inspector',width:'100',editable:true}, "+
			        "{name:'checkBomName',index:'checkBomName',editable:true},"+ 
			        "{name:'supplierName',index:'supplierName',editable:true}, "+      
		            "{name:'stockAmount',index:'stockAmount',editable:true}, "+
		            "{name:'inspectionAmount',index:'inspectionAmount',editable:true},"+ 
		            "{name:'qualifiedAmount',index:'qualifiedAmoun',editable:true}, "+
		            "{name:'unqualifiedAmount',index:'unqualifiedAmount',editable:true},"+ 
		            "{name:'inspectionConclusion',index:'inspectionConclusion',editable:true,edittype:'checkbox',editoptions: {value:'合格:不合格'}},"+ 
		            "{name:'qualifiedRate',index:'qualifiedRate',editable:true}, "+
		            "{name:'occurrenceNum',index:'occurrenceNum',editable:true}, "+
		            "{name:'unqualifiedType',index:'unqualifiedType',editable:true},"+ 
		            "{name:'unqualifiedGrade',index:'unqualifiedGrade',editable:true},"+ 
		            "{name:'processingResult',index:'processingResult',editable:true},"+
		            "{name:'remark',index:'remark',editable:true},";
		colModelByItem.append(colModel);
						
		List<Option> listOptions = ApiFactory.getSettingService().getOptionsByGroupCode("iqctableCol");
		this.setListOption(listOptions);
		for(Option option : listOption){
			//拼接colName
			colNamesByItem.append(option.getName()+",");
			//拼接colModel
			colModelByItem.append("{");
			colModelByItem.append("name:'params." + option.getValue() + "', ");
			colModelByItem.append("index:'params." + option.getValue() + "', ");
			colModelByItem.append("editable:true,");
			colModelByItem.append("width:100");
			colModelByItem.append("},");
			//拼接列代号（备用）
			colCodeByItem.append("params." + option.getValue()+",");
			//拼接列代号（备用）
			colCodeByItem.append("params." + option.getValue() + this.CUSTOM_COUNT_NAME_FREX+",");
		}
		//删除最后的逗号
		colNamesByItem.delete(colNamesByItem.length()-1, colNamesByItem.length());
		this.setColNames(colNamesByItem.toString());
		this.setColModel(colModelByItem.toString());
		this.setColCode(colCodeByItem.toString());
		return SUCCESS;
	}
	
	/**
	 * 进货检验一次合格率图表页面
	 */
	@Action("inspection-one-chart")
	public String onechart() throws Exception {
		List<Option> productType = productBomManager.getModelSpecificationToOptions();
		ActionContext.getContext().put("productTypes", productType);
		Calendar calendar = Calendar.getInstance();
		Integer currentYear = calendar.get(Calendar.YEAR);
		if(totalYear == null){
			totalYear = currentYear;
		}
		List<Option> options = new ArrayList<Option>();
		for(int i=currentYear;i>currentYear-5;i--){
			Option option = new Option();
			option.setName(String.valueOf(i));
			option.setValue(String.valueOf(i));
			options.add(option);
		}
		ActionContext.getContext().put("totalYears",options);
		if(startMonth == null || endMonth == null){
			startMonth = 1;
			endMonth = 12;
		}
		options = new ArrayList<Option>();
		for(int i=1;i<13;i++){
			Option option = new Option();
			option.setName(String.valueOf(i));
			option.setValue(String.valueOf(i));
			options.add(option);
		}
		ActionContext.getContext().put("totalMonths",options);
		return SUCCESS;
	}

	private Map<String,Object> getOneChartDatas(JSONObject params) throws Exception{
		Date startDate=new Date();
		Date endDate=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(params.get("endDate_le_date")!=null){
			startDate=sdf.parse(params.get("startDate_ge_date").toString());
			endDate=sdf.parse(params.get("endDate_le_date").toString());
		}
		//获取天数
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		startCal.setTime(startDate);
		endCal.setTime(endDate);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("title", "进货检验一次合格率");
		result.put("subtitle","(" + sdf.format(startDate) + "-" + sdf.format(endDate) + ")");
		List<Integer> categories = new ArrayList<Integer>();
		result.put("categories", categories);
		result.put("yAxisTitle1","批<br/>次<br/>数");
		result.put("yAxisTitle2","合<br/>格<br/>率");
		
		//检验批数
		Map<String,Object> series1 = new HashMap<String, Object>();
		series1.put("name", "检验批数");
		List<Map<String,Object>> data1 = new ArrayList<Map<String,Object>>();
		//合格批数
		Map<String,Object> series2 = new HashMap<String, Object>();
		series2.put("name", "合格批数");
		List<Map<String,Object>> data2 = new ArrayList<Map<String,Object>>();
		//批次合格率
		Map<String,Object> series3 = new HashMap<String, Object>();
		series3.put("name", "合格率");
		List<Double> data3 = new ArrayList<Double>();
		
		List<Integer> data = new ArrayList<Integer>();
		Calendar startCal1 = Calendar.getInstance();
		startCal1.setTime(startDate);
		double total =0, qualified = 0;
		while(startCal1.getTimeInMillis() <= endDate.getTime()){
			//横坐标天数的参数
			categories.add(startCal1.get(Calendar.DAY_OF_MONTH));
			
			Date startdate = startCal1.getTime();
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("date", sdf.format(startdate));
			startCal1.add(Calendar.DAY_OF_YEAR, 1);
			Date enddate=startCal1.getTime();
			List<MfgCheckInspectionReport> incomingInspectionActionsReport = mfgCheckInspectionReportManager.listAll(startdate,enddate);
			map.put("name","检查批数");
			map.put("y", incomingInspectionActionsReport.size());
			data1.add(map);
			data.add(incomingInspectionActionsReport.size());
			total = incomingInspectionActionsReport.size();
			
			data = new ArrayList<Integer>();
			map = new HashMap<String, Object>();
			map.put("date", sdf.format(startdate));
			incomingInspectionActionsReport = mfgCheckInspectionReportManager.listQualified(startdate, enddate, "OK");
			map.put("name","合格批数");
			map.put("in","合格批数");
			map.put("y", incomingInspectionActionsReport.size());
			data2.add(map);
			data.add(incomingInspectionActionsReport.size());
			qualified = incomingInspectionActionsReport.size();
			
			double rate = 0;
			if(total != 0){
				rate = (qualified/total)*100;
			}
			data3.add(rate);
		}
		ActionContext.getContext().put("categorieslist", categories);
		result.put("tableHeaderList", categories);
		result.put("firstColName","日期");
		series1.put("data",data1);
		result.put("series1", series1);
		series2.put("data",data2);
		result.put("series2", series2);
		series3.put("data",data3);
		result.put("series3", series3);
		result.put("max", 100);
		
		return result;
	}
	
	/**
	 * 进货检验一次合格率图表页面数据
	 */
	@Action("inspection-one-chart-datas")
	public String getonechartDatas() throws Exception {
		params = convertJsonObject(params);
		if(params.get("myType") != null && params.get("myType").toString().equals("date")){
			this.renderText(JSONObject.fromObject(getOneChartDatas(params)).toString());
		}else{
			this.renderText(JSONObject.fromObject(getOneChartDatasByMonths(params)).toString());
		}
		return null;	
	}
	 
	private Integer getYearAndMonthNumber(Calendar calendar){
		StringBuffer sb = new StringBuffer("");
		sb.append(calendar.get(Calendar.YEAR));
		if(calendar.get(Calendar.MONTH)+1<10){
			sb.append("0" + (calendar.get(Calendar.MONTH)+1));
		}else{
			sb.append(calendar.get(Calendar.MONTH)+1);
		}
		return Integer.valueOf(sb.toString());
	}
	
	private Map<String,Object> getOneChartDatasByMonths(JSONObject params) throws Exception{
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		int startyear = 0, startmonth = 0, endyear = 0, endmonth = 0, endMonthNumber = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		if(params.get("startDate_ge_date")!=null && params.get("endDate_le_date")!=null){
			startyear = Integer.valueOf(params.get("startDate_ge_date").toString().substring(0, 4));
			startmonth = Integer.valueOf(params.get("startDate_ge_date").toString().substring(5, 7));
			endyear = Integer.valueOf(params.get("endDate_le_date").toString().substring(0, 4));
			endmonth = Integer.valueOf(params.get("endDate_le_date").toString().substring(5, 7));
		}
		
		startCal.set(Calendar.YEAR,startyear);
		startCal.set(Calendar.MONTH,startmonth-1);
		startCal.set(Calendar.DATE,1);
		
		endCal.set(Calendar.YEAR,endyear);
		endCal.set(Calendar.MONTH,endmonth);
		endCal.set(Calendar.DATE,1);
		endCal.add(Calendar.DATE,-1);
		endMonthNumber = getYearAndMonthNumber(endCal);
		
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("title", "进货检验一次合格率");
		result.put("subtitle","(" + params.get("startDate_ge_date").toString() + "-" + params.get("endDate_le_date").toString() + ")");
		List<Integer> categories = new ArrayList<Integer>();
		result.put("categories", categories);
		result.put("yAxisTitle1","批<br/>次<br/>数");
		result.put("yAxisTitle2","合<br/>格<br/>率");
		
		Map<String,Object> series1 = new HashMap<String, Object>();
		series1.put("name", "检验批数");
		List<Map<String,Object>> data1 = new ArrayList<Map<String,Object>>();
		Map<String,Object> series2 = new HashMap<String, Object>();
		series2.put("name", "合格批数");
		List<Map<String,Object>> data2 = new ArrayList<Map<String,Object>>();
		Map<String,Object> series3 = new HashMap<String, Object>();
		series3.put("name", "合格率");
		List<Double> data3 = new ArrayList<Double>();
		
		List<Integer> data = new ArrayList<Integer>();
		double total =0, qualified = 0;
		while(getYearAndMonthNumber(startCal) <= endMonthNumber){
			//横坐标月份的参数
			categories.add(startCal.get(Calendar.MONTH)+1);
			Date startdate1 = startCal.getTime();
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(startdate1);
			endCalendar.add(Calendar.MONTH, 1);
			endCalendar.add(Calendar.DATE, -1);
			Date enddate1 = endCalendar.getTime();

			Map<String,Object> map = new HashMap<String, Object>();
			List<MfgCheckInspectionReport> incomingInspectionActionsReport = mfgCheckInspectionReportManager.listAll(startdate1,enddate1);
			map.put("date",sdf.format(startdate1));
			map.put("name","检查批数");
			map.put("y",incomingInspectionActionsReport.size());
			data1.add(map);
			data.add(incomingInspectionActionsReport.size());
			total = incomingInspectionActionsReport.size();
			
			data = new ArrayList<Integer>();
			map = new HashMap<String, Object>();
			map.put("date", sdf.format(startdate1));
			incomingInspectionActionsReport = mfgCheckInspectionReportManager.listQualified(startdate1, enddate1, "OK");
			map.put("name","合格批数");
			map.put("in","合格批数");
			map.put("y", incomingInspectionActionsReport.size());
			data2.add(map);
			data.add(incomingInspectionActionsReport.size());
			qualified = incomingInspectionActionsReport.size();
			
			double rate = 0;
			if(total != 0){
				rate = (qualified/total)*100;
			}
			data3.add(rate);
			
			startCal.add(Calendar.MONTH, 1);
		}
		ActionContext.getContext().put("categorieslist", categories);
		result.put("tableHeaderList", categories);
		result.put("firstColName","月份");
		series1.put("data", data1);
		result.put("series1", series1);
		series2.put("data", data2);
		result.put("series2", series2);
		series3.put("data", data3);
		result.put("series3", series3);
		result.put("max", 100);
		return result;
	}
	
	/**
	 * 原料供应商合格率图表和进货检验一次合格率链接页面数据
	 */
	@Action("inspection-report-detail-datas")
	public String getchartDatasDetailDatas() throws Exception {
		params= convertJsonObject(params);
		page = mfgCheckInspectionReportManager.queryIinspectionReportDetail(page, params);
		String result = mfgCheckInspectionReportManager.getResultJson(page,CUSTOM_COUNT_NAME_FREX);
		this.renderText(JSONObject.fromObject(result).toString());
		return null;
	}

	/**
	 * 进货检验不合格页面
	 */
	@Action("unlist")
	public String unlist() throws Exception {
		List<Option> listOptions = ApiFactory.getSettingService().getOptionsByGroupCode("tb_iqctableCol");
		this.setListOption(listOptions);
//		for(Option option : listOption){
//			DynamicColumnDefinition dynamicFieldOptions =new DynamicColumnDefinition();			
//			dynamicFieldOptions.setColName(option.getName());
//			dynamicFieldOptions.setName("params." + option.getValue());
//			dynamicFieldOptions.setEditable(false);
//			dynamicFieldOptions.setEdittype(EditControlType.TEXT);
//			if(option.getName().indexOf("不良数量")==0){
//				dynamicFieldOptions.setType(DataType.INTEGER);	
//			}else{
//				dynamicFieldOptions.setType(DataType.TEXT);	
//			}
//			dynamicColumn.add(dynamicFieldOptions);
//		}	
		return SUCCESS;
	}
	
	/**
	 * 进货检验不合格数据
	 */
	@Action("unlist-datas")
	public String getUnListDatas() throws Exception {
		try {
			page = mfgCheckInspectionReportManager.unlist(page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.renderText(PageUtils.pageToJson(page));
//		this.renderText(PageUtils.dynamicPageToJson(page,new DynamicColumnValues(){
//			public void addValuesTo(List<Map<String, Object>> result) {
//				for(Map<String, Object> map:result){
//					Long inspectionId=Long.valueOf(map.get("id").toString());
//					incomingInspectionActionsReport=incomingInspectionActionsReportManager.getIncomingInspectionActionsReport(inspectionId);
//				}
//			}
//			
//		}));
		logUtilDao.debugLog("查询", "进货检验管理：检验报告-检验报告不合格台帐");
		return null;
	}
	
	/**
	 * 进货检验检验报告导出
	 */
	@Action("export")
	@LogInfo(optType="导出",message="进货检验检验报告")
	public String export() throws Exception {
		Page<MfgCheckInspectionReport> page = new Page<MfgCheckInspectionReport>(100000);
		page = mfgCheckInspectionReportManager.list(page,workshop,Struts2Utils.getParameter("workProcedure"),InspectionPointTypeEnum.STORAGEINSPECTION);
		//ExcelExporter.export2003ForWeb(page.getResult(),"IQC_IIAR","iqc_iiar");
		ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_CHECK_INSPECTION_REPORT"),"检验报告");
		return null;
	}
	
	/**
	 * 进货检验不合格检验报告导出
	 */
	@Action("unexport")
	@LogInfo(optType="导出",message="进货检验不合格检验报告")
	public String unexport() throws Exception {
		Page<MfgCheckInspectionReport> page = new Page<MfgCheckInspectionReport>(100000);
		page = mfgCheckInspectionReportManager.unsearch(page);
		//ExcelExporter.export2003ForWeb(page.getResult(),"IQC_UNIIR","iqc_uniiar");
		ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"IQC_UNIIR"),"iqc_uniiar");
		return null;
	}
	
	/**
	 * 进货检验检验报告导入页面
	 */
	@Action("imports")
	public String imports() throws Exception {
		return "imports";
	}
	
	/**
	 * 进货检验检验报告导入数据处理
	 */
	@Action("import-datas")
	@LogInfo(optType="导入",message="进货检验检验报告")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(mfgCheckInspectionReportManager.importFile(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	
	
	/**
	 * 供应商合格率对比页面
	 */
	@Action("supplier-rate-chart")
	public String supplierRateChart() throws Exception {
		List<Option> productType = productBomManager.getModelSpecificationToOptions();
		ActionContext.getContext().put("productTypes", productType);
		return SUCCESS;
	}
	
	/**
	 * 供应商合格率对比数据
	 */
	@SuppressWarnings("unchecked")
	@Action("supplier-rate-chart-datas")
	public String supplierRateChartDatas() throws Exception {
		params = convertJsonObject(params);
		String materiel="";
		String dutySupplier="";
		if(params.containsKey("materiel")){
			 materiel=params.get("materiel").toString();
		}
		if(params.containsKey("dutySupplier")){
			 dutySupplier=params.get("dutySupplier").toString();
		}
		@SuppressWarnings("rawtypes")
		List supplierlist=new ArrayList();
		if(dutySupplier==""){
			 supplierlist=mfgCheckInspectionReportManager.listAllSuplier();
		}else{
			String[] dutySupplierArray=dutySupplier.split(",");
			for(int i=0;i<dutySupplierArray.length;i++){
				supplierlist.add(dutySupplierArray[i]);
			}
		}
	
		Date startDate=new Date();
		Date endDate=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(params.get("endDate_le_date")!=null){
			startDate=sdf.parse(params.get("startDate_ge_date").toString());
			endDate=sdf.parse(params.get("endDate_le_date").toString());
		}
		Map<String,Object> result = new HashMap<String, Object>();
		//表格的表头
		List<Object> colModels = new ArrayList<Object>();
		JSONObject modelJson = new JSONObject();
		modelJson.put("name","supplierName");
		modelJson.put("label","供应商名称");
		modelJson.put("index","supplierName");
		modelJson.put("width","250");
		colModels.add(modelJson);
		JSONObject modelJson1 = new JSONObject();
		modelJson1.put("name","inspection");
		modelJson1.put("label","检查批数");
		modelJson1.put("index","inspection");
		colModels.add(modelJson1);
		JSONObject modelJson2 = new JSONObject();
		modelJson2.put("name","quality");
		modelJson2.put("label","合格批数");
		modelJson2.put("index","quality");
		colModels.add(modelJson2);
		JSONObject modelJson3 = new JSONObject();
		modelJson3.put("name","rate");
		modelJson3.put("label","合格率");
		modelJson3.put("index","rate");
		colModels.add(modelJson3);

		
		result.put("title", "供应商合格率对比图");
		result.put("subtitle","(" + sdf.format(startDate) + "-" + sdf.format(endDate) + ")");
		List<String> categories = new ArrayList<String>();
		result.put("categories", categories);
		result.put("yAxisTitle1","批<br/>次<br/>数");
		result.put("yAxisTitle2","合<br/>格<br/>率");
		//检验批数
		Map<String,Object> series1 = new HashMap<String, Object>();
		series1.put("name", "检验批数");
		List<Integer> data = new ArrayList<Integer>();
		List<Map<String,Object>> data5 = new ArrayList<Map<String,Object>>();
		for(int i=0;i<supplierlist.size();i++){
			dutySupplier=(String) supplierlist.get(i);
			Map<String,Object> map = new HashMap<String, Object>();
			//横坐标天数的参数
			categories.add(dutySupplier);
			map.put("dutySupplier", dutySupplier);
			List<MfgCheckInspectionReport> incomingInspectionActionsReport=mfgCheckInspectionReportManager.listAll(startDate,endDate,materiel,dutySupplier,"","");
			map.put("name","检查批数");
			map.put("y", incomingInspectionActionsReport.size());
			data5.add(map);
			data.add(incomingInspectionActionsReport.size());
		}
		ActionContext.getContext().put("categorieslist", categories);
		result.put("tableHeaderList", categories);
		List<Integer> data1 = data;
		series1.put("data",data5);
		result.put("series1", series1);
		//合格批数
		Map<String,Object> series2 = new HashMap<String, Object>();
		series2.put("name", "合格批数");
		data = new ArrayList<Integer>();
		List<Map<String,Object>> data6 = new ArrayList<Map<String,Object>>();
		int j=0;
		for(int i=0;i<supplierlist.size();i++){
			dutySupplier=(String) supplierlist.get(i);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("dutySupplier", dutySupplier);
			List<MfgCheckInspectionReport> incomingInspectionActionsReport=mfgCheckInspectionReportManager.listQualified(startDate, endDate, "OK",materiel,dutySupplier,"","");
			map.put("name","合格批数");
			map.put("in","合格批数");
			map.put("y", incomingInspectionActionsReport.size());
			data6.add(map);
			data.add(incomingInspectionActionsReport.size());
			j++;
		}
		List<Integer> data2 = data;
		series2.put("data",data6);
		result.put("series2", series2);
		//批次合格率
		Map<String,Object> series3 = new HashMap<String, Object>();
		series3.put("name", "合格率");
		List<Double> data3 = new ArrayList<Double>();
		for(int i=0;i<j;i++){
			double jianyan=data1.get(i);
			double hege=data2.get(i);
			double rate=0;
			if(jianyan!=0){
				rate=(hege/jianyan)*100;
			}
			data3.add(rate);
		}
		series3.put("data",data3);
		result.put("series3", series3);
		
		List<Map<String,Object>> tabledata = new ArrayList<Map<String,Object>>();
		DecimalFormat  df=new DecimalFormat("0.00");
		for(int i=0;i<data3.size();i++){
			Map<String,Object> dataMap = new HashMap<String, Object>();
			dataMap.put("supplierName", categories.get(i));
			dataMap.put("inspection", data5.get(i).get("y"));
			dataMap.put("quality", data6.get(i).get("y"));
			dataMap.put("rate", df.format(data3.get(i)));
			tabledata.add(dataMap);
		}

		result.put("max", 100);
		result.put("colModel",colModels);
		result.put("tabledata", tabledata);
		renderText(JSONObject.fromObject(result).toString());
		return null;	
	}
	
	/**
	 * 物料合格率对比页面
	 */
	@Action("material-rate-chart")
	public String materialRateChart() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 物料合格率对比数据
	 */
	@SuppressWarnings("unchecked")
	@Action("material-rate-chart-datas")
	public String materialRateChartDatas() throws Exception {
		params = convertJsonObject(params);
		String materiel="";
		String dutySupplier="";
		if(params.containsKey("materiel")){
			materiel=params.get("materiel").toString();
		}
		if(params.containsKey("dutySupplier")){
			dutySupplier=params.get("dutySupplier").toString();
		}
		@SuppressWarnings("rawtypes")
		List materiellist=new ArrayList();
		if(materiel==""){
			materiellist=mfgCheckInspectionReportManager.listAllMaterial();
		}else{
			String[] materielArray=materiel.split(",");
			for(int i=0;i<materielArray.length;i++){
				materiellist.add(materielArray[i]);
			}
		}
	
		Date startDate=new Date();
		Date endDate=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(params.get("endDate_le_date")!=null){
			startDate=sdf.parse(params.get("startDate_ge_date").toString());
			endDate=sdf.parse(params.get("endDate_le_date").toString());
		}
		Map<String,Object> result = new HashMap<String, Object>();
		//表格的表头
		List<Object> colModels = new ArrayList<Object>();
		JSONObject modelJson = new JSONObject();
		modelJson.put("name","checkBomCode");
		modelJson.put("label","物料代码");
		modelJson.put("index","checkBomCode");
		colModels.add(modelJson);
		JSONObject modelJson1 = new JSONObject();
		modelJson1.put("name","inspection");
		modelJson1.put("label","检查批数");
		modelJson1.put("index","inspection");
		colModels.add(modelJson1);
		JSONObject modelJson2 = new JSONObject();
		modelJson2.put("name","quality");
		modelJson2.put("label","合格批数");
		modelJson2.put("index","quality");
		colModels.add(modelJson2);
		JSONObject modelJson3 = new JSONObject();
		modelJson3.put("name","rate");
		modelJson3.put("label","合格率");
		modelJson3.put("index","rate");
		colModels.add(modelJson3);
		
		result.put("title", "物料合格率对比图");
		result.put("subtitle","(" + sdf.format(startDate) + "-" + sdf.format(endDate) + ")");
		List<String> categories = new ArrayList<String>();
		result.put("categories", categories);
		result.put("yAxisTitle1","批<br/>次<br/>数");
		result.put("yAxisTitle2","合<br/>格<br/>率");
		//检验批数
		Map<String,Object> series1 = new HashMap<String, Object>();
		series1.put("name", "检验批数");
		List<Integer> data = new ArrayList<Integer>();
		List<Map<String,Object>> data5 = new ArrayList<Map<String,Object>>();
		for(int i=0;i<materiellist.size();i++){
			materiel=(String) materiellist.get(i);
			Map<String,Object> map = new HashMap<String, Object>();
			//横坐标天数的参数
			categories.add(materiel);
			map.put("materiel", materiel);
			List<MfgCheckInspectionReport> incomingInspectionActionsReport=mfgCheckInspectionReportManager.listAll(startDate,endDate,materiel,dutySupplier,"","");
			map.put("name","检查批数");
			map.put("y", incomingInspectionActionsReport.size());
			data5.add(map);
			data.add(incomingInspectionActionsReport.size());
		}
		ActionContext.getContext().put("categorieslist", categories);
		result.put("tableHeaderList", categories);
		List<Integer> data1 = data;
		series1.put("data",data5);
		result.put("series1", series1);
		//合格批数
		Map<String,Object> series2 = new HashMap<String, Object>();
		series2.put("name", "合格批数");
		data = new ArrayList<Integer>();
		List<Map<String,Object>> data6 = new ArrayList<Map<String,Object>>();
		int j=0;
		for(int i=0;i<materiellist.size();i++){
			materiel=(String) materiellist.get(i);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("materiel", materiel);
			List<MfgCheckInspectionReport> incomingInspectionActionsReport=mfgCheckInspectionReportManager.listQualified(startDate, endDate, "OK",materiel,dutySupplier,"","");
			map.put("name","合格批数");
			map.put("in","合格批数");
			map.put("y", incomingInspectionActionsReport.size());
			data6.add(map);
			data.add(incomingInspectionActionsReport.size());
			j++;
		}
		List<Integer> data2 = data;
		series2.put("data",data6);
		result.put("series2", series2);
		//批次合格率
		Map<String,Object> series3 = new HashMap<String, Object>();
		series3.put("name", "合格率");
		List<Double> data3 = new ArrayList<Double>();
		for(int i=0;i<j;i++){
			double jianyan=data1.get(i);
			double hege=data2.get(i);
			double rate=0;
			if(jianyan!=0){
				rate=(hege/jianyan)*100;
			}
			data3.add(rate);
		}
		List<Map<String,Object>> tabledata = new ArrayList<Map<String,Object>>();
		DecimalFormat  df=new DecimalFormat("0.00");
		for(int i=0;i<data3.size();i++){
			Map<String,Object> dataMap = new HashMap<String, Object>();
			dataMap.put("checkBomCode", categories.get(i));
			dataMap.put("inspection", data5.get(i).get("y"));
			dataMap.put("quality", data6.get(i).get("y"));
			dataMap.put("rate", df.format(data3.get(i)));
			tabledata.add(dataMap);
		}
		
		series3.put("data",data3);
		result.put("series3", series3);
		result.put("colModel",colModels);
		result.put("tabledata", tabledata);
		result.put("max", 100);
		this.renderText(JSONObject.fromObject(result).toString());
		return null;	
	}
	
	/**
	 * 生成检验数据录入模板
	 *
	 */
	@Action("general-model")
	public String generalModel() throws Exception {
		String checkItemStrs = Struts2Utils.getParameter("checkItemStrs");
		JSONArray checkItemArray = null;
		
		if(StringUtils.isNotEmpty(checkItemStrs) && !checkItemStrs.equals("[]")){
			try {
				checkItemArray = JSONArray.fromObject(checkItemStrs);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		if(checkItemArray != null){
			String featureIds = "",checkItemNames = "",countType = null; 
			int maxMount = 0;
			if(StringUtils.isNotEmpty(checkItemArray.getJSONObject(0).getString("inspectionAmount"))){
				maxMount = checkItemArray.getJSONObject(0).getInt("inspectionAmount");
			}
			for(int i=0;i<checkItemArray.size();i++){
				countType = checkItemArray.getJSONObject(i).getString("countType");
				if(MfgInspectingItem.COUNTTYPE_METERING.equals(countType)){
					if(StringUtils.isNotEmpty(checkItemArray.getJSONObject(i).getString("featureId"))){
						featureIds += checkItemArray.getJSONObject(i).getString("featureId")+",";
					}
					checkItemNames += checkItemArray.getJSONObject(i).getString("checkItemName")+",";
					if(checkItemArray.getJSONObject(i).getString("inspectionAmount")!=null){
						try {
							if(maxMount < checkItemArray.getJSONObject(i).getInt("inspectionAmount")){
								maxMount = checkItemArray.getJSONObject(i).getInt("inspectionAmount");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
			}
			String filename = Struts2Utils.getParameter("filename");
			String[] chars={"\\\\","\"","/",":","\\*","\\?",">","<","\\|"};
			if(filename != null){
				for(int i=0;i<chars.length;i++){
					if(i<2){
						filename = filename.replaceAll(chars[i],"_");
					}else{
						filename = filename.replaceAll(chars[i],"_");
					}
				}
			}
			//设置响应头和下载保存的文件名
			Struts2Utils.getResponse().reset();
			Struts2Utils.getResponse().setContentType("application/msexcel;charset=UTF-8");
			Struts2Utils.getResponse().setHeader("Content-Disposition", "attachment;" + " filename="+ CharacterConverter.encode(Struts2Utils.getRequest(),filename)+".xls");
			
			OutputStream os = null;
		    WritableWorkbook wwb = null;
		    try {
		         os = Struts2Utils.getResponse().getOutputStream();
		         wwb = Workbook.createWorkbook(os);
		    }catch (Exception e) {
		      e.printStackTrace();
		    }
		    WritableSheet ws = wwb.createSheet(filename, 0);
		    WritableFont wf = new WritableFont(WritableFont.TIMES,10, WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
		    WritableFont font = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.RED);
		    WritableFont font1 = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.ROSE);
		    
		    WritableCellFormat wcfF = new WritableCellFormat(wf);
		    WritableCellFormat wcfF1 = new WritableCellFormat(font);
		    WritableCellFormat wcfF2 = new WritableCellFormat(font1);
		    try {
				wcfF.setAlignment(Alignment.CENTRE);
				wcfF1.setAlignment(Alignment.CENTRE);
				wcfF2.setAlignment(Alignment.CENTRE);
			} catch (WriteException e) {
				e.printStackTrace();
			}
		    
		    if(checkItemNames.length() != 0){
		    	checkItemNames.trim();
		    	checkItemNames = checkItemNames.substring(0, checkItemNames.length()-1);
		    }
		    String[] checknames = checkItemNames.split(",");
		    String[] baseName = new String[]{"序号"};
		    int baseSize = 1;//基本信息列
		    
	    	//-----------第一行--------------------
		    for(int i=0;i<baseSize;i++){
		    	setLabel(i, 0,baseName[i], wcfF,ws);
		    }
			int col = 0;
			int size = checknames.length;
			for (int i = 0; i < size; i++) {
				setLabel(col+baseSize, 0, checknames[i], wcfF,ws);
				col++;
			}
			
			//第一列
		    for(int j = 0;j<maxMount;j++){
		    	int index = j + 1;
		    	setLabel(0, index, "X"+index, wcfF,ws);
		    }
		    
		    //质量参数
		    if(featureIds.length()!=0){
		    	featureIds.trim();
		    	featureIds = featureIds.substring(0,featureIds.length()-1);
		    }
		    String[] qualityIds = featureIds.split(",");
		    String layerNames = "";
	    	for(String id:qualityIds){
	    		if(id != null && !id.equals("")){
	    			QualityFeature qualityFeature = qualityFeatureManager.getQualityFeature(Long.valueOf(id));
			    	if(qualityFeature != null){
			    		if(qualityFeature.getFeatureLayers()!=null && qualityFeature.getFeatureLayers().size()!=0){
			    			for(FeatureLayer layer:qualityFeature.getFeatureLayers()){
			    				layerNames += layer.getDetailName() + ",";
			    			}
			    		}
			    	}
	    		}
		    }
		    if(layerNames != null && !layerNames.equals("")){
		    	layerNames.trim();
		    	layerNames = layerNames.substring(0,layerNames.length()-1);
		    	String[] depNames = layerNames.split(",");
		    	//Set<String> nameStr = new TreeSet<String>();
		    	Map<String,String> nameStr1 = new LinkedHashMap<String,String>();
		    	for(String name:depNames){
		    		//nameStr.add(name);
		    		nameStr1.put(name, name);
		    	}
		    	int index = 1;
//		    	Iterator<String> iterator = nameStr.iterator();
//				while(iterator.hasNext()){
//					setLabel(0, maxMount+index, iterator.next(), wcfF,ws);
//					index++;
//				}
				for(String key : nameStr1.keySet()){
					setLabel(0, maxMount+index, nameStr1.get(key).toString(), wcfF,ws);
					index++;
				}
		    }
		    
		    try {
				wwb.write();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		    finally {
				// 关闭Excel工作薄对象
				try {
					try {
						wwb.close();
					} catch (WriteException e) {
						e.printStackTrace();
					}
					os.close();
				} catch (IOException ex1) {
					ex1.printStackTrace();
				}
			}
	    }
		return null;
	}
	
	/**
	 * 设置单元格内容
	 *
	 */
	private void setLabel(int col, int row, String colName, WritableCellFormat wcfF, WritableSheet ws) {
		Label labelC0x = new jxl.write.Label(col, row, colName, wcfF);
		try {
			ws.addCell(labelC0x);
		} catch (RowsExceededException ex) {
			ex.printStackTrace();
		} catch (WriteException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 转换json
	 * @param params
	 * @return
	 */
	private JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		if(params != null){
			for(Object key : params.keySet()){
				resultJson.put(key,params.getJSONArray(key.toString()).get(0));
			}
		}
		return resultJson;
	}
	
}

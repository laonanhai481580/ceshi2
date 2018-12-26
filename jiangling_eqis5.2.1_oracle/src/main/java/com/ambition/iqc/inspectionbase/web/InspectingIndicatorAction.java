package com.ambition.iqc.inspectionbase.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.inspectionbase.service.IndicatorAttachManager;
import com.ambition.iqc.entity.InspectingIndicator;
import com.ambition.iqc.entity.InspectingItem;
import com.ambition.iqc.entity.ItemIndicator;
import com.ambition.iqc.entity.UseBaseType;
import com.ambition.iqc.inspectionbase.service.InspectingIndicatorManager;
import com.ambition.iqc.inspectionbase.service.InspectingItemManager;
import com.ambition.iqc.inspectionbase.service.ItemIndicatorManager;
import com.ambition.iqc.samplestandard.service.SampleCodeLetterManager;
import com.ambition.iqc.samplestandard.service.SampleSchemeManager;
import com.ambition.spc.dataacquisition.service.SpcSubGroupManager;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
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
 * InspectionIndicatorAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/iqc/inspection-base/indicator")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "iqc/inspection-base/indicator", type = "redirectAction") })
public class InspectingIndicatorAction extends CrudActionSupport<InspectingIndicator> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long indicatorId;//项目指标编号
	private Long nodeid;
	private Long parentId;
	private String expandIds;
	private String deleteIds;//删除的编号 
	private JSONObject params;//检验项目指标对象
	private String isSet;//是否设置
	private InspectingIndicator inspectingIndicator;
	
	@Autowired
	private SampleSchemeManager sampleSchemeManager;
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private SpcSubGroupManager spcSubGroupManager;
	
	private File myFile;
	
	private String myFileFileName;//导入文件名称

	@Autowired
	private LogUtilDao logUtilDao;
	
 	@Autowired
	private InspectingItemManager inspectingItemManager;//检验项目
 	
 	@Autowired
	private InspectingIndicatorManager inspectingIndicatorManager;//项目指标
 	
 	@Autowired
	private ItemIndicatorManager itemIndicatorManager;//检验项目指标
 	
 	@Autowired
 	private SampleCodeLetterManager sampleCodeLetterManager;
 	
 	@Autowired
 	private IndicatorAttachManager indicatorAttachManager;

 	private Page<InspectingIndicator> page;

	public Page<InspectingIndicator> getPage() {
		return page;
	}

	public void setPage(Page<InspectingIndicator> page) {
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

	public String getIsSet() {
		return isSet;
	}

	public void setIsSet(String isSet) {
		this.isSet = isSet;
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

	public String getExpandIds() {
		return expandIds;
	}

	public void setExpandIds(String expandIds) {
		this.expandIds = expandIds;
	}

	public Long getIndicatorId() {
		return indicatorId;
	}

	public void setIndicatorId(Long indicatorId) {
		this.indicatorId = indicatorId;
	}
	
	public InspectingIndicator getInspectingIndicator() {
		return inspectingIndicator;
	}

	public void setInspectingIndicator(InspectingIndicator inspectingIndicator) {
		this.inspectingIndicator = inspectingIndicator;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public String getMyFileFileName() {
		return myFileFileName;
	}

	public void setMyFileFileName(String myFileFileName) {
		this.myFileFileName = myFileFileName;
	}

	@Override
	public InspectingIndicator getModel() {
		return inspectingIndicator;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			inspectingIndicator = new InspectingIndicator();
			inspectingIndicator.setCreatedTime(new Date());
			inspectingIndicator.setCompanyId(ContextUtils.getCompanyId());
			inspectingIndicator.setCreator(ContextUtils.getUserName());
			inspectingIndicator.setLastModifiedTime(new Date());
			inspectingIndicator.setLastModifier(ContextUtils.getUserName());
			inspectingIndicator.setBusinessUnitName(ContextUtils.getSubCompanyName());
			inspectingIndicator.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			inspectingIndicator = inspectingIndicatorManager.getInspectingIndicator(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("save")
	@LogInfo(optType="保存",message="检验标准维护")
	@Override
	public String save() throws Exception {
		try {
			if(id == null){
				inspectingIndicatorManager.saveInspectingIndicator(inspectingIndicator);
				logUtilDao.debugLog("保存", inspectingIndicator.toString());
			}else{
				inspectingIndicator.setLastModifiedTime(new Date());
				inspectingIndicator.setLastModifier(ContextUtils.getUserName());
				inspectingIndicatorManager.saveInspectingIndicator(inspectingIndicator);
				logUtilDao.debugLog("修改", inspectingIndicator.toString());
			}
			this.renderText(JsonParser.getRowValue(inspectingIndicator));
		} catch (Exception e) {
			createErrorMessage("保存失败:" + e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="检验标准维护")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				inspectingIndicatorManager.deleteInspectingIndicator(deleteIds);
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
		return SUCCESS;
	}
	/**
	  * 方法名:查询最新版本检验标准 
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("list-datas")
	public String getBomListByParent() throws Exception {
		page = inspectingIndicatorManager.listMaxVersion(page);
		for(InspectingIndicator inspectingIndicator : page.getResult()){
			if(inspectingIndicator.getMaterielName() != null){
				inspectingIndicator.setMaterielName(inspectingIndicator.getMaterielName().replaceAll("\\n",""));
			}
		}
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	/**
	  * 方法名:查询所有版本的检验标准 
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("list-history")
	public String listHistory() throws Exception {
		return SUCCESS;
	}
	/**
	  * 方法名:查询所有版本的检验标准 
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("list-history-datas")
	public String listHistoryDatas() throws Exception {
		page = inspectingIndicatorManager.listAll(page,Struts2Utils.getParameter("materielCode"));
		for(InspectingIndicator inspectingIndicator : page.getResult()){
			if(inspectingIndicator.getMaterielName() != null){
				inspectingIndicator.setMaterielName(inspectingIndicator.getMaterielName().replaceAll("\\n",""));
			}
		}
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	/**
	 * 编辑检验项目指标
	 * @return
	 * @throws Exception
	 */
	@Action("edit-indicator")
	public String editIndicator() throws Exception {
		List<Option> countTypeOptions = ApiFactory.getSettingService().getOptionsByGroupCode("iqc_acceptance_quality_limit");
		StringBuffer sb = new StringBuffer("");
		for(Option option : countTypeOptions){
			if(sb.length()>0){
				sb.append(",");
			}
			sb.append("'" + option.getValue() + "':'" + option.getName() + "'");
		}
		ActionContext.getContext().put("countTypeEditOptions",sb.toString());
		UseBaseType useBaseType = sampleSchemeManager.getUseBaseType();
		JSONObject levelObject = new JSONObject();
		List<Option> options = sampleCodeLetterManager.getInspectionLevelOptions(useBaseType.getBaseType());
		sb.delete(0,sb.length());
		sb.append("'':'请选择...'");
		for(Option option : options){
			levelObject.put(option.getValue(),option.getName());
			sb.append(",'" + option.getValue() + "':'" + option.getName() + "'");
		}
		ActionContext.getContext().put("inspectionLevelOptions",sb.toString());
		ActionContext.getContext().put("levelObject",levelObject.toString());
		Struts2Utils.getRequest().setAttribute("baseType",useBaseType.getBaseType());
		//质量特性
		JSONObject featureObject = new JSONObject();
		List<QualityFeature> qualityFeatures = qualityFeatureManager.getList();
		if(qualityFeatures != null){
			options = spcSubGroupManager.convertListToOptions(qualityFeatures);
		}else{
			options = new ArrayList<Option>();
		}
		sb.delete(0,sb.length());
		for(Option option : options){
			if(option != null){
				featureObject.put(option.getValue(),option.getName());
				sb.append("'" + option.getValue() + "':'" + option.getName() + "',");
			}
		}
		if(sb.length() != 0){
			sb.delete(sb.length()-1,sb.length());
		}
		ActionContext.getContext().put("featureOptions",sb.toString());
		ActionContext.getContext().put("featureObject",featureObject.toString());
		return SUCCESS;
	}
	
	private void putInspectingItemParent(InspectingItem inspectingItem,Map<Long,ItemIndicator> selfMap){
		if(inspectingItem.getItemParent() != null){
			selfMap.put(inspectingItem.getItemParent().getId(),null);
			putInspectingItemParent(inspectingItem.getItemParent(),selfMap);
		}
	}
	/**
	 * 编辑检验项目的数据
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Action("edit-indicator-datas")
	public String editIndicatorDatas() throws Exception {
		if(indicatorId != null){
			inspectingIndicator = inspectingIndicatorManager.getInspectingIndicator(indicatorId);
		}else{
			inspectingIndicator = inspectingIndicatorManager.getInspectingIndicator(Struts2Utils.getParameter("materialCode"));
		}
		Page page = new Page();
		if(inspectingIndicator == null){
			renderText(PageUtils.pageToJson(page));
		}else{
			//所有已设置的检验项目
			List<ItemIndicator> itemIndicators = itemIndicatorManager.getAllItemIndicators(inspectingIndicator.getId());
			Map<Long,ItemIndicator> selfItemIndicatorMap = new HashMap<Long, ItemIndicator>();
			List<InspectingItem> parents = new ArrayList<InspectingItem>();
			for(ItemIndicator itemIndicator : itemIndicators){
				InspectingItem item = itemIndicator.getInspectingItem();
				selfItemIndicatorMap.put(item.getId(),itemIndicator);
				putInspectingItemParent(item,selfItemIndicatorMap);
				if(item.getItemParent() != null){
					if(!parents.contains(item.getItemParent())){
						parents.add(item.getItemParent());
					}
				}else{
					parents.add(item);
				}
			}
			
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(InspectingItem inspectingItem : parents){
				if(selfItemIndicatorMap.containsKey(inspectingItem.getId())){
					convertInspectingItem(inspectingItem,list,selfItemIndicatorMap);
				}
			}
			page.setResult(list);
			renderText(PageUtils.pageToJson(page));
		}
		return null;
	}
	
	/**
	 * 保存编辑的检验项目
	 * @return
	 * @throws Exception
	 */
	@Action("save-item")
	@LogInfo(optType="保存",message="保存编辑的检验项目")
	public String saveItem() throws Exception {
		try{
			InspectingIndicator inspectingIndicator = inspectingIndicatorManager.getInspectingIndicator(indicatorId);
			InspectingItem inspectingItem = inspectingItemManager.getInspectingItem(id);
			String featureId = Struts2Utils.getParameter("featureId")==null?"":Struts2Utils.getParameter("featureId");
			if(params==null){
				params = new JSONObject();
			}
				params.put("featureId", "['"+featureId+"']");
			String featureName = Struts2Utils.getParameter("featureName")==null?"":Struts2Utils.getParameter("featureName");
				params.put("featureName", "['"+featureName+"']");
			if(isSet != null){
				itemIndicatorManager.setItemIndicator(inspectingIndicator, inspectingItem, isSet,params);
				createMessage("操作成功!");
			}else{
				itemIndicatorManager.saveItemIndicator(inspectingIndicator, inspectingItem, params);
				createMessage("操作成功!");
			}
		}catch (Exception e) {
			e.printStackTrace();
			createErrorMessage("保存失败:" + e.getMessage());
		}
		return null;
	}
	/**
	 * 删除的检验项目
	 * @return
	 * @throws Exception
	 */
	@Action("delete-item")
	@LogInfo(optType="删除",message="删除检验项目")
	public String deleteItem() throws Exception {
		try{
			itemIndicatorManager.deleteItemIndicator(indicatorId);
			createMessage("删除成功!");
		}catch (Exception e) {
			e.printStackTrace();
			createErrorMessage("删除失败:" + e.getMessage());
		}
		return null;
	}
	/**
	 * 添加的检验项目
	 * @return
	 * @throws Exception
	 */
	@Action("add-item")
	@LogInfo(optType="添加",message="添加检验项目")
	public String addItem() throws Exception {
		try{
			int addCount = itemIndicatorManager.addItemIndicator(indicatorId, deleteIds);
			createMessage("操作成功!共添加了"+addCount + "个项目!");
		}catch (Exception e) {
			e.printStackTrace();
			createErrorMessage("操作失败:" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 复制检验标准
	 * @return
	 * @throws Exception
	 */
	@Action("copy-inspecting-indicator")
	@LogInfo(optType="复制",message="复制检验标准")
	public String copyInspectingIndicator() throws Exception {
		try{
			Map<String,Integer> result= itemIndicatorManager.copyInspectingIndicators(indicatorId, deleteIds);
			createMessage("操作成功!共添加成功了" + result.get("add") + "条物料的标准!"+result.get("repeat") + "条重复的物料未添加!");
		}catch (Exception e) {
			e.printStackTrace();
			createErrorMessage("操作失败:" + e.getMessage());
		}
		return null;
	}
	
	@Action("import-form")
	public String importForm() throws Exception {
		return SUCCESS;
	}
	
	@Action("imports")
	@LogInfo(optType="导入",message="导入检验标准")
	public String imports() throws Exception {
		try {
			String companyName = ContextUtils.getCompanyName();
			if(myFile != null){
//				if(companyName.equals("欧菲科技-CCM")){
//					renderHtml(itemIndicatorManager.importIndicatorCCM(myFile,myFileFileName));
//				}else{
//					renderHtml(itemIndicatorManager.importIndicator(myFile,myFileFileName));
//				}
				renderHtml(itemIndicatorManager.importIndicator(myFile,myFileFileName));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出",message="导出检验标准")
	public String export() throws Exception {
		try {
			Page<InspectingIndicator> page = new Page<InspectingIndicator>(Integer.MAX_VALUE);
			page = inspectingIndicatorManager.listMaxVersion(page);
//			List<InspectingIndicator> indicators = new ArrayList<InspectingIndicator>();
//			for(InspectingIndicator inspectingIndicator : page.getResult()){
//				InspectingIndicator indicator = new InspectingIndicator();
//				indicator.setMaterielCode(inspectingIndicator.getMaterielCode());
//				indicator.setMaterielName(inspectingIndicator.getMaterielName());
//				indicator.setRemark(inspectingIndicator.getRemark());
//				if(indicator.getMaterielCode() != null){
//					indicator.setMaterielCode(indicator.getMaterielCode().replace("\n",""));
//				}
//				if(indicator.getMaterielName() != null){
//					indicator.setMaterielName(indicator.getMaterielName().replace("\n",""));
//				}
//				indicators.add(indicator);
//			}
//			page.setResult(indicators);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"IQC_INSPECTING_INDICATOR"),"抽样标准"));
			logUtilDao.debugLog("导出", "进货检验管理：检验标准-导出检验标准");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Action("export-history")
	@LogInfo(optType="导出",message="导出历史检验标准")
	public String exportHistory() throws Exception {
		try {
			Page<InspectingIndicator> page = new Page<InspectingIndicator>(Integer.MAX_VALUE);
			page = inspectingIndicatorManager.listAll(page,Struts2Utils.getParameter("materielCode"));
//			List<InspectingIndicator> indicators = new ArrayList<InspectingIndicator>();
//			for(InspectingIndicator inspectingIndicator : page.getResult()){
//				InspectingIndicator indicator = new InspectingIndicator();
//				indicator.setMaterielCode(inspectingIndicator.getMaterielCode());
//				indicator.setMaterielName(inspectingIndicator.getMaterielName());
//				indicator.setRemark(inspectingIndicator.getRemark());
//				if(indicator.getMaterielCode() != null){
//					indicator.setMaterielCode(indicator.getMaterielCode().replace("\n",""));
//				}
//				if(indicator.getMaterielName() != null){
//					indicator.setMaterielName(indicator.getMaterielName().replace("\n",""));
//				}
//				indicators.add(indicator);
//			}
//			page.setResult(indicators);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"IQC_INSPECTING_INDICATOR"),"抽样标准"));
			logUtilDao.debugLog("导出", "进货检验管理：检验标准-导出检验标准");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	  * 方法名: 同步SPC历史页面
	  * @return
	  * @throws Exception
	 */
	@Action("synchro-spc-form")
	public String synchroSpcForm() throws Exception {
		ItemIndicator inspectingItemIndicator = itemIndicatorManager.getItemIndicator(Long.parseLong(Struts2Utils.getParameter("indicatorId")));
		QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureById(Long.valueOf(inspectingItemIndicator.getFeatureId()));
		ActionContext.getContext().getValueStack().push(qualityFeature);
		return SUCCESS;
	}
	/**
	  * 方法名: 同步SPC历史数据
	  * @return
	  * @throws Exception
	 */
	@Action("synchro-check-datas")
	@LogInfo(optType="同步",message="检验管理-基础设置-检验标准维护-同步SPC数据")
	public String synchroCheckDatas() throws Exception {
		try {
			params = CommonUtil1.convertJsonObject(params);
			String indicatorId = Struts2Utils.getParameter("indicatorId");
			String startDate = params.getString("startDate_ge_date");
			String endDate = params.getString("endDate_le_date");
			itemIndicatorManager.synchroCheckDatas(indicatorId,startDate,endDate);
			createMessage("同步成功");
		} catch (Exception e) {
			addActionError("同步失败："+e.getMessage());
			createErrorMessage("同步失败："+e.getMessage());
		}
		return null;
	}
	/**
	 * 转换检验项目结构至json对象
	 * @param inspectingItem
	 * @return
	 */
	private void convertInspectingItem(InspectingItem inspectingItem,List<Map<String,Object>> list,Map<Long,ItemIndicator> selfItemIndicatorMap){
		Boolean isLeaf = inspectingItem.getItemChildren().isEmpty();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id",inspectingItem.getId());
		map.put("itemName",inspectingItem.getItemName());
		map.put("itemLevel",inspectingItem.getItemLevel()-1);
		map.put("itemparent",inspectingItem.getItemParent()==null?"":inspectingItem.getItemParent().getId());
		map.put("isLeaf",isLeaf);
		list.add(map);
		ItemIndicator itemIndicator = selfItemIndicatorMap.get(inspectingItem.getId());
		if(!isLeaf){
			if(itemIndicator != null){
				map.put("itemIndicatorId",itemIndicator.getId());
			}
			map.put("expanded",true);
			map.put("loaded",true);
			for(InspectingItem child : inspectingItem.getItemChildren()){
				if(selfItemIndicatorMap.containsKey(child.getId())){
					convertInspectingItem(child,list,selfItemIndicatorMap);
				}
			}
		}else{
//			map.put("method",inspectingItem.getMethod());
			map.put("classification",inspectingItem.getClassification());
//			map.put("countType",inspectingItem.getCountType());
//			map.put("unit",inspectingItem.getUnit());
			map.put("loaded",true);
			if(itemIndicator != null){
				map.put("itemIndicatorId",itemIndicator.getId());
				map.put("params.indicatorMethod",itemIndicator.getIndicatorMethod());
				map.put("params.countType",itemIndicator.getCountType());
				map.put("params.indicatorUnit",itemIndicator.getIndicatorUnit());
				map.put("params.totalPoints",itemIndicator.getTotalPoints());
				map.put("params.specifications",itemIndicator.getSpecifications());
				map.put("params.aqlStandard",itemIndicator.getAqlStandard());
				map.put("params.inspectionLevel",itemIndicator.getInspectionLevel());
				map.put("params.inspectionAmount",itemIndicator.getInspectionAmount());
				map.put("params.levela",itemIndicator.getLevela());
				map.put("params.levelb",itemIndicator.getLevelb());
				map.put("params.massParameter",itemIndicator.getMassParameter());
				map.put("params.featureId",itemIndicator.getFeatureId());
				map.put("params.featureName",itemIndicator.getFeatureName());
				map.put("params.remark",itemIndicator.getRemark());
				map.put("canUse","yes");
				map.put("isSet","yes");
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
    /**
     * 保存附件名字
     */
	@Action("save-attach")
	@LogInfo(optType="保存附件",message="保存附件")
	public String getsaveAttach(){
		String filename=Struts2Utils.getParameter("filename");
	    String mid=Struts2Utils.getParameter("mid");;
		InspectingIndicator indicator=inspectingIndicatorManager.getInspectingIndicator(Long.valueOf(mid));
		if(filename.length()>0){
			indicator.setAttachmentFiles(filename);
			try{ 
				inspectingIndicatorManager.updateInspectingIndicator(indicator);
				}catch(Exception e){
					createErrorMessage("添加失败");
			}
		}
		createMessage("添加成功");
		return null;
	}
	
	/**
	   * 方法名: IIS文件
	   * <p>功能说明：</p>
	   * @return
	 * @throws IOException 
	  */
	@Action("download-attach")
	@LogInfo(optType="下载",message="下载检验标准文件")
	public String downloadAttach() throws IOException{
		indicatorAttachManager.downloadIISByAttachId(Struts2Utils.getResponse(),id);
		return null;
	}
	/**
	  * 方法名: 下载检验标准的模板
	  * <p>功能说明：下载检验标准的模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download-template")
	@LogInfo(optType="下载",message="进货检验标准模板")
	public String downloadTemplate() throws Exception {
		String fileName = "进货检验标准模板.xls";
		byte byname[] = fileName.getBytes("gbk");
		fileName = new String(byname, "8859_1");
		HttpServletResponse response = Struts2Utils.getResponse();
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", (new StringBuilder(
				"attachment; filename=\"")).append(fileName).append("\"")
				.toString());
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/iqc-inspection-template.xlsx");
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes);
		response.getOutputStream().write(bytes);
		inputStream.close();
		return null;
	}
}

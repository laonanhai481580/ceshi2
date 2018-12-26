package com.ambition.carmfg.inspectionbase.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.entity.MfgInspectingIndicator;
import com.ambition.carmfg.entity.MfgInspectingItem;
import com.ambition.carmfg.entity.MfgItemIndicator;
import com.ambition.carmfg.entity.PatrolSettings;
import com.ambition.carmfg.inspectionbase.service.IndicatorAttachManager;
import com.ambition.carmfg.inspectionbase.service.MfgInspectingIndicatorManager;
import com.ambition.carmfg.inspectionbase.service.MfgInspectingItemManager;
import com.ambition.carmfg.inspectionbase.service.MfgItemIndicatorManager;
import com.ambition.iqc.entity.UseBaseType;
import com.ambition.iqc.samplestandard.service.SampleCodeLetterManager;
import com.ambition.iqc.samplestandard.service.SampleSchemeManager;
import com.ambition.product.BaseAction;
import com.ambition.spc.dataacquisition.service.SpcSubGroupManager;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.exception.AmbFrameException;
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
@Namespace("/carmfg/inspection-base/indicator")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "carmfg/inspection-base/indicator", type = "redirectAction") })
public class MfgInspectingIndicatorAction extends BaseAction<MfgInspectingIndicator> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long indicatorId;//项目指标编号
	private Long nodeid;
	private Long parentId;
	private String expandIds;
	private String deleteIds;//删除的编号 
	private JSONObject params;//检验项目指标对象
	private String isSet;//是否设置
	private MfgInspectingIndicator mfgInspectingIndicator;
	
	@Autowired
	private IndicatorAttachManager indicatorAttachManager;
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
	private MfgInspectingItemManager mfgInspectingItemManager;//检验项目
 	
 	@Autowired
	private MfgInspectingIndicatorManager mfgInspectingIndicatorManager;//项目指标
 	
 	@Autowired
	private MfgItemIndicatorManager mfgItemIndicatorManager;//检验项目指标
 	
 	@Autowired
 	private SampleCodeLetterManager sampleCodeLetterManager;
 	

 	private Page<MfgInspectingIndicator> page;

	public Page<MfgInspectingIndicator> getPage() {
		return page;
	}

	public void setPage(Page<MfgInspectingIndicator> page) {
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
	
	public MfgInspectingIndicator getMfgInspectingIndicator() {
		return mfgInspectingIndicator;
	}

	public void setMfgInspectingIndicator(MfgInspectingIndicator mfgInspectingIndicator) {
		this.mfgInspectingIndicator = mfgInspectingIndicator;
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
	public MfgInspectingIndicator getModel() {
		return mfgInspectingIndicator;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			mfgInspectingIndicator = new MfgInspectingIndicator();
			mfgInspectingIndicator.setCreatedTime(new Date());
			mfgInspectingIndicator.setCompanyId(ContextUtils.getCompanyId());
			mfgInspectingIndicator.setCreator(ContextUtils.getUserName());
			mfgInspectingIndicator.setLastModifiedTime(new Date());
			mfgInspectingIndicator.setLastModifier(ContextUtils.getUserName());
			mfgInspectingIndicator.setBusinessUnitName(ContextUtils.getSubCompanyName());
			mfgInspectingIndicator.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			mfgInspectingIndicator = mfgInspectingIndicatorManager.getInspectingIndicator(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("save")
	@LogInfo(optType="保存",message="制造检验标准维护")
	@Override
	public String save() throws Exception {
		try {
			if(id == null){
				mfgInspectingIndicatorManager.saveInspectingIndicator(mfgInspectingIndicator);
				logUtilDao.debugLog("保存", mfgInspectingIndicator.toString());
			}else{
				mfgInspectingIndicator.setLastModifiedTime(new Date());
				mfgInspectingIndicator.setLastModifier(ContextUtils.getUserName());
				mfgInspectingIndicatorManager.saveInspectingIndicator(mfgInspectingIndicator);
				logUtilDao.debugLog("修改", mfgInspectingIndicator.toString());
			}
			this.renderText(JsonParser.getRowValue(mfgInspectingIndicator));
		} catch (Exception e) {
			createErrorMessage("保存失败:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				String str=mfgInspectingIndicatorManager.deleteInspectingIndicator(deleteIds);
				createMessage("删除成功!");
				Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除检验标准，机种为："+str);
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
	
	@Action("list-datas")
	public String getBomListByParent() throws Exception {
		page = mfgInspectingIndicatorManager.listMaxVersion(page);
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	@Action("list-history")
	public String listHistory() throws Exception {
		renderMenu();
		return SUCCESS;
	}
	
	@Action("list-history-datas")
	public String listHostoryDatas() throws Exception {
		page = mfgInspectingIndicatorManager.listAll(page,Struts2Utils.getParameter("workingProcedure"),Struts2Utils.getParameter("materielCode"));
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
		List<Option> mfg_isok =  ApiFactory.getSettingService().getOptionsByGroupCode("mfg_isok");
		sb.delete(0,sb.length());
		sb.append("'':'请选择...'");
		JSONObject isJnUnitOjbect = new JSONObject();
		for(Option option : mfg_isok){
			isJnUnitOjbect.put(option.getValue(),option.getName());
			sb.append(",'" + option.getValue() + "':'" + option.getName() + "'");
		}
		
		ActionContext.getContext().put("isJnUnitOjbect",sb.toString());
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
	
	private void putInspectingItemParent(MfgInspectingItem mfgInspectingItem,Map<Long,MfgItemIndicator> selfMap){
		if(mfgInspectingItem.getItemParent() != null){
			selfMap.put(mfgInspectingItem.getItemParent().getId(),null);
			putInspectingItemParent(mfgInspectingItem.getItemParent(),selfMap);
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
			mfgInspectingIndicator = mfgInspectingIndicatorManager.getInspectingIndicator(indicatorId);
		}else{
			mfgInspectingIndicator = mfgInspectingIndicatorManager.getInspectingIndicator(Struts2Utils.getParameter("workingProcedure"),Struts2Utils.getParameter("materialCode"));
		}
		Page page = new Page();
		if(mfgInspectingIndicator == null){
			renderText(PageUtils.pageToJson(page));
		}else{
			//所有已设置的检验项目
			List<MfgItemIndicator> mfgItemIndicators = mfgItemIndicatorManager.getAllItemIndicators(mfgInspectingIndicator.getId());
			Map<Long,MfgItemIndicator> selfItemIndicatorMap = new HashMap<Long, MfgItemIndicator>();
			List<MfgInspectingItem> parents = new ArrayList<MfgInspectingItem>();
			for(MfgItemIndicator mfgItemIndicator : mfgItemIndicators){
				MfgInspectingItem mfgItem = mfgItemIndicator.getMfgInspectingItem();
				selfItemIndicatorMap.put(mfgItem.getId(),mfgItemIndicator);
				putInspectingItemParent(mfgItem,selfItemIndicatorMap);
				if(mfgItem.getItemParent() != null){
					if(!parents.contains(mfgItem.getItemParent())){
						parents.add(mfgItem.getItemParent());
					}
				}else{
					parents.add(mfgItem);
				}
			}
			
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(MfgInspectingItem mfgInspectingItem : parents){
				if(selfItemIndicatorMap.containsKey(mfgInspectingItem.getId())){
					convertInspectingItem(mfgInspectingItem,list,selfItemIndicatorMap);
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
			MfgInspectingIndicator mfgInspectingIndicator =mfgInspectingIndicatorManager.getInspectingIndicator(indicatorId);
			MfgInspectingItem mfgInspectingItem = mfgInspectingItemManager.getInspectingItem(id);
			String featureId = Struts2Utils.getParameter("featureId")==null?"":Struts2Utils.getParameter("featureId");
			if(params==null){
				params = new JSONObject();
			}
//			if(StringUtils.isNotEmpty(featureId)){
				params.put("featureId", "['"+featureId+"']");
//			}
			String featureName = Struts2Utils.getParameter("featureName")==null?"":Struts2Utils.getParameter("featureName");
//			if(StringUtils.isNotEmpty(featureId)){
				params.put("featureName", "['"+featureName+"']");
//			}
			if(isSet != null){
				mfgItemIndicatorManager.setItemIndicator(mfgInspectingIndicator, mfgInspectingItem, isSet,params);
				createMessage("操作成功!");
			}else{
				mfgItemIndicatorManager.saveItemIndicator(mfgInspectingIndicator, mfgInspectingItem, params);
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
			mfgItemIndicatorManager.deleteItemIndicator(indicatorId);
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
			int addCount = mfgItemIndicatorManager.addItemIndicator(indicatorId, deleteIds);
			createMessage("操作成功!共添加了"+addCount + "个项目!");
		}catch (Exception e) {
			log.error("添加检验项目失败!",e);
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
			Map<String,Integer> result= mfgItemIndicatorManager.copyInspectingIndicators(indicatorId, deleteIds,Struts2Utils.getParameter("workingProcedure"));
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
			if(myFile != null){
				renderHtml(mfgItemIndicatorManager.importIndicator(myFile,myFileFileName));
			}
		} catch (Exception e) {
			if(!(e instanceof AmbFrameException)){
				log.error("导入检验标准失败",e);
			}
			renderHtml("<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	
	@Action("import-patrol")
	@LogInfo(optType="导入",message="导入工艺检验标准")
	public String importPatrol() throws Exception {
		try {
			if(myFile != null){
				renderHtml(mfgItemIndicatorManager.importPatrol(myFile));
			}
		} catch (Exception e) {
			if(!(e instanceof AmbFrameException)){
				log.error("导入工艺纪律检验标准失败",e);
			}
			renderHtml("<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	
	@Action("export")
	@LogInfo(optType="导出",message="导出检验标准")
	public String export() throws Exception {
		try {
			Page<MfgInspectingIndicator> page = new Page<MfgInspectingIndicator>(Integer.MAX_VALUE);
			page = mfgInspectingIndicatorManager.listMaxVersion(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_INSPECTING_INDICATOR"),"检验标准"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Action("export-history")
	@LogInfo(optType="导出",message="导出历史检验标准")
	public String exportHistory() throws Exception {
		try {
			Page<MfgInspectingIndicator> page = new Page<MfgInspectingIndicator>(Integer.MAX_VALUE);
			page = mfgInspectingIndicatorManager.listAll(page,Struts2Utils.getParameter("workingProcedure"),Struts2Utils.getParameter("materielCode"));
//			List<MfgInspectingIndicator> indicators = new ArrayList<MfgInspectingIndicator>();
//			for(MfgInspectingIndicator mfgInspectingIndicator : page.getResult()){
//				MfgInspectingIndicator indicator = new MfgInspectingIndicator();
//				indicator.setWorkingProcedure(mfgInspectingIndicator.getWorkingProcedure());
//				indicator.setMaterielCode(mfgInspectingIndicator.getMaterielCode());
//				indicator.setMaterielName(mfgInspectingIndicator.getMaterielName());
//				indicator.setRemark(mfgInspectingIndicator.getRemark());
//				if(indicator.getMaterielCode() != null){
//					indicator.setMaterielCode(indicator.getMaterielCode().replace("\n",""));
//				}
//				if(indicator.getMaterielName() != null){
//					indicator.setMaterielName(indicator.getMaterielName().replace("\n",""));
//				}
//				indicators.add(indicator);
//			}
//			page.setResult(indicators);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"IQC_INSPECTING_INDICATOR"),"抽样标准"));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	@LogInfo(optType="下载",message="下载检验标准的模板")
	public String downloadTemplate() throws Exception {
		String fileName = "制程检验标准模板.xls";
		byte byname[] = fileName.getBytes("gbk");
		fileName = new String(byname, "8859_1");
		HttpServletResponse response = Struts2Utils.getResponse();
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", (new StringBuilder(
				"attachment; filename=\"")).append(fileName).append("\"")
				.toString());
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/mfg-template.xls");
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes);
		response.getOutputStream().write(bytes);
		inputStream.close();
		return null;
	}
	
	/**
	  * 方法名: 保存巡检周期设置
	  * @return
	  * @throws Exception
	 */
	@Action("save-patrol-settings")
	@LogInfo(optType="修改",message="保存巡检周期设置")
	public String savePatrolSettings() throws Exception {
		try{
			PatrolSettings patrolSettings = new PatrolSettings();
			patrolSettings.setTimeIntervalType(Struts2Utils.getParameter("timeIntervalType"));
			patrolSettings.setTimeIntervalValue(Struts2Utils.getParameter("timeIntervalValue"));
			patrolSettings.setRemindSwitch(Boolean.valueOf(Struts2Utils.getParameter("remindSwitch")));
			if(patrolSettings.getRemindSwitch()){
				patrolSettings.setReceiveTypes(Struts2Utils.getParameter("receiveType"));
				patrolSettings.setReceiveUserIds(Struts2Utils.getParameter("receiveUserIds"));
				patrolSettings.setReceiveUserNames(Struts2Utils.getParameter("receiveUserNames"));
				patrolSettings.setRemindTimeType(Struts2Utils.getParameter("remindTimeType"));
				patrolSettings.setRemindTimeValue(Struts2Utils.getParameter("remindTimeValue"));
				patrolSettings.setTriggerType(Struts2Utils.getParameter("triggerType"));
				patrolSettings.setTriggerValue(Struts2Utils.getParameter("triggerValue"));
			}
			mfgInspectingIndicatorManager.savePatrolSettings(patrolSettings,Struts2Utils.getParameter("selFlag"), page);
			createMessage("设置成功!");
		}catch (Exception e) {
			createErrorMessage("保存失败:" + e.getMessage());
			log.error("保存巡检周期配置出错!",e);
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
		MfgItemIndicator inspectingItemIndicator = mfgItemIndicatorManager.getItemIndicator(Long.parseLong(Struts2Utils.getParameter("indicatorId")));
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
			mfgItemIndicatorManager.synchroCheckDatas(indicatorId,startDate,endDate);
			createMessage("同步成功");
		} catch (Exception e) {
			log.error("同步失败：",e);
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
	private void convertInspectingItem(MfgInspectingItem mfgInspectingItem,List<Map<String,Object>> list,Map<Long,MfgItemIndicator> selfItemIndicatorMap){
		Boolean isLeaf = mfgInspectingItem.getItemChildren().isEmpty();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id",mfgInspectingItem.getId());
		map.put("name",mfgInspectingItem.getItemName());
		map.put("level",mfgInspectingItem.getItemLevel()-1);
		map.put("parent",mfgInspectingItem.getItemParent()==null?"":mfgInspectingItem.getItemParent().getId());
		map.put("isLeaf",isLeaf);
		list.add(map);
		MfgItemIndicator mfgItemIndicator = selfItemIndicatorMap.get(mfgInspectingItem.getId());
		if(!isLeaf){
			if(mfgItemIndicator != null){
				map.put("itemIndicatorId",mfgItemIndicator.getId());
			}
			map.put("expanded",true);
			map.put("loaded",true);
			for(MfgInspectingItem child : mfgInspectingItem.getItemChildren()){
				if(selfItemIndicatorMap.containsKey(child.getId())){
					convertInspectingItem(child,list,selfItemIndicatorMap);
				}
			}
		}else{
//			map.put("method",mfgInspectingItem.getMethod());
//			map.put("countType",mfgInspectingItem.getCountType());
//			map.put("unit",mfgInspectingItem.getUnit());
			map.put("loaded",true);
			if(mfgItemIndicator != null){
				map.put("itemIndicatorId",mfgItemIndicator.getId());
				map.put("params.method",mfgItemIndicator.getMethod());
				map.put("params.countType",mfgItemIndicator.getCountType());
				map.put("params.unit",mfgItemIndicator.getUnit());
				map.put("params.totalPoints",mfgItemIndicator.getTotalPoints());
				map.put("params.specifications",mfgItemIndicator.getSpecifications());
				map.put("params.aqlStandard",mfgItemIndicator.getAqlStandard());
				map.put("params.inspectionLevel",mfgItemIndicator.getInspectionLevel());
				map.put("params.inspectionAmount",mfgItemIndicator.getInspectionAmount());
				map.put("params.levela",mfgItemIndicator.getLevela());
				map.put("params.levelb",mfgItemIndicator.getLevelb());
				map.put("params.inAmountFir",mfgItemIndicator.getInAmountFir());
				map.put("params.inAmountPatrol",mfgItemIndicator.getInAmountPatrol());
				map.put("params.inAmountEnd",mfgItemIndicator.getInAmountEnd());
				map.put("params.isJnUnit",mfgItemIndicator.getIsJnUnit());
				map.put("params.isInEquipment",mfgItemIndicator.getIsInEquipment());
				map.put("params.featureId",mfgItemIndicator.getFeatureId());
				map.put("params.featureName",mfgItemIndicator.getFeatureName());
				map.put("params.remark",mfgItemIndicator.getRemark());
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
	
	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(99);
		list.add(67);
		list.add(-1);
		list.add(199);
		list.add(78);
		list.add(7);
		list.add(11);
		list.add(23);
		Collections.sort(list,new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				if(o1 < o2){
					return -1;
				}else if(o1 > o2){
					return 1;
				}else{
					return 0;
				}
			}
		});
	}
}

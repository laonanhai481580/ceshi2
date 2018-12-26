package com.ambition.si.baseinfo.web;

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

import com.ambition.carmfg.entity.PatrolSettings;
import com.ambition.carmfg.inspectionbase.service.IndicatorAttachManager;
import com.ambition.iqc.entity.UseBaseType;
import com.ambition.iqc.samplestandard.service.SampleCodeLetterManager;
import com.ambition.iqc.samplestandard.service.SampleSchemeManager;
import com.ambition.product.BaseAction;
import com.ambition.si.baseinfo.service.SiInspectingIndicatorManager;
import com.ambition.si.baseinfo.service.SiInspectingItemManager;
import com.ambition.si.baseinfo.service.SiItemIndicatorManager;
import com.ambition.si.entity.SiInspectingIndicator;
import com.ambition.si.entity.SiInspectingItem;
import com.ambition.si.entity.SiItemIndicator;
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
@Namespace("/si/base-info/indicator")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "si/base-info/indicator", type = "redirectAction") })
public class SiInspectingIndicatorAction extends BaseAction<SiInspectingIndicator> {
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
	private SiInspectingIndicator siInspectingIndicator;
	
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
	private SiInspectingItemManager siInspectingItemManager;//检验项目
 	
 	@Autowired
	private SiInspectingIndicatorManager siInspectingIndicatorManager;//项目指标
 	
 	@Autowired
	private SiItemIndicatorManager siItemIndicatorManager;//检验项目指标
 	
 	@Autowired
 	private SampleCodeLetterManager sampleCodeLetterManager;
 	

 	private Page<SiInspectingIndicator> page;

	public Page<SiInspectingIndicator> getPage() {
		return page;
	}

	public void setPage(Page<SiInspectingIndicator> page) {
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
	
	public SiInspectingIndicator getSiInspectingIndicator() {
		return siInspectingIndicator;
	}

	public void setSiInspectingIndicator(SiInspectingIndicator siInspectingIndicator) {
		this.siInspectingIndicator = siInspectingIndicator;
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
	public SiInspectingIndicator getModel() {
		return siInspectingIndicator;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			siInspectingIndicator = new SiInspectingIndicator();
			siInspectingIndicator.setCreatedTime(new Date());
			siInspectingIndicator.setCompanyId(ContextUtils.getCompanyId());
			siInspectingIndicator.setCreator(ContextUtils.getUserName());
			siInspectingIndicator.setLastModifiedTime(new Date());
			siInspectingIndicator.setLastModifier(ContextUtils.getUserName());
			siInspectingIndicator.setBusinessUnitName(ContextUtils.getSubCompanyName());
			siInspectingIndicator.setBusinessUnitCode(CommonUtil1.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			siInspectingIndicator = siInspectingIndicatorManager.getInspectingIndicator(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("save")
	@Override
	public String save() throws Exception {
		try {
			if(id == null){
				siInspectingIndicatorManager.saveInspectingIndicator(siInspectingIndicator);
				logUtilDao.debugLog("保存", siInspectingIndicator.toString());
			}else{
				siInspectingIndicator.setLastModifiedTime(new Date());
				siInspectingIndicator.setLastModifier(ContextUtils.getUserName());
				siInspectingIndicatorManager.saveInspectingIndicator(siInspectingIndicator);
				logUtilDao.debugLog("修改", siInspectingIndicator.toString());
			}
			this.renderText(JsonParser.getRowValue(siInspectingIndicator));
		} catch (Exception e) {
			createErrorMessage("保存失败:" + e.getMessage());
			e.printStackTrace();
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
				siInspectingIndicatorManager.deleteInspectingIndicator(deleteIds);
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
	
	@Action("list-datas")
	public String getBomListByParent() throws Exception {
		page = siInspectingIndicatorManager.listMaxVersion(page);
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
		page = siInspectingIndicatorManager.listAll(page,Struts2Utils.getParameter("workingProcedure"),Struts2Utils.getParameter("materielCode"));
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
		List<Option> si_isok =  ApiFactory.getSettingService().getOptionsByGroupCode("si_isok");
		sb.delete(0,sb.length());
		sb.append("'':'请选择...'");
		JSONObject isJnUnitOjbect = new JSONObject();
		for(Option option : si_isok){
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
	
	private void putInspectingItemParent(SiInspectingItem siInspectingItem,Map<Long,SiItemIndicator> selfMap){
		if(siInspectingItem.getItemParent() != null){
			selfMap.put(siInspectingItem.getItemParent().getId(),null);
			putInspectingItemParent(siInspectingItem.getItemParent(),selfMap);
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
			siInspectingIndicator = siInspectingIndicatorManager.getInspectingIndicator(indicatorId);
		}else{
			siInspectingIndicator = siInspectingIndicatorManager.getInspectingIndicator(Struts2Utils.getParameter("workingProcedure"),Struts2Utils.getParameter("materialCode"));
		}
		Page page = new Page();
		if(siInspectingIndicator == null){
			renderText(PageUtils.pageToJson(page));
		}else{
			//所有已设置的检验项目
			List<SiItemIndicator> siItemIndicators = siItemIndicatorManager.getAllItemIndicators(siInspectingIndicator.getId());
			Map<Long,SiItemIndicator> selfItemIndicatorMap = new HashMap<Long, SiItemIndicator>();
			List<SiInspectingItem> parents = new ArrayList<SiInspectingItem>();
			for(SiItemIndicator siItemIndicator : siItemIndicators){
				SiInspectingItem siItem = siItemIndicator.getSiInspectingItem();
				selfItemIndicatorMap.put(siItem.getId(),siItemIndicator);
				putInspectingItemParent(siItem,selfItemIndicatorMap);
				if(siItem.getItemParent() != null){
					if(!parents.contains(siItem.getItemParent())){
						parents.add(siItem.getItemParent());
					}
				}else{
					parents.add(siItem);
				}
			}
			
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(SiInspectingItem siInspectingItem : parents){
				if(selfItemIndicatorMap.containsKey(siInspectingItem.getId())){
					convertInspectingItem(siInspectingItem,list,selfItemIndicatorMap);
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
	public String saveItem() throws Exception {
		try{
			SiInspectingIndicator siInspectingIndicator =siInspectingIndicatorManager.getInspectingIndicator(indicatorId);
			SiInspectingItem siInspectingItem = siInspectingItemManager.getInspectingItem(id);
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
				siItemIndicatorManager.setItemIndicator(siInspectingIndicator, siInspectingItem, isSet,params);
				createMessage("操作成功!");
			}else{
				siItemIndicatorManager.saveItemIndicator(siInspectingIndicator, siInspectingItem, params);
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
	public String deleteItem() throws Exception {
		try{
			siItemIndicatorManager.deleteItemIndicator(indicatorId);
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
	public String addItem() throws Exception {
		try{
			int addCount = siItemIndicatorManager.addItemIndicator(indicatorId, deleteIds);
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
	public String copyInspectingIndicator() throws Exception {
		try{
			Map<String,Integer> result= siItemIndicatorManager.copyInspectingIndicators(indicatorId, deleteIds,Struts2Utils.getParameter("workingProcedure"));
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
				renderHtml(siItemIndicatorManager.importIndicator(myFile,myFileFileName));
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
				renderHtml(siItemIndicatorManager.importPatrol(myFile));
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
	public String export() throws Exception {
		try {
			Page<SiInspectingIndicator> page = new Page<SiInspectingIndicator>(Integer.MAX_VALUE);
			page = siInspectingIndicatorManager.listMaxVersion(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_INSPECTING_INDICATOR"),"检验标准"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Action("export-history")
	public String exportHistory() throws Exception {
		try {
			Page<SiInspectingIndicator> page = new Page<SiInspectingIndicator>(Integer.MAX_VALUE);
			page = siInspectingIndicatorManager.listAll(page,Struts2Utils.getParameter("workingProcedure"),Struts2Utils.getParameter("materielCode"));
//			List<SiInspectingIndicator> indicators = new ArrayList<SiInspectingIndicator>();
//			for(SiInspectingIndicator siInspectingIndicator : page.getResult()){
//				SiInspectingIndicator indicator = new SiInspectingIndicator();
//				indicator.setWorkingProcedure(siInspectingIndicator.getWorkingProcedure());
//				indicator.setMaterielCode(siInspectingIndicator.getMaterielCode());
//				indicator.setMaterielName(siInspectingIndicator.getMaterielName());
//				indicator.setRemark(siInspectingIndicator.getRemark());
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
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/si-template.xls");
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes);
		response.getOutputStream().write(bytes);
		inputStream.close();
		return null;
	}
	/**
	  * 方法名: 同步SPC历史页面
	  * @return
	  * @throws Exception
	 */
	@Action("synchro-spc-form")
	public String synchroSpcForm() throws Exception {
		SiItemIndicator inspectingItemIndicator = siItemIndicatorManager.getItemIndicator(Long.parseLong(Struts2Utils.getParameter("indicatorId")));
		QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureById(Long.valueOf(inspectingItemIndicator.getFeatureId()));
		ActionContext.getContext().getValueStack().push(qualityFeature);
		return SUCCESS;
	}
	/**
	 * 转换检验项目结构至json对象
	 * @param inspectingItem
	 * @return
	 */
	private void convertInspectingItem(SiInspectingItem siInspectingItem,List<Map<String,Object>> list,Map<Long,SiItemIndicator> selfItemIndicatorMap){
		Boolean isLeaf = siInspectingItem.getItemChildren().isEmpty();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id",siInspectingItem.getId());
		map.put("name",siInspectingItem.getItemName());
		map.put("level",siInspectingItem.getItemLevel()-1);
		map.put("parent",siInspectingItem.getItemParent()==null?"":siInspectingItem.getItemParent().getId());
		map.put("isLeaf",isLeaf);
		list.add(map);
		SiItemIndicator siItemIndicator = selfItemIndicatorMap.get(siInspectingItem.getId());
		if(!isLeaf){
			if(siItemIndicator != null){
				map.put("itemIndicatorId",siItemIndicator.getId());
			}
			map.put("expanded",true);
			map.put("loaded",true);
			for(SiInspectingItem child : siInspectingItem.getItemChildren()){
				if(selfItemIndicatorMap.containsKey(child.getId())){
					convertInspectingItem(child,list,selfItemIndicatorMap);
				}
			}
		}else{
//			map.put("method",siInspectingItem.getMethod());
//			map.put("countType",siInspectingItem.getCountType());
//			map.put("unit",siInspectingItem.getUnit());
			map.put("loaded",true);
			if(siItemIndicator != null){
				map.put("itemIndicatorId",siItemIndicator.getId());
				map.put("params.method",siItemIndicator.getMethod());
			//	map.put("params.countType",siItemIndicator.getCountType());
				map.put("params.unit",siItemIndicator.getUnit());
				map.put("params.totalPoints",siItemIndicator.getTotalPoints());
				map.put("params.specifications",siItemIndicator.getSpecifications());
				map.put("params.aqlStandard",siItemIndicator.getAqlStandard());
				map.put("params.inspectionLevel",siItemIndicator.getInspectionLevel());
				map.put("params.inspectionAmount",siItemIndicator.getInspectionAmount());
				map.put("params.levela",siItemIndicator.getLevela());
				map.put("params.levelb",siItemIndicator.getLevelb());
				map.put("params.inAmountFir",siItemIndicator.getInAmountFir());
				map.put("params.inAmountPatrol",siItemIndicator.getInAmountPatrol());
				map.put("params.inAmountEnd",siItemIndicator.getInAmountEnd());
				map.put("params.isJnUnit",siItemIndicator.getIsJnUnit());
				map.put("params.isInEquipment",siItemIndicator.getIsInEquipment());
				map.put("params.featureId",siItemIndicator.getFeatureId());
				map.put("params.featureName",siItemIndicator.getFeatureName());
				map.put("params.remark",siItemIndicator.getRemark());
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

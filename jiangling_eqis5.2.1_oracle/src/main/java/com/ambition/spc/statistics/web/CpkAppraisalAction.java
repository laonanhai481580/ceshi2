package com.ambition.spc.statistics.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.spc.bsrules.service.BsRulesManager;
import com.ambition.spc.entity.BsRules;
import com.ambition.spc.entity.FeatureLayer;
import com.ambition.spc.entity.LayerType;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.jlanalyse.entity.JLOriginalData;
import com.ambition.spc.jlanalyse.service.JlanalyseDrawManager;
import com.ambition.spc.jlanalyse.util.JLcalculator;
import com.ambition.spc.layertype.service.LayerTypeManager;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.spc.statistics.service.CpMoudleanager;
import com.ambition.spc.statistics.service.CpkAppraisalTableManager;
import com.ambition.spc.statistics.service.CpkTrendDatasManager;
import com.ambition.spc.statistics.service.DrawControlManager;
import com.ambition.spc.statistics.service.DrawHistogramDrawManager;
import com.ambition.spc.statistics.service.StatisticsManager;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**    
 * CpkAppraisalAction.java(CPK过程能力考评)
 * @authorBy wanglf
 *
 */
@Namespace("/spc/statistics-analysis")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "spc/statistics-analysis", type = "redirectAction") })
public class CpkAppraisalAction extends com.ambition.product.base.CrudActionSupport<BsRules> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private BsRules bsRules;
	private Page<BsRules> page = new Page<BsRules>(Page.EACH_PAGE_TEN, true);
	private String deleteIds;
	private JSONObject params;
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private JlanalyseDrawManager jlanalyseDrawManager;
	@Autowired
	private StatisticsManager statisticsManager;
	@Autowired
	private LayerTypeManager layerTypeManager;
	@Autowired
	private BsRulesManager bsRulesManager;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private CpkTrendDatasManager cpkTrendDatasManager;
	@Autowired
	private DrawControlManager drawControlManager;
	@Autowired
	private DrawHistogramDrawManager drawHistogramDrawManager;
	@Autowired
	private CpkAppraisalTableManager cpkAppraisalTableManager;
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setPage(Page<BsRules> page) {
		this.page = page;
	}
	
	public Page<BsRules> getPage() {
		return page;
	}
	
	public BsRules getModel() {
		return bsRules;
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

	@Override
	protected void prepareModel() throws Exception {
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	
	@Action("save")
	@Override
	public String save() throws Exception {
		return null;
	}
	
	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String list() throws Exception {
		return null;
	}
	
	/**
	 * CPK过程能力考评表页面
	 * @return
	 * @throws Exception
	 */
	@Action("cpk-appraisal")
	public String cpkAppraisal() throws Exception {
		//质量特性
		List<QualityFeature> qualityFeatures = new ArrayList<QualityFeature>();
		QualityFeature firstFeature = qualityFeatureManager.queryFirst();
		if(firstFeature != null){
			qualityFeatures.add(firstFeature);
		}
		ActionContext.getContext().put("qualityFeatures",jlanalyseDrawManager.convertListToOptions(qualityFeatures));
		
		List<LayerType> layerTypes=layerTypeManager.getLayerTypes();
		ActionContext.getContext().put("layerTypes", layerTypes);
		return SUCCESS;
	}
	
	/**
	 * CPK过程能力考评表数据
	 * @return
	 * @throws Exception
	 */
	@Action("cpk-appraisal-table")
	public String cpkAppraisalTable() throws Exception {
		params = CommonUtil1.convertJsonObject(params);
		String startDateStr=params.getString("startDate_ge_date");
		String endDateStr=params.getString("endDate_le_date");
		String subtitle=startDateStr+"—"+endDateStr;
		String qualityFeatureIds=params.getString("qualityFeatures");
		List<String[]> result = cpkAppraisalTableManager.getcpkAppraisalTable(startDateStr, endDateStr, qualityFeatureIds,params);
		ActionContext.getContext().put("result", result);
		ActionContext.getContext().put("subtitle", subtitle);
		return SUCCESS;
	}
	
	/**
	 * CPK推移图页面
	 * @return
	 * @throws Exception
	 */
	@Action("cpk-trend")
	public String cpkTrend() throws Exception {
		//质量特性

		List<QualityFeature> qualityFeatures = new ArrayList<QualityFeature>();
		
		QualityFeature firstFeature = qualityFeatureManager.queryFirst();
		if(firstFeature != null){
			qualityFeatures.add(firstFeature);
		}
		ActionContext.getContext().put("qualityFeatures",jlanalyseDrawManager.convertListToOptions(qualityFeatures));
		
		List<LayerType> layerTypes=layerTypeManager.getLayerTypes();
		ActionContext.getContext().put("layerTypes", layerTypes);
		return SUCCESS;
	}
	
	/**
	 * cpk推移图表数据
	 * @return
	 * @throws Exception
	 */
	@Action("cpk-trend-datas")
	public String getCpkTrendDatas() throws Exception {
		try {
			renderText(JSONObject.fromObject(cpkTrendDatasManager.getCpkTrendDatas(params)).toString());
		} catch (Exception e) {
			log.error("cpk推移图表查询失败!",e);
			createErrorMessage("统计失败:" + e.getMessage());
		}
		return null;
	}
	
	
	/**
	 * CPK推移图页面
	 * @return
	 * @throws Exception
	 */
	@Action("sign-cpk-trend")
	public String signCpkTrend() throws Exception {
		//质量特性
		List<QualityFeature> qualityFeatures = new ArrayList<QualityFeature>();
		QualityFeature firstFeature = qualityFeatureManager.queryFirst();
		if(firstFeature != null){
			qualityFeatures.add(firstFeature);
		}
		ActionContext.getContext().put("qualityFeatures",jlanalyseDrawManager.convertListToOptions(qualityFeatures));
		List<LayerType> layerTypes=layerTypeManager.getLayerTypes();
		ActionContext.getContext().put("layerTypes", layerTypes);
		return SUCCESS;
	}
	
	/**
	 * cpk推移图表数据
	 * @return
	 * @throws Exception
	 */
	@Action("sign-cpk-trend-datas")
	public String getSignCpkTrendDatas() throws Exception {
		try {
			renderText(JSONObject.fromObject(statisticsManager.getSignCpkTrendDatas(params)).toString());
		} catch (Exception e) {
			log.error("统计cpk推移图表数据失败!",e);
			createErrorMessage("统计失败:" + e.getMessage());
		}
		return null;
	}
	
	
	/**
	 * 数据分析页面
	 * @return
	 * @throws Exception
	 */
	@Action("data-analysis")
	public String dataAnalysis() throws Exception {
		//质量特性
		List<QualityFeature> qualityFeatures = new ArrayList<QualityFeature>();
		QualityFeature firstFeature = qualityFeatureManager.queryFirst();
		if(firstFeature != null){
			qualityFeatures.add(firstFeature);
		}
		ActionContext.getContext().put("qualityFeatures",jlanalyseDrawManager.convertListToOptions(qualityFeatures));
		List<LayerType> layerTypes=layerTypeManager.getLayerTypes();
		ActionContext.getContext().put("layerTypes", layerTypes);
		return SUCCESS;
	}
	/**
	 * 数据分析页面
	 * @return
	 * @throws Exception
	 */
	@Action("data-analysis-view")
	public String dataAnalysisView() throws Exception {
		//质量特性
		List<QualityFeature> qualityFeatures = qualityFeatureManager.getList();
		ActionContext.getContext().put("qualityFeatures",jlanalyseDrawManager.convertListToOptions(qualityFeatures));
		List<LayerType> layerTypes=layerTypeManager.getLayerTypes();
		ActionContext.getContext().put("layerTypes", layerTypes);
		return SUCCESS;
	}
	
	/**
	 * 数据分析画控制图
	 * @return
	 * @throws Exception
	 */
	@Action("draw-control")
	public String drawControlPic() throws Exception {
		String featureId=Struts2Utils.getParameter("featureId");
//		featureId = "17";
		String lastAmout=Struts2Utils.getParameter("lastAmout");
		String startDateStr=Struts2Utils.getParameter("startDateStr");
		String endDateStr=Struts2Utils.getParameter("endDateStr");
		params = CommonUtil1.convertJsonObject(params);
		//封装显示规格线的参数
		Map<String,Object> showParamMap = new HashMap<String,Object>();
		String showGuiGe = Struts2Utils.getParameter("showGuiGe");
		if("1".equals(showGuiGe)){
			showParamMap.put("showGuiGe","1");
		}
		String showMaxAndMin = Struts2Utils.getParameter("showMaxAndMin");
		if("1".equals(showMaxAndMin)){
			showParamMap.put("showMaxAndMin","1");
		}
		try {
			drawControlManager.drawControlPic(featureId,startDateStr,endDateStr,"analysis",lastAmout,params,showParamMap);
		} catch (Exception e) {
			log.error("数据分析画控制图统计失败!",e);
		}
		return null;
	}
	
	/**
	  * 方法名: 获取控制点的坐标
	  * <p>功能说明：获取缓存的坐标</p>
	  * @return
	  * @throws Exception
	 */
	@Action("get-control-points")
	public String getControlPoints() throws Exception {
		JSONArray points = (JSONArray) Struts2Utils.getSession().getAttribute("featurePoints");
		if(points==null){
			renderText("{error:true,message:\"对应的坐标不存在!\"}");
		}else{
			renderText(points.toString());
		}
		Struts2Utils.getSession().removeAttribute("featurePoints");
		return null;
	}
	
	/**
	 * 根据质量特性ID获取层级
	 * @return
	 * @throws Exception
	 */
	@Action("get-layers-by-feature")
	public String getLayersByFeature() throws Exception{
		String featureId = Struts2Utils.getParameter("featureId");
		JSONArray layerCodes = new JSONArray();
		if(StringUtils.isNotEmpty(featureId)&&CommonUtil1.isInteger(featureId)){
			QualityFeature querFeature = qualityFeatureManager.getQualityFeatureFromCache(Long.valueOf(featureId),null);
			List<FeatureLayer> featureLayers = querFeature.getFeatureLayers();
			if(featureLayers != null){
				for(FeatureLayer layer : featureLayers){
					layerCodes.add(layer.getDetailCode());
				}
			}
		}
		renderText(layerCodes.toString());
		return null;
	}
	/**
	 * 数据分析画直方图
	 * @return
	 * @throws Exception
	 */
	@Action("histogram-draw")
	public String histogramDraw() throws Exception {
		String featureId=Struts2Utils.getParameter("featureId");
		String startDateStr=Struts2Utils.getParameter("startDateStr");
		String endDateStr=Struts2Utils.getParameter("endDateStr");
		String lastAmout=Struts2Utils.getParameter("lastAmout");
		params = CommonUtil1.convertJsonObject(params);
		drawHistogramDrawManager.drawHistogramDraw(featureId, startDateStr, endDateStr,lastAmout,params);
		return null;
	}
	@Autowired
	private CpMoudleanager cpMoudleanager;
	/**
	 * 数据分析统计量表格
	 * @return
	 * @throws Exception
	 */
	@Action("cp-moudle")
	public String cpMoudle() throws Exception {
		String featureId=Struts2Utils.getParameter("featureId");
		String startDateStr=Struts2Utils.getParameter("startDateStr");
		String endDateStr=Struts2Utils.getParameter("endDateStr");
		String lastAmout=Struts2Utils.getParameter("lastAmout");
		
		params = CommonUtil1.convertJsonObject(params);
		
		JLcalculator jLcalculator=new JLcalculator();
		JLOriginalData originalData=new JLOriginalData();
		//质量特性
		QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureFromCache(Long.valueOf(featureId),null);
		ActionContext.getContext().put("featurePrecs", qualityFeature.getPrecs());
		
		//封装originalDate
		cpMoudleanager.calculatJl(jLcalculator, originalData, "analysis", featureId, startDateStr, endDateStr, lastAmout, params);
		if(jLcalculator.getjLResult()!=null){
			originalData.setSCL(jLcalculator.getjLResult().getSCL());
			originalData.setSLCL(jLcalculator.getjLResult().getSLCL());
			originalData.setSUCL(jLcalculator.getjLResult().getSUCL());
			originalData.setXCL(jLcalculator.getjLResult().getXCL());
			originalData.setXLCL(jLcalculator.getjLResult().getXLCL());
			originalData.setXUCL(jLcalculator.getjLResult().getXUCL());
			ActionContext.getContext().put("jLResult", jLcalculator.getjLResult());
			ActionContext.getContext().put("cpkMoudle", jLcalculator.getjLResult().getCpkMoudle());
		}
		return SUCCESS;
	}
	
	
	/**
	 * 数据分析子组数据
	 * @return
	 * @throws Exception
	 */
	@Action("data-table")
	public String dataTable() throws Exception {
		try{
			String featureId=Struts2Utils.getParameter("featureId");
			String startDateStr=Struts2Utils.getParameter("startDateStr");
			String endDateStr=Struts2Utils.getParameter("endDateStr");
			String lastAmout=Struts2Utils.getParameter("lastAmout");
			params = CommonUtil1.convertJsonObject(params);
			
			this.renderText(JSONObject.fromObject(statisticsManager.getDateTable(featureId, startDateStr, endDateStr,lastAmout,params)).toString());
		}catch(Exception e){
			log.error("数据分析子组数据查询失败!",e);
		}
		return null;
	}
	
	/**
	 * 数据分析子组数据
	 * @return
	 * @throws Exception
	 */
	@Action("data-table-model")
	public String dataTableModel() throws Exception {
		String featureId=Struts2Utils.getParameter("featureId");
		int effectiveCapacity = 0;
		List<FeatureLayer>  featureLayers=null;
		if(StringUtils.isNotEmpty(featureId)){
			QualityFeature qualityFeature = qualityFeatureManager.getQualityFeature(Long.parseLong(featureId));
			effectiveCapacity = qualityFeature.getEffectiveCapacity();
			featureLayers = qualityFeature.getFeatureLayers();
		}
		
		JSONArray colModels = new JSONArray();//表格的表头
		JSONObject modelJson = new JSONObject();
		modelJson.put("name","no");
		modelJson.put("label","子组号");
		modelJson.put("index","no");
		modelJson.put("width","80");
		colModels.add(modelJson);
		for(int i=0;i<effectiveCapacity;i++){
			JSONObject modelJsonx = new JSONObject();
			modelJsonx.put("name","x"+i);
			modelJsonx.put("label","x"+(i+1));
			modelJsonx.put("index","x"+i);
			modelJsonx.put("width","60");
			colModels.add(modelJsonx);
		}
		JSONObject modelJson1 = new JSONObject();
		modelJson1.put("name","average");
		modelJson1.put("label","均值");
		modelJson1.put("index","average");
		modelJson1.put("width","80");
		colModels.add(modelJson1);
		JSONObject modelJson2 = new JSONObject();
		modelJson2.put("name","max");
		modelJson2.put("label","最大值");
		modelJson2.put("index","max");
		modelJson2.put("width","80");
		colModels.add(modelJson2);
		JSONObject modelJson3 = new JSONObject();
		modelJson3.put("name","min");
		modelJson3.put("label","最小值");
		modelJson3.put("index","min");
		modelJson3.put("width","80");
		colModels.add(modelJson3);
		JSONObject modelJson4 = new JSONObject();
		modelJson4.put("name","range");
		modelJson4.put("label","极差");
		modelJson4.put("index","range");
		modelJson4.put("width","80");
		colModels.add(modelJson4);
		JSONObject modelJson5 = new JSONObject();
		modelJson5.put("name","time");
		modelJson5.put("label","采集时间");
		modelJson5.put("index","time");
		modelJson5.put("width","135");
		colModels.add(modelJson5);
		if(featureLayers != null){
			for(int i=0;i<featureLayers.size();i++){
				JSONObject modelJsonx = new JSONObject();
				FeatureLayer f=featureLayers.get(i);
				modelJsonx.put("name",f.getDetailCode());
				modelJsonx.put("label",f.getDetailName());
				modelJsonx.put("index",f.getDetailCode());
				colModels.add(modelJsonx);
			}
		}
		this.renderText(colModels.toString());
		return null;
	}
	
	/**
	 * 数据分析查询条件
	 * @return
	 * @throws Exception
	 */
	@Action("query-condition")
	public String queryCondition() throws Exception {
		String featureId=Struts2Utils.getParameter("featureId");
		QualityFeature qualityFeature=new QualityFeature();
		if(featureId!=null){
			qualityFeature=qualityFeatureManager.getQualityFeature(Long.parseLong(featureId));
		}
		List<FeatureLayer> featureLayers=qualityFeature.getFeatureLayers();
		List<LayerType> layerTypes=layerTypeManager.getLayerTypes();
		List<LayerType> newlayerTypes=new ArrayList<LayerType>();
		if(featureLayers!=null){
			for(int i=0;i<featureLayers.size();i++){
				FeatureLayer f=featureLayers.get(i);
				for(int j=0;j<layerTypes.size();j++){
					LayerType l=layerTypes.get(j);
					if(String.valueOf(l.getId()).equals(String.valueOf(f.getTargetId()))){
						newlayerTypes.add(l);
						break;
					}
				}
			}
		}
		ActionContext.getContext().put("layerTypes", newlayerTypes);
		return SUCCESS;
	}
	
	
	/**
	 * 相关分析页面
	 * @return
	 * @throws Exception
	 */
	@Action("data-about-analysis")
	public String getDatasAboutAnalysis() throws Exception {
		//质量特性
		List<QualityFeature> qualityFeatures = new ArrayList<QualityFeature>();
		QualityFeature firstFeature = qualityFeatureManager.queryFirst();
		if(firstFeature != null){
			qualityFeatures.add(firstFeature);
		}
		ActionContext.getContext().put("qualityFeatures",jlanalyseDrawManager.convertListToParentOptions(qualityFeatures));
		return SUCCESS;
	}
	
	/**
	 * 数据分析画直方图
	 * @return
	 * @throws Exception
	 */
	@Action("distribute-draw")
	public String spreadDraw() throws Exception {
		String xqualityFeature=Struts2Utils.getParameter("xqualityFeature");
		String yqualityFeature=Struts2Utils.getParameter("yqualityFeature");
		String startDateStr=Struts2Utils.getParameter("startDateStr");
		String endDateStr=Struts2Utils.getParameter("endDateStr");
		String group=Struts2Utils.getParameter("group");
		String type=Struts2Utils.getParameter("type");
		String beginNo=Struts2Utils.getParameter("beginNo");
		String endNo=Struts2Utils.getParameter("endNo");
		jlanalyseDrawManager.drawDistributeChart(xqualityFeature, yqualityFeature, startDateStr, endDateStr, group,type,beginNo,endNo);
		return null;
	}
	/**
	 * 查看判异准则
	 * @return
	 * @throws Exception
	 */
	@Action("bs-rules-list")
	public String bsRulesList() throws Exception {
		return SUCCESS;
	}
	
	@Action("bs-rules-list-datas")
	public String getBsRulesListDatas() throws Exception {
		page = bsRulesManager.list(page);
		renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "SPC：判断规则");
		return null;
	}
	
	/**
	 * 数据分析导出
	 * @return
	 * @throws Exception
	 */
	@Action("data-analysis-export")
	public String dataAnalysisExport() throws Exception {
		try {
			String featureId=Struts2Utils.getParameter("featureId");
			String startDateStr=Struts2Utils.getParameter("startDateStr");
			String endDateStr=Struts2Utils.getParameter("endDateStr");
			String lastAmout=Struts2Utils.getParameter("lastAmout");
			
			params = CommonUtil1.convertJsonObject(params);
			
			//数据分析-统计量
			JLcalculator jLcalculator=new JLcalculator();
			JLOriginalData originalData=new JLOriginalData();
			cpMoudleanager.calculatJl(jLcalculator, originalData, "analysis", featureId, startDateStr, endDateStr, lastAmout, params);
			if(jLcalculator.getjLResult()!=null){
				originalData.setSCL(jLcalculator.getjLResult().getSCL());
				originalData.setSLCL(jLcalculator.getjLResult().getSLCL());
				originalData.setSUCL(jLcalculator.getjLResult().getSUCL());
				originalData.setXCL(jLcalculator.getjLResult().getXCL());
				originalData.setXLCL(jLcalculator.getjLResult().getXLCL());
				originalData.setXUCL(jLcalculator.getjLResult().getXUCL());
				ActionContext.getContext().put("jLResult", jLcalculator.getjLResult());
				ActionContext.getContext().put("cpkMoudle", jLcalculator.getjLResult().getCpkMoudle());
			}
			HashMap<String,Object> sheet1Map = new LinkedHashMap<String, Object>();
			sheet1Map.put("Mean", jLcalculator.getjLResult()==null?0:jLcalculator.getjLResult().getAverage());
			sheet1Map.put("Max", jLcalculator.getjLResult()==null?0:jLcalculator.getjLResult().getMax());
			sheet1Map.put("Min", jLcalculator.getjLResult()==null?0:jLcalculator.getjLResult().getMin());
			sheet1Map.put("Range", jLcalculator.getjLResult()==null?0:jLcalculator.getjLResult().getR());
			sheet1Map.put("StdDev", jLcalculator.getjLResult()==null?0:jLcalculator.getjLResult().getS());
			sheet1Map.put("Skewness", jLcalculator.getjLResult()==null?0:jLcalculator.getjLResult().getSkewness());
			sheet1Map.put("Kurtosis", jLcalculator.getjLResult()==null?0:jLcalculator.getjLResult().getKurtosis());
			if(jLcalculator.getjLResult()!=null){
				sheet1Map.put("Cp", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getCp());
				sheet1Map.put("Cr", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getCr());
				sheet1Map.put("K", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getK());
				sheet1Map.put("Cpu", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getCpu());
				sheet1Map.put("Cpl", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getCpl());
				sheet1Map.put("Cpk", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getCpk());
				sheet1Map.put("Cpm", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getCpm());
				sheet1Map.put("Zu_Cap", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getZu_cap());
				sheet1Map.put("Zl_Cap", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getZl_cap());
				sheet1Map.put("Fpu_Cap", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getFpu_cap());
				sheet1Map.put("Fpl_Cap", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getFpl_cap());
				sheet1Map.put("Fp_Cap", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getFp_cap());
				sheet1Map.put("Pp", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getPp());
				sheet1Map.put("Pr", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getPr());
				sheet1Map.put("Ppu", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getPpu());
				sheet1Map.put("Ppl", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getPpl());
				sheet1Map.put("Ppk", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getPpk());
				sheet1Map.put("Ppm", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getPpm());
				sheet1Map.put("Zu_Perf", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getZu_pref());
				sheet1Map.put("Zl_Perf", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getZl_pref());
				sheet1Map.put("Fpu_Perf", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getFpu_pref());
				sheet1Map.put("Fpl_Perf", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getFpl_pref());
				sheet1Map.put("Fp_Perf", jLcalculator.getjLResult().getCpkMoudle()==null?0:jLcalculator.getjLResult().getCpkMoudle().getFp_pref());
			}

			//数据分析-原始数据
			Map<String,Object> dataTableMap = statisticsManager.getDateTable(featureId,startDateStr,endDateStr,lastAmout,params);
			HashMap<String,String> sheet2Map = new LinkedHashMap<String, String>();
			Object colModel = dataTableMap.get("colModel");
			JSONArray colModels = JSONArray.fromObject(colModel);
			for (Object object : colModels) {
				//JSONObject jsonObject = JSONObject.fromObject(object);
				JSONObject jsonObject = (JSONObject)object;
				sheet2Map.put(jsonObject.getString("name"), jsonObject.getString("label"));
			}
			
			//导出为EXCEL
	        Workbook workbook = new HSSFWorkbook();
	        
	        Sheet sheet1 = workbook.createSheet("数据分析-统计量");
	        sheet1.addMergedRegion(CellRangeAddress.valueOf("$A$1:$B$1"));
	        sheet1.addMergedRegion(CellRangeAddress.valueOf("$A$2:$B$2"));
	        //创建模板样式
	        CellStyle cellStyle1 = workbook.createCellStyle();
	        cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        //创建导出模板
	        Row rowTitle1 = sheet1.createRow(0);
	        Cell cellTitle1 = rowTitle1.createCell(0);
	        cellTitle1.setCellValue("数据分析-统计量");
	        cellTitle1.setCellStyle(cellStyle1);

	        Row rowSubtitle1 = sheet1.createRow(1);
	        Cell cellSubtitle1 = rowSubtitle1.createCell(0);
	        cellSubtitle1.setCellValue("统计量 ");
	        cellSubtitle1.setCellStyle(cellStyle1);
	        
	        Font font1 = workbook.createFont();
	        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        font1.setColor(HSSFFont.COLOR_NORMAL);
	        cellStyle1.setFont(font1);
	        int rowIndex1 = 2;
	        for (String key : sheet1Map.keySet()) {
	        	Row rowN = sheet1.createRow(rowIndex1);
	        	Cell cell0 = rowN.createCell(0);
	        	cell0.setCellValue(key);
	        	sheet1.setColumnWidth(0, key.length()*1024);
		        Cell cell1 = rowN.createCell(1);
		        cell1.setCellValue(sheet1Map.get(key).toString());
		        sheet1.setColumnWidth(1, sheet1Map.get(key).toString().length()*512);
		        rowIndex1++;
			}
	        
	        Sheet sheet2 = workbook.createSheet("数据分析-原始数据");
	        sheet2.addMergedRegion(CellRangeAddress.valueOf("$A$1:$H$1"));
	        sheet2.addMergedRegion(CellRangeAddress.valueOf("$A$2:$H$2"));
	        //创建模板样式
	        CellStyle cellStyle2 = workbook.createCellStyle();
	        cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        //创建导出模板
	        Row rowTitle2 = sheet2.createRow(0);
	        Cell cellTitle2 = rowTitle2.createCell(0);
	        cellTitle2.setCellValue("数据分析-原始数据");
	        cellTitle2.setCellStyle(cellStyle2);
	        
	        Row rowSubtitle2 = sheet2.createRow(1);
	        Cell cellSubtitle2 = rowSubtitle2.createCell(0);
	        cellSubtitle2.setCellValue("原始数据 ");
	        cellSubtitle2.setCellStyle(cellStyle2);
	        
	        Font font2 = workbook.createFont();
	        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        font2.setColor(HSSFFont.COLOR_NORMAL);
	        cellStyle2.setFont(font2);
	        Row row2 = sheet2.createRow(2);
	        int columnIndex2 = 0;
	        for (String key : sheet2Map.keySet()) {
	        	Cell cell = row2.createCell(columnIndex2);
		        cell.setCellValue(sheet2Map.get(key));
		        cell.setCellStyle(cellStyle2);
		        sheet2.setColumnWidth(columnIndex2, sheet2Map.get(key).length()*1024);
		        columnIndex2++;
			}
			//填充模板数据
			Object tableData = dataTableMap.get("tabledata");
			JSONArray tableDatas = JSONArray.fromObject(tableData);
			int rowIndex2 = 3;
			for (Object object : tableDatas) {
				JSONObject jsonObject = (JSONObject)object;
				Row rowN = sheet2.createRow(rowIndex2);
				int cellIndex = 0;
				for (Object key : sheet2Map.keySet()) {
					Object value = jsonObject.get(key);
					if(value == null){
						value = "";
					}
					rowN.createCell(cellIndex).setCellValue(value.toString());
					cellIndex++;
				}
				rowIndex2++;
			}
			
			String fileName = "数据分析.xls";
			byte byname[] = fileName.getBytes("gbk");
			fileName = new String(byname, "8859_1");
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder("attachment;filename=\"")).append(fileName).append("\"").toString());
			workbook.write(response.getOutputStream());
		}catch (Exception e) {
			e.printStackTrace();
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder("attachment;filename=\"")).append("error.xls").append("\"").toString());
			response.getOutputStream().write(("服务器错误:" + e.getMessage()).getBytes("8859_1"));
		} catch (Throwable t) {
			log.error("统计失败：", t);
		}
		return null;
	}
}

package com.ambition.supplier.manager.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.entity.EstimateModel;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.entity.SupplierGoal;
import com.ambition.supplier.entity.SupplierImprove;
import com.ambition.supplier.entity.SupplierQcds;
import com.ambition.supplier.entity.SupplyProduct;
import com.ambition.supplier.entity.WarnSign;
import com.ambition.supplier.estimate.service.EstimateModelManager;
import com.ambition.supplier.manager.service.ManagerManager;
import com.ambition.supplier.manager.service.SupplierQcdsManager;
import com.ambition.supplier.manager.service.WarnSignManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.mms.base.DynamicColumnValues;
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
 * ManagerAction.java
 * @authorBy YUKE
 *
 */
@Namespace("/supplier/manager")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/manager", type = "redirectAction") })
public class ManagerAction extends CrudActionSupport<SupplierImprove> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private Page<SupplierImprove> page;
	private Page<SupplierQcds> qcdsPage;
	private SupplierImprove supplierImprove;
	private Page<SupplierGoal> signPage;
	private Supplier supplier;
	private Integer evaluateYear;
	private Integer evaluateMonth;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	@Autowired
	private ManagerManager managerManager;
	
	@Autowired
	private SupplierQcdsManager qcdsManager;
	
	@Autowired
 	private EstimateModelManager estimateModelManager;
	
	private Page<SupplyProduct> supplierProductPage;
	
	private JSONObject params;
	
	@Autowired
	private WarnSignManager warnSignManager;

	private List<DynamicColumnDefinition> dynamicColumn=new ArrayList<DynamicColumnDefinition>();
	
	public List<DynamicColumnDefinition> getDynamicColumn() {
		return dynamicColumn;
	}
	public void setDynamicColumn(List<DynamicColumnDefinition> dynamicColumn) {
		this.dynamicColumn = dynamicColumn;
	}
	public Integer getEvaluateYear() {
		return evaluateYear;
	}
	public void setEvaluateYear(Integer evaluateYear) {
		this.evaluateYear = evaluateYear;
	}
	public Integer getEvaluateMonth() {
		return evaluateMonth;
	}
	public void setEvaluateMonth(Integer evaluateMonth) {
		this.evaluateMonth = evaluateMonth;
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
	public Page<SupplierImprove> getPage() {
		return page;
	}
	public void setPage(Page<SupplierImprove> page) {
		this.page = page;
	}	
	public SupplierImprove getSupplierImprove() {
		return supplierImprove;
	}
	public void setSupplierImprove(SupplierImprove supplierImprove) {
		this.supplierImprove = supplierImprove;
	}
	public Page<SupplierGoal> getSignPage() {
		return signPage;
	}
	public void setSignPage(Page<SupplierGoal> signPage) {
		this.signPage = signPage;
	}
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public Page<SupplyProduct> getSupplierProductPage() {
		return supplierProductPage;
	}
	public void setSupplierProductPage(Page<SupplyProduct> supplierProductPage) {
		this.supplierProductPage = supplierProductPage;
	}
	public JSONObject getParams() {
		return params;
	}
	public void setParams(JSONObject params) {
		this.params = params;
	}
	public Page<SupplierQcds> getQcdsPage() {
		return qcdsPage;
	}
	public void setQcdsPage(Page<SupplierQcds> qcdsPage) {
		this.qcdsPage = qcdsPage;
	}
	@Override
	public SupplierImprove getModel() {
		return supplierImprove;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			supplierImprove = new SupplierImprove();
			supplierImprove.setCreatedTime(new Date());
			supplierImprove.setCompanyId(ContextUtils.getCompanyId());
			supplierImprove.setCreator(ContextUtils.getUserName());
			supplierImprove.setLastModifiedTime(new Date());
			supplierImprove.setLastModifier(ContextUtils.getUserName());
			supplierImprove.setBusinessUnitName(ContextUtils.getSubCompanyName());
			supplierImprove.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			supplierImprove = managerManager.getSupplierImprove(id);
		}
	}
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("save")
	@Override
	public String save() throws Exception {
		if(id == null){
			try{
				managerManager.saveSupplierImprove(supplierImprove);
				this.renderText(JsonParser.getRowValue(supplierImprove));
			}catch(Exception e){
				e.printStackTrace();
				createErrorMessage("保存失败：" + e.getMessage());
			}
		}else{
			if(supplierImprove != null){
				supplierImprove.setLastModifiedTime(new Date());
				supplierImprove.setLastModifier(ContextUtils.getUserName());
				try{
					managerManager.saveSupplierImprove(supplierImprove);
					this.renderText(JsonParser.getRowValue(supplierImprove));
				}catch(Exception e){
					e.printStackTrace();
					createErrorMessage("保存失败：" + e.getMessage());
				}
			}else{
				createErrorMessage("保存供应商改进失败,供应商改进记录为空!");
			}
		}
		return null;
	}
	@Action("delete")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			renderText("删除的对象不存在!");
		}else{
			try {
				managerManager.deleteSupplierImprove(deleteIds);
			} catch (Exception e) {
				renderText("删除失败:" + e.getMessage());
			}
		}
		return null;
	}
	@Action("list")
	@Override
	public String list() throws Exception {
		return null;
	}
	/**
	 * 供应商开发状态
	 */
	@Action("state-list-datas")
	public String getStateListDatas() throws Exception {
		try{
			supplierProductPage = managerManager.searchBySupplyProductPage(supplierProductPage);
			renderText(PageUtils.pageToJson(supplierProductPage));
		}catch (Exception e) {
			e.printStackTrace();
		}
		logUtilDao.debugLog("查询", "供应商质量管理：供应商管理-供应商开发状态查询");
		return null;
	}
	/**
	 * 导出
	 * @return
	 * @throws Exception 
	 */
	@Action("exports")
	public String exports() throws Exception {
		supplierProductPage = managerManager.searchBySupplyProductPage(new Page<SupplyProduct>(Integer.MAX_VALUE));
		ExcelExporter.export(ApiFactory.getMmsService().getExportData(supplierProductPage, "SUPPLIER_SUPPLY_PRODUCT"),"供应商产品台帐");
		logUtilDao.debugLog("导出", "供应商质量管理：供应商管理-供应商产品台帐");
		return null;
	}
	/**
	 * 供应商红黄牌数据
	 */
	@Action("warning-sign")
	public String listSign() throws Exception {
		//评价类型
//		List<EstimateModel> estimateModels = estimateModelManager.getTopEstimateModels(EvaluatingIndicator.TYPE_QUARTER);
//		StringBuffer sb = new StringBuffer("");
//		for(EstimateModel estimateModel : estimateModels){
//			if(sb.length() > 0){
//				sb.append(";");
//			}
//			sb.append(estimateModel.getName() + ":" + estimateModel.getName());
//		}
//		ActionContext.getContext().put("estimateModelOptions",sb.toString());
		return SUCCESS;
	}
	
	/**
	 * 供应商红黄牌数据
	 */
	@Action("warning-sign-view")
	public String listSignView() throws Exception {
		params=convertJsonObject(params);
		//评价类型
		List<EstimateModel> estimateModels = estimateModelManager.getTopEstimateModels();
		StringBuffer sb = new StringBuffer("");
		for(EstimateModel estimateModel : estimateModels){
			if(sb.length() > 0){
				sb.append(";");
			}
			sb.append(estimateModel.getName() + ":" + estimateModel.getName());
		}
		ActionContext.getContext().put("estimateModelOptions",sb.toString());
		return SUCCESS;
	}
	/**
	  * 方法名:红黄牌数据 
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("red-yellow-datas")
	@LogInfo(optType="查询",message="供应商质量管理：供应商管理-供应商红黄牌")
	public String getRedYellowDatas() throws Exception {
		try{
			String evaluateYear=Struts2Utils.getParameter("evaluateYear");
			String importance=Struts2Utils.getParameter("importance");
			if(evaluateYear != null && importance != null){
				signPage = managerManager.listpage(signPage,Integer.parseInt(evaluateYear),importance);
				renderText(PageUtils.pageToJson(signPage));
			}else{
				signPage = managerManager.searchBySignPage(signPage);
				renderText(PageUtils.pageToJson(signPage));
			}
		}catch (Exception e) {
			log.error("查询红黄牌数据失败!",e);
		}
		return null;
	}
	/**
	  * 方法名:供应商QCDS评价总表 
	  * <p>功能说明：供应商评价总表</p>
	  * @return
	  * @throws Exception
	 */
	@Action("supplier-qcds")
	@LogInfo(optType="页面",message="QCDS评价总表")
	public String supplierQcds() throws Exception {
		List<Option> options = new ArrayList<Option>();
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		for(int i=2013;i<=currentYear;i++){
			Option option = new Option();
			option.setName(i + "年");
			option.setValue(i + "");
			options.add(option);
		}
		ActionContext.getContext().put("evaluateYears",options);
		if(evaluateYear == null){
			evaluateYear = currentYear;
		}
		options = new ArrayList<Option>();
		int currentMonth = calendar.get(Calendar.MONTH)+1;
//		int currentQuarter = -1;
		for(int i=1;i<=12;i++){
			Option option = new Option();
			option.setName(i + "月");
			option.setValue(i+"");
			options.add(option);
//			if(i==currentMonth){
//				currentQuarter = i;
//			}
		}
		ActionContext.getContext().put("evaluateMonths",options);
		if(evaluateMonth==null){
			evaluateMonth = currentMonth;
		}
		List<EstimateModel> models = estimateModelManager.getEstimateModelOfChildren();
		Map<String,Boolean> nameMap = new HashMap<String, Boolean>();
		StringBuilder groupHeaders = new StringBuilder("[");
		for(EstimateModel model : models){
			if(nameMap.containsKey(model.getName())){
				continue;
			}
			nameMap.put(model.getName(),true);
			
			DynamicColumnDefinition dynamicColumnDefinition = new DynamicColumnDefinition();
//			dynamicColumnDefinition.setColName("总分");
//			dynamicColumnDefinition.setName("total_"+model.getName());
//			dynamicColumnDefinition.setEditable(false);
//			dynamicColumnDefinition.setVisible(true);
//			dynamicColumnDefinition.setColWidth("80");
//			dynamicColumn.add(dynamicColumnDefinition);
			
//			dynamicColumnDefinition = new DynamicColumnDefinition();
			String name = model.getName() + "("+model.getTotalPoints()+")";
			if("质量".equals(model.getName())){
				name = "本月得分";
				dynamicColumnDefinition.setColWidth("100");
			}else{
				dynamicColumnDefinition.setColWidth(name.length()*12+20+"");
			}
			dynamicColumnDefinition.setColName(name);
			dynamicColumnDefinition.setName("realTotal_"+model.getName());
			dynamicColumnDefinition.setEditable(false);
			dynamicColumnDefinition.setVisible(true);
			dynamicColumnDefinition.setExportable(true);
			dynamicColumn.add(dynamicColumnDefinition);
			
			if("质量".equals(model.getName())){
				dynamicColumnDefinition = new DynamicColumnDefinition();
				dynamicColumnDefinition.setColName("本月等级");
				dynamicColumnDefinition.setName("grade_" + evaluateMonth);
				dynamicColumnDefinition.setEditable(false);
				dynamicColumnDefinition.setVisible(true);
				dynamicColumnDefinition.setExportable(true);
				dynamicColumnDefinition.setColWidth("70");
				dynamicColumn.add(dynamicColumnDefinition);
				for(int i=1;i<3;i++){
					int month = evaluateMonth - i;
					if(month < 1){
						month = 12 + month;
					}
					dynamicColumnDefinition = new DynamicColumnDefinition();
					dynamicColumnDefinition.setColName(month + "月等级");
					dynamicColumnDefinition.setName("grade_" + month);
					dynamicColumnDefinition.setEditable(false);
					dynamicColumnDefinition.setVisible(true);
					dynamicColumnDefinition.setExportable(true);
					dynamicColumnDefinition.setColWidth("80");
					dynamicColumn.add(dynamicColumnDefinition);
				}
//				
//				dynamicColumnDefinition = new DynamicColumnDefinition();
//				dynamicColumnDefinition.setColName("季度得分等级");
//				dynamicColumnDefinition.setName("grade_quarter");
//				dynamicColumnDefinition.setEditable(false);
//				dynamicColumnDefinition.setVisible(true);
//				dynamicColumnDefinition.setExportable(true);
//				dynamicColumnDefinition.setColWidth("120");
//				dynamicColumn.add(dynamicColumnDefinition);
				groupHeaders.append("{startColumnName:'realTotal_"+model.getName()+"', numberOfColumns:4, titleText:'"+model.getName() + "("+model.getTotalPoints()+")'},");
			}
			dynamicColumnDefinition = new DynamicColumnDefinition();
			dynamicColumnDefinition.setColName("评价ID");
			dynamicColumnDefinition.setName("realTotal_"+model.getName() + "_id");
			dynamicColumnDefinition.setEditable(false);
			dynamicColumnDefinition.setVisible(false);
			dynamicColumnDefinition.setColWidth("80");
			dynamicColumn.add(dynamicColumnDefinition);
		}
		DynamicColumnDefinition dynamicColumnDefinition = new DynamicColumnDefinition();
		dynamicColumnDefinition.setColName("总分合计");
		dynamicColumnDefinition.setName("total");
		dynamicColumnDefinition.setEditable(false);
		dynamicColumnDefinition.setVisible(true);
		dynamicColumnDefinition.setExportable(true);
		dynamicColumnDefinition.setColWidth("80");
		
		dynamicColumn.add(dynamicColumnDefinition);
		dynamicColumnDefinition = new DynamicColumnDefinition();
		dynamicColumnDefinition.setColName("得分(百分制)");
		dynamicColumnDefinition.setName("percentageTotal");
		dynamicColumnDefinition.setEditable(false);
		dynamicColumnDefinition.setVisible(true);
		dynamicColumnDefinition.setExportable(true);
		dynamicColumnDefinition.setColWidth("80");
		dynamicColumn.add(dynamicColumnDefinition);
		
		dynamicColumnDefinition = new DynamicColumnDefinition();
		dynamicColumnDefinition.setColName("等级");
		dynamicColumnDefinition.setName("grade");
		dynamicColumnDefinition.setEditable(false);
		dynamicColumnDefinition.setVisible(true);
		dynamicColumnDefinition.setColWidth("80");
		dynamicColumnDefinition.setExportable(true);
		dynamicColumn.add(dynamicColumnDefinition);
		
//		dynamicColumnDefinition = new DynamicColumnDefinition();
//		dynamicColumnDefinition.setColName("年度批次数");
//		dynamicColumnDefinition.setName("barchNo");
//		dynamicColumnDefinition.setEditable(false);
//		dynamicColumnDefinition.setVisible(true);
//		dynamicColumnDefinition.setColWidth("80");
//		dynamicColumnDefinition.setExportable(true);
//		dynamicColumn.add(dynamicColumnDefinition);
		
		groupHeaders.append("{startColumnName:'total', numberOfColumns:3, titleText:'综合评价(得分/等级)'}]");
		ActionContext.getContext().put("groupHeaders",groupHeaders.toString());
		return SUCCESS;
	}
	
	/**
	  * 方法名:供应商QCDS评价总表 
	  * <p>功能说明：供应商评价总表</p>
	  * @return
	  * @throws Exception
	 */
	@Action("supplier-qcds-datas")
	@LogInfo(optType="页面",message="QCDS评价总表")
	public String supplierQcdsDatas() throws Exception {
		qcdsPage = qcdsManager.search(qcdsPage,evaluateYear, evaluateMonth);
		final List<WarnSign> warnSigns = new ArrayList<WarnSign>();
		final List<String> gradeStrs = new ArrayList<String>();
		for(int i=0;i<3;i++){
			int month = evaluateMonth - i;
			if(month < 1){
				month = 12 + month;
			}
			gradeStrs.add("grade_" + month);
		}
//		final DecimalFormat df = new DecimalFormat("#.##");
		this.renderText(PageUtils.dynamicPageToJson(qcdsPage,new DynamicColumnValues(){
			public void addValuesTo(List<Map<String, Object>> result) {
				for(Map<String, Object> map:result){
					Long id=Long.valueOf(map.get("id").toString());
					SupplierQcds qcds = qcdsManager.getSupplierQcds(id);
					for(int i=1;i<=6;i++){
						try {
							Object evaluateId = PropertyUtils.getProperty(qcds,"evaluate"+i+"Id");
							if(evaluateId != null){
								Object evaluateName = PropertyUtils.getProperty(qcds,"evaluate"+i+"Name");
								Object evaluateTotal = PropertyUtils.getProperty(qcds,"evaluate"+i+"Total");
								map.put("total_" + evaluateName,evaluateTotal);
								Object realTotal = PropertyUtils.getProperty(qcds,"evaluate"+i+"RealTotal");
								Object weight = PropertyUtils.getProperty(qcds,"evaluate"+i+"Weight");
//								if(realTotal!=null&&realTotal instanceof Double){
//									realTotal = df.format(realTotal);
//								}
								//计算当前供应商的批次数
								map.put("realTotal_" + evaluateName,"<a href='#' title='单击查看详情' onclick='gotoEvaluate("+evaluateId+")'>"+realTotal + "(" + weight + "%)</a>");
								map.put("realTotal_" + evaluateName+"_id",evaluateId);
							}
						} catch (Exception e) {
							throw new AmbFrameException("取值失败!",e);
						}
					}
					if(warnSigns.isEmpty()){
						warnSigns.addAll(warnSignManager.list());
					}
//					map.put("barchNo",qcdsManager.getIqcIiar(qcds,evaluateYear).size());
					Map<String,Object> lastThreeMonthGradeMap = qcdsManager.caculateLastThreeMonths(qcds, warnSigns);
					for(String grade : gradeStrs){
						String degree = (String)lastThreeMonthGradeMap.get(grade);
						map.put(grade,degree==null?null:degree.replaceAll("级", ""));
					}
				}
			}
		}));
		return null;
	}
	
	/**
	  * 方法名: 导出qcds
	  * <p>功能说明：</p>
	  * @return
	  * @throws Exception
	 */
	@Action("export-qcds")
	@LogInfo(optType="导出",message="导出qcds数据")
	public String exportsQcds() throws Exception {
		qcdsPage = qcdsManager.search(new Page<SupplierQcds>(Integer.MAX_VALUE),evaluateYear,evaluateMonth);
		qcdsManager.exportQcdsExcel(qcdsPage.getResult(),evaluateYear,evaluateMonth);
		return null;
	}
	/**
	  * 方法名: 更新全部的供应商qcds
	  * @return
	  * @throws Exception
	 */
	@Action("update-supplier-qcds")
	public String updateSupplierQcds() throws Exception {
		JSONObject json = new JSONObject();
		try {
			qcdsManager.updateSupplierQcds();
		} catch (Exception e) {
			json.put("error",true);
			json.put("message","更新失败!" + e.getMessage());
		}
		renderText(json.toString());
		return null;
	}
	
	/**
	 * 导出
	 * @return
	 * @throws Exception 
	 */
	@Action("sign-exports")
	public String exportsSign() throws Exception {
		signPage = managerManager.searchBySignPage(new Page<SupplierGoal>(Integer.MAX_VALUE));
		ExcelExporter.export(ApiFactory.getMmsService().getExportData(signPage, "SUPPLIER_SUPPLIER_GOAL"),"供应商红黄牌台帐");
		logUtilDao.debugLog("导出", "供应商质量管理：供应商管理-供应商红黄牌");
		return null;
	}
	/**
	 * 供应商改进数据
	 */
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try{
			page = managerManager.searchByPage(page);
			renderText(PageUtils.pageToJson(page));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 供应商改进数据
	 */
	@Action("supplier-improve-datas")
	public String getImproveDatas() throws Exception {
		try{
//			supplierImprovePage = managerManager.searchByImprovePage(supplierImprovePage);
//			renderText(PageUtils.pageToJson(supplierImprovePage));
//			String result = managerManager.getResultJson(supplierImprovePage);
//			renderText(result);
		}catch (Exception e) {
			e.printStackTrace();
		}
		logUtilDao.debugLog("查询", "供应商质量管理：供应商管理-供应商改进管理");
		return null;
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
	 * 转换json
	 * @param params
	 * @return
	 */
	private JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		if(params == null){
			return resultJson;
		}
		for(Object key : params.keySet()){
			resultJson.put(key,params.getJSONArray(key.toString()).get(0));
		}
		return resultJson;
	}	
}

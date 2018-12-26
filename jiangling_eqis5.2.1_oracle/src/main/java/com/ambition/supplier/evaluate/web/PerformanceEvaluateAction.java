
package com.ambition.supplier.evaluate.web;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.views.jsp.ActionTag;
import org.omg.CORBA.Current;
import org.springframework.beans.factory.annotation.Autowired;

import bsh.StringUtil;

import com.ambition.supplier.archives.service.SupplierManager;
import com.ambition.supplier.entity.EstimateModel;
import com.ambition.supplier.entity.PerformanceEvaluate;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.estimate.service.EstimateModelManager;
import com.ambition.supplier.evaluate.service.EvaluateManager;
import com.ambition.supplier.evaluate.service.PerformanceEvaluateManager;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.tools.StringUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.sun.jndi.cosnaming.CNCtx;
import com.sun.mail.handlers.message_rfc822;
/**
 * 类名: PerformanceEvaluateAction 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：供应商评价排名</p>
 * @author  刘承斌
 * @version 1.00  2015-3-23 下午5:51:30  发布
 */
@Namespace("/supplier/evaluate/point-rank")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/evaluate/point-rank", type = "redirectAction") })
@SuppressWarnings("unused")
public class PerformanceEvaluateAction  extends CrudActionSupport<PerformanceEvaluate> {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	private String fieldPermission;
	private Long id;//实体id
	private String ids;
	private PerformanceEvaluate performanceEvaluate;
	private Page<PerformanceEvaluate> page;
	private static final String SUCCESS_MESSAGE_LEFT = "<font class=\"onSuccess\"><nobr>";
	private static final String MESSAGE_RIGHT = "</nobr></font>";
	private static final String ERROR_MESSAGE_LEFT = "<font class=\"onError\"><nobr>";
	private Long supplierId;//供应商编号
	private Supplier supplier;//对应的供应商
	private Integer evaluateYear;//评价年份
	private Long estimateModelId;//评价模型的编号
	private EstimateModel estimateModel;
	
	@Autowired
	private PerformanceEvaluateManager performanceEvaluateManager;
	@Autowired
	private SupplierManager supplierManager;
	@Autowired
	private EvaluateManager evaluateManager;
	@Autowired
	private EstimateModelManager estimateModelManager;
	
	protected void addErrorMessage(String message){
		this.addActionMessage(ERROR_MESSAGE_LEFT+message+MESSAGE_RIGHT);
	}
	protected void addSuccessMessage(String message){
		this.addActionMessage(SUCCESS_MESSAGE_LEFT+message+MESSAGE_RIGHT);
	}
	
	/**
	 * 删除
	 */
	@Override
	@Action("performanceEvaluate-delete")
	public String delete() throws Exception {
		addActionMessage(SUCCESS_MESSAGE_LEFT+performanceEvaluateManager.deletePerformanceEvaluate(ids)+MESSAGE_RIGHT);
		return "performanceEvaluate-list";
	}

	/**
	 * 新建页面
	 */
	@Override
	@Action("performanceEvaluate-input")
	public String input() throws Exception {
		return SUCCESS;
	}

	/**
	 * 列表页面
	 */
	@Override
	@Action("performanceEvaluate-list")
	public String list() throws Exception {
	/*	ActionContext.getContext().put("error","");
		ActionContext.getContext().put("estimateModelMaps","[]");
		ActionContext.getContext().put("datas","[]");
		supplier = supplierManager.getSupplier(supplierId);
		if(supplier != null){
			ActionContext.getContext().put("supplierName",supplier.getName());
			estimateModel = estimateModelManager.getEstimateModelById(supplier.getEstimateModelId());
			if(estimateModel != null){
				List<Option> options = new ArrayList<Option>();
				Calendar calendar = Calendar.getInstance();
				for(int i=calendar.get(Calendar.YEAR);i>calendar.get(Calendar.YEAR)-2;i--){
					Option option = new Option();
					option.setName(i + "年");
					option.setValue(i + "");
					options.add(option);
				}
				ActionContext.getContext().put("evaluateYears",options);
				if(evaluateYear == null){
					evaluateYear = calendar.get(Calendar.YEAR);
				}
				
				List<Map<String,Object>> estimateModelMaps = new ArrayList<Map<String,Object>>();
				estimateModelMaps.add(convertEstimateModelToMap(estimateModel));
				estimateModelMaps.addAll(createOtherMap());
				ActionContext.getContext().put("estimateModelMaps",JSONArray.fromObject(estimateModelMaps).toString());
				estimateModelId = -4l;
				
				List<Map<String,Object>> datas = evaluateManager.getRealPointsByEstimateModel(estimateModel, supplier, evaluateYear);
				ActionContext.getContext().put("datas",JSONArray.fromObject(datas).toString());
				
				//显示的方式Q1,Q2,Q3,Q4,仅针对航嘉
				List<Integer> columns = new ArrayList<Integer>();
				Map<String,Integer> cycleMap = EstimateModel.getCycleMap();
				List<EstimateModel> modelChildren = evaluateManager.getAllChildren(estimateModel);
				Integer tempMonth = null;
				for(EstimateModel childModel : modelChildren){
					int interval = 1;
					if(cycleMap.containsKey(childModel.getCycle())){
						interval = cycleMap.get(childModel.getCycle()); 
					}
					for(int i=0;i<12/interval;i++){
						if(tempMonth == null){
							tempMonth = childModel.getStartMonth();
						}else{
							tempMonth += interval;
						}
						if(tempMonth > 12){
							continue;
						}
						if(!columns.contains(tempMonth)){
							columns.add(tempMonth);
						}
					}
				}
				Collections.sort(columns);
				Struts2Utils.getRequest().setAttribute("clumnNames",columns);
			}
		}*/
		List<EstimateModel> estimateModel = estimateModelManager.getParentEstimateModel();
		List<Option> estimateModels = new ArrayList<Option>();
		for (int i = 0; i < estimateModel.size(); i++) {
			Option option = new Option();
			option.setName(estimateModel.get(i).getName());
			option.setValue(estimateModel.get(i).getId().toString());
			estimateModels.add(option);
		}
		ActionContext.getContext().put("estimateModels", estimateModels);
		String parentModelId = Struts2Utils.getParameter("parentModelId");
		if(StringUtils.isNotEmpty(parentModelId)){
			EstimateModel model = estimateModelManager.getEstimateModel(Long.parseLong(parentModelId));
			Map<String,Integer> cycleMap = getCycleMap();
			ActionContext.getContext().put("months", createMonhts(cycleMap.get(model.getCycle())));
		}else if(estimateModel.isEmpty()){
			ActionContext.getContext().put("months", createMonhts(1));
		}else{
			Map<String,Integer> cycleMap = getCycleMap();
			ActionContext.getContext().put("months", createMonhts(cycleMap.get(estimateModel.get(0).getCycle())));
		}
		Calendar current = Calendar.getInstance();
		current.setTime(new Date());
		String year = Struts2Utils.getParameter("year");
		String month = Struts2Utils.getParameter("month");
		ActionContext.getContext().put("supplierName", Struts2Utils.getParameter("supplierName"));
		if(StringUtils.isEmpty(year)){
			ActionContext.getContext().put("year", current.get(Calendar.YEAR));
		}else{
			ActionContext.getContext().put("year", Integer.parseInt(year));
		}
		if(StringUtils.isEmpty(month)){
			ActionContext.getContext().put("month", current.get(Calendar.MONTH));
		}else{
			ActionContext.getContext().put("month", Integer.parseInt(month));
		}
		if(StringUtils.isEmpty(parentModelId)){
			ActionContext.getContext().put("parentModelId", estimateModel.get(0).getId());
		}else{
			ActionContext.getContext().put("parentModelId", Integer.parseInt(parentModelId));
		}
		List<Option> years = new ArrayList<Option>();
		for(int i=current.get(Calendar.YEAR);i>current.get(Calendar.YEAR)-2;i--){
			Option option = new Option();
			option.setName(i + "年");
			option.setValue(i + "");
			years.add(option);
		}
		ActionContext.getContext().put("years", years);
		return SUCCESS;
	}
	
	public List<Option> createMonhts(int cycle){
		List<Option> months = new ArrayList<Option>();
		for (int i = 0; i <= (12/cycle)-1; i++) {
			Option option = new Option();
			option.setName(i*cycle+1 + "");
			option.setValue(i*cycle+1 + "");
			months.add(option);
		}
		return months;
	}
	
	public Map<String,Integer> getCycleMap(){
		List<Option> options = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_estimate_model_cycle");
		Map<String,Integer> cycleMap = new HashMap<String, Integer>();
		for(Option option : options){
			if(StringUtils.isNumeric(option.getValue())){
				cycleMap.put(option.getName(),Integer.parseInt(option.getValue()));
			}
		}
		return cycleMap;
	}
	
	private List<Map<String,Object>> createOtherMap(){
		List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
		/*Map<String,Object> map = new HashMap<String, Object>();
		map.put("data","供应商评价得分总表");
		Map<String,Object> attrMap = new HashMap<String, Object>();
		attrMap.put("id",-3);
		attrMap.put("name","供应商评价得分总表");
		attrMap.put("customType","evaluate-total-table");
		attrMap.put("isLeaf",true);
		map.put("attr",attrMap);
		maps.add(map);
		
		map = new HashMap<String, Object>();
		map.put("data",evaluateYear + "年度总评价");
		attrMap = new HashMap<String, Object>();
		attrMap.put("id",-1);
		attrMap.put("name",evaluateYear + "年度总评价");
		attrMap.put("customType","all");
		attrMap.put("isLeaf",true);
		map.put("attr",attrMap);
		maps.add(map);
		
		map = new HashMap<String, Object>();
		map.put("data","供应商评价排名");
		attrMap = new HashMap<String, Object>();
		attrMap.put("id",-4);
		attrMap.put("name","供应商评价排名");
		attrMap.put("customType","point-rank");
		attrMap.put("isLeaf",true);
		map.put("attr",attrMap);
		maps.add(map);
		
		map = new HashMap<String, Object>();
		map.put("data","历史评价台帐");
		attrMap = new HashMap<String, Object>();
		attrMap.put("id",-2);
		attrMap.put("name","历史评价台帐");
		attrMap.put("customType","ledger");
		attrMap.put("isLeaf",true);
		map.put("attr",attrMap);
		maps.add(map);*/
		
		return maps;
	}
	
	/**
	 * 转换评价模型至map
	 * @param estimateModel
	 * @return
	 */
	private Map<String,Object> convertEstimateModelToMap(EstimateModel estimateModel){
		Map<String,Object> map = new HashMap<String, Object>();
//		{ 
//			"data" : "评价模型", 
//			"state" : "closed",
//			attr:{
//				id:'root',
//				level : 0,
//				rel:'drive'
//			}
//		}
		map.put("data",estimateModel.getName());
		Map<String,Object> attrMap = new HashMap<String, Object>();
		attrMap.put("id",estimateModel.getId());
		attrMap.put("name",estimateModel.getName());
		attrMap.put("customType","estimateModel");
		attrMap.put("isLeaf",true);
		map.put("attr",attrMap);
		if(!estimateModel.getChildren().isEmpty()){
			List<Map<String,Object>> children = new ArrayList<Map<String,Object>>();
			for(EstimateModel child : estimateModel.getChildren()){
				if(EstimateModel.STATE_ISUSE.equals(estimateModel.getState())){
					attrMap.put("isLeaf",false);
					map.put("state","open");
					children.add(convertEstimateModelToMap(child));	
				}
			}
			map.put("children",children);
		}else{
			if(estimateModelId == null){
				estimateModelId = estimateModel.getId();
			}
		}
		return map;
	}
	
	public String createWhere(){
		String where ="where 1=1";
		String str = CommonUtil.getBusinessUnitsString();
		if(StringUtils.isNotEmpty(str)){
			where += " and (";
			for (int i = 0; i < str.split(",").length; i++) {
				if(i==0){
					where += "t.sqeDepartmentName like '%" + str.split(",")[i] + "%'";
				}else{
					where += " or t.sqeDepartmentName like '%" + str.split(",")[i] + "%'";
				}
			}
			where += ")";
		}
		String years = Struts2Utils.getParameter("year");
		String month = Struts2Utils.getParameter("month");
		String parentModelId = Struts2Utils.getParameter("parentModelId");
		String supplierName = Struts2Utils.getParameter("supplierName");
		
		if(StringUtils.isNotEmpty(supplierName)){
			where += " and t.supplierName like '%"+supplierName+"%'";
		}
		if(StringUtils.isNotEmpty(parentModelId)){
			where += " and t.parentModelId = "+parentModelId;
		}
		if(StringUtils.isNotEmpty(years)&&StringUtils.isNotEmpty(month)){
			int year = Integer.parseInt(years);
			int [] days = {31,28,31,30,31,30,31,31,30,31,30,31};
			if(year%4==0&&year%400==0&&year%100!=0)
				days[1] = 29;
			where += " and t.evaluateDate>=to_date('"+years+"-"+month+"-1','yyyy-mm-dd') and t.evaluateDate<=to_date('"+years+"-"+month+"-"+days[Integer.parseInt(month)-1]+"','yyyy-mm-dd')";
		}else{
			Calendar current = Calendar.getInstance();
			current.setTime(new Date());
			int [] days = {31,28,31,30,31,30,31,31,30,31,30,31};
			if(current.get(Calendar.YEAR)%4==0&&current.get(Calendar.YEAR)%400==0&&current.get(Calendar.YEAR)%100!=0)
				days[1] = 29;
			where += " and t.evaluateDate>='"+current.get(Calendar.YEAR)+"-"+current.get(Calendar.MONTH)+"-1' and t.evaluateDate<='"+current.get(Calendar.YEAR)+"-"+current.get(Calendar.MONTH)+"-"+days[current.get(Calendar.MONTH)-1]+"'";
		}
		return where;
	}
	
	
	/**
	 * 列表数据
	 */
	@Action("performanceEvaluate-listDatas")
	public String getListDatas() throws Exception {
		String where = createWhere();
		page = performanceEvaluateManager.search(page,where);
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	/**
	 * 绑定查看页面
	 */
	public void prepareView() throws Exception {
		prepareModel();
	}
	
	/**
	 * 查看页面
	 */
	@Action("performanceEvaluate-view")
	public String view() throws Exception {
		fieldPermission=ApiFactory.getFormService().getFieldPermission(false);
		return SUCCESS;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			performanceEvaluate=new PerformanceEvaluate();
		}else{
			performanceEvaluate=performanceEvaluateManager.getPerformanceEvaluate(id);
		}
	}

	/**
	 * 获取计量编号规则级联选项组
	 * @return
	 * @throws Exception
	 */
	@Action("change-model")
	public String changeStations() throws Exception{
		try{
			String parentModelId = Struts2Utils.getParameter("parentModelId");
			Map<String,List<Option>> map = new HashMap<String, List<Option>>();
			EstimateModel model = estimateModelManager.getEstimateModel(Long.parseLong(parentModelId));
			Map<String,Integer> cycleMap = getCycleMap();
			List<Option> months = createMonhts(cycleMap.get(model.getCycle()));
			map.put("months",months);
			renderText(JSONObject.fromObject(map).toString());
		}catch(Exception e){
			logger.error("获取评价模型月份失败!", e);
	    }
    	return null;
	}
	
	/**
	 * 保存
	 */
	@Override
	@Action("performanceEvaluate-save")
	public String save() throws Exception {
		performanceEvaluateManager.savePerformanceEvaluate(performanceEvaluate);
		addSuccessMessage("保存成功！");
		return "performanceEvaluate-input";
	}

	@Override
	public PerformanceEvaluate getModel() {
		return performanceEvaluate;
	}
	
	public void prepareEditSave() throws Exception{
		prepareModel();
	}
	
	/**
	 * 编辑-保存
	 */
	@Action("performanceEvaluate-editSave")
	public String editSave() throws Exception {
		performanceEvaluateManager.savePerformanceEvaluate(performanceEvaluate);
			this.renderText(JsonParser.getRowValue(performanceEvaluate));
		return null;
	}
	
	/**
	 * 编辑-删除
	 */
	@Action("performanceEvaluate-editDelete")
	public String editDelete() throws Exception {
		ids=Struts2Utils.getParameter("deleteIds");
		String[] deleteIds=ids.split(",");
		for(String deleteId:deleteIds){
			performanceEvaluateManager.deletePerformanceEvaluate(Long.valueOf(deleteId));
		}
		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public PerformanceEvaluate getPerformanceEvaluate() {
		return performanceEvaluate;
	}

	public void setPerformanceEvaluate(PerformanceEvaluate performanceEvaluate) {
		this.performanceEvaluate = performanceEvaluate;
	}

	public Page<PerformanceEvaluate> getPage() {
		return page;
	}

	public void setPage(Page<PerformanceEvaluate> page) {
		this.page = page;
	}
	public String getFieldPermission() {
		return fieldPermission;
	}
	public void setFieldPermission(String fieldPermission) {
		this.fieldPermission = fieldPermission;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public Integer getEvaluateYear() {
		return evaluateYear;
	}
	public void setEvaluateYear(Integer evaluateYear) {
		this.evaluateYear = evaluateYear;
	}
	public Long getEstimateModelId() {
		return estimateModelId;
	}
	public void setEstimateModelId(Long estimateModelId) {
		this.estimateModelId = estimateModelId;
	}
	public EstimateModel getEstimateModel() {
		return estimateModel;
	}
	public void setEstimateModel(EstimateModel estimateModel) {
		this.estimateModel = estimateModel;
	}
	
}


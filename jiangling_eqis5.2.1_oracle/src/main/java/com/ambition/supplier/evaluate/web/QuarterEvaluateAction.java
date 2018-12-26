package com.ambition.supplier.evaluate.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

import com.ambition.product.base.CrudActionSupport;
import com.ambition.supplier.archives.service.SupplierManager;
import com.ambition.supplier.baseInfo.services.SupplierMaterialTypeGoalManager;
import com.ambition.supplier.datasource.service.EvaluateDataSourceManager;
import com.ambition.supplier.entity.EstimateModel;
import com.ambition.supplier.entity.Evaluate;
import com.ambition.supplier.entity.EvaluateDetail;
import com.ambition.supplier.entity.EvaluatingGradeRule;
import com.ambition.supplier.entity.EvaluatingIndicator;
import com.ambition.supplier.entity.ModelIndicator;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.entity.SupplierMaterialTypeGoal;
import com.ambition.supplier.estimate.service.EstimateModelManager;
import com.ambition.supplier.estimate.service.EvaluatingIndicatorManager;
import com.ambition.supplier.estimate.service.ModelIndicatorManager;
import com.ambition.supplier.evaluate.service.EvaluateManager;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.common.DateUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

/**
 * 供应商季度考评
 * @author 赵骏
 *
 */
@Namespace("/supplier/evaluate/quarter")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/evaluate/quarter", type = "redirectAction") })
@Conversion
public class QuarterEvaluateAction  extends CrudActionSupport<Evaluate> {
	private Logger logger = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long estimateModelId;//评价模型的编号
	private static Long oldSupplierId;
	private String deleteIds;//删除的编号 
	@Autowired
	private SupplierMaterialTypeGoalManager supplierMaterialTypeGoalManager;
	@Autowired
 	private ModelIndicatorManager modelIndicatorManager;
	private Evaluate evaluate;
	
	private Long supplierId;//供应商编号
	
	private Supplier supplier;//对应的供应商
	
	private Integer evaluateYear;//评价年份
	
	private Integer evaluateMonth;//评价的月份
	
	private Integer startMonth;//开始的月份
	
	private Integer endMonth;//结束的月份
	
	private String sqedep;//事业部
	
	private String materialType;//物料类别
	
	private EstimateModel estimateModel;
	
	private Boolean canModify=true;
	private Page<Evaluate> page;
//	private Page<NCReport> nCReportPage;
//	private Page<Improve8dReport> improve8dReportPage;
	
	@Autowired
	private AcsUtils acsUtils;
	
 	public Boolean getCanModify() {
		return canModify;
	}

	public void setCanModify(Boolean canModify) {
		this.canModify = canModify;
	}


	@Autowired
	private EvaluateManager evaluateManager;
 	
 	@Autowired
 	private EstimateModelManager estimateModelManager;
 	
 	@Autowired
 	private SupplierManager supplierManager;
 	
 	@Autowired
 	private EvaluatingIndicatorManager evaluatingIndicatorManager;
 	
 	@Autowired
 	private EvaluateDataSourceManager dataSourceManager;
 	
// 	@Autowired
// 	private SupplierEvaluateIntegrationService supplierEvaluateIntegrationService;
 	
 	@Autowired
	private LogUtilDao logUtilDao;
 	
 	private JSONObject params;

	public Page<Evaluate> getPage() {
		return page;
	}

	public void setPage(Page<Evaluate> page) {
		this.page = page;
	}
	
	

//	public Page<NCReport> getnCReportPage() {
//		return nCReportPage;
//	}
//
//	public void setnCReportPage(Page<NCReport> nCReportPage) {
//		this.nCReportPage = nCReportPage;
//	}
//
//	public Page<Improve8dReport> getImprove8dReportPage() {
//		return improve8dReportPage;
//	}
//
//	public void setImprove8dReportPage(Page<Improve8dReport> improve8dReportPage) {
//		this.improve8dReportPage = improve8dReportPage;
//	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public Evaluate getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(Evaluate evaluate) {
		this.evaluate = evaluate;
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

	public Integer getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(Integer startMonth) {
		this.startMonth = startMonth;
	}

	public Integer getEvaluateMonth() {
		return evaluateMonth;
	}

	public void setEvaluateMonth(Integer evaluateMonth) {
		this.evaluateMonth = evaluateMonth;
	}

	public Integer getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(Integer endMonth) {
		this.endMonth = endMonth;
	}

	public EstimateModel getEstimateModel() {
		return estimateModel;
	}

	public void setEstimateModel(EstimateModel estimateModel) {
		this.estimateModel = estimateModel;
	}

	public Long getEstimateModelId() {
		return estimateModelId;
	}

	public void setEstimateModelId(Long estimateModelId) {
		this.estimateModelId = estimateModelId;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Evaluate getModel() {
		return evaluate;
	}
	
	

	public Integer getEvaluateYear() {
		return evaluateYear;
	}

	public void setEvaluateYear(Integer evaluateYear) {
		this.evaluateYear = evaluateYear;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			evaluate = new Evaluate();
			evaluate.setCreatedTime(new Date());
			evaluate.setCompanyId(ContextUtils.getCompanyId());
			evaluate.setCreator(ContextUtils.getUserName());
			evaluate.setModifiedTime(new Date());
			evaluate.setModifier(ContextUtils.getUserName());
			evaluate.setBusinessUnitName(ContextUtils.getSubCompanyName());
			evaluate.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			evaluate = evaluateManager.getEvaluate(id);
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
		if(id == null){
			try{
				evaluate.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
				evaluateManager.saveEvaluate(evaluate,params);
				logUtilDao.debugLog("保存", evaluate.toString());
				createMessage("保存成功！",evaluate.getId());
			}catch(Exception e){
				e.printStackTrace();
				createErrorMessage("保存失败：" + e.getMessage());
			}
		}else{
			if(evaluate != null){
				evaluate.setModifiedTime(new Date());
				evaluate.setModifier(ContextUtils.getLoginName());
				try{
					evaluateManager.saveEvaluate(evaluate,params);
					logUtilDao.debugLog("修改", evaluate.toString());
					createMessage("保存成功！",evaluate.getId());
				}catch(Exception e){
					e.printStackTrace();
					createErrorMessage("保存成功！：" + e.getMessage());
				}
			}else{
				createErrorMessage("保存失败,供应商的评价不存在!");
			}
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
				evaluateManager.deleteEvaluate(deleteIds);
				createMessage("删除成功!");
			} catch (Exception e) {
				createErrorMessage("删除失败:" + e.getMessage());
			}
		}
		return null;
	}

//	@Override
//	public String list() throws Exception {
//		ActionContext.getContext().put("estimateModelMaps","[]");
//		return SUCCESS;
//	}
	
//	@Action("list-datas")
//	public String getBomListByParent() throws Exception {
//		return null;
//	}
	//物料类别加载
    public void selectMaterialType(){
		List<SupplierMaterialTypeGoal> types = supplierMaterialTypeGoalManager.getAllType();
		List<Option> options = new ArrayList<Option>();
		for(SupplierMaterialTypeGoal s : types){
			Option o = new Option();
			o.setName(s.getMaterialType());
			o.setValue(s.getMaterialType());
			options.add(o);
		}
		ActionContext.getContext().put("materialTypes",options);
	}
	/**
	 * 添加供应商评价单的页面
	 * @return
	 * @throws Exception
	 */
	@Action("add")
	public String add() throws Exception {
		ActionContext.getContext().put("evaluateLevels",ApiFactory.getSettingService().getOptionsByGroupCode("supplier_evaluate_level"));
		ActionContext.getContext().put("error","");
		ActionContext.getContext().put("estimateModelMaps","[]");
		ActionContext.getContext().put("datas","[]");
		Calendar calendar = Calendar.getInstance();
		ActionContext.getContext().put("evaluateYears",new ArrayList<Option>());
		ActionContext.getContext().put("evaluateMonths",new ArrayList<Option>());
		selectMaterialType();
		if(id != null){
			evaluate = evaluateManager.getEvaluate(id);
		}
		if(evaluate != null){
			return loadEvaluate(evaluate);
		}else{
			List<Option> options = new ArrayList<Option>();
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
			Department departMent1 =  acsUtils.getDepartmentById(ContextUtils.getDepartmentId());
			if(departMent1 != null && departMent1.getParent()!=null&&departMent1.getParent().getName().equals("Suppliers")){//不是海利达人员
				Supplier supplier = supplierManager.getSupplierByName(departMent1.getName());
				if(supplier==null){
					addActionMessage("供应商台账中没有该供应商的信息请先添加!");
					return SUCCESS;
				}else{
					supplierId = supplier.getId();
				}
			}
			if(supplierId != null){
				supplier = supplierManager.getSupplier(supplierId);
			}else{
				supplier = supplierManager.getSupplerByRandom();
			}
			if(supplier == null){
				ActionContext.getContext().put("error","supplier");
				addActionMessage("请选择供应商！");
				return SUCCESS;
			}else{
				ActionContext.getContext().put("supplierName",supplier.getName());
				ActionContext.getContext().put("supplierCode",supplier.getCode());
				supplierId = supplier.getId();
				EstimateModel estimateModel = estimateModelManager.getEstimateModelById(supplier.getEstimateModelId());
				if(estimateModel == null){
					ActionContext.getContext().put("error","parentEstimateModel");
					addActionMessage("请先到供应商档案管理设置供应商对应的评价模型!");
					return SUCCESS;
				}else{
					if(oldSupplierId != null&&!oldSupplierId.equals(supplierId)){
						estimateModelId = null;
					}
					oldSupplierId = supplierId;
					List<Map<String,Object>> estimateModelMaps = new ArrayList<Map<String,Object>>();
					estimateModelMaps.add(convertEstimateModelToMap(estimateModel));
					estimateModelMaps.addAll(createOtherMap());
					ActionContext.getContext().put("estimateModelMaps",JSONArray.fromObject(estimateModelMaps).toString());
					if(estimateModelId == null){
						ActionContext.getContext().put("error","childEstimateModel");
						addActionMessage("请选择评价模型!");
						return SUCCESS;
					}else{
						estimateModel = estimateModelManager.getEstimateModel(estimateModelId);
						if(estimateModel == null){
							ActionContext.getContext().put("error","childEstimateModel");
							addActionMessage("评价模型不存在!");
							return SUCCESS;
						}else if(StringUtils.isEmpty(estimateModel.getCycle())||estimateModel.getStartMonth() == null){
							ActionContext.getContext().put("error","childEstimateModel");
							addActionMessage("请先设置模型的评价周期和起始月!");
							return SUCCESS;
						}else{
							ActionContext.getContext().put("cycle",estimateModel.getCycle());
							options = new ArrayList<Option>();
							Map<String,Integer> cycleMap = EstimateModel.getCycleMap();
							int interval = 1;
							if(cycleMap.containsKey(estimateModel.getCycle())){
								interval = cycleMap.get(estimateModel.getCycle());
							}
							Integer tempMonth = null;
							Date date = new Date();
						    Integer month = date.getMonth()+1;
							for(int i=0;i<12/interval;i++){
								if(tempMonth == null){
									tempMonth = estimateModel.getStartMonth();
								}else{
									tempMonth += interval;
								}
								if(tempMonth > 12){
									continue;
								}
								if(interval==1){
									if(evaluateMonth == null && tempMonth >= (calendar.get(Calendar.MONTH) + 1)){
										evaluateMonth = tempMonth;
									}
								}else if(interval==3){
									if(evaluateMonth == null && tempMonth+2 >= (calendar.get(Calendar.MONTH) + 1)){
										evaluateMonth = tempMonth;
									}
								}else if(interval==6){
									if(evaluateMonth == null && tempMonth+5 >= (calendar.get(Calendar.MONTH) + 1)){
										evaluateMonth = tempMonth;
									}
								}else if(interval==12){
									if(evaluateMonth == null && tempMonth+11 >= (calendar.get(Calendar.MONTH) + 1)){
										evaluateMonth = tempMonth;
									}
								}
								if(evaluateMonth == null &&month>=4&&month<=6){
									evaluateMonth = 1;
								}else if(evaluateMonth == null &&month>=7&&month<=9){
									evaluateMonth = 4;
								}else if(evaluateMonth == null &&month>=10&&month<=12){
									evaluateMonth = 7;
								}else if(evaluateMonth == null &&month>=1&&month<=4){
									evaluateMonth = 10;
								}
								Option option = new Option();
								if(tempMonth==1){
									option.setName("Q-1");
								}else if(tempMonth==4){
									option.setName("Q-2");
								}else if(tempMonth==7){
									option.setName("Q-3");
								}else if(tempMonth==10){
									option.setName("Q-4");
								}
								option.setValue(tempMonth + "");
								options.add(option);
							}
							ActionContext.getContext().put("evaluateMonths",options);
							
							evaluate = evaluateManager.findEvaluateBySupplerAndYearMonth(supplier, evaluateYear, evaluateMonth, estimateModelId,materialType);
							if(evaluate == null){
								evaluate = new Evaluate();
								evaluate.setSupplierId(supplier.getId());
								evaluate.setSupplierName(supplier.getName());
								evaluate.setMaterialType(materialType);
								evaluate.setSupplyProducts(supplier.getSupplyProductCodes());
								evaluate.setStartMonth(estimateModel.getStartMonth());
								evaluate.setCycle(estimateModel.getCycle());
								evaluate.setEstimateModelId(estimateModel.getId());
								evaluate.setEstimateModelName(estimateModel.getName());
								evaluate.setParentModelId(estimateModel.getParentEstimateModelId()==null?estimateModel.getId():estimateModel.getParentEstimateModelId());
								evaluate.setWriteMan(ContextUtils.getUserName());
								evaluate.setWriteDate(new Date(System.currentTimeMillis()));
								evaluate.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
								SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
								SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
								evaluate.setEvaluateNo(sdf.format(new Date(System.currentTimeMillis())));
								calendar = Calendar.getInstance();
								if(calendar.get(Calendar.YEAR) != evaluateYear || calendar.get(Calendar.MONTH) != evaluateMonth-1){
									calendar.set(Calendar.YEAR,evaluateYear);
									calendar.set(Calendar.MONTH,evaluateMonth-1);
									calendar.set(Calendar.DATE,1);
								}
								evaluate.setEvaluateDate(calendar.getTime());
								evaluate.setReportDate(calendar.getTime());
								evaluate.setEvaluateDetails(convertIndicatorToEvaluateDetails(estimateModel.getModelIndicators()));
							}
							if(evaluate!=null){
								Calendar calendar2 = Calendar.getInstance();
								calendar2.set(calendar2.get(Calendar.YEAR), evaluate.getEvaluateMonth()-1, 1);
								calendar2.add(Calendar.DATE, 30);
								System.out.println(calendar2.getTime());
								if(calendar.getTimeInMillis()>calendar2.getTimeInMillis()){
									canModify=false;
								}
							}
//							evaluate.setSqeDepartmentName(supplier.getWhetherProducers());//事业部
							JSONObject autoGradeMap = new JSONObject();
							ActionContext.getContext().put("modelObj",createModelTableByEvaluate(evaluate.getEvaluateDetails(),autoGradeMap));
							ActionContext.getContext().put("autoGradeMap",autoGradeMap);
							calendar.setTime(evaluate.getEvaluateDate());
							calendar.set(Calendar.DATE,1);
							ActionContext.getContext().put("minDateStr",DateUtil.formateDateStr(calendar));
							calendar.add(Calendar.MONTH,1);
							calendar.add(Calendar.DATE,-1);
							ActionContext.getContext().put("maxDateStr",DateUtil.formateDateStr(calendar));
							return SUCCESS;
						}
					}
				}
			}
		}
	}
	/**
	 * 添加供应商评价单的页面
	 * @return
	 * @throws Exception
	 */
	@Action("print-form")
	public String printForm() throws Exception {
		ActionContext.getContext().put("evaluateLevels",ApiFactory.getSettingService().getOptionsByGroupCode("supplier_evaluate_level"));
		ActionContext.getContext().put("error","");
		ActionContext.getContext().put("estimateModelMaps","[]");
		ActionContext.getContext().put("datas","[]");
		Calendar calendar = Calendar.getInstance();
		ActionContext.getContext().put("evaluateYears",new ArrayList<Option>());
		ActionContext.getContext().put("evaluateMonths",new ArrayList<Option>());
		selectMaterialType();
		if(id != null){
			evaluate = evaluateManager.getEvaluate(id);
		}
		if(evaluate != null){
			return loadEvaluate(evaluate);
		}else{
			List<Option> options = new ArrayList<Option>();
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
			Department departMent1 =  acsUtils.getDepartmentById(ContextUtils.getDepartmentId());
			if(departMent1 != null && departMent1.getParent()!=null&&departMent1.getParent().getName().equals("Suppliers")){//不是海利达人员
				Supplier supplier = supplierManager.getSupplierByName(departMent1.getName());
				if(supplier==null){
					addActionMessage("供应商台账中没有该供应商的信息请先添加!");
					return SUCCESS;
				}else{
					supplierId = supplier.getId();
				}
			}
			if(supplierId != null){
				supplier = supplierManager.getSupplier(supplierId);
			}else{
				supplier = supplierManager.getSupplerByRandom();
			}
			if(supplier == null){
				ActionContext.getContext().put("error","supplier");
				addActionMessage("请选择供应商！");
				return SUCCESS;
			}else{
				ActionContext.getContext().put("supplierName",supplier.getName());
				ActionContext.getContext().put("supplierCode",supplier.getCode());
				supplierId = supplier.getId();
				EstimateModel estimateModel = estimateModelManager.getEstimateModelById(supplier.getEstimateModelId());
				if(estimateModel == null){
					ActionContext.getContext().put("error","parentEstimateModel");
					addActionMessage("请先到供应商档案管理设置供应商对应的评价模型!");
					return SUCCESS;
				}else{
					if(oldSupplierId != null&&!oldSupplierId.equals(supplierId)){
						estimateModelId = null;
					}
					oldSupplierId = supplierId;
					List<Map<String,Object>> estimateModelMaps = new ArrayList<Map<String,Object>>();
					estimateModelMaps.add(convertEstimateModelToMap(estimateModel));
					estimateModelMaps.addAll(createOtherMap());
					ActionContext.getContext().put("estimateModelMaps",JSONArray.fromObject(estimateModelMaps).toString());
					if(estimateModelId == null){
						ActionContext.getContext().put("error","childEstimateModel");
						addActionMessage("请选择评价模型!");
						return SUCCESS;
					}else{
						estimateModel = estimateModelManager.getEstimateModel(estimateModelId);
						if(estimateModel == null){
							ActionContext.getContext().put("error","childEstimateModel");
							addActionMessage("评价模型不存在!");
							return SUCCESS;
						}else if(StringUtils.isEmpty(estimateModel.getCycle())||estimateModel.getStartMonth() == null){
							ActionContext.getContext().put("error","childEstimateModel");
							addActionMessage("请先设置模型的评价周期和起始月!");
							return SUCCESS;
						}else{
							ActionContext.getContext().put("cycle",estimateModel.getCycle());
							options = new ArrayList<Option>();
							Map<String,Integer> cycleMap = EstimateModel.getCycleMap();
							int interval = 1;
							if(cycleMap.containsKey(estimateModel.getCycle())){
								interval = cycleMap.get(estimateModel.getCycle());
							}
							Integer tempMonth = null;
							Date date = new Date();
						    Integer month = date.getMonth()+1;
							for(int i=0;i<12/interval;i++){
								if(tempMonth == null){
									tempMonth = estimateModel.getStartMonth();
								}else{
									tempMonth += interval;
								}
								if(tempMonth > 12){
									continue;
								}
								if(interval==1){
									if(evaluateMonth == null && tempMonth >= (calendar.get(Calendar.MONTH) + 1)){
										evaluateMonth = tempMonth;
									}
								}else if(interval==3){
									if(evaluateMonth == null && tempMonth+2 >= (calendar.get(Calendar.MONTH) + 1)){
										evaluateMonth = tempMonth;
									}
								}else if(interval==6){
									if(evaluateMonth == null && tempMonth+5 >= (calendar.get(Calendar.MONTH) + 1)){
										evaluateMonth = tempMonth;
									}
								}else if(interval==12){
									if(evaluateMonth == null && tempMonth+11 >= (calendar.get(Calendar.MONTH) + 1)){
										evaluateMonth = tempMonth;
									}
								}
								if(evaluateMonth == null &&month>=4&&month<=6){
									evaluateMonth = 1;
								}else if(evaluateMonth == null &&month>=7&&month<=9){
									evaluateMonth = 4;
								}else if(evaluateMonth == null &&month>=10&&month<=12){
									evaluateMonth = 7;
								}else if(evaluateMonth == null &&month>=1&&month<=4){
									evaluateMonth = 10;
								}
								Option option = new Option();
								if(tempMonth==1){
									option.setName("Q-1");
								}else if(tempMonth==4){
									option.setName("Q-2");
								}else if(tempMonth==7){
									option.setName("Q-3");
								}else if(tempMonth==10){
									option.setName("Q-4");
								}
								option.setValue(tempMonth + "");
								options.add(option);
							}
							ActionContext.getContext().put("evaluateMonths",options);
							
							evaluate = evaluateManager.findEvaluateBySupplerAndYearMonth(supplier, evaluateYear, evaluateMonth, estimateModelId,materialType);
							if(evaluate == null){
								evaluate = new Evaluate();
								evaluate.setSupplierId(supplier.getId());
								evaluate.setSupplierName(supplier.getName());
								evaluate.setMaterialType(materialType);
								evaluate.setSupplyProducts(supplier.getSupplyProductCodes());
								evaluate.setStartMonth(estimateModel.getStartMonth());
								evaluate.setCycle(estimateModel.getCycle());
								evaluate.setEstimateModelId(estimateModel.getId());
								evaluate.setEstimateModelName(estimateModel.getName());
								evaluate.setParentModelId(estimateModel.getParentEstimateModelId()==null?estimateModel.getId():estimateModel.getParentEstimateModelId());
								evaluate.setWriteMan(ContextUtils.getUserName());
								evaluate.setWriteDate(new Date(System.currentTimeMillis()));
								evaluate.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
								SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
								SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
								evaluate.setEvaluateNo(sdf.format(new Date(System.currentTimeMillis())));
								calendar = Calendar.getInstance();
								if(calendar.get(Calendar.YEAR) != evaluateYear || calendar.get(Calendar.MONTH) != evaluateMonth-1){
									calendar.set(Calendar.YEAR,evaluateYear);
									calendar.set(Calendar.MONTH,evaluateMonth-1);
									calendar.set(Calendar.DATE,1);
								}
								evaluate.setEvaluateDate(calendar.getTime());
								evaluate.setReportDate(calendar.getTime());
								evaluate.setEvaluateDetails(convertIndicatorToEvaluateDetails(estimateModel.getModelIndicators()));
							}
							if(evaluate!=null){
								Calendar calendar2 = Calendar.getInstance();
								calendar2.set(calendar2.get(Calendar.YEAR), evaluate.getEvaluateMonth()-1, 1);
								calendar2.add(Calendar.DATE, 30);
								System.out.println(calendar2.getTime());
								if(calendar.getTimeInMillis()>calendar2.getTimeInMillis()){
									canModify=false;
								}
							}
//							evaluate.setSqeDepartmentName(supplier.getWhetherProducers());//事业部
							JSONObject autoGradeMap = new JSONObject();
							ActionContext.getContext().put("modelObj",createModelTableByEvaluate(evaluate.getEvaluateDetails(),autoGradeMap));
							ActionContext.getContext().put("autoGradeMap",autoGradeMap);
							calendar.setTime(evaluate.getEvaluateDate());
							calendar.set(Calendar.DATE,1);
							ActionContext.getContext().put("minDateStr",DateUtil.formateDateStr(calendar));
							calendar.add(Calendar.MONTH,1);
							calendar.add(Calendar.DATE,-1);
							ActionContext.getContext().put("maxDateStr",DateUtil.formateDateStr(calendar));
							return SUCCESS;
						}
					}
				}
			}
		}
	}
	
	private EvaluatingIndicator getEvaluatingIndicator(EvaluatingIndicator source,Map<Long,EvaluatingIndicator> targetMap,List<EvaluatingIndicator> topEvaluatingIndicators,Map<Long,Integer> timesMap){
		if(source == null){
			return null;
		}else if(targetMap.containsKey(source.getId())){
			timesMap.put(source.getId(),timesMap.get(source.getId()) + 1);
			EvaluatingIndicator target = targetMap.get(source.getId());
			getEvaluatingIndicator(target.getParent(),targetMap, topEvaluatingIndicators, timesMap);
			return target;
		}else{
			EvaluatingIndicator parent = getEvaluatingIndicator(source.getParent(),targetMap,topEvaluatingIndicators,timesMap);
			EvaluatingIndicator target = new EvaluatingIndicator();
			target.setParent(parent);
			target.setCreatedTime(source.getCreatedTime());
			target.setOrderByNum(source.getOrderByNum());
			target.setLevel(source.getLevel());
			target.setName(source.getName());
			target.setId(source.getId());
			target.setUnit(source.getUnit());
			target.setChildren(new ArrayList<EvaluatingIndicator>());
			targetMap.put(target.getId(),target);
			timesMap.put(source.getId(),1);
			if(parent != null){
				parent.getChildren().add(target);
			}else{
				topEvaluatingIndicators.add(target);
			}
			return target;
		}
	}
	//转换ModelIndicator为EvaluateDetail
	private List<EvaluateDetail> convertIndicatorToEvaluateDetails(List<ModelIndicator> modelIndicators){
		List<EvaluateDetail> evaluateDetails = new ArrayList<EvaluateDetail>();
		Map<Long,EvaluatingIndicator> targetMap = new HashMap<Long, EvaluatingIndicator>();
		List<EvaluatingIndicator> topEvaluatingIndicators = new ArrayList<EvaluatingIndicator>();
		Map<Long,ModelIndicator> detailMap = new HashMap<Long,ModelIndicator>();
		Map<Long,Integer> timesMap = new HashMap<Long, Integer>();
		for(ModelIndicator modelIndicator : modelIndicators){
			if(EstimateModel.STATE_DISABLED.equals(modelIndicator.getEstimateModel().getState())
				||!modelIndicator.getEstimateModel().getChildren().isEmpty()
				||!modelIndicator.getEvaluatingIndicator().getChildren().isEmpty()
//				||modelIndicator.getTotalPoints() == null
//				||modelIndicator.getTotalPoints() <= 0
				){
				continue;
			}
			EvaluatingIndicator evaluatingIndicator = getEvaluatingIndicator(modelIndicator.getEvaluatingIndicator(), targetMap,topEvaluatingIndicators,timesMap);
			detailMap.put(evaluatingIndicator.getId(),modelIndicator);
		}
		sort(topEvaluatingIndicators);
		for(EvaluatingIndicator evaluatingIndicator : topEvaluatingIndicators){
			ModelIndicator modelIndicator = detailMap.get(evaluatingIndicator.getId());
			if(modelIndicator != null){
				evaluateDetails.add(createEvaluateDetail(modelIndicator,"<td colspan='3'>"+evaluatingIndicator.getName()+"</td>",null,null));
			}else{
				sort(evaluatingIndicator.getChildren());
				for(EvaluatingIndicator level2 : evaluatingIndicator.getChildren()){
					modelIndicator = detailMap.get(level2.getId());
					if(modelIndicator!=null){
						String firstColName = null;
						if(timesMap.containsKey(evaluatingIndicator.getId())){
							firstColName = "<td rowspan='"+timesMap.get(evaluatingIndicator.getId())+"'>"+evaluatingIndicator.getName()+"</td>";
							timesMap.remove(evaluatingIndicator.getId());
						}
						EvaluateDetail detail = createEvaluateDetail(modelIndicator,firstColName,"<td colspan='2'>"+level2.getName()+"</td>",null);
						detail.setUnit(level2.getUnit());
						evaluateDetails.add(detail);
					}else{
						sort(level2.getChildren());
						for(EvaluatingIndicator level3 : level2.getChildren()){
							modelIndicator = detailMap.get(level3.getId());
							if(modelIndicator != null){
								String firstColName = null,secondColName = null;
								if(timesMap.containsKey(evaluatingIndicator.getId())){
									firstColName = "<td rowspan='"+timesMap.get(evaluatingIndicator.getId())+"'>"+evaluatingIndicator.getName()+"</td>";
									timesMap.remove(evaluatingIndicator.getId());
								}
								if(timesMap.containsKey(level2.getId())){
									secondColName = "<td rowspan='"+timesMap.get(level2.getId())+"'>"+level2.getName()+"</td>";
									timesMap.remove(level2.getId());
								}
								EvaluateDetail detail = createEvaluateDetail(modelIndicator,firstColName,secondColName,"<td>"+level3.getName()+"</td>");
								detail.setUnit(level3.getUnit());
								evaluateDetails.add(detail);
							}
						}
					}
				}
			}
		}
		return evaluateDetails;
	}
	private void sort(List<EvaluatingIndicator> evaluatingIndicators){
		Collections.sort(evaluatingIndicators,new Comparator<EvaluatingIndicator>() {
			@Override
			public int compare(EvaluatingIndicator o1,
					EvaluatingIndicator o2) {
				if(o1.getCreatedTime().getTime()<o2.getCreatedTime().getTime()){
					return 0;
				}else{
					return 1;
				}
//				if(o1.getOrderByNum()<o2.getOrderByNum()){
//					return o1.getOrderByNum()-o2.getOrderByNum();
//				}else{
//					return o2.getOrderByNum()-o1.getOrderByNum();
//				}
//				if(o1.getCreatedTime().getTime()<o2.getCreatedTime().getTime()){
//					return Integer.valueOf(String.valueOf(o1.getCreatedTime().getTime()-o2.getCreatedTime().getTime()));
//				}else{
//					return Integer.valueOf(String.valueOf(o2.getCreatedTime().getTime()-o1.getCreatedTime().getTime()));
//				}
			}
		});
	}
	@SuppressWarnings("unused")
	private void sortDetail(List<EvaluateDetail> evaluateDetails){
		Collections.sort(evaluateDetails,new Comparator<EvaluateDetail>() {
			@Override
			public int compare(EvaluateDetail o1,
					EvaluateDetail o2) {
//				if(o1.getEvaluatingIndicatorId()!=null&&o2.getEvaluatingIndicatorId()!=null){
//					if(o1.getEvaluatingIndicatorId()<o2.getEvaluatingIndicatorId()){
//						return 0;
//					}else{
//						return 1;
//					}
//				}else{
//					return 0;
//				}
				if(o1.getOrderByNum()!=null&&o2.getOrderByNum()!=null){
					if(o1.getOrderByNum()<o2.getOrderByNum()){
						return 0;
					}else{
						return 1;
					}
				}else{
					return 0;
				}
//				if(o1.getCreatedTime().getTime()<o2.getCreatedTime().getTime()){
//					return Integer.valueOf(String.valueOf(o1.getCreatedTime().getTime()-o2.getCreatedTime().getTime()));
//				}else{
//					return Integer.valueOf(String.valueOf(o2.getCreatedTime().getTime()-o1.getCreatedTime().getTime()));
//				}
			}
		});
	}
	private EvaluateDetail createEvaluateDetail(ModelIndicator modelIndicator,String firstColHtml,String secondColHtml,String thirdColHtml){
		EvaluateDetail evaluateDetail = new EvaluateDetail();
		evaluateDetail.setFirstColHtml(firstColHtml);
		evaluateDetail.setSecondColHtml(secondColHtml);
		evaluateDetail.setThirdColHtml(thirdColHtml);
		evaluateDetail.setTotalPoints(modelIndicator.getTotalPoints());
		evaluateDetail.setName(modelIndicator.getEvaluatingIndicator().getName());
		evaluateDetail.setModelIndicatorId(modelIndicator.getId());
		evaluateDetail.setRemark(modelIndicator.getRemark());
		evaluateDetail.setLevela(modelIndicator.getLevela());
		evaluateDetail.setLevelaMin(modelIndicator.getLevelaMin());
		evaluateDetail.setLevelaMax(modelIndicator.getLevelaMax());
		evaluateDetail.setLevelb(modelIndicator.getLevelb());
		evaluateDetail.setLevelbMin(modelIndicator.getLevelbMin());
		evaluateDetail.setLevelbMax(modelIndicator.getLevelbMax());
		evaluateDetail.setLevelc(modelIndicator.getLevelc());
		evaluateDetail.setLevelcMin(modelIndicator.getLevelcMin());
		evaluateDetail.setLevelcMax(modelIndicator.getLevelcMax());
		evaluateDetail.setLeveld(modelIndicator.getLeveld());
		evaluateDetail.setLeveldMin(modelIndicator.getLeveldMin());
		evaluateDetail.setLeveldMax(modelIndicator.getLeveldMax());
		evaluateDetail.setLevele(modelIndicator.getLevele());
		evaluateDetail.setLeveleMin(modelIndicator.getLeveleMin());
		evaluateDetail.setLeveleMax(modelIndicator.getLeveleMax());
		return evaluateDetail;
	}
	/**
	 * 根据已有的评价加载
	 * @param evaluate
	 * @return
	 */
	public String loadEvaluate(Evaluate evaluate){
		ActionContext.getContext().put("supplierName",evaluate.getSupplierName());
		supplierId = evaluate.getSupplierId();
		evaluateYear = evaluate.getEvaluateYear();
		evaluateMonth = evaluate.getEvaluateMonth();
		List<Option> options = new ArrayList<Option>();
		for(int i=evaluateYear;i>evaluateYear-2;i--){
			Option option = new Option();
			option.setName(i + "年");
			option.setValue(i + "");
			options.add(option);
		}
		ActionContext.getContext().put("evaluateYears",options);
		
		estimateModel = estimateModelManager.getEstimateModel(evaluate.getParentModelId());
		estimateModelId = evaluate.getEstimateModelId();
		
		List<Map<String,Object>> estimateModelMaps = new ArrayList<Map<String,Object>>();
		if(estimateModel != null){
			estimateModelMaps.add(convertEstimateModelToMap(estimateModel));
		}
		estimateModelMaps.addAll(createOtherMap());
		ActionContext.getContext().put("estimateModelMaps",JSONArray.fromObject(estimateModelMaps).toString());

		options = new ArrayList<Option>();
		Map<String,Integer> cycleMap = EstimateModel.getCycleMap();
		int interval = 1;
		if(cycleMap.containsKey(estimateModel.getCycle())){
			interval = cycleMap.get(estimateModel.getCycle());
		}
		Integer tempMonth = null;
		for(int i=0;i<12/interval;i++){
			if(tempMonth == null){
				tempMonth = evaluate.getStartMonth();
			}else{
				tempMonth += interval;
			}
			if(tempMonth > 12){
				continue;
			}
			Option option = new Option();
			option.setName(tempMonth+"");
			option.setValue(tempMonth + "");
			options.add(option);
		}
		ActionContext.getContext().put("evaluateMonths",options);
		JSONObject autoGradeMap = new JSONObject();
		ActionContext.getContext().put("modelObj",createModelTableByEvaluate(evaluate.getEvaluateDetails(),autoGradeMap));
		ActionContext.getContext().put("autoGradeMap",autoGradeMap);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(evaluate.getEvaluateDate());
		calendar.set(Calendar.DATE,1);
		ActionContext.getContext().put("minDateStr",df.format(calendar.getTime()));
		calendar.add(Calendar.MONTH,1);
		calendar.add(Calendar.DATE,-1);
		ActionContext.getContext().put("maxDateStr",df.format(calendar.getTime()));
		return SUCCESS;
	}
	/**
	 * 加载最新评价子项
	 * @return
	 */
	@Action("load-evaluate-detail")
	public String loadEvaluateDetail(){
		EstimateModel estimateModel = estimateModelManager.getEstimateModel(estimateModelId);
		if(estimateModel == null){
			createErrorMessage("评价模型不存在!");
		}else{
			evaluate = new Evaluate();
			evaluate.setEvaluateDetails(convertIndicatorToEvaluateDetails(estimateModel.getModelIndicators()));
			JSONObject autoGradeMap = new JSONObject();
			String str = createModelTableByEvaluate(evaluate.getEvaluateDetails(),autoGradeMap);
			str += "<script>autoGradeMap="+autoGradeMap+"</script>";
			createMessage(str);
		}
		return null;
	}
	
	/**
	 * 获取全部的评价
	 * @return
	 */
	@Action("all")
	public String getAllEvaluate(){
		ActionContext.getContext().put("error","");
		ActionContext.getContext().put("estimateModelMaps","[]");
		ActionContext.getContext().put("datas","[]");
		selectMaterialType();
		if(supplierId != null){
			supplier = supplierManager.getSupplier(supplierId);
		}else{
			Department departMent1 =  acsUtils.getDepartmentById(ContextUtils.getDepartmentId());
			if(departMent1 != null &&departMent1.getParent()!=null&&departMent1.getParent().getName().equals("Suppliers")){//不是海利达人员
				supplier = supplierManager.getSupplierByName(departMent1.getName());
				if(supplier==null){
					addActionMessage("供应商台账中没有该供应商的信息请先添加!");
					return SUCCESS;
				}else{
					supplierId = supplier.getId();
				}
			}else{
				ActionContext.getContext().put("error","supplier");
				addActionMessage("请选择供应商！");
			}
		}
		if(supplier != null){
			ActionContext.getContext().put("supplierName",supplier.getName());
			estimateModel = estimateModelManager.getEstimateModelById(supplier.getEstimateModelId());
			if(estimateModel != null){
				List<Option> options = new ArrayList<Option>();
				Calendar calendar = Calendar.getInstance();
				for(int i=calendar.get(Calendar.YEAR);i>calendar.get(Calendar.YEAR)-2;i--){
					Option option = new Option();
					option.setName(i + "");
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
				estimateModelId = -1l;
				
				List<Map<String,Object>> datas = evaluateManager.getRealPointsByEstimateModel(estimateModel, supplier, evaluateYear,materialType);
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
		}
		return SUCCESS;
	}
	
	/**
	 * 获取供应商的评价台帐
	 * @return
	 */
	@Action("ledger")
	public String getLedger(){
		ActionContext.getContext().put("error","");
		ActionContext.getContext().put("estimateModelMaps","[]");
		ActionContext.getContext().put("datas","[]");
		if(supplierId != null){
			supplier = supplierManager.getSupplier(supplierId);
		}else{
			Department departMent1 =  acsUtils.getDepartmentById(ContextUtils.getDepartmentId());
			if(departMent1 != null &&departMent1.getParent()!=null&&departMent1.getParent().getName().equals("Suppliers")){//不是海利达人员
				supplier = supplierManager.getSupplierByName(departMent1.getName());
				if(supplier==null){
					addActionMessage("供应商台账中没有该供应商的信息请先添加!");
					return SUCCESS;
				}else{
					supplierId = supplier.getId();
				}
			}else{
				ActionContext.getContext().put("error","supplier");
				addActionMessage("请选择供应商！");
			}
		}
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
				estimateModelId = -2l;
				
				List<Map<String,Object>> datas = evaluateManager.getRealPointsByEstimateModel(estimateModel, supplier, evaluateYear,materialType);
				ActionContext.getContext().put("datas",JSONArray.fromObject(datas).toString());
			}
		}
		return SUCCESS;
	}
	
	@Action("ledger-datas")
	public String getEvaluates() throws Exception {
		try{
			page = evaluateManager.search(page,supplierId);
		}catch (Exception e) {
			logger.error(e);
			addActionMessage("查询失败："+e.getMessage());
		}
		renderText(PageUtils.pageToJson(page));
		return null;
	}
	/**
	 * 导出
	 * @return
	 * @throws Exception 
	 */
	@Action("exports")
	public String exports() throws Exception {
		page = evaluateManager.search(new Page<Evaluate>(Integer.MAX_VALUE),supplierId);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"SUPPLIER_EVALUATE"),"历史评价台账"));
		return null;
	}
	@Action("evaluate-total-table")
	public String getEvaluateTotalTable() throws Exception{
		ActionContext.getContext().put("error","");
		ActionContext.getContext().put("estimateModelMaps","[]");
		ActionContext.getContext().put("datas","[]");
		selectMaterialType();
		if(supplierId != null){
			supplier = supplierManager.getSupplier(supplierId);
		}else{
			Department departMent1 =  acsUtils.getDepartmentById(ContextUtils.getDepartmentId());
			if(departMent1 != null &&departMent1.getParent()!=null&&departMent1.getParent().getName().equals("Suppliers")){//不是海利达人员
				supplier = supplierManager.getSupplierByName(departMent1.getName());
				if(supplier==null){
					addActionMessage("供应商台账中没有该供应商的信息请先添加!");
					ActionContext.getContext().put("error","供应商台账中没有该供应商的信息请先添加!");
					return SUCCESS;
				}else{
					supplierId = supplier.getId();
				}
			}else{
				ActionContext.getContext().put("error","请选择供应商！");
				addActionMessage("请选择供应商！");
			}
		}
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
				estimateModelId = -3l;
				
				List<ModelIndicator> modelIndicators = estimateModel.getAllModelIndicators();
				List<EvaluateDetail> evaluateDetails = convertIndicatorToEvaluateDetails(modelIndicators);
				Map<String,String> scoreMap = evaluateManager.caculateRealPointsByEvaluateDetails(evaluateDetails, supplier, evaluateYear, estimateModel.getName(),materialType);
				ActionContext.getContext().put("modelObj",createModelTableByEvaluateForTotal(evaluateDetails,scoreMap));
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 根据模型创建指标的表格
	 * @param estimateModel
	 * @return
	 */
	private String createModelTableByEvaluate(List<EvaluateDetail> evaluateDetails,JSONObject autoGradeMap){
		StringBuffer modelIndicatorIds = new StringBuffer("");
		Double allTotalPoints = 0.0,realTotalPoints=0.0;
		StringBuffer sb = new StringBuffer("");
		sortDetail(evaluateDetails);
		for(EvaluateDetail evaluateDetail : evaluateDetails){
			ModelIndicator modelInticator = null;
			EvaluatingIndicator indicator = null;
			if(evaluateDetail.getEvaluatingIndicatorId()==null){
				modelInticator = modelIndicatorManager.getModelIndicator(evaluateDetail.getModelIndicatorId());
				indicator = modelInticator.getEvaluatingIndicator();
			}else{
				indicator = evaluatingIndicatorManager.getEvaluatingIndicator(evaluateDetail.getEvaluatingIndicatorId());
			}
			sb.append("<tr>");
			if(evaluateDetail.getFirstColHtml() != null){
				sb.append(evaluateDetail.getFirstColHtml());
				sb.append("<input type=\"hidden\"  name=\"params.firstColHtml_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+evaluateDetail.getFirstColHtml()+"\"/>");
			}
			if(evaluateDetail.getSecondColHtml() != null){
				sb.append(evaluateDetail.getSecondColHtml());
				sb.append("<input type=\"hidden\" name=\"params.secondColHtml_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+evaluateDetail.getSecondColHtml()+"\"/>");
			}
			if(evaluateDetail.getThirdColHtml() != null){
				sb.append(evaluateDetail.getThirdColHtml());
				sb.append("<input type=\"hidden\" name=\"params.thirdColHtml_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+evaluateDetail.getThirdColHtml()+"\"/>");
			}
			sb.append("<input type=\"hidden\" name=\"params.evaluatingIndicatorId_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+indicator.getId()+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.orderByNum_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+indicator.getOrderByNum()+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.id_"+evaluateDetail.getModelIndicatorId()+"\" value=\"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.name_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+evaluateDetail.getName()+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.totalPoints_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+evaluateDetail.getTotalPoints()+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getScore()==null?"":evaluateDetail.getScore())+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.remark_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getRemark()==null?"":evaluateDetail.getRemark())+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.levela_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getLevela()==null?"":evaluateDetail.getLevela())+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.levelaMin_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getLevelaMin()==null?"":evaluateDetail.getLevelaMin())+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.levelaMax_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getLevelaMax()==null?"":evaluateDetail.getLevelaMax())+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.levelb_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getLevelb()==null?"":evaluateDetail.getLevelb())+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.levelbMin_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getLevelbMin()==null?"":evaluateDetail.getLevelbMin())+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.levelbMax_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getLevelbMax()==null?"":evaluateDetail.getLevelbMax())+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.levelc_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getLevelc()==null?"":evaluateDetail.getLevelc())+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.levelcMin_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getLevelcMin()==null?"":evaluateDetail.getLevelcMin())+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.levelcMax_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getLevelcMax()==null?"":evaluateDetail.getLevelcMax())+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.leveld_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getLeveld()==null?"":evaluateDetail.getLeveld())+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.leveldMin_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getLeveldMin()==null?"":evaluateDetail.getLeveldMin())+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.leveldMax_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getLeveldMax()==null?"":evaluateDetail.getLeveldMax())+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.levele_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getLevele()==null?"":evaluateDetail.getLevele())+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.leveleMin_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getLeveleMin()==null?"":evaluateDetail.getLeveleMin())+"\"/>");
			sb.append("<input type=\"hidden\" name=\"params.leveleMax_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getLeveleMax()==null?"":evaluateDetail.getLeveleMax())+"\"/>");
			sb.append("<td>"+CommonUtil.retainNDigits(evaluateDetail.getTotalPoints(),0)+"</td>");
			@SuppressWarnings("unused")
			double min=0,max=1;
			List<Double> values = new ArrayList<Double>();
			if(evaluateDetail.getTotalPoints()!=null){
				values.add(evaluateDetail.getTotalPoints());
			}
			if(evaluateDetail.getLevelaMin() != null){
				values.add(evaluateDetail.getLevelaMin());
			}
			if(evaluateDetail.getLevelaMax() != null){
				values.add(evaluateDetail.getLevelaMax());
			}
			if(evaluateDetail.getLevelbMin() != null){
				values.add(evaluateDetail.getLevelbMin());
			}
			if(evaluateDetail.getLevelbMax() != null){
				values.add(evaluateDetail.getLevelbMax());
			}
			if(evaluateDetail.getLevelcMin() != null){
				values.add(evaluateDetail.getLevelcMin());
			}
			if(evaluateDetail.getLevelcMax() != null){
				values.add(evaluateDetail.getLevelcMax());
			}
			if(evaluateDetail.getLeveldMin() != null){
				values.add(evaluateDetail.getLeveldMin());
			}
			if(evaluateDetail.getLeveldMax() != null){
				values.add(evaluateDetail.getLeveldMax());
			}
			if(evaluateDetail.getLeveleMin() != null){
				values.add(evaluateDetail.getLeveleMin());
			}
			if(evaluateDetail.getLeveleMax() != null){
				values.add(evaluateDetail.getLeveleMax());
			}
			Collections.sort(values,new Comparator<Double>() {
				@Override
				public int compare(Double o1, Double o2) {
					if(o1<o2){
						return 0;
					}else{
						return 1;
					}
				}
			});
			if(values.size()>0){
				max = values.get(values.size()-1);
				if(values.get(0)<min){
					min = values.get(0);
				}
			}
			//根据指标实现实绩与得分是否只读
//			EvaluatingIndicator indicator = evaluatingIndicatorManager.getEvaluatingIndicatorByModelIndicatorId(evaluateDetail.getModelIndicatorId());
			if(null == indicator.getReadonly() || !indicator.getReadonly()){
				if(evaluateDetail.getName().contains("配合度等级" )||evaluateDetail.getName().contains("配合等级" )){
					if(evaluateDetail.getScore()==null){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option> <option value=\"1.0\">优</option><option value=\"2.0\">中</option><option value=\"3.0\">差</option></select></td>");
					}else if(evaluateDetail.getScore()==1.0){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option> <option value=\"1.0\" selected='selected' >优</option><option value=\"2.0\">中</option><option value=\"3.0\">差</option></select></td>");
					}else if(evaluateDetail.getScore()==2.0){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option> <option value=\"1.0\" >优</option><option selected='selected' value=\"2.0\">中</option><option value=\"3.0\">差</option></select></td>");
					}else if(evaluateDetail.getScore()==3.0){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option> <option value=\"1.0\" >优</option><option value=\"2.0\">中</option><option selected='selected'  value=\"3.0\">差</option></select></td>");
					}
					sb.append("<td><input type=\"text\"   readonly=\"readonly\" realTotalPoint=true name=\"params.realTotalPoints_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getRealTotalPoints()==null?"":CommonUtil.retainNDigits(evaluateDetail.getRealTotalPoints(), 3))+"\" style=\"width:40px;background-color:#eeeeee;\"/></td>");
				}else if("交货方式".equals(evaluateDetail.getName())){
					if(evaluateDetail.getScore()==null){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option> <option value=\"0.0\" >工厂交货模式(EXW)</option> <option value=\"1.0\">供应商F系列</option><option value=\"2.0\">供应商C系列</option><option value=\"3.0\">供应商D系列</option></select></td>");
					}else if(evaluateDetail.getScore()==1.0){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option> <option value=\"0.0\">工厂交货模式(EXW)</option> <option value=\"1.0\" selected='selected'>供应商F系列</option><option value=\"2.0\">供应商C系列</option><option value=\"3.0\">供应商D系列</option></select></td>");
					}else if(evaluateDetail.getScore()==2.0){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option>  <option value=\"0.0\">工厂交货模式(EXW)</option> <option value=\"1.0\">供应商F系列</option><option value=\"2.0\" selected='selected'>供应商C系列</option><option value=\"3.0\">供应商D系列</option></select></td>");
					}else if(evaluateDetail.getScore()==3.0){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option>  <option value=\"0.0\">工厂交货模式(EXW)</option> <option value=\"1.0\">供应商F系列</option><option value=\"2.0\">供应商C系列</option><option value=\"3.0\" selected='selected'>供应商D系列</option></select></td>");
					}else if(evaluateDetail.getScore()==0.0){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(),3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option>  <option value=\"0.0\" selected='selected'>工厂交货模式(EXW)</option> <option value=\"1.0\">供应商F系列</option><option value=\"2.0\">供应商C系列</option><option value=\"3.0\">供应商D系列</option></select></td>");
					}
					sb.append("<td><input type=\"text\"   readonly=\"readonly\" realTotalPoint=true name=\"params.realTotalPoints_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getRealTotalPoints()==null?"":CommonUtil.retainNDigits(evaluateDetail.getRealTotalPoints(), 3))+"\" style=\"width:40px;background-color:#eeeeee;\"/></td>");
				}else if("付款条件".equals(evaluateDetail.getName())){
					if(evaluateDetail.getScore()==null){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option> <option value=\"0.0\" >RMB:当月结</option> <option value=\"1.0\">RMB:30-90天或WB:30-60天</option><option value=\"2.0\">RMB:90-180天或WB:60-90天</option><option value=\"3.0\">RMB:180天以上或WB:90天以上</option></select></td>");
					}else if(evaluateDetail.getScore()==1.0){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option> <option value=\"0.0\">RMB:当月结</option> <option value=\"1.0\" selected='selected'>RMB:30-90天或WB:30-60天</option><option value=\"2.0\">RMB:90-180天或WB:60-90天</option><option value=\"3.0\">RMB:180天以上或WB:90天以上</option></select></td>");
					}else if(evaluateDetail.getScore()==2.0){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option>  <option value=\"0.0\">RMB:当月结</option> <option value=\"1.0\">RMB:30-90天或WB:30-60天</option><option value=\"2.0\" selected='selected'>RMB:90-180天或WB:60-90天</option><option value=\"3.0\">RMB:180天以上或WB:90天以上</option></select></td>");
					}else if(evaluateDetail.getScore()==3.0){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option>  <option value=\"0.0\">RMB:当月结</option> <option value=\"1.0\">RMB:30-90天或WB:30-60天</option><option value=\"2.0\">RMB:90-180天或WB:60-90天</option><option value=\"3.0\" selected='selected'>RMB:180天以上或WB:90天以上</option></select></td>");
					}else if(evaluateDetail.getScore()==0.0){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option>  <option value=\"0.0\" selected='selected'>RMB:当月结</option> <option value=\"1.0\">RMB:30-90天或WB:30-60天</option><option value=\"2.0\">RMB:90-180天或WB:60-90天</option><option value=\"3.0\">RMB:180天以上或WB:90天以上</option></select></td>");
					}
					sb.append("<td><input type=\"text\"   readonly=\"readonly\" realTotalPoint=true name=\"params.realTotalPoints_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getRealTotalPoints()==null?"":CommonUtil.retainNDigits(evaluateDetail.getRealTotalPoints(), 3))+"\" style=\"width:40px;background-color:#eeeeee;\"/></td>");
				}
				/*else if("超额运费".equals(evaluateDetail.getName())){
					if(evaluateDetail.getScore()==null){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option> <option value=\"0.0\" >运输方式有更改</option> <option value=\"1.0\">运输方式无更改</option><option value=\"2.0\">有分批交货</option><option value=\"3.0\">无分批交货</option><option value=\"4.0\">有五批以上供应材料更换</option><option value=\"5.0\">无五批以上供应材料更换</option></select></td>");
					}else if(evaluateDetail.getScore()==0.0){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option></option> <option value=\"0.0\" selected='selected'>运输方式有更改</option> <option value=\"1.0\">运输方式无更改</option><option value=\"2.0\">有分批交货</option><option value=\"3.0\">无分批交货</option><option value=\"4.0\">有五批以上供应材料更换</option><option value=\"5.0\">无五批以上供应材料更换</option></select></td>");
					}else if(evaluateDetail.getScore()==1.0){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option> </option> <option value=\"0.0\" >运输方式有更改</option> <option value=\"1.0\" selected='selected'>运输方式无更改</option><option value=\"2.0\">有分批交货</option><option value=\"3.0\">无分批交货</option><option value=\"4.0\">有五批以上供应材料更换</option><option value=\"5.0\">无五批以上供应材料更换</option></select></td>");
					}else if(evaluateDetail.getScore()==2.0){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option></option> <option value=\"0.0\" >运输方式有更改</option> <option value=\"1.0\">运输方式无更改</option><option value=\"2.0\" selected='selected'>有分批交货</option><option value=\"3.0\">无分批交货</option><option value=\"4.0\">有五批以上供应材料更换</option><option value=\"5.0\">无五批以上供应材料更换</option></select></td>");
					}else if(evaluateDetail.getScore()==3.0){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option></option> <option value=\"0.0\" >运输方式有更改</option> <option value=\"1.0\">运输方式无更改</option><option value=\"2.0\">有分批交货</option><option value=\"3.0\" selected='selected'>无分批交货</option><option value=\"4.0\">有五批以上供应材料更换</option><option value=\"5.0\">无五批以上供应材料更换</option></select></td>");
					}else if(evaluateDetail.getScore()==4.0){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option></option> <option value=\"0.0\" >运输方式有更改</option> <option value=\"1.0\">运输方式无更改</option><option value=\"2.0\">有分批交货</option><option value=\"3.0\">无分批交货</option><option value=\"4.0\" selected='selected'>有五批以上供应材料更换</option><option value=\"5.0\">无五批以上供应材料更换</option></select></td>");
					}else if(evaluateDetail.getScore()==5.0){
						sb.append("<td><select onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,false);\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\"  name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\"><option value=\"\"></option></option> <option value=\"0.0\" >运输方式有更改</option> <option value=\"1.0\">运输方式无更改</option><option value=\"2.0\">有分批交货</option><option value=\"3.0\">无分批交货</option><option value=\"4.0\">有五批以上供应材料更换</option><option value=\"5.0\" selected='selected'>无五批以上供应材料更换</option></select></td>");
					}
					sb.append("<td><input type=\"text\"   readonly=\"readonly\" realTotalPoint=true name=\"params.realTotalPoints_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getRealTotalPoints()==null?"":CommonUtil.retainNDigits(evaluateDetail.getRealTotalPoints(), 3))+"\" style=\"width:40px;background-color:#eeeeee;\"/></td>");
				}*/else{
					String remark = indicator.getRemark()==null?"''":indicator.getRemark();
					if(indicator.getRemark()!=null){
						sb.append("<td><input type=\"text\" onchange=\"scortChangeRemark("+evaluateDetail.getModelIndicatorId()+",this);\"  score=true name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\" class=\"{number:true}\"/>"+(evaluateDetail.getUnit()==null?"":evaluateDetail.getUnit())+"</td>");
						sb.append("<td><input type=\"text\" realTotalPoint=true name=\"params.realTotalPoints_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getRealTotalPoints()==null?"":CommonUtil.retainNDigits(evaluateDetail.getRealTotalPoints(), 3))+"\" style=\"width:40px;\"/><input type=\"hidden\"  name=\"params.formula_"+evaluateDetail.getModelIndicatorId()+"\" value="+remark+" /></td>");
					}else{
						sb.append("<td><input type=\"text\" onchange=\"scortChangeObj("+evaluateDetail.getModelIndicatorId()+",this,true);\" score=true name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;\" class=\"{number:true}\"/>"+(evaluateDetail.getUnit()==null?"":evaluateDetail.getUnit())+"</td>");
						sb.append("<td><input type=\"text\" realTotalPoint=true name=\"params.realTotalPoints_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getRealTotalPoints()==null?"":CommonUtil.retainNDigits(evaluateDetail.getRealTotalPoints(), 3))+"\" style=\"width:40px;\"/><input type=\"hidden\"  name=\"params.script_"+evaluateDetail.getModelIndicatorId()+"\"  /></td>");
					}
					
				}
				
			}else{
				sb.append("<td><input type=\"text\" readonly=\"readonly\"  score=true name=\"params.score_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getScore()==null?"":CommonUtil.retainNDigits(evaluateDetail.getScore(), 3))+"\" style=\"width:40px;background-color:#eeeeee;\" class=\"{number:true}\"/>"+(evaluateDetail.getUnit()==null?"":evaluateDetail.getUnit())+"</td>");
				sb.append("<td><input type=\"text\"   readonly=\"readonly\" realTotalPoint=true name=\"params.realTotalPoints_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+(evaluateDetail.getRealTotalPoints()==null?"":CommonUtil.retainNDigits(evaluateDetail.getRealTotalPoints(), 3))+"\" style=\"width:40px;background-color:#eeeeee;\"/></td>");
			}
//			sb.append("<input type=\"hidden\" readonly=\"readonly\"   name=\"params.orderByNum_"+evaluateDetail.getModelIndicatorId()+"\" value=\""+indicator.getOrderByNum()+"\" />");
			StringBuffer remark = new StringBuffer(evaluateDetail.getRemark()==null?"":evaluateDetail.getRemark());
			JSONArray gradeRules = new JSONArray();
			if(indicator != null&&!indicator.getGradeRules().isEmpty()){
				for(EvaluatingGradeRule gradeRule : indicator.getGradeRules()){
					JSONObject ruleMap = new JSONObject();
					ruleMap.put("start",gradeRule.getStart()==null?Double.MIN_VALUE:gradeRule.getStart());
					ruleMap.put("end",gradeRule.getToend()==null?Double.MAX_VALUE:gradeRule.getToend());
					ruleMap.put("fee",gradeRule.getFee());
					gradeRules.add(ruleMap);
					if(remark.length()>0){
						remark.append("\n");
					}
					String unit = indicator.getUnit()==null?"":indicator.getUnit();
					String SJ = "实绩";
					String DE = "得";
					String FEN = "分";
					String and = "并且";
					if(gradeRule.getStart()==null){
						//remark.append("●" + SJ + "<" + CommonUtil.retainNDigits(gradeRule.getToend(), 0) + unit + DE + CommonUtil.retainNDigits(gradeRule.getFee(), 0) + FEN);
						if("超额运费1".equals(evaluateDetail.getName())||"付款条件".equals(evaluateDetail.getName())||"交货方式".equals(evaluateDetail.getName())||evaluateDetail.getName().contains("配合度等级" )||evaluateDetail.getName().contains("配合等级" )){
							remark.append("●等级:" + gradeRule.getLevel() + DE + gradeRule.getFee() + FEN);
						}else{
							remark.append("●" +  SJ + "<" + gradeRule.getToend() + unit + DE + gradeRule.getFee() + FEN);
						}
						
					}else if(gradeRule.getToend() == null){
						if("超额运费1".equals(evaluateDetail.getName())||"付款条件".equals(evaluateDetail.getName())||"交货方式".equals(evaluateDetail.getName())||evaluateDetail.getName().contains("配合度等级" )||evaluateDetail.getName().contains("配合等级" )){
							remark.append("●等级:" + gradeRule.getLevel() + DE + gradeRule.getFee() + FEN);
						}else{
							remark.append("●" + SJ+ ">=" + gradeRule.getStart() + unit + DE + gradeRule.getFee() + FEN);
						}
						//remark.append("●" + SJ + ">=" + CommonUtil.retainNDigits(gradeRule.getStart(), 0) + unit + DE + CommonUtil.retainNDigits(gradeRule.getFee(), 0) + FEN);
						
					}else{
						if("超额运费1".equals(evaluateDetail.getName())||"付款条件".equals(evaluateDetail.getName())||"交货方式".equals(evaluateDetail.getName())||evaluateDetail.getName().contains("配合度等级" )||evaluateDetail.getName().contains("配合等级" )){
							remark.append("●等级:" + gradeRule.getLevel() + DE + gradeRule.getFee() + FEN);
						}else{
							remark.append("●" + SJ+ ">=" + gradeRule.getStart() + unit + and + "<" + gradeRule.getToend() + unit + DE + gradeRule.getFee() + FEN);
						}
						//remark.append("●" + SJ + ">=" + CommonUtil.retainNDigits(gradeRule.getStart(), 0) + unit + and + "<" + CommonUtil.retainNDigits(gradeRule.getToend(), 0) + unit + DE + CommonUtil.retainNDigits(gradeRule.getFee(), 0) + FEN);
						
					}
				}
			}
			sb.append("<td style=\"text-align:left;padding-left:2px;\">"+remark+"</td>");
			sb.append("</tr>");
			if(evaluateDetail.getTotalPoints() != null){
				allTotalPoints += evaluateDetail.getTotalPoints();
			}
			realTotalPoints += evaluateDetail.getRealTotalPoints()==null?0:evaluateDetail.getRealTotalPoints();
			if(modelIndicatorIds.length()>0){
				modelIndicatorIds.append(",");
			}
			modelIndicatorIds.append(evaluateDetail.getModelIndicatorId());
			if(indicator != null){
				JSONObject indicatorMap = new JSONObject();
				indicatorMap.put("id",indicator.getId());
				indicatorMap.put("modelIndicatorId",evaluateDetail.getModelIndicatorId());
				indicatorMap.put("dataSourceCode",indicator.getDataSourceCode());
				indicatorMap.put("name",indicator.getName());
				indicatorMap.put("gradeRules",gradeRules);
				autoGradeMap.put(evaluateDetail.getModelIndicatorId(),indicatorMap);
			}
		}
		sb.append("<tr>");
		sb.append("<input type=\"hidden\" name=\"totalPoints\" id=\"totalPoints\" value=\""+allTotalPoints+"\"/>");
		sb.append("<input type=\"hidden\" name=\"params.modelIndicatorIds\" value=\""+modelIndicatorIds.toString()+"\"/>");
		sb.append("<td colspan=\"3\">"+"总分"+"</td>");
		sb.append("<td>"+CommonUtil.retainNDigits(allTotalPoints,3)+"</td>");
		sb.append("<td>&nbsp;</td>");
		sb.append("<td><span id=\"realTotalPoints\">"+CommonUtil.retainNDigits(realTotalPoints, 3)+"</span></td>");
		sb.append("<td>&nbsp;</td>");
		sb.append("</tr>");
		return sb.toString();
	}
	/**
	 * 根据模型创建指标的表格
	 * @param estimateModel
	 * @return
	 */
	private String createModelTableByEvaluateForTotal(List<EvaluateDetail> evaluateDetails,Map<String,String> scoreMap){
		StringBuffer sb = new StringBuffer("");
		Double totalPoints = 0.0;
		for(EvaluateDetail evaluateDetail : evaluateDetails){
			sb.append("<tr>");
			if(evaluateDetail.getFirstColHtml() != null){
				sb.append(evaluateDetail.getFirstColHtml());
			}
			if(evaluateDetail.getSecondColHtml() != null){
				sb.append(evaluateDetail.getSecondColHtml());
			}
			if(evaluateDetail.getThirdColHtml() != null){
				sb.append(evaluateDetail.getThirdColHtml());
			}
			totalPoints += evaluateDetail.getTotalPoints();
			sb.append("<td>"+CommonUtil.retainNDigits(evaluateDetail.getTotalPoints(),0)+"</td>");
			sb.append(scoreMap.get(evaluateDetail.getName()));
			sb.append("</tr>");
		}
		sb.append("<tr>")
		.append("<td colspan=3>合计</td>")
		.append("<td>"+CommonUtil.retainNDigits(totalPoints,0)+"</td>")
		.append(scoreMap.get("custom_total"))
		.append("</tr>");
		return sb.toString();
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
	@Action("load-data-source")
	public String getScoreByDataSource() throws Exception{
		try {
			String dataSourceCode = Struts2Utils.getParameter("dataSourceCode");
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR,evaluateYear);
			calendar.set(Calendar.MONTH,evaluateMonth-1);
			calendar.set(Calendar.DATE,1);
			calendar.set(Calendar.HOUR_OF_DAY,0);
			calendar.set(Calendar.MINUTE,0);
			calendar.set(Calendar.SECOND,0);
			calendar.set(Calendar.MILLISECOND,0);
			Date startDate = calendar.getTime();
			Map<String,Integer> cycleMap = EstimateModel.getCycleMap();
			int interval = 1;
			String cycle = Struts2Utils.getParameter("cycle");
			if(cycleMap.containsKey(cycle)){
				interval = cycleMap.get(cycle);
			}
			calendar.add(Calendar.MONTH,interval);
			calendar.add(Calendar.MILLISECOND,-1);
			Date endDate = calendar.getTime();
			Map<String,Object> number = dataSourceManager.executeEvaluateDataSource(dataSourceCode, Struts2Utils.getParameter("supplierCode"), startDate, endDate,materialType);
			this.renderText(JSONObject.fromObject(number).toString());
		} catch (Exception e) {
			createErrorMessage("获取数据来源失败:" + e.getMessage());
			logger.error("自动评分失败",e);
		}
		return null;
	}
	/**
	 * map
	 * @return
	 */
	private List<Map<String,Object>> createOtherMap(){
		List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
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
		
	/*	map = new HashMap<String, Object>();
		map.put("data","供应商评价排名");
		attrMap = new HashMap<String, Object>();
		attrMap.put("id",-4);
		attrMap.put("name","供应商评价排名");
		attrMap.put("customType","point-rank");
		attrMap.put("isLeaf",true);
		map.put("attr",attrMap);这个移动到评介总汇
		maps.add(map);*/
		
		map = new HashMap<String, Object>();
		map.put("data","历史评价台账");
		attrMap = new HashMap<String, Object>();
		attrMap.put("id",-2);
		attrMap.put("name","历史评价台账");
		attrMap.put("customType","ledger");
		attrMap.put("isLeaf",true);
		map.put("attr",attrMap);
		maps.add(map);
		
		return maps;
	}
	
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	private void createMessage(String message,Long id){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		map.put("id",id);
		renderText(JSONObject.fromObject(map).toString());
	}
	
	/**
	 * 供应商评价得分总表详情
	 * @return
	 * @throws Exception
	 */
	@Action("evaluate-detail")
    public String getEvaluateDetails() throws Exception{
        params = CommonUtil.convertJsonObject(params);
        return SUCCESS;
    }
	
	/**
	 * 供应商评价得分总表详情数据
	 * @return
	 * @throws Exception
	 */
	@Action("evaluate-detail-datas")
    public String getEvaluateDetailDatas() throws Exception{
        try{
        	params = JSONObject.fromObject(Struts2Utils.getParameter("searchStrs"));
        	String type = params.getString("type");
    		page = new Page<Evaluate>();
//    		page = evaluateManager.queryEvaluateDetail(page, params);
    		renderText(PageUtils.pageToJson(page));
        }catch(Exception e){
        	logger.error("供应商评价得分总表详情数据",e);
            createErrorMessage(e.getMessage());
        }
        return null;
    }

	public String getSqedep() {
		return sqedep;
	}

	public void setSqedep(String sqedep) {
		this.sqedep = sqedep;
	}
	

	/**
	 * 方法名: SupplierEvaluateSettings
	 * <p>功能说明：手动评价所有供应商业绩</p>
	 * @return
	 * @throws Exception
	 */
	@Action("supplier-evaluate-settings-erp")
	public String supplierEvaluateSettings() throws Exception {
		try {
//			supplierEvaluateIntegrationService.beginIntegration(ContextUtils.getCompanyId(),ContextUtils.getLoginName());
			createMessage("所有供应商业绩评价成功!");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("所有供应商业绩评价失败!",e);
			createErrorMessage("所有供应商业绩评价失败：" + e.getMessage());
		}
		return null;
	}
}

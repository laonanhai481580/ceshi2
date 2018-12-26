package com.ambition.supplier.evaluate.service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.archives.dao.SupplierDao;
import com.ambition.supplier.archives.dao.SupplierGoalDao;
import com.ambition.supplier.archives.service.SupplierCancleDatasManager;
import com.ambition.supplier.archives.service.SupplierManager;
import com.ambition.supplier.baseInfo.dao.SupplierLevelChangeDao;
import com.ambition.supplier.baseInfo.dao.SupplierLevelScoreDao;
import com.ambition.supplier.entity.EstimateModel;
import com.ambition.supplier.entity.Evaluate;
import com.ambition.supplier.entity.EvaluateDetail;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.entity.SupplierCancleDatas;
import com.ambition.supplier.entity.SupplierEvaluateTotal;
import com.ambition.supplier.entity.SupplierGoal;
import com.ambition.supplier.entity.SupplierLevelChange;
import com.ambition.supplier.entity.SupplierLevelScore;
import com.ambition.supplier.entity.SupplierQcds;
import com.ambition.supplier.entity.WarnSign;
import com.ambition.supplier.estimate.service.EstimateModelManager;
import com.ambition.supplier.evaluate.dao.EvaluateDao;
import com.ambition.supplier.evaluate.dao.SupplierEvaluateTotalDao;
import com.ambition.supplier.manager.service.SupplierQcdsManager;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.common.DateUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Service
@Transactional
public class EvaluateManager {
	
	@Autowired
	private SupplierManager supplierManager;
	@Autowired
	private SupplierLevelScoreDao supplierLevelScoreDao;
	@Autowired
	private SupplierEvaluateTotalDao supplierEvaluateTotalDao;
	@Autowired
	private SupplierCancleDatasManager supplierCancleDatasManager;
	@Autowired
	private SupplierLevelChangeDao supplierLevelChangeDao;
	/**比较符号字段**/
    private final Map<String, String> compareMap = new HashMap<String, String>();
    /**对象数据字段*/
    //private final Map<String, String> fieldMap = new HashMap<String, String>();
	/**对象数据字段*/
    private final Map<String, String> evaluateFieldMap = new HashMap<String, String>();
    /**对象数据字段*/
    private final Map<String, String> nCReportFieldMap = new HashMap<String, String>();
    /**对象数据字段*/
    private final Map<String, String> improve8dReportFieldMap = new HashMap<String, String>();
    
    public EvaluateManager() {
    	//初始化比较操作字符
        compareMap.put("equals", "=");
        compareMap.put("ge", ">=");
        compareMap.put("gt", ">");
        compareMap.put("le", "<=");
        compareMap.put("lt", "<");
        compareMap.put("like", "like");
        compareMap.put("in", "in");
        
    	//供应商评价指标
        evaluateFieldMap.put("startDate", "created_time");
        evaluateFieldMap.put("endDate", "created_time");
        evaluateFieldMap.put("estimateModelId", "estimate_model_id");
        evaluateFieldMap.put("estimateModelName", "estimate_model_name");
        evaluateFieldMap.put("supplierCode", "supplier_code");
        evaluateFieldMap.put("supplierName", "supplier_name");
        evaluateFieldMap.put("evaluateDate", "evaluate_date");
        
        evaluateFieldMap.put("evaluateDetails.modelIndicatorId", "model_indicator_id");
        evaluateFieldMap.put("evaluateDetails.parentName", "parent_name");
        evaluateFieldMap.put("evaluateDetails.name", "name");
        
        //NCR报告字段
        nCReportFieldMap.put("startDate", "created_time");
        nCReportFieldMap.put("endDate", "created_time");
        nCReportFieldMap.put("ontimeState", "ontime_state");
        nCReportFieldMap.put("deptName", "dept_name");
        nCReportFieldMap.put("sourceOfComplaint", "source_of_complaint");
        nCReportFieldMap.put("supplierCode", "supplier_code");
        nCReportFieldMap.put("supplierName", "supplier_name");
        nCReportFieldMap.put("partNumber", "partNumber");
        nCReportFieldMap.put("affectedPartName", "affectedPartName");
        nCReportFieldMap.put("rev", "rev");
        nCReportFieldMap.put("poNo", "poNo");
        nCReportFieldMap.put("lotNo", "lotNo");
        nCReportFieldMap.put("supplierLotNo", "supplierLotNo");
        nCReportFieldMap.put("cavity", "cavity");
        
        //8D改善报告字段
        improve8dReportFieldMap.put("startDate", "a0sponsor_date");
        improve8dReportFieldMap.put("endDate", "a0sponsor_date");
        improve8dReportFieldMap.put("ontimeState", "ontime_state");
        improve8dReportFieldMap.put("deptName", "dept_name");
        improve8dReportFieldMap.put("a0Subject", "a0subject");
        improve8dReportFieldMap.put("a0Project", "a0project");
        improve8dReportFieldMap.put("a0SeriesNumber", "a0series_number");
        improve8dReportFieldMap.put("a0Customer", "a0customer");
        improve8dReportFieldMap.put("a0CustomerCode", "a0customer_code");
        improve8dReportFieldMap.put("a0OccurredSite", "a0occurred_site");
        improve8dReportFieldMap.put("a0BatchNumber", "a0batch_number");
        improve8dReportFieldMap.put("a0OccurredTime", "a0occurred_time");
        improve8dReportFieldMap.put("a0ProductsNumber", "a0products_number");
        improve8dReportFieldMap.put("a0Plant", "a0plant");
        improve8dReportFieldMap.put("a0LineNo", "a0line_no");
        improve8dReportFieldMap.put("a0Process", "a0process");
        improve8dReportFieldMap.put("a0PartNo", "a0part_no");
        improve8dReportFieldMap.put("a0PartName", "a0part_name");
        improve8dReportFieldMap.put("a0ResponsibleDept", "a0responsible_dept");
        improve8dReportFieldMap.put("supplierName", "a0responsible_dept");
        improve8dReportFieldMap.put("a0SponsorDate", "a0sponsor_date");
        improve8dReportFieldMap.put("b0NcType", "b0nc_type");
        improve8dReportFieldMap.put("b0DrawingNo", "b0drawing_no");
        improve8dReportFieldMap.put("b0Version", "b0version");
	}
    
	@Autowired
	private EvaluateDao evaluateDao;

	@Autowired
	private EstimateModelManager estimateModelManager;

	@Autowired
	private SupplierGoalDao supplierGoalDao;

	@Autowired
	private SupplierDao supplierDao;

	@Autowired
	private LogUtilDao logUtilDao;

	public Evaluate getEvaluate(Long id) {
		return evaluateDao.get(id);
	}

	/**
	 * 查询评价台帐
	 * 
	 * @param page
	 * @param supplierId
	 * @return
	 */
	public Page<Evaluate> search(Page<Evaluate> page, Long supplierId) {
		return evaluateDao.search(page, supplierId);
	}

	/**
	 * 根据供应商的年月返回对应的评价
	 * 
	 * @param supplier
	 * @param year
	 * @param month
	 * @return
	 */
	public Evaluate findEvaluateBySupplerAndYearMonth(Supplier supplier,
			Integer year, Integer month, Long estimateModelId,String materialType) {
		return evaluateDao.findEvaluateBySupplerAndYearMonth(supplier, year,
				month, estimateModelId,materialType);
	}
	/**
	 * 根据供应商的年月及评价周期返回对应的评价
	 * 
	 * @param supplier
	 * @param year
	 * @param month
	 * @return
	 */
	/*public Evaluate findEvaluateBySupplerAndYearMonthSQ(Supplier supplier,
			Integer year, Integer month, Long estimateModelId,String materialType,String cycle) {
		return evaluateDao.findEvaluateBySupplerAndYearMonthSQ(supplier, year,
				month, estimateModelId,materialType,cycle);
	}*/
	/**
	 * 保存供应商评价
	 * 
	 * @param evaluate
	 * @throws Exception
	 */
	public void saveEvaluate(Evaluate evaluate, JSONObject params)
			throws Exception {
		
		if(evaluate.getId()==null||"".equals(evaluate.getId())){
			Supplier supplier = supplierDao.getSupplierByName(evaluate.getSupplierName());
//			if(supplier!=null){
//				evaluate.setSqeDepartmentName(supplier.getWhetherProducers());
//			}
		}
		if (StringUtils.isEmpty(evaluate.getSupplierName())) {
			throw new RuntimeException("供应商名称不能为空！");
		}
		if (evaluate.getId() == null) {
			evaluate.setEvaluateDetails(new ArrayList<EvaluateDetail>());
		} else {
			evaluate.getEvaluateDetails().clear();
		}
		Double realTotalPoints = 0.0;
		params = convertJsonObject(params);
		String modelIndicatorIds[] = params.getString("modelIndicatorIds")
				.split(",");
		for (String modelIndicatorId : modelIndicatorIds) {
			if (StringUtils.isNotEmpty(modelIndicatorId)) {
				EvaluateDetail evaluateDetail = null;
				evaluateDetail = new EvaluateDetail();
				evaluateDetail.setEvaluate(evaluate);
//				evaluateDetail.setSqeDepartmentName(evaluate.getSqeDepartmentName());
				evaluateDetail.setCreatedTime(new Date());
				evaluateDetail.setCompanyId(ContextUtils.getCompanyId());
				evaluateDetail.setCreator(ContextUtils.getLoginName());
				evaluateDetail.setModifiedTime(new Date());
				evaluateDetail.setModifier(ContextUtils.getUserName());
				evaluateDetail.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
				evaluateDetail.setModelIndicatorId(Long.valueOf(modelIndicatorId));
				for (Object key : params.keySet()) {
					if (key.toString().endsWith("_" + modelIndicatorId)) {
						String pro = key.toString().split("_")[0];
						if (!"id".equals(pro)&&!"evaluatingIndicatorId".equals(pro)) {
							setProperty(evaluateDetail, pro, params.get(key));
						}
						if("evaluatingIndicatorId".equals(pro)){
							evaluateDetail.setEvaluatingIndicatorId(Long.valueOf(params.get(key).toString()));
						}
					}
				}
				evaluate.getEvaluateDetails().add(evaluateDetail);
				if (evaluateDetail.getRealTotalPoints() != null) {
					realTotalPoints += evaluateDetail.getRealTotalPoints();
				}
			}
		}
		evaluate.setRealTotalPoints(realTotalPoints);
		if (evaluate.getTotalPoints() < evaluate.getRealTotalPoints()) {
			throw new RuntimeException("实际得分不能大于总分");
		}
		String isSubmit = Struts2Utils.getParameter("isSubmit");
		if("是".equals(isSubmit)){
			evaluate.setIsSubmit(isSubmit);
		}
		evaluateDao.save(evaluate);
		// 计算总分
		Supplier supplier = supplierDao.get(evaluate.getSupplierId());
		
		if("是".equals(isSubmit)){
			calculateRealPointsBySupplier(supplier, supplier.getEstimateModelId(),
					evaluate);
		}
		
	}

	@SuppressWarnings("deprecation")
	private void calculateRealPointsBySupplier(Supplier supplier,
			Long estimateModelId, Evaluate evaluate) {
		String businessUnitCode = CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName());
		String hql = " from Evaluate e where e.supplierId=? and e.evaluateYear=? and e.evaluateMonth=? and e.materialType=? and e.isSubmit=?";
		List<Evaluate> lists = evaluateDao.find(hql,evaluate.getSupplierId(),evaluate.getEvaluateYear(),evaluate.getEvaluateMonth(),evaluate.getMaterialType(),"是");
		Double totalPoint = 0.0;
		if(lists.size()!=0){
			for(Evaluate e : lists){
				if(e.getRealTotalPoints()!=null){
					totalPoint += e.getRealTotalPoints();
				}
			}
		}
		EstimateModel estimateMode = estimateModelManager.getEstimateModel(evaluate.getEstimateModelId());
		EstimateModel parentModel = estimateMode.getParent();
		Integer modelSize = parentModel.getChildren().size();
		String isSubmit = Struts2Utils.getParameter("isSubmit");
		if("是".equals(isSubmit)){
			//判断供应商级别
			String hqlBase = " from SupplierLevelScore s where s.companyId=? and s.scoreStart<=? and s.scoreEnd>=?";
			List<SupplierLevelScore> scores =  supplierLevelScoreDao.find(hqlBase,ContextUtils.getCompanyId(),totalPoint,totalPoint);
			String grade = "";
			if(scores.size()!=0){
				SupplierLevelScore score = scores.get(0);
				grade = score.getAuditLevel();
			}
			//更新插入到评价总分表
			String hqlTotal = " from SupplierEvaluateTotal e where  e.supplierId=? and e.evaluateYear=? and e.evaluateMonth=? and e.materialType=? ";
			List<SupplierEvaluateTotal> totals =  supplierEvaluateTotalDao.find(hqlTotal,evaluate.getSupplierId(),evaluate.getEvaluateYear(),evaluate.getEvaluateMonth(),evaluate.getMaterialType());
			SupplierEvaluateTotal supplierEvaluateTotal = null;
			if(totals.size()==0){
				supplierEvaluateTotal = new SupplierEvaluateTotal();
				supplierEvaluateTotal.setCreatedTime(new Date());
				supplierEvaluateTotal.setCreator(ContextUtils.getLoginName());
				supplierEvaluateTotal.setCreatorName(ContextUtils.getUserName());
				supplierEvaluateTotal.setSupplierId(evaluate.getSupplierId());
				supplierEvaluateTotal.setSupplierName(evaluate.getSupplierName());
				supplierEvaluateTotal.setMaterialType(evaluate.getMaterialType());
				supplierEvaluateTotal.setEvaluateYear(evaluate.getEvaluateYear());
				supplierEvaluateTotal.setCycle(evaluate.getCycle());
				supplierEvaluateTotal.setEvaluateMonth(evaluate.getEvaluateMonth());
				supplierEvaluateTotal.setBusinessUnitCode(businessUnitCode);
				supplierEvaluateTotal.setEvaluateDate(evaluate.getEvaluateDate());
				supplierEvaluateTotal.setTotal(evaluate.getRealTotalPoints());
				if(lists.size()==modelSize){
					supplierEvaluateTotal.setGrade(grade);
				}
				supplierEvaluateTotalDao.save(supplierEvaluateTotal);
			}else{
				supplierEvaluateTotal = totals.get(0);
				supplierEvaluateTotal.setModifiedTime(new Date());
				supplierEvaluateTotal.setModifier(ContextUtils.getLoginName());
				supplierEvaluateTotal.setModifierName(ContextUtils.getUserName());
				supplierEvaluateTotal.setTotal(totalPoint);
				supplierEvaluateTotal.setEvaluateDate(evaluate.getEvaluateDate());
				if(lists.size()==modelSize){
					supplierEvaluateTotal.setGrade(grade);
				}
				supplierEvaluateTotalDao.save(supplierEvaluateTotal);
			}
			//判断连续被评为D C级的连续次数 ,转移供应商级别；必须所有评价项目都提交了才进行转移
			if(lists.size()==modelSize){
				String changeHql = " from SupplierLevelChange s where s.companyId=? and s.isStartUp=?";
				List<SupplierLevelChange> changes = supplierLevelChangeDao.find(changeHql,ContextUtils.getCompanyId(),"0");
				Integer evaluateMonth = supplierEvaluateTotal.getEvaluateMonth();
				Date evalueteDate = evaluate.getEvaluateDate();
				Integer timesD = null;Integer timesC = null;
				for(SupplierLevelChange supplierLevelChange : changes){
					if("D".equals(supplierLevelChange.getAuditLevel())){
						timesD = supplierLevelChange.getQuarters();
					}else if("C".equals(supplierLevelChange.getAuditLevel())){
						timesC = supplierLevelChange.getQuarters();
					}
				}
				Boolean canSetCancle = false;
				String hqlTotalBefore = " from SupplierEvaluateTotal e where  e.supplierId=? and e.evaluateYear=?  and e.materialType=? and e.grade=?";
				List<SupplierEvaluateTotal> totalBefore =  supplierEvaluateTotalDao.find(hqlTotalBefore,evaluate.getSupplierId(),supplierEvaluateTotal.getEvaluateYear(),evaluate.getMaterialType(),"D");
				SupplierCancleDatas cancleDatas = supplierCancleDatasManager.searchDatas(supplierEvaluateTotal);
				if(timesD!=null&&totalBefore.size()==timesD){
					canSetCancle = true;
					if(cancleDatas==null){
						cancleDatas = new SupplierCancleDatas();
						cancleDatas.setCreatedTime(new Date());
						cancleDatas.setCreatorName(ContextUtils.getUserName());
						cancleDatas.setCreator(ContextUtils.getLoginName());
						cancleDatas.setCycle(evaluate.getCycle());
						cancleDatas.setEvaluateYear(evaluate.getEvaluateYear());
						cancleDatas.setCompanyId(ContextUtils.getCompanyId());
						cancleDatas.setMaterialType(evaluate.getMaterialType());
						cancleDatas.setSupplierId(evaluate.getSupplierId());
						supplierDao.getSession().save(cancleDatas);
					}
				}else{
					if(cancleDatas!=null){
						supplierCancleDatasManager.deleteDatas(cancleDatas);
					}
				}
				Date beforeDate = evalueteDate;
				Boolean canSetD = false;
				hqlTotalBefore = " from SupplierEvaluateTotal e where  e.supplierId=? and e.evaluateYear=?  and e.materialType=? and e.grade=?";
				totalBefore =  supplierEvaluateTotalDao.find(hqlTotalBefore,evaluate.getSupplierId(),supplierEvaluateTotal.getEvaluateYear(),evaluate.getMaterialType(),"C");
				if(timesC!=null&&totalBefore.size()==timesC){
					canSetD = true;
				}
				if(canSetD){
					supplier.setState(Supplier.STATE_QUALIFIED);
					supplier.setSupplyGrade("D");
					supplier.setEstimateDegree("D");
				}else{
					supplier.setSupplyGrade(supplierEvaluateTotal.getGrade());
					supplier.setEstimateDegree(supplierEvaluateTotal.getGrade());
				}
			}
			
		}
		String stateStr = "";
		if("三个月".equals(evaluate.getCycle())){
			if(evaluate.getEvaluateMonth()==1){
				stateStr= "Q-1";
			}else if(evaluate.getEvaluateMonth()==4){
				stateStr="Q-2";
			}else if(evaluate.getEvaluateMonth()==7){
				stateStr="Q-3";
			}else if(evaluate.getEvaluateMonth()==10){
				stateStr="Q-4";
			}
			if("质量".equals(evaluate.getEstimateModelName())){
				supplier.setEvaluateState(evaluate.getEvaluateYear()+" "+stateStr+evaluate.getEstimateModelName()+"已评");
			}else if("采购".equals(evaluate.getEstimateModelName())){
				supplier.setEvaluatePurcheState(evaluate.getEvaluateYear()+" "+stateStr+evaluate.getEstimateModelName()+"已评");
			}else if("研发".equals(evaluate.getEstimateModelName())){
				supplier.setEvaluateDevelopState(evaluate.getEvaluateYear()+" "+stateStr+evaluate.getEstimateModelName()+"已评");
			}else if("物控调达".equals(evaluate.getEstimateModelName())){
				supplier.setEvaluateConsoleState(evaluate.getEvaluateYear()+" "+stateStr+evaluate.getEstimateModelName()+"已评");
			}
			
		}
		supplierDao.save(supplier);
	}

	/**
	 * 删除供应商评价
	 * 
	 * @param id
	 */
	public void deleteEvaluate(String deleteIds) {
		List<Supplier> suppliers = new ArrayList<Supplier>();
		String[] ids = deleteIds.split(",");
		List<Evaluate> evaluates = new ArrayList<Evaluate>();
		for (String id : ids) {
			/*Evaluate evaluate = evaluateDao.get(Long.valueOf(id));
			if (evaluate.getId() != null) {
				logUtilDao.debugLog("删除", evaluate.toString());
				evaluateDao.delete(evaluate);
				Supplier supplier = supplierDao.get(evaluate.getSupplierId());
				if (!suppliers.contains(supplier)) {
					suppliers.add(supplier);
				}
				evaluates.add(evaluate);
			}*/
			evaluateDao.delete(Long.valueOf(id));
		}
		// 计算总分
		/*String hql = "from WarnSign w where w.companyId = ?";
		List<WarnSign> warnSigns = evaluateDao.find(hql,
				ContextUtils.getCompanyId());
		for (Supplier supplier : suppliers) {
			calculateRealPointsBySupplier(supplier,
					supplier.getEstimateModelId(), null, warnSigns,supplier.getMaterialType());
		}*/
	}

	/**
	 * 查询总得分
	 * @param model
	 * @param supplier
	 * @param evaluateYear
	 * @return
	 */
	public List<Map<String, Object>> getRealPointsByEstimateModel(EstimateModel model, Supplier supplier, Integer evaluateYear,String materialType) {
		DecimalFormat df = new DecimalFormat("0");
		Map<String, Integer> cycleMap = EstimateModel.getCycleMap();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		List<Evaluate> evaluates = evaluateDao.findEvaluateBySupplierAndYear(
				supplier, evaluateYear, model.getId(),materialType);
		List<EstimateModel> modelChildren = getAllChildren(model);
		Map<String, Object> totalData = new HashMap<String, Object>();
		List<String> columnList = new ArrayList<String>();
		totalData.put("estimateModelName", "Total");
		for (EstimateModel childModel : modelChildren) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("estimateModelId", childModel.getId());
			data.put("estimateModelName", childModel.getName());
			int interval = 1;
			if (cycleMap.containsKey(childModel.getCycle())) {
				interval = cycleMap.get(childModel.getCycle());
			}
			Integer tempMonth = null, times = 0;
			StringBuffer sb = new StringBuffer(",");
			Double allTotalPoints = 0.0, realTotalPoints = 0.0;
			for (int i = 0; i < 12 / interval; i++) {
				if (tempMonth == null) {
					tempMonth = childModel.getStartMonth();
				} else {
					tempMonth += interval;
				}
				if (tempMonth > 12) {
					continue;
				}
				sb.append(tempMonth + ",");
			}
			Double[] num = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
			for (int i = 1; i < 13; i++) {
				if (sb.indexOf("," + i + ",") > -1) {
					Evaluate evaluate = null;
					for (Evaluate e : evaluates) {
						if (childModel.getId().toString()
								.equals(e.getEstimateModelId().toString())
								&& e.getEvaluateMonth() == i) {
							evaluate = e;
							break;
						}
					}
					if (evaluate == null) {
						data.put("month" + i, "未评价_" + i);
					} else {
						evaluates.remove(evaluate);
						data.put("month" + i, CommonUtil.retainNDigits(evaluate.getRealTotalPoints(),0)	+ "_" + evaluate.getId());
//						date.put("JD"+i,evaluate.getRealTotalPoints()+"_")
						if (evaluate.getRealTotalPoints() != null) {
							times++;
							realTotalPoints += evaluate.getRealTotalPoints();
							allTotalPoints += evaluate.getTotalPoints();
							if (totalData.containsKey("month" + i)) {
								totalData.put("month" + i,CommonUtil.retainNDigits(Double.valueOf(totalData.get("month" + i).toString()) + evaluate.getRealTotalPoints(),0));
							} else {
								columnList.add("month" + i);
								totalData.put("month" + i, CommonUtil.retainNDigits(evaluate.getRealTotalPoints(),0));
							}
						}
						num[i-1] = evaluate.getRealTotalPoints();
					}
				} else {
					data.put("month" + i, "no");
				}
				if(model.getName().equals("供应商季度评价")){
					if(i==1||i==4||i==7||i==10){
						int k = i - 1;
						if(i==1){
							k = 12;
						}
						data.put("JD"+k, CommonUtil.retainNDigits(num[i-1],0));
						if (totalData.containsKey("JD" + k)) {
							totalData.put("JD" + k,CommonUtil.retainNDigits(Double.valueOf(totalData.get("JD" + k).toString()) + num[i-1],0));
						} else {
							columnList.add("JD" + k);
							totalData.put("JD" + k, CommonUtil.retainNDigits(num[i-1],0));
						}
						data.put("month" + i, "");
						totalData.remove("month"+i);
					}
				}else{
					if(i==3||i==6||i==9||i==12){
						int k = 3;
						Double number = 0.0;
						for(int j = i - 3; j < i; j++){
							if(num[j]==0.0){
								k--;
							}
							number += num[j];
						}
						if(number!=0.0){
							number = Double.parseDouble(new DecimalFormat("#.00").format(number/k));
						}
						data.put("JD"+i, CommonUtil.retainNDigits(number,0));
						if (totalData.containsKey("JD" + i)) {
							String nums =  totalData.get("JD"+i).toString();
							Double numbers = Double.valueOf(nums)+number;
							totalData.put("JD"+i, CommonUtil.retainNDigits(numbers,0));
							/*if(!totalData.get("JD"+i).toString().equals("0")){
								totalData.put("JD" + i,CommonUtil.retainNDigits((Double) totalData.get("JD" + i) + number,0));
							}else{
								totalData.put("JD"+i, 0);
							}*/
						} else {
							columnList.add("JD" + i);
							totalData.put("JD" + i, CommonUtil.retainNDigits(number,0));
						}
					}
				}
				
			}
			if (times > 0) {
				data.put("allTotalPoints", allTotalPoints / times);
				data.put("realTotalPoints", realTotalPoints / times);
			} else {
				data.put("allTotalPoints", childModel.getTotalPoints());
				data.put("realTotalPoints", data.get("allTotalPoints"));
			}
			if (totalData.containsKey("allTotalPoints")) {
				totalData.put("allTotalPoints",
						(Double) totalData.get("allTotalPoints")
								+ (Double) data.get("allTotalPoints"));
				totalData.put("realTotalPoints",
						(Double) totalData.get("realTotalPoints")
								+ (Double) data.get("realTotalPoints"));
			} else {
				totalData.put("allTotalPoints", data.get("allTotalPoints"));
				totalData.put("realTotalPoints", data.get("realTotalPoints"));
			}
			data.put("allTotalPoints", df.format(data.get("allTotalPoints")));
			data.put("realTotalPoints", df.format(data.get("realTotalPoints")));
			datas.add(data);
		}
		if (datas.size() > 1) {
			Double allTotalPoints = (Double) totalData.get("allTotalPoints");
			Double realTotalPoints = (Double) totalData.get("realTotalPoints");
			if (allTotalPoints != null && allTotalPoints > 0) {
				totalData.put("realTotalPoints",
						df.format(realTotalPoints / allTotalPoints * 100));
				totalData.put("allTotalPoints", "100");
				for (String column : columnList) {
					if(totalData.containsKey(column)&&StringUtils.isNotEmpty(totalData.get(column).toString())){
						String columns = totalData.get(column).toString();
						realTotalPoints = Double.valueOf(columns) ;
					}else{
						realTotalPoints = 0.0;
					}
					if (realTotalPoints == null) {
						realTotalPoints = 0.0;
					}
					if((realTotalPoints / allTotalPoints * 100)==0.0){
						totalData.put(column,"");
					}else{
						totalData.put(column,CommonUtil.retainNDigits(realTotalPoints / allTotalPoints * 100,0));
					}
				}
			}
			datas.add(totalData);
		}
		return datas;
	}
	/**
	 * 查询总得分
	 * 
	 * @param model
	 * @param supplier
	 * @param evaluateYear
	 * @return
	 */
	/*public Map<String, String> caculateRealPointsByEvaluateDetailsSQ(List<EvaluateDetail> evaluateDetails, Supplier supplier, Integer evaluateYear, String model,String materialType) {
		Map<String, Integer> cycleMap = EstimateModel.getCycleMap();
		List<EvaluateDetail> allDetails = evaluateDao.findEvaluateDetailBySupplierAndYear(supplier, evaluateYear,materialType);
		Map<Long, String> monthsMap = new HashMap<Long, String>();
		Map<String, List<Double>> realMap = new HashMap<String, List<Double>>();
		Map<String, List<Double>> totalMap = new HashMap<String, List<Double>>();
		Map<String, String> resultMap = new HashMap<String, String>();
		Map<String, Double> customTotal = new HashMap<String, Double>();
		for (EvaluateDetail evaluateDetail : allDetails) {
			if (evaluateDetail.getRealTotalPoints() == null) {
				continue;
			}
			String months = monthsMap.get(evaluateDetail.getEvaluate()
					.getEstimateModelId());
			if (months == null) {
				int interval = 1;
				if (cycleMap.containsKey(evaluateDetail.getEvaluate().getCycle())) {
					interval = cycleMap.get(evaluateDetail.getEvaluate().getCycle());
				}
				StringBuffer sb = new StringBuffer(",");
				Integer tempMonth = null;
				for (int i = 0; i < 12 / interval; i++) {
					if (tempMonth == null) {
						tempMonth = evaluateDetail.getEvaluate()
								.getStartMonth();
					} else {
						tempMonth += interval;
					}
					if (tempMonth > 12) {
						continue;
					}
					sb.append(tempMonth + ",");
				}
				months = sb.toString();
				monthsMap.put(
						evaluateDetail.getEvaluate().getEstimateModelId(),
						months);
			}
			int month = evaluateDetail.getEvaluate().getEvaluateMonth();
			if (months.indexOf("," + month + ",") > -1) {
				String key = evaluateDetail.getName() + "_" + month;
				if (!realMap.containsKey(key)) {
					realMap.put(key, new ArrayList<Double>());
					totalMap.put(key, new ArrayList<Double>());
				}
				realMap.get(key).add(evaluateDetail.getRealTotalPoints());
				totalMap.get(key).add(evaluateDetail.getTotalPoints());
			}
		}
		for (EvaluateDetail evaluateDetail : evaluateDetails) {
			StringBuffer tds = new StringBuffer("");
			Double[] num = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
			for (int i = 1; i <= 12; i++) {
				String key = evaluateDetail.getName() + "_" + i;
				if (realMap.containsKey(key)) {
					List<Double> realTotalPoints = realMap.get(key);
					Double val = 0.0;
					for (Double value : realTotalPoints) {
						val += value;
					}
					val = val / realTotalPoints.size();
					num[i - 1] = val;
					if(model.equals("供应商季度评价")){
						tds.append("<td></td>");
						customTotal.put(i + "", 0.0);
						continue;
					}
					tds.append("<td style='cursor: pointer;' onclick=\"evaluateDetail('"+key+"','month')\" title=\""+i+"\">"+CommonUtil.retainNDigits(val, 0)+"</td>");
					if (customTotal.containsKey(i + "")) {
						customTotal.put(i + "", customTotal.get(i + "") + val);
					} else {
						customTotal.put(i + "", val);
					}
				} else {
					tds.append("<td></td>");
				}
				if(!model.equals("供应商季度评价")){
					if (3 == i || 6 == i || 9 == i || 12 == i) {
						int k = 3;
						Double sum = 0.0;
						for (int j = i - 3; j < i; j++) {
							if (num[j] == 0.0) {
								k--;
							}
							sum += num[j];
						}
						if(sum!=0.0){
							sum = Double.parseDouble(new DecimalFormat("#.00").format(sum / k));
						}
						tds.append("<td>" + CommonUtil.retainNDigits(sum, 0) + "</td>");
						if(customTotal.containsKey(i+"JD")){
							customTotal.put(i+"JD", customTotal.get(i+"JD") + sum);
						}else{
							customTotal.put(i+"JD", sum);
						}
					}
				}
			}
			if(model.equals("供应商季度评价")){
				String [] td = tds.toString().split("<td>");
				for (int i = 0; i < 12; i++) {
					if(i == 1-1 || i== 4-1 || i== 7-1 || i == 10-1){
						int k = i;
						if(i==0){
							k = 12;
						    td[td.length-1] += "<td jd=true>" + CommonUtil.retainNDigits(num[i], 0) + "</td>";
						}else{
							td[i] += "<td jd=true>" + CommonUtil.retainNDigits(num[i], 0) + "</td>";
						}
						if(customTotal.containsKey(k+"JD")){
							customTotal.put(k+"JD", customTotal.get(k+"JD") + num[i]);
						}else{
							customTotal.put(k+"JD", num[i]);
						}
					}
				}
				tds.setLength(0);
				for (int i = 1; i < td.length; i++) {//必从1开始   因为之前分出了一个空的字符串
					tds.append("<td>" + td[i]);
				}
			}
			//System.out.println("new:"+tds.toString());
			resultMap.put(evaluateDetail.getName(), tds.toString());
		}
		StringBuffer custom_total = new StringBuffer("");
		for (int i = 1; i <= 12; i++) {
			if (customTotal.containsKey(i + "")&&customTotal.get(i + "")!=0.0) {
				custom_total.append("<td>" + CommonUtil.retainNDigits(Double.parseDouble(customTotal.get(i + "").toString()), 0) + "</td>");
				
			} else {
				custom_total.append("<td></td>");
			}
			if (i == 3 || i == 6 || i == 9 || i == 12) {
				if(customTotal.containsKey(i + "JD") && customTotal.get(i+"JD") != 0.0){
					custom_total.append("<td>"+ CommonUtil.retainNDigits(Double.parseDouble(customTotal.get(i+"JD").toString()), 0) + "</td>");
				}else{
					custom_total.append("<td></td>");
				}
			}
		}
		resultMap.put("custom_total", custom_total.toString());
		return resultMap;
	}*/
	/**
	 * 查询总得分
	 * 
	 * @param model
	 * @param supplier
	 * @param evaluateYear
	 * @return
	 */
	public Map<String, String> caculateRealPointsByEvaluateDetails(List<EvaluateDetail> evaluateDetails, Supplier supplier, Integer evaluateYear, String model,String materialType) {
		Map<String, Integer> cycleMap = EstimateModel.getCycleMap();
		List<EvaluateDetail> allDetails = evaluateDao.findEvaluateDetailBySupplierAndYear(supplier, evaluateYear,materialType);
		Map<Long, String> monthsMap = new HashMap<Long, String>();
		Map<String, List<Double>> realMap = new HashMap<String, List<Double>>();
		Map<String, List<Double>> totalMap = new HashMap<String, List<Double>>();
		Map<String, String> resultMap = new HashMap<String, String>();
		Map<String, Double> customTotal = new HashMap<String, Double>();
		for (EvaluateDetail evaluateDetail : allDetails) {
			if (evaluateDetail.getRealTotalPoints() == null) {
				continue;
			}
			String months = monthsMap.get(evaluateDetail.getEvaluate()
					.getEstimateModelId());
			if (months == null) {
				int interval = 1;
				if (cycleMap.containsKey(evaluateDetail.getEvaluate().getCycle())) {
					interval = cycleMap.get(evaluateDetail.getEvaluate().getCycle());
				}
				StringBuffer sb = new StringBuffer(",");
				Integer tempMonth = null;
				for (int i = 0; i < 12 / interval; i++) {
					if (tempMonth == null) {
						tempMonth = evaluateDetail.getEvaluate()
								.getStartMonth();
					} else {
						tempMonth += interval;
					}
					if (tempMonth > 12) {
						continue;
					}
					sb.append(tempMonth + ",");
				}
				months = sb.toString();
				monthsMap.put(
						evaluateDetail.getEvaluate().getEstimateModelId(),
						months);
			}
			int month = evaluateDetail.getEvaluate().getEvaluateMonth();
			if (months.indexOf("," + month + ",") > -1) {
				String key = evaluateDetail.getName() + "_" + month;
				if (!realMap.containsKey(key)) {
					realMap.put(key, new ArrayList<Double>());
					totalMap.put(key, new ArrayList<Double>());
				}
				realMap.get(key).add(evaluateDetail.getRealTotalPoints());
				totalMap.get(key).add(evaluateDetail.getTotalPoints());
			}
		}
		for (EvaluateDetail evaluateDetail : evaluateDetails) {
			StringBuffer tds = new StringBuffer("");
			Double[] num = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
			for (int i = 1; i <= 12; i++) {
				String key = evaluateDetail.getName() + "_" + i;
				if (realMap.containsKey(key)) {
					List<Double> realTotalPoints = realMap.get(key);
					Double val = 0.0;
					for (Double value : realTotalPoints) {
						val += value;
					}
					val = val / realTotalPoints.size();
					num[i - 1] = val;
					if(model.equals("合格")){
						tds.append("<td></td>");
						customTotal.put(i + "", 0.0);
						continue;
					}
//					tds.append("<td style='cursor: pointer;' onclick=\"evaluateDetail('"+key+"','month')\" title=\""+i+"\">"+CommonUtil.retainNDigits(val, 0)+"</td>");
//					tds.append("<td style='cursor: pointer;'>"+CommonUtil.retainNDigits(val, 0)+"</td>");
					if (customTotal.containsKey(i + "")) {
						customTotal.put(i + "", customTotal.get(i + "") + val);
					} else {
						customTotal.put(i + "", val);
					}
				} else {
//					tds.append("<td></td>");
				}
				if(!model.equals("合格")){
					if (3 == i || 6 == i || 9 == i || 12 == i) {
						int k = 3;
						Double sum = 0.0;
						for (int j = i - 3; j < i; j++) {
							if (num[j] == 0.0) {
								k--;
							}
							sum += num[j];
						}
						if(sum!=0.0){
							sum = Double.parseDouble(new DecimalFormat("#.00").format(sum / k));
						}
						tds.append("<td>" + CommonUtil.retainNDigits(sum, 0) + "</td>");
						if(customTotal.containsKey(i+"JD")){
							customTotal.put(i+"JD", customTotal.get(i+"JD") + sum);
						}else{
							customTotal.put(i+"JD", sum);
						}
					}
				}
			}
			if(model.equals("合格")){
				String [] td = tds.toString().split("<td>");
				for (int i = 0; i < 12; i++) {
					if(i == 1-1 || i== 4-1 || i== 7-1 || i == 10-1){
						int k = i;
						if(i==0){
							k = 12;
						    td[td.length-1] += "<td jd=true>" + CommonUtil.retainNDigits(num[i], 0) + "</td>";
						}else{
							td[i] += "<td jd=true>" + CommonUtil.retainNDigits(num[i], 0) + "</td>";
						}
						if(customTotal.containsKey(k+"JD")){
							customTotal.put(k+"JD", customTotal.get(k+"JD") + num[i]);
						}else{
							customTotal.put(k+"JD", num[i]);
						}
					}
				}
				tds.setLength(0);
				for (int i = 1; i < td.length; i++) {//必从1开始   因为之前分出了一个空的字符串
					tds.append("<td>" + td[i]);
				}
			}
			//System.out.println("new:"+tds.toString());
			resultMap.put(evaluateDetail.getName(), tds.toString());
		}
		StringBuffer custom_total = new StringBuffer("");
		for (int i = 1; i <= 12; i++) {
//			if (customTotal.containsKey(i + "")&&customTotal.get(i + "")!=0.0) {
//				custom_total.append("<td>" + CommonUtil.retainNDigits(Double.parseDouble(customTotal.get(i + "").toString()), 0) + "</td>");
//				
//			} else {
//				custom_total.append("<td></td>");
//			}
			if (i == 3 || i == 6 || i == 9 || i == 12) {
				if(customTotal.containsKey(i + "JD") && customTotal.get(i+"JD") != 0.0){
					custom_total.append("<td>"+ CommonUtil.retainNDigits(Double.parseDouble(customTotal.get(i+"JD").toString()), 0) + "</td>");
				}else{
					custom_total.append("<td></td>");
				}
			}
		}
		resultMap.put("custom_total", custom_total.toString());
		return resultMap;
	}

	/**
	 * 计算供应商本年度的实际得分
	 * 
	 * @param supplier
	 */
	public void calculateRealPointsBySupplier(Supplier supplier,
			Long estimateModelId, Evaluate qcdsEvaluate,
			List<WarnSign> warnSigns,String materialType) {
		Calendar calendar = Calendar.getInstance();
		if (estimateModelId != null) {//评价模型不为空
			Map<String, Integer> cycleMap = EstimateModel.getCycleMap();
			EstimateModel estimateModel = estimateModelManager.getEstimateModelById(estimateModelId);
			if (estimateModel != null) {
				List<Evaluate> evaluates = evaluateDao.findEvaluateBySupplierAndYear(supplier, calendar.get(Calendar.YEAR), estimateModel.getId(),materialType);
				List<EstimateModel> modelChildren = getAllChildren(estimateModel);
				Double allRrealPoints = 0.0, allTotalPoints = 0.0;
				for (EstimateModel childModel : modelChildren) {
					allTotalPoints += childModel.getTotalPoints();
					Double realPoints = 0.0;
					int count = 0;
					int interval = 1;
					if (cycleMap.containsKey(childModel.getCycle())) {
						interval = cycleMap.get(childModel.getCycle());
					}
					Integer tempMonth = null;
					StringBuffer sb = new StringBuffer(",");
					for (int i = 0; i < 12 / interval; i++) {
						if (tempMonth == null) {
							tempMonth = childModel.getStartMonth();
						} else {
							tempMonth += interval;
						}
						if (tempMonth > 12) {
							continue;
						}
						sb.append(tempMonth + ",");
					}
					for (int i = 1; i < 13; i++) {
						if (sb.indexOf("," + i + ",") > -1) {
							Evaluate evaluate = null;
							for (Evaluate e : evaluates) {
								if (childModel.getId().toString().equals(e.getEstimateModelId().toString()) && e.getEvaluateMonth() == i) {
									evaluate = e;
									break;
								}
							}
							if (evaluate != null) {
								evaluates.remove(evaluate);
								if (evaluate.getRealTotalPoints() != null) {
									count++;
									realPoints += evaluate.getRealTotalPoints();
								}
							}
						}
					}
					if (count > 0) {
						allRrealPoints += realPoints / count;
					} else {
						allRrealPoints += childModel.getTotalPoints();
					}
				}
				String hql = "from SupplierGoal s where s.supplier = ? and s.evaluateYear = ?";
				List<SupplierGoal> supplierGoals = evaluateDao.find(hql,
						supplier, calendar.get(Calendar.YEAR));
				SupplierGoal supplierGoal = null;
				if (supplierGoals.isEmpty()) {
					supplierGoal = new SupplierGoal();
					supplierGoal.setEvaluateYear(calendar.get(Calendar.YEAR));
					supplierGoal.setCreatedTime(new Date());
					supplierGoal.setCompanyId(ContextUtils.getCompanyId());
					supplierGoal.setCreator(ContextUtils.getUserName());
				} else {
					supplierGoal = supplierGoals.get(0);
				}
				supplierGoal.setModifiedTime(new Date());
				supplierGoal.setModifier(ContextUtils.getUserName());
				DecimalFormat df = new DecimalFormat("#.0");
				supplierGoal.setTotalPoints(allTotalPoints == 0 ? 0 : (Double
						.valueOf(df.format(allRrealPoints * 100
								/ allTotalPoints))));
				// 计算分类
				if (warnSigns == null) {
					hql = "from WarnSign w where w.companyId = ?";
					warnSigns = evaluateDao.find(hql,
							ContextUtils.getCompanyId());
				}
				supplierGoalDao.save(setEvaluateGrade(supplierGoal, warnSigns));
				// 计算QCDS
				if (qcdsEvaluate != null) {
					calculateQcds(qcdsEvaluate, warnSigns);
				}
			} else {
				/*String hql = "from SupplierGoal s where s.supplier = ? and s.evaluateYear = ?";
				List<SupplierGoal> supplierGoals = evaluateDao.find(hql,
						supplier, calendar.get(Calendar.YEAR));
				if (!supplierGoals.isEmpty()) {
					for (int i = 0; i < supplierGoals.size(); i++) {
						supplierGoalDao.delete(supplierGoals.get(i));
					}
				}*/
			}
		} else {
			/*String hql = "from SupplierGoal s where s.supplier = ? and s.evaluateYear = ?";
			List<SupplierGoal> supplierGoals = evaluateDao.find(hql, supplier,
					calendar.get(Calendar.YEAR));
			if (!supplierGoals.isEmpty()) {
				for (int i = 0; i < supplierGoals.size(); i++) {
					supplierGoalDao.delete(supplierGoals.get(i));
				}
			}*/
		}
	}

	@Autowired
	private SupplierQcdsManager qcdsManager;

	public void clearQcds(SupplierQcds qcds) {
		qcds.setTotal(0.0d);
		qcds.setRealTotal(0.0d);
		qcds.setEvaluate1Id(null);
		qcds.setEvaluate1Name(null);
		qcds.setEvaluate2Id(null);
		qcds.setEvaluate3Id(null);
		qcds.setEvaluate4Id(null);
		qcds.setEvaluate5Id(null);
		qcds.setEvaluate6Id(null);
	}

	/**
	 * 方法名: 计算qcds的得分
	 * <p>
	 * 功能说明：
	 * </p>
	 * 
	 * @param evaluate
	 * @param warnSigns
	 */
	public void calculateQcds(Evaluate evaluate, List<WarnSign> warnSigns) {
		String hql = "from Evaluate e where e.evaluateYear = ? and e.evaluateMonth = ? and e.supplierId = ?";
		List<Evaluate> evaluates = evaluateDao.find(hql,
				evaluate.getEvaluateYear(), evaluate.getEvaluateMonth(),
				evaluate.getSupplierId());
		SupplierQcds qcds = qcdsManager.getQcdsBySupplierAndEvaluate(
				evaluate.getSupplierName(), evaluate.getEvaluateYear(),
				evaluate.getEvaluateMonth());
		if (evaluates.isEmpty() && qcds != null) {
			qcdsManager.delete(qcds);
		} else if (!evaluates.isEmpty()) {
			if (qcds == null) {
				qcds = new SupplierQcds();
				qcds.setCreatedTime(new Date());
				qcds.setCompanyId(ContextUtils.getCompanyId());
				qcds.setCreator(ContextUtils.getUserName());
				qcds.setModifiedTime(new Date());
				qcds.setModifier(ContextUtils.getUserName());
				String supplierHql = "from Supplier s where s.id = ?";
				List<Supplier> suppliers = supplierDao.find(supplierHql,
						evaluate.getSupplierId());
				if (suppliers.isEmpty()) {
					return;
				}
				Supplier supplier = suppliers.get(0);
//				qcds.setSqeDepartmentName(supplier.getWhetherProducers());
				qcds.setCode(supplier.getCode());
				qcds.setName(supplier.getName());
				qcds.setSupplyProducts(supplier.getSupplyProductNames());
				qcds.setEvaluateYear(evaluate.getEvaluateYear());
				qcds.setEvaluateMonth(evaluate.getEvaluateMonth());
			}
			clearQcds(qcds);
			int size = evaluates.size();
			qcds.setEvaluateTimes(size);
			for (int i = 1; i <= size; i++) {
				Evaluate e = evaluates.get(i - 1);
				if (i == 1) {
					qcds.setEvaluate1Id(e.getId());
					qcds.setEvaluate1Name(e.getEstimateModelName());
					qcds.setEvaluate1RealTotal(e.getRealTotalPoints());
					qcds.setEvaluate1Total(e.getTotalPoints());
					qcds.setTotal(qcds.getTotal() + e.getTotalPoints());
					qcds.setRealTotal(qcds.getRealTotal() + e.getRealTotalPoints());
				} else if (i == 2) {
					qcds.setEvaluate2Id(e.getId());
					qcds.setEvaluate2Name(e.getEstimateModelName());
					qcds.setEvaluate2RealTotal(e.getRealTotalPoints());
					qcds.setEvaluate2Total(e.getTotalPoints());
					qcds.setTotal(qcds.getTotal() + e.getTotalPoints());
					qcds.setRealTotal(qcds.getRealTotal() + e.getRealTotalPoints());
				} else if (i == 3) {
					qcds.setEvaluate3Id(e.getId());
					qcds.setEvaluate3Name(e.getEstimateModelName());
					qcds.setEvaluate3RealTotal(e.getRealTotalPoints());
					qcds.setEvaluate3Total(e.getTotalPoints());
					qcds.setTotal(qcds.getTotal() + e.getTotalPoints());
					qcds.setRealTotal(qcds.getRealTotal() + e.getRealTotalPoints());
				} else if (i == 4) {
					qcds.setEvaluate4Id(e.getId());
					qcds.setEvaluate4Name(e.getEstimateModelName());
					qcds.setEvaluate4RealTotal(e.getRealTotalPoints());
					qcds.setEvaluate4Total(e.getTotalPoints());
					qcds.setTotal(qcds.getTotal() + e.getTotalPoints());
					qcds.setRealTotal(qcds.getRealTotal() + e.getRealTotalPoints());
				} else if (i == 5) {
					qcds.setEvaluate5Id(e.getId());
					qcds.setEvaluate5Name(e.getEstimateModelName());
					qcds.setEvaluate5RealTotal(e.getRealTotalPoints());
					qcds.setEvaluate5Total(e.getTotalPoints());
					qcds.setTotal(qcds.getTotal() + e.getTotalPoints());
					qcds.setRealTotal(qcds.getRealTotal() + e.getRealTotalPoints());
				} else if (i == 6) {
					qcds.setEvaluate6Id(e.getId());
					qcds.setEvaluate6Name(e.getEstimateModelName());
					qcds.setEvaluate6RealTotal(e.getRealTotalPoints());
					qcds.setEvaluate6Total(e.getTotalPoints());
					qcds.setTotal(qcds.getTotal() + e.getTotalPoints());
					qcds.setRealTotal(qcds.getRealTotal() + e.getRealTotalPoints());
				}
			}
			if (qcds.getTotal() > 0) {
				qcds.setPercentageTotal(qcds.getRealTotal() / qcds.getTotal()
						* 100);
			} else {
				qcds.setPercentageTotal(100.0);
			}
			Map<String, String> redYellowMap = setEvaluateGrade(
					qcds.getPercentageTotal(), warnSigns);
			qcds.setGrade(redYellowMap.get("grade"));
			qcdsManager.saveSupplierQcds(qcds);
		}
	}

	/**
	 * 改变评分规则
	 * 
	 * @param list
	 */
	public void calculateEvaluateGrade(List<SupplierGoal> list) {
		String hql = "from WarnSign w where w.companyId = ?";
		List<WarnSign> warnSigns = evaluateDao.find(hql,
				ContextUtils.getCompanyId());
		for (SupplierGoal supplierGoal : list) {
			supplierGoalDao.save(setEvaluateGrade(supplierGoal, warnSigns));
		}
	}

	public SupplierGoal setEvaluateGrade(SupplierGoal supplierGoal,
			List<WarnSign> warnSigns) {
		Map<String, String> redYellowMap = setEvaluateGrade(
				supplierGoal.getTotalPoints(), warnSigns);
		// String evaluateGrade = "其他",redYellow=null;
		// for(WarnSign warnSign : warnSigns){
		// WarnSign warnSign2 = null;
		// if(warnSign.getGoal1()==null){
		// if(supplierGoal.getTotalPoints()<warnSign.getGoal2()){
		// warnSign2 = warnSign;
		// }
		// }else if(warnSign.getGoal2()==null){
		// if(supplierGoal.getTotalPoints()>=warnSign.getGoal1()){
		// warnSign2 = warnSign;
		// }
		// }else
		// if(supplierGoal.getTotalPoints()==100&&warnSign.getGoal2()==100.0){
		// warnSign2 = warnSign;
		// }else if(warnSign.getGoal1() <=
		// supplierGoal.getTotalPoints()&&warnSign.getGoal2()>supplierGoal.getTotalPoints()){
		// warnSign2 = warnSign;
		// }
		// if(warnSign2 != null){
		// evaluateGrade = warnSign.getEstimateDegree();
		// redYellow = warnSign.getWaringSign();
		// break;
		// }
		// }
		supplierGoal.setEvaluateGrade(redYellowMap.get("grade"));
		supplierGoal.setRedYellowCard(redYellowMap.get("redYellow"));
		return supplierGoal;
	}

	/**
	 * 方法名: 评级计算
	 * <p>
	 * 功能说明：
	 * </p>
	 * 
	 * @param points
	 * @param warnSigns
	 * @return
	 */
	public Map<String, String> setEvaluateGrade(Double points,
			List<WarnSign> warnSigns) {
		String evaluateGrade = "其他", redYellow = null;
		for (WarnSign warnSign : warnSigns) {
			WarnSign warnSign2 = null;
			if (warnSign.getGoal1() == null) {
				if (points < warnSign.getGoal2()) {
					warnSign2 = warnSign;
				}
			} else if (warnSign.getGoal2() == null) {
				if (points >= warnSign.getGoal1()) {
					warnSign2 = warnSign;
				}
			} else if (points == 100 && warnSign.getGoal2() == 100.0) {
				warnSign2 = warnSign;
			} else if (warnSign.getGoal1() <= points
					&& warnSign.getGoal2() > points) {
				warnSign2 = warnSign;
			}
			if (warnSign2 != null) {
				evaluateGrade = warnSign.getEstimateDegree();
				redYellow = warnSign.getWaringSign();
				break;
			}
		}
		Map<String, String> redYellowMap = new HashMap<String, String>();
		redYellowMap.put("grade", evaluateGrade);
		redYellowMap.put("redYellow", redYellow);
		return redYellowMap;
	}

	/**
	 * 获取所有的子模型
	 * 
	 * @param estimateModel
	 * @return
	 */
	public List<EstimateModel> getAllChildren(EstimateModel estimateModel) {
		List<EstimateModel> estimateModels = new ArrayList<EstimateModel>();
		if (estimateModel.getChildren().isEmpty()) {
			if (StringUtils.isNotEmpty(estimateModel.getCycle())
					&& estimateModel.getStartMonth() != null) {
				estimateModels.add(estimateModel);
			}
		} else {
			for (EstimateModel child : estimateModel.getChildren()) {
				estimateModels.addAll(getAllChildren(child));
			}
		}
		return estimateModels;
	}

	/**
	 * 转换json
	 * 
	 * @param params
	 * @return
	 */
	private JSONObject convertJsonObject(JSONObject params) {
		JSONObject resultJson = new JSONObject();
		if (params == null) {
			return resultJson;
		}
		for (Object key : params.keySet()) {
			resultJson.put(key, params.getJSONArray(key.toString()).get(0));
		}
		return resultJson;
	}

	// 设置对象的值
	private void setProperty(Object obj, Object key, Object val)
			throws Exception {
		if ("null".equals(val)) {
			System.out.println("ss");
		}
		Class<?> type = PropertyUtils.getPropertyType(obj, key.toString());
		if (type != null) {
			if (StringUtils.isEmpty(val.toString())) {
				PropertyUtils.setProperty(obj, key.toString(), null);
			} else {
				if (Date.class.equals(type)) {
					SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
					try {
						PropertyUtils.setProperty(obj, key.toString(),
								df.parse(val.toString()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else if (Integer.class.equals(type)) {
					PropertyUtils.setProperty(obj, key.toString(),
							Integer.valueOf(val.toString()));
				} else if (Double.class.equals(type)) {
					PropertyUtils.setProperty(obj, key.toString(),
							Double.valueOf(val.toString()));
				} else {
					PropertyUtils.setProperty(obj, key.toString(), val);
				}
			}
		}
	}
	
	/*
	 * 供应商评价得分总表详情数据
	 * @param page
	 * @param params
	 * @return
	 */
	/*public Page<Evaluate> queryEvaluateDetail(Page<Evaluate> page, JSONObject params) {
		//检索语句
		Query query = getResults(params);
		//统计分页
		int totalCount = query.list().size();
		page.setTotalCount(totalCount);
		query.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
		query.setMaxResults(page.getPageSize());
		//统计封装
		List<Evaluate> reports = new ArrayList<Evaluate>();
		for (Object obj : query.list()) {
			Long id = Long.valueOf(obj.toString());
			Evaluate evaluate = (Evaluate) evaluateDao.getSession().get(Evaluate.class, id);
			reports.add(evaluate);
		}
		page.setResult(reports);
		return page;
	}*/
	
	/**
	 * 供应商评价得分总表详情数据(NCR)
	 * @param page
	 * @param params
	 * @return
	 */
	/*public Page<NCReport> queryNCReportDetail(Page<NCReport> nCReportPage, JSONObject params) {
		//检索语句
		Query query = getResults(params);
		//统计分页
		int totalCount = query.list().size();
		nCReportPage.setTotalCount(totalCount);
		query.setFirstResult((nCReportPage.getPageNo() - 1) * nCReportPage.getPageSize());
		query.setMaxResults(nCReportPage.getPageSize());
		//统计封装
		List<NCReport> reports = new ArrayList<NCReport>();
		for (Object obj : query.list()) {
			Long id = Long.valueOf(obj.toString());
			NCReport ncReport = (NCReport) evaluateDao.getSession().get(NCReport.class, id);
			reports.add(ncReport);
		}
		nCReportPage.setResult(reports);
		return nCReportPage;
	}
	
	*//**
	 * 供应商评价得分总表详情数据(8D)
	 * @param page
	 * @param params
	 * @return
	 *//*
	public Page<Improve8dReport> queryImprove8dReportDetail(Page<Improve8dReport> improve8dReportPage, JSONObject params) {
		//检索语句
		Query query = getResults(params);
		//统计分页
		int totalCount = query.list().size();
		improve8dReportPage.setTotalCount(totalCount);
		query.setFirstResult((improve8dReportPage.getPageNo() - 1) * improve8dReportPage.getPageSize());
		query.setMaxResults(improve8dReportPage.getPageSize());
		//统计封装
		List<Improve8dReport> reports = new ArrayList<Improve8dReport>();
		for (Object obj : query.list()) {
			Long id = Long.valueOf(obj.toString());
			Improve8dReport improve8dReport = (Improve8dReport) evaluateDao.getSession().get(Improve8dReport.class, id);
			reports.add(improve8dReport);
		}
		improve8dReportPage.setResult(reports);
		return improve8dReportPage;
	}*/
	
	private Query getResults(JSONObject params){
		//转化时间
		if(!params.containsKey("evaluateYear") || !StringUtils.isNotEmpty(params.getString("evaluateYear"))){
			throw new RuntimeException("统计对象不能为空！");
		}
		if(params.containsKey("evaluateYear") && params.containsKey("month")){
			params.put("startDate_ge_date", params.getString("evaluateYear")+"-"+params.getString("month"));
			params.put("endDate_le_date", params.getString("evaluateYear")+"-"+params.getString("month"));
			params.remove("evaluateYear");
			params.remove("month");
		}
		if(params.containsKey("evaluateYear") && params.containsKey("quarter")){
			params.put("startDate_ge_date", params.getString("evaluateYear")+"-"+params.getString("quarter"));
			params.put("endDate_le_date", params.getString("evaluateYear")+"-"+params.getString("quarter"));
			params.remove("evaluateYear");
			params.remove("quarter");
		}
		
		//统计过滤
		Object myType = params.get("myType");
    	if(params.containsKey("startDate_ge_date")){
    		if(null != myType && ("week".equals(myType) || "quarter".equals(myType))){
         		String[] dateParts = params.getString("startDate_ge_date").split("-");
         		Calendar calendar = Calendar.getInstance();
         		calendar.set(Calendar.YEAR, Integer.valueOf(dateParts[0]));
         		Integer datePart2 = Integer.valueOf(dateParts[1]);
         		if("week".equals(myType)){
 	        		calendar.set(Calendar.WEEK_OF_YEAR, datePart2);
 	        		calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMinimum(Calendar.DAY_OF_WEEK));
         		}
 	        	if("quarter".equals(myType)){
 	        		calendar.set(Calendar.MONTH, datePart2==1?0:datePart2==2?3:datePart2==3?6:9);
 	        		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
 	        	}
 	        	calendar.set(Calendar.HOUR, calendar.getActualMinimum(Calendar.HOUR));
         		calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
         		calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
 	        	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
     			params.put("startDate_ge_date_time", sdf.format(calendar.getTime()));
         	}
     		if(null != myType && "date".equals(myType))
     			params.put("startDate_ge_date_time",params.getString("startDate_ge_date"));
 			if(null != myType && "month".equals(myType))
     			params.put("startDate_ge_date_time",params.getString("startDate_ge_date") + "-01");
 			if(null != myType && "year".equals(myType))
     			params.put("startDate_ge_date_time",params.getString("startDate_ge_date") + "-01-01");
         	params.remove("startDate_ge_date");
        }
        if(params.containsKey("endDate_le_date")){
         	if(null != myType && ("week".equals(myType) || "quarter".equals(myType) || "month".equals(myType))){
         		String[] dateParts = params.getString("endDate_le_date").split("-");
         		Calendar calendar = Calendar.getInstance();
         		calendar.set(Calendar.YEAR, Integer.valueOf(dateParts[0]));
         		Integer datePart2 = Integer.valueOf(dateParts[1]);
         		if("week".equals(myType)){
 	        		calendar.set(Calendar.WEEK_OF_YEAR, datePart2);
 	        		calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMaximum(Calendar.DAY_OF_WEEK));
         		}
 	        	if("quarter".equals(myType)){
 	        		calendar.set(Calendar.MONTH, datePart2==1?2:datePart2==2?5:datePart2==3?8:11);
 	        		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
 	        	}
 	        	if("month".equals(myType)){
 	        		calendar.set(Calendar.MONTH, datePart2-1);
 	        		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
 	        	}
 	        	calendar.set(Calendar.HOUR, calendar.getActualMaximum(Calendar.HOUR));
         		calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
         		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
 	        	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     			params.put("endDate_le_datetime",sdf.format(calendar.getTime()));
         	}
     		if(null != myType && "date".equals(myType))
     			params.put("endDate_le_datetime",params.getString("endDate_le_date") + " 23:59:59");
 			/*if(null != myType && "month".equals(myType))
     			params.put("endDate_le_datetime",params.getString("endDate_le_date") + "-30 23:59:59");*/
 			if(null != myType && "year".equals(myType))
     			params.put("endDate_le_datetime",params.getString("endDate_le_date") + "-12-31 23:59:59");
         	params.remove("endDate_le_date");
        }

        //统计分组
		@SuppressWarnings("unused")
		String groupName = "统计分组";
		if (params.containsKey("groupName")) {
			groupName = params.getString("groupName");
			params.remove("groupName");
		}
		
        //统计对象
		String type = "统计对象";
		if (params.containsKey("type")) {
			type = params.getString("type");
			params.remove("type");
		}
		
		//统计处理
		if (!StringUtils.isNotEmpty(type)) {
			throw new RuntimeException("统计对象不能为空！");
		}
		
		//统计语句
		StringBuffer sql = new StringBuffer("SELECT");
		
		//统计字段
		sql.append(" DISTINCT a.id ");
		
		//统计表名//统计条件
		Map<String, String> fieldMap = null;
		
		/*if(type.contains("NCR") || type.contains("投诉") || type.contains("进货") || type.contains("制程") || type.contains("客户")){
			sql.append("FROM SUPPLIER_NCREPORT a");
			sql.append(" WHERE a.company_id = ? ");
			fieldMap = nCReportFieldMap;
		}else if(type.contains("8D")){
			sql.append("FROM SUPPLIER_IMPROVE_REPORT a");
			sql.append(" WHERE a.company_id = ? ");
			fieldMap = improve8dReportFieldMap;
		}else{
			sql.append("FROM SUPPLIER_EVALUATE a INNER JOIN SUPPLIER_EVALUATE_DETAIL b ON a.id = b.FK_EVALUATE_ID");
			sql.append(" WHERE a.company_id = ? AND b.score IS NOT NULL AND b.real_total_points IS NOT NULL ");
			fieldMap = evaluateFieldMap;
		}*/
		if(type.contains("8D")){
    		sql.append("FROM SUPPLIER_IMPROVE_REPORT a");
			sql.append(" WHERE a.company_id = ? ");
			fieldMap = improve8dReportFieldMap;
    	}else{
			sql.append("FROM SUPPLIER_NCREPORT a");
			sql.append(" WHERE a.company_id = ? ");
			fieldMap = nCReportFieldMap;
    	}
		if (params.containsKey("supplierId")) {
			String supplierId = params.getString("supplierId");
			try {
				Supplier supplier = supplierManager.getSupplier(Long.parseLong(supplierId));
				if(null!=supplier){
					params.put("supplierName_equals", supplier.getName());
				}else{
					throw new RuntimeException("缺少供应商查询条件！");
				}
				params.remove("supplierId");
			} catch (Exception e) {
				throw new RuntimeException("缺少供应商查询条件！");
			}
		}
		

		//统计参数
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		for (Object key : params.keySet()) {
			String[] properties = key.toString().split("_");
			if (properties.length < 2 || !fieldMap.containsKey(properties[0]))
				continue;
			if (!compareMap.containsKey(properties[1]))
				throw new RuntimeException("比较字符串不包含" + properties[1]);
			Object value = params.get(key);
			if (null == value)
				continue;
			if (properties.length > 2)
				value = convertObject(properties[2], value);
			if ("like".equals(properties[1])) {
				sql.append(" AND a." + fieldMap.get(properties[0]) + " LIKE ? ");
				searchParams.add("%" + value + "%");
			} else if ("in".equals(properties[1])) {
				sql.append(" AND a." + fieldMap.get(properties[0]) + " IN (" + value+ ") ");
			} else {
				sql.append(" AND a." + fieldMap.get(properties[0]) + " " + compareMap.get(properties[1]) + " ? ");
				searchParams.add(value);
			}
		}
		//统计执行
		Query query = evaluateDao.getSession().createSQLQuery(sql.toString());
		for (int i = 0; i < searchParams.size(); i++) {
			query.setParameter(i, searchParams.get(i));
		}
		return query;
	}
	
	/**
	 * 转换对象
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	private Object convertObject(String type, Object value) {
		if (value == null) {
			return null;
		} else if ("date".equals(type)) {
			java.util.Date date = DateUtil.parseDate(value.toString());
			if (date == null)
				throw new AmbFrameException("[" + value + "]"+"不是有效的日期格式！");
			return new Date(date.getTime());
		} else if ("utildate".equals(type)) {
			java.util.Date date = DateUtil.parseDate(value.toString());
			if (date == null)
				throw new AmbFrameException("[" + value + "]"+"不是有效的日期格式！");
			return date;
		} else if ("datetime".equals(type)) {
			java.util.Date date = DateUtil.parseDate(value.toString(),"yyyy-MM-dd HH:mm:ss");
			if (date == null)
				throw new AmbFrameException("[" + value + "]"+"不是有效的日期格式！");
			return new Timestamp(date.getTime());
		} else if ("utildatetime".equals(type)) {
			java.util.Date date = DateUtil.parseDate(value.toString(),"yyyy-MM-dd HH:mm:ss");
			if (date == null)
				throw new AmbFrameException("[" + value + "]"+"不是有效的日期格式！");
			return date;
		} else if ("long".equals(type)) {
			return Long.valueOf(value.toString());
		} else if ("double".equals(type)) {
			return Double.valueOf(value.toString());
		} else {
			return value;
		}
	}
}

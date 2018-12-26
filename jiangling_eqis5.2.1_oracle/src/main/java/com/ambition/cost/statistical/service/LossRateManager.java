package com.ambition.cost.statistical.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.cost.entity.Composing;
import com.ambition.cost.statistical.dao.CostRecordDao;
import com.ambition.cost.statistical.dao.LossRateDao;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.date.DateUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.opensymphony.xwork2.ActionContext;

/**
 * 内外部损失率推移图MANAGER
 * 
 * @author zhongjianhang
 * 
 */
@Service
@Transactional
public class LossRateManager {
	public SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-mm-dd hh:mm:ss");
	@Autowired
	private LossRateDao lossRateDao;
	@Autowired
	private CostRecordDao costRecordDao;
	@Autowired
	private LogUtilDao logUtilDao;

	public LossRateDao getLossRateDao() {
		return lossRateDao;
	}

	public void setLossRateDao(LossRateDao lossRateDao) {
		this.lossRateDao = lossRateDao;
	}

	public LogUtilDao getLogUtilDao() {
		return logUtilDao;
	}

	public void setLogUtilDao(LogUtilDao logUtilDao) {
		this.logUtilDao = logUtilDao;
	}

	// 获取内部损失费
	public Double getInteriolLossTotal(Date startDate, Date endDate) {// 获取某段时间的所有内部损失
		Double inInteriolLossTotal = 0.00;
		// System.out.println(startDate);
		// System.out.println(endDate);
		// simpleDateFormat.format(startDate);
		// simpleDateFormat.format(endDate);
		// System.out.println(startDate);
		// System.out.println(endDate);
		String hql1 = "select SUM(mdgci.value) from mfg_defective_goods_composing_item as mdgci,mfg_defective_goods as mdg"
				+ " where mdgci.occurring_date >? and mdgci.occurring_date<? and mdgci.fk_defective_goods_processing_form_id = mdg.id"
				+ " and mdg.is_over='是'";

		List<Object> params1 = new ArrayList<Object>();
		if (startDate != null && endDate != null) {
			params1.add(startDate);
			params1.add(endDate);

		}
		Query query1 = lossRateDao.getSession().createSQLQuery(hql1);
		for (int i = 0; i < params1.size(); i++) {
			query1.setParameter(i, params1.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list1 = query1.list();
		// if(list1.get(0)!=null){
		// Double lossRate1 = Double.valueOf(list1.get(0).toString());
		// }
		List<Object> params2 = new ArrayList<Object>();
		String hql2 = "select SUM(mt.money) from MFG_MATERIAL_TRANSFORM mt "
				+ "where mt.get_date between ? and ?  and mt.is_exceed=? and mt.form_type=?";
		if (startDate != null && endDate != null) {
			params2.add(startDate);
			params2.add(endDate);
			params2.add("是");
			params2.add("1");
		}
		Query query2 = lossRateDao.getSession().createSQLQuery(hql2);
		for (int i = 0; i < params2.size(); i++) {
			query2.setParameter(i, params2.get(i));

		}

		DecimalFormat df = new DecimalFormat("0.00");
		@SuppressWarnings("unchecked")
		List<Double> list2 = query2.list();
		if (list1.get(0) != null && list2.get(0) != null) {
			Double lossRate1 = Double.valueOf(list1.get(0).toString());
			Double lossRate2 = Double.valueOf(list2.get(0).toString());

			inInteriolLossTotal = Double.parseDouble(df.format(lossRate1))
					+ Double.parseDouble(df.format(lossRate2));
			//System.out.println(inInteriolLossTotal);

		}
		return inInteriolLossTotal;
		// return lossRateDao.getInteriolLossTotal(startDate,endDate,status);
	}

	// 获取外部损失费
	public Double getExteriorLossTotal(Date startDate, Date endDate) {
		Double exteriorLossTotal = 0.00;
		String hql = "select sum(mrrr.repair_total_cost) from MFG_REPAIR_RECORD_REPORT mrrr where mrrr.repair_date between ? and ? and bad_source='售后'";
		List<Object> params = new ArrayList<Object>();
		if (startDate != null && endDate != null) {
			params.add(startDate);
			params.add(endDate);

		}
		Query query = lossRateDao.getSession().createSQLQuery(hql);
		for (int i = 0; i < params.size(); i++) {
			query.setParameter(i, params.get(i));
		}
		DecimalFormat df = new DecimalFormat("0.00");
		@SuppressWarnings("unchecked")
		List<Double> list = query.list();
		if (list.get(0) != null)
			exteriorLossTotal = Double.valueOf(list.get(0).toString());

		return Double.parseDouble(df.format(exteriorLossTotal));

	}

	// 获取销售总额
	public Double getGrossSalesTotal(Date startDate, Date endDate) {
		Double grossSalesTotal = 0.00;
		String hql = "select SUM(msgr.amount_of_money)  from MFG_SEND_GOODS_RECORD msgr where msgr.send_date between ? and ?";
		List<Object> params = new ArrayList<Object>();
		params.add(startDate);
		params.add(endDate);
		Query query = lossRateDao.getSession().createSQLQuery(hql);
		for (int i = 0; i < params.size(); i++) {
			query.setParameter(i, params.get(i));
		}
		DecimalFormat df = new DecimalFormat("0.00");
		@SuppressWarnings("unchecked")
		List<Double> list = query.list();
		if (list.get(0) != null)
			grossSalesTotal = Double.valueOf(list.get(0).toString());

		return Double.parseDouble(df.format(grossSalesTotal));
	}

	/**
	 * 方法名: 查询每月的销售金额
	 * <p>
	 * 功能说明：
	 * </p>
	 * 
	 * @param params
	 * @param startMonth
	 * @param endMonth
	 * @return
	 */
	private Map<Integer, Double> searchSalesMoney(JSONObject params,
			Integer startMonth, Integer endMonth) {
		// 查询销售额
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(startMonth);
		searchParams.add(endMonth);
		String sql = "select occurring_month,sum(sales_fee) from COST_CACHE_SALES_RECORD where occurring_month between ? and ? ";
		sql += " group by occurring_month";
		Query query = lossRateDao.getSession().createSQLQuery(sql);
		for (int i = 0; i < searchParams.size(); i++) {
			query.setParameter(i, searchParams.get(i));
		}
		List<?> list = query.list();
		Map<Integer, Double> salesMap = new HashMap<Integer, Double>();
		for (Object obj : list) {
			Object[] objs = (Object[]) obj;
			if (objs[0] == null || objs[1] == null) {
				continue;
			}
			salesMap.put(Integer.valueOf(objs[0].toString()),
					Double.valueOf(objs[1].toString()));
		}
		return salesMap;
	}

	/**
	 * 方法名: 查询每月的内部损失成本
	 * <p>
	 * 功能说明：
	 * </p>
	 * 
	 * @param params
	 * @param startMonth
	 * @param endMonth
	 * @return
	 */
	private Map<Integer, Double> searchInnerCost(JSONObject params,
			Integer startMonth, Integer endMonth) {
		// 查询内部损失成本
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(startMonth);
		searchParams.add(endMonth);
		String sql = "select occurring_month,sum(value) from COST_CACHE_INNER_COST where occurring_month between ? and ? ";
		sql += " group by occurring_month";
		Query query = lossRateDao.getSession().createSQLQuery(sql);
		for (int i = 0; i < searchParams.size(); i++) {
			query.setParameter(i, searchParams.get(i));
		}
		List<?> list = query.list();
		Map<Integer, Double> innerMap = new HashMap<Integer, Double>();
		DecimalFormat df = new DecimalFormat("#.00");
		for (Object obj : list) {
			Object[] objs = (Object[]) obj;
			if (objs[0] == null || objs[1] == null) {
				continue;
			}
			innerMap.put(Integer.valueOf(objs[0].toString()), Double.valueOf(df
					.format(Double.valueOf(objs[1].toString()))));
		}
		return innerMap;
	}

	/**
	 * 方法名: 查询每月的内部损失成本
	 * <p>
	 * 功能说明：
	 * </p>
	 * 
	 * @param params
	 * @param startMonth
	 * @param endMonth
	 * @return
	 */
	private Map<Integer, Double> searchZHLDInnerCost(JSONObject params,
			Integer startMonth, Integer endMonth) {
		// 查询报废通知单报废数量
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(startMonth);
		searchParams.add(endMonth);
		String itemGroup = null, customerName = null, dutyDepart=null,feeState=null,project = null, companyName = null;
		String sql = "select occurring_month,sum(value) from QIS_VIEW_COST where  occurring_month between ? and ? ";
		if (params.containsKey("itemGroup")) {
			itemGroup = params.getString("itemGroup");
			if (StringUtils.isNotEmpty(itemGroup)) {
				sql += " and item_Group = ?";
				searchParams.add(itemGroup);
			}
		}
		if (params.containsKey("customerName")) {
			customerName = params.getString("customerName");
			if (StringUtils.isNotEmpty(customerName)) {
				sql += " and customer_Name = ?";
				searchParams.add(customerName);
			}
		}
		if (params.containsKey("project")) {
			project = params.getString("project");
			if (StringUtils.isNotEmpty(project)) {
				sql += " and project = ?";
				searchParams.add(project);
			}
		}
		if (params.containsKey("companyName")) {
			companyName = params.getString("companyName");
			if (StringUtils.isNotEmpty(companyName)) {
				sql += " and company_Name = ?";
				searchParams.add(companyName);
			}
		}
		if (params.containsKey("feeState")) {
			feeState = params.getString("feeState");
			if (StringUtils.isNotEmpty(feeState)) {
				sql += " and fee_State = ?";
				searchParams.add(feeState);
			}
		}
		if (params.containsKey("dutyDepart")) {
			dutyDepart = params.getString("dutyDepart");
			if (StringUtils.isNotEmpty(dutyDepart)) {
				sql += " and duty_Depart = ?";
				searchParams.add(dutyDepart);
			}
		}
		sql += " group by occurring_month";
		Query query = lossRateDao.getSession().createSQLQuery(sql);
		for (int i = 0; i < searchParams.size(); i++) {
			query.setParameter(i, searchParams.get(i));
		}
		List<?> list = query.list();
		Map<Integer, Double> innerMap = new HashMap<Integer, Double>();
		DecimalFormat df = new DecimalFormat("#.00");
		for (Object obj : list) {
			Object[] objs = (Object[]) obj;
			if (objs[0] == null || objs[1] == null) {
				continue;
			}
			innerMap.put(Integer.valueOf(objs[0].toString()), Double.valueOf(df
					.format(Double.valueOf(objs[1].toString()))));
		}
		return innerMap;
	}

	/**
	 * 内部损失率按月统计
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */

	public Map<String, Object> getInteriorLossRateProcessDatasByMonths(
			JSONObject params) throws Exception {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(DateUtil.parseDate(
				params.getString("startDate_ge_date"), "yyyy-MM"));
		Integer startMonth = CommonUtil1.getYearAndMonth(startCal);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(DateUtil.parseDate(params.getString("endDate_le_date"),
				"yyyy-MM"));
		Integer endMonth = CommonUtil1.getYearAndMonth(endCal);

		// 销售金额
		// Map<Integer,Double> salesMap = searchSalesMoney(params, startMonth,
		// endMonth);
		// 产品总成本
		// Map<Integer,Double> productsMap = searchProductsCost(params,
		// startMonth, endMonth);
		// 内部成本
		// Map<Integer,Double> innerMap = searchInnerCost(params, startMonth,
		// endMonth);
		Map<Integer, Double> innerZHLDMap = searchZHLDInnerCost(params,
				startMonth, endMonth);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("title", "质量成本金额推移图");
		result.put("subtitle", "(" + params.get("startDate_ge_date").toString()
				+ "-" + params.get("endDate_le_date").toString() + ")");
		List<Integer> categories = new ArrayList<Integer>();
		result.put("categories", categories);
		result.put("yAxisTitle2", "质<br/>量<br/>成<br/>本<br/>金<br/>额");

		List<Map<String, Object>> data1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> series2 = new HashMap<String, Object>();
		series2.put("name", "质量成本金额");
		List<Map<String, Object>> data2 = new ArrayList<Map<String, Object>>();
		List<Double> data3 = new ArrayList<Double>();

		List<Double> data = new ArrayList<Double>();
		while (startMonth <= endMonth) {
			// 横坐标月份的参数
			categories.add(startMonth);

			Map<String, Object> map = new HashMap<String, Object>();
			// Double salesTotal =
			// salesMap.containsKey(startMonth)?salesMap.get(startMonth):0.0;
			// Double productsTotal =
			// productsMap.containsKey(startMonth)?productsMap.get(startMonth):0.0;
			// map.put("date",startMonth);
			// map.put("name","产品总成本");
			// map.put("y",salesTotal);
			// map.put("y",productsTotal);
			// data1.add(map);
			// data.add(salesTotal);
			// data.add(productsTotal);

			data = new ArrayList<Double>();
			map = new HashMap<String, Object>();
			map.put("date", startMonth);
			// Double innerLoss =
			// innerMap.containsKey(startMonth)?innerMap.get(startMonth):0.0;
			Double innerZHLDLoss = innerZHLDMap.containsKey(startMonth) ? innerZHLDMap
					.get(startMonth) : 0.0;
			map.put("name", "质量成本损失");
			map.put("in", "质量成本损失");
			map.put("y", innerZHLDLoss);
			data2.add(map);
			data.add(innerZHLDLoss);
			double rate = 0;
			// if(productsTotal != 0.0){
			// rate = (innerZHLDLoss/productsTotal)*100;//报废损失率
			// }else if(innerZHLDLoss==0.0&&productsTotal==0.0){
			// rate =0.0;
			// }
			// data3.add(rate);

			startCal.add(Calendar.MONTH, 1);
			startMonth = CommonUtil1.getYearAndMonth(startCal);
		}
		ActionContext.getContext().put("categorieslist", categories);
		result.put("tableHeaderList", categories);
		result.put("firstColName", "月份");
		series2.put("data", data2);
		result.put("series2", series2);
		result.put("max", 100);
		return result;
	}

	/**
	 * 内部损失率按月统计
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */

	/*
	 * public Map<String,Object>
	 * getInteriorLossRateProcessDatasByMonths(JSONObject params) throws
	 * Exception{ Calendar startCal = Calendar.getInstance();
	 * startCal.setTime(DateUtil
	 * .parseDate(params.getString("startDate_ge_date"),"yyyy-MM")); Integer
	 * startMonth = CommonUtil.getYearAndMonth(startCal);
	 * 
	 * Calendar endCal = Calendar.getInstance();
	 * endCal.setTime(DateUtil.parseDate
	 * (params.getString("endDate_le_date"),"yyyy-MM")); Integer endMonth =
	 * CommonUtil.getYearAndMonth(endCal); //销售金额 Map<Integer,Double> salesMap =
	 * searchSalesMoney(params, startMonth, endMonth); //内部成本
	 * Map<Integer,Double> innerMap = searchInnerCost(params, startMonth,
	 * endMonth);
	 * 
	 * Map<String,Object> result = new HashMap<String, Object>();
	 * result.put("title",
	 * (params.containsKey("businessUnitName")?params.getString
	 * ("businessUnitName"):"") + "内部损失率"); result.put("subtitle","(" +
	 * params.get("startDate_ge_date").toString() + "-" +
	 * params.get("endDate_le_date").toString() + ")"); List<Integer> categories
	 * = new ArrayList<Integer>(); result.put("categories", categories);
	 * result.put("yAxisTitle1","销<br/>售<br/>额");
	 * result.put("yAxisTitle2","内<br/>部<br/>损<br/>失<br/>率");
	 * 
	 * Map<String,Object> series1 = new HashMap<String, Object>();
	 * series1.put("name", "销售额"); List<Map<String,Object>> data1 = new
	 * ArrayList<Map<String,Object>>(); Map<String,Object> series2 = new
	 * HashMap<String, Object>(); series2.put("name", "内部成本");
	 * List<Map<String,Object>> data2 = new ArrayList<Map<String,Object>>();
	 * Map<String,Object> series3 = new HashMap<String, Object>();
	 * series3.put("name", "内部损失率"); List<Double> data3 = new
	 * ArrayList<Double>();
	 * 
	 * List<Double> data = new ArrayList<Double>(); while(startMonth<=endMonth){
	 * //横坐标月份的参数 categories.add(startMonth);
	 * 
	 * Map<String,Object> map = new HashMap<String, Object>(); Double salesTotal
	 * = salesMap.containsKey(startMonth)?salesMap.get(startMonth):0.0;
	 * map.put("date",startMonth); map.put("name","销售额");
	 * map.put("y",salesTotal); data1.add(map); data.add(salesTotal);
	 * 
	 * data = new ArrayList<Double>(); map = new HashMap<String, Object>();
	 * map.put("date",startMonth); Double innerLoss =
	 * innerMap.containsKey(startMonth)?innerMap.get(startMonth):0.0;
	 * map.put("name","内部损失"); map.put("in","内部损失"); map.put("y", innerLoss);
	 * data2.add(map); data.add(innerLoss); double rate = 0; if(salesTotal !=
	 * 0.0){ rate = (innerLoss/salesTotal)*100;//内部损失率 } else
	 * if(innerLoss==0.0&&salesTotal==0.0){ rate =0.0; } data3.add(rate);
	 * 
	 * startCal.add(Calendar.MONTH,1); startMonth =
	 * CommonUtil.getYearAndMonth(startCal); }
	 * ActionContext.getContext().put("categorieslist", categories);
	 * result.put("tableHeaderList", categories);
	 * result.put("firstColName","月份"); series1.put("data", data1);
	 * result.put("series1", series1); series2.put("data", data2);
	 * result.put("series2", series2); series3.put("data", data3);
	 * result.put("series3", series3); result.put("max", 100); return result; }
	 */

	/**
	 * 方法名: 查询每月的外贸部损失成本
	 * <p>
	 * 功能说明：
	 * </p>
	 * 
	 * @param params
	 * @param startMonth
	 * @param endMonth
	 * @return
	 */
	private Map<Integer, Double> searchExeriorCost(JSONObject params,
			Integer startMonth, Integer endMonth) {
		// 查询销售额
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(startMonth);
		searchParams.add(endMonth);
		String sql = "select occurring_month,sum(value) from COST_CACHE_EXTERIOR_COST where occurring_month between ? and ? ";
		sql += " group by occurring_month";
		Query query = lossRateDao.getSession().createSQLQuery(sql);
		for (int i = 0; i < searchParams.size(); i++) {
			query.setParameter(i, searchParams.get(i));
		}
		List<?> list = query.list();
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		DecimalFormat df = new DecimalFormat("#.00");
		for (Object obj : list) {
			Object[] objs = (Object[]) obj;
			if (objs[0] == null || objs[1] == null) {
				continue;
			}
			map.put(Integer.valueOf(objs[0].toString()), Double.valueOf(df
					.format(Double.valueOf(objs[1].toString()))));
		}
		return map;
	}

	/**
	 * 外部损失率按月统计
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getExteriorLossRateProcessDatasByMonths(
			JSONObject params) throws Exception {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(DateUtil.parseDate(
				params.getString("startDate_ge_date"), "yyyy-MM"));
		Integer startMonth = CommonUtil1.getYearAndMonth(startCal);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(DateUtil.parseDate(params.getString("endDate_le_date"),
				"yyyy-MM"));
		Integer endMonth = CommonUtil1.getYearAndMonth(endCal);
		// 销售金额
		Map<Integer, Double> salesMap = searchSalesMoney(params, startMonth,
				endMonth);
		// 成本
		Map<Integer, Double> exteriorMap = searchExeriorCost(params,
				startMonth, endMonth);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(
				"title",
				(params.containsKey("businessUnitName") ? params
						.getString("businessUnitName") : "") + "外部损失率");
		result.put("subtitle", "(" + params.get("startDate_ge_date").toString()
				+ "-" + params.get("endDate_le_date").toString() + ")");
		List<Integer> categories = new ArrayList<Integer>();
		result.put("categories", categories);
		result.put("yAxisTitle1", "销<br/>售<br/>额");
		result.put("yAxisTitle2", "外<br/>部<br/>损<br/>失<br/>率");

		Map<String, Object> series1 = new HashMap<String, Object>();
		series1.put("name", "销售额");
		List<Map<String, Object>> data1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> series2 = new HashMap<String, Object>();
		series2.put("name", "外部损失");
		List<Map<String, Object>> data2 = new ArrayList<Map<String, Object>>();
		Map<String, Object> series3 = new HashMap<String, Object>();
		series3.put("name", "外部损失率");
		List<Double> data3 = new ArrayList<Double>();

		List<Double> data = new ArrayList<Double>();
		double total = 0, loss = 0;
		while (startMonth <= endMonth) {
			// 横坐标月份的参数
			categories.add(startMonth);

			Map<String, Object> map = new HashMap<String, Object>();
			Double grossSalesTotal = salesMap.containsKey(startMonth) ? salesMap
					.get(startMonth) : 0.0;
			map.put("date", startMonth);
			map.put("name", "销售金额");
			map.put("y", grossSalesTotal);
			data1.add(map);
			data.add(grossSalesTotal);
			total = grossSalesTotal;

			data = new ArrayList<Double>();
			map = new HashMap<String, Object>();
			map.put("date", startMonth);
			Double exteriorLossTotal = exteriorMap.containsKey(startMonth) ? exteriorMap
					.get(startMonth) : 0.0;
			map.put("name", "外部损失");
			map.put("in", "外部损失");
			map.put("y", exteriorLossTotal);
			data2.add(map);
			data.add(exteriorLossTotal);
			loss = exteriorLossTotal;// 外部损失金额

			double rate = 0;
			if (total != 0.0) {
				rate = (loss / total) * 100;// 内部损失率
			} else if (loss == 0.0 && total == 0.0) {
				rate = 0.0;
			}
			data3.add(rate);

			startCal.add(Calendar.MONTH, 1);
			startMonth = CommonUtil1.getYearAndMonth(startCal);
		}
		ActionContext.getContext().put("categorieslist", categories);
		result.put("tableHeaderList", categories);
		result.put("firstColName", "月份");
		series1.put("data", data1);
		result.put("series1", series1);
		series2.put("data", data2);
		result.put("series2", series2);
		series3.put("data", data3);
		result.put("series3", series3);
		result.put("max", 100);
		return result;
	}

/*	public String queryInteriorLoss(Page<DefectiveGoodsComposingItem> page,
			JSONObject params) throws ParseException {
		return lossRateDao.queryInteriorLossDetail(page, params);
	}*/

	/**
	 * 质量成本统计图
	 * 
	 * @param params
	 * @return
	 */

	public Map<String, Object> queryGeneralPlatoReport(Page<Composing> page,
			JSONObject params) throws Exception {
		Integer startMonth = Integer.valueOf(params.getString(
				"startDate_ge_date").replaceAll("-", ""));
		Integer endMonth = Integer.valueOf(params.getString("endDate_le_date")
				.replaceAll("-", ""));
		// 统计数量
		int pageSize = 20;
		String parentLevel = "one";
		String currentLevel = "two";
		String code = "";
		if (params.containsKey("parentLevel")) {
			parentLevel = params.getString("parentLevel");
			code = params.getString("code");
		}
		String totalField = "level_two_code,level_two_name";//
		String whereSql = "where occurring_month between ? and ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(startMonth);
		searchParams.add(endMonth);

		if ("two".equals(parentLevel)) {
			totalField = "level_three_code,level_three_name";
			whereSql += " and level_two_code = ?";
			searchParams.add(code);
			currentLevel = "three";
		} else if ("three".equals(parentLevel)) {
			totalField = "code,name";
			whereSql += " and level_three_code = ? ";
			searchParams.add(code);
			currentLevel = "four";
		}

		String sql = "SELECT " + totalField + ",SUM(value)"
				+ " FROM COST_CACHE_COST " + whereSql + " group by "
				+ totalField;
		Query query = lossRateDao.getSession().createSQLQuery(sql);
		if (StringUtils.isNotEmpty(code)) {

		}
		for (int i = 0; i < searchParams.size(); i++) {
			query.setParameter(i, searchParams.get(i));
		}

		List<?> list = query.list();
		Map<String, Object> result = new HashMap<String, Object>();
		String title = (params.containsKey("name") ? params.getString("name")
				: "");
		if (title.endsWith("<br/>")) {
			title.substring(0, title.lastIndexOf("<br/>"));
		}
		if (title.length() > 0) {
			title += "分布图";
		} else {
			title = "质量成本分布图";
		}
		result.put("title", title);
		List<String> categories = new ArrayList<String>();
		// 第一条Y轴线的数据
		List<Map<String, Object>> data1 = new ArrayList<Map<String, Object>>();
		// 第二条Y轴线的数据
		List<Map<String, Object>> data2 = new ArrayList<Map<String, Object>>();
		// 表格需要的数据
		List<Map<String, Object>> tableData = new ArrayList<Map<String, Object>>();
		Double hisCount = 0.0, otherData1 = 0.0;
		DecimalFormat numberFormat = new DecimalFormat("#.##");
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < list.size(); i++) {
			Object[] objs = (Object[]) list.get(i);
			Double number = 0.0;// ,inspectAmount = 0.0
			number = objs[2] == null ? 0.0 : Double.valueOf(objs[2].toString());
			String name = objs[1] == null ? "" : objs[1].toString();
			if (i < pageSize - 1) {
				categories.add(name);
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("name", objs[1]);
				data.put("code", objs[0]);
				data.put("currentLevel", currentLevel);
				data.put("arg", name + "_equals");
				data.put("y", number);
				data1.add(data);
				hisCount += number;

				data = new HashMap<String, Object>();
				data.put("name", "spline");
				data.put("y", hisCount);
				data2.add(data);
				// 表格数据
				data = new HashMap<String, Object>();
				data.put("name", name);
				data.put("total", (double) Math.round(number * 100) / 100);
				data.put("allTotal",
						Double.valueOf(numberFormat.format(hisCount)));
				data.put("id", i + 1);
				tableData.add(data);
				// 其他
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append("'" + name + "'");
			} else {
				hisCount += number;
				otherData1 += number;
			}
		}
		if (Double.valueOf(otherData1.toString()) > 0) {
			categories.add("其他");
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("name", "column");
			data.put("y", otherData1);
			data.put("arg", sb.toString() + "_notin");
			data1.add(data);
			data = new HashMap<String, Object>();
			data.put("name", "spline");
			data.put("y", hisCount);
			data2.add(data);
			// 表格数据
			data = new HashMap<String, Object>();
			data.put("name", "其他");
			data.put("total", (double) Math.round(otherData1 * 100) / 100);
			data.put("allTotal", Double.valueOf(numberFormat.format(hisCount)));
			data.put("id", tableData.size() + 1);
			tableData.add(data);
		}
		// 更新tableData里面的比率
		DecimalFormat df = new DecimalFormat("#.##%");
		for (Map<String, Object> map : tableData) {
			Double total = (Double) map.get("total");
			Double allTotal = (Double) map.get("allTotal");
			map.put("bi1", df.format(total / hisCount));
			map.put("bi2", df.format(allTotal / hisCount));
		}
		result.put("tableData", tableData);
		if (data2.size() == 1) {
			data2.clear();
		}
		// 转换
		for (int i = 0; i < categories.size(); i++) {
			String str = categories.get(i);
			StringBuffer newStr = new StringBuffer("");
			int count = 0;
			for (int j = 0; j < str.length(); j++) {
				char n = str.charAt(j);
				newStr.append(n);
				if (count > 0 && count % 5 == 0) {
					newStr.append("<br/>");
				}
				if (19968 <= n && n < 40623) {
					count++;
				} else {
					if (j + 1 < str.length()) {
						n = str.charAt(j + 1);
						if (!(19968 <= n && n < 40623)) {
							count++;
						}
					}
				}
			}
			categories.set(i, newStr.toString());
		}
		result.put("categories", categories);
		result.put("yAxisTitle1", "不<br/><br/>良<br/><br/>数<br/><br/>量");

		if (data2.isEmpty()) {
			result.put("yAxisTitle2", "");
		} else {
			result.put("yAxisTitle2",
					"不<br/><br/>良<br/><br/>百<br/><br/>分<br/><br/>比");
		}

		Map<String, Object> series1 = new HashMap<String, Object>();

		series1.put("data", data1);
		result.put("series1", series1);

		Map<String, Object> series2 = new HashMap<String, Object>();
		series2.put("data", data2);
		result.put("series2", series2);
		result.put("max", hisCount);

		return result;
	}

	/**
	 * 内部损失成本柏拉图
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> queryInteriorReport(JSONObject params) {
		params = CommonUtil1.convertJsonObject(params);
		String group = params.getString("group");
		// String type = params.getString("type");
		Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("business_unit_code", "in");
		fieldMap.put("form_type", "=");
		fieldMap.put("business_unit_name", "like");
		fieldMap.put("department_name", "like");
		fieldMap.put("code", "=");
		fieldMap.put("name", "like");

		if (StringUtils.isEmpty(group)) {
			throw new RuntimeException("统计分组不能为空!");
		} else {
			String titleStr = "内部损失金额分析柏拉图";
			String columnStr = "损失金额";
			String[] yAxisTitleStr = { "损<br/><br/>失<br/><br/>金<br/><br/>额",
					"损<br/><br/>失<br/><br/>金<br/><br/>额<br/><br/>百<br/><br/>分<br/><br/>比" };

			String groupName = params.getString("groupName");
			params.remove("groupName");

			StringBuffer sql = new StringBuffer("");

			if (!fieldMap.containsKey(group)) {
				throw new RuntimeException("当前统计对象不含 '" + groupName
						+ "' 的分组条件!");
			}

			List<Object> searchParams = new ArrayList<Object>();
			sql.append("select "
					+ group
					+ " as 'name',sum(value) as total from COST_CACHE_INNER_COST where occurring_month between ? and ?");
			searchParams.add(Integer.valueOf(params.getString("startDate")
					.replaceAll("-", "")));
			searchParams.add(Integer.valueOf(params.getString("endDate")
					.replaceAll("-", "")));

			// 统计数量
			int pageSize = Integer.valueOf(params.getString("pageSize"));
			params.remove("pageSize");
			// 绑定查询条件
			for (Object key : params.keySet()) {
				if (!fieldMap.containsKey(key.toString())) {
					continue;
				}

				Object value = params.get(key);
				if (value == null) {
					continue;
				}

				if ("like".equals(fieldMap.get(key.toString()))) {
					sql.append(" and " + key + " like ? ");
					searchParams.add("%" + value + "%");
				} else if ("in".equals(fieldMap.get(key.toString()))) {
					sql.append(" and " + key + " in ('"
							+ value.toString().replaceAll(",", "','") + "') ");
				} else {
					sql.append(" and " + key + " "
							+ fieldMap.get(key.toString()) + " ? ");
					searchParams.add(value);
				}
			}

			// 组装groupBy
			sql.append(" group by " + group + " order by total desc");

			Query query = lossRateDao.getSession().createSQLQuery(
					sql.toString());
			for (int i = 0; i < searchParams.size(); i++) {
				query.setParameter(i, searchParams.get(i));
			}
			List list = query.list();

			// 组装报表数据
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("title", titleStr);
			List<String> categories = new ArrayList<String>();
			// 第一条Y轴线的数据
			List<Map<String, Object>> data1 = new ArrayList<Map<String, Object>>();
			// 第二条Y轴线的数据
			List<Map<String, Object>> data2 = new ArrayList<Map<String, Object>>();
			// 表格需要的数据
			List<Map<String, Object>> tableData = new ArrayList<Map<String, Object>>();
			Double hisCount = 0.0, otherData1 = 0.0;
			StringBuffer sb = new StringBuffer("");
			DecimalFormat df = new DecimalFormat("#.##");
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				String name = objs[0] == null ? "" : objs[0].toString();
				Double number = Double.valueOf(objs[1].toString());
				if (i < pageSize - 1) {
					categories.add(name);
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("name", "column");
					data.put("arg", name + "_=");
					data.put("y", Double.valueOf(df.format(number)));
					data1.add(data);
					hisCount += number;
					data = new HashMap<String, Object>();
					data.put("name", "spline");
					data.put("y", Double.valueOf(df.format(hisCount)));
					data2.add(data);
					// 表格数据
					data = new HashMap<String, Object>();
					data.put("name", name);
					data.put("total", Double.valueOf(df.format(number)));
					data.put("allTotal", Double.valueOf(df.format(hisCount)));
					data.put("id", i + 1);
					tableData.add(data);
					// 其他
					if (sb.length() > 0) {
						sb.append(",");
					}
					sb.append(name);
				} else {
					otherData1 += number;
					hisCount += number;
				}
			}
			if (otherData1 > 0) {
				categories.add("其他");
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("name", "column");
				data.put("arg", sb.toString() + "_notin");
				data.put("y", Double.valueOf(df.format(otherData1)));
				data1.add(data);
				hisCount += otherData1;
				data = new HashMap<String, Object>();
				data.put("name", "spline");
				data.put("y", Double.valueOf(df.format(hisCount)));
				data2.add(data);
				// 表格数据
				data = new HashMap<String, Object>();
				data.put("name", "其他");
				data.put("total", Double.valueOf(df.format(otherData1)));
				data.put("allTotal", Double.valueOf(df.format(hisCount)));
				data.put("id", tableData.size() + 1);
				tableData.add(data);
			}
			// 更新tableData里面的比率
			DecimalFormat rateDf = new DecimalFormat("#.##%");
			for (Map<String, Object> map : tableData) {
				Double total = (Double) map.get("total");
				Double allTotal = (Double) map.get("allTotal");
				map.put("bi1", rateDf.format(total / hisCount));
				map.put("bi2", rateDf.format(allTotal / hisCount));
			}
			result.put("tableData", tableData);
			if (data2.size() == 1) {
				data2.clear();
			}
			for (int i = categories.size(); i < 10; i++) {
				categories.add("");
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("name", "column");
				data.put("y", "");
				data1.add(data);
			}
			result.put("categories", categories);
			result.put("yAxisTitle1", yAxisTitleStr[0]);
			if (data2.isEmpty()) {
				result.put("yAxisTitle2", "");
			} else {
				result.put("yAxisTitle2", yAxisTitleStr[1]);
			}

			Map<String, Object> series1 = new HashMap<String, Object>();
			series1.put("name", groupName);
			series1.put("data", data1);
			result.put("series1", series1);

			Map<String, Object> series2 = new HashMap<String, Object>();
			series2.put("name", "百分比");
			series2.put("data", data2);
			result.put("series2", series2);
			result.put("columnName", columnStr);
			result.put("max", hisCount);
			return result;
		}
	}

	/**
	 * 外部损失成本柏拉图
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> queryExteriorReport(JSONObject params) {
		params = CommonUtil1.convertJsonObject(params);
		String group = params.getString("group");
		Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("business_unit_code", "in");
		fieldMap.put("form_type", "=");
		fieldMap.put("business_unit_name", "like");
		fieldMap.put("department_name", "like");
		fieldMap.put("code", "=");
		fieldMap.put("name", "like");

		if (StringUtils.isEmpty(group)) {
			throw new RuntimeException("统计分组不能为空!");
		} else {
			String titleStr = "外部损失金额分析柏拉图";
			String columnStr = "损失金额";
			String[] yAxisTitleStr = { "损<br/><br/>失<br/><br/>金<br/><br/>额",
					"损<br/><br/>失<br/><br/>金<br/><br/>额<br/><br/>百<br/><br/>分<br/><br/>比" };

			String groupName = params.getString("groupName");
			params.remove("groupName");

			StringBuffer sql = new StringBuffer("");

			if (!fieldMap.containsKey(group)) {
				throw new RuntimeException("当前统计对象不含 '" + groupName
						+ "' 的分组条件!");
			}

			List<Object> searchParams = new ArrayList<Object>();
			sql.append("select "
					+ group
					+ " as 'name',sum(value) as total from COST_CACHE_EXTERIOR_COST where occurring_month between ? and ?");
			searchParams.add(Integer.valueOf(params.getString("startDate")
					.replaceAll("-", "")));
			searchParams.add(Integer.valueOf(params.getString("endDate")
					.replaceAll("-", "")));

			// 统计数量
			int pageSize = Integer.valueOf(params.getString("pageSize"));
			params.remove("pageSize");
			// 绑定查询条件
			for (Object key : params.keySet()) {
				if (!fieldMap.containsKey(key.toString())) {
					continue;
				}

				Object value = params.get(key);
				if (value == null) {
					continue;
				}

				if ("like".equals(fieldMap.get(key.toString()))) {
					sql.append(" and " + key + " like ? ");
					searchParams.add("%" + value + "%");
				} else if ("in".equals(fieldMap.get(key.toString()))) {
					sql.append(" and " + key + " in ('"
							+ value.toString().replaceAll(",", "','") + "') ");
				} else {
					sql.append(" and " + key + " "
							+ fieldMap.get(key.toString()) + " ? ");
					searchParams.add(value);
				}
			}

			// 组装groupBy
			sql.append(" group by " + group + " order by total desc");

			Query query = lossRateDao.getSession().createSQLQuery(
					sql.toString());
			for (int i = 0; i < searchParams.size(); i++) {
				query.setParameter(i, searchParams.get(i));
			}
			List list = query.list();

			// 组装报表数据
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("title", titleStr);
			List<String> categories = new ArrayList<String>();
			// 第一条Y轴线的数据
			List<Map<String, Object>> data1 = new ArrayList<Map<String, Object>>();
			// 第二条Y轴线的数据
			List<Map<String, Object>> data2 = new ArrayList<Map<String, Object>>();
			// 表格需要的数据
			List<Map<String, Object>> tableData = new ArrayList<Map<String, Object>>();
			Double hisCount = 0.0, otherData1 = 0.0;
			StringBuffer sb = new StringBuffer("");
			DecimalFormat df = new DecimalFormat("#.##");
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				String name = objs[0] == null ? "" : objs[0].toString();
				Double number = Double.valueOf(objs[1].toString());
				if (i < pageSize - 1) {
					categories.add(name);
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("name", "column");
					data.put("arg", name + "_=");
					data.put("y", Double.valueOf(df.format(number)));
					data1.add(data);
					hisCount += number;
					data = new HashMap<String, Object>();
					data.put("name", "spline");
					data.put("y", Double.valueOf(df.format(hisCount)));
					data2.add(data);
					// 表格数据
					data = new HashMap<String, Object>();
					data.put("name", name);
					data.put("total", Double.valueOf(df.format(number)));
					data.put("allTotal", Double.valueOf(df.format(hisCount)));
					data.put("id", i + 1);
					tableData.add(data);
					// 其他
					if (sb.length() > 0) {
						sb.append(",");
					}
					sb.append(name);
				} else {
					otherData1 += number;
					hisCount += number;
				}
			}
			if (otherData1 > 0) {
				categories.add("其他");
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("name", "column");
				data.put("arg", sb.toString() + "_notin");
				data.put("y", Double.valueOf(df.format(otherData1)));
				data1.add(data);
				hisCount += otherData1;
				data = new HashMap<String, Object>();
				data.put("name", "spline");
				data.put("y", Double.valueOf(df.format(hisCount)));
				data2.add(data);
				// 表格数据
				data = new HashMap<String, Object>();
				data.put("name", "其他");
				data.put("total", Double.valueOf(df.format(otherData1)));
				data.put("allTotal", Double.valueOf(df.format(hisCount)));
				data.put("id", tableData.size() + 1);
				tableData.add(data);
			}
			// 更新tableData里面的比率
			DecimalFormat rateDf = new DecimalFormat("#.##%");
			for (Map<String, Object> map : tableData) {
				Double total = (Double) map.get("total");
				Double allTotal = (Double) map.get("allTotal");
				map.put("bi1", rateDf.format(total / hisCount));
				map.put("bi2", rateDf.format(allTotal / hisCount));
			}
			result.put("tableData", tableData);
			if (data2.size() == 1) {
				data2.clear();
			}
			for (int i = categories.size(); i < 10; i++) {
				categories.add("");
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("name", "column");
				data.put("y", "");
				data1.add(data);
			}
			result.put("categories", categories);
			result.put("yAxisTitle1", yAxisTitleStr[0]);
			if (data2.isEmpty()) {
				result.put("yAxisTitle2", "");
			} else {
				result.put("yAxisTitle2", yAxisTitleStr[1]);
			}

			Map<String, Object> series1 = new HashMap<String, Object>();
			series1.put("name", groupName);
			series1.put("data", data1);
			result.put("series1", series1);

			Map<String, Object> series2 = new HashMap<String, Object>();
			series2.put("name", "百分比");
			series2.put("data", data2);
			result.put("series2", series2);
			result.put("columnName", columnStr);
			result.put("max", hisCount);
			return result;
		}
	}

	public JSONObject queryAnalysisDdata(JSONObject params) {
		JSONObject result = new JSONObject();
		Date startDate = DateUtil.parseDate(params.getString("startDate_date"));
		Date endDate = DateUtil.parseDate(params.getString("endDate_date"));
		String groupStr = params.getString("group");
		String dateField = "";
		if("day".equals(groupStr)){
			dateField = "to_char(c.ref_date,'YYYY-MM-DD')";
		}else{
			dateField = "to_char(c.ref_date,'yyyy-mm')";
		}
		String groupSql  = dateField ;
		//统计字段
		String totalSql = "sum(c.value) as total";
		String sql = "select " + groupSql + ","+totalSql+" ,c.level_two_name as two from COST_COST_RECORD c  " +
				" where c.ref_date between ? and ?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(startDate);
		searchParams.add(endDate);
		if(params.containsKey("businessUnitCode")){
			String businessUnitCode = params.getString("businessUnitCode");
			if(StringUtils.isNotEmpty(businessUnitCode)){
				sql += " and  c.business_unit_code = ?";
				searchParams.add(businessUnitCode);
			}
		}
		if(params.containsKey("levelTwoName")){
			String levelTwoName = params.getString("levelTwoName");
			if(StringUtils.isNotEmpty(levelTwoName)){
				sql += " and c.level_two_name like ?";
				searchParams.add("%" + levelTwoName + "%");
			}
		}
		if(params.containsKey("levelThreeName")){
			String levelThreeName = params.getString("levelThreeName");
			if(StringUtils.isNotEmpty(levelThreeName)){
				sql += " and c.level_three_name like ?";
				searchParams.add("%" + levelThreeName + "%");
			}
		}
		sql += " group by " + groupSql + ",c.level_two_name  order by " + dateField+",c.level_two_name ";
		
		List<?> list = costRecordDao.findBySql(sql,searchParams.toArray());
		List<String> levelTwoNames = new ArrayList<String>();
		JSONArray categories = new JSONArray();
		//缓存值,key为日期+不良
		Map<String,Double> valueMap = new HashMap<String,Double>();
		//Key哦日期
		Map<String,Double> dateValueMap = new HashMap<String, Double>();
		for(Object obj : list){
			Object objs[] = (Object[])obj;
			String levelTwoName = objs[2]+"";
			if(!levelTwoNames.contains(levelTwoName)){
				levelTwoNames.add(levelTwoName);
			}
			//统计周期
			String dateStr = objs[0]+"";
			if(!categories.contains(dateStr)){
				categories.add(dateStr);
			}
			Double badNum=0.0;
			if(objs[1]!=null){
				badNum = Double.valueOf(objs[1].toString());
			}
			valueMap.put(dateStr+levelTwoName,badNum);
			//日期的值
			if(!dateValueMap.containsKey(dateStr)){
				dateValueMap.put(dateStr,0.0);
			}
			dateValueMap.put(dateStr,dateValueMap.get(dateStr)+badNum);
		}
		JSONArray series = new JSONArray();
		//二级成本
		for(String levelTwoName : levelTwoNames){
			JSONObject serie = new JSONObject();
			serie.put("name",levelTwoName);
			JSONArray datas = new JSONArray();
			for(Object dateStr : categories){
				String key = dateStr + levelTwoName;
				Double badNum = valueMap.get(key);
				if(badNum==null){
					badNum = 0.0;
				}
				JSONObject point = new JSONObject();
				point.put("y",badNum);
				datas.add(point);
			}
			serie.put("data",datas);
			series.add(serie);
		}
		//总成本
		JSONObject totalSerie = new JSONObject();
		totalSerie.put("name","总成本");
		totalSerie.put("type","line");
		JSONArray totalDatas = new JSONArray();
		for(Object dateStr : categories){
			JSONObject point = new JSONObject();
			Double badNum = dateValueMap.get(dateStr);
			if(badNum == null){
				badNum = 0.0;
			}
			point.put("y",badNum);
			totalDatas.add(point);
		}
		totalSerie.put("data",totalDatas);
		series.add(totalSerie);
		
		result.put("title","质量成本推移图");
		result.put("categories", categories);
		result.put("tableHeaderList", categories);
		result.put("series",series);
		return result;
	}
}

package com.ambition.spc.statistics.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.spc.statistics.dao.KlippelCheckRecordDao;
import com.ambition.util.common.DateUtil;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 类名:Klippel采集合格率业务类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2013-7-3 发布
 */
@Service
public class KlippelCheckRecordManager {
	@Autowired
	private KlippelCheckRecordDao klippelCheckRecordDao;
	
	/**
	 * Klippele采集合格率按日期统计
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public Map<String,Object> getKlippelDatas(JSONObject params) throws Exception{
		String scope = Struts2Utils.getParameter("scope");
		String groupField = "detectDate";
		String firstColName = "日期";
		if("week".equals(scope)){
			groupField = "yearAndWeek";
			firstColName = "周";
		}else if("month".equals(scope)){
			groupField = "yearAndMonth";
			firstColName = "月";
		}else if("year".equals(scope)){
			groupField = "year";
			firstColName = "年";
		}
		String subTitle = "";
		List<Object> searchParams = new ArrayList<Object>();
		String hql = "select " + groupField + ",sum(k.amount) from KlippelCheckRecord k where k.companyId = ?";
		searchParams.add(ContextUtils.getCompanyId());
		if("week".equals(scope)){
			if(params.containsKey("yearAndWeek_ge_int")){
				hql += " and k.yearAndWeek >= ?";
				searchParams.add(Integer.valueOf(params.getInt("yearAndWeek_ge_int")));
				subTitle = params.getString("yearAndWeek_ge_int");
			}
			if(params.containsKey("yearAndWeek_le_int")){
				hql += " and k.yearAndWeek <= ?";
				searchParams.add(Integer.valueOf(params.getInt("yearAndWeek_le_int")));
				if(subTitle.length()>0){
					subTitle += "-";
				}
				subTitle += params.getString("yearAndWeek_le_int");
			}
		}else if("month".equals(scope)){
			if(params.containsKey("yearAndMonth_ge_int")){
				hql += " and k.yearAndMonth >= ?";
				searchParams.add(Integer.valueOf(params.getInt("yearAndMonth_ge_int")));
				subTitle = params.getString("yearAndMonth_ge_int");
			}
			if(params.containsKey("yearAndMonth_le_int")){
				hql += " and k.yearAndMonth <= ?";
				searchParams.add(Integer.valueOf(params.getInt("yearAndMonth_le_int")));
				if(subTitle.length()>0){
					subTitle += "-";
				}
				subTitle += params.getString("yearAndMonth_le_int");
			}
		}else if("year".equals(scope)){
			if(params.containsKey("year_ge_int")){
				hql += " and k.year >= ?";
				searchParams.add(Integer.valueOf(params.getInt("year_ge_int")));
				subTitle = params.getString("year_ge_int");
			}
			if(params.containsKey("year_le_int")){
				hql += " and k.year <= ?";
				searchParams.add(Integer.valueOf(params.getInt("year_le_int")));
				if(subTitle.length()>0){
					subTitle += "-";
				}
				subTitle += params.getString("year_le_int");
			}
		}else{
			if(params.containsKey("startDate_ge_date")){
				hql += " and k.detectDate >= ?";
				searchParams.add(DateUtil.parseDate(params.getString("startDate_ge_date")));
				subTitle = params.getString("startDate_ge_date");
			}
			if(params.containsKey("endDate_le_date")){
				hql += " and k.detectDate <= ?";
				searchParams.add(DateUtil.parseDate(params.getString("endDate_le_date")));
				if(subTitle.length()>0){
					subTitle += "-";
				}
				subTitle += params.getString("endDate_le_date");
			}
		}
		if(params.containsKey("productNo_like")){
			hql += " and productNo like ?";
			searchParams.add("%" + params.getString("productNo_like") + "%");
		}
		if(params.containsKey("machineNo_in")){
			hql += " and machineNo in ('"
			 + params.getString("machineNo_in").replaceAll(",","','") + "')";
		}
		String qualitedHql = hql + " and isPass = ? group by " + groupField + " order by " + groupField;
		hql += " group by " + groupField + " order by " + groupField;
		List<Object> totalList = klippelCheckRecordDao.find(hql,searchParams.toArray());
		searchParams.add(true);
		List<Object> qualitedList = klippelCheckRecordDao.find(qualitedHql,searchParams.toArray());
		Map<String,Integer> qualitedMap = new HashMap<String, Integer>();
		for(Object obj : qualitedList){
			Object[] objs = (Object[])obj;
			if("date".equals(scope)){
				qualitedMap.put(DateUtil.formateDateStr((Date)objs[0]),Integer.valueOf(objs[1].toString()));
			}else{
				qualitedMap.put(objs[0]+"",Integer.valueOf(objs[1].toString()));
			}
		}
		//获取天数
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("title", "Klippel直通率");
		result.put("subtitle","("+subTitle+")");
		List<String> categories = new ArrayList<String>();
		result.put("categories", categories);
		result.put("yAxisTitle1","检<br/>验<br/>数");
		result.put("yAxisTitle2","直<br/>通<br/>率");
		
		//检验批数
		Map<String,Object> series1 = new HashMap<String, Object>();
		series1.put("name", "检验数量");
		List<Map<String,Object>> data1 = new ArrayList<Map<String,Object>>();
		//合格批数
		Map<String,Object> series2 = new HashMap<String, Object>();
		series2.put("name", "合格数量");
		List<Map<String,Object>> data2 = new ArrayList<Map<String,Object>>();
		//批次合格率
		Map<String,Object> series3 = new HashMap<String, Object>();
		series3.put("name", "直通率");
		List<Double> data3 = new ArrayList<Double>();
		
		List<Integer> data = new ArrayList<Integer>();
		//double total =0, qualified = 0;
		for(Object obj : totalList){
			Object objs[] = (Object[])obj;
			Integer total = Integer.valueOf(objs[1].toString());
			String key = objs[0]+"";
			String header = key;
			if("date".equals(scope)){
				key = DateUtil.formateDateStr((Date)objs[0]);
				header = ((Date)objs[0]).getDate()+"";
			}
			categories.add(header);
			Integer qualited = 0;
			if(qualitedMap.containsKey(key)){
				qualited = qualitedMap.get(key);
			}
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("date",header);
			map.put("name","检查数量");
			map.put("y",total);
			data1.add(map);
			data.add(total);
			
			data = new ArrayList<Integer>();
			map = new HashMap<String, Object>();
			map.put("date",header);
			map.put("name","合格数量");
			map.put("in","合格数量");
			map.put("y",qualited);
			data2.add(map);
			
			double rate = 100.0d;
			if(total > 0){
				rate = (qualited*1.0d/total)*100;
			}
			data3.add(rate);
		}
		ActionContext.getContext().put("categorieslist", categories);
		result.put("tableHeaderList", categories);
		result.put("firstColName",firstColName);
		series1.put("data",data1);
		result.put("series1", series1);
		series2.put("data",data2);
		result.put("series2", series2);
		series3.put("data",data3);
		result.put("series3", series3);
		result.put("max", 100);
		return result;
	}
	
}

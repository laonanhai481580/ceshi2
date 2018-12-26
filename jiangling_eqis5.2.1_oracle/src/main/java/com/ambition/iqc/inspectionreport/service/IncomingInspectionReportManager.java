package com.ambition.iqc.inspectionreport.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.MaterielTypeLevel;
import com.ambition.iqc.inspectionreport.dao.IncomingInspectionActionsReportDao;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.product.util.ContextUtils;
import com.opensymphony.xwork2.ActionContext;
/**
 * 类名:报表
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2013-6-23 发布
 */
@Service
@Transactional
public class IncomingInspectionReportManager {
	@Autowired
	private  IncomingInspectionActionsReportDao incomingInspectionActionsReportDao;
	
	/**
	  * 方法名: 供应商合格率对比
	  * <p>功能说明：</p>
	  * @param params
	  * @return
	 */
	public Object supplierRateChartDatas(JSONObject params){
		Map<String,Object> result = new HashMap<String, Object>();
		//表格的表头
		List<Object> colModels = new ArrayList<Object>();
		JSONObject modelJson = new JSONObject();
		modelJson.put("name","supplierCode");
		modelJson.put("label","供应商名称");
		modelJson.put("index","supplierCode");
		modelJson.put("width","250");
		colModels.add(modelJson);
		JSONObject modelJson1 = new JSONObject();
		modelJson1.put("name","inspection");
		modelJson1.put("label","检查批数");
		modelJson1.put("index","inspection");
		colModels.add(modelJson1);
		JSONObject modelJson2 = new JSONObject();
		modelJson2.put("name","quality");
		modelJson2.put("label","合格批数");
		modelJson2.put("index","quality");
		colModels.add(modelJson2);
		JSONObject modelJson3 = new JSONObject();
		modelJson3.put("name","rate");
		modelJson3.put("label","合格率");
		modelJson3.put("index","rate");
		colModels.add(modelJson3);

		result.put("title", "供应商合格率对比图");
		result.put("subtitle","(" + params.getString("startDate_ge_date") + " - " + params.getString("endDate_le_date") + ")");
		List<String> categories = new ArrayList<String>();
		result.put("categories", categories);
		result.put("yAxisTitle1","批<br/>次<br/>数");
		result.put("yAxisTitle2","合<br/>格<br/>率");
		//检验批数
		Map<String,Object> series1 = new HashMap<String, Object>();
		series1.put("name", "检验批数");
		List<Integer> data = new ArrayList<Integer>();
		List<Map<String,Object>> data5 = new ArrayList<Map<String,Object>>();
		
		StringBuilder hql = new StringBuilder("select i.supplierName,count(*) from IncomingInspectionActionsReport i where i.companyId = ?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if("date".equals(params.getString("myType"))){
			hql.append(" and i.yearMonthAndDate between ? and ?");
		}else{
			hql.append(" and i.yearAndMonth between ? and ?");
		}
		searchParams.add(Integer.valueOf(params.getString("startDate_ge_date").replaceAll("-","")));
		searchParams.add(Integer.valueOf(params.getString("endDate_le_date").replaceAll("-","")));
		CommonUtil1.buildHql(params, hql, searchParams,IncomingInspectionActionsReportManager.getFieldMap(),"i");
		String oneMaterials="";
		String secondMaterials="";
		String threeMaterials="";
		for(Object key : params.keySet()){
			if("checkBomMaterialLevels".equals(key)){
				String [] leveIds=params.get(key).toString().split(",");
				for(String leveId:leveIds){
					String hqlLevel=" from MaterielTypeLevel leve where leve.id=? and leve.companyId=?";
					List<MaterielTypeLevel> levels=incomingInspectionActionsReportDao.find(hqlLevel,Long.valueOf(leveId),ContextUtils.getCompanyId());
					if(!levels.isEmpty()){
						MaterielTypeLevel level=levels.get(0);
						String levelName=level.getMaterielTypeName();
						if(level.getMaterielLevel()==1){
							oneMaterials+=levelName+",";
						}else if(level.getMaterielLevel()==2){
							secondMaterials+=levelName+",";
						}else if(level.getMaterielLevel()==3){
							threeMaterials+=levelName+",";
						}
					}
				}
				break;
			}
		}
		if(!"".equals(oneMaterials)){
			oneMaterials=oneMaterials.substring(0, oneMaterials.length()-1);
			hql.append(" and i.oneLevelMateriel in ('"+oneMaterials.toString().replaceAll(",","','")+"')");
		}
		if(!"".equals(secondMaterials)){
			secondMaterials=secondMaterials.substring(0, secondMaterials.length()-1);
			hql.append(" and i.secondLevelMateriel in ('"+secondMaterials.toString().replaceAll(",","','")+"')");
		}
		if(!"".equals(threeMaterials)){
			threeMaterials=threeMaterials.substring(0, threeMaterials.length()-1);
			hql.append(" and i.threeLevelMateriel in ('"+threeMaterials.toString().replaceAll(",","','")+"')");
		}
		List<Object> allList = incomingInspectionActionsReportDao.find(hql.toString() + " group by i.supplierName order by count(*) desc",searchParams.toArray());
		List<Object> okList = incomingInspectionActionsReportDao.find(hql.toString() + " and i.inspectionConclusion = 'OK' group by i.supplierName order by count(*) desc",searchParams.toArray());
		Map<String,Object[]> okMaps = new HashMap<String, Object[]>();
		for(Object obj : okList){
			Object[] objs = (Object[])obj;
			okMaps.put(objs[0]+"",objs);
		}
		List<Map<String,Object>> rateList = new ArrayList<Map<String,Object>>();
		int i=0;
		for(Object obj : allList){
			Object[] objs = (Object[])obj;
			Object[] okObjs = okMaps.get(objs[0]+"");
			Integer inspectionAmount = 0,okAmount = 0;
			if(objs[0] != null){
				inspectionAmount = Integer.valueOf(objs[1].toString());
			}
			if(okObjs != null && okObjs[1] != null){
				okAmount = Integer.valueOf(okObjs[1].toString());
			}
			double rate=0;
			if(inspectionAmount>0){
				rate=(okAmount*1.0/inspectionAmount)*100;
			}
			Map<String,Object> rateMap = new HashMap<String, Object>();
			rateMap.put("index",i++);
			rateMap.put("supplierName",objs[0]+"");
			rateMap.put("inspectionAmount",inspectionAmount);
			rateMap.put("okAmount",okAmount);
			rateMap.put("rate",rate);
			rateList.add(rateMap);
		}
		Collections.sort(rateList,new Comparator<Map<String,Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				if((Double)o1.get("rate") < (Double)o2.get("rate")){
					return -1;
				}else if((Double)o1.get("rate") > (Double)o2.get("rate")){
					return 1;
				}else{
					return 0;
				}
			}
		});
		List<Integer> data2 = new ArrayList<Integer>();
		List<Map<String,Object>> data6 = new ArrayList<Map<String,Object>>();
		List<Double> data3 = new ArrayList<Double>();
		List<Map<String,Object>> tabledata = new ArrayList<Map<String,Object>>();
		DecimalFormat  df=new DecimalFormat("0.00");
		int count = 0;
		for(Map<String,Object> rateMap : rateList){
			Map<String,Object> map = new HashMap<String, Object>();
			String supplierName = rateMap.get("supplierName")+"";
			//横坐标天数的参数
			categories.add(supplierName);
			map.put("dutySupplier", supplierName);
			map.put("name","检查批数");
			map.put("y",rateMap.get("inspectionAmount"));
			data5.add(map);
			data.add((Integer) rateMap.get("inspectionAmount"));
			//合格批数
			Map<String,Object> okMap = new HashMap<String, Object>();
			okMap.put("dutySupplier", supplierName);
			okMap.put("name","合格批数");
			okMap.put("in","合格批数");
			okMap.put("y", rateMap.get("okAmount"));
			data6.add(okMap);
			data2.add((Integer) rateMap.get("okAmount"));
			//批次合格率
			Double rate = (Double) rateMap.get("rate");
			data3.add(rate);
			//表格数据
			Map<String,Object> dataMap = new HashMap<String, Object>();
			dataMap.put("supplierCode",supplierName);
			dataMap.put("inspection",rateMap.get("inspectionAmount"));
			dataMap.put("quality",rateMap.get("okAmount"));
			dataMap.put("rate", df.format(rate));
			tabledata.add(dataMap);
			count++;
			if(count==15){
				break;
			}
		}
		ActionContext.getContext().put("categorieslist", categories);
		result.put("tableHeaderList", categories);
		series1.put("name", "检验批数");
		series1.put("data",data5);
		result.put("series1", series1);
		//合格批数
		Map<String,Object> series2 = new HashMap<String, Object>();
		series2.put("name", "合格批数");
		series2.put("data",data6);
		result.put("series2", series2);
		//批次合格率
		Map<String,Object> series3 = new HashMap<String, Object>();
		series3.put("name", "合格率");
		series3.put("data",data3);
		result.put("series3", series3);

		result.put("max", 100);
		result.put("colModel",colModels);
		result.put("tabledata", tabledata);
		return result;
	}
	
	/**
	  * 方法名: 物料对比分析
	  * <p>功能说明：</p>
	  * @param params
	  * @return
	 */
	public Object materialRateChartDatas(JSONObject params){
		Map<String,Object> result = new HashMap<String, Object>();
		//表格的表头
		List<Object> colModels = new ArrayList<Object>();
		JSONObject modelJson = new JSONObject();
		modelJson.put("name","checkBomCode");
		modelJson.put("label","物料");
		modelJson.put("index","checkBomCode");
		colModels.add(modelJson);
		JSONObject modelJson1 = new JSONObject();
		modelJson1.put("name","inspection");
		modelJson1.put("label","检查批数");
		modelJson1.put("index","inspection");
		colModels.add(modelJson1);
		JSONObject modelJson2 = new JSONObject();
		modelJson2.put("name","quality");
		modelJson2.put("label","合格批数");
		modelJson2.put("index","quality");
		colModels.add(modelJson2);
		JSONObject modelJson3 = new JSONObject();
		modelJson3.put("name","rate");
		modelJson3.put("label","合格率");
		modelJson3.put("index","rate");
		colModels.add(modelJson3);
		
		result.put("title", "物料合格率对比图");
		result.put("subtitle","(" + params.getString("startDate_ge_date") + " - " + params.getString("endDate_le_date") + ")");
		List<String> categories = new ArrayList<String>();
		result.put("categories", categories);
		result.put("yAxisTitle1","批<br/>次<br/>数");
		result.put("yAxisTitle2","合<br/>格<br/>率");
		//查询语
		StringBuilder hql = new StringBuilder("select i.checkBomName,count(*) from IncomingInspectionActionsReport i where i.companyId = ?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if("date".equals(params.getString("myType"))){
			hql.append(" and i.yearMonthAndDate between ? and ?");
		}else{
			hql.append(" and i.yearAndMonth between ? and ?");
		}
		searchParams.add(Integer.valueOf(params.getString("startDate_ge_date").replaceAll("-","")));
		searchParams.add(Integer.valueOf(params.getString("endDate_le_date").replaceAll("-","")));
		CommonUtil1.buildHql(params, hql, searchParams,IncomingInspectionActionsReportManager.getFieldMap(),"i");
		List<Object> allList = incomingInspectionActionsReportDao.find(hql + " and i.inspectionConclusion is not null group by i.checkBomName order by count(*) desc",searchParams.toArray());
		List<Object> okList = incomingInspectionActionsReportDao.find(hql + " and i.inspectionConclusion = 'OK' group by i.checkBomName order by count(*) desc",searchParams.toArray());
		Map<String,Object[]> okMaps = new HashMap<String, Object[]>();
		for(Object obj : okList){
			Object[] objs = (Object[])obj;
			okMaps.put(objs[0]+"",objs);
		}
		List<Map<String,Object>> rateList = new ArrayList<Map<String,Object>>();
		int i=0;
		for(Object obj : allList){
			Object[] objs = (Object[])obj;
			Object[] okObjs = okMaps.get(objs[0]+"");
			Integer inspectionAmount = 0,okAmount = 0;
			if(objs[0] != null){
				inspectionAmount = Integer.valueOf(objs[1].toString());
			}
			if(okObjs != null && okObjs[1] != null){
				okAmount = Integer.valueOf(okObjs[1].toString());
			}
			double rate=0;
			if(inspectionAmount>0){
				rate=(okAmount*1.0/inspectionAmount)*100;
			}
			Map<String,Object> rateMap = new HashMap<String, Object>();
			rateMap.put("index",i++);
			rateMap.put("checkBomName",objs[0]+"");
			rateMap.put("inspectionAmount",inspectionAmount);
			rateMap.put("okAmount",okAmount);
			rateMap.put("rate",rate);
			rateList.add(rateMap);
		}
		Collections.sort(rateList,new Comparator<Map<String,Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				if((Double)o1.get("rate") < (Double)o2.get("rate")){
					return -1;
				}else if((Double)o1.get("rate") > (Double)o2.get("rate")){
					return 1;
				}else{
					return 0;
				}
			}
		});
		
		List<Integer> data = new ArrayList<Integer>();
		List<Map<String,Object>> data5 = new ArrayList<Map<String,Object>>();
		
		List<Map<String,Object>> data6 = new ArrayList<Map<String,Object>>();
		List<Integer> data2 = new ArrayList<Integer>();
		
		List<Double> data3 = new ArrayList<Double>();
		DecimalFormat  df=new DecimalFormat("0.00");
		List<Map<String,Object>> tabledata = new ArrayList<Map<String,Object>>();
		int count = 0;
		for(Map<String,Object> rateMap : rateList){
			Map<String,Object> map = new HashMap<String, Object>();
			String checkBomName = rateMap.get("checkBomName")+"";
			//横坐标天数的参数
			categories.add(checkBomName);
			map.put("materiel", checkBomName);
			map.put("name","检查批数");
			map.put("y",rateMap.get("inspectionAmount"));
			data5.add(map);
			data.add((Integer) rateMap.get("inspectionAmount"));
			//合格批数
			Map<String,Object> okMap = new HashMap<String, Object>();
			okMap.put("materiel", checkBomName);
			okMap.put("name","合格批数");
			okMap.put("in","合格批数");
			okMap.put("y", rateMap.get("okAmount"));
			data6.add(okMap);
			data2.add((Integer) rateMap.get("okAmount"));
			//批次合格率
			Double rate = (Double) rateMap.get("rate");
			data3.add(rate);
			//表格数据
			Map<String,Object> dataMap = new HashMap<String, Object>();
			dataMap.put("checkBomCode",checkBomName);
			dataMap.put("inspection",rateMap.get("inspectionAmount"));
			dataMap.put("quality",rateMap.get("okAmount"));
			dataMap.put("rate",df.format(rate));
			tabledata.add(dataMap);
			count++;
			if(count==15){
				break;
			}
		}
		
		//检验批数
		Map<String,Object> series1 = new HashMap<String, Object>();
		series1.put("name", "检验批数");
		ActionContext.getContext().put("categorieslist", categories);
		result.put("tableHeaderList", categories);
		series1.put("data",data5);
		result.put("series1", series1);
		//合格批数
		Map<String,Object> series2 = new HashMap<String, Object>();
		series2.put("name", "合格批数");
		series2.put("data",data6);
		result.put("series2", series2);
		//批次合格率
		Map<String,Object> series3 = new HashMap<String, Object>();
		series3.put("name", "合格率");
		series3.put("data",data3);
		result.put("series3", series3);
		result.put("colModel",colModels);
		result.put("tabledata", tabledata);
		result.put("max", 100);
		return result;
	}
	
	/**
	 * 检验项目分析
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getCheckTypeChartDatas(JSONObject params) throws Exception{
		Map<String,Object> result = new HashMap<String, Object>();
		//表格的表头
		List<Object> colModels = new ArrayList<Object>();
		JSONObject modelJson = new JSONObject();
		modelJson.put("name","type");
		modelJson.put("label","检验项目");
		modelJson.put("index","type");
		colModels.add(modelJson);
		JSONObject modelJson1 = new JSONObject();
		modelJson1.put("name","count");
		modelJson1.put("label","不良批数");
		modelJson1.put("index","count");
		colModels.add(modelJson1);
		
		result.put("title", "检验项目不良分析");
		result.put("subtitle","(" + params.getString("startDate_ge_date") + " - " + params.getString("startDate_ge_date") + ")");
		List<String> categories = new ArrayList<String>();
		result.put("categories", categories);
		result.put("yAxisTitle","不<br/>良<br/>批<br/>数");
		
		//不良批数
		Map<String,Object> series = new HashMap<String, Object>();
		series.put("name", "不良批数");
		List<Map<String,Object>> seriesData = new ArrayList<Map<String,Object>>();
		List<Integer> data = new ArrayList<Integer>();
		StringBuilder hql = new StringBuilder("select item.inspectionType,count(distinct i.id) from IncomingInspectionActionsReport i inner join i.checkItems item where i.companyId = ?  and item.conclusion = ?");
		if("date".equals(params.getString("myType"))){
			hql.append(" and i.yearMonthAndDate between ? and ?");
		}else{
			hql.append(" and i.yearAndMonth between ? and ?");
		}
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add("NG");
		searchParams.add(Integer.valueOf(params.getString("startDate_ge_date").replaceAll("-","")));
		searchParams.add(Integer.valueOf(params.getString("endDate_le_date").replaceAll("-","")));
		String oneMaterials="";
		String secondMaterials="";
		String threeMaterials="";
		for(Object key : params.keySet()){
			if("checkBomMaterialLevels".equals(key)){
				String [] leveIds=params.get(key).toString().split(",");
				for(String leveId:leveIds){
					String hqlLevel=" from MaterielTypeLevel leve where leve.id=? and leve.companyId=?";
					List<MaterielTypeLevel> levels=incomingInspectionActionsReportDao.find(hqlLevel,Long.valueOf(leveId),ContextUtils.getCompanyId());
					if(!levels.isEmpty()){
						MaterielTypeLevel level=levels.get(0);
						String levelName=level.getMaterielTypeName();
						if(level.getMaterielLevel()==1){
							oneMaterials+=levelName+",";
						}else if(level.getMaterielLevel()==2){
							secondMaterials+=levelName+",";
						}else if(level.getMaterielLevel()==3){
							threeMaterials+=levelName+",";
						}
					}
				}
				break;
			}
		}
		//全部
		if(!"".equals(oneMaterials)){
			oneMaterials=oneMaterials.substring(0, oneMaterials.length()-1);
			hql.append(" and i.oneLevelMateriel in ('"+oneMaterials.toString().replaceAll(",","','")+"')");
		}
		if(!"".equals(secondMaterials)){
			secondMaterials=secondMaterials.substring(0, secondMaterials.length()-1);
			hql.append(" and i.secondLevelMateriel in ('"+secondMaterials.toString().replaceAll(",","','")+"')");
		}
		if(!"".equals(threeMaterials)){
			threeMaterials=threeMaterials.substring(0, threeMaterials.length()-1);
			hql.append(" and i.threeLevelMateriel in ('"+threeMaterials.toString().replaceAll(",","','")+"')");
		}
		CommonUtil1.buildHql(params, hql, searchParams,IncomingInspectionActionsReportManager.getFieldMap(),"i");
		hql.append(" group by item.inspectionType");
		@SuppressWarnings("unchecked")
		List<Object> list = incomingInspectionActionsReportDao.createQuery(hql.toString(),searchParams.toArray()).list();
		Collections.sort(list,new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				Object[] o1Objs = (Object[])o1;
				Object[] o2Objs = (Object[])o2;
				if(o1Objs[1]==null||o2Objs[1]==null){
					return 0;
				}
				if(Integer.valueOf(o1Objs[1].toString()) < Integer.valueOf(o2Objs[1].toString())){
					return 1;
				}else if(Integer.valueOf(o1Objs[1].toString()) > Integer.valueOf(o2Objs[1].toString())){
					return -1;
				}else{
					return 0;
				}
			}
		});
		for(int i=0;i<list.size()&&i<15;i++){
			Object[] obj = (Object[])list.get(i);
			//横坐标参数
			categories.add(obj[0].toString());
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("type", obj[0].toString());
			map.put("count", "不良批数");
			map.put("y", obj[1].toString());
			seriesData.add(map);
			data.add(Integer.valueOf(obj[1].toString()));
		}
		
		List<Map<String,Object>> tabledata = new ArrayList<Map<String,Object>>();
		for(int i=0; i<seriesData.size(); i++){
			Map<String,Object> dataMap = new HashMap<String, Object>();
			dataMap.put("type", categories.get(i));
			dataMap.put("count", seriesData.get(i).get("y"));
			tabledata.add(dataMap);
		}
		ActionContext.getContext().put("categorieslist", categories);
		result.put("tableHeaderList", categories);
		series.put("data",seriesData);
		result.put("series", series);
		result.put("colModel",colModels);
		result.put("tabledata", tabledata);
		result.put("max", 100);
		return result;
	}
}

package com.ambition.spc.statistics.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.spc.baseinfo.service.CpkRuleManager;
import com.ambition.spc.entity.CpkRule;
import com.ambition.spc.entity.ProcessPoint;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.jlanalyse.entity.JLOriginalData;
import com.ambition.spc.jlanalyse.service.JlanalyseDrawManager;
import com.ambition.spc.jlanalyse.util.JLcalculator;
import com.ambition.spc.processdefine.service.ProcessDefineManager;
import com.ambition.spc.statistics.dao.SpcApplicationDao;
import com.ambition.util.exception.AmbFrameException;
import com.ibm.icu.text.SimpleDateFormat;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**    
 * SpcApplicationManager.java
 * @authorBy YUKE
 *
 */
@Service
public class SpcApplicationManager {
	@Autowired
	private SpcApplicationDao spcApplicationDao;
	@Autowired
	private ProcessDefineManager processDefineManager;
	@Autowired
	private JlanalyseDrawManager jlanalyseDrawManager;
	@Autowired
	private CpkRuleManager cpkRuleManager;
	@Autowired
	private CpMoudleanager cpMoudleanager;

	
	/**
	 * SPC应用状况报表
	 * @param params
	 * @return
	 */
	public Map<String,Object> getSpcApplicationDatas(JSONObject params){
		params = convertJsonObject(params);
		String startDateStr = params.getString("startDate_ge_date");
		String endDateStr = params.getString("endDate_le_date");
		
		//计算质量特性总数
		List<ProcessPoint> processPoints = new ArrayList<ProcessPoint>();
		int featureCount = 0,subGroupCount = 0;
		String processIds = "";
		if(params.containsKey("processId") && params.getString("processId") != null){
			processIds = params.getString("processId");
			featureCount = spcApplicationDao.countFeatureNumByPoint(processIds);
			subGroupCount = spcApplicationDao.countSubGroupByPoint(processIds);
			ProcessPoint processPoint = null;
			String[] ids = processIds.split(",");
			for(String id:ids){
				if(id != null){
					processPoint = new ProcessPoint();
					processPoint = processDefineManager.getProcessPoint(Long.valueOf(id));
					if(processPoint != null){
						processPoints.add(processPoint);
					}
				}
			}
		}else{
			processPoints = processDefineManager.getAllProcessPoint();
			for(int i = 0;i<processPoints.size();i++){
				if(processPoints.get(i) != null && processPoints.get(i).getId() != null){
					processIds += String.valueOf(processPoints.get(i).getId())+",";
				}
			}
			processIds = processIds.substring(0, processIds.length()-1);
			if(processIds.length() != 0){
				featureCount = spcApplicationDao.countFeatureNumByPoint(processIds);
				subGroupCount = spcApplicationDao.countSubGroupByPoint(processIds);
			}
		}
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("featureCount",featureCount);
		result.put("subGroupCount",subGroupCount);

		List<String> categories = new ArrayList<String>();
		//第一Y轴线的数据
		List<Map<String,Object>> dataY = new ArrayList<Map<String,Object>>();
		
		//计算CPK范围数量
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = sdf.parse(startDateStr);
			endDate = sdf.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Map<String,Integer> countMap = new HashMap<String,Integer>();
		int count67=0,count1=0,count133=0,count167=0,count=0;
		int count67_1=0,count1_1=0,count133_1=0,count167_1=0,count_1=0;
		for(ProcessPoint processPoint:processPoints){
			if(processPoint != null && processPoint.getQualityFeatures() != null){
				if(processPoint.getQualityFeatures().size()!=0){
					countMap = countCpkRange(processPoint.getQualityFeatures(),startDate,endDate);
					//0.67以下
					if(countMap.containsKey("count67")&&countMap.get("count67")!=null){
						count67 += countMap.get("count67");
					}
					//0.67-1.0
					if(countMap.containsKey("count1")&&countMap.get("count1")!=null){
						count1 += countMap.get("count1");
					}
					//1.0-1.33
					if(countMap.containsKey("count133")&&countMap.get("count133")!=null){
						count133 += countMap.get("count133");
					}
					//1.33-1.67
					if(countMap.containsKey("count167")&&countMap.get("count167")!=null){
						count167 += countMap.get("count167");
					}
					//1.67以上
					if(countMap.containsKey("count")&&countMap.get("count")!=null){
						count += countMap.get("count");
					}
				}
			}
		}
		countMap = new HashMap<String,Integer>();
		countMap.put("0.67以下", count67+count67_1);
		countMap.put("0.67-1.0", count1+count1_1);
		countMap.put("1.0-1.33", count133+count133_1);
		countMap.put("1.33-1.67", count167+count167_1);
		countMap.put("1.67以上", count+count_1);
		
		//表格需要的数据
		List<Map<String,Object>> tableData = new ArrayList<Map<String,Object>>();
		int total = 0;
		for(Entry<String, Integer> mycount:countMap.entrySet()){
			addTableDatas(mycount.getKey(),mycount.getValue(),featureCount,tableData);
			total += mycount.getValue(); 
		}
		result.put("total",total);
		Collections.sort(tableData,new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> o1,
					Map<String, Object> o2) {
				if(Integer.valueOf(o1.get("id").toString())>Integer.valueOf(o2.get("id").toString())){
					return 1;
				}else{
					return 0;
				}
			}
		});
		
		//报表需要的数据
		for(Map<String,Object> map:tableData){
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("name","column");
			if(map.containsKey("name")&&map.get("name")!=null){
				categories.add(map.get("name").toString());
			}
			if(map.containsKey("bi1")&&map.get("bi1")!=null){
				String bi =	map.get("bi1").toString().replace("%", "").trim();
				data.put("y",Double.valueOf(bi));
			}else{
				data.put("y",0.0);
			}
			if(map.containsKey("total")&&map.get("total")!=null){
				data.put("arg",Double.valueOf(map.get("total").toString()));
			}
			dataY.add(data);
		}
		result.put("categories", categories);
		result.put("tableData",tableData);
		
		List<Map<String,Object>> myTableData = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map:tableData){
			myTableData.add(map);
		}
		//添加合计行
		Map<String,Object> data = new HashMap<String, Object>();
		DecimalFormat df = new DecimalFormat("#.##%");
		data.put("name", "合计");
		data.put("total", total);
		if(total==0||featureCount==0){
			data.put("bi1", 0.00+"%");
		}else{
			data.put("bi1", df.format(total/(featureCount*1.0)));
		}
		myTableData.add(data);
		
		result.put("myTableData",myTableData);
		result.put("title","CPK水平分布");
		Map<String,Object> series = new HashMap<String, Object>();
		series.put("data",dataY);
		result.put("series", series);
		
		return result;
	}
	
	private Map<String,Integer> countCpkRange(List<QualityFeature> qualityFeatures,Date startDate,Date endDate){
		Map<String,Integer> map = new HashMap<String, Integer>();
		int count67=0, count1=0, count133=0, count167=0, count=0;
		for(QualityFeature qualityFeature:qualityFeatures){
			if(qualityFeature != null){
				//封装数据
				JLcalculator jLcalculator = new JLcalculator();
				JLOriginalData originalData = new JLOriginalData();
				//封装originalDate
				jlanalyseDrawManager.calculatJl(jLcalculator,qualityFeature.getId().toString(),startDate,endDate,originalData,"analysis","",new JSONArray(),null);
				Double cpk = jLcalculator.getjLResult().getCpkMoudle().getCpk();
				if(cpk < 0.67){
					count67++;
					map.put("count67", count67);
				}
				if(cpk>=0.67 && cpk<1.0){
					count1++;
					map.put("count1", count1);
				}
				if(cpk>=1.0 && cpk<1.33){
					count133++;
					map.put("count133", count133);
				}
				if(cpk>=1.33 && cpk<1.67){
					count167++;
					map.put("count167", count167);
				}
				if(cpk >= 1.67){
					count++;
					map.put("count", count);
				}
			}
		}
		return map;
	}
	
	private Map<String,String> getIdsByCpk(List<QualityFeature> qualityFeatures,String startDateStr,String endDateStr,String lastAmout, List<CpkRule> cpkRules){
//		Map<String,String> map = new HashMap<String, String>();
		
		Map<String,String> cpkIdsMap = new HashMap<String, String>();
		for (CpkRule cpkRule : cpkRules) {
			cpkIdsMap.put(cpkRule.getName(), "");
		}
		for(QualityFeature qualityFeature:qualityFeatures){
			if(qualityFeature != null){
				//封装数据
				JLcalculator jLcalculator = new JLcalculator();
				JLOriginalData originalData = new JLOriginalData();
				//封装originalDate
//				jlanalyseDrawManager.calculatJl(jLcalculator,qualityFeature.getId().toString(),startDate,endDate,originalData,"analysis","",new JSONArray(),null);
				cpMoudleanager.calculatJl(jLcalculator, originalData, "analysis", qualityFeature.getId().toString(), startDateStr, endDateStr, lastAmout,null);
				DecimalFormat df= new DecimalFormat("#0.00000"); 
				Double cpk = jLcalculator.getjLResult().getCpkMoudle().getCpk();
				//设置cpk值
				qualityFeature.setCpk(Double.valueOf(df.format(cpk)));
				
				Double ppk = jLcalculator.getjLResult().getCpkMoudle().getPpk();
				//设置ppk值
				qualityFeature.setPpk(Double.valueOf(df.format(ppk)));
				for (int i = 0; i < cpkRules.size(); i++) {
					int k = 0;
					if(null == cpkRules.get(i).getBelowLimit() || cpk >= cpkRules.get(i).getBelowLimit()){
						k++;
					}
					if(null==cpkRules.get(i).getUpLimit() || cpk < cpkRules.get(i).getUpLimit()){
						k++;
					}
					if(k == 2){
						cpkIdsMap.put(cpkRules.get(i).getName(), cpkIdsMap.get(cpkRules.get(i).getName()) + qualityFeature.getId().toString() + ",");
					}
				}
			}
		}
		return cpkIdsMap;
	}
	
	private void addTableDatas(String name,Integer count,Integer featureCount,List<Map<String,Object>> tableData){
		Map<String,Object> data = new HashMap<String, Object>();
		DecimalFormat df = new DecimalFormat("#.##%");
		if(name.equals("0.67以下")){
			data.put("id", 1);
		}
		if(name.equals("0.67-1.0")){
			data.put("id", 2);
		}
		if(name.equals("1.0-1.33")){
			data.put("id", 3);
		}
		if(name.equals("1.33-1.67")){
			data.put("id", 4);
		}
		if(name.equals("1.67以上")){
			data.put("id", 5);
		}
		data.put("name", name);
		data.put("total", count);
		if(count==0||featureCount==0){
			data.put("bi1", 0.00+"%");
		}else{
			data.put("bi1", df.format(count/(featureCount*1.0)));
		}
		tableData.add(data);
	}
	
	public Page<QualityFeature> searchByPage(Page<QualityFeature> page,JSONObject params){
		params = convertJsonObject(params);
		String startDateStr = params.getString("startDate_ge_date");
		String endDateStr = params.getString("endDate_le_date");
		String lastAmout= params.containsKey("lastAmout")?params.getString("lastAmout"):null;
		//计算质量特性总数
		List<ProcessPoint> processPoints = new ArrayList<ProcessPoint>();
		String processIds = "";
		if(params.containsKey("processId") && params.getString("processId") != null){
			processIds = params.getString("processId");
			ProcessPoint processPoint = null;
			String[] ids = processIds.split(",");
			for(String id:ids){
				if(id != null){
					processPoint = new ProcessPoint();
					processPoint = processDefineManager.getProcessPoint(Long.valueOf(id));
					if(processPoint != null){
						processPoints.add(processPoint);
					}
				}
			}
		}else{
			processPoints = processDefineManager.getAllProcessPoint();
		}
		
		List<CpkRule> cpkRules = cpkRuleManager.getAllList();
		
		if(cpkRules.isEmpty()){
			throw new AmbFrameException("CPK分组规则未维护！");
		}
		
		
		Map<String,String> cpkIdsMap = new HashMap<String, String>();
		for (CpkRule cpkRule : cpkRules) {
			cpkIdsMap.put(cpkRule.getName(), "");
		}
		
		//计算CPK范围数量
		for(ProcessPoint processPoint:processPoints){
			if(processPoint != null && processPoint.getQualityFeatures() != null){
				if(processPoint.getQualityFeatures().size()!=0){
					Map<String,String> countIdsMap = getIdsByCpk(processPoint.getQualityFeatures(),startDateStr,endDateStr,lastAmout,cpkRules);
					
					for (String key : cpkIdsMap.keySet()) {
						if(countIdsMap.containsKey(key) && !"".equals(countIdsMap.get(key))){
							String id = cpkIdsMap.get(key);
							cpkIdsMap.put(key, id += countIdsMap.get(key)+",");
						}
					}
//					
//					//0.67以下
//					if(countIdsMap.containsKey("0.67以下")&&countIdsMap.get("0.67以下")!=null){
//						ids67 = ids67 + countIdsMap.get("0.67以下") + ",";
//					}
//					//0.67-1.0
//					if(countIdsMap.containsKey("0.67-1.0")&&countIdsMap.get("0.67-1.0")!=null){
//						ids1 = ids1 + countIdsMap.get("0.67-1.0") + ",";
//					}
//					//1.0-1.33
//					if(countIdsMap.containsKey("1.0-1.33")&&countIdsMap.get("1.0-1.33")!=null){
//						ids133 = ids133 + countIdsMap.get("1.0-1.33") + ",";
//					}
//					//1.33-1.67
//					if(countIdsMap.containsKey("1.33-1.67")&&countIdsMap.get("1.33-1.67")!=null){
//						ids167 = ids167 + countIdsMap.get("1.33-1.67") + ",";
//					}
//					//1.67以上
//					if(countIdsMap.containsKey("1.67以上")&&countIdsMap.get("1.67以上")!=null){
//						ids = ids + countIdsMap.get("1.67以上") + ",";
//					}
				}
			}
		}
//		countIdsMap = new HashMap<String,String>();
//		countIdsMap.put("0.67以下", ids67);
//		countIdsMap.put("0.67-1.0", ids1);
//		countIdsMap.put("1.0-1.33", ids133);
//		countIdsMap.put("1.33-1.67", ids167);
//		countIdsMap.put("1.67以上", ids);
		
		String featrueIds = "";
		if(params.containsKey("range") && params.getString("range") != null){
			String range = params.getString("range").toString();
			featrueIds = cpkIdsMap.get(range);
		}
		
		String hql = "from QualityFeature q where q.companyId = ?";
		//查询条件
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		
		ArrayList<Long> paramIds = new ArrayList<Long>();
		if(featrueIds != null && !featrueIds.equals("")){
			String[] myids = featrueIds.split(",");
			Set<Long> idsets = new HashSet<Long>();
			for(String id:myids){
				if(id != null && !id.equals("")){
					idsets.add(Long.valueOf(id));
				}
			}
			Object[] obj = idsets.toArray();
			for(Object o:obj){
				paramIds.add(Long.valueOf(o.toString()));
			}
			if(paramIds != null){
				hql = hql + " and q.id in (:paramIds)";
			}
		}
		Query query = spcApplicationDao.getSession().createQuery(hql.toString());
		for(int i = 0;i < searchParams.size();i++){
			query.setParameter(i, searchParams.get(i));
		}
		if(paramIds != null){
			query.setParameterList("paramIds", paramIds);
		}
		
		Page<QualityFeature> qualityFeaturePage = new Page<QualityFeature>();
		qualityFeaturePage.setPageNo(page.getPageNo());
		@SuppressWarnings("unchecked")
		List<QualityFeature> list = query.list();
		if(list!=null && list.size()>0){
			qualityFeaturePage.setTotalCount(list.size());
		}
		List<QualityFeature> qualityFeatures = new ArrayList<QualityFeature>();
		int h=-1;
		int j = (page.getPageNo()-1)*page.getPageSize();
		for(QualityFeature obj : list){
			h++;
			if(h==j){
				j++;
			}else{
				continue;
			}
			if(j>page.getPageNo()*page.getPageSize()){
				continue;
			}
			qualityFeatures.add(obj);
		}
		
		qualityFeaturePage.setPageSize(page.getPageSize());
		qualityFeaturePage.setResult(new ArrayList<QualityFeature>());
		qualityFeaturePage.getResult().addAll(qualityFeatures);
		return qualityFeaturePage;
	}
	
	/**
	 * 转换json
	 * @param params
	 * @return
	 */
	public JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		if(params != null){
			for(Object key : params.keySet()){
				resultJson.put(key,params.getJSONArray(key.toString()).get(0));
			}
		}
		return resultJson;
	}
}

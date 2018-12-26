package com.ambition.spc.statistics.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.spc.dataacquisition.service.SpcDataManager;
import com.ambition.spc.entity.ControlLimit;
import com.ambition.spc.entity.FeatureLayer;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.jlanalyse.entity.JLOriginalData;
import com.ambition.spc.jlanalyse.entity.JLSampleData;
import com.ambition.spc.jlanalyse.service.JlanalyseDrawManager;
import com.ambition.spc.jlanalyse.util.JLcalculator;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.spc.util.Calculator;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.DateUtil;
import com.ibm.icu.text.SimpleDateFormat;
import com.opensymphony.xwork2.ActionContext;

@Service
public class StatisticsManager {

	@Autowired
	private JlanalyseDrawManager jlanalyseDrawManager;
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private SpcDataManager spcDataManager;
	@Autowired
	private CpkTrendDatasManager cpkTrendDatasManager;
	@Autowired
	private SpcApplicationDatasManager spcApplicationDatasManager;
	/**
	 * CPK过程能力考评表核心算法
	 * @param startDateStr
	 * @param endDateStr
	 * @param qualityFeatureIds
	 * @return
	 */
	public List<String[]> getcpkAppraisalTable (String startDateStr,String endDateStr,String qualityFeatureIds){
		String[] qualityFeatures=qualityFeatureIds.split(",");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date startDate=null;
		Date endDate=null;
		try {
			startDate = sdf.parse(startDateStr);
			endDate = sdf.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String[] qualityFeatureName=new String[qualityFeatures.length+1];
		qualityFeatureName[0]="统计量\\质量特性";
		List<String[]> result=new ArrayList<String[]>();
		//统计量
		String[] mean=new String[qualityFeatures.length+1];
		mean[0]="Mean";
		String[] Max=new String[qualityFeatures.length+1];
		Max[0]="Max";
		String[] Min=new String[qualityFeatures.length+1];
		Min[0]="Min";
		String[] Range=new String[qualityFeatures.length+1];
		Range[0]="Range";
		String[] StdDev=new String[qualityFeatures.length+1];
		StdDev[0]="StdDev";
		String[] Skewness=new String[qualityFeatures.length+1];
		Skewness[0]="Skewness";
		String[] Kurtosis=new String[qualityFeatures.length+1];
		Kurtosis[0]="Kurtosis";
		String[] Cp=new String[qualityFeatures.length+1];
		Cp[0]="Cp";
		String[] Cr=new String[qualityFeatures.length+1];
		Cr[0]="Cr";
		String[] K=new String[qualityFeatures.length+1];
		K[0]="K";
		String[] Cpu=new String[qualityFeatures.length+1];
		Cpu[0]="Cpu";
		String[] Cpl=new String[qualityFeatures.length+1];
		Cpl[0]="Cpl";
		String[] Cpk=new String[qualityFeatures.length+1];
		Cpk[0]="Cpk";
		String[] Cpm=new String[qualityFeatures.length+1];
		Cpm[0]="Cpm";
		String[] Zu_Cap=new String[qualityFeatures.length+1];
		Zu_Cap[0]="Zu_Cap";
		String[] Zl_Cap=new String[qualityFeatures.length+1];
		Zl_Cap[0]="Zl_Cap";
		String[] Fpu_Cap=new String[qualityFeatures.length+1];
		Fpu_Cap[0]="Fpu_Cap";
		String[] Fpl_Cap=new String[qualityFeatures.length+1];
		Fpl_Cap[0]="Fpl_Cap";
		String[] Fp_Cap=new String[qualityFeatures.length+1];
		Fp_Cap[0]="Fp_Cap";
		String[] Pp=new String[qualityFeatures.length+1];
		Pp[0]="Pp";
		String[] Pr=new String[qualityFeatures.length+1];
		Pr[0]="Pr";
		String[] Ppu=new String[qualityFeatures.length+1];
		Ppu[0]="Ppu";
		String[] Ppl=new String[qualityFeatures.length+1];
		Ppl[0]="Ppl";
		String[] Ppk=new String[qualityFeatures.length+1];
		Ppk[0]="Ppk";
		String[] Ppm=new String[qualityFeatures.length+1];
		Ppm[0]="Ppm";
		String[] Zu_Perf=new String[qualityFeatures.length+1];
		Zu_Perf[0]="Zu_Perf";
		String[] Zl_Perf=new String[qualityFeatures.length+1];
		Zl_Perf[0]="Zl_Perf";
		String[] Fpu_Perf=new String[qualityFeatures.length+1];
		Fpu_Perf[0]="Fpu_Perf";
		String[] Fpl_Perf=new String[qualityFeatures.length+1];
		Fpl_Perf[0]="Fpl_Perf";
		String[] Fp_Perf=new String[qualityFeatures.length+1];
		Fp_Perf[0]="Fp_Perf";
		
		for(int i=0;i<qualityFeatures.length;i++){
			//封装表头
			String qualityFeatureId=qualityFeatures[i];
			QualityFeature qualityFeature=qualityFeatureManager.getQualityFeature(Long.parseLong(qualityFeatureId));
			qualityFeatureName[i+1]=qualityFeature.getName();
			//封装数据
			JLcalculator jLcalculator=new JLcalculator();
			JLOriginalData originalData=new JLOriginalData();
			//封装originalDate
			jlanalyseDrawManager.calculatJl(jLcalculator, qualityFeatureId,startDate,endDate,originalData,"analysis","",new JSONArray(),null);
			mean[i+1]=String.valueOf(jLcalculator.getjLResult().getAverage());
			Max[i+1]=String.valueOf(jLcalculator.getjLResult().getMax());
			Min[i+1]=String.valueOf(jLcalculator.getjLResult().getMin());
			Range[i+1]=String.valueOf(jLcalculator.getjLResult().getR());
			StdDev[i+1]=String.valueOf(jLcalculator.getjLResult().getS());
			Skewness[i+1]=String.valueOf(jLcalculator.getjLResult().getSkewness());
			Kurtosis[i+1]=String.valueOf(jLcalculator.getjLResult().getKurtosis());
			Cp[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getCp());
			Cr[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getCr());
			K[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getK());
			Cpu[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getCpu());
			Cpl[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getCpl());
			Cpk[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getCpk());
			Cpm[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getCpm());
			Zu_Cap[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getZu_cap());
			Zl_Cap[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getZl_cap());
			Fpu_Cap[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getFpu_cap());
			Fpl_Cap[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getFpl_cap());
			Fp_Cap[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getFp_cap());
			Pp[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getPp());
			Pr[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getPr());
			Ppu[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getPpu());
			Ppl[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getPpl());
			Ppk[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getPpk());
			Ppm[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getPpm());
			Zu_Perf[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getZu_pref());
			Zl_Perf[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getZl_pref());
			Fpu_Perf[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getFpu_pref());
			Fpl_Perf[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getFpl_pref());
			Fp_Perf[i+1]=String.valueOf(jLcalculator.getjLResult().getCpkMoudle().getFp_pref());
		}
		result.add(mean);
		result.add(Max);
		result.add(Min);
		result.add(Range);
		result.add(StdDev);
		result.add(Skewness);
		result.add(Kurtosis);
		result.add(Cp);
		result.add(Cr);
		result.add(K);
		result.add(Cpu);
		result.add(Cpl);
		result.add(Cpk);
		result.add(Cpm);
		result.add(Zu_Cap);
		result.add(Zl_Cap);
		result.add(Fpu_Cap);
		result.add(Fpl_Cap);
		result.add(Fp_Cap);
		result.add(Pp);
		result.add(Pr);
		result.add(Ppu);
		result.add(Ppl);
		result.add(Ppk);
		result.add(Ppm);
		result.add(Zu_Perf);
		result.add(Zl_Perf);
		result.add(Fpu_Perf);
		result.add(Fpl_Perf);
		result.add(Fp_Perf);
		ActionContext.getContext().put("qualityFeatureName", qualityFeatureName);
		return result;
	}
	
	/**
	 * CPK推移图
	 * @param params
	 * @return
	 */
	public Map<String,Object> getCpkTrendDatas(JSONObject params){
		params = jlanalyseDrawManager.convertJsonObject(params);
		String startDateStr=params.getString("startDate_ge_date");
		String endDateStr=params.getString("endDate_le_date");
		String qualityFeatureIds=params.getString("qualityFeatures");
		String group=params.getString("group");
		String[] qualityFeatures=null;
		if(qualityFeatureIds!=null){
			qualityFeatures=qualityFeatureIds.split(",");
		}
		//组装报表数据
		Map<String,Object> result = new HashMap<String, Object>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = sdf.parse(startDateStr);
			endDate = sdf.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		List<String> categories = new ArrayList<String>();
		Map<String,List<Object>> dataMap = new HashMap<String, List<Object>>();
		for(int i=0;i<qualityFeatures.length;i++){
			dataMap.put(qualityFeatures[i], new ArrayList<Object>());
		}
		while(startCalendar.getTimeInMillis() <= endDate.getTime()){
			categories.add(startCalendar.get(Calendar.DATE)+"");
			Date startdate = startCalendar.getTime();
			startCalendar.add(Calendar.DAY_OF_YEAR, 1);
			Date enddate=startCalendar.getTime();
			if(qualityFeatureIds!=null){
				for(int i=0;i<qualityFeatures.length;i++){
					String featureId=qualityFeatures[i];
					List<Object> dataList = dataMap.get(featureId);
					//封装数据
					JLcalculator jLcalculator=new JLcalculator();
					JLOriginalData originalData=new JLOriginalData();
					//封装originalDate
					jlanalyseDrawManager.calculatJl(jLcalculator,featureId,startdate,enddate,originalData,"analysis","",new JSONArray(),params);
					Map<String,Object> data = new HashMap<String, Object>();
					if(group.equals("CPK")){
						Double cpk=jLcalculator.getjLResult().getCpkMoudle().getCpk();
						data.put("y",cpk);
					}else if(group.equals("MEAN")){
						Double MEAN=jLcalculator.getjLResult().getAverage();
						data.put("y",MEAN);
					}else if(group.equals("StdDve")){
						Double StdDve=jLcalculator.getjLResult().getS();
						data.put("y",StdDve);
					}else if(group.equals("SIGMA")){
						Double SIGMA=jLcalculator.getjLResult().getCpkMoudle().getSigma();
						data.put("y",SIGMA);
					}else if(group.equals("Fpu(Perf)")){
						Double FpuPerf=jLcalculator.getjLResult().getCpkMoudle().getFpu_pref();
						data.put("y",FpuPerf);
					}
					dataList.add(data);
				}
			}
		}
		List<Map<String,Object>> series = new ArrayList<Map<String,Object>>();
		for(int i=0;i<qualityFeatures.length;i++){
			Map<String,Object> serie = new HashMap<String, Object>();
			QualityFeature qualityFeature=qualityFeatureManager.getQualityFeature(Long.parseLong(qualityFeatures[i]));
			serie.put("name", qualityFeature.getName());
			serie.put("data",dataMap.get(qualityFeatures[i]));
			series.add(serie);
		}
		result.put("series",series);
		result.put("title",  group+"推移图");
		result.put("subtitle","(" + sdf.format(startDate) + " - " + sdf.format(endDate) + ")");
		result.put("categories", categories);
		return result;
	}
	
	
	/**
	 * CPK推移图
	 * @param params
	 * @return
	 */
	public Map<String,Object> getSignCpkTrendDatas(JSONObject params){
		params = CommonUtil1.convertJsonObject(params);
		String startDateStr=params.getString("startDate_ge_date");
		String endDateStr=params.getString("endDate_le_date");
		String qualityFeatureId=params.getString("qualityFeature");
		String tagName = "";
		if(params.containsKey("tagName")){
			tagName = params.getString("tagName");
		}
		String tagCode = params.getString("tagCode");
		String[] tagValues = params.getString(tagCode).split(",");
//		String group="";
//		if(params.containsKey("group")){
//			group=params.getString("group");
//		}
		Integer lastAmount = null;
		if(params.containsKey("lastAmount")){
			String amountStr = params.getString("lastAmount");
			if(CommonUtil1.isInteger(amountStr)){
				lastAmount = Integer.valueOf(amountStr);
			}
		}
		//组装报表数据
		Map<String,Object> result = new HashMap<String, Object>();
		Date startDate = DateUtil.parseDate(startDateStr);
		Date endDate = DateUtil.parseDate(endDateStr);
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		List<String> categories = new ArrayList<String>();
		Map<String,Map<String,Object>> valueMap = new HashMap<String, Map<String,Object>>();
		QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureFromCache(Long.valueOf(qualityFeatureId),null);
		List<String> seriesNames = new ArrayList<String>();
		for(String tagValue:tagValues){
			tagValue = tagValue.trim();
			if(StringUtils.isEmpty(tagValue)){
				continue;
			}
			if(valueMap.containsKey(tagValue)){
				continue;
			}
			JSONObject layerParams = new JSONObject();
			layerParams.put(tagCode,tagValue);
			Map<String,Object> resultMap = spcDataManager.querySpcDataValues(qualityFeature, startDateStr, endDateStr, layerParams,1,null, lastAmount);
			valueMap.put(tagValue,resultMap);
			seriesNames.add(tagValue);
		}
		//以名称_日期作key,查询对应的数据
		Map<String,List<double[]>> serieDateMap = new HashMap<String,List<double[]>>();
		for(String serieName : valueMap.keySet()){
			Map<String,Object> resultMap = valueMap.get(serieName);
			@SuppressWarnings("unchecked")
			List<double[]> values = (List<double[]>)resultMap.get("values");
			JSONArray posDatas = (JSONArray)resultMap.get("posDatas");
			for(int i=0;i<values.size();i++){
				JSONObject posData = posDatas.getJSONObject(i);
				String dateStr = posData.getString("date");
				String key = serieName + "_" + dateStr;
				if(!serieDateMap.containsKey(key)){
					serieDateMap.put(key,new ArrayList<double[]>());
				}
				serieDateMap.get(key).add(values.get(i));
			}
		}
		
		//查询控制限
		Map<String,List<ControlLimit>> controlLimitMap = new HashMap<String, List<ControlLimit>>();
		spcApplicationDatasManager.searchControlLimits(null,qualityFeatureId, controlLimitMap);
		
		Map<String,List<Object>> dataMap = new HashMap<String, List<Object>>();
		while(startCalendar.getTimeInMillis() <= endDate.getTime()){
			String day = startCalendar.get(Calendar.DATE)+"";
			categories.add(day);
			startCalendar.add(Calendar.DAY_OF_YEAR, 1);
			
			String dateStr = DateUtil.formateDateStr(startCalendar);
			for(String tagValue : tagValues){
				if(!dataMap.containsKey(tagValue)){
					dataMap.put(tagValue,new ArrayList<Object>());
				}
				List<Object> dataList = dataMap.get(tagValue);
				
				Map<String,Object> data = new HashMap<String, Object>();
				
				String key = tagValue + "_" + dateStr;
				List<double[]> values = serieDateMap.get(key);
				if(values != null){
					//封装数据
					JLcalculator jLcalculator=new JLcalculator();
					JLOriginalData originalData=new JLOriginalData();
					//封装originalDate
					cpkTrendDatasManager.calculatJl(jLcalculator,qualityFeature,values,controlLimitMap.get(qualityFeatureId),originalData,null);
					Double cpk=jLcalculator.getjLResult().getCpkMoudle().getCpk();
					data.put("y",cpk);
				}
				dataList.add(data);
			}
		}
		List<Map<String,Object>> series = new ArrayList<Map<String,Object>>();
		for(String tagValue : tagValues){
			Map<String,Object> serie = new HashMap<String, Object>();
			serie.put("name", tagValue);
			serie.put("data",dataMap.get(tagValue));
			series.add(serie);
		}
		result.put("series",series);
		result.put("title",  tagName+"CPK对比推移图");
		result.put("subtitle","(" + DateUtil.formateDateStr(startDate) + " - " + DateUtil.formateDateStr(endDate) + ")");
		result.put("categories", categories);
		return result;
	}
	
	/**
	 * 数据分析子组数据
	 * @param featureId
	 * @param startDate
	 * @param endDate
	 */
	@SuppressWarnings({ "unchecked" })
	public Map<String,Object> getDateTable(String featureId,String startDateStr,String endDateStr,String lastAmoutStr,JSONObject layerParams){
		QualityFeature qualityFeature=new QualityFeature();
		Map<String,Object> result = new HashMap<String, Object>();
		
		if (featureId != null && !"".equals(featureId)) {
			//质量特性
			qualityFeature = qualityFeatureManager.getQualityFeatureFromCache(Long.valueOf(featureId),null);
		}
		Integer sampleCapacity = qualityFeature.getSampleCapacity();
		if(qualityFeature.getId()!=null && !"".equals(qualityFeature.getId())){
			Integer lastAmount = null;
			if(CommonUtil1.isInteger(lastAmoutStr)){
				lastAmount = Integer.valueOf(lastAmoutStr);
			}
			Map<String,Object> resultMap = spcDataManager.querySpcDataValues(qualityFeature, startDateStr, endDateStr, layerParams, 1,null, lastAmount);
			List<double[]> values = (List<double[]>)resultMap.get("values");
			JSONArray posDatas = (JSONArray)resultMap.get("posDatas");
			
			//封装每组样本数据的最大值、最小值、平均值等
			ArrayList<JLSampleData> datalist=new ArrayList<JLSampleData>();
			for(int i=0;i<values.size();i++){
				JLSampleData jl=new JLSampleData();
				double[] a=(double[])values.get(i);
				jl.setData(a);
				jl.setAverage(Calculator.average(a));
				jl.setMax(Calculator.max(a));
				jl.setMin(Calculator.min(a));
				jl.setS(Calculator.calculateS(a));
				jl.setR(Calculator.calculateR(a));
				jl.setMedian(Calculator.calculateMedian(a));
				datalist.add(jl);
			}
			
			List<Object> colModels = new ArrayList<Object>();//表格的表头
			List<Map<String,Object>> tabledata = new ArrayList<Map<String,Object>>();//表格数据
			JSONObject modelJson = new JSONObject();
			modelJson.put("name","no");
			modelJson.put("label","子组号");
			modelJson.put("sorttype","integer");
			modelJson.put("index","no");
			modelJson.put("width","80");
			modelJson.put("align","center");
			colModels.add(modelJson);
			for(int i=0;i<sampleCapacity;i++){
				JSONObject modelJsonx = new JSONObject();
				modelJsonx.put("name","x"+i);
				modelJsonx.put("label","x"+(i+1));
				modelJsonx.put("index","x"+i);
				modelJsonx.put("width","60");
				modelJsonx.put("align","center");
				colModels.add(modelJsonx);
			}
			//1-均值-极差;2-均值-标准差;4-单值-移动极差
			if(!qualityFeature.getControlChart().equals("4")){
				JSONObject modelJson1 = new JSONObject();
				modelJson1.put("name","average");
				modelJson1.put("label","均值");
				modelJson1.put("index","average");
				modelJson1.put("width","80");
				modelJson1.put("align","center");
				colModels.add(modelJson1);
				JSONObject modelJson2 = new JSONObject();
				modelJson2.put("name","max");
				modelJson2.put("label","最大值");
				modelJson2.put("index","max");
				modelJson2.put("width","80");
				modelJson2.put("align","center");
				colModels.add(modelJson2);
				JSONObject modelJson3 = new JSONObject();
				modelJson3.put("name","min");
				modelJson3.put("label","最小值");
				modelJson3.put("index","min");
				modelJson3.put("width","80");
				modelJson3.put("align","center");
				colModels.add(modelJson3);
				JSONObject modelJson4 = new JSONObject();
				modelJson4.put("name","range");
				modelJson4.put("label","极差");
				modelJson4.put("index","range");
				modelJson4.put("width","80");
				modelJson4.put("align","center");
				colModels.add(modelJson4);
				JSONObject modelJsonDev = new JSONObject();
				modelJsonDev.put("name","stdDev");
				modelJsonDev.put("label","标准差");
				modelJsonDev.put("index","stdDev");
				modelJsonDev.put("width","80");
				modelJsonDev.put("align","center");
				colModels.add(modelJsonDev);
			}else{
				JSONObject modelJsonMobileRange= new JSONObject();
				modelJsonMobileRange.put("name","mobileRange");
				modelJsonMobileRange.put("label","移动极差");
				modelJsonMobileRange.put("index","mobileRange");
				modelJsonMobileRange.put("width","80");
				modelJsonMobileRange.put("align","center");
				colModels.add(modelJsonMobileRange);
			}
			
			JSONObject modelJson5 = new JSONObject();
			modelJson5.put("name","time");
			modelJson5.put("label","采集时间");
			modelJson5.put("index","time");
			modelJson5.put("width","135");
			modelJson5.put("align","center");
			colModels.add(modelJson5);
			
			List<FeatureLayer> featureLayers = qualityFeature.getFeatureLayers();
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
			Double numberTemp=null;
			Integer precs = null;
			if(CommonUtil1.isInteger(qualityFeature.getPrecs())){
				precs = Integer.valueOf(qualityFeature.getPrecs());
			}
			for(int i=0;i<datalist.size();i++){
				JLSampleData jl=(JLSampleData)datalist.get(i);
				Integer groupNo = i+1;
				double[] datas=jl.getData();
				Map<String,Object> dataMap = new HashMap<String, Object>();
				dataMap.put("no", groupNo);//子组号
				for(int h=0;h<datas.length;h++){
					double number=datas[h];
					dataMap.put("x"+h, number);//x1,X2,X3
				}
				//1-均值-极差;2-均值-标准差;4-单值-移动极差
				if(!qualityFeature.getControlChart().equals("4")){
					dataMap.put("average", SpcDataManager.formatValue(jl.getAverage(),precs));//均值
					dataMap.put("max", SpcDataManager.formatValue(jl.getMax(),precs));//最大值
					dataMap.put("min", SpcDataManager.formatValue(jl.getMin(),precs));//最小值
					dataMap.put("range", SpcDataManager.formatValue(jl.getR(),precs));//极差
					dataMap.put("stdDev",SpcDataManager.formatValue(jl.getS(),precs));//标准差
				}else{
					if(numberTemp==null){//只有一组数据没有移动极差
						numberTemp=datas[0];
						dataMap.put("mobileRange","");//移动极差
					}else{
						dataMap.put("mobileRange",SpcDataManager.formatValue(Math.abs(datas[0]-numberTemp),precs));//移动极差
						numberTemp=datas[0];
					}
					
				}
				JSONObject posData = posDatas.getJSONObject(i);
				dataMap.put("time",posData.getString("date"));//采集时间
				for(int j=0;j<featureLayers.size();j++){
					FeatureLayer f=featureLayers.get(j);
					String detailCode = f.getDetailCode();
					if(posData.containsKey(detailCode)){
						dataMap.put(detailCode,posData.get(detailCode));
					}
				}
				tabledata.add(dataMap);
			}
			result.put("size", values.size());
			result.put("colModel",colModels);
			result.put("tabledata", tabledata);
			result.put("max", 100);
		}
		return result;
	}
}
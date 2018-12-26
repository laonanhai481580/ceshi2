package com.ambition.spc.statistics.service;

import java.lang.reflect.Method;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.spc.dataacquisition.service.SpcDataManager;
import com.ambition.spc.entity.FeatureLayer;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.entity.SpcSgSample;
import com.ambition.spc.entity.SpcSubGroup;
import com.ambition.spc.jlanalyse.entity.JLOriginalData;
import com.ambition.spc.jlanalyse.entity.JLSampleData;
import com.ambition.spc.jlanalyse.service.JlanalyseDrawManager;
import com.ambition.spc.jlanalyse.util.JLcalculator;
import com.ambition.spc.processdefine.dao.QualityFeatureDao;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.spc.util.Calculator;
import com.ambition.spc.util.StringUtil;
import com.ambition.util.common.CommonUtil1;
import com.ibm.icu.text.SimpleDateFormat;
import com.opensymphony.xwork2.ActionContext;

@Service
public class CpkAppraisalTableManager {

	@Autowired
	private JlanalyseDrawManager jlanalyseDrawManager;
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private CpkTrendDatasManager cpkTrendDatasManager;
	@Autowired
	private QualityFeatureDao qualityFeatureDao;
	/**
	 * CPK过程能力考评表核心算法
	 * @param startDateStr
	 * @param endDateStr
	 * @param qualityFeatureIds
	 * @return
	 * @throws InterruptedException 
	 */
	public List<String[]> getcpkAppraisalTable (String startDateStr,String endDateStr,String qualityFeatureIds,JSONObject layerParams) throws InterruptedException{
		String[] qualityFeatureIdArr=qualityFeatureIds.split(",");
		String[] qualityFeatureName = new String[qualityFeatureIdArr.length+1];
		qualityFeatureName[0]="统计量\\质量特性";
		List<String[]> result=new ArrayList<String[]>();
		//统计量
		String[] mean=new String[qualityFeatureIdArr.length+1];
		mean[0]="Mean";
		String[] Max=new String[qualityFeatureIdArr.length+1];
		Max[0]="Max";
		String[] Min=new String[qualityFeatureIdArr.length+1];
		Min[0]="Min";
		String[] Range=new String[qualityFeatureIdArr.length+1];
		Range[0]="Range";
		String[] StdDev=new String[qualityFeatureIdArr.length+1];
		StdDev[0]="StdDev";
		String[] Skewness=new String[qualityFeatureIdArr.length+1];
		Skewness[0]="Skewness";
		String[] Kurtosis=new String[qualityFeatureIdArr.length+1];
		Kurtosis[0]="Kurtosis";
		String[] Cp=new String[qualityFeatureIdArr.length+1];
		Cp[0]="Cp";
		String[] Cr=new String[qualityFeatureIdArr.length+1];
		Cr[0]="Cr";
		String[] K=new String[qualityFeatureIdArr.length+1];
		K[0]="K";
		String[] Cpu=new String[qualityFeatureIdArr.length+1];
		Cpu[0]="Cpu";
		String[] Cpl=new String[qualityFeatureIdArr.length+1];
		Cpl[0]="Cpl";
		String[] Cpk=new String[qualityFeatureIdArr.length+1];
		Cpk[0]="Cpk";
		String[] Cpm=new String[qualityFeatureIdArr.length+1];
		Cpm[0]="Cpm";
		String[] Zu_Cap=new String[qualityFeatureIdArr.length+1];
		Zu_Cap[0]="Zu_Cap";
		String[] Zl_Cap=new String[qualityFeatureIdArr.length+1];
		Zl_Cap[0]="Zl_Cap";
		String[] Fpu_Cap=new String[qualityFeatureIdArr.length+1];
		Fpu_Cap[0]="Fpu_Cap";
		String[] Fpl_Cap=new String[qualityFeatureIdArr.length+1];
		Fpl_Cap[0]="Fpl_Cap";
		String[] Fp_Cap=new String[qualityFeatureIdArr.length+1];
		Fp_Cap[0]="Fp_Cap";
		String[] Pp=new String[qualityFeatureIdArr.length+1];
		Pp[0]="Pp";
		String[] Pr=new String[qualityFeatureIdArr.length+1];
		Pr[0]="Pr";
		String[] Ppu=new String[qualityFeatureIdArr.length+1];
		Ppu[0]="Ppu";
		String[] Ppl=new String[qualityFeatureIdArr.length+1];
		Ppl[0]="Ppl";
		String[] Ppk=new String[qualityFeatureIdArr.length+1];
		Ppk[0]="Ppk";
		String[] Ppm=new String[qualityFeatureIdArr.length+1];
		Ppm[0]="Ppm";
		String[] Zu_Perf=new String[qualityFeatureIdArr.length+1];
		Zu_Perf[0]="Zu_Perf";
		String[] Zl_Perf=new String[qualityFeatureIdArr.length+1];
		Zl_Perf[0]="Zl_Perf";
		String[] Fpu_Perf=new String[qualityFeatureIdArr.length+1];
		Fpu_Perf[0]="Fpu_Perf";
		String[] Fpl_Perf=new String[qualityFeatureIdArr.length+1];
		Fpl_Perf[0]="Fpl_Perf";
		String[] Fp_Perf=new String[qualityFeatureIdArr.length+1];
		Fp_Perf[0]="Fp_Perf";
		//spc数据
		Map<String,List<Object>> dataMap = new HashMap<String, List<Object>>();
		for(int i=0;i<qualityFeatureIdArr.length;i++){
			dataMap.put(qualityFeatureIdArr[i], new ArrayList<Object>());
		}
		List<QualityFeature> qualityFeatures = new ArrayList<QualityFeature>();
		for(String featureId : qualityFeatureIdArr){
			qualityFeatures.add(qualityFeatureManager.getQualityFeatureFromCache(Long.valueOf(featureId),null));
		}
		SpcDataManager spcDataManager = new SpcDataManager(qualityFeatureDao.getSession());
		
		Map<String,Map<String,Object>> featureDataMap = spcDataManager.multiQuerySpcDataValues(qualityFeatures, startDateStr, endDateStr, layerParams,1, null,null);
		
		for(int i=0;i<qualityFeatures.size();i++){
			QualityFeature qualityFeature = qualityFeatures.get(i);
			qualityFeatureName[i+1]=qualityFeature.getName();
			//封装表头
			String qualityFeatureId = qualityFeature.getId().toString();
			
			Map<String,Object> resultMap = featureDataMap.get(qualityFeatureId);
			
			@SuppressWarnings("unchecked")
			List<double[]> values = (List<double[]>)resultMap.get("values");
			if(values == null){
				values = new ArrayList<double[]>();
			}
			//封装数据
			JLcalculator jLcalculator=new JLcalculator();
			JLOriginalData originalData=new JLOriginalData();
			//封装originalDate
			cpkTrendDatasManager.calculatJl(jLcalculator, 
					qualityFeature, values,qualityFeature.getControlLimits(), originalData,null);
			Integer precs = 0;
			if(CommonUtil1.isInteger(qualityFeature.getPrecs())){
				precs = Integer.valueOf(qualityFeature.getPrecs());
			}
			mean[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getAverage(),precs);
			Max[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getMax(),precs);
			Min[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getMin(),precs);
			Range[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getR(),precs);
			StdDev[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getS(),precs);
			Skewness[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getSkewness(),precs);
			Kurtosis[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getKurtosis(),precs);
			Cp[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getCp(),precs);
			Cr[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getCr(),precs);
			K[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getK(),precs);
			Cpu[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getCpu(),precs);
			Cpl[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getCpl(),precs);
			Cpk[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getCpk(),precs);
			Cpm[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getCpm(),precs);
			Zu_Cap[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getZu_cap(),precs);
			Zl_Cap[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getZl_cap(),precs);
			Fpu_Cap[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getFpu_cap(),precs);
			Fpl_Cap[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getFpl_cap(),precs);
			Fp_Cap[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getFp_cap(),precs);
			Pp[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getPp(),precs);
			Pr[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getPr(),precs);
			Ppu[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getPpu(),precs);
			Ppl[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getPpl(),precs);
			Ppk[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getPpk(),precs);
			Ppm[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getPpm(),precs);
			Zu_Perf[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getZu_pref(),precs);
			Zl_Perf[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getZl_pref(),precs);
			Fpu_Perf[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getFpu_pref(),precs);
			Fpl_Perf[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getFpl_pref(),precs);
			Fp_Perf[i+1]=StringUtil.formatdouble(jLcalculator.getjLResult().getCpkMoudle().getFp_pref(),precs);
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
	@SuppressWarnings("unused")
	public Map<String,Object> getSignCpkTrendDatas(JSONObject params){
		params = jlanalyseDrawManager.convertJsonObject(params);
		String startDateStr=params.getString("startDate_ge_date");
		String endDateStr=params.getString("endDate_le_date");
		String qualityFeatureId=params.getString("qualityFeatures");
		String tag_value=null;
		String tag_code=null;
		String group="";
		if(params.containsKey("tag_value")){
			tag_value=params.getString("tag_value");
		}
		if(params.containsKey("tag_code")){
			tag_code=params.getString("tag_code");
		}
		if(params.containsKey("group")){
			group=params.getString("group");
		}
		String[] tag_values=null;
		if(tag_value!=null){
			tag_values=tag_value.split(",");
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
		if(tag_values!=null){
			for(int i=0;i<tag_values.length;i++){
				dataMap.put(tag_values[i], new ArrayList<Object>());
			}
		}
		while(startCalendar.getTimeInMillis() <= endDate.getTime()){
			categories.add(startCalendar.get(Calendar.DATE)+"");
			Date startdate = startCalendar.getTime();
			startCalendar.add(Calendar.DAY_OF_YEAR, 1);
			Date enddate=startCalendar.getTime();
			if(tag_values!=null){
				for(int i=0;i<tag_values.length;i++){
					String value=tag_values[i];
					List<Object> dataList = dataMap.get(value);
					//封装数据
					JLcalculator jLcalculator=new JLcalculator();
					JLOriginalData originalData=new JLOriginalData();
					//封装originalDate
					jlanalyseDrawManager.cpkCalculator(jLcalculator,qualityFeatureId,startdate,enddate,originalData,group,value,params);
					Double cpk=jLcalculator.getjLResult().getCpkMoudle().getCpk();
					Map<String,Object> data = new HashMap<String, Object>();
					data.put("y",cpk);
					dataList.add(data);
				}
			}
		}
		List<Map<String,Object>> series = new ArrayList<Map<String,Object>>();
		if(tag_values!=null){
		for(int i=0;i<tag_values.length;i++){
			Map<String,Object> serie = new HashMap<String, Object>();
			serie.put("name", tag_values[i]);
			serie.put("data",dataMap.get(tag_values[i]));
			series.add(serie);
		}
		}
		result.put("series",series);
		result.put("title",  tag_value==null?"CPK对比推移图":tag_value+"CPK对比推移图");
		result.put("subtitle","(" + sdf.format(startDate) + " - " + sdf.format(endDate) + ")");
		result.put("categories", categories);
		return result;
		
		/*params = jlanalyseDrawManager.convertJsonObject(params);
		String startDateStr=params.getString("startDate_ge_date");
		String endDateStr=params.getString("endDate_le_date");
		String qualityFeatureId=params.getString("qualityFeatures");
		String tag_value=null;
		String tag_code=null;
		String group="";
		if(params.containsKey("tag_value")){
			tag_value=params.getString("tag_value");
		}
		if(params.containsKey("tag_code")){
			tag_code=params.getString("tag_code");
		}
		if(params.containsKey("group")){
			group=params.getString("group");
		}
		String[] tag_values=null;
		if(tag_value!=null){
			tag_values=tag_value.split(",");
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
		if(tag_values!=null){
			for(int i=0;i<tag_values.length;i++){
				dataMap.put(tag_values[i], new ArrayList<Object>());
			}
		}
		while(startCalendar.getTimeInMillis() <= endDate.getTime()){
			categories.add(startCalendar.get(Calendar.DATE)+"");
			Date startdate = startCalendar.getTime();
			startCalendar.add(Calendar.DAY_OF_YEAR, 1);
			Date enddate=startCalendar.getTime();
			if(tag_values!=null){
				for(int i=0;i<tag_values.length;i++){
					String value=tag_values[i];
					List<Object> dataList = dataMap.get(value);
					//封装数据
					JLcalculator jLcalculator=new JLcalculator();
					JLOriginalData originalData=new JLOriginalData();
					//封装originalDate
					jlanalyseDrawManager.cpkCalculator(jLcalculator,qualityFeatureId,startdate,enddate,originalData,group,value,params);
					Double cpk=jLcalculator.getjLResult().getCpkMoudle().getCpk();
					Map<String,Object> data = new HashMap<String, Object>();
					data.put("y",cpk);
					dataList.add(data);
				}
			}
		}
		List<Map<String,Object>> series = new ArrayList<Map<String,Object>>();
		if(tag_values!=null){
		for(int i=0;i<tag_values.length;i++){
			Map<String,Object> serie = new HashMap<String, Object>();
			serie.put("name", tag_values[i]);
			serie.put("data",dataMap.get(tag_values[i]));
			series.add(serie);
		}
		}
		result.put("series",series);
		result.put("title",  tag_value==null?"CPK对比推移图":tag_value+"CPK对比推移图");
		result.put("subtitle","(" + sdf.format(startDate) + " - " + sdf.format(endDate) + ")");
		result.put("categories", categories);
		return result;*/
	}
	
	/**
	 * 数据分析子组数据
	 * @param featureId
	 * @param startDate
	 * @param endDate
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String,Object> getDateTable(String featureId,Date startDate,Date endDate,String lastAmout,JSONArray condition){
		List<SpcSubGroup> spcSubGroupList=new ArrayList<SpcSubGroup>();
		QualityFeature qualityFeature=new QualityFeature();
		Map<String,Object> result = new HashMap<String, Object>();
		int effectiveCapacity=0;
		if (featureId != null && !"".equals(featureId)) {
			qualityFeature = qualityFeatureManager.getQualityFeature(Long.parseLong(featureId));
			effectiveCapacity = qualityFeature.getEffectiveCapacity();
		}
//		
		if(qualityFeature.getId()!=null && !"".equals(qualityFeature.getId())){
			//封装每组的样本数据
			List<FeatureLayer>  featureLayers=qualityFeature.getFeatureLayers();
			List<List<String>> infos=new ArrayList<List<String>>();
			List<String> times=new ArrayList<String>();
			List<Integer> groupNos=new ArrayList<Integer>();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ArrayList data=new ArrayList();
			for(int i=0;i<spcSubGroupList.size();i++){
				SpcSubGroup group=spcSubGroupList.get(i);
				groupNos.add(group.getSubGroupOrderNum());
				Date createTime=group.getCreatedTime();
				times.add(sdf.format(createTime));
				List<String> info=new ArrayList<String>();
				for(int j=0;j<featureLayers.size();j++){
					FeatureLayer f=featureLayers.get(j);
					String v=f.getDetailCode();
					String V=Character.toUpperCase(v.charAt(0))+v.substring(1);
					String method="get"+V;
					try {
						Method m=group.getClass().getMethod(method);
						String value=(String) m.invoke(group);
						info.add(value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				List<SpcSgSample> sampleList=group.getSpcSgSamples();
				double[] a=new double[effectiveCapacity];
				if(sampleList.size()>=effectiveCapacity){
					infos.add(info);
					for(int j=0;j<effectiveCapacity;j++){
						SpcSgSample sample=sampleList.get(j);
						a[j]=sample.getSamValue();
					}
					data.add(a);
				}
			}
			//封装每组样本数据的最大值、最小值、平均值等
			ArrayList<JLSampleData> datalist=new ArrayList<JLSampleData>();
			for(int i=0;i<data.size();i++){
				JLSampleData jl=new JLSampleData();
				double[] a=(double[])data.get(i);
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
			for(int i=0;i<featureLayers.size();i++){
				JSONObject modelJsonx = new JSONObject();
				FeatureLayer f=featureLayers.get(i);
				modelJsonx.put("name",f.getDetailCode());
				modelJsonx.put("label",f.getDetailName());
				modelJsonx.put("index",f.getDetailCode());
				colModels.add(modelJsonx);
			}
			DecimalFormat df = new DecimalFormat("0.00");
			for(int i=0;i<datalist.size();i++){
				JLSampleData jl=(JLSampleData)datalist.get(i);
				List<String> info=infos.get(i);
				Integer groupNo=groupNos.get(i);
				String time=times.get(i);
				double[] datas=jl.getData();
				Map<String,Object> dataMap = new HashMap<String, Object>();
				dataMap.put("no", groupNo);
				for(int h=0;h<datas.length;h++){
					double number=datas[h];
					dataMap.put("x"+h, number);
				}
				dataMap.put("average", df.format(jl.getAverage()));
				dataMap.put("max", df.format(jl.getMax()));
				dataMap.put("min", df.format(jl.getMin()));
				dataMap.put("range", df.format(jl.getR()));
				dataMap.put("time", time);
				
				for(int j=0;j<info.size();j++){
					String value=info.get(j);
					FeatureLayer f=featureLayers.get(j);
					dataMap.put(f.getDetailCode(), value);
				}
				tabledata.add(dataMap);
			}
			if(spcSubGroupList != null && spcSubGroupList.size() != 0){
				result.put("size", spcSubGroupList.size());
			}else{
				result.put("size", 0);
			}
			result.put("colModel",colModels);
			result.put("tabledata", tabledata);
			result.put("max", 100);
		}
		return result;
	}
}
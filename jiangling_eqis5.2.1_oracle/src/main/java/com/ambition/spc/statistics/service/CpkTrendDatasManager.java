package com.ambition.spc.statistics.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.spc.dataacquisition.dao.SpcSubGroupDao;
import com.ambition.spc.dataacquisition.service.SpcDataManager;
import com.ambition.spc.entity.ControlLimit;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.jlanalyse.entity.JLOriginalData;
import com.ambition.spc.jlanalyse.entity.JLSampleData;
import com.ambition.spc.jlanalyse.util.JLcalculator;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.spc.util.Calculator;
import com.ambition.spc.util.ConstantsOfSPC;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.DateUtil;
import com.opensymphony.xwork2.ActionContext;

@Service
public class CpkTrendDatasManager {
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private SpcSubGroupDao spcSubGroupDao;
	@Autowired
	private SpcApplicationDatasManager spcApplicationDatasManager;
	/**
	 * CPK推移图
	 * @param params
	 * @return
	 * @throws InterruptedException 
	 */
	public Map<String,Object> getCpkTrendDatas(JSONObject params) throws InterruptedException{
		params = CommonUtil1.convertJsonObject(params);
		final JSONObject layerParams = params;
		final String startDateStr=params.getString("startDate_ge_date");
		final String endDateStr=params.getString("endDate_le_date");
		String qualityFeatureStrs=params.getString("qualityFeatures");
		String group=params.getString("group");
		String[] qualityFeatureIds=null;
		if(qualityFeatureStrs!=null){
			qualityFeatureIds=qualityFeatureStrs.split(",");
		}
		//查询质量特性
//		Map<String,QualityFeature> featureMap = new HashMap<String, QualityFeature>();
//		searchQualityFeature(qualityFeatureIds, featureMap);
		//组装报表数据
		Map<String,Object> result = new HashMap<String, Object>();
		Date startDate = DateUtil.parseDate(startDateStr);
		Date endDate = DateUtil.parseDate(endDateStr + " 23:59:59","yyyy-MM-dd HH:mm:ss");
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		List<String> categories = new ArrayList<String>();
		List<QualityFeature> qualityFeatures = new ArrayList<QualityFeature>();
		for(int i=0;i<qualityFeatureIds.length;i++){
			QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureFromCache(Long.valueOf(qualityFeatureIds[i]),null);
			qualityFeatures.add(qualityFeature);
		}
		//查询所有的统计分组
		SpcDataManager spcDataManager = new SpcDataManager(spcSubGroupDao.getSession());
		Map<String,Map<String,Object>> dataMap = spcDataManager.multiQuerySpcDataValues(qualityFeatures, startDateStr, endDateStr, layerParams,1,null,null);
		//查询控制限
		Map<String,List<ControlLimit>> controlLimitMap = new HashMap<String, List<ControlLimit>>();
		spcApplicationDatasManager.searchControlLimits(null,qualityFeatureStrs, controlLimitMap);
		
		//横坐标
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.setTime(startCalendar.getTime());
		while(tempCalendar.getTimeInMillis() <= endDate.getTime()){
			categories.add(tempCalendar.get(Calendar.DATE)+"");
			tempCalendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		//多线程处理
		Map<String,Boolean> flagMap = new HashMap<String, Boolean>();
		Map<String,List<Map<String,Object>>> seriesDataMap = new HashMap<String,List<Map<String,Object>>>();
		for(int i=0;i<qualityFeatures.size();i++){
			QualityFeature qualityFeature = qualityFeatures.get(i);
			if(qualityFeature==null){
				continue;
			}
			synchronized (flagMap) {
				flagMap.put(qualityFeature.getId().toString(),true);
			}
			Calendar tempStart = Calendar.getInstance();
			tempStart.setTime(startCalendar.getTime());
			Calendar tempEnd = Calendar.getInstance();
			tempEnd.setTime(endDate);
			Thread thread = new Thread(new caculate(qualityFeature,dataMap.get(qualityFeature.getId().toString()),
					controlLimitMap.get(qualityFeature.getId().toString()),group, tempStart, tempEnd.getTime(),
					this,ActionContext.getContext(),flagMap,seriesDataMap));
			thread.start();
		}
		while(true){
			synchronized (flagMap) {
				if(flagMap.size()==0){
					break;
				}
			}
			Thread.sleep(100l);
		}
		controlLimitMap.clear();
//		
		List<Map<String,Object>> series = new ArrayList<Map<String,Object>>();
		for(int i=0;i<qualityFeatures.size();i++){
			Map<String,Object> serie = new HashMap<String, Object>();
			QualityFeature qualityFeature=qualityFeatures.get(i);
			serie.put("name", qualityFeature.getName());
			serie.put("data",seriesDataMap.get(qualityFeature.getId().toString()));
			series.add(serie);
		}
		result.put("series",series);
		result.put("title",  group+"推移图");
		result.put("subtitle","(" + startDateStr + " - " + endDateStr + ")");
		result.put("categories", categories);
		return result;
	}
	
	/**
	 * 封装数据计算x-s的数据,调用算法计算均值标准差放入getjLResult
	 * @param jLcalculator
	 * @param featureId
	 * @param startDate
	 * @param endDate
	 * @param originalData
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	public void calculatJl(JLcalculator jLcalculator,QualityFeature qualityFeature,List<double[]> values,
			List<ControlLimit> controlLimits,JLOriginalData originalData,ActionContext actionContext){
		Integer sampleCapacity = qualityFeature.getSampleCapacity();
		if(sampleCapacity == null){
			sampleCapacity = 5;
		}
		Integer effectiveCapacity = qualityFeature.getEffectiveCapacity();
		if(effectiveCapacity == null){
			effectiveCapacity = sampleCapacity;
		}

		//封装每组的样本数据
		ArrayList data=new ArrayList();
		for(int i=0;i<values.size();i++){
			data.add(values.get(i));
		}
		//封装所有数据
		if(qualityFeature.getControlChart()!=null){
			if(qualityFeature.getControlChart().equals("4")){//params!=null&&
				double alldataaverage=0.0;
				double[] alldata=new double[sampleCapacity*values.size()];
				for(int k=0;k<values.size();k++){
					double sampleList[] = values.get(k);
					for(int j=0;j<sampleList.length;j++){
						alldata[(k * sampleCapacity + j)] = sampleList[j];
					}
				}
				alldataaverage = Calculator.average(alldata);
				originalData.setAlldataaverage(alldataaverage);
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
			if(qualityFeature.getControlChart()!=null){
				if(qualityFeature.getControlChart().equals("4")){
					if(i!=0){
						double[] first=(double[])data.get(i-1);
						double firstno=first[0];
						double[] next=(double[])data.get(i);
						double nextnumber=next[0];
						jl.setR(Calculator.calculateRS(nextnumber,firstno));
					}
				}else{
					jl.setR(Calculator.calculateR(a));
				}
			}
			jl.setMedian(Calculator.calculateMedian(a));
			datalist.add(jl);
		}
		//封装originalData数据
		String isAutoCl="";
		Integer controlState=null;
		if(qualityFeature.getIsAutoCl()!=null){
			isAutoCl=qualityFeature.getIsAutoCl();
		}
		if(isAutoCl.equals("N")){
			controlState=ConstantsOfSPC.SPC_SAMPLING_STATE_CONTROL_I;
		}else{
			controlState=ConstantsOfSPC.SPC_SAMPLING_STATE_ANALYSIS_I;
		}
		originalData.setControlState(controlState);
		if(controlState.equals(ConstantsOfSPC.SPC_SAMPLING_STATE_CONTROL_I)){
			if(qualityFeature.getControlChart().equals("1")||qualityFeature.getControlChart().equals("4")){
				if(controlLimits.size()>0){
					ControlLimit c=controlLimits.get(0);
					originalData.setRCL(c.getScl());
					originalData.setRLCL(c.getSlcl());
					originalData.setRUCL(c.getSucl());
					originalData.setXCL(c.getXcl());
					originalData.setXLCL(c.getXlcl());
					originalData.setXUCL(c.getXucl());
				}
			}else if(qualityFeature.getControlChart().equals("2")){
				if(controlLimits.size()>0){
					ControlLimit c=controlLimits.get(0);
					originalData.setSCL(c.getScl());
					originalData.setSLCL(c.getSlcl());
					originalData.setSUCL(c.getSucl());
					originalData.setXCL(c.getXcl());
					originalData.setXLCL(c.getXlcl());
					originalData.setXUCL(c.getXucl());	
				}
			}
		}
		originalData.setTl(qualityFeature.getLowerTarge());
		originalData.setTu(qualityFeature.getUpperTarge());
		originalData.setM(qualityFeature.getTargeValue());
		if(qualityFeature.getControlChart()!=null){
			if(qualityFeature.getControlChart().equals("1")){
				originalData.setChartType(1);
			}else if(qualityFeature.getControlChart().equals("2")){
				originalData.setChartType(2);
			}else if(qualityFeature.getControlChart().equals("4")){
				originalData.setChartType(4);
			}
		}
		originalData.setDataList(datalist);
		originalData.setSampleQuantity(effectiveCapacity);
		//调用算法计算均值标准差放入getjLResult
		jLcalculator.calculate(originalData);
	}
	
	private class caculate implements Runnable{
		private Calendar startCalendar;
		private Date endDate;
		private QualityFeature qualityFeature;
		private List<ControlLimit> controlLimits;
		private CpkTrendDatasManager cpkTrendDatasManager;
		private Map<String,Boolean> flagMap;
		private String group;
		private ActionContext actionContext;
		private Map<String,Object> resultMap;
		private Map<String,List<Map<String,Object>>> seriesDataMap;
		private caculate(QualityFeature qualityFeature,Map<String,Object> resultMap,
				List<ControlLimit> controlLimits,
				String group ,
				Calendar startCalendar,Date endDate,
				CpkTrendDatasManager cpkTrendDatasManager,
				ActionContext actionContext,
				Map<String,Boolean> flagMap,
				Map<String,List<Map<String,Object>>> seriesDataMap){
			this.startCalendar = startCalendar;
			this.endDate = endDate;
			this.resultMap = resultMap;
			this.qualityFeature = qualityFeature;
			this.controlLimits = controlLimits;
			this.cpkTrendDatasManager = cpkTrendDatasManager;
			this.resultMap = resultMap;
			this.group = group;
			this.flagMap = flagMap;
			this.actionContext = actionContext;
			this.seriesDataMap = seriesDataMap;
		}
		@Override
		public void run() {
			try {
				execute();
			} catch (Exception e) {
				Logger.getLogger(cpkTrendDatasManager.getClass()).error("计算失败!",e);
			}finally{
				synchronized (flagMap) {
					flagMap.remove(qualityFeature.getId()+"");
				}
			}
		}
		
		private void execute(){
			List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
			@SuppressWarnings("unchecked")
			List<double[]> allValues = (List<double[]>)resultMap.get("values");
			JSONArray posDatas = (JSONArray)resultMap.get("posDatas");
			//按天分组
			Map<String,List<double[]>> dateValueMap = new HashMap<String, List<double[]>>();
			for(int i=0;i<allValues.size();i++){
				JSONObject posData = posDatas.getJSONObject(i);
				String date = posData.getString("date");
				if(!dateValueMap.containsKey(date)){
					dateValueMap.put(date, new ArrayList<double[]>());
				}
				dateValueMap.get(date).add(allValues.get(i));
			}
			while(startCalendar.getTimeInMillis() <= endDate.getTime()){
				String dateStr = DateUtil.formateDateStr(startCalendar);
				startCalendar.add(Calendar.DAY_OF_YEAR, 1);
				
				//封装数据
				JLcalculator jLcalculator=new JLcalculator();
				JLOriginalData originalData=new JLOriginalData();
				//封装originalDate
				List<double[]> values = dateValueMap.get(dateStr);
				if(values==null){
					values = new ArrayList<double[]>();
				}
				cpkTrendDatasManager.calculatJl(jLcalculator,qualityFeature,values,controlLimits,originalData,actionContext);
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
			
			seriesDataMap.put(qualityFeature.getId().toString(),dataList);
		}
	}
	
}
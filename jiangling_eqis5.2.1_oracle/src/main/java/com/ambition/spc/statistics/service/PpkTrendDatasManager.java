package com.ambition.spc.statistics.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ambition.spc.entity.ControlLimit;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.entity.SpcSubGroup;
import com.ambition.spc.jlanalyse.entity.JLOriginalData;
import com.ambition.spc.jlanalyse.entity.JLSampleData;
import com.ambition.spc.jlanalyse.util.JLcalculator;
import com.ambition.spc.util.Calculator;
import com.ambition.spc.util.ConstantsOfSPC;
import com.opensymphony.xwork2.ActionContext;

@Service
public class PpkTrendDatasManager {
	/**
	 * 封装数据计算x-s的数据,调用算法计算均值标准差放入getjLResult
	 * @param jLcalculator
	 * @param featureId
	 * @param startDate
	 * @param endDate
	 * @param originalData
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public void calculatJl(JLcalculator jLcalculator,QualityFeature qualityFeature,List featureGroupList,List allList,List<ControlLimit> controlLimits,JLOriginalData originalData,String type,String lastAmout,JSONArray condition,JSONObject params,ActionContext actionContext,Map<String,List<Double>> sampleMap){
		List<Object> spcSubGroupList=featureGroupList;
		if(spcSubGroupList==null){
			spcSubGroupList = new ArrayList<Object>();
		}
		List<Object> allspcSubGroupList=allList;
		if(allspcSubGroupList==null){
			allspcSubGroupList = new ArrayList<Object>();
		}
		int effectiveCapacity=0;
		if(qualityFeature.getEffectiveCapacity()!=null){
			effectiveCapacity = qualityFeature.getEffectiveCapacity();
		}

		//封装每组的样本数据
		ArrayList data=new ArrayList();
		for(int i=0;i<spcSubGroupList.size();i++){
			Object obj =spcSubGroupList.get(i);
			String groupId = "";
			if(obj instanceof SpcSubGroup){
				groupId = ((SpcSubGroup)obj).getId()+"";
			}else{
				groupId = obj+"";
			}
			List<Double> sampleList=sampleMap.get(groupId);
			double[] a=new double[effectiveCapacity];
			if(sampleList!=null && sampleList.size()>=effectiveCapacity){
				for(int j=0;j<effectiveCapacity;j++){
					a[j]=sampleList.get(j);
				}
				data.add(a);
			}
		}
		//封装所有数据
		if(qualityFeature.getControlChart()!=null){
			if(qualityFeature.getControlChart().equals("4")){//params!=null&&
				double alldataaverage=0.0;
				double[] alldata=new double[effectiveCapacity*allspcSubGroupList.size()];
				int k=0;
				for(int i=0;i<allspcSubGroupList.size();i++){
					Object obj =allspcSubGroupList.get(i);
					String groupId = "";
					if(obj instanceof SpcSubGroup){
						groupId = ((SpcSubGroup)obj).getId()+"";
					}else{
						groupId = obj+"";
					}
					List<Double> sampleList=sampleMap.get(groupId);
					double[] a=new double[effectiveCapacity];
					if(sampleList!=null && sampleList.size()>=effectiveCapacity){
						for(int j=0;j<effectiveCapacity;j++){
							alldata[(k * effectiveCapacity + j)] = sampleList.get(j);
						}
						k++;
					}
				}
				alldataaverage=Calculator.average(alldata);
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
		int n = 0;
		if(qualityFeature.getPrecs()!=null&&!qualityFeature.getPrecs().equals("")){
			n = Integer.parseInt(qualityFeature.getPrecs());
		}
		String digit="0.";
		for(int i=0;i<n;i++){
			digit=digit+"0";
		}
		if(actionContext!=null){
			DecimalFormat de=new DecimalFormat(digit);
			actionContext.put("decimalFormat", de);
		}
		//调用算法计算均值标准差放入getjLResult
		jLcalculator.calculate(originalData);
	}
	
//	private class caculate implements Runnable{
//		private Calendar startCalendar;
//		private Date endDate;
//		private List<Object> dataList;
//		private QualityFeature qualityFeature;
//		private List<ControlLimit> controlLimits;
//		private JSONObject params;
//		private PpkTrendDatasManager ppkTrendDatasManager;
//		private Map<String,List<SpcSubGroup>> spcSubGroupMap;
//		private Map<String,List<SpcSubGroup>> allGroupMap;
//		private Map<String,Boolean> flagMap;
//		private String group;
//		private ActionContext actionContext;
//		private Map<String,List<Double>> sampleMap;
//		private caculate(QualityFeature qualityFeature,Map<String,List<SpcSubGroup>> spcSubGroupMap,
//				Map<String,List<SpcSubGroup>> allGroupMap,
//				List<ControlLimit> controlLimits,
//				JSONObject params,
//				String group ,
//				Calendar startCalendar,Date endDate,List<Object> dataList,
//				PpkTrendDatasManager ppkTrendDatasManager,
//				ActionContext actionContext,
//				Map<String,Boolean> flagMap,
//				Map<String,List<Double>> sampleMap){
//			this.startCalendar = startCalendar;
//			this.endDate = endDate;
//			this.dataList = dataList;
//			this.qualityFeature = qualityFeature;
//			this.controlLimits = controlLimits;
//			this.params = params;
//			this.ppkTrendDatasManager = ppkTrendDatasManager;
//			this.spcSubGroupMap = spcSubGroupMap;
//			this.allGroupMap = allGroupMap;
//			this.group = group;
//			this.flagMap = flagMap;
//			this.actionContext = actionContext;
//			this.sampleMap = sampleMap;
//		}
//		@Override
//		public void run() {
//			try {
//				execute();
//			} catch (Exception e) {
//				Logger.getLogger(ppkTrendDatasManager.getClass()).error("计算失败!",e);
//			}finally{
//				synchronized (flagMap) {
//					flagMap.remove(qualityFeature.getId()+"");
//				}
//			}
//		}
//		
//		private void execute(){
//			while(startCalendar.getTimeInMillis() <= endDate.getTime()){
//				String dateStr = DateUtil.formateDateStr(startCalendar);
//				startCalendar.add(Calendar.DAY_OF_YEAR, 1);
//				
//				//封装数据
//				JLcalculator jLcalculator=new JLcalculator();
//				JLOriginalData originalData=new JLOriginalData();
//				//封装originalDate
//				String qualityKey = qualityFeature.getId() + "_" + dateStr;
//				ppkTrendDatasManager.calculatJl(jLcalculator,qualityFeature,spcSubGroupMap.get(qualityKey),allGroupMap.get(qualityFeature.getId()+""),controlLimits,originalData,"analysis","",new JSONArray(),params,actionContext,sampleMap);
//				Map<String,Object> data = new HashMap<String, Object>();
//				if(group.equals("PPK")){
//					Double ppk=jLcalculator.getjLResult().getCpkMoudle().getPpk();
//					data.put("y",ppk);
//				}else if(group.equals("MEAN")){
//					Double MEAN=jLcalculator.getjLResult().getAverage();
//					data.put("y",MEAN);
//				}else if(group.equals("StdDve")){
//					Double StdDve=jLcalculator.getjLResult().getS();
//					data.put("y",StdDve);
//				}else if(group.equals("SIGMA")){
//					Double SIGMA=jLcalculator.getjLResult().getCpkMoudle().getSigma();
//					data.put("y",SIGMA);
//				}else if(group.equals("Fpu(Perf)")){
//					Double FpuPerf=jLcalculator.getjLResult().getCpkMoudle().getFpu_pref();
//					data.put("y",FpuPerf);
//				}
//				dataList.add(data);
//			}
//		}
//	}
}
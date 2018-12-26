package com.ambition.spc.importutil.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.cfg.Settings;
import org.hibernate.impl.SessionImpl;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.spc.abnormal.service.AbnormalInfoManager;
import com.ambition.spc.abnormal.util.ConstantsOfSPC;
import com.ambition.spc.dataacquisition.service.SpcDataManager;
import com.ambition.spc.entity.AbnormalForRealTime;
import com.ambition.spc.entity.AbnormalInfo;
import com.ambition.spc.entity.Abnormity;
import com.ambition.spc.entity.ControlLimit;
import com.ambition.spc.entity.FeatureLayer;
import com.ambition.spc.entity.FeaturePerson;
import com.ambition.spc.entity.FeatureRules;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.jlanalyse.entity.JLOriginalData;
import com.ambition.spc.jlanalyse.entity.JLSampleData;
import com.ambition.spc.jlanalyse.util.JLJudgeAbnormity;
import com.ambition.spc.jlanalyse.util.JLcalculator;
import com.ambition.spc.processdefine.dao.QualityFeatureDao;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.spc.util.Calculator;
import com.ambition.util.common.DateUtil;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.util.AsyncMailUtils;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PropUtils;

/**
 * 类名:监控管理类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2018-7-22 发布
 */
@Service
public class SpcMonitorManager {
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private QualityFeatureDao qualityFeatureDao;
	private Object lockObj = new Object();
	private long lastExecute = 0l;
	private Boolean isBackExecute = false;
	//默认延迟1分钟执行
	private long executeJiangge = 10*1000;
	//缓存数据
	private Map<String,TreeSet<JSONObject>> cacheMap = new HashMap<String,TreeSet<JSONObject>>();
	//需要监控处理的数据
	private Map<String,TreeSet<JSONObject>> monitorMap = new HashMap<String, TreeSet<JSONObject>>();
	
	/**
	  * 方法名:添加到监控队列
	  * <p>功能说明
	  * data包含的key有 : inspectionDate:字符串 yyyyMMddHHmmss.SSS 如:20180722123035.222
	  * value:双精度数字,如10.01
	  * </p>
	  * @return
	 */
	public void addMonitor(JSONObject data){
		if(!data.containsKey("featureId")){
			return;
		}
		List<JSONObject> datas = new ArrayList<JSONObject>();
		datas.add(data);
		
		addMonitors(datas);
	}
	/**
	  * 方法名:添加到监控队列
	  * <p>功能说明
	  * data包含的key有 : inspectionDate:字符串 yyyyMMddHHmmss.SSS 如:20180722123035.222
	  * value:双精度数字,如10.01
	  * </p>
	  * @return
	 */
	public void addMonitors(List<JSONObject> datas){
		synchronized (lockObj) {
			for(JSONObject data : datas){
				if(!data.containsKey("featureId")){
					continue;
				}
				String featureId = data.getString("featureId");
				//添加到需要监控的缓存池中
				if(!monitorMap.containsKey(featureId)){
					TreeSet<JSONObject> set = new TreeSet<JSONObject>(new Comparator<JSONObject>() {
						@Override
						public int compare(JSONObject arg0, JSONObject arg1) {
							String inspectionDate1 = arg0.containsKey("inspectionDate")?arg0.getString("inspectionDate"):"";
							String inspectionDate2 = arg1.containsKey("inspectionDate")?arg1.getString("inspectionDate"):"";
							int compareTo = inspectionDate1.compareTo(inspectionDate2);
							if(compareTo == 0){
								compareTo = 1;
							}
							return compareTo;
						}
					});
					monitorMap.put(featureId,set);
				}
				monitorMap.get(featureId).add(data);
			}
		}
		
		synchronized (isBackExecute) {
			if(!isBackExecute){
				isBackExecute = true;
				startBackService();
			}
		}
	}
	/**
	  * 方法名:清除预警的缓存
	  * <p>功能说明：</p>
	  * @return
	 */
	public void clearMonitorCaches(String featureId){
		synchronized (cacheMap) {
			cacheMap.remove(featureId);
		}
	}
	
	/**
	  * 方法名:启动后台监控服务
	  * <p>功能说明：</p>
	  * @return
	 */
	private void startBackService(){
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					while(true){
						long between = System.currentTimeMillis() - lastExecute;
						long sleep = executeJiangge - between;
						if(sleep>0){
							Thread.sleep(sleep);
						}
						final Map<String,Boolean> executeMap = new HashMap<String, Boolean>();
						final Map<String,List<Map<String,Object>>> checkResultMap = new HashMap<String, List<Map<String,Object>>>();//检查结果
						Session session = null;
						try{
							session = qualityFeatureDao.getSessionFactory().openSession();
							synchronized (lockObj) {
									boolean hasMonitor = false;
									for(String featureId : monitorMap.keySet()){
										TreeSet<JSONObject> monitorData = monitorMap.get(featureId);
										if(monitorData.isEmpty()){
											continue;
										}
										
										//临时数据,延用tree的排序即可,不需要另外排序
										final List<JSONObject> tempMonitorDatas = new ArrayList<JSONObject>();
										tempMonitorDatas.addAll(monitorData);
										monitorData.clear();
										
										QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureFromCache(Long.valueOf(featureId), session);
										//添加缓存数据中,返回新的缓存数据
										List<JSONObject> cacheValues = addCacheValues(qualityFeature, session,tempMonitorDatas);
										
										//新增的数据添加到缓存集合中
										ExecuteCheck executeCheck = new ExecuteCheck(qualityFeature, tempMonitorDatas, cacheValues,executeMap,checkResultMap);
										Thread thread = new Thread(executeCheck);
										thread.start();
										hasMonitor = true;
										
										//更新执行的标志，一次最多执行10个线程
										synchronized (executeMap) {
											executeMap.put(featureId,true);
											if(executeMap.size()>=10){
												break;
											}
										}
									}
									
									if(!hasMonitor){
										break;
									}
							}
							while(true){
								//等待线程计算完成后
								synchronized (executeMap) {
									if(executeMap.isEmpty()){
										break;
									}
								}
								Thread.sleep(100l);
							}
							//保存到预警消息
							batchAddInfos(checkResultMap,session);
							checkResultMap.clear();
							lastExecute = System.currentTimeMillis();
						} finally{
							if(session != null && session.isOpen()){
								session.close();
							}
						}
					}
				} catch (Throwable e) {
					Logger.getLogger(SpcMonitorManager.class).error("后台检查符合监控条件出错",e);
				}finally{
					synchronized (isBackExecute) {
						isBackExecute = false;
						lastExecute = System.currentTimeMillis();
					}
				}
			}
		});
		thread.start();
	}
	/**
	  * 方法名:添加到缓存数据中
	  * <p>功能说明：</p>
	  * @return
	 */
	private List<JSONObject> addCacheValues(QualityFeature qualityFeature,Session session,List<JSONObject> addValues){
		synchronized (cacheMap) {
			String featureId = qualityFeature.getId().toString();
			if(!cacheMap.containsKey(featureId)){
				TreeSet<JSONObject> set = new TreeSet<JSONObject>(new Comparator<JSONObject>() {
					@Override
					public int compare(JSONObject arg0, JSONObject arg1) {
						String inspectionDate1 = arg0.containsKey("inspectionDate")?arg0.getString("inspectionDate"):"";
						String inspectionDate2 = arg1.containsKey("inspectionDate")?arg1.getString("inspectionDate"):"";
						int compareTo = inspectionDate1.compareTo(inspectionDate2);
						if(compareTo == 0){
							compareTo = 1;
						}
						return compareTo;
					}
				});
				cacheMap.put(featureId,set);
				SpcDataManager spcDataManager = new SpcDataManager(session);
				//查询最后500组的数据
				List<JSONObject> values = spcDataManager.queryLastGroupValuesForCache(qualityFeature,500);
				set.addAll(values);
			}else{
				TreeSet<JSONObject> treeSet = cacheMap.get(featureId);
				treeSet.addAll(addValues);
			}
			
			TreeSet<JSONObject> treeSet = cacheMap.get(featureId);
			//保持缓存中只有500组的数据
			int groupCount = treeSet.size()/qualityFeature.getSampleCapacity();
			int removeCount = (groupCount-500)*qualityFeature.getSampleCapacity();
			//移除多余的数据
			for(int i=0;i<removeCount;i++){
				JSONObject first = treeSet.first();
				treeSet.remove(first);
			}
			//复制到临时对象
			List<JSONObject> tempValues = new ArrayList<JSONObject>();
			tempValues.addAll(treeSet);
			return tempValues;
		}
	}
	
	/**
	 * 类名:执行后台检查的线程
	 */
	private class ExecuteCheck implements Runnable{
		private List<JSONObject> monitorDatas;
		private List<JSONObject> values;
		private Map<String,Boolean> executeMap;
		private QualityFeature qualityFeature;
		private Map<String,List<Map<String,Object>>> checkResultMap;
		public ExecuteCheck(QualityFeature qualityFeature,List<JSONObject> monitorDatas,List<JSONObject> values,Map<String,Boolean> executeMap,Map<String,List<Map<String,Object>>> checkResultMap) {
			this.monitorDatas = monitorDatas;
			this.values = values;
			this.executeMap = executeMap;
			this.qualityFeature = qualityFeature;
			this.checkResultMap = checkResultMap;
		}
		@Override
		public void run() {
			try {
				//确定监控数据在所有数据中的位置
				List<Map<String,Object>> errorList = new ArrayList<Map<String,Object>>();
				for(JSONObject monitorData : monitorDatas){
					int valueSize = values.size();
					for(int k=valueSize-1;k>-1;k--){
						JSONObject data = values.get(k);
						if(data.getString("id").equals(monitorData.getString("id"))){
							//检查是否是组的结束位置
							int dataIndex = k+1;
							int sampleCapacity = qualityFeature.getSampleCapacity();
							int effectiveCapacity = qualityFeature.getEffectiveCapacity();
							//有效样本量==样本容量时,余数为0时需要检查，否则余量等于有效容量时检查
							int firstValueIndex = -1;
							if(sampleCapacity == effectiveCapacity){
								if(dataIndex%sampleCapacity==0){
									//当前开始计算，到最后100组,不够时从0开始计算
									firstValueIndex = dataIndex - 100*sampleCapacity;
									if(firstValueIndex<0){
										firstValueIndex = 0;
									}
								}
							}else{
								if(dataIndex%sampleCapacity==qualityFeature.getEffectiveCapacity()){
									int tempEnd = dataIndex - qualityFeature.getEffectiveCapacity();
									firstValueIndex = tempEnd - 99 * sampleCapacity;
									if(firstValueIndex<0){
										firstValueIndex = 0;
									}
								}
							}
							if(firstValueIndex>-1){
								List<double[]> checkValues = new ArrayList<double[]>();
								for(int m=firstValueIndex;m<dataIndex;m+=sampleCapacity){
									double[] tempValue = new double[sampleCapacity];
									for(int l=0;l<sampleCapacity&&(l+m)<dataIndex;l++){
										tempValue[l] = values.get(l+m).getDouble("value");
									}
									checkValues.add(tempValue);
								}
								List<AbnormalForRealTime> result = executeCheck(qualityFeature, checkValues);
								if(result != null&&result.size()>0){
									Map<String,Object> errorMap = new HashMap<String, Object>();
									errorMap.put("data",monitorData);
									errorMap.put("errors",result);
									errorList.add(errorMap);
								}
							}
							break;
						}
					}
				}
				if(errorList.size()>0){
					checkResultMap.put(qualityFeature.getId().toString(),errorList);
				}
			} finally{
				synchronized (executeMap) {
					JSONObject data = monitorDatas.get(0);
					executeMap.remove(data.getString("featureId"));
				}
				values.clear();
				monitorDatas.clear();
			}
		}
	}
	
	/**
	  * 方法名:执行数据检查
	  * <p>功能说明：</p>
	  * @return
	 */
	private List<AbnormalForRealTime> executeCheck(QualityFeature qualityFeature,List<double[]> values){
		double[] lastValues = values.get(values.size()-1);
		String ss = "";
		for(double val : lastValues){
			ss += val + ",";
		}
		System.out.println("featue:" + qualityFeature.getName() + "("+qualityFeature.getId()+")" + "monitor:" + ss);
		
		JLOriginalData originalData = new JLOriginalData();
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
			if(qualityFeature.getControlChart()!=null){
				if(qualityFeature.getControlChart().equals("4")){
					if(i!=0){
						double[] first=(double[])values.get(i-1);
						double firstno=first[0];
						double[] next=(double[])values.get(i);
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
			List<ControlLimit> controlLimits = qualityFeature.getControlLimits();
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
		originalData.setSampleQuantity(qualityFeature.getEffectiveCapacity());
		//调用算法计算均值标准差放入getjLResult
		JLcalculator jLcalculator=new JLcalculator();
		jLcalculator.calculate(originalData);
		
		//封装画控制图参数
		Abnormity[] abnormityArray = AbnormalInfoManager.judgeCollectDataOfAbnormity(qualityFeature,false,0);
		if(qualityFeature.getControlChart()!=null){
			if(qualityFeature.getControlChart().equals("1")){
				originalData.setRCL(jLcalculator.getjLResult().getRCL());
				originalData.setRLCL(jLcalculator.getjLResult().getRLCL());
				originalData.setRUCL(jLcalculator.getjLResult().getRUCL());
				originalData.setXCL(jLcalculator.getjLResult().getXCL());
				originalData.setXLCL(jLcalculator.getjLResult().getXLCL());
				originalData.setXUCL(jLcalculator.getjLResult().getXUCL());
			}else if(qualityFeature.getControlChart().equals("2")){
				originalData.setSCL(jLcalculator.getjLResult().getSCL());
				originalData.setSLCL(jLcalculator.getjLResult().getSLCL());
				originalData.setSUCL(jLcalculator.getjLResult().getSUCL());
				originalData.setXCL(jLcalculator.getjLResult().getXCL());
				originalData.setXLCL(jLcalculator.getjLResult().getXLCL());
				originalData.setXUCL(jLcalculator.getjLResult().getXUCL());
			}else if(qualityFeature.getControlChart().equals("4")){
				originalData.setRCL(jLcalculator.getjLResult().getRCL());
				originalData.setRLCL(jLcalculator.getjLResult().getRLCL());
				originalData.setRUCL(jLcalculator.getjLResult().getRUCL());
				originalData.setXCL(jLcalculator.getjLResult().getXCL());
				originalData.setXLCL(jLcalculator.getjLResult().getXLCL());
				originalData.setXUCL(jLcalculator.getjLResult().getXUCL());
			}
		}
		
	    double tu = originalData.getTu();
	    double tl = originalData.getTl();
	    double ucl1 = 0,lcl1 = 0,cl1 = 0,ucl2 = 0,lcl2 = 0,cl2 = 0;
	    List<?> dataList = originalData.getDataList();
	    int groupTotal = dataList.size();

	    double[] arrayx = new double[groupTotal];
	    double[] arrayy = null;
	    if ((originalData.getChartType() == 
	      4) && (groupTotal > 0))
	      arrayy = new double[groupTotal - 1];
	    else {
	      arrayy = new double[groupTotal];
	    }
	    String[] dateTime = new String[groupTotal];
	    
	    switch (originalData.getChartType())
	    {
	    case 1:
	      for (int i = 0; i < groupTotal; i++) {
	        JLSampleData sampleData = (JLSampleData)dataList.get(i);
	        arrayx[i] = sampleData.getAverage();
	        arrayy[i] = sampleData.getR();
	        dateTime[i] = sampleData.getSamplingTime();
	      }

	      ucl1 = originalData.getXUCL();
	      lcl1 = originalData.getXLCL();
	      cl1 = originalData.getXCL();

	      ucl2 = originalData.getRUCL();
	      lcl2 = originalData.getRLCL();
	      cl2 = originalData.getRCL();

	      break;
	    case 2:
	      for (int i = 0; i < groupTotal; i++) {
	        JLSampleData sampleData = (JLSampleData)dataList.get(i);
	        arrayx[i] = sampleData.getAverage();
	        arrayy[i] = sampleData.getS();
	        dateTime[i] = sampleData.getSamplingTime();
	      }

	      ucl1 = originalData.getXUCL();
	      lcl1 = originalData.getXLCL();
	      cl1 = originalData.getXCL();

	      ucl2 = originalData.getSUCL();
	      lcl2 = originalData.getSLCL();
	      cl2 = originalData.getSCL();

	      break;
	    case 3:
	      for (int i = 0; i < groupTotal; i++) {
	        JLSampleData sampleData = (JLSampleData)dataList.get(i);
	        arrayx[i] = sampleData.getMedian();
	        arrayy[i] = sampleData.getR();
	        dateTime[i] = sampleData.getSamplingTime();
	      }

	      ucl1 = originalData.getXUCL();
	      lcl1 = originalData.getXLCL();
	      cl1 = originalData.getXCL();

	      ucl2 = originalData.getRUCL();
	      lcl2 = originalData.getRLCL();
	      cl2 = originalData.getRCL();

	      break;
	    case 4:
	      for (int i = 0; i < groupTotal; i++) {
	        JLSampleData sampleData = (JLSampleData)dataList.get(i);
	        arrayx[i] = sampleData.getAverage();

	        if (i > 0) {
	          arrayy[(i - 1)] = sampleData.getR();
	        }
	        dateTime[i] = sampleData.getSamplingTime();
	      }

	      ucl1 = originalData.getXUCL();
	      lcl1 = originalData.getXLCL();
	      cl1 = originalData.getXCL();

	      ucl2 = originalData.getRUCL();
	      lcl2 = originalData.getRLCL();
	      cl2 = originalData.getRCL();
	    }
	    
	    Abnormity[] abLS = JLJudgeAbnormity.getAbnormity(abnormityArray, 1);
	    List<AbnormalForRealTime> realTimes = new ArrayList<AbnormalForRealTime>();
	    if ((abLS != null) && (abLS.length > 0)) {
	    	List<?> retList = JLJudgeAbnormity.JudgeAbnormal(arrayx,abLS, null, null, 
	    			ucl1, lcl1, cl1, tu, tl, "",1,null);
	    	if(retList != null){
	    		for(Object obj : retList){
	    			AbnormalForRealTime realTime = (AbnormalForRealTime)obj;
	    			if(realTime.getEndPosition() == values.size()-1){
	    				realTimes.add(realTime);
	    			}
		    	}
	    	}
	    }
	    abLS = JLJudgeAbnormity.getAbnormity(abnormityArray, 2);
	    if ((abLS != null) && (abLS.length > 0)){
	    	List<?> retList = JLJudgeAbnormity.JudgeAbnormal(arrayx,abLS, null, null, 
	    			ucl2, lcl2, cl2, tu, tl, "",2,null);
	    	if(retList != null){
	    		for(Object obj : retList){
	    			AbnormalForRealTime realTime = (AbnormalForRealTime)obj;
	    			if(realTime.getEndPosition() == values.size()-1){
	    				realTimes.add(realTime);
	    			}
		    	}
	    	}
	    }
		return realTimes;
	}
	
	/**
	  * 方法名:批量保存预警信息
	  * <p>功能说明：</p>
	  * @return
	 */
	private void batchAddInfos(Map<String,List<Map<String,Object>>> checkResultMap,Session session){
		final List<Map<String,Object>> allDatas = new ArrayList<Map<String,Object>>();
		Map<QualityFeature,String> emailContentMap = new HashMap<QualityFeature, String>();
		for(String featureId : checkResultMap.keySet()){
			List<Map<String,Object>> results = checkResultMap.get(featureId);
			QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureFromCache(Long.valueOf(featureId),session);
			StringBuilder content = new StringBuilder();
			for(Map<String,Object>  result : results){
				JSONObject data = (JSONObject)result.get("data");
				Date inspectionDate = DateUtil.parseDate(data.getString("inspectionDate"),"yyyyMMddHHmmssSSS");
				if(inspectionDate == null){
					inspectionDate = new Date();
				}
				String happenTime = DateUtil.formateDateStr(inspectionDate,"yyyy-MM-dd HH:mm:ss");
				
				@SuppressWarnings("unchecked")
				List<AbnormalForRealTime> realTimes = (List<AbnormalForRealTime>)result.get("errors");
				for(AbnormalForRealTime realTime : realTimes){
					realTime.setHappenTime(happenTime);
					Map<String,Object> tempData = new HashMap<String, Object>();
					Timestamp timestamp = new Timestamp(inspectionDate.getTime());
					tempData.put("fk_quality_feature_id",qualityFeature.getId());
					tempData.put("by_para_mum",qualityFeature.getCode());
					tempData.put("by_para_name",qualityFeature.getName());
					tempData.put("company_id",qualityFeature.getCompanyId());
					tempData.put("created_time",timestamp);
					tempData.put("modified_Time",timestamp);
					tempData.put("creator","system");
					tempData.put("modifier","system");
					tempData.put("by_para_name",qualityFeature.getName());
					tempData.put("by_para_num",qualityFeature.getCode());
					
					List<FeatureRules> ruleSet = qualityFeature.getFeatureRules();
					if (ruleSet != null && ruleSet.size() > 0) {
						String ruleName = "";
						String ruleNo = "";
						for(FeatureRules rule:ruleSet){
							if (rule != null) {
								if (!"".equals(ruleName)) {
									ruleName += ";";
								}
								if (!"".equals(ruleNo)) {
									ruleNo += ";";
								}
								ruleName += rule.getName();
								ruleNo += rule.getNo();
							}
						}
						tempData.put("by_rule_name",ruleName);
						tempData.put("by_rule_num",ruleNo);
					}
					tempData.put("dsc",realTime.getAbnormityCN(true));
					tempData.put("name",realTime.getAbnormityCN());
					tempData.put("occur_time",timestamp);
					tempData.put("occur_date",timestamp);
					tempData.put("pri_state","2");
					tempData.put("new_attr1",0);
					allDatas.add(tempData);
					if(content.length()>0){
						content.append("\r\n");
					}
					content.append(tempData.get("dsc"));
				}
			}
			if(qualityFeature.getFeaturePersons().size()>0){
				emailContentMap.put(qualityFeature,content.toString());
			}
		}
		String params = "company_id,created_time,Modified_Time,creator,Modifier," +
				"By_Para_Name,By_Para_Num,by_rule_name,by_rule_num,name,Dsc,occur_date,Occur_Time,Type,Pri_State,fk_Quality_Feature_id,new_Attr1".toLowerCase();
		Settings settings = ((SessionImpl)session).getFactory().getSettings();
		String dialect = settings.getDialect().toString();
		final StringBuffer insertSql = new StringBuffer("insert into SPC_ABNORMAL_INFO(");
		if(dialect.indexOf(".Oracle")>-1){
			insertSql.append("id," + params + ") values(hibernate_sequence.nextval,");
		}else if(dialect.indexOf(".SQLServer")>-1){
			insertSql.append(params + ") values(");
		}else{
			//TODO:other db
		}
		final String[] paramNames = params.split(",");
		for(int i=0;i<paramNames.length;i++){
			if(i>0){
				insertSql.append(",");
			}
			insertSql.append("?");
		}
		insertSql.append(")");
		
		//每次2000条
		final int total = allDatas.size();
		final int groupCount= 2000;
		int times = total/groupCount;
		if(total%groupCount>0){
			times++;
		}
		long startTimeMillis = System.currentTimeMillis();
		for(int i=0;i<times;i++){
			final int start = i*groupCount;
			final int end = (i+1)*groupCount;
			session.doWork(new Work() {
				@Override
				public void execute(Connection conn) throws SQLException {
					conn.setAutoCommit(false);
					PreparedStatement ps = conn.prepareStatement(insertSql.toString());
					try {
						for(int j=start;j<end&j<total;j++){
							Map<String,Object> valueMap = allDatas.get(j);
							for(int i=0;i<paramNames.length;i++){
								String paramName = paramNames[i];
								ps.setObject(i+1,valueMap.get(paramName));
							}
							ps.addBatch();
						}
						ps.executeBatch();
						conn.commit();
						ps.clearBatch();
					} finally{
						if(ps != null){
							ps.close();
						}
					}
				}
			});
		}
		allDatas.clear();
		//邮件提醒
		Map<String,User> cacheUserMap = new HashMap<String, User>();
		for(QualityFeature qualityFeature : emailContentMap.keySet()){
			for(FeaturePerson person:qualityFeature.getFeaturePersons()){
				if(!cacheUserMap.containsKey(person.getCode())){
					User user = ApiFactory.getAcsService().getUserById(Long.valueOf(person.getCode()));
					if(user==null){
						continue;
					}
					cacheUserMap.put(person.getCode(),user);
				}
				String email = cacheUserMap.get(person.getCode()).getEmail();
				if(StringUtils.isNotEmpty(email)){
//				    //发送邮件(目前异常注释)
//					if(qualityFeature.getProcessPoint()!=null){
//						//质量特性总称ThreeName
//						subject.insert(0,qualityFeature.getProcessPoint().getName());
//						if(qualityFeature.getProcessPoint().getParent()!=null){
//							//质量特性模块TwoName
//							subject.insert(0,qualityFeature.getProcessPoint().getParent().getName());
//							if(qualityFeature.getProcessPoint().getParent().getParent()!=null){
//								//工厂名称
//								subject.insert(0,qualityFeature.getProcessPoint().getParent().getParent().getName());
//							}
//						}
//					}
					AsyncMailUtils.sendMail(email,qualityFeature.getName()+"异常预警",emailContentMap.get(qualityFeature) + "\r\n请登录质量管理系统SPC模块查看:" + PropUtils.getProp("") + ".");
				}
			}
		}
		cacheUserMap.clear();
		System.out.println(Thread.currentThread().getId() + ":insert into monitor infos: use times:" + (System.currentTimeMillis() - startTimeMillis)/1000 + ",success:" + total);
	}
	public static void main(String[] args) {
		TreeSet<JSONObject> set = new TreeSet<JSONObject>(new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject arg0, JSONObject arg1) {
				String inspectionDate1 = arg0.containsKey("inspectionDate")?arg0.getString("inspectionDate"):"";
				String inspectionDate2 = arg1.containsKey("inspectionDate")?arg1.getString("inspectionDate"):"";
				int compareTo = inspectionDate1.compareTo(inspectionDate2);
				if(compareTo == 0){
					compareTo = 1;
				}
				return compareTo;
			}
		});
		set.add(JSONObject.fromObject("{\"id\":1,\"inspectionDate\":\"20180501123032998\"}"));
		set.add(JSONObject.fromObject("{\"id\":2,\"inspectionDate\":\"20180501123032998\"}"));
		set.add(JSONObject.fromObject("{\"id\":3,\"inspectionDate\":\"20180501123032998\"}"));
		set.add(JSONObject.fromObject("{\"id\":4,\"inspectionDate\":\"20180501123032998\"}"));
		System.out.println(set);
	}
}

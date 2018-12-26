package com.ambition.spc.jlanalyse.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.spc.abnormal.service.AbnormalInfoManager;
import com.ambition.spc.dataacquisition.dao.SpcSubGroupDao;
import com.ambition.spc.dataacquisition.service.SpcDataManager;
import com.ambition.spc.dataacquisition.service.SpcSubGroupManager;
import com.ambition.spc.distributechart.draw.DrawDistributeChart;
import com.ambition.spc.distributechart.model.DistributeChartParam;
import com.ambition.spc.entity.Abnormity;
import com.ambition.spc.entity.ControlLimit;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.entity.SpcSgSample;
import com.ambition.spc.entity.SpcSubGroup;
import com.ambition.spc.histogram.draw.DrawHistogram;
import com.ambition.spc.histogram.entity.HistogramParam;
import com.ambition.spc.jlanalyse.draw.DrawJLControlChart;
import com.ambition.spc.jlanalyse.entity.JLControlChartParam;
import com.ambition.spc.jlanalyse.entity.JLOriginalData;
import com.ambition.spc.jlanalyse.entity.JLSampleData;
import com.ambition.spc.jlanalyse.util.JLcalculator;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.spc.util.Calculator;
import com.ambition.spc.util.ConstantsOfSPC;
import com.ambition.util.common.CommonUtil1;
import com.ambition.util.common.DateUtil;
import com.ibm.icu.text.SimpleDateFormat;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**    
* JlanalyseDrawManager.java
* @authorBy wlongfeng
*
*/
@Service
public class JlanalyseDrawManager {

	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private ControlLimitManager controlLimitManager;
	@Autowired
	private SpcSubGroupManager spcSubGroupManager;
	@Autowired
	private AbnormalInfoManager abnormalInfoManager;
	@Autowired
	private SpcSubGroupDao spcSubGroupDao;
	@Autowired
	private SpcDataManager spcDataManager;
	
	/**
	 * 封装数据计算x-s的数据,调用算法计算均值标准差放入getjLResult
	 * @param jLcalculator
	 * @param featureId
	 * @param startDate
	 * @param endDate
	 * @param originalData
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public void calculatJl(JLcalculator jLcalculator,String featureId,Date startDate,Date endDate,JLOriginalData originalData,String type,String lastAmout,JSONArray condition,JSONObject params){
		List<SpcSubGroup> spcSubGroupList=new ArrayList<SpcSubGroup>();
		List<SpcSubGroup> allspcSubGroupList=new ArrayList<SpcSubGroup>();
		QualityFeature qualityFeature=new QualityFeature();
		int effectiveCapacity=0;
		if (featureId != null && !"".equals(featureId)) {
			qualityFeature = qualityFeatureManager.getQualityFeature(Long.parseLong(featureId));
			if(qualityFeature!=null){
				if(qualityFeature.getEffectiveCapacity()!=null){
					effectiveCapacity = qualityFeature.getEffectiveCapacity();
				}
			}
		}
		if(qualityFeature.getId()!=null && !"".equals(qualityFeature.getId())){
			//封装每组的样本数据
			ArrayList data=new ArrayList();
			for(int i=0;i<spcSubGroupList.size();i++){
				SpcSubGroup group = spcSubGroupList.get(i);
				if(group.getSpcSgSamples() != null){
					List<SpcSgSample> sampleList = group.getSpcSgSamples();
					double[] a = new double[effectiveCapacity];
					if(sampleList.size()>=effectiveCapacity){
						for(int j=0;j<effectiveCapacity;j++){
							SpcSgSample sample=sampleList.get(j);
							if(sample!=null && sample.getSamValue()!=null){
								a[j]=sample.getSamValue();
							}
						}
						data.add(a);
					}
				}
			}
			//封装所有数据
			if(qualityFeature.getControlChart()!=null){
				if(params!=null&&qualityFeature.getControlChart().equals("4")){
					double alldataaverage=0.0;
					double[] alldata=new double[effectiveCapacity*allspcSubGroupList.size()];
					int k=0;
					for(int i=0;i<allspcSubGroupList.size();i++){
						SpcSubGroup group=allspcSubGroupList.get(i);
						List<SpcSgSample> sampleList=group.getSpcSgSamples();
						double[] a=new double[effectiveCapacity];
						if(sampleList.size()>=effectiveCapacity){
							for(int j=0;j<effectiveCapacity;j++){
								SpcSgSample sample=sampleList.get(j);
								alldata[(k * effectiveCapacity + j)] = sample.getSamValue();
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
			if(isAutoCl.equals("s")){
				controlState=ConstantsOfSPC.SPC_SAMPLING_STATE_CONTROL_I;
			}else{
				controlState=ConstantsOfSPC.SPC_SAMPLING_STATE_ANALYSIS_I;
			}
			originalData.setControlState(controlState);
			if(controlState.equals(ConstantsOfSPC.SPC_SAMPLING_STATE_CONTROL_I)){
				List<ControlLimit> controlLimits=controlLimitManager.getControlLimitDesc(Long.parseLong(featureId),null);
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
			DecimalFormat de=new DecimalFormat(digit);
			ActionContext.getContext().put("decimalFormat", de);
			//调用算法计算均值标准差放入getjLResult
			jLcalculator.calculate(originalData);
		}
	}
	
	/**
	 * 封装数据计算x-s的数据,调用算法计算均值标准差放入getjLResult
	 * @param jLcalculator
	 * @param featureId
	 * @param startDate
	 * @param endDate
	 * @param originalData
	 */
	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	public void cpkCalculator(JLcalculator jLcalculator,String featureId,Date startDate,Date endDate,JLOriginalData originalData,String tag_code,String tag_value,JSONObject params){
		List<SpcSubGroup> spcSubGroupList=new ArrayList<SpcSubGroup>();
		List<SpcSubGroup> allspcSubGroupList=new ArrayList<SpcSubGroup>();
		QualityFeature qualityFeature=new QualityFeature();
		int effectiveCapacity=0;
		if (featureId != null) {
			qualityFeature = qualityFeatureManager.getQualityFeature(Long.parseLong(featureId));
			if(qualityFeature!=null){
				if(qualityFeature.getEffectiveCapacity()!=null){
					effectiveCapacity = qualityFeature.getEffectiveCapacity();
				}
			}
		}
		if (featureId != null) {
			Date allstartDate = null;
			Date allendDate = null;
			if(params!=null){
				String startDateStr=params.getString("startDate_ge_date");
				String endDateStr=params.getString("endDate_le_date");
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				try {
					allstartDate = sdf.parse(startDateStr);
					allendDate = sdf.parse(endDateStr);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
			spcSubGroupList = spcSubGroupManager.getSpcSubGroupListByCodeAndValue(Long.parseLong(featureId),startDate,endDate,tag_code,tag_value,effectiveCapacity);
			allspcSubGroupList = spcSubGroupManager.getSpcSubGroupListByCodeAndValue(Long.parseLong(featureId),allstartDate,allendDate,tag_code,tag_value,effectiveCapacity);
		}
		if(qualityFeature.getId()!=null){
			//封装每组的样本数据
			ArrayList data=new ArrayList();
			for(int i=0;i<spcSubGroupList.size();i++){
				SpcSubGroup group=spcSubGroupList.get(i);
				List<SpcSgSample> sampleList=group.getSpcSgSamples();
				double[] a=new double[effectiveCapacity];
				if(sampleList.size()>=effectiveCapacity){
					for(int j=0;j<effectiveCapacity;j++){
						SpcSgSample sample=sampleList.get(j);
						a[j]=sample.getSamValue();
					}
					data.add(a);
				}
			}
			
			//封装所有数据当为单值控制图时候，需要封装所有的数据计算平均值
			if(params!=null&&qualityFeature.getControlChart().equals("4")){
				double alldataaverage=0.0;
				double[] alldata=new double[effectiveCapacity*allspcSubGroupList.size()];
				int k=0;
				for(int i=0;i<allspcSubGroupList.size();i++){
					SpcSubGroup group=allspcSubGroupList.get(i);
					List<SpcSgSample> sampleList=group.getSpcSgSamples();
					double[] a=new double[effectiveCapacity];
					if(sampleList.size()>=effectiveCapacity){
						for(int j=0;j<effectiveCapacity;j++){
							SpcSgSample sample=sampleList.get(j);
							alldata[(k * effectiveCapacity + j)] = sample.getSamValue();
						}
						k++;
					}
				}
				alldataaverage=Calculator.average(alldata);
				originalData.setAlldataaverage(alldataaverage);
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
				List<ControlLimit> controlLimits=controlLimitManager.getControlLimitDesc(Long.parseLong(featureId),null);
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
			DecimalFormat de=new DecimalFormat(digit);
			ActionContext.getContext().put("decimalFormat", de);
			//调用算法计算均值标准差放入getjLResult
			jLcalculator.calculate(originalData);
		}
	}
	
	/**
	 * 画控制图核心算法(封装数据计算x-s的数据,调用算法计算均值标准差放入getjLResul)
	 * @param featureId
	 * @param startDate
	 * @param endDate
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	public void drawControlPic1(String featureId,Date startDate,Date endDate,String type,String lastAmout,JSONArray condition){
		List<SpcSubGroup> spcSubGroupList=new ArrayList<SpcSubGroup>();
		QualityFeature qualityFeature=new QualityFeature();
		JLcalculator jLcalculator=new JLcalculator();
		JLOriginalData originalData=new JLOriginalData();
		int effectiveCapacity=0;
		if(featureId!=null && !"".equals(featureId)){
			qualityFeature=qualityFeatureManager.getQualityFeature(Long.parseLong(featureId));
			if(qualityFeature != null){
				if(qualityFeature.getEffectiveCapacity()!=null){
					effectiveCapacity=qualityFeature.getEffectiveCapacity();
				}
			}
		}
		JSONArray datas = new JSONArray();
		if(qualityFeature.getId()!=null && !"".equals(qualityFeature.getId())){
			//int h=0;
			//封装每组的样本数据
//			for(int i=0;i<spcSubGroupList.size();i++){
//				SpcSubGroup group=spcSubGroupList.get(i);
//				List<SpcSgSample> sampleList=group.getSpcSgSamples();
//			}
				
			ArrayList data=new ArrayList();
			for(int i=0;i<spcSubGroupList.size();i++){
				SpcSubGroup group=spcSubGroupList.get(i);
				//缓存坐标数据
				JSONObject d = new JSONObject();
				d.put("id",group.getId());
				d.put("date", DateUtil.formateDateStr(group.getCreatedTime()));
				datas.add(d);
				
				List<SpcSgSample> sampleList = group.getSpcSgSamples();
				double[] a=new double[effectiveCapacity];
				if(sampleList != null && sampleList.size()>=effectiveCapacity){
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
				List<ControlLimit> controlLimits=controlLimitManager.getControlLimitDesc(Long.parseLong(featureId),null);
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
			
			BufferedImage image = null ;
			//封装画控制图参数
			JLControlChartParam jlp=null;
			//封装异常信息准则数组abnormity
			Abnormity[] abnormityArray = null;
			try {
				abnormityArray = abnormalInfoManager.judgeCollectDataOfAbnormity(qualityFeature,false,0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(qualityFeature.getControlChart()!=null){
				if(qualityFeature.getControlChart().equals("1")){
					originalData.setRCL(jLcalculator.getjLResult().getRCL());
					originalData.setRLCL(jLcalculator.getjLResult().getRLCL());
					originalData.setRUCL(jLcalculator.getjLResult().getRUCL());
					originalData.setXCL(jLcalculator.getjLResult().getXCL());
					originalData.setXLCL(jLcalculator.getjLResult().getXLCL());
					originalData.setXUCL(jLcalculator.getjLResult().getXUCL());
					jlp=new JLControlChartParam(originalData, "X-R管理图", abnormityArray, false);
					jlp.setUpTitle("X管理图");
					jlp.setLowTitle("R管理图");
				}else if(qualityFeature.getControlChart().equals("2")){
					originalData.setSCL(jLcalculator.getjLResult().getSCL());
					originalData.setSLCL(jLcalculator.getjLResult().getSLCL());
					originalData.setSUCL(jLcalculator.getjLResult().getSUCL());
					originalData.setXCL(jLcalculator.getjLResult().getXCL());
					originalData.setXLCL(jLcalculator.getjLResult().getXLCL());
					originalData.setXUCL(jLcalculator.getjLResult().getXUCL());
					jlp=new JLControlChartParam(originalData, "X-S管理图", abnormityArray, false);
					jlp.setUpTitle("X管理图");
					jlp.setLowTitle("S管理图");
				}else if(qualityFeature.getControlChart().equals("4")){
					originalData.setRCL(jLcalculator.getjLResult().getRCL());
					originalData.setRLCL(jLcalculator.getjLResult().getRLCL());
					originalData.setRUCL(jLcalculator.getjLResult().getRUCL());
					originalData.setXCL(jLcalculator.getjLResult().getXCL());
					originalData.setXLCL(jLcalculator.getjLResult().getXLCL());
					originalData.setXUCL(jLcalculator.getjLResult().getXUCL());
					originalData.setUpTitle("单值控制图");
					originalData.setLowTitle("移动极差控制图");
					jlp=new JLControlChartParam(originalData, "", abnormityArray, false);
				}
			}else{
				jlp=new JLControlChartParam(originalData, "", abnormityArray, false);
			}
			if(qualityFeature.getPrecs()!=null){
				jlp.setDataPrecision(Integer.parseInt(qualityFeature.getPrecs()));
			}

			String offside = Struts2Utils.getParameter("offside");
			String dotWidth = Struts2Utils.getParameter("dotWidth");
			
			HttpServletRequest request=Struts2Utils.getRequest();
			HttpServletResponse response=Struts2Utils.getResponse();
			if(jlp!=null){
				if(offside != null && !offside.equals("undefined") && !offside.equals("")){
					jlp.setOffside(Integer.valueOf(offside));
				}else{
					jlp.setOffside(10);
				}
				if(dotWidth != null && !dotWidth.equals("undefined") && !dotWidth.equals("")){
					jlp.setDotWidth(Integer.valueOf(dotWidth));
				}else{
					jlp.setDotWidth(20);
				}
				jlp.setImageWidth(800);
				//缓存每个点对应的数据
				Struts2Utils.getRequest().setAttribute("datas",datas);
				image = DrawJLControlChart.drawJLChart(jlp,null);
			}
			if(offside != null && !offside.equals("undefined") && !offside.equals("")){
				 request.getSession().setAttribute("offside", Integer.valueOf(offside));
			}else{
				 request.getSession().setAttribute("offside", 10);
			}
		    response.setHeader("Pragma", "No-cache");
	    	response.setHeader("Cache-Control", "no-cache");
	    	response.setDateHeader("Expires", 0);
			OutputStream out = null;	
			try {
				String img_url=request.getParameter("imgUrl");
				if(img_url!=null){
					this.picImageToDir(img_url, request, image);
				}
				response.setContentType("image/jpeg");
				out = response.getOutputStream();
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				encoder.encode(image);
	            encoder.getOutputStream();
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					if(out!=null){
						out.close();
					}
				} catch (IOException ex3) {
					ex3.printStackTrace();
				}
			}
		}
	}
	
	/**
	  * 方法名:画控制图核心算法(封装数据计算x-s的数据,调用算法计算均值标准差放入getjLResul) 
	  * <p>功能说明：</p>
	  * @param featureId 质量特性ID
	  * @param lastAmout 最后子组数
	 */
	@SuppressWarnings({ "unchecked","static-access" })
	public void drawControlPic(String featureId,Integer lastAmout,Map<String,Object> showParamMap){
		QualityFeature qualityFeature = new QualityFeature();
		JLcalculator jLcalculator=new JLcalculator();
		JLOriginalData originalData=new JLOriginalData();
		if(CommonUtil1.isInteger(featureId)){
			qualityFeature = qualityFeatureManager.getQualityFeatureFromCache(Long.parseLong(featureId),null);
		}
		
		Integer lastAmount = null;
		String isLast = Struts2Utils.getParameter("isLast");
		if("true".equals(isLast)){
			lastAmount = 30;
		}
		Map<String,Object> resultMap = spcDataManager.querySpcDataValues(qualityFeature,"","", null,1,30, lastAmount);
		List<double[]> values = (List<double[]>)resultMap.get("values");
		JSONArray datas = (JSONArray)resultMap.get("posValues");
		
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
			List<ControlLimit> controlLimits=controlLimitManager.getControlLimitDesc(Long.parseLong(featureId),null);
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
		
		Integer effectiveCapacity = qualityFeature.getEffectiveCapacity();
		originalData.setSampleQuantity(effectiveCapacity);
		//调用算法计算均值标准差放入getjLResult
		jLcalculator.calculate(originalData);
		
		BufferedImage image = null ;
		//封装画控制图参数
		JLControlChartParam jlp=null;
		//封装异常信息准则数组abnormity
		Abnormity[] abnormityArray = null;
		try {
			abnormityArray = abnormalInfoManager.judgeCollectDataOfAbnormity(qualityFeature,false,0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(qualityFeature.getControlChart()!=null){
			if(qualityFeature.getControlChart().equals("1")){
				originalData.setRCL(jLcalculator.getjLResult().getRCL());
				originalData.setRLCL(jLcalculator.getjLResult().getRLCL());
				originalData.setRUCL(jLcalculator.getjLResult().getRUCL());
				originalData.setXCL(jLcalculator.getjLResult().getXCL());
				originalData.setXLCL(jLcalculator.getjLResult().getXLCL());
				originalData.setXUCL(jLcalculator.getjLResult().getXUCL());
				jlp=new JLControlChartParam(originalData, "X-R管理图", abnormityArray, false);
				jlp.setUpTitle("X管理图");
				jlp.setLowTitle("R管理图");
			}else if(qualityFeature.getControlChart().equals("2")){
				originalData.setSCL(jLcalculator.getjLResult().getSCL());
				originalData.setSLCL(jLcalculator.getjLResult().getSLCL());
				originalData.setSUCL(jLcalculator.getjLResult().getSUCL());
				originalData.setXCL(jLcalculator.getjLResult().getXCL());
				originalData.setXLCL(jLcalculator.getjLResult().getXLCL());
				originalData.setXUCL(jLcalculator.getjLResult().getXUCL());
				jlp=new JLControlChartParam(originalData, "X-S管理图", abnormityArray, false);
				jlp.setUpTitle("X管理图");
				jlp.setLowTitle("S管理图");
			}else if(qualityFeature.getControlChart().equals("4")){
				originalData.setRCL(jLcalculator.getjLResult().getRCL());
				originalData.setRLCL(jLcalculator.getjLResult().getRLCL());
				originalData.setRUCL(jLcalculator.getjLResult().getRUCL());
				originalData.setXCL(jLcalculator.getjLResult().getXCL());
				originalData.setXLCL(jLcalculator.getjLResult().getXLCL());
				originalData.setXUCL(jLcalculator.getjLResult().getXUCL());
				originalData.setUpTitle("单值控制图");
				originalData.setLowTitle("移动极差控制图");
				jlp=new JLControlChartParam(originalData, "", abnormityArray, false);
			}
		}else{
			jlp=new JLControlChartParam(originalData, "", abnormityArray, false);
		}
		if(qualityFeature.getPrecs()!=null){
			jlp.setDataPrecision(Integer.parseInt(qualityFeature.getPrecs()));
		}

		String offside = Struts2Utils.getParameter("offside");
		String dotWidth = Struts2Utils.getParameter("dotWidth");
		
		HttpServletRequest request=Struts2Utils.getRequest();
		HttpServletResponse response=Struts2Utils.getResponse();
		if(jlp!=null){
			if(offside != null && !offside.equals("undefined") && !offside.equals("")){
				jlp.setOffside(Integer.valueOf(offside));
			}else{
				jlp.setOffside(10);
			}
			if(dotWidth != null && !dotWidth.equals("undefined") && !dotWidth.equals("")){
				jlp.setDotWidth(Integer.valueOf(dotWidth));
			}else{
				jlp.setDotWidth(20);
			}
			jlp.setImageWidth(800);
			//缓存每个点对应的数据
			Struts2Utils.getRequest().setAttribute("datas",datas);
			image = DrawJLControlChart.drawJLChart(jlp,showParamMap);
		}
		if(offside != null && !offside.equals("undefined") && !offside.equals("")){
			 request.getSession().setAttribute("offside", Integer.valueOf(offside));
		}else{
			 request.getSession().setAttribute("offside", 10);
		}
	    response.setHeader("Pragma", "No-cache");
    	response.setHeader("Cache-Control", "no-cache");
    	response.setDateHeader("Expires", 0);
		OutputStream out = null;	
		try {
			String img_url=request.getParameter("imgUrl");
			if(img_url!=null){
				this.picImageToDir(img_url, request, image);
			}
			response.setContentType("image/jpeg");
			out = response.getOutputStream();
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
            encoder.getOutputStream();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(out!=null){
					out.close();
				}
			} catch (IOException ex3) {
				ex3.printStackTrace();
			}
		}
	}
	
	/**
	 * 画直方图核心算法
	 * @param featureId
	 * @param startDate
	 * @param endDate
	 */
	public void drawHistogramDraw(String featureId,Date startDate,Date endDate,String lastAmout,JSONArray condition){
		QualityFeature qualityFeature=new QualityFeature();
		int effectiveCapacity=0;		
		List<SpcSubGroup> spcSubGroupList=new ArrayList<SpcSubGroup>();
		if(featureId!=null && !"".equals(featureId)){
			qualityFeature=qualityFeatureManager.getQualityFeature(Long.parseLong(featureId));
			if(qualityFeature!=null){
				if(qualityFeature.getEffectiveCapacity()!=null){
					effectiveCapacity=qualityFeature.getSampleCapacity();
				}
			}
		}
		double[] itemList = new double[spcSubGroupList.size() * effectiveCapacity];
		if(qualityFeature.getId()!=null && !"".equals(qualityFeature.getId())){
			//封装总的样本数据
			for(int i=0;i<spcSubGroupList.size();i++){
				SpcSubGroup group=spcSubGroupList.get(i);
				List<SpcSgSample> sampleList=group.getSpcSgSamples();
				for(int j=0;j<sampleList.size();j++){
					SpcSgSample sample=sampleList.get(j);
					itemList[i * sampleList.size() + j]=sample.getSamValue();
				}
			}
			BufferedImage image = null;
			HistogramParam param = new HistogramParam(itemList, "X", qualityFeature.getUpperTarge(), qualityFeature.getLowerTarge());
			if(qualityFeature.getPrecs()!=null){
				param.setDataPrecision(Integer.parseInt(qualityFeature.getPrecs()));
			}
			param.setImageHeight(400);
			param.setImageWidth(800);
			HttpServletResponse response=Struts2Utils.getResponse();
			image = DrawHistogram.drawHistogram(param);
			OutputStream out = null;
			try {
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				response.setHeader("Pragma", "No-cache");
				response.setContentType("image/jpeg");
				out = response.getOutputStream();
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				encoder.encode(image);
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (IOException ex3) {
					ex3.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * 画散步图核心算法
	 * @param featureId
	 * @param startDate
	 * @param endDate
	 * @throws InterruptedException 
	 */
	public void drawDistributeChart(String xqualityFeatureId,String yqualityFeatureId,String startDateStr,String endDateStr,
			String group,String type,String beginNo,String endNo) throws InterruptedException{
		QualityFeature xqualityFeature = qualityFeatureManager.getQualityFeatureFromCache(Long.parseLong(xqualityFeatureId),null);
		QualityFeature yqualityFeature= qualityFeatureManager.getQualityFeatureFromCache(Long.parseLong(yqualityFeatureId),null);
		int xeffectiveCapacity=5;
		int yeffectiveCapacity=5;	
		if(xqualityFeature.getSampleCapacity()!=null){
			xeffectiveCapacity=xqualityFeature.getSampleCapacity();
		}
		if(yqualityFeature.getSampleCapacity()!=null){
			yeffectiveCapacity=yqualityFeature.getSampleCapacity();
		}
		List<QualityFeature> qualityFeatures = new ArrayList<QualityFeature>();
		qualityFeatures.add(xqualityFeature);
		qualityFeatures.add(yqualityFeature);
		
		SpcDataManager spcDataManager = new SpcDataManager(spcSubGroupDao.getSession());
		Map<String,Map<String,Object>> featureDataMap = null;
		//按子组号
		if("no".equalsIgnoreCase(type)){
			featureDataMap = new HashMap<String, Map<String,Object>>();
			//查询范围内的数据
			Integer firstNo = Integer.valueOf(beginNo);
			Integer lastNo = Integer.valueOf(endNo);
			Map<String,Object> xresultMap = spcDataManager.querySpcDataValueByRange(xqualityFeature,
					null,
					null,null,firstNo-1,lastNo-firstNo+1,null);
			featureDataMap.put(xqualityFeatureId,xresultMap);
			
			Map<String,Object> yresultMap = spcDataManager.querySpcDataValueByRange(yqualityFeature,
					null,null,null,firstNo-1,lastNo-firstNo+1,null);
			featureDataMap.put(yqualityFeatureId,yresultMap);
		}else{
			featureDataMap = spcDataManager.multiQuerySpcDataValues(qualityFeatures,
					startDateStr, endDateStr,null, 1,null, null);
		}
		
		//封装数据
		Map<String,Object> xresultMap = featureDataMap.get(xqualityFeatureId);
		@SuppressWarnings("unchecked")
		List<double[]> xdata = (List<double[]>)xresultMap.get("values");
//		
//		ArrayList<Integer> xNo=new ArrayList<Integer>();
//		for(int i=0;i<xspcSubGroupList.size();i++){
//			SpcSubGroup xSpcSubGroup=xspcSubGroupList.get(i);
//			List<SpcSgSample> sampleList=xSpcSubGroup.getSpcSgSamples();
//			double[] a=new double[xeffectiveCapacity];
//			if(sampleList.size()>=xeffectiveCapacity){
//				for(int j=0;j<xeffectiveCapacity;j++){
//					SpcSgSample sample=sampleList.get(j);
//					a[j]=sample.getSamValue();
//				}
//				xdata.add(a);
//				xNo.add(xSpcSubGroup.getSubGroupOrderNum());
//			}
//		}
		
		Map<String,Object> yresultMap = featureDataMap.get(yqualityFeatureId);
		@SuppressWarnings("unchecked")
		List<double[]> ydata = (List<double[]>)yresultMap.get("values");
//		ArrayList<Integer> yNo=new ArrayList<Integer>();
//		for(int i=0;i<yspcSubGroupList.size();i++){
//			SpcSubGroup ySpcSubGroup=yspcSubGroupList.get(i);
//			List<SpcSgSample> sampleList=ySpcSubGroup.getSpcSgSamples();
//			double[] a=new double[yeffectiveCapacity];
//			if(sampleList.size()>=yeffectiveCapacity){
//				for(int j=0;j<yeffectiveCapacity;j++){
//					SpcSgSample sample=sampleList.get(j);
//					a[j]=sample.getSamValue();
//				}
//				ydata.add(a);
//				yNo.add(ySpcSubGroup.getSubGroupOrderNum());
//			}
//		}
		//封装每组样本数据的最大值、最小值、平均值等
		ArrayList<JLSampleData> xdatalist=new ArrayList<JLSampleData>();
		for(int i=0;i<xdata.size();i++){
			JLSampleData jl=new JLSampleData();
			double[] a=(double[])xdata.get(i);
			jl.setData(a);
			jl.setAverage(Calculator.average(a));
			jl.setMax(Calculator.max(a));
			jl.setMin(Calculator.min(a));
			jl.setS(Calculator.calculateS(a));
			jl.setR(Calculator.calculateR(a));
			jl.setMedian(Calculator.calculateMedian(a));
			xdatalist.add(jl);
		}
		
		ArrayList<JLSampleData> ydatalist=new ArrayList<JLSampleData>();
		for(int i=0;i<ydata.size();i++){
			JLSampleData jl=new JLSampleData();
			double[] a=(double[])ydata.get(i);
			jl.setData(a);
			jl.setAverage(Calculator.average(a));
			jl.setMax(Calculator.max(a));
			jl.setMin(Calculator.min(a));
			jl.setS(Calculator.calculateS(a));
			jl.setR(Calculator.calculateR(a));
			jl.setMedian(Calculator.calculateMedian(a));
			ydatalist.add(jl);
		}
	//根据组号来查找数据
	Map<Integer,Double> xMap=new HashMap<Integer,Double>();
	Map<Integer,Double> yMap=new HashMap<Integer,Double>();
	//根据统计分组初始化数据
	double[] xData=null;
	double[] yData=null;
	if(group.equals("data")){
		 xData=new double[xdata.size()*xeffectiveCapacity];
		 yData=new double[ydata.size()*yeffectiveCapacity];
	}else{
		 xData=new double[xdatalist.size()];
		 yData=new double[ydatalist.size()];
	}
	
	//根据统计分组进行封装数据
	Integer startNo = 1;
	if("no".equals(group)&&CommonUtil1.isInteger(beginNo)){
		startNo = Integer.valueOf(beginNo);
	}
	if(group.equals("data")){
		int k=0;
		int  h=0;
		for(int i=0;i<xdata.size();i++){
			double[] datas=xdata.get(i);
			for (int j = 0; j < datas.length; j++) {
				xData[(k * datas.length + j)] = datas[j];
			}
			k++;
		}
		for(int i=0;i<ydata.size();i++){
			double[] datas=ydata.get(i);
			for (int j = 0; j < datas.length; j++) {
				yData[(h * datas.length + j)] = datas[j];
			}
			h++;
		}
	}else if(group.equals("mean")){
		for(int i=0;i<xdatalist.size();i++){
			JLSampleData jl=xdatalist.get(i);
			xData[i]=jl.getAverage();
//			int no=xNo.get(i);
			int no = i+startNo;
			xMap.put(no, jl.getAverage());
		}
		for(int i=0;i<ydatalist.size();i++){
			JLSampleData jl=ydatalist.get(i);
			yData[i]=jl.getAverage();
//			int no=yNo.get(i);
			int no = i+startNo;
			yMap.put(no, jl.getAverage());
		}
	}else if(group.equals("max")){
		for(int i=0;i<xdatalist.size();i++){
			JLSampleData jl=xdatalist.get(i);
			xData[i]=jl.getMax();
//			int no=xNo.get(i);
			int no = i+startNo;
			xMap.put(no, jl.getMax());
		}
		for(int i=0;i<ydatalist.size();i++){
			JLSampleData jl=ydatalist.get(i);
			yData[i]=jl.getMax();
//			int no=yNo.get(i);
			int no = i+startNo;
			yMap.put(no, jl.getMax());
		}
	}else if(group.equals("min")){
		for(int i=0;i<xdatalist.size();i++){
			JLSampleData jl=xdatalist.get(i);
			xData[i]=jl.getMin();
//			int no=xNo.get(i);
			int no = i+startNo;
			xMap.put(no, jl.getMin());
		}
		for(int i=0;i<ydatalist.size();i++){
			JLSampleData jl=ydatalist.get(i);
			yData[i]=jl.getMin();
//			int no=yNo.get(i);
			int no = i+startNo;
			yMap.put(no, jl.getMin());
		}
	}else if(group.equals("range")){
		for(int i=0;i<xdatalist.size();i++){
			JLSampleData jl=xdatalist.get(i);
			xData[i]=jl.getR();
//			int no=xNo.get(i);
			int no = i+startNo;
			xMap.put(no, jl.getR());
		}
		for(int i=0;i<ydatalist.size();i++){
			JLSampleData jl=ydatalist.get(i);
			yData[i]=jl.getR();
//			int no=yNo.get(i);
			int no = i+startNo;
			yMap.put(no, jl.getR());
		}
	}else if(group.equals("stdev")){
		for(int i=0;i<xdatalist.size();i++){
			JLSampleData jl=xdatalist.get(i);
			xData[i]=jl.getS();
//			int no=xNo.get(i);
			int no = i+startNo;
			xMap.put(no, jl.getS());
		}
		for(int i=0;i<ydatalist.size();i++){
			JLSampleData jl=ydatalist.get(i);
			yData[i]=jl.getS();
//			int no=yNo.get(i);
			int no = i+startNo;
			yMap.put(no, jl.getS());
		}
	}
	//封装图表数据
		int picDatalength=0;
		Boolean forxMap=true;
		if(xData.length>=yData.length){
			picDatalength=yData.length;
			forxMap=false;
		}else{
			picDatalength=xData.length;
		}
		double[] picData=new double[picDatalength*2];
		int j=0;
		if(type.equals("date")||group.equals("data")){
			for(int i=0;i<picDatalength;i++){
				picData[j]=yData[i];
				j=j+1;
				picData[j]=xData[i];
				j=j+1;
			}
		}else{
			//根据组号来匹配数据
			if(forxMap){
				for(Integer no:xMap.keySet()){
					double x=xMap.get(no);
					double y=yMap.get(no);
					if(xMap.containsKey(no)&&yMap.containsKey(no)){
						picData[j]=y;
						j=j+1;
						picData[j]=x;
						j=j+1;
					}
				}	
			}else{
				for(Integer no:yMap.keySet()){
					double x=xMap.get(no);
					double y=yMap.get(no);
					if(xMap.containsKey(no)&&yMap.containsKey(no)){
						picData[j]=y;
						j=j+1;
						picData[j]=x;
						j=j+1;
					}
				}	
			}
		}
		double[][] data6 = new double[picData.length/2][2];
		for (int i = 0; i < picData.length; i++) {
			try {
				data6[i/2][(i+1)%2] = picData[i];
			} catch (Exception ex) {
				data6[i/2][i%2] = 0;
			}
		}
		DistributeChartParam param = new DistributeChartParam(data6,"X", "Y");	
		BufferedImage image = null;
		param.setImageHeight(400);
		param.setImageWidth(800);
		HttpServletResponse response=Struts2Utils.getResponse();
		image = DrawDistributeChart.drawDistributeChart(param);
		OutputStream out = null;
		try {
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Pragma", "No-cache");
			response.setContentType("image/jpeg");
			out = response.getOutputStream();
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException ex3) {
				ex3.printStackTrace();
			}
		}
	}
	
	/**
	 * 计算往前几天时间
	 * @param date
	 * @return dateMap
	 */
	@SuppressWarnings("deprecation")
	public Map<String,Date> getStartEndDate(String date){
		Map<String,Date> dateMap=new HashMap<String,Date>();
		//计算时间begin
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		Date startDate=null;
		Date endDate=null;
		int n=0;
		if(StringUtils.isNotEmpty(date)){
			n=Integer.parseInt(date)+1;
			endDate=endCal.getTime();
			endDate.setMinutes(0);
			endDate.setHours(0);
			endDate.setSeconds(0);
			startCal.setTime(endDate);
			startCal.add(Calendar.DATE, -n);
			startDate=startCal.getTime();
			dateMap.put("startDate", startDate);
			dateMap.put("endDate", endDate);
		}else{
			endDate=endCal.getTime();
			endDate.setMinutes(0);
			endDate.setHours(0);
			endDate.setSeconds(0);
			startCal.setTime(endDate);
			startCal.add(Calendar.DATE, -1);
			startDate=startCal.getTime();
			dateMap.put("startDate", startDate);
			dateMap.put("endDate", endDate);
		}
		return dateMap;
		
	}
	
	/**
	 * 转换json
	 * @param params
	 * @return
	 */
	public JSONObject convertJsonObject(JSONObject params){
		JSONObject resultJson = new JSONObject();
		if(params == null){
			return resultJson;
		}
		for(Object key : params.keySet()){
			resultJson.put(key,params.getJSONArray(key.toString()).get(0));
		}
		return resultJson;
	}
	
	/**
	 * 
	 * @param img_url
	 * @param request
	 * @param image
	 */
	public static  void picImageToDir(String img_url,HttpServletRequest request,BufferedImage image){
		if(img_url!=null){
			String projectUrl=request.getSession().getServletContext().getRealPath("/");
			try {
				ImageIO.write(image, "JPEG", new File(projectUrl+img_url));
			} catch (IOException e) {
				e.printStackTrace();
			}// 输出到文件流
		}
	}
	
	/**
	 * 转换map对象到options(带父类)
	 * @param map
	 * @return
	 */
	public List<Option> convertListToParentOptions(List<QualityFeature> qualityFeatures){
		List<Option> options = new ArrayList<Option>();
		for(int i=0;i<qualityFeatures.size();i++){
			Option option = new Option();
			QualityFeature q=qualityFeatures.get(i);
			StringBuffer qAll = new StringBuffer("");
			if(q.getProcessPoint()!=null){
				//质量特性总称ThreeName
				qAll.insert(0,q.getProcessPoint().getName()+"-");
				if(q.getProcessPoint().getParent()!=null){
					//质量特性模块TwoName
					qAll.insert(0,q.getProcessPoint().getParent().getName()+"-");
					if(q.getProcessPoint().getParent().getParent()!=null){
						//工厂名称
						qAll.insert(0,q.getProcessPoint().getParent().getParent().getName()+"-");
					}
				}
			}
			option.setName(qAll.append(q.getName()).toString());
			option.setValue(Long.toString(q.getId()));
			options.add(option);
		}
		return options;
	}
	/**
	 * 转换map对象到options
	 * @param map
	 * @return
	 */
	public List<Option> convertListToOptions(List<QualityFeature> qualityFeatures){
		List<Option> options = new ArrayList<Option>();
		for(int i=0;i<qualityFeatures.size();i++){
			Option option = new Option();
			QualityFeature q=qualityFeatures.get(i);
			option.setName(q.getName());
			option.setValue(Long.toString(q.getId()));
			options.add(option);
		}
		return options;
	}
}

package com.ambition.spc.statistics.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.spc.abnormal.service.AbnormalInfoManager;
import com.ambition.spc.dataacquisition.service.SpcDataManager;
import com.ambition.spc.entity.Abnormity;
import com.ambition.spc.entity.ControlLimit;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.jlanalyse.draw.DrawJLControlChart;
import com.ambition.spc.jlanalyse.entity.JLControlChartParam;
import com.ambition.spc.jlanalyse.entity.JLOriginalData;
import com.ambition.spc.jlanalyse.entity.JLSampleData;
import com.ambition.spc.jlanalyse.service.JlanalyseDrawManager;
import com.ambition.spc.jlanalyse.util.JLcalculator;
import com.ambition.spc.processdefine.dao.QualityFeatureDao;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.spc.util.Calculator;
import com.ambition.spc.util.ConstantsOfSPC;
import com.ambition.util.common.CommonUtil1;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 类名:统计分析数据
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2015-7-1 发布
 */
@Service
public class DrawControlManager {
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private QualityFeatureDao qualityFeatureDao;
	/**
	 * 画控制图核心算法(封装数据计算x-s的数据,调用算法计算均值标准差放入getjLResul)
	 * @param featureId
	 * @param startDate
	 * @param endDate
	 */
	@SuppressWarnings({ "unchecked"})
	public void drawControlPic(String featureId,String startDateStr,String endDateStr,String type,String lastAmoutStr,
			JSONObject layerParams,Map<String,Object> showParamMap){
		JLcalculator jLcalculator=new JLcalculator();
		JLOriginalData originalData=new JLOriginalData();
		
		//质量特性
		QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureFromCache(Long.valueOf(featureId),null);
		int effectiveCapacity = qualityFeature.getEffectiveCapacity();
		
//		String currentPage = Struts2Utils.getParameter("currentPage");
//		int pageNo = 1;
//		if(CommonUtil.isInteger(currentPage)){
//			pageNo = Integer.valueOf(currentPage)+1;
//		}
		Integer lastAmount = null;
		if(CommonUtil1.isInteger(lastAmoutStr)){
			lastAmount = Integer.valueOf(lastAmoutStr);
		}
		//根据输入内容确定是否需要缓存
		boolean useCache = true;
		if("true".equals(Struts2Utils.getParameter("isInput"))){
			useCache = false;
		}
		SpcDataManager spcDataManager = new SpcDataManager(qualityFeatureDao.getSession(),useCache);
		Map<String,Object> resultMap = spcDataManager.querySpcDataValues(qualityFeature, startDateStr, endDateStr,layerParams,1,null, lastAmount);
		List<double[]> values = (List<double[]>)resultMap.get("values");
		JSONArray posDatas = (JSONArray)resultMap.get("posDatas");
		JSONArray datas = new JSONArray();
		for(int i=0;i<posDatas.size();i++){
			JSONObject posData = posDatas.getJSONObject(i);
			//缓存坐标数据
			JSONObject d = new JSONObject();
			d.put("id",posData.getString("id"));
			d.put("date",posData.getString("inspectionDate"));
			datas.add(d);
		}
		
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
		originalData.setSampleQuantity(effectiveCapacity);
		if("1".equals(showParamMap.get("showMaxAndMin"))){
			showParamMap.put("dataList",datalist);
		}
		//调用算法计算均值标准差放入getjLResult
		jLcalculator.calculate(originalData);
		BufferedImage image = null ;
		//封装画控制图参数
		JLControlChartParam jlp=null;
		Abnormity[] abnormityArray = AbnormalInfoManager.judgeCollectDataOfAbnormity(qualityFeature,false,0);
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
				JlanalyseDrawManager.picImageToDir(img_url, request, image);
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

package com.ambition.spc.abnormal.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ambition.spc.dataacquisition.dao.SpcSgSampleDao;
import com.ambition.spc.entity.Abnormity;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.jlanalyse.entity.JLOriginalData;
import com.ambition.spc.jlanalyse.entity.JLSampleData;
import com.ambition.spc.util.Calculator;

/**
 * BaseUtil.java
 * 
 * @authorBy YUKE
 * 
 */
public class BaseUtil {

	public static int JINGDU = 6;

	/**
	 * @param picType
	 * @return
	 */
	public static String chineseName(String type) {
		String NameCn = "";
		if ("xbar_S".equals(type)) {
			NameCn = "均值标准差控制图";
		} else if ("xbar_R".equals(type)) {
			NameCn = "均值极差控制图";
		} else if ("M_R".equals(type)) {
			NameCn = "中位极差控制图";
		} else if ("x_RS".equals(type)) {
			NameCn = "单值移动极差控制图";
		} else if ("nesting".equals(type)) {
			NameCn = "嵌套控制图";
		} else if ("regress".equals(type)) {
			NameCn = "回归控制图";
		} else if ("nest_reg".equals(type)) {
			NameCn = "嵌套回归控制图";
		} else if ("range".equals(type)) {
			NameCn = "直方图";
		} else if ("spread".equals(type)) {
			NameCn = "散布图";
		} else if ("run".equals(type)) {
			NameCn = "运行图";
		} else if ("P".equals(type)) {
			NameCn = "不合格品率控制图";
		} else if ("PN".equals(type)) {
			NameCn = "不合格品数控制图";
		} else if ("U".equals(type)) {
			NameCn = "单位缺陷控制图";
		} else if ("C".equals(type)) {
			NameCn = "缺陷控制图";
		} else if ("PRun".equals(type)) {
			NameCn = "不合格品率趋势图";
		} else if ("pola".equals(type)) {
			NameCn = "柏拉图";
		} else if ("rainbow".equals(type)) {
			NameCn = "彩虹图";
		} else if ("Z_MR".equalsIgnoreCase(type)) {
			NameCn = "通用单值移动极差控制图";
		}
		return NameCn;
	}

	/**
	 * md5加密
	 * @param s
	 * @return
	 */
	public static String MD5(String s) {
		char hexDigits[] = { '@', '0', '3', '4', '@', '5', '6', '#', '8', 'z',
				'h', 'n', 'g', 'l', '%', 'i', 'a', '$', 'e', '&', '*' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 6 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param curD
	 * @return
	 */
	public static Date setDate(Date curD) {
		if (curD != null) {
			return new java.sql.Date(curD.getTime());
		}
		return curD;
	}

	/**
	 * 获取计量型控制图的异常信息列表
	 * @param size
	 * @param collectList
	 * @param abnormityArray
	 * @param chartType
	 * @param controlState
	 * @param up
	 * @param low
	 * @param mid
	 * @param ucl
	 * @param lcl
	 * @param cl
	 * @param ucl1
	 * @param lcl1
	 * @param cl1
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList getNormalAbnor(List<double[]> values,
			Abnormity[] abnormityArray, int chartType, Integer controlState,
			Double up, Double low, Double mid, double ucl, double lcl,
			double cl, double ucl1, double lcl1, double cl1,
			QualityFeature param, boolean testflag) {
		ArrayList result = null;
		JLOriginalData originalData = (JLOriginalData) pakNormal(values, chartType, controlState, up, low, mid, ucl, lcl, cl,
				ucl1, lcl1, cl1, param, testflag);
		if (originalData != null) {
			result = JLJudgeAbnormalAPI.JudgeAbnormalRealTime(originalData,
					abnormityArray);
		}
		return result;
	}
	
	private static SpcSgSampleDao sampleDao;
	public static void setSpcSgSampleDao(SpcSgSampleDao spcSgSampleDao){
		sampleDao = spcSgSampleDao;
	}
	/**
	 * @param size
	 * @param collectList
	 * @param abnormityArray
	 * @param chartType
	 * @param controlState
	 * @param up
	 * @param low
	 * @param mid
	 * @param ucl
	 * @param lcl
	 * @param cl
	 * @param ucl1
	 * @param lcl1
	 * @param cl1
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object pakNormal(List<double[]> list, int chartType,
			Integer controlState, Double up, Double low, Double mid,
			double ucl, double lcl, double cl, double ucl1, double lcl1,
			double cl1, QualityFeature param, boolean testflag) {
		if (list == null || list.size() <= 0) {
			return null;
		}
		JLOriginalData originalData = new JLOriginalData();
		originalData.setChartType(chartType);
	    originalData.setControlState(controlState);
	    originalData.setSampleQuantity(param.getEffectiveCapacity());
	    originalData.setTu(up);
	    originalData.setTl(low);
	    originalData.setM(mid);
	    originalData.setXUCL(ucl);
	    originalData.setXLCL(lcl);
	    originalData.setXCL(cl);
	    if (chartType == 2) {
	    	originalData.setSUCL(ucl1);
	    	originalData.setSLCL(lcl1);
	    	originalData.setSCL(cl1);
	    } else {
	    	originalData.setRUCL(ucl1);
	    	originalData.setRLCL(lcl1);
	    	originalData.setRCL(cl1);
	    }
	    //设置控制图title
	    setConchartTitle(originalData,chartType);
	    //封装每组的样本数据
  		ArrayList data = new ArrayList();
  		for(int i=0;i<list.size();i++){
  			double[] values = list.get(i);
  			int effectiveCapacity = param.getEffectiveCapacity();
  			double[] a = new double[effectiveCapacity];
  			if(values.length>=effectiveCapacity){
	  			for(int j=0;j<effectiveCapacity;j++){
	  				a[j]=values[j];
  				}
  				data.add(a);
  			}
  		}
  		//封装每组样本数据的最大值、最小值、平均值等
		ArrayList<JLSampleData> dataList = new ArrayList<JLSampleData>();
		for(int i=0;i<data.size();i++){
			JLSampleData jl=new JLSampleData();
			double[] a=(double[])data.get(i);
			jl.setData(a);
			jl.setAverage(Calculator.average(a));
			jl.setMax(Calculator.max(a));
			jl.setMin(Calculator.min(a));
			jl.setS(Calculator.calculateS(a));
			jl.setMedian(Calculator.calculateMedian(a));
			//TODO:状态未确定
//			SpcSubGroup group = collectList.get(i);
			boolean state = false;
//			if (!testflag) {
//				state = getDataState(group.getJudgeState());
//			}
			state = true;
			jl.setJudgeState(state);
//			jl.setNum(group.getSubGroupOrderNum());
//			jl.setSamplingTime(group.getCreatedTime().toString());
			dataList.add(jl);
		}
		if(dataList.size()!=0){
			originalData.setDataList(dataList);
		}
		return originalData;
	}
	
	private static boolean getDataState(int state){
		if(state!=0){
			return true;
  		}
  		return false;
	}
	
	private static void setConchartTitle(JLOriginalData originalData,int chartType) {
		switch (chartType) {
		case 1:
			originalData.setUpTitle("均值控制图");
			originalData.setLowTitle("极差控制图");
			break;
		case 2:
			originalData.setUpTitle("均值控制图");
			originalData.setLowTitle("标准偏差控制图");
			break;
		case 3:
			originalData.setUpTitle("中位数控制图");
			originalData.setLowTitle("极差控制图");
			break;
		case 4:
			originalData.setUpTitle("单值控制图");
			originalData.setLowTitle("移动极差控制图");
			break;
		}
	}
}

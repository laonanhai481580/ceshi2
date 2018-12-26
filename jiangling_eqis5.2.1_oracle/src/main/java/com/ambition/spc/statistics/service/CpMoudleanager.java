package com.ambition.spc.statistics.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.spc.dataacquisition.service.SpcDataManager;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.jlanalyse.entity.JLOriginalData;
import com.ambition.spc.jlanalyse.util.JLcalculator;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.spc.util.Calculator;
import com.ambition.util.common.CommonUtil1;

@Service
public class CpMoudleanager {
	@Autowired
	private CpkTrendDatasManager cpkTrendDatasManager;
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private SpcDataManager spcDataManager;
	/**
	  * 方法名: 计算控制图
	  * <p>功能说明：</p>
	  * @param startDateStr
	  * @param endDateStr
	  * @param qualityFeatureIds
	  * @return
	 */
	public  void calculatJl(JLcalculator jLcalculator,JLOriginalData originalData,String type,String featureId,String startDateStr,String endDateStr,String lastAmoutStr,JSONObject layerParams){
		//质量特性
		QualityFeature qualityFeature = qualityFeatureManager.getQualityFeatureFromCache(Long.valueOf(featureId),null);
		//spc数据
		Integer lastAmount = null;
		if(CommonUtil1.isInteger(lastAmoutStr)){
			lastAmount = Integer.valueOf(lastAmoutStr);
		}
		Map<String,Object> resultMap = spcDataManager.querySpcDataValues(qualityFeature,startDateStr,endDateStr, layerParams,1,null,lastAmount);
		@SuppressWarnings("unchecked")
		List<double[]> values = (List<double[]>)resultMap.get("values");
		
		//封装originalDate
		cpkTrendDatasManager.calculatJl(jLcalculator, 
				qualityFeature,values,
				qualityFeature.getControlLimits(),originalData,null);
	}
	
	public static void main(String[] args) {
		double values[] = new double[12];
		values[0] = 13d;
		values[1] = 14d;
		double val = Calculator.average(values);
		System.out.println(val);
	}
}
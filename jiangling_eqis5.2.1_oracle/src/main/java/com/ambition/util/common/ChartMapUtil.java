package com.ambition.util.common;

import java.util.HashMap;
import java.util.Map;

public class ChartMapUtil {

	/**
	 * PDI问题
	 */
	private static final Map<String,String> pdiProblemMap = new HashMap<String, String>();
	static{
		pdiProblemMap.put("pdiDate", "pdi_date");
		pdiProblemMap.put("id", "id");
		pdiProblemMap.put("dealerName", "dealer_name");
		pdiProblemMap.put("PDIResult", "PDIRESULT");
		pdiProblemMap.put("pdiType", "PDI_TYPE");
		pdiProblemMap.put("PDIUserName", "PDIUSER_NAME");
		pdiProblemMap.put("problemCategory", "problem_category");
		pdiProblemMap.put("dutyDept", "duty_dept");
		pdiProblemMap.put("materialCode", "material_code");
		pdiProblemMap.put("materialName", "material_name");
		pdiProblemMap.put("seriesName", "series_name");
		pdiProblemMap.put("dealerName", "dealer_name");
		pdiProblemMap.put("itemName", "item_name");
	}
	
	//封装方法
	public static Map<String, String> getPDIProblemMap() {
		return pdiProblemMap;
	}
}
